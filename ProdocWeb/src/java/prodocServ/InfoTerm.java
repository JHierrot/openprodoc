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

package prodocServ;

import html.Element;
import html.Table;
import java.io.*;
import javax.servlet.http.*;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;
import prodoc.Record;


/**
 *
 * @author jhierrot
 * @version
 */
public class InfoTerm extends ServParent
{
//-----------------------------------------------------------------------------------------------
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
HttpSession Sess=Req.getSession(true);
String IdTerm=(String)Req.getParameter("Term");
DriverGeneric PDSession=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
PDThesaur Term = new PDThesaur(PDSession);
Term.Load(IdTerm);
Table InfoTab=new Table(2, 8, 0);
InfoTab.getCelda(1,0).AddElem(new Element(Term.getName()));
InfoTab.getCelda(1,1).AddElem(new Element(Term.getDescription()));
if (Term.getParentId()!=null && Term.getParentId().length()!=0)
    {
    PDThesaur TermP = new PDThesaur(PDSession);
    TermP.Load(Term.getParentId());
    InfoTab.getCelda(1,3).AddElem(new Element("BT:"+TermP.getName()));
    }
if (Term.getUse()!=null && Term.getUse().length()!=0)
    {
    PDThesaur TermU = new PDThesaur(PDSession);
    TermU.Load(Term.getUse());
    InfoTab.getCelda(1,2).AddElem(new Element("USE:"+TermU.getName()));
    }
out.println(InfoTab.ToHtml(Sess));
}
/** Returns a short description of the servlet.
*/
public String getServletInfo()
{
return "Servlet AJAX returning list of Term";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListTerm");
}

}
