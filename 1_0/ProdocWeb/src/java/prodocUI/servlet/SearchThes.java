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

// TODO Cambiar implementaciónArbol para situar desde búsqueda carpetas

package prodocUI.servlet;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SearchThes extends SParent
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
try {
Cursor TermsList=null;
if (Reading(Req)) // analize query
    {
    TermsList=SearchThes(Req);
    if (TermsList==null) // cancel
        {
        GenListThes(Req, out, LISTDOC_FORM, null, null);
        return;
        }
    }
else
    CleanCondsThes(Req); // directly from menu
GenListThes(Req, out, SEARCHTERM_FORM, null, TermsList);
} catch (PDException ex)
    {
    ShowMessage( Req,  out, SParent.TT(Req, ex.getLocalizedMessage()));
    }
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "SearchThes Servlet";
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @return
 */
static public String getUrlServlet()
{
return("SearchThes");
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 * @throws PDException
 */
private Cursor SearchThes(HttpServletRequest Req) throws PDException
{
String Acept=Req.getParameter("BOk");
if (Acept==null || Acept.length()==0)
    return(null);
DriverGeneric PDSession=getSessOPD(Req);
PDThesaur F = new PDThesaur(PDSession);
String SubTerms=Req.getParameter("SubTerms");
Conditions Cond=new Conditions();
Record Rec=F.getRecord();
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    String Val=Req.getParameter(Attr.getName());
    if (!(Val == null || Val.length()==0) )
        {
        Cond.addCondition(SParent.FillCondLike(Req, Attr, Val));
        }
    Attr=Rec.nextAttr();
    }
SaveCondsThes(Req, Cond, (SubTerms!=null), getActTermId(Req), null, Rec);
Cursor c=F.Search( Cond, (SubTerms!=null), getActTermId(Req), null);
return(c);
}
//-----------------------------------------------------------------------------------------------

}
