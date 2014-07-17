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
import java.io.PrintWriter;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jhierrot
 */
public class PDFolders extends ObjPD
{
/**
 *
 */
public static final String fPDID="PDId";
/**
 *
 */
public static final String fTITLE="Title";
/**
 *
 */
public static final String fACL="ACL";
/**
 *
 */
public static final String fFOLDTYPE="FolderType";
/**
 *
 */
public static final String fPARENTID="ParentId";
/**
 *
 */
public static final String fGRANTPARENTID="GrantParentId";
/**
 *
 */
public static final String ROOTFOLDER="RootFolder";
/**
 *
 */
public static final String USERSFOLDER="Users";
/**
 *
 */
static private Record FoldersStruct=null;
/**
 *
 */
static private Record FoldersLevStruct=null;
/**
 *
 */
private String PDId=null;
/**
 *
 */
private String Title=null;
/**
 *
 */
private String ACL=null;
/**
 *
 */
private String FolderType=null;
/**
 *
 */
private String ParentId=null;
/**
 *
 */
private boolean IsRootFolder=false;
/**
 *
 */
private ArrayList TypeDefs=null;
/**
 *
 */
private ArrayList TypeRecs=null;
/**
 *
 */
private Record RecSum=null;

static private ObjectsCache FoldObjectsCache = null;

static private final String NotAllowedChars="/\\:*?";

//-------------------------------------------------------------------------
/**
 *
 * @param Drv
 * @throws PDException
 */
public PDFolders(DriverGeneric Drv) throws PDException
{
super(Drv);
setFolderType(getTableName());
}
//-------------------------------------------------------------------------
/**
 *
 * @param Drv
 * @param pFoldType
 * @throws PDException
 */
public PDFolders(DriverGeneric Drv, String pFoldType) throws PDException
{
super(Drv);
setFolderType(pFoldType);
getTypeDefs();
}
//-------------------------------------------------------------------------
@Override
public void assignValues(Record Rec) throws PDException
{
setPDId((String) Rec.getAttr(fPDID).getValue());
setTitle((String) Rec.getAttr(fTITLE).getValue());
setACL((String) Rec.getAttr(fACL).getValue());
if (FolderType==null || ! FolderType.equals(Rec.getAttr(fFOLDTYPE).getValue()))
    setFolderType((String) Rec.getAttr(fFOLDTYPE).getValue());
setParentId((String) Rec.getAttr(fPARENTID).getValue());
assignCommonValues(Rec);
getRecSum().assign(Rec);
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
 * @param pPDId
 * @throws PDExceptionFunc  
 */
public void setPDId(String pPDId) throws PDExceptionFunc
{
this.PDId = pPDId;
}
//-------------------------------------------------------------------------
/**
* @return the Title
*/
public String getTitle()
{
return Title;
}
//-------------------------------------------------------------------------
/**
 * @param pTitle
 * @throws PDExceptionFunc  
*/
public void setTitle(String pTitle) throws PDExceptionFunc
{
if (pTitle==null)    
    return;
pTitle=pTitle.trim();
if (pTitle.length()==0)   
    PDExceptionFunc.GenPDException("Empty_Name_not_allowed",pTitle);
if (pTitle.length()>254)   
    PDExceptionFunc.GenPDException("Name_longer_than_allowed",pTitle);
for (int i=0; i<pTitle.length(); i++)
    {
    if (! (NotAllowedChars.indexOf(pTitle.charAt(i))==-1))
       PDExceptionFunc.GenPDException("Character_not_included_in_the_allowed_set",AllowedChars);
    }
this.Title = pTitle;

}
//-------------------------------------------------------------------------
/**
 * object "method" needed because static overloading doesn't work in java
 * @return
 */
@Override
public String getTabName()
{
return(getTableName());
}
//-------------------------------------------------------------------------
/**
 * static equivalent method
 * @return
 */
static public String getTableName()
{
return("PD_FOLDERS");
}
//-------------------------------------------------------------------------
/**
 * static method
 * @return
 */
static public String getTableNameFoldLev()
{
return("PD_FOLD_LEV");
}
//-------------------------------------------------------------------------
/**
 * The install method is generic becaus for instantiate a object, the class
 * need to access to the tables for definition
 * @param Drv
 * @throws PDException
 */
static public void Install(DriverGeneric Drv) throws PDException
{
Drv.CreateTable(getTableName(), getRecordStructPDFolder());
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
@Override
synchronized public Record getRecord() throws PDException
{
return(getRecSum());
}
//-------------------------------------------------------------------------
@Override
protected Record getRecordStruct() throws PDException
{
return( getRecSum().Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure
 * @return
 * @throws PDException
 */
static public Record getRecordStructPDFolder() throws PDException
{
if (FoldersStruct==null)
    FoldersStruct=CreateRecordStructPDFolder();
return(FoldersStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDFolder() throws PDException
{
if (FoldersStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDID, "PDID","Unique_identifier", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fTITLE, "Folder_Title","Folder_Title", Attribute.tSTRING, true, null, 254, false, true, true));
    R.addAttr( new Attribute(fACL, "Folder_ACL", "Folder_ACL", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fFOLDTYPE, "Folder_Type", "Folder_Type", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fPARENTID, "Parent_Folder","Parent_Folder", Attribute.tSTRING, true, null, 32, false, true, false));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(FoldersStruct);
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of folders
 * @return
 * @throws PDException
 */
static protected Record getRecordStructPDFolderLev() throws PDException
{
if (FoldersLevStruct==null)
    FoldersLevStruct=CreateRecordStructPDFolderLev();
return(FoldersLevStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of folders
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDFolderLev() throws PDException
{
if (FoldersLevStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fGRANTPARENTID,"GRANTPARENTID", "Identificador único sucesor", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPDID, "PDID", "Identificador único antecesor", Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(FoldersLevStruct);
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected Conditions getConditions() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ListCond.addCondition(CondAcl);
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Builds the default conditions identifying the folder: Id="id"
 * @return List of conditions
 * @throws PDException
 */
protected Conditions getConditionsMaint() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
return(ListCond);
}
//-------------------------------------------------------------------------
protected Conditions getConditionsLike(String Name) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cLIKE, VerifyWildCards(Name)));
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ListCond.addCondition(CondAcl);
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @throws PDExceptionFunc  
 */
protected void AsignKey(String Ident) throws PDExceptionFunc
{
setPDId(Ident);
}
/**
* @return the ACL
*/
public String getACL()
{
return ACL;
}

/**
 * @param pACL
*/
public void setACL(String pACL)
{
this.ACL = pACL;
}

/**
* @return the FolderType
*/
public String getFolderType()
{
return FolderType;
}

/**
 * @param pFolderType
 */
public void setFolderType(String pFolderType)
{
this.FolderType = pFolderType;
//RecSum.getAttr(fFOLDTYPE).setValue(pFolderType);
this.TypeDefs=null;
this.TypeRecs=null;
}
//-------------------------------------------------------------------------
/**
* The install method is generic becaus for instantiate a object, the class
* need to access to the tables for definition
 * @param Drv
 * @throws PDException
*/
static protected void InstallMulti(DriverGeneric Drv) throws PDException
{
Drv.AddIntegrity(getTableName(), fACL,      PDACL.getTableName(),     PDACL.fNAME);
Drv.AddIntegrity(getTableName(), fFOLDTYPE, PDObjDefs.getTableName(), PDObjDefs.fNAME);
Drv.AddIntegrity(getTableName(), fPARENTID, getTableName(),           fPDID);
Drv.CreateTable(getTableNameFoldLev(), getRecordStructPDFolderLev());
Drv.AddIntegrity(getTableNameFoldLev(), fPDID,          getTableName(), fPDID);
Drv.AddIntegrity(getTableNameFoldLev(), fGRANTPARENTID, getTableName(), fPDID);
}
/**
 *
 * @throws PDException
 */
public void insert() throws PDException
{
boolean InTransLocal;
VerifyAllowedIns();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
if (getPDId()==null || getPDId().length()==0)
    setPDId(GenerateId());
else if (!IsRootFolder && getParentId().equalsIgnoreCase(getPDId()) && !getPDId().equalsIgnoreCase(ROOTFOLDER))
       PDException.GenPDException("Parent_folder_equals_to_actual_folder", getParentId());
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.insert:"+getPDId());
if (IsRootFolder)
    {
    setParentId(ROOTFOLDER);
    if (PDLog.isDebug())
        PDLog.Debug("PDFolders.insert root folder");
    }
else
    { if (!(getDrv().getUser().getName().equals("Install") && getDrv().getUser().getAclList()==null))
        {
        PDFolders Parent=new PDFolders(getDrv());
        Parent.Load(getParentId());
        if (!getDrv().getUser().getAclList().containsKey(Parent.getACL()))
           PDExceptionFunc.GenPDException("User_without_permissions_on_container_folder", getParentId());
        Integer Perm=(Integer)getDrv().getUser().getAclList().get(Parent.getACL());
        if (Perm.intValue()<=PDACL.pREAD)
           PDExceptionFunc.GenPDException("User_without_permissions_on_container_folder", getParentId());
        if (getACL()==null || getACL().equals(""))
            setACL(Parent.getACL());
        Parent.update();
        }
    }
AddLogFields();
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=(Record)getTypeDefs().get(i);
    Record DatParc=(Record)getTypeRecs().get(i);
    if (i!=getTypeDefs().size()-1)
        {
        DatParc.addAttr(getRecSum().getAttr(fPDID));
        }
    DatParc.assign(getRecSum().CopyMono());
    getDrv().InsertRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc);
    }
MultiInsert(getRecSum());
if (!IsRootFolder)
    ActFoldLev();
ExecuteTransThreads(PDTasksDefEvent.fMODEINS);
GenerateNoTransThreads(PDTasksDefEvent.fMODEINS);
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_creating_folder",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.Insert<");
}
//-------------------------------------------------------------------------
/**
 * Inserts all the multivalued attributed during the Insert
 * @param Rec Record with the sum of attributes
 * @throws PDException in any error
 */
private void MultiInsert(Record Rec) throws PDException
{
Record TypDef;    
String MultiName;
Attribute AtrOrig, Atr2Ins;
TreeSet Values;
Record RecSave=new Record();
Object Val2Ins;
for (int NumDefTyp = 0; NumDefTyp<getTypeDefs().size(); NumDefTyp++)
    {
    TypDef=((Record)getTypeRecs().get(NumDefTyp)).CopyMulti();
    String TabName=(String) ((Record)getTypeDefs().get(NumDefTyp)).getAttr(PDObjDefs.fNAME).getValue();
    TypDef.initList();
    for (int NumAttr = 0; NumAttr < TypDef.NumAttr(); NumAttr++)
        {
        AtrOrig=TypDef.nextAttr();
        Atr2Ins=Rec.getAttr(AtrOrig.getName());
        if (Atr2Ins==null || Atr2Ins.getValuesList()==null || Atr2Ins.getValuesList().isEmpty())
           continue;
        AtrOrig=Atr2Ins;
        Values=AtrOrig.getValuesList();
        MultiName=PDObjDefs.genMultValNam(TabName,AtrOrig.getName());
        RecSave.Clear();
        RecSave.addAttr(Rec.getAttr(fPDID));
        Atr2Ins=AtrOrig.Copy();
        Atr2Ins.ClearValues();
        Atr2Ins.setMultivalued(false);
        RecSave.addAttr(Atr2Ins);
        for (Iterator it = Values.iterator(); it.hasNext();)
            {
            Val2Ins = it.next();
            //Atr2Ins.setValue(Val2Ins);
            RecSave.getAttr(Atr2Ins.getName()).setValue(Val2Ins);
            getDrv().InsertRecord(MultiName, RecSave);    
            }
        }
     }
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))  
if (!getDrv().getUser().getRol().isAllowCreateFolder() )
   PDExceptionFunc.GenPDException("Folder_creation_not_allowed_to_user", null);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
    if (!getDrv().getUser().getRol().isAllowMaintainFolder() )
       PDExceptionFunc.GenPDException("Folder_delete_not_allowed_to_user", null);
    PDFolders TobeDeleted=new PDFolders(getDrv());
    TobeDeleted.Load(getPDId());
    if (!getDrv().getUser().getAclList().containsKey(TobeDeleted.getACL()))
       PDExceptionFunc.GenPDException("User_without_permissions_over_folder", null);
    Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeDeleted.getACL());
    if (Perm.intValue()!=PDACL.pDELETE)
       PDExceptionFunc.GenPDException("User_without_permissions_over_folder", null);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainFolder() )
   PDExceptionFunc.GenPDException("Folder_update_not_allowed_to_user", null);
PDFolders TobeUpdated=new PDFolders(getDrv());
TobeUpdated.Load(getPDId());
if (!getDrv().getUser().getAclList().containsKey(TobeUpdated.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_over_folder", null);
Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeUpdated.getACL());
if (Perm.intValue()<=PDACL.pREAD)
   PDExceptionFunc.GenPDException("User_without_permissions_over_folder", null);
}
//-------------------------------------------------------------------------
/**
* @return the ParentId
*/
public String getParentId()
{
return ParentId;
}
//-------------------------------------------------------------------------
/**
 * @param pParentId
 */
public void setParentId(String pParentId)
{
this.ParentId = pParentId;
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
public void CreateRootFolder() throws PDException
{
IsRootFolder=true;
insert();
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
public String GenerateId()
{
StringBuffer genId = new StringBuffer();
genId.append(Long.toHexString(System.currentTimeMillis()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
return genId.toString();
}
//-------------------------------------------------------------------------
/**
 *
 * @param tableName
 * @throws PDException
 */
private void LoadDef(String tableName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.LoadDef");
setFolderType(tableName);
TypeDefs=new ArrayList();
TypeRecs=new ArrayList();
getDrv().LoadDef(tableName, getTypeDefs(), getTypeRecs());
RecSum=new Record();
for (int i = 0; i < getTypeRecs().size(); i++)
    {
    RecSum.addRecord((Record)getTypeRecs().get(i));
    }
RecSum.getAttr(fFOLDTYPE).setValue(getFolderType());
}
//-------------------------------------------------------------------------
/** Return an ordered list of types
 * @return the TypeDefs
 * @throws PDException
*/
public ArrayList getTypeDefs() throws PDException
{
if (TypeDefs==null)
    LoadDef(getFolderType());
return TypeDefs;
}
//-------------------------------------------------------------------------
/**
 * @return the TypeRecs
 * @throws PDException
*/
public ArrayList getTypeRecs() throws PDException
{
if (TypeRecs==null)
    LoadDef(getFolderType());
return TypeRecs;
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
private void ActFoldLev() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.ActFoldLev");
HashSet GrandParentList=getListGrandParentList(getParentId());
GrandParentList.add(getParentId());
Record RecFL=getRecordStructPDFolderLev();
RecFL.getAttr(fPDID).setValue(getPDId());
for (Iterator it = GrandParentList.iterator(); it.hasNext();)
    {
    Object GP = it.next();
    RecFL.getAttr(fGRANTPARENTID).setValue((String)GP);
    getDrv().InsertRecord(getTableNameFoldLev(), RecFL);
    }
}
//-------------------------------------------------------------------------
/**
 *
 * @param Parent
 * @return a Set containing the parents of Parent
 * @throws PDException
 */
public HashSet getListGrandParentList(String Parent) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListGrandParentList>:"+Parent);
HashSet Result=new HashSet();
Condition CondParents=new Condition( fPDID, Condition.cEQUAL, Parent);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTableNameFoldLev(), getRecordStructPDFolderLev(), Conds);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Acl=(String) Res.getAttr(fGRANTPARENTID).getValue();
    Result.add(Acl);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListGrandParentList<:"+Parent);
return(Result);
}
//-------------------------------------------------------------------------
/**
 *
 * @param PDId
 * @return a Set containing the parents of Parent
 * @throws PDException
 */
public HashSet getListDescendList(String PDId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDescendList>:"+PDId);
HashSet Result=new HashSet();
Condition CondParents=new Condition( fGRANTPARENTID, Condition.cEQUAL, PDId);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTableNameFoldLev(), getRecordStructPDFolderLev(), Conds);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Acl=(String) Res.getAttr(fPDID).getValue();
    Result.add(Acl);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDescendList<:"+PDId);
return(Result);
}
//-------------------------------------------------------------------------
/**
 * @return the RecSum
 * @throws PDException
*/
public Record getRecSum() throws PDException
{
if (RecSum==null)
   LoadDef(getFolderType());
RecSum.getAttr(fPDID).setValue(getPDId());
RecSum.getAttr(fTITLE).setValue(getTitle());
RecSum.getAttr(fPDDATE).setValue(getPDDate());
RecSum.getAttr(fACL).setValue(getACL());
RecSum.getAttr(fFOLDTYPE).setValue(getFolderType());
RecSum.getAttr(fPARENTID).setValue(getParentId());
getCommonValues(RecSum);
return RecSum;
}
//-------------------------------------------------------------------------
/**
 * Deletes the folder and all the documents and subfolders contained recursively
 * If the number of folders, documents and levels is too big, this method can 
 * create problems in the rollback of the database storing the metadata
 * @throws PDException in any problem and cancels the Transaction. When enough permissions throws PDFuncException.
 */
@Override
public void delete() throws PDException
{
if (PDLog.isInfo())
    PDLog.Debug("PDFolders.delete>:"+getPDId());
boolean InTransLocal;
Load(getPDId());
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
ExecuteTransThreads(PDTasksDefEvent.fMODEDEL);
DeleteFoldersInFolder();
DeleteDocsInFolder();
DeleteFoldLevelParents();
MultiDelete(getPDId());
DelFoldMetadata();
GenerateNoTransThreads(PDTasksDefEvent.fMODEDEL);
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    Ex.printStackTrace();    
    getDrv().AnularTrans();
    PDException.GenPDException("Error_deleting_folder",Ex.getLocalizedMessage());
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.delete<:"+getPDId());
}
//-------------------------------------------------------------------------
/**
 * Deletes all the multivalued atributes of the current element
 * @param Id2Del PDId of document to delete
 * @param Vers  Version of Document to delete. When null, deletes ALL versions
 * @throws PDException
 */
private void MultiDelete(String Id2Del) throws PDException
{
Record TypDef;    
String MultiName;
Attribute Atr;
Conditions Conds;
for (int NumDefTyp = 0; NumDefTyp<getTypeDefs().size(); NumDefTyp++)
    {
    TypDef=((Record)getTypeRecs().get(NumDefTyp)).CopyMulti();
    String TabName=(String)((Record)getTypeDefs().get(NumDefTyp)).getAttr(PDObjDefs.fNAME).getValue();
    TypDef.initList();
    for (int NumAttr = 0; NumAttr < TypDef.NumAttr(); NumAttr++)
        {
        Atr=TypDef.nextAttr();
        MultiName=PDObjDefs.genMultValNam(TabName,Atr.getName());
        Conds=new Conditions();
        Conds.addCondition(new Condition(fPDID, Condition.cEQUAL, Id2Del));
        getDrv().DeleteRecord(MultiName, Conds);
        }
     }
}
//-------------------------------------------------------------------------
/**
 * Deletes all the docs in actual folder. 
 * Used as part of folder delete
 */
private void DeleteDocsInFolder() throws PDException
{   
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.DeleteDocsInFolder:"+getPDId());     
Conditions Conds=new Conditions();
Condition CondChilds=new Condition(PDDocs.fPARENTID, Condition.cEQUAL, getPDId());
Conds.addCondition(CondChilds);
Query Q=new Query(PDDocs.getTableName(), PDDocs.getRecordStructPDDocs(), Conds);   
Cursor ListDocsContained=getDrv().OpenCursor(Q);
Record NextDoc=getDrv().NextRec(ListDocsContained);
PDDocs Doc2del=new PDDocs(getDrv());
while (NextDoc!=null)
    {
    Doc2del.assignValues(NextDoc);  
    Doc2del.delete();
    NextDoc=getDrv().NextRec(ListDocsContained);
    }
getDrv().CloseCursor(ListDocsContained);
}
//-------------------------------------------------------------------------
/**
 * Deletes all the Folders in actual folder. 
 * Used as part of folder delete
 */
private void DeleteFoldersInFolder() throws PDException
{   
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.DeleteFoldersInFolder:"+getPDId());     
Conditions Conds=new Conditions();
Condition CondChilds=new Condition(PDFolders.fPARENTID, Condition.cEQUAL, getPDId());
Conds.addCondition(CondChilds);
Query Q=new Query(PDFolders.getTableName(), PDFolders.getRecordStructPDFolder(), Conds);   
Cursor ListFoldersContained=getDrv().OpenCursor(Q);
Record NextFold=getDrv().NextRec(ListFoldersContained);
PDFolders Fold2del=new PDFolders(getDrv());
while (NextFold!=null)
    {
    Fold2del.assignValues(NextFold);  
    Fold2del.delete();
    NextFold=getDrv().NextRec(ListFoldersContained);
    }
getDrv().CloseCursor(ListFoldersContained);
}
//-------------------------------------------------------------------------
private void DelFoldMetadata() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.DelFoldMetadata:"+getPDId());  
for (int i = 0; i < getTypeDefs().size(); i++)
    {
    Record TypDef=(Record)getTypeDefs().get(i);
//    Record DatParc=(Record)F.getTypeRecs().get(i);
//    DatParc.assign(this.getRecSum());
    getDrv().DeleteRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), getConditionsMaint());
    }
}
//-------------------------------------------------------------------------
/**
 * Deletes all the references in upper levels to this folder
 * @throws PDException
 */
