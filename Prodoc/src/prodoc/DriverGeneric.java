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

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prodoc.security.*;

/**
 *
 * @author jhierrot
 */
abstract public class DriverGeneric
{
/**
 *
 */
private String URL;
/**
 *
 */
private String PARAM;
/**
 *
 */
private String DBUser;
/**
 *
 */
private String DBPassword;
/**
 *
 */
private boolean Locked=false;
/**
 *
 */
private Date TimeUsed;
/**
 *
 */
private Date TimeLocked;
/**
 *
 */
private PDUser User=null;
/**
 *
 */
private boolean InTransaction=false;
/**
 *
 */
private HashMap OpenCur=null;
/**
 *
 */
private HashMap Class2Reposit=null;
/**
 *
 */
private HashMap ListReposit=null;
/**
 *
 */
private HashMap ListAuth=null;
private final char[] ServerKey = "Esta es la clave".toCharArray();
private static HashMap TransList=new HashMap();
private String AppLang=null;
private static String DefAppLang=null;
private PDCustomization PDCust=null;

/**
 *
 * @param pURL
 * @param pPARAM
 * @param pUser
 * @param pPassword
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
 * 
 * @return
 */
public boolean isLocked()
{
return(Locked);
}
//--------------------------------------------------------------------------
/**
 *
 */
public void Lock()
{
Locked=true;
TimeLocked=new Date();
}
//--------------------------------------------------------------------------
/**
 *
 */
public void UnLock()
{
Locked=false;
TimeLocked=null;
}
//--------------------------------------------------------------------------
/**
* @return the TimeUsed
*/
public Date getTimeUsed()
{
return TimeUsed;
}
//--------------------------------------------------------------------------

/**
* @return the TimeLocked
*/
public Date getTimeLocked()
{
return TimeLocked;
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
abstract public void delete() throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @return
 */
abstract public boolean isConnected();
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param Fields
 * @throws PDException
 */
abstract protected void CreateTable(String TableName, Record Fields) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @throws PDException
 */
abstract protected void DropTable(String TableName) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param Fields New fields to add
 * @throws PDException
 */
abstract protected void AlterTable(String TableName, Record Fields) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param Fields
 * @throws PDException
 */
abstract protected void InsertRecord(String TableName, Record Fields) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param DelConds 
 * @throws PDException
 */
abstract protected void DeleteRecord(String TableName, Conditions DelConds) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param NewFields
 * @param UpConds 
 * @throws PDException
 */
abstract protected void UpdateRecord(String TableName, Record NewFields, Conditions UpConds) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName1
 * @param Field1
 * @param TableName2
 * @param Field2 
 * @throws PDException
 */
abstract protected void AddIntegrity(String TableName1, String Field1, String TableName2, String Field2) throws PDException;
//--------------------------------------------------------------------------
/**
 * 
 * @param RootPassword 
 * @param DefLang 
 * @param RepUrl 
 * @param DefTimeFormat 
 * @param DefDateFormat 
 * @param RepName 
 * @param MainKey 
 * @param RepEncrypt 
 * @param Trace 
 * @param RepUser 
 * @param RepParam 
 * @param RepType 
 * @param RepPassword 
 * @throws PDException
 */
public void Install(String RootPassword, String DefLang, String DefTimeFormat, 
                    String DefDateFormat, String MainKey, String RepName,
                    boolean RepEncrypt, String RepUrl, String RepUser,
                    String RepPassword, String RepType, String RepParam, Vector Trace) throws PDException
{
//-------- Tables creation ----
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
//-------- Tables creation 2 phase ----
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
//--- insertion of objects ----
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
Cu.setDescription("Estilo por defecto de Prodoc");
Cu.setDateFormat(DefDateFormat); // "dd-MM-yyyy"
Cu.setTimeFormat(DefTimeFormat); // "dd-MM-yyyy HH:mm:ss"
Cu.setLanguage(DefLang);
Cu.setStyle("");
Cu.setSwingStyle("\"Arial\",0,12,\"Arial\",0,12,\"Arial\",0,12,\"Arial\",0,12");
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
Trace.add("ACL elements created");
// --- Group all ---
PDGroups Grp=new PDGroups(this);
Grp.setName("All");
Grp.setDescription("All users group");
Grp.setAcl("Administrators");
Grp.insert();
Grp.setName("Administrators");
Grp.setDescription("Users with administration rights");
Grp.setAcl("Administrators");
Grp.insert();
Trace.add("Groups elements created");
//---- Definitions folder -----------------
D.setName(PDFolders.getTableName());
D.setDescription("Base Folder");
D.setActive(true);
D.setClassType(PDObjDefs.CT_FOLDER);
D.setParent(PDFolders.getTableName());
D.setACL(Aclpublic.getName());
D.insert();
Record Rec=PDFolders.getRecordStructPDFolder();
Rec.initList();
for (int i = 0; i < Rec.NumAttr(); i++)
    {
    D.addAtribute(Rec.nextAttr());
    }
Trace.add("Definitions elements created");
// --- Folders ---
PDFolders.CreateBaseRootFold(this);
PDFolders UsersFold=new PDFolders(this);
//UsersFold.setPDId(PDFolders.USERSFOLDER);
//UsersFold.setTitle(PDFolders.USERSFOLDER);
//UsersFold.setParentId(PDFolders.ROOTFOLDER);
//UsersFold.setACL("Public");
// --- Administrator ---
PDUser Usu=new PDUser(this);
Usu.setName("root");
Usu.setPassword(RootPassword);
Usu.setDescription("Administrator");
Usu.setActive(true);
Usu.setValidation(Au.getName());
Usu.setRole(Ro.getName());
Usu.setCustom(Cu.getName());
Usu.insert();
Grp.addUser("root");
Trace.add("Administrator created");

// --- Group Administrator ---
AclAdmin.addUser( "root", PDACL.pDELETE);
AclAdmin.addGroup("Administrators", PDACL.pDELETE);
Aclpublic.addUser( "root", PDACL.pDELETE);
Aclpublic.addGroup("Administrators", PDACL.pDELETE);
Aclpublic.addGroup("All", PDACL.pREAD);
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
D.setACL(Aclpublic.getName());
D.setReposit(R.getName());
D.insert();
Record RecD=DD.getRecordStruct();
RecD.initList();
for (int i = 0; i < RecD.NumAttr(); i++)
    {
    D.addAtribute(RecD.nextAttr());
    }
Trace.add("Document elements created");
//----------------------
File FileImp=new File("ex/defs.opd");
ProcessXML(FileImp, PDFolders.ROOTFOLDER);
this.CerrarTrans();
Trace.add("Installation finished");
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException 
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
* @return the InTransaction
*/
public boolean isInTransaction()
{
return InTransaction;
}
//--------------------------------------------------------------------------
/**
 * @param pInTransaction 
*/
public void setInTransaction(boolean pInTransaction)
{
InTransaction = pInTransaction;
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
abstract public void IniciarTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
abstract public void CerrarTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 * 
 * @throws PDException
 */
abstract public void AnularTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @param Search
 * @return a CursorCode, stored by the drivers
 * @throws PDException
 */
abstract public Cursor OpenCursor(Query Search) throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @return
 */
private String genCursorName()
{
return ("Cursor"+(getOpenCur().size()*2));
}
////-----------------------------------------------------------------------------------
//private int IdentifyCursor(String CursorIdent)  throws PDException
//{
//if (CursorIdent.length()<7)
//    throw new PDException("Cursor no identificado");
//int NumCur=Integer.parseInt(CursorIdent.substring(6));
//if (NumCur<0||NumCur>=getOpenCur().size())
//    throw new PDException("Cursor Inexistente");
//return(NumCur);
//}
//-----------------------------------------------------------------------------------
/**
* @return the OpenCur
*/
private HashMap getOpenCur()
{
if (OpenCur==null)
    OpenCur=new HashMap();
return OpenCur;
}
//-----------------------------------------------------------------------------------
///**
//* @return the OpenCur
//*/
//private Vector getFieldsCur()
//{
//if (FieldsCur==null)
//    FieldsCur=new Vector();
//return FieldsCur;
//}
//-----------------------------------------------------------------------------------
/**
 *
 * @param rs
 * @param Fields
 * @return
 */
protected Cursor StoreCursor(Object rs, Record Fields)
{
Cursor Cur=new Cursor();
Cur.setCursorId(genCursorName());
Cur.setFieldsCur(Fields);
Cur.setResultSet(rs);
getOpenCur().put(Cur.getCursorId(), Cur);
return(Cur);
}
//-----------------------------------------------------------------------------------
/**
 * 
 * @param CursorIdent
 * @return
 * @throws PDException 
 */
protected Object getCursor(Cursor CursorIdent) throws PDException
{
return(CursorIdent.getResultSet());
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param CursorIdent
 * @throws PDException
 */
protected void delCursor(Cursor CursorIdent) throws PDException
{
getOpenCur().remove(CursorIdent.getCursorId());
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param CursorIdent
 * @return
 * @throws PDException
 */
abstract public Record NextRec(Cursor CursorIdent)  throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @param CursorIdent
 * @throws PDException
 */
abstract public void CloseCursor(Cursor CursorIdent) throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
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
//-----------------------------------------------------------------------------------
/**
 * 
 * @throws PDException
 */
public void RefreshUser() throws PDException
{
getUser().LoadAll(getUser().getName());
}
//-----------------------------------------------------------------------------------
/**
 * @return the User
 * @throws PDException
*/
public PDUser getUser() throws PDException
{
if (User==null)
   User=new PDUser(this);
return User;
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param tableName 
 * @param TypeDef
 * @param TypeRecs
 * @throws PDException
 */
protected void LoadDef(String tableName, ArrayList TypeDef, ArrayList TypeRecs) throws PDException
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
public void Export(PrintWriter FileOut, ObjPD Obj) throws PDException
{
FileOut.println(Obj.getTabName());
Export(FileOut, Obj.getRecord());
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param ObjName
 * @param RecList
 * @throws PDException
 */
public void Export(PrintWriter FileOut, String ObjName, Vector RecList) throws PDException
{
for (int i = 0; i < RecList.size(); i++)
    {
    FileOut.println(ObjName);
    Record Rec = (Record)RecList.elementAt(i);
    Export(FileOut, Rec);
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param ObjName
 * @param CursorId
 * @throws PDException
 */
public void Export(PrintWriter FileOut, String ObjName, Cursor CursorId) throws PDException
{
Record Rec=this.NextRec(CursorId);
while (Rec!=null)
    {
    FileOut.println(ObjName);
    Export(FileOut, Rec);
    Rec=this.NextRec(CursorId);
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param Rec
 * @throws PDException
 */
public void Export(PrintWriter FileOut, Record Rec) throws PDException
{
FileOut.println(Rec.NumAttr());
Rec.initList();
for (int i = 0; i < Rec.NumAttr(); i++)
    {
    Attribute A=Rec.nextAttr();
    FileOut.println(A.getName());
    FileOut.println(A.Export());
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileIn
 * @param Rec
 * @throws PDException
 */
public void Import(BufferedReader FileIn, Record Rec) throws PDException
{
try {
String NumStr = FileIn.readLine();
int NumAtt = Integer.parseInt(NumStr);
String NomAtt;
String ValAtt;
for (int i = 0; i < NumAtt; i++)
    {
    NomAtt=FileIn.readLine();
    Attribute Att = Rec.getAttr(NomAtt);
    if (Att==null)
        PDExceptionFunc.GenPDException("Unknown_attibute",NomAtt);
    ValAtt=FileIn.readLine();
    Att.Import(ValAtt);
    }
} catch (IOException ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(),null);
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Rep
 * @throws PDException
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
 *
 * @param Rep
 * @throws PDException
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
 *
 * @param Rep
 * @return
 * @throws PDException
 */
private StoreGeneric ConstrucStore(PDRepository Rep) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstrucStore>:"+Rep.getName());
StoreGeneric st=null;
String RepTyp=Rep.getRepType();
if (RepTyp.equals(PDRepository.tFS))
    st=new StoreFS(Rep.getURL(), Rep.getUser(), Decode(Rep.getPassword()), Rep.getParam());
else if (RepTyp.equals(PDRepository.tBBDD))
    st=new StoreDDBB(Rep.getURL(), Rep.getUser(), Decode(Rep.getPassword()), Rep.getParam());
else if (RepTyp.equals(PDRepository.tFTP))
    st=new Storeftp(Rep.getURL(), Rep.getUser(), Decode(Rep.getPassword()), Rep.getParam());
else if (RepTyp.equals(PDRepository.tREFURL))
    st=new StoreRefURL(Rep.getURL(), Rep.getUser(), Decode(Rep.getPassword()), Rep.getParam());
else
    PDException.GenPDException("Repository_type_unsuported", RepTyp);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstrucStore<");
return(st);
}
//-----------------------------------------------------------------------------------
/**
 * Search, for a DocType, the define Reposit
 * @param docType
 * @return
 * @throws PDException
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
 *
 * @param RepName
 * @return
 * @throws PDException
 */
protected StoreGeneric getRepository(String RepName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getRepository>:"+RepName);
StoreGeneric Rep=null;
Rep=(StoreGeneric)getListReposit().get(RepName);
if (Rep!=null)
    {
    if (PDLog.isDebug())
        PDLog.Debug("DriverGeneric.Rep yet Instanced:"+Rep.getServer());
    return (Rep);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Rep new Instance:"+RepName);
PDRepository RepDesc=new PDRepository(this);
RepDesc.Load(RepName);
Rep=ConstrucStore(RepDesc);
getListReposit().put(RepName, Rep);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getRepository<:"+RepName);
return(Rep);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param ValidatName
 * @return
 * @throws PDException
 */
private AuthGeneric getAuthentic(String ValidatName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAuthentic>:"+ValidatName);
AuthGeneric Auth=null;
Auth=(AuthGeneric)getListAuth().get(ValidatName);
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
 *
 * @param Auth
 * @return
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
* @return the ListAuth
*/
private HashMap getListAuth()
{
if (ListAuth==null)
    ListAuth=new HashMap();
return ListAuth;
}
//-----------------------------------------------------------------------------------
/**
 * @return the ListReposit
 */
private HashMap getListReposit()
{
if (ListReposit==null)
    ListReposit=new HashMap();
return ListReposit;
}
//-----------------------------------------------------------------------------------
/**
 * 
 * @param UserName
 * @param OldPassword
 * @param NewPassword
 * @throws PDException
 */
public void ChangePassword(String UserName, String OldPassword, String NewPassword) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ChangePassword>:"+UserName);
PDUser U2cp=new PDUser(this);
if (U2cp.Load(UserName)==null)
    PDExceptionFunc.GenPDException("User_or_password_incorrect", UserName);
AuthGeneric Auth=getAuthentic(U2cp.getValidation());
Auth.Authenticate(UserName, OldPassword);
if (U2cp.getValidation().equals(PDAuthenticators.tOPD))
    Auth.UpdatePass(UserName, Encode(NewPassword));
else
    Auth.UpdatePass(UserName, NewPassword);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ChangePassword<:"+UserName);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param UserName
 * @param NewPassword
 * @throws PDException
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
 * Returns the conector version
 * @return
 */
static public String getVersion()
{
return("0.7");
}
private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
//-----------------------------------------------------------------------------------
private static final char[] Key = "Esta es la clave".toCharArray();
/**
 *
 * @param ToBeEncoded
 * @return
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
 *
 * @param ToBeDecoded
 * @return
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
private byte[] EncodeBlock(byte[] buff)
{
for (int i = 0; i <buff.length; i++)
    {
    buff[i]=(byte)(buff[i]^ ServerKey[i%16]);
    }
return buff;
}
//-------------------------------------------------------------------------
private byte[] DecodeBlock(byte[] buff)
{
for (int i = 0; i <buff.length; i++)
    {
    buff[i]=(byte)(buff[i]^ ServerKey[i%16]);
    }
return buff;
}
//-------------------------------------------------------------------------
/**
 * Creates a properties file to be used by any client, including to create metadata repository
 * @param FileName 
 * @param UserName 
 * @param ConnectName 
 * @param UrlServer 
 * @param JDBCClass 
 * @param Password 
 * @throws Exception
 */
static public void generateProps(String FileName, String ConnectName, String UrlServer, String UserName, String Password, String JDBCClass) throws Exception
{
ConnectName=ConnectName.trim();
PrintWriter FProps = new PrintWriter(FileName, "UTF-8");
FProps.println("#------------------------------------------------------------");
FProps.println("# The Kind of connection to data (JDBC, Remote, ...");
FProps.println(ConnectName+".DATA_TYPE=JDBC");
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
FProps.println("# maximum number of sessions in documents repository");
FProps.println(ConnectName+".PR_MIN=1");
FProps.println("# maximum number of sessions in documents repository");
FProps.println(ConnectName+".PR_MAX=1");
FProps.println("# Timeout (in ms) before the conection is closed");
FProps.println(ConnectName+".PR_TIMEOUT=300000");
FProps.println("# TraceLevel LOGLEVELERROR=0, LOGLEVELINFO=1, LOGLEVELDEBUG=2");
FProps.println("TRACELEVEL=0");
FProps.println("# Path to the log4j properties file");
FProps.println("# Beware that the TRACELEVEL has priority over level defined in the file");
FProps.println("TRACECONF=log4j.properties");
FProps.flush();
FProps.close();
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Text
 * @return
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
 * 
 * @param Text
 * @return
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
 * 
 * @param Lang
 * @param Text
 * @return
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
static private Properties getProperties(String Lang)
{
Lang=Lang.toUpperCase();
Properties Trans=(Properties)TransList.get(Lang);
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
 * @param pAppLang the AppLang to set
 */
public void setAppLang(String pAppLang)
{
AppLang = pAppLang.toUpperCase();
}
//---------------------------------------------------------------------
/**
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
 * @param aDefAppLang the DefAppLang to set
 */
public static void setDefAppLang(String aDefAppLang)
{
DefAppLang = aDefAppLang.toUpperCase();
}
//---------------------------------------------------------------------

/**
 * @return the PDCust
 * @throws PDException  
 */
public PDCustomization getPDCust() throws PDException
{
if (PDCust==null)
   PDCust=new PDCustomization(this);
return PDCust;
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLFile
 * @param ParentFolderId
 * @throws PDException
 */
public int ProcessXML(File XMLFile, String ParentFolderId) throws PDException
{
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject = null;
ObjPD Obj2Build=null;
int Tot=0;
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
return(Tot);
}catch(Exception ex)
    {
    ex.printStackTrace();
    PDLog.Error(ex.getLocalizedMessage());
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
private ObjPD BuildObj(Node OPDObject) throws PDException
{
NamedNodeMap attributes = OPDObject.getAttributes();
Node namedItem = attributes.getNamedItem("type");
String OPDObjectType=namedItem.getNodeValue();
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
throw new PDException("Inexistent_OPD_object_type");
}
//---------------------------------------------------------------------
}