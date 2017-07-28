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

package OpenProdocUI;

import static OpenProdocUI.SParent.getProdocProperRef;
import static OpenProdocUI.SParent.setSessOPD;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ExtConf;
import prodoc.PDDocs;
import prodoc.PDObjDefs;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class OPAC extends SParent
{
private static String HtmlFinal=null;    
private static final String HtmlBase="<!DOCTYPE html>\n" +
"<html>" +
    "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
        "<title>OpenProdoc2 Web OPAC</title>" +
        "<script>"+
        "function ExecMenu(IdType)\n" +
        "{\n" +
        "switch (IdType)\n" +
          "{\n" +
          "@SWITCH@"+
           "}\n"+
        "}\n"+
        "</script>"+
         // "<script src=\"js/OpenProdoc.js\" type=\"text/javascript\"></script>" +
         // "<script src=\"js/dhtmlx.js\" type=\"text/javascript\"></script>" +
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>" +       
         // "<link rel=\"STYLESHEET\" type=\"text/css\" href=\"js/dhtmlx.css\"/>" +
        "@CSS@"+
    "</head>\n" +
    "<body class=\"OPACBODY\" onload=\"ExecMenu(\'@FIRSTTYPE@\');\" >\n" +
       "<form action=\"OPAC2\"  target=\"_blank\" method=\"post\" class=\"OPACFORM\">" +
       "<table align=\"center\"  class=\"OPACTABLE\">\n" +
        "<tr><td>&nbsp</td></tr>" +
        "<tr><td>@LOGO@<H3>OpenProdoc</H3></td></tr>" +
        "<tr><td>"+
        "<fieldset class=\"OPACFS\"><legend class=\"OPACLEG\">&nbsp;&nbsp;@TITLE@&nbsp;&nbsp;</legend>\n"+
         "<table>\n" +   
          "<tr><td><div class=\"OPACDT\" >@DTLABEL@</div></td><td><select class=\"OPACCOMB\" onChange=\"ExecMenu(this.options[this.selectedIndex].value)\">@DTVALS@</select></td></tr>\n" +
          "<tr><td><div class=\"OPACFTLAB\" >@FTLABEL@</div></td><td><input class=\"OPACFTINP\" type=\"text\" name=\"FT\"></td></tr>\n" +
          "@OPACFIELDS@"+
          "<tr><td></td><td><input  class=\"OPACBUT\" type=\"submit\" value=\"  Ok  \"></td></tr>" +
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
DriverGeneric sessOPD = getSessOPD(Req);
PrintWriter out = response.getWriter();  
try {
ProdocFW.InitProdoc("PD", getProdocProperRef());
if (ExtConf.isOpacActive())
    {
    DriverGeneric D=ProdocFW.getSession("PD", ExtConf.getUser(), ExtConf.getPass());
    setSessOPD(Req, D);
    out.println(GenHtml( D ));        
    }
else
    {
    AskLogin(Req, out);
    }
} catch (Exception Ex)
    {
    throw new ServletException(Ex.getLocalizedMessage());
    }
//response.setStatus(HttpServletResponse.SC_OK);
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "OPAC Servlet";
}
//-----------------------------------------------------------------------------------------------

