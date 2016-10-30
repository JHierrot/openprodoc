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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import prodoc.PDTasksExec;
import prodoc.PDTasksExecEnded;


/**
 *
 * @author jhierrot
 */
public class Relaunch extends SParent
{
private static final String RESULT_UPFILE="RESULT_UPFILE";    
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(200);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
try {
String Id=Req.getParameter("Id");
PDTasksExecEnded TE=new PDTasksExecEnded(getSessOPD(Req));
PDTasksExec TNew=new PDTasksExec(getSessOPD(Req));
TE.Load(Id); 
TNew.assignValues(TE.getRecord());
TNew.setPDId(TE.GenerateId());  //avoid repeating Id
TNew.setNextDate(new Date());
TNew.insert();
Resp.append("<status>").append(TT(Req, "Task_relaunched")).append("</status>");
out.println(Resp.toString());
} catch (Exception Ex)
        {
        Resp.append("<status>").append(Ex.getLocalizedMessage()).append("</status>");
        out.println(Resp.toString());
        }
finally {
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
return "Relaunch Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("Relaunch");
}
//-----------------------------------------------------------------------------------------------
}
