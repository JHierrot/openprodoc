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
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDTasksCron;
import prodoc.PDTasksDef;

/**
 *
 * @author jhierrot
 */
public class TaskWiz extends SParent
{

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{   
int TT=Integer.parseInt(Req.getParameter("TT"));
String TE=Req.getParameter("TE");
if (TE.equals(ListElem.MANTTASKCRON))
    {
    String[] listTypeTask = PDTasksCron.getListTypeTask(); 
    String[] ParamTexts=new String[4];
    switch (TT)
        {
        case PDTasksDef.fTASK_DELETE_OLD_FOLD: 
        case PDTasksDef.fTASK_DELETE_OLD_DOC:
            ParamTexts[0]=TT(Req, "Subtypes");
            ParamTexts[1]=TT(Req, "Days_to_Maintain");
            ParamTexts[2]=TT(Req, "Parent_Folder");
            ParamTexts[3]="";
            break;
        case PDTasksDef.fTASK_PURGEDOC:
            ParamTexts[0]=TT(Req, "Days_to_Maintain");
            ParamTexts[1]="";
            ParamTexts[2]="";
            ParamTexts[3]="";
            break;
        case PDTasksDef.fTASK_IMPORT:
            ParamTexts[0]=TT(Req, "SubFolders");
            ParamTexts[1]=TT(Req, "Default_Docs_Type");
            ParamTexts[2]=TT(Req, "Parent_Folder");
            ParamTexts[3]=TT(Req, "Source_Folder");
            break;
        case PDTasksDef.fTASK_EXPORT:
            ParamTexts[0]=TT(Req, "Subtypes");
            ParamTexts[1]=TT(Req, "Days_From_Update");
            ParamTexts[2]=TT(Req, "Parent_Folder");
            ParamTexts[3]=TT(Req, "Destination_Folder");
            break;
        case PDTasksDef.fTASK_DOCSREPORT:
            ParamTexts[0]=TT(Req, "Subtypes");
            ParamTexts[1]=TT(Req, "From_Number_of_days");
            ParamTexts[2]=TT(Req, "Group");
            ParamTexts[3]=TT(Req, "Parent_Folder");
            break;
        case 6:
            ParamTexts[0]=TT(Req, "Subtypes");
            ParamTexts[1]=TT(Req, "From_Number_of_days");
            ParamTexts[2]=TT(Req, "Group");
            ParamTexts[3]=TT(Req, "Parent_Folder");
            break;
        }
    out.println(
    "[ {type: \"settings\", position: \"label-left\", labelWidth: 200, inputWidth: 150}," +
    "{type: \"label\", label: \""+listTypeTask[TT]+"\"}," +
    "{type: \"input\", name: \"TaskParam\",  label: \""+ParamTexts[0]+"\""+(ParamTexts[0].length()==0?",hidden:true":"")+"}," +
    "{type: \"input\", name: \"TaskParam2\", label: \""+ParamTexts[1]+"\""+(ParamTexts[1].length()==0?",hidden:true":"")+"}," +
    "{type: \"input\", name: \"TaskParam3\", label: \""+ParamTexts[2]+"\""+(ParamTexts[2].length()==0?",hidden:true":"")+"}," +
    "{type: \"input\", name: \"TaskParam4\", label: \""+ParamTexts[3]+"\""+(ParamTexts[3].length()==0?",hidden:true":"")+"}," +
    "{type: \"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
        "{type: \"newcolumn\", offset:20 }," +
        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}," +
    "]} ];");
    }
else
    {
    
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
return "TaskWiz Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("TaskWiz");
}
//-----------------------------------------------------------------------------------------------
}
