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
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 *
 * @author jhierrot
 */
public class StoreFS extends StoreGeneric
{
//-----------------------------------------------------------------
    /**
     *
     * @param pServer
     * @param pUser
     * @param pPassword
     * @param pParam
     */
    protected StoreFS(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
if (!getServer().substring(getServer().length()-2).equals(getSeparator()) )
    setServer(getServer()+getSeparator());
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Create() throws PDException
{
File BasePath=new File(getPath());
if (BasePath.isDirectory())
    return;
try {
BasePath.mkdirs();
} catch (Exception e)
    {
    PDException.GenPDException("Error_creating_folder_for_StoreFS",getPath()+"="+e.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Destroy() throws PDException
{
File BasePath=new File(getPath());
if (!BasePath.isDirectory())
    return;
BasePath.delete();
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
FileOutputStream fo=null;
int Tot=0;
try {
File Path=new File(getPath()+ GenPath(Id, Ver));
if (!Path.isDirectory())
    Path.mkdirs();
fo = new FileOutputStream(getPath()+ GenPath(Id, Ver) +Id+"_"+Ver);
int readed=Bytes.read(Buffer);
while (readed!=-1)
    {
    fo.write(Buffer, 0, readed);
    Tot+=readed;
    readed=Bytes.read(Buffer);
    }
Bytes.close();
fo.close();
} catch (Exception e)
    {
    PDException.GenPDException("Error_writing_to_file",Id+"/"+Ver+"="+e.getLocalizedMessage());
    }
finally
    {
    try {
    if (fo!=null)
        fo.close();
    } catch (Exception e)
        {
        }
    }
return(Tot);
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
VerifyId(Id);
File f=new File(getPath()+ GenPath(Id, Ver)+Id+"_"+Ver);
f.delete();
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
FileInputStream in=null;
try {
in = new FileInputStream(getPath() + GenPath(Id, Ver) + Id+"_"+Ver);
} catch (FileNotFoundException ex)
    {
    PDException.GenPDException("Error_retrieving_file",Id+"/"+Ver+"="+ex.getLocalizedMessage());
    }
return(in);
}
//-----------------------------------------------------------------
/**
 *
 * @return
 */
private String getPath()
{
return(getServer());
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
VerifyId(Id1);
File f=new File(getPath()+ GenPath(Id1, Ver1)+Id1+"_"+Ver1);
File f2=new File(getPath()+ GenPath(Id2, Ver2)+Id2+"_"+Ver2);
f.renameTo(f2);
}
//-----------------------------------------------------------------
}
