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
public class FCheckIn extends FFormBase
{
public FieldText VerLabel;

/** Creates a new instance of Formulario Checkin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FCheckIn(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Checkin_Selected_Document"), pMode, pRec);
Table BorderTab=new Table(1, 3, 0);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
BorderTab.getCelda(0,0).AddElem(new Element(TT("Checkin_Selected_Document")));
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
PDDocs TmpFold=new PDDocs(Session);
Attr=TmpFold.getRecord().getAttr(PDDocs.fVERSION);
VerLabel=new FieldText(Attr.getName());
VerLabel.setMaxSize(Attr.getLongStr());
VerLabel.setCSSClass("FFormInput");
VerLabel.setMensStatus(TT(Attr.getDescription()));
FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,0).AddElem(new Element(TT("Version_identifier")+":"));
FormTab.getCelda(2,0).AddElem(VerLabel);
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
return("CheckIn");
}
//-----------------------------------------------------------------------------------------------    
}
