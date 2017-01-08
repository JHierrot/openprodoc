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
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class AddThes extends SParent
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
PDThesaur TmpThes=new PDThesaur(PDSession);
String CurrThes=Req.getParameter("T");
boolean ReadOnly=false;
boolean Modif=false;
if (CurrThes!=null)
    {
    out.println(GenerateCompleteThesForm("Create_Theusurus", Req, PDSession, TmpThes, ReadOnly, Modif));
    }
else
    {
    try {    
    TmpThes.setParentId(TmpThes.ROOTTERM);
    String NewId=Req.getParameter(PDThesaur.fPDID);    
    TmpThes.setPDId(NewId);
    String NewName=Req.getParameter(PDThesaur.fNAME);    
    TmpThes.setName(NewName);
    String NewDesc=Req.getParameter(PDThesaur.fDESCRIP);    
    TmpThes.setDescription(NewDesc);
    String NewLang=Req.getParameter(PDThesaur.fLANG);    
    TmpThes.setLang(NewLang);
    String NewSCN=Req.getParameter(PDThesaur.fSCN);    
    TmpThes.setSCN(NewSCN);        
    TmpThes.insert();
    out.println("OK"+TmpThes.getPDId());
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
return "AddThes Servlet";
}
//-----------------------------------------------------------------------------------------------
}
