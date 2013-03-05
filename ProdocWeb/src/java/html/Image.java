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
public class Image extends Element
{
private String UrlImag;
private String TextAlt;
private int Width=0;
private int Height=0;
private String CSSClass=null;
private int Alineacion=-1;

//-----------------------------------------------------------------------------------------------
/** Creates a new instance of Image
 * @param pUrlImag
 * @param pTextAlt  
 */
public Image(String pUrlImag, String pTextAlt)
{
UrlImag=pUrlImag;
TextAlt=pTextAlt;
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
String retValue="<img "+CalcularEstilo()+" alt=\""+TextAlt+"\" src=\""+UrlImag+"\" border=0 "+Html+" >\n";
return(retValue);
}
//-----------------------------------------------------------------------------------------------

protected String CalcularEstilo()
{
if (getCSSClass()!=null)
    return(" class=\""+getCSSClass()+"\" ");
String retValue="";    
if (Width<0)
    retValue+="width: "+(-Width)+"%; ";
else if (Width>0)
    retValue+="width: "+Width+"px; ";            
if (Height<0)
    retValue+="height: "+(-Height)+"%; ";
else if (Height>0)
    retValue+="height: "+Height+"px; ";            
if (retValue.length()!=0)
    retValue+=" style=\""+retValue+"\"";
switch (Alineacion)
    {
    case RIGHT: retValue+=" align=\"right\"";
                break;
    case LEFT:  retValue+=" align=\"left\"";
                break;
    }
return(retValue);
}
//-----------------------------------------------------------------------------------------------
public void setWidth(int pWidth)
{
Width = pWidth;
}
//-----------------------------------------------------------------------------------------------
public void setHeight(int pHeight)
{
Height = pHeight;
}
//-----------------------------------------------------------------------------------------------
public void setAlineacion(int pAlineacion)
{
Alineacion = pAlineacion;
}
//-----------------------------------------------------------------------------------------------
}
