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
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDReport;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class RepList extends SParent
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
return "RepList Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListDoc(HttpServletRequest Req) 
{
StringBuilder FolderDocs=new StringBuilder(3000);
String Type=Req.getParameter("Type");
//FolderDocs.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows>");
FolderDocs.append("<rows>");
DriverGeneric PDSession=getSessOPD(Req);
Attribute AttrD;
try {
Record NextDoc=PDDocs.getRecordStructPDDocs();
PDReport Rep=new PDReport(PDSession);
Cursor ListDocs=Rep.GetListReports();
NextDoc=PDSession.NextRec(ListDocs);
String DocId;
while (NextDoc!=null)
    {  
    AttrD=NextDoc.getAttr(PDDocs.fPDID);  
    DocId=(String)AttrD.getValue();
    FolderDocs.append("<row id=\"").append(DocId).append("\">");       
    AttrD=NextDoc.getAttr(PDDocs.fTITLE);
    FolderDocs.append("<cell>").append(AttrD.getValue()).append("^GenReport?Id=").append(DocId).append("^_blank").append("</cell>");       
    AttrD=NextDoc.getAttr(PDDocs.fMIMETYPE);
    FolderDocs.append("<cell>").append(AttrD.Export()).append("</cell>");       
    AttrD=NextDoc.getAttr(PDReport.fDOCSPAGE);
    FolderDocs.append("<cell>").append(AttrD.Export()).append("</cell>");       
    AttrD=NextDoc.getAttr(PDReport.fPAGESDOC);
    FolderDocs.append("<cell>").append(AttrD.Export()).append("</cell></row>");
    NextDoc=PDSession.NextRec(ListDocs);
    }
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
FolderDocs.append("</rows>");
return(FolderDocs.toString());
}
//-----------------------------------------------------------------------------------------------

}
