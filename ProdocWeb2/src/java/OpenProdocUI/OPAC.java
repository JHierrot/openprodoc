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
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ExtConf;
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
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
        "<title>OpenProdoc2 Web OPAC</title>" +
         // "<script src=\"js/OpenProdoc.js\" type=\"text/javascript\"></script>" +
         // "<script src=\"js/dhtmlx.js\" type=\"text/javascript\"></script>" +
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>" +       
         // "<link rel=\"STYLESHEET\" type=\"text/css\" href=\"js/dhtmlx.css\"/>" +
        "@CSS@"+
    "</head>" +
    "<body>" +
       "<form action=\"OPAC2\"  target=\"_blank\" method=\"post\" class=\"OPACFORM\">" +
"        <table align=\"center\"  width=\"300\" class=\"OPACTABLE\">" +
"        <tr><td>&nbsp</td></tr>" +
"        <tr><td>@LOGO@<H3>OpenProdoc</H3></td></tr>\n" +
"        <tr><td>\n"+
        "  <fieldset class=\"OPACFS\"><legend class=\"OPACLEG\">@TITLE@</legend><table>" +
        
        "  <tr><td width=\"100\"><div class=\"OPACDT\" >@DTLABEL@</div></td>" +
        "  <td><select class=\"OPACCOMB\">@DTVALS@</select></td></tr>" +
        
        "  <tr><td width=\"100\"><div class=\"OPACLAB\" >User:</div></td>" +
        "  <td><input class=\"OPACINP\" type=\"text\" name=\"User\"></td></tr>" +

        "  <tr><td></td><td><input  class=\"OPACBUT\" type=\"submit\" value=\"  Ok  \"></td></tr>" +
        "  </table></fieldset>" +
"        </td></tr>" +
"    </table>\n"+        
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
if (sessOPD==null)
    {
    if (ExtConf.isOpacActive())
        {
        ProdocFW.InitProdoc("PD", getProdocProperRef());
        DriverGeneric D=ProdocFW.getSession("PD", ExtConf.getUser(), ExtConf.getPass());
        setSessOPD(Req, D);
        out.println(GenHtml( sessOPD ));        
        }
    else
        {
        AskLogin(Req, out);
        }
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
if (HtmlFinal!=null)    
    return(HtmlFinal);
if (ExtConf.getFormSearchCSS()!=null)
    HtmlFinal=HtmlBase.replace("@CSS@", "<link rel=\"STYLESHEET\" type=\"text/css\" href=\"/SendDoc?Id="+ExtConf.getFormSearchCSS()+"\"/>");
else
    HtmlFinal=HtmlBase.replace("@CSS@", "");
if (ExtConf.getFormSearchLogo()!=null) // <img src=\"/SendDoc?Id="+ExtConf.getFormSearchLogo()+"\">"
    HtmlFinal=HtmlBase.replace("@LOGO@", "<img "+ExtConf.getFormSearchLogo()+">");
else
    HtmlFinal=HtmlBase.replace("@LOGO@", "");
if (ExtConf.getTitle()!=null) // "OPAC  OpenProdoc"
    HtmlFinal=HtmlBase.replace("@TITLE@", ExtConf.getTitle());
else
    HtmlFinal=HtmlBase.replace("@TITLE@", "");
if (ExtConf.getDTLabel()!=null) // "Select DocType"
    HtmlFinal=HtmlBase.replace("@DTLABEL@", ExtConf.getDTLabel());
else
    HtmlFinal=HtmlBase.replace("@DTLABEL@", "");
Vector<String> DocTipesList = ExtConf.getDocTipesList();
Vector<String> FieldsToInclude = ExtConf.getFieldsToInclude();
StringBuilder DTVals=new StringBuilder(1000);
for (int NDT = 0; NDT < DocTipesList.size(); NDT++)
    {
    String DT = DocTipesList.elementAt(NDT);
    PDObjDefs  Def=new PDObjDefs(sessOPD);
    try {
    Def.Load(DT);
    Record AttrDef = Def.GetAttrDef();
    AttrDef.initList();
    for (int NAT = 0; NAT < AttrDef.NumAttr(); NAT++)
        {
        Attribute Attr = AttrDef.nextAttr();  
        if (FieldsToInclude.contains(Attr.getName()))
            {
            
            }
        }
    } catch (Exception Ex)
        {        
        }
    DTVals.append("<option value=\"").append(Def.getName()).append("\">").append(Def.getDescription()).append("</option>");
    }
HtmlFinal=HtmlBase.replace("@DTVALS@", DTVals);
return(HtmlFinal);
}
//-----------------------------------------------------------------------------------------------
}
