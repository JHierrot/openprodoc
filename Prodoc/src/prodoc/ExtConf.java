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
 * author: Joaquin Hierro      2017
 * 
 */

package prodoc;

import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author Joaquin
 */
public class ExtConf
{
static private boolean OpacActive=false;
static private boolean WebInactive=false;
private static final Vector<String> DocTipesList=new Vector();
private static final Vector<String> FieldsToInclude=new Vector();
static private String BaseFolder=null;
static private boolean Inheritance=false;
static private String ResultForm=null;
static private int MaxResults;
static private String FormSearchCSS=null;
static private String FormSearchLogo=null;
static private String User=null;
static private String Pass=null;
static private String Title=null;
static private String DTLabel=null;

//----------------------------------------------------------------------------    
static void AssignConf(Properties ProdocProperties)
{
String ConfOpacActive=ProdocProperties.getProperty("OpacActive");
if (ConfOpacActive!=null && ConfOpacActive.trim().equals("1"))
        setOpacActive(true);
String ConfWebInactive=ProdocProperties.getProperty("WebInactive");
if (ConfWebInactive!=null && ConfWebInactive.trim().equals("1"))
        setWebInactive(true);
String ConfDocTipesList=ProdocProperties.getProperty("DocTipesList");
if (ConfDocTipesList!=null && ConfDocTipesList.trim().length()!=0)
    {
    String[] DTL = ConfDocTipesList.trim().split("\\|");
    for (int i = 0; i < DTL.length; i++)
        getDocTipesList().add(DTL[i]);
    }
String ConfFieldsToInclude=ProdocProperties.getProperty("FieldsToInclude");
if (ConfFieldsToInclude!=null && ConfFieldsToInclude.trim().length()!=0)
    {
    String[] FTL = ConfDocTipesList.trim().split("\\|");
    for (int i = 0; i < FTL.length; i++)
        getFieldsToInclude().add(FTL[i]);    
    }
String ConfBaseFolder=ProdocProperties.getProperty("BaseFolder");
if (ConfBaseFolder!=null && ConfBaseFolder.trim().length()!=0)
    {
    BaseFolder=ConfBaseFolder.trim();
    }
String ConfInheritance=ProdocProperties.getProperty("Inheritance");
if (ConfInheritance!=null && ConfInheritance.trim().equals("1"))
    {
    Inheritance=true;
    }
String ConfResultForm=ProdocProperties.getProperty("ResultForm");
if (ConfResultForm!=null && ConfResultForm.trim().length()!=0)
    {
    ResultForm=ConfResultForm.trim();
    }
String ConfMaxResults=ProdocProperties.getProperty("MaxResults");
if (ConfMaxResults!=null && ConfMaxResults.trim().length()!=0)
    {
    MaxResults=Integer.parseInt(ConfMaxResults.trim());
    }
String ConfFormSearchCSS=ProdocProperties.getProperty("FormSearchCSS");
if (ConfFormSearchCSS!=null && ConfFormSearchCSS.trim().length()!=0)
    {
    FormSearchCSS=ConfFormSearchCSS.trim();
    }    
String ConfFormSearchLogo=ProdocProperties.getProperty("FormSearchLogo");
if (ConfFormSearchLogo!=null && ConfFormSearchLogo.trim().length()!=0)
    {
    FormSearchLogo=ConfFormSearchLogo.trim();
    }    
String ConfUser=ProdocProperties.getProperty("User");
if (ConfUser!=null && ConfUser.trim().length()!=0)
    {
    User=ConfUser.trim();
    }    
String ConfPass=ProdocProperties.getProperty("Pass");
if (ConfPass!=null && ConfPass.trim().length()!=0)
    {
    Pass=ConfPass.trim();
    }    
String ConfTitle=ProdocProperties.getProperty("Title");
if (ConfTitle!=null && ConfTitle.trim().length()!=0)
    {
    Title=ConfTitle.trim();
    }    
String ConfDTLabel=ProdocProperties.getProperty("DTLabel");
if (ConfDTLabel!=null && ConfDTLabel.trim().length()!=0)
    {
    DTLabel=ConfDTLabel.trim();
    }    
}
//----------------------------------------------------------------------------    
/**
* @return the OpacActive
*/
public static boolean isOpacActive()
{
return OpacActive;
}
//----------------------------------------------------------------------------    
/**
* @param aOpacActive the OpacActive to set
*/
public static void setOpacActive(boolean aOpacActive)
{
OpacActive = aOpacActive;
}
//----------------------------------------------------------------------------    
/**
* @return the DocTipesList
*/
public static Vector<String> getDocTipesList()
{
return DocTipesList;
}
//----------------------------------------------------------------------------    
/**
* @return the FieldsToInclude
*/
public static Vector<String> getFieldsToInclude()
{
return FieldsToInclude;
}
//----------------------------------------------------------------------------    
/**
* @return the BaseFolder
*/
public static String getBaseFolder()
{
return BaseFolder;
}
//----------------------------------------------------------------------------    
/**
* @return the Inheritance
*/
public static boolean isInheritance()
{
return Inheritance;
}
//----------------------------------------------------------------------------    
/**
* @return the ResultForm
*/
public static String getResultForm()
{
return ResultForm;
}
//----------------------------------------------------------------------------    
/**
* @return the MaxResults
*/
public static int getMaxResults()
{
return MaxResults;
}
//----------------------------------------------------------------------------    
/**
* @return the FormSearchCSS
*/
public static String getFormSearchCSS()
{
return FormSearchCSS;
}
//----------------------------------------------------------------------------    
/**
* @return the WebInactive
*/
public static boolean isWebInactive()
{
return WebInactive;
}
//----------------------------------------------------------------------------    
/**
* @param aWebInactive the WebInactive to set
*/
public static void setWebInactive(boolean aWebInactive)
{
WebInactive = aWebInactive;
}
//----------------------------------------------------------------------------    
/**
* @return the User
*/
public static String getUser()
{
return User;
}
//----------------------------------------------------------------------------    
/**
* @return the Pass
*/
public static String getPass()
{
return Pass;
}
//----------------------------------------------------------------------------    
/**
* @return the FormSearchLogo
*/
public static String getFormSearchLogo()
{
return FormSearchLogo;
}
//----------------------------------------------------------------------------    
/**
* @return the Title
*/
public static String getTitle()
{
return Title;
}
//----------------------------------------------------------------------------    
/**
* @return the DTLabel
*/
public static String getDTLabel()
{
return DTLabel;
}
//----------------------------------------------------------------------------    
}
