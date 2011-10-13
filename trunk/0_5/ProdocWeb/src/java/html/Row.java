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
public class Row  extends Container
{
int NumCol;
private int BordeFila=-1;        

/** Creates a new instance of Row
 * @param pNumCol 
 */
public Row(int pNumCol)
{
NumCol=pNumCol;    
for (int i=0; i<NumCol; i++)
    {
    Cell Cel=new Cell();
    ListaElementos.add(null);
    }
}
//-----------------------------------------------------------------------------------------------
public String ToHtml(HttpSession Sess)
{
String retValue="<tr"+CalcStyle();
if (BordeFila!=-1)
    retValue+=" border=\""+BordeFila+"\"";
retValue+=">\n";
for (int c=0; c<NumCol; c++)
    {
    if  (c<ListaElementos.size() && ListaElementos.get(c)!=null)
        {
        retValue+=getCeldaNula(c).ToHtml(Sess);
        }      
    else
        retValue+="<td> </td>";
    }
retValue+="</tr>\n";
return(retValue);
}
//-----------------------------------------------------------------------------------------------
public Cell getCeldaNula(int pCol)
{
if (pCol<0 || pCol>=NumCol)
    return(null);
return((Cell)ListaElementos.get(pCol));
}
//-----------------------------------------------------------------------------------------------
public Cell getCelda(int pCol)
{
if (ListaElementos.get(pCol)==null)
    {
    Cell Cel=new Cell();
    ListaElementos.set(pCol, Cel);
    }
return((Cell)ListaElementos.get(pCol));
}
//-----------------------------------------------------------------------------------------------
public int setCelda(int pCol, Cell Cel)
{
if (pCol<0 || pCol>=NumCol)
    return(1);
ListaElementos.set(pCol, Cel);
return(0);
}
//-----------------------------------------------------------------------------------------------

public void setBordeFila(int pBordeFila)
{
BordeFila = pBordeFila;
}
//-----------------------------------------------------------------------------------------------
}
