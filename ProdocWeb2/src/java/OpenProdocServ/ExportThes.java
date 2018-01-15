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
 * author: Joaquin Hierro      2015
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.ShowMessage;
import static OpenProdocUI.SParent.TT;
import static OpenProdocUI.SParent.getSessOPD;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class ExportThes extends SParent
{
/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
 * Diferent from Sparent because ContentType, OutputStream vs Printwriter ...
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException
*/
@Override
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
try {
    ProcessPage(request, response);
} catch (Exception e)
    {
    PrintWriter out = response.getWriter();
    ShowMessage(request, out, e.getLocalizedMessage());
    e.printStackTrace();
    AddLog(e.getMessage());
    }
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req 
 * @param response
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, HttpServletResponse response) throws Exception
{
PrintWriter out=null;
String CurrThesId=Req.getParameter("CurrThesId");
if (CurrThesId!=null)
    {
    response.setContentType("text/html;charset=UTF-8");
    out = response.getWriter();
    out.println("[ {type: \"settings\", position: \"label-left\", labelWidth: 200}, ");
    out.println("{type: \"label\", label: \""+TT(Req, "Export_Thesaurus")+"\"},");
    out.println("{type: \"input\", name: \"RootText\", label: \""+TT(Req, "Root_Text")+"\", required: true, inputWidth: 240, maxLength:128},");
    out.println("{type: \"input\", name: \"MainLanguage\", label: \""+TT(Req, "Main_Language")+"\", required: true, inputWidth: 30, maxLength:6},");
    out.println("{type: \"hidden\", name:\"HideThesId\", value: \""+CurrThesId+"\"},");
    out.println("{type: \"block\", width: 250, list:[");
    out.println("{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"},");
    out.println("{type: \"newcolumn\", offset:20 },");
    out.println("{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}]} ];");
    out.close();
    }
else
    {
    response.setContentType("application/rdf+xml;charset=UTF-8"); 
    out = response.getWriter();   
    String Uri=Req.getParameter("RootText");
    String MainLang=Req.getParameter("MainLanguage");
    CurrThesId=Req.getParameter("HideThesId");
    response.setHeader("Content-disposition", "attachment; filename=\"Thes_"+CurrThesId+".rdf\"");
    try {
    DriverGeneric PDSession = getSessOPD(Req); 
    PDThesaur Thes=new PDThesaur(PDSession);
    Thes.Export(CurrThesId, out, Uri, MainLang);
    out.flush();
    out.close();
    } catch (Exception e)
        {
        if (out!=null)
            out.close();
        throw e;
        }
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
return "ExportThes Servlet";
}
//-----------------------------------------------------------------------------------------------
}
