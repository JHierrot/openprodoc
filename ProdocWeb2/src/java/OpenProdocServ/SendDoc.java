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

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.FileInputStream;
import java.io.IOException;
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
static final int TAMBUFF=1024*64;    
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
if (!Connected(request))
    {
    PrintWriter out = response.getWriter();    
    AskLogin(request, out);
    }
else    
    ProcessPage(request, response);
} catch (Exception e)
    {
    PrintWriter out = response.getWriter();
    ShowMessage(request, out, e.getLocalizedMessage());
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
PDDocs doc=new PDDocs(getSessOPD(Req));
String Id=Req.getParameter("Id");
doc.setPDId(Id);
String Ver=Req.getParameter("Ver");
if (Ver!=null && Ver.length()!=0)
    doc.LoadVersion(Id, Ver);
else
    {
    if (Id.contains("|"))
        {
        String[] Id2 = Id.split("\\|");
        Id=Id2[0];
        Ver=Id2[1];
        doc.LoadVersion(Id, Ver);
        }
    else    
        doc.LoadCurrent(Id);
    }
if (doc.IsUrl())
    {
    String UrlTmp;    
    if (Ver!=null && Ver.length()!=0)
        UrlTmp=doc.getUrlVer(Ver);    
    else
        UrlTmp=doc.getUrl();   
    if (UrlTmp.substring(0,4).equalsIgnoreCase("http"))
        {
        response.setStatus(302);
        response.setHeader("Location", UrlTmp);
        response.setHeader("Connection", "close");
        }
    else
        {
        ServletOutputStream out=response.getOutputStream();
        PDMimeType mt=new PDMimeType(getSessOPD(Req));
        mt.Load(doc.getMimeType());
        response.setContentType(mt.getMimeCode());
        response.setHeader("Content-disposition", "inline; filename=\"" + doc.getName()+"\"");
        response.setCharacterEncoding("UTF-8"); // just for text family docs
        FileInputStream Is=null;
        byte Buffer[]=new byte[TAMBUFF];
        try {
        Is=new FileInputStream(UrlTmp);
        int readed=Is.read(Buffer);
        while (readed!=-1)
            {
            out.write(Buffer, 0, readed);
            readed=Is.read(Buffer);
            }
        Is.close();
        out.flush();
        } catch (Exception e)
            {
            throw e;
            }
        finally
            {
            if (Is!=null)    
                Is.close();    
            out.close();        
            }        
        }
    }
else
    {
    ServletOutputStream out=response.getOutputStream();
    PDMimeType mt=new PDMimeType(getSessOPD(Req));
    mt.Load(doc.getMimeType());
    response.setContentType(mt.getMimeCode());
    response.setHeader("Content-disposition", "inline; filename=\"" + doc.getName()+"\"");
    response.setCharacterEncoding("UTF-8"); // just for text family docs
    try {
    if (Ver!=null && Ver.length()!=0)
        doc.getStreamVer(out);
    else
        doc.getStream(out);
    } catch (Exception e)
        {
        throw e;
        }
    finally
        {
        out.close();        
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
}
