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
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.ContribConf;
import prodoc.DriverGeneric;
import prodoc.ExtConf;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ContribList extends SParent
{
private static final HashMap<String,String> ContribLogins=new HashMap(); 
private static final HashMap<String,ContribConf> Confs=new HashMap(); 
private static Date LastCacheUpdate=null;
private static final long CacheCaducity=1*1*60000;
private static final String HtmlBase="<!DOCTYPE html>\n" +
"<html>" +
    "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
        "<title>OpenProdoc2 Web Contrib List</title>\n" +
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>\n" +       
        "@CSS@"+
    "</head>\n" +
    "<body class=\"CONTRIBBODY\" >\n" +
       "<form action=\"ContribList\" method=\"post\" class=\"CONTRIBFORM\">" +
       "<table align=\"center\"  class=\"CONTRIBTABLE\">\n" +
        "<tr><td>&nbsp</td></tr>" +
        "<tr><td>@LOGO@<H3>OpenProdoc</H3></td></tr>" +
        "<tr><td>"+
        "<fieldset class=\"CONTRIBFS\"><legend class=\"CONTRIBLEG\">&nbsp;&nbsp;@TITLELIST@&nbsp;&nbsp;</legend>\n"+
         "<table>\n" +   
          "@CONTRIBFIELDS@"+
          "<tr><td><a class=\"CONTRIBHELP\" href=\"@URLHELP@\" target=\"_blank\">?</a></td><td><input  class=\"CONTRIBBUT\" type=\"submit\" value=\"  Ok  \"></td></tr>" +
          "</table>\n" +
         "</fieldset>" +
        "</td></tr>" +
       "</table>\n"+        
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
response.setContentType("text/html;charset=UTF-8");
PrintWriter out = response.getWriter(); 
DriverGeneric LocalSess=null;
try {
if (!OPDFWLoaded)  
    {
    ProdocFW.InitProdoc("PD", getProdocProperRef());
    OPDFWLoaded=true;
    }
//String IdContrib=Req.getParameter("IdContrib"); 
//if (IdContrib==null)
//    throw new Exception("Inexistent Contrib Form");
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
            // go to login
            ProdocFW.freeSesion(getConnector(), LocalSess);
            return;
            }
        }
    setContribFolder(Req, F);    
    }
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
return "ContribLogin Servlet";
}
//-----------------------------------------------------------------------------------------------
static synchronized private String GenHtml(HttpServletRequest Req, DriverGeneric LocalSess, ContribConf ConfContrib, PDFolders FoldUser) throws Exception
{
String HtmlFinal;   
String Agent=Req.getHeader("User-Agent");
String DimHtml=ConfContrib.SolveHtml(Agent);
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
String DT=FoldUser.getFolderType();
Vector<String> FieldsToAsk=ConfContrib.getFieldsToRead();
Record AttrDef = FoldUser.getRecSum();
StringBuilder Fields=new StringBuilder(3000);
Attribute Attr;
for (int i = 0; i < FieldsToAsk.size(); i++)
    {
    String F = FieldsToAsk.elementAt(i);
    Attr = AttrDef.getAttr(F).Copy();    
    if (Attr.getType()==Attribute.tTHES)
        Fields.append(GenThesVals(Req, LocalSess, DT, Attr)); 
    else if (Attr.getType()==Attribute.tBOOLEAN)
        Fields.append(GenBoolVals(Req, DT, Attr)); 
    else
        Fields.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"CONTRIBLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_CONTRIBINP\"><input class=\"CONTRIBINP\" type=\"text\" readonly name=\"").append(Attr.getName()).append("\" value=\"").append(Attr.Export()).append("\"></td></tr>\n");
    }
HtmlFinal=HtmlFinal.replace("@CONTRIBFIELDS@", Fields);
LastCacheUpdate=new Date();
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
private static Properties getContribProperties(String IdContrib) throws Exception
{
DriverGeneric sessOPD=ProdocFW.getSession("PD", ExtConf.getDefUser(), ExtConf.getDefPass());    
Properties P=new Properties();
PDDocs DocCSS=new PDDocs(sessOPD);
DocCSS.setPDId(IdContrib);
ByteArrayOutputStream OutBytes = new ByteArrayOutputStream();
DocCSS.getStream(OutBytes);
P.load(new StringReader(OutBytes.toString()));
ProdocFW.freeSesion("PD", sessOPD);
return P;
}
//-----------------------------------------------------------------------------------------------
private static StringBuilder GenBoolVals(HttpServletRequest Req, String DT, Attribute Attr)
{
StringBuilder SB=new StringBuilder(2000);
SB.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"CONTRIBLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_CONTRIBINP\"><select class=\"CONTRIBFORMATTHES\" name=\"").append(Attr.getName()).append("\">").append("<option value=\"\" selected></option><option value=\"1\">true</option></option><option value=\"0\">false</option>").append("</select><span class=\"tooltiptext\">").append(TT(Req,Attr.getDescription())).append("</span></td></tr>\n");
return(SB);
}
//-----------------------------------------------------------------------------------------------
private static StringBuilder GenThesVals(HttpServletRequest Req, DriverGeneric LocalSess, String DT, Attribute Attr) throws PDException
{
StringBuilder SB=new StringBuilder(2000);
StringBuilder Ops=new StringBuilder(2000);
CalcOps(Ops, String.valueOf(Attr.getLongStr()), LocalSess, 0);
SB.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"CONTRIBLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_CONTRIBINP\"><select class=\"CONTRIBFORMATTHES\" name=\"").append(Attr.getName()).append("\">").append(Ops).append("</select><span class=\"tooltiptext\">").append(TT(Req,Attr.getDescription())).append("</span></td></tr>\n");
return(SB);
}
//-----------------------------------------------------------------------------------------------
private static StringBuilder CalcOps(StringBuilder Ops, String TermId, DriverGeneric LocalSess, int Level) throws PDException
{
PDThesaur T=new PDThesaur(LocalSess);
T.Load(TermId);
StringBuilder SLev=new StringBuilder(100);
for (int i = 0; i < Level-1; i++)
    SLev.append("&nbsp;&nbsp;");
SLev.append("└ ");
//String SLev=ThesTree.substring(0, Level);
if (Level==0)
    Ops.append("<option value=\"\" selected> </option>");
else
    Ops.append("<option value=\"").append(T.getPDId()).append("\">").append(SLev).append(T.getName()).append("</option>");
HashSet listDirectDescendList = T.getListDirectDescendList(TermId);
for (Iterator iterator = listDirectDescendList.iterator(); iterator.hasNext();)
    {
    CalcOps(Ops,(String)iterator.next(), LocalSess, Level+1 );    
    }
return(Ops);
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
}
