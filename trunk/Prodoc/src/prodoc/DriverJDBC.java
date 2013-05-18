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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;



/**
 *
 * @author jhierrot
 */
public class DriverJDBC extends DriverGeneric
{
/**
 *
 */
private Connection con;
/**
 *
 */
private Statement stmt;
/**
 *
 */
//static final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyyMMddHHmmss");
/**
 *
 */
//static final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyyMMdd");
/**
 *
 * @param pURL
 * @param pPARAM
 * @param pUser
 * @param pPassword
 * @throws PDException
 */
public DriverJDBC(String pURL, String pPARAM, String pUser, String pPassword) throws PDException
{
super(pURL, pPARAM, pUser, pPassword);
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.DriverJDBC>:"+pURL+"/"+pUser+"/"+pPARAM);
try {
    Class.forName(getPARAM());
} catch (ClassNotFoundException ex)
    {
    PDException.GenPDException("Driver_JDBC_not_found",ex.getLocalizedMessage());
    }
try {
    con = DriverManager.getConnection(getURL(), getDBUser(), getDBPassword());
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_connecting_trough_JDBC", ex.getLocalizedMessage());
    }
try {
    stmt = con.createStatement();
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_creating_JDBC_Sentence",ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.DriverJDBC<");
}
//--------------------------------------------------------------------------
/**
 * 
 * @throws PDException
 */
public void delete() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.delete>:"+getURL());
try {
stmt.close();
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_closing_JDBC_Sentence",ex.getLocalizedMessage());
    }
try {
con.close();
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_closing_JDBC_connection",ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.delete<:"+getURL());
}
//--------------------------------------------------------------------------
/**
 * Verify if the conection with the repository is ok
 * @return true if the connection is valid
 */
public boolean isConnected()
{
try {
return con.isValid(1);
} catch (SQLException ex)
  {
   return(false);
   }
}
//--------------------------------------------------------------------------

protected void CreateTable(String TableName, Record Fields) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverJDBC.CreateTable>:"+TableName+"/"+Fields);
String SQL;
String ClavePrin="";
String UniqueKey="";
Fields.initList();
SQL="CREATE TABLE "+TableName+"( ";
for (int i=0; i<Fields.NumAttr(); i++)
    {
    Attribute A=Fields.nextAttr();
    SQL+=A.getName();
    if (A.getType()==Attribute.tINTEGER)
        SQL+=" INTEGER ";
    else if (A.getType()==Attribute.tBOOLEAN)
        SQL+=" SMALLINT ";
    else if (A.getType()==Attribute.tDATE)
        SQL+=" CHAR(8) ";//        SQL+=" DATE ";
    else if (A.getType()==Attribute.tTIMESTAMP)
        SQL+=" CHAR(14) "; // SQL+=" TIMESTAMP ";
    else if (A.getType()==Attribute.tTHES)
        SQL+=" VARCHAR(32) ";
    else 
        SQL+=" VARCHAR("+ A.getLongStr()+") ";
    if (A.isRequired())
        SQL+=" NOT NULL ";
    if (A.isPrimKey())
        {if (ClavePrin.length()>0)
            ClavePrin+=", ";
        ClavePrin+=A.getName();}
    if (A.isUnique())
        {if (UniqueKey.length()>0)
            UniqueKey+=", ";
        UniqueKey+=A.getName();}
    SQL+=", ";  // ya que luego siempre está clave principal
    }
if (UniqueKey.length()!=0)
    SQL+=" CONSTRAINT "+TableName+"_uniq UNIQUE("+UniqueKey+"), ";
if (ClavePrin.length()!=0)
    SQL+=" PRIMARY KEY("+ClavePrin+") ";
else // debemos quitar la última ,
    SQL=SQL.substring(0, SQL.length()-2);
SQL+="  ) ";
ExecuteSql(SQL);
Fields.initList();
for (int i=0; i<Fields.NumAttr(); i++)
    {
    Attribute A=Fields.nextAttr();
    if (A.getType()==Attribute.tTHES)
        AddIntegrity(TableName, A.getName(), PDThesaur.getTableName(), PDThesaur.fPDID);
    }
