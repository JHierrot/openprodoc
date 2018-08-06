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
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import javax.servlet.http.*;
import prodoc.DriverGeneric;


/**
 *
 * @author jhierrot
 */
public class FormEPerm extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
DriverGeneric PDSession=SParent.getSessOPD(Req);
StringBuilder SB=new StringBuilder(2000);
String Type=Req.getParameter("Type");
String Oper=Req.getParameter("Oper");
String Id=Req.getParameter("Id");
SB.append("[{type: \"label\", label: \"").append(TT(Req, Oper.equalsIgnoreCase("New")?"Add_Permission":"Update_Permission")).append("\"},");  
SB.append("{type: \"block\", list:[" );
if (Oper.equalsIgnoreCase("New"))            
    {
    SB.append("{type: \"combo\", name: \"").append(Type).append("\", label: \"").append(TT(Req, Type)).append("\", readonly:1, options:[");
    SB.append(getComboModel(Type,PDSession, null) );
    SB.append("]},");
    }
else
    {
    SB.append("{type: \"input\", name: \"").append(Type).append("\", readonly: \"true\",value:\"").append(Id).append("\"}, ");
    }
SB.append("{type: \"newcolumn\", offset:20 },");
SB.append("{type: \"combo\", label: \"").append(TT(Req, "Permission")).append("\", name: \"Permission\", readonly:1, inputWidth:\"auto\", options:[");
SB.append("{text: \"READ\", value: \"1\", selected: true},");
SB.append("{text: \"UPDATE\", value: \"4\"},");
SB.append("{text: \"DELETE\", value: \"5\"}");
SB.append("]}");
SB.append("]},");
SB.append("{type: \"block\", width: 250, list:[" );
SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
SB.append("{type: \"newcolumn\", offset:20 },");
SB.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
SB.append("]} ];");
out.println(SB.toString());
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "FormEPerm Servlet";
}
//-----------------------------------------------------------------------------------------------
}
