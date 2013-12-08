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
import prodocUI.forms.FCheckIn;

/**
 *
 * @author jhierrot
 */
public class CheckIn extends SParent
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
    FCheckIn f=new FCheckIn(Req, FCheckIn.ADDMOD, null, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    CheckIn(Req);
    GenListForm(Req, out, LAST_FORM, null, null);
//    FListDocs fm=new FListDocs(Req, getActFolderId(Req));
//    out.println(fm.ToHtml(Req.getSession()));
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
return "CheckIn Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("CheckIn");
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 * @throws PDException
 */
private boolean CheckIn(HttpServletRequest Req) throws PDException
{
String Acept=Req.getParameter("BOk");
if (Acept==null || Acept.length()==0)
    return(false);
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDDocs F = new PDDocs(PDSession);
F.setPDId(getActDocId(Req));
String Ver=Req.getParameter(PDDocs.fVERSION);
F.Checkin(Ver);
return(true);
}
//-----------------------------------------------------------------------------------------------
}
