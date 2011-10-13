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
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.Record;
import prodocUI.servlet.SPaperBin;
import prodocUI.servlet.SParent;
import prodocUI.servlet.SendDoc;

/*
 * FPaperBin.java
 *
 * Created on 14 de abril de 2005, 18:34
 * 
 * @author jhierrot
 */
public class FPaperBin extends FFormBase
{
public FieldText FoldTitle;

/** Creates a new instance of FMantFoldAdv
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination 
 * @param ListDelDocs
 * @throws PDException
 */
public FPaperBin(HttpServletRequest Req, String DocTypeSel, Record pRec,String Destination, Cursor ListDelDocs) throws PDException
{
super(Req, SParent.TT(Req,"Trash_bin"), FPaperBin.ADDMOD, pRec);
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDDocs DocAct=new PDDocs(PDSession);
DocAct.assignValues(pRec);
AddCSS("prodoc.css");
AddJS("Types.js");
Table Tab=new Table(1,3,0);
Tab.setCSSId("HeaderRight");
Tab.setCellSpacing(0);
Tab.setWidth(-100);
Tab.setHeight(-100);
AddElem(Tab);
Table TabTipDocDel=new Table(4, 3, 0);
TabTipDocDel.setWidth(-100);
TabTipDocDel.setCellPadding(0);
TabTipDocDel.setCSSClass("FFormularios");
TabTipDocDel.getCelda(1,0).AddElem(new Element(TT("Trash_bin")));
TabTipDocDel.getFila(0).setCSSClass("FTitle");
TabTipDocDel.getCelda(1,1).AddElem(new Element(TT("Documental_type_to_undelete")+":"));
FieldCombo ListTip=new FieldCombo(PDDocs.fDOCTYPE);
ListTip.setCSSClass("FFormInputCombo");
FillTip(ListTip, false);
if (DocTypeSel!=null)
    ListTip.setValue(DocTypeSel);
TabTipDocDel.getCelda(2,1).AddElem(ListTip);
TabTipDocDel.getCelda(2,2).AddElem(this.OkButton);
TabTipDocDel.getCelda(3,2).AddElem(HHelp);
Form FormLisTipDel=new Form(SPaperBin.getUrlServlet(), "");
FormLisTipDel.AddElem(TabTipDocDel);
Tab.getCelda(0,0).AddElem(FormLisTipDel);
Tab.getCelda(0,0).setHeight(-10);
Table TabDocs=new Table(6,1,1);
TabDocs.setCellSpacing(0);
TabDocs.setWidth(-100);
TabDocs.setHeight(-100);
TabDocs.getFila(0).setCSSClass("ListDocsHead");
Record NextDoc=PDDocs.getRecordStructPDDocs();
Attribute AttrD=NextDoc.getAttr(PDDocs.fTITLE);
TabDocs.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fDOCTYPE);
TabDocs.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fDOCDATE);
TabDocs.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fPDDATE);
TabDocs.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
String DocId=null;
String DocType=null;
int Row=0;
HashMap ListDel=null;
if (ListDelDocs!=null)
    NextDoc=PDSession.NextRec(ListDelDocs);
else
    NextDoc=null;
while (NextDoc!=null)
    {
    if (ListDel==null)    
        ListDel=new HashMap();
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDDocs.fTITLE);
    DocId=(String)NextDoc.getAttr(PDDocs.fPDID).getValue();
    ListDel.put(DocId, NextDoc);
    DocType=(String)NextDoc.getAttr(PDDocs.fDOCTYPE).getValue();
    HiperlinkText hv=new html.HiperlinkText(SendDoc.getUrlServlet()+"?Id="+DocId, (String)AttrD.getValue());
    hv.setTarget("_blank");
    TabDocs.getCelda(0,Row).AddElem(hv);
    AttrD=NextDoc.getAttr(PDDocs.fDOCTYPE);
    TabDocs.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDDocs.fDOCDATE);
    TabDocs.getCelda(2,Row).AddElem(new Element(SParent.FormatDate(Req,(Date)AttrD.getValue())));
    AttrD=NextDoc.getAttr(PDDocs.fPDDATE);
    TabDocs.getCelda(3,Row).AddElem(new Element(SParent.FormatTS(Req,(Date)AttrD.getValue())));
    TabDocs.getFila(Row).setCSSClass("ListDocs");
    HiperlinkImag hvi=new html.HiperlinkImag("img/"+getStyle()+"undel.png", SParent.TT(Req, "Undelete_deleted_documents"), SPaperBin.getUrlServlet()+"?DocType="+DocType+"&Id="+DocId+"&Undel=1", null);
    TabDocs.getCelda(4,Row).AddElem(hvi);
    hvi=new html.HiperlinkImag("img/"+getStyle()+"del.png", SParent.TT(Req, "Permanently_removes_the_selected_documents"), SPaperBin.getUrlServlet()+"?DocType="+DocType+"&Id="+DocId+"&Purge=1", null);
    TabDocs.getCelda(5,Row).AddElem(hvi);
    TabDocs.getFila(Row).setOnClick("ShowDocDel('"+DocId+"')");
    NextDoc=PDSession.NextRec(ListDelDocs);
    }
Tab.getCelda(0,2).setCSSClass("ListDocs");
Tab.getCelda(0,2).AddElem(TabDocs);
SParent.setListDocs(Req, ListDel);
if (ListDelDocs!=null)
    PDSession.CloseCursor(ListDelDocs);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
return("PaperBin");
}
//-----------------------------------------------------------------------------------------------    
}
