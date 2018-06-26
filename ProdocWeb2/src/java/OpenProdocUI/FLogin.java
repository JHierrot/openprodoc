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
package OpenProdocUI;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Joaquin
 */
public class FLogin 
{
private String Html2=Html;    
private static final String Html="<!DOCTYPE html>" +
"<html>\n" +
        "<head>\n" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
        "<title>OpenProdoc2 Web</title>\n" +
        "<script src=\"js/OpenProdoc2.2.js\" type=\"text/javascript\"></script>\n" +
        "<script src=\"js/dhtmlx.js\" type=\"text/javascript\"/></script>\n" +
"        <link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\">" +       
        "<link rel=\"STYLESHEET\" type=\"text/css\" href=\"js/dhtmlx.css\">\n" +
"        <link rel=\"STYLESHEET\" type=\"text/css\" href=\"css/OpenProdoc.css\">\n" +
"        <style>\n" +
"        html, body {" +
"                width: 100%;" +
"                height: 100%;" +
"                margin: 0px;" +
"                padding: 0px;" +
"                overflow: hidden;" +
"           }\n" +
"	</style>\n" +
"        </head>\n" +
"    <body>\n" +
        "<form action=\"Login\"  method=\"post\">\n" +
"        <table align=\"center\"  width=\"300\" class=\"OPDForm\">\n" +
"        <tr><td>&nbsp</td></tr>\n" +
"        <tr><td><img src=\"img/Logo48.jpg\"><H3>OpenProdoc</H3></td></tr>\n" +
"        <tr><td>\n"+
        "  <fieldset class=\"dhxform_fs\"><legend class=\"fs_legend\">Login OpenProdoc:</legend><table>" +
        "  <tr><td width=\"100\"><div class=\"dhxform_label dhxform_label_align_left\" >User:</div></td>" +
        "  <td><div class=\"dhxform_control\"><input class=\"dhxform_textarea\" type=\"text\" name=\"User\"></div></td></tr>" +
        "  <tr><td><div class=\"dhxform_label dhxform_label_align_left\">Password:</div></td>" +
        "  <td><div class=\"dhxform_control\"><input class=\"dhxform_textarea\" type=\"password\" name=\"Password\"></div></td></tr>" +        
        "  <tr><td></td><td><input  class=\"dhxform_btn_filler\" type=\"submit\" value=\"  Ok  \"></td></tr>" +
        "  </table></fieldset>" +
"        </td></tr>" +
         "<tr><td>@Msg@</td></tr>"+  
"    </table>\n"+        
        "</form>\n" +
"    </body>\n" +
"</html>\n";

//-------------------------------------------------------------------
public FLogin(HttpSession Sess, String Msg)
{
if (Msg!=null && Msg.length()!=0)   
    Html2=Html.replace("@Msg@", Msg);
else
    Html2=Html.replace("@Msg@", "");
}
//-------------------------------------------------------------------
public String toHtml()
{
return(Html2);    
}
//-------------------------------------------------------------------
    
}
