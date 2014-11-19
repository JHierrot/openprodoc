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

package prodocUI.forms;

import html.*;
import javax.servlet.http.HttpServletRequest;
import prodocUI.servlet.SMain;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FBase extends Page
{
//-----------------------------------------------------------------------------------------------    
private Table PageTab;
private Table PageHead;

/** Creates a new instance of FBase
 * @param Req 
 * @param pTitulo
 * @param pCabecera
 */
public FBase(HttpServletRequest Req, String pTitulo, String pCabecera)
{ 
super(Req, pTitulo+SParent.getSessName(Req), pCabecera);
AddCSS("prodoc.css");
PageTab=new Table(1, 5, 0);
PageTab.setWidth(-100);
PageTab.setCellSpacing(0);
PageTab.getFila(0).setCSSClass("UDFrames");
PageTab.getFila(4).setCSSClass("UDFrames");
PageTab.getFila(4).setHeight(10);
AddElem(PageTab);
PageHead=new Table(3, 1, 0);
PageHead.setWidth(-100);
PageHead.getCelda(2,0).AddElem(new Image("img/"+getStyle()+"Logo48.jpg", "Logo OpenProdoc"));
PageHead.getCelda(0,0).setWidth(400);
PageHead.getCelda(2,0).setAlineacion(RIGHT);
PageHead.getCelda(1,0).setAlineacion(CENTER);
Paragraph p=new Paragraph();
p.setCSSClass("FTitle");
p.AddElem(new Element("OpenProdoc"));
PageHead.getCelda(1,0).AddElem(p);
PageTab.getCelda(0,0).AddElem(PageHead);
}
//-----------------------------------------------------------------------------------------------    
public void AddBody(Element e)
{
PageTab.getCelda(0,2).AddElem(e);
}
//-----------------------------------------------------------------------------------------------
public void AddHead(Element e)
{
PageHead.getCelda(0,0).AddElem(e);
}
//-----------------------------------------------------------------------------------------------
}
