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
import static OpenProdocUI.SParent.StoreDat;
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.*;
import prodoc.DriverGeneric;


/**
 *
 * @author jhierrot
 */
public class ImportRIS extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
DriverGeneric PDSession=SParent.getSessOPD(Req);
String CurrFold=Req.getParameter("F");
if (CurrFold!=null)
    {
    out.println(
    "[" +
    "{type: \"label\", label: \""+TT(Req, "Import_RIS")+"\"}," +
    "{type: \"combo\", name: \"RISType\", label: \""+TT(Req,"Document_Type")+"\", options:["+
    getComboModelDocRIS(PDSession, null)+"]},"+
    "{type: \"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
        "{type: \"newcolumn\", offset:20 }," +
        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}," +
        "{type: \"hidden\", name:\"CurrFold\", value: \""+CurrFold+"\"}]},"
    + "{type: \"fieldset\", label: \""+TT(Req, "Import_Doc")+"\", list:["+  
    "{type: \"upload\", name: \"UpFile\", titleText:\""+TT(Req, "Drag_n_Drop_file_or_click_icon_to_select_file")+"\", url: \"ImportDocRIS\", inputWidth: 350, autoStart: true, disabled:true }"+    
    "]},"              
    + " ];");
    }
else
    {
    HashMap <String, String>ListFields=new HashMap(); 
    ListFields.put("CurrFold", Req.getParameter("CurrFold"));
    ListFields.put("RISType", Req.getParameter("RISType")); 
    StoreDat(Req, ListFields);
    out.println("OK");
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
return "ImportRIS Servlet";
}
//-----------------------------------------------------------------------------------------------
}
