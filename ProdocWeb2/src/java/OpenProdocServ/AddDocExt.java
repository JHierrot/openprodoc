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
import static OpenProdocUI.SParent.StoreDat;
import static OpenProdocUI.SParent.getSessOPD;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDMimeType;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class AddDocExt extends SParent
{

final static private String List=PDDocs.fPARENTID+"/"+PDDocs.fPDID
                    +"/"+PDDocs.fPDAUTOR+"/"+PDDocs.fPDDATE
                    +"/"+PDDocs.fLOCKEDBY+"/"+PDDocs.fVERSION+"/"+PDDocs.fPURGEDATE
                    +"/"+PDDocs.fREPOSIT+"/"+PDDocs.fSTATUS;
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
        NewType=TmpDoc.getDocType();
        }
    PDFolders F=new PDFolders(PDSession);
    F.Load(CurrFold);
    out.println( GenerateCompleteDocForm("Add_Document", Req, PDSession, CurrFold, NewType, TmpDoc.getRecSum(), false, false, F.getACL()) );    
    }
else
    {
    HashMap <String, String>ListFields=new HashMap(); 
    ListFields.put("CurrFold", Req.getParameter("CurrFold"));
    String NewType=Req.getParameter(PDDocs.fDOCTYPE); 
    if (NewType==null)
       TmpDoc=new PDDocs(PDSession);
    else
        {
        TmpDoc=new PDDocs(PDSession, NewType); 
        ListFields.put(PDDocs.fDOCTYPE, NewType);
        }
    Record Rec=TmpDoc.getRecSum();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (!List.contains(Attr.getName()))
            {
            if (Attr.getType()==Attribute.tTHES)
                ListFields.put(Attr.getName(), Req.getParameter("TH_"+Attr.getName()));
            else    
                ListFields.put(Attr.getName(), Req.getParameter(Attr.getName()));
            }
        Attr=Rec.nextAttr();
        }
//    if (!ListFields.containsKey(PDDocs.fNAME))
    if (Req.getParameter(PDDocs.fNAME)==null || Req.getParameter(PDDocs.fNAME).length()==0)
        {
        StoreDat(Req, ListFields);
        out.println("OK");
        return;
        }
    PDDocs Doc;
    String DType=(String) ListFields.get(PDDocs.fDOCTYPE);
    if (DType==null)
        Doc = new PDDocs(PDSession);
    else
        Doc = new PDDocs(PDSession, DType);
    Rec.initList();
    Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (!List.contains(Attr.getName()))
            {
            String Val=(String) ListFields.get(Attr.getName());
            if (Attr.getType()==Attribute.tBOOLEAN)
                {
                if(Val == null || Val.length()==0 || Val.equals("0"))
                    Attr.setValue(false);
                else
                    Attr.setValue(true);
                }
            else if(Val != null)
                {
                SParent.FillAttr(Req, Attr, Val, false);
                }
            }
        Attr=Rec.nextAttr();
        }
    Doc.assignValues(Rec);
    Doc.setParentId(ListFields.get("CurrFold"));
    Doc.setName(ListFields.get(PDDocs.fNAME));
    PDMimeType mt=new PDMimeType(PDSession);
    Doc.setMimeType(mt.SolveName(ListFields.get(PDDocs.fNAME)));
    Doc.insert();
    out.println("OK");
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
return "AddDocExt Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("AddDocExt");
}

}
