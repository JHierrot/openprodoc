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
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author jhierrot
 */
public class StoreRefFS extends StoreGeneric
{
/**
 *
 */
private Connection con=null;
/**
 *
 */
private Statement stmt=null;
/**
 *
 */
private String Table=null;
/**
 *
 */
private String Driver=null;


//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
protected StoreRefFS(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
setDriver(pParam);
setTable(pParam);
}

//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Create() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Destroy() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Connect() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Disconnect() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
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
throw new UnsupportedOperationException("Not supported yet.");
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
throw new UnsupportedOperationException("Not supported yet.");
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
throw new UnsupportedOperationException("Not supported yet.");
}
//-----------------------------------------------------------------

/**
 *
 * @return
 */
protected String getDriver()
{
return(Driver);
}
//-----------------------------------------------------------------
/**
* @return the Table
*/
protected String getTable()
{
return Table;
}
//-----------------------------------------------------------------
/**
* @param pParam the Table to set
*/
protected void setTable(String pParam)
{
throw new UnsupportedOperationException("Not supported yet.");
}
//-----------------------------------------------------------------
/**
* @param pParam the Driver to set
*/
protected void setDriver(String pParam)
{
throw new UnsupportedOperationException("Not supported yet.");
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
