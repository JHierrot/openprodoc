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

package prodoc.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Properties;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDExceptionFunc;
import prodoc.PDLog;

/**
 *
 * @author jhierrot
 */
public class AuthCustom extends AuthGeneric
{
private AuthGeneric Bin=null;    
static private HashMap<String, Class> DownloadedClasses=new HashMap<String, Class>();
static private HashMap<String, Properties> DownloadedProp=new HashMap<String, Properties>();

/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
public AuthCustom(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
}
//----------------------------------------------------------------------

    /**
     *
     * @param pServer
     * @param pUser
     * @param pPassword
     * @param pParam
     * @param Drv
     * @throws PDExceptionFunc
     */
public AuthCustom(String pServer, String pUser, String pPassword, String pParam, DriverGeneric Drv) throws PDExceptionFunc
{
super(pServer, pUser, pPassword, pParam);
String[] Params = pParam.split("\\|");
String PDId=Params[0];
String ClassName=Params[1];
String PDIdProps=null;
if ( Params.length>=3)
   PDIdProps=Params[2];
try {
if (!DownloadedClasses.containsKey(ClassName))
    DownloadBin(Drv, PDId, ClassName);
if (PDIdProps!=null && !DownloadedProp.containsKey(PDIdProps))
    DownloadProp(Drv, PDIdProps);
Class CustomAuth=DownloadedClasses.get(ClassName);
Constructor DefCons=CustomAuth.getDeclaredConstructor(String.class, String.class, String.class, String.class);
Bin=(AuthGeneric)DefCons.newInstance(pServer, pUser, pPassword, pParam);
if (PDIdProps!=null)
    Bin.setProp(DownloadedProp.get(PDIdProps));
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    PDExceptionFunc.GenPDException("Unable_Instantiate_Custom_Authenticator"+":"+ClassName, Ex.getLocalizedMessage());
    }
}
//----------------------------------------------------------------------
/**
 *
 * @param User
 * @param Pass
 * @throws PDException
 */
@Override
public void Authenticate(String User, String Pass) throws PDException
{
Bin.Authenticate(User, Pass);
}
//----------------------------------------------------------------------
private synchronized void DownloadBin(DriverGeneric Drv, String PdId, String ClassName) throws Exception
{
if (DownloadedClasses.containsKey(ClassName))
    return;
PDDocs D=new PDDocs(Drv);
D.setPDId(PdId);
String DownFile = D.getFile(System.getProperty("java.io.tmpdir"));
URLClassLoader CL=new URLClassLoader(new URL[]{new URL("file:"+DownFile)}, getClass().getClassLoader());
Class AuthCustom=Class.forName(ClassName, true, CL);
DownloadedClasses.put(ClassName, AuthCustom);
if (PDLog.isDebug())
    PDLog.Debug("AuthCustom.DownloadBin:"+PdId+"/"+ClassName);
}
//-----------------------------------------------------------------
private synchronized void DownloadProp(DriverGeneric Drv, String PDIdProps) throws Exception
{
if (DownloadedProp.containsKey(PDIdProps))
    return;
PDDocs D=new PDDocs(Drv);
D.setPDId(PDIdProps);
ByteArrayOutputStream OutBytes=new ByteArrayOutputStream();
D.getStream(OutBytes);
Properties Proper=new Properties();
Proper.load(new ByteArrayInputStream(OutBytes.toByteArray()) );
DownloadedProp.put(PDIdProps, Proper);
if (PDLog.isDebug())
    PDLog.Debug("AuthCustom.DownloadProp:"+PDIdProps);
}
//-----------------------------------------------------------------
}
