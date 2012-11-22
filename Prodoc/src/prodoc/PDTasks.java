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
public class PDTasks extends ObjPD
{
/**
 *
 */
public static final String fPDID="PDId";
public static final String fNAME="Name";
public static final String fCATEGORY="Category";
public static final String fTYPE="TaskType";
public static final String fOBJTYPE="ObjType";
public static final String fFILTER="ObjFilter";
public static final String fPARAM="TaskParam";
public static final String fTASKUSER="TaskUser";
public static final String fACTIVE="Active";
public static final String fTRANSACT="Transact";

/**
 *
 */
static private Record TaksTypeStruct=null;
/**
 *
 */
private String Name;
private String PDId;
private String Category;
/**
 * Type of task:
 * -1: Externa
 * 0: Expurgo
 * 1: Copia
 * 
 */
private int Type=0;
/**
 * Parameters to be interpreteddepending of task
 */
private String ObjType="";
private String ObjFilter="";
private String Param="";
private String TaskUser="";
private boolean Transact=false;

static private ObjectsCache TaksDefObjectsCache = null;

//-------------------------------------------------------------------------
/**
 * 
 * @param Drv
 * @throws PDException
 */
public PDTasks(DriverGeneric Drv)  throws PDException
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
setCategory((String) Rec.getAttr(fCATEGORY).getValue());
setPDId((String) Rec.getAttr(fPDID).getValue());
setType((Integer) Rec.getAttr(fTYPE).getValue());
setObjType((String) Rec.getAttr(fOBJTYPE).getValue());
setObjFilter((String) Rec.getAttr(fFILTER).getValue());
setParam((String) Rec.getAttr(fPARAM).getValue());
setTaskUser((String) Rec.getAttr(fTASKUSER).getValue());
setTransact((Boolean) Rec.getAttr(fTRANSACT).getValue());
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
Rec.getAttr(fPDID).setValue(getPDId());
Rec.getAttr(fTYPE).setValue(getType());
Rec.getAttr(fPARAM).setValue(getParam());
Rec.getAttr(fTASKUSER).setValue(getTaskUser());
Rec.getAttr(fOBJTYPE).setValue(getObjType());
Rec.getAttr(fFILTER).setValue(getObjFilter());
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
    R.addAttr( new Attribute(fPDID, fPDID, "PDId", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fNAME, fNAME, "Name of tasks", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fCATEGORY, fCATEGORY, "Group of tasks", Attribute.tSTRING, false, null, 32, false, false, true));
    R.addAttr( new Attribute(fTYPE, fTYPE, "Type of Task", Attribute.tINTEGER, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fOBJTYPE, fOBJTYPE, "Target Object(s)", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fFILTER, fFILTER, "Filter of Object(s)", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fPARAM, fPARAM, "Params of Task", Attribute.tSTRING, false, null, 256, false, false, true));
    R.addAttr( new Attribute(fTASKUSER, fTASKUSER, "User for executing Task", Attribute.tSTRING, true, null, 32, false, false, true));
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
Drv.AddIntegrity(getTableName(), fTASKUSER, PDUser.getTableName(),PDUser.fNAME);
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
/**
 * @return the PDId
 */
public String getPDId()
{
return PDId;
}
//-------------------------------------------------------------------------
/**
 * @param PDId the PDId to set
 */
public void setPDId(String PDId)
{
this.PDId = PDId;
}
//-------------------------------------------------------------------------
/*
 * Executes the Tasks connecting trought the Driver of the object
 */
protected void Execute()
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasks.Execute>:"+PDId);  
try {
Cursor ObjList=PDTasksDef.GenerateList(this);   
Record Res=getDrv().NextRec(ObjList);
while (Res!=null)
    {
    String PD1=(String) Res.getAttr(fPDID).getValue();
//    ListUF.add(PD1);
    Res=getDrv().NextRec(ObjList);
    }
getDrv().CloseCursor(ObjList);
} catch(Exception ex)
    {
    
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasks.Execute<:"+PDId);    
}
//-------------------------------------------------------------------------
}
