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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

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

/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
StoreGeneric(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt)
{
Server=pServer;
User=pUser;
Password=pPassword;
Param=pParam;
Encrypt=pEncrypt;
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
 * @return
 * @throws PDException
 */
abstract protected int Insert(String Id, String Ver, InputStream Bytes) throws PDException;
/**
 *
 * @param Id
 * @param Ver
 * @throws PDException
 */
abstract protected void Delete(String Id, String Ver) throws PDException;
/**
 *
 * @param Id
 * @param Ver
 * @return
 * @throws PDException
 */
abstract protected InputStream Retrieve(String Id, String Ver) throws PDException;
/**
 *
 * @param Id
 * @param Ver
 * @param fo
 * @return
 * @throws PDException
 */
abstract protected int Retrieve(String Id, String Ver, OutputStream fo) throws PDException;
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
protected String getSeparator()
{
return (""+File.separatorChar);
}
//-------------------------------------------------------------------------
/**
 * generates Path
 * @param Id Identifier of document
 * @param Ver
 * @return path calculated ADITIONAL a repository path ENDED with OS separator
 */
protected String GenPath(String Id, String Ver)
{
/**
Fecha:Tue Mar 09 10:11:12 CET 2010 /  12742312f80-3fc5b4a2322bd9d8
Fecha:Wed Mar 10 10:11:12 CET 2010 /  12747578b80-3fe9d0a875193663
Fecha:Mon Mar 15 10:11:12 CET 2010 /  12761175780-3fe79dd735b89da3
Fecha:Sat Apr 10 10:11:12 CET 2010 /  127e6c5c100-3fda6052f513c84e
 */
return(Id.substring(0, 5)+getSeparator());
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
 * @return
 * @throws PDException
 */
protected int Insert(String Id, String Ver, String FileName) throws PDException
{
try {
if (PDLog.isDebug())
    PDLog.Debug("StoreGeneric.Insert"+FileName);
FileInputStream in = new FileInputStream(FileName);
return(Insert(Id, Ver, in));
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
 * @throws PDException
 */
protected void Copy(String Id1, String Ver1, String Id2, String Ver2) throws PDException
{
try {
InputStream Bytes=Retrieve(Id1, Ver1);
Insert(Id2, Ver2, Bytes);
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
     *
     * @param FilePath
     * @return
     */
    protected String ObtainName(String FilePath)
{
if (IsRef())
    {
    if (FilePath.startsWith(Server))
        return(FilePath.substring(Server.length()));
    else
        return(FilePath);
    }
throw new UnsupportedOperationException("Unsupported");
}
//-----------------------------------------------------------------
    /**
     *
     * @param DocName
     * @return
     */
    protected String GetUrl(String DocName)
{
throw new UnsupportedOperationException("Unsupported");    
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
protected void EncriptPass(byte[] Buffer, int readed)
{
byte[] Pass=EncriptPass.getBytes();    
for (int i = 0; i < readed; i++)
    {
    Buffer[i] = (byte)(Buffer[i] ^ Pass[ i % Pass.length]); 
    }
}
//-----------------------------------------------------------------
protected void DecriptPass(byte[] Buffer, int readed)
{
byte[] Pass=EncriptPass.getBytes();    
for (int i = 0; i < readed; i++)
    {
    Buffer[i] = (byte)(Buffer[i] ^ Pass[ i % Pass.length]); 
    }
}
//-----------------------------------------------------------------
}
