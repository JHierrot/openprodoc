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
import Sessions.CurrentSession;
import Sessions.PoolSessions;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.DriverGeneric;

/**
 *
 * @author jhierrot
 */
public class ListLogedUsers extends SParent
{
static final String FORMATTS="yyyy-MM-dd HH:mm:ss";
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
     * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
DriverGeneric PDSession=SParent.getSessOPD(Req);
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(3000);
Resp.append("<rows>");
try {
PoolSessions.InitList();
int i=0;
SimpleDateFormat formatterTS = new SimpleDateFormat(FORMATTS);
CurrentSession CS=null;
CS=PoolSessions.NextList();
while (CS!=null)
    {
    Resp.append("<row id=\"").append(i++).append("\">");
    Resp.append("<cell>").append(CS.getHost()).append("</cell>");
    Resp.append("<cell>").append(CS.GetMod()).append("</cell>");
    Resp.append("<cell>").append(CS.getUserName()).append("</cell>");
    Resp.append("<cell>").append(formatterTS.format(CS.getLoginTime())).append("</cell>");
    Resp.append("<cell>").append(formatterTS.format(CS.getLastUse())).append("</cell>");
    Resp.append("</row>");
    CS=PoolSessions.NextList();
    }
Resp.append("</rows>");
} catch (Exception ex)
    {
    Resp.append("<LV>Error</LV></row>");
    }
out.println(  Resp );   
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
return "ListLogedUsers Servlet";
}
//-----------------------------------------------------------------------------------------------
}
