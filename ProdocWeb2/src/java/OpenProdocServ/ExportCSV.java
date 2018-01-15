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
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ExportCSV extends SParent
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
response.setContentType("text/csv; charset=UTF-8");
response.setHeader("Content-disposition", "inline; filename=ResultFold_"+Long.toHexString(Double.doubleToLongBits(Math.random()))+".csv");
PrintWriter out = response.getWriter();
String Type=(String)Sess.getAttribute(SParent.SD_QType);
String FType=(String)Sess.getAttribute(SParent.SD_FType);
Conditions Conds=(Conditions)Sess.getAttribute(SParent.SD_Cond);
boolean SubT=(Boolean)Sess.getAttribute(SParent.SD_SubT);
boolean SubF=(Boolean)Sess.getAttribute(SParent.SD_SubF);
String actFolderId=(String)Sess.getAttribute(SParent.SD_actFolderId);
Vector Ord=(Vector)Sess.getAttribute(SParent.SD_Ord);
Cursor Cur=null;
try {
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
Record Rec=PDSession.NextRec(Cur);
if (Rec!=null)
    {
    StringBuilder Resp=new StringBuilder(1000);    
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        Resp.append(Attr.getUserName()).append(";");
        Attr=Rec.nextAttr();
        }
    Resp.deleteCharAt(Resp.length()-1);
    out.println(Resp);
    }
while (Rec!=null)
    {
    out.println(SParent.GenRowCSV(Req, Rec));    
    Rec=PDSession.NextRec(Cur);
    }
} catch (PDException ex)
    {
    out.println(ex.getLocalizedMessage());
    }
finally 
    {
    if (Cur!=null)
       PDSession.CloseCursor(Cur);
    out.close();
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
