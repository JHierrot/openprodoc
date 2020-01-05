/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU Affero GPL v3 http://www.gnu.org/licenses/agpl.html
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.DriverGeneric;
import prodoc.PDTasksDef;
import prodoc.PDTasksExec;

/**
 *
 * @author jhierrot
 */
public class RunProcess extends SParent
{
/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "RunProcess Servlet";
}
//-----------------------------------------------------------------------------------------------
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(3000);
//Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
DriverGeneric PDSession=getSessOPD(Req);
int Task=Integer.parseInt(Req.getParameter("Task"));
String T1=Req.getParameter("T1");
String T2=Req.getParameter("T2");
String T3=Req.getParameter("T3");
String T4=Req.getParameter("T4");
String TDesc=Req.getParameter("TDesc");
String TObj=Req.getParameter("TObj");
String Filter=Req.getParameter("Filter");
String ItemFold=Req.getParameter("Item");
String ItemDoc=Req.getParameter("Item2");
String NextDate=Req.getParameter("NextDate");
String Name=Req.getParameter("Name");
if (Name==null)
    Name="Test";
String Descrip=Req.getParameter("Descrip");
if (Descrip==null)
    Descrip="Test";
Date NDate=null;
if (NextDate!=null && NextDate.length()!=0)
    NDate=new Date(Long.parseLong(NextDate));
try {
PDTasksExec TC=new PDTasksExec(PDSession);
if ( Task==PDTasksDef.fTASK_DELETE_OLD_FOLD 
   || Task==PDTasksDef.fTASK_IMPORT
   || Task==PDTasksDef.fTASK_EXPORT
   || Task==PDTasksDef.fTASK_FOLDSREPORT 
   || Task==PDTasksDef.fTASK_LOCALSYNC )
    {
    TC.setObjType(ItemFold);    
    }
else
    {      
    TC.setObjType(ItemDoc);    
    }
TC.setName(Name);
TC.setDescription(Descrip);
TC.setObjFilter(Filter);
TC.setDescription(TDesc);
TC.setObjType(TObj);
TC.setParam(T1);
TC.setParam2(T2);
TC.setParam3(T3);
TC.setParam4(T4);
TC.setNextDate(NDate);
TC.setType(Task);
TC.Execute();
Resp.append("<status>OK</status>");
} catch (Exception Ex)
    {
    Resp.append("<status>"+TT(Req,Ex.getLocalizedMessage())+"</status>");    
    Ex.printStackTrace();
    }
finally 
    {
    out.println(Resp.toString());
    out.close();
    }
}
//-----------------------------------------------------------------------------------------------

}
