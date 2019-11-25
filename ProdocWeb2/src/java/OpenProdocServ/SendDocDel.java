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
 * author: Joaquin Hierro      2011
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDMimeType;
import prodoc.PDRepository;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SendDocDel extends SParent
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
DriverGeneric PDSession = getSessOPD(Req);    
String Id0=Req.getParameter("Id");     
String[] Id2 = Id0.split("\\|");
String Id=Id2[0];
String DocType=Id2[1];   
PDDocs doc=new PDDocs(getSessOPD(Req));
doc.LoadDeleted(DocType, Id);
PDRepository Rep=new PDRepository(PDSession);
Rep.Load(doc.getReposit());
if (Rep.IsRef())
    {
    String UrlTmp;    
    UrlTmp=doc.getName();      
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
        doc.getStreamDel(out);
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