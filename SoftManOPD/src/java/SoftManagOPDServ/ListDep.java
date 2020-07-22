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
 * author: Joaquin Hierro      2019
 * 
 */

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ListDep extends SParent
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
out.println(GenListDoc(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ListVers Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListDoc(HttpServletRequest Req) 
{
StringBuilder ListProducts=new StringBuilder(5000);
ListProducts.append("<rows>");
DriverGeneric PDSession=getSessOPD(Req);
Attribute AttrD;
try {
PDFolders Fold=new PDFolders(PDSession, getProductsVersType(Req));
PDFolders FoldRelProdVer=new PDFolders(PDSession, getProductsVersType(Req));
PDFolders FoldRelParent=new PDFolders(PDSession, getProductType(Req));
PDThesaur TermTmp=new PDThesaur(PDSession);
String IdVers=Req.getParameter("IdVers");
Fold.LoadFull(IdVers);
Record Rec = Fold.getRecSum();
Attribute Attr = Rec.getAttr(DEPENDENCIES);
TreeSet<String> ListDep = Attr.getValuesList();
for (String Dep : ListDep)
    {
    String[] split = Dep.split(REL_SEP);
    String ProdVers=split[0];
    String Relation=split[1];
    FoldRelProdVer.Load(ProdVers);
    FoldRelParent.Load(FoldRelProdVer.getParentId());
    TermTmp.Load(Relation);
    ListProducts.append("<row id=\"").append(Dep).append("\">");       
    ListProducts.append("<cell>").append(FoldRelParent.getTitle()).append("</cell>");       
    ListProducts.append("<cell>").append(FoldRelProdVer.getTitle()).append("</cell>");       
    ListProducts.append("<cell>").append(TermTmp.getName()).append("</cell></row>");       
    }
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
ListProducts.append("</rows>");
return(ListProducts.toString());
}
//-----------------------------------------------------------------------------------------------
}
