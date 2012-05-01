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
 * author: Joaquin Hierro      2011
 * 
 */

package prodocUI.servlet;

import html.Page;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.PDDocs;
import prodoc.PDMimeType;

/**
 *
 * @author jhierrot
 */
public class SendDoc extends SParent
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
 * @param out 
 * @param response
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, HttpServletResponse response) throws Exception
{
PDDocs doc=new PDDocs(getSessOPD(Req));
String Id=Req.getParameter("Id");
doc.setPDId(Id);
String Ver=Req.getParameter("Ver");
if (Ver!=null && Ver.length()!=0)
    doc.LoadVersion(Id, Ver);
else
    doc.LoadCurrent(Id);
if (doc.IsUrl())
    {
    PrintWriter out = response.getWriter();
    HttpSession Sess=Req.getSession(true);
    String HeadRefresh="<META http-equiv=\"refresh\" content=\"0; URL=";    
    if (Ver!=null && Ver.length()!=0)
        HeadRefresh+=doc.getUrlVer(Ver) +"\">";    
    else
        HeadRefresh+=doc.getUrl() +"\">";            
    Page p= new Page(Req, "Opening....", HeadRefresh);
    out.println(p.ToHtml(Sess));
    out.close();
    }
else
    {
    ServletOutputStream out=response.getOutputStream();
    PDMimeType mt=new PDMimeType(getSessOPD(Req));
    mt.Load(doc.getMimeType());
    response.setContentType(mt.getMimeCode());
    response.setHeader("Content-disposition", "inline; filename=" + doc.getName());
    try {
    if (Ver!=null && Ver.length()!=0)
        doc.getStreamVer(out);
    else
        doc.getStream(out);
    } catch (Exception e)
        {
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
return "SendDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("SendDoc");
}
//-----------------------------------------------------------------------------------------------
}
