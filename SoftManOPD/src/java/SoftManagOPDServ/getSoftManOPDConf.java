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


import Config.SoftManOPDConfig;
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
public class getSoftManOPDConf extends SParent
{
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
try (PrintWriter out = response.getWriter()) {
StringBuilder Resp=new StringBuilder(200);
Resp.append("<Conf>").append(getSoftManConf(Req).getJSON()).append("</Conf>");
out.println(Resp.toString());
} catch (Exception Ex)
    {
    Ex.printStackTrace();
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
return "getSoftManOPDConf Servlet";
}
//-----------------------------------------------------------------------------------------------
}
