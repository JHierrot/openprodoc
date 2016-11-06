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
import prodoc.PDDocs;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SearchDoc extends SParent
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
PDDocs TmpDoc;
String CurrFold=Req.getParameter("F");
if (CurrFold!=null)
    {
    String NewType=Req.getParameter("Ty");
    if (NewType!=null && NewType.length()!=0)
        TmpDoc=new PDDocs(PDSession, NewType);
    else
        {
        TmpDoc=new PDDocs(PDSession);
        TmpDoc.LoadFull(CurrFold);
        NewType=TmpDoc.getDocType();
        }
    Record R=TmpDoc.getRecSum();
    out.println( GenSearchDocForm("Search_Folder", Req, PDSession, CurrFold, NewType, TmpDoc.getRecSum(), false, false) );    
    }
else
    {
    Cursor c=null;    
    try {    
    String CurrentFold=Req.getParameter("CurrFold");   
    String CurrType=Req.getParameter("OPDNewType"); 
    String SubTypes=Req.getParameter("Subtypes"); 
    String SubFolders=Req.getParameter("SubFolders"); 
    String IncludeVers=Req.getParameter("IncludeVers"); 
    String FullTextSearch=Req.getParameter("FullTextSearch"); 
    TmpDoc=new PDDocs(PDSession, CurrType);
    Record Rec=TmpDoc.getRecSum();
    Conditions Cond=new Conditions();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (Attr.getName().equals(PDDocs.fDOCTYPE))
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
        else if (!(Val == null || Val.length()==0 || Attr.getName().equals(PDDocs.fACL) && Val.equals("null") 
              || Attr.getType()==Attribute.tBOOLEAN && Val.equals("0") ) )
            {
            Cond.addCondition(SParent.FillCond(Req, Attr, Val, Comp));
            }
        Attr=Rec.nextAttr();
        }
    out.println("OK"+GenHeader(Req, Rec, true));
    out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows>");
    SaveConds(Req, "Doc", CurrType, Cond, (SubTypes.equals("1")), (SubFolders.equals("1")),(IncludeVers.equals("1")), CurrentFold, null, Rec, null);
    c=TmpDoc.Search(FullTextSearch, CurrType, Cond, (SubTypes.equals("1")), (SubFolders.equals("1")),(IncludeVers.equals("1")), CurrentFold, null);
    Record NextDoc=PDSession.NextRec(c);
    while (NextDoc!=null)
        {
        out.print(SParent.GenRowGrid(Req, (String)NextDoc.getAttr(PDDocs.fPDID).getValue(), NextDoc, true));    
        NextDoc=PDSession.NextRec(c);
        }
    out.println("</rows>");
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
return "SearchDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("SearchDoc");
}
//-----------------------------------------------------------------------------------------------
protected String GenSearchDocForm(String Title, HttpServletRequest Req, DriverGeneric PDSession, String CurrFold, String NewType, Record FR, boolean ReadOnly, boolean Modif) throws PDException
{
StringBuilder Form= new StringBuilder(3000);
Attribute Attr;
Form.append("[ {type: \"settings\", position: \"label-left\", labelWidth: 120, inputWidth: 200},");
Form.append("{type: \"label\", label: \"").append(TT(Req, Title)).append("\"},");
Form.append("{type: \"block\", width: 600, list:[");
Form.append("{type: \"checkbox\", name: \"Subtypes\", label:\"").append(TT(Req, "Subtypes")).append("\", tooltip:\"").append(TT(Req,"When_checked_includes_subtypes_of_folders_in_results")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"checkbox\", name: \"SubFolders\", label:\"").append(TT(Req, "SubFolders")).append("\", tooltip:\"").append(TT(Req,"When_checked_limits_the_search_to_actual_folder_and_subfolders")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"checkbox\", name: \"IncludeVers\", label:\"").append(TT(Req, "Versions")).append("\", tooltip:\"").append(TT(Req,"When_checked_includes_all_versions_of_document_in_results")).append("\"}");
Form.append("]},{type: \"input\", name: \"FullTextSearch\", label: \"").append(TT(Req, "Full_Text_Search")).append("\", inputWidth: 300},");

FR.initList();
Attr=FR.nextAttr();
while (Attr!=null)
    {
    if (! (Attr.getName().equals(PDDocs.fDOCTYPE) || (Attr.getName().equals(PDDocs.fSTATUS)) ) )    
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
