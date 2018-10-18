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
private static final ContribConf DefExtConf=new ContribConf();
private String UrlHelp=null;
private int NumHtmlContLog=0;
private Vector<String[]> ListAgentLog=null;
private Vector<String> HtmlAgentLog=null;
private int NumHtmlContList=0;
private Vector<String[]> ListAgentList=null;
private Vector<String> HtmlAgentList=null;
private int NumHtmlContAdd=0;
private Vector<String[]> ListAgentAdd=null;
private Vector<String> HtmlAgentAdd=null;
private final HashMap<String, HashSet> FieldsByType=new HashMap();
private static final String FIELDSPREFIX="Fields_";
private static HashSet<String> GlobalExcluded=null;

//----------------------------------------------------------------------------    
static void AssignDefConf(Properties ProdocProperties)
{
DefExtConf.AssignConf(ProdocProperties);
}
//----------------------------------------------------------------------------    
/**
* @return the User
*/
public static String getDefUser()
{
return DefExtConf.getUser();
}
//----------------------------------------------------------------------------    
/**
* @return the Pass
*/
public static String getDefPass()
{
return DefExtConf.getPass();
}
//----------------------------------------------------------------------------    

    /**
     *
     * @param ProdocProperties
     */
public void AssignConf(Properties ProdocProperties)
{
String ConfDocTipesList=ProdocProperties.getProperty("DocTipesList");
if (ConfDocTipesList!=null && ConfDocTipesList.trim().length()!=0)
    {
    String[] DTL = ConfDocTipesList.trim().split("\\|");
    for (String DTL1 : DTL)
        getDocTipesList().add(DTL1.trim());
    }
for (int i = 0; i < getDocTipesList().size(); i++)
    {
    String FieldsType = ProdocProperties.getProperty(FIELDSPREFIX+getDocTipesList().elementAt(i));
    if (FieldsType!=null && FieldsType.trim().length()!=0) 
        {
        String[] FTL = FieldsType.trim().split("\\|");
        HashSet Temp=new HashSet();
        for (String FTL1 : FTL)  
            Temp.add(FTL1.trim().toUpperCase());
        getFieldsByType().put(getDocTipesList().elementAt(i), Temp);
        }
    }
String ConfFieldsToInclude=ProdocProperties.getProperty("FieldsToInclude");
if (ConfFieldsToInclude!=null && ConfFieldsToInclude.trim().length()!=0)
    {
    String[] FTL = ConfFieldsToInclude.trim().split("\\|");
    for (String FTL1 : FTL)  
        getFieldsToRead().add(FTL1.trim());
    }
String ConfOpenContrib=ProdocProperties.getProperty("OpenContrib");
if (ConfOpenContrib!=null && ConfOpenContrib.trim().equals("1"))
    {
    OpenContrib=true;
    }
String ConfLoginFolder=ProdocProperties.getProperty("LoginFolderType");
if (ConfLoginFolder!=null && ConfLoginFolder.trim().length()!=0)
    {
    LoginFolderType=ConfLoginFolder.trim();
    }
String ConfLoginFields=ProdocProperties.getProperty("LoginFields");
if (ConfLoginFields!=null && ConfLoginFields.trim().length()!=0)
    {
    String[] FTL = ConfLoginFields.trim().split("\\|");
    for (String FTL1 : FTL) 
        LoginFields.add(FTL1.trim());
    }
String ConfBaseFolder=ProdocProperties.getProperty("BaseFolder");
if (ConfBaseFolder!=null && ConfBaseFolder.trim().length()!=0)
    {
    BaseFolder=ConfBaseFolder.trim();
    }
String ConfContribCSS=ProdocProperties.getProperty("ContribCSS");
if (ConfContribCSS!=null && ConfContribCSS.trim().length()!=0)
    {
    FormContribCSS=ConfContribCSS.trim();
    }    
String ConfContribLogo=ProdocProperties.getProperty("ContribLogo");
if (ConfContribLogo!=null && ConfContribLogo.trim().length()!=0)
    {
    FormContribLogo=ConfContribLogo.trim();
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
String ConfDocsReportId=ProdocProperties.getProperty("DocsReportId");
if (ConfDocsReportId!=null && ConfDocsReportId.trim().length()!=0)
    {
    DocsReportId=ConfDocsReportId.trim();
    }    
String ConfTitleList=ProdocProperties.getProperty("TitleList");
if (ConfTitleList!=null && ConfTitleList.trim().length()!=0)
    {
    TitleList=ConfTitleList.trim();
    }    
String ConfUrlHelp=ProdocProperties.getProperty("UrlHelp");
if (ConfUrlHelp!=null && ConfUrlHelp.trim().length()!=0)
    {
    UrlHelp=ConfUrlHelp.trim();
    }    
//--------------
String ConfNumHtmlContLog=ProdocProperties.getProperty("NumHtmlContLog");
if (ConfNumHtmlContLog!=null && ConfNumHtmlContLog.trim().length()!=0)
    {
    NumHtmlContLog=Integer.parseInt(ConfNumHtmlContLog.trim());
    }    
ListAgentLog=new Vector(NumHtmlContLog);
HtmlAgentLog=new Vector(NumHtmlContLog);
for (int NHO = 0; NHO < NumHtmlContLog; NHO++)
    {
    String LAgent=ProdocProperties.getProperty("ListAgentLog"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgentLog.add(LA);
        }    
    String HAgent=ProdocProperties.getProperty("HtmlAgentLog"+NHO);
    HtmlAgentLog.add(HAgent);
    }
//--------------
String ConfNumHtmlContList=ProdocProperties.getProperty("NumHtmlContList");
if (ConfNumHtmlContList!=null && ConfNumHtmlContList.trim().length()!=0)
    {
    NumHtmlContList=Integer.parseInt(ConfNumHtmlContList.trim());
    }    
ListAgentList=new Vector(NumHtmlContList);
HtmlAgentList=new Vector(NumHtmlContList);
for (int NHO = 0; NHO < NumHtmlContList; NHO++)
    {
    String LAgent=ProdocProperties.getProperty("ListAgentList"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgentList.add(LA);
        }    
    String HAgent=ProdocProperties.getProperty("HtmlAgentList"+NHO);
    HtmlAgentList.add(HAgent);
    }
//--------------
String ConfNumHtmlContAdd=ProdocProperties.getProperty("NumHtmlContAdd");
if (ConfNumHtmlContAdd!=null && ConfNumHtmlContAdd.trim().length()!=0)
    {
    NumHtmlContAdd=Integer.parseInt(ConfNumHtmlContAdd.trim());
    }    
ListAgentAdd=new Vector(NumHtmlContAdd);
HtmlAgentAdd=new Vector(NumHtmlContAdd);
for (int NHO = 0; NHO < NumHtmlContAdd; NHO++)
    {
    String LAgent=ProdocProperties.getProperty("ListAgentAdd"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgentAdd.add(LA);
        }    
    String HAgent=ProdocProperties.getProperty("HtmlAgentAdd"+NHO);
    HtmlAgentAdd.add(HAgent);
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
 *
 * @param Agent
 * @return
 */
public String SolveHtml(String Agent)
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
* @return the NumHtmlContLog
*/
public int getNumHtmlContLog()
{
return NumHtmlContLog;
}
//---------------------------------------------------------

/**
* @return the OpenContrib
*/
public boolean isOpenContrib()
{
return OpenContrib;
}

/**
* @return the LoginFolderType
*/
public String getLoginFolderType()
{
return LoginFolderType;
}

/**
* @return the LoginFields
*/
public Vector<String> getLoginFields()
{
return LoginFields;
}

/**
* @return the DocsReportId
*/
public String getDocsReportId()
{
return DocsReportId;
}

/**
* @return the FieldsByType
*/
private HashMap<String, HashSet> getFieldsByType()
{
return FieldsByType;
}
    /**
 * @return the GlobalExcluded
 */
private synchronized static HashSet<String> getGlobalExcluded()
{
if (GlobalExcluded==null)    
    {
    GlobalExcluded=new HashSet();
    }
return GlobalExcluded;
}
//-----------------------------------------------------------------------------------------------

public boolean Allowed(String NameDocT, String name)
{
HashSet AllowFields = getFieldsByType().get(NameDocT);    
if (AllowFields!=null && !AllowFields.contains(name.toUpperCase()))
    return(false);
if (getGlobalExcluded().contains(name.toUpperCase()))
    return(false);
return(true);
}
//-----------------------------------------------------------------------------------------------
}

/***************************

#########################################################################
####            OPAC                                                 ####
#########################################################################
OpacActive=1
WebInactive=0
DocTipesList=Article|ECM_Standards|InternetProfile|MusicRecords|Picture
FieldsToInclude=Author|Authors|Keywords|Player|Title|Country|CreativeCommons
## Operators for search. 1 for each field. Default EQ
## = EQ, <> NE, > GT, >= GE, < LT, <= LE, Contains CT 
FieldsComp=EQ|EQ|EQ|EQ|CT|EQ|EQ
// BaseFolder=/Examples - Ejemplos
BaseFolder=/
Inheritance=1
ResultForm=150c9be080c-3fe46f69eb1b2cb7|150c9be8462-3fd76612bb72fece
MaxResults=0
FormSearchCSS=15db73b6628-3fee99cd40e27fee
#FormSearchLogo=SendDoc?Id=15d8a225786-3fe11b750cd2517e
FormSearchLogo=img/LogoProdoc.jpg
User=root
Pass=root
Title=Punto de Consulta Simplificada
DTLabel=Seleccionar tipo de documento a buscar
FTLabel=Introducir algunas palabras de b&uacute;squeda
FormatLabel=Seleccionar formato de salida de los resultados
HelpForDocType=Ayuda Tipos Documentales
HelpForFullText=Ayuda B&uacute;squeda Texto Completo<br>Introduzca cualquier palabra(s) del contenido de documento para recuperar por criterios aproximados. Si se desea que el docuemnto contenga la palabra, debe incluirse un signo +. Si desea buscar literalmente debe incluirse la palabra entre comillas. Para buscar documentos que NO contengan una palabra debe incluirse un signo menos -<br>Puede combinarse con la b&uacute;sqeda por metadatos de los documentos.
HelpForFormatType=Ayuda Formatos Salida
UrlHelp=help/EN/HelpIndex.html

*****************************/