private void DeleteFoldLevelParents()  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.DeleteFoldLevelParents:"+getPDId());    
Condition CondChilds=new Condition( fPDID, Condition.cEQUAL, getPDId());
Conditions Conds=new Conditions();
Conds.addCondition(CondChilds);
getDrv().DeleteRecord(getTableNameFoldLev(), Conds);
}
//-------------------------------------------------------------------------
/**
 * Updates the metadata of the folder
 * @throws PDException
 */
@Override
public void update()  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.update:"+getPDId());
boolean InTransLocal;
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
AddLogFields();
Record R=getRecord().Copy();
R.delAttr(fPARENTID);
R.delAttr(fFOLDTYPE);
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=(Record)getTypeDefs().get(i);
    Record DatParc=(Record)getTypeRecs().get(i);
    if (i!=getTypeDefs().size()-1)
        {
        DatParc.addAttr(getRecSum().getAttr(fPDID));
        }
    DatParc.assign(this.getRecSum());
    if (PDLog.isDebug())
        PDLog.Debug("PDFolders.update2:"+(String)TypDef.getAttr(PDObjDefs.fNAME).getValue()+ ">"+DatParc);
    if (DatParc.NumAttrFilled()>1) // Id + some value
        getDrv().UpdateRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc, getConditionsMaint());
    }
