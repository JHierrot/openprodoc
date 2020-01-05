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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jhierrot
 */
public abstract class StoreGeneric
{
/**
 *
 */
private String Server=null;
/**
 *
 */
private String User=null;
/**
 *
 */
private String Password=null;
/**
 *
 */
private String Param=null;
/**
 *
 */
private boolean Encrypt=false;
private String EncriptPass=null;
/**
 *
 */
static final int TAMBUFF=1024*64;
/**
 *
 */
protected byte Buffer[]=new byte[TAMBUFF];
private Properties Prop=null;

/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
     * @param pEncrypt
     * @throws prodoc.PDExceptionFunc
 */
protected StoreGeneric(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt) throws PDExceptionFunc
{
Server=pServer;
User=pUser;
Password=pPassword;
Param=pParam;
Encrypt=pEncrypt;
if (Encrypt && (Param==null || Param.length()==0) )
    PDExceptionFunc.GenPDException("Encript_Pass_Required_as_Param", Server);
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
abstract protected void Create() throws PDException;
/**
 *
 * @throws PDException
 */
abstract protected void Destroy() throws PDException;
/**
 *
 * @throws PDException
 */
abstract protected void Connect() throws PDException;
/**
 *
 * @throws PDException
 */
abstract protected void Disconnect() throws PDException;
/**
 *
 * @param Id
 * @param Ver
 * @param Bytes
     * @param Rec
     * @param OPDPath
 * @return
 * @throws PDException
 */
abstract protected int Insert(String Id, String Ver, InputStream Bytes, Record Rec, String OPDPath) throws PDException;
/**
 *
 * @param Id
 * @param Ver
     * @param Rec
 * @throws PDException
 */
abstract protected void Delete(String Id, String Ver, Record Rec) throws PDException;
/**
 *
 * @param Id
 * @param Ver
 * @param Rec
 * @return
 * @throws PDException
 */
abstract protected InputStream Retrieve(String Id, String Ver, Record Rec) throws PDException;
/**
 *
 * @param Id
 * @param Ver
 * @param fo
 * @return
 * @throws PDException
 */
//abstract protected int Retrieve(String Id, String Ver, OutputStream fo) throws PDException;
/**
 *
 * @param Id
 * @param Ver
 * @param fo
 * @return
 * @throws PDException
 */
//abstract protected int RetrieveB64(String Id, String Ver, OutputStream fo) throws PDException;
/**
 *
 * @param Id1
 * @param Ver1
 * @param Id2
 * @param Ver2
 * @throws PDException
 */
abstract protected void Rename(String Id1, String Ver1, String Id2, String Ver2) throws PDException;

//-------------------------------------------------------------------------
/**
* @return the Server
*/
protected String getServer()
{
return Server;
}
//-------------------------------------------------------------------------
/**
* @param Server the Server to set
*/
protected void setServer(String Server)
{
this.Server = Server;
}
//-------------------------------------------------------------------------
/**
* @return the User
*/
protected String getUser()
{
return User;
}
//-------------------------------------------------------------------------
/**
* @param User the User to set
*/
protected void setUser(String User)
{
this.User = User;
}
//-------------------------------------------------------------------------
/**
* @return the Password
*/
protected String getPassword()
{
return Password;
}
//-------------------------------------------------------------------------
/**
* @param Password the Password to set
*/
protected void setPassword(String Password)
{
this.Password = Password;
}
//-------------------------------------------------------------------------
/**
* @return the Param
*/
protected String getParam()
{
return Param;
}
//-------------------------------------------------------------------------
/**
* @param Param the Param to set
*/
protected void setParam(String Param)
{
this.Param = Param;
}
//-------------------------------------------------------------------------
/**
* @return the Encript
*/
protected boolean isEncript()
{
return Encrypt;
}
//-------------------------------------------------------------------------
/**
* @param Encript the Encript to set
*/
protected void setEncript(boolean Encript)
{
this.Encrypt = Encript;
}
//-------------------------------------------------------------------------
/**
 *
 */
protected void Encode()
{
}
//-------------------------------------------------------------------------
/**
 *
 */
protected void Decode()
{
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
//protected String getSeparator()
//{
//return (""+File.separatorChar);
//}
//-------------------------------------------------------------------------
/**
 * generates Path
 * @param Id Identifier of document
 * @param Ver
 * @return path calculated ADITIONAL a repository path ENDED with OS separator
 */
protected String GenPath(String Id, String Ver)
{
//return(Id.substring(0, 5)+getSeparator());
return(Id.substring(0, 5)+DriverGeneric.LINSEP);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Id
 * @throws PDException
 */
protected void VerifyId(String Id)  throws PDException
{
if (Id==null || Id.length()==0)
    {
    PDLog.Error("Id contenido nulo");
    throw new PDException("Id contenido nulo");
    }
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Id
 * @param Ver
 * @param FileName
     * @param Rec
     * @param OPDPath
 * @return
 * @throws PDException
 */
protected int Insert(String Id, String Ver, String FileName, Record Rec, String OPDPath) throws PDException
{
try {
if (PDLog.isDebug())
    PDLog.Debug("StoreGeneric.Insert"+FileName+"Id="+Id+" Ver="+Ver+" Rec="+Rec+ "Path="+OPDPath);
FileInputStream in = new FileInputStream(FileName);
return(Insert(Id, Ver, in, Rec, OPDPath));
} catch (FileNotFoundException ex)
    {
    PDException.GenPDException("Error_inserting_content", FileName+"="+ex.getLocalizedMessage());
    return(-1);
    }
}
//-------------------------------------------------------------------------
/**
 *
 * @param Id1 Original Identifier
 * @param Ver1 Original Version
 * @param Id2  New Identifier
 * @param Ver2  New Version
 * @param Rec  Record with all the metadata (mainly for custom drivers)
 * @param OPDPath Path of document in OPD (mainly for custom drivers)
 * @throws PDException
 */
protected void Copy(String Id1, String Ver1, String Id2, String Ver2, Record Rec, String OPDPath) throws PDException
{
try {
if (PDLog.isDebug())
    PDLog.Debug("StoreGeneric.Copy<: Id1="+Id1+" Ver1="+Ver1+" Id2="+Id2+" Ver2="+Ver2+ " Rec="+Rec+ "Path="+OPDPath);    
Record Rec2=Rec.Copy();
InputStream Bytes=Retrieve(Id1, Ver1, Rec2);
Insert(Id2, Ver2, Bytes, Rec2, OPDPath);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_copying_content", ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 */
protected void InitTrans()
{
}
//-----------------------------------------------------------------
/**
 *
 */
protected void CommitTrans()
{
}
//-----------------------------------------------------------------
/**
 *
 */
protected void CancelTrans()
{
}
//-----------------------------------------------------------------
/**
 * Indicates if the repository is a Referencial Repositorie and then R/O
 * @return if the repository ir Reference
 */
protected boolean IsRef()
{
return(false);
}
//-----------------------------------------------------------------
/**
 * Indicates if the repository is a Referencial Repositorie and then R/O
 * @return if the repository ir Reference
 */
protected boolean IsURL()
{
return(false);
}
//-----------------------------------------------------------------
/**
 * @return the EncriptPass
 */
protected String getEncriptPass()
{
return EncriptPass;
}
//-----------------------------------------------------------------
/**
 * @param EncriptPass the EncriptPass to set
 */
public void setEncriptPass(String EncriptPass)
{
this.EncriptPass = EncriptPass;
}
//-----------------------------------------------------------------

    /**
     *
     * @param Buffer
     * @param readed
     */
protected void EncriptPass(byte[] Buffer, int readed)
{
byte[] Pass=EncriptPass.getBytes();    
for (int i = 0; i < readed; i++)
    {
    Buffer[i] = (byte)(Buffer[i] ^ Pass[ i % Pass.length]); 
    }
}
//-----------------------------------------------------------------

    /**
     *
     * @param Buffer
     * @param readed
     */
protected void DecriptPass(byte[] Buffer, int readed)
{
byte[] Pass=EncriptPass.getBytes();    
for (int i = 0; i < readed; i++)
    {
    Buffer[i] = (byte)(Buffer[i] ^ Pass[ i % Pass.length]); 
    }
}
//-----------------------------------------------------------------
/**
 * 
 * @param Id
 * @param Ver
 * @param fo
 * @param Rec
 * @return
 * @throws PDException 
 */
protected int Retrieve(String Id, String Ver, OutputStream fo, Record Rec) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("StoreGeneric.Retrieve: Id="+Id+" Ver="+Ver+" Rec="+Rec);            
int Tot=0;
VerifyId(Id);    
InputStream in=null;
try {
in = Retrieve(Id, Ver, Rec);
int readed=in.read(Buffer);
while (readed!=-1)
    {
    if (isEncript())
       DecriptPass(Buffer, readed);
    fo.write(Buffer, 0, readed);
    Tot+=readed;
    readed=in.read(Buffer);
    }
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_file",Id+"/"+Ver+"="+ex.getLocalizedMessage());
    }
finally 
    {
    try {
    if (in!=null)
        in.close();
    } catch (IOException ex)
        {}
    try {
    fo.flush();
    } catch (IOException ex)
        {}
    try {
        fo.close();
    } catch (IOException ex)
        {}
    }
return(Tot);
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
//protected int Retrieve(String Id, String Ver, OutputStream fo, Record Rec) throws PDException
//{
//int Tot=0;
//VerifyId(Id);    
//InputStream in=null;
//try {
//in = Retrieve(Id, Ver, Rec);
//int readed=in.read(Buffer);
//while (readed!=-1)
//    {
//    if (isEncript())
//       DecriptPass(Buffer, readed);
//    fo.write(Buffer, 0, readed);
//    Tot+=readed;
//    readed=in.read(Buffer);
//    }
//} catch (Exception ex)
//    {
//    PDException.GenPDException("Error_retrieving_file",Id+"/"+Ver+"="+ex.getLocalizedMessage());
//    }
//finally 
//    {
//    try {
//    if (in!=null)
//        in.close();
//    } catch (IOException ex)
//        {}
//    try {
//    fo.flush();
//    } catch (IOException ex)
//        {}
//    try {
//        fo.close();
//    } catch (IOException ex)
//        {}
//    }
//return(Tot);
//}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @param fo
 * @return
 * @throws PDException
 */
//protected int RetrieveB64(String Id, String Ver, OutputStream fo) throws PDException
//{
//int Tot=0;
//VerifyId(Id);    
//InputStream in=null;
//Base64InputStream b=null;
//try {
//in = Retrieve(Id, Ver);
//byte[] bytes = IOUtils.toByteArray(in);
//if (isEncript())
//   DecriptPass(bytes, bytes.length);
//b=new Base64InputStream(new ByteArrayInputStream(bytes),true);
//int readed=b.read(Buffer);
//while (readed!=-1)
//    {
//    fo.write(Buffer, 0, readed);
//    Tot+=readed;
//    readed=b.read(Buffer);
//    }
//} catch (Exception ex)
//    {
//    PDException.GenPDException("Error_retrieving_file",Id+"/"+Ver+"="+ex.getLocalizedMessage());
//    }
//finally 
//    {
//    try {
//    if (b!=null)
//        b.close();
//    } catch (IOException ex)
//        {}
//    try {
//    if (in!=null)
//        in.close();
//    } catch (IOException ex)
//        {}
//    try {
//    fo.flush();
//    } catch (IOException ex)
//        {}
//    try {
//    fo.close();
//    } catch (IOException ex)
//        {}
//    }
//return(Tot);
//}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @param fo
     * @param Rec
 * @return
 * @throws PDException
 */
protected int RetrieveB64(String Id, String Ver, OutputStream fo, Record Rec) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("StoreGeneric.RetrieveB64: Id="+Id+" Ver="+Ver+" Rec="+Rec);                
int Tot=0;
VerifyId(Id);    
InputStream in=null;
Base64InputStream b=null;
try {
in = Retrieve(Id, Ver, Rec);
byte[] bytes = IOUtils.toByteArray(in);
if (isEncript())
   DecriptPass(bytes, bytes.length);
b=new Base64InputStream(new ByteArrayInputStream(bytes),true);
int readed=b.read(Buffer);
while (readed!=-1)
    {
    fo.write(Buffer, 0, readed);
    Tot+=readed;
    readed=b.read(Buffer);
    }
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_file",Id+"/"+Ver+"="+ex.getLocalizedMessage());
    }
finally 
    {
    try {
    if (b!=null)
        b.close();
    } catch (IOException ex)
        {}
    try {
    if (in!=null)
        in.close();
    } catch (IOException ex)
        {}
    try {
    fo.flush();
    } catch (IOException ex)
        {}
    try {
    fo.close();
    } catch (IOException ex)
        {}
    }
return(Tot);
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @param Bytes
     * @param Rec
     * @param OPDPath
 * @return
 * @throws PDException
 */
//protected int InsertB64(String Id, String Ver, InputStream Bytes) throws PDException
//{
//int Res =0;
//Base64InputStream b=new Base64InputStream(Bytes,false);
//try {
//Res = Insert(Id,Ver,b);
//} catch (PDException Ex)
//    {
//    throw Ex;
//    }
//finally
//    {
//    try {
//    b.close();
//    } catch (Exception Ex)
//        {}
//    }
//return(Res);    
//}
//-----------------------------------------------------------------
protected int InsertB64(String Id, String Ver, InputStream Bytes, Record Rec, String OPDPath) throws PDException
{
int Res =0;
Base64InputStream b=new Base64InputStream(Bytes,false);
try {
Res = Insert(Id,Ver,b, Rec, OPDPath);
} catch (PDException Ex)
    {
    throw Ex;
    }
finally
    {
    try {
    b.close();
    } catch (Exception Ex)
        {}
    }
return(Res);    
}
//-----------------------------------------------------------------
//public boolean IsExtended()
//{
//return(false);    
//}    
//-----------------------------------------------------------------
//public int Insert(String PDId, String version, InputStream FileStream, Record Rec)  throws PDException
//{
//throw new UnsupportedOperationException("Not supported."); 
//}
//-----------------------------------------------------------------
//public int Insert(String PDId, String version, String FilePath, Record Rec) throws PDException
//{
//throw new UnsupportedOperationException("Not supported."); 
//}
//-----------------------------------------------------------------
//public InputStream Retrieve(String Id, String Ver, Record Rec) throws PDException
//{
//throw new UnsupportedOperationException("Not supported."); 
//}
//-----------------------------------------------------------------
//public void Delete(String Id, String Ver, Record Rec) throws PDException
//{
//throw new UnsupportedOperationException("Not supported."); 
//}
//-----------------------------------------------------------------
/**
* @return the Prop
*/
protected Properties getProp()
{
if (Prop==null)    
    Prop=new Properties();
return Prop;
}
//-----------------------------------------------------------------
/**
* @param Prop the Prop to set
*/
protected void setProp(Properties Prop)
{
this.Prop = Prop;
}
//-----------------------------------------------------------------
}
