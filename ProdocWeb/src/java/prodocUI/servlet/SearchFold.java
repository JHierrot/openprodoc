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

// TODO Cambiar implementaciónArbol para situar desde búsqueda carpetas

package prodocUI.servlet;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;
import prodocUI.forms.FSearchFoldAdv;

/**
 *
 * @author jhierrot
 */
public class SearchFold extends SParent
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
Cursor Folders=null;
if (Reading(Req)) // analize query
    {
    Folders=SearchFolder(Req);
    if (Folders==null) // cancel
        {
        GenListForm(Req, out, LISTDOC_FORM, null, null);
        return;
        }
    }
else
    CleanConds(Req); // directly from menu
GenListForm(Req, out, SEARCHFOLD_FORM, null, Folders);
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
return "SearchFold Servlet";
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @return
 */
static public String getUrlServlet()
{
return("SearchFold");
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 * @throws PDException
 */
private Cursor SearchFolder(HttpServletRequest Req) throws PDException
{
String Acept=Req.getParameter("BOk");
if (Acept==null || Acept.length()==0)
    return(null);
DriverGeneric PDSession=getSessOPD(Req);
String FType=Req.getParameter(PDFolders.fFOLDTYPE);
PDFolders F;
if (FType==null)
    F = new PDFolders(PDSession);
else
    F = new PDFolders(PDSession, FType);
String Acl=Req.getParameter(PDFolders.fACL);
String SubTypes=Req.getParameter("Subtypes");
String SubFolders=Req.getParameter("SubFolders");
Conditions Cond=new Conditions();
Record Rec=F.getRecSum();
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    if (Attr.getName().equals(PDFolders.fFOLDTYPE))
        {
        Attr=Rec.nextAttr();
        continue;
        }
    String Val=Req.getParameter(Attr.getName());
    String Comp=Req.getParameter(FSearchFoldAdv.COMP+Attr.getName());
    SParent.getOperMap(Req).put(FSearchFoldAdv.COMP+Attr.getName(), Comp);
    if (!(Val == null || Val.length()==0 || Val != null && Attr.getName().equals(PDFolders.fACL) && Val.equals("None")))
        {
        int Oper=Integer.parseInt(Comp);
        Cond.addCondition(SParent.FillCond(Req, Attr, Val, Oper));
        }
    Attr=Rec.nextAttr();
    }
SaveConds(Req, FType, Cond, (SubTypes!=null), (SubFolders!=null), false, getActFolderId(Req), null, Rec, null);
Cursor c=F.Search(FType, Cond, (SubTypes!=null), (SubFolders!=null), getActFolderId(Req), null);
return(c);
}
//-----------------------------------------------------------------------------------------------

}
