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
public class Container extends Element
{
protected Vector ListaElementos=new Vector();

protected Font f=null;
protected int Alineacion=NADA;
private int MargenIzq=0;
private int MargenDer=0;
private int Width=0;
private int Height=0;

private String OnClick=null;

private String ColorBack=null;
private String CadHtml=null;

//-----------------------------------------------------------------------------------------------
/** Creates a new instance of Container */
public Container()
{
}
//-----------------------------------------------------------------------------------------------
public int AddElem(Element E)
{
ListaElementos.add(E);
return(0);    
}
//-----------------------------------------------------------------------------------------------
@Override
public String ToHtml(HttpSession Sess)
{
Element E; 
StringBuilder ResHtml=new StringBuilder(1000);
ResHtml.append(StartCont());
for (int i=0; i<ListaElementos.size(); i++)
    {
    E=(Element)ListaElementos.get(i);
    ResHtml.append(E.ToHtml(Sess));
    }
ResHtml.append(EndCont());
return(ResHtml.toString());
}
//-----------------------------------------------------------------------------------------------
protected String StartCont()
{
return("");    
}
//-----------------------------------------------------------------------------------------------
protected String EndCont()
{
return("");    
}
//-----------------------------------------------------------------------------------------------
public void setMargenIzq(int pMargenIzq)
{
MargenIzq = pMargenIzq;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------
public void setMargenDer(int pMargenDer)
{
MargenDer = pMargenDer;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------
public void setFont(Font pf)
{
f = pf;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------
public void setWidth(int pWidth)
{
Width = pWidth;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------
public void setHeight(int pHeight)
{
Height = pHeight;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------

protected String CalcStyle()
{
String CStyle="";
if (Width<0)
    CStyle+=" width: "+(-Width)+"%; ";
else if (Width>0)
    CStyle+=" width: "+Width+"px; ";
if (Height<0)
    CStyle+=" height: "+(-Height)+"%; ";
else if (Height>0)
    CStyle+=" height: "+Height+"px; ";
switch (Alineacion)
    {
    case CENTER: CStyle+=" text-align: center; ";
                 break;
    case RIGHT:  CStyle+=" text-align: right; ";
                 break;
    case JUST:   CStyle+=" text-align: justify; ";
                 break;
    case LEFT:   CStyle+=" text-align: left; ";
                 break;
    }
if (f!=null)
    CStyle+=f.ToHtml();
if (MargenIzq!=0)
    CStyle+=" margin-left: "+MargenIzq+"px; ";
if (MargenDer!=0)
    CStyle+=" margin-right: "+MargenDer+"px; ";
if (ColorBack!=null)
    CStyle+=" background-color: rgb("+ColorBack+"); ";
if (CStyle.length()!=0)
    CStyle=" style=\""+CStyle+"\"";
if (getCSSClass()!=null)
    CStyle+=" class=\""+getCSSClass()+"\" ";
if (getCSSId()!=null)
    CStyle+=" id=\""+getCSSId()+"\" ";
if (OnClick!=null)
    CStyle+=" onclick=\""+OnClick+"\" ";
return(CStyle);
}
//-----------------------------------------------------------------------------------------------
public void setAlineacion(int pAlineacion)
{
Alineacion = pAlineacion;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------
public void setColorBack(String pColorBack)
{
ColorBack = pColorBack;
CadHtml=null;
}
//-----------------------------------------------------------------------------------------------
/**
* @return the OnClick
*/
public String getOnClick()
{
return OnClick;
}
//-----------------------------------------------------------------------------------------------
/**
* @param OnClick the OnClick to set
*/
public void setOnClick(String OnClick)
{
this.OnClick = OnClick;
}
//-----------------------------------------------------------------------------------------------
}
