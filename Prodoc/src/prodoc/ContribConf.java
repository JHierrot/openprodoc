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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author Joaquin
 */
public class ContribConf
{
private String Id;
private final Vector<String> DocTipesList=new Vector();
private String BaseFolder=null;
private boolean OpenContrib=false;
private String LoginFolderType=null;
private final Vector<String> LoginFields=new Vector();
private final Vector<String> FieldsToRead=new Vector();
private String FormContribCSS=null;
private String FormContribLogo=null;
private String User=null;
private String Pass=null;
private String Title=null;
private String DocsReportId=null;
private String TitleList=null;
//private static final ContribConf DefExtConf=new ContribConf();
private String UrlHelp=null;
private String OKMsg="Ok";
private int NumHtmlContLog=0;
private Vector<String[]> ListAgentLog=null;
private Vector<String> HtmlAgentLog=null;
private Vector<Boolean> is1ColAgentLog=null;
private int NumHtmlContList=0;
private Vector<String[]> ListAgentList=null;
private Vector<String> HtmlAgentList=null;
private Vector<Boolean> is1ColAgentList=null;
private int NumHtmlContAdd=0;
private Vector<String[]> ListAgentAdd=null;
private Vector<String> HtmlAgentAdd=null;
private Vector<Boolean> is1ColAgentAdd=null;
private int NumHtmlContRes=0;
private Vector<String[]> ListAgentRes=null;
private Vector<String> HtmlAgentRes=null;
private final HashMap<String, HashSet> FieldsByType=new HashMap();
private final HashSet<String> AllowedExt=new HashSet();
private int MaxSize=30000000;

private static final String FIELDSPREFIX="Fields_";
private static HashSet<String> GlobalExcluded=null;

/**
 * Default constructor
 * @param IdContrib Identifier of the OPD document containing the Contrib definition
 */
public ContribConf(String IdContrib)
{
Id=IdContrib;
}
//----------------------------------------------------------------------------    
/**
 * Assign configuration of contribution from a properties
 * @param ContribProperties Properties of the contribution forms
 */
public void AssignConf(Properties ContribProperties)
{
String ConfDocTipesList=ContribProperties.getProperty("DocTipesList");
if (ConfDocTipesList!=null && ConfDocTipesList.trim().length()!=0)
    {
    String[] DTL = ConfDocTipesList.trim().split("\\|");
    for (String DTL1 : DTL)
        getDocTipesList().add(DTL1.trim());
    }
for (int i = 0; i < getDocTipesList().size(); i++)
    {
    String FieldsType = ContribProperties.getProperty(FIELDSPREFIX+getDocTipesList().elementAt(i));
    if (FieldsType!=null && FieldsType.trim().length()!=0) 
        {
        String[] FTL = FieldsType.trim().split("\\|");
        HashSet Temp=new HashSet();
        for (String FTL1 : FTL)  
            Temp.add(FTL1.trim().toUpperCase());
        getFieldsByType().put(getDocTipesList().elementAt(i), Temp);
        }
    }
String ConfFieldsToInclude=ContribProperties.getProperty("FieldsToInclude");
if (ConfFieldsToInclude!=null && ConfFieldsToInclude.trim().length()!=0)
    {
    String[] FTL = ConfFieldsToInclude.trim().split("\\|");
    for (String FTL1 : FTL)  
        getFieldsToRead().add(FTL1.trim());
    }
String ConfOpenContrib=ContribProperties.getProperty("OpenContrib");
if (ConfOpenContrib!=null && ConfOpenContrib.trim().equals("1"))
    {
    OpenContrib=true;
    }
String ConfLoginFolder=ContribProperties.getProperty("LoginFolderType");
if (ConfLoginFolder!=null && ConfLoginFolder.trim().length()!=0)
    {
    LoginFolderType=ConfLoginFolder.trim();
    }
String ConfLoginFields=ContribProperties.getProperty("LoginFields");
if (ConfLoginFields!=null && ConfLoginFields.trim().length()!=0)
    {
    String[] FTL = ConfLoginFields.trim().split("\\|");
    for (String FTL1 : FTL) 
        LoginFields.add(FTL1.trim());
    }
String ConfBaseFolder=ContribProperties.getProperty("BaseFolder");
if (ConfBaseFolder!=null && ConfBaseFolder.trim().length()!=0)
    {
    BaseFolder=ConfBaseFolder.trim();
    }
String ConfContribCSS=ContribProperties.getProperty("ContribCSS");
if (ConfContribCSS!=null && ConfContribCSS.trim().length()!=0)
    {
    FormContribCSS=ConfContribCSS.trim();
    }    
String ConfContribLogo=ContribProperties.getProperty("ContribLogo");
if (ConfContribLogo!=null && ConfContribLogo.trim().length()!=0)
    {
    FormContribLogo=ConfContribLogo.trim();
    }    
String ConfUser=ContribProperties.getProperty("User");
if (ConfUser!=null && ConfUser.trim().length()!=0)
    {
    User=ConfUser.trim();
    }    
String ConfPass=ContribProperties.getProperty("Pass");
if (ConfPass!=null && ConfPass.trim().length()!=0)
    {
    Pass=ConfPass.trim();
    }    
String ConfTitle=ContribProperties.getProperty("Title");
if (ConfTitle!=null && ConfTitle.trim().length()!=0)
    {
    Title=ConfTitle.trim();
    }    
String ConfDocsReportId=ContribProperties.getProperty("DocsReportId");
if (ConfDocsReportId!=null && ConfDocsReportId.trim().length()!=0)
    {
    DocsReportId=ConfDocsReportId.trim();
    }    
String ConfTitleList=ContribProperties.getProperty("TitleList");
if (ConfTitleList!=null && ConfTitleList.trim().length()!=0)
    {
    TitleList=ConfTitleList.trim();
    }    
String ConfUrlHelp=ContribProperties.getProperty("UrlHelp");
if (ConfUrlHelp!=null && ConfUrlHelp.trim().length()!=0)
    {
    UrlHelp=ConfUrlHelp.trim();
    }    

String ConfOKMsg=ContribProperties.getProperty("OKMsg");
if (ConfOKMsg!=null && ConfOKMsg.trim().length()!=0)
    {
    OKMsg=ConfOKMsg.trim();
    }    
//--------------
String ConfNumHtmlContLog=ContribProperties.getProperty("NumHtmlContLog");
if (ConfNumHtmlContLog!=null && ConfNumHtmlContLog.trim().length()!=0)
    {
    NumHtmlContLog=Integer.parseInt(ConfNumHtmlContLog.trim());
    }    
ListAgentLog=new Vector(NumHtmlContLog);
HtmlAgentLog=new Vector(NumHtmlContLog);
is1ColAgentLog=new Vector(NumHtmlContLog);
for (int NHO = 0; NHO < NumHtmlContLog; NHO++)
    {
    String LAgent=ContribProperties.getProperty("ListAgentLog"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgentLog.add(LA);
        }    
    String HAgent=ContribProperties.getProperty("HtmlAgentLog"+NHO);
    HtmlAgentLog.add(HAgent);
    String NumColAgent=ContribProperties.getProperty("NumColAgentLog"+NHO);
    is1ColAgentLog.add(NumColAgent!=null&&NumColAgent.equals("1"));
    }
//--------------
String ConfNumHtmlContList=ContribProperties.getProperty("NumHtmlContList");
if (ConfNumHtmlContList!=null && ConfNumHtmlContList.trim().length()!=0)
    {
    NumHtmlContList=Integer.parseInt(ConfNumHtmlContList.trim());
    }    
ListAgentList=new Vector(NumHtmlContList);
HtmlAgentList=new Vector(NumHtmlContList);
is1ColAgentList=new Vector(NumHtmlContList);
for (int NHO = 0; NHO < NumHtmlContList; NHO++)
    {
    String LAgent=ContribProperties.getProperty("ListAgentList"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgentList.add(LA);
        }    
    String HAgent=ContribProperties.getProperty("HtmlAgentList"+NHO);
    HtmlAgentList.add(HAgent);
    String NumColAgent=ContribProperties.getProperty("NumColAgentList"+NHO);
    is1ColAgentList.add(NumColAgent!=null&&NumColAgent.equals("1"));
    }
//--------------
String ConfNumHtmlContAdd=ContribProperties.getProperty("NumHtmlContAdd");
if (ConfNumHtmlContAdd!=null && ConfNumHtmlContAdd.trim().length()!=0)
    {
    NumHtmlContAdd=Integer.parseInt(ConfNumHtmlContAdd.trim());
    }    
ListAgentAdd=new Vector(NumHtmlContAdd);
HtmlAgentAdd=new Vector(NumHtmlContAdd);
is1ColAgentAdd=new Vector(NumHtmlContAdd);
for (int NHO = 0; NHO < NumHtmlContAdd; NHO++)
    {
    String LAgent=ContribProperties.getProperty("ListAgentAdd"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgentAdd.add(LA);
        }    
    String HAgent=ContribProperties.getProperty("HtmlAgentAdd"+NHO);
    HtmlAgentAdd.add(HAgent);
    String NumColAgent=ContribProperties.getProperty("NumColAgentAdd"+NHO);
    is1ColAgentAdd.add(NumColAgent!=null&&NumColAgent.equals("1"));
    }
//--------------
String ConfNumHtmlContRes=ContribProperties.getProperty("NumHtmlContRes");
if (ConfNumHtmlContRes!=null && ConfNumHtmlContRes.trim().length()!=0)
    {
    NumHtmlContRes=Integer.parseInt(ConfNumHtmlContRes.trim());
    }    
ListAgentRes=new Vector(NumHtmlContRes);
HtmlAgentRes=new Vector(NumHtmlContRes);
for (int NHO = 0; NHO < NumHtmlContRes; NHO++)
    {
    String LAgent=ContribProperties.getProperty("ListAgentRes"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgentRes.add(LA);
        }    
    String HAgent=ContribProperties.getProperty("HtmlAgentRes"+NHO);
    HtmlAgentRes.add(HAgent);
    }
//---------------
String ConfAllowedExt=ContribProperties.getProperty("AllowedExt");
if (ConfLoginFields!=null && ConfLoginFields.trim().length()!=0)
    {
    String[] ALLE = ConfAllowedExt.trim().split("\\|");
    for (String ALLE1 : ALLE) 
        AllowedExt.add(ALLE1.trim());
    }
String ConfMaxSize=ContribProperties.getProperty("MaxSize");
if (ConfMaxSize!=null && ConfMaxSize.trim().length()!=0)
    {
    MaxSize=Integer.parseInt(ConfMaxSize.trim());
    }       

}
//---------------------------------------------------------------------------- 
/**
 * @return the TitleList
 */
public String getTitleList()
{
return TitleList;
}
//---------------------------------------------------------------------------- 
/**
 * Calculates a html page for log of documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public String SolveHtmlLog(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlContLog; NHO++)
    {
    String[] LA =ListAgentLog.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(HtmlAgentLog.get(NHO)); 
        }
    }
return(null);    
}
//---------------------------------------------------------------------------- 
/**
 * Calculates if must be 1 column for log of documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public Boolean Is1ColHtmlLog(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlContLog; NHO++)
    {
    String[] LA =ListAgentLog.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(is1ColAgentLog.get(NHO)); 
        }
    }
return(false);    
}
//---------------------------------------------------------------------------- 
/**
 * Calculates a html page for list of documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public String SolveHtmlList(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlContList; NHO++)
    {
    String[] LA =ListAgentList.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(HtmlAgentList.get(NHO)); 
        }
    }
return(null);    
}
//---------------------------------------------------------------------------- 
/**
 * Calculates if use 1 column for list of documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public Boolean Is1ColHtmlList(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlContList; NHO++)
    {
    String[] LA =ListAgentList.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(is1ColAgentList.get(NHO)); 
        }
    }
return(false);    
}
//---------------------------------------------------------------------------- 
/**
 * Calculates a html page for adding documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public String SolveHtmlAdd(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlContAdd; NHO++)
    {
    String[] LA =ListAgentAdd.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(HtmlAgentAdd.get(NHO)); 
        }
    }
return(null);    
}
//---------------------------------------------------------------------------- 
/**
 * Calculates if use 1 column for adding documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public Boolean Is1ColHtmlAdd(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlContAdd; NHO++)
    {
    String[] LA =ListAgentAdd.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(is1ColAgentAdd.get(NHO)); 
        }
    }
return(false);    
}
//---------------------------------------------------------------------------- 
/**
 * Calculates a html page for result of adding documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public String SolveHtmlRes(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlContRes; NHO++)
    {
    String[] LA =ListAgentRes.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(HtmlAgentRes.get(NHO)); 
        }
    }
return(null);    
}
//---------------------------------------------------------------------------- 
/**
* @return the DocTipesList
*/
public Vector<String> getDocTipesList()
{
return DocTipesList;
}
//----------------------------------------------------------------------------    
/**
* @return the FieldsToRead
*/
public Vector<String> getFieldsToRead()
{
return FieldsToRead;
}
//----------------------------------------------------------------------------    
/**
* @return the BaseFolder
*/
public String getBaseFolder()
{
return BaseFolder;
}
//----------------------------------------------------------------------------    
/**
* @return the FormContribCSS
*/
public String getFormContribCSS()
{
return FormContribCSS;
}
//----------------------------------------------------------------------------    
/**
* @return the User
*/
public String getUser()
{
return User;
}
//----------------------------------------------------------------------------    
/**
* @return the Pass
*/
public String getPass()
{
return Pass;
}
//----------------------------------------------------------------------------    
/**
* @return the FormContribLogo
*/
public String getFormContribLogo()
{
return FormContribLogo;
}
//----------------------------------------------------------------------------    
/**
* @return the Title
*/
public String getTitle()
{
return Title;
}
//----------------------------------------------------------------------------    
/**
* @return the UrlHelp
*/
public String getUrlHelp()
{
return UrlHelp;
}
//---------------------------------------------------------
/**
* @return the OpenContrib
*/
public boolean isOpenContrib()
{
return OpenContrib;
}
//---------------------------------------------------------
/**
* @return the LoginFolderType
*/
public String getLoginFolderType()
{
return LoginFolderType;
}
//---------------------------------------------------------
/**
* @return the LoginFields
*/
public Vector<String> getLoginFields()
{
return LoginFields;
}
//---------------------------------------------------------
/**
* @return the DocsReportId
*/
public String getDocsReportId()
{
return DocsReportId;
}
//---------------------------------------------------------
/**
* @return the FieldsByType
*/
private HashMap<String, HashSet> getFieldsByType()
{
return FieldsByType;
}
//---------------------------------------------------------
/**
 * @return the GlobalExcluded
 */
private synchronized static HashSet<String> getGlobalExcluded()
{
if (GlobalExcluded==null)    
    {
    GlobalExcluded=new HashSet();
    GlobalExcluded.add(PDDocs.fACL);
    GlobalExcluded.add(PDDocs.fDOCTYPE);
    GlobalExcluded.add(PDDocs.fLOCKEDBY);
    GlobalExcluded.add(PDDocs.fMIMETYPE);
    GlobalExcluded.add(PDDocs.fNAME);
    GlobalExcluded.add(PDDocs.fPARENTID);
    GlobalExcluded.add(PDDocs.fPDAUTOR);
    GlobalExcluded.add(PDDocs.fPDDATE);
    GlobalExcluded.add(PDDocs.fPDID);
    GlobalExcluded.add(PDDocs.fPURGEDATE);
    GlobalExcluded.add(PDDocs.fREPOSIT);
    GlobalExcluded.add(PDDocs.fSTATUS);
    GlobalExcluded.add(PDDocs.fVERSION);
    }
return GlobalExcluded;
}
//-----------------------------------------------------------------------------------------------
/**
 * Verifies if the field is allowed to be uploaded/filled in the contribution form
 * @param NameDocT Documetn Type
 * @param name Name of Attribute
 * @return true if the Attribute is allowed to be uploaded/filled in the contribution form
 */
public boolean Allowed(String NameDocT, String name)
{
if (getGlobalExcluded().contains(name))
    return(false);
HashSet AllowFields = getFieldsByType().get(NameDocT);    
if (AllowFields==null)
    return(true);
if (AllowFields.contains(name.toUpperCase()))
    return(true);
return(false);
}
//-----------------------------------------------------------------------------------------------
/**
 * Verifies if the file extension is allowed to upload
 * @param NewExt Extension to check
 * @return true if the file extension is allowed to upload
 */
public boolean IsAllowedExt(String NewExt)
{
return (AllowedExt.contains(NewExt));    
}
//-----------------------------------------------------------------------------------------------
/**
 * @return the MaxSize
 */
public int getMaxSize()
{
return MaxSize;
}
//-----------------------------------------------------------------------------------------------

    /**
     * @return the Id
     */
    public String getId()
    {
        return Id;
    }

    /**
     * @return the OKMsg
     */
    public String getOKMsg()
    {
        return OKMsg;
    }
}