MultiDelete(this.getPDId());
MultiInsert(R);
ExecuteTransThreads(PDTasksDefEvent.fMODEUPD);
GenerateNoTransThreads(PDTasksDefEvent.fMODEUPD);
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_updating_folder",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
}
//-------------------------------------------------------------------------
/**
 * Creates the main folder used by all the elements as "infraestructure"
 * @param Drv to be used (is a "prefolder")
 * @throws PDException
 */
protected static void CreateBaseRootFold(DriverGeneric Drv) throws PDException
{
PDFolders f=new PDFolders(Drv);
f.setFolderType(getTableName());
f.setPDId(ROOTFOLDER);
f.setTitle(ROOTFOLDER);
f.setParentId(ROOTFOLDER);
f.setACL("Public");
f.CreateRootFolder();
PDFolders f2=new PDFolders(Drv);
f2.setFolderType(getTableName());
f2.setPDId(USERSFOLDER);
f2.setTitle(USERSFOLDER);
f2.setParentId(ROOTFOLDER);
f2.setACL("Public");
f2.insert();
}
//-------------------------------------------------------------------------
/**
 * Return a list of all the folders of any type whose DIRECT parent is PDId
 * @param PDId Id of the parent folder
 * @return a hashset with all the DirectDescend
 * @throws PDException in any error
 */
