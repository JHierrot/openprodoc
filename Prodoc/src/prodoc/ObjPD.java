/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU Affero GPL v3 http://www.gnu.org/licenses/agpl.html
 * 
 * you may not use this file except in compliance with the License.  
 * Unless agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Joaquin Hierro      2011
 * 
 */

package prodoc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.util.TablesNamesFinder;
import static prodoc.Attribute.DECIMALPATTERN;


/**
 * Manages the STORAGE of definition of authenticators, not the authentication function
 * @author jhierrot
 */
public abstract class ObjPD
{
/**
 *
 */
private DriverGeneric Drv=null;
/**
 *
 */
static public final String fPDDATE="PDDate";
/**
 *
 */
static public final String fPDAUTOR="PDAutor";
/**
 *
 */
private Date   PDDate;
/**
 *
 */
private String PDAutor;

/**
 *
 */
static private Record CommonStruct=null;

/**
 *
 */
static public final String XML_ListAttr="ListAttr";
/**
 *
 */
static public final String XML_OPDObject="OPDObject";
/**
 *
 */
static public final String XML_OPDList="OPDList";
/**
 *
 */
static public final String XML_GroupMembers="GroupMembers";
/**
 *
 */
static public final String XML_UserMembers="UserMembers";
/**
 *
 */
static public final String XML_Group="Group";
/**
 *
 */
static public final String XML_User="User";
/**
 *
 */
static public final String XML_Metadata="Metadata";
/**
 *
 */
static public final String XML_Field="Field";
/**
 *
 */
static public final String XML_Attr="Attr";

/**
 *
 */
static public final String AllowedChars="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
// Ctes for evaluating syntax of tasks of updating

/**
 * SYMBOL for delimiting constants
 */
static public final char SYN_SEP='#';
/**
 * SYMBOL for adding values
 */
static public final char SYN_ADD='+';
/**
 * SYMBOL for substract values
 */
static public final char SYN_DEL='-';
/**
 * SYMBOL for Reference to Parent metadata
 */
public static final char SYN_PARENT='@';
/**
 * SYMBOL for Reference to Thesaur Term Name
 */
public static final char SYN_THES='{';
//-------------------------------------------------------------------------
/**
 *
 */
public ObjPD()
{
}
//-------------------------------------------------------------------------
/**
 * Default constructor of an OpenProdoc object, assigning driver for database access
 * @param pDrv OpenProdoc Driver/session to assign
 */
public ObjPD(DriverGeneric pDrv)
{
setDrv(pDrv);
}
//-------------------------------------------------------------------------
/**
 * Generic method for inserting an element
 * @throws PDException in any error
 */
public void insert() throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.insert>:"+getTabName());
VerifyAllowedIns();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
AddLogFields();
getRecord().Verify();
getDrv().InsertRecord(getTabName(), getRecord());
InsertMulti();
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.insert <");
}
//-------------------------------------------------------------------------
/**
 * Generic method for deleting an element
 * @throws PDException in any error
 */
public void delete()  throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.delete>:"+getTabName());
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
DeleteMulti();
getDrv().DeleteRecord(getTabName(), getConditions());
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.delete <");
}
//------------------------------------------------------------------------
/**
 * Generic method for updating an element
 * @throws PDException in any error
 */
public void update()  throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.update>:"+getTabName());
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
AddLogFields();
getDrv().UpdateRecord(getTabName(), getRecord(), getConditions());
UpdateMulti();
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.update <");
}
//-------------------------------------------------------------------------
/**
 * Generic "abstract" method for obtaining the name of the table
 * @return the name of the DDB table that stores the current OPD object
 */
public String getTabName()
{
return(null);
}
//-------------------------------------------------------------------------
/**
 * Generic "abstract" method for returning a Record of loaded OPD Object
 * @return a record with attributes of the loaded OPD object
 * @throws PDException in any error
 */
public Record getRecord() throws PDException
{
return(null);
}
//-------------------------------------------------------------------------
/**
 * Returns the Structure/definition of current OPD Class (PDUsers, PDGroups, PDAcl,..) as a record 
 * @return a Record containing the definition/structure of current OPD class
 * @throws PDException in any error
 */
