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

package prodocUI.servlet;

import html.Element;
import html.Image;
import html.Page;
import html.Table;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDServer;

/**
 *
 * @author jhierrot
 */
public class About  extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDServer Serv=new PDServer(PDSession);
Serv.Load("Prodoc");
Page AboutPage=new Page(Req, "About", "");
AboutPage.setWidth(440);
AboutPage.setHeight(330);
AboutPage.AddCSS("prodoc.css");
Table TabAbout=new Table(2, 9, 0);
TabAbout.setCSSClass("FFormulReq");
TabAbout.getCelda(0,0).AddElem(new Element("OpenProdoc"));
TabAbout.getCelda(1,0).AddElem(new Image("img/LogoProdoc.jpg", "Logo OpenProdoc"));
TabAbout.getCelda(0,1).setHeight(10);
TabAbout.getCelda(0,2).AddElem(new Element("OPD Repository:"));
TabAbout.getCelda(1,2).AddElem(new Element(Serv.getVersion()));
TabAbout.getCelda(0,3).AddElem(new Element("OPD Web:"));
TabAbout.getCelda(1,3).AddElem(new Element(SParent.getVersion()));
TabAbout.getCelda(0,4).AddElem(new Element("OPD Engine:"));
TabAbout.getCelda(1,4).AddElem(new Element(DriverGeneric.getVersion()));
TabAbout.getCelda(0,5).setHeight(10);
AboutPage.AddElem(TabAbout);
TabAbout.getCelda(0,6).AddElem(new Element("Technical Design and Development:"));
TabAbout.getCelda(1,6).AddElem(new Element("Joaquín Hierro"));
TabAbout.getCelda(0,7).setHeight(10);
TabAbout.getCelda(0,8).AddElem(new Element("log4j, commons-net.ftp, commons-fileupload:"));
TabAbout.getCelda(1,8).AddElem(new Element("licence Apache Software Foundation"));
out.print(AboutPage.ToHtml(Req.getSession()));
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "About Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("About");
}
//-----------------------------------------------------------------------------------------------
}
