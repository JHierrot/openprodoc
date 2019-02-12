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
 * author: Joaquin Hierro      2018
 * 
 */
package OpenProdocUI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.ContribConf;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDObjDefs;
import prodoc.PDReport;
import prodoc.PDThesaur;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ContribList extends SParent
{
private static final String HtmlBase="<!DOCTYPE html>\n" +
"<html>" +
    "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UF-8\"/>" +
        "<title>OpenProdoc2 Web Contrib List</title>\n" +
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>\n" +       
        "@CSS@"+
    "</head>\n" +
    "<body class=\"CONTRIBBODY\" >\n" +
       "<form action=\"ContribAdd\" method=\"post\" class=\"CONTRIBFORM\"  accept-charset=\"UTF-8\">" +
       "<table align=\"center\"  class=\"CONTRIBTABLE\">\n" +
        "<tr><td>&nbsp</td></tr>" +
        "<tr><td>@LOGO@<H3>OpenProdoc</H3></td></tr>" +
        "<tr><td>"+
        "<fieldset class=\"CONTRIBFS\"><legend class=\"CONTRIBLEG\">&nbsp;&nbsp;@TITLELIST@&nbsp;&nbsp;</legend>\n"+
         "<table>\n" +   
          "@CONTRIBFIELDS@"+
          "<tr></tr><tr><td></td><td><select class=\"CONTRIBDOCTYPESCOMB\" name=\"CONTRIB_DT\">@CONTRIB_DT@</select><input  class=\"CONTRIBBUT\" type=\"submit\" value=\"  +  \"></td></tr>" +
        "<tr><td><a class=\"CONTRIBLOG\" href=\"ContribLogin?Id=@CONTRIBID@\">exit</a></td><td><a class=\"CONTRIBHELP\" href=\"@URLHELP@\" target=\"_blank\">?</a></td></tr>"+
          "</table>\n" +
         "</fieldset>" +
        "</td></tr>" +
       "</table>\n"+        
     "</form>\n" +
     "@DOCSCONT@"+
    "</body>" +
"</html>";

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws javax.servlet.ServletException
 * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
DriverGeneric LocalSess=null;
try {
if (!OPDFWLoaded)  
    {
    ProdocFW.InitProdoc("PD", getProdocProperRef());
    OPDFWLoaded=true;
    }
Req.setCharacterEncoding("UTF-8");
ContribConf ConfContr=getContribConf(Req);
LocalSess=ProdocFW.getSession(getConnector(), ConfContr.getUser(), ConfContr.getPass()); 
PDFolders F=getContribFolder(Req);
if (F==null) // direct from login
    {
    F=CheckFolder(Req, LocalSess, ConfContr);
    if (F==null) // non existing folder
        {
        if (ConfContr.isOpenContrib()) 
            {
            F=new PDFolders(LocalSess, ConfContr.getLoginFolderType());
            F.getRecSum();
            Vector<String> FToRead = ConfContr.getFieldsToRead();
            for (int i = 0; i < FToRead.size(); i++)
                {
                String Field = FToRead.elementAt(i);
                F.getRecSum().getAttr(Field).Import(Req.getParameter(Field));
                }
            PDFolders Ftmp=new PDFolders(LocalSess);
            F.setParentId(Ftmp.getIdPath(ConfContr.getBaseFolder()));
            F.setTitle("Title_"+System.currentTimeMillis()); // set an task event
            F.insert();
            } 
        else  // Access forbiden
            {
            ProdocFW.freeSesion(getConnector(), LocalSess);
            ContribLogin.ShowLoginContrib(Req, response);
            return;
            }
        }
    setContribFolder(Req, F);    
    }
response.setContentType("text/html;charset=UTF-8");
PrintWriter out = response.getWriter(); 
F.setDrv(LocalSess);
out.println(GenHtml(Req, LocalSess, ConfContr, F)); 
ProdocFW.freeSesion(getConnector(), LocalSess);
} catch (Exception Ex)
    {
    if (LocalSess!=null)  
        try {
        ProdocFW.freeSesion(getConnector(), LocalSess);
        } catch (Exception e){}
    ServletException SE= new ServletException(Ex.getLocalizedMessage());
    SE.setStackTrace(Ex.getStackTrace());
    throw SE;
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
return "ContribList Servlet";
}
//-----------------------------------------------------------------------------------------------
static synchronized private String GenHtml(HttpServletRequest Req, DriverGeneric LocalSess, ContribConf ConfContrib, PDFolders FoldUser) throws Exception
{
String HtmlFinal;   
String Agent=Req.getHeader("User-Agent");
String DimHtml=ConfContrib.SolveHtmlList(Agent);
Boolean is1Col=ConfContrib.Is1ColHtmlList(Agent);
if (DimHtml!=null) 
    {
    HtmlFinal=getHtml(LocalSess, DimHtml);
    }
else
    HtmlFinal=HtmlBase;
if (ConfContrib.getFormContribCSS()!=null)
    {
    if (ConfContrib.getFormContribCSS().startsWith("http"))    
       HtmlFinal=HtmlFinal.replace("@CSS@", "<link rel=\"STYLESHEET\" type=\"text/css\" href=\""+ConfContrib.getFormContribCSS()+"\"/>");
    else
       HtmlFinal=HtmlFinal.replace("@CSS@", GenCSS(LocalSess, ConfContrib.getFormContribCSS()));
    }
else
    HtmlFinal=HtmlFinal.replace("@CSS@", "");
if (ConfContrib.getFormContribLogo()!=null) // <img src=\"/SendDoc?Id="+ExtConf.getFormSearchLogo()+"\">"
    HtmlFinal=HtmlFinal.replace("@LOGO@", "<img src=\""+ConfContrib.getFormContribLogo()+"\">");
else
    HtmlFinal=HtmlFinal.replace("@LOGO@", "");
if (ConfContrib.getTitleList()!=null)
    HtmlFinal=HtmlFinal.replace("@TITLELIST@", ConfContrib.getTitleList());
else
    HtmlFinal=HtmlFinal.replace("@TITLELIST@", "Archive");
if (ConfContrib.getUrlHelp()!=null) 
    HtmlFinal=HtmlFinal.replace("@URLHELP@", ConfContrib.getUrlHelp());
else
    HtmlFinal=HtmlFinal.replace("@URLHELP@", "");
HtmlFinal=HtmlFinal.replace("@CONTRIBID@",ConfContrib.getId());
HtmlFinal=HtmlFinal.replace("@CONTRIB_DT@", CalcListTips(LocalSess, ConfContrib));
HtmlFinal=HtmlFinal.replace("@DOCSCONT@", genReport(LocalSess, ConfContrib, FoldUser));
Vector<String> FieldsToShow=ConfContrib.getFieldsToRead();
Record AttrDef = FoldUser.getRecSum();
StringBuilder Fields=new StringBuilder(3000);
Attribute Attr;
for (int i = 0; i < FieldsToShow.size(); i++)
    {
    String F = FieldsToShow.elementAt(i);
    Attr = AttrDef.getAttr(F).Copy();  
    if (Attr.getType()==Attribute.tBOOLEAN)
        {
        Fields.append(GenBoolVals(Req, Attr, is1Col, true));
        }
    else
        {
        if (Attr.getType()==Attribute.tTHES)
            {
            PDThesaur PDThes=new PDThesaur(LocalSess);
            if (Attr.getValue()!=null)
                {
                PDThes.Load((String)Attr.getValue());
                Attr.setValue(PDThes.getName());
                }
            }
        Fields.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"CONTRIBLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td>").append(is1Col?"</tr><tr>":"").append("<td class=\"TD_CONTRIBINP\"><input class=\"CONTRIBINP\" type=\"text\" readonly name=\"").append(Attr.getName()).append("\" value=\"").append(Attr.Export()).append("\"></td></tr>\n");
        }
    }
HtmlFinal=HtmlFinal.replace("@CONTRIBFIELDS@", Fields);
return(HtmlFinal);
}
//-----------------------------------------------------------------------------------------------
private static String GenCSS(DriverGeneric sessOPD, String formSearchCSS)
{
StringBuilder CSS=new StringBuilder();
CSS.append("<style>\n");
try {
PDDocs DocCSS=new PDDocs(sessOPD);
DocCSS.setPDId(formSearchCSS);
ByteArrayOutputStream OutBytes = new ByteArrayOutputStream();
DocCSS.getStream(OutBytes);
CSS.append(OutBytes.toString());
    } catch (Exception Ex)
        {        
        }
CSS.append("</style>\n");
return(CSS.toString());
}
//-----------------------------------------------------------------------------------------------
private PDFolders CheckFolder(HttpServletRequest Req, DriverGeneric sessOPD, ContribConf ConfContr) throws PDException
{
PDFolders F=new PDFolders(sessOPD, ConfContr.getLoginFolderType());
Record Rec = F.getRecSum();
String IdBaseF=F.getIdPath(ConfContr.getBaseFolder());
Vector<String> Fields2Search = ConfContr.getLoginFields();
Conditions Cs=new Conditions();
for (int i = 0; i < Fields2Search.size(); i++)
    {
    Attribute Attr=Rec.getAttr(Fields2Search.get(i));
    Attr.setValue(Req.getParameter(Fields2Search.get(i)));
    Condition c=new Condition(Attr);
    Cs.addCondition(c);
    }
Vector<Record> ResV = F.SearchV(ConfContr.getLoginFolderType(), Cs, false, false, IdBaseF, null);
if (ResV.size()!=1)
    return(null);
F.assignValues(ResV.get(0));
return(F);
}
//-----------------------------------------------------------------------------------------------
private static StringBuilder CalcListTips(DriverGeneric LocalSess, ContribConf ConfContrib) throws PDException
{
StringBuilder SelOps=new StringBuilder(1000);   
Vector<String> docTipesList = ConfContrib.getDocTipesList();
PDObjDefs Def=new PDObjDefs(LocalSess);
for (int i = 0; i < docTipesList.size(); i++)
    {
    Def.Load(docTipesList.elementAt(i));
    SelOps.append("<option value=\"").append(Def.getName()).append("\">").append(Def.getDescription()).append("</option>");
    }
return(SelOps);
}
//-----------------------------------------------------------------------------------------------
private static StringBuilder genReport(DriverGeneric LocalSess, ContribConf ConfContrib, PDFolders FoldUser) throws PDException
{  
PDDocs Doc=new PDDocs(LocalSess);
Cursor ListContDocs = Doc.getListContainedDocs(FoldUser.getPDId());
PDReport Rep=new PDReport(LocalSess);
Rep.Load(ConfContrib.getDocsReportId());
return (Rep.GenerateRepString(FoldUser.getPDId(), ListContDocs, null));
}
//-----------------------------------------------------------------------------------------------
}
