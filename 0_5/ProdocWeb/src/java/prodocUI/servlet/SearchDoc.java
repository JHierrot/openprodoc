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

package prodocUI.servlet;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDDocs;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SearchDoc extends SParent
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
try {
Cursor DocsList=null;
if (Reading(Req)) // analize query
    {
    DocsList=SearchDoc(Req);
    if (DocsList==null) // cancel
        {
        GenListForm(Req, out, LISTDOC_FORM, null, null);
        return;
        }
    }
else
    CleanConds(Req); // directly from menu
GenListForm(Req, out, SEARCHDOC_FORM, null, DocsList);
} catch (PDException ex)
    {
    ShowMessage( Req,  out, SParent.TT(Req, ex.getLocalizedMessage()));
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
return "SearchDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @return
 */
static public String getUrlServlet()
{
return("SearchDoc");
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @return
 * @throws PDException
 */
private Cursor SearchDoc(HttpServletRequest Req) throws PDException
{
String Acept=Req.getParameter("BOk");
if (Acept==null || Acept.length()==0)
    return(null);
DriverGeneric PDSession=getSessOPD(Req);
String FType=Req.getParameter(PDDocs.fDOCTYPE);
PDDocs F;
if (FType==null)
    F = new PDDocs(PDSession);
else
    F = new PDDocs(PDSession, FType);
String Acl=Req.getParameter(PDDocs.fACL);
String SubTypes=Req.getParameter("Subtypes");
String SubFolders=Req.getParameter("SubFolders");
String Versions=Req.getParameter("Versions");
Conditions Cond=new Conditions();
Record Rec=F.getRecSum();
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    if (Attr.getName().equals(PDDocs.fDOCTYPE))
        {
        Attr=Rec.nextAttr();
        continue;
        }
    String Val=Req.getParameter(Attr.getName());
    if (!(Val == null || Val.length()==0 || Val != null && Attr.getName().equals(PDDocs.fACL) && Val.equals("None")))
        {
        Cond.addCondition(SParent.FillCond(Req, Attr, Val));
        }
    Attr=Rec.nextAttr();
    }
SaveConds(Req, FType, Cond, (SubTypes!=null), (SubFolders!=null), (Versions!=null), getActFolderId(Req), null, Rec);
Cursor c=F.Search(FType, Cond, (SubTypes!=null), (SubFolders!=null), (Versions!=null), getActFolderId(Req), null);
return(c);
}
//-----------------------------------------------------------------------------------------------

}
