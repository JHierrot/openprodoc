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

/**
 * 
 * @author jhierrot
 */
public class PDTasksDefEvent extends PDTasksDef
{

public static final String fEVENTYPE="EvenType";
public static final String fEVENORDER="EvenOrder";

private String EvenType;
private int EvenOrder;

public static final String fMODEINS="INS";
public static final String fMODEUPD="UPD";
public static final String fMODEDEL="DEL";

public static final int STARTNUM=200;


public static final int fTASKEVENT_UPDATE_DOC =STARTNUM+0;
public static final int fTASKEVENT_UPDATE_FOLD=STARTNUM+1;
public static final int fTASKEVENT_COPY_DOC   =STARTNUM+2;
public static final int fTASKEVENT_COPY_FOLD  =STARTNUM+3;
public static final int fTASKEVENT_EXPORT_DOC =STARTNUM+4;
public static final int fTASKEVENT_EXPORT_FOLD=STARTNUM+5;
public static final int fTASKEVENT_CONVERT_DOC=STARTNUM+6;

private static final String[] LisTypeEventTask= {"UPDATE_DOC",
                                                 "UPDATE_FOLD",
                                                 "COPY_DOC",
                                                 "COPY_FOLD",
                                                 "EXPORT_DOC",
                                                 "EXPORT_FOLD",
                                                 "CONVERT_DOC"
                                                 };
/**
 *
 */
static private Record TaksTypeStruct=null;

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
setEvenType((String) Rec.getAttr(fEVENTYPE).getValue());
if (Rec.getAttr(fEVENORDER).getValue()==null)
    setEvenOrder(1);
else    
    setEvenOrder((Integer) Rec.getAttr(fEVENORDER).getValue());
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
Rec.getAttr(fEVENTYPE).setValue(getEvenType());
Rec.getAttr(fEVENORDER).setValue(getEvenOrder());
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
return ("PD_TASKSDEFEVEN");
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
    R.getAttr(fOBJTYPE).setUnique(true);  // DocType+EventType+Order=unique
    R.addAttr( new Attribute(fEVENTYPE, fEVENTYPE, "Type of event (INS, DEL, UP..)", Attribute.tSTRING, true, null, 32, false, true, true));
    R.addAttr( new Attribute(fEVENORDER, fEVENORDER, "Order of Execution after event", Attribute.tINTEGER, true, null, 8, false, true, true));
    return(R);
    }
else
    return(TaksTypeStruct);
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
    TaksDefObjectsCache=new ObjectsCache("TasksDefEven");
return(TaksDefObjectsCache);    
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
 * @return the EvenType
 */
public String getEvenType()
{
return EvenType;
}
//-------------------------------------------------------------------------
/**
 * @param EvenType the EvenType to set
 */
public void setEvenType(String EvenType)
{
this.EvenType = EvenType;
}
//-------------------------------------------------------------------------
/**
* @return the EvenOrder
*/
public int getEvenOrder()
{
return EvenOrder;
}
//-------------------------------------------------------------------------
/**
* @param EvenOrder the EvenOrder to set
*/
public void setEvenOrder(int EvenOrder)
{
this.EvenOrder = EvenOrder;
}
//-------------------------------------------------------------------------
static public String[] getListTypeEventTask()
{
return LisTypeEventTask;    
}
//-------------------------------------------------------------------------
protected void Execute(PDFolders Fold) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.Execute-Fold>:"+Fold.getPDId()+"/"+Fold.getTitle());                                
switch (this.getType())
    {case fTASKEVENT_UPDATE_FOLD:
        ExecuteUpdFold(Fold);
        break;
     case fTASKEVENT_COPY_FOLD:
        ExecuteCopyFold(Fold);
        break;
     case fTASKEVENT_EXPORT_FOLD:
        ExecuteExportFold(Fold);
        break;
     default:
         PDException.GenPDException("Unexpected_Task", "Type"+getType());
         break;
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.Execute-Fold<");                                
}
//-------------------------------------------------------------------------
protected void Execute(PDDocs Doc) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.Execute-Doc>:"+Doc.getPDId()+"/"+Doc.getTitle());                            
switch (this.getType())
    {
    case fTASKEVENT_UPDATE_DOC:
        ExecuteUpdDoc(Doc);
        break;
     case fTASKEVENT_COPY_DOC:
        ExecuteCopyDoc(Doc);
        break;
     case fTASKEVENT_EXPORT_DOC:
        ExecuteExportDoc(Doc);
        break;
     case fTASKEVENT_CONVERT_DOC:
        ExecuteConvertDoc(Doc);
        break;
     default:
         PDException.GenPDException("Unexpected_Task", "Type"+getType());
         break;
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.Execute-Doc<");                            
}
//-------------------------------------------------------------------------
static boolean isFolder(int TaskType)
{
if (TaskType==fTASKEVENT_UPDATE_FOLD || TaskType==fTASKEVENT_COPY_FOLD)
    return(true);
else
    return(false);
}
//-------------------------------------------------------------------------    
/**
 * Updates Attributes of a Folder
 * @param Fold Folder to Update
 * @throws PDException in any error
 */
