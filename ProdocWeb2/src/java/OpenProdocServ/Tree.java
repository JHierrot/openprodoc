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

import OpenProdocUI.SMain;
import OpenProdocUI.SParent;
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
public class Tree extends SParent
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
static public String getUrlServlet()
{
return("Tree");
}
//-----------------------------------------------------------------------------------------------

private String GenTree(HttpServletRequest Req) 
{
StringBuilder FolderTree=new StringBuilder(3000);
FolderTree.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
DriverGeneric PDSession=SMain.getSessOPD(Req);
String FoldId=Req.getParameter("id");
try {
PDFolders CurFold = new PDFolders(PDSession);
if (FoldId==null)
    { 
    FoldId="RootFolder";    
    FolderTree.append("<tree id=\"0\" radio=\"1\"><item child=\"1\" id=\"RootFolder\" text=\"RootFolder\" open=\"1\">");
    }
else
    FolderTree.append("<tree id=\"").append(FoldId).append("\" open=\"1\">");
// FolderTree.append("<item text=\"").append(CurFold.getTitle()).append("\" id=\"").append(FoldId).append("\" open=\"1\">");
HashSet Child =CurFold.getListDirectDescendList(FoldId);
for (Iterator it = Child.iterator(); it.hasNext();)
    {
    String ChildId=(String)it.next();
     if (ChildId.compareTo(PDFolders.ROOTFOLDER)==0)
        continue;
    PDFolders ChildFolder=new PDFolders(PDSession);
    ChildFolder.Load(ChildId);
    FolderTree.append("<item child=\"1\" id=\"").append(ChildFolder.getPDId()).append("\" text=\"").append(ChildFolder.getTitle()).append("\"></item>");
    }
} catch(Exception Ex)
    {  
    }
if (FoldId.equals("RootFolder"))
    FolderTree.append("</item>");
FolderTree.append("</tree>");
return(FolderTree.toString());
}
//-----------------------------------------------------------------------------------------------

}
