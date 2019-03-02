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
package prodoc;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static prodoc.PDFolders.ROOTFOLDER;
import static prodoc.PDFolders.SYSTEMFOLDER;
import static prodoc.PDFolders.getTableName;
import prodoc.security.*;

/**
 * Abstract class that represent a session that manages  the access to ALL the data and metadata, 
 * by means of JDBC or a remote connection, both implemented as subclasses
 * @author jhierrot
 */
abstract public class DriverGeneric
{
/**
 * Url of the connection
 */
private String URL;
/**
 * Aditional param
 */
private String PARAM;
/**
 * Connection user to the Database
 */
private String DBUser;
/**
 * Password of the database
 */
private String DBPassword;
/**
 * When true, the session is locked by an user/thread/http session
 */
private boolean Locked=false;
/**
 * Last time the session was used
 */
private Date TimeUsed;
/**
 * Time when teh session was locked
 */
private Date TimeLocked;
/**
 * OpenProdoc user owner/authenticated in the session
 */
private PDUser User=null;
/**
 * The session has a transaction started
 */
private boolean InTransaction=false;
/**
 * Container for all the opened cursors, with key=CursorId and Value=Cursor
 */
private HashMap<String, Cursor> OpenCur=null;
/**
 * Contaner for cache of Repositories by type, with key=DocType and Value=RepositName
 */
private HashMap<String, String> Class2Reposit=null;
//private HashMap ListReposit=null;
/**
 * Container for all the instantiated authenticators, with key=AuthName and Value=Authenticater
 */
private HashMap<String, AuthGeneric> ListAuth=null;
/**
 * Encription default key
 */
private final char[] ServerKey = "Esta es la clave".toCharArray();
/**
 * Container for all the translations, with key=language and Value=properties with trnaslated values
 */
private static HashMap<String, Properties> TransList=new HashMap();
/**
 * Language of the session for translation
 */
private String AppLang=null;
/**
 * Defaut laanguage for translation
 */
private static String DefAppLang=null;
/**
 * Customization assigned to session (current user)
 */
private PDCustomization PDCust=null;
/**
 * Order for remote session
 */
static final public String S_LOGIN   ="LOGIN";  // Ok
/**
 * Order for remote session
 */
static final public String S_LOGOUT  ="LOGOUT"; // Ok
/**
 * Order for remote session
 */
static final public String S_SELECT  ="SELECT";  // Ok
/**
 * Order for remote session
 */
static final public String S_INSERT  ="INSERT";  // Ok
/**
 * Order for remote session
 */
static final public String S_DELETE  ="DELETE";   // Ok
/**
 * Order for remote session
 */
static final public String S_UPDATE  ="UPDATE";   // Ok
/**
 * Order for remote session
 */
static final public String S_CREATE  ="CREATE";    // Ok
/**
 * Order for remote session
 */
static final public String S_DROP    ="DROP";     // Ok
/**
 * Order for remote session
 */
static final public String S_ALTER   ="ALTER";    // Ok
/**
 * Order for remote session
 */
static final public String S_ALTERDEL="ALTERDEL";   // Ok
/**
 * Order for remote session
 */
static final public String S_INTEGRIT="INTEGRIT";  // Ok
/**
 * Order for remote session
 */
static final public String S_INTEGRIT2="INTEGRIT2";  // Ok
/**
 * Order for remote session
 */
static final public String S_INITTRANS="INITTRANS";  // Ok
/**
 * Order for remote session
 */
static final public String S_COMMIT   ="COMMIT";    // Ok
/**
 * Order for remote session
 */
static final public String S_CANCEL   ="CANCEL";    // Ok
/**
 * Order for remote session
 */
static final public String S_UNLOCK   ="UNLOCK"; // Ok
/**
 * Order for remote session
 */
static final public String S_DELFILE   ="DELFILE";    
/**
 * Order for remote session
 */
static final public String S_RENFILE   ="RENFILE";    
/**
 * Order for remote session
 */
static final public String S_RETRIEVEFILE   ="RETRIEVEFILE";    
/**
 * Order for remote session
 */
static final public String S_INSFILE   ="INSFILE";    
/**
 * Order for remote session
 */
static final public String S_FTINS   ="FTINS";    
/**
 * Order for remote session
 */
static final public String S_FTUPD   ="FTUPD";    
/**
 * Order for remote session
 */
static final public String S_FTDEL   ="FTDEL";    
/**
 * Order for remote session
 */
static final public String S_FTSEARCH ="FTSEARCH";    
/**
 * ORDERED Container of all the transactional Tasks, with key=Doctype+event+order and value=Tasks definition
 */
private TreeMap<String, PDTasksDefEvent> AllTaskTrans=null;
/**
 * ORDERED Container of all the NON-transactional Tasks, with key=Doctype+event+order and value=Tasks definition
 */
private TreeMap<String, PDTasksDefEvent> AllTaskNoTrans=null;
/**
 * Connector for FullText indexing
 */
protected static FTConnector FTConn=null;
/**
 * Defualt constructor
 * @param pURL    url of database or OPD server when remote
 * @param pPARAM  Additional params
 * @param pUser   User of connnection
 * @param pPassword password of connection
 */
public DriverGeneric(String pURL, String pPARAM, String pUser, String pPassword)
{
URL=pURL.trim();
PARAM=pPARAM.trim();
DBUser=pUser.trim();
DBPassword=Decode(pPassword);
}
//--------------------------------------------------------------------------
/**
 * @return the URL
 */
public String getURL()
{
return URL;
}
//--------------------------------------------------------------------------
/**
 * @return the PARAM
 */
public String getPARAM()
{
return PARAM;
}
//--------------------------------------------------------------------------
/**
 * @return the DBUser
 */
public String getDBUser()
{
return DBUser;
}
//--------------------------------------------------------------------------
/**
 * @return the DBPassword
 */
protected String getDBPassword()
{
return DBPassword;
}
//--------------------------------------------------------------------------
/**
 * Returns true when the session is locked (by a thread/http session)
 * @return true when the session is locked (by a thread/http session)
 */
public boolean isLocked()
{
return(Locked);
}
//--------------------------------------------------------------------------
/**
 * Locks the session, setting the timestamp 
 */
public void Lock()
{
Locked=true;
TimeLocked=new Date();
}
//--------------------------------------------------------------------------
/**
 * Unlocks the session
 */
public void UnLock()
{
Locked=false;
TimeLocked=null;
}
//--------------------------------------------------------------------------
/**
 * Returns the last tme the session was used
 * @return the TimeUsed
 */
public Date getTimeUsed()
{
return TimeUsed;
}
//--------------------------------------------------------------------------
/**
 * Returns when the session was locked
 * @return the TimeLocked
 */
public Date getTimeLocked()
{
return TimeLocked;
}
//--------------------------------------------------------------------------
/**
 * Deletes e sesion
 * @throws PDException in any error
 */
abstract public void delete() throws PDException;
//--------------------------------------------------------------------------
/**
 * Returns if the session is connected
 * @return true when session is connected
 */
abstract public boolean isConnected();
//--------------------------------------------------------------------------
/**
 * Creates a table for storing all or part of the mmetadata of the docuemnt type
 * @param TableName name of the table
 * @param Fields    Record with the attributes
 * @throws PDException in any error
 */
abstract protected void CreateTable(String TableName, Record Fields) throws PDException;
//--------------------------------------------------------------------------
/**
 * deletes a table
 * @param TableName Name of the table to drop
 * @throws PDException in any error
 */
abstract protected void DropTable(String TableName) throws PDException;
//--------------------------------------------------------------------------
/**
 * Modifies a table adding an Attribute
 * @param TableName name of the table
 * @param NewAttr New field to add
 * @param IsVer indictes if the table is a version table in order to add idex or not
 * @throws PDException in any error
 */
abstract protected void AlterTableAdd(String TableName, Attribute NewAttr, boolean IsVer) throws PDException;
//--------------------------------------------------------------------------
/**
 * Modifies a table removing an Attribute
 * @param TableName Name of the table
 * @param OldAttr old field to delete
 * @throws PDException in any error
 */
abstract protected void AlterTableDel(String TableName, String OldAttr) throws PDException;
//--------------------------------------------------------------------------
/**
 * Inserts a row in the table
 * @param TableName Name of the table
 * @param Fields   Attributs to add
 * @throws PDException in any error
 */
abstract protected void InsertRecord(String TableName, Record Fields) throws PDException;
//--------------------------------------------------------------------------
/**
 * Deletes a row (or several)
 * @param TableName Name of the table
 * @param DelConds conditions (where) for deleting
 * @throws PDException in any error
 */
abstract protected void DeleteRecord(String TableName, Conditions DelConds) throws PDException;
//--------------------------------------------------------------------------
/**
 * Updates a rw 8or several)
 * @param TableName Name of the table
 * @param NewFields new values so assign
 * @param UpConds   conditions (where) for updating
 * @throws PDException in any error
 */
abstract protected void UpdateRecord(String TableName, Record NewFields, Conditions UpConds) throws PDException;
//--------------------------------------------------------------------------
/**
 * Adds referential integrity (foreign key) between tables
 * @param TableName1 Name of the Table Origin
 * @param Field1   Name of Field Origin
 * @param TableName2 Name of the Table Target
 * @param Field2   Name of Field Target
 * @throws PDException  in any error
 */
abstract protected void AddIntegrity(String TableName1, String Field1, String TableName2, String Field2) throws PDException;
//--------------------------------------------------------------------------
/**
 * Adds referential integrity (foreign key) between tables combining fields
 * @param TableName1 Name of the Table Origin
 * @param Field11  Name of Field1 Origin
 * @param Field12  Name of Field2 Origin
 * @param TableName2 Name of the Table Target
 * @param Field21  Name of Field1 Target
 * @param Field22  Name of Field2 Target
 * @throws PDException  in any error
 */
abstract protected void AddIntegrity(String TableName1, String Field11, String Field12, String TableName2, String Field21, String Field22) throws PDException;
/**
 * Installs OpenProdoc in the current/configured session database, creating all the datamodel
 * and inserting the base definitions and elements (document types, users, folders, roles, groups, acl,...)
 * @param RootPassword  Password for "root" user
 * @param DefLang       Default language
 * @param RepUrl        Url of the default documents repository
 * @param DefTimeFormat Default TimeStamp format for Swing interface 
 * @param DefDateFormat Default Date format for Swing interface 
 * @param RepName       name of the default repository
 * @param MainKey       Key for encription oof documents
 * @param RepEncrypt    When true, the repository is encripted
 * @param Trace         Vector for storing the messages of the installation in order to show the evolution or final result of the installation
 * @param RepUser       Name of the Repository user (i.e. name for a database-blob repository)
 * @param RepParam      Additional parameter of the repository
 * @param RepType       Kind of rspository (FileSystem, blob, S3, Custom,..)
 * @param RepPassword   Repository Password
 * @throws PDException in any error
 */
public void Install(String RootPassword, String DefLang, String DefTimeFormat, 
                    String DefDateFormat, String MainKey, String RepName,
                    boolean RepEncrypt, String RepUrl, String RepUser,
                    String RepPassword, String RepType, String RepParam, Vector<String> Trace) throws PDException
{
//-------- Tables creation ---------------------------------------------------
Trace.add("Starting Installation....");
PDServer Se=new PDServer(this);
Se.Install();
Trace.add("Server Table created");
PDRoles Ro=new PDRoles(this);
Ro.Install();
Trace.add("Roles Table created");
PDCustomization Cu=new PDCustomization(this);
Cu.Install();
Trace.add("Customization Table created");
PDAuthenticators Au=new PDAuthenticators(this);
Au.Install();
Trace.add("Authenticators Table created");
//PDUser U=new PDUser(this);
getUser().Install();
PDACL A=new PDACL(this);
A.Install();
Trace.add("ACL Table created");
PDGroups G=new PDGroups(this);
G.Install();
Trace.add("Groups Table created");
PDRepository R=new PDRepository(this);
R.Install();
Trace.add("Repository Table created");
PDObjDefs D=new PDObjDefs(this);
D.Install();
Trace.add("Definitions Table created");
//PDFolders F=new PDFolders(this);
PDFolders.Install(this);
Trace.add("Folder Table created");
PDMimeType M=new PDMimeType(this);
M.Install();
Trace.add("MimeType Table created");
PDDocs DD=new PDDocs(this);
DD.Install();
Trace.add("Doces Table created");
PDTrace T=new PDTrace(this);
T.Install();
Trace.add("Trace Table created");
PDEvent E=new PDEvent(this);
E.Install();
Trace.add("Event Table created");
PDMessage ME=new PDMessage(this);
ME.Install();
Trace.add("Message Table created");
PDThesaur TE=new PDThesaur(this);
TE.Install();
Trace.add("Thesaur Table created");
PDTasksDefEvent TDE=new PDTasksDefEvent(this);
TDE.Install();
Trace.add("PDTasksDefEvent Table created");
PDTasksCron TDC=new PDTasksCron(this);
TDC.Install();
Trace.add("PDTasksCron Table created");
PDTasksExec TEx=new PDTasksExec(this);
TEx.Install();
Trace.add("PDTasksExec Table created");
PDTasksExecEnded TExEnd=new PDTasksExecEnded(this);
TExEnd.Install();
Trace.add("PDTasksExecEnded Table created");
//-------- Tables creation 2 phase -------------------------------------
getUser().InstallMulti();
Trace.add("User relations created");
G.InstallMulti();
Trace.add("Groups relations created");
A.InstallMulti();
Trace.add("ACL relations created");
R.InstallMulti();
Trace.add("Repository relations created");
PDFolders.InstallMulti(this);
Trace.add("Folders relations created");
DD.InstallMulti();
Trace.add("Docs relations created");
D.InstallMulti();
Trace.add("Definitions relations created");
M.InstallMulti();
Trace.add("Mime relations created");
ME.InstallMulti();
Trace.add("Message relations created");
PDThesaur.InstallMulti(this);
Trace.add("Thesaur relations created");
TDE.InstallMulti();
Trace.add("PDTasksDefEvent relations created");
TDC.InstallMulti();
Trace.add("PDTasksCron relations created");
TEx.InstallMulti();
Trace.add("PDTasksExec relations created");
TExEnd.InstallMulti();
Trace.add("PDTasksExecEnded relations created");
//--- insertion of objects --------------------------------------------
this.IniciarTrans(); 
//----- Servidor ---------
Se.setName("Prodoc");
Se.setKey(Encode(MainKey));
Se.setVersion(getVersion());
Se.insert();
Trace.add("Server element created");
//----- Autorization ---------
Au.setName("Prodoc");
Au.setDescription("Default Validation in this system");
Au.setAuthType(PDAuthenticators.tOPD);
Au.insert();
Trace.add("Authenticator element created");
//----- Styles ---------
Cu.setName("Prodoc");
Cu.setDescription("Prodoc default style");
Cu.setDateFormat(DefDateFormat); // "dd-MM-yyyy"
Cu.setTimeFormat(DefTimeFormat); // "dd-MM-yyyy HH:mm:ss"
Cu.setLanguage(DefLang);
Cu.setStyle("");
Cu.setSwingStyle("\"Arial\",0,12,\"Arial\",0,12,\"Arial\",0,12,\"Arial\",0,12");
Cu.insert();
Cu.setName("ProdocAdm");
Cu.setDescription("Prodoc Admin style");
Cu.setStyle("adm");
Cu.insert();
Trace.add("Customization element created");
//----- Roles ---------
Ro.setName("Users");
Ro.setDescription("Perfil de usuarios estándar");
Ro.setAllowCreateFolder(true);
Ro.setAllowMaintainFolder(true);
Ro.setAllowCreateDoc(true);
Ro.setAllowMaintainDoc(true);
Ro.insert();
Ro.setName("Administrators");
Ro.setDescription("Perfil de usuarios administradores");
Ro.setAllowCreateUser(true);
Ro.setAllowMaintainUser(true);
Ro.setAllowCreateGroup(true);
Ro.setAllowMaintainGroup(true);
Ro.setAllowCreateAcl(true);
Ro.setAllowMaintainAcl(true);
Ro.setAllowCreateRole(true);
Ro.setAllowMaintainRole(true);
Ro.setAllowCreateObject(true);
Ro.setAllowMaintainObject(true);
Ro.setAllowCreateRepos(true);
Ro.setAllowMaintainRepos(true);
Ro.setAllowCreateFolder(true);
Ro.setAllowMaintainFolder(true);
Ro.setAllowCreateDoc(true);
Ro.setAllowMaintainDoc(true);
Ro.setAllowCreateMime(true);
Ro.setAllowMaintainMime(true);
Ro.setAllowCreateAuth(true);
Ro.setAllowMaintainAuth(true);
Ro.setAllowCreateCustom(true);
Ro.setAllowMaintainCustom(true);
Ro.setAllowCreateThesaur(true);
Ro.setAllowMaintainThesaur(true);
Ro.setAllowCreateTask(true);
Ro.setAllowMaintainTask(true);
Ro.insert();
Trace.add("Roles elements created");
// --- Acl Administrators ---
PDACL AclAdmin=new PDACL(this);
AclAdmin.setName("Administrators");
AclAdmin.setDescription("Permision to maintain Administrators");
AclAdmin.insert();
// --- Acl Public  ---
PDACL Aclpublic=new PDACL(this);
Aclpublic.setName("Public");
Aclpublic.setDescription("Public Information");
Aclpublic.insert();
// --- Acl DocsCommon  ---
PDACL DocsCommon=new PDACL(this);
DocsCommon.setName("DocsCommon");
DocsCommon.setDescription("Public Docs Types");
DocsCommon.insert();
Trace.add("ACL elements created");
// --- Group all ---
PDGroups Grp=new PDGroups(this);
Grp.setName("All");
Grp.setDescription("All users group");
Grp.setAcl("Public");
Grp.insert();
Grp.setName("Administrators");
Grp.setDescription("Users with administration rights");
Grp.setAcl("Public");
Grp.insert();
Trace.add("Groups elements created");
//---- Definitions folder -----------------------
D.setName(PDFolders.getTableName());
D.setDescription("Base Folder");
D.setActive(true);
D.setClassType(PDObjDefs.CT_FOLDER);
D.setParent(PDFolders.getTableName());
D.setACL(DocsCommon.getName());
D.insert();
Record Rec=PDFolders.getRecordStructPDFolder();
Rec.initList();
for (int i = 0; i < Rec.NumAttr(); i++)
    {
    D.addAtribute(Rec.nextAttr());
    }
Trace.add("Definitions elements created");
// --- Folders -----------------------------------------
PDFolders.CreateBaseRootFold(this); //and all the system folders
// --- Administrator ---
PDUser Usu=new PDUser(this);
Usu.setName("root");
Usu.setPassword(RootPassword);
Usu.setDescription("Administrator");
Usu.setActive(true);
Usu.setValidation(Au.getName());
Usu.setRole(Ro.getName());
Usu.setCustom(Cu.getName()); // ProdocAdm
Usu.insert();
Grp.addUser("root");
Trace.add("Administrator created");

// --- Group Administrator ---
AclAdmin.addUser( "root", PDACL.pDELETE);
AclAdmin.addGroup("Administrators", PDACL.pDELETE);
Aclpublic.addUser( "root", PDACL.pDELETE);
Aclpublic.addGroup("Administrators", PDACL.pDELETE);
Aclpublic.addGroup("All", PDACL.pREAD);
DocsCommon.addGroup("Administrators", PDACL.pDELETE);
DocsCommon.addGroup("All", PDACL.pUPDATE);
R.setName(RepName);
R.setDescription("Repositorio por defecto");
R.setEncrypted(RepEncrypt);
R.setURL(RepUrl);
R.setUser(RepUser);
R.setPassword(Encode(RepPassword));
R.setRepType(RepType);
R.setParam(RepParam);
R.insert();
Trace.add("Repository element created");
//---- Definitions Doc -----------------
D.setName(PDDocs.getTableName());
D.setDescription("Base Document");
D.setActive(true);
D.setClassType(PDObjDefs.CT_DOC);
D.setParent(PDDocs.getTableName());
D.setACL(DocsCommon.getName());
D.setReposit(R.getName());
D.insert();
Record RecD=DD.getRecordStruct();
RecD.initList();
for (int i = 0; i < RecD.NumAttr(); i++)
    {
    D.addAtribute(RecD.nextAttr());
    }
Trace.add("Document elements created");
//----------MIME Types -------------------------------------------
File FileImp=new File("ex/defs.opd");
ProcessXML(FileImp, PDFolders.ROOTFOLDER);
Trace.add("MIME types created");
//--- Creating Reposit Complete Type -------------
FileImp=new File("ex/PD_REPOSIT_URL.opd");
ProcessXML(FileImp, PDFolders.ROOTFOLDER);
//FileImp=new File("ex/PD_FTRep.opd");
//ProcessXML(FileImp, PDFolders.ROOTFOLDER);
//--- Creating Reports Type -------------
FileImp=new File("ex/PD_REPORTS.opd");
ProcessXML(FileImp, PDFolders.ROOTFOLDER);
D.CreateObjectTables(PDReport.REPTABNAME, false); 
FileImp=new File("ex/PD_REP_EXA_TXT.opd");
ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
FileImp=new File("ex/PD_REP_EXA_CSV.opd");
ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
FileImp=new File("ex/PD_REP_EXA_HTML.opd");
ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
//FileImp=new File("ex/PD_REP_EXA_XML.opd");
//ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
Trace.add("Reports Type and Examples created");
//--- Creating RIS Reassign Type -------------
FileImp=new File("ex/PD_TASKSFTDEFEVEN.opd");
ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);  
Trace.add("Full Text Tasks Added (Disabled)");
//--- Creating RIS Reassign Type -------------
FileImp=new File("ex/PD_RIS_COMP.opd");
ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
FileImp=new File("ex/PD_RIS_REASIG.opd");
ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
FileImp=new File("ex/PD_REP_EXA_RIS.opd");
ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
Trace.add("RIS types created");
TE.CreateRootThesaur(DefLang);
//-----------------------
Trace.addAll(ImportThes("ex/Thes"));
Trace.add("Thesaurus imported");
//-----------------------
Trace.addAll(ImportDefs("ex/Def", true));
Trace.add("Custom Definitions imported");
//-----------------------
PDFolders FoldRoot=new PDFolders(this);
FoldRoot.Load(PDFolders.ROOTFOLDER);
File FDef=new File("ex/Obj");
File[] ListDefs = FDef.listFiles();
if (ListDefs!=null)
    {
    for (File ListDef : ListDefs)
        ImportFolder(FoldRoot, ListDef.getAbsolutePath(), false, true, true, PDFolders.getTableName(), PDDocs.getTableName(), false);
    }
Trace.add("Custom Objects imported: Folders="+getImpFolds()+" Docs="+getImpDocs());
//-----------------------
CerrarTrans();
Trace.add("Installation finished");
}
//--------------------------------------------------------------------------
/**
 * Updates the version and structure of repository database
 * @param UpMetadataInc when tru, the updating will be incremental, doing as much as posible, even afcter error, otherwise the process stops at the first error
 * @param Trace Vector for storing the messages of the installation in order to show the evolution or final result of the installation
 * @throws PDException In any error
 */
