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
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDMimeType;
import prodoc.PDReport;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class GenReportListFold extends SParent
{
/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
 * Diferent from Sparent because ContentType, OutputStream vs Printwriter ...
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException
*/
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
DriverGeneric PDSession = getSessOPD(Req); 
HttpSession Sess=Req.getSession(true);
String Type=Req.getParameter("Type");
String CurrFold=Req.getParameter("F");
String IdRep=Req.getParameter("IdRep");
//Vector<Record> ListRes=null;
Vector<Record> Res=null;
ArrayList<String> GeneratedRep=null;
PDReport Rep=new PDReport(PDSession);
Rep.LoadFull(IdRep);
if (Type.equalsIgnoreCase("Fold"))
    {
    PDFolders Fold=new PDFolders(PDSession);
    Conditions Conds=new Conditions();
    Condition Cond=new Condition(PDFolders.fPARENTID, Condition.cEQUAL, CurrFold);
    Conds.addCondition(Cond);
    Res = Fold.SearchV(PDFolders.getTableName(), Conds,  true, false, null, null);
    }
else
    {
    PDDocs Doc=new PDDocs(PDSession);
    Res=new Vector();
    Cursor CursorId = Doc.getListContainedDocs(CurrFold);
    Record Rec=PDSession.NextRec(CursorId);
    while (Rec!=null)
        {
        Res.add(Rec);
        Rec=PDSession.NextRec(CursorId);
        }
    PDSession.CloseCursor(CursorId);
    CursorId =null;
    }
GeneratedRep = Rep.GenerateRep(CurrFold, null, Res, Rep.getDocsPerPage(), Rep.getPagesPerFile(), getIO_OSFolder());
Res.clear();
String File2Send;
if (GeneratedRep.size()==1)
    {
    File2Send=GeneratedRep.get(0);
    PDMimeType mt=new PDMimeType(PDSession);
    mt.Load(Rep.getMimeType());
    response.setHeader("Content-disposition", "attachment; filename=" + Rep.getName());    
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
    response.setHeader("Content-disposition", "attachment; filename=" + File2Send +".zip");    
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
}