if (PDLog.isInfo())
    PDLog.Info("DriverJDBC.CreateTable<:"+TableName);
}
//--------------------------------------------------------------------------
protected void DropTable(String TableName) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverJDBC.DropTable>:"+TableName);
String SQL="";
SQL="DROP TABLE "+TableName;
ExecuteSql(SQL);
if (PDLog.isInfo())
    PDLog.Info("DriverJDBC.DropTable<:"+TableName);
}
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param Fields New fields to add
 * @throws PDException
 */
protected void AlterTable(String TableName, Record Fields) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

//--------------------------------------------------------------------------

protected void InsertRecord(String TableName, Record Fields) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.InsertRecord>:"+TableName+"="+Fields);
String SQL="Insert into "+TableName+"(";
String Attrs="";
String Vals="";
int NumAttr=Fields.NumAttr();
Attribute At;
Fields.initList();
for (int i = 0; i < NumAttr; i++)
    {
    At=Fields.nextAttr();
    if (At.getValue()!=null)
        {
        if (i>0 && Attrs.length()>0)
            {
            Attrs+=",";
            Vals+=",";
            }
        Attrs+=At.getName();
        if (At.getType()==Attribute.tSTRING)
            Vals+=toString((String)At.getValue());
        else if (At.getType()==Attribute.tTHES)
            {
            if (At.getValue()==null || ((String)At.getValue()).length()==0)
                Vals+="Null";
            else
                Vals+=toString((String)At.getValue());
            }
        else if (At.getType()==Attribute.tDATE)
            Vals+=toDate((Date)At.getValue());
        else if (At.getType()==Attribute.tTIMESTAMP)
            Vals+=toTimeStamp((Date)At.getValue());
        else if (At.getType()==Attribute.tBOOLEAN)
            Vals+=toBooleanString((Boolean)At.getValue());
        else
            Vals+=At.getValue();
        }
    }
SQL+=Attrs+") values ("+Vals+")";
this.ExecuteSql(SQL);
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.InsertRecord<");
}
//--------------------------------------------------------------------------

protected void DeleteRecord(String TableName, Conditions DelConds) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.DeleteRecord>:"+TableName);
String SQL="delete from "+TableName+" where ";
SQL+=EvalConditions(DelConds, null);
this.ExecuteSql(SQL);
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.DeleteRecord<");   
}
//--------------------------------------------------------------------------

protected void UpdateRecord(String TableName, Record NewFields, Conditions UpConds) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.UpdateRecord>:"+TableName+"="+NewFields);
String SQL="update "+TableName+" set ";
String Attrs="";
String Vals="";
int NumAttr=NewFields.NumAttr();
Attribute At;
NewFields.initList();
boolean Second=false;
for (int i = 0; i < NumAttr; i++)
    {
    At=NewFields.nextAttr();
    if ((At.getValue()!=null || (At.getValue()==null && At.getType()==Attribute.tTHES)) && !At.isPrimKey())
        {
        if (Second)
            SQL+=", ";
        SQL+=At.getName()+"=";
        Second=true;
        if (At.getType()==Attribute.tSTRING)
            SQL+=toString((String)At.getValue());
        else if (At.getType()==Attribute.tTHES)
            {
            if (At.getValue()==null || ((String)At.getValue()).length()==0)
                SQL+="Null";
            else
                SQL+=toString((String)At.getValue());
            }

        else if (At.getType()==Attribute.tDATE)
            SQL+=toDate((Date)At.getValue());
        else if (At.getType()==Attribute.tTIMESTAMP)
            SQL+=toTimeStamp((Date)At.getValue());
        else if (At.getType()==Attribute.tBOOLEAN)
            SQL+=toBooleanString((Boolean)At.getValue());
        else
            SQL+=At.getValue();
        }
    }
SQL+=" where "+EvalConditions(UpConds, null);
this.ExecuteSql(SQL);
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.UpdateRecord<:"+TableName+"="+NewFields);
}
//--------------------------------------------------------------------------
/**
 *
 * @param TableName1
 * @param Field1
 * @param TableName2
 * @param Field2
 * @throws PDException
 */