public void Update(boolean UpMetadataInc, Vector<String> Trace)  throws PDException
{
PDServer Serv=new PDServer(this);
Serv.Load("Prodoc");
if (Serv.getVersion().equalsIgnoreCase("2.1"))
    {
    Trace.add("NO Update possible. Already 2.1 version");    
    return;
    }
Trace.add("Update started");    
if (Serv.getVersion().equalsIgnoreCase("0.7")) //******************************
    {
    // -- Adding Thesaurus tables -----------------------------------------    
    PDThesaur TE=new PDThesaur(this);
    try {
    TE.Install();
    Trace.add("Thesaur Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    PDThesaur.InstallMulti(this);
    Trace.add("Thesaur relations created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    TE.CreateRootThesaur("EN");
    Trace.add("Root Term created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Adding new Roles attributes -------------------------------------    
    PDRoles Rup=new PDRoles(this);
    Attribute Attr1=Rup.getRecordStruct().getAttr(PDRoles.fALLOWCREATETHESAUR).Copy();
    Attr1.setValue(false);
    try {
    AlterTableAdd(PDRoles.getTableName(), Attr1, false);
    Trace.add(PDRoles.fALLOWCREATETHESAUR+" created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Attribute Attr2=Rup.getRecordStruct().getAttr(PDRoles.fALLOWMAINTAINTHESAUR).Copy();
    try {
    Attr2.setValue(false);
    AlterTableAdd(PDRoles.getTableName(), Attr2, false);
    Trace.add(PDRoles.fALLOWMAINTAINTHESAUR+" created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating administrators Roles granting new attributes ----------------    
    try {
    Record NewAdmvals=new Record();
    Attr1.setValue(true);
    NewAdmvals.addAttr(Attr1);
    Attr2.setValue(true);
    NewAdmvals.addAttr(Attr2);
    Conditions AdmRole=new Conditions();
    AdmRole.addCondition(new Condition(Rup.fNAME, Condition.cEQUAL, "Administrators"));
    UpdateRecord(Rup.getTabName(), NewAdmvals, AdmRole);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating Repository version ------------------------------------------    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("0.8");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(Serv.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Update ended ----
    Trace.add("Updated to 0.8");
    }
Serv.Load("Prodoc"); 
if (Serv.getVersion().equalsIgnoreCase("0.8")  //*****************************
    || Serv.getVersion().equalsIgnoreCase("0.8.1"))
    {
    // -- Adding Task tables ----------------------------------    
    PDTasksCron TC=new PDTasksCron(this);
    try {
    TC.Install();
    Trace.add("TasksCron Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    PDTasksDefEvent TE=new PDTasksDefEvent(this);
    try {
    TE.Install();
    Trace.add("Tasks Event Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    PDTasksExec TExec=new PDTasksExec(this);
    try {
    TExec.Install();
    Trace.add("Tasks Pending Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    PDTasksExecEnded TExecEnd=new PDTasksExecEnded(this);
    try {
    TExecEnd.Install();
    Trace.add("Tasks Ended Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Adding new Roles attributes -----------------------------    
    PDRoles Rup=new PDRoles(this);
    Attribute Attr1=Rup.getRecordStruct().getAttr(PDRoles.fALLOWCREATETASK).Copy();
    Attr1.setValue(false);
    try {
    AlterTableAdd(PDRoles.getTableName(), Attr1, false);
    Trace.add(PDRoles.fALLOWCREATETASK+" created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Attribute Attr2=Rup.getRecordStruct().getAttr(PDRoles.fALLOWMAINTAINTASK).Copy();
    try {
    Attr2.setValue(false);
    AlterTableAdd(PDRoles.getTableName(), Attr2, false);
    Trace.add(PDRoles.fALLOWMAINTAINTASK+" created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating administrators Roles granting new attributes -----    
    try {
    Record NewAdmvals=new Record();
    Attr1.setValue(true);
    NewAdmvals.addAttr(Attr1);
    Attr2.setValue(true);
    NewAdmvals.addAttr(Attr2);
    Conditions AdmRole=new Conditions();
    AdmRole.addCondition(new Condition(Rup.fNAME, Condition.cEQUAL, "Administrators"));
    UpdateRecord(Rup.getTabName(), NewAdmvals, AdmRole);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating Repository version -----    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("1.0");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(PDServer.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Update ended ----
    Trace.add("Updated to 1.0");    
    }
Serv.Load("Prodoc"); 
if (Serv.getVersion().equalsIgnoreCase("1.0"))
    {
    //--- Update ended ----    
    PDTrace TR=new PDTrace(this);
    Attribute Attr1=TR.getRecordStruct().getAttr(PDTrace.fRESULT).Copy();
    Attr1.setValue(false);
    try {
    AlterTableAdd(PDTrace.getTableName(), Attr1, false);
    Trace.add(PDTrace.fRESULT+" created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating Repository version -----    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("1.1");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(PDServer.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Update ended ----
    Trace.add("Updated to 1.1");
    }
Serv.Load("Prodoc");
if (Serv.getVersion().equalsIgnoreCase("1.1") || Serv.getVersion().equalsIgnoreCase("1.0"))
    {
    try {
    PDFolders f3=new PDFolders(this);
    f3.setFolderType(getTableName());
    f3.setPDId(SYSTEMFOLDER);
    f3.setTitle(SYSTEMFOLDER);
    f3.setParentId(ROOTFOLDER);
    f3.setACL("Public");
    f3.insert();
        } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    PDMimeType M=new PDMimeType(this);
    File FileImp=new File("ex/PD_REPORTS.opd");
    PDObjDefs D=new PDObjDefs(this);
    try {
    ProcessXML(FileImp, PDFolders.ROOTFOLDER);
    D.CreateObjectTables(PDReport.REPTABNAME, false); 
    FileImp=new File("ex/PD_REP_EXA_TXT.opd");
    ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
        } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    M.setName("csv");
    M.setMimeCode("text/csv");
    M.setDescription("text/csv");
    M.insert();
    Trace.add("MimeType CSV created");  
        } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    FileImp=new File("ex/PD_REP_EXA_CSV.opd");
    ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
    FileImp=new File("ex/PD_REP_EXA_HTML.opd");
    ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
        } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    M.setName("xml");
    M.setMimeCode("application/xml");
    M.setDescription("application/xml");
    M.insert();
    Trace.add("MimeType XML created");  
        } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Trace.add("Reports Type and Examples created");
    //--- Creating RIS Complete Type -------------
    try {
    FileImp=new File("ex/PD_REPOSIT_URL.opd");
    ProcessXML(FileImp, PDFolders.ROOTFOLDER);
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Creating RIS Complete Type -------------
    try {
    M.setName("ris");
    M.setMimeCode("application/x-research-info-systems");
    M.setDescription("application/x-research-info-systems");
    M.insert();
    Trace.add("MimeType RIS created");  
        } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    FileImp=new File("ex/PD_RIS_COMP.opd");
    ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);
    try {
    D.CreateObjectTables("RIS_Complete", false);    
        } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Creating RIS Reassign Type -------------
    try {
    FileImp=new File("ex/PD_RIS_REASIG.opd");
    ProcessXML(FileImp, PDFolders.ROOTFOLDER);
    D.CreateObjectTables("RIS_Reasign", false);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    FileImp=new File("ex/PD_REP_EXA_RIS.opd");
    ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);    
    Trace.add("RIS types created");  
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Full text Reposit -----------
    try {
    PDRepository R=new PDRepository(this);
    R.Load("Reposit");
    if (R.getURL().equals("Rep")) // es Portable
        {
        FileImp=new File("ex/PD_FTRep.opd");
        ProcessXML(FileImp, PDFolders.ROOTFOLDER);
        FileImp=new File("./RepFT");
        FileImp.mkdir();
        Trace.add("Full Text Repository Created");
        FileImp=new File("ex/PD_TASKSFTDEFEVEN_AC.opd");
        ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);  
        Trace.add("Full Text Tasks Added");
        }
    else
        {
        FileImp=new File("ex/PD_TASKSFTDEFEVEN.opd");
        ProcessXML(FileImp, PDFolders.SYSTEMFOLDER);  
        Trace.add("Full Text Tasks Added (Disabled)");
        }
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
        // -- Updating Repository version -----    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("1.2");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(PDServer.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Trace.add("Updated to 1.2");
    }
Serv.Load("Prodoc");
if (Serv.getVersion().equalsIgnoreCase("1.2") || Serv.getVersion().equalsIgnoreCase("1.2.1"))
    {
        // -- Updating Repository version -----    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("2.0");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(PDServer.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Trace.add("Updated to 2.0");    
    }
Serv.Load("Prodoc");
if (Serv.getVersion().equalsIgnoreCase("2.0") )
    {
        // -- Updating Repository version -----    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("2.1");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(PDServer.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Trace.add("Updated to 2.1");    
    }
Trace.add("Update finished");
}
//--------------------------------------------------------------------------
/**
 * Uninstall OpenProdoc from the database
 * @throws PDException In any Error
 */
public void Uninstall() throws PDException
{
PDACL A=new PDACL(this);
try {
A.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando ACL:"+pDException.getLocalizedMessage());
    }
PDDocs DD=new PDDocs(this);
try {
DD.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Documentos:"+pDException.getLocalizedMessage());
    }
PDFolders F=new PDFolders(this);
try {
F.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Definiciones Carpetas:"+pDException.getLocalizedMessage());
    }
PDObjDefs D=new PDObjDefs(this);
try {
D.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Definiciones Documentos:"+pDException.getLocalizedMessage());
    }
PDGroups G=new PDGroups(this);
try {
G.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Grupos:"+pDException.getLocalizedMessage());
    }
PDUser U=new PDUser(this);
try {
    U.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando usuario:"+pDException.getLocalizedMessage());
    }
PDMimeType M=new PDMimeType(this);
try {
M.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Tipos Mime:"+pDException.getLocalizedMessage());
    }
PDRepository R=new PDRepository(this);
try {
R.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Repositorio:"+pDException.getLocalizedMessage());
    }
PDRoles Ro=new PDRoles(this);
try {
Ro.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Roles:"+pDException.getLocalizedMessage());
    }
//--- uninstall 2 phase ---
try {
PDAuthenticators Au=new PDAuthenticators(this);
Au.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Autentificadores:"+pDException.getLocalizedMessage());
    }
try {
PDTrace T=new PDTrace(this);
T.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando traza:"+pDException.getLocalizedMessage());
    }
try {
PDEvent E=new PDEvent(this);
E.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Eventos:"+pDException.getLocalizedMessage());
    }
try {
PDMessage ME=new PDMessage(this);
ME.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Mensajes:"+pDException.getLocalizedMessage());
    }
try {
DD.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Documentos 2:"+pDException.getLocalizedMessage());
    }
try {
F.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Carpetas 2:"+pDException.getLocalizedMessage());
    }
try {
D.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Definiciones Documentos 2:"+pDException.getLocalizedMessage());
    }
try {
M.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Tipos Mime 2:"+pDException.getLocalizedMessage());
    }
try {
G.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Grupos 2:"+pDException.getLocalizedMessage());
    }
try {
A.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando ACL 2:"+pDException.getLocalizedMessage());
    }
try {
U.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando usuario 2:"+pDException.getLocalizedMessage());
    }
try {
R.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Repositorio:"+pDException.getLocalizedMessage());
    }
try {
Ro.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Roles:"+pDException.getLocalizedMessage());
    }
}
//--------------------------------------------------------------------------
/**
 * When true the session is in a transaction
 * @return the InTransaction
 */
public boolean isInTransaction()
{
return InTransaction;
}
//--------------------------------------------------------------------------
/**
 * Assign the transaction status to a the session
 * @param pInTransaction boolean value indicating the status
 */
public void setInTransaction(boolean pInTransaction)
{
InTransaction = pInTransaction;
}
//--------------------------------------------------------------------------
/**
 * Ends/commit a transaction
 * @throws PDException In any error
 */
abstract public void IniciarTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 * Starts a transaction
 * @throws PDException In any error
 */
abstract public void CerrarTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 * Stops/roolback a transaction
 * @throws PDException In any error
 */
abstract public void AnularTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 * Opens a cursor with the information included in Query
 * @param Search Query information
 * @return a CursorCode, stored by the drivers
 * @throws PDException In any error
 */
abstract protected Cursor OpenCursor(Query Search) throws PDException;
//-----------------------------------------------------------------------------------
/**
 * Generates a cursor identifier
 * @return the generated random name
 */
private String genCursorName()
{
return ("Cursor"+Math.random());
}
//-----------------------------------------------------------------------------------
/**
 * Creates if needed and returns the collection containing open cursors
 * @return the collection of open cursors
 */
private HashMap<String, Cursor> getOpenCur()
{
if (OpenCur==null)
    OpenCur=new HashMap();
return OpenCur;
}
//-----------------------------------------------------------------------------------
/**
 * Creates a cursor and stores in the collection
 * @param rs Object representing a collection of results (JDBC Resulset, Vector,..)
 * @param Fields Record of attributes returned by the cursor
 * @return Created Cursor
 */
protected Cursor StoreCursor(Object rs, Record Fields)
{
Cursor Cur=new Cursor(genCursorName(), Fields, rs);
getOpenCur().put(Cur.getCursorId(), Cur);
return(Cur);
}
//-----------------------------------------------------------------------------------
/**
 * Returns a stored cursor
 * @param CursorIdent Cursor identifier to be returned
 * @return the stred cursor or null if don't exist
 * @throws PDException In any error
 */
protected Object getCursor(Cursor CursorIdent) throws PDException
{
return(CursorIdent.getResultSet());
}
//-----------------------------------------------------------------------------------
/**
 * Removes a Cursor from the stored collection
 * @param CursorIdent Cursor to be removed
 * @throws PDException In any error
 */
protected void delCursor(Cursor CursorIdent) throws PDException
{
if (getOpenCur().containsKey(CursorIdent.getCursorId()))    
   getOpenCur().remove(CursorIdent.getCursorId());
}
//-----------------------------------------------------------------------------------
/**
 * Returns the next record of the cursor
 * @param CursorIdent Cursor Identifier
 * @return Next Record or null if there are n more rows
 * @throws PDException In any error
 */
abstract public Record NextRec(Cursor CursorIdent)  throws PDException;
//-----------------------------------------------------------------------------------
/**
 * Closes the cursor, calling also to {@link #delCursor(prodoc.Cursor) }
 * @param CursorIdent Cursor Identifier
 * @throws PDException In any error
 */
abstract public void CloseCursor(Cursor CursorIdent) throws PDException;
//-----------------------------------------------------------------------------------
/**
 * Logged user acording his authenticator
 * @param userName
 * @param Password
 * @throws PDException
 */
void Assign(String userName, String Password) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Assign>:"+userName);
if (!userName.equalsIgnoreCase("Install"))
    {
    if (getUser().Load(userName)==null)
        PDExceptionFunc.GenPDException("User_or_password_incorrect", userName);
    if (!getUser().isActive())
        PDExceptionFunc.GenPDException("Inactive_User", userName);
    AuthGeneric Auth=getAuthentic(getUser().getValidation());
    Auth.Authenticate(userName, Password);
    getUser().LoadAll(userName);
    getPDCust().Load(getUser().getCustom());
    setAppLang(getPDCust().getLanguage());
    }
else
    {
    getUser().setName("Install");
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Assign<:"+userName);
}
//---------------------------------------------------------------------
/**
 * Protected method to create a Special taskUser
 * @throws prodoc.PDException
 */
protected void AssignTaskUser() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.AssignTaskUser");
User=new PDUser(this);
User.CreateTaskUser();
}
//-----------------------------------------------------------------------------------
/**
 * Refresh current all the information of current user
 * @throws PDException In any error
 */
public void RefreshUser() throws PDException
{
getUser().LoadAll(getUser().getName());
}
//-----------------------------------------------------------------------------------
/**
 * Returns, and create if needed, the session user
 * @return the User
 * @throws PDException In any error
*/
public PDUser getUser() throws PDException
{
if (User==null)
   User=new PDUser(this);
return User;
}
//-----------------------------------------------------------------------------------
/**
 * Loads definition of a document type
 * @param tableName Name of the document types
 * @param TypeDef Collection of definitions (hierachy of ancestors)
 * @param TypeRecs Collection of metadata of each ancestor
 * @throws PDException In any Error
 */
protected void LoadDef(String tableName, ArrayList<Record> TypeDef, ArrayList<Record> TypeRecs) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.LoadDef>:"+tableName);
PDObjDefs Def=new PDObjDefs(this);
Def.setParent(tableName);
while (!Def.getParent().equalsIgnoreCase(Def.getName()))
    {
    Def.Load(Def.getParent());
    TypeDef.add(Def.getRecord().Copy());
    TypeRecs.add(Def.GetAttrDef().Copy());
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.LoadDef<:"+tableName);
}
//-----------------------------------------------------------------------------------
/**
 * 
 * @param FileOut
 * @param Obj
 * @throws PDException
 */
//public void Export(PrintWriter FileOut, ObjPD Obj) throws PDException
//{
//FileOut.println(Obj.getTabName());
//Export(FileOut, Obj.getRecord());
//}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param ObjName
 * @param RecList
 * @throws PDException
 */
//public void Export(PrintWriter FileOut, String ObjName, Vector RecList) throws PDException
//{
//for (int i = 0; i < RecList.size(); i++)
//    {
//    FileOut.println(ObjName);
//    Record Rec = (Record)RecList.elementAt(i);
//    Export(FileOut, Rec);
//    }
//}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param ObjName
 * @param CursorId
 * @throws PDException
 */
//public void Export(PrintWriter FileOut, String ObjName, Cursor CursorId) throws PDException
//{
//Record Rec=this.NextRec(CursorId);
//while (Rec!=null)
//    {
//    FileOut.println(ObjName);
//    Export(FileOut, Rec);
//    Rec=this.NextRec(CursorId);
//    }
//}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param Rec
 * @throws PDException
 */
//public void Export(PrintWriter FileOut, Record Rec) throws PDException
//{
//FileOut.println(Rec.NumAttr());
//Rec.initList();
//for (int i = 0; i < Rec.NumAttr(); i++)
//    {
//    Attribute A=Rec.nextAttr();
//    FileOut.println(A.getName());
//    FileOut.println(A.Export());
//    }
//}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileIn
 * @param Rec
 * @throws PDException
 */
//public void Import(BufferedReader FileIn, Record Rec) throws PDException
//{
//try {
//String NumStr = FileIn.readLine();
//int NumAtt = Integer.parseInt(NumStr);
//String NomAtt;
//String ValAtt;
//for (int i = 0; i < NumAtt; i++)
//    {
//    NomAtt=FileIn.readLine();
//    Attribute Att = Rec.getAttr(NomAtt);
//    if (Att==null)
//        PDExceptionFunc.GenPDException("Unknown_attibute",NomAtt);
//    ValAtt=FileIn.readLine();
//    Att.Import(ValAtt);
//    }
//} catch (IOException ex)
//    {
//    PDException.GenPDException(ex.getLocalizedMessage(),null);
//    }
//}
//-----------------------------------------------------------------------------------
/**
 * Creates a repository using the definition received (i.e. a Folder, a table blob, etc)
 * @param Rep Repository definition
 * @throws PDException In any error
 */
public void CreateRep(PDRepository Rep) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.CreateRep>:"+Rep.getName());
StoreGeneric st=ConstrucStore(Rep);
st.Connect();
st.Create();
st.Disconnect();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.CreateRep<");
}
//-----------------------------------------------------------------------------------
/**
 * DESTROY a repository using the definition received (i.e. a Folder, a table blob, etc)
 * @param Rep Repository definition
 * @throws PDException In any error
 */
public void DestroyRep(PDRepository Rep) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.DestroyRep>:"+Rep.getName());
StoreGeneric st=ConstrucStore(Rep);
st.Connect();
st.Destroy();
st.Disconnect();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.DestroyRep<");}
//-----------------------------------------------------------------------------------
/**
 * Creates an instance of Store using the received definition
 * @param Rep Repository definition
 * @return Created object
 * @throws PDException In any error
 */
private StoreGeneric ConstrucStore(PDRepository Rep) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstrucStore>:"+Rep.getName());
StoreGeneric st=null;
String RepTyp=Rep.getRepType();
if (RepTyp.equals(PDRepository.tFS))
    st=new StoreFS(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tBBDD))
    st=new StoreDDBB(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tFTP))
    st=new Storeftp(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tREFURL))
    st=new StoreRefURL(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tS3))
    st=new StoreAmazonS3(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tCUSTOM))
    st=new StoreCustom(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted(), this);
else
    PDException.GenPDException("Repository_type_unsuported", RepTyp);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstrucStore<");
return(st);
}
//-----------------------------------------------------------------------------------
/**
 * Search, for a DocType, the defined Reposit and store it in the cache
 * @param docType Name of doctype to look for
 * @return name of Assigned Reposit
 * @throws PDException In any error
 */
protected String getAssignedRepos(String docType) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAssignedRepos>:"+docType);
String Rep=null;
if (Class2Reposit==null)
    Class2Reposit=new HashMap();
else
    {
    Rep=(String)Class2Reposit.get(docType);
    if (Rep!=null)
        {
        if (PDLog.isDebug())
            PDLog.Debug("DriverGeneric.getAssignedRepos yet Instanced:"+Rep);
        return(Rep);
        }
    }
PDObjDefs Def=new PDObjDefs(this);
Def.Load(docType);
Rep=Def.getReposit();
Class2Reposit.put(docType, Rep);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAssignedRepos<");
return(Rep);
}
//-----------------------------------------------------------------------------------
/**
 * Returns an object of type repository from its name
 * if the repository is yet constructed, returns the constructed one
 * @param RepName Nmae of repository
 * @return object of type repository
 * @throws PDException in any error
 */
protected StoreGeneric getRepository(String RepName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getRepository>:"+RepName);
StoreGeneric Rep=null;
//Rep=(StoreGeneric)getListReposit().get(RepName);
//if (Rep!=null)
//    {
//    if (PDLog.isDebug())
//        PDLog.Debug("DriverGeneric.Rep yet Instanced:"+Rep.getServer());
//    return (Rep);
//    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Rep new Instance:"+RepName);
PDRepository RepDesc=new PDRepository(this);
RepDesc.Load(RepName);
Rep=ConstrucStore(RepDesc);
//getListReposit().put(RepName, Rep);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getRepository<:"+RepName);
return(Rep);
}
//-----------------------------------------------------------------------------------
/**
 * Creates an instance of authenticator based on name received and 
 * store it in the authenticators collection
 * @param ValidatName Name of an authenticator definition
 * @return Created instance of authenticator
 * @throws PDException In any Error
 */
private AuthGeneric getAuthentic(String ValidatName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAuthentic>:"+ValidatName);
AuthGeneric Auth=null;
Auth=getListAuth().get(ValidatName);
if (Auth!=null)
    {
    if (PDLog.isDebug())
        PDLog.Debug("DriverGeneric.Auth yet Instanced:"+ValidatName);
    return (Auth);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Auth new Instance:"+ValidatName);
PDAuthenticators RepDesc=new PDAuthenticators(this);
RepDesc.Load(ValidatName);
Auth=ConstructAuthentic(RepDesc);
getListAuth().put(ValidatName, Auth);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAuthentic<:"+ValidatName);
return(Auth);
}
//-----------------------------------------------------------------------------------
/**
 * Creates an instance of uthenticator based on definition received
 * @param Auth Authenticator definition
 * @return Instance created
 */
private AuthGeneric ConstructAuthentic(PDAuthenticators Auth) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstructAuthentic>:"+Auth.getName());
AuthGeneric st=null;
String AuthType=Auth.getAuthType();
if (AuthType.equals(PDAuthenticators.tOPD))
    st=new AuthOPD( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam(), this);
else if (AuthType.equals(PDAuthenticators.tBBDD))
    st=new AuthDDBB( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam());
else if (AuthType.equals(PDAuthenticators.tLDAP))
    st=new AuthLDAP( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam());
else if (AuthType.equals(PDAuthenticators.tSO))
    st=new AuthSO( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam());
else
    PDException.GenPDException("Authentication_type_unsuported", AuthType);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstructAuthentic<:"+Auth.getName());
return(st);
}
//-----------------------------------------------------------------------------------
/**
 * Returns, and create if needed, the colection of authenticators
 * @return the ListAuth
 */
private HashMap<String, AuthGeneric> getListAuth()
{
if (ListAuth==null)
    ListAuth=new HashMap();
return ListAuth;
}
//-----------------------------------------------------------------------------------
/**
 * @return the ListReposit
 */
//private HashMap getListReposit()
//{
//if (ListReposit==null)
//    ListReposit=new HashMap();
//return ListReposit;
//}
//-----------------------------------------------------------------------------------
/**
 * Changes (or transmit the change to server defined in the authenticator) the password
 * @param UserName     Name of the user changing the password
 * @param OldPassword  Old password
 * @param NewPassword  New password
 * @throws PDException In any error
 */
public void ChangePassword(String UserName, String OldPassword, String NewPassword) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.ChangePassword>:"+UserName);
PDUser U2cp=new PDUser(this);
if (U2cp.Load(UserName)==null)
    PDExceptionFunc.GenPDException("User_or_password_incorrect", UserName);
AuthGeneric Auth=getAuthentic(U2cp.getValidation());
Auth.Authenticate(UserName, OldPassword);
//if (U2cp.getValidation().equals(PDAuthenticators.tOPD))
//    Auth.UpdatePass(UserName, Encode(NewPassword));
//else
    Auth.UpdatePass(UserName, NewPassword);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ChangePassword<:"+UserName);
}
//-----------------------------------------------------------------------------------
/**
 * Assigns (or transmit the assignation to server defined in the authenticator) the password
 * @param UserName     Name of the user changing the password
 * @param NewPassword  New password
 * @throws PDException In any error
 */
public void SetPassword(String UserName, String NewPassword) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.SetPassword>:"+UserName);
PDUser U2cp=new PDUser(this);
if (U2cp.Load(UserName)==null)
    PDExceptionFunc.GenPDException("User_or_password_incorrect", UserName);
AuthGeneric Auth=getAuthentic(U2cp.getValidation());
//if (U2cp.getValidation().equals(PDAuthenticators.tOPD))
//    Auth.UpdatePass(UserName, Encode(NewPassword));
//else
    Auth.UpdatePass(UserName, NewPassword);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.SetPassword<:"+UserName);
}
//-----------------------------------------------------------------------------------
/**
 * Returns the core(nucleo) version
 * @return the version
 */
static public String getVersion()
{
return("2.3");
}
/**
 * constant used for bin <-> hexadecimal conversión
 */
private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
/** 
 * default constant
 */
private static final char[] Key = "Esta es la clave".toCharArray();
/**
 * Encoding of Strings
 * @param ToBeEncoded Text to be encoded
 * @return ncoded text
 */
static protected String Encode(String ToBeEncoded)
{
if (ToBeEncoded==null || ToBeEncoded.length()==0)
    return(ToBeEncoded);
StringBuilder S=new StringBuilder(ToBeEncoded);
for (int i = 0; i < S.length(); i++)
    {
    S.setCharAt(i, (char)(S.charAt(i)^ Key[i%16]));
    }
byte[] buf=S.toString().getBytes();
char[] chars = new char[2 * buf.length];
for (int i = 0; i < buf.length; ++i)
    {
    chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
    chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
    }
return new String(chars);
}
//-------------------------------------------------------------------------
/**
 * Decoding of strings
 * @param ToBeDecoded text to be decoded
 * @return Decoded Text
 */
static private String Decode(String ToBeDecoded)
{
if (ToBeDecoded==null || ToBeDecoded.length()==0)
    return(ToBeDecoded);
byte[] buf=new byte[ToBeDecoded.length()/2];
for (int i = 0; i < buf.length; i++)
    {
    String tmp=ToBeDecoded.substring(i*2, i*2+2);
    Integer n=Integer.parseInt(ToBeDecoded.substring(i*2, i*2+2), 16);
    buf[i] = (byte) Integer.parseInt(ToBeDecoded.substring(i*2, i*2+2), 16) ;
    }
StringBuffer S=new StringBuffer(new String(buf));
for (int i = 0; i < S.length(); i++)
    {
    S.setCharAt(i, (char)(S.charAt(i)^ Key[i%16]));
    }
return(S.toString());
}
//-------------------------------------------------------------------------
/**
 * Creates a properties file to be used by any client, including to create metadata repository
 * Review Installation product help for details
 * @param FileName Name of the properties file to create
 * @param UserName User name for connecting to the database 
 * @param ConnectName name of connection (PD usually)
 * @param UrlServer URL of he database server 
 * @param JDBCClass JDBC class for connection
 * @param Password Password (clear)for connecting to the database 
 * @throws Exception In any error
 */
static public void generateProps(String FileName, String ConnectName, String UrlServer, String UserName, String Password, String JDBCClass) throws Exception
{
ConnectName=ConnectName.trim();
PrintWriter FProps = new PrintWriter(FileName, "UTF-8");
FProps.println("#------------------------------------------------------------");
FProps.println("# The Kind of connection to data (JDBC, Remote, ...");
FProps.println(ConnectName+".DATA_TYPE=JDBC");
FProps.println("#----for remote conection -----------------------------------");
FProps.println("# Uncomment:");
FProps.println("# PD.DATA_TYPE=Remote");
FProps.println("# and uncomment and modify");
FProps.println("# PD.DATA_URL=http://localhost:8080/ProdocWeb2/Oper");
FProps.println("# where host= IP of OPD server, 8080= the port used and ");
FProps.println("# ProdocWeb2= url where deployed OPD J2EE application");
FProps.println("#------------------------------------------------------------");
FProps.println("# URL form conection");
FProps.println(ConnectName+".DATA_URL="+UrlServer.trim());
FProps.println("# connection user");
FProps.println(ConnectName+".DATA_USER="+UserName.trim());
FProps.println("# connection password");
FProps.println(ConnectName+".DATA_PASSWORD="+Encode(Password.trim()));
FProps.println("# minimum number of sessions for metadata");
FProps.println(ConnectName+".DATA_MIN=1");
FProps.println("# maximum number of sessions for metadata");
FProps.println(ConnectName+".DATA_MAX=30");
FProps.println("# Aditional param to be interpreted by every driver (the jdbc class for DATA_TYPE=JDBC)");
FProps.println(ConnectName+".DATA_PARAM="+JDBCClass.trim());
FProps.println("# Timeout (in ms) before the conection is closed");
FProps.println(ConnectName+".DATA_TIMEOUT=300000");
FProps.println("#--------------------------------------------------------------");
FProps.println("# minimum number of sessions in documents repository");
FProps.println(ConnectName+".PR_MIN=1");
FProps.println("# maximum number of sessions in documents repository");
FProps.println(ConnectName+".PR_MAX=10");
FProps.println("# Timeout (in ms) before the conection is closed");
FProps.println(ConnectName+".PR_TIMEOUT=300000");
FProps.println("# TraceLevel LOGLEVELERROR=0, LOGLEVELINFO=1, LOGLEVELDEBUG=2");
FProps.println("TRACELEVEL=0");
FProps.println("# Path to the log4j properties file");
FProps.println("# Beware that the TRACELEVEL has priority over level defined in the file");
// FProps.println("TRACECONF=log4j.properties");
FProps.println("TRACECONF="+Paths.get(".").toAbsolutePath().normalize().toString().replace("\\", "/")+"/log4j.properties");
FProps.println("#--------------------------------------------------------------");
FProps.println("# Elements related to Tasks");
FProps.println("#--------------------------------------------------------------");
FProps.println("# Category of task to generate and execute in this computer (* = all categories");
FProps.println("#"+ConnectName+".TaskCategory=*");
FProps.println("# Pooling frecuency for Generation in miliseconds");
FProps.println("#"+ConnectName+".TaskSearchFreq=120000");
FProps.println("# Pooling frecuency for Execution  in miliseconds");
FProps.println("#"+ConnectName+".TaskExecFreq=60000");
FProps.println("#--------------------------------------------------------------");
FProps.println("#  UserData for just reading OPAC configuration files");
FProps.println("#--------------------------------------------------------------");
FProps.println("#User=guest");
FProps.println("#Pass=passguest");
FProps.flush();
FProps.close();
}
//-------------------------------------------------------------------------
/**
 * Translates a text using the session language and the language properties
 * @param Text Text to translate
 * @return Translated text or the same test if not found
 */
public String TT(String Text)
{
if (Text==null)
    return("NullPointerException");
String Lang=getAppLang();
if (Lang.equals("EN"))
    return(Text.replace("_", " "));
Properties Trans=getProperties(Lang);
if (Trans==null)
    return(Text);
String ToTrans;
int Pos=Text.indexOf(':');
String AddText;
if (Pos!=-1)
    {
    ToTrans = Text.substring(0, Pos);
    AddText=Text.substring(Pos);
    }
else
    {
    ToTrans=Text;
    AddText="";
    }
String Translation=Trans.getProperty(ToTrans);
if (Translation==null)
    return(ToTrans.replace("_", " ")+AddText);
else
    return(Translation+AddText);
}
//-------------------------------------------------------------------------
/**
 * Translates a text using the default language and the language properties
 * @param Text Text to translate
 * @return Translated text or the same test if not found
 */
public static String DefTT(String Text)
{
String Lang=getDefAppLang();
if (Lang.equals("EN"))
    return(Text.replace("_", " "));
Properties Trans=getProperties(Lang);
if (Trans==null)
    return(Text);
String ToTrans;
int Pos=Text.indexOf(':');
String AddText;
if (Pos!=-1)
    {
    ToTrans = Text.substring(0, Pos);
    AddText=Text.substring(Pos);
    }
else
    {
    ToTrans=Text;
    AddText="";
    }
String Translation=Trans.getProperty(ToTrans);
if (Translation==null)
    return(ToTrans.replace("_", " ")+AddText);
else
    return(Translation+AddText);
}
//-------------------------------------------------------------------------
/**
 * Translates a text using the specified language and the language properties
 * @param Lang language to use
 * @param Text Text to translate
 * @return Translated text or the same test if not found
 */
public static String DefTT(String Lang, String Text)
{
Lang=Lang.toUpperCase();
if (Lang.equals("EN"))
    return(Text.replace("_", " "));
Properties Trans=getProperties(Lang);
if (Trans==null)
    return(Text);
String ToTrans;
int Pos=Text.indexOf(':');
String AddText;
if (Pos!=-1)
    {
    ToTrans = Text.substring(0, Pos);
    AddText=Text.substring(Pos);
    }
else
    {
    ToTrans=Text;
    AddText="";
    }
String Translation=Trans.getProperty(ToTrans);
if (Translation==null)
    return(ToTrans.replace("_", " ")+AddText);
else
    return(Translation+AddText);
}
//----------------------------------------------------------
/**
 * Load the file containing the translation for the specifies language and
 * stores the language properties for the next time
 * @param Lang Language to load
 * @return Loaded properties
 */
static private Properties getProperties(String Lang)
{
Lang=Lang.toUpperCase();
Properties Trans=TransList.get(Lang);
if (Trans!=null)
    return(Trans);
InputStream f=null;
try {
f= DriverGeneric.class.getResourceAsStream("lang/Text_"+Lang+".properties");
Trans=new Properties();
Trans.load(f);
} catch (Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage()+":"+Lang);
    return(null);
    }
finally
    {
    try {
    if (f!=null)
        f.close();
    } catch (Exception ex)
        {
        PDLog.Error(ex.getLocalizedMessage()+":"+Lang);
        }
    }
TransList.put(Lang, Trans);
return(Trans);
}
//----------------------------------------------------------
/**
 * Returns the language to use
 * @return the AppLang
 */
public String getAppLang()
{
if (AppLang==null)
    {
    Locale locale = Locale.getDefault();
    setAppLang(locale.getLanguage().toUpperCase());
    }
return AppLang;
}
//----------------------------------------------------------
/**
 * Sets the language to use
 * @param pAppLang the AppLang to set
 */
public void setAppLang(String pAppLang)
{
AppLang = pAppLang.toUpperCase();
}
//---------------------------------------------------------------------
/**
 * Returns the default language
 * @return the DefAppLang
 */
public static String getDefAppLang()
{
if (DefAppLang==null)
    {
    Locale locale = Locale.getDefault();
    setDefAppLang(locale.getLanguage().toUpperCase());
    }
return DefAppLang;
}
//---------------------------------------------------------------------
/**
 * Sets the default language
 * @param aDefAppLang the DefAppLang to set
 */
public static void setDefAppLang(String aDefAppLang)
{
DefAppLang = aDefAppLang.toUpperCase();
}
//---------------------------------------------------------------------
/**
 * Returns the customization assigned to session, creating if needed
 * @return the PDCust the session customization
 * @throws PDException in any error 
 */
public PDCustomization getPDCust() throws PDException
{
if (PDCust==null)
   PDCust=new PDCustomization(this);
return PDCust;
}
//---------------------------------------------------------------------
/**
 * Imports ANY kind of OpenProdoc object(s) (doc, folder, definition, object of any kind,..) in XML format from a file
 * @param XMLFile local file containing the object
 * @param ParentFolderId Folder Id for importing folders or docs.
 * @return Number of objects found and processed in the XML
 * @throws PDException In any error
 */
public int ProcessXML(File XMLFile, String ParentFolderId) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.ProcessXML>:XMLFile="+XMLFile.getAbsolutePath()+" ParentFolderId="+ParentFolderId);        
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject = null;
ObjPD Obj2Build=null;
int Tot=0;
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ProcessXML:Elements="+OPDObjectList.getLength());        
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    Obj2Build=BuildObj(OPDObject);
    if (Obj2Build instanceof PDDocs)
        {
        ((PDDocs)Obj2Build).ImportXMLNode(OPDObject, XMLFile.getAbsolutePath().substring(0, 
                                           XMLFile.getAbsolutePath().lastIndexOf(File.separatorChar)),
                                           ParentFolderId, false);
        Tot++;
        }
    else if (Obj2Build instanceof PDFolders)
            ;  // ((PDFolders)Obj2Build).ImportXMLNode(OPDObject, ParentFolderId, false);
    else
        {
        Obj2Build.ProcesXMLNode(OPDObject);
        Tot++;
        }
    }
DB.reset();
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.ProcessXML<");        
return(Tot);
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
/**
 * Imports ANY kind of OpenProdoc object(s) (doc, folder, definition, object of any kind,..) in XML format from a InputStream
 * @param XMLFile InputStream containing the object
 * @param ParentFolderId Folder Id for importing folders or docs.
 * @return Number of objects found and processed in the XML
 * @throws PDException In any error
 */
public int ProcessXML(InputStream XMLFile, String ParentFolderId) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.ProcessXML>:InputStream. ParentFolderId="+ParentFolderId);        
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject = null;
ObjPD Obj2Build=null;
int Tot=0;
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ProcessXML:Elements="+OPDObjectList.getLength());        
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    Obj2Build=BuildObj(OPDObject);
    if (Obj2Build instanceof PDDocs)
        {
        ((PDDocs)Obj2Build).ImportXMLNode(OPDObject, ParentFolderId, false);
        Tot++;
        }
    else if (Obj2Build instanceof PDFolders)
        {
//        ((PDFolders)Obj2Build).ImportXMLNode(OPDObject, ParentFolderId, false);
//        Tot++;
        }
    else
        {
        Obj2Build.ProcesXMLNode(OPDObject);
        Tot++;
        }
    }
DB.reset();
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.ProcessXML<");        
return(Tot);
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
/**
 * Imports ANY kind of OpenProdoc object(s) (doc, folder, definition, object of any kind,..) in XML format from a InputStream with base64 content
 * @param XMLFile InputStream containing the object
 * @param ParentFolderId Folder Id for importing folders or docs.
 * @return Number of objects found and processed in the XML
 * @throws PDException In any error
 */
public int ProcessXMLB64(InputStream XMLFile, String ParentFolderId) throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.ProcessXML>:InputStream. ParentFolderId="+ParentFolderId);        
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject = null;
ObjPD Obj2Build=null;
int Tot=0;
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ProcessXML:Elements="+OPDObjectList.getLength());        
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    Obj2Build=BuildObj(OPDObject);
    if (Obj2Build instanceof PDDocs)
        {
        ((PDDocs)Obj2Build).ImportXMLNode(OPDObject, ParentFolderId, false);
        Tot++;
        }
    else if (Obj2Build instanceof PDFolders)
        {
        ParentFolderId=((PDFolders)Obj2Build).ImportXMLNode(OPDObject, ParentFolderId, false).getPDId();
        Tot++;
        }
    else
        {
        Obj2Build.ProcesXMLNode(OPDObject);
        Tot++;
        }
    }
DB.reset();
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.ProcessXML<");        
return(Tot);
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
/**
 * Build an OpenProdoc object from an XML node
 * @param OPDObject XML node containing an object
 * @return Created OpenProdoc object
 * @throws PDException in any error
 */
public ObjPD BuildObj(Node OPDObject) throws PDException
{           
NamedNodeMap attributes = OPDObject.getAttributes();
Node namedItem = attributes.getNamedItem("type");
String OPDObjectType=namedItem.getNodeValue();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.BuildObj:Tipe="+OPDObjectType);            
if (OPDObjectType.equalsIgnoreCase(PDDocs.getTableName()))
    return(new PDDocs(this));
if (OPDObjectType.equalsIgnoreCase(PDFolders.getTableName()))
    return(new PDFolders(this));
if (OPDObjectType.equalsIgnoreCase(PDACL.getTableName()))
    return(new PDACL(this));
if (OPDObjectType.equalsIgnoreCase(PDAuthenticators.getTableName()))
    return(new PDAuthenticators(this));
if (OPDObjectType.equalsIgnoreCase(PDCustomization.getTableName()))
    return(new PDCustomization(this));
if (OPDObjectType.equalsIgnoreCase(PDGroups.getTableName()))
    return(new PDGroups(this));
if (OPDObjectType.equalsIgnoreCase(PDMimeType.getTableName()))
    return(new PDMimeType(this));
if (OPDObjectType.equalsIgnoreCase(PDObjDefs.getTableName()))
    return(new PDObjDefs(this));
if (OPDObjectType.equalsIgnoreCase(PDRepository.getTableName()))
    return(new PDRepository(this));
if (OPDObjectType.equalsIgnoreCase(PDRoles.getTableName()))
    return(new PDRoles(this));
if (OPDObjectType.equalsIgnoreCase(PDUser.getTableName()))
    return(new PDUser(this));
if (OPDObjectType.equalsIgnoreCase(PDTasksDefEvent.getTableName()))
    return(new PDTasksDefEvent(this));
if (OPDObjectType.equalsIgnoreCase(PDTasksCron.getTableName()))
    return(new PDTasksCron(this));
throw new PDException("Inexistent_OPD_object_type");
}
//---------------------------------------------------------------------
/**
 * Executes a received order from a remote installation
 * @param Order Order to execute
 * @param XMLObjects XML containign the parameters for the order
 * @return XML result containing OK + information of the operation
 * @throws PDException In any error
 */
public String RemoteOrder(String Order, Document XMLObjects) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.RemoteOrder:"+Order);            
if (Order.equals(S_SELECT))
    {
    return("<OPD><Result>OK</Result><Data>"+GenVector(XMLObjects)+"</Data></OPD>");
    }
else if (Order.equals(S_DELFILE))
    {
    return("<OPD><Result>OK</Result><Data>"+DeleteFile(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_RENFILE))
    {
    return("<OPD><Result>OK</Result><Data>"+RenameFile(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_UPDATE))
    {
    return("<OPD><Result>OK</Result><Data>"+UpdateRecord(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_INSERT))
    {
    return("<OPD><Result>OK</Result><Data>"+InsertRecord(XMLObjects)+"</Data></OPD>");
    }
else if (Order.equals(S_DELETE))
    {
    return("<OPD><Result>OK</Result><Data>"+DeleteRecord(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_CREATE))
    {
    return("<OPD><Result>OK</Result><Data>"+CreateTable(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_DROP))
    {
    return("<OPD><Result>OK</Result><Data>"+DropTable(XMLObjects)+"</Data></OPD>");        
    }
else if (Order.equals(S_ALTER))
    {
    return("<OPD><Result>OK</Result><Data>"+AlterTableAdd(XMLObjects)+"</Data></OPD>");            
    }
else if (Order.equals(S_ALTERDEL))
    {
    return("<OPD><Result>OK</Result><Data>"+AlterTableDel(XMLObjects)+"</Data></OPD>");                
    }
else if (Order.equals(S_INTEGRIT))
    {
    return("<OPD><Result>OK</Result><Data>"+AddIntegrity(XMLObjects)+"</Data></OPD>");                    
    }
else if (Order.equals(S_INTEGRIT2))
    {
    return("<OPD><Result>OK</Result><Data>"+AddIntegrity2(XMLObjects)+"</Data></OPD>");                    
    }
else if (Order.equals(S_INITTRANS))
    {
    IniciarTrans();
    }
else if (Order.equals(S_COMMIT))
    {
    CerrarTrans();
    }
else if (Order.equals(S_CANCEL))
    {
    AnularTrans();
    }
else if (Order.equals(S_FTSEARCH))
    {
    return("<OPD><Result>OK</Result><Data>"+FTSearch(XMLObjects)+"</Data></OPD>");                    
    }
else if (Order.equals(S_FTINS))
    {
    return("<OPD><Result>OK</Result><Data>"+FTIns(XMLObjects)+"</Data></OPD>");                        
    }
else if (Order.equals(S_FTUPD))
    {
    return("<OPD><Result>OK</Result><Data>"+FTUpd(XMLObjects)+"</Data></OPD>");                            
    }
else if (Order.equals(S_FTDEL))
    { 
    return("<OPD><Result>OK</Result><Data>"+FTDel(XMLObjects)+"</Data></OPD>");                    
    }
else 
    return("<OPD><Result>KO</Result><Msg>Unknown Order</Msg></OPD>");
return("<OPD><Result>OK</Result></OPD>");
}        
//---------------------------------------------------------------------
/**
 * Opens a Cursor using he query included as XML and generates a "Vector" with all the results
 * @param XMLObjects Query as XML
 * @return XML with all the records of the query
 * @throws PDException  In any error
 */
private String GenVector(Document XMLObjects) throws PDException
{
StringBuilder Res=new StringBuilder();
Query Q=new Query(XMLObjects);    
Cursor C=null;
try {
C=OpenCursor(Q);
Record R=NextRec(C);
while (R!=null)
    {
    Res.append(R.toXMLt());
    R=NextRec(C);
    }
} finally
{
if (C!=null)
    {    
    CloseCursor(C);
    delCursor(C);
    }
}
return(Res.toString());
}
//---------------------------------------------------------------------
/**
 * Insert a record using the received XML
 * @param XMLObjects Data to insert
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String InsertRecord(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Rec");
OPDObject = OPDObjectList.item(0);
Record R=Record.CreateFromXML(OPDObject);
InsertRecord(Tab, R);
return("");
}
//---------------------------------------------------------------------
/**
 * Delete a record using the received XML
 * @param XMLObjects Data to delete
 * @return An empty string when ok
 * @throws PDException In any error
*/
private String DeleteRecord(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("DelConds");
OPDObject = OPDObjectList.item(0);
Conditions C= new Conditions(OPDObject);
DeleteRecord(Tab, C);
return("");
}
//---------------------------------------------------------------------
/**
 * Updates a record using the received XML
 * @param XMLObjects Data to update
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String UpdateRecord(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Rec");
OPDObject = OPDObjectList.item(0);
Record R=Record.CreateFromXML(OPDObject);
OPDObjectList = XMLObjects.getElementsByTagName("UpConds");
OPDObject = OPDObjectList.item(0);
Conditions C= new Conditions(OPDObject);
UpdateRecord(Tab, R, C);
return("");
}
//---------------------------------------------------------------------
/**
 * Creates a table using the received XML
 * @param XMLObjects table definition to create
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String CreateTable(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Rec");
OPDObject = OPDObjectList.item(0);
Record R=Record.CreateFromXML(OPDObject);
CreateTable(Tab, R);
return("");
}
//---------------------------------------------------------------------
/**
 * Drops a table using the received XML
 * @param XMLObjects table definition to drop
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String DropTable(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
DropTable(Tab);
return("");
}
//---------------------------------------------------------------------
/**
 * Alter a table adding a column using the received XML
 * @param XMLObjects table definition to alter
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String AlterTableAdd(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Attr");
OPDObject = OPDObjectList.item(0);
Attribute A=new Attribute(OPDObject);
OPDObjectList = XMLObjects.getElementsByTagName("IsVer");
OPDObject = OPDObjectList.item(0);
String IsVer=OPDObject.getTextContent();
AlterTableAdd(Tab, A, IsVer.equals("1"));
return("");
}
//---------------------------------------------------------------------
/**
 * Alter a table adding a column using the received XML
 * @param XMLObjects table definition to alter
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String AlterTableDel(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("OldAttr");
OPDObject = OPDObjectList.item(0);
String OldAttr=OPDObject.getTextContent();
AlterTableDel(Tab, OldAttr);
return("");
}
//---------------------------------------------------------------------
/**
 * Alter a table adding integrity with one field using the received XML
 * @param XMLObjects table definition to alter
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String AddIntegrity(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab1");
Node OPDObject = OPDObjectList.item(0);
String Tab1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field1");
OPDObject = OPDObjectList.item(0);
String Field1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Tab2");
OPDObject = OPDObjectList.item(0);
String Tab2=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field2");
OPDObject = OPDObjectList.item(0);
String Field2=OPDObject.getTextContent();
AddIntegrity(Tab1, Field1, Tab2, Field2);
return("");
}
//---------------------------------------------------------------------
/**
 * Alter a table adding integrity with two fields using the received XML
 * @param XMLObjects table definition to alter
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String AddIntegrity2(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab1");
Node OPDObject = OPDObjectList.item(0);
String Tab1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field11");
OPDObject = OPDObjectList.item(0);
String Field11=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field12");
OPDObject = OPDObjectList.item(0);
String Field12=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Tab2");
OPDObject = OPDObjectList.item(0);
String Tab2=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field21");
OPDObject = OPDObjectList.item(0);
String Field21=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field22");
OPDObject = OPDObjectList.item(0);
String Field22=OPDObject.getTextContent();
AddIntegrity(Tab1, Field11,Field12, Tab2, Field21, Field22);
return("");
}
//---------------------------------------------------------------------
/**
 * Allows to decide how to download file
 * @return true ir Driver is remote
 */
protected boolean IsRemote()
{
return(false);
}
//---------------------------------------------------------------------
/**
 * Deletes a file from repository the received XML
 * @param XMLObjects File to delete
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String DeleteFile(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Id");
Node OPDObject = OPDObjectList.item(0);
String Id=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Ver");
OPDObject = OPDObjectList.item(0);
String Ver=OPDObject.getTextContent();
PDDocs D=new PDDocs(this);
D.LoadVersion(Id, Ver);
String RepName=D.getReposit();
//OPDObjectList = XMLObjects.getElementsByTagName("Rep");
//OPDObject = OPDObjectList.item(0);
//String RepName=OPDObject.getTextContent();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.DeleteFileXML:"+Id+"/"+Ver);    
StoreGeneric Rep=getRepository(RepName);
if (!Rep.IsRef())
    {
    Rep.Connect();
    Rep.Delete(Id, Ver, D.getRecSum());
    Rep.Disconnect();
    }
return("");
}
//---------------------------------------------------------------------
/**
 * Renames a file from repository the received XML
 * @param XMLObjects File to rename
 * @return An empty string when ok
 * @throws PDException In any error
 */
private String RenameFile(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Id1");
Node OPDObject = OPDObjectList.item(0);
String Id1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Ver1");
OPDObject = OPDObjectList.item(0);
String Ver1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Id2");
OPDObject = OPDObjectList.item(0);
String Id2=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Ver2");
OPDObject = OPDObjectList.item(0);
String Ver2=OPDObject.getTextContent();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.RenameFileXML:"+Id1+"/"+Ver1+"->"+Id2+"/"+Ver2);    
PDDocs doc=new PDDocs(this);
doc.setPDId(Id1);
doc.LoadVersion(Id1, Ver1);
StoreGeneric Rep=getRepository(doc.getReposit());
if (!Rep.IsRef())
    {
    Rep.Connect();
    Rep.Rename(Id1, Ver1, Id2, Ver2);
    Rep.Disconnect();
    }
return("");
}
//---------------------------------------------------------------------
/**
 * Creates a File in the repository
 * @param Id Identification of file
 * @param Ver Version of file
 * @param FileData InpusStream cotaining the data
 * @throws PDException In any error 
 */
public void InsertFile(String Id, String Ver, InputStream FileData) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.InsertFile: Id="+Id+" Ver="+Ver);            
PDDocs Doc=new PDDocs(this);
Doc.Load(Id);
StoreGeneric St=getRepository(Doc.getReposit());
St.Insert(Id, Ver, FileData, Doc.getRecSum(), null);
}
//-----------------------------------------------------------------   
/**
 * Escape of values for avoiding problems with xml/html 
 * @param Text Text to escape
 * @return "Escaped" Text
 */
static public String Codif(String Text)
{
return(Text.replace('<', '^').replace("%", "¡1").replace("&", "¡2"));
}    
//-----------------------------------------------------------------   
/**
 * UNescape of values for avoiding problems with xml/html 
 * @param Text Text to UNescape
 * @return "UNescaped" Text
 */
static public String DeCodif(String Text)
{
return(Text.replace('^', '<').replace("¡1", "%").replace("¡2","&"));
}    
//---------------------------------------------------------------------
/**
 * Returns a list of Transactional tasks ORDERED for selected fold type AND operation
 *    including parent Fold types
 * @param folderType Folder type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 * @throws PDException in any error
 */
protected ArrayList<PDTasksDefEvent> getFoldTransThreads(String folderType, String MODE) throws PDException
{
ArrayList TotalTask=new ArrayList();    
PDFolders f=new PDFolders(this,folderType);
ArrayList TypList=f.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)((Record)TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
/**
 * Returns a list of Non Transactional tasks ORDERED for selected fold type AND operation
 *    including parent types
 * @param folderType Folder type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 * @throws PDException In any error
 */
protected ArrayList getFoldNoTransThreads(String folderType, String MODE) throws PDException
{
ArrayList TotalTask=new ArrayList();    
PDFolders f=new PDFolders(this,folderType);
ArrayList TypList=f.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)((Record)TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskNoTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
/**
 * Returns, and creates and loads if needed, the collection of transactional tasks
 * @return the AllTaskTrans Collection of transactional tasks
 * @throws PDException In any error
  */
private TreeMap<String, PDTasksDefEvent> getAllTaskTrans() throws PDException
{
if (AllTaskTrans==null)    
    LoadAllTaks();
return AllTaskTrans;
}
//---------------------------------------------------------------------
/**
 * Returns, and creates and loads if needed, the collection of NON transactional tasks
 * @return the AllTaskNOTrans Collection of transactional tasks
 * @throws PDException In any error
  */
private TreeMap<String, PDTasksDefEvent> getAllTaskNoTrans() throws PDException
{
if (AllTaskNoTrans==null)    
    LoadAllTaks();
return AllTaskNoTrans;
}
//---------------------------------------------------------------------
/**
 * Loads AllTaskNoTrans and AllTaskTrans the first time
 * @throws PDException In any error
  */
private void LoadAllTaks() throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("DriverGeneric.LoadAllTaks");                
AllTaskTrans=new TreeMap();
AllTaskNoTrans=new TreeMap();
PDTasksDefEvent TE=new PDTasksDefEvent(this);
Cursor C=TE.SearchLike("");
Record R=NextRec(C);
while (R!=null)
    {
    PDTasksDefEvent TDE=new PDTasksDefEvent(this);  
    TDE.assignValues(R);
    if (TDE.isActive())
        {
        if (TDE.isTransact())
            AllTaskTrans.put(TDE.getObjType()+"/"+TDE.getEvenType()+("0000"+TDE.getEvenOrder()).substring(0, 5), TDE);
        else
            AllTaskNoTrans.put(TDE.getObjType()+"/"+TDE.getEvenType()+("0000"+TDE.getEvenOrder()).substring(0, 5), TDE);
        }
    R=NextRec(C);
    }
CloseCursor(C);
}
//---------------------------------------------------------------------
/**
 * Returns a list of Transactional tasks ORDERED for selected Doc type AND operation
 *    including parent types
 * @param docType Doc type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 * @throws PDException In any error
 */
 protected ArrayList<PDTasksDefEvent> getDocTransThreads(String docType, String MODE)  throws PDException
{    
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getDocTransThreads: DocType="+docType+ " Mode="+MODE);                
ArrayList TotalTask=new ArrayList();    
PDDocs D=new PDDocs(this,docType);
ArrayList<Record> TypList=D.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)(TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
/**
 * Returns a list of Non Transactional tasks ORDERED for selected DOC type AND operation
 *    including parent types
 * @param docType Doc type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 * @throws PDException In any error
 */
 protected ArrayList getDocNoTransThreads(String docType, String MODE) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getDocNoTransThreads: DocType="+docType+ " Mode="+MODE);                    
ArrayList TotalTask=new ArrayList();    
PDDocs D=new PDDocs(this,docType);
ArrayList<Record> TypList=D.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)(TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskNoTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
/**
 * Returns the language of the help for a session language
 * @param UserLang Language of current session/user
 * @return Language of the help (that can be different because there are no help for all languages of interface
 */
static public String getHelpLang(String UserLang)
{
if (UserLang.equalsIgnoreCase("ES") || UserLang.equalsIgnoreCase("PT") || UserLang.equalsIgnoreCase("CT") )    
    return("ES");
else
    return("EN");
}
//---------------------------------------------------------------------
/**
 * Returns an object of type Fulltext indexer
 * if the repository is yet constructed, returns the constructed one
 * @param pDocType Document type (for a future evolutio where different document types use different FT engines
 * @return object of type repository
 * @throws PDException in any error
 */
protected FTConnector getFTRepository(String pDocType) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getFTRepository>");
if (FTConn!=null)
    {
    if (PDLog.isDebug())
        PDLog.Debug("DriverGeneric.Rep yet Instantiated");
    return (FTConn);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Rep new Instance");
PDRepository RepDesc=new PDRepository(this);
RepDesc.Load("PD_FTRep");
FTConn=new FTConnLucene(RepDesc.getURL(), RepDesc.getUser(), RepDesc.getPassword(), RepDesc.getParam());
if (FTConn.getStopWords()!=null)
    {
    PDDocs SW=new PDDocs(this);
    SW.setPDId(FTConn.getStopWords());
    String FileSW = SW.getFileOpt(System.getProperty("java.io.tmpdir"), Locked);
    FTConn.setSWFile(FileSW);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getFTRepository<");
return(FTConn);
}
//-----------------------------------------------------------------------------------
/**
 * Execute a Fulltext search using the xml parameter received
 * @param XMLObjects FT Searching parameters
 * @return XML list of Id of documents
 * @throws PDException In any error
 */
private String FTSearch(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Type");
Node OPDObject = OPDObjectList.item(0);
String Type=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("DocMetadata");
OPDObject = OPDObjectList.item(0);
String DocMetadata=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Body");
OPDObject = OPDObjectList.item(0);
String Body=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Metadata");
OPDObject = OPDObjectList.item(0);
String Metadata=OPDObject.getTextContent();
ArrayList<String> FTRes=null; 
FTConnector FTConn=null;
try {
FTConn=getFTRepository(Type);
FTConn.Connect();
FTRes=FTConn.Search(Type, DocMetadata, Body, Metadata);
FTConn.Disconnect();
} catch (Exception Ex)
    {
    if (FTConn!=null)
       FTConn.Disconnect();
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }   
StringBuilder S=new StringBuilder();
for (String Id : FTRes)
    {
    S.append("<ID>").append(Id).append("</ID>");
    }
return(S.toString());    
}
//-----------------------------------------------------------------------------------
/**
 * Deletes a doc from Fulltext index using the xml parameter received
 * @param XMLObjects FT deleting parameters
 * @return empty string when ok
 * @throws PDException In any error
 */
private String FTDel(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Id");
Node OPDObject = OPDObjectList.item(0);
String Id=OPDObject.getTextContent();
try {
PDDocs D=new PDDocs(this);
D.Load(Id);
D.ExecuteFTDel();
} catch (Exception Ex)
    {
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }   
return("");    
}
    
//-----------------------------------------------------------------------------------
/**
 * Inserts a doc in Fulltext index using the xml parameter received
 * @param XMLObjects FT insert parameters
 * @return empty string when ok
 * @throws PDException In any error
 */
private String FTIns(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Type");
Node OPDObject = OPDObjectList.item(0);
String Type=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Id");
OPDObject = OPDObjectList.item(0);
String Id=OPDObject.getTextContent();
try {
PDDocs D=new PDDocs(this, Type);
D.setPDId(Id);
D.ExecuteFTAdd();
} catch (Exception Ex)
    {
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }   
return("");    
}
//-----------------------------------------------------------------------------------
/**
 * Updates a doc in Fulltext index using the xml parameter received
 * @param XMLObjects FT Update parameters
 * @return empty string when ok
 * @throws PDException In any error
 */
private String FTUpd(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Type");
Node OPDObject = OPDObjectList.item(0);
String Type=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Id");
OPDObject = OPDObjectList.item(0);
String Id=OPDObject.getTextContent();
try {
PDDocs D=new PDDocs(this, Type);
D.setPDId(Id);
D.ExecuteFTUpd();
} catch (Exception Ex)
    {
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }   
return("");    
}
private int ImpFolds;
private int ImpDocs;
//-----------------------------------------------------------------------------------
public void ImportFolder(PDFolders FoldAct, String OriginPath, boolean IsOneLevel, boolean IncludeMetadata, boolean IncludeDocs, String FoldType, String DocType, boolean Strict) throws PDException
{
ImpFolds=0;
ImpDocs=0;
Import(FoldAct, OriginPath, IsOneLevel, IncludeMetadata, IncludeDocs, FoldType, DocType, Strict);
}
//---------------------------------------------------------------------
private void Import(PDFolders FoldAct, String OriginPath, boolean IsOneLevel, boolean IncludeMetadata, boolean IncludeDocs, String FoldType, String DocType, boolean Strict) throws PDException
{
PDFolders NewFold=new PDFolders(FoldAct.getDrv(), FoldType); 
if (OriginPath.lastIndexOf(File.separatorChar)==OriginPath.length()-1) // ends with separtos
   OriginPath=OriginPath.substring(0, OriginPath.length()-1);
boolean FoldExist=false;
if (!Strict)
    {
    String Name=OriginPath.substring(OriginPath.lastIndexOf(File.separatorChar)+1);
    try {
    String IdFold=NewFold.GetIdChild(FoldAct.getPDId(), Name);
    NewFold.Load(IdFold);
    FoldExist=true;
    } catch( PDException ex)
        { // don't exits
        }
    }
if (Strict || (!Strict && !FoldExist))
    {
    if (IncludeMetadata)   
        {
        try{    
        NewFold=NewFold.ProcessXML(new File(OriginPath+".opd"), FoldAct.getPDId()); 
        } catch (PDException ex)
            {
            throw new PDException(ex.getLocalizedMessage()+"->"+OriginPath+".opd");    
            }
        }   
    else
        {
        String Name=OriginPath.substring(OriginPath.lastIndexOf(File.separatorChar)+1);
        NewFold.setTitle(Name);
        NewFold.setParentId(FoldAct.getPDId());   
        NewFold.insert();
        }
    }
ImpFolds++;
File ImpFold=new File(OriginPath);
File []ListOrigin=ImpFold.listFiles();
ArrayList DirList=new ArrayList(5);
for (File ListElement : ListOrigin)
    {
    if (ListElement.isDirectory())
        {
        if (!IsOneLevel)
            DirList.add(ListElement);
        continue;
        }
    if (IncludeDocs)
        {
        if (ListElement.getName().endsWith(".opd"))
            {
            if (IncludeMetadata)
                {
                try {
                    ImpDocs+=ProcessXML(ListElement, NewFold.getPDId());
                } catch (PDException ex)
                    {
                    throw new PDException(ex.getLocalizedMessage()+"->"+ListElement.getAbsolutePath());    
                    }
                }
            }
        else
            {
            if (!IncludeMetadata)
                {
                PDDocs NewDoc=new PDDocs(FoldAct.getDrv(), DocType);
                NewDoc.setTitle(ListElement.getName());
                NewDoc.setFile(ListElement.getAbsolutePath());
                NewDoc.setDocDate(new Date(ListElement.lastModified()));
                NewDoc.setParentId(NewFold.getPDId());
                NewDoc.insert();
                ImpDocs++;
                }
            }
        }
    }
ListOrigin=null; // to help gc and save memory during recursivity
for (int i = 0; i < DirList.size(); i++)
    {
    File SubDir = (File) DirList.get(i);
    Import(NewFold, SubDir.getAbsolutePath(), IsOneLevel, IncludeMetadata, IncludeDocs, FoldType, DocType, Strict);    
    }
}
//---------------------------------------------------------------------
/**
* @return the ImpFolds
*/
public int getImpFolds()
{
return ImpFolds;
}
//---------------------------------------------------------------------
/**
* @return the ImpDocs
*/
public int getImpDocs()
{
return ImpDocs;
}
public ArrayList<String> ImportThes(String FolderPath) throws PDException
{
ArrayList<String> ListImps=new ArrayList();   
PDThesaur Thes=new PDThesaur(this);
ListImps.addAll(Thes.ImportPackThes(FolderPath));
return(ListImps);    
}
//---------------------------------------------------------------------
public ArrayList<String> ImportDefs(String FolderPath, boolean CreateTypes) throws PDException
{  
PDObjDefs Defs=new PDObjDefs(this);
HashSet<String> ListPreviousDefsDocs=Defs.getNamesUncreatedDefs(false);
HashSet<String> ListPreviousDefsFolds=Defs.getNamesUncreatedDefs(true);
ArrayList<String> ListImps=new ArrayList();   
File FDef=new File(FolderPath);
File[] ListDefs = FDef.listFiles();
if (ListDefs!=null)
    {
    Arrays.sort(ListDefs);
    for (File ListDef : ListDefs)
        {
        try {
            ProcessXML(ListDef, PDFolders.SYSTEMFOLDER);
            ListImps.add("ObjDef imported:" + ListDef);
        } catch (Exception Ex)
            {
            ListImps.add("Error in ObjDef import:" + ListDef);
            ListImps.add("Error:"+Ex.getLocalizedMessage());
            }
        }
    if (CreateTypes)
        {
        TreeSet<String> ListUpdatedDefsDocs=new TreeSet<>(Defs.getNamesUncreatedDefs(false));
        TreeSet<String> ListUpdatedDefsFolds=new TreeSet<>(Defs.getNamesUncreatedDefs(true));
        for (String NameDef : ListUpdatedDefsDocs)
            {    
            if (!ListPreviousDefsDocs.contains(NameDef))
                Defs.CreateObjectTables(NameDef, false);
            }
        for (String NameDef : ListUpdatedDefsFolds)
            {    
            if (!ListPreviousDefsFolds.contains(NameDef))
                Defs.CreateObjectTables(NameDef, true);
            }
        }
    }
return(ListImps);    
}
//---------------------------------------------------------------------
public ArrayList<String> ImportPack(String FolderPath) throws PDException
{
ArrayList<String> ListDef=ImportThes(FolderPath+"/Thes");    
ListDef.addAll(ImportDefs(FolderPath+"/Def", true));
PDFolders FoldRoot=new PDFolders(this);
FoldRoot.Load(PDFolders.ROOTFOLDER);
File FDef=new File(FolderPath+"/Obj");
File[] ListFolds = FDef.listFiles();
for (int i = 0; i < ListFolds.length; i++)
    {
    File Fold = ListFolds[i];
    if (Fold.isDirectory())    
        ImportFolder(FoldRoot, Fold.getAbsolutePath(), false, true, true, PDFolders.getTableName(), PDDocs.getTableName(), false);  
    }
return(ListDef);
}
//---------------------------------------------------------------------
//***********************************************************************************
class FilterOPDFiles implements FileFilter
{
@Override
public boolean accept(File PathName)
{
if (PathName.getName().toLowerCase().endsWith(".opd"))   
    return(true);
else
    return(false);    
}
}
//***********************************************************************************
}