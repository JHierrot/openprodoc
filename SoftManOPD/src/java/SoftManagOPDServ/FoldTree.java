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
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class FoldTree extends SParent
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
out.println(GenTree(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "Tree Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenTree(HttpServletRequest Req) 
{
StringBuilder FolderTree=new StringBuilder(3000);
DriverGeneric PDSession=getSessOPD(Req);
String FoldId=Req.getParameter("id");
boolean Close=false;
try {
PDFolders CurFold = new PDFolders(PDSession);
if (FoldId==null)
    { 
    FoldId=Req.getParameter("FoldId");
    CurFold.Load(FoldId);
    Close=true;
    FolderTree.append("<tree id=\"0\" radio=\"1\"><item child=\"1\" id=\"").append(FoldId).append("\" text=\"").append(CurFold.getTitle()).append("\" open=\"1\">");
    }
else
    FolderTree.append("<tree id=\"").append(FoldId).append("\" open=\"1\">");
HashSet Child =CurFold.getListDirectDescendList(FoldId);
for (Iterator it = Child.iterator(); it.hasNext();)
    {
    String ChildId=(String)it.next();
     if (ChildId.compareTo(PDFolders.ROOTFOLDER)==0)
        continue;
    PDFolders ChildFolder=new PDFolders(PDSession);
    ChildFolder.LoadRefresh(ChildId);
    FolderTree.append("<item child=\"1\" id=\"").append(ChildFolder.getPDId()).append("\" text=\"").append(EscapeTree(ChildFolder.getTitle()+"("+ChildFolder.getFolderType()+")")).append("\"></item>");
    }
} catch(Exception Ex)
    {  
    }
if (Close)
    FolderTree.append("</item>");
FolderTree.append("</tree>");
return(FolderTree.toString());
}
//-----------------------------------------------------------------------------------------------

}
