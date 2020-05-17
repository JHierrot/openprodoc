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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.PDException;
import prodoc.PDRoles;

/**
 *
 * @author jhierrot
 */
public class Menu extends SParent
{

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
/**
 *
 * @param Req
     * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws IOException
{
response.setContentType("text/xml;charset=UTF-8");
PrintWriter out = response.getWriter();
out.println(genMenu(Req));
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
return "Menu Servlet";
}
//-----------------------------------------------------------------------------------------------
private String genMenu(HttpServletRequest Req)
{
try {    
StringBuilder Men=new StringBuilder(2000);
Men.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><sidebar>");
Men.append("<item id=\"Products\" text=\"Products\" icon=\"img/icons8-software-installer-80.png\" selected=\"true\"/>");
Men.append("<item id=\"Departments\" text=\"Departments\" icon=\"img/icons8-genealogy-80.png\"/>");
Men.append("<item id=\"Issues\" text=\"Issues\" icon=\"img/icons8-high-importance-80.png\"/>");
Men.append("<item id=\"SoftCompanies\" text=\"Soft Companies\" icon=\"img/icons8-company-240.png\"/>");
Men.append("<item id=\"Help\" text=\"Help\" icon=\"img/icons8-help-80.png\"/>");
Men.append("<item id=\"About\" text=\"About\" icon=\"img/icons8-about-80.png\"/>");
Men.append("<item id=\"Exit\" text=\"Exit\" icon=\"img/icons8-exit-80.png\"/>");
Men.append("</sidebar>");
return(Men.toString());
} catch (Exception ex)
    {
    return ("<?xml version=\"1.0\"?><menu></menu>item id=\"Error\" text=\""+ex.getLocalizedMessage()+"\"></menu>");
    }
}
//-----------------------------------------------------------------------------------------------
}
