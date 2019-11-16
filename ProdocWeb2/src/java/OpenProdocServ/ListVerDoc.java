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
import static OpenProdocUI.SParent.TT;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ListVerDoc extends SParent
{

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
     * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
DriverGeneric PDSession=SParent.getSessOPD(Req);
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(3000);
//Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows><head>");
Resp.append("<rows><head>");
try {
PDDocs TmpDoc=new PDDocs(PDSession);
String CurrDoc=Req.getParameter("Id");
TmpDoc.Load(CurrDoc);
Record Rec=TmpDoc.getRecSum().CopyMono();
Rec.delAttr(PDDocs.fSTATUS);
Rec.delAttr(PDDocs.fDOCTYPE);
Rec.initList();
Attribute Attr=Rec.nextAttr();
int Count=1;
while (Attr!=null)
    {
    Resp.append("<column width=\"").append(Count==Rec.NumAttr()?"*":600/Rec.NumAttr()).append("\" type=\""+(Attr.getName().equals(PDDocs.fTITLE)?"link":"ro")+"\" align=\"left\" sort=\"str\">").append(TT(Req,Attr.getUserName())).append("</column>");
    Count++;
    Attr=Rec.nextAttr();
    }
Resp.append("</head>");
Cursor ListVer = TmpDoc.ListVersions(TmpDoc.getDocType(), TmpDoc.getPDId());
Record NextVer=PDSession.NextRec(ListVer);
while (NextVer!=null)
    {
    String Id=(String)NextVer.getAttr(PDDocs.fPDID).getValue()+"%7C"; // "|" removed by problems with Tomcat
    Id+=(String)NextVer.getAttr(PDDocs.fVERSION).getValue();   
    Rec.assign(NextVer);
    Resp.append(SParent.GenRowGrid(Req, Id, Rec, true));       
    NextVer=PDSession.NextRec(ListVer);
    }
Resp.append("</rows>");
} catch (PDException ex)
    {
    Resp.append("<LV>Error</LV></row>");
    }
out.println(  Resp );   
out.close();
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ListVerDoc Servlet";
}
//-----------------------------------------------------------------------------------------------
}
