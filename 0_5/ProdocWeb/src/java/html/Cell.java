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
public class Cell  extends Container
{
//-----------------------------------------------------------------------------------------------
private int VAlineacion=NADA;
private String ImagFondo=null;
/** Creates a new instance of Cell */
public Cell()
{
}
//-----------------------------------------------------------------------------------------------
protected String StartCont()
{
String RetVal;    
RetVal="<td"+CalcStyle();    
if (VAlineacion!=NADA) 
    {
    switch (VAlineacion)
        {
        case VTOP:      RetVal+=" valign=\"top\" ";
                        break;
        case VMIDDLE:   RetVal+=" valign=\"middle\" ";
                        break;
        case VBOTTOM:   RetVal+=" valign=\"bottom\" ";
                        break;
        case VBASELINE: RetVal+=" valign=\"baseline\" ";
                        break;
        }
    }
if (Alineacion!=NADA)
    {
    switch (Alineacion)
        {
        case CENTER: RetVal+=" align=\"center\" ";
                     break;
        case RIGHT:  RetVal+=" align=\"right\" ";
                     break;
        case JUST:   RetVal+=" align=\"justify\" ";
                     break;
        case LEFT:   RetVal+="align=\"left\" ";
                     break;
        }
    }
if (ImagFondo!=null)
    {
    RetVal+=" background=\""+ImagFondo+"\" ";
    }
RetVal+=" >\n";
return(RetVal);    
}
//-----------------------------------------------------------------------------------------------
protected String EndCont()
{
return("</td>\n");    
}
//-----------------------------------------------------------------------------------------------
public void setVertAlineacion(int pVAlineacion)
{
VAlineacion = pVAlineacion;
}    
//-----------------------------------------------------------------------------------------------
public void SetElem(Element e)
{
ListaElementos.clear();
ListaElementos.add(e);
}
//-----------------------------------------------------------------------------------------------
public void SetImagFondo(String pNomImg)
{
ImagFondo=pNomImg;

}
//-----------------------------------------------------------------------------------------------
}
