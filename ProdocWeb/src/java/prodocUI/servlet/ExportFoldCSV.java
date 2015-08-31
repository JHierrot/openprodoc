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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;
import static prodocUI.servlet.SParent.ShowMessage;

/**
 *
 * @author Joaquin
 */
public class ExportFoldCSV extends SParent
{
@Override
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
response.setCharacterEncoding("UTF-8");
PrintWriter PW = response.getWriter();
try {
HttpSession Sess=request.getSession(true);
PDFolders F = new PDFolders(getSessOPD(request));
PDThesaur UseTerm=new PDThesaur(getSessOPD(request));    
String FType=(String)Sess.getAttribute(SParent.SD_FType);
Conditions Cond=(Conditions)Sess.getAttribute(SParent.SD_Cond);
boolean SubT=(Boolean) Sess.getAttribute(SParent.SD_SubT);
boolean SubF=(Boolean) Sess.getAttribute(SParent.SD_SubF);
String actFolderId=(String) Sess.getAttribute(SParent.SD_actFolderId);
Vector Ord=(Vector)Sess.getAttribute(SParent.SD_Ord);
Cursor Res = F.Search(FType, Cond, SubT, SubF, actFolderId, Ord);
boolean HeaderWrite=false;
response.setContentType("text/csv; charset=UTF-8");
response.setHeader("Content-disposition", "inline; filename=" + "ExportFold_"+Long.toHexString(Double.doubleToLongBits(Math.random()))+".csv");
Record r=getSessOPD(request).NextRec(Res);
while (r!=null)
    {
    if (!HeaderWrite)
        {
        r.initList();
        for (int NumAt = 0; NumAt < r.NumAttr(); NumAt++)
            {    
            Attribute At=r.nextAttr(); 
            PW.print(At.getName());
            if (NumAt<r.NumAttr()-1)
               PW.print(";");
            }
        PW.println("");
        HeaderWrite=true;
        }
    r.initList();
    for (int NumAt = 0; NumAt < r.NumAttr(); NumAt++)
        {
        Attribute At=r.nextAttr(); 
        if (At.getType()==Attribute.tTHES)
            {
            if (At.getValue()!=null && ((String)At.getValue()).length()!=0)
                {
                try {
                UseTerm.Load((String)At.getValue());
                PW.print("\""+UseTerm.getName()+"\"");
                } catch (PDException ex)
                    {
                    PW.print("\"\"");
                    }
                }
            else
                PW.print("\"\"");
            }
        else
            PW.print(At.ToCSV());
        if (NumAt<r.NumAttr()-1)
           PW.print(";");
        }
    PW.println("");
    r=getSessOPD(request).NextRec(Res);
    }
response.flushBuffer();
PW.close();
} catch (Exception e)
    {
    ShowMessage(request, PW, e.getLocalizedMessage());
    AddLog(e.getMessage());
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
return "Export Fold results in CSV format Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ExportFoldCSV");
}
//-----------------------------------------------------------------------------------------------
}
