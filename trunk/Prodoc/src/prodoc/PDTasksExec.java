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

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author jhierrot
 */
public class PDTasksExec extends PDTasksDef
{

    /**
     *
     */
    public static final String fPDID="PDId";
    /**
     *
     */
    public static final String fNEXTDATE="NextDate";


/**
 *
 */
static private Record TaksTypeStruct=null;

/**
 *
 */
private String PDId;
private Date NextDate;

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
setNextDate((Date) Rec.getAttr(fNEXTDATE).getValue());
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
Rec.getAttr(fNEXTDATE).setValue(getNextDate());
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
    R.addAttr( new Attribute(fNEXTDATE, fNEXTDATE, "Next Date of execution", Attribute.tTIMESTAMP, true, null, 128, false, false, true));
    return(R);
    }
else
    return(TaksTypeStruct);
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

void GenFromDef(PDTasksCron Task) throws PDException
{
super.assignValues(Task.getRecord());
setPDId(GenerateId());
setNextDate(Task.getNextDate());
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
/**
 * Retrieves and executes all the pending task
 * @param TaskCategory catrgory group (* for all)
 */
void ExecutePendingTaskCat(String TaskCategory) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.ExecutePendingTaskCat>:" + TaskCategory);
Cursor CursorId=null;
try {
Conditions CondT=new Conditions();
if (TaskCategory!=null && !TaskCategory.equals("*"))
    {
    Condition c=new Condition(fCATEGORY, Condition.cEQUAL, TaskCategory);
    CondT.addCondition(c);
    }
Query QBE=new Query(getTabName(), getRecordStruct(),CondT, fPDID);
CursorId=getDrv().OpenCursor(QBE);
Record Res=getDrv().NextRec(CursorId);
PDTasksExec Task=new PDTasksExec(getDrv());
PDTasksExecEnded TaskEnd=new PDTasksExecEnded(getDrv());
while (Res!=null)
    {
    Task.assignValues(Res);    
    getDrv().IniciarTrans();
    Record R=TaskEnd.getRecord();
    R.assign(Task.getRecord());
    TaskEnd.assignValues(R);
    TaskEnd.setStartDate(new Date());
    try {
    Task.Execute();
    TaskEnd.setEndsOk(true);
    TaskEnd.setResult("");
    } catch (Exception ex)
        {
        if (PDLog.isInfo())
            PDLog.Info("ExecutePendingTaskCat error:"+ex.getLocalizedMessage());
        TaskEnd.setEndsOk(false);
        TaskEnd.setResult(ex.getLocalizedMessage());        
        }
    TaskEnd.setEndDate(new Date());   
    TaskEnd.insert();
    Task.delete();    
    getDrv().CerrarTrans();
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
} catch (Exception ex)
    { 
    ex.printStackTrace();
    if (CursorId!=null)    
        getDrv().CloseCursor(CursorId);
    if (getDrv().isInTransaction())
        getDrv().AnularTrans();
    throw new PDException(ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.ExecutePendingTaskCat <");
}
//-------------------------------------------------------------------------
/**
 * Executes the task defined in current object
 * Beware that can be VERY DANGEROUS and DELETE THOUsands OF oBJECTS
 * @throws PDException 
 */
public void Execute()  throws PDException
{
switch (getType())
    {
    case fTASK_DELETE_OLD_FOLD: DeleteOldFold();
        break;
    case fTASK_DELETE_OLD_DOC: DeleteOldDoc();
        break;
    case fTASK_DELETEFOLD: DeleteFold();
        break;
    case fTASK_DELETEDOC:DeleteDoc();
        break;
    case fTASK_PURGEDOC: PurgeDoc();
        break;
    case fTASK_COPYDOC: CopyDoc();
        break;
    case fTASK_MOVEDOC: MoveDoc();
        break;
    case fTASK_UPDATEDOC: UpdateDoc();
        break;
    case fTASK_UPDATEFOLD: UpdateFold();
        break;
    case fTASK_IMPORT: Import();
        break;
    case fTASK_EXPORT: Export();
        break;
    default: PDExceptionFunc.GenPDException("Unexpected_Task", ""+getType());
        break;
    }
}
//-------------------------------------------------------------------------
/**
 * Executes the task defined in current object
 * @return
 * @throws PDException In any error 
 */
public Cursor GenCur()  throws PDException
{
switch (getType())
    {
    case fTASK_DELETE_OLD_FOLD: return CurDeleteOldFold();
    case fTASK_DELETE_OLD_DOC: return CurDeleteOldDoc();
    case fTASK_PURGEDOC: return CurPurgeDoc();
    case fTASK_IMPORT: return CurImport();
    case fTASK_EXPORT: return CurExport();

    case fTASK_DELETEFOLD: return CurDeleteFold();
    case fTASK_DELETEDOC:return CurDeleteDoc();
    case fTASK_COPYDOC: return CurCopyDoc();
    case fTASK_MOVEDOC: return CurMoveDoc();
    case fTASK_UPDATEDOC: return CurUpdateDoc();
    case fTASK_UPDATEFOLD: return CurUpdateFold();
    }
PDExceptionFunc.GenPDException("Undefined_task", ""+getType());
return (null);
}
//-------------------------------------------------------------------------

private void DeleteFold() throws PDException
{
throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private void DeleteDoc() throws PDException
{
throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private void PurgeDoc() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.PurgeDoc >"+getPDId());
PDDocs D=new PDDocs(this.getDrv());
Cursor CursorId=null;
try {
if (isTransact())  
    getDrv().IniciarTrans();
String Id=null;    
CursorId=this.GenCur();    
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    try {
    Id=(String)Res.getAttr(PDDocs.fPDID).getValue(); 
    D.Purge(getObjType(), Id); 
    } catch (Exception ex)
        {
        if (isTransact())    
            PDException.GenPDException(ex.getMessage(), Id);
        else    
            PDLog.Error("PDTasksExec.PurgeDoc:"+ex.getLocalizedMessage());

        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
getDrv().CerrarTrans();
} catch (Exception ex)
    {
    if (CursorId!=null)    
        getDrv().CloseCursor(CursorId);
    getDrv().AnularTrans();
    PDLog.Error("PDTasksExec.PurgeDoc:"+ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.PurgeDoc <"+getPDId());
}
//-------------------------------------------------------------------------

private void CopyDoc() throws PDException
{
throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private void MoveDoc() throws PDException
{
throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private void UpdateDoc() throws PDException
{
throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private void UpdateFold() throws PDException
{
throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private void Import() throws PDException
{
throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private void Export() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.Export >"+getPDId());
PDFolders F2Exp=new PDFolders(this.getDrv());
Cursor CursorId=null;
try {
if (isTransact())  
    getDrv().IniciarTrans();
String Id=null;    
//File Path=new File(getParam4());
CursorId=this.GenCur();    
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    try {
    F2Exp.assignValues(Res);    
    F2Exp.ExportPath(getParam3(), getParam4());   
    } catch (Exception ex)
        {
        if (isTransact())    
            PDException.GenPDException(ex.getMessage(), Id);
        else    
            PDLog.Error("PDTasksExec.Export:"+ex.getLocalizedMessage());

        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
getDrv().CerrarTrans();
} catch (Exception ex)
    {
    if (CursorId!=null)    
        getDrv().CloseCursor(CursorId);
    getDrv().AnularTrans();
    PDLog.Error("PDTasksExec.Export:"+ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.Export <"+getPDId());
}
//-------------------------------------------------------------------------
/**
 * Deletes old folders.
 * Param -> boolean 0/1 indcate Subtypes.
 * Param2 -> Integer indicates number of days
 * Param3 -> PDID Folder
 * @throws PDException 
 */
private void DeleteOldFold() throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("PDTasksExec.DeleteOldFold >"+getPDId());
PDFolders F=new PDFolders(this.getDrv());
Cursor CursorId=null;
try {
if (isTransact())  
    getDrv().IniciarTrans();
CursorId=CurDeleteOldFold();
Record Res=getDrv().NextRec(CursorId);
boolean PrevDeleted;
while (Res!=null)
    {
    F.assignValues(Res);
    try {
    F.Load((String)Res.getAttr(PDFolders.fPDID ).getValue());
    PrevDeleted=false;
    } catch (Exception ex) // control of sinultaneous delete or deleting of folders before subfolders
        {
        PrevDeleted=true;
        }
    if (!PrevDeleted)
        {
        try {
        F.delete(); 
        } catch (Exception ex)
            {
            if (isTransact())    
                PDException.GenPDException(ex.getMessage(), F.getPDId());
            else    
                PDLog.Error("PDTasksExec.DeleteOldFold:"+ex.getLocalizedMessage());
            }
        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
getDrv().CerrarTrans();
} catch (Exception ex)
    {
    if (CursorId!=null)    
        getDrv().CloseCursor(CursorId);
    getDrv().AnularTrans();
    PDLog.Error("PDTasksExec.DeleteOldFold:"+ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.DeleteOldFold >"+getPDId());
}
//-------------------------------------------------------------------------
/**
 * Common method to simulate affected elements 
 * @return 
 */
private Cursor CurDeleteOldFold() throws PDException
{
boolean SubTypes;
if (getParam()==null || getParam().length()==0 || getParam().charAt(0)=='0')
    SubTypes=false;
else
    SubTypes=true;
PDFolders F=new PDFolders(this.getDrv());
String FoldType;
if ("*".equals(getObjType()))
    FoldType=PDFolders.getTableName();
else
    FoldType=getObjType();
Calendar Date2Del=Calendar.getInstance();
Date2Del.setTime(getNextDate());
Date2Del.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(getParam2()));
Attribute Attr=F.getRecord().getAttr(PDFolders.fPDDATE);
Attr.setValue(Date2Del.getTime());
Condition c=new Condition(Attr, Condition.cLET);
Conditions Conds=new Conditions();
Conds.addCondition(c);
return(F.Search(FoldType,  Conds, SubTypes, true, getParam3(), null))  ;
}
//-------------------------------------------------------------------------
/**
 * Deletes old folders.
 * Param -> boolean 0/1 indcate Subtypes.
 * Param2 -> Integer indicates number of days
 * Param3 -> PDID Folder
 * @throws PDException 
 */
private void DeleteOldDoc() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.DeleteOldDoc >"+getPDId());
PDDocs D=new PDDocs(this.getDrv());
Cursor CursorId=null;
try {
if (isTransact())  
    getDrv().IniciarTrans();
CursorId=this.GenCur();    
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    D.assignValues(Res);
    try {
    D.delete(); 
    } catch (Exception ex)
        {
        PDLog.Error("PDTasksExec.DeleteOldDoc:"+ex.getLocalizedMessage());
        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
getDrv().CerrarTrans();
} catch (Exception ex)
    {
    if (CursorId!=null)    
        getDrv().CloseCursor(CursorId);
    getDrv().AnularTrans();
    PDLog.Error("PDTasksExec.DeleteOldDoc:"+ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.DeleteOldDoc >"+getPDId());
}
//-------------------------------------------------------------------------

private Cursor CurDeleteFold()
{
    throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private Cursor CurDeleteDoc()
{
    throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private Cursor CurPurgeDoc() throws PDException
{
String DocType;
if ("*".equals(getObjType()))
    DocType=PDFolders.getTableName();
else
    DocType=getObjType();
PDDocs Doc=new PDDocs(this.getDrv());
Calendar Date2Del=Calendar.getInstance();
Date2Del.setTime(new Date());
Date2Del.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(getParam2()));
return(Doc.ListDeletedBefore(DocType, Date2Del.getTime()));
}
//-------------------------------------------------------------------------

private Cursor CurCopyDoc()
{
    throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private Cursor CurMoveDoc()
{
    throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private Cursor CurUpdateDoc()
{
    throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private Cursor CurUpdateFold()
{
    throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private Cursor CurImport()
{
    throw new UnsupportedOperationException("Not yet implemented");
}
//-------------------------------------------------------------------------

private Cursor CurExport() throws PDException
{
boolean SubTypes;
if (getParam()==null || getParam().length()==0 || getParam().charAt(0)=='0')
    SubTypes=false;
else
    SubTypes=true;
PDFolders F=new PDFolders(this.getDrv());
String FoldType;
if ("*".equals(getObjType()))
    FoldType=PDFolders.getTableName();
else
    FoldType=getObjType();
Calendar Date2Del=Calendar.getInstance();
Date2Del.setTime(getNextDate());
Date2Del.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(getParam2()));
Attribute Attr=F.getRecord().getAttr(PDFolders.fPDDATE);
Attr.setValue(Date2Del.getTime());
Condition c=new Condition(Attr, Condition.cGET);
Conditions Conds=new Conditions();
Conds.addCondition(c);
return(F.Search(FoldType,  Conds, SubTypes, true, getParam3(), null))  ;
}
//-------------------------------------------------------------------------

private Cursor CurDeleteOldDoc() throws PDException
{
boolean SubTypes;
if (getParam()==null || getParam().length()==0 || getParam().charAt(0)=='0')
    SubTypes=false;
else
    SubTypes=true;
PDDocs Doc=new PDDocs(this.getDrv());
String DocType;
if ("*".equals(getObjType()))
    DocType=PDFolders.getTableName();
else
    DocType=getObjType();
Calendar Date2Del=Calendar.getInstance();
Date2Del.setTime(getNextDate());
Date2Del.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(getParam2()));
Attribute Attr=Doc.getRecord().getAttr(PDDocs.fPDDATE);
Attr.setValue(Date2Del.getTime());
Condition c=new Condition(Attr, Condition.cLET);
Conditions Conds=new Conditions();
Conds.addCondition(c);
return(Doc.Search(DocType, Conds, SubTypes, true, false, getParam3(), null))  ;
}
//-------------------------------------------------------------------------
/**
 * @return the NextDate
 */
public Date getNextDate()
{
return NextDate;
}
//-------------------------------------------------------------------------
/**
 * @param NextDate the NextDate to set
 */
public void setNextDate(Date NextDate)
{
this.NextDate = NextDate;
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
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
return(ListCond);
}
//-------------------------------------------------------------------------
}
