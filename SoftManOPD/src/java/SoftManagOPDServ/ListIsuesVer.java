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
 * author: Joaquin Hierro      2020
 * 
 */

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ListIsuesVer extends SParent
{

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{   
out.println(GenListIssues(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ListIsuesVer Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListIssues(HttpServletRequest Req) 
{
StringBuilder TableIssues=new StringBuilder(5000);
String IdVers=Req.getParameter("IdVers");
TableIssues.append("<rows>");
DriverGeneric PDSession=getSessOPD(Req);
Attribute AttrD;
Cursor ListIssues=null;
try {
PDFolders Fold=new PDFolders(PDSession, getIssuesType());
String CurrentFold=PDFolders.ROOTFOLDER;
boolean SubFolders=false;
Condition C;
if (IdVers!=null)
    C=new Condition(PDFolders.fPARENTID , Condition.cEQUAL, IdVers);
else
    C=new Condition(PDFolders.fPDID , Condition.cNE, "z");
Conditions Cond=new Conditions();
Cond.addCondition(C);
ListIssues=Fold.Search( getIssuesType(), Cond, true, SubFolders, CurrentFold, null);
Record NextIssue=PDSession.NextRec(ListIssues);
String ProdId;
PDFolders TmpFold=new PDFolders(PDSession);
PDThesaur TmpTerm=new PDThesaur(PDSession);
while (NextIssue!=null)
    {  // Code,Title,Produc, Env, Status, Criticity, Solver, DateOpen, DateClosed
    AttrD=NextIssue.getAttr(PDFolders.fPDID);  
    ProdId=(String)AttrD.getValue();
    TableIssues.append("<row id=\"").append(ProdId).append("\">");       
    AttrD=NextIssue.getAttr("Code");
    TableIssues.append("<cell>").append(AttrD.Export()).append("</cell>");       
    AttrD=NextIssue.getAttr(PDFolders.fTITLE);
    TableIssues.append("<cell>").append(AttrD.Export()).append("</cell>");       
    TmpTerm.Load((String)NextIssue.getAttr("Env").getValue());
    TableIssues.append("<cell>").append(TmpTerm.getName()).append("</cell>");       
    TmpTerm.Load((String)NextIssue.getAttr("IssueStatus").getValue());
    TableIssues.append("<cell>").append(TmpTerm.getName()).append("</cell>");
    TmpTerm.Load((String)NextIssue.getAttr("IssueCrit").getValue());
    TableIssues.append("<cell>").append(TmpTerm.getName()).append("</cell>");
    AttrD=NextIssue.getAttr("DateOpen");
    TableIssues.append("<cell>").append(AttrD.Export()).append("</cell>");       
    AttrD=NextIssue.getAttr("DateClosed");
    TableIssues.append("<cell>").append(AttrD.Export()).append("</cell>");       
    String Solver=(String)NextIssue.getAttr("IssueSolver").getValue();
    if (Solver!=null && Solver.length()>0)
        {
        TmpTerm.Load(Solver);
        TableIssues.append("<cell>").append(TmpTerm.getName()).append("</cell></row>");
        }
    else
        TableIssues.append("<cell></cell></row>");
    NextIssue=PDSession.NextRec(ListIssues);
    }
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
finally
    {
    if (ListIssues!=null)    
        try {
        PDSession.CloseCursor(ListIssues);
        } catch (Exception e){}
    }
TableIssues.append("</rows>");
return(TableIssues.toString());
}
//-----------------------------------------------------------------------------------------------
}
