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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;
import prodocUI.forms.FMantFoldAdv;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class ListTypeFolds extends ServParent
{

static private String List=PDFolders.fACL+"/"+PDFolders.fFOLDTYPE+"/"+PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fTITLE+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE;

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
HttpSession Sess=Req.getSession(true);
DriverGeneric PDSession=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
String Typ=(String)Req.getParameter("Type");
PDFolders F = new PDFolders(PDSession, Typ);
Record Rec=F.getRecSum();
Element TabFields=GenTabFields(Req, Rec, FMantFoldAdv.ADDMOD);
out.println(TabFields.ToHtml(Sess));
}
//-----------------------------------------------------------------------------------------------
/** Returns a short description of the servlet.
 * @return
 */
@Override
public String getServletInfo()
{
return "Servlet AJAX returning atributes of type of Folder";
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @return
 */
static public String getUrlServlet()
{
return("ListTypeFolds");
}
//-----------------------------------------------------------------------------------------------
static public Element GenTabFields(HttpServletRequest Req, Record Rec, int pMode) throws PDException
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
AditionFieldsTab.setCellPadding(5);
AditionFieldsTab.setCSSClass("FFormularios");
AditionFieldsTab.getCelda(0,0).setWidth(-25);
AditionFieldsTab.getCelda(0,0).setHeight(30);
Field FieldHtml;
for (int i = 0; i < FL.size(); i++)
    {
    Attr= (Attribute)FL.get(i);
    AditionFieldsTab.getCelda(1,i).AddElem(new Element(TT(Req, Attr.getUserName())+":"));
    if (Attr.isRequired())
        AditionFieldsTab.getCelda(1,i).setCSSClass("FFormulReq");
    if (Attr.isMultivalued())
        {
        FieldHtml=new FieldMultiOPD(Attr.getName());
        FieldHtml.setCSSId(Attr.getName()+"_"+i);
        }
    else if (Attr.getType()==Attribute.tBOOLEAN)
        {
        FieldHtml=new FieldCheck(Attr.getName());
        }
    else
        {
        FieldHtml=new FieldText(Attr.getName());
        ((FieldText)FieldHtml).setMaxSize(Attr.getLongStr());
        }
    if (Attr.getType()==Attribute.tDATE)
        {
        FieldHtml.setMensStatus(Attr.getDescription()+" ( "+SParent.getFormatDate(Req) +" )");
        FieldHtml.setValue(SParent.FormatDate(Req, (Date)Attr.getValue()));
        FieldHtml.setCSSClass("FFormInputDate");
        }
    else if (Attr.getType()==Attribute.tTIMESTAMP)
        {
        FieldHtml.setMensStatus(Attr.getDescription()+" ( "+SParent.getFormatTS(Req) +" )");
        FieldHtml.setValue(SParent.FormatTS(Req, (Date)Attr.getValue()));
        FieldHtml.setCSSClass("FFormInputTS");
        }
    else
        {
        FieldHtml.setMensStatus(Attr.getDescription());
        FieldHtml.setValue(Attr.Export());
        if (Attr.getType()==Attribute.tBOOLEAN)
            {
            FieldHtml.setCSSClass("FFormInputCheck");
            }
        else
            {
            FieldHtml.setCSSClass("FFormInput");
            }
        }
    if (pMode==FMantFoldAdv.DELMOD || pMode==FMantFoldAdv.EDIMOD && !Attr.isModifAllowed())
        FieldHtml.setActivado(false);
//    FieldHtml.setCSSClass("FFormInput");
    AditionFieldsTab.getCelda(2,i).AddElem(FieldHtml);
    }
return(AditionFieldsTab);
}
//-----------------------------------------------------------------------------------------------
}
