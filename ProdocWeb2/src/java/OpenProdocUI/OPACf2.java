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

import static OpenProdocUI.SParent.Confs;
import static OpenProdocUI.SParent.ShowMessage;
import static OpenProdocUI.SParent.TT;
import static OpenProdocUI.SParent.getActFolderId;
import static OpenProdocUI.SParent.getConnector;
import static OpenProdocUI.SParent.getOPACProperties;
import static OpenProdocUI.SParent.getSessOPD;
import static OpenProdocUI.SParent.setOPACConf;
import static OpenProdocUI.SParent.setSessOPD;
import Sessions.CurrentSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ExtConf;
import prodoc.PDException;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDLog;
import prodoc.PDMimeType;
import prodoc.PDReport;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class OPACf2 extends SParent
{
@Override
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
try {
    ProcessPage(request, response);
} catch (Exception e)
    {
    PrintWriter out = response.getWriter();
    ShowMessage(request, out, e.getLocalizedMessage());
    e.printStackTrace();
    AddLog(e.getMessage());
    }
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param response
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, HttpServletResponse response) throws Exception
{   
Req.setCharacterEncoding("UTF-8");
StringBuffer OPACUrl=Req.getRequestURL();    
String IdOPAC=Req.getParameter("OPAC_Id"); 
OPACUrl.append("?OPAC_Id=").append(IdOPAC);
DriverGeneric PDSession=getSessOPD(Req);
if (PDSession==null) // http sessions timed out
    {
    if (IdOPAC!=null && IdOPAC.length()!=0) // we can create session from Id
        {
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
            if (ConfOPAC.getUser()==null || ConfOPAC.getUser().length()==0)
                {
                try (ServletOutputStream out = response.getOutputStream()) {
                out.println("ERROR NO OPAC Configured User");;
                    } 
                }
            else if(PDLog.isDebug())
                PDLog.Debug("OPACUser: "+ConfOPAC.getUser());        
            LocalSess=ProdocFW.getSession(getConnector(), ConfOPAC.getUser(), ConfOPAC.getPass()); // just for translation   
            setSessOPD(Req, LocalSess, CurrentSession.Mode.OPAC);
            PDSession=LocalSess;
            }
        }
    else // no session nor IdOPAC -> Ask for Refresh Page
        {
        try (ServletOutputStream out = response.getOutputStream()) {
        out.println("Session Expired...");
        out.println("Load OPAC again...");
        } catch (IOException ex)
            {
            ex.printStackTrace();
            }
        }
    }
PDFolders TmpFold;
Cursor Cur=null;    
try {      
PDFolders F=new PDFolders(PDSession);
ExtConf ConfOPAC=Confs.get(IdOPAC);
String CurrFoldId=F.getIdPath(ConfOPAC.getBaseFolder());
String CurrType=Req.getParameter("DT"); 
if (!ConfOPAC.getDocTipesList().contains(CurrType))
    PDException.GenPDException("Incorrect_type", CurrType);
OPACUrl.append("&DT=").append(CurrType);
String ReportId=Req.getParameter("FORMAT_REP");
OPACUrl.append("&FORMAT_REP=").append(ReportId);
TmpFold=new PDFolders(PDSession, CurrType);
Record Rec=TmpFold.getRecSum();
Conditions Cond=new Conditions();
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    if (Attr.getName().equals(PDDocs.fDOCTYPE))
        {
        Attr=Rec.nextAttr();
        continue;
        }
    String Val=Req.getParameter(CurrType+"_"+Attr.getName());
    if (Val == null || Val.length()==0)
        {
        Attr=Rec.nextAttr();
        continue;
        }
    OPACUrl.append("&").append(CurrType).append("_").append(Attr.getName()).append("=").append(Val.replace(" ", "%20"));
    String Comp="EQ";
    int PosField=ConfOPAC.getFieldsToInclude().indexOf(Attr.getName());
    if (PosField!=-1 && PosField<ConfOPAC.getFieldsComp().size())
        {
        Comp=ConfOPAC.getFieldsComp().elementAt(PosField);
        }
    if (Attr.getType()==Attribute.tTHES)
        Cond.addCondition(SParent.FillCond(Req, Attr, Val, Comp));
    else if (!(Attr.getName().equals(PDDocs.fACL) && Val.equals("null") 
          || Attr.getType()==Attribute.tBOOLEAN && Val.equals("0") ) )
        {
        Cond.addCondition(SParent.FillCond(Req, Attr, Val, Comp));
        }
    Attr=Rec.nextAttr();
    }
