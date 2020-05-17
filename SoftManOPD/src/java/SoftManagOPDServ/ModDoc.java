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

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ModDoc extends SParent
{

final static private String List=PDDocs.fPARENTID
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
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDDocs TmpDoc=new PDDocs(PDSession);
String CurrDoc=Req.getParameter("D");
String RO=Req.getParameter("RO");
String Vers=Req.getParameter("Vers");
String AclMode=Req.getParameter("AclMode");
String Title;
boolean IsAclMode=false;
try {
if (CurrDoc!=null)
    {
    if (Vers!=null&&Vers.length()!=0)       
        TmpDoc.LoadVersion(CurrDoc, Vers);
    else
        TmpDoc.LoadFull(CurrDoc);
    if (AclMode!=null && AclMode.equals("1"))
        {
        Title="Change_ACL";
        IsAclMode=true;
        RO="true";
        }
    else if (RO.equalsIgnoreCase("true"))    
        Title="Document_Attributes";
    else
        Title="Update_Document";
    out.println( GenerateCompleteDocForm(Title, Req, PDSession, TmpDoc.getParentId(), TmpDoc.getDocType(), TmpDoc.getRecSum(), RO.equalsIgnoreCase("true"), !RO.equalsIgnoreCase("true"), null, IsAclMode) );   
    }
else
    {
    HashMap <String, String>ListFields=new HashMap(); 
    ListFields.put("CurrFold", Req.getParameter("CurrFold"));
    ListFields.put(PDDocs.fPDID, Req.getParameter(PDDocs.fPDID));
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
    StoreDat(Req, ListFields);    
    if (Req.getParameter("EndMod")==null && Req.getParameter(PDDocs.fNAME)==null)
        {
        out.println("OK");    
        return;
        }
    TmpDoc.LoadFull((String) ListFields.get(PDDocs.fPDID));
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
    TmpDoc.assignValues(Rec);
    TmpDoc.setParentId(ListFields.get("CurrFold"));
    TmpDoc.update();
    out.println("OK");    
    }
} catch (Exception Ex)
    {
    out.print("KO");
    PrepareError(Req, Ex.getLocalizedMessage(), out);
    out.println();
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
return "ModDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
}

