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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Manages the storage of documents in a filesystem
 * @author jhierrot
 */
public class StoreFS extends StoreGeneric
{
static final String SEP="_";
    
//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
     * @param pEncrypt
     * @throws prodoc.PDExceptionFunc
 */
protected StoreFS(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt) throws PDExceptionFunc
{
super(pServer, pUser, pPassword, pParam, pEncrypt);
//if (!getServer().substring(getServer().length()-2).equals(getSeparator()) )
//    setServer(getServer()+getSeparator());
if (isEncript())
    setEncriptPass(pParam);
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
 * Do the actual insertion of the document binary. 
 * Always close the input stream to avoid problems
 * and degradation.
 * @param Id OpenProdoc identifier (PDId) of the document to store
 * @param Ver Versión Label of the Document
 * @param Bytes Inpuit stream to store
 * @param Rec   Record with Metadata of the document (not used in Filesystem storage)
 * @param OPDPath Path in OpenProdoc of the document (not used in Filesystem storage)
 * @return the size of the file
 * @throws PDException In any error
 */
protected int Insert(String Id, String Ver, InputStream Bytes, Record Rec, String OPDPath) throws PDException
{
VerifyId(Id);
FileOutputStream fo=null;
int Tot=0;
try {
File Path=new File(getPath()+ GenPath(Id, Ver));
if (!Path.isDirectory())
    Path.mkdirs();
fo = new FileOutputStream(getPath()+ GenPath(Id, Ver) +Id+SEP+Ver);
int readed=Bytes.read(Buffer);
while (readed!=-1)
    {
    if (isEncript())
       EncriptPass(Buffer, readed);
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
    Bytes.close();
    } catch (Exception e)
        {
        }
    }
return(Tot);
}
//-----------------------------------------------------------------
/**
 * Deletes the binary
 * @param Id Identifier of document
 * @param Ver Identifier of version
 * @param Rec Record with Metadata of the document (not used in Filesystem storage)
 * @throws PDException In Any Error
 */
@Override
protected void Delete(String Id, String Ver, Record Rec) throws PDException
{
VerifyId(Id);
File f=new File(getPath()+ GenPath(Id, Ver)+Id+SEP+Ver);
f.delete();
}
//-----------------------------------------------------------------
/**
 * Return the binary as InputStream
 * @param Id Identifier of document
 * @param Ver Identifier of version
 * @return an InputStream with the content
 * @throws PDException in any error
 */
@Override
protected InputStream Retrieve(String Id, String Ver, Record Rec) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("StoreFS.Retrieve: Id="+Id+" Ver="+Ver+" Rec="+Rec);                    
VerifyId(Id);    
FileInputStream in=null;
try {
if (PDLog.isDebug())
    PDLog.Debug("StoreFS.InputStream="+getPath() + GenPath(Id, Ver) + Id+SEP+Ver);                        
in = new FileInputStream(getPath() + GenPath(Id, Ver) + Id+SEP+Ver);
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
 * Changes the name of e binary. Used for CheckInCheckOut,..
 * @param Id1 Identifier of original document
 * @param Ver1 Identifier of original version
 * @param Id2 Identifier of Target document
 * @param Ver2 Identifier of Target version
 * @throws PDException
 */
@Override
protected void Rename(String Id1, String Ver1, String Id2, String Ver2) throws PDException
{
VerifyId(Id1);
File f=new File(getPath()+ GenPath(Id1, Ver1)+Id1+SEP+Ver1);
File f2=new File(getPath()+ GenPath(Id2, Ver2)+Id2+SEP+Ver2);
f.renameTo(f2);
}
//-----------------------------------------------------------------
}
