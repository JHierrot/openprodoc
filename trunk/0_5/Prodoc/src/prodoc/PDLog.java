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

import java.io.FileOutputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 *
 * @author jhierrot
 */
public class PDLog
{
private static Logger log4j = null;
/**
 *
 */
private static boolean Debug=false;
/**
 *
 */
private static boolean Info=false;
/**
 *
 */
private static boolean Error=true;

static private String PropFile="log4j.properties";
/**
* @return the Debug
*/
public static boolean isDebug()
{
return Debug;
}
//----------------------------------------------------------------
/**
* @param aDebug the Debug to set
*/
public static void setDebug(boolean aDebug)
{
Debug = aDebug;
}
//----------------------------------------------------------------
/**
* @return the Error
*/
public static boolean isError()
{
return Error;
}
//----------------------------------------------------------------
/**
* @param aError the Error to set
*/
public static void setError(boolean aError)
{
Error = aError;
}
//----------------------------------------------------------------
/**
* @return the Info
*/
public static boolean isInfo()
{
return Info;
}
//----------------------------------------------------------------
/**
* @param aInfo the Info to set
*/
public static void setInfo(boolean aInfo)
{
Info = aInfo;
}
//----------------------------------------------------------------
/**
 *
 * @param s
 */
public static void Debug(String s)
{
// System.out.println(new Date()+" DEBUG ==>"+s);
getLogger().debug(s);
}
//----------------------------------------------------------------
/**
 *
 * @param s
 */
public static void Info(String s)
{
// System.out.println(new Date()+" INFO ==>"+s);
getLogger().info(s);
}
//----------------------------------------------------------------
/**
 *
 * @param s
 */
public static void Error(String s)
{
// System.out.println(new Date()+" ERROR ==>"+s);
getLogger().error(s);
}
//----------------------------------------------------------------
/**
 * 
 */
static final public int LOGLEVELERROR=0;
/**
 * 
 */
static final public int LOGLEVELINFO=1;
/**
 * 
 */
static final public int LOGLEVELDEBUG=2;

static void setLevel(int LogLevel)
{
switch(LogLevel)
    {case LOGLEVELINFO:
         Info=true;
         Error=true;
         break;
     case LOGLEVELDEBUG:
         Info=true;
         Debug=true;
         Error=true;
         break;
    default:
         Error=true;
    }
}
//----------------------------------------------------------------
/**
 * @return the log4j
 */
public static Logger getLogger()
{
if (log4j==null)
    {
    PropertyConfigurator.configure(getPropFile());
    FileOutputStream f;
//            try
//            {
//                f = new FileOutputStream("log4j.prop");
//           f.close();
//            } catch (Exception ex)
//            {
//                System.out.println(ex.getLocalizedMessage());
//            }
    log4j = Logger.getLogger("OpenProdoc");
    if (Debug)
       log4j.setLevel(Level.DEBUG);
    else if (Info)
       log4j.setLevel(Level.INFO);
    else if(Error)
       log4j.setLevel(Level.ERROR);
    }
return log4j;
}
//----------------------------------------------------------------
/**
 * @return the PropFile
 */
public static String getPropFile()
{
return PropFile;
}
//----------------------------------------------------------------
/**
 * @param aPropFile the PropFile to set
 */
public static void setPropFile(String aPropFile)
{
PropFile = aPropFile;
}
//----------------------------------------------------------------
}

