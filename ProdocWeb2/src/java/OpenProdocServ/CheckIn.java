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
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class CheckIn extends SParent
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
    Attribute Attr;
    TmpDoc.LoadCurrent(CurrDoc);
    Attr=TmpDoc.getRecord().getAttr(PDDocs.fVERSION);
    out.println(
    "[" +
    "{type: \"label\", label: \""+TT(Req, "Checkin_Selected_Document")+"\"}," +
    "{type: \"label\", label: \""+TmpDoc.getTitle()+" ( "+TmpDoc.getVersion()+" ) "+"\"}," +
    "{type: \"input\", name: \""+PDDocs.fVERSION+"\", label: \""+TT(Req, Attr.getUserName())
                   +"\", required: true, tooltip:\""+TT(Req, Attr.getDescription())+"\", inputWidth: 150, maxLength:"+Attr.getLongStr()+"}," +
    "{type: \"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
        "{type: \"newcolumn\", offset:20 }," +
        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}," +
        "{type: \"hidden\", name:\"CurrDoc\", value: \""+CurrDoc+"\"}" +
    "]} ];");
    }
else
    {
    try {    
    CurrDoc=Req.getParameter("CurrDoc");    
    String Verlabel=Req.getParameter(PDDocs.fVERSION); 
    TmpDoc.Load(CurrDoc);
    TmpDoc.Checkin(Verlabel);
    out.println("OK");
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
return "CheckIn Servlet";
}
//-----------------------------------------------------------------------------------------------
}
