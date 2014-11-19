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

import javax.servlet.http.HttpSession;

/**
 *
 * @author jhierrot
 */
public class FieldMulti extends Field
{
private int Size=0;    
private String UrlImagArrowD="";
private String UrlImagAdd="";
private String AltImagAdd="";
private String UrlImagDel="";
private String AltImagDel="";
private String UrlImagMod="";
private String AltImagMod="";
private String UrlImagExit="";
private String AltImagExit="";

//-----------------------------------------------------------------------------------------------    
    
/** Creates a new instance of Literal
 * @param pNombre 
 */
public FieldMulti(String pNombre)
{
super(pNombre);    
}
//-----------------------------------------------------------------------------------------------    
/**
 * 
 * @param pSize
 */
public void setSize(int pSize)
{
Size = pSize;
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
StringBuilder Result=new StringBuilder(600);    
Result.append("<table cellpadding='0'><tr>");
if (Activado)
    {
    Result.append("<td><img src=\"").append(UrlImagArrowD).append("\" onclick=\"Ver('").append(getCSSId()).append("')\">");
    Result.append("<div id=\"").append(getCSSId()).append("P");
    Result.append("\" class=\"MultiEdit\"><table id=\"").append(getCSSId()).append("T");
    Result.append("\"><tr><td></td><td><input id=\"").append(getCSSId()).append("F");
    if (getCSSClass()!=null)
        Result.append("\" class=\"").append(getCSSClass());
    Result.append("\" name=\"Entry\"></td><td><img src=\"").append(UrlImagAdd).append("\" alt=\"").append(AltImagAdd).append("\" onclick='Add(\"").append(getCSSId());
    Result.append("\")'> <img  src=\"").append(UrlImagDel).append("\" alt=\"").append(AltImagDel).append("\" onclick='Del(\"").append(getCSSId());
    Result.append("\")'> <img  src=\"").append(UrlImagMod).append("\" alt=\"").append(AltImagMod).append("\" onclick='Mod(\"").append(getCSSId());
    Result.append("\")'> <img  src=\"").append(UrlImagExit).append("\" alt=\"").append(AltImagExit).append("\" onclick='NoVer(\"").append(getCSSId());
    Result.append("\")'></td></tr><tr><td colspan=\"3\"><select id=\"").append(getCSSId()).append("S");
    Result.append("\" size=\"4\" onChange='SelOption(\"").append(getCSSId()).append("\")'");
    if (getCSSClass()!=null)
        Result.append(" class=\"").append(getCSSClass()).append("\"");
    Result.append("></select></td></tr></table></div></td>");
    }
Result.append("<td><input id=");
Result.append("\"").append(getCSSId());
Result.append("\" ").append(Activado?"":" disabled=\"disabled\" ").append("name=");
Result.append("\"").append(getNombre());
Result.append("\" readonly=\"readonly\" onclick=\"Ver('").append(getCSSId()).append("')\" value=\"");
Result.append(this.Value);
if (Size!=0)
    Result.append("\" size=\"").append(Size);
if (getCSSClass()!=null)
    Result.append("\" class=\"").append(getCSSClass());
Result.append("\"");
if (MensStatus!=null && Activado)
   Result.append(" onmouseover=\"this.form.Status.value='").append(MensStatus).append("'\" onfocus=\"this.form.Status.value='").append(MensStatus).append("'\" onmouseout=\"this.form.Status.value=''\" onblur=\"this.form.Status.value=''\"");
Result.append("></td>");
Result.append("</tr></table>");
return(Result.toString());  
}        
//-----------------------------------------------------------------------------------------------    
/**
* @param UrlImagAdd the UrlImagAdd to set
*/
public void setUrlImagAdd(String UrlImagAdd)
{
this.UrlImagAdd = UrlImagAdd;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param AltImagAdd the AltImagAdd to set
*/
public void setAltImagAdd(String AltImagAdd)
{
this.AltImagAdd = AltImagAdd;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param UrlImagDel the UrlImagDel to set
*/
public void setUrlImagDel(String UrlImagDel)
{
this.UrlImagDel = UrlImagDel;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param AltImagDel the AltImagDel to set
*/
public void setAltImagDel(String AltImagDel)
{
this.AltImagDel = AltImagDel;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param UrlImagMod the UrlImagMod to set
*/
public void setUrlImagMod(String UrlImagMod)
{
this.UrlImagMod = UrlImagMod;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param AltImagMod the AltImagMod to set
*/
public void setAltImagMod(String AltImagMod)
{
this.AltImagMod = AltImagMod;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param UrlImagExit the UrlImagExit to set
*/
public void setUrlImagExit(String UrlImagExit)
{
this.UrlImagExit = UrlImagExit;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param AltImagExit the AltImagExit to set
*/
public void setAltImagExit(String AltImagExit)
{
this.AltImagExit = AltImagExit;
}
//-----------------------------------------------------------------------------------------------    
/**
* @param UrlImagArrowD the UrlImagArrowD to set
*/
public void setUrlImagArrowD(String UrlImagArrowD)
{
this.UrlImagArrowD = UrlImagArrowD;
}
}
