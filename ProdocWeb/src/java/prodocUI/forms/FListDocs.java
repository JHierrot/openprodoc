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
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.servlet.SParent;
import prodocUI.servlet.SendDoc;

/**
 *
 * @author jhierrot
 */
public class FListDocs extends Page
{
public FieldText FoldTitle;
public FieldButton2 OkButton;
public FieldButton2 CancelButton;

final static private String List=PDFolders.fACL+"/"+PDFolders.fFOLDTYPE+"/"+PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fTITLE+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE;

/** Creates a new instance of FormularioLogin
 * @param Req
 * @param CarpId
 * @throws PDException 
 */
public FListDocs(HttpServletRequest Req, String CarpId) throws PDException
{
super( Req, SParent.TT(Req, "Maintenance_Folders"), "");
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDFolders FoldAct=new PDFolders(PDSession);
Record DatFold=SParent.getActFold(Req);
if (DatFold==null || !((String)DatFold.getAttr(PDFolders.fPDID).getValue()).equals(CarpId))
    {
    FoldAct.LoadFull(CarpId);
    DatFold=FoldAct.getRecSum();
    SParent.setActFold(Req, DatFold);
    }
else
    FoldAct.assignValues(DatFold);
AddCSS("prodoc.css");
AddJS("Types.js");
Table Tab=new Table(1,2,0);
Tab.getCelda(0,0).setCSSId("HeaderRight");
Tab.setCellSpacing(0);
Tab.setWidth(-100);
Tab.setHeight(-100);
StringBuilder DescFold=new StringBuilder("<b>"+FoldAct.getTitle()+"</b> ("+FoldAct.getFolderType() +") - "+SParent.FormatTS(Req, FoldAct.getPDDate())+ "<hr> ACL="+FoldAct.getACL());
DatFold.initList();
Attribute Attr=DatFold.nextAttr();
while (Attr!=null)
    {
    if (!List.contains(Attr.getName()))
        {
        DescFold.append("<br><b>").append(Attr.getUserName()).append("= </b>");
        if (Attr.getType()==Attribute.tTHES)
            {
            PDThesaur Term=new PDThesaur(PDSession);
            if (Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
                {
                Term.Load((String)Attr.getValue());
                DescFold.append(Term.getName());
                }
            }
        else
            DescFold.append(Attr.Export());
        }
    Attr=DatFold.nextAttr();
    }
Tab.getCelda(0,0).AddElem(new Element(DescFold.toString()));
Tab.getCelda(0,0).setCSSId("HeaderRight");
AddElem(Tab);
Table TabDocs=new Table(5,1,1);
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
AttrD=NextDoc.getAttr(PDDocs.fLOCKEDBY);
TabDocs.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fPDDATE);
TabDocs.getCelda(4,0).AddElem(new Element(TT(AttrD.getUserName())));
String ActDoc=SParent.getActDocId(Req);
String DocId=null;
PDDocs Doc=new PDDocs(PDSession);
Cursor ListDocs=Doc.getListContainedDocs(FoldAct.getPDId());
int Row=0;
NextDoc=PDSession.NextRec(ListDocs);
while (NextDoc!=null)
    {
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDDocs.fTITLE);
    DocId=(String)NextDoc.getAttr(PDDocs.fPDID).getValue();
    // TabDocs.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
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
Tab.getCelda(0,1).setCSSClass("ListDocs");
Tab.getCelda(0,1).AddElem(TabDocs);
PDSession.CloseCursor(ListDocs);
}
//-----------------------------------------------------------------------------------------------    
}