public HashSet getListDirectDescendList(String PDId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDirectDescendList>:"+PDId);
LinkedHashSet Result=new LinkedHashSet();
Condition CondParents=new Condition( fPARENTID, Condition.cEQUAL, PDId);
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Conds.addCondition(CondAcl);
Query Q=new Query(getTabName(), getRecordStructPDFolder(), Conds, fTITLE);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Acl=(String) Res.getAttr(fPDID).getValue();
    Result.add(Acl);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDirectDescendList<:"+PDId);
return(Result);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @return
 * @throws PDException
 */
public Record Load(String Ident)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.Load:"+Ident);
AsignKey(Ident);
Record r=(Record)getObjCache().get(Ident);
if (r==null) 
    {
    Query LoadAct=new Query(getTabName(), getRecordStructPDFolder(),getConditions());
    Cursor Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    getObjCache().put(Ident, r);
    }
if (r!=null)
    assignValues(r);
return(r);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @return
 * @throws PDException
 */
public Record LoadFull(String Ident)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.LoadFull:"+Ident);
Record r=Load(Ident);
if (getTypeDefs().size()>1)
    {
    Conditions Conds=getConditions();
    Vector ListTabs=new Vector();
    for (int i = 0; i<getTypeDefs().size(); i++)
        {
        Record TypDef=(Record)getTypeDefs().get(i);
        ListTabs.add((String)TypDef.getAttr(PDObjDefs.fNAME).getValue());
        if (! ((String)ListTabs.elementAt(i)).equals(PDFolders.getTableName()))
            {
            Condition Con=new Condition(PDFolders.getTableName()+"."+PDFolders.fPDID,
                                        (String)ListTabs.elementAt(i)+"."+PDFolders.fPDID ) ;
            Conds.addCondition(Con);
            }
        }
    Record Rec=getRecSum().CopyMono();
    Rec.getAttr(fPDID).setName(PDFolders.getTableName()+"."+fPDID);
    Query LoadAct=new Query(ListTabs, Rec, Conds, null);
    Cursor Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    if (r!=null)
        {
        MultiLoad(r);    
        assignValues(r);
        }
    getDrv().CloseCursor(Cur);
    }
return(r);
}
//-------------------------------------------------------------------------
//-------------------------------------------------------------------------
/**
 * Loads all the multivalued attributed during the LoadFull
 * @param Rec Record with the sum of attributes
 * @throws PDException in any error
 */
