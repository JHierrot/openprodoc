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

package prodoc;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author jhierrot
 */
public class Conector
{
/*
 * Conector's name, used also in hash
 */
private String ConectorName;
/**
 *
 */
private String User;
/**
 *
 */
private String Password;
/**
 *
 */
private String URL;
/**
 *
 */
private String PARAM;
/**
 *
 */
private int MinPoolSize;
/**
 *
 */
private int MaxPoolSize;
/**
 *
 */
private int TimeOutPool;
/**
 *
 */
private String DataAccessType;
/**
 *
 */
private Vector ListSesion;
/**
 *
 */
private int TaskSearchFreq=0;

private int TaskExecFreq=0;

static private Hashtable TaskSearchList=new Hashtable();
static private Hashtable TaskExecList=new Hashtable();  
//--------------------------------------------------------------------------
/**
 * reads, interpret and store the requiered elements of the configuration
 * @param ProdocProperties  prpoerties readed from configuration file
 */
Conector(String pConectorName, Properties ProdocProperties) throws PDException
{
ConectorName=pConectorName;
DataAccessType=ProdocProperties.getProperty(ConectorName+".DATA_TYPE");
MinPoolSize=new Integer(ProdocProperties.getProperty(ConectorName+".DATA_MIN")).intValue();
MaxPoolSize=new Integer(ProdocProperties.getProperty(ConectorName+".DATA_MAX")).intValue();
TimeOutPool=new Integer(ProdocProperties.getProperty(ConectorName+".DATA_TIMEOUT")).intValue();
User=ProdocProperties.getProperty(ConectorName+".DATA_USER");
Password=ProdocProperties.getProperty(ConectorName+".DATA_PASSWORD");
URL=ProdocProperties.getProperty(ConectorName+".DATA_URL");
PARAM=ProdocProperties.getProperty(ConectorName+".DATA_PARAM");
int LogLevel=new Integer(ProdocProperties.getProperty("TRACELEVEL")).intValue();
PDLog.setLevel(LogLevel);
String LogProp=ProdocProperties.getProperty("TRACECONF");
if (LogProp==null || LogProp.length()==0)
    PDLog.setPropFile("log4j.properties");
else
    PDLog.setPropFile(LogProp);
String Temp=ProdocProperties.getProperty(ConectorName+".TaskSearchFreq");
if (Temp!=null)
    TaskSearchFreq=new Integer(Temp);
Temp=ProdocProperties.getProperty(ConectorName+".TaskExecFreq");
if (Temp!=null)
    TaskExecFreq=new Integer(Temp);
ListSesion=new Vector();
for (int i = 0; i < MinPoolSize; i++)
    {
    ListSesion.add(CreateSesion());
    }
CreateTask();
}
//--------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
private DriverGeneric CreateSesion()  throws PDException
{
DriverGeneric NewSesion=null;
if (DataAccessType.equals("JDBC"))
   NewSesion=new DriverJDBC(URL,PARAM, User, Password);
else if (DataAccessType.equals("Remote"))
   NewSesion=new DriverRemote(URL,PARAM, User, Password);
else
    PDException.GenPDException("Connection_type_unsupported", DataAccessType);
return(NewSesion);
}
//--------------------------------------------------------------------------
/**
 * returns a session from the pool
 * adding a new session if all the session are Locked
 * @param user        name of the Prodoc to connect
 * @param Password    password to connect
 * @return A locked session with the desired user conected
 * @throws PDException when we have the maximum (MaxPoolSize) of sessions
 */
public synchronized DriverGeneric getSession(String user, String Password) throws PDException
{
if(PDLog.isDebug())
    PDLog.Debug("Obtainning_Sessio");
DriverGeneric Session;
for (int i = 0; i < ListSesion.size(); i++)
    {
    Session =(DriverGeneric) ListSesion.elementAt(i);
    if (!Session.isLocked())
       {
       Session.Lock();
       Session.Assign(user, Password);
       return(Session);
       }
    }
if (ListSesion.size()<MaxPoolSize)
    {
    Session=CreateSesion();
    Session.Lock();
    Session.Assign(user, Password);
    ListSesion.add(Session);
    return(Session);
    }
PDException.GenPDException("MaxPoolSize_reached", null);
return(null);
}
//--------------------------------------------------------------------------
/**
 *
 * @param Session
 * @throws PDException
 */
public void freeSesion(DriverGeneric Session)  throws PDException
{
if(PDLog.isDebug())
    PDLog.Debug("unlocking_session");
Session.UnLock();
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
public void Shutdown() throws PDException
{
DriverGeneric Session;
if (PDLog.isDebug())
    PDLog.Debug("closing_sessions_of"+":"+this.ConectorName);
DestroyTask();
for (int i = 0; i < ListSesion.size(); i++)
    {
     Session = (DriverGeneric) ListSesion.elementAt(i);
     Session.delete();
    }
}
//--------------------------------------------------------------------------
/**
 * Starts the search and execute tasks
 */
private void CreateTask()
{
if (TaskSearchFreq!=0)   
    Conector.CreateSearchTask(TaskSearchFreq, ConectorName );
if (TaskExecFreq!=0)
    Conector.CreateExecTask(TaskExecFreq, ConectorName );
}
//--------------------------------------------------------------------------
/**
 * Stops the search and execute tasks
 */
private void DestroyTask()
{
if (TaskSearchFreq!=0)    
    Conector.DestroySearchTask(ConectorName);
if (TaskExecFreq!=0)
    Conector.DestroyExecTask(ConectorName);   
}
//--------------------------------------------------------------------------
/**
 * 
 * @param TaskSearchFreq
 * @param ConectorName 
 */
private static void CreateSearchTask(int TaskSearchFreq, String ConectorName)
{
throw new UnsupportedOperationException("Not yet implemented");
}
//--------------------------------------------------------------------------
/**
 * 
 * @param TaskExecFreq
 * @param ConectorName 
 */
private static void CreateExecTask(int TaskExecFreq, String ConectorName)
{
throw new UnsupportedOperationException("Not yet implemented");
}
//--------------------------------------------------------------------------
/**
 * 
 * @param ConectorName 
 */
private static void DestroySearchTask(String ConectorName)
{
throw new UnsupportedOperationException("Not yet implemented");
}
//--------------------------------------------------------------------------
/**
 * 
 * @param ConectorName 
 */
private static void DestroyExecTask(String ConectorName)
{
throw new UnsupportedOperationException("Not yet implemented");
}
//--------------------------------------------------------------------------
}
