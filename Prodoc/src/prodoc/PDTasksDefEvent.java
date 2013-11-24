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

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jhierrot
 */
public class PDTasksDefEvent extends PDTasksDef
{



/**
 *
 */
static private Record TaksTypeStruct=null;

/**
 *
 */
private String Name;
private String Category;
/**
 *
 */
private String Description;
/**
 *
 */
private Date StartDate;
private Date NextDate;
private int AddMonth=0;
private int AddDays=0;
private int AddHours=0;
private int AddMins=0;


private int Type=0;
/**
 * Parameters to be interpreteddepending of task
 */
private String ObjType="";
private String ObjFilter="";
private String Param="";
private String TaskUser="";
private boolean Active=false;
private boolean Transact=false;

static private ObjectsCache TaksDefObjectsCache = null;

//-------------------------------------------------------------------------
/**
 * 
 * @param Drv
 * @throws PDException
 */
public PDTasksDefEvent(DriverGeneric Drv)  throws PDException
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
Rec.getAttr(fCATEGORY).setValue(getCategory());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fSTARTDATE).setValue(getStartDate());
Rec.getAttr(fTYPE).setValue(getType());
Rec.getAttr(fPARAM).setValue(getParam());
Rec.getAttr(fOBJTYPE).setValue(getObjType());
Rec.getAttr(fFILTER).setValue(getObjFilter());
Rec.getAttr(fACTIVE).setValue(isActive());
Rec.getAttr(fTRANSACT).setValue(isTransact());
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
return ("PD_TASKSDEF");
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
    R.addAttr( new Attribute(fNAME, fNAME, "Name of tasks", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fCATEGORY, fCATEGORY, "Group of tasks", Attribute.tSTRING, false, null, 32, false, false, true));
    R.addAttr( new Attribute(fDESCRIPTION, fDESCRIPTION, "Description", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fSTARTDATE, fSTARTDATE, "Start Date of execution", Attribute.tDATE, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fTYPE, fTYPE, "Type of Task", Attribute.tINTEGER, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fOBJTYPE, fOBJTYPE, "Target Object(s)", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fFILTER, fFILTER, "Filter of Object(s)", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fPARAM, fPARAM, "Params of Task", Attribute.tSTRING, false, null, 256, false, false, true));
    R.addAttr( new Attribute(fACTIVE, fACTIVE, "Indicates if the definition is active", Attribute.tBOOLEAN, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fTRANSACT, fTRANSACT, "Indicates if the task id transactional", Attribute.tBOOLEAN, true, null, 32, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(TaksTypeStruct);
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
/**
* @return the Name
*/
public String getName()
{
return Name;
}
//-----------------------------------------------------------------------
/**
 * @param Name the Name to set
 * @throws PDExceptionFunc  
*/
public void setName(String Name) throws PDExceptionFunc
{
this.Name = CheckName(Name);
}
//-----------------------------------------------------------------------
/**
* @return the Description
*/
public String getDescription()
{
return Description;
}
//-----------------------------------------------------------------------
/**
* @param Description the Description to set
*/
public void setDescription(String Description)
{
this.Description = Description;
}
//-----------------------------------------------------------------------
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
public Date getNextDate()
{
return NextDate;
}
//-----------------------------------------------------------------------
/**
 * @param NextDate 
 */
public void setNextDate(Date NextDate)
{
this.NextDate = NextDate;
}
//-----------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))  
    if (!getDrv().getUser().getRol().isAllowCreateRepos() )
   PDException.GenPDException("Tasks_creation_not_allowed_to_user",getName());
}
//-----------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRepos() )
   PDException.GenPDException("Tasks_delete_not_allowed_to_user",getName());
}
//-----------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainRepos() )
   PDException.GenPDException("Tasks_modification_not_allowed_to_user",getName());
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
if (TaksDefObjectsCache==null)
    TaksDefObjectsCache=new ObjectsCache("TasksDef");
return(TaksDefObjectsCache);    
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
 * @return the AddMonth
 */
public int getAddMonth()
{
return AddMonth;
}
//-------------------------------------------------------------------------
/**
 * @param AddMonth the AddMonth to set
 */
public void setAddMonth(int AddMonth)
{
this.AddMonth = AddMonth;
}
//-------------------------------------------------------------------------
/**
 * @return the AddDays
 */
public int getAddDays()
{
return AddDays;
}
//-------------------------------------------------------------------------
/**
 * @param AddDays the AddDays to set
 */
public void setAddDays(int AddDays)
{
this.AddDays = AddDays;
}
//-------------------------------------------------------------------------
/**
 * @return the AddHours
 */
public int getAddHours()
{
return AddHours;
}
//-------------------------------------------------------------------------
/**
 * @param AddHours the AddHours to set
 */
public void setAddHours(int AddHours)
{
this.AddHours = AddHours;
}
//-------------------------------------------------------------------------
/**
 * @return the AddMins
 */
public int getAddMins()
{
return AddMins;
}
//-------------------------------------------------------------------------
/**
 * @param AddMins the AddMins to set
 */
public void setAddMins(int AddMins)
{
this.AddMins = AddMins;
}
//-------------------------------------------------------------------------
/**
 * @return the Type
 */
public int getType()
{
return Type;
}
//-------------------------------------------------------------------------
/**
 * @param Type the Type to set
 */
public void setType(int Type)
{
this.Type = Type;
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
 * @param Param the Param to set
 */
public void setParam(String Param)
{
this.Param = Param;
}
//-------------------------------------------------------------------------
/**
 * @return the TaskUser
 */
public String getTaskUser()
{
return TaskUser;
}
//-------------------------------------------------------------------------
/**
 * @param TaskUser the TaskUser to set
 */
public void setTaskUser(String TaskUser)
{
this.TaskUser = TaskUser;
}
//-------------------------------------------------------------------------
/**
 * Start the main tasks that supervises and starts all the others.
 * @param Drv
 */
public static void StartTaskGenerator(DriverGeneric Drv)
{
    
}
//-------------------------------------------------------------------------
/**
 * Stops the main tasks that supervises and starts all the others.
 */
public static void StopTaskGenerator()
{
    
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
 * @return the ObjType
 */
public String getObjType()
{
return ObjType;
}
//-------------------------------------------------------------------------
/**
 * @param ObjType the ObjType to set
 */
public void setObjType(String ObjType)
{
this.ObjType = ObjType;
}
//-------------------------------------------------------------------------
/**
 * @return the ObjFilter
 */
public String getObjFilter()
{
return ObjFilter;
}
//-------------------------------------------------------------------------
/**
 * @param ObjFilter the ObjFilter to set
 */
public void setObjFilter(String ObjFilter)
{
this.ObjFilter = ObjFilter;
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
 * @return the Transact
 */
public boolean isTransact()
{
return Transact;
}
//-------------------------------------------------------------------------
/**
 * @param Transact the Transact to set
 */
public void setTransact(boolean Transact)
{
this.Transact = Transact;
}
//-------------------------------------------------------------------------
/**
 * @return the Category
 */
public String getCategory()
{
return Category;
}
//-------------------------------------------------------------------------
/**
 * @param Category the Category to set
 */
public void setCategory(String Category)
{
this.Category = Category;
}
//-------------------------------------------------------------------------
protected static Cursor GenerateList(PDTasks aThis)
{
Cursor List=null;
// TODO: implement cursor
return(List);
}
//-------------------------------------------------------------------------
}
