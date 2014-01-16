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

import java.util.Date;

/**
 *
 * @author jhierrot
 */
public class PDTasksExec extends PDTasksDef
{

public static final String fPDID="PDId";
public static final String fSTARTDATE="StartDate";
public static final String fENDDATE="EndDate";
public static final String fRESULT="Result";
public static final String fENDSOK="EndsOk";

/**
 *
 */
static private Record TaksTypeStruct=null;

/**
 *
 */
private String PDId;
private Date StartDate;
private Date EndDate;
private String Result;
private boolean EndsOk;

static private ObjectsCache TaksDefObjectsCache = null;

//-------------------------------------------------------------------------
/**
 * 
 * @param Drv
 * @throws PDException
 */
public PDTasksExec(DriverGeneric Drv)  throws PDException
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
super.assignValues(Rec);    
setPDId((String) Rec.getAttr(fPDID).getValue());
setStartDate((Date) Rec.getAttr(fSTARTDATE).getValue());
setEndDate((Date) Rec.getAttr(fENDDATE).getValue());
setResult((String) Rec.getAttr(fRESULT).getValue());
if (Rec.getAttr(fENDSOK).getValue()==null)
   setEndsOk(false);
else
    setEndsOk((Boolean) Rec.getAttr(fENDSOK).getValue());
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
Rec.assign(super.getRecord().Copy());
Rec.getAttr(fPDID).setValue(getPDId());
Rec.getAttr(fSTARTDATE).setValue(getStartDate());
Rec.getAttr(fENDDATE).setValue(getEndDate());
Rec.getAttr(fRESULT).setValue(getResult());
Rec.getAttr(fENDSOK).setValue(isEndsOk());
getCommonValues(Rec);
return(Rec);
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
@Override
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
return ("PD_TASKSEXECPEND");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
@Override
Record getRecordStruct() throws PDException
{
if (TaksTypeStruct==null)
    TaksTypeStruct=CreateRecordStruct();
return(TaksTypeStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (TaksTypeStruct==null)
    {
    Record R=new Record();
    CreateRecordStructBase(R);
    R.addAttr( new Attribute(fPDID, fPDID, "Unique_identifier", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fSTARTDATE, fSTARTDATE, "Start Date of execution", Attribute.tDATE, false, null, 32, false, false, true));
    R.addAttr( new Attribute(fENDDATE, fENDDATE, "Next Date of execution", Attribute.tDATE, false, null, 32, false, false, true));
    R.addAttr( new Attribute(fRESULT, fRESULT, "Description of error", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fENDSOK, fENDSOK, "True if the taks end correctly", Attribute.tBOOLEAN, false, null, 32, false, false, true));
return(R);
    }
else
    return(TaksTypeStruct);
}
//-------------------------------------------------------------------------
/**
* @return the StartDate
*/
public Date getStartDate()
{
return StartDate;
}
//-----------------------------------------------------------------------
/**
* @param StartDate the StartDate to set
*/
public void setStartDate(Date StartDate)
{
this.StartDate = StartDate;
}
//-----------------------------------------------------------------------
/**
* @return the NextDate
*/
public Date getEndDate()
{
return EndDate;
}
//-----------------------------------------------------------------------
/**
 * @param NextDate 
 */
public void setEndDate(Date NextDate)
{
this.EndDate = NextDate;
}
//-------------------------------------------------------------------------
/**
* The install method is generic because for instantiate a object, the class
* need to access to the tables for definition
 * @param Drv
 * @throws PDException
*/
static protected void InstallMulti(DriverGeneric Drv) throws PDException
{
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
 @Override
 protected ObjectsCache getObjCache()
{
if (TaksDefObjectsCache==null)
    TaksDefObjectsCache=new ObjectsCache("TasksDefExec");
return(TaksDefObjectsCache);    
}
//-------------------------------------------------------------------------
/**
 * @return the Result
 */
public String getResult()
{
return Result;
}
//-------------------------------------------------------------------------
/**
 * @param Result the Result to set
 */
public void setResult(String Result)
{
this.Result = Result;
}
//-------------------------------------------------------------------------
/**
 * @return the EndsOk
 */
public boolean isEndsOk()
{
return EndsOk;
}
//-------------------------------------------------------------------------
/**
 * @param EndsOk the EndsOk to set
 */
public void setEndsOk(boolean EndsOk)
{
this.EndsOk = EndsOk;
}
//-------------------------------------------------------------------------

void GenFromDef(PDTasksCron Task) throws PDException
{
super.assignValues(Task.getRecord());
setPDId(GenerateId());
}
//-------------------------------------------------------------------------

/**
 * @return the PDId
 */
public String getPDId()
{
    return PDId;
}
//---------------------------------------------------------------------
/**
 * @param PDId the PDId to set
 */
public void setPDId(String PDId)
{
    this.PDId = PDId;
}
//---------------------------------------------------------------------
/**
 * Generates an unique identifier
 * @return The identifier generated
 */
public String GenerateId()
{
StringBuilder genId = new StringBuilder();
genId.append(Long.toHexString(System.currentTimeMillis()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
return genId.toString();
}
//-------------------------------------------------------------------------

}
