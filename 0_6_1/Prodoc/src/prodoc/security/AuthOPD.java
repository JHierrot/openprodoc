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

import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDExceptionFunc;
import prodoc.PDUser;

/**
 *
 * @author jhierrot
 */
public class AuthOPD extends AuthGeneric
{
/**
 *
 */
DriverGeneric OPDInstance;

/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 * @param pOPDInstance
 */
public AuthOPD(String pServer, String pUser, String pPassword, String pParam, DriverGeneric pOPDInstance)
{
super(pServer, pUser, pPassword, pParam);
OPDInstance=pOPDInstance;
}
//----------------------------------------------------------------------
/**
 *
 * @param User
 * @param Pass
 * @throws PDException
 */
@Override
public void Authenticate(String User, String Pass) throws PDException
{
PDUser U=new PDUser(OPDInstance);
U.Load(User);
PDUser U2=new PDUser(OPDInstance);
U2.setPassword(Pass);
if (!U.getPassword().equals(U2.getPassword()))
    PDExceptionFunc.GenPDException("User_or_password_incorrect", User);
}
//----------------------------------------------------------------------
/**
 * Method to change the password in OPD
 * @param User User who will change his password
 * @param NewPass New Password to assign
 * @throws PDException In ani problem
 */
@Override
public void UpdatePass(String User, String NewPass) throws PDException
{// TODO: actualización clave falla
    // TODO: Comprobar activo
PDUser U=new PDUser(OPDInstance);
U.Load(User);
U.setPassword(NewPass);
U.update();
}
//----------------------------------------------------------------------
}
