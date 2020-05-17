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
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ImpactTree extends SParent
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
out.println(GenImpactTree(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ImpactTree Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenImpactTree(HttpServletRequest Req) 
{
StringBuilder DepTree=new StringBuilder(5000);
DepTree.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n").append("<tree id=\"0\">");
DriverGeneric PDSession=getSessOPD(Req);
String IdVersProdSec=Req.getParameter("IdVers");
try {
PDFolders F=new PDFolders(PDSession);
F.LoadFull(IdVersProdSec);
DepTree.append("<item id=\"").append(F.getPDId()).append("\" text=\"").append(F.getTitle()).append("\" open=\"1\">");
DepTree.append(SubImpactTree(PDSession, F.getPDId()));
DepTree.append("</item>");
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
DepTree.append("</tree>");
return(DepTree.toString());
}
//-----------------------------------------------------------------------------------------------

private StringBuilder SubImpactTree(DriverGeneric PDSession, String IdPrim) throws PDException
{
StringBuilder DepTree=new StringBuilder(5000);
PDFolders F=new PDFolders(PDSession);
PDThesaur T=new PDThesaur(PDSession);
Condition C=new Condition(DEPENDENCIES , Condition.cGET, IdPrim+REL_SEP);
Condition C1=new Condition(DEPENDENCIES , Condition.cLET, IdPrim+REL_SEP+"}");
Conditions Cond=new Conditions();
Cond.addCondition(C);
Cond.addCondition(C1);
Vector<Record> ListImpProdVers = F.SearchV(getProductsVersType(), Cond, true, false, PDFolders.ROOTFOLDER, null);
for (int i = 0; i < ListImpProdVers.size(); i++)
    {
    Record R = ListImpProdVers.elementAt(i);
//    F.LoadFull((String)ListImpProdVers.elementAt(i).getAttr(PDFolders.fPDID).getValue());
    DepTree.append("<item id=\"").append(Math.random()).append("\" text=\"").append((String)R.getAttr(PDFolders.fTITLE).getValue()).append("\" open=\"1\">");
    DepTree.append(SubImpactTree(PDSession, (String)R.getAttr(PDFolders.fPDID).getValue()));
    DepTree.append("</item>");    
    }

return(DepTree);
}
//-----------------------------------------------------------------------------------------------
}
