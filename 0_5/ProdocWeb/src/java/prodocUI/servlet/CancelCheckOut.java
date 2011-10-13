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
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodocUI.forms.FMessageQuestion;

/**
 *
 * @author jhierrot
 */
public class CancelCheckOut  extends SParent
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
if (!Reading(Req))
    {
    FMessageQuestion f=new FMessageQuestion(Req, TT(Req, "Cancel_Checkout_Selected_Document"),  TT(Req, "Cancel_Checkout_Selected_Document"), TT(Req, "Do_you_want_to_cancel_edition_and_lost_changes"), getUrlServlet()+"?Read=1");
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    String Ok=Req.getParameter("BOk");
    if (Ok!=null)
        {
        DriverGeneric PDSession=SParent.getSessOPD(Req);
        PDDocs F = new PDDocs(PDSession);
        F.setPDId(getActDocId(Req));
        F.CancelCheckout();
        }
    GenListForm(Req, out, LAST_FORM, null, null);
    }
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
return "Cancel CheckOut Doc Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("CancelCheckOut");
}
//-----------------------------------------------------------------------------------------------
}
