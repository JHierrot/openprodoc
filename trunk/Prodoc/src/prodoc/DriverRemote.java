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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class implements a remote conection to a Prodoc Server
 * @author jhierrot
 */
public class DriverRemote  extends DriverGeneric
{

URL OUrl=null;
URLConnection OUrlc=null;
HttpURLConnection URLCon=null;
static final String charset="UTF-8";
// OutputStream output;
OutputStreamWriter output;
private static final String NEWLINE = "\r\n";
boolean Conected=false;
StringBuilder Answer=new StringBuilder(3000);

final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyyMMddHHmmss");
/**
 *
 */
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyyMMdd");

DocumentBuilder DB;

/**
* 
* @param pURL
* @param pPARAM
* @param pUser
* @param pPassword
* @throws prodoc.PDException
*/
public DriverRemote(String pURL, String pPARAM, String pUser, String pPassword) throws PDException
{
super(pURL, pPARAM, pUser, pPassword);
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.DriverRemote>:"+pURL+"/"+pUser+"/"+pPARAM);
try {
OUrl=new URL(pURL); 
DB =  DocumentBuilderFactory.newInstance().newDocumentBuilder();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_connecting_trough_URL"+pURL,ex.getLocalizedMessage());
    }
java.net.CookieManager cm = new java.net.CookieManager();
cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
java.net.CookieHandler.setDefault(cm);
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.DriverRemote<");
}
//--------------------------------------------------------------------------
/**
 * Logged user acording his authenticator
 * @param userName
 * @param Password
 * @throws PDException
 */
@Override
void Assign(String userName, String Password) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.Assign>:"+userName);
ReadWrite(S_LOGIN,"<OPD><U>"+userName+"</U><C>"+Password+"</C></OPD>");
Conected=true;
getUser().LoadAll(userName);
getPDCust().Load(getUser().getCustom());
setAppLang(getPDCust().getLanguage());
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.Assign<:"+userName);
}

//--------------------------------------------------------------------------
/**
 * Disconects freeing all resources
 * @throws PDException
 */
public void delete() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.delete>:"+getURL());
try {
OUrl=null;
Conected=false;
} catch (Exception ex)
    {
    PDException.GenPDException("Error_closing_JDBC_connection",ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.delete<:"+getURL());
}
//--------------------------------------------------------------------------
/**
 * Verify if the conection with the repository is ok
 * @return true if the connection is valid
 */
public boolean isConnected()
{
return (Conected);
}
//--------------------------------------------------------------------------
/**
 * Create a table
 * @param TableName
 * @param Fields
 * @throws PDException 
 */
protected void CreateTable(String TableName, Record Fields) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.CreateTable>:"+TableName+"/"+Fields);
ReadWrite(S_CREATE, "<OPD><Tab>"+TableName+"</Tab><Rec>"+Fields.toXMLt()+"</Rec></OPD>");
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.CreateTable<:"+TableName);
}
//--------------------------------------------------------------------------
/**
 * Drops a table
 * @param TableName
 * @throws PDException 
 */
protected void DropTable(String TableName) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.DropTable>:"+TableName);
ReadWrite(S_DROP, TableName);
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.DropTable<:"+TableName);
}
//--------------------------------------------------------------------------
/**
 * Modifies a table adding a field
 * @param TableName
 * @param NewAttr New field to add
 * @throws PDException
 */
protected void AlterTableAdd(String TableName, Attribute NewAttr, boolean IsVer) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.AlterTable>:"+TableName);
ReadWrite(S_ALTER, "<OPD><Tab>"+TableName+"</Tab><NewAttr>"+NewAttr.toXMLt()+"</NewAttr><IsVer>"+(IsVer?"1":"0")+"</IsVer></OPD>");
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.AlterTable<:"+TableName);
}
//--------------------------------------------------------------------------
/**
 * Modifies a table deleting a field
 * @param TableName
 * @param OldAttr old field to delete
 * @throws PDException
 */
protected void AlterTableDel(String TableName, String OldAttr) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.AlterTable>:"+TableName);
ReadWrite(S_ALTERDEL, "<OPD><Tab>"+TableName+"</Tab><OldAttr>"+OldAttr+"</OldAttr></OPD>");
if (PDLog.isInfo())
    PDLog.Info("DriverRemote.AlterTable<:"+TableName);
}
//--------------------------------------------------------------------------
/**
 * Inserts a record/row
 * @param TableName
 * @param Fields
 * @throws PDException 
 */
