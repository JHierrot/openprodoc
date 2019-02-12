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
import java.util.Properties;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.ContribConf;
import prodoc.DriverGeneric;
import prodoc.ExtConf;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ContribLogin extends SParent
{
private static final HashMap<String,String> ContribLogins=new HashMap(); 
private static final HashMap<String,ContribConf> Confs=new HashMap(); 
private static Date LastCacheUpdate=null;
private static final long CacheCaducity=1*1*60000;
private static final String HtmlBase="<!DOCTYPE html>\n" +
"<html>" +
    "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
        "<title>OpenProdoc2 Web Contrib Login</title>\n" +
        "<script src=\"js/OPDCombo2.3.js\" type=\"text/javascript\"></script>\n" +
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>\n" +       
        "@CSS@"+
    "</head>\n" +
    "<body class=\"CONTRIBBODY\" >\n" +
       "<form action=\"ContribList\" method=\"post\" class=\"CONTRIBFORM\"  accept-charset=\"UTF-8\">" +
       "<table align=\"center\"  class=\"CONTRIBTABLE\">\n" +
        "<tr><td>&nbsp</td></tr>" +
        "<tr><td>@LOGO@<H3>OpenProdoc</H3></td></tr>" +
        "<tr><td>"+
        "<fieldset class=\"CONTRIBFS\"><legend class=\"CONTRIBLEG\">&nbsp;&nbsp;@TITLE@&nbsp;&nbsp;</legend>\n"+
         "<table>\n" +   
          "@CONTRIBFIELDS@"+
          "<tr><td><a class=\"CONTRIBHELP\" href=\"@URLHELP@\" target=\"_blank\">?</a></td><td><input  class=\"CONTRIBBUT\" type=\"submit\" value=\"  Ok  \"></td></tr>" +
          "</table>\n" +
         "</fieldset>" +
        "</td></tr>" +
       "</table>\n"+      
      "<input type=\"hidden\" name=\"Id\" value=\"@CONTRIB_ID@\">"+  
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
ShowLoginContrib(Req, response);    
}
//----------------------------------------------------------------------------
static void ShowLoginContrib(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
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
String IdContrib=Req.getParameter("Id"); 
if (IdContrib==null)
    throw new Exception("Inexistent Contrib Form");
if (IsCacheExpired()) 
    CleanCache();
ContribConf ConfContr=Confs.get(IdContrib);
if (ConfContr==null)
    {
    ConfContr=new ContribConf(IdContrib);
    ConfContr.AssignConf(getContribProperties(IdContrib));
    Confs.put(IdContrib, ConfContr);
    }
setContribConf(Req, ConfContr);
setContribFolder(Req, null);    
LocalSess=ProdocFW.getSession(getConnector(), ConfContr.getUser(), ConfContr.getPass());   
if (ContribLogins.containsKey(IdContrib))
    out.println(ContribLogins.get(IdContrib));   
else
    out.println(GenHtml(Req, ConfContr, LocalSess)); 
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
static synchronized private boolean IsCacheExpired()
{
if (LastCacheUpdate==null || (new Date().getTime()-LastCacheUpdate.getTime())>CacheCaducity) 
    return(true);
else
    return(false);
}
//-----------------------------------------------------------------------------------------------
static synchronized private String GenHtml(HttpServletRequest Req, ContribConf ConfContrib, DriverGeneric LocalSess) throws Exception
{
String HtmlFinal;   
String Agent=Req.getHeader("User-Agent");
String DimHtml=ConfContrib.SolveHtmlLog(Agent);
Boolean is1Col=ConfContrib.Is1ColHtmlLog(Agent);
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
if (ConfContrib.getTitle()!=null)
    HtmlFinal=HtmlFinal.replace("@TITLE@", ConfContrib.getTitle());
else
    HtmlFinal=HtmlFinal.replace("@TITLE@", "Title");
if (ConfContrib.getUrlHelp()!=null) 
    HtmlFinal=HtmlFinal.replace("@URLHELP@", ConfContrib.getUrlHelp());
else
    HtmlFinal=HtmlFinal.replace("@URLHELP@", "");
HtmlFinal=HtmlFinal.replace("@CONTRIB_ID@", ConfContrib.getId());
Vector<String> FieldsLogin = ConfContrib.getLoginFields();
Vector<String> FieldsToAsk;
if (ConfContrib.isOpenContrib())
    FieldsToAsk=ConfContrib.getFieldsToRead();
else
    FieldsToAsk=ConfContrib.getLoginFields();
String DT =ConfContrib.getLoginFolderType();
PDFolders  Fold=new PDFolders(LocalSess, DT);
Record AttrDef = Fold.getRecSum();
StringBuilder Fields=new StringBuilder(3000);
Attribute Attr;
for (int i = 0; i < FieldsToAsk.size(); i++)
    {
    String F = FieldsToAsk.elementAt(i);
    Attr = AttrDef.getAttr(F).Copy();    
    switch (Attr.getType())
        {
        case Attribute.tTHES:
            Fields.append(GenThesVals(Req, LocalSess, Attr, is1Col));
            break;
        case Attribute.tBOOLEAN:
            Fields.append(GenBoolVals(Req, Attr, is1Col, false));
            break;
        default:
            if (Attr.getType()==Attribute.tSTRING &&Attr.getLongStr()>256)
                Fields.append(GenArea(Req, Attr, is1Col));
            else    
                Fields.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"").append(FieldsLogin.contains(Attr.getName())?"CONTRIBLAB_LOGIN":"CONTRIBLAB").append("\" >").append(TT(Req, Attr.getUserName())).append("</div></td>").append(is1Col?"</tr><tr>":"").append("<td class=\"TD_CONTRIBINP\"><input class=\"CONTRIBINP\" type=\"text\" name=\"").append(Attr.getName()).append("\"><span class=\"tooltiptext\">").append(TT(Req,Attr.getDescription())).append("</span></td></tr>\n");
            break;
        }
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
private static void CleanCache()
{
ContribLogins.clear();
Confs.clear();
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
ProdocFW.freeSesion(getConnector(), sessOPD);
return P;
}
//-----------------------------------------------------------------------------------------------
}

//#======= Document configuration =============================================
//# Fields used for "login"/verifiation of identity
//LoginFields=Correo|Telef
//# Fields of the Foolder type to ask to be filled
//FieldsToInclude=Nombre|Apellidos|Correo|Telef|Idioma
//# Path of folder where folders will be created
//BaseFolder=/Donaciones
//# Document types allowed to be uploded
//DocTipesList=PD_DOCS|Manual|Picture|Grabaciones
//# Non included doc types show ALL fields
//#Fields_PD_DOCS=
//Fields_Manual=Title|DocDate
//Fields_Picture=Title|Author|Keywords|DocDate
//#Fields_Grabaciones=
//#======= Security ==========================================================
//# Open (1) or closed (0)system. When closed, Folder MUST be created and login information transmited to external user.
//OpenContrib=1           
//# Folder type to use
//LoginFolderType=Donaciones
//# UserName and Password of the user that will  do the actual insert in openprodoc of Folders and docs. 
//# It is recmmended to be a user with a limiteed rol (only insert of folders and docs) and permissions only in the Contribution folder
//User=root
//Pass=root               
//# Allowed extensions to upload
//AllowedExt=doc|docx|xls|xlsx|ppt|pptx|txt|pdf|jpg|jpeg|tiff|tif|png|gif|odt
//# MaxSize upload (bytes)
//MaxSize=20000000                         
//#======= Interface =========================================================
//# Openprodoc identifier of CSS or http url of CSS
//ContribCSS=16697ec3694-3fe7288b86493159
//# url of logo. Can be a "local" url using the format /SendDoc?Id=Identifier of doc
//ContribLogo=img/LogoProdoc.jpg
//# Title to be show in login
//Title=Aportaciones
//# Title to be show in content of folder
//TitleList=Archivo personal
//# Id of Report used for showing docs infolder
//DocsReportId=16654ff6af1-3f9b78099c0147a0
//# Url of help
//UrlHelp=
//#======= Alternative htmls ==================================================
//# Alternative htmls depending on agent
//#---------------------------------------------
//# Num alternatives for login
//NumHtmlContLog=0
//# Agents for login
//ListAgentLog0=
//# html for each agent of login
//HtmlAgentLog0=
//#---------------------------------------------
//# Num alternatives for Lista of docs
//NumHtmlContList=0
//# Agents for List
//ListAgentList0=
//# html for each agent of List
//HtmlAgentList0=
//#---------------------------------------------
//# Num alternatives for adding docs
//NumHtmlContAdd=0
//# Agents for adding docs
//ListAgentAdd0=
//# html for each agent of adding docs
//HtmlAgentAdd0=
//#---------------------------------------------
//# Num alternatives for Results adding docs
//NumHtmlContRes=0
//# Agents for Results adding docs
//ListAgentRes0=
//# html for each agent of Results adding docs
//HtmlAgentRes0=
//#====================================================


///* OpenProdoc CONTRIB default CSS */
//.CONTRIBBODY
//{
//background-color: #DFDFDF;
//}
///* Style for whole body */
//.CONTRIBFORM
//{  
//}
///* Style for table containing fields */
//.CONTRIBTABLE
//{
//}
///* Style for FieldSet */
//.CONTRIBFS
//{
//}
///* Style for Legend */
//.CONTRIBLEG
//{
//font-family: Tahoma,Helvetica;
//font-size: 20px; 
//font-weight: bold;
//}
///* Style for Document Type Combo */
//.CONTRIBDOCTYPESCOMB
//{
//background-color: #A6A6A6;
//}
//.CONTRIBDOCTYPESCOMB:hover .tooltiptext 
//{
//visibility: visible;
//}
//
///* Style for Fields Label */
//.CONTRIBLAB
//{
//font-family: Tahoma,Helvetica;
//font-size: 12px; 
//border-bottom-color: #FFFFFF;
//}
///* Style for Fields Label */
//.CONTRIBLAB_LOGIN
//{
//font-family: Tahoma,Helvetica;
//font-size: 15px; 
//border-bottom-color: #FFFFFF;
//}
//.CONTRIBLABELFILEUP
//{
//font-family: Tahoma,Helvetica;
//font-size: 15px; 
//border-bottom-color: #FFFFFF;
//}
//.CONTRIBRESOK
//{
//font-family: Tahoma,Helvetica;
//font-size: 15px; 
//border-bottom-color: #FFFFFF;
//}
//.CONTRIBRESKO
//{
//font-family: Tahoma,Helvetica;
//font-size: 15px; 
//border-bottom-color: #FF0000;
//}
//
///* Style for Fields Input */
//.CONTRIBINP
//{
//width: 200px;
//}
//.TD_CONTRIBINP:hover .tooltiptext 
//{
//    visibility: visible;
//}
///* format for thesaurus combos **/
//.CONTRIBFORMATTHES
//{
//font-family: Tahoma,Helvetica;
//}
//
///* Style for Fields Input */
//.CONTRIBBUT
//{
//height: 40px;
//width: 70px;
//border-style: outset;
//border-width: 4px;
//font-size: 14px; 
//font-style: italic;
//font-weight: bold;
//border-radius: 8px;
//}
//.CONTRIBHELP
//{
//font-family: Tahoma,Helvetica;
//font-size: 16px; 
//font-style: bold;
//border-style: outset;
//border-width: 4px;
//border-radius: 8px;
//padding: 5px;
//background-color: #DFDFDF;
//}
//.CONTRIBLOG
//{
//font-family: Tahoma,Helvetica;
//font-size: 16px; 
//font-style: bold;
//border-style: outset;
//border-width: 4px;
//border-radius: 8px;
//padding: 5px;
//background-color: #DFDFDF;
//}
//.CONTRIBLISTURL
//{
//font-family: Tahoma,Helvetica;
//font-size: 16px; 
//font-style: bold;
//border-style: outset;
//border-width: 4px;
//border-radius: 8px;
//padding: 5px;
//background-color: #DFDFDF;
//}
//
//img
//{
//height: 83px;
//width: 80px;
//}
//.tooltiptext 
//{
//visibility: hidden;
//font-family: Tahoma,Helvetica;
//font-size: 12px; 
//width: 200px;
//background-color: grey;
//color: #fff;
//padding: 8px;
//border-radius: 6px;
//position: absolute;
//z-index: 1;
//}
