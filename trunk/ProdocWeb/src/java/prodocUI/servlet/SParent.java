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
 * author: Joaquin Hierro      2011
 * 
 */

package prodocUI.servlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;
import prodoc.Attribute;
import prodoc.Condition;
import prodoc.Conditions;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.ProdocFW;
import prodoc.Record;
import prodocUI.forms.FListDocs;
import prodocUI.forms.FListTerm;
import prodocUI.forms.FLogin;
import prodocUI.forms.FMessage;
import prodocUI.forms.FSearchDocAdv;
import prodocUI.forms.FSearchFoldAdv;



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
public final static int SEARCHFOLD_FORM=3;


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
HttpSession Sess=Req.getSession(true);
DriverGeneric D=getSessOPD(Req);
if (D!=null)
    {
    return(true);
    }
FLogin f=new FLogin(Req, "Login Prodoc", "");
String Nombre;
String Clave;
// nos estamos validando en login
Nombre=f.NomUsu.getValue(Req);
Clave=f.Clave.getValue(Req);
if (Nombre==null || Nombre.length()==0)
    return(false);
if (Clave==null || Clave.length()==0)
    return(false);
ProdocFW.InitProdoc("PD", getProdocProperRef());
D=ProdocFW.getSession("PD", Nombre, Clave);
Sess.setAttribute("PRODOC_SESS", D);
return(true);
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
FLogin f=new FLogin(Req, "Login Prodoc", "");
out.println(f.ToHtml(Sess));
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
FMessage f=new FMessage(Req, "Mensaje Prodoc", "", Message);
out.println(f.ToHtml(Sess));
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
    FMessage f = new FMessage(Req, "Mensaje Prodoc", "", Message);
    out.println(f.ToHtml(Sess));
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
throw new PDException("Página Inexistente");
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
 * @return
 */
static public String getUrlServlet()
{
return("SParent");
}
//----------------------------------------------------------
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
SimpleDateFormat formatterTS;
try {
    formatterTS = new SimpleDateFormat(getSessOPD(Req).getPDCust().getTimeFormat());
} catch (PDException ex)
    {
    formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
return formatterTS;
}
//---------------------------------------------------------------------
/**
 * @param Req 
 * @return the formatterDate
 */
public static SimpleDateFormat getFormatterDate(HttpServletRequest Req)
{
SimpleDateFormat formatterDate;
try {
    formatterDate = new SimpleDateFormat(getSessOPD(Req).getPDCust().getDateFormat());
} catch (PDException ex)
    {
    formatterDate = new SimpleDateFormat("yyyy-MM-dd");
    }
return formatterDate;
}
//---------------------------------------------------------------------
/**
 * @param Req 
 * @return the formatterDate
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
else if (Attr.getType()==Attribute.tSTRING)
    {
    Attr.setValue(Val);
    }
else if (Attr.getType()==Attribute.tDATE)
    {
    if (Val.length()>0)
        Attr.setValue(getFormatterDate(Req).parse(Val));
    }
else if (Attr.getType()==Attribute.tTIMESTAMP)
    {
    if (Val.length()>0)
        Attr.setValue(getFormatterTS(Req).parse(Val));
    }
//else if (Attr.getType()==Attribute.tBOOLEAN)
//    {
//    Boolean Act;
//    if (((JCheckBox)JTF).isSelected())
//        Act=new Boolean(true);
//    else
//        Act=new Boolean(false);
//    Attr.setValue(Act);
//    }
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
Condition Cond = null;
try {
if (Attr.getType()==Attribute.tSTRING)
    {
    Cond=new Condition(Attr.getName(), Condition.cEQUAL, Val);
    Attr.setValue(Val);
    }
else if (Attr.getType()==Attribute.tDATE)
    {
    Cond=new Condition(Attr.getName(), Condition.cEQUAL, getFormatterDate(Req).parse(Val));
    Attr.setValue(getFormatterDate(Req).parse(Val));
    }
else if (Attr.getType()==Attribute.tTIMESTAMP)
    {
    Cond=new Condition(Attr.getName(), Condition.cEQUAL, getFormatterTS(Req).parse(Val));
    Attr.setValue(getFormatterTS(Req).parse(Val));
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
//--------------------------------------------------------------
/**
 * Evaluates y generates corrected form
 * @param Req Servlet request
 * @param out 
 * @param Form  one of the *_FORM constants
 * @param OnLoadJS
 * @param Results
 * @throws PDException 
 */
