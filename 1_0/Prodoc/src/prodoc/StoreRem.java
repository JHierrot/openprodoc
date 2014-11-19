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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static prodoc.DriverRemote.ORDER;
import static prodoc.DriverRemote.PARAM;

/**
 *
 * @author jhierrot
 */
public class StoreRem extends StoreGeneric
{
private CloseableHttpClient httpclient;
private HttpContext context;
private HttpPost UrlPost;
OutputStreamWriter output;
StringBuilder Answer=new StringBuilder(300);
DocumentBuilder DB=null;
final static String NEWLINE="\r\n";

//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
protected StoreRem(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt, HttpPost pUrlPost, CloseableHttpClient phttpclient, HttpContext pcontext, DocumentBuilder pDB)
{
super(pServer, pUser, pPassword, pParam, pEncrypt);
context=pcontext;
UrlPost=pUrlPost;
httpclient=phttpclient;
DB=pDB;
}

//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Create() throws PDException
{
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Destroy() throws PDException
{
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Connect() throws PDException
{
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Disconnect() throws PDException
{
}
//-----------------------------------------------------------------
/**
 * 
 * @param Id
 * @param Ver
 * @param Bytes
 * @return
 * @throws PDException
 */
protected int Insert(String Id, String Ver, InputStream Bytes) throws PDException
{
VerifyId(Id);
CloseableHttpResponse response2 = null;
Node OPDObject=null;
int Tot=0;
try {
InputStreamBody bin = new InputStreamBody(Bytes, "File"+System.currentTimeMillis() );
HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("bin", bin)
                    .addTextBody(ORDER, DriverRemote.S_INSFILE)
                    .addTextBody("Id", Id)
                    .addTextBody("Ver", Ver)
                    .build();
UrlPost.setEntity(reqEntity);
response2 = httpclient.execute(UrlPost, context);
HttpEntity Resp=response2.getEntity();
Bytes.close();
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
} catch (Exception ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(), "");
    }
finally
    {
    DB.reset();
    if (response2!=null)    
        try {
            response2.close();
        } catch (IOException ex) 
            {
            PDException.GenPDException(ex.getLocalizedMessage(), "");
            }
    }
return (Tot); 
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @throws PDException
 */
protected void Delete(String Id, String Ver) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("StoreRem.Delete:"+Id+"/"+Ver);
ReadWrite(DriverGeneric.S_DELFILE, "<OPD><Id>"+Id+"</Id><Ver>"+Ver+"</Ver></OPD>");
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @throws PDException
 */
protected void Delete(String Reposit, String Id, String Ver) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("StoreRem.Delete:"+Id+"/"+Ver);
ReadWrite(DriverGeneric.S_DELFILE, "<OPD><Rep>"+Reposit+"</Rep><Id>"+Id+"</Id><Ver>"+Ver+"</Ver></OPD>");
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @return
 * @throws PDException
 */
protected InputStream Retrieve(String Id, String Ver) throws PDException
{
VerifyId(Id);
CloseableHttpResponse response2 = null;
try {
List <NameValuePair> nvps = new ArrayList <NameValuePair>();
nvps.add(new BasicNameValuePair(ORDER, DriverRemote.S_RETRIEVEFILE));
nvps.add(new BasicNameValuePair(DriverRemote.PARAM, "<OPD><Id>"+Id+"</Id><Ver>"+Ver+"</Ver></OPD>"));
UrlPost.setEntity(new UrlEncodedFormEntity(nvps));
response2 = httpclient.execute(UrlPost, context);
HttpEntity Resp=response2.getEntity();
return(Resp.getContent());
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_content", ex.getLocalizedMessage());
    }
return (null); 
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @param fo
 * @return
 * @throws PDException
 */
protected int Retrieve(String Id, String Ver, OutputStream fo) throws PDException
{
VerifyId(Id);
CloseableHttpResponse response2 = null;
int Tot=0;
InputStream in=null;
HttpEntity Resp=null;
try {
List <NameValuePair> nvps = new ArrayList <NameValuePair>();
nvps.add(new BasicNameValuePair(ORDER, DriverRemote.S_RETRIEVEFILE));
nvps.add(new BasicNameValuePair(DriverRemote.PARAM, "<OPD><Id>"+Id+"</Id><Ver>"+Ver+"</Ver></OPD>"));
UrlPost.setEntity(new UrlEncodedFormEntity(nvps));
response2 = httpclient.execute(UrlPost, context);
Resp=response2.getEntity();
in=Resp.getContent();
int readed=in.read(Buffer);
while (readed!=-1)
    {
    fo.write(Buffer, 0, readed);
    Tot+=readed;
    readed=in.read(Buffer);
    }
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
    if (response2!=null)
        {
        try {
        response2.close();
        } catch (IOException ex)
            {
            ex.printStackTrace();
            }        
        }
    }
return(Tot);
}
//-----------------------------------------------------------------
/**
 * 
 * @param Id1 Original Identifier
 * @param Ver1 Original Version
 * @param Id2  New Identifier
 * @param Ver2  New Version
 * @throws PDException
 */
@Override
protected void Rename(String Id1, String Ver1, String Id2, String Ver2) throws PDException
{
VerifyId(Id1);
ReadWrite(DriverGeneric.S_RENFILE, "<OPD><Id1>"+Id1+"</Id1><Ver1>"+Ver1+"</Ver1><Id2>"+Id2+"</Id2><Ver2>"+Ver2+"</Ver2></OPD>");
}
//-----------------------------------------------------------------
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
