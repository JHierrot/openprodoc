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
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jhierrot
 */
public class ElemFilter extends SParent
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
StringBuilder Form= new StringBuilder(3000);
String TypeElem=Req.getParameter("TE");
Form.append("[{type: \"settings\", labelWidth: 140},");
Form.append("{type: \"block\", width: 450, list:[");
if (TypeElem.equals("PendTaskLog") || TypeElem.equals("EndTaskLogs") || TypeElem.equals("TraceLogs"))
    {
    SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String Now = formatterTS.format(new Date(System.currentTimeMillis()-60000));
    if (TypeElem.equals("TraceLogs"))    
        Form.append("{type: \"input\", name: \"DocType\", label: \"").append(TT(Req, "Document_Type")).append("\", tooltip:\"").append(TT(Req, "Enter_full_or_partial_name_of_the_item")).append("\"},");
    else    
        Form.append("{type: \"input\", name: \"Category\", label: \"").append(TT(Req, "Category")).append("\", tooltip:\"").append(TT(Req, "Enter_full_or_partial_name_of_the_item")).append("\"},");
    Form.append("{type: \"calendar\", name: \"Fec1\", label: \"").append(TT(Req, "Timestamp_from")).append("\", dateFormat: \"%Y-%m-%d %H:%i:%s\", calendarPosition: \"right\", value:\""+Now+"\"},");
    Form.append("{type: \"calendar\", name: \"Fec2\", label: \"").append(TT(Req, "Timestamp_to")).append("\", dateFormat: \"%Y-%m-%d %H:%i:%s\", calendarPosition: \"right\"},");
    }
else
    Form.append("{type: \"input\", name: \"filter\", label: \"").append(TT(Req, "Filter")).append("\", tooltip:\"").append(TT(Req, "Enter_full_or_partial_name_of_the_item")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"}");
Form.append("]}];");
out.println(Form.toString());
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ElemFilter Servlet";
}
//-----------------------------------------------------------------------------------------------
}
