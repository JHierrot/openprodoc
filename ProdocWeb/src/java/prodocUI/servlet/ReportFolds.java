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
 * author: Joaquin Hierro      2015
 * 
 */
package prodocUI.servlet;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.PDException;
import prodocUI.forms.FMantFold;
import prodocUI.forms.FReportSel;
import static prodocUI.servlet.SParent.Reading;
import static prodocUI.servlet.SParent.ShowMessage;

/**
 *
 * @author Joaquin
 */
public class ReportFolds extends SParent
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
    FReportSel f=new FReportSel(Req, FReportSel.MODEFOLD, null, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
GenListForm(Req, out, LISTDOC_FORM, null, null);
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
return "Reports Folds in selected report format";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ReportFolds");
}
//-----------------------------------------------------------------------------------------------
}
