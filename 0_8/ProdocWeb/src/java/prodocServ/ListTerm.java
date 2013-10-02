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
import prodoc.PDThesaur;


/**
 *
 * @author jhierrot
 * @version
 */
public class ListTerm extends ServParent
{
//-----------------------------------------------------------------------------------------------
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
out.println("<ListTerm>");
HttpSession Sess=Req.getSession(true);
String Id=(String)Req.getParameter("Id");
DriverGeneric PDSession=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
PDThesaur RootFolder = new PDThesaur(PDSession);
HashSet Child =RootFolder.getListDirectDescendList(Id);
for (Iterator it = Child.iterator(); it.hasNext();)
    {
    String ChildId=(String)it.next();
     if (ChildId.compareTo(PDThesaur.ROOTTERM)==0)
        continue;
    PDThesaur ChildFolder=new PDThesaur(PDSession);
    ChildFolder.Load(ChildId);
    out.println("<Term><id>"+ChildFolder.getPDId()+"</id>");
    out.println("<name>"+ChildFolder.getName()+"</name></Term>");
     }
out.println("</ListTerm>");
}
/** Returns a short description of the servlet.
*/
public String getServletInfo()
{
return "Servlet AJAX returning list of Term";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListTerm");
}

}
