/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU Affero GPL v3 http://www.gnu.org/licenses/agpl.html
 * 
 * you may not use this file except in compliance with the License.  
 * Unless agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDException;

/**
 *
 * @author jhierrot
 */
public class PassChange extends SParent
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
String OldPass=Req.getParameter("OldPass");
if (OldPass==null)
    {
    out.println(
    "[ {type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 150}," +
    "{type: \"label\", label: \""+TT(Req, "Password_change")+"\"}," +
    "{type: \"password\", name: \"OldPass\", label: \""+TT(Req, "Current_Password")+"\", required: true}," +
    "{type: \"password\", name: \"NewPass\", label: \""+TT(Req, "New_Password")+"\", required: true}," +
    "{type: \"password\", name: \"NewPass2\", label: \""+TT(Req, "password_retype")+"\", required: true}," +    
    "{type: \"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
        "{type: \"newcolumn\", offset:20 }," +
        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}," +
        "{type: \"hidden\", name:\"CurrFold\", value: \""+OldPass+"\"}" +
    "]} ];");
    }
else
    {
    try {    
    String NewPass1=Req.getParameter("NewPass");    
    String NewPass2=Req.getParameter("NewPass2");    
    if (!NewPass1.equals(NewPass2))
        {
        out.println(TT(Req,"Both_new_passwords_different"));
        return;
        }
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    PDSession.ChangePassword(PDSession.getUser().getName(), OldPass, NewPass1);
    out.println("OK");
    } catch (PDException Ex)
        {
        PrepareError(Req, Ex.getLocalizedMessage(), out);
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
return "PassChange Servlet";
}
//-----------------------------------------------------------------------------------------------
}
