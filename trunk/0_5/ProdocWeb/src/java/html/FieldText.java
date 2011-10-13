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
public class FieldText extends Field
{
private int Size=0;    
private int MaxSize=0;    
private boolean Clave=false;
private boolean Oculto=false;
//-----------------------------------------------------------------------------------------------    
    
/** Creates a new instance of Literal */
public FieldText(String pNombre)
{
super(pNombre);    
}
//-----------------------------------------------------------------------------------------------    

public void setSize(int pSize)
{
Size = pSize;
}
//-----------------------------------------------------------------------------------------------    

public void setMaxSize(int pMaxSize)
{
MaxSize = pMaxSize;
}
//-----------------------------------------------------------------------------------------------    
protected String HtmlPropio()    
{
String retValue="";
if (Size!=0)
    retValue+=" size=\""+Size+"\" ";
if (MaxSize!=0)
    retValue+=" maxlength=\""+MaxSize+"\" ";
if (Clave)
    retValue+=" type=\"password\" ";
else if (Oculto)
    retValue+=" type=\"hidden\" ";
return(retValue);    
}
//-----------------------------------------------------------------------------------------------    

public void setClave(boolean pClave)
{
Clave = pClave;
}
//-----------------------------------------------------------------------------------------------    

public void setOculto(boolean pOculto)
{
Oculto = pOculto;
}
//-----------------------------------------------------------------------------------------------    
    
}
