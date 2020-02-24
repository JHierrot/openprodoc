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

import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Manages the storage of Groups
 * @author jhierrot
 */
public class PDGroups  extends ObjPD
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
public static final String fACL="Acl";
/**
 *
 */
public static final String fGROUPNAME="GroupName";
/**
 *
 */
public static final String fMEMBERNAME="MemberName";
/**
 *
 */
public static final String fUSERNAME="UserName";

/**
 *
 */
static private Record GroupStruct=null;
/**
 *
 */
static private Record GroupUsersStruct=null;
/**
 *
 */
static private Record GroupGroupsStruct=null;
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
private String Acl;
/**
 *
 */
private Cursor CurUsers=null;
/**
 *
 */
private Cursor CurMembers=null;
/**
 *
 */
private Cursor CurGroupMemberShip=null;
/**
 *
 */
private Cursor CurUserMemberShip=null;

static private ObjectsCache GroupsObjectsCache = null;

/**
 * default Constructor
 * @param Drv OPD Session
 * @throws PDException in any error
 */
public PDGroups(DriverGeneric Drv)  throws PDException
{
super(Drv);
}
//-------------------------------------------------------------------------
/**
 * Returns a record with the current values
 * @return a record with the current values
 * @throws PDException in any error
 */
@Override
synchronized public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fACL).setValue(getAcl());
getCommonValues(Rec);
return(Rec);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException in any error
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
Condition CondAcl=new Condition(PDGroups.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ListCond.addCondition(CondAcl);
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Assign new values to the Group object
 * @param Rec Record of Group type with new values
 * @throws PDException in any error
 */
public void assignValues(Record Rec) throws PDException
{
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
setAcl((String) Rec.getAttr(fACL).getValue());
assignCommonValues(Rec);
}
//-------------------------------------------------------------------------
/**
 * Returns the name of Groups table in DDBB
 * @return the name of Groups table in DDBB
 */
public String getTabName()
{
return(getTableName());
}
//-------------------------------------------------------------------------
/**
 * Returns the name of Groups table in DDBB
 * @return the name of Groups table in DDBB
 */
static public String getTableName()
{
return("PD_GROUPS");
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getTabNameGroupUsers()
{
return("PD_GROUPUSER");
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getTabNameGroupGroups()
{
return("PD_GROUPGROUP");
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException in any error
 */
protected  void InstallMulti()  throws PDException
{
getDrv().AddIntegrity(getTabName(), fACL, PDACL.getTableName(), PDACL.fNAME);
getDrv().CreateTable(getTabNameGroupUsers(),  getRecordGroupUsersStruct());
getDrv().AddIntegrity(getTabNameGroupUsers(), fGROUPNAME, getTabName(), fNAME);
getDrv().AddIntegrity(getTabNameGroupUsers(), fUSERNAME, PDUser.getTableName(), PDUser.fNAME);
getDrv().CreateTable(getTabNameGroupGroups(), getRecordGroupGroupsStruct());
getDrv().AddIntegrity(getTabNameGroupGroups(), fGROUPNAME, getTabName(), fNAME);
getDrv().AddIntegrity(getTabNameGroupGroups(), fMEMBERNAME, getTabName(), fNAME);
}
//-------------------------------------------------------------------------
/**
 *
 */
Record getRecordStruct() throws PDException
{
if (GroupStruct==null)
    GroupStruct=CreateRecordStruct();
return(GroupStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 *
 */
static synchronized private Record CreateRecordStruct() throws PDException
{
if (GroupStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, "Name", "Name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, "Description", "Description", Attribute.tSTRING, false, null, 128, false, false, true));
    R.addAttr( new Attribute(fACL, "ACL", "ACL", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(GroupStruct);
}
//-------------------------------------------------------------------------
/**
 * returns the structure of relation GroupsUsers
 * @return a record with the structure of relation GroupsUsers
 * @throws PDException in any error
 */
public Record getRecordGroupUsersStruct() throws PDException
{
if (GroupUsersStruct==null)
    GroupUsersStruct=CreateRecordGroupUsersStruct();
return(GroupUsersStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 *
 */
static synchronized private Record CreateRecordGroupUsersStruct() throws PDException
{
if (GroupUsersStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fGROUPNAME, "Group", "Group", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fUSERNAME, "User", "User", Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(GroupUsersStruct);
}
//-------------------------------------------------------------------------
/**
 * Returns the name of GroupsGroups relation table in DDBB
 * @return the name of GroupsGroups relation table in DDBB
 * @throws PDException in any error
 */
public Record getRecordGroupGroupsStruct() throws PDException
{
if (GroupGroupsStruct==null)
    GroupGroupsStruct=CreateRecordGroupGroupsStruct();
return(GroupGroupsStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 *
 */
static synchronized private Record CreateRecordGroupGroupsStruct() throws PDException
{
if (GroupGroupsStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fGROUPNAME, "Group", "Group", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fMEMBERNAME, "SubGroup", "SubGroup", Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(GroupGroupsStruct);
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException in any error
 */
protected void unInstallMulti() throws PDException
{
getDrv().DropTable(getTabNameGroupUsers());
getDrv().DropTable(getTabNameGroupGroups());
}
//-------------------------------------------------------------------------

/**
* @return the GroupName
*/
public String getName()
{
return Name;
}
//-------------------------------------------------------------------------
/**
 * sets the name of the Group
 * @param Name new name of Group
 * @throws PDExceptionFunc  in any error
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
//-------------------------------------------------------------------------
/**
* @return the Acl
*/
public String getAcl()
{
return Acl;
}
//-------------------------------------------------------------------------
/**
* @param Acl the Acl to set
*/
public void setAcl(String Acl)
{
this.Acl = Acl;
}
//-------------------------------------------------------------------------
/**
 * Add (and saves) a new user to the Group
 * @param UserName Name of User to be added
 * @throws PDException in any error
 */
public void addUser( String UserName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.addUser>:"+getName()+"/"+UserName);
VerifyAllowedUpd();
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Record GroupUser=getRecordGroupUsersStruct();
GroupUser.getAttr(fGROUPNAME).setValue(getName());
GroupUser.getAttr(fUSERNAME).setValue(UserName);
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().InsertRecord(getTabNameGroupUsers(), GroupUser);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
getDrv().getUser().RefreshAuth();
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.addUser<:"+getName()+"/"+UserName);
}
//-------------------------------------------------------------------------
/**
 * Delete a User from his parent Group
 * @param UserName Name of User to be deleted
 * @throws PDException in any error
 */
public void delUser(String UserName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delUser>:"+getName()+"/"+UserName);
VerifyAllowedUpd();
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, getName()));
ListCond.addCondition(new Condition(fUSERNAME, Condition.cEQUAL, UserName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameGroupUsers(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delUser<:"+getName()+"/"+UserName);
}
//-------------------------------------------------------------------------
/**
 * Add (and Saves)N a Group as member of another Group
 * @param GroupName name of Group to be added
 * @throws PDException in any error
 */
public void addGroup(String GroupName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.addGroup>:"+getName()+"/"+GroupName);
if (getName().equalsIgnoreCase(GroupName))
    PDExceptionFunc.GenPDException("Error_assigning_group_to_same_group_Circular_reference", GroupName);
VerifyAllowedUpd();
boolean InTransLocal=!getDrv().isInTransaction();
Record GroupGroup=getRecordGroupGroupsStruct();
GroupGroup.getAttr(fGROUPNAME).setValue(getName());
GroupGroup.getAttr(fMEMBERNAME).setValue(GroupName);
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().InsertRecord(getTabNameGroupGroups(), GroupGroup);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
getDrv().getUser().RefreshAuth();
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.addGroup<:"+getName()+"/"+GroupName);
}
//-------------------------------------------------------------------------
/**
 * Delete a Group from his parent Group
 * @param GroupName name of Group to be deleted
 * @throws PDException in any error
 */
public void delGroup(String GroupName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delGroup>:"+getName()+"/"+GroupName);
VerifyAllowedUpd();
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, getName()));
ListCond.addCondition(new Condition(fMEMBERNAME, Condition.cEQUAL, GroupName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameGroupGroups(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delGroup<:"+getName()+"/"+GroupName);
}
//-------------------------------------------------------------------------
/**
 *
     * @throws prodoc.PDException
 */
@Override
protected void DeleteMulti() throws PDException
{
PDACL AclTmp=new PDACL(getDrv());
AclTmp.delGroupReferences(getName());
this.delGroupReferences(getName());
}
//-------------------------------------------------------------------------
/**
 * Deletes ALL references to a User in ANY Group
 * @param UserName Name of the user to be deleted
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void delUserReferences( String UserName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delUserReferences>:"+UserName);
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fUSERNAME, Condition.cEQUAL, UserName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameGroupUsers(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delUserReferences>:"+UserName);
}
//-------------------------------------------------------------------------
/**
 * Deletes ALL references to a Group in ANY Group
 * @param GroupName Name of the Group to be deleted
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void delGroupReferences(String GroupName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delGroupReferences>:"+GroupName);
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.setOperatorAnd(false);
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, GroupName));
ListCond.addCondition(new Condition(fMEMBERNAME, Condition.cEQUAL, GroupName));
Conditions ListCondUser=new Conditions();
ListCondUser.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, GroupName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(this.getTabNameGroupGroups(), ListCond);
getDrv().DeleteRecord(this.getTabNameGroupUsers(), ListCondUser);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.delGroupReferences>:"+GroupName);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @throws PDExceptionFunc  in any error 
 */
protected void AsignKey(String Ident) throws PDExceptionFunc
{
setName(Ident);
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of users members of a group
 * @param GrpName name of Group containing the Users
 * @return a New Cursor
 * @throws PDException in any error
 */
public Cursor ListUsers(String GrpName) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, GrpName));
Query q=new Query(getTabNameGroupUsers(), getRecordGroupUsersStruct(), ListCond);
return(getDrv().OpenCursor(q));
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of users members of a group
 * @throws PDException in any error
 */
public void InitListUsers() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, getName()));
Query q=new Query(getTabNameGroupUsers(), getRecordGroupUsersStruct(), ListCond);
CurUsers=getDrv().OpenCursor(q);
}
//-------------------------------------------------------------------------
/**
 * Used for obtaining the list of user of a Group
 * @return the name of the next user of the cursor
 * @throws PDException in any error
 */
public String NextUsers() throws PDException
{
Record r=getDrv().NextRec(CurUsers);
if (r==null)
    {
    getDrv().CloseCursor(CurUsers);
    return(null);
    }
Attribute Attr=r.getAttr(fUSERNAME);
return((String)Attr.getValue());
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of users members of a group
 * @param GrpName Name of Group copnmtaining the Groups
 * @return a created cursor
 * @throws PDException in any error
 */
public Cursor ListGroups(String GrpName) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, GrpName));
Query q=new Query(getTabNameGroupGroups(), getRecordGroupGroupsStruct(), ListCond);
return(getDrv().OpenCursor(q));
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of users members of a group
 * @throws PDException in any error
 */
public void InitListMembers() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, getName()));
Query q=new Query(getTabNameGroupGroups(), getRecordGroupGroupsStruct(), ListCond);
CurMembers=getDrv().OpenCursor(q);
}
//-------------------------------------------------------------------------
/**
 * Used for traveling by the members of a Group
 * @return the name of the next member of a Group
 * @throws PDException in any error
 */
public String NextMember() throws PDException
{
Record r=getDrv().NextRec(CurMembers);
if (r==null)
    {
    getDrv().CloseCursor(CurMembers);
    return(null);
    }
Attribute Attr=r.getAttr(fMEMBERNAME);
return((String)Attr.getValue());
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of what groups the actual group is member
 * @throws PDException in any error
 */
public void InitGroupMemberShip() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fMEMBERNAME, Condition.cEQUAL, getName()));
Query q=new Query(getTabNameGroupGroups(), getRecordGroupGroupsStruct(), ListCond);
CurGroupMemberShip=getDrv().OpenCursor(q);
}
//-------------------------------------------------------------------------
/**
 * Used for traveling by the list of memberships
 * @return the name of the next group "container"
 * @throws PDException in any error
 */
public String NextGroupMemberShip() throws PDException
{
Record r=getDrv().NextRec(CurGroupMemberShip);
if (r==null)
    {
    getDrv().CloseCursor(CurGroupMemberShip);
    return(null);
    }
Attribute Attr=r.getAttr(fGROUPNAME);
return((String)Attr.getValue());
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of what groups the User is member
 * @param UserName Name of User to check membership
 * @throws PDException in any error
 */
public void InitUserMemberShip(String UserName) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fUSERNAME, Condition.cEQUAL, UserName));
Query q=new Query(getTabNameGroupUsers(), getRecordGroupUsersStruct(), ListCond);
CurUserMemberShip=getDrv().OpenCursor(q);
}
//-------------------------------------------------------------------------
/**
 * Used for traveling by the memberships of a User
 * @return the name of the next group the user is member
 * @throws PDException in any error
 */
public String NextUserMemberShip() throws PDException
{
Record r=getDrv().NextRec(CurUserMemberShip);
if (r==null)
    {
    getDrv().CloseCursor(CurUserMemberShip);
    return(null);
    }
Attribute Attr=r.getAttr(fGROUPNAME);
return((String)Attr.getValue());
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of what groups the UserName is member
 * @param UserName Name of user to check
 * @return a HashSet of names of Groups the user is member
 * @throws PDException in any error
 */
public HashSet FullUserMemberShip(String UserName) throws PDException
{
HashSet Result=new HashSet();
HashSet Partial=DirectUserMemberShip(UserName);
Result.addAll(Partial);
for (Iterator it = Partial.iterator(); it.hasNext();)
    {
    String GrpName = (String)it.next();
    PDGroups G=new PDGroups(getDrv());
    Result.addAll(G.FullGroupMemberShip(GrpName));
    }
return(Result);
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of what groups the user is member
 * @param UserName Name of User to check
 * @return a HashSet of names of Groups the user is DIRECT Member
 * @throws PDException in any error
 */
public HashSet DirectUserMemberShip(String UserName) throws PDException
{
HashSet Partial=new HashSet();
String Grp;
InitUserMemberShip(UserName);
Grp=NextUserMemberShip();
while (Grp!=null)
    {
    Partial.add(Grp);
    Grp=NextUserMemberShip();
    }
return(Partial);
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of what groups the  group is member
 * @param GroupName name of the Group to check
 * @return A HashSet of ALL the Groups the Group is Member
 * @throws PDException in any error
 */
public HashSet FullGroupMemberShip(String GroupName) throws PDException
{
HashSet Result=new HashSet();
HashSet Partial=new HashSet();
String Grp;
PDGroups G1=new PDGroups(getDrv());
G1.setName(GroupName);
G1.InitGroupMemberShip();
Grp=G1.NextGroupMemberShip();
while (Grp!=null)
    {
    Partial.add(Grp);
    Result.add(Grp);
    Grp=G1.NextGroupMemberShip();
    }
for (Iterator it = Partial.iterator(); it.hasNext();)
    {
    String GrpName = (String)it.next();
    PDGroups G=new PDGroups(getDrv());
    Result.addAll(G.FullGroupMemberShip(GrpName));
    }
return(Result);
}
//--------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))
    if (!getDrv().getUser().getRol().isAllowCreateGroup() )
        PDExceptionFunc.GenPDException("Group_creation_not_allowed_to_user", null);
}
//--------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainGroup() )
   PDExceptionFunc.GenPDException("Group_delete_not_allowed_to_user", null);
PDGroups G=new PDGroups(getDrv());
G.Load(getName());    
if (!getDrv().getUser().getAclList().containsKey(G.getAcl()))
    PDExceptionFunc.GenPDException("Group_delete_not_allowed_to_user",getName());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(G.getAcl());
if (Perm.intValue()<PDACL.pDELETE)
    PDExceptionFunc.GenPDException("Group_delete_not_allowed_to_user",getName());
}
//--------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))
    {
    if (!getDrv().getUser().getRol().isAllowMaintainGroup() )
       PDExceptionFunc.GenPDException("Group_modification_not_allowed_to_user", null);
    PDGroups G=new PDGroups(getDrv());
    G.Load(getName());    
    if (!getDrv().getUser().getAclList().containsKey(G.getAcl()))
        PDExceptionFunc.GenPDException("Group_modification_not_allowed_to_user",getName());
    Integer Perm=(Integer)getDrv().getUser().getAclList().get(G.getAcl());
    if (Perm.intValue()<PDACL.pUPDATE)
        PDExceptionFunc.GenPDException("Group_modification_not_allowed_to_user",getName());
    }
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
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (GroupsObjectsCache==null)
    GroupsObjectsCache=new ObjectsCache("Groups");
return(GroupsObjectsCache);    
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
/**
 * Add aditional information, oriented a "extended" object with childrn nodes
 * @return The aditional XML
 * @throws PDException in any error
 */
@Override
protected String toXML2() throws PDException
{
StringBuilder XML=new StringBuilder("</"+XML_ListAttr+">");  
XML.append("<"+XML_GroupMembers+">");
InitListMembers();
String Member=NextMember();
while (Member!=null)
    {
    XML.append("<"+XML_Group+">");
    XML.append(Member);    
    XML.append("</"+XML_Group+">");
    Member=NextMember();
    }
XML.append("</"+XML_GroupMembers+">");
XML.append("<"+XML_UserMembers+">");  
InitListUsers();
Member=NextUsers();
while (Member!=null)
    {
    XML.append("<"+XML_User+">");
    XML.append(Member);    
    XML.append("</"+XML_User+">");
    Member=NextUsers();
    }
XML.append("</"+XML_UserMembers+">");
return(XML.toString());    
}
//-------------------------------------------------------------------------
/**
 * Process the object definition inserting a new object
 * @param OPDObject XML node containing theobject data
 * @throws PDException if object name/index duplicated or in any error
 */
@Override
public void ProcesXMLNode(Node OPDObject) throws PDException
{
NodeList childNodes = OPDObject.getChildNodes();
for (int NumNodes = 0; NumNodes < childNodes.getLength(); NumNodes++)
    {
    Node ListElements = childNodes.item(NumNodes);
    if (ListElements.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(ListElements, getRecord());
        assignValues(r);
        insert();
        }
    else if (ListElements.getNodeName().equalsIgnoreCase(XML_GroupMembers)) 
        {
        NodeList ListGroups = ListElements.getChildNodes();
        for (int NumGrp = 0; NumGrp < ListGroups.getLength(); NumGrp++)
            {
            Node Group = ListGroups.item(NumGrp);
            if (Group.getNodeName().equalsIgnoreCase(XML_Group))
                {
                String GroupName=Group.getTextContent();
                addGroup(GroupName);
                }
            }
        }
    else if (ListElements.getNodeName().equalsIgnoreCase(XML_UserMembers)) 
        {
        NodeList ListUsers = ListElements.getChildNodes();
        for (int NumUser = 0; NumUser < ListUsers.getLength(); NumUser++)
            {
            Node Group = ListUsers.item(NumUser);
            if (Group.getNodeName().equalsIgnoreCase(XML_User))
                {
                String UserName=Group.getTextContent();
                addUser(UserName);
                }
            }
        }
    }
}    
//-------------------------------------------------------------------------

/**
 *
 * @throws PDException in any error
 */
public void DelAllSubGroups() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.DelAllSubGroups>:"+getName());
VerifyAllowedUpd();
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, getName()));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(this.getTabNameGroupGroups(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
getDrv().getUser().RefreshAuth();
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.DelAllSubGroups<");
}
//-------------------------------------------------------------------------

/**
 *
 * @throws PDException in any error
 */
public void DelAllUsers() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.DelAllSubGroups>:"+getName());
VerifyAllowedUpd();
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCondUser=new Conditions();
ListCondUser.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, getName()));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(this.getTabNameGroupUsers(), ListCondUser);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
getDrv().getUser().RefreshAuth();
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDGroups.DelAllSubGroups<");
}
//-------------------------------------------------------------------------
}