public static void GenListForm(HttpServletRequest Req, PrintWriter out, int Form, String OnLoadJS, Cursor Results) throws PDException
{
HttpSession Sess=Req.getSession(true);
Integer Last=(Integer)Sess.getAttribute("LastForm");
int LastForm;
if (Last!=null)
    LastForm=Last;
else
    LastForm=LISTDOC_FORM;
if (Form==SEARCHDOC_FORM || Form==LAST_FORM && LastForm==SEARCHDOC_FORM)
    {
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    PDDocs F = new PDDocs(PDSession);
    Record Rec=F.getRecord();
    if (Results==null)
        {
        String FType=(String)Sess.getAttribute("SD_FType");
        if (FType!=null)
            {
            Conditions Cond=(Conditions)Sess.getAttribute("SD_Cond");
            boolean SubT=(Boolean) Sess.getAttribute("SD_SubT");
            boolean SubF=(Boolean) Sess.getAttribute("SD_SubF");
            boolean Vers=(Boolean) Sess.getAttribute("SD_Vers");
            String actFolderId=(String) Sess.getAttribute("SD_actFolderId");
            Vector Ord=(Vector)Sess.getAttribute("SD_Ord");
            Results=F.Search(FType, Cond, SubT, SubF, Vers, actFolderId, Ord);
            }
        } 
    FSearchDocAdv f=new FSearchDocAdv(Req, FSearchDocAdv.ADDMOD, Rec, SearchDoc.getUrlServlet(), Results);
    out.println(f.ToHtml(Req.getSession()));
    Sess.setAttribute("LastForm", SEARCHDOC_FORM);
    return;
    }
if (Form==SEARCHFOLD_FORM || Form==LAST_FORM && LastForm==SEARCHFOLD_FORM)
    {
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    PDFolders F = new PDFolders(PDSession);
    Record Rec=F.getRecord();
    if (Results==null)
        {
        String FType=(String)Sess.getAttribute("SD_FType");
        if (FType!=null)
            {
            Conditions Cond=(Conditions)Sess.getAttribute("SD_Cond");
            boolean SubT=(Boolean) Sess.getAttribute("SD_SubT");
            boolean SubF=(Boolean) Sess.getAttribute("SD_SubF");
            String actFolderId=(String) Sess.getAttribute("SD_actFolderId");
            Vector Ord=(Vector)Sess.getAttribute("SD_Ord");
            Results=F.Search(FType, Cond, SubT, SubF, actFolderId, Ord);
            }
        } 
    FSearchFoldAdv f=new FSearchFoldAdv(Req, FSearchFoldAdv.ADDMOD, Rec, SearchFold.getUrlServlet(), Results);
    out.println(f.ToHtml(Req.getSession()));
    Sess.setAttribute("LastForm", SEARCHFOLD_FORM);
    return;
    }
String CarpId=Req.getParameter("FoldId");
if (CarpId==null)
    {
    CarpId=SParent.getActFolderId(Req);
    if (CarpId==null)
        CarpId=PDFolders.ROOTFOLDER;
    }
SParent.setActFoldId(Req, CarpId);
FListDocs fm=new FListDocs(Req, CarpId);
if (OnLoadJS!=null && OnLoadJS.length()>0)
    fm.AddOnLoad(OnLoadJS);
out.println(fm.ToHtml(Sess));
Sess.setAttribute("LastForm", LISTDOC_FORM);
}
//--------------------------------------------------------------
public static void GenListThes(HttpServletRequest Req, PrintWriter out, int Form, String OnLoadJS, Cursor Results) throws PDException
{
HttpSession Sess=Req.getSession(true);
Integer Last=(Integer)Sess.getAttribute("ThesLastForm");
int LastForm;
if (Last!=null)
    LastForm=Last;
else
    LastForm=LISTDOC_FORM;
if (Form==SEARCHFOLD_FORM || Form==LAST_FORM && LastForm==SEARCHFOLD_FORM)
    {
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    PDFolders F = new PDFolders(PDSession);
    Record Rec=F.getRecord();
    if (Results==null)
        {
        String FType=(String)Sess.getAttribute("SD_FType");
        if (FType!=null)
            {
            Conditions Cond=(Conditions)Sess.getAttribute("SD_Cond");
            boolean SubT=(Boolean) Sess.getAttribute("SD_SubT");
            boolean SubF=(Boolean) Sess.getAttribute("SD_SubF");
            String actFolderId=(String) Sess.getAttribute("SD_actFolderId");
            Vector Ord=(Vector)Sess.getAttribute("SD_Ord");
            Results=F.Search(FType, Cond, SubT, SubF, actFolderId, Ord);
            }
        } 
    FSearchFoldAdv f=new FSearchFoldAdv(Req, FSearchFoldAdv.ADDMOD, Rec, SearchFold.getUrlServlet(), Results);
    out.println(f.ToHtml(Req.getSession()));
    Sess.setAttribute("ThesLastForm", SEARCHFOLD_FORM);
    return;
    }
String TermId=Req.getParameter("ThesId");
if (TermId==null)
    {
    TermId=SParent.getActTermId(Req);
    if (TermId==null)
        TermId=PDThesaur.ROOTTERM;
    }
SParent.setActTermId(Req, TermId);
FListTerm fm=new FListTerm(Req, TermId);
if (OnLoadJS!=null && OnLoadJS.length()>0)
    fm.AddOnLoad(OnLoadJS);
out.println(fm.ToHtml(Sess));
Sess.setAttribute("ThesLastForm", LISTDOC_FORM);
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 */
protected void CleanConds(HttpServletRequest Req)
{
HttpSession Sess=Req.getSession(true);
Sess.setAttribute("SD_FType", null);
Sess.setAttribute("SD_Cond", null);
Sess.setAttribute("SD_SubT", null);
Sess.setAttribute("SD_SubF", null);
Sess.setAttribute("SD_Vers", null);
Sess.setAttribute("SD_actFolderId", null);
Sess.setAttribute("SD_Ord", null);
Sess.setAttribute("SD_Rec", null);
}
//-----------------------------------------------------------------------------------------------
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
protected void SaveConds(HttpServletRequest Req, String FType, Conditions Cond, boolean SubT, boolean SubF, boolean Vers, String actFolderId, Vector Ord, Record Rec)
{
HttpSession Sess=Req.getSession(true);
Sess.setAttribute("SD_FType", FType);
Sess.setAttribute("SD_Cond", Cond);
Sess.setAttribute("SD_SubT", SubT);
Sess.setAttribute("SD_SubF", SubF);
Sess.setAttribute("SD_Vers", Vers);
Sess.setAttribute("SD_actFolderId", actFolderId);
Sess.setAttribute("SD_Ord", Ord);
Sess.setAttribute("SD_Rec", Rec);
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @return
 */
static public String getVersion()
{
return("0.7");
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
protected static String getProdocProperRef() throws Exception
{
if (ProdocProperRef==null)
    {
//   String Path=System.getProperty("java.class.path");
//   System.out.println("java.class.path="+Path);
//    InputStream Is=ClassLoader.getSystemResourceAsStream("OPDWeb.properties"); 
//    InputStream Is=SParent.class.getResourceAsStream("OPDWeb.properties");
//    String Sep=System.getProperty("path.separator");
        
//    String PropName=System.getProperty("OPDWeb_properties");   
//    System.out.println("OPDWeb_properties="+PropName);
    String Path=System.getProperty("user.home");    
    InputStream Is  = new FileInputStream(Path+File.separator+"OPDWeb.properties");
    Properties p= new Properties();
    p.load(Is);
    Is.close();
    ProdocProperRef=p.getProperty("OPDConfig");
    //ProdocProperRef="e:\\Prodoc\\Varios\\Prodoc_derby.properties";
    //ProdocProperRef="/media/Iomega/Prodoc/Varios/Prodoc_derby.properties";
    }
return(ProdocProperRef);
}
//----------------------------------------------------------        
}

