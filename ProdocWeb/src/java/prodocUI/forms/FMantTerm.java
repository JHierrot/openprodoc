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
import prodoc.Cursor;
import prodoc.PDException;
import prodoc.PDThesaur;
import prodoc.Record;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FMantTerm extends FFormBase
{
public FieldText TermName;
public FieldText TermId;
public FieldText TermDef;
public FieldThesOPD TermUse;
public FieldText TermLang;
public FieldText TermSCN;
//public FieldCombo ListMime;


/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FMantTerm(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Thesaurus"), pMode, pRec);
PDThesaur TermAct=new PDThesaur(Session);
TermAct.Load(SParent.getActTermId(Req));
String IdThesCont=TermAct.getIDThesaur();
//AddJS("Types.js");
AddJS("ThesTreeSel.js");
Table BorderTab=new Table(1, 3, 0);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
if (pMode==ADDMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Add_Term")));
else if (pMode==DELMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Delete_Term")));
else
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Update_Term")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,2).AddElem(Status);
BorderTab.getCelda(0,2).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,2).AddElem(HHelp);
BorderTab.setContorno(true);
Table FormTab=new Table(3, 11, 0);
FormTab.setCellPadding(10);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
Attribute Attr;
Record TermRec;
if (pRec!=null)
   TermRec=pRec;
else
    TermRec=PDThesaur.getRecordStructPDThesaur();
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
Attr=TermRec.getAttr(PDThesaur.fPDID);
TermId=new FieldText(Attr.getName());
TermId.setMaxSize(Attr.getLongStr());
TermId.setCSSClass("FFormInput");
TermId.setMensStatus(TT(Attr.getDescription()));
TermId.setActivado(false);
if (Attr.getValue()!=null)
    TermId.setValue((String)Attr.getValue());
FormTab.getCelda(1,0).AddElem(new Element(TT(Attr.getUserName())+":"));
FormTab.getCelda(2,0).AddElem(TermId);
Attr=TermRec.getAttr(PDThesaur.fNAME);
TermName=new FieldText(Attr.getName());
TermName.setMaxSize(Attr.getLongStr());
TermName.setCSSClass("FFormInput");
TermName.setMensStatus(TT(Attr.getDescription()));
if (Attr.getValue()!=null)
    TermName.setValue((String)Attr.getValue());
FormTab.getCelda(1,1).AddElem(new Element(TT(Attr.getUserName())+":"));
FormTab.getCelda(2,1).AddElem(TermName);
Attr=TermRec.getAttr(PDThesaur.fDESCRIP);
TermDef=new FieldText(Attr.getName());
TermDef.setMaxSize(Attr.getLongStr());
TermDef.setCSSClass("FFormInput");
TermDef.setMensStatus(TT(Attr.getDescription()));
if (Attr.getValue()!=null)
    TermDef.setValue((String)Attr.getValue());
FormTab.getCelda(1,2).AddElem(new Element(TT(Attr.getUserName())+":"));
FormTab.getCelda(2,2).AddElem(TermDef);

Attr=TermRec.getAttr(PDThesaur.fUSE);
TermUse=new FieldThesOPD(Attr.getName(), getStyle(), IdThesCont);
TermUse.setCSSClass("MultiEdit");
TermUse.setCSSId(Attr.getName());
TermUse.setMensStatus(TT(Attr.getDescription()));
if (Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
    {
    PDThesaur TermU=new PDThesaur(Session);
    TermU.Load((String)Attr.getValue());
    TermUse.setValue(TermU.getName());
    TermUse.setIdTerm((String)Attr.getValue());
    }
FormTab.getCelda(1,3).AddElem(new Element(TT(Attr.getUserName())+":"));
FormTab.getCelda(2,3).AddElem(TermUse);

Attr=TermRec.getAttr(PDThesaur.fLANG);
TermLang=new FieldText(Attr.getName());
TermLang.setMaxSize(Attr.getLongStr());
TermLang.setCSSClass("FFormInput");
TermLang.setMensStatus(TT(Attr.getDescription()));
if (Attr.getValue()!=null)
    TermLang.setValue((String)Attr.getValue());
FormTab.getCelda(1,4).AddElem(new Element(TT(Attr.getUserName())+":"));
FormTab.getCelda(2,4).AddElem(TermLang);
Attr=TermRec.getAttr(PDThesaur.fSCN);
TermSCN=new FieldText(Attr.getName());
TermSCN.setMaxSize(Attr.getLongStr());
TermSCN.setCSSClass("FFormInput");
TermSCN.setMensStatus(TT(Attr.getDescription()));
if (Attr.getValue()!=null)
    TermSCN.setValue((String)Attr.getValue());
FormTab.getCelda(1,5).AddElem(new Element(TT(Attr.getUserName())+":"));
FormTab.getCelda(2,5).AddElem(TermSCN);
if (pMode==DELMOD)
    {
    TermName.setActivado(false);
    TermDef.setActivado(false);
    TermLang.setActivado(false);
    TermSCN.setActivado(false);
    TermUse.setActivado(false);
    }
Record NextTerm;
Attribute AttrD;
int Row=0;
if (!TermAct.getListRT(TermAct.getPDId()).isEmpty())
    {
    Span S=new Span();
    S.AddElem(new Element(TT("Related_Terms")));
    S.setCSSClass("FFormularios");
    FormTab.getCelda(1,7).AddElem(S);
    Table TabRT=new Table(5,1,1);
    TabRT.setCellSpacing(0);
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
    Cursor ListRT=TermAct.ListRT(TermAct.getPDId());
    NextTerm=Session.NextRec(ListRT);
    while (NextTerm!=null)
        {
        TabRT.AddFila(); Row++;
        AttrD=NextTerm.getAttr(PDThesaur.fNAME);
        TabRT.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
        TabRT.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fUSE);
        if (AttrD.getValue()!=null && ((String)AttrD.getValue()).length()!=0)
            {
            TermAct.Load((String)AttrD.getValue());    
            TabRT.getCelda(2,Row).AddElem(new Element(TermAct.getName()));
            }
        AttrD=NextTerm.getAttr(PDThesaur.fLANG);
        TabRT.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fPDID);
        TabRT.getCelda(4,Row).AddElem(new Element((String)AttrD.getValue()));
        NextTerm=Session.NextRec(ListRT);
        }
    Session.CloseCursor(ListRT);
    FormTab.getCelda(2,7).AddElem(TabRT);
    }
if (!TermAct.getListUF(TermAct.getPDId()).isEmpty())
    {
    Span S=new Span();
    S.AddElem(new Element(TT("Used_For")));
    S.setCSSClass("FFormularios");
    FormTab.getCelda(1,8).AddElem(S);
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
    Cursor ListUF=TermAct.ListUF(TermAct.getPDId());
    Row=0;
    NextTerm=Session.NextRec(ListUF);
    while (NextTerm!=null)
        {
        TabUF.AddFila(); Row++;
        AttrD=NextTerm.getAttr(PDThesaur.fNAME);
        TabUF.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
        TabUF.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fUSE);
        if (AttrD.getValue()!=null && ((String)AttrD.getValue()).length()!=0)
            {
            TermAct.Load((String)AttrD.getValue());    
            TabUF.getCelda(2,Row).AddElem(new Element(TermAct.getName()));
            }
        AttrD=NextTerm.getAttr(PDThesaur.fLANG);
        TabUF.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fPDID);
        TabUF.getCelda(4,Row).AddElem(new Element((String)AttrD.getValue()));
        NextTerm=Session.NextRec(ListUF);
        }
    Session.CloseCursor(ListUF);
    FormTab.getCelda(2,8).AddElem(TabUF);
    }
