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
import prodoc.Attribute;
import prodoc.PDFolders;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class MantProducts extends SParent
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
PDFolders TmpFold=new PDFolders(SParent.getSessOPD(Req), getProductType());    
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
String Id=Req.getParameter("D");
boolean ReadOnly=false;
boolean Modif=true;
try {
switch (Oper)
    {case ADD:
        Title=TT(Req, "Add_Product");
        break;
    case UPD:
        Title=TT(Req, "Update_Product");
        break;
    case DEL:
        Title=TT(Req, "Delete_Product");
        ReadOnly=true;
        break;
    }
if (!Oper.equals(ADD))
    TmpFold.LoadFull(Id);
Form.append("[ {type: \"settings\", position: \"label-left\", offsetLeft:10, labelWidth: 180, inputWidth: 250},");
if (Oper.equals(DEL))
    Form.append("{type: \"label\", labelWidth: 500,label: \"ALL INFORMATION OF THE PRODUCT, INCLUDING THE VERSIONS, DOCUMENTS AND ISSUES WILL BE DELETED\"},");    
Form.append("{type: \"label\", label: \"").append(Title).append("\"},");
Vector<String> ProdFields = getProductFields();
for (int i = 0; i < ProdFields.size(); i++)
    {
    Form.append(GenInput(Req, TmpFold.getRecord().getAttr(ProdFields.elementAt(i)), ReadOnly, Modif));
    }
Form.append("{type: \"block\", width: 250, list:[");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
Form.append("{type: \"hidden\", name:\"Oper2\", value: \"").append(Oper).append("\"},");
Form.append("{type: \"hidden\", name:\"D\", value: \"").append(Id).append("\"}]}");
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
String Id=Req.getParameter("D");
try {
if (!Oper2.equals(ADD))
    TmpFold.LoadFull(Id);
Vector<String> ProdFields = getProductFields();
Record recSum = TmpFold.getRecSum();
for (int i = 0; i < ProdFields.size(); i++)
    {
    String Val=Req.getParameter(ProdFields.elementAt(i));
    Attribute Attr = recSum.getAttr(ProdFields.elementAt(i));
    if (Attr.getType()==Attribute.tBOOLEAN)
        {
        if(Val == null || Val.length()==0 || Val.equals("0"))
            Attr.setValue(false);
        else
            Attr.setValue(true);
        }
    else if (Attr.getType()==Attribute.tTHES)
        {
        Val=Req.getParameter("TH_"+Attr.getName());    
        SParent.FillAttr(Req, Attr, Val, false);
        }
    else if(Val != null)
        {
        SParent.FillAttr(Req, Attr, Val, false);
        }
    }
if (Oper2.equalsIgnoreCase(ADD))
    {
    PDFolders Fold=new PDFolders(TmpFold.getDrv());
    Fold.setPDId(Id);
    recSum.getAttr("Internal").setValue( Fold.IsUnder(getDepartsRoot(TmpFold.getDrv())) );
    }
TmpFold.assignValues(recSum);
switch (Oper2)
    {case ADD:
        TmpFold.setParentId(Id);
        TmpFold.insert();
        break;
    case UPD:
        TmpFold.setPDId(Id);
        TmpFold.update();
        break;
    case DEL:
        TmpFold.setPDId(Id);
        TmpFold.delete();
        break;
    }
return("OK:"+Oper2);
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return("Error:"+Ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------------------------------------
}