private void MultiLoad(Record Rec) throws PDException
{
Record TypDef;    
String MultiName;
Attribute Atr, Atr2, Attr2Load;
Query LoadAct;
Conditions Conds;
String Id=(String)Rec.getAttr(fPDID).getValue();
Record r;
Cursor Cur;
Record RecLoad=new Record();
for (int NumDefTyp = 0; NumDefTyp<getTypeDefs().size(); NumDefTyp++)
    {
    TypDef=((Record)getTypeRecs().get(NumDefTyp)).CopyMulti();
    String TabName=(String)((Record)getTypeDefs().get(NumDefTyp)).getAttr(PDObjDefs.fNAME).getValue();
    TypDef.initList();
    for (int NumAttr = 0; NumAttr < TypDef.NumAttr(); NumAttr++)
        {
        Atr=TypDef.nextAttr();
        Atr2=Rec.getAttr(Atr.getName());
        if (Atr2==null) 
            {
            Atr2=Atr.Copy();
            Rec.addAttr(Atr2);
            }
        Atr2.ClearValues();
        MultiName=PDObjDefs.genMultValNam(TabName,Atr2.getName());
        Conds=new Conditions();
        Conds.addCondition(new Condition(fPDID, Condition.cEQUAL, Id));
        RecLoad.Clear();
        Attr2Load=Atr2.Copy();
        Attr2Load.setMultivalued(false);
        RecLoad.addAttr(Attr2Load);
        LoadAct=new Query(MultiName, RecLoad, Conds, null);
        Cur=getDrv().OpenCursor(LoadAct);
        r=getDrv().NextRec(Cur);
        while (r!=null)
            {
            Rec.getAttr(Atr.getName()).AddValue(r.getAttr(Atr.getName()).getValue());    
            r=getDrv().NextRec(Cur);            
            }
        getDrv().CloseCursor(Cur);
        }
     }
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getDefaultOrder()
{
return(fTITLE);
}
//-------------------------------------------------------------------------
public String toString()
{
return this.getTitle();
}
//-------------------------------------------------------------------------
/**
 * Creates a new folder under the current object with inherited ACL,
 *      default folder type and the name FoldName
 * @param FoldName Name of the new folder
 * @return Id of the new folder
 * @throws PDException
 */
public String CreateChild(String FoldName) throws PDException
{
Load(getPDId());    
PDFolders f=new PDFolders(getDrv());
f.setTitle(FoldName);
f.setACL(getACL());
f.setParentId(getPDId());
f.insert();
return(f.getPDId());
}
//-------------------------------------------------------------------------
/**
 * Returns the Id of a folder with name FoldName child of folder ParentId
 * @param ParentId id of parent folder
 * @param FoldName name of child folder
 * @return Id of the found folder
 * @throws PDException if the folder dosen't exist or the user it'snt allowed
 */
public String GetIdChild(String ParentId, String FoldName) throws PDException
{
Condition CT=new Condition(fTITLE, Condition.cEQUAL, FoldName);
Condition CP=new Condition(fPARENTID, Condition.cEQUAL, ParentId);
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
Conditions Conds=new Conditions();
Conds.addCondition(CT);
Conds.addCondition(CP);
Conds.addCondition(CondAcl);
Query LoadAct=new Query(getTabName(), getRecordStructPDFolder(), Conds);
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record r=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
if (r==null)
    PDExceptionFunc.GenPDException("do_not_exist_subfolder_under_folder", ParentId+"/"+FoldName);
Attribute A=r.getAttr(fPDID);
return((String)A.getValue());
}
//-------------------------------------------------------------------------
/**
 * Travels by the path to find the Id of last folder in path
 * @param FoldName name of child folder
 * @return the Id of the last folder in the path
 * @throws PDException if the folder dosen't exist or the user it'snt allowed
 */
public String getIdPath(String FoldName)  throws PDException
{
String Id="";
return(Id);
}
//-------------------------------------------------------------------------
/**
 * Travels by and create all the necesari folders
 * @param FoldName name of child folder
 * @return the Id of the last folder in the path
 * @throws PDException if the folder dosen't exist or the user it'snt allowed
 */
public String CreatePath(String FoldName)  throws PDException
{
String Path="";
return(Path);
}
//-------------------------------------------------------------------------
/**
 * Search for Folders returning a cursor with the results of folders with the
 * indicated values of fields. Only return the folders alowed for the user, as defined by ACL.
 * @param FolderType Type of folder to search. Can return folders of subtype.
 * @param AttrConds Conditions over the fields ofthe FolderType
 * @param SubTypes if true, returns results of the indicated type AND susbtipes
 * @param SubFolders if true seach in actual folder AND subfolders, if false, serach in ALL the structure
 * @param IdActFold Folder to start the search. if null, start in the root level
 * @param Ord
 * @return a Cursor with the results of the query to use o send to NextFold()
 * @throws PDException when occurs any problem
 */
public Cursor Search(String FolderType, Conditions AttrConds, boolean SubTypes, boolean SubFolders, String IdActFold, Vector Ord) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.Search >:"+FolderType+" {"+AttrConds+"} SubTypes:"+SubTypes+" SubFolders:"+SubFolders+" IdActFold:"+IdActFold+" Ord:"+Ord);
PDFolders F=new PDFolders(getDrv(), FolderType);
Conditions ComposedConds=new Conditions();
ComposedConds.addCondition(AttrConds);
Vector TypList=new Vector();
TypList.add(FolderType);
if (!SubTypes)
    {
    Condition C=new Condition(PDFolders.fFOLDTYPE, Condition.cEQUAL, FolderType);
    ComposedConds.addCondition(C);
    }
if (!FolderType.equalsIgnoreCase(getTableName()))
    { // we add other "parts" of the folder in the "join"
    Conditions CondTyps=new Conditions();
    ArrayList ListTip=F.getTypeDefs();
    for (int i = 0; i < ListTip.size(); i++)
        {
        Record R= (Record)ListTip.get(i);
        Attribute Attr=R.getAttr(PDObjDefs.fNAME);
        String Typ =(String) Attr.getValue();
        if (!Typ.equalsIgnoreCase(getTableName()))
            {
            Condition Con=new Condition(getTableName()+"."+fPDID, Typ+"."+fPDID);
            CondTyps.addCondition(Con);
            }
        if (!Typ.equalsIgnoreCase(FolderType))
            TypList.add(Typ);
        }
    ComposedConds.addCondition(CondTyps);
    }
if (SubFolders)
    {
    if (!(IdActFold==null || IdActFold.equalsIgnoreCase(PDFolders.ROOTFOLDER)))
        { // add list to conditions
        Condition C=new Condition(PDFolders.fPDID, F.getListDescendList(IdActFold));
        ComposedConds.addCondition(C);
        }
    }
//else
//    {
//    Condition C=new Condition(PDFolders.fPARENTID, Condition.cEQUAL, IdActFold);
//    ComposedConds.addCondition(C);
//    }
Record RecSearch=F.getRecSum().CopyMono();
if (RecSearch.ContainsAttr(fPDID))
    {
    RecSearch.getAttr(fPDID).setName((String)TypList.get(0)+"."+fPDID);
    }
else
    {
    Attribute Atr=getRecord().getAttr(fPDID).Copy();
    Atr.setName((String)TypList.get(0)+"."+fPDID);
    RecSearch.addAttr(Atr);
    }
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ComposedConds.addCondition(CondAcl);
Query FoldSearch=new Query(TypList, RecSearch, ComposedConds, Ord);
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.Search <");
return(getDrv().OpenCursor(FoldSearch));
}
//-------------------------------------------------------------------------
/**
 * Receives a cursor created with method Search and returns the next folder or null if
 * there are no more.
 * @param Res Cursor created with method Search and serevral parameters
 * @return A new Object created with the data of the next row
 * @throws PDException when occurs any problem
 */
