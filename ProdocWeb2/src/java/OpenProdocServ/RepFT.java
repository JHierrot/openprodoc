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
import static OpenProdocUI.SParent.PrepareError;
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class RepFT extends SParent
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
out.println(
"[ {type: \"settings\", offsetLeft:10, position: \"label-left\", labelWidth: 140}," +
"{type: \"label\", label: \""+TT(Req, "Cong.FT")+"\"}," +
"{type: \"combo\", label: \""+TT(Req, "GlobLang")+"\", name: \"GlobLang\", inputWidth:\"auto\", options:["+
"{text:\"*\", value:\"*\"},"+
"{text:\"English\", value:\"EN\"},"+
"{text:\"Español\", value:\"ES\"},"+
"{text:\"Português\", value:\"PT\"},"+
"{text:\"Català\", value:\"CT\"},"+
"]},"+
"{type: \"input\", name: \"SW\", label: \""+TT(Req, "StopWords_File")+"\", inputWidth: 120}," +
"{type: \"block\", width: 250, list:[" +
    "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
    "{type: \"newcolumn\", offset:20 }," +
    "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}" +
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
return "RepFT Servlet";
}
//-----------------------------------------------------------------------------------------------
}
