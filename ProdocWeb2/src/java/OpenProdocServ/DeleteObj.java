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
import static OpenProdocUI.SParent.getSessOPD;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.PDObjDefs;

/**
 *
 * @author jhierrot
 */
public class DeleteObj extends SParent
{
/**
 *
 */
public static final String[] LTyp={"Integer", "Float", "String", "Date", "Boolean", "TimeStamp", "Thesaur"};
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
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
try {
String S;
//Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
String Id=Req.getParameter("Id");
if (Id==null || Id.length()==0)
    Resp.append("<status>Undefined Type Id</status>");
else
    {
    PDObjDefs Def=new PDObjDefs(getSessOPD(Req));
    Def.Load(Id);
    Def.DeleteObjectTables(Id);
    Resp.append("<status>OK</status>");
    }   
out.println(Resp.toString());
} catch (Exception Ex)
        {
//        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.print("<status>");
        PrepareError(Req, Ex.getLocalizedMessage(), out);
        out.println("</status>");
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
return "DeleteObj Servlet";
}
//-----------------------------------------------------------------------------------------------
}
