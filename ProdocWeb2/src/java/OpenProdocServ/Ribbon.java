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

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDRoles;

/**
 *
 * @author jhierrot
 */
public class Ribbon extends SParent
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
try {
PDDocs D=new PDDocs(getSessOPD(Req));
D.Load(D.GetIdChild("Users/"+getSessOPD(Req).getUser().getName(), "Ribbon"));
ByteArrayOutputStream stream = new ByteArrayOutputStream();
D.getStream(stream);
out.println(new String(stream.toByteArray()));
} catch (PDException ex)
    {
    out.println(genMenu(Req));
    }
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
return "Ribbon Servlet";
}
//-----------------------------------------------------------------------------------------------
private String genMenu(HttpServletRequest Req)
{
try {        
StringBuilder Men=new StringBuilder(3000);
PDRoles R=getSessOPD(Req).getUser().getRol();
Men.append("<?xml version='1.0' encoding='UTF-8'?><ribbon>");
Men.append("<item id='Folders' type='block' mode='cols' text='").append(TT(Req, "Maintenance_Folders")).append("'>");
    if (R.isAllowCreateFolder())
        Men.append("<item id='AddExtF' type='button' isbig='true' text='").append(TT(Req, "Extended_Add")).append("' img='img/FoldAdd.png' />");
    if (R.isAllowCreateFolder())
       Men.append("<item id='AddFold' type='button' text='").append(TT(Req, "Add")).append("' img='img/Fold.png' imgdis='img/Fold.png' />");
    if (R.isAllowMaintainFolder())
        {
        Men.append("<item id='ModExtF' type='button' text='").append(TT(Req, "Update")).append("' img='img/FoldEdit.png' />");
        Men.append("<item id='DelFold' type='button' text='").append(TT(Req, "Delete")).append("' img='img/FoldDel.png' imgdis='img/FoldDel.png' />");
        }
Men.append("</item>");
Men.append("<item id='Folders2' type='block' mode='cols' text='").append(TT(Req, "Folders")).append(" "+TT(Req, "Info")).append("'>");
    Men.append("<item id='RefreshFold' type='button' text='").append(TT(Req, "Refresh")).append("'  img='img/refresh.png' imgdis='img/refresh.png' />");
    Men.append("<item id='SearchFold' type='button' text='").append(TT(Req, "Search")).append("' img='img/FoldSearch.png' imgdis='img/FoldSearch.png' />");
    Men.append("<item id='FoldReports' type='button' text='").append(TT(Req, "Reports")).append("' img='img/Reports.png' imgdis='img/Reports.png' />");
Men.append("</item>");
Men.append("<item id='Documentos'  type='block' mode='cols' text='").append(TT(Req, "Maintenance_Documents")).append("'>");
    if (R.isAllowCreateDoc())
        {
        Men.append("<item id='AddExtDoc' type='button' text='").append(TT(Req, "Extended_Add")).append("' isbig='true' img='img/DocAdd.png' />");    
        Men.append("<item id='AddDoc' type='button' text='").append(TT(Req, "Add")).append("'  img='img/Doc.png' imgdis='img/Doc.png' />");
        }    
    if (R.isAllowMaintainDoc())    
        {
        Men.append("<item id='ChangeACL' type='button' text='").append(TT(Req, "Change_ACL")).append("' img='img/DocACL.png' />");
        Men.append("<item id='DelDoc' type='button' text='").append(TT(Req, "Delete")).append("' img='img/DocDel.png' imgdis='img/DocDel.png' />");  
        }
Men.append("</item>");
Men.append("<item id='Documentos2'  type='block' mode='cols' text='").append(TT(Req, "Documents")).append(" "+TT(Req, "Versions")).append(" '>");
    if (R.isAllowMaintainDoc())
        {
        Men.append("<item id='CheckOut' type='button' text='").append(TT(Req, "CheckOut")).append("' img='img/checkout.png' imgdis='img/checkout.png' />");
        Men.append("<item id='ModExtDoc' type='button' text='").append(TT(Req, "Update")).append("' img='img/DocEdit.png' imgdis='img/DocEdit.png' />");
        Men.append("<item id='CheckIn' type='button' text='").append(TT(Req, "CheckIn")).append("' img='img/checkin.png' imgdis='img/checkin.png' />");
        Men.append("<item id='CancelCheckOut' type='button' text='").append(TT(Req, "Cancel_CheckOut")).append("' img='img/cancelcheckout.png' imgdis='img/cancelcheckout.png' />");    
        }
    Men.append("<item id='ListVer' type='button' text='").append(TT(Req, "Versions")).append("' img='img/ListVers.png' imgdis='img/ListVers.png' />");
    Men.append("<item id='SearchDoc' type='button' text='").append(TT(Req, "Search")).append("' img='img/DocSearch.png' imgdis='img/DocSearch.png' />");
Men.append("</item>");

Men.append("<item id='Other'  type='block' mode='cols' text='").append(TT(Req, "Other_Tasks")).append("'>");
if (R.isAllowCreateThesaur() || R.isAllowMaintainThesaur())
    Men.append("<item id='Thesaurus' type='button' text='").append(TT(Req, "Thesaurus")).append("' isbig='true' img='img/Thesaurus.png' />");
Men.append("</item>");

Men.append("<item id='AdminDoc'  type='block' mode='cols' text='").append(TT(Req, "Adm.Doc")).append("'>");
if (R.isAllowCreateObject()|| R.isAllowMaintainObject() )
   Men.append("<item id='ObjDef' type='button' text=\"").append(TT(Req, "Object_definitions")).append("\"/>");
if (R.isAllowCreateMime()|| R.isAllowMaintainMime())
   Men.append("<item id='MimeTypes' type='button' text='").append(TT(Req, "Mime_Types")).append("'/>");
if (R.isAllowCreateRepos() || R.isAllowMaintainRepos())
   Men.append("<item id='Repositories' type='button' text='").append(TT(Req, "Repositories")).append("'/>");
Men.append("</item>");

Men.append("<item id='AdminSec'  type='block' mode='cols' text='").append(TT(Req, "Adm.Sec")).append("'>");
if (R.isAllowCreateAcl() || R.isAllowMaintainAcl())
   Men.append("<item id='ACL' type='button' text='").append(TT(Req, "ACL")).append("'/>");
if (R.isAllowCreateGroup() || R.isAllowMaintainGroup())
   Men.append("<item id='Groups' type='button' text='").append(TT(Req, "Groups")).append("'/>");
if (R.isAllowCreateUser() || R.isAllowMaintainUser())
   Men.append("<item id='Users' type='button' text='").append(TT(Req, "Users")).append("'/>");
if (R.isAllowCreateRole()|| R.isAllowMaintainRole())
   Men.append("<item id='Roles' type='button' text='").append(TT(Req, "Roles")).append("'/>");
if (R.isAllowCreateAuth()|| R.isAllowMaintainAuth() )
   Men.append("<item id='Authenticators' type='button' text=\"").append(TT(Req, "Authenticators")).append("\"/>");
if (R.isAllowCreateCustom()|| R.isAllowMaintainCustom() )
   Men.append("<item id='Customizations' type='button' text='").append(TT(Req, "Customizations")).append("'/>");
Men.append("</item>");

if (R.isAllowCreateTask()|| R.isAllowMaintainTask() )
    {
    Men.append("<item id='AdminTasks'  type='block' mode='cols' text='").append(TT(Req, "Adm.Tasks")).append("'>");
    Men.append("<item id='TaskCron' type='button' text='").append(TT(Req, "Task_Cron")).append("'/>");
    Men.append("<item id='TaskEvents' type='button' text='").append(TT(Req, "Task_Events")).append("'/>");
    Men.append("<item id='PendTasklog' type='button' text='").append(TT(Req, "Pending_Task_log")).append("'/>"); 
    Men.append("<item id='EndTasksLogs' type='button' text='").append(TT(Req, "Ended_Tasks_Logs")).append("'/>");
    if (R.isAllowCreateObject()|| R.isAllowMaintainObject() )
        Men.append("<item id='TraceLogs' type='button' text='").append(TT(Req, "Trace_Logs")).append("'/>");
    Men.append("</item>");
    }

Men.append("</ribbon>");
return(Men.toString());
} catch (PDException ex)
    {
    return ("<?xml version='1.0'?><ribbon><item id='Error' type='block' text='"+ex.getLocalizedMessage()+"'/></ribbon>");
    }
}
//-----------------------------------------------------------------------------------------------
}
