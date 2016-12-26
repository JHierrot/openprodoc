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
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.*;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDDocs;


/**
 *
 * @author jhierrot
 */
public class AddDoc extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req 
 * @param response
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDDocs TmpDoc=new PDDocs(PDSession);
String CurrFold=Req.getParameter("F");
if (CurrFold!=null)
    {
    Attribute AttrTit=TmpDoc.getRecord().getAttr(PDDocs.fTITLE);
    Attribute AttrDate=TmpDoc.getRecord().getAttr(PDDocs.fDOCDATE);
    out.println(
    "[  {type:\"settings\", offsetLeft:10, labelWidth: 150}," +
    "{type: \"label\", label: \""+TT(Req, "Add_Document")+"\"}," +
    GenInput(Req, AttrTit,  false, false)+        
    GenInput(Req, AttrDate,  false, false)+        
    "{type: \"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
        "{type: \"newcolumn\", offset:20 }," +
        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}," +
        "{type: \"hidden\", name:\"CurrFold\", value: \""+CurrFold+"\"}]},"
    + "{type: \"fieldset\", label: \""+TT(Req, "Import_Doc")+"\", list:["+  
    "{type: \"upload\", name: \"UpFile\", titleText:\""+TT(Req, "Drag_n_Drop_file_or_click_icon_to_select_file")+"\", url: \"ImportDocF\", inputWidth: 350, autoStart: true, disabled:true }"+    
    "]},"              
    + " ];");
    }
else
    {
    HashMap <String, String>ListFields=new HashMap(); 
    ListFields.put("CurrFold", Req.getParameter("CurrFold"));
    ListFields.put(PDDocs.fTITLE, Req.getParameter(PDDocs.fTITLE)); 
    ListFields.put(PDDocs.fDOCDATE, Req.getParameter(PDDocs.fDOCDATE)); 
    StoreDat(Req, ListFields);
    out.println("OK");
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
return "AddDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("AddDoc");
}
//-----------------------------------------------------------------------------------------------
}
