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

/**
 *
 * @author jhierrot
 */
public class PDRoles extends ObjPD
{
/**
 *
 */
public static final String fNAME="Name";
/**
 *
 */
public static final String fDESCRIPTION="Description";
/**
 *
 */
public static final String fALLOWCREATEUSER="AllowCreateUser";
/**
 *
 */
public static final String fALLOWMAINTAINUSER="AllowMaintainUser";
/**
 *
 */
public static final String fALLOWCREATEGROUP="AllowCreateGroup";
/**
 *
 */
public static final String fALLOWMAINTAINGROUP="AllowMaintainGroup";
/**
 *
 */
public static final String fALLOWCREATEACL="AllowCreateAcl";
/**
 *
 */
public static final String fALLOWMAINTAINACL="AllowMaintainAcl";
/**
 *
 */
public static final String fALLOWCREATEROLE="AllowCreateRole";
/**
 *
 */
public static final String fALLOWMAINTAINROLE="AllowMaintainRole";
/**
 *
 */
public static final String fALLOWCREATEOBJECT="AllowCreateObject";
/**
 *
 */
public static final String fALLOWMAINTAINOBJECT="AllowMaintainObject";
/**
 *
 */
public static final String fALLOWCREATEREPOS="AllowCreateRepos";
/**
 *
 */
public static final String fALLOWMAINTAINREPOS="AllowMaintainRepos";
/**
 *
 */
public static final String fALLOWCREATEFOLD="AllowCreateFolder";
/**
 *
 */
public static final String fALLOWMAINTAINFOLD="AllowMaintainFolder";
/**
 *
 */
public static final String fALLOWCREATEDOC="AllowCreateDoc";
/**
 *
 */
public static final String fALLOWMAINTAINDOC="AllowMaintainDoc";
/**
 *
 */
public static final String fALLOWCREATEMIME="AllowCreateMime";
/**
 *
 */
public static final String fALLOWMAINTAINMIME="AllowMaintainMime";
/**
 *
 */
public static final String fALLOWCREATEAUTH="AllowCreateAuth";
/**
 *
 */
public static final String fALLOWMAINTAINAUTH="AllowMaintainAuth";
/**
 *
 */
public static final String fALLOWCREATECUSTOM="AllowCreateCustom";
/**
 *
 */
public static final String fALLOWMAINTAINCUSTOM="AllowMaintainCustom";

/**
 *
 */
static private Record RolesStruct=null;
/**
 *
 */
private String Name;
/**
 *
 */
private String Description;
/**
 *
 */
private boolean AllowCreateUser=false;
/**
 *
 */
private boolean AllowMaintainUser=false;
/**
 *
 */
private boolean AllowCreateGroup=false;
/**
 *
 */
private boolean AllowMaintainGroup=false;
/**
 *
 */
private boolean AllowCreateAcl=false;
/**
 *
 */
private boolean AllowMaintainAcl=false;
/**
 *
 */
private boolean AllowCreateRole=false;
/**
 *
 */
private boolean AllowMaintainRole=false;
/**
 *
 */
private boolean AllowCreateObject=false;
/**
 *
 */
private boolean AllowMaintainObject=false;
/**
 *
 */
private boolean AllowCreateRepos=false;
/**
 *
 */
private boolean AllowMaintainRepos=false;
/**
 *
 */
private boolean AllowCreateFolder=false;
/**
 *
 */
private boolean AllowMaintainFolder=false;
/**
 *
 */
private boolean AllowCreateDoc=false;
/**
 *
 */
private boolean AllowMaintainDoc=false;
/**
 *
 */
private boolean AllowCreateMime=false;
/**
 *
 */
private boolean AllowMaintainMime=false;
/**
 *
 */
private boolean AllowCreateAuth=false;
/**
 *
 */
private boolean AllowMaintainAuth=false;
/**
 *
 */
private boolean AllowCreateCustom=false;
/**
 *
 */
private boolean AllowMaintainCustom=false;

static private ObjectsCache RolesObjectsCache = null;

/**
 *
 * @param Drv
 * @throws PDException
 */
public PDRoles(DriverGeneric Drv) throws PDException
{
super(Drv);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Rec
 * @throws PDException
 */
    @Override
public void assignValues(Record Rec) throws PDException
{
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
if (Rec.getAttr(fALLOWCREATEUSER).getValue()!=null)
    setAllowCreateUser(((Boolean)Rec.getAttr(fALLOWCREATEUSER).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINUSER).getValue()!=null)
    setAllowMaintainUser(((Boolean)Rec.getAttr(fALLOWMAINTAINUSER).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEGROUP).getValue()!=null)
    setAllowCreateGroup(((Boolean)Rec.getAttr(fALLOWCREATEGROUP).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINGROUP).getValue()!=null)
    setAllowMaintainGroup(((Boolean)Rec.getAttr(fALLOWMAINTAINGROUP).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEACL).getValue()!=null)
    setAllowCreateAcl(((Boolean)Rec.getAttr(fALLOWCREATEACL).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINACL).getValue()!=null)
    setAllowMaintainAcl(((Boolean)Rec.getAttr(fALLOWMAINTAINACL).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEROLE).getValue()!=null)
    setAllowCreateRole(((Boolean)Rec.getAttr(fALLOWCREATEROLE).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINROLE).getValue()!=null)
    setAllowMaintainRole(((Boolean)Rec.getAttr(fALLOWMAINTAINROLE).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEOBJECT).getValue()!=null)
    setAllowCreateObject(((Boolean)Rec.getAttr(fALLOWCREATEOBJECT).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINOBJECT).getValue()!=null)
    setAllowMaintainObject(((Boolean)Rec.getAttr(fALLOWMAINTAINOBJECT).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEREPOS).getValue()!=null)
    setAllowCreateRepos(((Boolean)Rec.getAttr(fALLOWCREATEREPOS).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINREPOS).getValue()!=null)
    setAllowMaintainRepos(((Boolean)Rec.getAttr(fALLOWMAINTAINREPOS).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEFOLD).getValue()!=null)
    setAllowCreateFolder(((Boolean)Rec.getAttr(fALLOWCREATEFOLD).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINFOLD).getValue()!=null)
    setAllowMaintainFolder(((Boolean)Rec.getAttr(fALLOWMAINTAINFOLD).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEDOC).getValue()!=null)
    setAllowCreateDoc(((Boolean)Rec.getAttr(fALLOWCREATEDOC).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINDOC).getValue()!=null)
    setAllowMaintainDoc(((Boolean)Rec.getAttr(fALLOWMAINTAINDOC).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEMIME).getValue()!=null)
    setAllowCreateMime(((Boolean)Rec.getAttr(fALLOWCREATEMIME).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINMIME).getValue()!=null)
    setAllowMaintainMime(((Boolean)Rec.getAttr(fALLOWMAINTAINMIME).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATEAUTH).getValue()!=null)
    setAllowCreateAuth(((Boolean)Rec.getAttr(fALLOWCREATEAUTH).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINAUTH).getValue()!=null)
    setAllowMaintainAuth(((Boolean)Rec.getAttr(fALLOWMAINTAINAUTH).getValue()).booleanValue());
if (Rec.getAttr(fALLOWCREATECUSTOM).getValue()!=null)
    setAllowCreateCustom(((Boolean)Rec.getAttr(fALLOWCREATECUSTOM).getValue()).booleanValue());
if (Rec.getAttr(fALLOWMAINTAINCUSTOM).getValue()!=null)
    setAllowMaintainCustom(((Boolean)Rec.getAttr(fALLOWMAINTAINCUSTOM).getValue()).booleanValue());
assignCommonValues(Rec);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
@Override
synchronized public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fALLOWCREATEUSER).setValue(isAllowCreateUser());
Rec.getAttr(fALLOWMAINTAINUSER).setValue(isAllowMaintainUser());
Rec.getAttr(fALLOWCREATEGROUP).setValue(isAllowCreateGroup());
Rec.getAttr(fALLOWMAINTAINGROUP).setValue(isAllowMaintainGroup());
Rec.getAttr(fALLOWCREATEACL).setValue(isAllowCreateAcl());
Rec.getAttr(fALLOWMAINTAINACL).setValue(isAllowMaintainAcl());
Rec.getAttr(fALLOWCREATEROLE).setValue(isAllowCreateRole());
Rec.getAttr(fALLOWMAINTAINROLE).setValue(isAllowMaintainRole());
Rec.getAttr(fALLOWCREATEOBJECT).setValue(isAllowCreateObject());
Rec.getAttr(fALLOWMAINTAINOBJECT).setValue(isAllowMaintainObject());
Rec.getAttr(fALLOWCREATEREPOS).setValue(isAllowCreateRepos());
Rec.getAttr(fALLOWMAINTAINREPOS).setValue(isAllowMaintainRepos());
Rec.getAttr(fALLOWCREATEFOLD).setValue(isAllowCreateFolder());
Rec.getAttr(fALLOWMAINTAINFOLD).setValue(isAllowMaintainFolder());
Rec.getAttr(fALLOWCREATEDOC).setValue(isAllowCreateDoc());
Rec.getAttr(fALLOWMAINTAINDOC).setValue(isAllowMaintainDoc());
Rec.getAttr(fALLOWCREATEMIME).setValue(isAllowCreateMime());
Rec.getAttr(fALLOWMAINTAINMIME).setValue(isAllowMaintainMime());
Rec.getAttr(fALLOWCREATEAUTH).setValue(isAllowCreateAuth());
Rec.getAttr(fALLOWMAINTAINAUTH).setValue(isAllowMaintainAuth());
Rec.getAttr(fALLOWCREATECUSTOM).setValue(isAllowCreateCustom());
Rec.getAttr(fALLOWMAINTAINCUSTOM).setValue(isAllowMaintainCustom());
getCommonValues(Rec);
return(Rec);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
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
* @return
*/
public String getTabName()
{
return (getTableName());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static public String getTableName()
{
return ("PD_ROLES");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
Record getRecordStruct() throws PDException
{
if (RolesStruct==null)
    RolesStruct=CreateRecordStruct();
return(RolesStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (RolesStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, "Name", "Name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, "Description", "Description", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEUSER,fALLOWCREATEUSER, "When_true_the_user_can_create_Users", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINUSER,fALLOWMAINTAINUSER, "When_true_the_user_can_maintain_Users", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEGROUP,fALLOWCREATEGROUP, "When_true_the_user_can_create_Groups", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINGROUP,fALLOWMAINTAINGROUP, "When_true_the_user_can_maintain_Groups", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEACL,fALLOWCREATEACL, "When_true_the_user_can_create_ACL", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINACL,fALLOWMAINTAINACL, "When_true_the_user_can_maintain_ACL", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEROLE,fALLOWCREATEROLE, "When_true_the_user_can_create_Roles", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINROLE,fALLOWMAINTAINROLE, "When_true_the_user_can_maintain_Roles", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEOBJECT,fALLOWCREATEOBJECT, "When_true_the_user_can_create_Objects_Definitions", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINOBJECT,fALLOWMAINTAINOBJECT, "When_true_the_user_can_maintain_Objects_Definitions", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEREPOS,fALLOWCREATEREPOS, "When_true_the_user_can_create_Repositories", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINREPOS,fALLOWMAINTAINREPOS, "When_true_the_user_can_maintain_Repositories", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEFOLD,fALLOWCREATEFOLD, "When_true_the_user_can_create_Folders", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINFOLD,fALLOWMAINTAINFOLD, "When_true_the_user_can_maintain_Folders", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEDOC,fALLOWCREATEDOC, "When_true_the_user_can_create_Documents", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINDOC,fALLOWMAINTAINDOC, "When_true_the_user_can_maintain_Documents", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEMIME,fALLOWCREATEMIME, "When_true_the_user_can_create_Mime_Types", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINMIME, fALLOWMAINTAINMIME,"When_true_the_user_can_maintain_Mime_Types", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATECUSTOM,fALLOWCREATECUSTOM, "When_true_the_user_can_create_Configurations", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINCUSTOM, fALLOWMAINTAINCUSTOM,"When_true_the_user_can_maintain_Configurations", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWCREATEAUTH,fALLOWCREATEAUTH, "When_true_the_user_can_create_Authenticators", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fALLOWMAINTAINAUTH, fALLOWMAINTAINAUTH,"When_true_the_user_can_maintain_Authenticators", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(RolesStruct);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 */
protected void AsignKey(String Ident) throws PDExceptionFunc
{
setName(Ident);
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
*/
public void setName(String Name) throws PDExceptionFunc
{
this.Name = CheckName(Name);
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

/**
* @return the AllowCreateUser
*/
public boolean isAllowCreateUser()
{
return AllowCreateUser;
}

/**
* @param AllowCreateUser the AllowCreateUser to set
*/
public void setAllowCreateUser(boolean AllowCreateUser)
{
this.AllowCreateUser = AllowCreateUser;
}

/**
* @return the AllowMaintainUser
*/
public boolean isAllowMaintainUser()
{
return AllowMaintainUser;
}

/**
* @param AllowMaintainUser the AllowMaintainUser to set
*/
public void setAllowMaintainUser(boolean AllowMaintainUser)
{
this.AllowMaintainUser = AllowMaintainUser;
}

/**
* @return the AllowCreateAcl
*/
public boolean isAllowCreateAcl()
{
return AllowCreateAcl;
}

/**
* @param AllowCreateAcl the AllowCreateAcl to set
*/
public void setAllowCreateAcl(boolean AllowCreateAcl)
{
this.AllowCreateAcl = AllowCreateAcl;
}

/**
* @return the AllowMaintainAcl
*/
public boolean isAllowMaintainAcl()
{
return AllowMaintainAcl;
}

/**
* @param AllowMaintainAcl the AllowMaintainAcl to set
*/
public void setAllowMaintainAcl(boolean AllowMaintainAcl)
{
this.AllowMaintainAcl = AllowMaintainAcl;
}

/**
* @return the AllowCreateRole
*/
public boolean isAllowCreateRole()
{
return AllowCreateRole;
}

/**
* @param AllowCreateRole the AllowCreateRole to set
*/
public void setAllowCreateRole(boolean AllowCreateRole)
{
this.AllowCreateRole = AllowCreateRole;
}

/**
* @return the AllowMaintainRole
*/
public boolean isAllowMaintainRole()
{
return AllowMaintainRole;
}

/**
* @param AllowMaintainRole the AllowMaintainRole to set
*/
public void setAllowMaintainRole(boolean AllowMaintainRole)
{
this.AllowMaintainRole = AllowMaintainRole;
}

/**
* @return the AllowCreateObject
*/
public boolean isAllowCreateObject()
{
return AllowCreateObject;
}

/**
* @param AllowCreateObject the AllowCreateObject to set
*/
public void setAllowCreateObject(boolean AllowCreateObject)
{
this.AllowCreateObject = AllowCreateObject;
}

/**
* @return the AllowMaintainObject
*/
public boolean isAllowMaintainObject()
{
return AllowMaintainObject;
}

/**
* @param AllowMaintainObject the AllowMaintainObject to set
*/
public void setAllowMaintainObject(boolean AllowMaintainObject)
{
this.AllowMaintainObject = AllowMaintainObject;
}

/**
* @return the AllowCreateRepos
*/
public boolean isAllowCreateRepos()
{
return AllowCreateRepos;
}

/**
* @param AllowCreateRepos the AllowCreateRepos to set
*/
public void setAllowCreateRepos(boolean AllowCreateRepos)
{
this.AllowCreateRepos = AllowCreateRepos;
}

/**
* @return the AllowMaintainRepos
*/
public boolean isAllowMaintainRepos()
{
return AllowMaintainRepos;
}

/**
* @param AllowMaintainRepos the AllowMaintainRepos to set
*/
public void setAllowMaintainRepos(boolean AllowMaintainRepos)
{
this.AllowMaintainRepos = AllowMaintainRepos;
}

/**
* @return the AllowCreateFolder
*/
public boolean isAllowCreateFolder()
{
return AllowCreateFolder;
}

/**
* @param AllowCreateFolder the AllowCreateFolder to set
*/
public void setAllowCreateFolder(boolean AllowCreateFolder)
{
this.AllowCreateFolder = AllowCreateFolder;
}

/**
* @return the AllowMaintainFolder
*/
public boolean isAllowMaintainFolder()
{
return AllowMaintainFolder;
}

/**
* @param AllowMaintainFolder the AllowMaintainFolder to set
*/
public void setAllowMaintainFolder(boolean AllowMaintainFolder)
{
this.AllowMaintainFolder = AllowMaintainFolder;
}

/**
* @return the AllowCreateDoc
*/
public boolean isAllowCreateDoc()
{
return AllowCreateDoc;
}

/**
* @param AllowCreateDoc the AllowCreateDoc to set
*/
public void setAllowCreateDoc(boolean AllowCreateDoc)
{
this.AllowCreateDoc = AllowCreateDoc;
}

/**
* @return the AllowMaintainDoc
*/
public boolean isAllowMaintainDoc()
{
return AllowMaintainDoc;
}

/**
* @param AllowMaintainDoc the AllowMaintainDoc to set
*/
public void setAllowMaintainDoc(boolean AllowMaintainDoc)
{
this.AllowMaintainDoc = AllowMaintainDoc;
}
/**
* @return the AllowCreateMime
*/
public boolean isAllowCreateMime()
{
return AllowCreateMime;
}

/**
* @param AllowCreateMime the AllowCreateMime to set
*/
public void setAllowCreateMime(boolean AllowCreateMime)
{
this.AllowCreateMime = AllowCreateMime;
}

/**
* @return the AllowMaintainMime
*/
public boolean isAllowMaintainMime()
{
return AllowMaintainMime;
}

/**
* @param AllowMaintainMime the AllowMaintainMime to set
*/
public void setAllowMaintainMime(boolean AllowMaintainMime)
{
this.AllowMaintainMime = AllowMaintainMime;
}

    /**
     * @return the AllowCreateGroup
     */
    public boolean isAllowCreateGroup()
    {
        return AllowCreateGroup;
    }

    /**
     * @param AllowCreateGroup the AllowCreateGroup to set
     */
    public void setAllowCreateGroup(boolean AllowCreateGroup)
    {
        this.AllowCreateGroup = AllowCreateGroup;
    }

    /**
     * @return the AllowMaintainGroup
     */
    public boolean isAllowMaintainGroup()
    {
        return AllowMaintainGroup;
    }

    /**
     * @param AllowMaintainGroup the AllowMaintainGroup to set
     */
    public void setAllowMaintainGroup(boolean AllowMaintainGroup)
    {
        this.AllowMaintainGroup = AllowMaintainGroup;
    }
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))    
    if (!getDrv().getUser().getRol().isAllowCreateRole() )
       PDExceptionFunc.GenPDException("Roles_creation_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRole() )
   PDExceptionFunc.GenPDException("Roles_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRole() )
   PDExceptionFunc.GenPDException("Roles_modification_not_allowed_to_user",getName());
}
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
 * @return the AllowCreateAuth
 */
public boolean isAllowCreateAuth()
{
return AllowCreateAuth;
}
//-------------------------------------------------------------------------
/**
 * @param AllowCreateAuth the AllowCreateAuth to set
 */
public void setAllowCreateAuth(boolean AllowCreateAuth)
{
this.AllowCreateAuth = AllowCreateAuth;
}
//-------------------------------------------------------------------------
/**
 * @return the AllowMaintainAuth
 */
public boolean isAllowMaintainAuth()
{
return AllowMaintainAuth;
}
//-------------------------------------------------------------------------
/**
 * @param AllowMaintainAuth the AllowMaintainAuth to set
 */
public void setAllowMaintainAuth(boolean AllowMaintainAuth)
{
this.AllowMaintainAuth = AllowMaintainAuth;
}
//-------------------------------------------------------------------------
/**
 * @return the AllowCreateCustom
 */
public boolean isAllowCreateCustom()
{
return AllowCreateCustom;
}
//-------------------------------------------------------------------------
/**
 * @param AllowCreateCustom the AllowCreateCustom to set
 */
public void setAllowCreateCustom(boolean AllowCreateCustom)
{
this.AllowCreateCustom = AllowCreateCustom;
}
//-------------------------------------------------------------------------
/**
 * @return the AllowMaintainCustom
 */
public boolean isAllowMaintainCustom()
{
return AllowMaintainCustom;
}
//-------------------------------------------------------------------------
/**
 * @param AllowMaintainCustom the AllowMaintainCustom to set
 */
public void setAllowMaintainCustom(boolean AllowMaintainCustom)
{
this.AllowMaintainCustom = AllowMaintainCustom;
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (RolesObjectsCache==null)
    RolesObjectsCache=new ObjectsCache("Roles");
return(RolesObjectsCache);    
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
