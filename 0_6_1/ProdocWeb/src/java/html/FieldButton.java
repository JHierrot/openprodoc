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

package html;

import javax.servlet.http.*;

/**
 *
 * @author jhierrot
 */
public class FieldButton extends Field
{
private String Func=null;     
    /** Creates a new instance of Boton */
public FieldButton(String pTexto, String pNombre, String pFunc)
{
super(pNombre);    
Value=pTexto;
Func=pFunc;
}
//-----------------------------------------------------------------------------------------------    
public String ToHtml(HttpSession Sess)
{    
String retValue="<button  name=\""+Nombre+"\" type=\"button\" onclick=\""+Func+"\" "+(Activado?"":" disabled=\"disabled\" ");
retValue+=CarcularEstilo();
if (MensStatus!=null && Activado)
    retValue+=" onmouseover=\"this.form.Status.value='"+MensStatus
              +"'\" onfocus=\"this.form.Status.value='"+MensStatus
           +"'\" onmouseout=\"this.form.Status.value=''\" onblur=\"this.form.Status.value=''\"";
retValue+=">"+Value+"</button>";
return(retValue);
}
//-----------------------------------------------------------------------------------------------    

   
}
