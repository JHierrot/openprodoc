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
import javax.servlet.http.HttpSession;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FSearchThesAdv extends FFormBase
{
FieldText TermSCN;
FieldText TermDesc;
public FieldText TermName;

static private String ListExcluded=PDThesaur.fPARENTID+"/"+PDThesaur.fPDID+"/"+PDThesaur.fPDAUTOR+"/"+PDThesaur.fPDDATE;

/** Creates a new instance of FMantFoldAdv
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination 
 * @param ListFold 
 * @throws PDException
 */
public FSearchThesAdv(HttpServletRequest Req, int pMode, Record pRec,String Destination, Cursor ListFold) throws PDException
{
super(Req, SParent.TT(Req,"Search_Term"), pMode, pRec);
AddJS("Types.js");
HttpSession Sess=Req.getSession(true);
Record Rec=(Record)Sess.getAttribute("SD_Rec");
DriverGeneric PDSession=SParent.getSessOPD(Req);
Table BorderTab=new Table(1, 5, 1);
BorderTab.setCSSId("BordTab");
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
BorderTab.getCelda(0,0).AddElem(new Element(TT("Search_Term")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,3).AddElem(OkButton);
BorderTab.getCelda(0,3).AddElem(CancelButton);
BorderTab.getCelda(0,4).AddElem(Status);
BorderTab.getCelda(0,4).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,4).AddElem(HHelp);
Table FormTab=new Table(4, 5, 0);
FormTab.setCellPadding(5);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
PDThesaur TmpFold=new PDThesaur(Session);
Attribute Attr=TmpFold.getRecord().getAttr(PDThesaur.fNAME);
TermName=new FieldText(Attr.getName());
TermName.setMaxSize(Attr.getLongStr());
TermName.setCSSClass("FFormInput");
TermName.setMensStatus(TT(Attr.getDescription()));
String Val=null;
if (Rec!=null)
    {
    Val=(String)Rec.getAttr(PDThesaur.fNAME).getValue();
    if (Val!=null)
        TermName.setValue(Val);
    }
Attr=TmpFold.getRecord().getAttr(PDThesaur.fDESCRIP);
TermDesc=new FieldText(Attr.getName());
TermDesc.setCSSClass("FFormInput");
TermDesc.setMensStatus(TT(Attr.getDescription()));
if (Rec!=null)
    {
    Val=(String)Rec.getAttr(PDThesaur.fDESCRIP).getValue();
    if (Val!=null)
         TermDesc.setValue(Val);
    }
Attr=TmpFold.getRecord().getAttr(PDThesaur.fSCN);
TermSCN=new FieldText(Attr.getName());
TermSCN.setCSSClass("FFormInput");
TermSCN.setMensStatus(TT(Attr.getDescription()));
if (Rec!=null)
    {
    Val=(String)Rec.getAttr(PDThesaur.fSCN).getValue();
    if (Val!=null)
         TermSCN.setValue(Val);
    }
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,1).AddElem(new Element(TT("Term_Name")+":"));
FormTab.getCelda(2,1).AddElem(TermName);
FormTab.getCelda(1,2).AddElem(new Element(TT("Scope_Note")+":"));
FormTab.getCelda(2,2).AddElem(TermSCN);
FormTab.getCelda(1,3).AddElem(new Element(TT("Description")+":"));
FormTab.getCelda(2,3).AddElem(TermDesc);
FormTab.getCelda(2,4).AddElem(new Element(TT("Specific_Terms")+":"));
FieldCheck SubFCh=new FieldCheck("SubTerms");
SubFCh.setCSSClass("FFormInputCheck");
SubFCh.setMensStatus(TT("When_checked_limits_the_search_to_actual_term_and_specific_terms"));
if (Rec!=null)
    {
    if ((Boolean) Sess.getAttribute("ST_SubT"))
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
Record NextDoc=PDThesaur.getRecordStructPDThesaur();
Attribute AttrD=NextDoc.getAttr(PDThesaur.fNAME);
TabDocs.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDThesaur.fDESCRIP);
TabDocs.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDThesaur.fLANG);
TabDocs.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
String ActFold=SParent.getActFolderId(Req);
String FoldId=null;
int Row=0;
NextDoc=PDSession.NextRec(ListFold);
while (NextDoc!=null)
    {
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDThesaur.fNAME);
    FoldId=(String)NextDoc.getAttr(PDThesaur.fPDID).getValue();
    TabDocs.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDThesaur.fDESCRIP);
    TabDocs.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDThesaur.fLANG);
    TabDocs.getCelda(2,Row).AddElem(new Element((String)AttrD.getValue()));
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
return("SearchThes");
}
//-----------------------------------------------------------------------------------------------    
}
