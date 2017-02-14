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
import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class DelFold extends SParent
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
PDFolders TmpFold=new PDFolders(PDSession);
String CurrFold=Req.getParameter("F");
if (CurrFold!=null)
    {
    TmpFold.LoadFull(CurrFold);
    out.println( GenerateCompleteFoldForm("Delete_Folder", Req, PDSession, CurrFold, TmpFold.getFolderType(), TmpFold.getRecSum(), true, false, null) );   
    }
else
    {
    try {    
    String IdDel=Req.getParameter("CurrFold");      
    TmpFold.setPDId(IdDel);
    TmpFold.delete();
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
return "DelFold Servlet";
}
//-----------------------------------------------------------------------------------------------
}
