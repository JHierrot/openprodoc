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
PDThesaur Term=new PDThesaur(Session);
Term.Load(SParent.getActTermId(Req));
String IdThesCont=Term.getIDThesaur();
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
Table FormTab=new Table(3, 8, 0);
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
if (Attr.getValue()!=null)
    TermUse.setValue((String)Attr.getValue());
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
    }
FormTab.getCelda(2,7).AddElem(OkButton);
FormTab.getCelda(2,7).AddElem(CancelButton);
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
