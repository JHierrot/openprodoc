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
import prodoc.PDServer;

/**
 *
 * @author jhierrot
 */
public class About extends SParent
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
PDServer Serv=new PDServer(PDSession);
Serv.Load("Prodoc");
out.println("[" +
            "{type:\"settings\", labelHeight:8}," +
            "{type: \"image\", name: \"OPD\", url: \"img/LogoProdoc.jpg\"}," +
            "{type: \"label\", label: \"- Soft. Management OpenProdoc: "+SParent.getVersion()+"\"}," +
            "{type: \"label\", label: \"- OPD Engine: "+DriverGeneric.getVersion()+"\"}," +
            "{type: \"label\", label: \"- OPD Repository: "+Serv.getVersion()+"\"}," +
            "{type: \"label\", label: \"- Server Url: "+PDSession.getURL()+"\"}," +
            "{type: \"label\", label: \"\"}," +
            "{type: \"label\", label: \"Technical Design and Development:  Joaquín Hierro\"}," +
            "{type: \"label\", label: \"DHTMLX: license: Dinamenta UAB\"}," +
            "{type: \"label\", label: \"commons-net.ftp, commons-fileupload, Tika, Lucene: licence Apache Software\"}," +
            "{type: \"label\", label: \"Icons: https://icons8.com\"}" +
            "];");
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "About Servlet";
}
//-----------------------------------------------------------------------------------------------
}
