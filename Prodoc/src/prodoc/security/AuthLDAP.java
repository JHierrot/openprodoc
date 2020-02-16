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

import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;
import prodoc.PDException;
import prodoc.PDExceptionFunc;
/**
 * Authenticator that authenticates a user against an LDAP system
 * @author jhierrot
 */
public class AuthLDAP extends AuthGeneric
{
/**
 * Constructor of the Ldap authenticator
 * @param pServer utl of LDAP server
 * @param pUser Not used in this authenticator
 * @param pPassword Not used in this authenticator
 * @param pParam "path/structure" of user
 */
public AuthLDAP(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
}

//----------------------------------------------------------------------
/**
 * Authenticates the user against ldap 
 * @param User User to authenticate
 * @param Pass Password of user
 * @throws PDException if fails the authentication or in any technical error
 */
@Override
public void Authenticate(String User, String Pass) throws PDException
{
DirContext ctx=null;
Hashtable env = new Hashtable();
env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
env.put(Context.PROVIDER_URL, getServer());
env.put(Context.SECURITY_AUTHENTICATION, "simple");
env.put(Context.SECURITY_PRINCIPAL, "cn="+User+" "+getParam());
env.put(Context.SECURITY_CREDENTIALS, Pass);
try {
ctx = new InitialDirContext(env);
ctx.close();
} catch (NamingException e)
    {
    PDExceptionFunc.GenPDException("User_or_password_incorrect",User);
    }
}
//----------------------------------------------------------------------
}
