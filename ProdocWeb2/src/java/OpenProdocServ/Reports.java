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
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class Reports extends SParent
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
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDFolders TmpFold=new PDFolders(PDSession);
StringBuilder SB=new StringBuilder(2000);
Attribute Attr;
Attr=TmpFold.getRecord().getAttr(PDFolders.fTITLE);
SB.append("[");
SB.append("{type: \"label\", label: \"").append(TT(Req, "Reports")).append("\"},");
SB.append("{type: \"combo\", name: \"Report\", label: \"").append(TT(Req, "Reports")).append("\",").append(" required: true, options:[");
SB.append(getComboReports(PDSession,null));
SB.append("]},");
SB.append("{type: \"block\", width: 250, list:[");
SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
SB.append("{type: \"newcolumn\", offset:20 },");
SB.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
SB.append("]} ];");
out.println(SB);
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "AddFold Servlet";
}
//-----------------------------------------------------------------------------------------------
}
