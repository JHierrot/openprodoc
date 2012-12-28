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
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.servlet.SParent;
import prodocUI.servlet.SendDoc;

/**
 *
 * @author jhierrot
 */
public class FListTerm extends Page
{
public FieldText FoldTitle;
public FieldButton2 OkButton;
public FieldButton2 CancelButton;

//final static private String List=PDFolders.fACL+"/"+PDFolders.fFOLDTYPE+"/"+PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fTITLE+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE;

/** Creates a new instance of FormularioLogin
 * @param Req
 * @param CarpId
 * @throws PDException 
 */
public FListTerm(HttpServletRequest Req, String CarpId) throws PDException
{
super( Req, SParent.TT(Req, "Term_Maintenance"), "");
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDThesaur TermAct=new PDThesaur(PDSession);
Record DatTerm=SParent.getActTerm(Req);
if (DatTerm==null || !((String)DatTerm.getAttr(PDThesaur.fPDID).getValue()).equals(CarpId))
    {
    TermAct.Load(CarpId);
    DatTerm=TermAct.getRecord();
    SParent.setActFold(Req, DatTerm);
    }
else
    TermAct.assignValues(DatTerm);
PDThesaur Term=new PDThesaur(PDSession);
AddCSS("prodoc.css");
AddJS("Types.js");
Table Tab=new Table(2,6,0);
Tab.setCSSClass("FFormularios");
Tab.setCellSpacing(2);
Tab.getCelda(0,0).AddElem(new Element(DatTerm.getAttr(PDThesaur.fNAME).getUserName()));
Tab.getCelda(0,0).setCSSClass("FFormulReq");
Tab.getCelda(0,1).AddElem(new Element(DatTerm.getAttr(PDThesaur.fDESCRIP).getUserName()));
Tab.getCelda(0,1).setCSSClass("FFormulReq");
Tab.getCelda(0,2).AddElem(new Element(DatTerm.getAttr(PDThesaur.fUSE).getUserName()));
Tab.getCelda(0,2).setCSSClass("FFormulReq");
Tab.getCelda(0,3).AddElem(new Element(DatTerm.getAttr(PDThesaur.fLANG).getUserName()));
Tab.getCelda(0,3).setCSSClass("FFormulReq");
Tab.getCelda(0,4).AddElem(new Element(DatTerm.getAttr(PDThesaur.fSCN).getUserName()));
Tab.getCelda(0,4).setCSSClass("FFormulReq");
Tab.getCelda(1,0).AddElem(new Element((String)DatTerm.getAttr(PDThesaur.fNAME).getValue()));
Tab.getCelda(1,1).AddElem(new Element((String)DatTerm.getAttr(PDThesaur.fDESCRIP).getValue()));
if (((String)DatTerm.getAttr(PDThesaur.fUSE).getValue())!=null)
    {
    Term.Load((String)DatTerm.getAttr(PDThesaur.fUSE).getValue());    
    Tab.getCelda(1,2).AddElem(new Element(Term.getName()));
    }
Tab.getCelda(1,3).AddElem(new Element((String)DatTerm.getAttr(PDThesaur.fLANG).getValue()));
Tab.getCelda(1,4).AddElem(new Element((String)DatTerm.getAttr(PDThesaur.fSCN).getValue()));
AddElem(Tab);
/***/
Record NextTerm;
Attribute AttrD;
int Row=0;
if (!Term.getListRT(TermAct.getPDId()).isEmpty())
    {
    Span S=new Span();
    S.AddElem(new Element(TT("Related_Terms")));
    S.setCSSClass("FFormularios");
    AddElem(S);
    Table TabRT=new Table(5,1,1);
    TabRT.setCellSpacing(0);
//    TabRT.setWidth(-100);
//    TabRT.setHeight(-100);
    TabRT.setCSSClass("ListThes");
    TabRT.getFila(0).setCSSClass("ListThesHead");
    NextTerm=PDThesaur.getRecordStructPDThesaur();
    AttrD=NextTerm.getAttr(PDThesaur.fNAME);
    TabRT.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
    TabRT.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fUSE);
    TabRT.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fLANG);
    TabRT.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fSCN);
    TabRT.getCelda(4,0).AddElem(new Element(TT(AttrD.getUserName())));
    Cursor ListRT=Term.ListRT(TermAct.getPDId());
    NextTerm=PDSession.NextRec(ListRT);
    while (NextTerm!=null)
        {
        TabRT.AddFila(); Row++;
        AttrD=NextTerm.getAttr(PDThesaur.fNAME);
        TabRT.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
        TabRT.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fUSE);
        if (((String)AttrD.getValue())!=null)
            {
            Term.Load((String)AttrD.getValue());    
            TabRT.getCelda(2,Row).AddElem(new Element(Term.getName()));
            }
        AttrD=NextTerm.getAttr(PDThesaur.fLANG);
        TabRT.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fSCN);
        TabRT.getCelda(4,Row).AddElem(new Element((String)AttrD.getValue()));
        NextTerm=PDSession.NextRec(ListRT);
        }
    PDSession.CloseCursor(ListRT);
    AddElem(TabRT);
    }
