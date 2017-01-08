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
import prodoc.Attribute;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDTrace;


/**
 *
 * @author jhierrot
 */
public class DelLogs extends SParent
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
String Filter=Req.getParameter("F");
DriverGeneric PDSession=SParent.getSessOPD(Req); 
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
String S="";
Date D1=null;
Date D2=null;
try {
String ObjType=Req.getParameter("Cat");
String SFec1=Req.getParameter("Fec1");
if (SFec1!=null && SFec1.length()!=0)
    D1=new Date(Long.parseLong(SFec1));
String SFec2=Req.getParameter("Fec2");
if (SFec2!=null && SFec2.length()!=0)
    D2=new Date(Long.parseLong(SFec2));
PDTrace TraceLogs=new PDTrace(PDSession);
TraceLogs.DeleteRange(ObjType, D1, D2);
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
return "DelLogs Servlet";
}
//-----------------------------------------------------------------------------------------------
}
