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
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.Record;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FMantThes extends FFormBase
{
public FieldText  DocTitle;
public FieldText  DocDate;
public FieldFile DocFile;
//public FieldCombo ListMime;


/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FMantThes(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Thesaurus"), pMode, pRec);
Table BorderTab=new Table(1, 3, 0);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
if (pMode==ADDMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Create_Theusurus")));
else if (pMode==DELMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Delete_Thesaurus")));
else
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Update_Thesaurus")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,2).AddElem(Status);
BorderTab.getCelda(0,2).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,2).AddElem(HHelp);
BorderTab.setContorno(true);
Table FormTab=new Table(3, 5, 0);
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
DocFile.setCSSClass("FFormInput");
DocFile.setMensStatus(TT(Attr.getDescription()));
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,0).AddElem(new Element(TT("Document_Title")+":"));
FormTab.getCelda(2,0).AddElem(DocTitle);
FormTab.getCelda(1,1).AddElem(new Element(TT("Document_Date")+":"));
FormTab.getCelda(2,1).AddElem(DocDate);
FormTab.getCelda(1,2).AddElem(new Element(TT("File_name")+":"));
FormTab.getCelda(2,2).AddElem(DocFile);
FormTab.getCelda(2,4).AddElem(OkButton);
FormTab.getCelda(2,4).AddElem(CancelButton);
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
        return("AddDoc");
    case DELMOD:
        return("DelDoc");
    case EDIMOD:
        return("ModDoc");
    }
return("HelpIndex");
}
//------------------------------------------------------------------------------
}
