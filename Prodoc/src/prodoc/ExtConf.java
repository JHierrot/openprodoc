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
private final Vector<String> DocTipesList=new Vector();
private final Vector<String> FieldsToInclude=new Vector();
private final Vector<String> FieldsComp=new Vector();
private String BaseFolder=null;
private boolean Inheritance=false;
private final Vector<String> ResultForm=new Vector();
private int MaxResults;
private String FormSearchCSS=null;
private String FormSearchLogo=null;
private String User=null;
private String Pass=null;
private String Title=null;
private String DTLabel=null;
private String FTLabel=null;
private String FormatLabel=null;
private String HelpForDocType=null;
private String HelpForFullText=null;
private String HelpForFormatType=null;
private static final ExtConf DefExtConf=new ExtConf();
private String UrlHelp=null;
private int NumHtmlOpac=0;
private Vector<String[]> ListAgent=null;
private Vector<String> HtmlAgent=null;
private Vector<Boolean> OnColAgent=null;

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
 * Assign a properties for configuration of OPAC
 * @param ProdocProperties properties loaded
 */
public void AssignConf(Properties ProdocProperties)
{
String ConfDocTipesList=ProdocProperties.getProperty("DocTipesList");
if (ConfDocTipesList!=null && ConfDocTipesList.trim().length()!=0)
    {
    String[] DTL = ConfDocTipesList.trim().split("\\|");
    for (int i = 0; i < DTL.length; i++)
        getDocTipesList().add(DTL[i].trim());
    }
String ConfFieldsToInclude=ProdocProperties.getProperty("FieldsToInclude");
if (ConfFieldsToInclude!=null && ConfFieldsToInclude.trim().length()!=0)
    {
    String[] FTL = ConfFieldsToInclude.trim().split("\\|");
    for (int i = 0; i < FTL.length; i++)
        getFieldsToInclude().add(FTL[i].trim());    
    }
String ConfFieldsComp=ProdocProperties.getProperty("FieldsComp");
if (ConfFieldsComp!=null && ConfFieldsComp.trim().length()!=0)
    {
    String[] FTL = ConfFieldsComp.trim().split("\\|");
    for (int i = 0; i < FTL.length; i++)
        getFieldsComp().add(FTL[i].trim());    
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
    String[] FTL = ConfResultForm.trim().split("\\|");
    for (int i = 0; i < FTL.length; i++)
        getResultForm().add(FTL[i].trim());    
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
String ConfFTLabel=ProdocProperties.getProperty("FTLabel");
if (ConfFTLabel!=null && ConfFTLabel.trim().length()!=0)
    {
    FTLabel=ConfFTLabel.trim();
    }    
String ConfFormatLabel=ProdocProperties.getProperty("FormatLabel");
if (ConfFormatLabel!=null && ConfFormatLabel.trim().length()!=0)
    {
    FormatLabel=ConfFormatLabel.trim();
    }    
String ConfHelpForDocType=ProdocProperties.getProperty("HelpForDocType");
if (ConfHelpForDocType!=null && ConfHelpForDocType.trim().length()!=0)
    {
    HelpForDocType=ConfHelpForDocType.trim();
    }    
String ConfHelpForFullText=ProdocProperties.getProperty("HelpForFullText");
if (ConfHelpForFullText!=null && ConfHelpForFullText.trim().length()!=0)
    {
    HelpForFullText=ConfHelpForFullText.trim();
    }    
String ConfHelpForFormatType=ProdocProperties.getProperty("HelpForFormatType");
if (ConfHelpForFormatType!=null && ConfHelpForFormatType.trim().length()!=0)
    {
    HelpForFormatType=ConfHelpForFormatType.trim();
    }    
String ConfUrlHelp=ProdocProperties.getProperty("UrlHelp");
if (ConfUrlHelp!=null && ConfUrlHelp.trim().length()!=0)
    {
    UrlHelp=ConfUrlHelp.trim();
    }    
String ConfNumHtmlOpac=ProdocProperties.getProperty("NumHtmlOpac");
if (ConfNumHtmlOpac!=null && ConfNumHtmlOpac.trim().length()!=0)
    {
    NumHtmlOpac=Integer.parseInt(ConfNumHtmlOpac.trim());
    }    
ListAgent=new Vector(NumHtmlOpac);
HtmlAgent=new Vector(NumHtmlOpac);
for (int NHO = 0; NHO < NumHtmlOpac; NHO++)
    {
    String LAgent=ProdocProperties.getProperty("ListAgent"+NHO);
    if (LAgent!=null && LAgent.trim().length()!=0)
        {
        String[] LA = LAgent.trim().toUpperCase().split("\\|");
        ListAgent.add(LA);
        }    
    String HAgent=ProdocProperties.getProperty("HtmlAgent"+NHO);
    HtmlAgent.add(HAgent);
    }
}
//---------------------------------------------------------------------------- 
/**
 * Calculates a html page for searching documents depending on the browser
 * @param Agent Browser type
 * @return id of Html page to return
 */
public String SolveHtml(String Agent)
{
Agent=Agent.toUpperCase();
for (int NHO = 0; NHO < NumHtmlOpac; NHO++)
    {
    String[] LA =ListAgent.get(NHO);    
    for (int i = 0; i < LA.length; i++)
        {
        String Age = LA[i];
        if (Age.equals("*") || Agent.contains(Age))
           return(HtmlAgent.get(NHO)); 
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
* @return the FieldsToInclude
*/
public Vector<String> getFieldsToInclude()
{
return FieldsToInclude;
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
* @return the Inheritance
*/
public boolean isInheritance()
{
return Inheritance;
}
//----------------------------------------------------------------------------    
/**
* @return the ResultForm
*/
public Vector<String> getResultForm()
{
return ResultForm;
}
//----------------------------------------------------------------------------    
/**
* @return the MaxResults
*/
public int getMaxResults()
{
return MaxResults;
}
//----------------------------------------------------------------------------    
/**
* @return the FormSearchCSS
*/
public String getFormSearchCSS()
{
return FormSearchCSS;
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
* @return the FormSearchLogo
*/
public String getFormSearchLogo()
{
return FormSearchLogo;
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
* @return the DTLabel
*/
public String getDTLabel()
{
return DTLabel;
}
//----------------------------------------------------------------------------    

/**
* @return the FTLabel
*/
public String getFTLabel()
{
return FTLabel;
}

/**
 * @return the FormatLabel
 */
public String getFormatLabel()
{
return FormatLabel;
}

/**
* @return the HelpForDocType
*/
public String getHelpForDocType()
{
return HelpForDocType;
}

/**
* @return the HelpForFullText
*/
public String getHelpForFullText()
{
return HelpForFullText;
}

/**
* @return the HelpForFormatType
*/
public String getHelpForFormatType()
{
return HelpForFormatType;
}

/**
* @return the FieldsComp
*/
public Vector<String> getFieldsComp()
{
return FieldsComp;
}
//---------------------------------------------------------
/**
* @return the UrlHelp
*/
public String getUrlHelp()
{
return UrlHelp;
}
//---------------------------------------------------------
/**
* @return the NumHtmlOpac
*/
public int getNumHtmlOpac()
{
return NumHtmlOpac;
}
//---------------------------------------------------------
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