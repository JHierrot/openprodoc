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
public class ElemSesion  extends Element
{
String NomElemSesion;
//-----------------------------------------------------------------------------------------------
/** Creates a new instance of ElemSesion */
public ElemSesion(String pNomElemSesion)
{
NomElemSesion=pNomElemSesion;    
}
//-----------------------------------------------------------------------------------------------
public String ToHtml(HttpSession Sess)
{
String RetValue;
try { 
RetValue=((Element)Sess.getAttribute(NomElemSesion)).ToHtml(Sess); 
if (RetValue==null)
    return("");
} catch(Exception e)
      {return("");}
Sess.setAttribute(NomElemSesion, null); // limpiamos la memoria
return (RetValue);    
}
//-----------------------------------------------------------------------------------------------
    
}
