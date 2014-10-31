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
import prodoc.PDException;
import prodoc.PDRoles;
import prodoc.PDThesaur;
import prodocUI.servlet.*;

/**
 *
 * @author jhierrot
 */
public class FMainThes extends FBase
{
//-----------------------------------------------------------------------------------------------    


/** Creates a new instance of FBase
 * @param Req
 * @param pTitulo
 * @param pCabecera
 * @throws PDException
 */
public FMainThes(HttpServletRequest Req, String pTitulo, String pCabecera) throws PDException
{
super(Req, pTitulo, pCabecera);
setStrict(true);
AddOnLoad("init()");
AddCSS("MainMenu.css");
AddCSS("TestTree.css");
AddJS("MainMenu.js");
AddJS("ThesTree.js");
//---- Menu -------------------------
// TODO revisar permisos para componer dinámicamente menú
String ThesWorkArea="ThesMainFrame";
Menu MainMenu=new Menu();
MenuCol MC1=new MenuCol(TT("Thesaurus"), 0);
MainMenu.add(MC1);
PDRoles R=SMain.getSessOPD(Req).getUser().getRol();
if (R.isAllowCreateThesaur())
    MC1.add(new MenuItem(TT("Create_Theusurus"), AddThes.getUrlServlet(), ThesWorkArea));
if (R.isAllowMaintainThesaur())
    MC1.add(new MenuItem(TT("Update_Thesaurus"), ModThes.getUrlServlet(), ThesWorkArea));
if (R.isAllowMaintainThesaur())
    MC1.add(new MenuItem(TT("Delete_Thesaurus"), DelThes.getUrlServlet(), ThesWorkArea));
MC1.add(MenuItem.getMISeparator());
MC1.add(new MenuItem(TT("Close_Window"), SExit.getUrlServlet()));
MenuCol MC2=new MenuCol(TT("Terms"), 1);
MainMenu.add(MC2);
if (R.isAllowCreateThesaur())
    MC2.add(new MenuItem(TT("Add_Term"), AddTerm.getUrlServlet(), ThesWorkArea));
if (R.isAllowMaintainThesaur())
    MC2.add(new MenuItem(TT("Update_Term"), ModTerm.getUrlServlet(), ThesWorkArea));
if (R.isAllowMaintainThesaur())
    MC2.add(new MenuItem(TT("Delete_Term"), DelTerm.getUrlServlet(), ThesWorkArea));
MC2.add(MenuItem.getMISeparator());
MC2.add(new MenuItem(TT("Search_Terms"), SearchThes.getUrlServlet(), ThesWorkArea));
MenuCol MC4=new MenuCol(TT("Help"), 2);
MainMenu.add(MC4);
MC4.add(new MenuItem(TT("Thesaurus_MainWin"), "help/"+SParent.getHelpLang(Req)+"/MainWinThes.html", "_blank"));
AddHead(MainMenu);
//---- Menu -------------------------
Table CentralArea=new Table(3,1,1);
CentralArea.setWidth(-100);
CentralArea.setHeight(600);
CentralArea.setCSSClass("CentArea");
CentralArea.getCelda(0,0).setWidth(300);
AddBody(CentralArea);
Tree Ar=new Tree("RefreshThes", "ThesMainFrame", "ThesExpandContract", "ThesId");
Ar.add(new Branch(PDThesaur.ROOTTERM, PDThesaur.ROOTTERM, true, getStyle()));
CentralArea.getCelda(0,0).AddElem(Ar);
CentralArea.getCelda(0,0).setCSSId("IdTreeFrame");
CentralArea.getCelda(1,0).setCSSClass("UDFrames");
CentralArea.getCelda(1,0).setWidth(2);
CentralArea.getCelda(2,0).setCSSId("IdMain");
CentralArea.getCelda(2,0).AddElem(new Element("<iframe src=\"RefreshThes\" width=\"100%\" height=\"600px\" FRAMEBORDER=\"0\" name=\"ThesMainFrame\"><p>Your browser does not support iframes.</p></iframe>"));
}
//----------------------------------------------------------------------------
}