public PDFolders NextFold(Cursor Res) throws PDException
{
PDFolders NextF=null;
Record Rec=getDrv().NextRec(Res);
if (Rec==null)
    return(NextF);
String Typ=(String)Rec.getAttr(fFOLDTYPE).getValue();
NextF=new PDFolders(getDrv(), Typ);
NextF.assignValues(Rec);
return(NextF);
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (FoldObjectsCache==null)
    FoldObjectsCache=new ObjectsCache("Fold");
return(FoldObjectsCache);    
}
//-------------------------------------------------------------------------
/**
 * Returns the value/field used as key of the object (Id) to ve used in Cache index
 * @return the value of the  Id field
 */
@Override
protected String getKey()
{
return(getPDId());
}
//-------------------------------------------------------------------------
/**
 * Add aditional information, oriented a "extended" object with childrn nodes
 * @return The aditional XML
 * @throws PDException
 */
@Override
protected String toXML2() throws PDException
{
Record RFull=getRecSum().Copy();
RFull.delRecord(getRecord());
return(RFull.toXML()+"</ListAttr>");    
}
//-------------------------------------------------------------------------
/**
 *
 * @param OPDObject
 * @param ParentFolderId
 * @param MaintainId
 * @return
 * @throws PDException
 */
public PDFolders ImportXMLNode(Node OPDObject, String ParentFolderId, boolean MaintainId) throws PDException
{
NodeList childNodes = OPDObject.getChildNodes();
PDFolders NewFold=null;
for (int i = 0; i < childNodes.getLength(); i++)
    {
    Node item = childNodes.item(i);
    if (item.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(item, getRecord());
        String FoldTypReaded=(String)r.getAttr(PDFolders.fFOLDTYPE).getValue();
        NewFold=new PDFolders(getDrv(), FoldTypReaded); // to be improved to analize the type BEFORE
        r=Record.FillFromXML(item, NewFold.getRecSum());
        NewFold.assignValues(r);
        if (!MaintainId)
            NewFold.setPDId(null);
        NewFold.setParentId(ParentFolderId);
        }
    }
NewFold.insert();
return NewFold;
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLFile
 * @param ParentFolderId
 * @return 
 * @throws PDException
 */
public PDFolders ProcessXML(File XMLFile, String ParentFolderId) throws PDException
{
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject;
PDFolders NewFold=null;
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    NewFold=ImportXMLNode(OPDObject, ParentFolderId, false);
    }
DB.reset();
return(NewFold); // returned LAST Folder when opd file contains several.
}catch(Exception ex)
    {
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
/**
 * Exports the 
 * @param Path path to the place to star exporting
 */
void ExportPath(String IdTopLevel, String Path) throws Exception
{
ArrayList Family=OrderedGrandParents(getPDId());
PDFolders Fold=new PDFolders(getDrv());
File SOFolder;
for (int i = Family.size()-1; i >=0; i--)
    {
    String Id = (String)Family.get(i);
    if (Id.equals(IdTopLevel) || Id.equals(ROOTFOLDER))
            continue;
    Fold.Load(Id);
    Path+=File.separatorChar+Fold.getTitle();
    SOFolder=new File(Path);
    if (!SOFolder.exists())
        SOFolder.mkdir();
    }
String Destpath=Path+File.separatorChar+getTitle();    
SOFolder=new File(Destpath);    
if (!SOFolder.exists())
    SOFolder.mkdir();
PrintWriter PW = new PrintWriter(Destpath+".opd");
PW.print(StartXML());    
PW.print(toXML());
PW.print(EndXML());    
PW.flush();
PW.close();
ExportDocs(Destpath);
}
//---------------------------------------------------------------------
/**
 * Returns a Collection containing an ordered set of parent, from father to root.
 * Excludeing Id
 * @param Id Identifier of folder
 * @return 
 * @throws prodoc.PDException
 */
public ArrayList OrderedGrandParents(String Id) throws PDException
{
ArrayList Family=new ArrayList();
PDFolders F=new PDFolders(getDrv());
while (!Id.equals(PDFolders.ROOTFOLDER))
        {
        F.Load(Id);
        Id=F.getParentId();
        Family.add(Id);
        }
return(Family);
}
//---------------------------------------------------------------------
/**
 * Export All documents in folder
 * @param Path OS path to export to
 */
public void ExportDocs(String Path) throws PDException
{
PDDocs Doc = new PDDocs(getDrv());    
Cursor ListDocs=Doc.getListContainedDocs(getPDId());
Record Res=getDrv().NextRec(ListDocs);
PDDocs ExpDoc=new PDDocs(getDrv());
while (Res!=null)
    {
    ExpDoc.assignValues(Res);    
    ExpDoc.ExportXML(Path, false);
    Res=getDrv().NextRec(ListDocs);
    }
getDrv().CloseCursor(ListDocs);
}
//---------------------------------------------------------------------
/** Executes all the transactional defined threads
 * 
 */
protected void ExecuteTransThreads(String MODE) throws PDException
{
ArrayList L =getDrv().getFoldTransThreads(this.getFolderType(), MODE); 
PDTasksDefEvent T;
for (int i = 0; i < L.size(); i++)
    {
    T = (PDTasksDefEvent)L.get(i);
    T.Execute(this);
    }  
}
//---------------------------------------------------------------------
/** Generates all the NO transactional defined threads
 * 
 */
protected void GenerateNoTransThreads(String MODE) throws PDException
{
ArrayList L =getDrv().getFoldNoTransThreads(this.getFolderType(), MODE); 
PDTasksDefEvent T;
PDTasksExec TE;
for (int i = 0; i < L.size(); i++)
    {
    T = (PDTasksDefEvent)L.get(i);
    TE=new PDTasksExec(getDrv());
    TE.GenFromDef(T, this);
    TE.insert();
    }
}
//-------------------------------------------------------------------------
}
