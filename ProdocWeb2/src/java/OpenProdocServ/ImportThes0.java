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
import javax.servlet.http.*;


/**
 *
 * @author jhierrot
 */
public class ImportThes0 extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
out.println("[ {type: \"settings\", position: \"label-left\", labelWidth: 200}, ");
out.println("{type: \"label\", label: \""+TT(Req, "Import_Thesaurus")+"\"},");
out.println("{type: \"input\", name: \"ThesNum\", label: \""+TT(Req, "Thesaurus_Number")+"\", required: true, inputWidth: 70, maxLength:32},");
out.println("{type: \"input\", name: \"ThesName\", label: \""+TT(Req, "Thesaurus_Name")+"\", required: true, inputWidth: 240, maxLength:128},");
out.println("{type: \"input\", name: \"RootText\", label: \""+TT(Req, "Root_Text")+"\", required: true, inputWidth: 240, maxLength:200},");
out.println("{type: \"input\", name: \"MainLanguage\", label: \""+TT(Req, "Main_Language")+"\", required: true, inputWidth: 30, maxLength:6},");
//    out.println("{type: \"upload\", name: \"ImportThes\", label: \""+TT(Req, "Import_Doc")+"\", required: true, mode:\"html4\", url:\"ImportThes\"},");
out.println("{type: \"checkbox\", name: \"SubByLang\", label: \""+TT(Req, "Create_SubThes_by_Lang")+"\"},");
out.println("{type: \"checkbox\", name: \"Transact\", label: \""+TT(Req, "Transactional_Import")+"\", checked:true},");
out.println("{type: \"checkbox\", name: \"RetainCodes\", label: \""+TT(Req, "Retain_Codes")+"\"},");
out.println("{type: \"block\", width: 250, list:[");
out.println("{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"},");
out.println("{type: \"newcolumn\", offset:20 },");
out.println("{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}]}"
        +",{type: \"upload\", name: \"UpFile\", url: \"ImportThesF\", autoStart: true, disabled:true }"
        + " ];");
out.close();
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ImportThes0 Servlet";
}
//-----------------------------------------------------------------------------------------------
}
