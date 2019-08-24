/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIRest;

import APIRest.beans.CurrentSession;
import APIRest.beans.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.ProdocFW;

/**
 *
 * @author jhier
 */
public class APICore
{
private static boolean Started=false;
protected static String ProdocProperRef=null;

private final static Hashtable<String, CurrentSession> ListOPSess=new Hashtable();
public final static String PRODOC_SESS="PRODOC_SESS";
public final static String PRODOC_SESSID="PRODOC_SESSID";


public static final String OK="OK";  
public static final String SESSTOK="Token";

//--------------------------------------------------------------------------
protected Response returnOK(String Msg)
{
return(Response.ok("{\"Res\":\"OK\",\"Msg\":\""+(Msg!=null?Msg:"")+"\"}").build());    
}
//--------------------------------------------------------------------------
protected Response returnERROR(String Msg)
{
return(Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Res\":\"KO\",\"Msg\":\""+(Msg!=null?Msg:"")+"\"}").build());    
}
//--------------------------------------------------------------------------
protected Response NewOKSesion(String Token)
{
return(Response.ok("{\"Res\":\"OK\",\"Token\":\""+(Token!=null?Token:"")+"\"}").build());    
}
//--------------------------------------------------------------------------
protected Response returnUnathorize()
{
return(Response.status(Response.Status.UNAUTHORIZED).entity("{\"Res\":\"KO\",\"Msg\":\"Unauthorized\"}").build());    
}
//--------------------------------------------------------------------------
protected String CanCreateSess(String Credentials, HttpServletRequest request)
{
if (!Started) 
    StartFramework();
User U=User.CreateUser(Credentials);
if (U.getName()==null||U.getName().length()==0 || U.getPassword()==null||U.getPassword().length()==0)
    return(null);
String Token=U.getName()+"|"+U.getPassword();
try {
DriverGeneric D=ProdocFW.getSession("PD", U.getName(), U.getPassword());
setSessOPD(request, D);
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(null);
    }
return(Token);
}
//--------------------------------------------------------------------------
protected boolean IsConnected(HttpServletRequest request)
{
DriverGeneric sessOPD = getSessOPD(request);
return(sessOPD!=null);
}
//--------------------------------------------------------------------------
Response CloseSession(HttpServletRequest request)
{
if (IsConnected(request))  
    {
    request.getSession(true).setAttribute(SESSTOK, null);
    }
return(returnOK("Closed"));
}
//--------------------------------------------------------------------------
protected boolean isLogDebug()
{
return(true);    
}
//--------------------------------------------------------------------------
protected void Debug(String Txt)
{
System.out.println("DEBUG:"+Txt);    
}
//--------------------------------------------------------------------------
protected boolean isLogInfo()
{
return(true);    
}
//--------------------------------------------------------------------------
protected void Info(String Txt)
{
System.out.println("INFO :"+Txt);    
}
//--------------------------------------------------------------------------
protected boolean isLogError()
{
return(true);    
}
//--------------------------------------------------------------------------
protected void Error(String Txt)
{
System.out.println("ERROR:"+Txt);    
}
//--------------------------------------------------------------------------
static synchronized private void StartFramework()
{
if (Started)   
    return;
try {
ProdocFW.InitProdoc("PD", getProdocProperRef());    
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
Started=true;
}
//--------------------------------------------------------------------------
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
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @param OPDSess
 */
public static void setSessOPD(HttpServletRequest Req, DriverGeneric OPDSess) throws PDException
{
CurrentSession CS=new CurrentSession(OPDSess.getUser().getName(), new Date(), Req.getRemoteHost());
String SesId=Long.toHexString(System.currentTimeMillis());
getListOPSess().put(SesId, CS);
Req.getSession(true).setAttribute(PRODOC_SESSID, SesId);
Req.getSession().setAttribute(PRODOC_SESS, OPDSess);

}
//-----------------------------------------------------------------------------------------------
/**
 * @return the ListOPSess
 */
protected static Hashtable<String, CurrentSession> getListOPSess()
{
return ListOPSess;
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @return
 */
public static DriverGeneric getSessOPD(HttpServletRequest Req)
{
try {    
String SesId=(String)Req.getSession().getAttribute(PRODOC_SESSID);
getListOPSess().get(SesId).setLastUse(new Date());    
} catch (Exception Ex){}
return (DriverGeneric)Req.getSession(true).getAttribute(PRODOC_SESS);
}
//--------------------------------------------------------------
SimpleDateFormat formatterTS = new SimpleDateFormat();
static final String DATEFORMAT="yyyy-MM-dd";
static final String TSFORMAT="yyyy-MM-dd HH:mm:ss";

static public Date Str2Date(String D) throws ParseException
{
SimpleDateFormat formatterDate = new SimpleDateFormat(DATEFORMAT);
return(formatterDate.parse(D));
}
static public String Date2Str(Date D)
{
SimpleDateFormat formatterDate = new SimpleDateFormat(DATEFORMAT);
return(formatterDate.format(D));    
}
static public Date Str2TS(String D) throws ParseException
{
SimpleDateFormat formatterDate = new SimpleDateFormat(TSFORMAT);
return(formatterDate.parse(D));
}
static public String TS2Str(Date D)
{
SimpleDateFormat formatterDate = new SimpleDateFormat(TSFORMAT);
return(formatterDate.format(D));    
}
}
