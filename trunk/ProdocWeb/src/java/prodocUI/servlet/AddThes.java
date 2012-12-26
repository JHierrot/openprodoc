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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDMimeType;
import prodoc.Record;
import prodocUI.forms.FMantThes;

/**
 *
 * @author jhierrot
 */
public class AddThes extends SParent
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
if (!Reading(Req))
    {
    FMantThes f=new FMantThes(Req, FMantThes.ADDMOD, null, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    String FoldTitle;
    FMantThes f=new FMantThes(Req, FMantThes.ADDMOD, null, getUrlServlet() );
    String Acept=f.OkButton.getValue(Req);
    if (Acept!=null && Acept.length()!=0)
        {
        ModThes(Req);
        GenListThes(Req, out, LAST_FORM,  null, null);
        }
    else
        GenListThes(Req, out, LAST_FORM, null, null);
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
return "Add Thesaurus Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("AddThes");
}
//-----------------------------------------------------------------------------------------------
private boolean ModThes(HttpServletRequest Req) throws PDException
{
PDDocs Doc;
Record Rec;
String FileName=null;
InputStream FileData=null;
HashMap ListFields=new HashMap();
    {
    String Acept=Req.getParameter("BOk");
    if (Acept==null || Acept.length()==0)
        return(false);
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    Doc = new PDDocs(PDSession);
    Doc.LoadFull(getActDocId(Req));
    Rec=Doc.getRecSum();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
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
        Attr=Rec.nextAttr();
        }
    }
Doc.assignValues(Rec);
String RefFile=(String) ListFields.get(PDDocs.fNAME+"_");
if (RefFile!=null && RefFile.length()!=0)
    Doc.setFile(RefFile);
else
    {
    if (FileName!=null && FileName.length()>0)
        {
        PDMimeType mt=new PDMimeType(SParent.getSessOPD(Req));
        Doc.setMimeType(mt.SolveName(FileName));
        Doc.setName(FileName);
        if (FileData!=null)
            Doc.setStream(FileData);    
        }
    }
Doc.setParentId(getActFolderId(Req));
Doc.update();
return(true);
}
//-----------------------------------------------------------------------------------------------

}
