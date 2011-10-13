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
import prodocUI.servlet.RefreshDocs;

/**
 *
 * @author jhierrot
 */
public class FMessage  extends FBase
{
 
public FieldButton2 AceptButton;

/** Creates a new instance of FormularioLogin
 * @param Req 
 * @param pTitulo
 * @param pCabecera
 * @param Message  
 */
public FMessage(HttpServletRequest Req, String pTitulo, String pCabecera, String Message)
{
super(Req, pTitulo, pCabecera);
AceptButton=new FieldButton2(TT("Ok"), "BOk");
AceptButton.setCSSClass("FFormInputButton");
Table FormTab=new Table(2, 4, 0);
FormTab.setCSSClass("FFormularios");
FormTab.getCelda(0,0).setWidth(-40);
FormTab.getCelda(1,0).AddElem(new Element(Message));
FormTab.getCelda(1,2).AddElem(AceptButton);
Form LoginForm=new Form(RefreshDocs.getUrlServlet(),"FormVal");
// Form LoginForm=new Form(SMain.getUrlServlet(),"FormVal");
LoginForm.AddElem(FormTab);
AddBody(LoginForm);
}
//-----------------------------------------------------------------------------------------------    
}