private void ExecuteUpdFold(PDFolders Fold) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteUpdFold>:"+Fold.getPDId()+"/"+Fold.getTitle());    
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
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteUpdFold<");    
}
//-------------------------------------------------------------------------    
/**
 * Copy a fold in destination folder 
 * @param Fold Folder to be copied
 * @throws PDException in any error
 */
private void ExecuteCopyFold(PDFolders Fold) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteCopyFold>:"+Fold.getPDId()+"/"+Fold.getTitle());        
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
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteCopyFold<");    
}
//-------------------------------------------------------------------------    
/**
 * Export a fold in destination folder 
 * @param Fold Folder to be exported
 * @throws PDException in any error
 */
private void ExecuteExportFold(PDFolders Fold) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteExportFold>:"+Fold.getPDId()+"/"+Fold.getTitle());    
String IdUnder=Fold.getIdPath(getParam());
if (!Fold.IsUnder(IdUnder))    
   return; 
try {
Fold.ExportPath(Fold.getPDId(), getParam2());
} catch (Exception ex)
    {
    PDException.GenPDException("Error_Exporting_Folder", ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteExportFold<");    
}
//-------------------------------------------------------------------------    
/**
 * Updates Doc metadata
 * @param Doc Current Doc
 * @throws PDException in any error
 */
private void ExecuteUpdDoc(PDDocs Doc) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteUpdDoc>:"+Doc.getPDId()+"/"+Doc.getTitle());            
PDFolders Fold=new PDFolders(getDrv());
String IdUnder=Fold.getIdPath(getParam4());
Fold.setPDId(Doc.getParentId());
if (!Fold.IsUnder(IdUnder))    
   return; 
Record r=Doc.getRecSum();
r=Update(getParam(), r);
if (getParam2()!=null && getParam2().length()!=0)
    r=Update(getParam2(), r);
if (getParam3()!=null && getParam2().length()!=0)
    r=Update(getParam3(), r);
Doc.assignValues(r);
Doc.updateFragments(r, Doc.getPDId());
Doc.UpdateVersion(Doc.getPDId(), Doc.getVersion(), r);
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteUpdDoc<");            
}
//-------------------------------------------------------------------------  
/**
 * Creates a copy of the doc
 * @param Doc Current Doc
 * @throws PDException in any error
 */
private void ExecuteCopyDoc(PDDocs Doc) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteCopyDoc>:"+Doc.getPDId()+"/"+Doc.getTitle());                
PDFolders Fold=new PDFolders(getDrv());
String IdUnder=Fold.getIdPath(getParam2());
Fold.setPDId(Doc.getParentId());
if (!Fold.IsUnder(IdUnder))    
   return;     
