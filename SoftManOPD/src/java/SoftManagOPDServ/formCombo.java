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

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class formCombo extends SParent
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
DriverGeneric PDSession=getSessOPD(Req);
String Active=Req.getParameter("A");
PDFolders TmpFold=new PDFolders(PDSession);
StringBuilder Form= new StringBuilder(3000);
Attribute Attr;
Form.append("[");
Attr=TmpFold.getRecord().getAttr(PDFolders.fFOLDTYPE);
Form.append("{type: \"combo\", name: \"" + PDFolders.fFOLDTYPE + "\", offsetLeft:10, label: \"").append(TT(Req, Attr.getUserName())).append("\", required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", readonly:1, options:[");
Form.append(getComboModelFold(PDSession, null, Active!=null) );
Form.append("]} ];");
out.println(Form.toString());
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "formCombo Servlet";
}
//-----------------------------------------------------------------------------------------------
}
