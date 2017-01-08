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
import javax.servlet.ServletException;
import javax.servlet.http.*;
import prodoc.DriverGeneric;
import prodoc.PDTasksExecEnded;


/**
 *
 * @author jhierrot
 */
public class DelElem extends SParent
{
private static final String RESULT_UPFILE="RESULT_UPFILE";    
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
String Ids=Req.getParameter("ElemId");
DriverGeneric PDSession=SParent.getSessOPD(Req); 
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
String S="";
try {
String[] ListId = Ids.split(",");
for (int i = 0; i < ListId.length; i++)
    { 
    PDTasksExecEnded T=new PDTasksExecEnded(PDSession);
    T.setPDId(ListId[i]);
    T.delete();        
    }
} catch (Exception Ex)
        {
        S=Ex.getLocalizedMessage();
        }
StringBuilder Resp=new StringBuilder(200);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
Resp.append("<status>").append(S==null?"":S).append("</status>");
out.println(Resp.toString());
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
return "DelElem Servlet";
}
//-----------------------------------------------------------------------------------------------
}
