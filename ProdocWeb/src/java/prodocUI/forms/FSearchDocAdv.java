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
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;
import prodocServ.ListTypeDocs;
import prodocUI.servlet.ExportDocCSV;
import prodocUI.servlet.ReportDocs;
import prodocUI.servlet.SParent;
import prodocUI.servlet.SendDoc;

/**
 *
 * @author jhierrot
 */
public class FSearchDocAdv extends FFormBase
{
FieldCombo ListTip;
FieldCombo ListACL;
public FieldText DocTitle;
public FieldText DocPDid;
public FieldText FTQuery;

final static private String ListExcluded=PDDocs.fDOCTYPE+"/"+PDDocs.fPARENTID+"/"+PDDocs.fPDID
                        +"/"+PDDocs.fPDAUTOR+"/"+PDDocs.fPDDATE
                        +"/"+PDDocs.fLOCKEDBY+"/"+PDDocs.fVERSION+"/"+PDDocs.fPURGEDATE
                        +"/"+PDDocs.fREPOSIT+"/"+PDDocs.fSTATUS;

static final public String COMP="COMP__";
static final private String COMPTITLE=COMP+PDDocs.fTITLE;
static final private String COMPACL=COMP+PDDocs.fACL;
static final private String COMPPDID=COMP+PDDocs.fPDID;
static final public String COMPFTQ=COMP+"COMPFTQ";

/** Creates a new instance of FMantFoldAdv
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination 
 * @param ListDocs
 * @throws PDException
 */
public FSearchDocAdv(HttpServletRequest Req, int pMode, Record pRec,String Destination, Cursor ListDocs) throws PDException
{
super(Req, SParent.TT(Req,"Search_Documents"), pMode, pRec);
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
BorderTab.getCelda(0,0).AddElem(new Element(TT("Search_Documents")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,3).AddElem(OkButton);
BorderTab.getCelda(0,3).AddElem(CancelButton);
BorderTab.getCelda(0,4).AddElem(Status);
BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,4).AddElem(HHelp);
if (ListDocs!=null) //second time
    {
    BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
    HiperlinkImag ExportCsv=new HiperlinkImag("img/"+getStyle()+"expCSV.png" , "CSV Export", ExportDocCSV.getUrlServlet(), "CSV Export");
    ExportCsv.setTarget("_blank");
    BorderTab.getCelda(0,4).AddElem(ExportCsv);
    BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
    HiperlinkImag Reports=new HiperlinkImag("img/"+getStyle()+"Report.png" , "Reports", ReportDocs.getUrlServlet(), "Reports");
    BorderTab.getCelda(0,4).AddElem(Reports);
    }
Table FormTab=new Table(5, 6, 0);
FormTab.setCellPadding(5);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
PDDocs TmpDoc=new PDDocs(Session);
Attribute Attr=TmpDoc.getRecord().getAttr(PDDocs.fTITLE);
DocTitle=new FieldText(Attr.getName());
DocTitle.setMaxSize(Attr.getLongStr());
DocTitle.setCSSClass("FFormInput");
DocTitle.setMensStatus(TT(Attr.getDescription()));
String Val=null;
if (Rec!=null && Rec.getAttr(PDDocs.fTITLE)!=null)
    {
    Val=(String)Rec.getAttr(PDDocs.fTITLE).getValue();
    if (Val!=null)
        DocTitle.setValue(Val);
    }
Attr=TmpDoc.getRecord().getAttr(PDFolders.fPDID);
DocPDid=new FieldText(Attr.getName());
DocPDid.setMaxSize(Attr.getLongStr());
DocPDid.setCSSClass("FFormInput");
DocPDid.setMensStatus(TT(Attr.getDescription()));
Val=null;
if (Rec!=null && Rec.getAttr(PDDocs.fPDID)!=null)
    {
    Val=(String)Rec.getAttr(PDDocs.fPDID).getValue();
    if (Val!=null)
        DocPDid.setValue(Val);
    }
Attr=TmpDoc.getRecord().getAttr(PDDocs.fDOCTYPE);
ListTip=new FieldCombo(Attr.getName());
ListTip.setCSSClass("FFormInputCombo");
ListTip.setMensStatus(TT(Attr.getDescription()));
ListTip.AddOnChange("ListTypeDoc(this.options[this.selectedIndex].value);");
FillTip(ListTip, false);
if (Rec!=null && Rec.getAttr(PDDocs.fDOCTYPE)!=null)
    {
    Val=(String)Rec.getAttr(PDDocs.fDOCTYPE).getValue();
    if (Val!=null)
        {
        ListTip.setValue(Val);
        SParent.FillRec(Req, ListExcluded, Rec);
        Element TabFields=ListTypeDocs.GenTabFields(Req, Rec, FMantFoldAdv.ADDMOD, true);
        BorderTab.getCelda(0,2).AddElem(TabFields);
        }
    }
Attr=TmpDoc.getRecord().getAttr(PDDocs.fACL);
ListACL=new FieldCombo(Attr.getName());
ListACL.setCSSClass("FFormInputCombo");
ListACL.setMensStatus(TT(Attr.getDescription()));
FillAcl(ListACL);
ListACL.AddOpcion("None", "");
ListACL.setValue("None");
if (Rec!=null  && Rec.getAttr(PDDocs.fACL)!=null)
    {
    Val=(String)Rec.getAttr(PDDocs.fACL).getValue();
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
FormTab.getCelda(3,0).AddElem(DocPDid);
FormTab.getCelda(1,1).AddElem(new Element(TT("Document_Title")+":"));
FieldComboOper TitleOper=new FieldComboOper(COMPTITLE);
OperComp=SParent.getOperMap(Req);
Oper=OperComp.get(COMPTITLE);
if (Oper!=null)
   TitleOper.setValue(Oper);
FormTab.getCelda(2,1).AddElem(TitleOper);
FormTab.getCelda(3,1).AddElem(DocTitle);
FormTab.getCelda(1,2).AddElem(new Element(TT("Document_type")+":"));
FormTab.getCelda(3,2).AddElem(ListTip);
FormTab.getCelda(1,3).AddElem(new Element(TT("Document_ACL")+":"));
FieldComboOper ACLOper=new FieldComboOper(COMPACL);
Oper=OperComp.get(COMPACL);
if (Oper!=null)
   ACLOper.setValue(Oper);
FormTab.getCelda(2,3).AddElem(ACLOper);
FormTab.getCelda(3,3).AddElem(ListACL);
FormTab.getCelda(1,4).AddElem(new Element(TT("Subtypes")+":"));
FieldCheck SubTCh=new FieldCheck("Subtypes");
SubTCh.setMensStatus(TT("When_checked_includes_subtypes_of_document_in_results"));
SubTCh.setCSSClass("FFormInputCheck");
if (Sess.getAttribute(SParent.SD_SubT)!=null)
    {
    if ((Boolean) Sess.getAttribute(SParent.SD_SubT))
         SubTCh.setValue("1");
    }
FormTab.getCelda(1,4).AddElem(SubTCh);
FormTab.getCelda(2,4).AddElem(new Element(TT("SubFolders")+":"));

FTQuery=new FieldText(COMPFTQ);
FTQuery.setMaxSize(255);
FTQuery.setCSSClass("FFormInput");
//FTQuery.setMensStatus(TT(Attr.getDescription()));
if (Sess.getAttribute(SParent.SD_FTQ)!=null)
    {
    FTQuery.setValue(Sess.getAttribute(SParent.SD_FTQ));
    }
FormTab.getCelda(1,5).AddElem(new Element(TT("Full_Text_Search")+":"));
FormTab.getCelda(3,5).AddElem(FTQuery);

FieldCheck SubFCh=new FieldCheck("SubFolders");
SubFCh.setCSSClass("FFormInputCheck");
SubFCh.setMensStatus(TT("When_checked_limits_the_search_to_actual_folder_and_subfolders"));
if (Sess.getAttribute(SParent.SD_SubF)!=null)
    {
    if ((Boolean) Sess.getAttribute(SParent.SD_SubF))
        SubFCh.setValue("1");
    }
FormTab.getCelda(2,4).AddElem(SubFCh);
FormTab.getCelda(3,4).AddElem(new Element(TT("Versions")+":"));
FieldCheck VersCh=new FieldCheck("Versions");
VersCh.setCSSClass("FFormInputCheck");
VersCh.setMensStatus(TT("When_checked_includes_all_versions_of_document_in_results"));
if (Sess.getAttribute(SParent.SD_Vers)!=null)
    {
    if ((Boolean) Sess.getAttribute(SParent.SD_Vers))
        VersCh.setValue("1");
    }
FormTab.getCelda(3,4).AddElem(VersCh);
Form FolderForm=new Form(Destination+"?Read=1","FormVal");
BorderTab.getCelda(0,1).AddElem(FormTab);
FolderForm.AddElem(BorderTab);
AddElem(FolderForm);
if (ListDocs==null) // first time
    return;
Table TabDocs=new Table(5,1,1);
TabDocs.setCellSpacing(0);
TabDocs.setWidth(-100);
//TabDocs.setHeight(-100);
TabDocs.getFila(0).setCSSClass("ListDocsHead");
Record NextDoc=PDDocs.getRecordStructPDDocs();
Attribute AttrD=NextDoc.getAttr(PDDocs.fTITLE);
TabDocs.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fDOCTYPE);
TabDocs.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fDOCDATE);
TabDocs.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fLOCKEDBY);
TabDocs.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fPDDATE);
TabDocs.getCelda(4,0).AddElem(new Element(TT(AttrD.getUserName())));
String ActDoc=SParent.getActDocId(Req);
String DocId=null;
int Row=0;
NextDoc=PDSession.NextRec(ListDocs);
while (NextDoc!=null)
    {
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDDocs.fTITLE);
    DocId=(String)NextDoc.getAttr(PDDocs.fPDID).getValue();
    HiperlinkText hv=new html.HiperlinkText(SendDoc.getUrlServlet()+"?Id="+DocId, (String)AttrD.getValue());
    hv.setTarget("_blank");
    TabDocs.getCelda(0,Row).AddElem(hv);
    AttrD=NextDoc.getAttr(PDDocs.fDOCTYPE);
    TabDocs.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDDocs.fDOCDATE);
    TabDocs.getCelda(2,Row).AddElem(new Element(SParent.FormatDate(Req,(Date)AttrD.getValue())));
    AttrD=NextDoc.getAttr(PDDocs.fLOCKEDBY);
    if (AttrD.getValue()==null || ((String)AttrD.getValue()).length()==0)
        TabDocs.getCelda(3,Row).AddElem(Element.getEspacio());
    else
        TabDocs.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDDocs.fPDDATE);
    TabDocs.getCelda(4,Row).AddElem(new Element(SParent.FormatTS(Req,(Date)AttrD.getValue())));
    if (DocId.equals(ActDoc))
        TabDocs.getFila(Row).setCSSClass("ListDocsSel");
    else
        TabDocs.getFila(Row).setCSSClass("ListDocs");
    TabDocs.getFila(Row).setCSSId(DocId);
    TabDocs.getFila(Row).setOnClick("SelectRow('"+DocId+"')");
    NextDoc=PDSession.NextRec(ListDocs);
    }
AddElem(TabDocs);
PDSession.CloseCursor(ListDocs);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
return("SearchDocs");
}
//-----------------------------------------------------------------------------------------------    
}
