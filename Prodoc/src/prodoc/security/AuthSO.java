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

package prodoc.security;

import prodoc.PDException;
import prodoc.PDExceptionFunc;

/**
 * Authenticator that delegates authentication in the Operative system, trusting in curent O.S. user
 * @author jhierrot
 */
public class AuthSO extends AuthGeneric
{
/**
 * Constructor of the Operatice System Authenticator
 * @param pServer not used in this authenticator
 * @param pUser not used in this authenticator
 * @param pPassword not used in this authenticator
 * @param pParam not used in this authenticator
 */
public AuthSO(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
}
//----------------------------------------------------------------------
/**
 * Checks if the user to authenticate if the same Operative System user running the current proces 
 * @param User user to authenticate
 * @param Pass not used. delegated to the Operative System
 * @throws PDException if the authentication fails or in any error
 */
@Override
public void Authenticate(String User, String Pass) throws PDException
{
String UserSO=System.getProperty("user.name");
if (!UserSO.equalsIgnoreCase(User))
    PDExceptionFunc.GenPDException("User_or_password_incorrect", User);
}
//----------------------------------------------------------------------
}
