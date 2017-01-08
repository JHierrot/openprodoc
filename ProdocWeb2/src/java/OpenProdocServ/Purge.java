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
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;

/**
 *
 * @author jhierrot
 */
public class Purge extends SParent
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
StringBuilder Resp=new StringBuilder(100);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
String DocId=Req.getParameter("Id");  
try {
String[] Ids = DocId.split("\\|");
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDDocs TmpDoc=new PDDocs(PDSession);
TmpDoc.Purge(Ids[1], Ids[0]);
Resp.append("<status>OK").append(DocId).append("</status>");
} catch (PDException ex)
    {
    Resp.append("<status>").append(DocId).append(TT(Req,ex.getLocalizedMessage())).append("</status>");
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
return "Purge Servlet";
}
//-----------------------------------------------------------------------------------------------
}
