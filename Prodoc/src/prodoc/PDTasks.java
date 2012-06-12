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
public static final String fNAME="Name";
/**
 *
 */
public static final String fDESCRIPTION="Description";
/**
 *
 */
public static final String fSTARTDATE="StartDate";
/**
 *
 */
public static final String fNEXTDATE="NextDate";
public static final String fADDMONTH="AddMonth";
public static final String fADDDAYS="AddDays";
public static final String fADDHOURS="AddHours";
public static final String fADDMINS="AddMins";

/**
 *
 */
static private Record TaksTypeStruct=null;
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
private Date StartDate;
private Date NextDate;
private int AddMonth=0;
private int AddDays=0;
private int AddHours=0;
private int AddMins=0;

static private ObjectsCache TaksObjectsCache = null;

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
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
setStartDate((Date) Rec.getAttr(fSTARTDATE).getValue());
setNextDate((Date) Rec.getAttr(fNEXTDATE).getValue());
setAddDays(AddDays);
setAddHours(AddHours);
setAddMins(AddMins);
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
Rec.getAttr(fSTARTDATE).setValue(getStartDate());
Rec.getAttr(fNEXTDATE).setValue(getNextDate());
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
return ("PD_TASKS");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
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
    R.addAttr( new Attribute(fNAME, "Name", "Name of tasks", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, "Description", "Description", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fSTARTDATE, "StartDate", "Start Date of execution", Attribute.tDATE, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fNEXTDATE, "NextDate", "Next Date of execution", Attribute.tDATE, true, null, 128, false, false, true));
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

/**
 * @param Name the Name to set
 * @throws PDExceptionFunc  
*/
public void setName(String Name) throws PDExceptionFunc
{
this.Name = CheckName(Name);
}

/**
* @return the Description
*/
public String getDescription()
{
return Description;
}

/**
* @param Description the Description to set
*/
public void setDescription(String Description)
{
this.Description = Description;
}

/**
* @return the StartDate
*/
public Date getStartDate()
{
return StartDate;
}

/**
* @param StartDate the StartDate to set
*/
public void setStartDate(Date StartDate)
{
this.StartDate = StartDate;
}
/**
* @return the NextDate
*/
public Date getNextDate()
{
return NextDate;
}

/**
* @param StartDate the NextDate to set
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
if (TaksObjectsCache==null)
    TaksObjectsCache=new ObjectsCache("Tasks");
return(TaksObjectsCache);    
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

    /**
     * @param AddMonth the AddMonth to set
     */
    public void setAddMonth(int AddMonth)
    {
        this.AddMonth = AddMonth;
    }

    /**
     * @return the AddDays
     */
    public int getAddDays()
    {
        return AddDays;
    }

    /**
     * @param AddDays the AddDays to set
     */
    public void setAddDays(int AddDays)
    {
        this.AddDays = AddDays;
    }

    /**
     * @return the AddHours
     */
    public int getAddHours()
    {
        return AddHours;
    }

    /**
     * @param AddHours the AddHours to set
     */
    public void setAddHours(int AddHours)
    {
        this.AddHours = AddHours;
    }

    /**
     * @return the AddMins
     */
    public int getAddMins()
    {
        return AddMins;
    }

    /**
     * @param AddMins the AddMins to set
     */
    public void setAddMins(int AddMins)
    {
        this.AddMins = AddMins;
    }

}