String FName=null;    
PDDocs NewDoc=new PDDocs(getDrv(), Doc.getDocType());
NewDoc.assignValues(Doc.getRecSum());
NewDoc.setPDId(Doc.GenerateId());
try {
FName=Doc.getFile(System.getProperty("java.io.tmpdir"));
NewDoc.setFile(FName);
NewDoc.setParentId(Fold.getIdPath(getParam()));
NewDoc.insert();
} catch (PDException Ex)
    {
    PDException.GenPDException(Ex.getLocalizedMessage(), FName);
    }
finally {
    if (FName!=null)
        {
        File f=new File(FName);
        if (f.exists())
            f.delete();
        }
    }
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteCopyDoc<");                
}
//-------------------------------------------------------------------------   
/**
 * Implements the event Export a document 
 * @param Doc Document to be exported
 * @throws PDException in any error
 */
private void ExecuteExportDoc(PDDocs Doc) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteExportDoc>:"+Doc.getPDId()+"/"+Doc.getTitle());                    
PDFolders Fold=new PDFolders(getDrv());
String IdUnder=Fold.getIdPath(getParam());
Fold.setPDId(Doc.getParentId());
if (!Fold.IsUnder(IdUnder))    
   return;  
Doc.ExportXML(getParam2(), true);
Doc.getFile(getParam2());
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteExportDoc<");                    
}
//-------------------------------------------------------------------------    
/**
 * 
 * @param Doc
 * @throws PDException 
 */
@SuppressWarnings("SleepWhileInLoop")
private void ExecuteConvertDoc(PDDocs Doc) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteConvertDoc>:"+Doc.getPDId()+"/"+Doc.getTitle());                        
PDFolders Fold=new PDFolders(getDrv());
String IdUnder=Fold.getIdPath(getParam2());
Fold.setPDId(Doc.getParentId());
if (!Fold.IsUnder(IdUnder))    
   return;     
String FName=null;    
String DestName=null;
PDDocs NewDoc=new PDDocs(getDrv(), Doc.getDocType());
NewDoc.assignValues(Doc.getRecSum());
NewDoc.setPDId(Doc.GenerateId());
try {
FName=Doc.getFile(System.getProperty("java.io.tmpdir"));
String Order=getParam3();
Order=Order.replace("@1", FName);
DestName=FName.substring(0, FName.lastIndexOf('.')+1)+getParam4();
Order=Order.replace("@2", DestName);
Process Proc=Runtime.getRuntime().exec(Order);
File f=new File(DestName);
int Res;
for (int i = 0; i < 30; i++)
    {
    Thread.sleep(1000); 
    try {
    Res=Proc.exitValue();
    } catch (IllegalThreadStateException e)
        {
        Res=-1111;
        }
//    if (!Proc.isAlive())  // deprecated in some Java versions
    if (Res!=1111)
        break;
    }
NewDoc.setName("");
NewDoc.setMimeType(null);
NewDoc.setFile(DestName);
NewDoc.setParentId(Fold.getIdPath(getParam()));
NewDoc.insert();
} catch (Exception Ex)
    {
    PDException.GenPDException(Ex.getLocalizedMessage(), FName);
    }
finally 
{
if (FName!=null)
    {
    File f=new File(FName);
    if (f.exists())
        f.delete();
    }
if (DestName!=null)
    {
    File f=new File(DestName);
    if (f.exists())
        f.delete();
    }
}
if (PDLog.isDebug())
    PDLog.Debug("PDTasksDefEvent.ExecuteConvertDoc<");                        
}
//-------------------------------------------------------------------------
@Override
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))    
    if (!getDrv().getUser().getRol().isAllowCreateTask())
       PDExceptionFunc.GenPDException("Operation_do_not_allowed",getName());
}
//-------------------------------------------------------------------------
@Override
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainTask() )
   PDExceptionFunc.GenPDException("Operation_do_not_allowed",getName());
}
//-------------------------------------------------------------------------
@Override
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainTask() )
   PDExceptionFunc.GenPDException("Operation_do_not_allowed",getName());
}
//-------------------------------------------------------------------------
}
