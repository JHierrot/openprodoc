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
public class Element
{
protected String Html="";

public static final int NADA  =-1; 
public static final int LEFT  =0; 
public static final int CENTER=1;
public static final int RIGHT =2;
public static final int JUST  =3;
public static final int VTOP  =0; 
public static final int VMIDDLE=1;
public static final int VBOTTOM =2;
public static final int VBASELINE =3;
private static Element Salto=new Element("<br>");
private static Element Salto2=new Element("<br><br>");
private static Element Espacio=new Element("&nbsp;");
private static Element Espacio2=new Element("&nbsp;&nbsp;");
private String CSSId=null;

//-----------------------------------------------------------------------------------------------    
/** Creates a new instance of Element */
public Element()
{    
}
//-----------------------------------------------------------------------------------------------    
/** Creates a new instance of Element */
public Element(String pHtml)
{
Html=pHtml;    
}
//-----------------------------------------------------------------------------------------------
public String ToHtml(HttpSession Sess)
{
return(Html);
}
//-----------------------------------------------------------------------------------------------
public static Element getSalto()
{
return(Salto);    
}
//-----------------------------------------------------------------------------------------------
public static Element getSalto2()
{
return(Salto2);    
}
//-----------------------------------------------------------------------------------------------
public static Element getEspacio()
{
return(Espacio);    
}
//-----------------------------------------------------------------------------------------------
public static Element getEspacio2()
{
return(Espacio2);    
}
//-----------------------------------------------------------------------------------------------
public void setHtml(String pHtml)
{
Html = pHtml;
}
//-----------------------------------------------------------------------------------------------
/**
* @param CSSId the CSSId to set
*/
public void setCSSId(String CSSId)
{
this.CSSId = CSSId;
}
//-----------------------------------------------------------------------------------------------
/**
* @return the CSSId
*/
public String getCSSId()
{
return CSSId;
}

}
