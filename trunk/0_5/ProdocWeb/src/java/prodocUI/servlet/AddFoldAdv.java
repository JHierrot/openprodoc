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
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;
import prodocUI.forms.FMantFoldAdv;

/**
 *
 * @author jhierrot
 */
public class AddFoldAdv extends SParent
{

static private String List=PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE;

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
if (!Reading(Req))
    {
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    PDFolders F = new PDFolders(PDSession);
    Record Rec=F.getRecord();
    FMantFoldAdv f=new FMantFoldAdv(Req, FMantFoldAdv.ADDMOD, Rec, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    String NewId=AddFolder(Req);
    if (NewId!=null)
        {
        String Title=Req.getParameter(PDFolders.fTITLE);
        GenListForm(Req, out, LAST_FORM, "parent.Append('"+getActFolderId(Req)+"','"+NewId+"', '"+Title+"')", null);
        }
    else
        GenListForm(Req, out, LAST_FORM, null, null);
    } catch (PDException ex)
        {
        ShowMessage( Req,  out, SParent.TT(Req, ex.getLocalizedMessage()));
        }
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
return "AddFoldAdv Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("AddFoldAdv");
}
//-----------------------------------------------------------------------------------------------
private String AddFolder(HttpServletRequest Req) throws PDException
{
String Acept=Req.getParameter("BOk");
if (Acept==null || Acept.length()==0)
    return(null);
DriverGeneric PDSession=SParent.getSessOPD(Req);
String FType=Req.getParameter(PDFolders.fFOLDTYPE);
PDFolders F;
if (FType==null)
    F = new PDFolders(PDSession);
else
    F = new PDFolders(PDSession, FType);
Record Rec=F.getRecSum();
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    if (!List.contains(Attr.getName()))
        {
        String Val=Req.getParameter(Attr.getName());
        if (Attr.getType()==Attribute.tBOOLEAN)
            {
            if(Val == null)
                Attr.setValue(false);
            else
                Attr.setValue(true);
            }
        else if(Val != null)
            {
            SParent.FillAttr(Req, Attr, Val, false);
            }
        }
    Attr=Rec.nextAttr();
    }
F.assignValues(Rec);
F.setParentId(getActFolderId(Req));
F.insert();
String Id=F.getPDId();
return(Id);
}
//-----------------------------------------------------------------------------------------------

}
