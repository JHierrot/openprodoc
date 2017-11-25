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
public class ModACLDoc extends SParent
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
String CurrDoc=Req.getParameter(PDDocs.fPDID);
String ACL=Req.getParameter(PDDocs.fACL);
try {
TmpDoc.setPDId(CurrDoc);
TmpDoc.ChangeACL(ACL);
out.println("OK");    
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
