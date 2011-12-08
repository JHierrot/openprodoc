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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * This class allows to mantain a temporal copy of the loaded objects
 * @author jhierrot
 */
public class ObjectsCache
{
private Hashtable CacheValues=null;
private static Hashtable ListofCaches=new Hashtable(15);
static private CacheCleaner CleanerThread=null;

//----------------------------------------------------------------------------
ObjectsCache(String Name)
{
CacheValues=(Hashtable)ListofCaches.get(Name);
if (CacheValues==null)
    {
    CacheValues=new Hashtable(10);
    ListofCaches.put(Name, CacheValues);
    if (CleanerThread==null)
        StartThread();
    }
}    
//----------------------------------------------------------------------------
/**
 * Stores the object with the corresponding key
 * updating the timer
 * @param Key the index key
 * @param Value to store
 */
public void put(Object Key, Object Value)
{
//System.out.println("put:"+Key);          
CacheValues.put(Key, new CacheObject(Value));
} 
//----------------------------------------------------------------------------
/**
 * Returns the object from the cache
 * updating the timer
 * @param Key index to retrieve
 * @return theobject or nullif it didn't exist
 */
public Object get(Object Key)
{
CacheObject CO=(CacheObject)CacheValues.get(Key);
if (CO==null)
    return (null);
CO.UpdateCachedTime();
return(CO.getContent());   
} 
//----------------------------------------------------------------------------
/**
 * remove the object from the cache
 * @param Key
 */
public void remove(Object Key)
{
//System.out.println("Remove:"+Key);      
CacheValues.remove(Key);    
} 
//----------------------------------------------------------------------------
static synchronized private void StartThread()
{
if (CleanerThread!=null)
    return;
CleanerThread=new CacheCleaner();
CleanerThread.start();
}
//----------------------------------------------------------------------------
static public void EndCleaner()
{
if (CleanerThread==null)
    return;
CleanerThread.End();
}
//---------------------------------------------------------------------------
public Iterator getIter()
{
return(CacheValues.keySet().iterator());    
}
// **** END of ObjectsCache ***************************************
private class CacheObject
{
private long CachedTime;
private Object Content;
//----------------------------------------------------------------------------
CacheObject(Object pContent)
{
Content=pContent;
UpdateCachedTime();
}
//-------------------------------------------------
/**
 * @return the CachedTime
 */
public long getCachedTime()
{
return CachedTime;
}
//-------------------------------------------------
/**
 * @return the Content
 */
public Object getContent()
{
return Content;
}
//-------------------------------------------------
private void UpdateCachedTime()
{
CachedTime=System.currentTimeMillis();
}
//-------------------------------------------------
} // **** END of CacheObject ***************************************
static private class CacheCleaner extends Thread  
{
static final long MIN=60*1000; // 1 minute
static private long SleepTime=MIN; 
static private long PurgeTime=5*MIN; 
static private boolean Continue=true;
//-------------------------------------------------
public void End()
{
Continue=false;    
//System.out.println("Continue:"+Continue);
} 
//-------------------------------------------------
public void run() 
{
while (Continue) 
    {
    try {  
    long ActTime=System.currentTimeMillis();  
//    System.out.println("ActTime:"+ActTime);                    
    for (Iterator ListCaches = ListofCaches.values().iterator(); ListCaches.hasNext();) 
        {
        Hashtable CacheValues = (Hashtable) ListCaches.next();
//        System.out.println("CacheValues:"+CacheValues);   
        HashSet TobeDeleted=new HashSet();
        for (Iterator ListId = CacheValues.keySet().iterator(); ListId.hasNext();) 
            {
            String IdElement=(String) ListId.next();
            CacheObject CO=(CacheObject) CacheValues.get(IdElement);
//            System.out.println("IdElement:"+IdElement+"/CO.getCachedTime():"+CO.getCachedTime());            
            if (ActTime-CO.getCachedTime()>PurgeTime)
                {        
                TobeDeleted.add(IdElement);                        
                }
            }
        for (Iterator ListId2Del = TobeDeleted.iterator(); ListId2Del.hasNext();)
            {
            String IdElement=(String) ListId2Del.next();    
            CacheValues.remove(IdElement);
//            System.out.println("Remove:"+IdElement);      
            }
        }
    } catch (Exception e) 
        {
        }
    try {
//    System.out.println("Start sleeping");            
    Thread.sleep(SleepTime);
//    System.out.println("End sleeping");            
    } catch (InterruptedException e) 
        {
        }
    }
} 
//-------------------------------------------------
} // **** END of Limpiador del pool ***************************************
}
