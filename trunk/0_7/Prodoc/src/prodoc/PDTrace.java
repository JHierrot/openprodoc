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
public class PDTrace extends ObjPD
{
/**
 *
 */
public static final String fOBJECTTYPE="ObjectType";
/**
 *
 */
public static final String fOBJECTID="ObjectId";
/**
 *
 */
public static final String fOPERATION="Operation";

/**
 *
 */
static private Record TraceStruct=null;
/**
 *
 */
private String ObjectType;
/**
 *
 */
private String ObjectId;
/**
 *
 */
private String Operation;

static private ObjectsCache TraceObjectsCache = null;

//-------------------------------------------------------------------------
/**
 *
 * @param Drv
 */
public PDTrace(DriverGeneric Drv)
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
setObjectType((String) Rec.getAttr(fOBJECTTYPE).getValue());
setName((String) Rec.getAttr(fOBJECTID).getValue());
setOperation((String) Rec.getAttr(fOPERATION).getValue());
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
Rec.getAttr(fOBJECTTYPE).setValue(getObjectType());
Rec.getAttr(fOBJECTID).setValue(getName());
Rec.getAttr(fOPERATION).setValue(getOperation());
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
ListCond.addCondition(new Condition(fOBJECTID, Condition.cEQUAL, getName()));
return(ListCond);
}
//-------------------------------------------------------------------------
protected Conditions getConditionsLike(String Name) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fOBJECTID, Condition.cLIKE, VerifyWildCards(Name)));
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
return ("PD_TRACE");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
Record getRecordStruct() throws PDException
{
if (TraceStruct==null)
    TraceStruct=CreateRecordStruct();
return(TraceStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (TraceStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fOBJECTTYPE, "Object_type", "Object_type", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fOBJECTID, "PDID", "PDID", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fOPERATION, "Operation", "Operation", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(TraceStruct);
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
return ObjectId;
}
//-------------------------------------------------------------------------
/**
* @param Name the Name to set
*/
public void setName(String Name)
{
this.ObjectId = Name;
}
//-------------------------------------------------------------------------
/**
* @return the Description
*/
public String getOperation()
{
return Operation;
}
//-------------------------------------------------------------------------
/**
 * @param pOperation
*/
public void setOperation(String pOperation)
{
this.Operation = pOperation;
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
// TODO: review VerifyAllowedDel in PDTrace
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
PDException.GenPDException("Log_modification_not_allowed_to_user", null);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
}
//-------------------------------------------------------------------------
/**
* @return the ObjectType
*/
public String getObjectType()
{
return ObjectType;
}
//-------------------------------------------------------------------------
/**
 * @param pObjectType
*/
public void setObjectType(String pObjectType)
{
ObjectType = pObjectType;
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getDefaultOrder()
{
return("");
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (TraceObjectsCache==null)
    TraceObjectsCache=new ObjectsCache("Trace");
return(TraceObjectsCache);    
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
