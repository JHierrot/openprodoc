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

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.TT;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.PDException;
import prodoc.PDRoles;

/**
 *
 * @author jhierrot
 */
public class MenuThes extends SParent
{

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
/**
 *
 * @param Req
     * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws IOException
{
response.setContentType("text/xml;charset=UTF-8");
PrintWriter out = response.getWriter();
out.println(genMenu(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "MenuThes Servlet";
}
//-----------------------------------------------------------------------------------------------
private String genMenu(HttpServletRequest Req)
{
try {    
StringBuilder Men=new StringBuilder(2000);
PDRoles R=getSessOPD(Req).getUser().getRol();
Men.append("<?xml version=\"1.0\"?><menu><item id=\"Thesaurus\" text=\"").append(TT(Req, "Thesaurus")).append("\">");
if (R.isAllowCreateThesaur())
   Men.append("<item id=\"CreateTheusurus\" text=\"").append(TT(Req, "Create_Theusurus")).append("\" img=\"img/new.gif\" imgdis=\"img/new_dis.gif\"/>");
if (R.isAllowMaintainThesaur())
    {
    Men.append("<item id=\"UpdateThesaurus\" text=\"").append(TT(Req, "Update_Thesaurus")).append("\"/>");
    Men.append("<item id=\"DeleteThesaurus\" text=\"").append(TT(Req, "Delete_Thesaurus")).append("\"/>");    
    Men.append("<item id=\"file_sep_01\" type=\"separator\"/>");
    Men.append("<item id=\"RefreshThesaurus\" text=\"").append(TT(Req, "Refresh")).append("\"/>");    
    Men.append("<item id=\"ExportThesaurus\" text=\"").append(TT(Req, "Export_Thesaurus")).append("\"/>");    
    Men.append("<item id=\"ImportThesaurus\" text=\"").append(TT(Req, "Import_Thesaurus")).append("\"/>");    
    }
Men.append("<item id=\"file_sep_02\" type=\"separator\"/>");
Men.append("<item id=\"CloseWindow\" text=\"").append(TT(Req, "Close_Window")).append("\"/>");
Men.append("</item><item id=\"Terms\" text=\"").append(TT(Req, "Terms")).append("\">");
if (R.isAllowCreateThesaur())
    Men.append("<item id=\"AddTerm\" text=\"").append(TT(Req, "Add_Term")).append("\" img=\"img/new.gif\" imgdis=\"img/new_dis.gif\"/>");
if (R.isAllowMaintainThesaur())    
    {
    Men.append("<item id=\"UpdateTerm\" text=\"").append(TT(Req, "Update_Term")).append("\" />");  
    Men.append("<item id=\"DeleteTerm\" text=\"").append(TT(Req, "Delete_Term")).append("\" />");  
    }
Men.append("<item id=\"file_sep_04\" type=\"separator\"/>");
Men.append("<item id=\"SearchTerms\" text=\"").append(TT(Req, "Search_Terms")).append("\"/>");    
Men.append("</item><item id=\"Help\" text=\"").append(TT(Req, "Help")).append("\">");
Men.append("<item id=\"ThesaurusMainWin\" text=\"").append(TT(Req, "Thesaurus_MainWin")).append("\" img=\"img/help.gif\"/>");
Men.append("</item></menu>");
return(Men.toString());
} catch (PDException ex)
    {
    return ("<?xml version=\"1.0\"?><menu></menu>item id=\"Error\" text=\""+ex.getLocalizedMessage()+"\"></menu>");
    }
}
//-----------------------------------------------------------------------------------------------
}
