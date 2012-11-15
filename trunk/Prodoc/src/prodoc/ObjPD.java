/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU GPL v3 http://www.gnu.org/licenses/gpl.html
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

import java.util.Date;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
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

//-------------------------------------------------------------------------
/**
 *
 */
public ObjPD()
{
}
//-------------------------------------------------------------------------
/**
 *
 * @param pDrv
 */
public ObjPD(DriverGeneric pDrv)
{
setDrv(pDrv);
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
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
 *
 * @throws PDException
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
/**
 *
 * @throws PDException
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
 *
 * @return
 */
public String getTabName()
{
return(null);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
public Record getRecord() throws PDException
{
return(null);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
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
 *
 * @param Rec
 * @throws PDException
 */
public void assignValues(Record Rec) throws PDException
{

}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
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
 *
 * @throws PDException
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
 * @throws PDException
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
    Cursor Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
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
 * @return the getDrv()
 * @throws PDException
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
 *
 * @throws PDException
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
return(CommonStruct);
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
    R.addAttr( new Attribute(fPDAUTOR, "Autor", "Nombre Usuario Autor", Attribute.tSTRING, true, null, 32, false, false, false ));
    R.addAttr( new Attribute(fPDDATE, "Fecha", "Fecha de inserción en en gestor", Attribute.tTIMESTAMP, true, null, 0, false, false, false));
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
 *
 * @return
 * @throws PDException
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
 *
 * @param Name
 * @return
 * @throws PDException
 */
public Cursor SearchLike(String Name) throws PDException
{
Query qLike=new Query(getTabName(), getRecordStruct(), getConditionsLike(Name), getDefaultOrder());
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
 *
 * @return
 * @throws PDException
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
 * @return the XML
 * @throws PDException  
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
     *
     * @return
     */
    public String StartXML()
{
return("<"+XML_OPDList+">\n");    
}
//-------------------------------------------------------------------------
    /**
     *
     * @return
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
     *
     * @param Name
     * @return
     * @throws PDExceptionFunc
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
//-------------------------------------------------------------------------
}
