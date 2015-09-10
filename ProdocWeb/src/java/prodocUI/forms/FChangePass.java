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
 * author: Joaquin Hierro      2015
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
public class FChangePass extends FFormBase
{
public FieldText OldPass;
public FieldText NewPass1;
public FieldText NewPass2;
//public FieldText FoldTitle;

/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FChangePass(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Change_of_password"), pMode, pRec);
Table BorderTab=new Table(1, 3, 0);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
BorderTab.getCelda(0,0).AddElem(new Element(TT("Change_of_password")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,2).AddElem(Status);
BorderTab.getCelda(0,2).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,2).AddElem(HHelp);
BorderTab.setContorno(true);
Table FormTab=new Table(3, 4, 0);
FormTab.setCellPadding(10);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
OldPass=new FieldText("OldPass");
OldPass.setMaxSize(32);
OldPass.setCSSClass("FFormInput");
OldPass.setMensStatus(TT("Actual_Password"));
OldPass.setClave(true);
NewPass1=new FieldText("NewPass1");
NewPass1.setMaxSize(32);
NewPass1.setCSSClass("FFormInput");
NewPass1.setMensStatus(TT("New_Password"));
NewPass1.setClave(true);
NewPass2=new FieldText("NewPass2");
NewPass2.setMaxSize(32);
NewPass2.setCSSClass("FFormInput");
NewPass2.setMensStatus(TT("New_Password"));
NewPass2.setClave(true);
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,0).AddElem(new Element(TT("Actual_Password")+":"));
FormTab.getCelda(2,0).AddElem(OldPass);
FormTab.getCelda(1,1).AddElem(new Element(TT("New_Password")+":"));
FormTab.getCelda(2,1).AddElem(NewPass1);
FormTab.getCelda(1,2).AddElem(new Element(TT("New_Password")+":"));
FormTab.getCelda(2,2).AddElem(NewPass2);
FormTab.getCelda(1,3).AddElem(OkButton);
FormTab.getCelda(2,3).AddElem(CancelButton);
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
