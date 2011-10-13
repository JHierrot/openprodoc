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

package prodocUI.servlet;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import prodoc.DriverGeneric;
import prodoc.ProdocFW;

/**
 *
 * @author jhierrot
 */
public class SExit extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
HttpSession Sess=Req.getSession(true);
DriverGeneric D=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
String User="";
if (D!=null)
    {
    User=D.getUser().getName();
    ProdocFW.freeSesion("PD", D);
    Sess.setAttribute("PRODOC_SESS", null);
    }
ShowMessage(Req, out, "Usuario [ <b>"+User+"</b> ] Desconectado");
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "Salida de Prodoc";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("SExit");
}
//-----------------------------------------------------------------------------------------------
}
