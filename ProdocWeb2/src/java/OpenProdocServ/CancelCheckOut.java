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
import static OpenProdocUI.SParent.getSessOPD;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import prodoc.DriverGeneric;
import prodoc.PDDocs;


/**
 *
 * @author jhierrot
 */
public class CancelCheckOut extends SParent
{   
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(200);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><status>");
try {
String IdDoc=Req.getParameter("D");
if (IdDoc==null || IdDoc.length()==0)
   Resp.append(TT(Req, "Document_unselected")).append("</status>"); 
else
    {
    DriverGeneric Sess=getSessOPD(Req);
    PDDocs Doc=new PDDocs(Sess);
    Doc.setPDId(IdDoc);
    Doc.CancelCheckout();
    Resp.append("OK</status>"); 
    }
out.println(Resp.toString());
} catch (Exception Ex)
        {
        String[] Ms = Ex.getLocalizedMessage().split(":");
        for (int i = 0; i < Ms.length; i++)
            {
            Resp.append(TT(Req,Ms[i])).append(":"); 
            }
        Resp.append("</status>"); 
        out.println(Resp.toString());
        }
finally {
out.close();
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
return "CancelCheckOut Servlet";
}
//-----------------------------------------------------------------------------------------------
}
