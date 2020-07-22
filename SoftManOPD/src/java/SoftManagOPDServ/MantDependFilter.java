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
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDReport;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class MantDependFilter extends SParent
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
out.println(GenerateForm(Oper, TmpFold, Req));
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
String ProdVersPrim=null;
String RelationSecPrim=null;
String IdProdPrim=null;
try{
Form.append("[ {type: \"settings\", position: \"label-left\", offsetLeft:10, labelWidth: 180, inputWidth: 250},");
Form.append("{type: \"combo\", name: \"IdProd\", label: \"").append(TT(Req, "Product")).append("\",").append("filtering:1,required:true,").append(IdProdPrim!=null?("value:\""+IdProdPrim+"\","):"").append(IdProdPrim!=null?"readonly:1,":"").append(" options:[");
Form.append(getComboProducts(Req, TmpFold.getDrv(), IdProdPrim));
Form.append("]}");
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
private StringBuilder getComboProducts(HttpServletRequest Req,DriverGeneric PDSession, String SelId) throws PDException
{
StringBuilder ListVals=new StringBuilder(5000);
Cursor CursorId=null;
try {
PDFolders Fold=new PDFolders(PDSession, getProductType(Req));
String CurrentFold=PDFolders.ROOTFOLDER;
boolean SubFolders=false;
Condition C=new Condition(PDFolders.fPDID , Condition.cNE, "z");
Conditions Cond=new Conditions();
Cond.addCondition(C);
Vector<String> Ord=new Vector();
Ord.add(PDFolders.fTITLE);
CursorId=Fold.Search( getProductType(Req), Cond, true, SubFolders, CurrentFold, Ord);
Record Res=PDSession.NextRec(CursorId);
while (Res!=null)
    {
    ListVals.append("{text: \"").append(Res.getAttr(PDFolders.fTITLE).getValue()).append("\", value: \"").append(Res.getAttr(PDFolders.fPDID).getValue()).append("\" ").append((SelId!=null&&SelId.equalsIgnoreCase((String)Res.getAttr(PDFolders.fPDID).getValue()))?", selected: true":"").append("}");
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

}
