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

import java.util.Date;

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
public static final String fRESULT="Result";
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
private boolean Result;

static private ObjectsCache TraceObjectsCache = null;

//-------------------------------------------------------------------------
/**
 * Default constructor
 * @param Drv OpenProdoc session
 */
public PDTrace(DriverGeneric Drv)
{
super(Drv);
}
//-------------------------------------------------------------------------
/**
 * Assign new values to the PDTrace object
 * @param Rec Record of PDTrace type with new values
 * @throws PDException in any error
 */
public void assignValues(Record Rec) throws PDException
{
setObjectType((String) Rec.getAttr(fOBJECTTYPE).getValue());
setName((String) Rec.getAttr(fOBJECTID).getValue());
setOperation((String) Rec.getAttr(fOPERATION).getValue());
setResult((Boolean) Rec.getAttr(fRESULT).getValue());
assignCommonValues(Rec);
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
Rec.getAttr(fOBJECTTYPE).setValue(getObjectType());
Rec.getAttr(fOBJECTID).setValue(getName());
Rec.getAttr(fOPERATION).setValue(getOperation());
Rec.getAttr(fRESULT).setValue(isResult());
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
 * Returns the name of PDTrace table in DDBB
 * @return the name of PDTrace table in DDBB
 */
public String getTabName()
{
return (getTableName());
}
//-------------------------------------------------------------------------
/**
 * Static method that returns the name of PDTrace table in DDBB
 * @return the name of PDTrace table in DDBB
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
    R.addAttr( new Attribute(fOPERATION, "Operation", "Operation", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fRESULT, "Result", "Result", Attribute.tBOOLEAN, false, null, 0, false, false, false));
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
 * @param pOperation Operation (Insert, Update, delete, View) to trace
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
 * @param pObjectType Object Class to trace
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

/**
* @return the Result
*/
public boolean isResult()
{
return Result;
}
//-------------------------------------------------------------------------
/**
* @param pResult the Result to set
*/
public void setResult(boolean pResult)
{
Result = pResult;
}
//-------------------------------------------------------------------------
/**
 * Search PDTrace objects with conditions creating a Cursor
 * @param Conds Conditions to gfilter
 * @return a created cursor
 * @throws PDException in any error
 */    
public Cursor Search(Conditions Conds) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDef.Search>:"+getTabName());
Query QBE=new Query(getTabName(), getRecordStruct(), Conds);
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDef.Search <");
return(getDrv().OpenCursor(QBE));
}
//-------------------------------------------------------------------------
/**
 * Deletes a range of traces
 * @param ObjType Class of Object
 * @param D1 Mimimum date
 * @param D2 Máximum date
 * @throws PDException in any error
 */
public void DeleteRange(String ObjType, Date D1, Date D2) throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDTrace.DeleteRange>");
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
Conditions Conds=new Conditions();   
if (ObjType!=null && ObjType.length()!=0)
    {
    Condition C=new Condition(PDTrace.fOBJECTTYPE, Condition.cEQUAL, ObjType);
    Conds.addCondition(C);
    }
if (D1!=null)
    { 
    Attribute AttrF1=getRecord().getAttr(PDTasksExecEnded.fPDDATE);
    AttrF1.setValue(D1);
    Condition C1=new Condition(AttrF1, Condition.cGET);     
    Conds.addCondition(C1);
    }
if (D2!=null)
    { 
    Attribute AttrF2=getRecord().getAttr(PDTasksExecEnded.fPDDATE);
    AttrF2.setValue(D2);    
    Condition C2=new Condition(AttrF2, Condition.cLET);  
    Conds.addCondition(C2);    
    }
getDrv().DeleteRecord(getTabName(), Conds);
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDTrace.DeleteRange<");
}
//-------------------------------------------------------------------------
}
