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
import prodoc.PDExceptionFunc;
import prodocUI.forms.FChangePass;
import static prodocUI.servlet.SParent.GenListForm;
import static prodocUI.servlet.SParent.LAST_FORM;
import static prodocUI.servlet.SParent.Reading;
import static prodocUI.servlet.SParent.ShowMessage;

/**
 *
 * @author jhierrot
 */
public class SPassChange extends SParent
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
    FChangePass f=new FChangePass(Req, FChangePass.ADDMOD, null, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    FChangePass f=new FChangePass(Req, FChangePass.ADDMOD, null, getUrlServlet() );
    String Acept=f.OkButton.getValue(Req);
    if (Acept!=null && Acept.length()!=0)
        {
        String OldPass;
        String NewPass1;
        String NewPass2;
        OldPass=f.OldPass.getValue(Req);
        NewPass1=f.NewPass1.getValue(Req);
        NewPass2=f.NewPass2.getValue(Req);
        if (!NewPass1.equals(NewPass2))
            PDExceptionFunc.GenPDException(TT(Req,"Both_new_passwords_different"), "");
        DriverGeneric sessOPD = SMain.getSessOPD(Req);
        sessOPD.ChangePassword(sessOPD.getUser().getName(), OldPass, NewPass1);
        }
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
return "Cambio de clave";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("SPassChange");
}
//-----------------------------------------------------------------------------------------------
}
