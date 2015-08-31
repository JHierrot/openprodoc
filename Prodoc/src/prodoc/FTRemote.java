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
 * author: Joaquin Hierro      2015
 * 
 */

package prodoc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.ContentHandler;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static prodoc.DriverRemote.ORDER;
import static prodoc.DriverRemote.PARAM;


/**
 *
 * @author Joaquín Hierro
 */
public class FTRemote extends FTConnector
{
private CloseableHttpClient httpclient;
private HttpContext context;
private HttpPost UrlPost;
OutputStreamWriter output;
StringBuilder Answer=new StringBuilder(300);
DocumentBuilder DB=null;
final static String NEWLINE="\r\n";
    
//-------------------------------------------------------------------------
public FTRemote(String pServer, String pUser, String pPassword, String pParam, HttpPost pUrlPost, CloseableHttpClient phttpclient, HttpContext pcontext, DocumentBuilder pDB)
{
super(pServer, pUser, pPassword, pParam);
context=pcontext;
UrlPost=pUrlPost;
httpclient=phttpclient;
DB=pDB;
}
//-------------------------------------------------------------------------
@Override
protected void Create() throws PDException
{
throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}
//-------------------------------------------------------------------------
@Override
protected void Destroy() throws PDException
{
throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}
//-------------------------------------------------------------------------
@Override
protected void Connect() throws PDException
{
}
//-------------------------------------------------------------------------
@Override
protected void Disconnect() throws PDException
{
}
//-------------------------------------------------------------------------
@Override
protected int Insert(String Type, String Id, InputStream Bytes, Record sMetadata) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("FTRemote.Insert:"+Id);
ReadWrite(DriverGeneric.S_FTUPD, "<OPD><Type>"+Type+"</Type><Id>"+Id+"</Id></OPD>");
return(0);
}
//-------------------------------------------------------------------------
@Override
protected int Update(String Type, String Id, InputStream Bytes, Record sMetadata) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("FTRemote.Update:"+Id);
ReadWrite(DriverGeneric.S_FTUPD, "<OPD><Type>"+Type+"</Type><Id>"+Id+"</Id></OPD>");
return(0);
}
//-------------------------------------------------------------------------
@Override
protected void Delete(String Id) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("FTRemote.Delete:"+Id);
ReadWrite(DriverGeneric.S_FTDEL, "<OPD><Id>"+Id+"</Id></OPD>");
}
//-------------------------------------------------------------------------
@Override
protected ArrayList<String> Search(String Type, String sDocMetadata, String sBody, String sMetadata) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("FTRemote.Search:"+Type+"/");
ArrayList<String> Res=new ArrayList<String>();
Node N=ReadWrite(DriverGeneric.S_FTSEARCH, "<OPD><Type>"+Type+"</Type><DocMetadata>"+(sDocMetadata!=null?sDocMetadata:"")+"</DocMetadata><Body>"+sBody+"</Body><Metadata>"+sMetadata+"</Metadata></OPD>");
NodeList RecLst = N.getChildNodes();
for (int i = 0; i < RecLst.getLength(); i++)
    {
    Node ID = RecLst.item(i);
    String IdRes="";
    if (ID.getNodeName().equalsIgnoreCase("ID"))  
        {
        IdRes=ID.getTextContent();
        Res.add(IdRes);
        }
    }
return(Res);
}
//-------------------------------------------------------------------------
/**
 * Communicates to the OpenProdoc Server by http sending instructionss
 * @param pOrder Order to execute
 * @param pParam Parameters of the order (can be empty or null depending on order
 * @return an xml node extracted form XML answer.
 * @throws PDException in any error
 */
private Node ReadWrite(String pOrder, String pParam) throws PDException
{
Node OPDObject=null;
CloseableHttpResponse response2 = null;
if (PDLog.isDebug())
    {
    PDLog.Debug("DriverRemote. ReadWrite: Order:"+pOrder);
    PDLog.Debug("Param:"+pParam);
    }
try {
List <NameValuePair> nvps = new ArrayList <NameValuePair>();
nvps.add(new BasicNameValuePair(ORDER, pOrder));
nvps.add(new BasicNameValuePair(PARAM, pParam));
UrlPost.setEntity(new UrlEncodedFormEntity(nvps));
response2 = httpclient.execute(UrlPost, context);
HttpEntity Resp=response2.getEntity();
Document XMLObjects = DB.parse(Resp.getContent());
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Result");
OPDObject = OPDObjectList.item(0);
if (OPDObject.getTextContent().equalsIgnoreCase("KO"))
    {
    OPDObjectList = XMLObjects.getElementsByTagName("Msg");
    if (OPDObjectList.getLength()>0)
        {
        OPDObject = OPDObjectList.item(0);
        PDException.GenPDException("Server_Error", DriverGeneric.DeCodif(OPDObject.getTextContent()));
        }
    else
        PDException.GenPDException("Server_Error", "");
    }
OPDObjectList = XMLObjects.getElementsByTagName("Data");
OPDObject = OPDObjectList.item(0);
} catch (Exception ex)
    {
    if (PDLog.isDebug())
        ex.printStackTrace();
    PDException.GenPDException(ex.getLocalizedMessage(), "");
    }
finally
    {
    if (response2!=null)    
        try {
            response2.close();
        } catch (IOException ex) 
            {
            PDException.GenPDException(ex.getLocalizedMessage(), "");
            }
    }
return(OPDObject);
}
//-----------------------------------------------------------------   

}
