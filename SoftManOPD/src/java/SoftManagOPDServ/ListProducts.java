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

import Config.SoftManOPDConfig;
import SoftManagOPDUI.SParent;
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
public class ListProducts extends SParent
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
out.println(GenListProducts(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ListProducts Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListProducts(HttpServletRequest Req) 
{
StringBuilder ListProducts=new StringBuilder(5000);
ListProducts.append("<rows>");
DriverGeneric PDSession=getSessOPD(Req);
Attribute AttrD;
try {
PDFolders Fold=new PDFolders(PDSession, getProductType(Req));
String CurrentFold=PDFolders.ROOTFOLDER;
boolean SubFolders=false;
Record Rec=Fold.getRecSum();
Conditions Cond=new Conditions();
Vector<String> FiltProdFields = getProductFieldsFilter(Req);
for (int i = 0; i < FiltProdFields.size(); i++)
    {
    String Filt = Req.getParameter(FiltProdFields.elementAt(i));
    if (Filt!=null && Filt.length()!=0) 
        {
        Attribute Attr = Rec.getAttr(FiltProdFields.elementAt(i));
        Condition C;
        if (Attr.getType()==Attribute.tSTRING)
            C=new Condition(FiltProdFields.elementAt(i) , Condition.cLIKE, Filt);
        else
            C=new Condition(FiltProdFields.elementAt(i) , Condition.cEQUAL, Filt);
        Cond.addCondition(C);
        }
    }
String Internal = Req.getParameter("Internal");
if (Internal!=null && Internal.length()!=0 && !Internal.equalsIgnoreCase("0"))
    {
    Condition C=new Condition("Internal", Condition.cEQUAL, Internal.equalsIgnoreCase("1"));
    Cond.addCondition(C);
    }    
if (Cond.NumCond()==0)
    {
    Condition C=new Condition(PDFolders.fPDID , Condition.cNE, "z");
    Cond.addCondition(C);
    }
Cursor ListProd=Fold.Search( getProductType(Req), Cond, true, SubFolders, CurrentFold, null);
String ProdId;
PDFolders TmpFold=new PDFolders(PDSession);
PDThesaur TmpTerm=new PDThesaur(PDSession);
SoftManOPDConfig SoftManConf = getSoftManConf(Req);
String[] ListFields = SoftManConf.getGridConfList().get("ListProducts").getColumnIds().split(",");
Record NextProd=PDSession.NextRec(ListProd);
while (NextProd!=null)
    {
    AttrD=NextProd.getAttr(PDFolders.fPDID);  
    ProdId=(String)AttrD.getValue();
    ListProducts.append("<row id=\"").append(ProdId).append("\">"); 
    for (String ListField : ListFields)
        {
        AttrD = NextProd.getAttr(ListField);
        if (AttrD.getType()==Attribute.tTHES)
            {
            String Tmp=(String)AttrD.getValue();
            if (Tmp!=null && Tmp.length()>0)
                {
                TmpTerm.Load(Tmp);
                ListProducts.append("<cell>").append(TmpTerm.getName()).append("</cell>");
                }
            else
                ListProducts.append("<cell></cell>");
            }
        else if (AttrD.getName().equalsIgnoreCase(PDFolders.fPARENTID))
            {
            TmpFold.Load((String)AttrD.getValue());
            ListProducts.append("<cell>").append(TmpFold.getTitle()).append("</cell>");
            }
        else    
            ListProducts.append("<cell>").append(AttrD.Export()).append("</cell>");  
        }
    ListProducts.append("</row>");
    NextProd=PDSession.NextRec(ListProd);
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
