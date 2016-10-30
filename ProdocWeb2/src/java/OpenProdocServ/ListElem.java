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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ObjPD;
import prodoc.PDACL;
import prodoc.PDAuthenticators;
import prodoc.PDCustomization;
import prodoc.PDException;
import prodoc.PDGroups;
import prodoc.PDMimeType;
import prodoc.PDObjDefs;
import prodoc.PDRepository;
import prodoc.PDRoles;
import prodoc.PDTasksCron;
import prodoc.PDTasksDef;
import prodoc.PDTasksDefEvent;
import prodoc.PDTasksExec;
import prodoc.PDTasksExecEnded;
import prodoc.PDUser;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ListElem extends SParent
{
public static final String MANTACL="ACL";
public static final String MANTGROUPS="Groups";
public static final String MANTUSERS="Users";
public static final String MANTROLES="Roles";
public static final String MANTMIME="MimeTypes";
public static final String MANTREPO="Repositories";
public static final String MANTOBJ="ObjDef";
public static final String MANTAUTH="Authenticators";
public static final String MANTCUST="Customizations";
public static final String MANTTASKCRON="TaskCron";
public static final String MANTTASKEVENT="TaskEvents";
public static final String MANTPENDTASK="PendTaskLog";
public static final String MANTTASKENDED="EndTaskLogs";

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
DriverGeneric PDSession=getSessOPD(Req);
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(3000);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows><head>");
try {
Record Rec;
String ElemType=Req.getParameter("TE");
String Filter=Req.getParameter("F");
if (Filter==null)
    Filter="";
ObjPD Obj=GenObj(ElemType, PDSession, null);
Rec=Obj.getRecord().CopyMono();
if (ElemType.equals(MANTUSERS))
    Rec.delAttr(PDUser.fPASSWORD);
else if (ElemType.equals(MANTROLES))
    {
    Rec.delAttr(PDRoles.fALLOWCREATEACL);
    Rec.delAttr(PDRoles.fALLOWCREATEAUTH);
    Rec.delAttr(PDRoles.fALLOWCREATECUSTOM);
    Rec.delAttr(PDRoles.fALLOWCREATEDOC);
    Rec.delAttr(PDRoles.fALLOWCREATEFOLD);
    Rec.delAttr(PDRoles.fALLOWCREATEGROUP);
    Rec.delAttr(PDRoles.fALLOWCREATEMIME);
    Rec.delAttr(PDRoles.fALLOWCREATEOBJECT);
    Rec.delAttr(PDRoles.fALLOWCREATEREPOS);
    Rec.delAttr(PDRoles.fALLOWCREATEROLE);
    Rec.delAttr(PDRoles.fALLOWCREATETASK);
    Rec.delAttr(PDRoles.fALLOWCREATETHESAUR);
    Rec.delAttr(PDRoles.fALLOWCREATEUSER);
    Rec.delAttr(PDRoles.fALLOWMAINTAINACL);
    Rec.delAttr(PDRoles.fALLOWMAINTAINAUTH);
    Rec.delAttr(PDRoles.fALLOWMAINTAINCUSTOM);
    Rec.delAttr(PDRoles.fALLOWMAINTAINDOC);
    Rec.delAttr(PDRoles.fALLOWMAINTAINFOLD);
    Rec.delAttr(PDRoles.fALLOWMAINTAINGROUP);
    Rec.delAttr(PDRoles.fALLOWMAINTAINMIME);
    Rec.delAttr(PDRoles.fALLOWMAINTAINOBJECT);
    Rec.delAttr(PDRoles.fALLOWMAINTAINREPOS);
    Rec.delAttr(PDRoles.fALLOWMAINTAINROLE);
    Rec.delAttr(PDRoles.fALLOWMAINTAINTASK);
    Rec.delAttr(PDRoles.fALLOWMAINTAINTHESAUR);
    Rec.delAttr(PDRoles.fALLOWMAINTAINUSER);
    }
//Rec.delAttr(Filter);
int WIDTH=600;
if (ElemType.equals(MANTOBJ) || ElemType.equals(MANTTASKCRON)|| ElemType.equals(MANTTASKEVENT)
        || ElemType.equals(MANTTASKENDED) || ElemType.equals(MANTPENDTASK))
   WIDTH=1000; 
Rec.initList();
Attribute Attr=Rec.nextAttr();
int Count=1;
while (Attr!=null)
    {
    Resp.append("<column width=\"").append(Count==Rec.NumAttr()?"*":WIDTH/Rec.NumAttr()).append("\" type=\"ro\" align=\"left\" sort=\"str\">").append(TT(Req,Attr.getUserName())).append("</column>");
    Count++;
    Attr=Rec.nextAttr();
    }
Resp.append("</head>");
Cursor ListObj;
if (ElemType.equals("PendTaskLog") || ElemType.equals("EndTaskLogs") )
    {
    Conditions Conds=new Conditions();
    String Category=Req.getParameter("Category");
    if (Category!=null && Category.length()!=0)
        {
        Condition C=new Condition(PDTasksDef.fCATEGORY, Condition.cEQUAL, Category);    
        Conds.addCondition(C);
        }
    String Fec1=Req.getParameter("Fec1");
    Date D1;
    if (Fec1!=null && Fec1.length()!=0)
        D1=new Date(Long.parseLong(Fec1));
    else
        D1=new Date(System.currentTimeMillis()-600000);    
    PDTasksExecEnded T=new PDTasksExecEnded(PDSession);
    Attribute AttrF1=T.getRecord().getAttr(PDTasksExecEnded.fPDDATE);
    AttrF1.setValue(D1);
    Condition C1=new Condition(AttrF1, Condition.cGET);  
    Conds.addCondition(C1);
    String Fec2=Req.getParameter("Fec2");
    if (Fec2!=null && Fec2.length()!=0)
        {    
        Attribute AttrF2=AttrF1.Copy();
        AttrF2.setValue(new Date(Long.parseLong(Fec2)));
        Condition C2=new Condition(AttrF2, Condition.cGET);  
        Conds.addCondition(C2);
        }
    if (ElemType.equals("PendTaskLog")  )
        {
        PDTasksExec Task=new PDTasksExec(PDSession);
        ListObj=Task.Search(Conds);
        }
    else 
        {
        PDTasksExecEnded Task=new PDTasksExecEnded(PDSession);
        ListObj=Task.Search(Conds);
        }
    }
else
    ListObj=Obj.SearchLike(Filter);
Record NextObj=PDSession.NextRec(ListObj);
while (NextObj!=null)
    {
    String Id;
    if (ElemType.equals("PendTaskLog") || ElemType.equals("EndTaskLogs") )
        {
        Id=(String)NextObj.getAttr(PDTasksExecEnded.fPDID).getValue();
        }
    else
        Id=(String)NextObj.getAttr(PDACL.fNAME).getValue();
    Rec.assign(NextObj);
    Resp.append(SParent.GenRowGrid(Req, Id, Rec, true));    
    NextObj=PDSession.NextRec(ListObj);
    }   
PDSession.CloseCursor(ListObj);
Resp.append("</rows>");
} catch (PDException ex)
    {
    Resp.append("<LV>Error</LV></row>");
    }
out.println( Resp.toString().replace("&", "&amp;") );   
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
return "ListElem Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListElem");
}
//-----------------------------------------------------------------------------------------------
static public ObjPD GenObj(String ElemType, DriverGeneric PDSession, String Id) throws PDException
{   
ObjPD Obj=null;
if (ElemType.equals(MANTACL))
    Obj=new PDACL(PDSession);
else if (ElemType.equals(MANTGROUPS))
    Obj=new PDGroups(PDSession);
else if (ElemType.equals(MANTUSERS))
    Obj=new PDUser(PDSession);
else if (ElemType.equals(MANTROLES))
    Obj=new PDRoles(PDSession);
else if (ElemType.equals(MANTMIME))
    Obj=new PDMimeType(PDSession);
else if (ElemType.equals(MANTREPO))
    Obj=new PDRepository(PDSession);
else if (ElemType.equals(MANTOBJ))
    Obj=new PDObjDefs(PDSession);
else if (ElemType.equals(MANTAUTH))
    Obj=new PDAuthenticators(PDSession);
else if (ElemType.equals(MANTCUST))
    Obj=new PDCustomization(PDSession);
else if (ElemType.equals(MANTTASKCRON))
    Obj=new PDTasksCron(PDSession);
else if (ElemType.equals(MANTTASKEVENT))
    Obj=new PDTasksDefEvent(PDSession);
else if (ElemType.equals(MANTPENDTASK))
    Obj=new PDTasksExec(PDSession);
else if (ElemType.equals(MANTTASKENDED))
    Obj=new PDTasksExecEnded(PDSession);
if (Id!=null)
    Obj.Load(Id);
return (Obj);
}

}
/***
 * 
 * http://dhtmlx.com/docs/products/dhtmlxGrid/samples/12_initialization_loading/01_grid_config_xml.html
 * 
 ***/