if (!Term.getListUF(TermAct.getPDId()).isEmpty())
    {
    Span S=new Span();
    S.AddElem(new Element(TT("Used_For")));
    S.setCSSClass("FFormularios");
    AddElem(S);
    Table TabUF=new Table(5,1,1);
    TabUF.setCellSpacing(0);
    TabUF.setWidth(-100);
    TabUF.setHeight(-100);
    TabUF.setCSSClass("ListThes");
    TabUF.getFila(0).setCSSClass("ListThesHead");
    NextTerm=PDThesaur.getRecordStructPDThesaur();
    AttrD=NextTerm.getAttr(PDThesaur.fNAME);
    TabUF.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
    TabUF.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fUSE);
    TabUF.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fLANG);
    TabUF.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fSCN);
    TabUF.getCelda(4,0).AddElem(new Element(TT(AttrD.getUserName())));
    Cursor ListUF=Term.ListUF(TermAct.getPDId());
    Row=0;
    NextTerm=PDSession.NextRec(ListUF);
    while (NextTerm!=null)
        {
        TabUF.AddFila(); Row++;
        AttrD=NextTerm.getAttr(PDThesaur.fNAME);
        TabUF.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
        TabUF.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fUSE);
        if (((String)AttrD.getValue())!=null)
            {
            Term.Load((String)AttrD.getValue());    
            TabUF.getCelda(2,Row).AddElem(new Element(Term.getName()));
            }
        AttrD=NextTerm.getAttr(PDThesaur.fLANG);
        TabUF.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fSCN);
        TabUF.getCelda(4,Row).AddElem(new Element((String)AttrD.getValue()));
        NextTerm=PDSession.NextRec(ListUF);
        }
    AddElem(TabUF);
    PDSession.CloseCursor(ListUF);
    }
if (!Term.getListLang(TermAct.getPDId()).isEmpty())
    {
    Span S=new Span();
    S.AddElem(new Element(TT("Translations")));
    S.setCSSClass("FFormularios");
    AddElem(S);
    Table TabLang=new Table(5,1,1);
    TabLang.setCellSpacing(0);
    TabLang.setWidth(-100);
    TabLang.setHeight(-100);
    TabLang.setCSSClass("ListThes");
    TabLang.getFila(0).setCSSClass("ListThesHead");
    NextTerm=PDThesaur.getRecordStructPDThesaur();
    AttrD=NextTerm.getAttr(PDThesaur.fNAME);
    TabLang.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
    TabLang.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fUSE);
    TabLang.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fLANG);
    TabLang.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
    AttrD=NextTerm.getAttr(PDThesaur.fSCN);
    TabLang.getCelda(4,0).AddElem(new Element(TT(AttrD.getUserName())));
    Cursor ListLang=Term.ListLang(TermAct.getPDId());
    Row=0;
    NextTerm=PDSession.NextRec(ListLang);
    while (NextTerm!=null)
        {
        TabLang.AddFila(); Row++;
        AttrD=NextTerm.getAttr(PDThesaur.fNAME);
        TabLang.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
        TabLang.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fUSE);
        if (((String)AttrD.getValue())!=null)
            {
            Term.Load((String)AttrD.getValue());    
            TabLang.getCelda(2,Row).AddElem(new Element(Term.getName()));
            }
        AttrD=NextTerm.getAttr(PDThesaur.fLANG);
        TabLang.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fSCN);
        TabLang.getCelda(4,Row).AddElem(new Element((String)AttrD.getValue()));
        NextTerm=PDSession.NextRec(ListLang);
        }
    AddElem(TabLang);
    PDSession.CloseCursor(ListLang);
    }
/****/
}
//-----------------------------------------------------------------------------------------------    
}