static synchronized private String GenHtml(DriverGeneric sessOPD)
{
//if (HtmlFinal!=null)    
//    return(HtmlFinal);
if (ExtConf.getFormSearchCSS()!=null)
    HtmlFinal=HtmlBase.replace("@CSS@", "<link rel=\"STYLESHEET\" type=\"text/css\" href=\"SendDoc?Id="+ExtConf.getFormSearchCSS()+"\"/>");
else
    HtmlFinal=HtmlBase.replace("@CSS@", "");
if (ExtConf.getFormSearchLogo()!=null) // <img src=\"/SendDoc?Id="+ExtConf.getFormSearchLogo()+"\">"
    HtmlFinal=HtmlFinal.replace("@LOGO@", "<img src=\""+ExtConf.getFormSearchLogo()+"\">");
else
    HtmlFinal=HtmlFinal.replace("@LOGO@", "");
if (ExtConf.getTitle()!=null) // "OPAC  OpenProdoc"
    HtmlFinal=HtmlFinal.replace("@TITLE@", ExtConf.getTitle());
else
    HtmlFinal=HtmlFinal.replace("@TITLE@", "Title");
if (ExtConf.getDTLabel()!=null) // "Select DocType"
    HtmlFinal=HtmlFinal.replace("@DTLABEL@", ExtConf.getDTLabel());
else
    HtmlFinal=HtmlFinal.replace("@DTLABEL@", "DocTipes");
if (ExtConf.getDTLabel()!=null) // "Intro search words"
    HtmlFinal=HtmlFinal.replace("@FTLABEL@", ExtConf.getFTLabel());
else
    HtmlFinal=HtmlFinal.replace("@FTLABEL@", "Intro search words");
Vector<String> DocTipesList = ExtConf.getDocTipesList();
HtmlFinal=HtmlFinal.replace("@FIRSTTYPE@",DocTipesList.elementAt(0));
Vector<String> FieldsToInclude = ExtConf.getFieldsToInclude();
HashMap <String, Boolean> FieldsIncForm=new HashMap(FieldsToInclude.size());
for (int i = 0; i < FieldsToInclude.size(); i++)
    FieldsIncForm.put(FieldsToInclude.elementAt(i), false);
StringBuilder DTVals=new StringBuilder(1000);
StringBuilder Fields=new StringBuilder(3000);
StringBuilder SwitchDT=new StringBuilder(3000);
for (int NDT = 0; NDT < DocTipesList.size(); NDT++)
    {
    String DT = DocTipesList.elementAt(NDT);
    HashMap <String, Boolean> FieldsVisib=new HashMap(FieldsToInclude.size());
    for (int i = 0; i < FieldsToInclude.size(); i++)
        FieldsVisib.put(FieldsToInclude.elementAt(i), false);
    SwitchDT.append("case \"").append(DT).append("\":\n");
    try {
    PDObjDefs  Def=new PDObjDefs(sessOPD);
    Def.Load(DT);  
    PDDocs  Doc=new PDDocs(sessOPD, DT);
    Record AttrDef = Doc.getRecSum();
    AttrDef.initList();
    for (int NAT = 0; NAT < AttrDef.NumAttr(); NAT++)
        {
        Attribute Attr = AttrDef.nextAttr();  
        if (FieldsToInclude.contains(Attr.getName()) )
            {
            if (!FieldsIncForm.get(Attr.getName())) // to avoid duplicates in form
                {
                Fields.append("<tr id=\"").append(Attr.getName()).append("\"><td><div class=\"OPACLAB\" >").append(Attr.getUserName()).append("</div></td><td><input class=\"OPACINP\" type=\"text\" name=\"").append(Attr.getName()).append("\"></td></tr>\n");
                FieldsIncForm.put(Attr.getName(), true);
                }
            FieldsVisib.put(Attr.getName(), true); // to enable when changing doctype
            }
        }
    DTVals.append("<option value=\"").append(Def.getName()).append("\">").append(Def.getDescription()).append("</option>");
    for (Map.Entry<String, Boolean> FieldVis : FieldsVisib.entrySet())
        {
        SwitchDT.append("document.getElementById(\"").append(FieldVis.getKey()).append("\").style.visibility=\"").append(FieldVis.getValue()?"visible":"collapse").append("\";\n");
        }
    } catch (Exception Ex)
        {        
        }
    SwitchDT.append("break;\n");
    }
HtmlFinal=HtmlFinal.replace("@DTVALS@", DTVals);
HtmlFinal=HtmlFinal.replace("@OPACFIELDS@", Fields);
HtmlFinal=HtmlFinal.replace("@SWITCH@", SwitchDT);
return(HtmlFinal);
}
//-----------------------------------------------------------------------------------------------
}
