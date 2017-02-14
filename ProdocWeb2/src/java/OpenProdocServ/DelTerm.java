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
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class DelTerm extends SParent
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
String CurrThes=Req.getParameter("Tes");
try {    
if (CurrThes!=null)
    {
    TmpTerm.Load(CurrTerm);
    String idThesaur = TmpTerm.getIDThesaur();
    out.println( GenerateCompleteTermForm("Delete_Term", Req, PDSession, TmpTerm, true, false, CurrTerm, idThesaur) );   
    }
else
    {
    String IdDel=Req.getParameter(PDThesaur.fPDID);      
    TmpTerm.setPDId(IdDel);
    TmpTerm.delete();
    out.println("OK"+TmpTerm.getPDId());
    }
} catch (Exception ex)
    {
    if (CurrThes!=null) //return Form
        SParent.GenErrorForm(ex.getLocalizedMessage());
    else    
        PrepareError(Req, ex.getLocalizedMessage(), out);
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
return "DelTerm Servlet";
}
//-----------------------------------------------------------------------------------------------
}
