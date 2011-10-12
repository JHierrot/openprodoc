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
import prodoc.Attribute;


/**
 *
 * @author jhierrot
 */
public class Field extends Element
{
protected String Nombre;
protected String Value;
protected boolean Activado=true;
protected Font f=null;
private int Width=0;
private int Height=0;
private String ColorBack=null;
protected String MensStatus=null;
private String CSSClass=null;

/** Creates a new instance of Field
 * @param pNombre
 */
public Field(String pNombre)
{
Nombre=pNombre;
Value="";
}
//-----------------------------------------------------------------------------------------------    

public String getValue()
{
return Value; 
}
////-----------------------------------------------------------------------------------------------    
public void setValue(Object pValue)
{
Value = pValue.toString(); 
}
//-----------------------------------------------------------------------------------------------    
public void setValue(HttpSession Sess, String pValor)
{
Sess.setAttribute(Nombre, pValor);     
}
//-----------------------------------------------------------------------------------------------    
public String getValue(HttpSession Sess)
{
return((String)Sess.getAttribute(Nombre));        
}
//-----------------------------------------------------------------------------------------------    
public String getValue(HttpServletRequest Req)
{
return((String)Req.getParameter(Nombre));        
}
//-----------------------------------------------------------------------------------------------    

public boolean isActivado()
{
return Activado;
}
//-----------------------------------------------------------------------------------------------    

public void setActivado(boolean pActivado)
{
Activado = pActivado;
}
//-----------------------------------------------------------------------------------------------
public String ToHtml(HttpSession Sess)
{    
String Val="";
if (Value.length()!=0)
    Val=Value;
else if (Sess!=null)
    {
    Val=getValue(Sess);
    if (Val==null)
        Val="";
    if (Activado)
        setValue(Sess, null);
    }
String retValue="<input name=\""+Nombre+"\" value=\""+Val+"\" "+HtmlPropio()+(Activado?"":" disabled=\"disabled\" ");
retValue+=CarcularEstilo();
if (MensStatus!=null && Activado)
    retValue+=" onmouseover=\"this.form.Status.value='"+MensStatus
              +"'\" onfocus=\"this.form.Status.value='"+MensStatus
           +"'\" onmouseout=\"this.form.Status.value=''\" onblur=\"this.form.Status.value=''\"";
return(retValue+" >");
}
//-----------------------------------------------------------------------------------------------    
protected String CarcularEstilo()
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
if (f!=null)
    retValue+=f.ToHtml();
if (ColorBack!=null)
    retValue+="background-color: rgb("+ColorBack+"); ";
if (retValue.length()!=0)
    return(" style=\""+retValue+"\"");
else
    return("");
}
//-----------------------------------------------------------------------------------------------    
protected String HtmlPropio()    
{
return("");    
}
//-----------------------------------------------------------------------------------------------
public void setFuente(Font pf)
{
f = pf;
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
public void setColorBack(String pColorBack)
{
ColorBack = pColorBack;
}
//-----------------------------------------------------------------------------------------------    
public String getMensStatus()
{
return MensStatus;
}
//-----------------------------------------------------------------------------------------------    
public void setMensStatus(String pMensStatus)
{
MensStatus = pMensStatus;
}
//-----------------------------------------------------------------------------------------------    
//
//public String getNombre()
//{
//return Nombre;
//}
//-----------------------------------------------------------------------------------------------    
public static Field CreaCamp(Attribute def)
{
FieldText c=new FieldText(def.getUserName());
c.setMensStatus(def.getDescription());
c.setMaxSize(def.getLongStr());
c.setSize(def.getLongStr()>70?70:def.getLongStr());
return(c);    
}
//-----------------------------------------------------------------------------------------------    
public String getNombre()
{
return Nombre;
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
}
