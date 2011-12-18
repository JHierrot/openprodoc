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

package prodoc.security;

import prodoc.PDException;

/**
 *
 * @author jhierrot
 */
public abstract class AuthGeneric
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

//----------------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
public AuthGeneric(String pServer, String pUser, String pPassword, String pParam)
{
Server=pServer;
User=pUser;
Password=pPassword;
Param=pParam;
}
//----------------------------------------------------------------------
/**
 * 
 * @param User
 * @param Pass
 * @throws PDException
 */
abstract public void Authenticate(String User, String Pass) throws PDException;

//----------------------------------------------------------------------
/**
 * Method to change the password when is allowed by the kind and actual authenticator
 * @param User User who will change his password
 * @param NewPass New Password to assign
 * @throws PDException In ani problem
 */
public void UpdatePass(String User, String NewPass) throws PDException
{
PDException.GenPDException("Operation_do_not_allowed", null);
}
//----------------------------------------------------------------------
/**
* @return the Server
*/
protected String getServer()
{
return Server;
}
//----------------------------------------------------------------------
/**
* @param Server the Server to set
*/
protected void setServer(String Server)
{
this.Server = Server;
}
//----------------------------------------------------------------------
/**
* @return the User
*/
protected String getUser()
{
return User;
}
//----------------------------------------------------------------------
/**
* @param User the User to set
*/
protected void setUser(String User)
{
this.User = User;
}
//----------------------------------------------------------------------
/**
* @return the Password
*/
protected String getPassword()
{
return Password;
}
//----------------------------------------------------------------------
/**
* @param Password the Password to set
*/
protected void setPassword(String Password)
{
this.Password = Password;
}
//----------------------------------------------------------------------
/**
* @return the Param
*/
protected String getParam()
{
return Param;
}
//----------------------------------------------------------------------
/**
* @param Param the Param to set
*/
protected void setParam(String Param)
{
this.Param = Param;
}
//----------------------------------------------------------------------
}
