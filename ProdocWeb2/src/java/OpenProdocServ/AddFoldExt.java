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
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class AddFoldExt extends SParent
{

private static final String List2=PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE;

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
DriverGeneric PDSession=getSessOPD(Req);
PDFolders TmpFold;
String CurrFold=Req.getParameter("F");
if (CurrFold!=null)
    {
    String NewType=Req.getParameter("Ty");
    if (NewType!=null && NewType.length()!=0)
        TmpFold=new PDFolders(PDSession, NewType);
    else
        {
        TmpFold=new PDFolders(PDSession);
        NewType=TmpFold.getFolderType();
        }
//    TmpFold.Load(CurrFold);
    out.println( GenerateCompleteFoldForm("Add_Folder", Req, PDSession, CurrFold, NewType, TmpFold.getRecSum(), false, false, TmpFold.getACL()) );    
    }
else
    {
    try {    
    String IdParent=Req.getParameter("CurrFold");   
    String NewType=Req.getParameter("OPDNewType"); 
    TmpFold=new PDFolders(PDSession, NewType);
    Record Rec=TmpFold.getRecSum();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (!List2.contains(Attr.getName()))
            {
            String Val=Req.getParameter(Attr.getName());
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
        Attr=Rec.nextAttr();
        }
    TmpFold.assignValues(Rec);
    TmpFold.setParentId(IdParent);
    TmpFold.insert();
    out.println("OK"+TmpFold.getPDId());
    } catch (PDException Ex)
        {
        PrepareError(Req, Ex.getLocalizedMessage(), out);
        }
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
return "AddFoldExt Servlet";
}
//-----------------------------------------------------------------------------------------------
}
