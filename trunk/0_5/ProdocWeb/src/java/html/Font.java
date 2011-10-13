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

/**
 *
 * @author jhierrot
 */
public class Font
{
private boolean Bold=false;
private boolean Italic=false;
private boolean Underline=false;
private String Family="";
private String Color=null;
private int Size=0;
private String CadHtml=null;

/** Creates a new instance of Font */
public Font(boolean pBold, boolean pItalic, boolean pUnderline, String pFamily)
{
Bold=pBold;
Italic=pItalic;
Underline=pUnderline;
Family=pFamily;
}
//-----------------------------------------------------------------------------------------------
public String ToHtml()
{
if (CadHtml!=null)
    return(CadHtml);    
CadHtml="";
if (Bold)
    CadHtml+="font-weight: bold; ";
if (Italic)
    CadHtml+="font-style: italic; ";
if (Underline)
    CadHtml+="text-decoration: underline; ";
else
    CadHtml+="text-decoration: none; ";
if (Family!=null)
    CadHtml+="font-family: "+Family+"; ";   
if (Color!=null && Color.length()!=0)
    CadHtml+="color: rgb("+Color+"); ";
if (Size!=0)
    CadHtml+="font-size: "+Size+"px; ";    
return(CadHtml);
}
//-----------------------------------------------------------------------------------------------    

public void setColor(String pColor)
{
Color = pColor;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------    
public void setSize(int pSize)
{
Size = pSize;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------    
}
