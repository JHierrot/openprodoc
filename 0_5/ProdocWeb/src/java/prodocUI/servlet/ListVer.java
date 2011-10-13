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
import prodoc.Cursor;
import prodoc.PDDocs;
import prodocUI.forms.FListVer;

/**
 *
 * @author jhierrot
 */
public class ListVer extends SParent
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
PDDocs d=new PDDocs(SMain.getSessOPD(Req) );
d.Load(SMain.getActDocId(Req));
Cursor Lver=d.ListVersions( d.getDocType(), d.getPDId());
FListVer f=new FListVer(Req, FListVer.ADDMOD, d.getRecord(), getUrlServlet(), Lver);
out.println(f.ToHtml(Req.getSession()));
return;
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ListVer";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListVer");
}
//-----------------------------------------------------------------------------------------------
}
