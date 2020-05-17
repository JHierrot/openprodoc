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
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;

/**
 *
 * @author jhierrot
 */
public class DelDoc extends SParent
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
PDDocs TmpDoc=new PDDocs(PDSession);
String CurrDoc=Req.getParameter("D");
if (CurrDoc!=null)
    {
    TmpDoc.LoadFull(CurrDoc);
    out.println( GenerateCompleteDocForm("Delete_Document", Req, PDSession, CurrDoc, TmpDoc.getDocType(), TmpDoc.getRecSum(), true, false, null, false) );   
    }
else
    {
    try {    
    String IdDel=Req.getParameter(PDDocs.fPDID);
    TmpDoc.setPDId(IdDel);
    TmpDoc.delete();
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
return "DelDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
}
