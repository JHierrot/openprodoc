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
import java.net.URL;
import java.net.URLConnection;
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

//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
protected StoreRem(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt, URL pOUrl, DocumentBuilder pDB)
{
super(pServer, pUser, pPassword, pParam, pEncrypt);
OUrl=pOUrl;
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
try {
//con.setAutoCommit(false);
//String SQL = "INSERT INTO "+getTable()+" (PDId, PDVersion, PDDATE, PDCONT) VALUES (?, ?, ?, ?)";
//PreparedStatement BlobStmt = con.prepareStatement(SQL);
//BlobStmt.setString(1, Id);
//BlobStmt.setString(2, Ver);
//BlobStmt.setTimestamp(3,  new Timestamp(System.currentTimeMillis()));
//// BlobStmt.setBinaryStream(4, Bytes);
//SerialBlob Bl=null;//  new SerialBlob(Buffer);
//int Tot=0;
//int readed=Bytes.read(Buffer);
//while (readed!=-1)
//    {
//    if (isEncript())
//       EncriptPass(Buffer, readed);
//    if (Bl==null)
//        Bl=new SerialBlob(Buffer);
//    else
//        Bl.setBytes(Tot, Buffer);
//    Tot+=readed;
//    readed=Bytes.read(Buffer);
//    }
//Bl.truncate(Tot);
//Bytes.close();
//BlobStmt.setBlob(4, Bl);
//BlobStmt.execute();
//con.commit();
//con.setAutoCommit(true);
//Bytes.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_inserting_content",ex.getLocalizedMessage());
    }
return (-1); 
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
try {
ReadWrite(DriverGeneric.S_DELFILE, "<OPD></OPD>");
} catch (Exception ex)
    {
    PDException.GenPDException("Error_deleting",ex.getLocalizedMessage());
    }
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
try {
//String SQL = "SELECT PDCONT FROM "+getTable()+" where PDId='"+Id+"' and PDVersion='"+Ver+"'";
//PreparedStatement BlobStmt = con.prepareStatement(SQL);
//ResultSet resultSet = BlobStmt.executeQuery();
//while (resultSet.next())
//    {
//    return (resultSet.getBinaryStream(1));
//    }
//PDException.GenPDException("Inexistent_content", Id+"/"+Ver);
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
   
//String SQL = "SELECT PDCONT FROM "+getTable()+" where PDId='"+Id+"' and PDVersion='"+Ver+"'";
//PreparedStatement BlobStmt = con.prepareStatement(SQL);
//ResultSet resultSet = BlobStmt.executeQuery();
//if (resultSet.next())
//    {
//    InputStream in=resultSet.getBinaryStream(1);
//    int readed=in.read(Buffer);
//    while (readed!=-1)
//        {
//        if (isEncript())
//           DecriptPass(Buffer, readed);
//        fo.write(Buffer, 0, readed);
//        Tot+=readed;
//        readed=in.read(Buffer);
//        }
//    in.close();
//    fo.flush();
//    fo.close();
//    }
//resultSet.close();
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
try {
ReadWrite(DriverGeneric.S_RENFILE, "<OPD></OPD>");
} catch (Exception ex)
    {
    PDException.GenPDException("Error_renaming_content:",ex.getLocalizedMessage());
    }
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
