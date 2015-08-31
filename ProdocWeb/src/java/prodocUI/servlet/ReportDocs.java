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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.forms.FMantFold;
import prodocUI.forms.FReportSel;
import static prodocUI.servlet.ReportFolds.getUrlServlet;
import static prodocUI.servlet.SParent.GenListForm;
import static prodocUI.servlet.SParent.LISTDOC_FORM;
import static prodocUI.servlet.SParent.Reading;
import static prodocUI.servlet.SParent.ShowMessage;
import static prodocUI.servlet.SParent.getSessOPD;

/**
 *
 * @author Joaquin
 */
public class ReportDocs extends SParent
{
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
try {
if (!Reading(Req))
    {
    FReportSel f=new FReportSel(Req, FReportSel.MODEDOC, null, getUrlServlet());
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
return "Reports ocs in selected report format";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ReportDocs");
}
//-----------------------------------------------------------------------------------------------
}
