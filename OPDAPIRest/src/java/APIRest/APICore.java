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
 * author: Joaquin Hierro      2019
 * 
 */
package APIRest;

import APIRest.beans.CurrentSession;
import APIRest.beans.Rec;
import APIRest.beans.User;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDLog;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhier
 */
public class APICore
{
private static boolean Started=false;
protected static String ProdocProperRef=null;

private final static Hashtable<String, CurrentSession> ListOPSess=new Hashtable();

public static final String OK="OK";  
public static final String SESSTOK="Token";

//--------------------------------------------------------------------------
protected Response returnOK(String Msg)
{
return(Response.ok("{\"Res\":\"OK\",\"Msg\":\""+(Msg!=null?Msg:"")+"\"}").build());    
}
//--------------------------------------------------------------------------
protected Response returnErrorInput(String Msg)
{
return(Response.status(Response.Status.BAD_REQUEST).entity("{\"Res\":\"KO\",\"Msg\":\""+(Msg!=null?Msg:"")+"\"}").build());    
}
//--------------------------------------------------------------------------
protected Response returnErrorInternal(String Msg)
{
return(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"Res\":\"KO\",\"Msg\":\""+(Msg!=null?Msg:"")+"\"}").build());    
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
protected String CanCreateSess(User U, HttpServletRequest Req)
{
if (!Started) 
    StartFramework();
try {
DriverGeneric D=ProdocFW.getSession("PD", U.getName(), U.getPassword());
String AuthGenJWT = D.AuthGenJWT(U.getName(), U.getPassword());
CurrentSession CS=new CurrentSession(D.getUser().getName(), new Date(), Req.getRemoteHost(), D);
getListOPSess().put(AuthGenJWT, CS);
return(AuthGenJWT);
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    PDLog.Error(Ex.getLocalizedMessage());
    return(null);
    }
}
//--------------------------------------------------------------------------
static final private String TOKPREF="Bearer ";
protected DriverGeneric IsConnected(HttpServletRequest Req)
{
if (!Started) 
    StartFramework();    
String Tok=ExtractTok(Req);
if (Tok!=null &&getListOPSess().get(Tok)!=null && getListOPSess().get(Tok).getDrv().IsConnected())
    {
    getListOPSess().get(Tok).setLastUse(new Date());
    return(getListOPSess().get(Tok).getDrv());
    }
try {
DriverGeneric D=ProdocFW.getSession("PD", Tok, Tok);
CurrentSession CS=new CurrentSession(D.getUser().getName(), new Date(), Req.getRemoteHost(), D);
getListOPSess().put(Tok, CS);
return(D);
} catch (Exception Ex)
    {
    PDLog.Error(Ex.getLocalizedMessage());
    return(null);
    }
}
//--------------------------------------------------------------------------
Response CloseSession(HttpServletRequest request)
{  
String Tok=ExtractTok(request);
if (Tok!=null)    
    {
    try {    
    ProdocFW.freeSesion("PD", getListOPSess().get(Tok).getDrv());
    } catch (Exception Ex)
        {
        PDLog.Error(Ex.getLocalizedMessage());
        }
    getListOPSess().remove(Tok);
    }
return(returnOK("Closed"));
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
SessCleaner  SC=new SessCleaner(); 
SC.start();
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
 * @return the ListOPSess
 */
public static Hashtable<String, CurrentSession> getListOPSess()
{
return ListOPSess;
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

protected String genCursor(DriverGeneric sessOPD, Cursor SearchFold, int Initial, int Final) throws PDException
{
try {
ArrayList<Rec> L=new ArrayList();
int N=0;
Record NextFold=sessOPD.NextRec(SearchFold);
while (NextFold!=null)
    {
    if (N>=Initial) 
        {
        Rec r=new Rec(NextFold);
        L.add(r);
        }
    if (++N>=Final)
        break;
    NextFold=sessOPD.NextRec(SearchFold);
    }
Gson g = new Gson();
return g.toJson(L);    
} finally
    {
    sessOPD.CloseCursor(SearchFold);    
    }
}
//-------------------------------------------------------------------------
protected boolean Valid(String Param)
{
return(Param!=null && Param.length()>0);    
}
//-------------------------------------------------------------------------
protected Response ErrorParam(String Param)
{
return(returnErrorInput("Empty Param:"+Param));    
}
//-------------------------------------------------------------------------

private String ExtractTok(HttpServletRequest request)
{
Enumeration<String> headers = request.getHeaders("Authorization");
while (headers.hasMoreElements()) 
    {
    String Tok = headers.nextElement();
    if (Tok.startsWith(TOKPREF))
        {
        Tok=Tok.substring(TOKPREF.length());
        return(Tok);
        }
    }
return(null);
}
//-------------------------------------------------------------------------
//***********************************************************************
static private class SessCleaner extends Thread  
{
static private final long TIMEOUT=5*60*1000;   

public SessCleaner()
{
super();
setName("SessCleaner");
}
/**
 * 
 */
@Override 
public void run() 
{
while (true)
    {    
    Hashtable<String, CurrentSession> listOPSess = getListOPSess();
    for (Map.Entry<String, CurrentSession> entry : listOPSess.entrySet())
        {
        CurrentSession CS = entry.getValue();
        if ((System.currentTimeMillis()-CS.getLastUse().getTime())>TIMEOUT)  
            {
            try{
            ProdocFW.freeSesion("PD", CS.getDrv());
            } catch (Exception Ex)
                {
                PDLog.Error(Ex.getLocalizedMessage());
                }
            getListOPSess().remove(entry.getKey());
            if (PDLog.isDebug())
                PDLog.Debug("Autodesconnect:"+CS.getHost()+"/"+CS.getUserName());
            }
        }
        try {
    Thread.sleep(TIMEOUT);
    } catch (InterruptedException e) 
        {
        }

    }
}
//-------------------------------------------------------------------------
}
//***********************************************************************
}