if (ConfOPAC.getOrdFields()==null)
    Cur=TmpFold.Search(CurrType, Cond, ConfOPAC.isInheritance(), true, CurrFoldId, null);
else
    Cur=TmpFold.Search(CurrType, Cond, ConfOPAC.isInheritance(), true, CurrFoldId, ConfOPAC.getOrdFields(), ConfOPAC.getOrdOrd());
    
Vector<Record> ListRes=new Vector();
Record Res=PDSession.NextRec(Cur);
while (Res!=null)
    {
    ListRes.add(Res);
    Res=PDSession.NextRec(Cur);
    }
PDSession.CloseCursor(Cur);   
Cur=null;
PDReport Rep=new PDReport(PDSession);
Rep.LoadFull(ReportId);
ArrayList<String> GeneratedRep= Rep.GenerateRep(getActFolderId(Req), null, ListRes, 0, 0, SParent.getIO_OSFolder(),ConfOPAC.getMaxResults(), OPACUrl.toString());
String File2Send=GeneratedRep.get(0);
PDMimeType mt=new PDMimeType(PDSession);
mt.Load(Rep.getMimeType());
response.setHeader("Content-disposition", "inline; filename=" + Rep.getName());    
response.setContentType(mt.getMimeCode()+";charset=UTF-8");
response.setCharacterEncoding("UTF-8");
FileInputStream in=null;
byte Buffer[]=new byte[64*1024];
ServletOutputStream out=null;
try {
out=response.getOutputStream();
in = new FileInputStream(File2Send);
int readed=in.read(Buffer);
while (readed!=-1)
    {
    out.write(Buffer, 0, readed);
    readed=in.read(Buffer);
    }
in.close();
out.flush();
out.close();
File f=new File(File2Send);
f.delete();
} catch (Exception e)
    {
    if (out!=null)
        out.close();
    if (in!=null)
        in.close();
    throw e;
    }
}
finally 
    {
    if (Cur!=null)
       PDSession.CloseCursor(Cur);
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
return "OPACf2 Servlet";
}
//-----------------------------------------------------------------------------------------------

    /**
     *
     * @param Title
     * @param Req
     * @param PDSession
     * @param CurrFold
     * @param NewType
     * @param FR
     * @param ReadOnly
     * @param Modif
     * @return
     * @throws PDException
     */
protected String GenSearchDocForm(String Title, HttpServletRequest Req, DriverGeneric PDSession, String CurrFold, String NewType, Record FR, boolean ReadOnly, boolean Modif) throws PDException
{
StringBuilder Form= new StringBuilder(3000);
Attribute Attr;
Form.append("[ {type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 200},");
Form.append("{type: \"label\", label: \"").append(TT(Req, Title)).append("\", labelWidth:200},");
Form.append("{type: \"block\", width: 600, list:[");
Form.append("{type: \"checkbox\", name: \"Subtypes\", label:\"").append(TT(Req, "Subtypes")).append("\", tooltip:\"").append(TT(Req,"When_checked_includes_subtypes_of_folders_in_results")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"checkbox\", name: \"SubFolders\", label:\"").append(TT(Req, "SubFolders")).append("\", tooltip:\"").append(TT(Req,"When_checked_limits_the_search_to_actual_folder_and_subfolders")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"checkbox\", name: \"IncludeVers\", label:\"").append(TT(Req, "Versions")).append("\", tooltip:\"").append(TT(Req,"When_checked_includes_all_versions_of_document_in_results")).append("\"}");
Form.append("]},{type: \"input\", name: \"FullTextSearch\", label: \"").append(TT(Req, "Full_Text_Search")).append("\", inputWidth: 300, labelWidth:200},");

FR.initList();
Attr=FR.nextAttr();
while (Attr!=null)
    {
    if (! (Attr.getName().equals(PDDocs.fDOCTYPE) || (Attr.getName().equals(PDDocs.fSTATUS)) ) )    
        Form.append(GenSearchInput(Req, Attr));
    Attr=FR.nextAttr();
    }
Form.append("{type: \"block\", width: 250, list:[");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
Form.append("{type: \"hidden\", name:\"OPDNewType\", value: \"").append(NewType).append("\"},");
Form.append("{type: \"hidden\", name:\"CurrFold\", value: \"").append(CurrFold).append("\"}");
Form.append("]}");
Form.append("];");
return(Form.toString());
}
//----------------------------------------------------------------------------
}