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
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class DelThes extends SParent
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
if (CurrThes!=null)
    {
    TmpThes.Load(CurrThes);
    out.println( GenerateCompleteThesForm("Delete_Thesaurus", Req, PDSession, TmpThes, true, false) );   
    }
else
    {
    try {    
    String IdDel=Req.getParameter(PDThesaur.fPDID);      
    TmpThes.setPDId(IdDel);
    TmpThes.delete();
    out.println("OK");
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
return "DelThes Servlet";
}
//-----------------------------------------------------------------------------------------------
}
