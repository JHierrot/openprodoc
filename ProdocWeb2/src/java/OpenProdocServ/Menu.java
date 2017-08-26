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
public class Menu extends SParent
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
out.close();
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "Menu Servlet";
}
//-----------------------------------------------------------------------------------------------
private String genMenu(HttpServletRequest Req)
{
try {    
StringBuilder Men=new StringBuilder(2000);
PDRoles R=getSessOPD(Req).getUser().getRol();
Men.append("<?xml version=\"1.0\"?><menu><item id=\"Folders\" text=\"").append(TT(Req, "Folders")).append("\">");
if (R.isAllowCreateFolder())
   Men.append("<item id=\"AddFold\" text=\"").append(TT(Req, "Add")).append("\" img=\"img/add.png\" imgdis=\"img/add.png\" />");
if (R.isAllowMaintainFolder())
    {
    Men.append("<item id=\"DelFold\" text=\"").append(TT(Req, "Delete")).append("\" img=\"img/del.png\" imgdis=\"img/del.png\" />");
    Men.append("<item id=\"UpdFold\" text=\"").append(TT(Req, "Update")).append("\" img=\"img/edit.png\" imgdis=\"img/edit.png\" />");    
    }
Men.append("<item id=\"file_sep_01\" type=\"separator\"/>");
if (R.isAllowCreateFolder())
    Men.append("<item id=\"AddExtF\" text=\"").append(TT(Req, "Extended_Add")).append("\"/>");
if (R.isAllowMaintainFolder())
    Men.append("<item id=\"ModExtF\" text=\"").append(TT(Req, "Update_Extended")).append("\"/>");
Men.append("<item id=\"RefreshFold\" text=\"").append(TT(Req, "Refresh")).append("\"  img=\"img/refresh.png\" imgdis=\"img/refresh.png\" />");
Men.append("<item id=\"file_sep_02\" type=\"separator\"/>");
Men.append("<item id=\"SearchFold\" text=\"").append(TT(Req, "Search_Folders")).append("\" img=\"img/search.png\" imgdis=\"img/search.png\" />");
if (R.isAllowMaintainDoc())
    {
    Men.append("<item id=\"file_sep_02a\" type=\"separator\"/>");
    Men.append("<item id=\"ExportFold\" text=\"").append(TT(Req, "Export_Folders")).append("\"/>");
    Men.append("<item id=\"ImportFold\" text=\"").append(TT(Req, "Import_Folders")).append("\"/>");
    }
Men.append("<item id=\"file_sep_03\" type=\"separator\"/>");
Men.append("<item id=\"Exit\" text=\"").append(TT(Req, "Exit")).append("\" img=\"img/close.gif\" imgdis=\"img/close_dis.gif\"/></item>");
Men.append("<item id=\"Documentos\" text=\"").append(TT(Req, "Documents")).append("\">");
if (R.isAllowCreateDoc())
    Men.append("<item id=\"AddDoc\" text=\"").append(TT(Req, "Add")).append("\"  img=\"img/add.png\" imgdis=\"img/add.png\" />");
if (R.isAllowMaintainDoc())    
    Men.append("<item id=\"DelDoc\" text=\"").append(TT(Req, "Delete")).append("\" img=\"img/del.png\" imgdis=\"img/del.png\" />");  
Men.append("<item id=\"file_sep_04\" type=\"separator\"/>");
if (R.isAllowCreateDoc())
    Men.append("<item id=\"AddExtDoc\" text=\"").append(TT(Req, "Extended_Add")).append("\"/>");    
if (R.isAllowMaintainDoc())    
    Men.append("<item id=\"ModExtDoc\" text=\"").append(TT(Req, "Update_Extended")).append("\" img=\"img/edit.png\" imgdis=\"img/edit.png\" />");
Men.append("<item id=\"DocMetadata\" text=\"").append(TT(Req, "Document_Attributes")).append("\" img=\"img/data.png\" imgdis=\"img/data.png\" />");
Men.append("<item id=\"file_sep_05\" type=\"separator\"/>");
if (R.isAllowMaintainDoc())
    {
    Men.append("<item id=\"CheckOut\" text=\"").append(TT(Req, "CheckOut")).append("\" img=\"img/checkout.png\" imgdis=\"img/checkout.png\" />");
    Men.append("<item id=\"CheckIn\" text=\"").append(TT(Req, "CheckIn")).append("\" img=\"img/checkin.png\" imgdis=\"img/checkin.png\" />");
    Men.append("<item id=\"CancelCheckOut\" text=\"").append(TT(Req, "Cancel_CheckOut")).append("\" img=\"img/cancelcheckout.png\" imgdis=\"img/cancelcheckout.png\" />");    
    }
Men.append("<item id=\"ListVer\" text=\"").append(TT(Req, "List_of_Versions")).append("\"/>");
Men.append("<item id=\"file_sep_06\" type=\"separator\"/>");
Men.append("<item id=\"SearchDoc\" text=\"").append(TT(Req, "Search_Documents")).append("\" img=\"img/search.png\" imgdis=\"img/search.png\" />");
if (R.isAllowMaintainDoc())
    {
    Men.append("<item id=\"file_sep_07\" type=\"separator\"/>");
    Men.append("<item id=\"ImportRIS\" text=\"").append(TT(Req, "Import_RIS")).append("\"/>");
    Men.append("<item id=\"ExportDoc\" text=\"").append(TT(Req, "Export_Doc")).append("\"/>");
    Men.append("<item id=\"ImportDoc\" text=\"").append(TT(Req, "Import_Doc")).append("\"/>");
    }
Men.append("</item>");
Men.append("<item id=\"Other\" text=\"").append(TT(Req, "Other_Tasks")).append("\">");
if (R.isAllowCreateDoc() && R.isAllowMaintainDoc())
    Men.append("<item id=\"TrashBin\" text=\"").append(TT(Req, "Trash_bin")).append("\" />");
Men.append("<item id=\"PasswordChange\" text=\"").append(TT(Req, "Password_change")).append("\"/>");
if (R.isAllowCreateThesaur() || R.isAllowMaintainThesaur())
    Men.append("<item id=\"Thesaurus\" text=\"").append(TT(Req, "Thesaurus")).append("\"/>");
Men.append("</item>");
Men.append("<item id=\"Administration\" text=\"").append(TT(Req, "Administration")).append("\">");
if (R.isAllowCreateAcl() || R.isAllowMaintainAcl())
   Men.append("<item id=\"ACL\" text=\"").append(TT(Req, "ACL")).append("\"/>");
if (R.isAllowCreateGroup() || R.isAllowMaintainGroup())
   Men.append("<item id=\"Groups\" text=\"").append(TT(Req, "Groups")).append("\"/>");
if (R.isAllowCreateUser() || R.isAllowMaintainUser())
   Men.append("<item id=\"Users\" text=\"").append(TT(Req, "Users")).append("\"/>");
if (R.isAllowCreateRole()|| R.isAllowMaintainRole())
   Men.append("<item id=\"Roles\" text=\"").append(TT(Req, "Roles")).append("\"/>");
if (R.isAllowCreateMime()|| R.isAllowMaintainMime())
   Men.append("<item id=\"MimeTypes\" text=\"").append(TT(Req, "Mime_Types")).append("\"/>");
if (R.isAllowCreateRepos() || R.isAllowMaintainRepos())
   Men.append("<item id=\"Repositories\" text=\"").append(TT(Req, "Repositories")).append("\"/>");
if (R.isAllowCreateObject()|| R.isAllowMaintainObject() )
   Men.append("<item id=\"ObjDef\" text=\"").append(TT(Req, "Object_definitions")).append("\"/>");
if (R.isAllowCreateAuth()|| R.isAllowMaintainAuth() )
   Men.append("<item id=\"Authenticators\" text=\"").append(TT(Req, "Authenticators")).append("\"/>");
if (R.isAllowCreateCustom()|| R.isAllowMaintainCustom() )
   Men.append("<item id=\"Customizations\" text=\"").append(TT(Req, "Customizations")).append("\"/>");
if (R.isAllowCreateTask()|| R.isAllowMaintainTask() )
    {
    Men.append("<item id=\"TaskCron\" text=\"").append(TT(Req, "Task_Cron")).append("\"/>");
    Men.append("<item id=\"TaskEvents\" text=\"").append(TT(Req, "Task_Events")).append("\"/>");
    Men.append("<item id=\"PendTasklog\" text=\"").append(TT(Req, "Pending_Task_log")).append("\"/>"); 
    Men.append("<item id=\"EndTasksLogs\" text=\"").append(TT(Req, "Ended_Tasks_Logs")).append("\"/>");
    }
if (R.isAllowCreateObject()|| R.isAllowMaintainObject() )
    Men.append("<item id=\"TraceLogs\" text=\"").append(TT(Req, "Trace_Logs")).append("\"/>");
Men.append("</item>");
Men.append("<item id=\"Help\" text=\"").append(TT(Req, "Help")).append("\">");
Men.append("<item id=\"Contents\" text=\"").append(TT(Req, "Contents")).append("\" img=\"img/help.gif\"/>");
Men.append("<item id=\"About\" text=\"").append(TT(Req, "About")).append("\" img=\"img/about.gif\"/>");
Men.append("<item id=\"ReportingBugs\" text=\"").append(TT(Req, "Reporting_Bugs")).append("\" img=\"img/bug_reporting.gif\"/></item>");
Men.append("</menu>");
return(Men.toString());
} catch (PDException ex)
    {
    return ("<?xml version=\"1.0\"?><menu></menu>item id=\"Error\" text=\""+ex.getLocalizedMessage()+"\"></menu>");
    }
}
//-----------------------------------------------------------------------------------------------
}
