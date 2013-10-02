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

import java.util.HashSet;

/**
 *
 * @author jhierrot
 */
public class PDAuthenticators extends ObjPD
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
public static final String fAUTHTYPE="AuthType";
/**
 *
 */
public static final String fURL="URL";
/**
 *
 */
public static final String fPARAM="Param";
/**
 *
 */
public static final String fUSERNAME="UserName";
/**
 *
 */
public static final String fPASSWORD="Password";

/**
 *
 */
public static final String tBBDD="DDBB";
/**
 *
 */
public static final String tLDAP="LDAP";
/**
 *
 */
public static final String tSO="SO";
/**
 *
 */
public static final String tOPD="OPD";
/**
 *
 */
public static final String tCUSTOM="CUSTOM";
/**
 *
 */
private static HashSet tList=null;
/**
* @return the tList
*/
public static HashSet gettList()
{
if (tList==null)
    {
    tList=new HashSet();
    tList.add(tLDAP);
    tList.add(tBBDD);
    tList.add(tSO);
    tList.add(tOPD);
    tList.add(tCUSTOM);
    }
return tList;
}

/**
 *
 */
static private Record RepositoriesStruct=null;

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
private String AuthType;
/**
 *
 */
private String URL;
/**
 *
 */
private String Param;
/**
 *
 */
private String UserName;
/**
 *
 */
private String Password;

static private ObjectsCache AuthObjectsCache = null;

/**
 *
 * @param Drv
 * @throws PDException
 */
public PDAuthenticators(DriverGeneric Drv) throws PDException
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
if (PDLog.isDebug())
    PDLog.Debug("PDAthenticators.assignValues>:"+Rec);
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
if (!gettList().contains((String) Rec.getAttr(fAUTHTYPE).getValue()))
    setAuthType(tOPD);
else
    setAuthType((String) Rec.getAttr(fAUTHTYPE).getValue());
setURL((String) Rec.getAttr(fURL).getValue());
setParam((String) Rec.getAttr(fPARAM).getValue());
setUser((String) Rec.getAttr(fUSERNAME).getValue());
setPassword((String) Rec.getAttr(fPASSWORD).getValue());
assignCommonValues(Rec);
if (PDLog.isDebug())
    PDLog.Debug("PDAthenticators.assignValues<:"+Rec);
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
Rec.getAttr(fAUTHTYPE).setValue(getAuthType());
Rec.getAttr(fURL).setValue(getURL());
Rec.getAttr(fPARAM).setValue(getParam());
Rec.getAttr(fUSERNAME).setValue(getUser());
Rec.getAttr(fPASSWORD).setValue(getPassword());
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
return ("PD_AUTHENTICATOR");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
Record getRecordStruct() throws PDException
{
if (RepositoriesStruct==null)
    RepositoriesStruct=CreateRecordStruct();
return(RepositoriesStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (RepositoriesStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, fNAME, "Authentication_system_name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, fDESCRIPTION, "Authentication_system_description", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fAUTHTYPE, "Authentication_system_type", "Authentication_system_type", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fURL, "URL", "Authentication_system_reference", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fPARAM, "Additional_parameters", "Additional_parameters_for_the_specific_Authentication_System", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fUSERNAME, "User", "Connection_user_name", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fPASSWORD, "Password", "Connection_Password", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(RepositoriesStruct);
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
//-------------------------------------------------------------------------
/**
* @return the URL
*/
public String getURL()
{
return URL;
}
//-------------------------------------------------------------------------
/**
* @param URL the URL to set
*/
public void setURL(String URL)
{
this.URL = URL;
}
//-------------------------------------------------------------------------
/**
* @return the Param
*/
public String getParam()
{
return Param;
}
//-------------------------------------------------------------------------
/**
 * @param PARAM
*/
public void setParam(String PARAM)
{
this.Param = PARAM;
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
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))     
    if (!getDrv().getUser().getRol().isAllowCreateRepos() )
       PDExceptionFunc.GenPDException("PDAuthenticators_creation_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRepos() )
   PDExceptionFunc.GenPDException("PDAuthenticators_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRepos() )
   PDExceptionFunc.GenPDException("PDAuthenticators_modification_not_allowed_to_user",getName());
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
* @return the AuthType
*/
public String getAuthType()
{
return AuthType;
}
//-------------------------------------------------------------------------
/**
 * @param pAuthType the AuthType to set
 * @throws PDException 
*/
public void setAuthType(String pAuthType)  throws PDException
{
if (!gettList().contains(pAuthType))
    PDException.GenPDException("Incorrect_Authenticator_type", pAuthType);
AuthType = pAuthType;
}
//-------------------------------------------------------------------------
/**
* @return the UserName
*/
public String getUser()
{
return UserName;
}
//-------------------------------------------------------------------------
/**
 * @param User
*/
public void setUser(String User)
{
this.UserName = User;
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
 * @param pPassword
*/
public void setPassword(String pPassword)
{
Password = DriverGeneric.Encode(pPassword);
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (AuthObjectsCache==null)
    AuthObjectsCache=new ObjectsCache("AUTH");
return(AuthObjectsCache);    
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

