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
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jhierrot
 */
public class FormAttr extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{   
StringBuilder SB=new StringBuilder(1000);    
String Oper=Req.getParameter("Oper");
boolean ReadOnly;
if (Oper.equals("Delete"))
    ReadOnly=true;
else
    ReadOnly=false;
SB.append("[ {type: \"settings\", position: \"label-left\", labelWidth: 180},{type: \"label\", label: \"").append(TT(Req, "Attributes_Maintenance")).append("\"},");
SB.append("{type: \"input\", name: \"Name\", label: \"").append(TT(Req, "Name")).append("\",").append(ReadOnly||Oper.equalsIgnoreCase("Modif")?"readonly:1,":"").append(" required: true, inputWidth: 100, maxLength:32},");
SB.append("{type: \"input\", name: \"UserName\", label: \"").append(TT(Req, "Visible_Name_of_attribute")).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, inputWidth: 100, maxLength:32},");
SB.append("{type: \"input\", name: \"Descrip\", label: \"").append(TT(Req, "Description")).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, inputWidth: 250, maxLength:32},");
SB.append("{type: \"combo\", name: \"Type\", label: \"").append(TT(Req, "attribute_type")).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, inputWidth: 200, options:[");
SB.append("{text: \"Integer  \", value: \"Integer\"},");
SB.append("{text: \"Float    \", value: \"Float\"},");
SB.append("{text: \"String   \", value: \"String\"},");
SB.append("{text: \"Date     \", value: \"Date\"},");
SB.append("{text: \"Boolean  \", value: \"Boolean\"},");
SB.append("{text: \"TimeStamp\", value: \"TimeStamp\"},");
SB.append("{text: \"Thesaur  \", value: \"Thesaur\"}");
SB.append("]},");
SB.append("{type: \"input\", name: \"LongStr\", label: \"").append(TT(Req, "Length")).append("\",").append(ReadOnly?"readonly:1,":"").append("inputWidth: 50, maxLength:32},");
SB.append("{type: \"checkbox\", name: \"Req\", label: \"").append(TT(Req, "Required")).append("\",").append(ReadOnly?"readonly:1,":"").append("},");
SB.append("{type: \"checkbox\", name: \"UniKey\", label: \"").append(TT(Req, "Unique_value")).append("\",").append(ReadOnly?"readonly:1,":"").append("},");
SB.append("{type: \"checkbox\", name: \"ModAllow\", label: \"").append(TT(Req, "Modifiable")).append("\",").append(ReadOnly?"readonly:1,":"").append("},");
SB.append("{type: \"checkbox\", name: \"Multi\", label: \"").append(TT(Req, "MultiValued")).append("\",").append(ReadOnly?"readonly:1,":"").append("},");
SB.append("{type: \"block\", width: 250, list:[");
SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
SB.append(  "{type: \"newcolumn\", offset:20 },");
SB.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"}");
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
return "FormAttr Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("FormAttr");
}
//-----------------------------------------------------------------------------------------------
}
