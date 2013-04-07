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
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;
import prodocServ.ListTypeFolds;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FMantFoldAdv extends FFormBase
{
FieldCombo ListTip;
FieldCombo ListACL;
public FieldText FoldTitle;

/** Creates a new instance of FMantFoldAdv
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination 
 * @throws PDException
 */
public FMantFoldAdv(HttpServletRequest Req, int pMode, Record pRec,String Destination) throws PDException
{
super(Req, SParent.TT(Req,"Maintenance_Folders"), pMode, pRec);
AddJS("Types.js");
AddJS("ThesTreeSel.js");
Table BorderTab=new Table(1, 5, 1);
BorderTab.setCSSId("BordTab");
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
if (pMode==ADDMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Add_Folder")));
else if (pMode==DELMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Delete_Folder")));
else
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Update_Folder")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,3).AddElem(OkButton);
BorderTab.getCelda(0,3).AddElem(CancelButton);
BorderTab.getCelda(0,4).AddElem(Status);
BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,4).AddElem(HHelp);
Table FormTab=new Table(4, 4, 0);
FormTab.setCellPadding(5);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
PDFolders TmpFold=new PDFolders(Session);
Attribute Attr=TmpFold.getRecord().getAttr(PDFolders.fTITLE);
FoldTitle=new FieldText(Attr.getName());
FoldTitle.setMaxSize(Attr.getLongStr());
FoldTitle.setCSSClass("FFormInput");
FoldTitle.setMensStatus(TT(Attr.getDescription()));
Attr=TmpFold.getRecord().getAttr(PDFolders.fFOLDTYPE);
ListTip=new FieldCombo(Attr.getName());
ListTip.setCSSClass("FFormInputCombo");
ListTip.setMensStatus(TT(Attr.getDescription()));
ListTip.AddOnChange("ListType(this.options[this.selectedIndex].value);");
FillTip(ListTip, true);
Attr=TmpFold.getRecord().getAttr(PDFolders.fACL);
ListACL=new FieldCombo(Attr.getName());
ListACL.setCSSClass("FFormInputCombo");
ListACL.setMensStatus(TT(Attr.getDescription()));
FillAcl(ListACL);
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,1).AddElem(new Element(TT("Folder_Title")+":"));
FormTab.getCelda(1,1).setCSSClass("FFormulReq");
FormTab.getCelda(2,1).AddElem(FoldTitle);
FormTab.getCelda(1,2).AddElem(new Element(TT("Folder_Type")+":"));
FormTab.getCelda(1,2).setCSSClass("FFormulReq");
FormTab.getCelda(2,2).AddElem(ListTip);
FormTab.getCelda(1,3).AddElem(new Element(TT("Folder_ACL")+":"));
FormTab.getCelda(1,3).setCSSClass("FFormulReq");
FormTab.getCelda(2,3).AddElem(ListACL);
if (pMode!=ADDMOD)
    {
    String Typ=(String)pRec.getAttr(PDFolders.fFOLDTYPE).getValue();
    ListTip.setValue(Typ);
    ListTip.setActivado(false);
    DriverGeneric PDSession=SParent.getSessOPD(Req);
//    PDFolders F = new PDFolders(PDSession, Typ);
//    F.LoadFull(SParent.getActFolderId(Req));
//    FoldTitle.setValue(F.getTitle());
//    ListACL.setValue(F.getACL());
    FoldTitle.setValue((String)pRec.getAttr(PDFolders.fTITLE).getValue());
    ListACL.setValue((String)pRec.getAttr(PDFolders.fACL).getValue());
    if (pMode==DELMOD)
        {
        FoldTitle.setActivado(false);
        ListACL.setActivado(false);
        }
//    Record Rec=F.getRecSum();
    Element ListFields=ListTypeFolds.GenTabFields(Req, pRec, pMode);
    BorderTab.getCelda(0,2).AddElem(ListFields);
    }
Form FolderForm=new Form(Destination+"?Read=1","FormVal");
BorderTab.getCelda(0,1).AddElem(FormTab);
FolderForm.AddElem(BorderTab);
AddElem(FolderForm);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
switch (Mode)  
    {
    case ADDMOD:
        return("AddExtFolder");
    case DELMOD:
        return("DelFolder");
    case EDIMOD:
        return("ModExtFolder");
    }
return("HelpIndex");
}
//-----------------------------------------------------------------------------------------------    
}
