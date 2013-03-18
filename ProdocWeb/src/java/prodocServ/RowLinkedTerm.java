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

import html.DelEntryListOPD;
import html.Element;
import html.Row;
import java.io.*;
import java.util.HashSet;
import javax.servlet.http.*;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.servlet.SParent;


/**
 *
 * @author jhierrot
 * @version
 */
public class RowLinkedTerm extends ServParent
{
//-----------------------------------------------------------------------------------------------
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
HttpSession Sess=Req.getSession(true);
// String Tab=(String)Req.getParameter("Tab");
String SearchTerm=(String)Req.getParameter("Term");
DriverGeneric PDSession=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
PDThesaur Term = new PDThesaur(PDSession);
Term.Load(SearchTerm);
Row R=new Row(5);
R.getCelda(0).AddElem(new Element(Term.getName()));
R.getCelda(1).AddElem(new Element(Term.getDescription()));
R.getCelda(3).AddElem(new Element(Term.getLang()));
R.getCelda(4).AddElem(new DelEntryListOPD("img/"+"del.png", SParent.TT(Req,"Delete"), "RT_T", Term.getPDId()));
if (Term.getUse()!=null && Term.getUse().length()!=0)
    {        
    Term.Load(Term.getUse());    
    R.getCelda(2).AddElem(new Element(Term.getName()));
    }
out.println(R.ToHtml(Sess));
}
/** Returns a short description of the servlet.
*/
public String getServletInfo()
{
return "Servlet AJAX returning ROW Term";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("RowLinkedTerm");
}

}
