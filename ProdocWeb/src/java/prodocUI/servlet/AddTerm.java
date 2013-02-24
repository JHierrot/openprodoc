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
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.forms.FMantTerm;


/**
 *
 * @author jhierrot
 */
public class AddTerm extends SParent
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
    FMantTerm f=new FMantTerm(Req, FMantTerm.ADDMOD, null, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    FMantTerm f=new FMantTerm(Req, FMantTerm.ADDMOD, null, getUrlServlet() );
    String Acept=f.OkButton.getValue(Req);
    if (Acept!=null && Acept.length()!=0)
        {
        String Id=AddThes(Req);
        String TermName=f.TermName.getValue(Req);
        GenListThes(Req, out, LAST_FORM,  "parent.ThesAppend('"+getActTermId(Req)+"','"+Id+"','"+TermName+"')", null);
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
return "Add Term Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("AddTerm");
}
//-----------------------------------------------------------------------------------------------
private String AddThes(HttpServletRequest Req) throws PDException
{
PDThesaur Newterm;
Record Rec;
DriverGeneric PDSession=SParent.getSessOPD(Req);
Newterm = new PDThesaur(PDSession);
Rec=Newterm.getRecord();
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
Newterm.assignValues(Rec);
Newterm.setParentId(getActTermId(Req));
Newterm.insert();
return(Newterm.getPDId());
}
//-----------------------------------------------------------------------------------------------

}
