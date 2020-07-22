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
 * author: Joaquin Hierro      2016
 * 
 */

package SoftManagOPDServ;

import SoftManagOPDUI.FLogin;
import SoftManagOPDUI.FMain;
import SoftManagOPDUI.SParent;
import static SoftManagOPDUI.SParent.getProdocProperRef;
import static SoftManagOPDUI.SParent.setSessOPD;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prodoc.DriverGeneric;
import prodoc.ProdocFW;

/**
 *
 * @author jhierrot
 */
public class Login extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
     * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws IOException
{
response.setContentType("text/html;charset=UTF-8");
PrintWriter out = response.getWriter();
HttpSession Sess=Req.getSession();
String IdConfig=Req.getParameter("IdConfig");
try {
String User=Req.getParameter("User");
String Password=Req.getParameter("Password");
if (User==null || User.length()==0 ||Password==null || Password.length()==0)
    {
    out.println(new FLogin(Sess, null, IdConfig).toHtml());
    return;
    }
if (!OPDFWLoaded)
    {
    ProdocFW.InitProdoc("PD", getProdocProperRef());
    OPDFWLoaded=true;
    }
if (IdConfig==null || IdConfig.length()==0)
    IdConfig="SoftManOpdConf";
setIdConf(Req, IdConfig);
DriverGeneric D=ProdocFW.getSession("PD", User, Password);
setSessOPD(Req, D);
out.println(new FMain(Sess).toHtml());
} catch (Exception Ex)
    {
    out.println(new FLogin(Sess, Ex.getLocalizedMessage(), IdConfig).toHtml());
    }
out.close();
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "Login Servlet";
}
//-----------------------------------------------------------------------------------------------
}
