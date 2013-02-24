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

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
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
public class SearchTerm extends ServParent
{
//-----------------------------------------------------------------------------------------------
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
out.println("<SearchTerm>");
HttpSession Sess=Req.getSession(true);
String IdThes=(String)Req.getParameter("IdThes");
String SearchTerm=(String)Req.getParameter("SearchTerm");
DriverGeneric PDSession=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
PDThesaur Thes = new PDThesaur(PDSession);
Condition Cond=new Condition(PDThesaur.fNAME, Condition.cLIKE, SearchTerm);
Conditions Conds=new Conditions();
Conds.addCondition(Cond);
Cursor Child =Thes.Search(Conds, true, IdThes, null);
Record Term=PDSession.NextRec(Child);
while (Term!=null)
    {
    out.println("<Term><id>"+Term.getAttr(PDThesaur.fPDID).getValue()+"</id>");
    out.println("<name>"+Term.getAttr(PDThesaur.fNAME).getValue()+"</name></Term>");
    Term=PDSession.NextRec(Child);
    }
out.println("</SearchTerm>");
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