if (!TermAct.getListLang(TermAct.getPDId()).isEmpty())
    {
    Span S=new Span();
    S.AddElem(new Element(TT("Translations")));
    S.setCSSClass("FFormularios");
    FormTab.getCelda(1,9).AddElem(S);
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
    Cursor ListLang=TermAct.ListLang(TermAct.getPDId());
    Row=0;
    NextTerm=Session.NextRec(ListLang);
    while (NextTerm!=null)
        {
        TabLang.AddFila(); Row++;
        AttrD=NextTerm.getAttr(PDThesaur.fNAME);
        TabLang.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fDESCRIP);
        TabLang.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fUSE);
        if (AttrD.getValue()!=null && ((String)AttrD.getValue()).length()!=0)
            {
            TermAct.Load((String)AttrD.getValue());    
            TabLang.getCelda(2,Row).AddElem(new Element(TermAct.getName()));
            }
        AttrD=NextTerm.getAttr(PDThesaur.fLANG);
        TabLang.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
        AttrD=NextTerm.getAttr(PDThesaur.fPDID);
        TabLang.getCelda(4,Row).AddElem(new Element((String)AttrD.getValue()));
        NextTerm=Session.NextRec(ListLang);
        }
    Session.CloseCursor(ListLang);
    FormTab.getCelda(2,9).AddElem(TabLang);
    }

FormTab.getCelda(2,10).AddElem(OkButton);
FormTab.getCelda(2,10).AddElem(CancelButton);
Form DocForm=new Form(Destination+"?Read=1","FormVal");
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
        return("AddThes");
    case DELMOD:
        return("DelThes");
    case EDIMOD:
        return("ModThes");
    }
return("HelpIndex");
}
//------------------------------------------------------------------------------
}
