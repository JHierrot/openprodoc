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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jhierrot
 */
public class PDACL extends ObjPD
{

/**
 *
 */
static private Record AclStruct = null;
/**
 *
 */
static private Record AclUsersStruct = null;
/**
 *
 */
static private Record AclGroupsStruct = null;
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
public static final String fACLNAME="AclName";
/**
 *
 */
public static final String fUSERNAME="UserName";
/**
 *
 */
public static final String fGROUPNAME="GroupName";
/**
 *
 */
public static final String fPERMISION="Permision";
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
public static final int pNOTHING =0;
/**
 *
 */
public static final int pREAD    =1;
/**
 *
 */
public static final int pCATALOG =2;
/**
 *
 */
public static final int pVERSION =3;
/**
 *
 */
public static final int pUPDATE  =4;
/**
 *
 */
public static final int pDELETE  =5;

static private ObjectsCache AclObjectsCache = null;

/**
 *
 * @param Drv
 * @throws PDException
 */
public PDACL(DriverGeneric Drv) throws PDException
{
super(Drv);    
}
//-------------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
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
 * @param Rec
 * @throws PDException
 */
public void assignValues(Record Rec) throws PDException
{
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
assignCommonValues(Rec);
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
return ("PD_ACL");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
public String getTabNameAclUsers()
{
return ("PD_ACL_USERS");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
public String getTabNameAclGroups()
{
return ("PD_ACL_GROUPS");
}
//-------------------------------------------------------------------------
/**
*
* @throws PDException
*/
protected void unInstallMulti() throws PDException
{
getDrv().DropTable(getTabNameAclUsers());
getDrv().DropTable(getTabNameAclGroups());
}
//-------------------------------------------------------------------------
/**
*
* @throws PDException
*/
protected void InstallMulti() throws PDException
{
getDrv().CreateTable(getTabNameAclUsers(), getRecordAclUsersStruct());
getDrv().AddIntegrity(getTabNameAclUsers(), fUSERNAME, PDUser.getTableName(), PDUser.fNAME);
getDrv().AddIntegrity(getTabNameAclUsers(), fACLNAME, getTabName(), fNAME);
getDrv().CreateTable(getTabNameAclGroups(), getRecordAclGroupsStruct());
getDrv().AddIntegrity(getTabNameAclGroups(), fGROUPNAME, PDGroups.getTableName(), fNAME);
getDrv().AddIntegrity(getTabNameAclGroups(), fACLNAME, getTabName(), fNAME);
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
Record getRecordStruct() throws PDException
{
if (AclStruct == null)
    AclStruct = CreateRecordStructACL();
return (AclStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStructACL() throws PDException
{
if (AclStruct == null)
    {
    Record R = new Record();
    R.addAttr(new Attribute(fNAME, fNAME, "Unique_name_of_the_pemissions_set", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr(new Attribute(fDESCRIPTION, fDESCRIPTION, "Description_of_the_permissions_set", Attribute.tSTRING, false, null, 128, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return (AclStruct);
}
//-------------------------------------------------------------------------
/**
*
 * @return
 * @throws PDException
*/
public Record getRecordAclUsersStruct() throws PDException
{
if (AclUsersStruct == null)
    AclUsersStruct = CreateRecordAclUsersStruct();
return (AclUsersStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordAclUsersStruct() throws PDException
{
if (AclUsersStruct == null)
    {
    Record R = new Record();
    R.addAttr(new Attribute(fACLNAME, fACLNAME,"", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr(new Attribute(fUSERNAME, "User_Name", "User_Name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr(new Attribute(fPERMISION, "Permission", "Permission_assigned_to_user", Attribute.tINTEGER, true, null, 32, false, false, false));
    return(R);
    }
else
    return (AclUsersStruct);
}
//-------------------------------------------------------------------------
/**
*
 * @return
 * @throws PDException
*/
public Record getRecordAclGroupsStruct() throws PDException
{
if (AclGroupsStruct == null)
    AclGroupsStruct = CreateRecordAclGroupsStruct();
return (AclGroupsStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordAclGroupsStruct() throws PDException
{
if (AclGroupsStruct == null)
    {
    Record R = new Record();
    R.addAttr(new Attribute(fACLNAME, fACLNAME, "", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr(new Attribute(fGROUPNAME, "Group_name", "Group_name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr(new Attribute(fPERMISION, "Permission", "Permission_assigned_to_group", Attribute.tINTEGER, true, null, 32, false, false, false));
    return(R);
    }
else
    return (AclGroupsStruct);
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
//-------------------------------------------------------------------------EditACL.
/**
 * Add an User to an ACL
 * @param UserName Name of the user to be added
 * @param Permision Permission to be assigned
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void addUser(String UserName, int Permision)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.addUser>:"+getName()+"/"+UserName+"="+Permision);
boolean InTransLocal;
if (Permision<pNOTHING || Permision>pDELETE)
    PDException.GenPDException("Incorrect_permission", ""+Permision);
InTransLocal=!getDrv().isInTransaction();
Record AclUser=getRecordAclUsersStruct();
AclUser.getAttr(fACLNAME).setValue(getName());
AclUser.getAttr(fUSERNAME).setValue(UserName);
AclUser.getAttr(fPERMISION).setValue(new Integer(Permision));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().InsertRecord(getTabNameAclUsers(), AclUser);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDACL.addUser<:"+getName()+"/"+UserName+"="+Permision);
}
//-------------------------------------------------------------------------
/**
 * Add a Group to an ACL
 * @param GroupName Name of the user to be added
 * @param Permision Permission to be assigned
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void addGroup(String GroupName, int Permision)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.addGroup>:"+getName()+"/"+GroupName+"="+Permision);
boolean InTransLocal;
if (Permision<pNOTHING || Permision>pDELETE)
    PDException.GenPDException("Incorrect_permission", ""+Permision);
InTransLocal=!getDrv().isInTransaction();
Record AclGroup=getRecordAclGroupsStruct();
AclGroup.getAttr(fACLNAME).setValue(getName());
AclGroup.getAttr(fGROUPNAME).setValue(GroupName);
AclGroup.getAttr(fPERMISION).setValue(new Integer(Permision));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().InsertRecord(getTabNameAclGroups(), AclGroup);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDACL.addGroup<:"+getName()+"/"+GroupName+"="+Permision);
}
//-------------------------------------------------------------------------
/**
 * Deletes a User from an ACL
 * @param UserName Name of the user to be deleted
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void delUser( String UserName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delUser>:"+getName()+"/"+UserName);
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fACLNAME, Condition.cEQUAL, getName()));
ListCond.addCondition(new Condition(fUSERNAME, Condition.cEQUAL, UserName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameAclUsers(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delUser<:"+getName()+"/"+UserName);
}
//-------------------------------------------------------------------------
/**
 * Deletes a Group from an ACL
 * @param GroupName Name of the Group to be deleted
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void delGroup(String GroupName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delGroup>:"+getName()+"/"+GroupName);
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fACLNAME, Condition.cEQUAL, getName()));
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, GroupName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameAclGroups(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delGroup<:"+getName()+"/"+GroupName);
}
//-------------------------------------------------------------------------
/**
 * Deletes ALL references to a User in ANY ACL
 * @param UserName Name of the user to be deleted
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void delUserReferences(String UserName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delUserReferences>:"+UserName);
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fUSERNAME, Condition.cEQUAL, UserName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameAclUsers(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delUserReferences<:"+UserName);
}
//-------------------------------------------------------------------------
/**
 * Deletes ALL references to a Group in ANY ACL
 * @param GroupName Name of the Group to be deleted
 * @throws PDException if occurs any problem updatig the DDBB
 */
public void delGroupReferences( String GroupName)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delGroupReferences>:"+GroupName);
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fGROUPNAME, Condition.cEQUAL, GroupName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameAclGroups(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDACL.delGroupReferences<:"+GroupName);
}
//-------------------------------------------------------------------------
/**
 *
 */
protected void DeleteMulti() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.DeleteMulti>:"+getName());
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fACLNAME, Condition.cEQUAL, getName()));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameAclUsers(), ListCond);
getDrv().DeleteRecord(getTabNameAclGroups(), ListCond);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDACL.DeleteMulti<:"+getName());
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @throws PDException  
 */
protected void AsignKey(String Ident) throws PDException
{
setName(Ident);
}
//-------------------------------------------------------------------------
/**
 * Init retrieving of what groups the UserName is member
 * @param User
 * @return
 * @throws PDException
 */
public HashMap FullUserMemberShip(PDUser User) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.FullUserMemberShip>:"+User.getName());
HashMap Result=new HashMap();
Condition CondGroups=new Condition(fGROUPNAME, User.getGroupList());
Conditions Conds=new Conditions();
Conds.addCondition(CondGroups);
Query Q=new Query(getTabNameAclGroups(), getRecordAclGroupsStruct(), Conds);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    Integer Permision=(Integer)Res.getAttr(fPERMISION).getValue();
    String Acl=(String) Res.getAttr(fACLNAME).getValue();
    Integer ValAct=(Integer)Result.get(Acl);
    if (ValAct==null)
        Result.put(Acl, Permision);
    else if (ValAct.compareTo(Permision)<0)
        {
        Result.remove(Acl);
        Result.put(Acl, Permision);
        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
Condition CondUser=new Condition(fUSERNAME,Condition.cEQUAL ,User.getName());
Conds=new Conditions();
Conds.addCondition(CondUser);
Q=new Query(getTabNameAclUsers(), getRecordAclUsersStruct(), Conds);
CursorId=getDrv().OpenCursor(Q);
Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    Integer Permision=(Integer)Res.getAttr(fPERMISION).getValue();
    String Acl=(String) Res.getAttr(fACLNAME).getValue();
    Integer ValAct=(Integer)Result.get(Acl);
    if (ValAct==null)
        Result.put(Acl, Permision);
    else if (ValAct.compareTo(Permision)<0)
        {
        Result.remove(Acl);
        Result.put(Acl, Permision);
        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDACL.FullUserMemberShip<:"+User.getName());
return(Result);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))
    if (!getDrv().getUser().getRol().isAllowCreateAcl() )
        PDExceptionFunc.GenPDException("ACL_creation_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainAcl() )
   PDExceptionFunc.GenPDException("ACL_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainAcl())
   PDExceptionFunc.GenPDException("ACL_modification_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
/**
 *
 * @param ACLName
 * @return
 * @throws PDException
 */
public Cursor ListGroups(String ACLName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.ListGroups:"+ACLName);
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fACLNAME, Condition.cEQUAL, ACLName));
Query Groups=new Query(getTabNameAclGroups(), getRecordAclGroupsStruct(), ListCond);
return(getDrv().OpenCursor(Groups));
}
//-------------------------------------------------------------------------
/**
 *
 * @param ACLName
 * @return
 * @throws PDException
 */
public Cursor ListUsers(String ACLName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDACL.ListUsers:"+ACLName);
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fACLNAME, Condition.cEQUAL, ACLName));
Query Users=new Query(getTabNameAclUsers(), getRecordAclUsersStruct(), ListCond);
return(getDrv().OpenCursor(Users));
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
if (AclObjectsCache==null)
    AclObjectsCache=new ObjectsCache("ACL");
return(AclObjectsCache);    
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
 * @throws PDException
 */
@Override
protected String toXML2() throws PDException
{
StringBuilder XML=new StringBuilder("</"+XML_ListAttr+">");  
XML.append("<"+XML_GroupMembers+">");
Cursor LstGrps=ListGroups(getName());
Record GrpAcl=getDrv().NextRec(LstGrps);
while (GrpAcl!=null)
    {
    XML.append("<"+XML_Group+" Name=\"");
    XML.append(GrpAcl.getAttr(PDACL.fGROUPNAME).getValue());    
    XML.append("\">");
    XML.append(GrpAcl.getAttr(PDACL.fPERMISION).getValue());    
    XML.append("</"+XML_Group+">");
    GrpAcl=getDrv().NextRec(LstGrps);
    }
XML.append("</"+XML_GroupMembers+">");
XML.append("<"+XML_UserMembers+">");  
Cursor LstUsrs=ListUsers(getName());
Record UsrAcl=getDrv().NextRec(LstUsrs);
while (UsrAcl!=null)
    {
    XML.append("<"+XML_User+" Name=\"");
    XML.append(UsrAcl.getAttr(PDACL.fUSERNAME).getValue());    
    XML.append("\">");
    XML.append(UsrAcl.getAttr(PDACL.fPERMISION).getValue());    
    XML.append("</"+XML_User+">");
    UsrAcl=getDrv().NextRec(LstUsrs);
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
                NamedNodeMap XMLattributes = Group.getAttributes();
                Node XMLGrpName = XMLattributes.getNamedItem("Name");
                String GroupName=XMLGrpName.getNodeValue();
                String GroupPerm=Group.getTextContent();
                addGroup(GroupName, Integer.parseInt(GroupPerm));
                }
            }
        }
    else if (ListElements.getNodeName().equalsIgnoreCase(XML_UserMembers)) 
        {
        NodeList ListUsers = ListElements.getChildNodes();
        for (int NumUser = 0; NumUser < ListUsers.getLength(); NumUser++)
            {
            Node User = ListUsers.item(NumUser);
            if (User.getNodeName().equalsIgnoreCase(XML_User))
                {
                NamedNodeMap XMLattributes = User.getAttributes();
                Node XMLUsrName = XMLattributes.getNamedItem("Name");
                String UserName=XMLUsrName.getNodeValue();
                String UserPerm=User.getTextContent();
                addUser(UserName, Integer.parseInt(UserPerm));
                }
            }
        }
    }
}    
//-------------------------------------------------------------------------
}
