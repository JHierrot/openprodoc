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
 * author: Joaquin Hierro      2019
 * 
 */

package SoftManagOPDServ;


import SoftManagOPDUI.SParent;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.DriverGeneric;

/**
 *
 * @author jhierrot
 */
public class LocaleTrans extends SParent
{
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
try {
String S=null;
StringBuilder Resp=new StringBuilder(200);
DriverGeneric PDSession=SParent.getSessOPD(Req);
String Par=Req.getParameter("Par");
if (Par.equals("_Help"))
    S="help/"+PDSession.getHelpLang(PDSession.getUser().getCustomData().getLanguage())+"/MainWin.html";
else if (Par.equals("_User"))
    S="@"+PDSession.getUser().getName()+" ( "+PDSession.getUser().getDescription()+" )";
else
    S=TT(Req, Par);
Resp.append("<tr>").append(S).append("</tr>");
out.println(Resp.toString());
} catch (Exception Ex)
    {

    }
finally 
    {
    out.close();
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
return "LocaleTrans Servlet";
}
//-----------------------------------------------------------------------------------------------
}
