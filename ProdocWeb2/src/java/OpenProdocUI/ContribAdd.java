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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.ContribConf;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDObjDefs;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ContribAdd extends SParent
{
private static final String HtmlBase="<!DOCTYPE html>\n" +
"<html>" +
    "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
        "<title>OpenProdoc2 Web Contrib Add</title>\n" +
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>\n" +       
        "@CSS@"+
    "</head>\n" +
    "<body class=\"CONTRIBBODY\" >\n" +
       "<form action=\"ContribRes\" method=\"post\" class=\"CONTRIBFORM\" enctype=\"multipart/form-data\"  accept-charset=\"UTF-8\">" +
       "<table align=\"center\"  class=\"CONTRIBTABLE\">\n" +
        "<tr><td>&nbsp</td></tr>" +
        "<tr><td>@LOGO@<H3>OpenProdoc</H3></td></tr>" +
        "<tr><td>"+
        "<fieldset class=\"CONTRIBFS\"><legend class=\"CONTRIBLEG\">&nbsp;&nbsp;@DOCTITLE@&nbsp;&nbsp;</legend>\n"+
         "<table>\n" +   
          "@CONTRIB_DOCFIELDS@"+
          "<tr><td><div class=\"CONTRIBLABELFILEUP\">Archivo a aportar</div></td><td><input type=\"file\" class=\"CONTRIBFILEUP\" name=\"FileUp\"></td></tr>"+
          "<tr></tr><tr><td><a class=\"CONTRIBHELP\" href=\"@URLHELP@\" target=\"_blank\">?</a></td><td><a class=\"CONTRIBLISTURL\" href=\"ContribList\" ><-</a><input  class=\"CONTRIBBUT\" type=\"submit\" value=\"  +  \"></td></tr>" +
          "</table>\n" +
         "</fieldset>" +
        "</td></tr>" +
       "</table>\n"+       
        "<input type=\"hidden\" name=\"CONTRIB_DT\" value=\"@CONTRIBDTVAL@\">"+
     "</form>\n" +
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
ContribConf ConfContr=getContribConf(Req);
LocalSess=ProdocFW.getSession(getConnector(), ConfContr.getUser(), ConfContr.getPass()); 
PDFolders F=getContribFolder(Req);
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
return "ContribAdd Servlet";
}
//-----------------------------------------------------------------------------------------------
static synchronized private String GenHtml(HttpServletRequest Req, DriverGeneric LocalSess, ContribConf ConfContrib, PDFolders FoldUser) throws Exception
{
String HtmlFinal;   
String Agent=Req.getHeader("User-Agent");
String DimHtml=ConfContrib.SolveHtmlAdd(Agent);
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
if (ConfContrib.getUrlHelp()!=null) 
    HtmlFinal=HtmlFinal.replace("@URLHELP@", ConfContrib.getUrlHelp());
else
    HtmlFinal=HtmlFinal.replace("@URLHELP@", "");
String NameDocT=Req.getParameter("CONTRIB_DT");
HtmlFinal=HtmlFinal.replace("@CONTRIBDTVAL@", NameDocT); // for actual ADD servlet
PDObjDefs DocT=new PDObjDefs(LocalSess);
DocT.Load(NameDocT);
HtmlFinal=HtmlFinal.replace("@DOCTITLE@", DocT.getDescription());
PDDocs DocTmp=new PDDocs(LocalSess, NameDocT);
Record AttrDef = DocTmp.getRecSum();
StringBuilder Fields=new StringBuilder(3000);
Attribute Attr;
AttrDef.initList();
for (int i = 0; i < AttrDef.NumAttr(); i++)
    {
    Attr = AttrDef.nextAttr();   
    if (!ConfContrib.Allowed(NameDocT, Attr.getName()))
        continue; 
    switch (Attr.getType())
        {
        case Attribute.tTHES:
            Fields.append(GenThesVals(Req, LocalSess, Attr));
            break;
        case Attribute.tBOOLEAN:
            Fields.append(GenBoolVals(Req, Attr));
            break;
        default:
//            Fields.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"CONTRIBLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_CONTRIBINP\"><input class=\"CONTRIBINP\" type=\"text\" name=\"").append(Attr.getName()).append("\" value=\"").append(Attr.Export()).append("\"></td></tr>\n");
            Fields.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"").append(Attr.isRequired()?"CONTRIBLAB_LOGIN":"CONTRIBLAB").append("\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_CONTRIBINP\"><input class=\"CONTRIBINP\" type=\"text\" name=\"").append(Attr.getName()).append("\"><span class=\"tooltiptext\">").append(TT(Req,Attr.getDescription())).append("</span></td></tr>\n");
            break;
        }
    }
HtmlFinal=HtmlFinal.replace("@CONTRIB_DOCFIELDS@", Fields);
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
}
