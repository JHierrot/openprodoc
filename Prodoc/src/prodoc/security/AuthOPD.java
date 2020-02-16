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

import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDExceptionFunc;
import prodoc.PDUser;

/**
 * Ayuthenticator that authenticates using User and password stored in OPD database 
 * @author jhierrot
 */
public class AuthOPD extends AuthGeneric
{
/**
 *
 */
DriverGeneric OPDInstance;

/**
 * Constructor of OPD Authentication
 * @param pServer not used for this authenticator
 * @param pUser not used for this authenticator
 * @param pPassword not used for this authenticator
 * @param pParam not used for this authenticator
 * @param pOPDInstance OpenProdoc session for loading the user information
 */
public AuthOPD(String pServer, String pUser, String pPassword, String pParam, DriverGeneric pOPDInstance)
{
super(pServer, pUser, pPassword, pParam);
OPDInstance=pOPDInstance;
}
//----------------------------------------------------------------------
/**
 * Authenticates the user against the internal database of OpenProdoc
 * @param User User to authenticate
 * @param Pass Pawwsord
 * @throws PDException if fails the authentication or in any technical error
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
{
PDUser U=new PDUser(OPDInstance);
U.UpdatePass(User, NewPass);
}
//----------------------------------------------------------------------
}
