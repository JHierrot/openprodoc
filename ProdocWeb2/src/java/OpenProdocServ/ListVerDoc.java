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
import prodoc.PDThesaur;
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
 * @param out
 * @throws Exception
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
DriverGeneric PDSession=SParent.getSessOPD(Req);
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(3000);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><row>");
try {
PDDocs TmpDoc=new PDDocs(PDSession);
String CurrDoc=Req.getParameter("Id");
TmpDoc.Load(CurrDoc);
Record Rec=TmpDoc.getRecSum();
StringBuilder Head=new StringBuilder(300);
StringBuilder Edit=new StringBuilder(300);
StringBuilder Type=new StringBuilder(300);
Rec.initList();
Attribute Attr=Rec.nextAttr();
//ArrayList<Attribute> FL=new ArrayList();
while (Attr!=null)
    {
    if (!Attr.isMultivalued()) 
        {
        Head.append(TT(Req,Attr.getUserName())).append(",");
        if (Attr.getName().equals(PDDocs.fTITLE))
           Edit.append("link,");
        else if (Attr.getName().equals(PDDocs.fLOCKEDBY))
            Edit.append("rotxt,");
        else        
            Edit.append("ro,");
        Type.append("str,");
        }
    Attr=Rec.nextAttr();
    }
Head.deleteCharAt(Head.length()-1);
Edit.deleteCharAt(Edit.length()-1);
Type.deleteCharAt(Type.length()-1);
StringBuilder VersionsData=new StringBuilder(1000);
VersionsData.append("data={ rows:[");
Cursor ListVer = TmpDoc.ListVersions(TmpDoc.getDocType(), TmpDoc.getPDId());
Record NextVer=PDSession.NextRec(ListVer);
while (NextVer!=null)
    {
    VersionsData.append(SParent.GenRowGrid(Req, NextVer, false, true));    
    NextVer=PDSession.NextRec(ListVer);
    if (NextVer!=null)
        VersionsData.append(",");
    }
VersionsData.append("] };");
Resp.append("<LV>").append(Head).append("</LV>");       
Resp.append("<LV>").append(Edit).append("</LV>");       
Resp.append("<LV>").append(Type).append("</LV>");           
Resp.append("<LV>").append(VersionsData).append("</LV></row>");
} catch (PDException ex)
    {
    Resp.append("<LV>Error</LV></row>");
    }
out.println( Resp );   
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
static public String getUrlServlet()
{
return("ListVerDoc");
}
//-----------------------------------------------------------------------------------------------
}
