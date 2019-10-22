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
 * author: Joaquin Hierro      2018
 * 
 */

package prodoc;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

/**
 *
 * @author jhierrot
 */
public class CustomTask
{
private CustomTask Bin=null;    
private static final HashMap<String, Class> DownloadedClasses=new HashMap();
//-----------------------------------------------------------------

    /**
     *
     * @param Drv
     * @param PDId
     * @param ClassName
     * @throws PDExceptionFunc
     */
protected CustomTask(DriverGeneric Drv, String PDId, String ClassName) throws PDExceptionFunc
{
try {
if (!DownloadedClasses.containsKey(ClassName))
    DownloadBin(Drv, PDId, ClassName);
Class CustomTask=DownloadedClasses.get(ClassName);
Constructor DefCons=CustomTask.getDeclaredConstructor();
Bin=(CustomTask)DefCons.newInstance();
} catch (Exception Ex)
    {
    PDExceptionFunc.GenPDException("Unable_Instantiate_Custom_Task"+":"+ClassName, Ex.getLocalizedMessage());
    }
}
//------------------------------------------------------
/** cnstructor to be overloaded
 * 
 */
public CustomTask()
{  
}
//-----------------------------------------------------------------
/**
 * Calls the custom class for the developed Method 
 * @param Param1 Parameter 1 of the configured Event
 * @param Param2 Parameter 2 of the configured Event
 * @param Param3 Parameter 3 of the configured Event
 * @param Param4 Parameter 4 of the configured Event
 * @param Doc    PDDocs Document that triggers the Event
 * @throws PDException In any Error
 */
final protected void ExecuteEvent (String Param1,String Param2,String Param3,String Param4, PDDocs Doc)  throws PDException
{ 
if (PDLog.isInfo())    
    PDLog.Info("ExecuteEventDoc.Param1=["+Param1+"] Param2=["+Param2+"] Param3=["+Param3+"] Param4=["+Param4+"] Doc=["+Doc.getRecSum()+"]");
Bin.ExecuteEventDoc(Param1, Param2, Param3, Param4, Doc);
}
//-----------------------------------------------------------------
final protected boolean CustomMeetsReq(String param, String param2, String param3, String param4, Record Rec)
{
return(Bin.CustomMeetsReqRec(param, param2, param3, param4, Rec));
}
       
//-----------------------------------------------------------------
/**
 * Calls the custom class for the developed Method 
 * @param Param1 Parameter 1 of the configured Event
 * @param Param2 Parameter 2 of the configured Event
 * @param Param3 Parameter 3 of the configured Event
 * @param Param4 Parameter 4 of the configured Event
 * @param Fold   PDFolders Folder Document that triggers the Event
 * @throws PDException In any Error
 */
final protected void ExecuteEvent(String Param1, String Param2,String Param3, String Param4, PDFolders Fold) throws PDException
{
if (PDLog.isInfo())    
    PDLog.Info("ExecuteEventFold.Param1=["+Param1+"] Param2=["+Param2+"] Param3=["+Param3+"] Param4=["+Param4+"] Fold=["+Fold.getRecSum()+"]");
Bin.ExecuteEventFold(Param1, Param2, Param3, Param4, Fold);
}
//-----------------------------------------------------------------
private synchronized void DownloadBin(DriverGeneric Drv, String PdId, String ClassName) throws Exception
{
if (DownloadedClasses.containsKey(ClassName))
    return;
PDDocs D=new PDDocs(Drv);
D.setPDId(PdId);
String DownFile = D.getFile(System.getProperty("java.io.tmpdir"));
if (PDLog.isInfo())    
    PDLog.Info("DownloadBin.Id=["+PdId+"] DownFile=["+DownFile+"]");
URLClassLoader CL=new URLClassLoader(new URL[]{new URL("file:"+DownFile)}, getClass().getClassLoader());
Class CustomTask=Class.forName(ClassName, true, CL);
DownloadedClasses.put(ClassName, CustomTask);
}
//-----------------------------------------------------------------    
/**
 *
 * @param Param1
 * @param Param2
 * @param Param3
 * @param Param4
 * @param Fold
 */
protected void ExecuteEventFold(String Param1, String Param2, String Param3, String Param4, PDFolders Fold) throws PDException
{
}
//-----------------------------------------------------------------    
/**
 *
 * @param Param1
 * @param Param2
 * @param Param3
 * @param Param4
 * @param Doc
 */
protected void ExecuteEventDoc(String Param1, String Param2, String Param3, String Param4, PDDocs Doc) throws PDException
{
}
//-----------------------------------------------------------------    
/**
 *
 * @param param
 * @param param2
 * @param param3
 * @param param4
 * @param Rec
 * @return
 */
protected boolean CustomMeetsReqRec(String param, String param2, String param3, String param4, Record Rec)
{
return(true);
}
//-----------------------------------------------------------------    
}
