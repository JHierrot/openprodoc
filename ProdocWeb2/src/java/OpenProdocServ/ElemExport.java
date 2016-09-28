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
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import static OpenProdocServ.ListElem.GenObj;
import OpenProdocUI.SParent;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ObjPD;
import prodoc.Record;


/**
 *
 * @author jhierrot
 */
public class ElemExport extends SParent
{   
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(200);
String TypeElem=Req.getParameter("Ty");
String Id=Req.getParameter("Id");
String Filter=Req.getParameter("F");
response.setHeader("Content-disposition", "attachment; filename=\"List_"+TypeElem+(Id!=null?("_"+Id):"")+".opd\"");
try {
DriverGeneric PDSession=SParent.getSessOPD(Req);    
ObjPD Obj=GenObj(TypeElem, PDSession, Id);
if (Id!=null)
    {
    out.print(Obj.StartXML());    
    out.print(Obj.toXML());
    out.print(Obj.EndXML());    
    }
else if (Filter!=null) // TODO vacio?
    {
    Cursor ListObj=Obj.SearchLike(Filter);
    Record NextObj=PDSession.NextRec(ListObj);
    out.print(Obj.StartXML());    
    while (NextObj!=null)
        {
        Obj.assignValues(NextObj);
        out.print(Obj.toXML());
        NextObj=PDSession.NextRec(ListObj);
        }   
    PDSession.CloseCursor(ListObj);
    out.print(Obj.EndXML());    
    }
} catch (Exception Ex)
        {
        out.println(Ex.getLocalizedMessage());
        }
finally {
out.close();
        }
}
//-----------------------------------------------------------------------------------------------
/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ElemExport Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ElemExport");
}
//-----------------------------------------------------------------------------------------------
}
