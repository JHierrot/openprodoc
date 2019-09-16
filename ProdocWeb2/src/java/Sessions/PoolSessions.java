/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sessions;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import prodoc.PDLog;
import prodoc.ProdocFW;

/**
 *
 * @author jhier
 */
public class PoolSessions
{
private final static Hashtable<String, CurrentSession> ListOPSess=new Hashtable();
static private long TIMEOUT=10*60*1000;   
private static boolean Started=false;
private static Enumeration<CurrentSession> ListCS=null;

//-------------------------------------------------------------------------
public static void AddSession(String Id, CurrentSession CS)
{
if (!Started) 
    StartFramework();   
getListOPSess().put(Id, CS);
}
//-------------------------------------------------------------------------
public static void InitList()
{
ListCS = ListOPSess.elements();
}
//-------------------------------------------------------------------------
public static CurrentSession NextList()
{
if (ListCS!=null && ListCS.hasMoreElements())
    return(ListCS.nextElement());
else
    return(null);
}
//-------------------------------------------------------------------------
public static boolean HasExpired(CurrentSession CS)
{
return((System.currentTimeMillis()-CS.getLastUse().getTime())>getTIMEOUT());    
}
//-------------------------------------------------------------------------
public static synchronized CurrentSession GetSession(String Id)
{
CurrentSession CS = getListOPSess().get(Id);
if (CS!=null && CS.getDrv().IsConnected())
    return(CS);
return(null);
}
//-------------------------------------------------------------------------
public static synchronized boolean DelSession(String Id)
{
CurrentSession CS = getListOPSess().get(Id);
if (CS==null)
    return(false);
try{
ProdocFW.freeSesion("PD", CS.getDrv());
} catch (Exception Ex)
    {
    PDLog.Error(Ex.getLocalizedMessage());
    }
getListOPSess().remove(Id);
if (PDLog.isDebug())
    PDLog.Debug("Disconected:"+CS.getHost()+"/"+CS.getUserName());
return(true);
}
//--------------------------------------------------------------------------
static synchronized private void StartFramework()
{
if (Started)   
    return;
SessCleaner  SC=new SessCleaner(); 
SC.start();
Started=true;
}
//--------------------------------------------------------------------------
/**
 * @return the ListOPSess
 */
private static Hashtable<String, CurrentSession> getListOPSess()
{
return ListOPSess;
}
//-------------------------------------------------------------------------
/**
 * @return the TIMEOUT
 */
public static long getTIMEOUT()
{
return TIMEOUT;
}
//-------------------------------------------------------------------------
/**
 * @param aTIMEOUT the TIMEOUT to set
 */
public static void setTIMEOUT(long aTIMEOUT)
{
TIMEOUT = aTIMEOUT;
}
//--------------------------------------------------------------

//***********************************************************************
static private class SessCleaner extends Thread  
{

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
        if (HasExpired(entry.getValue()))  
            DelSession(entry.getKey());
        }
    try {
    Thread.sleep(getTIMEOUT());
    } catch (InterruptedException e) 
        {
        }

    }

}
//-------------------------------------------------------------------------
}
//***********************************************************************
    
}
