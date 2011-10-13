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

/**
 *
 * @author jhierrot
 */
public class Table extends Container
{
private int NumCol=0;
private int NumFil=0;
private int Border=0;
private int CellPadding=-1;
private int CellSpacing=-1;
//private String CadHtml=null;
private boolean Contorno=false;
//-----------------------------------------------------------------------------------------------
/** Creates a new instance of Tabla */
public Table(int pNumCol,int pNumFil, int pBorder)
{
NumCol=pNumCol;
NumFil=pNumFil;   
Border=pBorder;
for (int i=0; i<NumFil; i++)
    {
    Row Fil=new Row(NumCol);
    ListaElementos.add(Fil);
    }
}
///-----------------------------------------------------------------------------------------------
public int AddFila()
{
Row Fil=new Row(NumCol);
ListaElementos.add(Fil);    
return(++NumFil);
}
///-----------------------------------------------------------------------------------------------
protected String StartCont()
{
//if (CadHtml!=null)
//    return(CadHtml);
String CadHtml="<table"+CalcStyle();
if (Border!=0)
    CadHtml+=" border=\""+Border+"\"";
if (CellPadding>=0)
    CadHtml+=" cellpadding=\""+CellPadding+"\"";
if (CellSpacing>=0)
    CadHtml+=" cellspacing=\""+CellSpacing+"\""; 
if (Contorno)
    CadHtml+=" frame=\"box\""; 
CadHtml+="><tbody>\n";
return(CadHtml);    
}
//-----------------------------------------------------------------------------------------------
protected String EndCont()
{
return("</tbody></table>\n");    
}

//-----------------------------------------------------------------------------------------------

public void setCellPadding(int pCellPadding)
{
CellPadding = pCellPadding;
}
//-----------------------------------------------------------------------------------------------

public void setCellSpacing(int pCellSpacing)
{
CellSpacing = pCellSpacing;
}
//-----------------------------------------------------------------------------------------------
public int SetAnchoCol(int pNumCol, int Ancho)
{
if (pNumCol<0 || pNumCol>=NumCol)
    return(1);    
for (int i=0; i<NumFil; i++)
    {
    getCelda(pNumCol, i).setWidth(Ancho);
    }
return(0);    
}
//-----------------------------------------------------------------------------------------------
public Row getFila(int pFil)
{
if (pFil<0 || pFil>=NumFil)
    return(null);
return((Row)ListaElementos.get(pFil));
}
//-----------------------------------------------------------------------------------------------
public Cell getCeldaNula(int pCol, int pFil)
{
return(getFila(pFil).getCeldaNula(pCol));
}
//-----------------------------------------------------------------------------------------------
public Cell getCelda(int pCol, int pFil)
{
return(getFila(pFil).getCelda(pCol));
}
//-----------------------------------------------------------------------------------------------
public int setCelda(int pCol, int pFil, Cell Cel)
{
return(getFila(pFil).setCelda(pCol, Cel));
}
//----------------------------------------------------------------------------------

public void setContorno(boolean pContorno)
{
Contorno = pContorno;
}
//-----------------------------------------------------------------------------------------------

public int getNumCol()
{
return NumCol;
}

//-----------------------------------------------------------------------------------------------
public int getNumFil()
{
return NumFil;
}
//-----------------------------------------------------------------------------------------------

}
