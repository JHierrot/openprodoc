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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static prodoc.PDTasksDefEvent.fTASKEVENT_COPY_FOLD;
import static prodoc.PDTasksDefEvent.fTASKEVENT_EXPORT_FOLD;
import static prodoc.PDTasksDefEvent.fTASKEVENT_UPDATE_FOLD;

/**
 * Class responsible of actual execution of pending task
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
/**
 * Generates a programed task from a definition
 * @param Task Definition of the task
 * @throws PDException in any error
 */
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
ArrayList<Record> LT=new ArrayList<Record>();
while (Res!=null)
    {
    LT.add(Res);
    Res=getDrv().NextRec(CursorId);
    }      
PDTasksExec Task=new PDTasksExec(getDrv());
PDTasksExecEnded TaskEnd=new PDTasksExecEnded(getDrv());
for (int i = 0; i < LT.size(); i++)
    {
    Res = LT.get(i);
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
    case fTASK_PURGEDOC: PurgeDoc();
        break;
    case fTASK_IMPORT: Import(getParam2(), getParam3(), getParam4(), getParam()!=null && getParam().equals("1"));
        break;
    case fTASK_EXPORT: Export();
        break;
    case fTASKEVENT_UPDATE_FOLD:
        ExecuteUpdFold();
        break;
    case fTASKEVENT_COPY_FOLD:
        ExecuteCopyFold();
        break;
    case fTASKEVENT_EXPORT_FOLD:
        ExecuteExportFold();
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
//    case fTASK_IMPORT: return CurImport(); No sense
    case fTASK_EXPORT: return CurExport();
    }
PDExceptionFunc.GenPDException("Undefined_task", ""+getType());
return (null);
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
/**
 * Imports O.S. folders into OpenProdoc repository
 * @throws PDException 
 */
private void Import(String DefDocType, String ParentId, String Path, boolean Recursive) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.Import >"+getPDId());
File ImpFold=new File(Path);
File []ListOrigin=ImpFold.listFiles();
ArrayList DirList=new ArrayList(5);
for (int i = 0; i < ListOrigin.length; i++) // first only folders and opd files to avouid double insert
    {
    File ListElement = ListOrigin[i];
    if (ListElement==null) // deleted by double reference opd + doc
        continue;
    if (ListElement.isDirectory())
        {
        DirList.add(ListElement);
        ListOrigin[i]=null;
        continue;
        }
    if (ListElement.getName().endsWith(".opd"))
        {
        getDrv().ProcessXML(ListElement, ParentId);
        int L2comp=ListElement.getName().length()-3;
        String FileName=ListElement.getName().substring(0, L2comp);
        for (int j = 0; j < ListOrigin.length; j++)
            {   
            if (ListOrigin[j]!=null && ListOrigin[j].getName().substring(0, L2comp).equals(FileName) 
                    && !ListOrigin[j].isDirectory())
               ListOrigin[j]=null; 
            }
        }
    }
for (File ListElement : ListOrigin)
    {
        if (ListElement==null) // deleted by double reference opd + doc
            continue;
        PDDocs NewDoc=new PDDocs(getDrv(), DefDocType);
        NewDoc.setTitle(ListElement.getName());
        NewDoc.setFile(ListElement.getAbsolutePath());
        NewDoc.setDocDate(new Date(ListElement.lastModified()));
        NewDoc.setParentId(ParentId);
        NewDoc.insert();  
    }
ListOrigin=null; // to help gc and save memory during recursivity
if (Recursive)
    {
    for (int i = 0; i < DirList.size(); i++)
        {
        File SubDir = (File) DirList.get(i);
        PDFolders f=new PDFolders(getDrv(), getObjType());
        f.setTitle(SubDir.getName());
        f.insert();
        Import(DefDocType, f.getPDId(), SubDir.getAbsolutePath(), Recursive);    
        }
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksExec.Import <"+getPDId());
}
//-------------------------------------------------------------------------
/**
 * 
 * @throws PDException 
 */
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
} catch (PDException ex)
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
String IdAct=F.getIdPath(getParam3());
return(F.Search(FoldType,  Conds, SubTypes, true, IdAct, null))  ;
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
String IdAct=F.getIdPath(getParam3());
return(F.Search(FoldType,  Conds, SubTypes, true, IdAct, null))  ;
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
PDFolders F=new PDFolders(getDrv());
String IdAct=F.getIdPath(getParam3());
return(Doc.Search(DocType, Conds, SubTypes, true, false, IdAct, null))  ;
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
 * Generates the default conditions that is PDID=current PDID
 * @return Conditions containing 1 condition
 * @throws PDException
 */
protected Conditions getConditions() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Generates a Task from the definition and the actual Folder
 * @param pTask Taks definition
 * @param pFold Folder
 */
void GenFromDef(PDTasksDefEvent pTask, PDFolders pFold) throws PDException
{
super.assignValues(pTask.getRecord());
setPDId(GenerateId());
this.setObjFilter(pFold.getPDId());
this.setObjType(pFold.getFolderType());
}
//-------------------------------------------------------------------------
/**
 * Generates a Task from the definition and the actual Folder
 * @param pTask Taks definition
 * @param pDoc Folder
 */
void GenFromDef(PDTasksDefEvent pTask, PDDocs pDoc) throws PDException
{
super.assignValues(pTask.getRecord());
setPDId(GenerateId());
this.setObjFilter(pDoc.getPDId());
this.setObjType(pDoc.getDocType());
}
//-------------------------------------------------------------------------
private void ExecuteUpdFold() throws PDException
{
PDFolders Fold=new PDFolders(this.getDrv(), getObjType());
Fold.LoadFull(getObjFilter());
String IdUnder=Fold.getIdPath(getParam4());
if (!Fold.IsUnder(IdUnder))    
   return; 
Record r=Fold.getRecSum();
r=Update(getParam(), r);
if (getParam2()!=null && getParam2().length()!=0)
    r=Update(getParam2(), r);
if (getParam3()!=null && getParam2().length()!=0)
    r=Update(getParam3(), r);
Fold.assignValues(r);
Fold.MonoUpdate();
}
//-------------------------------------------------------------------------

private void ExecuteCopyFold() throws PDException
{
PDFolders Fold=new PDFolders(this.getDrv(), getObjType());
Fold.LoadFull(getObjFilter());
if (Fold.getIdPath(getParam()).equals(Fold.getParentId())) // to avoid "recursivity"
    return;
String IdUnder=Fold.getIdPath(getParam2());
if (!Fold.IsUnder(IdUnder))    
   return; 
PDFolders f=new PDFolders(Fold.getDrv(), Fold.getFolderType());
f.assignValues(Fold.getRecSum());
f.setPDId(null);
f.setParentId(Fold.getIdPath(getParam()));
f.insert();
}
//-------------------------------------------------------------------------

private void ExecuteExportFold() throws PDException
{
PDFolders Fold=new PDFolders(this.getDrv(), getObjType());
Fold.LoadFull(getObjFilter());
String IdUnder=Fold.getIdPath(getParam());
if (!Fold.IsUnder(IdUnder))    
   return; 
try {
Fold.ExportPath(Fold.getPDId(), getParam2());
} catch (Exception ex)
    {
    PDException.GenPDException("Error_Exporting_Folder", ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
}
