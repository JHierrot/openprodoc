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

import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;
import prodoc.PDException;
import prodoc.PDExceptionFunc;
/**
 *
 * @author jhierrot
 */
public class AuthLDAP extends AuthGeneric
{
    /**
     *
     * @param pServer
     * @param pUser
     * @param pPassword
     * @param pParam
     */
    public AuthLDAP(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
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
