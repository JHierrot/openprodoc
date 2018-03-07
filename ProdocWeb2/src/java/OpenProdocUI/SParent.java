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
 * author: Joaquin Hierro      2011
 * 
 */

package OpenProdocUI;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.Attribute;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ExtConf;
import prodoc.ObjPD;
import prodoc.PDACL;
import prodoc.PDAuthenticators;
import prodoc.PDCustomization;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDGroups;
import prodoc.PDObjDefs;
import prodoc.PDRepository;
import prodoc.PDRoles;
import prodoc.PDThesaur;
import prodoc.PDUser;
import prodoc.Record;




/**
 *
 * @author jhierrot
 * @version
 */
public class SParent extends HttpServlet
{

protected static String ProdocProperRef=null;
/**
 * 
 */
public final static int LAST_FORM      =0;
/**
 * 
 */
public final static int LISTDOC_FORM   =1;
/**
 * 
 */
public final static int SEARCHDOC_FORM =2;
/**
 * 
 */
//private static final String ListAttrFold=PDFolders.fACL+"/"+PDFolders.fFOLDTYPE+"/"+PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fTITLE+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE;
//private static final String ListAttrDoc=PDDocs.fACL+"/"+PDDocs.fDOCTYPE+"/"+PDDocs.fPARENTID+"/"+PDDocs.fPDID+"/"+PDDocs.fTITLE+"/"+"/"+PDDocs.fPDID+"/"+PDDocs.fPDAUTOR+"/"+PDDocs.fPDDATE+"/"+PDDocs.fLOCKEDBY+"/"+PDDocs.fVERSION+"/"+PDDocs.fPURGEDATE+"/"+PDDocs.fREPOSIT+"/"+PDDocs.fSTATUS+"/"+PDDocs.fMIMETYPE+"/"+PDDocs.fNAME;
private static HashSet ListAttrFold=null;
private static HashSet ListAttrDoc=null;
        
public static HashSet ListThes=null;

public final static String SD_QType="SD_QType";
public final static String SD_FType="SD_FType";
public final static String SD_Cond="SD_Cond";
public final static String SD_SubT="SD_SubT";
public final static String SD_SubF="SD_SubF";
public final static String SD_Vers="SD_Vers";
public final static String SD_actFolderId="SD_actFolderId";
public final static String SD_Ord="SD_Ord";
public final static String SD_Rec="SD_Rec";
public final static String SD_OperComp="SD_OperComp";
public final static String SD_FTQ="SD_FTQ";

protected static boolean OPDFWLoaded=false;

/** Initializes the servlet.
 * @param config 
 * @throws ServletException 
 */
public void init(ServletConfig config) throws ServletException
{
super.init(config);
}
//-----------------------------------------------------------------------------------------------

/** Destroys the servlet.
*/
public void destroy()
{

}
//-----------------------------------------------------------------------------------------------

/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException
*/
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
response.setContentType("text/html;charset=UTF-8");
PrintWriter out = response.getWriter();
try {
if (!Connected(request))
    {
    AskLogin(request, out);
    }
else
    {
    request.setCharacterEncoding("UTF-8");
    ProcessPage(request, out);
    }
} catch (Exception e)
    {
    ShowMessage(request, out, e.getLocalizedMessage());
    e.printStackTrace();
    AddLog(e.getMessage());
    }
finally {
        out.close();
        }
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Texto
 */
protected void AddLog(String Texto)
{
System.out.println(this.getServletName()+":"+new Date()+"="+Texto);
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 * @throws PDException
 */
protected boolean Connected(HttpServletRequest Req) throws Exception
{
DriverGeneric D=getSessOPD(Req);
if (D!=null)
    return(true);
return(false);
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @param out
 * @throws Exception
 */
protected void AskLogin(HttpServletRequest Req, PrintWriter out) throws Exception
{
HttpSession Sess=Req.getSession();
out.println(new FLogin(Sess).toHtml());
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @param Message
 */
static public void ShowMessage(HttpServletRequest Req, PrintWriter out, String Message)
{
HttpSession Sess=Req.getSession();
//FMessage f=new FMessage(Req, "Mensaje Prodoc", "", Message);
//out.println(f.ToHtml(Sess));
}
/**
 *
 * @param Req
 * @param out
 * @param Message
 */
static public void ShowMessage(HttpServletRequest Req, ServletOutputStream out, String Message)
{
try {
    HttpSession Sess = Req.getSession();
//    FMessage f = new FMessage(Req, "Mensaje Prodoc", "", Message);
//    out.println(f.ToHtml(Sess));
    out.flush();
} catch (IOException ex)
    {
    ex.printStackTrace();
    }
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @param out
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
throw new PDException("Wrong Address");
}
//-----------------------------------------------------------------------------------------------

/** Handles the HTTP <code>GET</code> method.
* @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException  
*/
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
processRequest(request, response);
}
//-----------------------------------------------------------------------------------------------

/** Handles the HTTP <code>POST</code> method.
* @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException
*/
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException
{
processRequest(request, response);
}
//-----------------------------------------------------------------------------------------------

/** Returns a short description of the servlet.
 * @return 
 */
@Override
public String getServletInfo()
{
return "Servlet Base del resto de aplicacion";
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @param Text
 * @return
 */
static public String TT(HttpServletRequest Req, String Text)
{
if (getSessOPD(Req)==null)
    return( DriverGeneric.DefTT(Req.getLocale().getLanguage(), Text) );
else
    return( getSessOPD(Req).TT(Text) );
}
//----------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 */
public static boolean Reading (HttpServletRequest Req)
{
String Read=Req.getParameter("Read");
if (Read==null || !Read.equals("1"))
    return(false);
else
    return(true);
}
//----------------------------------------------------------
/**
 * 
 * @param Req
 * @param d
 * @return
 */
static public String FormatDate(HttpServletRequest Req, Date d)
{
if (d==null)
    return("");
return(getFormatterDate(Req).format(d));
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param d
 * @return
 */
static public String FormatTS(HttpServletRequest Req, Date d)
{
if (d==null)
    return("");
return(getFormatterTS(Req).format(d));
}
//----------------------------------------------------------
/**
 * @param Req 
 * @return the formatterTS
 */
public static SimpleDateFormat getFormatterTS(HttpServletRequest Req)
{
/**  due the new JS controls
SimpleDateFormat formatterTS;
try {
 formatterTS = new SimpleDateFormat(getSessOPD(Req).getPDCust().getTimeFormat());
formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
} catch (PDException ex)
    {
    formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
return formatterTS;**/
return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
//---------------------------------------------------------------------
/**
 * @param Req 
 * @return the formatterDate
 */
public static SimpleDateFormat getFormatterDate(HttpServletRequest Req)
{
    /*  due the new JS controls
SimpleDateFormat formatterDate;
try {
    formatterDate = new SimpleDateFormat(getSessOPD(Req).getPDCust().getDateFormat());
} catch (PDException ex)
    {
    formatterDate = new SimpleDateFormat("yyyy-MM-dd");
    }
return formatterDate;**/
return new SimpleDateFormat("yyyy-MM-dd");
}
//---------------------------------------------------------------------
/** Returns a String with 2 characters code of language for the user sesion
 * @param Req httprequest of the page
 * @return the Language for the user sesion
 */
public static String getLang(HttpServletRequest Req)
{
try {
    return(getSessOPD(Req).getPDCust().getLanguage());
} catch (PDException ex)
    {
    return("EN");
    }
}
//---------------------------------------------------------------------
/** Returns a String with 2 characters code of HELP language for the user sesion
 * @param Req httprequest of the page
 * @return the HELP Language for the user sesion
 */
public static String getHelpLang(HttpServletRequest Req)
{
try {
    return(DriverGeneric.getHelpLang(getSessOPD(Req).getPDCust().getLanguage()));
} catch (PDException ex)
    {
    return("EN");
    }
}
//---------------------------------------------------------------------
/**
 * @param Req 
 * @return the formatTS to show on tooltips
 */
public static String getFormatTS(HttpServletRequest Req)
{
try {
return (getSessOPD(Req).getPDCust().getTimeFormat() );
}  catch (PDException ex)
    {
    return("yyyy-MM-dd HH:mm:ss");
    }
}
//---------------------------------------------------------------------
/**
 * @param Req 
 * @return the formatDate to show on tooltips
 */
public static String getFormatDate(HttpServletRequest Req)
{
try {
return(getSessOPD(Req).getPDCust().getDateFormat());
} catch (PDException ex)
    {
    return("yyyy-MM-dd");
    }
}
//---------------------------------------------------------------------
/**
 * Fill the value of the attribute with the text o value of the fieldest1
 * @param Req 
 * @param Attr
 * @param Val
 * @param Modif
 * @throws PDException 
 */
static public void FillAttr(HttpServletRequest Req, Attribute Attr, String Val, boolean Modif) throws PDException
{
if (Modif && !Attr.isModifAllowed())
    return;
try {
if (Attr.isMultivalued())    
    {
    Attr.ClearValues();   
    Attr.Import(Val);
    }
else if (Attr.getType()==Attribute.tSTRING || Attr.getType()==Attribute.tTHES)
    {
    Attr.setValue(Val);
    }
else if (Attr.getType()==Attribute.tDATE)
    {
    if (Val.length()>0)
        Attr.setValue(getFormatterDate(Req).parse(Val));
    else
        Attr.setValue(null);
    }
else if (Attr.getType()==Attribute.tTIMESTAMP)
    {
    if (Val.length()>0)
        Attr.setValue(getFormatterTS(Req).parse(Val));
    else
        Attr.setValue(null);
    }
else if (Attr.getType()==Attribute.tINTEGER)
    {
    if (Val.length()>0)
        Attr.setValue(Integer.parseInt(Val));
    else
        Attr.setValue(null);
    }
else
    Attr.setValue("Error");
} catch (Exception ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(), Val);
    }
}
//--------------------------------------------------------------
/**
 * Creates a new condition of the attribute with the text o value of the fieldest1
 * @param Req
 * @param Attr
 * @param Val
 * @return a new condition
 * @throws PDException
 */
static public Condition FillCond(HttpServletRequest Req, Attribute Attr, String Val) throws PDException
{
return(FillCond(Req, Attr, Val, "EQ"));    
}
//--------------------------------------------------------------
/**
 * Creates a new condition of the attribute with the text o value of the fieldest1
 * @param Req
 * @param Attr
 * @param Val
 * @return a new condition
 * @throws PDException
 */
static public Condition FillCond(HttpServletRequest Req, Attribute Attr, String Val, String OperS) throws PDException
{
Val=Val.trim();
Condition Cond = null;
int Oper;
if (OperS.equals("EQ"))
    Oper=Condition.cEQUAL;
else if (OperS.equals("GT"))
    Oper=Condition.cGT;
else if (OperS.equals("LT"))
    Oper=Condition.cLT;
else if (OperS.equals("GE"))
    Oper=Condition.cGET;
else if (OperS.equals("LE"))
    Oper=Condition.cLET;
else if (OperS.equals("NE"))
    Oper=Condition.cNE;
else 
    Oper=Condition.cLIKE;
try {
if (Attr.getType()==Attribute.tSTRING || Attr.getType()==Attribute.tTHES)
    {
    Cond=new Condition(Attr.getName(), Oper, Val);
    if (Attr.isMultivalued())
        Attr.Import(Val);
    else
        Attr.setValue(Val);
    }
else if (Attr.getType()==Attribute.tDATE)
    {
    Attr.setValue(getFormatterDate(Req).parse(Val));   
    Cond=new Condition(Attr, Oper);
    }
else if (Attr.getType()==Attribute.tTIMESTAMP)
    {
    Attr.setValue(getFormatterTS(Req).parse(Val));
    Cond=new Condition(Attr, Oper);
    }
else if (Attr.getType()==Attribute.tBOOLEAN)
    {
    if (Val.equals("1"))    
        Cond=new Condition(Attr.getName(), Oper, true);
    }
return(Cond);
} catch (Exception ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(), Val);
    throw new PDException(ex.getLocalizedMessage());
    }
}
//--------------------------------------------------------------
/**
 * Creates a new condition of the attribute with the text o value of the fieldest1
 * @param Req
 * @param Attr
 * @param Val
 * @return a new condition
 * @throws PDException
 */
static public Condition FillCondLike(HttpServletRequest Req, Attribute Attr, String Val) throws PDException
{
Condition Cond = null;
try {
if (Attr.getType()==Attribute.tSTRING)
    {
    Cond=new Condition(Attr.getName(), Condition.cLIKE, Val);
    if (Attr.isMultivalued())
        Attr.Import(Val);
    else
        Attr.setValue(Val);
    }
else if (Attr.getType()==Attribute.tDATE)
    {
//    Cond=new Condition(Attr.getName(), Condition.cEQUAL, getFormatterDate(Req).parse(Val));
    Attr.setValue(getFormatterDate(Req).parse(Val));
    Cond=new Condition(Attr, Condition.cEQUAL);
    }
else if (Attr.getType()==Attribute.tTIMESTAMP)
    {
//    Cond=new Condition(Attr.getName(), Condition.cEQUAL, getFormatterTS(Req).parse(Val));
    Attr.setValue(getFormatterTS(Req).parse(Val));
    Cond=new Condition(Attr, Condition.cEQUAL);
    }
else if (Attr.getType()==Attribute.tBOOLEAN)
    {
    if (Val!=null)
        {
        Cond=new Condition(Attr.getName(), Condition.cEQUAL, true);
        Attr.setValue(true);
        }
    }
return(Cond);
} catch (Exception ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(), Val);
    throw new PDException(ex.getLocalizedMessage());
    }
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @return
 */
public static DriverGeneric getSessOPD(HttpServletRequest Req)
{
return (DriverGeneric)Req.getSession(true).getAttribute("PRODOC_SESS");
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @param OPDSess
 */
public static void setSessOPD(HttpServletRequest Req, DriverGeneric OPDSess)
{
Req.getSession().setAttribute("PRODOC_SESS", OPDSess);
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @return
 */
public static Record getActFold(HttpServletRequest Req)
{
return (Record)Req.getSession().getAttribute("FOLD_REC");
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @param ActFold 
 */
public static void setActFold(HttpServletRequest Req, Record ActFold)
{
Req.getSession().setAttribute("FOLD_REC", ActFold);
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @return
 */
public static Record getActTerm(HttpServletRequest Req)
{
return (Record)Req.getSession().getAttribute("TERM_REC");
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @param ActFold 
 */
public static void setActTerm(HttpServletRequest Req, Record ActTerm)
{
Req.getSession().setAttribute("TERM_REC", ActTerm);
}
//--------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 */
public static String getActFolderId(HttpServletRequest Req)
{
HttpSession Sess=Req.getSession();
return (String)Sess.getAttribute("FoldId");
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @param ActFoldId
 */
public static void setActFoldId(HttpServletRequest Req, String ActFoldId)
{
Req.getSession().setAttribute("FoldId", ActFoldId);
}
//--------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 */
public static String getActTermId(HttpServletRequest Req)
{
HttpSession Sess=Req.getSession();
return (String)Sess.getAttribute("TermId");
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @param ActTermId
 */
public static void setActTermId(HttpServletRequest Req, String ActTermId)
{
Req.getSession().setAttribute("TermId", ActTermId);
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @return
 */
public static Record getActDoc(HttpServletRequest Req)
{
return (Record)Req.getSession().getAttribute("DOC_REC");
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @param ActDoc
 */
public static void setActDoc(HttpServletRequest Req, Record ActDoc)
{
Req.getSession().setAttribute("DOC_REC", ActDoc);
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @return
 */
public static String getActDocId(HttpServletRequest Req)
{
Cookie[] cookies = Req.getCookies();
for (int i = 0; i < cookies.length; i++)
    {
    Cookie cookie = cookies[i];
    if (cookie.getName().equals("DocId"))
        return(cookie.getValue());
    }
return(null);
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param ActDocId
 */
public static void setActDocId(HttpServletRequest Req, String ActDocId)
{
Req.getSession().setAttribute("DocId", ActDocId);
}
//--------------------------------------------------------------
/**
 * 
 * @param Req
 * @param ListDel
 */
public static void setListDocs(HttpServletRequest Req, HashMap ListDel)
{
Req.getSession().setAttribute("ListDocs", ListDel);
}
//--------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 */
public static HashMap getListDocs(HttpServletRequest Req)
{
return (HashMap)Req.getSession().getAttribute("ListDocs");
}
//--------------------------------------------------------------


/**
 *
 * @param Req
 * @param Excluded
 * @param Rec
 * @throws PDException 
 */
public static void FillRec(HttpServletRequest Req, String Excluded, Record Rec) throws PDException
{
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    if (!Excluded.contains(Attr.getName()))
        {
        String Val=Req.getParameter(Attr.getName());
        if (Attr.getType()==Attribute.tBOOLEAN)
            {
            if(Val == null)
                Attr.setValue(false);
            else
                Attr.setValue(true);
            }
        else if(Val != null)
            {
            SParent.FillAttr(Req, Attr, Val, false);
            }
        }
    Attr=Rec.nextAttr();
    }
}

//-----------------------------------------------------------------------------------------------
/**
 * 
 * @return
 */
static public String getVersion()
{
return("2.1.2");
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @return
 */
static public String getStyle(HttpServletRequest Req)
{
try {
if (getSessOPD(Req)==null)
   return("");
String s=getSessOPD(Req).getPDCust().getStyle();
if (s==null || s.length()==0)
    return("");
else
    return (s+"/");
} catch (PDException ex)
    {
    return("");
    }
}
//----------------------------------------------------------
public static String getProdocProperRef() throws Exception
{
if (ProdocProperRef==null)
    {
    InputStream Is=null;    
    File f=new File("../conf/Prodoc.properties");
System.out.println("OpenProdoc Properties 1=["+f.getAbsolutePath()+"]");    
    if (f.exists())
        {
        ProdocProperRef=f.getAbsolutePath();    
        return(ProdocProperRef);
        }
    f=new File("conf/Prodoc.properties");
System.out.println("OpenProdoc Properties 2=["+f.getAbsolutePath()+"]");    
    if (f.exists())
        {
        ProdocProperRef=f.getAbsolutePath();    
        return(ProdocProperRef);
        }
    String Path=System.getProperty("user.home");    
System.out.println("OpenProdoc Properties 3=["+Path+"]");    
    try {
    Is  = new FileInputStream(Path+File.separator+"OPDWeb.properties");        
    } catch (Exception ex)
        {
        Is=null;    
        }
    if (Is==null)
        {
        Path=System.getenv("OPDWeb");
 System.out.println("OpenProdoc Properties 4=["+Path+"]");    
       try {
        Is  = new FileInputStream(Path+File.separator+"OPDWeb.properties");
        } catch (Exception ex)
            {
            Is=null;    
            }
        }
    Properties p= new Properties(); // TODO: CAMBIAR DOC apunta a OPEWEB , no properties y jdbc en path. Interfaz administración tareas ingles y 't''
    p.load(Is);
    Is.close();
    ProdocProperRef=p.getProperty("OPDConfig");
    }
System.out.println("ProdocProperRef=["+ProdocProperRef+"]");
return(ProdocProperRef);
}
//----------------------------------------------------------   
public static String getSessName(HttpServletRequest Req)
{
try {    
return (" @"+getSessOPD(Req).getUser().getName()+" ( "+getSessOPD(Req).getUser().getDescription()+" )");
} catch (Exception ex)
    {
    return ("");
    }
}
//----------------------------------------------------------   

public static boolean getIsSearch(HttpServletRequest Req)
{
String IsSearch=Req.getParameter("IsSearch");
if (IsSearch==null || !IsSearch.equals("1"))
    return(false);
else
    return(true);
}
//-----------------------------------------------------------
public static String getIO_OSFolder()
{
return(System.getProperty("java.io.tmpdir"));
}
//-----------------------------------------------------------
/**
 * 
 * @param ListName
 * @return
 */
static public String getComboModel(String ListName, DriverGeneric Session, String Value) throws PDException
{
StringBuilder ListVals=new StringBuilder(5000);
ObjPD Obj = null;
if (ListName.equals("Roles"))
    Obj = new PDRoles(Session);
else if (ListName.equals("Users"))
    Obj = new PDUser(Session);
else if (ListName.equals("Groups"))
    Obj = new PDGroups(Session);
else if (ListName.equals("ACL"))
    Obj = new PDACL(Session);
else if (ListName.equals("Reposit"))
    Obj = new PDRepository(Session);
else if (ListName.equals("Authenticators"))
    Obj = new PDAuthenticators(Session);
else if (ListName.equals("Customizers"))
    Obj = new PDCustomization(Session);
Cursor CursorId = Obj.SearchLikeDesc("*");
Record Res=Session.NextRec(CursorId);
while (Res!=null)
    {
    ListVals.append("{text: \"").append(Res.getAttr(PDRoles.fDESCRIPTION).getValue()).append("\", value: \"").append(Res.getAttr(PDRoles.fNAME).getValue()).append("\" "+((Value!=null&&Value.equalsIgnoreCase((String)Res.getAttr(PDRoles.fNAME).getValue()))?", selected: true":"")+"}");
    Res=Session.NextRec(CursorId);
    if (Res!=null)
        ListVals.append(",");
    }
Session.CloseCursor(CursorId);
return(ListVals.toString());
}
//----------------------------------------------------------------
/**
 * 
 * @param ListName
 * @return
 */
static public String getComboModelSearch(String ListName, DriverGeneric Session) throws PDException
{
StringBuilder ListVals=new StringBuilder(5000);
ObjPD Obj = null;
if (ListName.equals("Roles"))
    Obj = new PDRoles(Session);
else if (ListName.equals("Users"))
    Obj = new PDUser(Session);
else if (ListName.equals("Groups"))
    Obj = new PDGroups(Session);
else if (ListName.equals("ACL"))
    Obj = new PDACL(Session);
else if (ListName.equals("Reposit"))
    Obj = new PDRepository(Session);
else if (ListName.equals("Authenticators"))
    Obj = new PDAuthenticators(Session);
else if (ListName.equals("Customizers"))
    Obj = new PDCustomization(Session);
Cursor CursorId = Obj.SearchLike("*");
Record Res=Session.NextRec(CursorId);
while (Res!=null)
    {
    ListVals.append("{text: \"").append(Res.getAttr(PDRoles.fDESCRIPTION).getValue()).append("\", value: \"").append(Res.getAttr(PDRoles.fNAME).getValue()).append("\"},");
    Res=Session.NextRec(CursorId);
    }
ListVals.append("{text: \" \", value: \"null\", selected: true}");
Session.CloseCursor(CursorId);
return(ListVals.toString());
}
//----------------------------------------------------------------
/**
 * Obtains a list of clases of type folder allowed to the user
 * @return a DefaultComboModel with names of classes of folder
 */
static protected String getComboModelFold(DriverGeneric Session, String Value) throws PDException
{
if (Value==null || Value.length()==0)   
    Value=PDFolders.getTableName();
StringBuilder ListVals=new StringBuilder(5000);
PDObjDefs Obj = new PDObjDefs(Session);
Cursor CursorId = Obj.getListFold();
Record Res=Session.NextRec(CursorId);
while (Res!=null)
    {
    ListVals.append("{text: \"").append(Res.getAttr(PDObjDefs.fDESCRIPTION).getValue()).append("\", value: \"").append(Res.getAttr(PDObjDefs.fNAME).getValue()).append("\"").append(Res.getAttr(PDObjDefs.fNAME).getValue().equals(Value)?",selected:true":"").append("}");
    Res=Session.NextRec(CursorId);
    if (Res!=null)
        ListVals.append(",");
    }
Session.CloseCursor(CursorId);
return(ListVals.toString());
}
//-----------------------------------------------------------------------------------------------
/**
 * Obtains a list of clases of type folder allowed to the user
 * @return a DefaultComboModel with names of classes of folder
 */
static protected String getComboModelDoc(DriverGeneric Session, String Value) throws PDException
{
if (Value==null || Value.length()==0)   
    Value=PDDocs.getTableName();
StringBuilder ListVals=new StringBuilder(5000);
PDObjDefs Obj = new PDObjDefs(Session);
Cursor CursorId = Obj.getListDocs();
Record Res=Session.NextRec(CursorId);
while (Res!=null)
    {
    ListVals.append("{text: \"").append(Res.getAttr(PDObjDefs.fDESCRIPTION).getValue()).append("\", value: \"").append(Res.getAttr(PDObjDefs.fNAME).getValue()).append("\"").append(Res.getAttr(PDObjDefs.fNAME).getValue().equals(Value)?",selected:true":"").append("}");
    Res=Session.NextRec(CursorId);
    if (Res!=null)
        ListVals.append(",");
    }
Session.CloseCursor(CursorId);
return(ListVals.toString());
}
//-----------------------------------------------------------------------------------------------
/**
 * Obtains a list of clases of type folder allowed to the user
 * @return a DefaultComboModel with names of classes of folder
 */
static protected String getComboModelDocRIS(DriverGeneric Session, String Value) throws PDException
{
if (Value==null || Value.length()==0)   
    Value=PDDocs.getTableName();
StringBuilder ListVals=new StringBuilder(5000);
PDObjDefs Obj = new PDObjDefs(Session);
Cursor CursorId = Obj.getListDocsRIS();
Record Res=Session.NextRec(CursorId);
while (Res!=null)
    {
    ListVals.append("{text: \"").append(Res.getAttr(PDObjDefs.fDESCRIPTION).getValue()).append("\", value: \"").append(Res.getAttr(PDObjDefs.fNAME).getValue()).append("\"").append(Res.getAttr(PDObjDefs.fNAME).getValue().equals(Value)?",selected:true":"").append("}");
    Res=Session.NextRec(CursorId);
    if (Res!=null)
        ListVals.append(",");
    }
Session.CloseCursor(CursorId);
return(ListVals.toString());
}
//-----------------------------------------------------------------------------------------------
protected String GenerateCompleteFoldForm(String Title, HttpServletRequest Req, DriverGeneric PDSession, String CurrFold, String NewType, Record FR, boolean ReadOnly, boolean Modif, String DefACL) throws PDException
{
StringBuilder Form= new StringBuilder(3000);
Attribute Attr;
Form.append("[ {type: \"settings\", position: \"label-left\",offsetLeft:10,labelWidth: 180, inputWidth: 300},");
Form.append("{type: \"label\", label: \"").append(TT(Req, Title)).append("\"},");
Attr=FR.getAttr(PDFolders.fTITLE);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=FR.getAttr(PDFolders.fACL);
Form.append("{type: \"combo\", name: \"" + PDFolders.fACL + "\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" options:[");
if (Attr.getValue()==null)
    Form.append(getComboModel("ACL",PDSession,DefACL) );
else
    Form.append(getComboModel("ACL",PDSession,(String)Attr.getValue()) );
Form.append("]},");
FR.initList();
Attr=FR.nextAttr();
ArrayList<Attribute> FL=new ArrayList();
while (Attr!=null)
    {
    if (!getListAttrFold().contains(Attr.getName()))
        {
        FL.add(Attr);
        }
    Attr=FR.nextAttr();
    }
for (int i = 0; i < FL.size(); i++)
    {
    Attr = FL.get(i);
    Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
    }
Form.append("{type: \"block\", width: 250, list:[{type: \"button\", name: \"OK\", value: \"").
    append(TT(Req, "Ok")).
    append("\"},{type: \"newcolumn\", offset:20 },{type: \"button\", name: \"CANCEL\", value: \"").
    append(TT(Req, "Cancel")).
    append("\"},{type: \"hidden\", name:\"OPDNewType\", value: \"").
    append(NewType).
    append("\"},{type: \"hidden\", name:\"CurrFold\", value: \"").
    append(CurrFold).append("\"}]}");
Form.append("];");
return(Form.toString());
}
//-----------------------------------------------------------------------------------------------
protected String GenerateCompleteDocForm(String Title, HttpServletRequest Req, DriverGeneric PDSession, String CurrFold, String NewType, Record FR, boolean ReadOnly, boolean Modif, String DefACL, boolean AclMode) throws PDException
{
StringBuilder Form= new StringBuilder(3000);
Attribute Attr;
Form.append("[ {type: \"settings\", position: \"label-left\", offsetLeft:10, labelWidth: 180, inputWidth: 300},");
Form.append("{type: \"label\", label: \"").append(TT(Req, Title)).append("\", labelWidth: 280},");
Attr=FR.getAttr(PDFolders.fTITLE);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=FR.getAttr(PDFolders.fACL);
Form.append("{type: \"combo\", name: \"" + PDDocs.fACL + "\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly&&!AclMode?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" options:[");
if (Attr.getValue()==null)
    Form.append(getComboModel("ACL",PDSession,DefACL) );
else
    Form.append(getComboModel("ACL",PDSession,(String)Attr.getValue()) );
Form.append("]},");
FR.initList();
Attr=FR.nextAttr();
ArrayList<Attribute> FL=new ArrayList();
while (Attr!=null)
    {
    if (!getListAttrDoc().contains(Attr.getName()))
        {
        FL.add(Attr);
        }
    Attr=FR.nextAttr();
    }
for (int i = 0; i < FL.size(); i++)
    {
    Attr = FL.get(i);
    Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
    }
Form.append("{type: \"block\", width: 350, list:[");
if (!Title.equals("Document_Attributes"))
    {
    Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
    Form.append("{type: \"newcolumn\", offset:20 },");
    }
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
if (Modif)
    {
    Form.append("{type: \"newcolumn\", offset:20 },");
    Form.append("{type: \"button\", name: \"OK2\", value: \"").append(TT(Req, "Modif_without_File")).append("\"},");
    }
Form.append("{type: \"hidden\", name:\""+PDDocs.fDOCTYPE+"\", value: \"").append(NewType).append("\"},");
Form.append("{type: \"hidden\", name:\"CurrFold\", value: \"").append(CurrFold).append("\"}");
Attr=FR.getAttr(PDDocs.fPDID);
if (Attr!=null && Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
    Form.append(",{type: \"hidden\", name:\""+PDDocs.fPDID+"\", value: \"").append((String)Attr.getValue()).append("\"}");
if (!ReadOnly)
    {
    String DT=(String)FR.getAttr(PDDocs.fDOCTYPE).getValue();
    PDObjDefs Def=new PDObjDefs(PDSession);
    Def.Load(DT);
    String Rp=Def.getReposit();
    PDRepository Rep=new PDRepository(PDSession);
    Rep.Load(Rp);
    if (Rep.IsRef())
        {
        String Path=(String)FR.getAttr(PDDocs.fNAME).getValue(); 
        if (Path==null)
            Path="";
        Form.append("]},{type: \"input\", name: \""+PDDocs.fNAME+"\", label: \"URL\",").append(" required: ").append(!Modif?"true":"false").append(", inputWidth: 300, value:\""+Path+"\"}];");
        }
    else
        {
        if (Modif)
           Form.append("]},{type: \"fieldset\", label: \""+TT(Req, "Import_Doc")+"\", list:["+  
           "{type: \"upload\", name: \"UpFile\", titleText:\""+TT(Req, "Drag_n_Drop_file_or_click_icon_to_select_file")+"\", url: \"ModDocF\", inputWidth: 350, autoStart: true, disabled:true }"+    
           "]}];");              
        else
           Form.append("]},{type: \"fieldset\", label: \""+TT(Req, "Import_Doc")+"\", list:["+  
           "{type: \"upload\", name: \"UpFile\", titleText:\""+TT(Req, "Drag_n_Drop_file_or_click_icon_to_select_file")+"\", url: \"ImportDocF\", inputWidth: 350, autoStart: true, disabled:true }"+    
           "]}];"); 
        }
    }
else
   Form.append("]}];");
return(Form.toString());
}
//----------------------------------------------------------------------------
protected String GenerateCompleteThesForm(String Title, HttpServletRequest Req, DriverGeneric PDSession, PDThesaur TmpThes, boolean ReadOnly, boolean Modif) throws PDException
{
StringBuilder Form=new StringBuilder(2000);    
Attribute Attr;
Form.append("[ {type: \"settings\", position: \"label-left\",offsetLeft:10,labelWidth: 140}, ");
Form.append("{type: \"label\", label: \"").append(TT(Req, Title)).append("\"},");
Attr=TmpThes.getRecord().getAttr(PDThesaur.fPDID);
Attr.setUserName("Thesaurus_Number");
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fNAME);
Attr.setUserName("Thesaurus_Name");
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fDESCRIP);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fLANG);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fSCN);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Form.append("{type: \"block\", width: 250, list:[");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"}]} ];");
return(Form.toString());    
}
//----------------------------------------------------------------------------
protected String GenerateCompleteTermForm(String Title, HttpServletRequest Req, DriverGeneric PDSession, PDThesaur TmpThes, boolean ReadOnly, boolean Modif, String CurrTerm, String CurrThes) throws PDException
{
StringBuilder Form=new StringBuilder(2000);    
Attribute Attr;
Form.append("[ {type: \"settings\", offsetLeft:10, position: \"label-left\", labelWidth: 140}, ");
Form.append("{type: \"label\", label: \"").append(TT(Req, Title)).append("\"},");
Attr=TmpThes.getRecord().getAttr(PDThesaur.fPDID);
Form.append("{type: \"hidden\", name:\"PDId\", value: \"").append(Attr.getValue()).append("\"},");
Form.append("{type: \"hidden\", name:\"CurrTerm\", value: \"").append(CurrTerm).append("\"},");
Form.append("{type: \"hidden\", name:\"H_RT\"},"); // no needed initial vaues because are in grid
Form.append("{type: \"hidden\", name:\"H_Lang\"},");
Attr=TmpThes.getRecord().getAttr(PDThesaur.fNAME);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fDESCRIP);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fLANG);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fSCN);
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Attr=TmpThes.getRecord().getAttr(PDThesaur.fUSE);
Attr.setType(Attribute.tTHES);
Attr.setLongStr(new Integer(CurrThes));
Form.append(GenInput(Req, Attr,  ReadOnly, Modif));
Form.append("{type: \"block\", width: 250, list:[");
Form.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
Form.append("{type: \"newcolumn\", offset:20 },");
Form.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"}]} ];");
return(Form.toString());    
}

//----------------------------------------------------------------
public StringBuilder GenInput(HttpServletRequest Req,Attribute Attr, boolean ReadOnly, boolean Modif) throws PDException
{
if (Modif&&!Attr.isModifAllowed()) 
    ReadOnly=true;
StringBuilder FormField= new StringBuilder(300);  
switch (Attr.getType())
    {
    case Attribute.tDATE:
        FormField.append("{type: \"calendar\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: ").append(Attr.isRequired()?"true":"false").append(",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", dateFormat: \"%Y-%m-%d\", calendarPosition: \"right\"},");
        break;
    case Attribute.tTIMESTAMP:
        FormField.append("{type: \"calendar\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: ").append(Attr.isRequired()?"true":"false").append(",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", dateFormat: \"%Y-%m-%d %H:%i:%s\", calendarPosition: \"right\"},");
        break;
    case Attribute.tBOOLEAN:
        FormField.append("{type: \"checkbox\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(Attr.getValue()!=null?("checked:"+((Boolean)(Attr.getValue())==true)+","):"").append(" tooltip:\"").append(TT(Req, Attr.getDescription())).append("\"},");
        break;
    case Attribute.tINTEGER:
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: ").append(Attr.isRequired()?"true":"false").append(",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", numberFormat:\"000000000\", validate:\"ValidInteger\"},");
        break;
    case Attribute.tFLOAT:
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: ").append(Attr.isRequired()?"true":"false").append(",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", numberFormat:\"0000000000.00\", validate:\"ValidNumeric\"},");
        break;
    case Attribute.tSTRING:
        if (Attr.isMultivalued())
        {
            FormField.append("{type: \"block\", width: 550, offsetLeft:1, list:[");
            FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\", readonly: \"true\",value:\"").append(EscapeHtmlJson(Attr.Export())).append("\", tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", labelWidth: 180, inputWidth: 250},");
            FormField.append("{type: \"newcolumn\", offset:2 },");
            FormField.append("{type: \"button\",").append(ReadOnly?"disabled:1,":"").append(" name:  \"M_").append(Attr.getName()).append("\", value: \"*\", width: 20}]},");
        }
        else
            FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: ").append(Attr.isRequired()?"true":"false").append(",").append(Attr.getValue()!=null?("value:\""+EscapeHtmlJson(Attr.Export())+"\","):"").append(" tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", inputWidth: 300, maxLength:").append(Attr.getLongStr()).append("},");
        break;
    case Attribute.tTHES:
        {
        if (Attr.isMultivalued())
            {
            StringBuilder SBNames=new StringBuilder();
            StringBuilder SBId=new StringBuilder();
            TreeSet<String> ThesTermsList = Attr.getValuesList();
            if (!ThesTermsList.isEmpty())
                {
                PDThesaur TmpThes=new PDThesaur(getSessOPD(Req));
                for (Iterator<String> iterator = ThesTermsList.iterator(); iterator.hasNext();)
                    {
                    String NextTerm = iterator.next();
                    TmpThes.Load(NextTerm);
                    SBId.append(TmpThes.getPDId());
                    SBNames.append(TmpThes.getName());
                    if (iterator.hasNext())
                        {
                        SBId.append(Attribute.StringListSeparator);
                        SBNames.append(Attribute.StringListSeparator);                
                        }
                    }
                }
            FormField.append("{type: \"block\", width: 550, offsetLeft:1, list:[");
            FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\", readonly: \"true\",value:\"").append(EscapeHtmlJson(SBNames.toString())).append("\", tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", labelWidth: 180, inputWidth: 250, userdata: {ThesId:").append(Attr.getLongStr()).append("}},");
            FormField.append("{type: \"newcolumn\", offset:2 },");
            FormField.append("{type: \"hidden\", name:\"TH_").append(Attr.getName()).append("\", value: \"").append(SBId).append("\"},");
            FormField.append("{type: \"button\",").append(ReadOnly?"disabled:1,":"").append(" name:  \"MT_").append(Attr.getName()).append("\", value: \"*T\", width: 20}]},");
            }
        else
            {
            PDThesaur TmpThes=new PDThesaur(getSessOPD(Req));
            if (Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
               TmpThes.Load((String)Attr.getValue());
            else
                TmpThes.setPDId("");
            FormField.append("{type: \"block\", width: 550, offsetLeft:1, list:[");
            FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\", labelWidth: 180, inputWidth: 250, readonly: \"true\",value:\"").append(TmpThes.getName()==null?"":TmpThes.getName()).append("\", tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", userdata: {ThesId:").append(Attr.getLongStr()).append("}},");
            FormField.append("{type: \"hidden\", name:\"TH_").append(Attr.getName()).append("\", value: \"").append(TmpThes.getPDId()).append("\"},");
            FormField.append("{type: \"newcolumn\" },");
            FormField.append("{type: \"button\",").append(ReadOnly?"disabled:1,":"").append(" name:  \"T_").append(Attr.getName()).append("\", value: \"T\", width: 20},");
            FormField.append("{type: \"newcolumn\" },");
            FormField.append("{type: \"button\",").append(ReadOnly?"disabled:1,":"").append(" name:  \"TD_").append(Attr.getName()).append("\", value: \"-\", width: 20}]},");
            }
        }
        break;
    default:
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"ERROR TIPO\", required: ").append(Attr.isRequired()?"true":"false").append(", tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", inputWidth: 300, maxLength:").append(Attr.getLongStr()).append("},");
        break;
    }
return(FormField);
}
//----------------------------------------------------------------
protected StringBuilder GenSearchInput(HttpServletRequest Req, Attribute Attr) throws PDException
{
StringBuilder FormField= new StringBuilder(300);  
FormField.append("{type: \"block\", width: 650, list:[");
FormField.append(GenCompCombo(Req, Attr));
FormField.append("{type: \"newcolumn\", offset:5 },");
if (Attr.getName().equals(PDFolders.fACL) || Attr.getName().equals(PDDocs.fACL) )
    {
    DriverGeneric PDSession=getSessOPD(Req);   
    FormField.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", inputWidth: 300, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", options:[");
    FormField.append(getComboModelSearch("ACL",PDSession) );
    FormField.append("]},");    
    }
else if (Attr.getName().equals(PDDocs.fDOCTYPE))
    {
    DriverGeneric PDSession=getSessOPD(Req);   
    FormField.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", inputWidth: 200, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", options:[");
    FormField.append(getComboModel("ACL",PDSession,(String)Attr.getValue()) );
    FormField.append("]},");    
    }
else switch (Attr.getType())
    {
    case Attribute.tDATE:
        FormField.append("{type: \"calendar\", name: \"").append(Attr.getName()).append("\", tooltip:\"").append(Attr.getDescription()).append("\", dateFormat: \"%Y-%m-%d\", calendarPosition: \"right\"}");
        break;
    case Attribute.tTIMESTAMP:
        FormField.append("{type: \"calendar\", name: \"").append(Attr.getName()).append("\", tooltip:\"").append(Attr.getDescription()).append("\", dateFormat: \"%Y-%m-%d %H:%i:%s\", calendarPosition: \"right\"}");
        break;
    case Attribute.tBOOLEAN:
        FormField.append("{type: \"checkbox\", name: \"").append(Attr.getName()).append("\", tooltip:\"").append(Attr.getDescription()).append("\"}");
        break;
    case Attribute.tINTEGER:
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", tooltip:\"").append(Attr.getDescription()).append("\", numberFormat:\"000000000\", validate:\"ValidInteger\"}");
        break;
    case Attribute.tFLOAT:
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", tooltip:\"").append(Attr.getDescription()).append("\", numberFormat:\"0000000000.00\", validate:\"ValidNumeric\"}");
        break;
    case Attribute.tSTRING:
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", inputWidth: 300, maxLength:").append(Attr.getLongStr()).append("}");
        break;
    case Attribute.tTHES:
        {
        PDThesaur TmpThes=new PDThesaur(getSessOPD(Req));
        if (Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
           TmpThes.Load((String)Attr.getValue());
        else
            TmpThes.setPDId("");
        FormField.append("{type: \"block\", width: 300, list:[");
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", readonly: \"true\",value:\"").append(TmpThes.getName()==null?"":TmpThes.getName()).append("\", tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", userdata: {ThesId:").append(Attr.getLongStr()).append("}}, ");
        FormField.append("{type: \"hidden\", name:\"TH_").append(Attr.getName()).append("\", value: \"").append(TmpThes.getPDId()).append("\"},");
        FormField.append("{type: \"newcolumn\" },");
        FormField.append("{type: \"button\", name:  \"T_").append(Attr.getName()).append("\", value: \"T\", width: 20},");
        FormField.append("{type: \"newcolumn\" },");
        FormField.append("{type: \"button\", name:  \"TD_").append(Attr.getName()).append("\", value: \"-\", width: 20}]},");
        }
        break;
    default:
        FormField.append("{type: \"input\", name: \"").append(Attr.getName()).append("\", label: \"ERROR TIPO\", required: ").append(Attr.isRequired()?"true":"false").append(", tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", inputWidth: 300, maxLength:").append(Attr.getLongStr()).append("} ");
        break;
    }
FormField.append("]},");
return(FormField);
}
//----------------------------------------------------------------

private StringBuilder GenCompCombo(HttpServletRequest Req, Attribute Attr )
{
StringBuilder CompCombo=new StringBuilder(200);
CompCombo.append("{type: \"combo\", label: \"").append(TT(Req, Attr.getUserName())).append("\", name: \"Comp_").append(Attr.getName()).append("\", inputWidth:\"auto\", options:[");
CompCombo.append("{text: \"=\", value: \"EQ\", selected: true},");
CompCombo.append("{text: \"<>      \", value: \"NE\"}");
if (! (Attr.getType()==Attribute.tBOOLEAN || Attr.getName().equals(PDFolders.fACL) || Attr.getName().equals(PDDocs.fACL) 
        || Attr.getName().equals(PDFolders.fFOLDTYPE) || Attr.getName().equals(PDDocs.fDOCTYPE) 
        || Attr.getType()==Attribute.tTHES ) )
    {
    CompCombo.append(", {text: \">\", value: \"GT\"},");
    CompCombo.append("{text: \">=\", value: \"GE\"},");
    CompCombo.append("{text: \"<\", value: \"LT\"},");
    CompCombo.append("{text: \"<=\", value: \"LE\"},");
    CompCombo.append("{text: \"Contains\", value: \"CT\"}");
    }
CompCombo.append("]},");
return(CompCombo);
}
//----------------------------------------------------------------
protected String GenHeader(HttpServletRequest Req,Record Rec, boolean IsDoc)
{
StringBuilder Head=new StringBuilder(300);
StringBuilder Edit=new StringBuilder(300);
StringBuilder Type=new StringBuilder(300);
Rec.initList();
Attribute Attr=Rec.nextAttr();
//ArrayList<Attribute> FL=new ArrayList();
while (Attr!=null)
    {
    if (!Attr.isMultivalued()) 
        {
        Head.append(TT(Req,Attr.getUserName())).append(",");
        if (Attr.getName().equals(PDDocs.fTITLE))
           Edit.append("link,");
        else if (Attr.getName().equals(PDDocs.fLOCKEDBY))
            Edit.append("rotxt,");
        else        
            Edit.append("ro,");
        Type.append("str,");
        }
    Attr=Rec.nextAttr();
    }
Head.deleteCharAt(Head.length()-1);
Head.append("\n"); 
Edit.deleteCharAt(Edit.length()-1);
Edit.append("\n"); 
Type.deleteCharAt(Type.length()-1);
// Type.append("\n"); Using println       
Head.append(Edit);
Head.append(Type);
return(Head.toString());
}
//----------------------------------------------------------------
public static String GenRowGrid(HttpServletRequest Req, String Id, Record NextRec, boolean IsXML)
{
boolean IsDoc;
if (NextRec.ContainsAttr(PDDocs.fDOCTYPE) || Id.contains("|"))
    IsDoc=true;
else
    IsDoc=false;
StringBuilder Row=new StringBuilder(1000);
Attribute Attr;
if (IsXML)
    {
    Row.append("<row id=\"").append(Id).append("\">");
    }
else
    {
    Row.append("{ id:\"").append(Id).append("\", data:[");
    }
NextRec.initList();
Attr=NextRec.nextAttr();
while (Attr!=null)
    {
    if (Attr.getType()==Attribute.tTHES)    
        {
        if (Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
            {
            try {    
            PDThesaur TmpThes=new PDThesaur(SParent.getSessOPD(Req));
            TmpThes.Load((String)Attr.getValue());
            if (IsXML)
                Row.append("<cell>").append(EscapeTree(TmpThes.getName())).append("</cell>");
            else
                Row.append("\"").append(EscapeHtmlJson(TmpThes.getName())).append("\",");
            } catch(Exception ex)
                {    
                if (IsXML)
                    Row.append("<cell></cell>");
                else    
                    Row.append("\"\",");
                }
            }
        else
            {
            if (IsXML)
                Row.append("<cell></cell>");
            else    
                Row.append("\"\",");
            }
        }
    else if (IsXML && IsDoc && Attr.getName().equals(PDDocs.fTITLE))
        Row.append("<cell>").append(EscapeTree(Attr.ExportXML())).append("^SendDoc?Id=").append(Id).append("^_blank</cell>");
    else    
        {
        if (IsXML)
            Row.append("<cell>").append(EscapeTree(Attr.ExportXML())).append("</cell>");
        else
            Row.append("\"").append(EscapeHtmlJson(Attr.ExportXML())).append("\",");
        }
    Attr=NextRec.nextAttr();
    }
if (IsXML)
    Row.append("]</row>");
else
    {
    Row.deleteCharAt(Row.length()-1);
    Row.append("]}");
    }
return(Row.toString());
}
//----------------------------------------------------------------
public static StringBuilder GenRowCSV(HttpServletRequest Req, Record NextRec)
{
StringBuilder Row=new StringBuilder(1000);
Attribute Attr;
NextRec.initList();
Attr=NextRec.nextAttr();
while (Attr!=null)
    {
    if (Attr.getType()==Attribute.tTHES)    
        {
        if (Attr.getValue()!=null && ((String)Attr.getValue()).length()!=0)
            {
            try {    
            PDThesaur TmpThes=new PDThesaur(SParent.getSessOPD(Req));
            TmpThes.Load((String)Attr.getValue());
            Row.append("\"").append(TmpThes.getName()).append("\";");
            } catch(Exception ex)
                {    
                Row.append("\"\";");
                }
            }
        else
            Row.append("\"\";");
        }
    else 
        {
        Row.append("\"").append(Attr.Export()).append("\";");
        }
    Attr=NextRec.nextAttr();
    }
Row.deleteCharAt(Row.length()-1);
return(Row);
}
//----------------------------------------------------------------
/**
 *
 * @param Req
 * @param FType
 * @param Cond
 * @param SubT
 * @param SubF
 * @param Vers
 * @param actFolderId
 * @param Ord
 * @param Rec
 */
protected void SaveConds(HttpServletRequest Req, String QueryType, String FType, Conditions Cond, boolean SubT, boolean SubF, boolean Vers, String actFolderId, Vector Ord, Record Rec, String FTQuery)
{
HttpSession Sess=Req.getSession(true);
Sess.setAttribute(SParent.SD_QType, QueryType);
Sess.setAttribute(SParent.SD_FType, FType);
Sess.setAttribute(SParent.SD_Cond, Cond);
Sess.setAttribute(SParent.SD_SubT, SubT);
Sess.setAttribute(SParent.SD_SubF, SubF);
Sess.setAttribute(SParent.SD_Vers, Vers);
Sess.setAttribute(SParent.SD_actFolderId, actFolderId);
Sess.setAttribute(SParent.SD_Ord, Ord);
Sess.setAttribute(SParent.SD_Rec, Rec);
Sess.setAttribute(SParent.SD_FTQ, FTQuery);
}
//-----------------------------------------------------------------------------------------------
public static String GenErrorForm(String localizedMessage)
{
return("[{type: \"label\", label: \"Error\"},\n" +
"    {type: \"label\", label: \""+localizedMessage+"\"},\n" +
"    {type: \"button\", name: \"CANCEL\", value: \"CANCEL\"}];");
}
//-----------------------------------------------------------------------------------------------
public static void StoreDat(HttpServletRequest Req, HashMap<String, String> ListFields)
{
Req.getSession().setAttribute("PENDING_DATA", ListFields); 
}
//-----------------------------------------------------------------------------------------------
public static HashMap<String, String> GetDat(HttpServletRequest Req )
{
return((HashMap)Req.getSession().getAttribute("PENDING_DATA")); 
}
//-----------------------------------------------------------------------------------------------
public static void PrepareError(HttpServletRequest Req, String Msg, PrintWriter out)
{
String[] ListMsg = Msg.split(":");
for (int i = 0; i < ListMsg.length; i++)
    out.println(TT(Req, ListMsg[i]));       
}
//-----------------------------------------------------------------------------------------------
protected static void setOPACConf(HttpServletRequest Req, ExtConf ConfOPAC)
{
Req.getSession().setAttribute("OPAC_CONF", ConfOPAC); 
}
//-----------------------------------------------------------------------------------------------
protected static ExtConf getOPACConf(HttpServletRequest Req)
{
return((ExtConf)Req.getSession().getAttribute("OPAC_CONF")); 
}
//-----------------------------------------------------------------------------------------------
protected static String EscapeHtmlJson(String Text)
{
if (Text==null)  
    return(Text);
return(Text.replace("\"", "\\\"")); 
//return(Text.replace("\"", "&quot;")); 
}
//-----------------------------------------------------------------------------------------------
protected static String EscapeTree(String Text)
{
if (Text==null)  
    return(Text);
return(Text.replace("\"", "&quot;")); 
// return(Text.replace("\"", "&quot;").replace("<", "&lt;")); 
}
//-----------------------------------------------------------------------------------------------
/**
 * @return the ListAttrFold
 */
public synchronized static HashSet getListAttrFold()
{
if (ListAttrFold==null)
    {
    ListAttrFold=new HashSet(7);
    ListAttrFold.add(PDFolders.fACL);
    ListAttrFold.add(PDFolders.fFOLDTYPE);
    ListAttrFold.add(PDFolders.fPARENTID);
    ListAttrFold.add(PDFolders.fPDID);
    ListAttrFold.add(PDFolders.fTITLE);
    ListAttrFold.add(PDFolders.fPDAUTOR);
    ListAttrFold.add(PDFolders.fPDDATE);
    }
return ListAttrFold;
}

/**
 * @return the ListAttrDoc
 */
public synchronized static HashSet getListAttrDoc()
{
if (ListAttrDoc==null)    
    {
    ListAttrDoc=new HashSet(15);
    ListAttrDoc.add(PDDocs.fACL);
    ListAttrDoc.add(PDDocs.fDOCTYPE);
    ListAttrDoc.add(PDDocs.fPARENTID);
    ListAttrDoc.add(PDDocs.fPDID);
    ListAttrDoc.add(PDDocs.fTITLE);
    ListAttrDoc.add(PDDocs.fPDID);
    ListAttrDoc.add(PDDocs.fPDAUTOR);
    ListAttrDoc.add(PDDocs.fPDDATE);
    ListAttrDoc.add(PDDocs.fLOCKEDBY);
    ListAttrDoc.add(PDDocs.fVERSION);
    ListAttrDoc.add(PDDocs.fPURGEDATE);
    ListAttrDoc.add(PDDocs.fREPOSIT);
    ListAttrDoc.add(PDDocs.fSTATUS);
    ListAttrDoc.add(PDDocs.fMIMETYPE);
    ListAttrDoc.add(PDDocs.fNAME);
    }
return ListAttrDoc;
}

}