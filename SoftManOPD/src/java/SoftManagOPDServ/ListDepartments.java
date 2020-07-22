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
import static SoftManagOPDUI.SParent.getSessOPD;
import java.io.PrintWriter;
import java.util.Vector;
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
public class ListDepartments extends SParent
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
return "DocList Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListDoc(HttpServletRequest Req) 
{
StringBuilder ListProducts=new StringBuilder(5000);
ListProducts.append("<rows>");
DriverGeneric PDSession=getSessOPD(Req);
Attribute AttrD;
try {
PDFolders Fold=new PDFolders(PDSession, getDepartmentType(Req));
String CurrentFold=PDFolders.ROOTFOLDER;
boolean SubFolders=false;
Record Rec=Fold.getRecSum();
Conditions Cond=new Conditions();
Vector<String> FiltDepFields = getDepartFieldsFilter(Req);
for (int i = 0; i < FiltDepFields.size(); i++)
    {
    String Filt = Req.getParameter(FiltDepFields.elementAt(i));
    if (Filt!=null && Filt.length()!=0) 
        {
        Attribute Attr = Rec.getAttr(FiltDepFields.elementAt(i));
        Condition C;
        if (Attr.getType()==Attribute.tSTRING)
            C=new Condition(FiltDepFields.elementAt(i) , Condition.cLIKE, Filt);
        else
            C=new Condition(FiltDepFields.elementAt(i) , Condition.cEQUAL, Filt);
        Cond.addCondition(C);
        }
    }
if (Cond.NumCond()==0)
    {
    Condition C=new Condition(PDFolders.fPDID , Condition.cNE, "z");
    Cond.addCondition(C);
    }
Cursor ListDocs=Fold.Search( getDepartmentType(Req), Cond, true, SubFolders, CurrentFold, null);
Record NextProd=PDSession.NextRec(ListDocs);
String ProdId;
PDThesaur TmpTerm=new PDThesaur(PDSession);
while (NextProd!=null)
    {  // 
    AttrD=NextProd.getAttr(PDFolders.fPDID);  
    ProdId=(String)AttrD.getValue();
    ListProducts.append("<row id=\"").append(ProdId).append("\">");       
    AttrD=NextProd.getAttr(PDFolders.fTITLE);
    ListProducts.append("<cell>").append(AttrD.Export()).append("</cell>");       
    TmpTerm.Load((String)NextProd.getAttr("Responsible").getValue());
    ListProducts.append("<cell>").append(TmpTerm.getName()).append("</cell>");
    AttrD=NextProd.getAttr("Description");
    ListProducts.append("<cell>").append(AttrD.Export()).append("</cell></row>");
    NextProd=PDSession.NextRec(ListDocs);
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