protected void AddIntegrity(String TableName1, String Field1, String TableName2, String Field2) throws PDException
{
String SQL="ALTER TABLE "+TableName1+" ADD FOREIGN KEY ("+Field1+") REFERENCES "+TableName2+"("+Field2+")";
this.ExecuteSql(SQL);
}
/**
 *
 * @param TableName1
 * @param Field11 
 * @param Field12 
 * @param TableName2
 * @param Field21 
 * @param Field22 
 * @throws PDException
 */
protected void AddIntegrity(String TableName1, String Field11, String Field12, String TableName2, String Field21, String Field22) throws PDException
{
String SQL="ALTER TABLE "+TableName1+" ADD FOREIGN KEY ("+Field11+","+Field12+") REFERENCES "+TableName2+"("+Field21+","+Field22+")";
this.ExecuteSql(SQL);
}

/**
 *
 * @return
 */
private boolean ExecuteSql(String SQL) throws PDException
{
if (SQL == null || SQL.length() == 0)
    {
    PDException.GenPDException("SQL_query_empty", null);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.ExecuteSql>:"+SQL);
try {
stmt.execute(SQL);
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_executing_SQL",ex.getLocalizedMessage());
    }
return(true);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @return
 */
private ResultSet RsExecuteSql(String SQL) throws PDException
{
if (SQL == null || SQL.length() == 0)
    {
    PDException.GenPDException("SQL_query_empty", null);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.RsExecuteSql:"+SQL);
try{
Statement stmtSQL = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
return stmtSQL.executeQuery(SQL);
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_executing_SQL",ex.getLocalizedMessage());
    return(null);
    }
}
//-----------------------------------------------------------------------------------
public void IniciarTrans() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.InitTrans");
try {
con.setAutoCommit(false);
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_starting_transaction",ex.getLocalizedMessage());
    }
setInTransaction(true);
}
//-----------------------------------------------------------------------------------
public void CerrarTrans() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.CommitTrans");
try {
con.commit();
con.setAutoCommit(true);
setInTransaction(false);
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_closing_transaction",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------------------------
public void AnularTrans() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.CancelTrans");
try {
con.rollback();
con.setAutoCommit(true);
setInTransaction(false);
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_canceling_transaction",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Fec
 * @return
 */
protected String toDate(Date Fec)
{
return("'"+ formatterDate.format(Fec)+"'");
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Fec
 * @return
 */
protected String toTimeStamp(Date Fec)
{
return("'"+ formatterTS.format(Fec)+"'");
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Val
 * @return
 */
protected String toString(String Val)
{
return("'"+Val.replace("'", "·")+"'");
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Val
 * @return
 */
protected String toBooleanString(Boolean Val)
{
if (Val.booleanValue())
    return("1");
else
    return("0");
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param DelCons
 * @param TablesNames
 * @return
 * @throws PDException
 */
protected String EvalConditions(Conditions DelCons, Vector TablesNames) throws PDException
{
if (DelCons.NumCond()<1)
    PDException.GenPDException("Empty_conditions",null);
String SQLWhere="(";
for (int i = 0; i < DelCons.NumCond(); i++)
    {
    Object O=DelCons.Cond(i);
    if (O instanceof Conditions)
        SQLWhere+=EvalConditions((Conditions)O, TablesNames);
    else
        SQLWhere+=EvalCondition((Condition)O, TablesNames);
    if (i<DelCons.NumCond()-1)
        {
        if (DelCons.isOperatorAnd())
            SQLWhere+=" AND ";
        else
            SQLWhere+=" OR ";
        }
    }
SQLWhere+=")";
return(SQLWhere);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Condit
 * @param TablesNames
 * @return
 * @throws PDException
 */
protected String EvalCondition(Condition Condit, Vector TablesNames) throws PDException
{
String SQLWhere="(";
if (Condit.isInvert())
    SQLWhere+="NOT (";
String FieldName=Condit.getField();
if (TablesNames!=null && TablesNames.size()>1 && FieldName.equals(PDFolders.fPDID))
    {
    Vector v=TablesNames;
    for (int j = 0; j < v.size(); j++)
        {
        String TabName = (String)v.elementAt(j);
        if (TabName.equals(PDFolders.getTableName()) || TabName.equals(PDDocs.getTableName()))
            {
            SQLWhere+=TabName+"."+FieldName;
            break;
            }
        }
    }
else
    SQLWhere+=FieldName;
if (Condit.getcType()==Condition.ctNORMAL)
    {
    switch(Condit.getComparation())
        {case Condition.cEQUAL:
             SQLWhere+="=";
             break;
        case Condition.cGT:
             SQLWhere+=">";
             break;
        case Condition.cGET:
             SQLWhere+=">=";
             break;
        case Condition.cLT:
             SQLWhere+="<";
             break;
        case Condition.cLET:
             SQLWhere+="<=";
             break;
        case Condition.cNE:
             SQLWhere+="<>";
             break;
        case Condition.cLIKE:
             SQLWhere+=" LIKE ";
             break;
        }
    Object O=Condit.getValue();
    if (O instanceof String)
        {
        String S=(String)O;    
        if (Condit.getComparation()==Condition.cLIKE && S.indexOf('%')==-1)
            SQLWhere+=toString("%"+S+"%");
        else    
            SQLWhere+=toString(S);
        }
    else if (O instanceof Date)
        SQLWhere+=toDate((Date)O);
    else if (O instanceof Boolean)
        SQLWhere+=toBooleanString((Boolean)O);
    else
        SQLWhere+=O;
    }
else if (Condit.getcType()==Condition.cEQUALFIELDS)
    {
    switch(Condit.getComparation())
        {case Condition.cEQUAL:
             SQLWhere+="=";
             break;
        }
    Object O=Condit.getValue();
    SQLWhere+=(String)O;
    }
else if (Condit.getcType()==Condition.ctIN)
    {
    SQLWhere+=" IN ( ";
    if (Condit.getComparation()==Condition.cINList)
        {
        HashSet List=(HashSet)Condit.getValue();
        Object l[]=List.toArray();
        for (int i = 0; i < l.length; i++)
            {
            Object object = l[i];
            if (object instanceof String)
                SQLWhere+=toString((String)object);
            else if (object instanceof Date)
                SQLWhere+=toDate((Date)object);
            else if (object instanceof Boolean)
                SQLWhere+=toBooleanString((Boolean)object);
            else
                SQLWhere+=object;
            if (i<l.length-1)
                SQLWhere+=",";
            }
        }
    else
        {
        SQLWhere+=EvalQuery((Query)Condit.getValue());
        }
    SQLWhere+=" )";
    }
//else if (Condit.getcType()==Condition.cINQuery)
//    {
//      implementation pending
//    }    
else
    {
    PDException.GenPDException("Unsupported_kind_of_condition", ""+Condit.getcType());
    }
if (Condit.isInvert())
    SQLWhere+="))";
else
    SQLWhere+=")";
return(SQLWhere);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Search
 * @return String identifier of the cursor
 * @throws PDException
 */
public Cursor OpenCursor(Query Search) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.OpenCursor:"+Search);
String SQL=EvalQuery(Search);
ResultSet rs=RsExecuteSql(SQL);
return(StoreCursor(rs, Search.getRetrieveFields()));
}
//-----------------------------------------------------------------------------------
public void CloseCursor(Cursor CursorIdent) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.CloseCursor:"+CursorIdent);
ResultSet rs=(ResultSet)CursorIdent.getResultSet();
try {
rs.close();
delCursor(CursorIdent);
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_closing_cursor", ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------------------------
public Record NextRec(Cursor CursorIdent) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.NextRec:"+CursorIdent);
ResultSet rs=(ResultSet)CursorIdent.getResultSet();
boolean hashNext=false;
try {
hashNext=rs.next();
} catch (SQLException ex)
    {
    PDException.GenPDException("Error_retrieving_next_record",ex.getLocalizedMessage());
    }
if (!hashNext)
    return(null);
Record Fields=CursorIdent.getFieldsCur();
Fields.initList();
for (int i = 0; i < Fields.NumAttr(); i++)
    {
    Attribute Attr=Fields.nextAttr();
    if (Attr.getName().contains("."+PDDocs.fVERSION))
        Attr.setName(PDDocs.fVERSION);
    else
    if (Attr.getName().contains("."+PDDocs.fPDID))
        Attr.setName(PDDocs.fPDID);
    try {
    if (Attr.getType()==Attribute.tSTRING)
        Attr.setValue(rs.getString(Attr.getName()));
    else if (Attr.getType()==Attribute.tTHES)
        Attr.setValue(rs.getString(Attr.getName()));
    else if (Attr.getType()==Attribute.tDATE)
            {
            String D=rs.getString(Attr.getName());
            if (D!=null && D.length()==8)
                Attr.setValue(formatterDate.parse(D));
            }
    else if (Attr.getType()==Attribute.tTIMESTAMP)
            {
            String D=rs.getString(Attr.getName());
            if (D!=null && D.length()==14)
                Attr.setValue(formatterTS.parse(D));
            }
    else if (Attr.getType()==Attribute.tINTEGER)
        Attr.setValue(new Integer(rs.getInt(Attr.getName())));
    else if (Attr.getType()==Attribute.tFLOAT)
        Attr.setValue(new Float(rs.getFloat(Attr.getName())));
    else if (Attr.getType()==Attribute.tBOOLEAN)
        Attr.setValue(rs.getBoolean(Attr.getName()));
    } catch(Exception ex)
        {
        PDException.GenPDException("Error_retrieving_attributes", Attr.getName()+"/"+ex.getLocalizedMessage());
        }
    }
return(Fields.Copy());
}
//-----------------------------------------------------------------------------------

/**
 *
 * @param Search
 * @return
 * @throws PDException
 */
private String EvalQuery(Query Search) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverJDBC.EvalQuery:"+Search);
String SQL="select ";
Record FieldNames=Search.getRetrieveFields();
FieldNames.initList();
for (int i = 0; i < FieldNames.NumAttr(); i++)
    {
    String FieldName=FieldNames.nextAttr().getName();
//    if (Search.getTables()!=null && Search.getTables().size()>1 && FieldName.equals(PDFolders.fPDID))
//        {
//        Vector v=Search.getTables();
//        for (int j = 0; j < v.size(); j++)
//            {
//            String TabName = (String)v.elementAt(j);
//            if (TabName.equals(PDFolders.getTableName()) || TabName.equals(PDDocs.getTableName()))
//                {
//                SQL+=TabName+"."+FieldName;
//                if (i!=FieldNames.NumAttr()-1)
//                   SQL+=",";
//                break;
//                }
//            }
//        }
//    else
        {
        SQL+=FieldName;
        if (i!=FieldNames.NumAttr()-1)
           SQL+=",";
        }
    }
SQL+=" from ";
if (Search.getTable()!=null)
    {
    if (PDLog.isDebug())
        PDLog.Debug("SQL_Table:"+Search.getTable());
    SQL+=Search.getTable();
    }
else
    {
    Vector TabNames=Search.getTables();
    for (int i = 0; i < TabNames.size(); i++)
        {
        if (PDLog.isDebug())
            PDLog.Debug("SQL_Tables:"+(String)TabNames.elementAt(i));
        SQL+=(String)TabNames.elementAt(i);
        if (i!=TabNames.size()-1)
           SQL+=",";
        }
    }
if (Search.getWhere()!=null && Search.getWhere().NumCond()>0)
    SQL+=" where "+EvalConditions(Search.getWhere(), Search.getTables());
if (Search.getOrder()!=null && Search.getOrder().length()>0)
    {
    SQL+=" order by "+Search.getOrder();
    }
else if (Search.getOrderList()!=null)
    {
    SQL+=" order by ";
    Vector v=Search.getOrderList();
    for (int i = 0; i < v.size(); i++)
        {
        String F = (String)v.elementAt(i);
        SQL+=F;
        if (i!=v.size()-1)
           SQL+=",";
        }
    }
return(SQL);
}
//-----------------------------------------------------------------------------------
}