protected void InsertRecord(String TableName, Record Fields) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.InsertRecord>:"+TableName+"="+Fields);
ReadWrite(S_INSERT, "<OPD><Tab>"+TableName+"</Tab><Rec>"+Fields.toXMLt()+"</Rec></OPD>");
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.InsertRecord<");
}
//--------------------------------------------------------------------------
/**
 * Deletes SEVERAL records acording conditions
 * @param TableName
 * @param DelConds
 * @throws PDException 
 */
protected void DeleteRecord(String TableName, Conditions DelConds) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.DeleteRecord>:"+TableName);
ReadWrite(S_DELETE, "<OPD><Tab>"+TableName+"</Tab><DelConds>"+DelConds.toXML()+"</DelConds></OPD>");
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.DeleteRecord<");   
}
//--------------------------------------------------------------------------
/**
 * Update SEVERAL records acording conditions
 * @param TableName
 * @param NewFields
 * @param UpConds
 * @throws PDException 
 */
protected void UpdateRecord(String TableName, Record NewFields, Conditions UpConds) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.UpdateRecord>:"+TableName+"="+NewFields);
ReadWrite(S_UPDATE, "<OPD><Tab>"+TableName+"</Tab><Rec>"+NewFields.toXMLt()+"</Rec><DelConds>"+UpConds.toXML()+"</DelConds></OPD>");
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.UpdateRecord<:"+TableName+"="+NewFields);
}
//--------------------------------------------------------------------------
/**
 * Add referential integrity between two tables and one field each
 * @param TableName1
 * @param Field1
 * @param TableName2
 * @param Field2
 * @throws PDException
 */
protected void AddIntegrity(String TableName1, String Field1, String TableName2, String Field2) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.AddIntegrity>:"+TableName1+","+Field1+"/"+TableName2+","+Field2);
ReadWrite(S_INTEGRIT, "<OPD><Tab1>"+TableName1+"</Tab1><Field1>"+Field1+"</Field1><Tab2>"+TableName2+"</Tab2><Field2>"+Field2+"</Field2></OPD>");
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.AddIntegrity<");
}
//--------------------------------------------------------------------------
/**
 * Add referential integrity between two tables and 2 fields each
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
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.AddIntegrity2>:"+TableName1+","+Field11+","+Field12+"/"+TableName2+","+Field12+","+Field22);
ReadWrite(S_INTEGRIT2, "<OPD><Tab1>"+TableName1+"</Tab1><Field11>"+Field11+"</Field11><Field12>"+Field12+"</Field12><Tab2>"+TableName2+"</Tab2><Field21>"+Field21+"</Field21><Field22>"+Field22+"</Field22></OPD>");
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.AddIntegrity2<");
}
//-----------------------------------------------------------------------------------
/**
 * Starts a Transaction
 * @throws PDException 
 */
public void IniciarTrans() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.InitTrans");
try {
ReadWrite(S_INITTRANS, "<OPD></OPD>");
} catch (Exception ex)
    {
    PDException.GenPDException("Error_starting_transaction",ex.getLocalizedMessage());
    }
setInTransaction(true);
}
//-----------------------------------------------------------------------------------
/**
 * Ends a transaction
 * @throws PDException 
 */
