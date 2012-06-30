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
 * author: Joaquin Hierro      2011
 * 
 */

package prodocServ;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDObjDefs;
import prodoc.PDRepository;


/**
 *
 * @author jhierrot
 * @version
 */
public class ListTypeRep extends ServParent
{

//-----------------------------------------------------------------------------------------------
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
DriverGeneric Drv=getSession(Req);
String Typ=(String)Req.getParameter("Type");
PDObjDefs Def=new PDObjDefs(Drv);
Def.Load(Typ);
PDRepository Rep=new PDRepository(Drv);
Rep.Load(Def.getReposit());
out.print(Rep.getRepType());
}
//-----------------------------------------------------------------------------------------------
/** Returns a short description of the servlet.
 * @return 
 */
@Override
public String getServletInfo()
{
return "Servlet AJAX returning atributes of type of Rep";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListTypeRep");
}

}
