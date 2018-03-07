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
import java.util.Arrays;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class AddTerm extends SParent
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
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDThesaur TmpTerm=new PDThesaur(PDSession);
String CurrTerm=Req.getParameter("T");
//String CurrThes=Req.getParameter("Tes");
boolean ReadOnly=false;
boolean Modif=false;
try {    
if (CurrTerm!=null)
    {
    TmpTerm.Load(CurrTerm);
    String idThesaur = TmpTerm.getIDThesaur(); // too avoid problems when selected another Thes  
    TmpTerm.Clear();
    TmpTerm.setPDId(TmpTerm.GenerateId()); // for inserting relations also
    out.println(GenerateCompleteTermForm("Add_Term", Req, PDSession, TmpTerm, ReadOnly, Modif, CurrTerm, idThesaur));
    }
else
    {
    CurrTerm=Req.getParameter("CurrTerm"); 
    TmpTerm.setParentId(CurrTerm);
    String NewName=Req.getParameter(PDThesaur.fNAME);    
    TmpTerm.setName(NewName);
    String NewDesc=Req.getParameter(PDThesaur.fDESCRIP);    
    TmpTerm.setDescription(NewDesc);
    String NewLang=Req.getParameter(PDThesaur.fLANG);    
    TmpTerm.setLang(NewLang);
    String NewSCN=Req.getParameter(PDThesaur.fSCN);    
    TmpTerm.setSCN(NewSCN);        
    String NewUSE=Req.getParameter("TH_"+PDThesaur.fUSE);    
    TmpTerm.setUse(NewUSE); 
    String NewListRT=Req.getParameter("H_RT"); 
    String NewListLang=Req.getParameter("H_Lang"); 
    PDSession.IniciarTrans();
    TmpTerm.insert();
    if (NewListRT!=null && NewListRT.length()>1)
        {
        String[] ListRT = NewListRT.split("\\|");
        HashSet HListRT=new HashSet();
        HListRT.addAll(Arrays.asList(ListRT));
        TmpTerm.AddRT(HListRT);
        }
    if (NewListLang!=null && NewListLang.length()>1)
        {
        String[] ListLang = NewListLang.split("\\|");
        HashSet HListLang=new HashSet();
        HListLang.addAll(Arrays.asList(ListLang));
        TmpTerm.AddLang(HListLang);
        }
    PDSession.CerrarTrans();
    out.print("OK"+TmpTerm.getPDId());
    }
} catch (Exception ex)
    {
    if (Req.getParameter("CurrTerm")!=null) //return Form
        SParent.GenErrorForm(ex.getLocalizedMessage());
    else    
        {    
        if (PDSession.isInTransaction())
            PDSession.AnularTrans();
        PrepareError(Req, ex.getLocalizedMessage(), out);
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
return "AddTerm Servlet";
}
//-----------------------------------------------------------------------------------------------
}
