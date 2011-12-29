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

package prodocServ;

import java.io.*;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.ProdocFW;
import prodocUI.forms.FLogin;



/**
 *
 * @author jhierrot
 * @version
 */
public class ServParent extends HttpServlet
{

private DriverGeneric Con=null;

/** Initializes the servlet.
*/
public void init(ServletConfig config) throws ServletException
{
super.init(config);

}
//-----------------------------------------------------------------------------------------------

/** Destroys the servlet.
*/
public void destroy()
{

}
//-----------------------------------------------------------------------------------------------

/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
* @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException
*/
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
response.setContentType("text/xml;charset=UTF-8");
PrintWriter out = response.getWriter();
try {
if (!Connected(request))
    {
    AnswerNotConected(request, out);
    }
else
    {
    ProcessPage(request, out);
    }
} catch (Exception e)
    {
    out.println(e.getMessage());
    e.printStackTrace(out);
    e.printStackTrace();
    AddLog(e.getMessage());
    }
finally {
        out.close();
        }
}
//-----------------------------------------------------------------------------------------------
protected void AddLog(String Texto)
{
System.out.println(this.getServletName()+":"+new Date()+"="+Texto);
}
//-----------------------------------------------------------------------------------------------
protected boolean Connected(HttpServletRequest Req) throws PDException
{
HttpSession Sess=Req.getSession(true);
DriverGeneric D=(DriverGeneric)Sess.getAttribute("PRODOC_SESS");
if (D!=null)
    {
    Con=D;
    return(true);
    }
FLogin f=new FLogin(Req, "Login Prodoc", "");
String Nombre;
String Clave;
// nos estamos validando en login
Nombre=f.NomUsu.getValue(Req);
Clave=f.Clave.getValue(Req);
if (Nombre==null || Nombre.length()==0)
    return(false);
if (Clave==null || Clave.length()==0)
    return(false);
String DB="PD";
ProdocFW.InitProdoc(DB, "/media/Iomega/Prodoc/Prodoc/src/prodoc/Prodoc.properties");
////Instalar(DB);
D=ProdocFW.getSession(DB, Nombre, Clave);
Con=D;
Sess.setAttribute("PRODOC_SESS", D);
return(true);
}
//-----------------------------------------------------------------------------------------------
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
throw new PDException("Página Inexistente");
}
//-----------------------------------------------------------------------------------------------

/** Handles the HTTP <code>GET</code> method.
* @param request servlet request
* @param response servlet response
*/
protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException
{
processRequest(request, response);
}
//-----------------------------------------------------------------------------------------------

/** Handles the HTTP <code>POST</code> method.
* @param request servlet request
* @param response servlet response
*/
protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException
{
processRequest(request, response);
}
//-----------------------------------------------------------------------------------------------
/** Returns a short description of the servlet.
*/
public String getServletInfo()
{
return "Servlet Base del resto de aplicacion";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ServParent");
}
//-----------------------------------------------------------------------------------------------
/**
* @return the Con
*/
protected DriverGeneric getCon()
{
return Con;
}
//-----------------------------------------------------------------------------------------------
protected void AnswerNotConected(HttpServletRequest request, PrintWriter out)
{
out.println("<error>no conectado</error>");
}
//-----------------------------------------------------------------------------------------------
static public String TT(HttpServletRequest Req, String Text)
{
if (getSession(Req)==null)
    return( DriverGeneric.DefTT(Req.getLocale().getLanguage(), Text) );
else
    return( getSession(Req).TT(Text) );
}
//----------------------------------------------------------
public static DriverGeneric getSession(HttpServletRequest Req)
{
HttpSession Sess=Req.getSession(true);
return (DriverGeneric)Sess.getAttribute("PRODOC_SESS");
}
//----------------------------------------------------------


}



