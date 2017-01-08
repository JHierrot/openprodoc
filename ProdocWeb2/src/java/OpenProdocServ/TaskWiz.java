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
import prodoc.PDTasksDefEvent;

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
        case PDTasksDef.fTASK_FOLDSREPORT:
            ParamTexts[0]=TT(Req, "Subtypes");
            ParamTexts[1]=TT(Req, "From_Number_of_days");
            ParamTexts[2]=TT(Req, "Group");
            ParamTexts[3]=TT(Req, "Parent_Folder");
            break;
        }
    StringBuilder SB=new StringBuilder(1000);
    SB.append("[ {type: \"settings\", position: \"label-left\", labelWidth: 200, inputWidth: 150},");
    SB.append("{type: \"label\", label: \"").append(listTypeTask[TT]).append("\"},");
    SB.append("{type: \"input\", name: \"TaskParam\",  label: \"").append(ParamTexts[0]).append("\"").append(ParamTexts[0].length()==0?",hidden:true":"").append("},");
    SB.append("{type: \"input\", name: \"TaskParam2\", label: \"").append(ParamTexts[1]).append("\"").append(ParamTexts[1].length()==0?",hidden:true":"").append("},");
    SB.append("{type: \"input\", name: \"TaskParam3\", label: \"").append(ParamTexts[2]).append("\"").append(ParamTexts[2].length()==0?",hidden:true":"").append("},");
    SB.append("{type: \"input\", name: \"TaskParam4\", label: \"").append(ParamTexts[3]).append("\"").append(ParamTexts[3].length()==0?",hidden:true":"").append("},");
    SB.append("{type: \"block\", width: 250, list:[");
    SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
    SB.append("{type: \"newcolumn\", offset:20 },");
    SB.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
    SB.append("]} ];");
    out.println(SB.toString());
    }
else
    {
    String[] listTypeTask = PDTasksDefEvent.getListTypeTask(); 
    String[] ParamTexts=new String[4];
    switch (TT)
        {
        case PDTasksDefEvent.fTASKEVENT_UPDATE_DOC: 
        case PDTasksDefEvent.fTASKEVENT_UPDATE_FOLD: 
            ParamTexts[0]="Attribute 1";
            ParamTexts[1]="Attribute 2";
            ParamTexts[2]="Attribute 3";
            ParamTexts[3]=TT(Req, "Folder");
            break;  
        case PDTasksDefEvent.fTASKEVENT_COPY_DOC: 
        case PDTasksDefEvent.fTASKEVENT_COPY_FOLD: 
            ParamTexts[0]=TT(Req, "Destination_Folder");
            ParamTexts[1]=TT(Req, "Parent_Folder");
            ParamTexts[2]="";
            ParamTexts[3]="";
            break;  
        case PDTasksDefEvent.fTASKEVENT_EXPORT_DOC: 
        case PDTasksDefEvent.fTASKEVENT_EXPORT_FOLD: 
            ParamTexts[0]=TT(Req, "Parent_Folder");
            ParamTexts[1]=TT(Req, "Destination_Folder");
            ParamTexts[2]="";
            ParamTexts[3]="";
            break;  
        case PDTasksDefEvent.fTASKEVENT_CONVERT_DOC: 
            ParamTexts[0]=TT(Req, "Destination_Folder");
            ParamTexts[1]=TT(Req, "Parent_Folder");
            ParamTexts[2]=TT(Req, "Shell_Order");
            ParamTexts[3]=TT(Req, "Extension");
            break;  
        case PDTasksDefEvent.fTASKEVENT_FTINDEX_DOC: 
        case PDTasksDefEvent.fTASKEVENT_FTUPDA_DOC: 
        case PDTasksDefEvent.fTASKEVENT_FTDEL_DOC: 
            ParamTexts[0]=TT(Req, "Parent_Folder");
            ParamTexts[1]="";
            ParamTexts[2]="";
            ParamTexts[3]="";
            break;  
        }
    StringBuilder SB=new StringBuilder(1000);
    SB.append("[ {type: \"settings\", position: \"label-left\", labelWidth: 200, inputWidth: 150},");
    SB.append("{type: \"label\", label: \"").append(listTypeTask[TT-PDTasksDefEvent.STARTNUM]).append("\"},");
    if (TT!=PDTasksDef.fTASK_PURGEDOC)
        SB.append("{type: \"checkbox\", name: \"TaskParam\",  label: \"").append(ParamTexts[0]).append("\"").append(ParamTexts[0].length()==0?",hidden:true":"").append("},");
    else
        SB.append("{type: \"input\", name: \"TaskParam\",  label: \"").append(ParamTexts[0]).append("\"").append(ParamTexts[0].length()==0?",hidden:true":"").append("},");
    if (TT==PDTasksDef.fTASK_IMPORT)
        {
        SB.append("{type: \"combo\", name: \"TaskParam2\", label: \"").append(ParamTexts[1]).append("\", options:[");
        SB.append(getComboModelDoc(getSessOPD(Req), null) );
        SB.append("]},");        
        }
    else    
        SB.append("{type: \"input\", name: \"TaskParam2\", label: \"").append(ParamTexts[1]).append("\"").append(ParamTexts[1].length()==0?",hidden:true":"").append("},");
    if (TT==PDTasksDef.fTASK_DOCSREPORT || TT==PDTasksDef.fTASK_FOLDSREPORT)
        {
        SB.append("{type: \"combo\", name: \"TaskParam3\", label: \"").append(ParamTexts[2]).append("\", options:[");
        SB.append(getComboModel("Groups", getSessOPD(Req), "") );
        SB.append("]},");        
        }
    else    
        SB.append("{type: \"input\", name: \"TaskParam3\", label: \"").append(ParamTexts[2]).append("\"").append(ParamTexts[2].length()==0?",hidden:true":"").append("},");
    SB.append("{type: \"input\", name: \"TaskParam4\", label: \"").append(ParamTexts[3]).append("\"").append(ParamTexts[3].length()==0?",hidden:true":"").append("},");
    SB.append("{type: \"block\", width: 250, list:[");
    SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
    SB.append(    "{type: \"newcolumn\", offset:20 },");
    SB.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
    SB.append("]} ];");
    out.println(SB.toString());
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
}
