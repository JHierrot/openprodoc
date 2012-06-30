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

package prodocUI.forms;


import html.*;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.PDException;
import prodoc.PDDocs;
import prodoc.Record;
import prodocServ.ListTypeDocs;
import prodocServ.ServParent;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FMantDocAdv extends FFormBase
{
public FieldText  DocTitle;
public FieldText  DocDate;
public FieldFile DocFile;
public FieldText DocFile2;
//public FieldCombo ListMime;
FieldCombo ListTip;
FieldCombo ListACL;


/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FMantDocAdv(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Maintenance_Documents"), pMode, pRec);
AddJS("Types.js");
Table BorderTab=new Table(1, 5, 1);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setCSSId("BordTab");
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
if (pMode==ADDMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Add_Document")));
else if (pMode==DELMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Delete_Document")));
else
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Update_Document")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,3).AddElem(OkButton);
BorderTab.getCelda(0,3).AddElem(CancelButton);
BorderTab.getCelda(0,4).AddElem(Status);
BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,4).AddElem(HHelp);
Table FormTab=new Table(3, 6, 0);
FormTab.setCellPadding(10);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
Attribute Attr;
PDDocs TmpFold=new PDDocs(Session);
Attr=TmpFold.getRecord().getAttr(PDDocs.fTITLE);
DocTitle=new FieldText(Attr.getName());
DocTitle.setMaxSize(Attr.getLongStr());
DocTitle.setCSSClass("FFormInput");
DocTitle.setMensStatus(TT(Attr.getDescription()));
Attr=TmpFold.getRecord().getAttr(PDDocs.fDOCDATE);
DocDate=new FieldText(Attr.getName());
DocDate.setMaxSize(Attr.getLongStr());
DocDate.setCSSClass("FFormInputDate");
DocDate.setMensStatus(TT(Attr.getDescription()));
Attr=TmpFold.getRecord().getAttr(PDDocs.fNAME);
DocFile=new FieldFile("", Attr.getName());
DocFile.setCSSId("IdFile");
DocFile.setCSSClass("FFormInput");
DocFile.setMensStatus(TT(Attr.getDescription()));
DocFile2=new FieldText(Attr.getName()+"_");
DocFile2.setCSSId("IdFile2");
DocFile2.setCSSClass("FFormInputHide");
DocFile2.setMensStatus(TT(Attr.getDescription()));
Attr=TmpFold.getRecord().getAttr(PDDocs.fDOCTYPE);
ListTip=new FieldCombo(Attr.getName());
ListTip.setCSSClass("FFormInputCombo");
ListTip.setMensStatus(TT(Attr.getDescription()));
ListTip.AddOnChange("ListTypeDoc(this.options[this.selectedIndex].value);");
FillTip(ListTip, false);
Attr=TmpFold.getRecord().getAttr(PDDocs.fACL);
ListACL=new FieldCombo(Attr.getName());
ListACL.setCSSClass("FFormInputCombo");
ListACL.setMensStatus(TT(Attr.getDescription()));
FillAcl(ListACL);
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,0).AddElem(new Element(TT("Document_Title")+":"));
FormTab.getCelda(2,0).AddElem(DocTitle);
FormTab.getCelda(1,1).AddElem(new Element(TT("Document_Date")+":"));
FormTab.getCelda(2,1).AddElem(DocDate);
FormTab.getCelda(1,2).AddElem(new Element(TT("File_name")+":"));
FormTab.getCelda(2,2).AddElem(DocFile);
FormTab.getCelda(2,2).AddElem(DocFile2);
FormTab.getCelda(1,3).AddElem(new Element(TT("Document_Type")+":"));
FormTab.getCelda(2,3).AddElem(ListTip);
FormTab.getCelda(1,4).AddElem(new Element(TT("Document_ACL")+":"));
FormTab.getCelda(2,4).AddElem(ListACL);
if (pMode!=ADDMOD)
    {
    String Typ=(String)pRec.getAttr(PDDocs.fDOCTYPE).getValue();
    ListTip.setValue(Typ);
    ListTip.setActivado(false);
    DocTitle.setValue((String)pRec.getAttr(PDDocs.fTITLE).getValue());
    ListACL.setValue((String)pRec.getAttr(PDDocs.fACL).getValue());
    DocDate.setValue(SParent.FormatDate(Req, (Date)pRec.getAttr(PDDocs.fDOCDATE).getValue()));
    if (pMode==DELMOD)
        {
        DocTitle.setActivado(false);
        ListACL.setActivado(false);
        DocDate.setActivado(false);
        DocFile.setActivado(false);
        DocFile2.setActivado(false);
        }
    Element ListFields=ListTypeDocs.GenTabFields(Req, pRec, pMode);
    BorderTab.getCelda(0,2).AddElem(ListFields);
    String NomRep=(String)pRec.getAttr(PDDocs.fREPOSIT).getValue();
    if (ServParent.IsUrl(Req, NomRep))
        {
        DocFile.setCSSClass("FFormInputHide");
        DocFile2.setCSSClass("FFormInput");
        }
    else
        {
        DocFile.setCSSClass("FFormInput");
        DocFile2.setCSSClass("FFormInputHide");
        }
    }
Form DocForm=new Form(Destination+"?Read=1","FormVal");
DocForm.setModoEnvio(true);
BorderTab.getCelda(0,1).AddElem(FormTab);
DocForm.AddElem(BorderTab);
AddElem(DocForm);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
switch (Mode)  
    {
    case ADDMOD:
        return("AddExtDoc");
    case DELMOD:
        return("DelDoc");
    case EDIMOD:
        return("ModExtDoc");
    }
return("HelpIndex");
}
//-----------------------------------------------------------------------------------------------    
}
