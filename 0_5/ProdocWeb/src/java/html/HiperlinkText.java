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
public class HiperlinkText extends Element
{
private String Url="";
private String Texto="";
private String CSSClass=null;
private String Target=null;

    
/** Creates a new instance of HiperlinkText
 * @param pUrl
 * @param pTexto
 */
public HiperlinkText(String pUrl, String pTexto)
{
Url=pUrl; 
Texto=pTexto;
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Sess
 * @return
 */
@Override
public String ToHtml(HttpSession Sess)
{
return("<a href=\""+Url+ "\" "+((getCSSClass()!=null)?(" class=\""+getCSSClass()+"\" "):"")
        +((Target!=null)?(" target=\""+Target+ "\" "):"")
        +">"+Texto+"</a>");
}
//-----------------------------------------------------------------------------------------------
/**
* @return the CSSClass
*/
public String getCSSClass()
{
return CSSClass;
}
//-----------------------------------------------------------------------------------------------
/**
* @param CSSClass the CSSClass to set
*/
public void setCSSClass(String CSSClass)
{
this.CSSClass = CSSClass;
}
//-----------------------------------------------------------------------------------------------
public String getTarget()
{
return Target;
}
//-----------------------------------------------------------------------------------------------
public void setTarget(String pTarget)
{
Target = pTarget;
}
//-----------------------------------------------------------------------------------------------
}
