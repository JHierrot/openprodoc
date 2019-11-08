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
package prodoc;

import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import prodoc.security.AuthGeneric;
/**
 * Class connector responsible of reading configuration, starting tasks 
 * and managing sessions to an specific OpenProdoc Repository
 * @author jhierrot
 */
public class Conector
{
/*
 * Conector's name, used also in hash
 */
private String ConectorName;
/**
 * User of the connection to the Database of the repository
 */
private String User;
/**
 * Password of the connection to the Database of the repository
 */
private String Password;
/**
 * Url of the database of the repository
 */
private String URL;
/**
 * Additional params
 */
private String PARAM;
/**
 * Min size of the database pool
 */
private int MinPoolSize;
/**
 * MAX size of the database pool
 */
private int MaxPoolSize;
/**
 * pool timeout
 */
private int TimeOutPool;
/**
 * Kind of the connector (JDBC or REMOTE currently)
 */
private String DataAccessType;
/**
 * Collection containing the current sessions
 */
private Vector<DriverGeneric> ListSesion;
/**
 * Frecuency of pooling the creation of tasks (in ms)
 */
private int TaskSearchFreq=0;
/**
 * Frecuency of pooling the execution of pending tasks (in ms)
 */
private int TaskExecFreq=0;
/**
 * Contains a collection of TaskCreator identified by Connector name
 */
private static final Hashtable<String, TaskCreator> TaskSearchList=new Hashtable();
/**
 * Contains a collection of TaskRunner identified by Connector name
 */
private static final Hashtable<String, TaskRunner> TaskExecList=new Hashtable(); 
/**
 * Indicates the category of task to be created/executed in the cuurrent node
 */
private String TaskCategory="*";
/**
 * Indicates if the tasks for the connector has beeen started
 */
private boolean TasksStarted=false;
private boolean EndShutdown=false;
//--------------------------------------------------------------------------
/**
 * Reads, interpret and store the requiered elements of the configuration
 * @param ProdocProperties  prpoerties readed from configuration file
 */
Conector(String pConectorName, Properties ProdocProperties) throws PDException
{
ConectorName=pConectorName;
DataAccessType=ProdocProperties.getProperty(ConectorName+".DATA_TYPE").trim();
MinPoolSize=Integer.parseInt(ProdocProperties.getProperty(ConectorName+".DATA_MIN"));
MaxPoolSize=Integer.parseInt(ProdocProperties.getProperty(ConectorName+".DATA_MAX"));
TimeOutPool=Integer.parseInt(ProdocProperties.getProperty(ConectorName+".DATA_TIMEOUT"));
User=ProdocProperties.getProperty(ConectorName+".DATA_USER").trim();
Password=ProdocProperties.getProperty(ConectorName+".DATA_PASSWORD").trim();
URL=ProdocProperties.getProperty(ConectorName+".DATA_URL").trim();
PARAM=ProdocProperties.getProperty(ConectorName+".DATA_PARAM").trim();
int LogLevel=Integer.parseInt(ProdocProperties.getProperty("TRACELEVEL"));
PDLog.setLevel(LogLevel);
String LogProp=ProdocProperties.getProperty("TRACECONF");
if (LogProp==null || LogProp.length()==0)
    PDLog.setPropFile("log4j.properties");
else
    PDLog.setPropFile(LogProp);
TaskCategory=ProdocProperties.getProperty(ConectorName+".TaskCategory");
if (TaskCategory!=null)
    TaskCategory=TaskCategory.trim();
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
if (!TasksStarted && !IsInstallMode())
    {    
    CreateTask();
    LoadAuth();
    }
}
//--------------------------------------------------------------------------
private void LoadAuth() throws PDException
{
DriverGeneric Session=CreateSesion();
Session.Lock();
Session.AssignTaskUser();   
PDAuthenticators Auth=new PDAuthenticators(Session);
Vector<Record> ListCustAuth = Auth.SearchSelectV("select * from this where AUTHTYPE='CUSTOM'");
for (int i = 0; i < ListCustAuth.size(); i++)
    {
    Auth.assignValues(ListCustAuth.elementAt(i));
    Session.ConstructAuthentic(Auth); // just in order to download binaries
    }
}
//--------------------------------------------------------------------------
/**
 * Creates a session based on configuration
 * @return a new session of type JDBC or remote with the parameters specified
 * @throws PDException in any error
 */
private DriverGeneric CreateSesion()  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("Conector.CreateSesion");
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
 * Returns a session from the pool
 * adding a new session if all the session are Locked
 * @param user        name of the Prodoc to connect
 * @param Password    password to connect
 * @return A locked session with the desired user conected
 * @throws PDException when we have the maximum (MaxPoolSize) of sessions
 */
public synchronized DriverGeneric getSession(String user, String Password) throws PDException
{
if(PDLog.isDebug())
    PDLog.Debug("Obtaining_Sessio");
DriverGeneric Session;
for (int i = 0; i < ListSesion.size(); i++)
    {
    Session = ListSesion.elementAt(i);
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
 * Frees a sesion locked previously in {@link #getSession(java.lang.String, java.lang.String) }
 * @param Session Session to be unlocked
 * @throws PDException in any error
 */
public synchronized void freeSesion(DriverGeneric Session)  throws PDException
{
if(PDLog.isDebug())
    PDLog.Debug("unlocking_session");
if (Session==null)
    PDException.GenPDException("Null_session_to_unlock", null);
if (!Session.isLocked())
    PDException.GenPDException("Unlocked_session_to_unlock", null);
Session.UnLock();
if (ListSesion.size()>MinPoolSize)
    {
    ListSesion.remove(Session);
    }
}
//--------------------------------------------------------------------------
/**
 * Stops all the tasks, sessions and framework of OpenProdoc
 * @throws PDException in any error
 */
public void Shutdown() throws PDException
{
if (EndShutdown) // to avoid double invocation
    return;
DriverGeneric Session;
if (PDLog.isDebug())
    PDLog.Debug("closing_sessions_of"+":"+this.ConectorName);
DestroyTask();
for (int i = 0; i < ListSesion.size(); i++)
    {
     Session = ListSesion.elementAt(i);
     Session.delete();
    }
EndShutdown=true;
}
//--------------------------------------------------------------------------
/**
 * Starts the search and execute tasks
 */
private void CreateTask()
{
if (PDLog.isDebug())
    PDLog.Debug("CreateTask >");        
if (TaskCategory==null || TaskCategory.length()==0)
    return;
if (TaskSearchFreq!=0)   
    CreateSearchTask(TaskSearchFreq, TaskCategory);
if (TaskExecFreq!=0)
    CreateExecTask(TaskExecFreq, TaskCategory);
TasksStarted=true;
if (PDLog.isDebug())
    PDLog.Debug("CreateTask <");        
}
//--------------------------------------------------------------------------
/**
 * Stops the search and execute tasks
 */
private void DestroyTask()
{
if (PDLog.isDebug())
    PDLog.Debug("DestroyTask >");        
if (TaskSearchFreq!=0)    
    {
    Conector.DestroySearchTask(ConectorName);
    TaskSearchFreq=0;
    }
if (TaskExecFreq!=0)
    {
    Conector.DestroyExecTask(ConectorName);  
    TaskExecFreq=0;
    }
if (PDLog.isDebug())
    PDLog.Debug("DestroyTask <");        
}
//--------------------------------------------------------------------------
/**
 * Creates and starts a thread for checking schedulled task to be instantiated
 * @param TaskSearchFreq Pooling frecuency (in ms)
 * @param TaskCategory Category of Tasks to pool and create
 */
synchronized private void CreateSearchTask(int TaskSearchFreq, String TaskCategory)
{
if (TaskSearchList.contains(ConectorName))
    return;
if (PDLog.isDebug())
    PDLog.Debug("CreateSearchTask: Cat="+TaskCategory+"Freq="+TaskSearchFreq);        
TaskCreator  TaskCreatorTask=new TaskCreator(TaskSearchFreq, TaskCategory, this); 
TaskCreatorTask.start();
TaskSearchList.put(ConectorName, TaskCreatorTask); 
}
//--------------------------------------------------------------------------
/**
 * Creates and starts a thread for executing pending task (schedulled or not trans assitiated to an event)
 * @param TaskExecFreq Pooling frecuency (in ms)
 * @param TaskCategory Category of Tasks to exetute
 */
synchronized private void CreateExecTask(int TaskExecFreq, String TaskCategory)
{
if (TaskExecList.contains(ConectorName))
    return;
if (PDLog.isDebug())
    PDLog.Debug("CreateExecTask: Cat="+TaskCategory+"Freq="+TaskExecFreq);        
TaskRunner  TaskRunnerTask=new TaskRunner(TaskExecFreq, TaskCategory, this); 
TaskRunnerTask.start();
TaskExecList.put(ConectorName, TaskRunnerTask); 
}
//--------------------------------------------------------------------------
/**
 * Stops the Search Task
 * @param ConectorName Name of Connector "Owner" of task
 */
synchronized private static void DestroySearchTask(String ConectorName)
{
if (!TaskSearchList.containsKey(ConectorName))
    return;
if (PDLog.isDebug())
    PDLog.Debug("DestroySearchTask: Con="+ConectorName);        
TaskCreator  TaskCreatorTask=TaskSearchList.get(ConectorName); 
TaskCreatorTask.End();
TaskSearchList.remove(ConectorName);
}
//--------------------------------------------------------------------------
/**
 * Stops the Search Task
 * @param ConectorName Name of Connector "Owner" of task
 */
synchronized private static void DestroyExecTask(String ConectorName)
{
if (!TaskExecList.containsKey(ConectorName))
    return;
if (PDLog.isDebug())
    PDLog.Debug("DestroyExecTask: Con="+ConectorName);        
TaskRunner  TaskRunnerTask=TaskExecList.get(ConectorName); 
TaskRunnerTask.End();
TaskExecList.remove(ConectorName);
}

private boolean IsInstallMode() throws PDException
{
DriverGeneric Session=CreateSesion();
Session.Lock();
boolean IsInstallMode=!Session.IsConnected();
Session.UnLock();
Session.delete();
return(IsInstallMode);
}
//--------------------------------------------------------------------------
//*******************************************************************
/**
 * Thread responsible of the actual pooling in definition and creation of schedulled tasks instances
 */
static private class TaskCreator extends Thread  
{
/**
 * Time for pooling the status and shutdown orders
 */
static private final long SleepTime=5000; 
/**
 * When false the task must stop
 */
private boolean Continue=true;
/**
 * Frecuency for pooling the time for creating Tasks
 */
private int TaskSearchFreq;
/**
 * Category of tasks to pool
 */
private String TaskCategory;
/**
 * Object for creating the tasks when it is the time for creating them
 */
PDTasksCron TaskGen;

//-------------------------------------------------
/**
 * Constructor
 * @param pTaskSearchFreq Frecuency of poolong in ms
 * @param pTaskCategory Category of tasks to pool
 * @param pCon Connector Used
 */
public TaskCreator(int pTaskSearchFreq, String pTaskCategory, Conector pCon)
{
setName("TaskCreator"+System.currentTimeMillis());
TaskSearchFreq=pTaskSearchFreq;   
TaskCategory=pTaskCategory;
//Con=pCon;
try {
DriverGeneric Session=pCon.CreateSesion();
Session.Lock();
Session.AssignTaskUser();
TaskGen=new PDTasksCron(Session);
} catch (Exception ex)
    {
    ex.printStackTrace();    
    PDLog.Error("TaskCreator error:"+ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("TaskCreator.TaskCreator:"+pTaskSearchFreq+"-"+pTaskCategory+"-"+pCon);
}
//-------------------------------------------------
/**
 * Signal the Thread to end in the next check of status (that is smaller than TaskSearchFreq)
 */
public void End()
{
Continue=false;    
} 
//-------------------------------------------------
/**
 * Runs  the Thread, pooling until the Thread si signed to stop by {@link #End()}
 */
@Override 
public void run() 
{
if (PDLog.isInfo())
    PDLog.Info("TaskCreator starts");    
Date d1=new Date(0);   
Date d2;   
while (Continue) 
    {
    try {
    Thread.sleep(SleepTime);
    } catch (InterruptedException e) 
        {
        }
    try {  
    d2=new Date();
    if (d2.getTime()-d1.getTime()>TaskSearchFreq)
        {
        d1=new Date();
        if (PDLog.isDebug())
            PDLog.Debug("TaskCreator run: "+d2);    
        TaskGen.GenerateTaskCat(TaskCategory);
        if (PDLog.isDebug())
            PDLog.Debug("TaskCreator ends: "+new Date());    
        }
    } catch (Exception e) 
        {
        e.printStackTrace();    
        PDLog.Error("TaskCreator run error:"+e.getLocalizedMessage());
        }
    }
if (PDLog.isInfo())
    PDLog.Info("TaskCreator destroy");    
} 
//-------------------------------------------------
} // **** END of task creator ***************************************

//*******************************************************************
/**
 * Thread for executing the instantiated tasks
 */
static private class TaskRunner extends Thread  
{
    /**
 * Time for pooling the status and shutdown orders
 */
static private long SleepTime=5000; 
/**
 * When false the task must stop
 */
private boolean Continue=true;
/**
 * Frecuency for pooling the time for executing pending Tasks
 */
private int TaskExecFreq;
/**
 * Category of tasks to pool for executing
 */
private String TaskCategory;
/**
 * Object for executing the tasks when it is the time for executing them
 */
PDTasksExec TaskRun;

//-------------------------------------------------
/**
 * Constuctor
 * @param pTaskExecFreq Pooling frecuency for execution
 * @param pTaskCategory Category of tasks for pool
 * @param pCon Connector
 */
public TaskRunner(int pTaskExecFreq, String pTaskCategory, Conector pCon)
{
setName("TaskRunner"+System.currentTimeMillis());
TaskExecFreq=pTaskExecFreq;   
TaskCategory=pTaskCategory;
try {
DriverGeneric Session=pCon.CreateSesion();
Session.Lock();
Session.AssignTaskUser();
TaskRun=new PDTasksExec(Session);
} catch (Exception ex)
    {
    ex.printStackTrace();    
    PDLog.Error("TaskRunner error:"+ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("TaskRunner.TaskRunner:"+pTaskExecFreq+"-"+pTaskCategory+"-"+pCon);
}
//-------------------------------------------------
/**
 * Signal the Thread to end in the next check of status (that is smaller than TaskExecFreq)
 */
public void End()
{
Continue=false;    
} 
//-------------------------------------------------
/**
 * Runs  the Thread, pooling until the Thread si signed to stop by {@link #End()}
 */
@Override 
public void run() 
{
if (PDLog.isInfo())
    PDLog.Info("TaskRunner starts");    
Date d1=new Date(0);   
Date d2;   
while (Continue) 
    {
    try {
    Thread.sleep(SleepTime);
    } catch (InterruptedException e) 
        {
        }
    try {  
    d2=new Date();
    if (d2.getTime()-d1.getTime()>TaskExecFreq)
        {
        d1=new Date();
        if (PDLog.isDebug())
            PDLog.Debug("TaskRunner run: "+d2);    
       TaskRun.getDrv().AssignTaskUser(); // to refresh ACL and other changes
       TaskRun.ExecutePendingTaskCat(TaskCategory);
        if (PDLog.isDebug())
            PDLog.Debug("TaskRunner ends: "+new Date());    
        }
    } catch (Exception e) 
        {
        e.printStackTrace();    
        PDLog.Error("TaskRunner run error:"+e.getLocalizedMessage());
        }
    }
if (PDLog.isInfo())
    PDLog.Info("TaskRunner Destroy");    
} 
//-------------------------------------------------
} // **** END of Task runner ***************************************
}
