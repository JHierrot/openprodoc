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

import OpenProdocUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDObjDefs;

/**
 *
 * @author jhierrot
 */
public class AddAttr extends SParent
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
try {    
String ObjType=Req.getParameter("ObjType");    
PDObjDefs Def=new PDObjDefs(PDSession);
String Name=Req.getParameter("Name");  
String Username=Req.getParameter("UserName"); 
String Descrip=Req.getParameter("Descrip"); 
String Type=Req.getParameter("Type"); 
int TypeN=2;
for (int i = 0; i < ListAttrInherited.LTyp.length; i++)
    {
    if (Type.equals(ListAttrInherited.LTyp[i]))  
        {
        TypeN=i;
        break;
        }
    }
String Required=Req.getParameter("Req"); 
String LongStr=Req.getParameter("LongStr");    
if (LongStr.equals("_"))
    LongStr="0";
String Unique=Req.getParameter("UniKey"); 
String ModifAllow=Req.getParameter("ModAllow"); 
String Multival=Req.getParameter("Multi"); 
Attribute Attr=new Attribute(Name,Username, Descrip, TypeN, Required.equals("1"), null, Integer.parseInt(LongStr), false, Unique.equals("1"),ModifAllow.equals("1"),Multival.equals("1"));
Def.AddObjectTables(ObjType, Attr);
out.println("OK");
} catch (PDException ex)
    {
    out.println(ex.getLocalizedMessage());
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
return "AddAttr Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("AddAttr");
}
//-----------------------------------------------------------------------------------------------
}
