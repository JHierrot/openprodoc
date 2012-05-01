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

import java.io.InputStream;

/**
 *
 * @author jhierrot
 */
public class StoreRefURL extends StoreGeneric
{


//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
protected StoreRefURL(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
}
//-----------------------------------------------------------------
/**
 * @param Id
 * @param Ver
 * @param FileName
 * @return
 * @throws PDException
 */
@Override
protected int Insert(String Id, String Ver, String FileName) throws PDException
{
throw new UnsupportedOperationException("Not supported.");   
}
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
throw new UnsupportedOperationException("Not supported.");
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
// ignored. Maintained for generalization.
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
throw new UnsupportedOperationException("Not supported.");
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
throw new UnsupportedOperationException("Not supported.");
}
//-----------------------------------------------------------------
@Override
protected void Create() throws PDException
{
// not necessary
}
@Override
protected void Destroy() throws PDException
{
// not necessary
}
@Override
protected void Connect() throws PDException
{
// not necessary
}
@Override
protected void Disconnect() throws PDException
{
// not necessary
}
//-----------------------------------------------------------------
/**
 * Indicates if the repository is a Referencial Repositorie and then R/O
 * @return if the repository ir Reference
 */
@Override
protected boolean IsRef()
{
return(true);
}
//-----------------------------------------------------------------
/**
 * Indicates if the repository is a Referencial Repositorie and then R/O
 * @return if the repository ir Reference
 */
@Override
protected boolean IsURL()
{
return(true);
}
//-----------------------------------------------------------------
@Override
protected String GetUrl(String DocName)
{
return(getServer()+DocName);    
}        
//-----------------------------------------------------------------
}
