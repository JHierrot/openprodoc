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

package prodocUI.forms;


import html.*;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.Record;
import prodocUI.servlet.SParent;
import prodocUI.servlet.SendDoc;

/**
 *
 * @author jhierrot
 */
public class FListVer extends FFormBase
{
public FieldText FoldTitle;

//final static private String ListExcluded=PDDocs.fDOCTYPE+"/"+PDDocs.fPARENTID+"/"+PDDocs.fPDID
//                        +"/"+PDDocs.fPDAUTOR+"/"+PDDocs.fPDDATE
//                        +"/"+PDDocs.fLOCKEDBY+"/"+PDDocs.fVERSION+"/"+PDDocs.fPURGEDATE
//                        +"/"+PDDocs.fREPOSIT+"/"+PDDocs.fSTATUS;

/** Creates a new instance of FMantFoldAdv
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination 
 * @param ListDocVers
 * @throws PDException
 */
public FListVer(HttpServletRequest Req, int pMode, Record pRec,String Destination, Cursor ListDocVers) throws PDException
{
super(Req, SParent.TT(Req,"List_of_Versions"), pMode, pRec);
DriverGeneric PDSession=SParent.getSessOPD(Req);
PDDocs DocAct=new PDDocs(PDSession);
DocAct.assignValues(pRec);
AddCSS("prodoc.css");
AddJS("Types.js");
Table Tab=new Table(1,2,0);
Tab.getCelda(0,0).setCSSId("HeaderRight");
Tab.setCellSpacing(0);
Tab.setWidth(-100);
Tab.setHeight(-100);
//StringBuilder DescFold=new StringBuilder("<b>"+DocAct.getTitle()+"</b> ("+DocAct.getDocType() +") - "+SParent.FormatTS(Req, DocAct.getPDDate())+ "<hr> ACL="+DocAct.getACL());
AddElem(Tab);
Table TabDocs=new Table(5,1,1);
TabDocs.setCellSpacing(0);
TabDocs.setWidth(-100);
TabDocs.setHeight(-100);
TabDocs.getFila(0).setCSSClass("ListDocsHead");
Record NextDoc=PDDocs.getRecordStructPDDocs();
Attribute AttrD=NextDoc.getAttr(PDDocs.fTITLE);
TabDocs.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fVERSION);
TabDocs.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fDOCDATE);
TabDocs.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fPDAUTOR);
TabDocs.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDDocs.fPDDATE);
TabDocs.getCelda(4,0).AddElem(new Element(TT(AttrD.getUserName())));
//PDDocs Doc=new PDDocs(PDSession);
int Row=0;
NextDoc=PDSession.NextRec(ListDocVers);
String Ver="";
HashMap ListDel=null;
while (NextDoc!=null)
    {
    if (ListDel==null)    
        ListDel=new HashMap();
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDDocs.fTITLE);
    Ver=(String)NextDoc.getAttr(PDDocs.fVERSION).getValue();
    ListDel.put(DocAct.getPDId()+"_"+Ver, NextDoc);
    HiperlinkText hv=new html.HiperlinkText(SendDoc.getUrlServlet()+"?Id="+DocAct.getPDId()+"&Ver="+Ver, (String)AttrD.getValue());
    hv.setTarget("_blank");
    TabDocs.getCelda(0,Row).AddElem(hv);
    AttrD=NextDoc.getAttr(PDDocs.fVERSION);
    TabDocs.getCelda(1,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDDocs.fDOCDATE);
    TabDocs.getCelda(2,Row).AddElem(new Element(SParent.FormatDate(Req,(Date)AttrD.getValue())));
    AttrD=NextDoc.getAttr(PDDocs.fPDAUTOR);
    TabDocs.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDDocs.fPDDATE);
    TabDocs.getCelda(4,Row).AddElem(new Element(SParent.FormatTS(Req,(Date)AttrD.getValue())));
    TabDocs.getFila(Row).setCSSClass("ListDocs");
    TabDocs.getFila(Row).setOnClick("ShowDoc('"+DocAct.getPDId()+"_"+Ver+"')");
    NextDoc=PDSession.NextRec(ListDocVers);
    }
Tab.getCelda(0,1).setCSSClass("ListDocs");
Tab.getCelda(0,1).AddElem(TabDocs);
SParent.setListDocs(Req, ListDel);
PDSession.CloseCursor(ListDocVers);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
return("ListVersions");
}
//-----------------------------------------------------------------------------------------------    
}
