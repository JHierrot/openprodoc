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
import prodoc.PDException;
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.forms.FMantThes;

/**
 *
 * @author jhierrot
 */
public class DelThes extends SParent
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
DriverGeneric PDSession=SParent.getSessOPD(Req)  ;
PDThesaur F = new PDThesaur(PDSession);
F.Load(getActTermId(Req));
if (!Reading(Req))
    {
    Record Rec=F.getRecord();
    FMantThes f=new FMantThes(Req, FMantThes.DELMOD, Rec, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    boolean Acept=DelTerm(Req);
    if (Acept)
        {
        SParent.setActTermId(Req, F.getParentId());
        GenListThes(Req, out, LAST_FORM, "parent.ThesDelete('"+F.getPDId()+"')", null);
        }
    else
        GenListThes(Req, out, LAST_FORM, null, null);
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
return "DelThes Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("DelThes");
}
//-----------------------------------------------------------------------------------------------
private boolean DelTerm(HttpServletRequest Req) throws PDException
{
String Acept=Req.getParameter("BOk");
if (Acept==null || Acept.length()==0)
    return(false);
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDThesaur F = new PDThesaur(PDSession);
F.setPDId(getActTermId(Req));
F.delete();
return(true);
}
//-----------------------------------------------------------------------------------------------
}