public void CerrarTrans() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.CommitTrans");
try {
ReadWrite(S_COMMIT, "<OPD></OPD>");
setInTransaction(false);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_closing_transaction",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------------------------
/**
 * Aborts a Transaction
 * @throws PDException 
 */
public void AnularTrans() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.CancelTrans");
try {
ReadWrite(S_CANCEL, "<OPD></OPD>");
setInTransaction(false);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_canceling_transaction",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------------------------
/**
 * Opens a cursor
 * @param Search
 * @return String identifier of the cursor
 * @throws PDException
 */
public Cursor OpenCursor(Query Search) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.OpenCursor:"+Search);
Cursor C=new Cursor();
Node N=ReadWrite(S_SELECT, "<OPD><Query>"+Search.toXML()+"</Query></OPD>");
Vector Res=new Vector();

NodeList RecLst = N.getChildNodes();
for (int i = 0; i < RecLst.getLength(); i++)
    {
    Node Rec = RecLst.item(i);
    Record R=new Record();
    NodeList AttrLst = Rec.getChildNodes();
    for (int j = 0; j < AttrLst.getLength(); j++)
        {
        Node Attr = RecLst.item(j);
        if (Attr.getNodeName().equalsIgnoreCase("attr"))   
            {
            NamedNodeMap XMLattributes = Attr.getAttributes();
            Node XMLAttrName = XMLattributes.getNamedItem("Name");
            String AttrName=XMLAttrName.getNodeValue();
            XMLAttrName = XMLattributes.getNamedItem("Type");
            int Type=Integer.parseInt(XMLAttrName.getNodeValue());
            String Value=Attr.getTextContent(); 
            Attribute At=new Attribute(AttrName, "", "", Type, false, null, 254, false, false, false);
            At.Import(Value);
            R.addAttr(At);
            }
        }
    Res.add(R);
    }
return(StoreCursor(Res, Search.getRetrieveFields()));
}

//-----------------------------------------------------------------------------------
/**
 * Close a Cursor
 * @param CursorIdent
 * @throws PDException 
 */
public void CloseCursor(Cursor CursorIdent) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.CloseCursor:"+CursorIdent);
((Vector)CursorIdent.getResultSet()).clear();
CursorIdent.setResultSet(null);
delCursor(CursorIdent);
}
//-----------------------------------------------------------------------------------
/**
 * Retrieves next record of cursor
 * @param CursorIdent
 * @return OPD next Record
 * @throws PDException 
 */
public Record NextRec(Cursor CursorIdent) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverRemote.NextRec:"+CursorIdent);
Vector rs=(Vector)CursorIdent.getResultSet();
if (rs.isEmpty())
    return(null);
Record Fields=CursorIdent.getFieldsCur();
Fields.assign((Record)rs.get(0));
rs.remove(0);
return(Fields.Copy());
}
//-----------------------------------------------------------------------------------
private Node ReadWrite(String pOrder, String pParam) throws PDException
{
Node OPDObject =null;
try {
URLCon=(HttpURLConnection) OUrl.openConnection();
URLCon.setDoOutput(true);
URLCon.setDoInput(true);
URLCon.setRequestProperty("Accept-Charset", charset);
URLCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
URLCon.setRequestMethod("POST");
//URLCon.setReadTimeout(60000);
String Param="";
if (pParam!=null && pParam.length()!=0)
    Param="Param="+pParam; // TODO Encode
String Order="Order="+pOrder;
String SumPar=Order+"&"+Param;
URLCon.setRequestProperty("Content-Length", "" + SumPar.getBytes(charset).length );
URLCon.connect();
output = new OutputStreamWriter(URLCon.getOutputStream());
output.write(SumPar);
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    } 
finally 
    {
     try { 
     output.close(); 
     } catch (IOException ex) 
        {
        ex.printStackTrace();
        }
    }
BufferedReader in = null;
try {
in = new BufferedReader( new InputStreamReader(URLCon.getInputStream()));
Answer.setLength(0);
String Line;
while ((Line= in.readLine()) != null)
    Answer.append(Line);
Document XMLObjects = DB.parse(new ByteArrayInputStream(Answer.toString().getBytes("UTF-8")));
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Result");
OPDObject = OPDObjectList.item(0);
if (OPDObject.getTextContent().equalsIgnoreCase("KO"))
    {
    OPDObjectList = XMLObjects.getElementsByTagName("Msg");
    if (OPDObjectList.getLength()>0)
        {
        OPDObject = OPDObjectList.item(0);
        PDException.GenPDException("Error_Communicating_server", OPDObject.getTextContent().replace('^', '<'));
        }
    else
        PDException.GenPDException("Error_Communicating_server", "");
    }
OPDObjectList = XMLObjects.getElementsByTagName("Data");
OPDObject = OPDObjectList.item(0);
} catch (Exception ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(), "");
    }
finally
    {
    if (in!=null)
        {
        try{
        in.close();
        } catch (IOException ex)
            {
            ex.printStackTrace();
            }
        }
    URLCon.disconnect();
    }
return(OPDObject);
}
//-----------------------------------------------------------------   
}
