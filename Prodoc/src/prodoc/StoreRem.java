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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jhierrot
 */
public class StoreRem extends StoreGeneric
{
URL OUrl=null;
URLConnection OUrlc=null;
HttpURLConnection URLCon=null;
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
protected StoreRem(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt, URL pOUrl, HttpURLConnection pURLCon, DocumentBuilder pDB)
{
super(pServer, pUser, pPassword, pParam, pEncrypt);
OUrl=pOUrl;
URLCon=pURLCon;
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
protected int Insert0(String Id, String Ver, InputStream Bytes) throws PDException
{
VerifyId(Id);
int Tot=0;
try {
URLCon=(HttpURLConnection) OUrl.openConnection();
URLCon.setDoOutput(true);
URLCon.setDoInput(true);
//URLCon.setRequestProperty("Accept-Charset", DriverRemote.charset);
String BoundString="----------------------"+Long.toString(System.currentTimeMillis(), 16)+"";
URLCon.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BoundString);
URLCon.setRequestMethod("POST");
//URLCon.setReadTimeout(60000);
URLCon.connect();
DataOutputStream outputD = new DataOutputStream(URLCon.getOutputStream());
outputD.writeBytes((BoundString+NEWLINE));
outputD.writeBytes(("Content-Disposition: form-data; name=\"Order\"\r\n\r\n"+DriverRemote.S_INSFILE+NEWLINE));
outputD.writeBytes((BoundString+NEWLINE));
outputD.writeBytes(("Content-Disposition: form-data; name=\"Id\"\r\n\r\n"+Id+NEWLINE));
outputD.writeBytes((BoundString+NEWLINE));
outputD.writeBytes(("Content-Disposition: form-data; name=\"Ver\"\r\n\r\n"+Ver+NEWLINE));
outputD.writeBytes((BoundString+NEWLINE));
outputD.writeBytes(("Content-Disposition: form-data; name=\"FileUploaded\"; filename=\""+Id+"."+Ver+"\"\r\n\r\n"));
if (PDLog.isDebug())
    {  
    PDLog.Debug(BoundString+NEWLINE);
    PDLog.Debug("Content-Disposition: form-data; name=\"Order\"\r\n\r\n"+DriverRemote.S_INSFILE+NEWLINE);
    PDLog.Debug(BoundString+NEWLINE);
    PDLog.Debug("Content-Disposition: form-data; name=\"Id\"\r\n\r\n"+Id+NEWLINE);
    PDLog.Debug(BoundString+NEWLINE);
    PDLog.Debug("Content-Disposition: form-data; name=\"Ver\"\r\n\r\n"+Ver+NEWLINE);
    PDLog.Debug(BoundString+NEWLINE);
    PDLog.Debug("Content-Disposition: form-data; name=\"FileUploaded\"; filename=\""+Id+"."+Ver+"\"\r\n\r\n");
    }
int readed=Bytes.read(Buffer);
while (readed!=-1)
    {
    outputD.write( Buffer, 0, readed);
    if (PDLog.isDebug())
       PDLog.Debug(Arrays.toString(Buffer));
    Tot+=readed;
    readed=Bytes.read(Buffer);
    }
outputD.writeBytes(NEWLINE+BoundString+"--"+NEWLINE);
if (PDLog.isDebug())
   PDLog.Debug(NEWLINE+BoundString+"--"+NEWLINE);
outputD.flush();
outputD.close();
BufferedReader in = null;
try {
in = new BufferedReader( new InputStreamReader(URLCon.getInputStream()));
Answer.setLength(0);
String Line;
while ((Line= in.readLine()) != null)
    Answer.append(Line);
Document XMLObjects = DB.parse(new ByteArrayInputStream(Answer.toString().getBytes("UTF-8")));
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Result");
Node OPDObject = OPDObjectList.item(0);
if (OPDObject.getTextContent().equalsIgnoreCase("KO"))
    {
    OPDObjectList = XMLObjects.getElementsByTagName("Msg");
    if (OPDObjectList.getLength()>0)
        {
        OPDObject = OPDObjectList.item(0);
        PDException.GenPDException("Server_Error", DriverRemote.DeCodif(OPDObject.getTextContent()));
        }
    else
        PDException.GenPDException("Server_Error", "");
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
} catch (Exception ex)
    {
    PDException.GenPDException("Error_inserting_content",ex.getLocalizedMessage());
    }
return (Tot); 
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
int Tot=0;
try {
String boundary = MultiPartFormOutputStream.createBoundary();
URLCon=(HttpURLConnection) OUrl.openConnection();
URLCon.setDoOutput(true);
URLCon.setDoInput(true);
URLCon.setRequestProperty("Accept", "*/*");
URLCon.setRequestProperty("Content-Type", 
 MultiPartFormOutputStream.getContentType(boundary));
// set some other request headers...
URLCon.setRequestProperty("Connection", "Keep-Alive");
URLCon.setRequestProperty("Cache-Control", "no-cache");
MultiPartFormOutputStream MPOut = new MultiPartFormOutputStream(URLCon.getOutputStream(), boundary);
MPOut.writeField("Order", DriverRemote.S_INSFILE);
MPOut.writeField("Id", Id);
MPOut.writeField("Ver", Ver);
MPOut.writeFile("FileUploaded", null, Id+"."+Ver, Bytes);
MPOut.close();
BufferedReader in = null;
try {
in = new BufferedReader( new InputStreamReader(URLCon.getInputStream()));
Answer.setLength(0);
String Line;
while ((Line= in.readLine()) != null)
    Answer.append(Line);
Document XMLObjects = DB.parse(new ByteArrayInputStream(Answer.toString().getBytes("UTF-8")));
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Result");
Node OPDObject = OPDObjectList.item(0);
if (OPDObject.getTextContent().equalsIgnoreCase("KO"))
    {
    OPDObjectList = XMLObjects.getElementsByTagName("Msg");
    if (OPDObjectList.getLength()>0)
        {
        OPDObject = OPDObjectList.item(0);
        PDException.GenPDException("Server_Error", DriverRemote.DeCodif(OPDObject.getTextContent()));
        }
    else
        PDException.GenPDException("Server_Error", "");
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
} catch (Exception ex)
    {
    PDException.GenPDException("Error_inserting_content",ex.getLocalizedMessage());
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
 * @return
 * @throws PDException
 */
protected InputStream Retrieve(String Id, String Ver) throws PDException
{
VerifyId(Id);
VerifyId(Id);
int Tot=0;
if (PDLog.isDebug())
    PDLog.Debug("Retrieve: "+Id+"/"+Ver);
try {
URLCon=(HttpURLConnection) OUrl.openConnection();
URLCon.setDoOutput(true);
URLCon.setDoInput(true);
URLCon.setRequestProperty("Accept-Charset", DriverRemote.charset);
URLCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + DriverRemote.charset);
URLCon.setRequestMethod("POST");
//URLCon.setReadTimeout(60000);
String SumPar=DriverRemote.ORDER+"="+DriverRemote.S_RETRIEVEFILE+"&"+DriverRemote.PARAM+"=<OPD><Id>"+Id+"</Id><Ver>"+Ver+"</Ver></OPD>";
URLCon.setRequestProperty("Content-Length", "" + SumPar.getBytes(DriverRemote.charset).length );
URLCon.connect();
output = new OutputStreamWriter(URLCon.getOutputStream());
output.write(SumPar);
output.close(); 
} catch (Exception Ex)
    {
     try { 
     output.close(); 
     } catch (IOException ex) 
        {
        ex.printStackTrace();
        } 
    PDException.GenPDException("Error_retrieving_content", Ex.getLocalizedMessage());
    } 
try {
return(URLCon.getInputStream());
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_content", ex.getLocalizedMessage());
    }
return(null);
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
int Tot=0;
if (PDLog.isDebug())
    PDLog.Debug("Retrieve: "+Id+"/"+Ver);
try {
URLCon=(HttpURLConnection) OUrl.openConnection();
URLCon.setDoOutput(true);
URLCon.setDoInput(true);
URLCon.setRequestProperty("Accept-Charset", DriverRemote.charset);
URLCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + DriverRemote.charset);
URLCon.setRequestMethod("POST");
//URLCon.setReadTimeout(60000);
String SumPar=DriverRemote.ORDER+"="+DriverRemote.S_RETRIEVEFILE+"&"+DriverRemote.PARAM+"=<OPD><Id>"+Id+"</Id><Ver>"+Ver+"</Ver></OPD>";
URLCon.setRequestProperty("Content-Length", "" + SumPar.getBytes(DriverRemote.charset).length );
URLCon.connect();
output = new OutputStreamWriter(URLCon.getOutputStream());
output.write(SumPar);
output.close(); 
} catch (Exception Ex)
    {
     try { 
     output.close(); 
     } catch (IOException ex) 
        {
        ex.printStackTrace();
        } 
    PDException.GenPDException("Error_retrieving_content", Ex.getLocalizedMessage());
    } 
InputStream in=null;
try {
in=URLCon.getInputStream();
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
    URLCon.disconnect();
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
private Node ReadWrite(String pOrder, String pParam) throws PDException
{
Node OPDObject =null;
if (PDLog.isDebug())
    {
    PDLog.Debug("ReadWrite: Order:"+pOrder);
    PDLog.Debug("Param:"+pParam);
    }
try {
URLCon=(HttpURLConnection) OUrl.openConnection();
URLCon.setDoOutput(true);
URLCon.setDoInput(true);
URLCon.setRequestProperty("Accept-Charset", DriverRemote.charset);
URLCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + DriverRemote.charset);
URLCon.setRequestMethod("POST");
//URLCon.setReadTimeout(60000);
String Param="";
if (pParam!=null && pParam.length()!=0)
    Param=DriverRemote.PARAM+"="+pParam; // TODO Encode
String Order=DriverRemote.ORDER+"="+pOrder;
String SumPar=Order+"&"+Param;
URLCon.setRequestProperty("Content-Length", "" + SumPar.getBytes(DriverRemote.charset).length );
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
        PDException.GenPDException("Server_Error", DriverRemote.DeCodif(OPDObject.getTextContent()));
        }
    else
        PDException.GenPDException("Server_Error", "");
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
