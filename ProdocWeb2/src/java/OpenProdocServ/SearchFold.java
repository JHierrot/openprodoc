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
import static OpenProdocUI.SParent.TT;
import static OpenProdocUI.SParent.getSessOPD;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SearchFold extends SParent
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
    out.println( GenSearchFoldForm("Search_Folders", Req, PDSession, CurrFold, NewType, TmpFold.getRecSum(), false, false) );    
    }
else
    {
    Cursor c=null;    
    try {    
    String CurrentFold=Req.getParameter("CurrFold");   
    String CurrType=Req.getParameter("OPDNewType"); 
    String SubTypes=Req.getParameter("Subtypes"); 
    String SubFolders=Req.getParameter("SubFolders"); 
    TmpFold=new PDFolders(PDSession, CurrType);
    Record Rec=TmpFold.getRecSum();
    Conditions Cond=new Conditions();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (Attr.getName().equals(PDFolders.fFOLDTYPE))
            {
            Attr=Rec.nextAttr();
            continue;
            }
        String Val=Req.getParameter(Attr.getName());
        String Comp=Req.getParameter("Comp_"+Attr.getName());
        if (Attr.getType()==Attribute.tTHES)
                {
                Val=Req.getParameter("TH_"+Attr.getName());    
                if (Val != null && Val.length()!=0)
                    Cond.addCondition(SParent.FillCond(Req, Attr, Val, Comp));
                }
        else if (!(Val == null || Val.length()==0 || Attr.getName().equals(PDFolders.fACL) && Val.equals("null") 
              || Attr.getType()==Attribute.tBOOLEAN && Val.equals("0") ) )
            {
            Cond.addCondition(SParent.FillCond(Req, Attr, Val, Comp));
            }
        Attr=Rec.nextAttr();
        }
    out.println("OK"+GenHeader(Req, Rec, false));
    out.print("data={ rows:[");
    SaveConds(Req, "Fold", CurrType, Cond, (SubTypes.equals("1")), (SubFolders.equals("1")), false, CurrentFold, null, Rec, null);
    c=TmpFold.Search(CurrType, Cond, (SubTypes.equals("1")), (SubFolders.equals("1")), CurrentFold, null);
    Record NextFold=PDSession.NextRec(c);
    while (NextFold!=null)
        {
        out.print(SParent.GenRowGrid(Req, (String)NextFold.getAttr(PDFolders.fPDID).getValue(), NextFold, false));    
        NextFold=PDSession.NextRec(c);
        if (NextFold!=null)
            out.print(",");
        }
    out.println("] };\n");
    } catch (PDException ex)
        {
        out.println(ex.getLocalizedMessage());
        }
    finally 
        {
        if (c!=null)
           PDSession.CloseCursor(c);
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
return "SearchFold Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("SearchFold");
}
//-----------------------------------------------------------------------------------------------
protected String GenSearchFoldForm(String Title, HttpServletRequest Req, DriverGeneric PDSession, String CurrFold, String NewType, Record FR, boolean ReadOnly, boolean Modif) throws PDException
{
StringBuilder Form= new StringBuilder(3000);
Attribute Attr;
Form.append("[ {type: \"settings\", position: \"label-left\", labelWidth: 130, inputWidth: 200},");
Form.append("{type: \"label\", label: \"").append(TT(Req, Title)).append("\", labelWidth:200},");
Form.append("{type: \"block\", width: 500, list:[");
Form.append("{type: \"checkbox\", name: \"Subtypes\", label:\"").append(TT(Req, "Subtypes")).append("\", tooltip:\"").append(TT(Req,"When_checked_includes_subtypes_of_folders_in_results")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"checkbox\", name: \"SubFolders\", label:\"").append(TT(Req, "SubFolders")).append("\", tooltip:\"").append(TT(Req,"When_checked_limits_the_search_to_actual_folder_and_subfolders")).append("\"}");
Form.append("]},");
FR.initList();
Attr=FR.nextAttr();
while (Attr!=null)
    {
    if (!Attr.getName().equals(PDFolders.fFOLDTYPE))    
        Form.append(GenSearchInput(Req, Attr));
    Attr=FR.nextAttr();
    }
Form.append("{type: \"block\", width: 250, list:[");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
Form.append("{type: \"hidden\", name:\"OPDNewType\", value: \"").append(NewType).append("\"},");
Form.append("{type: \"hidden\", name:\"CurrFold\", value: \"").append(CurrFold).append("\"}");
Form.append("]}");
Form.append("];");
return(Form.toString());
}
//----------------------------------------------------------------------------

}
