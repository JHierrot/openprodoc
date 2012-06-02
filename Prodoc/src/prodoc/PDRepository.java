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
public class PDRepository extends ObjPD
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
public static final String fREPTYPE="RepType";
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
public static final String fENCRYPT="Encrypt";

/**
 *
 */
public static final String tBBDD="DDBB";
/**
 *
 */
public static final String tFS="FS";
/**
 *
 */
public static final String tFTP="FTP";
/**
 *
 */
public static final String tREFURL="REFURL";
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
    tList.add(tFS);
    tList.add(tBBDD);
    tList.add(tFTP);
    tList.add(tREFURL);
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
private String RepType;
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
/**
 *
 */
private boolean Encrypted=false;

static private ObjectsCache RepObjectsCache = null;

/**
 *
 * @param Drv
 * @throws PDException
 */
public PDRepository(DriverGeneric Drv) throws PDException
{
super(Drv);
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
if (!gettList().contains((String) Rec.getAttr(fREPTYPE).getValue()))
    setRepType(tFS);
else
    setRepType((String) Rec.getAttr(fREPTYPE).getValue());
setURL((String) Rec.getAttr(fURL).getValue());
setParam((String) Rec.getAttr(fPARAM).getValue());
setUser((String) Rec.getAttr(fUSERNAME).getValue());
setPassword((String) Rec.getAttr(fPASSWORD).getValue());
if (Rec.getAttr(fENCRYPT).getValue()!=null)
    setEncrypted((Boolean) Rec.getAttr(fENCRYPT).getValue());
else
    setEncrypted(false);
assignCommonValues(Rec);
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
Rec.getAttr(fREPTYPE).setValue(getRepType());
Rec.getAttr(fURL).setValue(getURL());
Rec.getAttr(fPARAM).setValue(getParam());
Rec.getAttr(fUSERNAME).setValue(getUser());
Rec.getAttr(fPASSWORD).setValue(getPassword());
Rec.getAttr(fENCRYPT).setValue(isEncrypted());
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
return ("PD_REPOSITORIES");
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
    R.addAttr( new Attribute(fNAME, "Name", "Name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, "Description", "Description", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fREPTYPE, "Repository_type", "Repository_type", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fURL, "URL", "URL_URI_Repository_reference", Attribute.tSTRING, true, null, 254, false, false, true));
    R.addAttr( new Attribute(fPARAM, "Additional_parameters", "Additional_parameters", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fUSERNAME, "User", "Connection_user_name", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fPASSWORD, "Password", "Connection_Password", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fENCRYPT, "Encrypted", "When_true_the_Repository_it_is_encrypted", Attribute.tBOOLEAN, false, null, 0, false, false, false));
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
Password = (pPassword);
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
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))  
  if (!getDrv().getUser().getRol().isAllowCreateRepos() )
   PDExceptionFunc.GenPDException("Repository_creation_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRepos() )
   PDExceptionFunc.GenPDException("Repository_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRepos() )
   PDExceptionFunc.GenPDException("Repository_modification_not_allowed_to_user",getName());
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
* @return the RepType
*/
public String getRepType()
{
return RepType;
}
//-------------------------------------------------------------------------
/**
 * @param pRepType the RepType to set
 * @throws PDException 
*/
public void setRepType(String pRepType)  throws PDException
{
if (!gettList().contains(pRepType))
    PDException.GenPDException("Repository_type_incorrect", pRepType);
RepType = pRepType;
}
//-------------------------------------------------------------------------
/**
 * @return the Encrypted
 */
public boolean isEncrypted()
{
return Encrypted;
}
//-------------------------------------------------------------------------
/**
 * @param Encrypted the Encrypted to set
 */
public void setEncrypted(boolean Encrypted)
{
this.Encrypted = Encrypted;
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (RepObjectsCache==null)
    RepObjectsCache=new ObjectsCache("Rep");
return(RepObjectsCache);    
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

