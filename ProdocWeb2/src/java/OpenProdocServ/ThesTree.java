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
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class ThesTree extends SParent
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
out.println(GenTreeThes(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ThesTree Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenTreeThes(HttpServletRequest Req) 
{
StringBuilder ThesTree=new StringBuilder(3000);
//ThesTree.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
DriverGeneric PDSession=getSessOPD(Req);
String ThesaurId=Req.getParameter("ThesId");
String ThesId=Req.getParameter("id");
try {
PDThesaur CurThes = new PDThesaur(PDSession);
if (ThesaurId!=null)
    { 
//    ThesId=PDThesaur.ROOTTERM;  
    CurThes.Load(ThesaurId);
    ThesTree.append("<tree id=\"0\" radio=\"1\"><item child=\"1\" id=\"").append(CurThes.getPDId()).append("\" text=\"").append(CurThes.getName()).append("\" open=\"1\">");
    }
else
    ThesTree.append("<tree id=\"").append(ThesId).append("\" open=\"1\">");
HashSet Child =CurThes.getListDirectDescendList(ThesId);
for (Iterator it = Child.iterator(); it.hasNext();)
    {
    String ChildId=(String)it.next();
     if (ChildId.compareTo(PDThesaur.ROOTTERM)==0)
        continue;
    PDThesaur ChildTerm=new PDThesaur(PDSession);
    ChildTerm.Load(ChildId);
    ThesTree.append("<item child=\"1\" id=\"").append(ChildTerm.getPDId()).append("\" text=\"").append(ChildTerm.getName()).append("\"></item>");
    }
} catch(Exception Ex)
    {  
    }
//if (ThesId.equals(PDThesaur.ROOTTERM))
if (ThesaurId!=null)
    ThesTree.append("</item>");
ThesTree.append("</tree>");
return(ThesTree.toString());
}
//-----------------------------------------------------------------------------------------------

}
