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
private PDDocs D=null;
private PDFolders F=null;
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
     *
     * @param Event
     * @param Param1
     * @param Param2
     * @param Param3
     * @param Param4
     * @param Doc
     * @throws PDException
     */
protected void ExecuteEvent (String Event, String Param1,String Param2,String Param3,String Param4, PDDocs Doc)  throws PDException
{
D=Doc;    
Bin.ExecuteEvent(Event, Param1, Param2, Param3, Param4, Doc);
}
//-----------------------------------------------------------------
/**
 * 
 * @param NewVals
 * @throws PDException 
 */
protected void ModDoc(Record NewVals)  throws PDException
{
D.assignValues(NewVals);
}        
//-----------------------------------------------------------------
/**
 *
 * @param Event
 * @param Param1
 * @param Param2
 * @param Param3
 * @param Param4
 * @param Fold
 * @throws PDException
 */
protected void ExecuteEvent(String Event,String Param1, String Param2,String Param3, String Param4, PDFolders Fold) throws PDException
{
F=Fold;    
Bin.ExecuteEvent(Event, Param1, Param2, Param3, Param4, Fold);
}
//-----------------------------------------------------------------
private synchronized void DownloadBin(DriverGeneric Drv, String PdId, String ClassName) throws Exception
{
if (DownloadedClasses.containsKey(ClassName))
    return;
PDDocs D=new PDDocs(Drv);
D.setPDId(PdId);
String DownFile = D.getFile(System.getProperty("java.io.tmpdir"));
URLClassLoader CL=new URLClassLoader(new URL[]{new URL("file:"+DownFile)}, getClass().getClassLoader());
Class CustomTask=Class.forName(ClassName, true, CL);
DownloadedClasses.put(ClassName, CustomTask);
}
//-----------------------------------------------------------------    
}
