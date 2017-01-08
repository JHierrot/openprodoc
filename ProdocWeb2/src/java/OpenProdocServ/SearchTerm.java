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
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SearchTerm extends SParent
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
String CurrTerm=Req.getParameter("Term");
String CurrThes=Req.getParameter("Thes");
if (CurrThes!=null)
    {
    PDThesaur TmpTerm=new PDThesaur(PDSession);    
    TmpTerm.Load(CurrTerm);
    String idThesaur = TmpTerm.getIDThesaur(); // too avoid problems when selected another Thes
    out.println(GenerateSearchTermForm(Req, PDSession, TmpTerm, CurrTerm, idThesaur));
    }
else
    {
    try {    
    CurrTerm=Req.getParameter("CurrTerm");   
    CurrThes=Req.getParameter("CurrThes"); 
    String NT=Req.getParameter("NarrowTerms"); 
    boolean IsNT;
    if (NT!=null && NT.equals("1"))
        IsNT=true;
    else
        IsNT=false;
    PDThesaur TmpTerm=new PDThesaur(PDSession); 
    Record Rec=TmpTerm.getRecord();
    Conditions Cond=new Conditions();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        String Val=Req.getParameter(Attr.getName());
        String Comp=Req.getParameter("Comp_"+Attr.getName());
        if (!(Val == null || Val.length()==0 ) )
            Cond.addCondition(SParent.FillCond(Req, Attr, Val, Comp));
        Attr=Rec.nextAttr();
        }
    out.println("OK"+GenHeader(Req, Rec, false));
    out.print("data={ rows:[");
    Cursor c = TmpTerm.Search(Cond, IsNT, CurrTerm.length()>0?CurrTerm:CurrThes, null);
    Record NextTerm=PDSession.NextRec(c);
    while (NextTerm!=null)
        {
        out.print(GenRowGridTerm(PDSession, NextTerm));    
        NextTerm=PDSession.NextRec(c);
        if (NextTerm!=null)
            out.print(",");
        }
    out.println("] };\n");
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
return "SearchTerm Servlet";
}
//----------------------------------------------------------------------------
private String GenerateSearchTermForm(HttpServletRequest Req, DriverGeneric PDSession, PDThesaur TmpTerm, String CurrTerm, String idThesaur) throws PDException
{
StringBuilder Form= new StringBuilder(3000);
Attribute Attr;
Record FR=TmpTerm.getRecord();
Form.append("[ {type: \"settings\", position: \"label-left\", labelWidth: 130, inputWidth: 200},");
Form.append("{type: \"label\", label: \"").append(TT(Req, "Search_Term")).append("\"},");
Form.append("{type: \"checkbox\", name: \"NarrowTerms\", label:\"").append(TT(Req, "Narrow_Terms")).append("\", tooltip:\"").append(TT(Req,"When_checked_limits_the_search_to_actual_term_and_specific_terms")).append("\"},");
FR.initList();
Attr=FR.nextAttr();
while (Attr!=null)
    {
    Form.append(GenSearchInput(Req, Attr));
    Attr=FR.nextAttr();
    }
Form.append("{type: \"block\", width: 250, list:[");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
Form.append("{type: \"hidden\", name:\"CurrThes\", value: \"").append(idThesaur).append("\"},");
Form.append("{type: \"hidden\", name:\"CurrTerm\", value: \"").append(CurrTerm).append("\"}");
Form.append("]}");
Form.append("];");
return(Form.toString());
}
//----------------------------------------------------------------
private static String GenRowGridTerm(DriverGeneric PDSession, Record NextTerm)
{
StringBuilder Row=new StringBuilder(500);
Attribute Attr=NextTerm.getAttr(PDThesaur.fPDID);
Row.append("{ id:\"").append(Attr.getValue()).append("\", data:[");
NextTerm.initList();
Attr=NextTerm.nextAttr();
while (Attr!=null)
    {
    if (Attr.getValue()==null)
        Row.append("\"\",");
    else
        {
        if ( (Attr.getName().equals(PDThesaur.fUSE) || Attr.getName().equals(PDThesaur.fPARENTID)) && ((String)Attr.getValue()).length()!=0 )  
            {
            try {    
            PDThesaur TmpTerm=new PDThesaur(PDSession);
            TmpTerm.Load((String)Attr.getValue());
            Row.append("\"").append(TmpTerm.getName()).append("\",");
            } catch (Exception Ex)
                {
                Row.append("\"Error:"+Ex.getLocalizedMessage()+"\",");
                }
            }
        else
            Row.append("\"").append(Attr.Export()).append("\",");
        }
    Attr=NextTerm.nextAttr();
    }
Row.deleteCharAt(Row.length()-1);
Row.append("]}");
return(Row.toString());
}
//----------------------------------------------------------------------------

}
