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

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.TT;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import javax.servlet.http.*;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDRepository;
import prodoc.Record;


/**
 *
 * @author jhierrot
 */
public class Status extends SParent
{
static final String FORMAT="###,###,###,###";    
private String Format(long val)
{
DecimalFormat Formatter = new DecimalFormat(FORMAT);  
return (Formatter.format(val));
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
DriverGeneric PDSession=SParent.getSessOPD(Req);
StringBuilder SB=new StringBuilder(3000);
SB.append("[{type:\"settings\", offsetLeft:10, labelWidth: 150},");
SB.append("{type: \"label\", label: \"Status\"},");
Runtime RT = Runtime.getRuntime();
SB.append("{type: \"input\", name: \"JV\", label: \"Java Version\", readonly:1, value:\"").append(System.getProperty("java.version")).append("\"},");
SB.append("{type: \"input\", name: \"UN\", label: \"User Name\", readonly:1, value:\"").append(System.getProperty("user.name")).append("\"},");
SB.append("{type: \"input\", name: \"FM\", label: \"Free.Memory\", readonly:1, value:\"").append(Format(RT.freeMemory())).append("\"},");
SB.append("{type: \"input\", name: \"TM\", label: \"Tot.Memory\", readonly:1, value:\"").append(Format(RT.totalMemory())).append("\"},");
SB.append("{type: \"input\", name: \"MM\", label: \"Max.Memory\", readonly:1, value:\"").append(Format(RT.maxMemory())).append("\"},");
SB.append("{type: \"input\", name: \"TMPDIR\", label: \"TemDir\", readonly:1, value:\"").append(System.getProperty("java.io.tmpdir").replace("\\", "/")).append("\", inputWidth:450},");
File f=new File(System.getProperty("java.io.tmpdir"));
SB.append("{type: \"input\", name: \"FREETMPDIR\", label: \"Free Space TemDir\", readonly:1, value:\"").append(Format(f.getFreeSpace())).append("\"},");
File f2=new File(".");
SB.append("{type: \"input\", name: \"CurDir\", label: \"Current Dir\", readonly:1, value:\"").append(f2.getAbsolutePath().replace("\\", "/")).append("\", inputWidth:450},");
PDRepository Rep=new PDRepository(PDSession);
Cursor AllRep = Rep.getAll();
Record R=PDSession.NextRec(AllRep);
while (R!=null)
    {
    if (((String)R.getAttr(PDRepository.fREPTYPE).getValue()).equals(PDRepository.tFS))
        {
        try {    
        File fr=new File((String)R.getAttr(PDRepository.fURL).getValue());
        String RepName=(String)R.getAttr(PDRepository.fNAME).getValue();
        SB.append("{type: \"input\", name: \"Rep_"+RepName+"\", label: \""+RepName+"\", readonly:1, value:\"").append(Format(fr.getFreeSpace())).append("\"},");
        } catch (Exception Ex){}
        }
    R=PDSession.NextRec(AllRep);
    }
SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"}");
SB.append(" ];");
out.println(SB);
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "Status Servlet";
}
//-----------------------------------------------------------------------------------------------

}
