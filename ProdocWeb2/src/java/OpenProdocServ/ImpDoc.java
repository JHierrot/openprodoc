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
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import javax.servlet.http.*;


/**
 *
 * @author jhierrot
 */
public class ImpDoc extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
String FoldId=Req.getParameter("F");   
setActFoldId(Req, FoldId);
out.println("[" +
    "{type: \"fieldset\", label: \""+TT(Req, "Import_Doc")+"\", offsetLeft:15, list:["+  
    "{type: \"upload\", name: \"UpFile\", titleText:\""+TT(Req, "Drag_n_Drop_file_or_click_icon_to_select_file")+"\", url: \"ImpDocXML\", inputWidth: 350, autoStart: true }"+    
    "]},"+  
    "{type: \"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}" +
//        "{type: \"newcolumn\", offset:20 }," +
//        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}" +
    "]} ];");
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ImpDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
}
