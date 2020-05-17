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
 * author: Joaquin Hierro      2019
 * 
 */

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class FilterForm extends SParent
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
PDFolders TmpFold=null;    
StringBuilder Form=new StringBuilder(1000);
String Filt=Req.getParameter("Filt");
try {
Form.append("[ {type: \"settings\", position: \"label-left\", offsetLeft:10, labelWidth: 180, inputWidth: 250},");
Form.append("{type: \"label\", label: \"").append(TT(Req, "Filter_"+Filt)).append("\"},");
Vector<String> FiltProdFields = null;
if (Filt.equalsIgnoreCase("Products"))
    {
    FiltProdFields=getProductFieldsFilter();
    TmpFold=new PDFolders(SParent.getSessOPD(Req), getProductType());
    }
else if (Filt.equalsIgnoreCase("Issues"))
    {
    FiltProdFields=getIssuesFieldsFilter();
    TmpFold=new PDFolders(SParent.getSessOPD(Req), getIssuesType());
    }
else if (Filt.equalsIgnoreCase("Depart"))
    {
    FiltProdFields=getDepartFieldsFilter();
    TmpFold=new PDFolders(SParent.getSessOPD(Req), getDepartmentType());
    }
else if (Filt.equalsIgnoreCase("SoftProv"))
    {
    FiltProdFields=getSoftProvFieldsFilter();
    TmpFold=new PDFolders(SParent.getSessOPD(Req), getSoftProviderType());
    }

for (int i = 0; i < FiltProdFields.size(); i++)
    Form.append(GenInput(Req, TmpFold.getRecord().getAttr(FiltProdFields.elementAt(i)), false, false));
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("];");
} catch (Exception Ex)
    {
    Form=new StringBuilder(1000);
    Form.append("[");
    Form.append("{type: \"label\", label: \"").append("Error:").append(Ex.getLocalizedMessage()).append("\"}");
    Form.append("];");
    }
out.println(Form);

}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ProductsFilt Servlet";
}
//-----------------------------------------------------------------------------------------------
}
