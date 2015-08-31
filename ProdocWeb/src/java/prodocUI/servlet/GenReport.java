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
 * author: Joaquin Hierro      2015
 * 
 */

package prodocUI.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDMimeType;
import prodoc.PDReport;
import static prodocUI.servlet.SParent.ShowMessage;
import static prodocUI.servlet.SParent.getSessOPD;

/**
 *
 * @author jhierrot
 */
public class GenReport extends SParent
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
DriverGeneric PDSession = getSessOPD(Req); 
HttpSession Sess=Req.getSession(true);
String Id=Req.getParameter("Id");
String Type=Req.getParameter("Type");
String FType=(String)Sess.getAttribute(SParent.SD_FType);
Conditions Conds=(Conditions)Sess.getAttribute(SParent.SD_Cond);
boolean SubT=(Boolean)Sess.getAttribute(SParent.SD_SubT);
boolean SubF=(Boolean)Sess.getAttribute(SParent.SD_SubF);
String actFolderId=(String)Sess.getAttribute(SParent.SD_actFolderId);
Vector Ord=(Vector)Sess.getAttribute(SParent.SD_Ord);
Cursor Cur=null;
if (Type.equalsIgnoreCase("Fold"))
    {
    PDFolders Fold;
    if (FType==null)
        Fold = new PDFolders(PDSession);
    else
        Fold = new PDFolders(PDSession, FType);
    Cur=Fold.Search(FType, Conds, SubT, SubF, actFolderId, Ord);
    }
else
    {
    PDDocs F;
    if (FType==null)
        F = new PDDocs(PDSession);
    else
        F = new PDDocs(PDSession, FType);
    boolean Vers=(Boolean)Sess.getAttribute(SParent.SD_Vers);
    String FTQuery=(String) Sess.getAttribute(SParent.SD_FTQ);
    Cur=F.Search(FTQuery, FType, Conds, SubT, SubF, Vers, actFolderId, Ord);
    }
PDReport Rep=new PDReport(PDSession);
Rep.LoadFull(Id);
ArrayList<String> GeneratedRep= Rep.GenerateRep(getActFolderId(Req), Cur, null, Rep.getDocsPerPage(), Rep.getPagesPerFile(), SParent.getIO_OSFolder());
String File2Send;
if (GeneratedRep.size()==1)
    {
    File2Send=GeneratedRep.get(0);
    PDMimeType mt=new PDMimeType(PDSession);
    mt.Load(Rep.getMimeType());
    response.setHeader("Content-disposition", "inline; filename=" + Rep.getName());    
    response.setContentType(mt.getMimeCode()+";charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    }
else
    {
    File2Send=GeneratedRep.get(0)+".zip";
    ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(File2Send));
    final int BUFFER = 64*1024;
    byte Buffer[] = new byte[BUFFER];
    for (String FileGen : GeneratedRep)
        {
        File f=new File(FileGen);
        FileInputStream in = new FileInputStream(FileGen);
        zout.putNextEntry(new ZipEntry(f.getName()));
        int len;
        while ((len = in.read(Buffer)) > 0) 
            zout.write(Buffer, 0, len);
        zout.closeEntry();
        in.close();
        f.delete();
        }
    zout.close();
    response.setHeader("Content-disposition", "inline; filename=" + File2Send +".zip");    
    response.setContentType("application/zip");
    }
FileInputStream in=null;
byte Buffer[]=new byte[64*1024];
ServletOutputStream out=null;
try {
out=response.getOutputStream();
in = new FileInputStream(File2Send);
int readed=in.read(Buffer);
while (readed!=-1)
    {
    out.write(Buffer, 0, readed);
    readed=in.read(Buffer);
    }
in.close();
out.flush();
out.close();
File f=new File(File2Send);
f.delete();
} catch (Exception e)
    {
    if (out!=null)
        out.close();
    if (in!=null)
        in.close();
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
return "GenReport Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("GenReport");
}
//-----------------------------------------------------------------------------------------------
}
