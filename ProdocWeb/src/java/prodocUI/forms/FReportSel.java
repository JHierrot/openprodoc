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
 * author: Joaquin Hierro      2015
 * 
 */

package prodocUI.forms;


import html.*;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDReport;
import prodoc.Record;
import prodocUI.servlet.GenReport;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FReportSel extends FFormBase
{
//final static public String NumDocsPageName="NUMDOCSPAGE";
//final static public String NumPagesFileName="NUMPAGESFILE";
//public FieldText NumDocsPage;
//public FieldText NumPagesFile;
final static public int MODEFOLD=0;
final static public int MODEDOC=1;

/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FReportSel(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Reports_Generation"), pMode, pRec);
DriverGeneric PDSession=SParent.getSessOPD(Req);
AddCSS("prodoc.css");
AddJS("Types.js");
Table BorderTab=new Table(1, 3, 0);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
BorderTab.getCelda(0,0).AddElem(new Element(TT("Reports_Generation")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,2).AddElem(Status);
BorderTab.getCelda(0,2).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,2).AddElem(HHelp);
BorderTab.setContorno(true);
Table FormTab=new Table(3, 4, 0);
FormTab.setCellPadding(10);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
FormTab.getCelda(0,0).setHeight(30);
Table TabDocs=new Table(5,1,1);
TabDocs.setCellSpacing(0);
TabDocs.setWidth(-100);
TabDocs.setHeight(-100);
TabDocs.getFila(0).setCSSClass("ListDocsHead");
PDReport Rep=new PDReport(PDSession);
Record NextDoc=Rep.getRecSum();
Attribute AttrD=NextDoc.getAttr(PDReport.fTITLE);
TabDocs.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDReport.fDOCSPAGE);
TabDocs.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDReport.fPAGESDOC);
TabDocs.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDReport.fMIMETYPE);
TabDocs.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
TabDocs.getCelda(4,0).AddElem(new Element(TT("Generate_Report")));
String ActDoc=SParent.getActDocId(Req);
String DocId=null;
Cursor ListDocs=Rep.GetListReports();
int Row=0;
NextDoc=PDSession.NextRec(ListDocs);
while (NextDoc!=null)
    {
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDReport.fTITLE);
    TabDocs.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDReport.fDOCSPAGE);
    TabDocs.getCelda(1,Row).AddElem(new Element(((Integer)AttrD.getValue()).toString()));
    AttrD=NextDoc.getAttr(PDReport.fPAGESDOC);
    TabDocs.getCelda(2,Row).AddElem(new Element(((Integer)AttrD.getValue()).toString()));
    AttrD=NextDoc.getAttr(PDReport.fMIMETYPE);
    TabDocs.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
    DocId=(String)NextDoc.getAttr(PDDocs.fPDID).getValue();
    HiperlinkImag hv=new html.HiperlinkImag("img/"+getStyle()+"Report.png" , "Report", GenReport.getUrlServlet()+"?Type="+(pMode==MODEFOLD?"Fold":"Doc")+"&Id="+DocId, "");
    hv.setTarget("_blank");
    TabDocs.getCelda(4,Row).setAlineacion(Element.CENTER); 
    TabDocs.getCelda(4,Row).AddElem(hv);
    NextDoc=PDSession.NextRec(ListDocs);
    }
FormTab.getCelda(1,2).setCSSClass("ListDocs");
FormTab.getCelda(1,2).AddElem(TabDocs);
PDSession.CloseCursor(ListDocs);
FormTab.getCelda(1,3).AddElem(CancelButton);
Form LoginForm=new Form(Destination+"?Read=1","FormVal");
BorderTab.getCelda(0,1).AddElem(FormTab);
LoginForm.AddElem(BorderTab);
AddElem(LoginForm);
}
//-----------------------------------------------------------------------------------------------    
}
