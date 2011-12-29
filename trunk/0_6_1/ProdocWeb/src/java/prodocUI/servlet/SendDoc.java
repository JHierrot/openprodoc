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

import java.io.IOException;
import java.io.OutputStream;
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
ServletOutputStream out=response.getOutputStream();
try {
// if (!Connected(request))
// an error is generated
    ProcessPage(request, out, response);
} catch (Exception e)
    {
    ShowMessage(request, out, e.getLocalizedMessage());
    e.printStackTrace();
    AddLog(e.getMessage());
    }
finally {
        if (out!=null)
            out.close();
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
protected void ProcessPage(HttpServletRequest Req, OutputStream out,  HttpServletResponse response) throws Exception
{
try {
out = response.getOutputStream();
PDDocs doc=new PDDocs(getSessOPD(Req));
String Id=Req.getParameter("Id");
doc.setPDId(Id);
String Ver=Req.getParameter("Ver");
if (Ver!=null && Ver.length()!=0)
    doc.LoadVersion(Id, Ver);
else
    doc.LoadCurrent(Id);
PDMimeType mt=new PDMimeType(getSessOPD(Req));
mt.Load(doc.getMimeType());
response.setContentType(mt.getMimeCode());
response.setHeader("Content-disposition", "inline; filename=" + doc.getName());
if (Ver!=null && Ver.length()!=0)
    doc.getStreamVer(out);
else
    doc.getStream(out);
} catch (Exception e)
    {
     if (out != null)
         out.close();
    throw e;
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
