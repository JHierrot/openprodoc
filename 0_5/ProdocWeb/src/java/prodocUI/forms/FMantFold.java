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
import prodoc.PDFolders;
import prodoc.Record;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FMantFold extends FFormBase
{
public FieldText FoldTitle;

/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FMantFold(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Maintenance_Folders"), pMode, pRec);
Table BorderTab=new Table(1, 3, 0);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
if (pMode==ADDMOD)
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Add_Folder")));
else
    BorderTab.getCelda(0,0).AddElem(new Element(TT("Update_Folder")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,2).AddElem(Status);
BorderTab.getCelda(0,2).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,2).AddElem(HHelp);
BorderTab.setContorno(true);
Table FormTab=new Table(3, 2, 0);
FormTab.setCellPadding(10);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
Attribute Attr;
if (pRec==null)
    {
    PDFolders TmpFold=new PDFolders(Session);
    Attr=TmpFold.getRecord().getAttr(PDFolders.fTITLE);
    }
else
    Attr=pRec.getAttr(PDFolders.fTITLE);
FoldTitle=new FieldText(Attr.getName());
if (Attr.getValue()!=null)
    FoldTitle.setValue((String)Attr.getValue());
FoldTitle.setMaxSize(Attr.getLongStr());
FoldTitle.setCSSClass("FFormInput");
FoldTitle.setMensStatus(TT(Attr.getDescription()));
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,0).AddElem(new Element(TT("Folder_Title")+":"));
FormTab.getCelda(2,0).AddElem(FoldTitle);
FormTab.getCelda(2,1).AddElem(OkButton);
FormTab.getCelda(2,1).AddElem(CancelButton);
Form LoginForm=new Form(Destination+"?Read=1","FormVal");
BorderTab.getCelda(0,1).AddElem(FormTab);
LoginForm.AddElem(BorderTab);
AddElem(LoginForm);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
switch (Mode)  
    {
    case ADDMOD:
        return("AddFolder");
    case DELMOD:
        return("DelFolder");
    case EDIMOD:
        return("ModFolder");
    }
return("HelpIndex");
}
//-----------------------------------------------------------------------------------------------    
}
