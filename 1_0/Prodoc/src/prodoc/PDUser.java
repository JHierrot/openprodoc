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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author jhierrot
 */
public class PDUser extends ObjPD
{
/**
 *
 */
public static final String fNAME="Name";
/**
 *
 */
public static final String fPASSWORD="Password";
/**
 *
 */
public static final String fDESCRIPTION="Description";
/**
 *
 */
public static final String fEMAIL="email";
/**
 *
 */
public static final String fVALIDATION="Validation";
/**
 *
 */
public static final String fACTIVE="Active";
/**
 *
 */
public static final String fROLE="Role";
/**
 *
 */
public static final String fCUSTOM="Custom";
/**
 *
 */
static private Record UserStruct=null;

/**
 *
 */
private String Name;
/**
 *
 */
private String Password;
/**
 *
 */
private String Description;
/**
 *
 */
private String Validation;
/**
 *
 */
private String eMail;
/**
 *
 */
private boolean Active=true;
/**
 *
 */
private String Role;
/**
 *
 */
private String Custom;

/**
 *
 */
private PDRoles Rol=null;
/**
 *
 */
private PDCustomization CustomData=null;

/**
 *
 */
private HashMap AclList=null;
/**
 *
 */
private HashSet GroupList=null;

/**
 *
 */
private Iterator iAcl;

static private ObjectsCache UserObjectsCache = null;


//-------------------------------------------------------------------------
/**
 *
 * @param Drv 
 * @throws PDException
 */
public PDUser(DriverGeneric Drv) throws PDException
{
super(Drv);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Rec
 * @throws PDException
 */
public PDUser(Record Rec) throws PDException
{
super();
assignValues(Rec);
}
//-------------------------------------------------------------------------
public void assignValues(Record Rec) throws PDException
{
setName((String) Rec.getAttr(fNAME).getValue());
setDirectPassword((String) Rec.getAttr(fPASSWORD).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
seteMail((String) Rec.getAttr(fEMAIL).getValue());
setValidation((String) Rec.getAttr(fVALIDATION).getValue());
if (Rec.getAttr(fACTIVE).getValue()!=null)
    setActive(((Boolean)Rec.getAttr(fACTIVE).getValue()).booleanValue());
setRole((String)Rec.getAttr(fROLE).getValue());
setCustom((String)Rec.getAttr(fCUSTOM).getValue());
assignCommonValues(Rec);
}
//-------------------------------------------------------------------------
/**
* @return the Name
*/
public String getName()
{
return Name;
}
//-------------------------------------------------------------------------

/**
 * @param Name the Name to set
 * @throws PDExceptionFunc  
*/
public void setName(String Name) throws PDExceptionFunc
{
this.Name = CheckName(Name);
}
//-------------------------------------------------------------------------
/**
* @return the Password
*/
public String getPassword()
{
return Password;
}
//-------------------------------------------------------------------------

/**
* @param pPassword the Password to set
*/
public void setPassword(String pPassword)
{
Password = DriverGeneric.Encode(pPassword);
}
//-------------------------------------------------------------------------
/** Assign password from internal methods (load, assignvalues, etc) to avoid double encoding.
* @param pPassword the Password to set
*/
private void setDirectPassword(String pPassword)
{
Password = pPassword;
}
//-------------------------------------------------------------------------

/**
* @return the Description
*/
public String getDescription()
{
return Description;
}
//-------------------------------------------------------------------------

/**
* @param Description the Description to set
*/
public void setDescription(String Description)
{
this.Description = Description;
}
//-------------------------------------------------------------------------

/**
* @return the eMail
*/
public String geteMail()
{
return eMail;
}
//-------------------------------------------------------------------------

/**
* @param eMail the eMail to set
*/
public void seteMail(String eMail)
{
this.eMail = eMail;
}
//-------------------------------------------------------------------------

/**
* @return the ACLNames
*/
public int getACLNumber()
{
return AclList.size();
}
//-------------------------------------------------------------------------
///**
//*
//*/
//public void initACLList()
//{
//iAcl=AclList.iterator();
//}
////-------------------------------------------------------------------------
///**
// *
// * @return
// */
//public String NextACL()
//{
//if (iAcl.hasNext())
//    return (String)iAcl.next();
//else
//    return(null);
//}
////-------------------------------------------------------------------------
/**
 *
 * @return
 */
public String ACLList()
{
StringBuffer ACLList=new StringBuffer();
String List[]=(String [])AclList.keySet().toArray();
int Tot=AclList.size();
for (int i = 0; i < Tot; i++)
    {
    ACLList.append(List[i]);
    if (i<Tot-1)
       ACLList.append(',');
    }
return ACLList.toString();
}
//-------------------------------------------------------------------------
///**
// *
// * @param newACL
// */
//public void AddACL(String newACL)
//{
//AclList.add(newACL);
//}
//-------------------------------------------------------------------------
///**
// *
// */
//public void ClearACL()
//{
//AclList.clear();
//}
//-------------------------------------------------------------------------
/**
 * object "method" needed because static overloading doesn't work in java
 * @return
 */
public String getTabName()
{
return(getTableName());
}
//-------------------------------------------------------------------------
/**
 * static equivalent method
 * @return
 */
static public String getTableName()
{
return("PD_USERS");
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException 
 */
@Override
synchronized public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fPASSWORD).setValue(getPassword());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fEMAIL).setValue(geteMail());
Rec.getAttr(fVALIDATION).setValue(getValidation());
Rec.getAttr(fACTIVE).setValue(isActive());
Rec.getAttr(fROLE).setValue(getRole());
Rec.getAttr(fCUSTOM).setValue(getCustom());
getCommonValues(Rec);
return(Rec);
}
//-------------------------------------------------------------------------
/**
 * 
 */
synchronized Record getRecordStruct() throws PDException
{
if (UserStruct==null)
    UserStruct=CreateRecordStruct();
return(UserStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 *
 */
static synchronized private Record CreateRecordStruct() throws PDException
{
if (UserStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, "User_Name", "User_Name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPASSWORD,"Password", "Password", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fDESCRIPTION, "Description", "Description", Attribute.tSTRING, false, null, 128, false, false, true));
    R.addAttr( new Attribute(fEMAIL, "eMail", "eMail", Attribute.tSTRING, false, null, 128, false, false, true));
    R.addAttr( new Attribute(fVALIDATION, "Validation", "Validation_name", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fACTIVE, "Active", "Active", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fROLE, "Rol", "Name_of_assigned_rol", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fCUSTOM, "Customization_name", "Customization_name", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(UserStruct);
}
//-------------------------------------------------------------------------
/**
* @return the Active
*/
public boolean isActive()
{
return Active;
}
//-------------------------------------------------------------------------
/**
* @param Active the Active to set
*/
public void setActive(boolean Active)
{
this.Active = Active;
}
//-------------------------------------------------------------------------
/**
 *
 */
protected Conditions getConditions() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fNAME, Condition.cEQUAL, getName()));
return(ListCond);
}
//-------------------------------------------------------------------------
protected Conditions getConditionsLike(String Name) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fNAME, Condition.cLIKE, VerifyWildCards(Name)));
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 *
 */
protected void DeleteMulti() throws PDException
{
PDACL AclTmp=new PDACL(getDrv());
AclTmp.delUserReferences(getName());
PDGroups GroupTmp=new PDGroups(getDrv());
GroupTmp.delUserReferences(getName());
PDFolders f=new PDFolders(getDrv());
f.setPDId(getUserFolder());
f.delete();
PDACL A=new PDACL(getDrv());
A.setName("U_"+getName());
A.delete();
}

/**
* @return the Validation
*/
public String getValidation()
{
return Validation;
}

/**
 * Method for validation of the user (password, ldap, etc)
 * @param pValidation the Validation to set
 * @throws PDException 
*/
public void setValidation(String pValidation) throws PDException
{
//if (PDAuthenticators.gettList().contains(pValidation))
    Validation = pValidation;
//else
//    throw new PDException("Criterios de validación erroneo");
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @throws PDExceptionFunc  
 */
protected void AsignKey(String Ident) throws PDExceptionFunc
{
setName(Ident);
}

/**
* @return the AclList
*/
public HashMap getAclList()
{
return AclList;
}

/**
* @param AclList the AclList to set
*/
private void setAclList(HashMap AclList)
{
this.AclList = AclList;
}

/**
* @return the GroupList
*/
public HashSet getGroupList()
{
return GroupList;
}

/**
* @param GroupList the GroupList to set
*/
public void setGroupList(HashSet GroupList)
{
this.GroupList = GroupList;
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void InsertMulti() throws PDException
{
PDGroups G=new PDGroups(getDrv());
G.setName("All");
G.addUser(getName());
PDACL A=new PDACL(getDrv());
A.setName("U_"+getName());
A.setDescription("Permiso para carpeta personal de "+getName());
A.insert();
A.addUser(getName(), PDACL.pDELETE);
if (!getName().equalsIgnoreCase("root"))
    A.addGroup("Administrators", PDACL.pDELETE);
PDFolders f=new PDFolders(getDrv());
f.setPDId(getUserFolder());
f.setTitle(getName());
f.setParentId(PDFolders.USERSFOLDER);
f.setACL(A.getName());
f.insert();
if (!getDrv().getUser().getName().equals("Install"))
    getDrv().RefreshUser(); // to be able of delete the created folder IN the SAME Session reading the ACL
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected  void InstallMulti()  throws PDException
{
getDrv().AddIntegrity(getTabName(), fCUSTOM, PDCustomization.getTableName(), PDCustomization.fNAME);
getDrv().AddIntegrity(getTabName(), fROLE, PDRoles.getTableName(), PDRoles.fNAME);
getDrv().AddIntegrity(getTabName(), fVALIDATION, PDAuthenticators.getTableName(), PDAuthenticators.fNAME);
}
//-------------------------------------------------------------------------
/**
* @return the Role
*/
public String getRole()
{
return Role;
}
//-------------------------------------------------------------------------
/**
* @param Role the Role to set
*/
public void setRole(String Role)
{
this.Role = Role;
}
//-------------------------------------------------------------------------
/**
 * 
 * @param UserName
 * @throws PDException 
 */
public void LoadAll(String UserName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDUser.LoadAll>:"+UserName);
int NumErrors=0;
String Error="";
try {
if (Load(UserName)==null)
    PDExceptionFunc.GenPDException("Incorrect_user_or_password", UserName);
} catch (PDException pDException)
    {
    if (!UserName.equals("Install"))
        throw pDException;
    Error+=pDException.getLocalizedMessage();
    NumErrors++;
    }
try {
PDGroups G = new PDGroups(getDrv());
setGroupList(G.FullUserMemberShip(UserName));
} catch (PDException pDException)
    {
    if (!UserName.equals("Install"))
        throw pDException;
    Error+="/"+pDException.getLocalizedMessage();
    NumErrors++;
    }
try {
PDACL A = new PDACL(getDrv());
setAclList(A.FullUserMemberShip(this));
} catch (PDException pDException)
    {
    if (!UserName.equals("Install"))
        throw pDException;
    Error+="/"+pDException.getLocalizedMessage();
    NumErrors++;
    }
try {
Rol=new PDRoles(getDrv());
Rol.Load(getRole());
} catch (PDException pDException)
    {
    Error+=pDException.getLocalizedMessage();
    if (!UserName.equals("Install") || NumErrors<2)
        throw new PDException(Error);
    Rol=CreateSuperRol();
    }
try {
CustomData=new PDCustomization(getDrv());
getCustomData().Load(getCustom());
} catch (PDException pDException)
    {if (!UserName.equals("Install"))
        throw pDException;
    }
if (PDLog.isDebug())
    PDLog.Debug("PDUser.LoadAll<:"+UserName);
}
/**
 * Creates rol for install o task users
 * @param drv Session of rol
 * @return Created Rol
 */
private PDRoles CreateSuperRol() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDUser.CreateSuperRol>");        
PDRoles Rol1=new PDRoles(getDrv());
Rol1.setName("Administrators");
Rol1.setAllowCreateUser(true);
Rol1.setAllowMaintainUser(true);
Rol1.setAllowCreateGroup(true);
Rol1.setAllowMaintainGroup(true);
Rol1.setAllowCreateAcl(true);
Rol1.setAllowMaintainAcl(true);
Rol1.setAllowCreateRole(true);
Rol1.setAllowMaintainRole(true);
Rol1.setAllowCreateObject(true);
Rol1.setAllowMaintainObject(true);
Rol1.setAllowCreateRepos(true);
Rol1.setAllowMaintainRepos(true);
Rol1.setAllowCreateFolder(true);
Rol1.setAllowMaintainFolder(true);
Rol1.setAllowCreateDoc(true);
Rol1.setAllowMaintainDoc(true);
Rol1.setAllowCreateMime(true);
Rol1.setAllowMaintainMime(true);
Rol1.setAllowCreateThesaur(true);
Rol1.setAllowMaintainThesaur(true);
Rol1.setAllowCreateTask(true);
Rol1.setAllowMaintainTask(true);
if (PDLog.isDebug())
    PDLog.Debug("PDUser.CreateSuperRol<");        
return Rol1;
}
//-------------------------------------------------------------------------
/**
 * Creates special user for task
 * @return Creates PDUser
 */
protected void CreateTaskUser() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDUser.CreateTaskUser>");    
Rol=CreateSuperRol();
PDACL A = new PDACL(getDrv());
setAclList(A.FillAclTaskUser());
CustomData=new PDCustomization(getDrv());
setName("TaskUser");
setDescription("TaskUser");
setActive(true);
if (PDLog.isDebug())
    PDLog.Debug("PDUser.CreateTaskUser>");    
}
//-------------------------------------------------------------------------
/**
 *
 * @param Acl
 * @return
 */
public int PermisionForAcl(String Acl)
{
Integer ValAct=(Integer) this.getAclList().get(Acl);
if (ValAct==null)
    return(PDACL.pNOTHING);
return(ValAct.intValue());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))  
    if (!getDrv().getUser().getRol().isAllowCreateUser() )
       PDExceptionFunc.GenPDException("User_creation_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainUser() )
   PDExceptionFunc.GenPDException("User_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainUser() )
   PDExceptionFunc.GenPDException("User_modification_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
/**
 * @return the a COPY of the Rol (for security reasons to avoid manipulation
 *         the rol is loaded by PDUser)
 * @throws PDException
*/
public PDRoles getRol() throws PDException
{
PDRoles R=new PDRoles(getDrv());
R.assignValues(Rol.getRecord());
return R;
}
////-------------------------------------------------------------------------
//public String CreateFolder()  throws PDException
//{
//PDFolders f=new PDFolders(getDrv());
//String FId=PDFolders.USERSFOLDER+"/"+getName();
//f.setPDId(FId);
//f.setTitle(getName());
//f.setParentId(PDFolders.USERSFOLDER);
//f.setACL("Public");
//f.insert();
//return(FId);
//}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getDefaultOrder()
{
return(fNAME);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
public String getUserFolder()
{
return(PDFolders.USERSFOLDER+"/"+getName());
}
//-------------------------------------------------------------------------

/**
 * @return the Custom
 */
public String getCustom()
{
return Custom;
}

/**
 * @param Custom the Custom to set
 */
public void setCustom(String Custom)
{
this.Custom = Custom;
}

/**
 * @return the CustomData
 */
public PDCustomization getCustomData()
{
return CustomData;
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (UserObjectsCache==null)
    UserObjectsCache=new ObjectsCache("User");
return(UserObjectsCache);    
}
//-------------------------------------------------------------------------
/**
 * Returns the value/field used as key of the object (Name) to ve used in Cache index
 * @return the value of the  Name field
 */
@Override
protected String getKey()
{
return(getName());
}
//-------------------------------------------------------------------------
}