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
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;
import prodocServ.ListTypeFolds;
import prodocUI.servlet.ExportFoldCSV;
import prodocUI.servlet.ReportFolds;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FSearchFoldAdv extends FFormBase
{
FieldCombo ListTip;
FieldCombo ListACL;
public FieldText FoldTitle;
public FieldText FoldPDid;

private static final String ListExcluded=PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE;

static final public String COMP="COMP__";
static final private String COMPTITLE=COMP+PDFolders.fTITLE;
static final private String COMPACL=COMP+PDFolders.fACL;
static final private String COMPPDID=COMP+PDFolders.fPDID;

/** Creates a new instance of FMantFoldAdv
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination 
 * @param ListFold 
 * @throws PDException
 */
public FSearchFoldAdv(HttpServletRequest Req, int pMode, Record pRec,String Destination, Cursor ListFold) throws PDException
{
super(Req, SParent.TT(Req,"Search_Folders"), pMode, pRec);
AddJS("TypesSearch.js");
AddJS("ThesTreeSel.js");
HttpSession Sess=Req.getSession(true);
Record Rec=(Record)Sess.getAttribute(SParent.SD_Rec);
DriverGeneric PDSession=SParent.getSessOPD(Req);
Table BorderTab=new Table(1, 5, 1);
BorderTab.setCSSId("BordTab");
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
BorderTab.getCelda(0,0).AddElem(new Element(TT("Search_Folders")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,3).AddElem(OkButton);
BorderTab.getCelda(0,3).AddElem(CancelButton);
BorderTab.getCelda(0,4).AddElem(Status);
BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,4).AddElem(HHelp);
if (ListFold!=null) //second time
    {
    BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
    HiperlinkImag ExportCsv=new HiperlinkImag("img/"+getStyle()+"expCSV.png" , "CSV Export", ExportFoldCSV.getUrlServlet(), "CSV Export");
    ExportCsv.setTarget("_blank");
    BorderTab.getCelda(0,4).AddElem(ExportCsv);
    BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
    HiperlinkImag Reports=new HiperlinkImag("img/"+getStyle()+"Report.png" , "Reports", ReportFolds.getUrlServlet(), "Reports");
    BorderTab.getCelda(0,4).AddElem(Reports);
    }
Table FormTab=new Table(5, 6, 0);
FormTab.setCellPadding(5);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
PDFolders TmpFold=new PDFolders(Session);
Attribute Attr=TmpFold.getRecord().getAttr(PDFolders.fTITLE);
FoldTitle=new FieldText(Attr.getName());
FoldTitle.setMaxSize(Attr.getLongStr());
FoldTitle.setCSSClass("FFormInput");
FoldTitle.setMensStatus(TT(Attr.getDescription()));
String Val=null;
if (Rec!=null && Rec.getAttr(PDFolders.fTITLE)!=null)
    {
    Val=(String)Rec.getAttr(PDFolders.fTITLE).getValue();
    if (Val!=null)
        FoldTitle.setValue(Val);
    }
Attr=TmpFold.getRecord().getAttr(PDFolders.fPDID);
FoldPDid=new FieldText(Attr.getName());
FoldPDid.setMaxSize(Attr.getLongStr());
FoldPDid.setCSSClass("FFormInput");
FoldPDid.setMensStatus(TT(Attr.getDescription()));
Val=null;
if (Rec!=null && Rec.getAttr(PDFolders.fPDID)!=null)
    {
    Val=(String)Rec.getAttr(PDFolders.fPDID).getValue();
    if (Val!=null)
        FoldPDid.setValue(Val);
    }
Attr=TmpFold.getRecord().getAttr(PDFolders.fFOLDTYPE);
ListTip=new FieldCombo(Attr.getName());
ListTip.setCSSClass("FFormInputCombo");
ListTip.setMensStatus(TT(Attr.getDescription()));
ListTip.AddOnChange("ListType(this.options[this.selectedIndex].value);");
FillTip(ListTip, true);
if (Rec!=null && Rec.getAttr(PDFolders.fFOLDTYPE)!=null)
    {
    Val=(String)Rec.getAttr(PDFolders.fFOLDTYPE).getValue();
    if (Val!=null)
        {
        ListTip.setValue(Val);
        SParent.FillRec(Req, ListExcluded, Rec);
        Element TabFields=ListTypeFolds.GenTabFields(Req, Rec, FMantFoldAdv.ADDMOD, true);
        BorderTab.getCelda(0,2).AddElem(TabFields);
        }
    }
Attr=TmpFold.getRecord().getAttr(PDFolders.fACL);
ListACL=new FieldCombo(Attr.getName());
ListACL.setCSSClass("FFormInputCombo");
ListACL.setMensStatus(TT(Attr.getDescription()));
FillAcl(ListACL);
ListACL.AddOpcion("None", "");
ListACL.setValue("None");
if (Rec!=null && Rec.getAttr(PDFolders.fACL)!=null)
    {
    Val=(String)Rec.getAttr(PDFolders.fACL).getValue();
    if (Val!=null)
         ListACL.setValue(Val);
    }
FormTab.getCelda(0,0).setWidth(-15);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,0).AddElem(new Element(TT("Id")+":"));
FieldComboOper IdOper=new FieldComboOper(COMPPDID);
FormTab.getCelda(2,0).AddElem(IdOper);
HashMap<String, String> OperComp=SParent.getOperMap(Req);
String Oper=OperComp.get(COMPPDID);
if (Oper!=null)
   IdOper.setValue(Oper);
FormTab.getCelda(3,0).AddElem(FoldPDid);
FormTab.getCelda(1,1).AddElem(new Element(TT("Folder_Title")+":"));
FieldComboOper TitleOper=new FieldComboOper(COMPTITLE);
FormTab.getCelda(2,1).AddElem(TitleOper);
Oper=OperComp.get(COMPTITLE);
if (Oper!=null)
   TitleOper.setValue(Oper);
FormTab.getCelda(3,1).AddElem(FoldTitle);
FormTab.getCelda(1,2).AddElem(new Element(TT("Folder_Type")+":"));
FormTab.getCelda(3,2).AddElem(ListTip);
FormTab.getCelda(1,3).AddElem(new Element(TT("Folder_ACL")+":"));
FieldComboOper ACLOper=new FieldComboOper(COMPACL);
Oper=OperComp.get(COMPACL);
if (Oper!=null)
   ACLOper.setValue(Oper);
FormTab.getCelda(2,3).AddElem(ACLOper);
FormTab.getCelda(3,3).AddElem(ListACL);
FormTab.getCelda(1,4).AddElem(new Element(TT("Subtypes")+":"));
FieldCheck SubTCh=new FieldCheck("Subtypes");
SubTCh.setCSSClass("FFormInputCheck");
SubTCh.setMensStatus(TT("When_checked_includes_subtypes_of_folders_in_results"));
if (Sess.getAttribute(SParent.SD_SubT)!=null)
    {
    if ((Boolean) Sess.getAttribute(SParent.SD_SubT))
         SubTCh.setValue("1");
    }
FormTab.getCelda(1,4).AddElem(SubTCh);
FormTab.getCelda(2,4).AddElem(new Element(TT("SubFolders")+":"));
FieldCheck SubFCh=new FieldCheck("SubFolders");
SubFCh.setCSSClass("FFormInputCheck");
SubFCh.setMensStatus(TT("When_checked_limits_the_search_to_actual_folder_and_subfolders"));
if (Sess.getAttribute(SParent.SD_SubF)!=null)
    {
    if ((Boolean) Sess.getAttribute(SParent.SD_SubF))
        SubFCh.setValue("1");
    }
FormTab.getCelda(2,4).AddElem(SubFCh);
Form FolderForm=new Form(Destination+"?Read=1","FormVal");
BorderTab.getCelda(0,1).AddElem(FormTab);
FolderForm.AddElem(BorderTab);
AddElem(FolderForm);
if (ListFold==null) // first time
    return;
Table TabDocs=new Table(3,1,1);
TabDocs.setCellSpacing(0);
TabDocs.setWidth(-100);
//TabDocs.setHeight(-100);
TabDocs.getFila(0).setCSSClass("ListDocsHead");
Record NextDoc=PDFolders.getRecordStructPDFolder();
Attribute AttrD=NextDoc.getAttr(PDFolders.fTITLE);
TabDocs.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDFolders.fFOLDTYPE);
TabDocs.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDFolders.fPDDATE);
TabDocs.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
String ActFold=SParent.getActFolderId(Req);
String FoldId=null;
int Row=0;
NextDoc=PDSession.NextRec(ListFold);
while (NextDoc!=null)
    {
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDFolders.fTITLE);
    FoldId=(String)NextDoc.getAttr(PDFolders.fPDID).getValue();
    TabDocs.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDFolders.fFOLDTYPE);
    TabDocs.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDFolders.fPDDATE);
    TabDocs.getCelda(2,Row).AddElem(new Element(SParent.FormatTS(Req,(Date)AttrD.getValue())));
    if (FoldId.equals(ActFold))
        TabDocs.getFila(Row).setCSSClass("ListFoldsSel");
    else
        TabDocs.getFila(Row).setCSSClass("ListFolds");
    TabDocs.getFila(Row).setCSSId(FoldId);
    TabDocs.getFila(Row).setOnClick("SelectRowFold('"+FoldId+"')");
    NextDoc=PDSession.NextRec(ListFold);
    }
AddElem(TabDocs);
PDSession.CloseCursor(ListFold);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
return("SearchFolder");
}
//-----------------------------------------------------------------------------------------------    
}
