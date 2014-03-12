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

import java.util.Vector;

/**
 *
 * @author jhierrot
 */
public class PDTasksDef extends ObjPD
{
public static final String fNAME="Name";
public static final String fCATEGORY="Category";
public static final String fDESCRIPTION="Description";
public static final String fTYPE="TaskType";
public static final String fOBJTYPE="ObjType";
public static final String fFILTER="ObjFilter";
public static final String fPARAM="TaskParam";
public static final String fPARAM2="TaskParam2";
public static final String fPARAM3="TaskParam3";
public static final String fPARAM4="TaskParam4";
public static final String fACTIVE="Active";
public static final String fTRANSACT="Transact";

public static final String fMODETIM="TIM";

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

public static final int fTASK_DELETE_OLD_FOLD=0;
public static final int fTASK_DELETE_OLD_DOC=1;
public static final int fTASK_PURGEDOC=2;

public static final int fTASK_COPYDOC=3;
public static final int fTASK_MOVEDOC=4;
public static final int fTASK_UPDATEDOC=5;
public static final int fTASK_UPDATEFOLD=6;
public static final int fTASK_IMPORT=7;
public static final int fTASK_EXPORT=8;
public static final int fTASK_DELETEFOLD=9;
public static final int fTASK_DELETEDOC=10;

private static final String[] LisTypeTask= {"DELETE_OLD_FOLD",
                                            "DELETE_OLD_DOC",
                                            "PURGE_DOC",
                                            /* "DELETE_FOLD",
                                            "DELETE_DOC",
                                            "COPY_DOC",
                                            "MOVE_DOC",
                                            "UPDATE_DOC",
                                            "UPDATE_FOLD",
                                            "IMPORT",
                                            "EXPORT" */
                                            };

        
private int Type=0;
/**
 * Parameters to be interpreteddepending of task
 */
private String ObjType="";
private String ObjFilter="";
private String Param="";
private String Param2="";
private String Param3="";
private String Param4="";
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
public PDTasksDef(DriverGeneric Drv)  throws PDException
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
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
if (Rec.getAttr(fTYPE).getValue()==null)
    setType(0);
else
    setType((Integer) Rec.getAttr(fTYPE).getValue());
setObjType((String) Rec.getAttr(fOBJTYPE).getValue());
setObjFilter((String) Rec.getAttr(fFILTER).getValue());
setParam((String) Rec.getAttr(fPARAM).getValue());
setParam2((String) Rec.getAttr(fPARAM2).getValue());
setParam3((String) Rec.getAttr(fPARAM3).getValue());
setParam4((String) Rec.getAttr(fPARAM4).getValue());
if (Rec.getAttr(fACTIVE).getValue()==null)
    setActive(true);
else
    setActive((Boolean) Rec.getAttr(fACTIVE).getValue());
if (Rec.getAttr(fTRANSACT).getValue()==null)
    setTransact(true);
else
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
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fTYPE).setValue(getType());
Rec.getAttr(fPARAM).setValue(getParam());
Rec.getAttr(fPARAM2).setValue(getParam2());
Rec.getAttr(fPARAM3).setValue(getParam3());
Rec.getAttr(fPARAM4).setValue(getParam4());
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
Record getRecordStruct() throws PDException
{
throw new PDException("PDTasksDef getRecoedStruct null");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized protected void CreateRecordStructBase(Record R) throws PDException
{
R.addAttr( new Attribute(fNAME, fNAME, "Name of tasks", Attribute.tSTRING, true, null, 32, true, false, false));
R.addAttr( new Attribute(fCATEGORY, fCATEGORY, "Group of tasks", Attribute.tSTRING, false, null, 32, false, false, true));
R.addAttr( new Attribute(fDESCRIPTION, fDESCRIPTION, "Description", Attribute.tSTRING, true, null, 128, false, false, true));
R.addAttr( new Attribute(fTYPE, fTYPE, "Type of Task", Attribute.tINTEGER, true, null, 128, false, false, true));
R.addAttr( new Attribute(fOBJTYPE, fOBJTYPE, "Target Object(s)", Attribute.tSTRING, true, null, 128, false, false, true));
R.addAttr( new Attribute(fFILTER, fFILTER, "Filter of Object(s)", Attribute.tSTRING, false, null, 254, false, false, true));
R.addAttr( new Attribute(fPARAM, fPARAM, "Params of Task", Attribute.tSTRING, false, null, 254, false, false, true));
R.addAttr( new Attribute(fPARAM2, fPARAM2, "Params of Task2", Attribute.tSTRING, false, null, 254, false, false, true));
R.addAttr( new Attribute(fPARAM3, fPARAM3, "Params of Task3", Attribute.tSTRING, false, null, 254, false, false, true));
R.addAttr( new Attribute(fPARAM4, fPARAM4, "Params of Task4", Attribute.tSTRING, false, null, 254, false, false, true));
R.addAttr( new Attribute(fACTIVE, fACTIVE, "Indicates if the definition is active", Attribute.tBOOLEAN, true, null, 32, false, false, true));
R.addAttr( new Attribute(fTRANSACT, fTRANSACT, "Indicates if the task is transactional", Attribute.tBOOLEAN, true, null, 32, false, false, true));
R.addRecord(getRecordStructCommon());
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
/***
protected ArrayList getListTaskTrans(String tabName, String MODE) throws PDException
{
Condition CMode=new Condition(f, Condition.cEQUAL , MODE);    
Condition CTab=new Condition(f, Condition.cEQUAL , tabName); 
Condition CTrans=new Condition(fTRANSACT, Condition.cNE, true ); 
Conditions c=new Conditions();
c.addCondition(CMode);
c.addCondition(CTab);
c.addCondition(CTrans);
Query LoadAct=new Query(getTabName(), getRecordStruct(), c);
ArrayList TList=new ArrayList();    
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record r=getDrv().NextRec(Cur);
while (r!=null)
    {
    PDTasksDef t=new PDTasksDef(getDrv());   
    t.assignValues(r);
    TList.add(t);
    r=getDrv().NextRec(Cur);
    }
getDrv().CloseCursor(Cur);
return(TList);
}
//-------------------------------------------------------------------------

ArrayList getListTaskNoTrans(String tabName, String MODE) throws PDException
{
Condition CMode=new Condition(f, Condition.cEQUAL , MODE);    
Condition CTab=new Condition(f, Condition.cEQUAL , tabName); 
Condition CTrans=new Condition(fTRANSACT, Condition.cNE, false ); 
Conditions c=new Conditions();
c.addCondition(CMode);
c.addCondition(CTab);
c.addCondition(CTrans);
Query LoadAct=new Query(getTabName(), getRecordStruct(), c);
ArrayList TList=new ArrayList();    
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record r=getDrv().NextRec(Cur);
while (r!=null)
    {
    PDTasksDef t=new PDTasksDef(getDrv());   
    t.assignValues(r);
    TList.add(t);
    r=getDrv().NextRec(Cur);
    }
getDrv().CloseCursor(Cur);
return(TList);
}**/
//-------------------------------------------------------------------------
static public String[] getListTypeTask()
{
return LisTypeTask;    
}
//-------------------------------------------------------------------------
/**
 * @return the Param2
 */
public String getParam2()
{
return Param2;
}
//-------------------------------------------------------------------------
/**
 * @param Param2 the Param2 to set
 */
public void setParam2(String Param2)
{
this.Param2 = Param2;
}
//-------------------------------------------------------------------------
/**
 * @return the Param3
 */
public String getParam3()
{
return Param3;
}
//-------------------------------------------------------------------------
/**
 * @param Param3 the Param3 to set
 */
public void setParam3(String Param3)
{
this.Param3 = Param3;
}
//-------------------------------------------------------------------------
/**
 * @return the Param4
 */
public String getParam4()
{
return Param4;
}
//-------------------------------------------------------------------------
/**
 * @param Param4 the Param4 to set
 */
public void setParam4(String Param4)
{
this.Param4 = Param4;
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Conds
 * @return 
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
}
