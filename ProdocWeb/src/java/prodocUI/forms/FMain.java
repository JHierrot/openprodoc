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
import prodoc.PDFolders;
import prodoc.PDRoles;
import prodocUI.servlet.*;

/**
 *
 * @author jhierrot
 */
public class FMain extends FBase
{
//-----------------------------------------------------------------------------------------------    


/** Creates a new instance of FBase
 * @param Req
 * @param pTitulo
 * @param pCabecera
 * @throws PDException
 */
public FMain(HttpServletRequest Req, String pTitulo, String pCabecera) throws PDException
{
super(Req, pTitulo, pCabecera);
setStrict(true);
AddOnLoad("init()");
AddCSS("MainMenu.css");
AddCSS("TestTree.css");
AddJS("MainMenu.js");
AddJS("Tree.js");
AddJS("Types.js");
//---- Menu -------------------------
// TODO revisar permisos para componer dinámicamente menú
String WorkArea="MainFrame";
Menu MainMenu=new Menu();
MenuCol MC1=new MenuCol(TT("Folders"), 0);
MainMenu.add(MC1);
PDRoles R=SMain.getSessOPD(Req).getUser().getRol();
if (R.isAllowCreateFolder())
    MC1.add(new MenuItem(TT("Add"), AddFold.getUrlServlet(), WorkArea));
if (R.isAllowMaintainFolder())
    MC1.add(new MenuItem(TT("Delete"), DelFold.getUrlServlet(), WorkArea));
if (R.isAllowMaintainFolder())
    MC1.add(new MenuItem(TT("Update"), ModFold.getUrlServlet(), WorkArea));
MC1.add(MenuItem.getMISeparator());
if (R.isAllowCreateFolder())
    MC1.add(new MenuItem(TT("Extended_Add"), AddFoldAdv.getUrlServlet(), WorkArea));
if (R.isAllowMaintainFolder())
    MC1.add(new MenuItem(TT("Update_Extended"), ModFoldAdv.getUrlServlet(), WorkArea));
//MC1.add(new MenuItem(TT("Refresh"), RefreshFold.getUrlServlet(), WorkArea));
MC1.add(MenuItem.getMISeparator());
MC1.add(new MenuItem(TT("Search_Folders"), SearchFold.getUrlServlet(), WorkArea));
MC1.add(MenuItem.getMISeparator());
MC1.add(new MenuItem(TT("Exit"), SExit.getUrlServlet()));
MenuCol MC2=new MenuCol(TT("Documents"), 1);
MainMenu.add(MC2);
if (R.isAllowCreateDoc())
    MC2.add(new MenuItem(TT("Add"), AddDoc.getUrlServlet(), WorkArea));
if (R.isAllowMaintainDoc())
    MC2.add(new MenuItem(TT("Delete"), DelDoc.getUrlServlet(), WorkArea));
MC2.add(MenuItem.getMISeparator());
if (R.isAllowCreateDoc())
    MC2.add(new MenuItem(TT("Extended_Add"), AddDocAdv.getUrlServlet(), WorkArea));
if (R.isAllowMaintainDoc())
    MC2.add(new MenuItem(TT("Update_Extended"), ModDocAdv.getUrlServlet(), WorkArea));
//MC2.add(new MenuItem(TT("Refresh"), RefreshFold.getUrlServlet(), WorkArea));
MC2.add(MenuItem.getMISeparator());
if (R.isAllowMaintainDoc())
    MC2.add(new MenuItem(TT("CheckOut"), CheckOut.getUrlServlet(), WorkArea));
if (R.isAllowMaintainDoc())
    MC2.add(new MenuItem(TT("CheckIn"), CheckIn.getUrlServlet(), WorkArea));
if (R.isAllowMaintainDoc())
    MC2.add(new MenuItem(TT("Cancel_CheckOut"), CancelCheckOut.getUrlServlet(), WorkArea));
MC2.add(new MenuItem(TT("List_of_Versions"), ListVer.getUrlServlet(), WorkArea));
MC2.add(MenuItem.getMISeparator());
MC2.add(new MenuItem(TT("Search_Documents"), SearchDoc.getUrlServlet(), WorkArea));
MenuCol MC3=new MenuCol(TT("Other_Tasks"), 2);
MainMenu.add(MC3);
if (R.isAllowCreateDoc() && R.isAllowMaintainDoc())
    MC3.add(new MenuItem(TT("Trash_bin"), SPaperBin.getUrlServlet(), WorkArea));
MC3.add(new MenuItem(TT("Password_change"), SPassChange.getUrlServlet(), WorkArea));
MenuCol MC4=new MenuCol(TT("Help"), 3);
MainMenu.add(MC4);
MC4.add(new MenuItem(TT("Contents"), "help/"+SParent.getLang(Req)+"/MainWin.html", "_blank"));
MC4.add(new MenuItem(TT("About"), About.getUrlServlet(), "_blank"));
MC4.add(new MenuItem(TT("Reporting_Bugs"), "https://docs.google.com/spreadsheet/viewform?formkey=dFF6ZndKWXFUQnJ0MWtVZWdUWk10X2c6MQ", "_blank"));
AddHead(MainMenu);
//---- Menu -------------------------
Table CentralArea=new Table(3,1,1);
CentralArea.setWidth(-100);
CentralArea.setHeight(600);
CentralArea.setCSSClass("CentArea");
CentralArea.getCelda(0,0).setWidth(300);
AddBody(CentralArea);
Tree Ar=new Tree();
Ar.add(new Branch(PDFolders.ROOTFOLDER, PDFolders.ROOTFOLDER, true, getStyle()));
CentralArea.getCelda(0,0).AddElem(Ar);
CentralArea.getCelda(0,0).setCSSId("IdTreeFrame");
CentralArea.getCelda(1,0).setCSSClass("UDFrames");
CentralArea.getCelda(1,0).setWidth(2);
CentralArea.getCelda(2,0).setCSSId("IdMain");
// CentralArea.getCelda(2,0).AddElem(new Element("<iframe src=\"RefreshDocs\" width=\"100%\" height=\"100%\" FRAMEBORDER=\"0\" name=\"MainFrame\"><p>Your browser does not support iframes.</p></iframe>"));
CentralArea.getCelda(2,0).AddElem(new Element("<iframe src=\"RefreshDocs\" width=\"100%\" height=\"600px\" FRAMEBORDER=\"0\" name=\"MainFrame\"><p>Your browser does not support iframes.</p></iframe>"));
}
//----------------------------------------------------------------------------
}
