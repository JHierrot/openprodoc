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
import prodocUI.forms.FMantThes;

/**
 *
 * @author jhierrot
 */
public class ModThes extends SParent
{
static private String List=PDThesaur.fPARENTID+"/"+PDThesaur.fPDID+"/"+PDThesaur.fPDAUTOR+"/"+PDThesaur.fPDDATE;

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
DriverGeneric PDSession=SParent.getSessOPD(Req)  ;
PDThesaur F = new PDThesaur(PDSession);
F.Load(getActTermId(Req));
if (!Reading(Req))
    {
    Record Rec=F.getRecord();
    FMantThes f=new FMantThes(Req, FMantThes.EDIMOD, Rec, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    }
else
    {
    try {
    boolean Acept=ModThes(Req);
    if (Acept)
        {
        String NewTit=Req.getParameter(PDThesaur.fNAME);
        GenListThes(Req, out, LAST_FORM, "parent.ThesUpdate('"+F.getPDId()+"','"+NewTit+"')", null);
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
return "ModThes Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ModThes");
}
//-----------------------------------------------------------------------------------------------
private boolean ModThes(HttpServletRequest Req) throws PDException
{
String Acept=Req.getParameter("BOk");
if (Acept==null || Acept.length()==0)
    return(false);
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDThesaur F = new PDThesaur(PDSession);
F.Load(getActTermId(Req));
Record Rec=F.getRecord();
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
F.update();
SParent.setActTerm(Req, F.getRecord());
return(true);
}
//-----------------------------------------------------------------------------------------------
}
