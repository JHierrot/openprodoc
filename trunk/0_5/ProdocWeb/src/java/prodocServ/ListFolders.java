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

package prodocServ;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.http.*;
import prodoc.DriverGeneric;
import prodoc.PDFolders;


/**
 *
 * @author jhierrot
 * @version
 */
public class ListFolders extends ServParent
{
//-----------------------------------------------------------------------------------------------
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
out.println("<ListFold>");
HttpSession Sess=Req.getSession(true);
String Id=(String)Req.getParameter("Id");
DriverGeneric PDSession=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
PDFolders RootFolder = new PDFolders(PDSession);
HashSet Child =RootFolder.getListDirectDescendList(Id);
for (Iterator it = Child.iterator(); it.hasNext();)
    {
    String ChildId=(String)it.next();
     if (ChildId.compareTo(PDFolders.ROOTFOLDER)==0)
        continue;
    PDFolders ChildFolder=new PDFolders(PDSession);
    ChildFolder.Load(ChildId);
    out.println("<Fold><id>"+ChildFolder.getPDId()+"</id>");
    out.println("<name>"+ChildFolder.getTitle()+"</name></Fold>");
     }
out.println("</ListFold>");
}
/** Returns a short description of the servlet.
*/
public String getServletInfo()
{
return "Servlet AJAX returning list of folders";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListFolders");
}

}
