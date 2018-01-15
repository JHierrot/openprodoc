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
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.*;


/**
 *
 * @author jhierrot
 */
public class ImportThes extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
HashMap <String, String>ListFields=new HashMap(); 
ListFields.put("ThesNum", Req.getParameter("ThesNum"));
ListFields.put("ThesName", Req.getParameter("ThesName"));
ListFields.put("RootText", Req.getParameter("RootText"));
ListFields.put("MainLanguage", Req.getParameter("MainLanguage"));
ListFields.put("SubByLang", Req.getParameter("SubByLang"));
ListFields.put("Transact", Req.getParameter("Transact"));
ListFields.put("RetainCodes", Req.getParameter("RetainCodes"));
StoreDat(Req, ListFields);
out.println("OK");
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ImportThes Servlet";
}
//-----------------------------------------------------------------------------------------------
}
