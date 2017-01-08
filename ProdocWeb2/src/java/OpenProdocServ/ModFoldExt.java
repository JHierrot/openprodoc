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
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.getSessOPD;
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
public class ModFoldExt extends SParent
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
        TmpFold.LoadFull(CurrFold);
        NewType=TmpFold.getFolderType();
        }
    Record R=TmpFold.getRecSum();
    out.println( GenerateCompleteFoldForm("Update_Folder", Req, PDSession, CurrFold, NewType, TmpFold.getRecSum(), false, true, null) );    
    }
else
    {
    try {    
    String IdCurr=Req.getParameter("CurrFold");   
    TmpFold=new PDFolders(PDSession);
    TmpFold.LoadFull(IdCurr);
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
    TmpFold.update();
    out.println("OK"+TmpFold.getPDId());
    } catch (PDException ex)
        {
        out.println(ex.getLocalizedMessage());
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
return "ModFoldExt Servlet";
}
//-----------------------------------------------------------------------------------------------
}
