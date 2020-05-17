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

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SThesRec extends SParent
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
DriverGeneric PDSession=getSessOPD(Req);
String FoldId=Req.getParameter("ThesId");
PDThesaur Thes=new PDThesaur(PDSession);
Thes.Load(FoldId);
out.println(CreateForm(Req, Thes));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "SThesRec Servlet";
}
//-----------------------------------------------------------------------------------------------
private String CreateForm(HttpServletRequest Req, PDThesaur ThesAct)
{  
StringBuilder Html=new StringBuilder(5000);
try {    
Html.append("<html>\n" +
"    <head>\n" +
"        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
"        <title>OpenProdoc2 Web</title>\n" +
"        <link rel=\"STYLESHEET\" type=\"text/css\" href=\"css/OpenProdoc.css\">\n" +
"<body><div class=\"OPDForm\"><b>"+ThesAct.getName()+"</b><br>");
PDThesaur U=new PDThesaur(ThesAct.getDrv());
Record Rec=ThesAct.getRecord();
DriverGeneric PDSession=ThesAct.getDrv();
Attribute Attr;
Attr=Rec.getAttr(PDThesaur.fDESCRIP);
if (Attr.getValue()!=null & Attr.Export().length()>0)
    {
    Html.append(TT(Req, Attr.getUserName())).append("= <b>");
    Html.append(Attr.ExportXML()+"</b><br>");
    }
Attr=Rec.getAttr(PDThesaur.fSCN);
if (Attr.getValue()!=null & Attr.Export().length()>0)
    {
    Html.append(TT(Req, Attr.getUserName())).append("= <b>");
    Html.append(Attr.ExportXML()+"</b><br>");
    }
Attr=Rec.getAttr(PDThesaur.fLANG);
if (Attr.getValue()!=null & Attr.Export().length()>0)
    {
    Html.append(TT(Req, Attr.getUserName())).append("= <b>");
    Html.append(Attr.Export()+"</b><br>");
    }
Attr=Rec.getAttr(PDThesaur.fUSE);
if (Attr.getValue()!=null & Attr.Export().length()>0)
    {
    U.Load((String)Attr.getValue());
    Html.append(TT(Req, Attr.getUserName())).append("= <b>");
    Html.append(U.getName()+"</b><br>");
    }
if (!ThesAct.getListRT(ThesAct.getPDId()).isEmpty())
    {
    Cursor ListRT = ThesAct.ListRT(ThesAct.getPDId());
    Html.append("<b>"+TT(Req, "Related_Terms")+"</b><br>");
    Html.append(Gentab(Req,PDSession, ListRT));
    }
if (!ThesAct.getListUF(ThesAct.getPDId()).isEmpty())
    {
    Cursor ListUF = ThesAct.ListUF(ThesAct.getPDId());
    Html.append("<b>"+TT(Req, "Used_For")+"</b><br>");   
    Html.append(Gentab(Req,PDSession, ListUF));
    }
if (!ThesAct.getListLang(ThesAct.getPDId()).isEmpty())
    {
    Cursor ListLang = ThesAct.ListLang(ThesAct.getPDId());
    Html.append("<b>"+TT(Req, "Translations")+"</b><br>");   
    Html.append(Gentab(Req,PDSession, ListLang));
    }
Html.append("</div></body>");
} catch (Exception Ex)
    {
    return ("<html><body>"+Ex.getLocalizedMessage()+"</body>");
    }
return(Html.toString());
}
//-----------------------------------------------------------------------------------------------

private StringBuilder Gentab(HttpServletRequest Req, DriverGeneric PDSession, Cursor ListRT) 
{
StringBuilder Html=new StringBuilder(2000);   
try {    
Attribute Attr;
PDThesaur U=new PDThesaur(PDSession);
String Val="";
Record NextTerm=PDSession.NextRec(ListRT);  
Html.append("<table class=\"TabTerm\"><tr class=\"HeadTerm\">");   
Attr=NextTerm.getAttr(PDThesaur.fNAME);
Html.append("<td>").append(TT(Req,Attr.getUserName())).append("</td>");
Attr=NextTerm.getAttr(PDThesaur.fDESCRIP);
Html.append("<td>").append(TT(Req,Attr.getUserName())).append("</td>");
Attr=NextTerm.getAttr(PDThesaur.fUSE);
Html.append("<td>").append(TT(Req,Attr.getUserName())).append("</td>");
Attr=NextTerm.getAttr(PDThesaur.fLANG);
Html.append("<td>").append(TT(Req,Attr.getUserName())).append("</td></tr>");
while (NextTerm!=null)
    {
    Html.append("<tr>");
    Attr=NextTerm.getAttr(PDThesaur.fNAME);
    Html.append("<td>").append(Attr.getValue()).append("</td>");
    Attr=NextTerm.getAttr(PDThesaur.fDESCRIP);
    Html.append("<td>").append(Attr.getValue()!=null?Attr.getValue():"").append("</td>");
    Attr=NextTerm.getAttr(PDThesaur.fUSE);
    if (Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
        {
        U.Load((String)Attr.getValue());
        Val=U.getName();
        } 
    else 
        Val="";
    Html.append("<td>").append(Val).append("</td>");
    Attr=NextTerm.getAttr(PDThesaur.fLANG);
    Html.append("<td>").append(Attr.getValue()!=null?Attr.getValue():"").append("</td>");
    Html.append("</tr>");
    NextTerm=PDSession.NextRec(ListRT);
    }
Html.append("</table><br>");
} catch (Exception Ex)
    {
    return (new StringBuilder("Error:"+Ex.getLocalizedMessage()));
    }
return(Html);
}
//-----------------------------------------------------------------------------------------------
}