Record getRecordStruct() throws PDException
{
return(null);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
abstract protected Conditions getConditions() throws PDException;
//-------------------------------------------------------------------------
/**
 *
 * @param Name
 * @return
 * @throws PDException
 */
abstract protected Conditions getConditionsLike(String Name) throws PDException;
/**
 * Generic "abstract" method for assigning a Record Values to an OPD Object
 * @param Rec Record to be assigned
 * @throws PDException in any error
 */
public void assignValues(Record Rec) throws PDException
{

}
//-------------------------------------------------------------------------
/**
 * Creates the Database structure of OpenProdoc
 * @throws PDException in any error
 */
public void Install() throws PDException
{
getDrv().CreateTable(getTabName(), getRecordStruct());
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void InstallMulti()  throws PDException
{
}
//-------------------------------------------------------------------------
/**
 * Drops DDBB tables of current class
 * @throws PDException in any error
 */
public void unInstall() throws PDException
{
getDrv().DropTable(getTabName());
}
//-------------------------------------------------------------------------

/**
 *
 * @throws PDException
 */
protected void unInstallMulti() throws PDException
{
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void InsertMulti() throws PDException
{
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void UpdateMulti() throws PDException
{
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void DeleteMulti() throws PDException
{
}
//-------------------------------------------------------------------------
/**
 * Load the object of the actual type based on the Iden
 * @param Ident Identifier of the objetct to be loaded
 * @return A record with the attributes of the object
 * @throws PDException In any error
 */
public Record Load(String Ident)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.Load>:"+getTabName()+"-"+Ident);
AsignKey(Ident);
Record r=(Record)getObjCache().get(Ident);
if (r==null)
    {
    Query LoadAct=new Query(getTabName(), getRecordStruct(),getConditions());
    Cursor Cur=null;
    try {
    Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    } catch (Exception Ex)
        {
        if (Cur!=null)
           getDrv().CloseCursor(Cur); 
        PDException.GenPDException("Error_loading_in_cache", Ex.getLocalizedMessage());
        }
    getObjCache().put(Ident, r);
    }
if (r!=null) // can be null the result loaded (and assigned/cleaned to cache
    assignValues(r);
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.Load <");
return(r);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @throws PDException  
 */
abstract protected void AsignKey(String Ident) throws PDException;
//-------------------------------------------------------------------------
    /**
     *
     * @return
     */
    abstract protected String getKey();
//-------------------------------------------------------------------------
/**
 * @return the OPD "Driver" Session of the current object
 * @throws PDException In any error
*/
public DriverGeneric getDrv()  throws PDException
{
if (Drv==null)
    PDException.GenPDException("Driver_not_assigned", null);
return Drv;
}
//-------------------------------------------------------------------------
/**
* @param Drv the Drv to set
*/
public void setDrv(DriverGeneric Drv)
{
this.Drv = Drv;
}
//-------------------------------------------------------------------------
/**
 * Deletes the values assigned to the current object
 * @throws PDException in any error
 */
public void Clear() throws PDException
{
assignValues(getRecordStruct());
}
/**
 *
 * @throws PDException
 */
abstract protected void VerifyAllowedIns() throws PDException;
/**
 *
 * @throws PDException
 */
abstract protected void VerifyAllowedDel() throws PDException;
/**
 *
 * @throws PDException
 */
abstract protected void VerifyAllowedUpd() throws PDException;
//-------------------------------------------------------------------------
/**
* @return the PDDate
*/
public Date getPDDate()
{
return PDDate;
}
//-------------------------------------------------------------------------
/**
* @param PDDate the PDDate to set
*/
protected void setPDDate(Date PDDate)
{
this.PDDate = PDDate;
}
//-------------------------------------------------------------------------

/**
 * Returns the fixed structure
 * @return
 * @throws PDException
 */
static protected Record getRecordStructCommon() throws PDException
{
if (CommonStruct==null)
    {
    CommonStruct=CreateRecordStructCommon();
    }
return(CommonStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructCommon() throws PDException
{
if (CommonStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDAUTOR, "Author", "Name_of_Author", Attribute.tSTRING, true, null, 32, false, false, false ));
    R.addAttr( new Attribute(fPDDATE, "Date", "Date_of_update_in_repository", Attribute.tTIMESTAMP, true, null, 0, false, false, false));
    return(R);
    }
else
    return(CommonStruct);
}
//-------------------------------------------------------------------------
/**
* @return the PDAutor
*/
public String getPDAutor()
{
return PDAutor;
}
//-------------------------------------------------------------------------
/**
* @param PDAutor the PDAutor to set
*/
protected void setPDAutor(String PDAutor)
{
this.PDAutor = PDAutor;
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void AddLogFields() throws PDException
{
setPDDate(new Date());
setPDAutor(getDrv().getUser().getName());
}
//-------------------------------------------------------------------------
/**
 *
 * @param Rec
 * @throws PDException
 */
protected void assignCommonValues(Record Rec)  throws PDException
{
setPDDate((Date) Rec.getAttr(fPDDATE).getValue());
setPDAutor((String)Rec.getAttr(fPDAUTOR).getValue());
}
//-------------------------------------------------------------------------
/**
 *
 * @param Rec
 * @throws PDException
 */
protected void getCommonValues(Record Rec)  throws PDException
{
Rec.getAttr(fPDDATE).setValue(getPDDate());
Rec.getAttr(fPDAUTOR).setValue(getPDAutor());
}
//-------------------------------------------------------------------------
/**
 * Do a Query By Example (bases in values assigned to Atributes) anc returns a Cursor
 * @return a created Cursor
 * @throws PDException in any error
 */
public Cursor SearchQBE() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.SearchQBE>:"+getTabName());
Conditions CondQBE=new Conditions();
Query QBE=new Query(getTabName(), getRecordStruct(),CondQBE);
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.SearchQBE <");
return(getDrv().OpenCursor(QBE));
}
//-------------------------------------------------------------------------
/**
 * Search object of the current OPD classby its main identifier (Name, Code,..) using Like DDBB expression
 * @param Name Name to Search for
 * @return a created cursor
 * @throws PDException In any error
 */
public Cursor SearchLike(String Name) throws PDException
{
Query qLike=new Query(getTabName(), getRecordStruct(), getConditionsLike(Name), getDefaultOrder());
return(getDrv().OpenCursor(qLike));
}
//-------------------------------------------------------------------------
/**
 * Search object of the current OPD classby its descriptionusing Like DDBB expression
 * @param Name Description of the Object
 * @return a created cursor
 * @throws PDException In any error
 */
public Cursor SearchLikeDesc(String Name) throws PDException
{
Query qLike=new Query(getTabName(), getRecordStruct(), getConditionsLike(Name), getDescOrder());
return(getDrv().OpenCursor(qLike));
}
//-------------------------------------------------------------------------
/**
 *
 * @param Name
 * @return
 */
protected String VerifyWildCards(String Name)
{
if (!Name.contains("*"))
    Name+="*";
return(Name.replace('*','%')); // OJO: Revisar para Driver NO BBDD
}
/**
 *
 * @return
 */
abstract protected String getDefaultOrder();
//-------------------------------------------------------------------------
/**
 * Returns ALL the elements of the current OPD Class
 * @return A created Cursor
 * @throws PDException In any error
 */
public Cursor getAll() throws PDException
{
Query qLike=new Query(getTabName(), getRecordStruct(), null, getDefaultOrder());
return(getDrv().OpenCursor(qLike));
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
abstract protected ObjectsCache getObjCache();
//-------------------------------------------------------------------------
/**
 * Builds an XML of the object to be printed or exported
 * @return the XML created
 * @throws PDException In any error 
 */
public String toXML() throws PDException
{
StringBuilder XML=new StringBuilder("<"+XML_OPDObject+" type=\""+getTabName()+"\">\n");
XML.append("<"+XML_ListAttr+">\n");
XML.append(getRecord().toXML());
XML.append(toXML2());
XML.append("</"+XML_OPDObject+">\n");
return XML.toString();
}
//-------------------------------------------------------------------------
/**
 * Returns the Start String of OpenProdoc XML export format
 * @return The Start String  of OpenProdoc XML export format
 */
public String StartXML()
{
return("<"+XML_OPDList+">\n");    
}
//-------------------------------------------------------------------------
/**
 * Returns the END String  of OpenProdoc XML export format
 * @return The END String  of OpenProdoc XML export format
 */
public String EndXML()
{
return("</"+XML_OPDList+">\n");    
}
//-------------------------------------------------------------------------
/**
 * Add aditional information, oriented a "extended" object with childrn nodes
 * @return The aditional XML
 * @throws PDException
 */
protected String toXML2() throws PDException
{
return("</"+XML_ListAttr+">\n");    
}
//-------------------------------------------------------------------------
/**
 * Process the object definition inserting a new object
 * @param OPDObject XML node containing theobject data
 * @throws PDException if object name/index duplicated or in any error
 */
public void ProcesXMLNode(Node OPDObject) throws PDException
{
NodeList childNodes = OPDObject.getChildNodes();
for (int i = 0; i < childNodes.getLength(); i++)
    {
    Node item = childNodes.item(i);
    if (item.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(item, getRecord());
        assignValues(r);
        }
    }
insert();
}    
//-------------------------------------------------------------------------
/**
 * Verifis if the name is correct to be used as object or attribute name
 * @param Name name to be verified
 * @return the received name
 * @throws PDExceptionFunc in the name is not correct
 */
public static String CheckName(String Name) throws PDExceptionFunc
{
// pendiente de resolver compatibilidad con clear()
if (Name==null)    
    return(Name);
Name=Name.trim();
if (Name.length()==0)   
    PDExceptionFunc.GenPDException("Empty_Name_not_allowed",Name);
if (Name.length()>32)   
    PDExceptionFunc.GenPDException("Name_longer_than_allowed",Name);
for (int i=0; i<Name.length(); i++)
    {
    if (AllowedChars.indexOf(Name.charAt(i))==-1)
       PDExceptionFunc.GenPDException("Character_not_included_in_the_allowed_set",AllowedChars);
    }
return(Name);
}
//---------------------------------------------------------------------
/**
 * Updates fields. Syntax:
 * Field1=Field2;
 * Field1=Field1+Field2;
 * @param param Expresión to use
 * @param r Record of the document or folder to update
 * @param rParent Record of the Parent Folder
 * @param ListThes List of names/values of the Thesaur elements
 * @return Updated record
 * @throws PDException in any error
 */
protected Record Update(String param, Record r, Record rParent, HashMap<String, String> ListThes) throws PDException
{
if (param==null || param.length()==0)    
    return(r);
String DestAttr=param.substring(0, param.indexOf('='));
if (DestAttr.equalsIgnoreCase(PDDocs.fDOCTYPE) || DestAttr.equalsIgnoreCase(PDDocs.fPDID) 
    || DestAttr.equalsIgnoreCase(PDDocs.fREPOSIT) || DestAttr.equalsIgnoreCase(PDDocs.fVERSION)
    || DestAttr.equalsIgnoreCase(PDDocs.fLOCKEDBY) || DestAttr.equalsIgnoreCase(PDDocs.fPDAUTOR)
    || DestAttr.equalsIgnoreCase(PDDocs.fPDDATE) || DestAttr.equalsIgnoreCase(PDDocs.fSTATUS))
    PDExceptionFunc.GenPDException("Attribute_not_allowed_to_change", DestAttr);
Attribute Att=r.getAttr(DestAttr);
String NewExp=param.substring(param.indexOf('=')+1);
ArrayList<String> ListElem=new ArrayList<String>(NewExp.length()/4);
String Current="";
boolean Constant=false;
for (int i = 0; i < NewExp.length(); i++)
    {
    char c=NewExp.charAt(i);
    if (c!=SYN_SEP && c!=SYN_ADD && c!=SYN_DEL)  
        Current+=c;
    else if (c==SYN_ADD || c==SYN_DEL)
        {
        if (Current.length()!=0)    
            {
            ListElem.add(Current);
            Current="";
            }
        ListElem.add(""+c);
        }
    else if (c==SYN_SEP)
        {
        Current+=c;
        if (Constant)
            {
            ListElem.add(Current);
            Current="";
            }
        Constant=!Constant;
        }
    }
if (Current.length()!=0)
    {
    ListElem.add(Current);
    Current="";
    }
String TotalVal="";
String NewVal;
Attribute Attr1;
for (int i = 0; i < ListElem.size(); i++)
    {
    if (i % 2 == 0) // Field 
        {
        String Elem = ListElem.get(i);
        if (Elem.charAt(0)==SYN_SEP)
            NewVal=Elem.substring(1, Elem.length()-1);
        else if (Elem.charAt(0)==SYN_PARENT)
            {
            Attr1=rParent.getAttr(Elem.substring(1));
            NewVal=Attr1.Export();
            }       
         else if (Elem.charAt(0)==SYN_THES)
            {
            NewVal=ListThes.get(Elem.substring(1));
            }       
        else 
            {
            Attr1=r.getAttr(Elem);
            NewVal=Attr1.Export();
            }       
        if (Current.equals(""))
            {
            TotalVal=NewVal;
            }
        else if (Current.charAt(0)==SYN_ADD)
            {
            if (Att.getType()==Attribute.tDATE)
               TotalVal=AddDate(TotalVal, NewVal);
            else if (Att.getType()==Attribute.tSTRING)            
               TotalVal+=NewVal;
            }
        }
    else
        {
        Current=ListElem.get(i);
        }
    }
r.getAttr(DestAttr).Import(TotalVal);
return(r);
}
//---------------------------------------------------------------------
/**
 * Adds dates in the export/import format. Dates can be "partial" that is date with some 00 values.
 * @param TotalVal Date/partial date to be added YYYY-MM-DD
 * @param NewVal Date/partial date to be added   YYYY-MM-DD
 * @return Date/partial date to be added YYYY-MM-DD
 */
private String AddDate(String TotalVal, String NewVal)
{
int Year1=Integer.parseInt(TotalVal.substring(0,4));
int Month1=Integer.parseInt(TotalVal.substring(5,7));
int Day1=Integer.parseInt((TotalVal+" ").substring(8,10));
Year1+=Integer.parseInt(NewVal.substring(0,4));
Month1+=Integer.parseInt(NewVal.substring(5,7));
Day1+=Integer.parseInt((NewVal+" ").substring(8,10));
return(String.format("%04d", Year1)+"-"+String.format("%02d", Month1)+"-"+String.format("%02d", Day1));
}
//---------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getDescOrder()
{
return(PDObjDefs.fDESCRIPTION);
}
//---------------------------------------------------------------------
/**
 * Run a query and return the result as aVector or records
 * @param SQL Query to run
 * @return Vector of results as Records
 * @throws PDException in any error
 */
public Vector<Record> SearchSelectV(String SQL) throws PDException
{
Vector<Record> ListRes=new Vector<Record>(); 
Cursor CurSelect = SearchSelect(SQL);
try {
Record NextRec=getDrv().NextRec(CurSelect);
while (NextRec!=null)
    {
    ListRes.add(NextRec);
    NextRec=getDrv().NextRec(CurSelect);
    }
} 
finally 
    {
    getDrv().CloseCursor(CurSelect);
    }
return(ListRes);
}

/**
 * Search in ANY object using SQL Syntax subset, similar to CMIS SQL
 * @param SQL complete query
 * @return and opened @Cursor
 * @throws PDException In any Error
 */
public Cursor SearchSelect(String SQL) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.SearchSelect>:"+SQL);
Query QBE=null;
try {
Select ParsedSQL = (Select) CCJSqlParserUtil.parse(SQL);
//-- Calculate Table Names ------------
TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
List<String> TableListSQL = tablesNamesFinder.getTableList(ParsedSQL);
Vector <String> OPDTabs=CalculateTabs(TableListSQL);
//-- Calculate Fields -------------
List<SelectItem> selectItems = ((PlainSelect)ParsedSQL.getSelectBody()).getSelectItems();
Vector<String> Fields=new Vector<String>();
if (!( selectItems.get(0) instanceof AllColumns))
    for (int i = 0; i < selectItems.size(); i++)
        Fields.add(((SelectExpressionItem)selectItems.get(i)).getExpression().toString());      
Record Rec=CalculateRec(Fields, OPDTabs);
//-- Calculate Conds in Select ------------
Expression When = ((PlainSelect)ParsedSQL.getSelectBody()).getWhere();
Conditions CondSel=EvalExpr(When);

//-- Check Additional-Security Conditions ----
Conditions FinalConds;
Conditions AddedConds=NeededMoreConds(TableListSQL, OPDTabs);
if (AddedConds==null)
    FinalConds=CondSel;
else
    {
    FinalConds=new Conditions();
    FinalConds.addCondition(AddedConds);
    FinalConds.addCondition(CondSel);
    }
//-- Calculate Order ------------
Vector <String> Order=new Vector<String>();
Vector <Boolean> OrderAsc=new Vector<Boolean>();
List<OrderByElement> orderByElements = ((PlainSelect)ParsedSQL.getSelectBody()).getOrderByElements(); 
if (orderByElements!=null)
    for (int i = 0; i < orderByElements.size(); i++)
        {
        Order.add(orderByElements.get(i).getExpression().toString());
        OrderAsc.add(orderByElements.get(i).isAsc());
        }
//-- Create Query --------------
QBE=new Query(OPDTabs, Rec, FinalConds, Order, OrderAsc);
if (PDLog.isDebug())
    PDLog.Debug("ObjPD.SearchSelect <");
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    PDException.GenPDException("Processing_SQL", Ex.getLocalizedMessage());
    }
return(getDrv().OpenCursor(QBE));
}
//-------------------------------------------------------------------------
/**
 *
 * @param tableList
 * @return
 * @throws PDException in any error
 */
protected Vector<String> CalculateTabs(List<String> tableList) throws PDException
{
Vector <String> Tabs=new Vector<String>();
Tabs.add(getTabName());
return(Tabs);
}
//-------------------------------------------------------------------------

    /**
     *
     * @param Fields
     * @param Tabs
     * @return
     * @throws PDException in any error
     */
protected Record CalculateRec(Vector<String> Fields, Vector <String> Tabs) throws PDException
{
if (Fields.isEmpty())    
    return getRecordStruct();
Record R=getRecordStruct();
Record R2=new Record();
Attribute attr;
for (int i = 0; i < Fields.size(); i++)
    {
    attr = R.getAttr(Fields.elementAt(i));
    if (attr!=null)
        R2.addAttr(attr);
    }
if (R2.NumAttr()==0)
    PDException.GenPDException("Empty_or_Erroneus_list_of_Fields", null);
return(R2);
}
//-------------------------------------------------------------------------
/**
 *
     * @param tableListSQL
     * @param OPDTabs
 * @return
     * @throws prodoc.PDException
 */
protected Conditions NeededMoreConds(List<String> tableListSQL, Vector <String> OPDTabs) throws PDException
{
return(null);
}
//-------------------------------------------------------------------------
static private HashMap<String, Integer>CompConv=new HashMap<String, Integer>(); 

static private final int EXPR_BASIC=0;
static private final int EXPR_AND=1;
static private final int EXPR_OR=2;
static private final int EXPR_PAR=3;
static private final int EXPR_FUNCT=4;
static private final int EXPR_IN=5;
static private final int EXPR_NOT=6;

//---------------------------------------------------------------------------
static private synchronized HashMap<String, Integer>getCompConv()
{
if (CompConv.isEmpty())   
    {
    CompConv.put("=", Condition.cEQUAL);
    CompConv.put(">=", Condition.cGET );
    CompConv.put("<=", Condition.cLET);
    CompConv.put(">", Condition.cGT);
    CompConv.put("<", Condition.cLT);
    CompConv.put("<>", Condition.cNE);
    }
return(CompConv);
}
//---------------------------------------------------------------------------
private boolean isField(String Text)
{
char c=Text.toLowerCase().charAt(0);
return(c>='a' && c<='z');
}
final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/**
 * Default formater, used to store in DDBB, export, etc
 */
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");

//---------------------------------------------------------------------------
private Object CalcVal(String Text)
{
//System.out.println("CalcVal=("+Text+")");        
if (Text==null || Text.length()==0)    
    return(Text);
if (Text.charAt(0)=='\'')
    {
    Text=Text.substring(1, Text.length()-1);    
    try {
        return(formatterTS.parse(Text));
    } catch (ParseException ex)
        { // No Timestamp
        }
    try {
        return(formatterDate.parse(Text));
    } catch (ParseException ex)
        { // No Date
        }
    return Text;
    }
if (Text.contains(".") || Text.contains("."))
    return(String2BD(Text));
else
    return(Integer.valueOf(Text));  
}
//---------------------------------------------------------------------------
private int CalcTypeVal(String Text)
{
//System.out.println("CalcTypeVal=("+Text+")");        
if (Text==null || Text.length()==0)    
    return(Attribute.tSTRING);
if (Text.charAt(0)=='\'')
    {
    Text=Text.substring(1, Text.length()-1);    
    try {
        formatterTS.parse(Text);
        return(Attribute.tTIMESTAMP);
    } catch (ParseException ex)
        { // No Timestamp
        }
    try {
        formatterDate.parse(Text);
        return(Attribute.tDATE);
    } catch (ParseException ex)
        { // No Date
        }
    return (Attribute.tSTRING);
    }
if (Text.contains(".") || Text.contains("."))
    return((Attribute.tFLOAT));
else
    return((Attribute.tINTEGER));  
}
//---------------------------------------------------------------------------
/**
 * Evaluates a String stored in Database in OPD format and returns the BigDecimal that represents
 * @param SBD Sttring representing a BigDecimal
 * @return BigDecimal
 */
public BigDecimal String2BD(String SBD)
{
DecimalFormat DF=new DecimalFormat(DECIMALPATTERN);
return(new BigDecimal(DF.format(new BigDecimal(SBD.replace(',','.').replace("_", ""))).replace(',','.').replace("_", "")));
}
//---------------------------------------------------------------------------
private int EvalExprType(Expression where)
{
if (where instanceof AndExpression)
    return (EXPR_AND);
else if (where instanceof OrExpression)
    return (EXPR_OR);
else if (where instanceof BinaryExpression)
    return(EXPR_BASIC);
else if (where instanceof Function)
    return(EXPR_FUNCT);
else if (where instanceof Parenthesis)
    return (EXPR_PAR);
else if (where instanceof InExpression) 
    return (EXPR_IN);
else if (where instanceof NotExpression) 
    return (EXPR_NOT);
return(-1);
}
//------------------------------------------------------------------------------
private Conditions EvalExpr(Expression ParentExpr ) throws PDException 
{
Conditions New = new Conditions();    
int ExprType= EvalExprType(ParentExpr);
//System.out.println("ParentExpr=["+ParentExpr+"]  Type="+ExprType);    
switch (ExprType)
    {
    case EXPR_BASIC:
        ComparisonOperator CO = (ComparisonOperator) ParentExpr;
        String Left=CO.getLeftExpression().toString();
        String Comp=CO.getStringExpression();
        String Right=CO.getRightExpression().toString();
        if (isField(Left) && isField(Right))
            New.addCondition(new Condition(Left,  Right));
        else  
            {
            String FieldName;
            Object Value;
            int TypeVal;
            if (isField(Left))
                {
                FieldName=Left;
                Value=CalcVal(Right);
                TypeVal=CalcTypeVal(Right);
                }
            else
                {
                FieldName=Right;
                Value=CalcVal(Left);
                TypeVal=CalcTypeVal(Left);
                }
//            System.out.println("Value="+Value+"  class="+Value.getClass().getName());
            New.addCondition(new Condition(FieldName,  getCompConv().get(Comp), Value, TypeVal));
            }
        break;    
    case EXPR_NOT:
        Conditions Cs=EvalExpr(((NotExpression) ParentExpr).getExpression());
        Cs.setInvert(true);
        New.addCondition(Cs);
        break;
    case EXPR_AND:
        New.addCondition(EvalExpr(((AndExpression) ParentExpr).getLeftExpression() ));
        New.addCondition(EvalExpr(((AndExpression) ParentExpr).getRightExpression() ));
        break;
    case EXPR_OR:
        New.addCondition(EvalExpr(((OrExpression) ParentExpr).getLeftExpression() ));
        New.addCondition(EvalExpr(((OrExpression) ParentExpr).getRightExpression() ));
        New.setOperatorAnd(false);
        break;
    case EXPR_PAR:
        New.addCondition(EvalExpr(((Parenthesis) ParentExpr).getExpression() ));
        break;
    case EXPR_IN:
        String FieldNameIn=((InExpression)ParentExpr).getLeftExpression().toString();
        HashSet<String> ListTerms = new HashSet<String>();
        List<Expression> LT =((ExpressionList)((InExpression)ParentExpr).getLeftItemsList()).getExpressions();
        for (Iterator<Expression> iterator = LT.iterator(); iterator.hasNext();)
            {
            StringValue NextTerm = (StringValue)iterator.next();
            ListTerms.add(NextTerm.getValue());
            }
        New.addCondition(new Condition(FieldNameIn,ListTerms));
        break;
    case EXPR_FUNCT:
        String Arg=((Function)ParentExpr).getParameters().getExpressions().get(0).toString();
        switch (((Function)ParentExpr).getName())
            {
            case Condition.CONTAINS:
                New.addCondition(Condition.genContainsCond(PDDocs.getTableName(),Arg, getDrv())); 
                break;
            case Condition.INTREE:
                New.addCondition(Condition.genInTreeCond( Arg, getDrv())); 
                break;
            case Condition.INFOLDER:
                New.addCondition(Condition.genInFolder(Arg, getDrv()));
                break;
                
            }
        break;    
        
    }
return(New);
}
//------------------------------------------------------------------------------
}
