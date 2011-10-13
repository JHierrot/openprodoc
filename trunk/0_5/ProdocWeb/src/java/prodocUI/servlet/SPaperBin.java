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

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Cursor;
import prodoc.PDDocs;
import prodocUI.forms.FPaperBin;

/*
 * SPaperBin.java
 *
 * Created on 14 de abril de 2005, 18:34
 * 
 * @author jhierrot
 */
public class SPaperBin extends SParent
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
PDDocs d=new PDDocs(SMain.getSessOPD(Req) );
String Undel=Req.getParameter("Undel");
String DocType=Req.getParameter("DocType");  
if (Undel!=null && Undel.equals("1"))
    {
    String Id=Req.getParameter("Id");    
    d.UnDelete(DocType, Id);
    }
else 
    {
    String Purge=Req.getParameter("Purge");
    if (Purge!=null && Purge.equals("1"))
        {
        String Id=Req.getParameter("Id");    
        d.Purge(DocType, Id);
        }
    }
Cursor ListDocDeleted=null;
if (DocType!=null)
    ListDocDeleted=d.ListDeleted(DocType);
FPaperBin f=new FPaperBin(Req, DocType, d.getRecord(), getUrlServlet(), ListDocDeleted);
out.println(f.ToHtml(Req.getSession()));
return;
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "papelera";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("SPaperBin");
}
//-----------------------------------------------------------------------------------------------
}
