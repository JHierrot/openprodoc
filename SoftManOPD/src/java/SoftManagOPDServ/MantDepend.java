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
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class MantDepend extends SParent
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
PDFolders TmpFold=new PDFolders(SParent.getSessOPD(Req));    
String Oper=Req.getParameter("Oper");
if (Oper!=null) // Second time)
    out.println(GenerateForm(Oper, TmpFold, Req));
else
    out.println(ExecuteOper(Req, TmpFold));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "MantProducts Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenerateForm(String Oper, PDFolders TmpFold, HttpServletRequest Req)
{
StringBuilder Form=new StringBuilder(1000);
String Title="";
String IdVersProdSec=Req.getParameter("IdVers");
String IdRel=Req.getParameter("IdRel");
String IdProdPrim=Req.getParameter("IdProd");
String ProdVersPrim=null;
String RelationSecPrim=null;
try {
if (!Oper.equals(ADD))
    {
    String[] split = IdRel.split(REL_SEP);
    ProdVersPrim=split[0];
    RelationSecPrim=split[1];
    TmpFold.Load(ProdVersPrim);
    IdProdPrim=TmpFold.getParentId();  
    }
switch (Oper)
    {case ADD:
        Title=TT(Req, "Add_Dependency");
        break;
    case UPD:
        Title=TT(Req, "Update_Dependency");
        break;
    case DEL:
        Title=TT(Req, "Delete_Dependency");
        break;
    }
Form.append("[ {type: \"settings\", position: \"label-left\", offsetLeft:10, labelWidth: 180, inputWidth: 250},");
Form.append("{type: \"label\", label: \"").append(Title).append("\"},");
Form.append("{type: \"combo\", name: \"Relations\", label: \"").append(TT(Req, "Relations")).append("\",").append("filtering:1,required:true,")/*.append(RelationSecPrim!=null?("value:\""+RelationSecPrim+"\","):"")*/.append((Oper.equalsIgnoreCase(DEL))?"disabled:1,":"").append(" options:[");
Form.append(getComboRelations(TmpFold.getDrv(), RelationSecPrim));
Form.append("]},");
Form.append("{type: \"combo\", name: \"ProdVers\", label: \"").append(TT(Req, "Product Versions")).append("\",").append("filtering:1,required:true,")/*.append(ProdVersPrim!=null?("value:\""+ProdVersPrim+"\","):"")*/.append((!Oper.equalsIgnoreCase(ADD))?"disabled:1,":"").append(" options:[");
Form.append(getComboProductsVers(TmpFold.getDrv(), IdProdPrim, ProdVersPrim));
Form.append("]},");
Form.append("{type: \"block\", width: 250, list:[");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
Form.append("{type: \"hidden\", name:\"Oper2\", value: \"").append(Oper).append("\"},");
Form.append("{type: \"hidden\", name:\"IdVers\", value: \"").append(IdVersProdSec).append("\"}]}");
Form.append("];");
} catch (Exception Ex)
    {
    Form=new StringBuilder(1000);
    Form.append("[");
    Form.append("{type: \"label\", label: \"").append("Error:").append(Ex.getLocalizedMessage()).append("\"}");
    Form.append("];");
    }
return(Form.toString());
}
//-----------------------------------------------------------------------------------------------
private String ExecuteOper(HttpServletRequest Req, PDFolders TmpFold)
{
String Oper2=Req.getParameter("Oper2");
String IdVersProdSec=Req.getParameter("IdVers");
String ProdVersPrim=Req.getParameter("ProdVers");
String RelationSecPrim=Req.getParameter("Relations");
String Rel=ProdVersPrim+REL_SEP+RelationSecPrim;
try {
TmpFold.LoadFull(IdVersProdSec);
TreeSet<String> ListDep = TmpFold.getRecSum().getAttr(DEPENDENCIES).getValuesList();
switch (Oper2)
    {case ADD:
        ListDep.add(Rel);
        break;
    case UPD:
        String Rel2Up=null;
        for (Iterator<String> iterator = ListDep.iterator(); iterator.hasNext();)
            {
            String IsRel2Up = iterator.next();
            if (IsRel2Up.startsWith(ProdVersPrim+REL_SEP))
                {
                Rel2Up=IsRel2Up;
                break;
                }
            }
        ListDep.remove(Rel2Up);
        ListDep.add(Rel);
        break;
    case DEL:
        ListDep.remove(Rel);
        break;
    }
TmpFold.update();
return("OK:"+Oper2);
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return("Error:"+Ex.getLocalizedMessage());
    }
}
//----------------------------------------------------------------

private StringBuilder getComboProductsVers(DriverGeneric PDSession, String IdProd, String ProdVersPrim)  throws PDException
{
StringBuilder ListVals=new StringBuilder(5000);
Cursor CursorId=null;
try {
PDFolders Fold=new PDFolders(PDSession, getProductsVersType());
String CurrentFold=PDFolders.ROOTFOLDER;
boolean SubFolders=false;
Condition C;
if (IdProd==null || IdProd.length()==0)
    C=new Condition(PDFolders.fPDID , Condition.cNE, "z");
else
    C=new Condition(PDFolders.fPARENTID , Condition.cEQUAL, IdProd);
Conditions Cond=new Conditions();
Cond.addCondition(C);
Vector<String> Ord=new Vector();
Ord.add(PDFolders.fTITLE);
CursorId=Fold.Search( getProductsVersType(), Cond, true, SubFolders, CurrentFold, Ord);
Record Res=PDSession.NextRec(CursorId);
while (Res!=null)
    {
    ListVals.append("{text: \"").append(Res.getAttr(PDFolders.fTITLE).getValue()).append("\", value: \"").append(Res.getAttr(PDFolders.fPDID).getValue()).append("\" ").append((ProdVersPrim!=null&&ProdVersPrim.equalsIgnoreCase((String)Res.getAttr(PDFolders.fPDID).getValue()))?", selected: true":"").append("}");
    Res=PDSession.NextRec(CursorId);    
    if (Res!=null)
        ListVals.append(",");
    }
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
finally 
    {
    if (CursorId!=null)    
        PDSession.CloseCursor(CursorId);
    }
return(ListVals);
}
//----------------------------------------------------------------

private StringBuilder getComboRelations(DriverGeneric PDSession, String IdRel)  throws PDException
{
StringBuilder ListVals=new StringBuilder(5000);
PDThesaur Thes=new PDThesaur(PDSession);
HashSet<String> listDirectDescendList = Thes.getListDirectDescendList(getRelationsThes());
for (Iterator<String> iterator = listDirectDescendList.iterator(); iterator.hasNext();)
    {
    Thes.Load(iterator.next());
    if (ListVals.length()!=0)
        ListVals.append(",");
    ListVals.append("{text: \"").append(Thes.getName()).append("\", value: \"").append(Thes.getPDId()).append("\"").append((IdRel!=null&&IdRel.equalsIgnoreCase(Thes.getPDId()))?", selected: true":"").append(" }");
    }
return(ListVals);
}
//----------------------------------------------------------------

}
