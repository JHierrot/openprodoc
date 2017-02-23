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
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class TermDetails extends SParent
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
DriverGeneric PDSession=SParent.getSessOPD(Req);
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(2000);
// Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><row>");
Resp.append("<row>");
try {
PDThesaur Term=new PDThesaur(PDSession);
String CurrTerm=Req.getParameter("Term");
Term.Load(CurrTerm);
Resp.append("<col>").append(Term.getName()).append("</col>");       
Resp.append("<col>").append(Term.getDescription()).append("</col>");       
Resp.append("<col>").append(Term.getUse()==null?"":Term.getUse()).append("</col>");       
Resp.append("<col>").append(Term.getSCN()).append("</col>");       
Resp.append("<col>").append(Term.getLang()).append("</col></row>");
} catch (PDException ex)
    {
    Resp.append("<col>Error</col></row>");
    }
out.println( Resp );   
out.close();
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "TermDetails Servlet";
}
//-----------------------------------------------------------------------------------------------
}
