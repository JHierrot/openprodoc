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
import prodocUI.servlet.SMain;

/**
 *
 * @author jhierrot
 */
public class FLogin  extends FBase
{
public FieldText NomUsu;    
public FieldText Clave;    
public FieldButton2 Aceptar;

/** Creates a new instance of FormularioLogin
 * @param Req 
 * @param pTitulo
 * @param pCabecera
 */
public FLogin(HttpServletRequest Req, String pTitulo, String pCabecera)
{
super(Req, pTitulo, pCabecera);
NomUsu=new FieldText("NomUsu");    
NomUsu.setCSSClass("FFormularios");
Clave=new FieldText("Clave");   
Clave.setCSSClass("FFormularios");
Clave.setClave(true);
Aceptar=new FieldButton2(TT("Ok"), "BAceptar");
Aceptar.setCSSClass("FFormInputButton");
Table FormTab=new Table(4, 3, 0);
FormTab.setCSSClass("FFormularios");
FormTab.getCelda(0,0).setWidth(-45);
FormTab.getCelda(1,0).AddElem(new Element(TT("Name")+":"));
FormTab.getCelda(2,0).AddElem(NomUsu);
FormTab.getCelda(1,1).AddElem(new Element(TT("Password")+":"));
FormTab.getCelda(2,1).AddElem(Clave);
FormTab.getCelda(2,2).AddElem(Aceptar);
Form LoginForm=new Form(SMain.getUrlServlet(),"FormVal");
LoginForm.AddElem(FormTab);
AddBody(LoginForm);
}
//-----------------------------------------------------------------------------------------------    
}
