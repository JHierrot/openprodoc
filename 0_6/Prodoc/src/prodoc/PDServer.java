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
public class PDServer extends ObjPD
{
/**
 *
 */
public static final String fNAME="Name";
/**
 *
 */
public static final String fPRODOCKEY="ProdocKey";
/**
 *
 */
public static final String fVERSION="Version";

/**
 *
 */
static private Record ServerTypeStruct=null;
/**
 *
 */
private String Name;
/**
 *
 */
private String ProdocKey;
/**
 *
 */
private String Version;

static private ObjectsCache ServerObjectsCache = null;

//-------------------------------------------------------------------------
/**
 * 
 * @param Drv
 * @throws PDException
 */
public PDServer(DriverGeneric Drv)  throws PDException
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
setKey((String) Rec.getAttr(fPRODOCKEY).getValue());
setVersion((String) Rec.getAttr(fVERSION).getValue());
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
Rec.getAttr(fPRODOCKEY).setValue(getKey());
Rec.getAttr(fVERSION).setValue(getVersion());
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
return ("PD_SERVER");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
Record getRecordStruct() throws PDException
{
if (ServerTypeStruct==null)
    ServerTypeStruct=CreateRecordStruct();
return(ServerTypeStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (ServerTypeStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, "Name", "Name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPRODOCKEY, "Key", "Key", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fVERSION, "Version", "Version", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(ServerTypeStruct);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 */
protected void AsignKey(String Ident)
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
public void setName(String Name)
{
this.Name = Name;
}
//-------------------------------------------------------------------------
/**
* @return the Description
*/
protected String getKey()
{
return ProdocKey;
}
//-------------------------------------------------------------------------
/**
* @param pKey the Description to set
*/
protected void setKey(String pKey)
{
ProdocKey = pKey;
}
//-------------------------------------------------------------------------
/**
* @return the Extension
*/
public String getVersion()
{
return Version;
}
//-------------------------------------------------------------------------
/**
* @param pVersion the Extension to set
*/
public void setVersion(String pVersion)
{
Version = pVersion;
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))
   PDException.GenPDException("Edition_of_Server_is_not_allowed", getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
PDException.GenPDException("Edition_of_Server_is_not_allowed", getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
PDException.GenPDException("Edition_of_Server_is_not_allowed", getName());
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
if (ServerObjectsCache==null)
    ServerObjectsCache=new ObjectsCache("Server");
return(ServerObjectsCache);    
}
//-------------------------------------------------------------------------
}
