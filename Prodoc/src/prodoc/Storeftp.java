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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author jhierrot
 */
public class Storeftp extends StoreGeneric
{
/**
 *
 */
FTPClient ftpCon = new FTPClient();
//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
protected Storeftp(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
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
try {
ftpCon.connect(getServer());
ftpCon.login(getUser(), getPassword());
if (getParam()!=null && getParam().length()!=0)
   ftpCon.changeWorkingDirectory(getParam());
ftpCon.setFileType(FTP.BINARY_FILE_TYPE);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_connecting_to_ftp",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Disconnect() throws PDException
{
try {
ftpCon.logout();
ftpCon.disconnect();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_disconnecting_from_ftp",ex.getLocalizedMessage());
    }
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
try {
String NPath=GenPath(Id, Ver);
if (!ftpCon.changeWorkingDirectory(NPath))
    if (!ftpCon.makeDirectory(NPath)) 
        PDException.GenPDException("Error_creating_dir_ftp",NPath);
if (!ftpCon.storeFile(Id, Bytes))
    PDException.GenPDException("Error_writing_file_to_ftp",Id+"/"+Ver);
Bytes.close();
ftpCon.completePendingCommand();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_writing_file_to_ftp",Id+"/"+Ver+"="+ex.getLocalizedMessage());
    }
return(0);
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
try {
VerifyId(Id);
ftpCon.deleteFile(GenPath(Id, Ver) + Id);
} catch (IOException ex)
    {
    PDException.GenPDException("Error_deleting_file_from_ftp",Id+"/"+Ver+"="+ex.getLocalizedMessage());
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
InputStream in=null;
try {
in=ftpCon.retrieveFileStream(GenPath(Id, Ver) + Id);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_file_ftp",Id+"/"+Ver+"="+ex.getLocalizedMessage());
    }
return(in);

}
//-----------------------------------------------------------------
/**
 *
 * @param Id1
 * @param Ver1
 * @param Id2
 * @param Ver2
 * @throws PDException
 */
@Override
protected void Rename(String Id1, String Ver1, String Id2, String Ver2) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//-----------------------------------------------------------------
}
