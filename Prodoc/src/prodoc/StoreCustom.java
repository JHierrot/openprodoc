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

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

/**
 *
 * @author Joaquin
 */
public class StoreCustom extends StoreGeneric
{
private StoreCustom Bin=null;    
static private HashMap<String, Class> DownloadedClasses=new HashMap();
private String PDId=null;
private String ClassName=null;
//-----------------------------------------------------------------
public StoreCustom(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt) throws PDExceptionFunc
{
super(pServer, pUser, pPassword, pParam, pEncrypt);
}
//-----------------------------------------------------------------
public StoreCustom(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt, DriverGeneric Drv) throws PDExceptionFunc
{
super(pServer, pUser, pPassword, pParam, pEncrypt);
String[] Params = pParam.split("\\|");
PDId=Params[0];
ClassName=Params[1];
try {
if (!DownloadedClasses.containsKey(ClassName))
    DownloadBin(Drv, PDId, ClassName);
Class CustomStore=DownloadedClasses.get(ClassName);
Constructor DefCons=CustomStore.getDeclaredConstructor(String.class, String.class, String.class, String.class, boolean.class);
Bin=(StoreCustom)DefCons.newInstance(pServer, pUser, pPassword, pParam, pEncrypt);
} catch (Exception Ex)
    {
    PDExceptionFunc.GenPDException("Unable_Instantiate_Custom_Repository"+":"+ClassName, Ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
@Override
protected void Create() throws PDException
{
Bin.Create();
}
//-----------------------------------------------------------------
@Override
protected void Destroy() throws PDException
{
Bin.Destroy();
}
//-----------------------------------------------------------------
@Override
protected void Connect() throws PDException
{
Bin.Connect();
}
//-----------------------------------------------------------------

@Override
protected void Disconnect() throws PDException
{
Bin.Disconnect();
}
//-----------------------------------------------------------------

@Override
protected int Insert(String Id, String Ver, InputStream Bytes, Record Rec, String OPDPath) throws PDException
{
return(Bin.Insert(Id, Ver, Bytes, Rec, OPDPath));
}
//-----------------------------------------------------------------

@Override
protected void Delete(String Id, String Ver, Record Rec) throws PDException
{
Bin.Delete(Id, Ver, Rec);
}
//-----------------------------------------------------------------

@Override
protected InputStream Retrieve(String Id, String Ver, Record Rec) throws PDException
{
return(Bin.Retrieve(Id, Ver, Rec));
}
//-----------------------------------------------------------------
@Override
protected void Rename(String Id1, String Ver1, String Id2, String Ver2) throws PDException
{
Bin.Rename(Id1, Ver1, Id2, Ver2);
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
Class CustomStore=Class.forName(ClassName, true, CL);
DownloadedClasses.put(ClassName, CustomStore);
}
////-----------------------------------------------------------------
//public int Insert(String PDId, String version, InputStream FileStream, Record Rec) throws PDException
//{
//return(Bin.Insert( PDId, version, FileStream, Rec));
//}
////-----------------------------------------------------------------
//public int Insert(String PDId, String version, String FilePath, Record Rec)  throws PDException
//{
//return(Bin.Insert(PDId, version, FilePath, Rec));
//}
//-----------------------------------------------------------------

}
