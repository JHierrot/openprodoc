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
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDTasksDef;
import prodoc.PDTasksExec;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class TestProcess extends SParent
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
out.println(GenListDoc(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "TestProcess Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListDoc(HttpServletRequest Req) 
{
StringBuilder Resp=new StringBuilder(3000);
//Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows><head>");
Resp.append("<rows><head>");
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
Date NDate=null;
if (NextDate!=null && NextDate.length()!=0)
    NDate=new Date(Long.parseLong(NextDate));
try {
Record Rec;
boolean IsDoc;
PDTasksExec TC=new PDTasksExec(PDSession);
if ( Task==PDTasksDef.fTASK_DELETE_OLD_FOLD 
   || Task==PDTasksDef.fTASK_IMPORT
   || Task==PDTasksDef.fTASK_EXPORT
   || Task==PDTasksDef.fTASK_FOLDSREPORT )
    {
    PDFolders F=new PDFolders(PDSession,ItemFold );
    Rec=F.getRecSum().CopyMono();
    TC.setObjType(ItemFold);    
    IsDoc=false;
    }
else
    {
    PDDocs D=new PDDocs(PDSession,ItemDoc );
    Rec=D.getRecSum().CopyMono();        
    TC.setObjType(ItemDoc);    
    IsDoc=true;
    }
Rec.initList();
Attribute Attr=Rec.nextAttr();
int Count=1;
while (Attr!=null)
    {
    Resp.append("<column width=\"").append(Count==Rec.NumAttr()?"*":600/Rec.NumAttr()).append("\" type=\""+(Attr.getName().equals(PDDocs.fTITLE)&&IsDoc?"link":"ro")+"\" align=\"left\" sort=\"str\">").append(TT(Req,Attr.getUserName())).append("</column>");
    Count++;
    Attr=Rec.nextAttr();
    }
Resp.append("</head>");
TC.setObjFilter(Filter);
TC.setDescription(TDesc);
TC.setObjType(TObj);
TC.setParam(T1);
TC.setParam2(T2);
TC.setParam3(T3);
TC.setParam4(T4);
TC.setNextDate(NDate);
TC.setType(Task);
Cursor CursorId=TC.GenCur();
Record NextObj=PDSession.NextRec(CursorId);
while (NextObj!=null)
    {
    String Id=(String)NextObj.getAttr(PDDocs.fPDID).getValue();
    Rec.assign(NextObj);
    Resp.append(SParent.GenRowGrid(Req, Id, Rec, true, false));    
    NextObj=PDSession.NextRec(CursorId);
    }
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
Resp.append("</rows>");
return(Resp.toString());
}
//-----------------------------------------------------------------------------------------------

}
