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

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jhierrot
 */
public class FormSQL extends SParent
{
final static String ExampleFoldSQL="<b>Select</b> PDId, Title, FolderType, ACL <br> <b>from</b> PD_FOLDERS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
final static String HelpFoldSQL="<b>Select</b> PDId, Title, FolderType, ACL <br> <b>from</b> PD_FOLDERS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
final static String ExampleDocSQL="<b>Select</b> PDId, Title, DocType, ACL <br> <b>from</b> PD_DOCS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
final static String HelpDocSQL="<b>Select</b> PDId, Title, DocType, ACL <br> <b>from</b> PD_DOCS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
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
String ObjType=Req.getParameter("Type");  
boolean IsFold=false;
if (ObjType!=null && ObjType.equals("FOLD"))
    IsFold=true;
out.println("[" +
    "{type: \"label\", label: \""+TT(Req, IsFold?"Advanced_Folder_Search":"Advanced_Doc_Search")+"\"}," +
    "{type:\"editor\", name:\"SQLF\", label:\""+TT(Req, "OPD_SQL_Expresion")+"\", value:\""+(IsFold?ExampleFoldSQL:ExampleDocSQL)+"\", position:'label-top', offsetLeft:20, required:true, inputWidth:600, inputHeight:140}," +
    "{type:\"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
        "{type: \"newcolumn\", offset:20 }," +
        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}," +
        "{type: \"hidden\", name:\"CurrFold\", value: \"RootFolder\"}]},"+
    "{type:\"editor\", name:\"SQLJELP\", label:\""+TT(Req, "OPD_SQL_Help")+"\", value:\""+(IsFold?HelpFoldSQL:HelpDocSQL)+"\", disabled:true, position:'label-top', offsetLeft:20, inputWidth:600, inputHeight:240}" +
     "];");

}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "FormSQL Servlet";
}
//-----------------------------------------------------------------------------------------------
}
