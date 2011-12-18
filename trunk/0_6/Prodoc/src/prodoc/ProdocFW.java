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

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

/**
 *
 * @author jhierrot
 */
public class ProdocFW
{
/**
 *
 */
static private Hashtable ConectList=new Hashtable();
/**
 *
 */
static private Properties ProdocProperties = new Properties();
/**
 *
 * @param ConectorName
 * @param FileConf
 * @throws PDException
 */
static private synchronized void InitEngine(String ConectorName, String FileConf) throws PDException
{
if (ConectList.isEmpty())
    {
    Conector Engine=null;
    ReadConfiguration(FileConf);
    Engine=new Conector(ConectorName, ProdocProperties);
    ConectList.put(ConectorName, Engine);
    }
try {
SearchConector(ConectorName);
} catch (PDException e)
    {
    Conector Engine=null;
    ReadConfiguration(FileConf);
    Engine=new Conector(ConectorName, ProdocProperties);
    ConectList.put(ConectorName, Engine);
    }
}
//-------------------------------------------------------------------------
/**
 *
 * @param FileConf
 * @throws PDException
 */
static private void ReadConfiguration(String FileConf) throws PDException
{
try {
ProdocProperties.load(new FileInputStream(FileConf));
    } catch (Exception e)
        {
        PDException.GenPDException("Error_reading_OpenProdoc_configuration", FileConf);
        }
}
//-------------------------------------------------------------------------
/**
 *
 * @param ConectorName
 * @param FileConf
 * @throws PDException
 */
static public void InitProdoc(String ConectorName, String FileConf) throws PDException
{
if (ConectList.isEmpty())
    {
    InitEngine(ConectorName, FileConf);
    return;
    }
if(PDLog.isDebug())
    PDLog.Debug("ProdocFW.InitProdoc:"+ConectorName+"="+FileConf);
try {
SearchConector(ConectorName);
} catch (PDException e)
    {
    InitEngine(ConectorName, FileConf);
    }
}
//-------------------------------------------------------------------------
/**
 * 
 * @param ConectorName
 * @return
 */
static private Conector SearchConector(String ConectorName) throws PDException
{
Conector Engine=(Conector)ConectList.get(ConectorName);
if (Engine==null)
    {
    PDException.GenPDException("unknown_conector_name", ConectorName);
    return(null);
    }
else
    return(Engine);
}
//-------------------------------------------------------------------------
/**
 *
 * @param ConectorName
 * @throws PDException
 */
static public void ShutdownProdoc(String ConectorName) throws PDException
{
if(PDLog.isDebug())
    PDLog.Debug("ProdocFW.ShutdownProdoc:"+ConectorName);
SearchConector(ConectorName).Shutdown();
}
//---------------------------------------------------------------------
/**
 *
 * @param ConectorName
 * @param user
 * @param Password
 * @return
 * @throws PDException
 */
static public DriverGeneric getSession(String ConectorName, String user, String Password) throws PDException
{
if(PDLog.isDebug())
    PDLog.Debug("ProdocFW.getSession: "+ConectorName);
return SearchConector(ConectorName).getSession(user, Password);
}
//-------------------------------------------------------------------------
/**
 *
 * @param ConectorName 
 * @param Session
 * @throws PDException
 */
static public void freeSesion(String ConectorName, DriverGeneric Session)  throws PDException
{
if(PDLog.isDebug())
    PDLog.Debug("ProdocFW.freeSesion: "+ConectorName);
SearchConector(ConectorName).freeSesion(Session);
}
//-------------------------------------------------------------------------
}
