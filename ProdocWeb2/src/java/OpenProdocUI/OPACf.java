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
 * author: Joaquin Hierro      2017
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
import prodoc.DriverGeneric;
import prodoc.ExtConf;
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
public class OPACf extends SParent
{
private static final HashMap<String,String> OPACs=new HashMap(); 
private static final HashMap<String,ExtConf> Confs=new HashMap(); 
private static Date LastCacheUpdate=null;
private static final long CacheCaducity=1*1*1000;
private static final String HtmlBase="<!DOCTYPE html>\n" +
"<html>" +
    "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
        "<title>OpenProdoc2 Web OPAC Fold</title>\n" +
        "<script src=\"js/OPDCombo2.3.js\" type=\"text/javascript\"></script>\n" +
        "<script>\n"+
        "@LISTFIELDS@\n"+
        "function ExecMenu(IdType)\n" +
        "{\n" +
"var L=IdType.length;\n"+        
"for (i = 0; i < ListF.length; i++)\n"+ 
    "{\n"+
    "if ((ListF[i].substring(0,L)+\"_\")!=(IdType+\"_\"))\n"+
        "document.getElementById(ListF[i]).style.visibility=\"collapse\";\n"+
    "else\n"+    
        "document.getElementById(ListF[i]).style.visibility=\"visible\";\n"+
    "}\n"+
        "}\n"+
        "</script>\n"+
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>\n" +       
        "@CSS@"+
    "</head>\n" +
    "<body class=\"OPACBODY\" onload=\"ExecMenu(\'@FIRSTTYPE@\');\" >\n" +
       "<form action=\"OPACf2\"  target=\"_blank\" method=\"post\" class=\"OPACFORM\">" +
       "<table align=\"center\"  class=\"OPACTABLE\">\n" +
        "<tr><td>&nbsp</td></tr>" +
        "<tr><td>@LOGO@<H3>OpenProdoc</H3></td></tr>" +
        "<tr><td>"+
        "<fieldset class=\"OPACFS\"><legend class=\"OPACLEG\">&nbsp;&nbsp;@TITLE@&nbsp;&nbsp;</legend>\n"+
         "<table>\n" +   
          "<tr><td><div class=\"OPACDT\" >@DTLABEL@</div></td><td  class=\"TD_OPACCOMB\"><select class=\"OPACCOMB\" name=\"DT\" onChange=\"ExecMenu(this.options[this.selectedIndex].value)\">@DTVALS@</select><span class=\"tooltiptext\">@HelpForDocType@</span></td></tr>\n" +
          "@OPACFIELDS@"+
          "<tr><td><div class=\"OPACFORMATLAB\" >@FormatLabel@</div></td><td class=\"TD_PACFORMATCOMB\"><select class=\"OPACFORMATCOMB\" name=\"FORMAT_REP\">@FORMATVALS@</select><span class=\"tooltiptext\">@HelpForFormatType@</span></td></tr>\n" +
          "<tr><td><a class=\"OPACHELP\" href=\"@URLHELP@\" target=\"_blank\">?</a></td><td><input  class=\"OPACBUT\" type=\"submit\" value=\"  Ok  \"></td></tr>" +
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
try {
if (!OPDFWLoaded)  
    {
    ProdocFW.InitProdoc("PD", getProdocProperRef());
    OPDFWLoaded=true;
    }
String IdOPAC=Req.getParameter("Id"); 
if (IdOPAC==null)
    throw new Exception("Inexistent OPAC");
if (IsCacheExpired()) 
    CleanCache();
ExtConf ConfOPAC=Confs.get(IdOPAC);
if (ConfOPAC==null)
    {
    ConfOPAC=new ExtConf();
    ConfOPAC.AssignConf(getOPACProperties(IdOPAC));
    Confs.put(IdOPAC, ConfOPAC);
    }
setOPACConf(Req, ConfOPAC);
DriverGeneric LocalSess=getSessOPD(Req);
if (LocalSess==null)
    {
    LocalSess=ProdocFW.getSession(getConnector(), ConfOPAC.getUser(), ConfOPAC.getPass()); // just for translation   
    setSessOPD(Req, LocalSess);
    }
if (OPACs.containsKey(IdOPAC))
    out.println(OPACs.get(IdOPAC));   
else
    out.println(GenHtml(Req, ConfOPAC, LocalSess, IdOPAC)); 
} catch (Exception Ex)
    {
    throw new ServletException(Ex.getLocalizedMessage());
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
return "OPACf Servlet";
}
//-----------------------------------------------------------------------------------------------
static synchronized private boolean IsCacheExpired()
{
if (LastCacheUpdate==null || (new Date().getTime()-LastCacheUpdate.getTime())>CacheCaducity) 
    return(true);
else
    return(false);
}
//-----------------------------------------------------------------------------------------------
static synchronized private String GenHtml(HttpServletRequest Req, ExtConf ConfOPAC, DriverGeneric LocalSess, String IdOPAC) throws Exception
{
String HtmlFinal;   
String Agent=Req.getHeader("User-Agent");
String DimHtml=ConfOPAC.SolveHtml(Agent);
if (DimHtml!=null) 
    {
    HtmlFinal=getHtml(LocalSess, DimHtml);
    }
else
    HtmlFinal=HtmlBase;
if (ConfOPAC.getFormSearchCSS()!=null)
    {
    if (ConfOPAC.getFormSearchCSS().startsWith("http"))    
       HtmlFinal=HtmlFinal.replace("@CSS@", "<link rel=\"STYLESHEET\" type=\"text/css\" href=\""+ConfOPAC.getFormSearchCSS()+"\"/>");
    else
       HtmlFinal=HtmlFinal.replace("@CSS@", GenCSS(LocalSess, ConfOPAC.getFormSearchCSS()));
    }
else
    HtmlFinal=HtmlFinal.replace("@CSS@", "");
if (ConfOPAC.getFormSearchLogo()!=null) // <img src=\"/SendDoc?Id="+ExtConf.getFormSearchLogo()+"\">"
    HtmlFinal=HtmlFinal.replace("@LOGO@", "<img src=\""+ConfOPAC.getFormSearchLogo()+"\">");
else
    HtmlFinal=HtmlFinal.replace("@LOGO@", "");
if (ConfOPAC.getTitle()!=null) // "OPAC  OpenProdoc"
    HtmlFinal=HtmlFinal.replace("@TITLE@", ConfOPAC.getTitle());
else
    HtmlFinal=HtmlFinal.replace("@TITLE@", "Title");
if (ConfOPAC.getDTLabel()!=null) // "Select DocType"
    HtmlFinal=HtmlFinal.replace("@DTLABEL@", ConfOPAC.getDTLabel());
else
    HtmlFinal=HtmlFinal.replace("@DTLABEL@", "DocTipes");
if (ConfOPAC.getFormatLabel()!=null) // "Output Format"
    HtmlFinal=HtmlFinal.replace("@FormatLabel@", ConfOPAC.getFormatLabel());
else
    HtmlFinal=HtmlFinal.replace("@FormatLabel@", "Output Format");
if (ConfOPAC.getHelpForDocType()!=null) 
    HtmlFinal=HtmlFinal.replace("@HelpForDocType@", ConfOPAC.getHelpForDocType());
else
    HtmlFinal=HtmlFinal.replace("@HelpForDocType@", "");
if (ConfOPAC.getHelpForFormatType()!=null) 
    HtmlFinal=HtmlFinal.replace("@HelpForFormatType@", ConfOPAC.getHelpForFormatType());
else
    HtmlFinal=HtmlFinal.replace("@HelpForFormatType@", "");
if (ConfOPAC.getUrlHelp()!=null) 
    HtmlFinal=HtmlFinal.replace("@URLHELP@", ConfOPAC.getUrlHelp());
else
    HtmlFinal=HtmlFinal.replace("@URLHELP@", "");
Vector<String> DocTipesList = ConfOPAC.getDocTipesList();
HtmlFinal=HtmlFinal.replace("@FIRSTTYPE@",DocTipesList.elementAt(0));
Vector<String> FieldsToInclude = ConfOPAC.getFieldsToInclude();
Vector<String> FormatsToInclude = ConfOPAC.getResultForm();
HashMap <String, Boolean> FieldsIncForm=new HashMap(FieldsToInclude.size());
for (int i = 0; i < FieldsToInclude.size(); i++)
    FieldsIncForm.put(FieldsToInclude.elementAt(i), false);
StringBuilder DTVals=new StringBuilder(1000);
StringBuilder FormatVals=new StringBuilder(1000);
for (int NFT = 0; NFT < FormatsToInclude.size(); NFT++)
    {
    String FT = FormatsToInclude.elementAt(NFT); 
    try {
    PDReport  Rep=new PDReport(LocalSess);
    Rep.Load(FT);  
    FormatVals.append("<option value=\"").append(Rep.getPDId()).append("\">").append(Rep.getTitle()).append("</option>");
    } catch (Exception Ex)
        {        
        }
    }
HtmlFinal=HtmlFinal.replace("@FORMATVALS@", FormatVals);
StringBuilder Fields=new StringBuilder(3000);
HashSet <String> FieldsVisib=new HashSet(FieldsToInclude.size()*DocTipesList.size());
StringBuilder FieldsArray=new StringBuilder("var ListF=[");
for (int NDT = 0; NDT < DocTipesList.size(); NDT++)
    {
    String DT = DocTipesList.elementAt(NDT);
    try {
    PDObjDefs  Def=new PDObjDefs(LocalSess);
    Def.Load(DT);  
    PDFolders  Fold=new PDFolders(LocalSess, DT);
    Record AttrDef = Fold.getRecSum();
    AttrDef.initList();
    for (int NAT = 0; NAT < AttrDef.NumAttr(); NAT++)
        {
        Attribute Attr = AttrDef.nextAttr();  
        if (FieldsToInclude.contains(Attr.getName()) )
            {
            if (Attr.getType()==Attribute.tTHES)
                Fields.append(GenThesVals(Req, LocalSess, DT, Attr)); 
            else if (Attr.getType()==Attribute.tBOOLEAN)
                Fields.append(GenBoolVals(Req, DT, Attr)); 
            else
                Fields.append("<tr id=\"").append(DT+"_"+Attr.getName()).append("\"><td><div class=\"OPACLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_OPACINP\"><input class=\"OPACINP\" type=\"text\" name=\"").append(DT+"_"+Attr.getName()).append("\"><span class=\"tooltiptext\">").append(TT(Req,Attr.getDescription())).append("</span></td></tr>\n");
            FieldsIncForm.put(DT+"_"+Attr.getName(), true);
            FieldsVisib.add(DT+"_"+Attr.getName()); // to enable when changing doctype
            }
        }
    DTVals.append("<option value=\"").append(Def.getName()).append("\">").append(Def.getDescription()).append("</option>");
    } catch (Exception Ex)
        {        
        }
    }
//for (String NextField : FieldsVisib)
for (Iterator<String> iterator = FieldsVisib.iterator(); iterator.hasNext();)
    {
    String NextField = iterator.next();
    FieldsArray.append("\"").append(NextField).append("\"");
    if (iterator.hasNext())
        FieldsArray.append(",");
    }
FieldsArray.append("];");
HtmlFinal=HtmlFinal.replace("@DTVALS@", DTVals);
HtmlFinal=HtmlFinal.replace("@OPACFIELDS@", Fields);
HtmlFinal=HtmlFinal.replace("@LISTFIELDS@", FieldsArray);
//OPACs.put(IdOPAC, HtmlFinal);
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
private static void CleanCache()
{
OPACs.clear();
Confs.clear();
}
//-----------------------------------------------------------------------------------------------
private static Properties getOPACProperties(String IdOPAC) throws Exception
{
DriverGeneric sessOPD=ProdocFW.getSession("PD", ExtConf.getDefUser(), ExtConf.getDefPass());    
Properties P=new Properties();
PDDocs DocCSS=new PDDocs(sessOPD);
DocCSS.setPDId(IdOPAC);
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
SB.append("<tr id=\"").append(DT+"_"+Attr.getName()).append("\"><td><div class=\"OPACLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_OPACINP\"><select class=\"OPACFORMATTHES\" name=\"").append(DT+"_"+Attr.getName()).append("\">").append("<option value=\"\" selected></option><option value=\"1\">true</option></option><option value=\"0\">false</option>").append("</select><span class=\"tooltiptext\">").append(TT(Req,Attr.getDescription())).append("</span></td></tr>\n");
return(SB);
}
//-----------------------------------------------------------------------------------------------
private static StringBuilder GenThesVals(HttpServletRequest Req, DriverGeneric LocalSess, String DT, Attribute Attr) throws PDException
{
StringBuilder SB=new StringBuilder(2000);
StringBuilder Ops=new StringBuilder(2000);
CalcOps(Ops, String.valueOf(Attr.getLongStr()), LocalSess, 0);
SB.append("<tr id=\"").append(DT+"_"+Attr.getName()).append("\"><td><div class=\"OPACLAB\" >").append(TT(Req, Attr.getUserName())).append("</div></td><td class=\"TD_OPACINP\"><select class=\"OPACFORMATTHES\" name=\"").append(DT+"_"+Attr.getName()).append("\"  onkeyup=\"Search(event)\" onblur=\"Clean(event)\">").append(Ops).append("</select><span class=\"tooltiptext\">").append(TT(Req,Attr.getDescription())).append("</span></td></tr>\n");
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

}
