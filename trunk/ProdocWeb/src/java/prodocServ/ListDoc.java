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

package prodocServ;

import html.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import prodoc.Attribute;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.Record;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class ListDoc extends ServParent
{

static private String List=PDDocs.fREPOSIT+"/"+PDDocs.fSTATUS+"/"+PDDocs.fNAME;
//-----------------------------------------------------------------------------------------------
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
HttpSession Sess=Req.getSession(true);
String DocId=(String)Req.getParameter("DocId");
HashMap ListDel=(HashMap)SParent.getListDocs(Req);
Record Rec=(Record) ListDel.get(DocId);
Element TabFields=GenTabFields(Req, Rec);
out.println(TabFields.ToHtml(Sess));
}
//-----------------------------------------------------------------------------------------------
/** Returns a short description of the servlet.
 * @return 
 */
@Override
public String getServletInfo()
{
return "Servlet AJAX returning atributes Doc";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListDoc");
}
//-----------------------------------------------------------------------------------------------
static public Element GenTabFields(HttpServletRequest Req, Record Rec) throws PDException
{
Rec.initList();
Attribute Attr=Rec.nextAttr();
ArrayList FL=new ArrayList();
while (Attr!=null)
    {
    if (!List.contains(Attr.getName()))
        {
        FL.add(Attr);
        }
    Attr=Rec.nextAttr();
    }
if (FL.isEmpty())
    {
    return(new Element(" "));
    }
Table AditionFieldsTab=new Table(4, FL.size(), 0);
AditionFieldsTab.setWidth(-100);
AditionFieldsTab.setCellPadding(0);
AditionFieldsTab.setCSSClass("CentArea");
//AditionFieldsTab.setCSSClass("FFormularios");
AditionFieldsTab.getCelda(0,0).setWidth(-5);
AditionFieldsTab.getCelda(1,0).setWidth(-45);
AditionFieldsTab.getCelda(2,0).setWidth(-45);
Field FieldHtml;
for (int i = 0; i < FL.size(); i++)
    {
    Attr= (Attribute)FL.get(i);
    AditionFieldsTab.getCelda(1,i).AddElem(new Element(TT(Req, Attr.getUserName())+":"));
    if (Attr.getType()==Attribute.tBOOLEAN)
        {
        FieldHtml=new FieldCheck(Attr.getName());
        FieldHtml.setCSSClass("FFormInputCheck");
        }
    else
        {
        FieldHtml=new FieldText(Attr.getName());
        }
    if (Attr.getType()==Attribute.tDATE)
        {
        FieldHtml.setValue(SParent.FormatDate(Req, (Date)Attr.getValue()));
        FieldHtml.setCSSClass("FFormInputDate");
        }
    else if (Attr.getType()==Attribute.tTIMESTAMP)
        {
        FieldHtml.setValue(SParent.FormatTS(Req, (Date)Attr.getValue()));
        FieldHtml.setCSSClass("FFormInputTS");
        }
    else
        {
        FieldHtml.setValue(Attr.Export());
        FieldHtml.setCSSClass("FFormInput");
        }
    FieldHtml.setActivado(false);
    AditionFieldsTab.getCelda(2,i).AddElem(FieldHtml);
    }
HiperlinkImag HHelp;
HHelp=new HiperlinkImag("img/"+SParent.getStyle(Req)+"help.jpg", "Help", "help/"+SParent.getLang(Req)+"/ListVersions.html", "");
HHelp.setTarget("_blank");
AditionFieldsTab.getCelda(3,FL.size()-1).AddElem(HHelp);
return(AditionFieldsTab);
}
//-----------------------------------------------------------------------------------------------

}
