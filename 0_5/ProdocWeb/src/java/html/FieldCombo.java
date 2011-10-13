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

import java.util.Vector;
import javax.servlet.http.*;


/**
 *
 * @author jhierrot
 */
public class FieldCombo extends Field
{
private int Size=1;    
private Vector ListaOpc=new Vector();
private Vector ListaLit=new Vector();
private String OnChange=null;
//-----------------------------------------------------------------------------------------------    
    
/** Creates a new instance of Field
 * @param pNombre 
 */
public FieldCombo(String pNombre)
{
super(pNombre);    
}
//-----------------------------------------------------------------------------------------------    
public void setSize(int pSize)
{
Size = pSize;
}
//-----------------------------------------------------------------------------------------------
public void AddOnChange(String Script)
{
OnChange=Script;
}
//-----------------------------------------------------------------------------------------------    
public int EliminarOpcion(String Opc)
{
if (Opc==null)
    return(0);
for (int i=0; i<ListaOpc.size(); i++)
    {
    if (Opc.equalsIgnoreCase((String)ListaOpc.get(i)))
        {
        ListaOpc.remove(i);
        ListaLit.remove(i);
        return(0);
        }
    }   
return(1);    
}
//-----------------------------------------------------------------------------------------------    
public int AddOpcion(String Opc, String Lit)
{
if (Opc==null || Lit==null)
    return(1);
ListaOpc.add(Opc);
ListaLit.add(Lit);
return(0);    
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
String retValue="<select size=\""+Size+"\" name=\""+Nombre+"\" value=\""+Value+"\" "+(Activado?"":" disabled=\"disabled\" ");
retValue+=CarcularEstilo();
if (OnChange!=null)
    retValue+=" onchange=\""+OnChange+"\" ";
if (MensStatus!=null && Activado)
    retValue+=" onmouseover=\"this.form.Status.value='"+MensStatus
              +"'\" onfocus=\"this.form.Status.value='"+MensStatus
              +"'\" onmouseout=\"this.form.Status.value=''\" onblur=\"this.form.Status.value=''\"";
retValue+=">\n"; 
for (int i=0; i<ListaOpc.size(); i++)
    {
    retValue+="<option ";
    if (Value.equalsIgnoreCase((String)ListaOpc.get(i)))
        retValue+="selected ";
    retValue+="value=\""+ListaOpc.get(i)+"\">"+ListaLit.get(i)+"</option>\n";
    }
retValue+="</select>\n";
return(retValue);
}
//-----------------------------------------------------------------------------------------------    

}
