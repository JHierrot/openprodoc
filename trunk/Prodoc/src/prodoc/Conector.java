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

import java.util.Date;
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
private String TaskCategory="*";
//--------------------------------------------------------------------------
/**
 * reads, interpret and store the requiered elements of the configuration
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
if (PDLog.isDebug())
    PDLog.Debug("CreateTask >");        
if (TaskCategory==null || TaskCategory.length()==0)
    return;
if (TaskSearchFreq!=0)   
    CreateSearchTask(TaskSearchFreq, TaskCategory);
if (TaskExecFreq!=0)
    CreateExecTask(TaskExecFreq, TaskCategory);
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
    Conector.DestroySearchTask(ConectorName);
if (TaskExecFreq!=0)
    Conector.DestroyExecTask(ConectorName);   
if (PDLog.isDebug())
    PDLog.Debug("DestroyTask <");        
}
//--------------------------------------------------------------------------
/**
 * 
 * @param TaskSearchFreq
 * @param ConectorName 
 */
private void CreateSearchTask(int TaskSearchFreq, String TaskCategory)
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
 * 
 * @param TaskExecFreq
 * @param ConectorName 
 */
private void CreateExecTask(int TaskExecFreq, String TaskCategory)
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
 * 
 * @param ConectorName 
 */
private static void DestroySearchTask(String ConectorName)
{
if (!TaskSearchList.contains(ConectorName))
    return;
if (PDLog.isDebug())
    PDLog.Debug("DestroySearchTask: Con="+ConectorName);        
TaskCreator  TaskCreatorTask=(TaskCreator)TaskSearchList.get(ConectorName); 
TaskCreatorTask.End();
TaskSearchList.put(ConectorName, null);
}
//--------------------------------------------------------------------------
/**
 * 
 * @param ConectorName 
 */
private static void DestroyExecTask(String ConectorName)
{
if (!TaskExecList.contains(ConectorName))
    return;
if (PDLog.isDebug())
    PDLog.Debug("DestroyExecTask: Con="+ConectorName);        
TaskRunner  TaskRunnerTask=(TaskRunner)TaskExecList.get(ConectorName); 
TaskRunnerTask.End();
TaskExecList.put(ConectorName, null);
}
//--------------------------------------------------------------------------

//*******************************************************************
static private class TaskCreator extends Thread  
{
static private final long SleepTime=1000; 
private boolean Continue=true;
private int TaskSearchFreq;
private String TaskCategory;
//private Conector Con;
PDTasksCron TaskGen;

//-------------------------------------------------
public TaskCreator(int pTaskSearchFreq, String pTaskCategory, Conector pCon)
{
setName("TaskCreator");
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
public void End()
{
Continue=false;    
//System.out.println("Continue:"+Continue);
} 
//-------------------------------------------------
@Override 
public void run() 
{
if (PDLog.isDebug())
    PDLog.Debug("TaskCreator starts");    
Date d1=new Date(0);   
Date d2;   
while (Continue) 
    {
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
    try {
    Thread.sleep(SleepTime);
    } catch (InterruptedException e) 
        {
        }
    }
} 
//-------------------------------------------------
} // **** END of task creator ***************************************
//*******************************************************************
static private class TaskRunner extends Thread  
{
static private long SleepTime=1000; 
private boolean Continue=true;
private int TaskExecFreq;
private String TaskCategory;
//private Conector Con;
PDTasksExec TaskRun;

//-------------------------------------------------
public TaskRunner(int pTaskExecFreq, String pTaskCategory, Conector pCon)
{
setName("TaskRunner");
TaskExecFreq=pTaskExecFreq;   
TaskCategory=pTaskCategory;
//Con=pCon;
try {
DriverGeneric Session=pCon.CreateSesion();
Session.Lock();
Session.AssignTaskUser();
TaskRun=new PDTasksExec(Session);
} catch (Exception ex)
    {
    ex.printStackTrace();    
    PDLog.Error("TaskCreator error:"+ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("TaskRunner.TaskRunner:"+pTaskExecFreq+"-"+pTaskCategory+"-"+pCon);
}
//-------------------------------------------------
public void End()
{
Continue=false;    
//System.out.println("Continue:"+Continue);
} 
//-------------------------------------------------
@Override 
public void run() 
{
if (PDLog.isDebug())
    PDLog.Debug("TaskCreator starts");    
Date d1=new Date(0);   
Date d2;   
while (Continue) 
    {
    try {  
    d2=new Date();
    if (d2.getTime()-d1.getTime()>TaskExecFreq)
        {
        d1=new Date();
        if (PDLog.isDebug())
            PDLog.Debug("TaskRunner run: "+d2);    
        TaskRun.ExecutePendingTaskCat(TaskCategory);
        if (PDLog.isDebug())
            PDLog.Debug("TaskRunner ends: "+new Date());    
        }
    } catch (Exception e) 
        {
        e.printStackTrace();    
        PDLog.Error("TaskRunner run error:"+e.getLocalizedMessage());
        }
    try {
    Thread.sleep(SleepTime);
    } catch (InterruptedException e) 
        {
        }
    }
} 
//-------------------------------------------------
} // **** END of Task runner ***************************************

}
