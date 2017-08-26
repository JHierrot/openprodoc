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
 * author: Joaquin Hierro      2017
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.ShowMessage;
import static OpenProdocUI.SParent.getSessOPD;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDMimeType;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SendFoldXML extends SParent
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
DriverGeneric sessOPD = getSessOPD(Req);    
PDFolders Fold=new PDFolders(sessOPD);
String FoldId=Req.getParameter("Id");
Fold.LoadFull(FoldId);
ServletOutputStream out=response.getOutputStream();
response.setContentType("application/octet-stream");
response.setHeader("Content-disposition", "inline; filename=\"" + Fold.getPDId()+".opd\"");
response.setCharacterEncoding("UTF-8"); 
Cursor listContainedDocs = null;
try {
out.println("<OPDList>");
out.println(Fold.toXML());
PDDocs D1=new PDDocs(sessOPD);
listContainedDocs = D1.getListContainedDocs(FoldId);
Record NextRec = sessOPD.NextRec(listContainedDocs);
while (NextRec!=null)
    {
    PDDocs D=new PDDocs(sessOPD);
    D.LoadFull((String)NextRec.getAttr(PDDocs.fPDID).getValue());
    out.println(D.toXML(true));
    NextRec = sessOPD.NextRec(listContainedDocs);
    }
out.println("</OPDList>");
} catch (Exception e)
    {
    throw e;
    }
finally
    {
    out.close();
    if (listContainedDocs != null)
        sessOPD.CloseCursor(listContainedDocs);
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
return "SendFoldXML Servlet";
}
//-----------------------------------------------------------------------------------------------
}
