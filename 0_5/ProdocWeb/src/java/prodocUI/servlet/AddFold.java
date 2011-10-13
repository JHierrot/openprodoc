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
import prodoc.PDException;
import prodoc.PDFolders;
import prodocUI.forms.FMantFold;

/**
 *
 * @author jhierrot
 */
public class AddFold extends SParent
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
if (!Reading(Req))
    {
    FMantFold f=new FMantFold(Req, FMantFold.ADDMOD, null, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    String FoldTitle;
    FMantFold f=new FMantFold(Req, FMantFold.ADDMOD, null, getUrlServlet() );
    String Acept=f.OkButton.getValue(Req);
    if (Acept!=null && Acept.length()!=0)
        {
        FoldTitle=f.FoldTitle.getValue(Req);
        PDFolders Fold=new PDFolders(getSessOPD(Req));
        Fold.setPDId(getActFolderId(Req));
        String Id=Fold.CreateChild(FoldTitle);
        GenListForm(Req, out, LAST_FORM, "parent.Append('"+getActFolderId(Req)+"','"+Id+"','"+FoldTitle+"')", null);
        }
    else
        GenListForm(Req, out, LAST_FORM, null, null);
    } catch (PDException ex)
        {
        ShowMessage( Req,  out, SParent.TT(Req, ex.getLocalizedMessage()));
        }
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
return "Add Fold Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("AddFold");
}
//-----------------------------------------------------------------------------------------------
}
