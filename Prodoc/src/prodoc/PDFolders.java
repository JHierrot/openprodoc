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

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Main public class that manages all the operations for Folders in OpenProdoc
 * @author jhierrot
 */
public class PDFolders extends ObjPD
{
/**
 * Name of the attribute identifier of the Folder
 */
public static final String fPDID="PDId";
/**
 * Name of the attribute containing the "name" of the folder
 */
public static final String fTITLE="Title";
/**
 * Name of the attribute containing the ACL of the folder
 */
public static final String fACL="ACL";
/**
 * Name of the attribute containing the document type of the document
 */
public static final String fFOLDTYPE="FolderType";
/**
 * Name of the attribute containing the Identifier of the folder that "contains" the folder
 */
public static final String fPARENTID="ParentId";
/**
 * Name of the attribute containing the Identifier of ANY of the folders that "contains" the folder
 */
public static final String fGRANTPARENTID="GrantParentId";
/**
 * Constant with the value of the base folder, that is folder containing ALL the folders 
 */
public static final String ROOTFOLDER="RootFolder";
/**
 * Constant with the value of the folder containing users personal folders
 */
public static final String USERSFOLDER="Users";
/**
 * Constant with the value of the folderdefined for storing tchnical/system documents and elements
 */
public static final String SYSTEMFOLDER="System";
/**
 * Default table name for defauls folder type
 */    
private static final String DEFTABNAME="PD_FOLDERS";

/**
 * Internal Record for managing a copy of the structure of a Folder
 */
static private Record FoldersStruct=null;
/**
 * Internal Record for managing a copy of the structure of folder levels table
 */
static private Record FoldersLevStruct=null;
/**
 * Field identifier
 */
private String PDId=null;
/**
 * Field name/title of folder
 */
private String Title=null;
/**
 * Field ACL
 */
private String ACL=null;
/**
 * Field Folder type
 */
private String FolderType=DEFTABNAME;
/**
 * Field identifier of Folder containing the folder
 */
private String ParentId=null;
/**
 * Boolean field that return true when current folder is root folder
 * that is PdId=Title=ROOTFOLDER
 */
private boolean IsRootFolder=false;
/**
 * Collection of type definitions that compound the current document type
 */
private ArrayList<Record> TypeDefs=null;
/**
 * Collection of type definitions Attributes that compound the current document type
 */
private ArrayList<Record> TypeRecs=null;
/**
 * Collection of ALL the attributes defined for a folder type (including inherited Attributes)
 */
private Record RecSum=null;
/**
 * Cache of PDDolders managed in JVM the last minutes
 */
static private ObjectsCache FoldObjectsCache = null;
/**
 * Characters not allowed in Folders name (potential problems when exporting or searching)
 */
static private final String NotAllowedChars="/\\:*?";
/**
 * Constant for trace of operations
 */
private static final String fOPERINS="INSERT";
/**
 * Constant for trace of operations
 */
private static final String fOPERDEL="DELETE";
/**
 * Constant for trace of operations
 */
private static final String fOPERUPD="UPDATE";
/**
 * Constant for trace of operations
 */
private static final String fOPERVIE="VIEW";

//-------------------------------------------------------------------------
/**
 * Default constructor that creates a Folder of the default Foldertype
 * @param Drv Driver generic to use for reading and writing
 * @throws PDException In any error
 */
public PDFolders(DriverGeneric Drv) throws PDException
{
super(Drv);
setFolderType(getTableName());
}
//-------------------------------------------------------------------------
/**
 * Default constructor that creates a document of the specified foldertype
 * @param Drv Driver generic to use for reading and writing
 * @param pFoldType Type of folder to create
 * @throws PDException In any error
 */
public PDFolders(DriverGeneric Drv, String pFoldType) throws PDException
{
super(Drv);
setFolderType(pFoldType);
getTypeDefs();
}
//-------------------------------------------------------------------------
/**
 * Assign the values of the record to the fields of the object
 * @param Rec values to assign
 * @throws PDException In any error
*/
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
 * Sets the unique identifier
 * @param pPDId Identifier to set
 * @throws PDExceptionFunc  In any error
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
 * Sets the Folder title/name
 * @param pTitle Value to set
 * @throws PDExceptionFunc in any error
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
 * @return the name of the table for the default folder type
 */
@Override
public String getTabName()
{
return(getTableName());
}
//-------------------------------------------------------------------------
/**
 * static equivalent method to getTabName() that returns the name
 * @return the name of the table for the default folder type
 */
static public String getTableName()
{
return(DEFTABNAME);
}
//-------------------------------------------------------------------------
/**
 * Returns the name of the table containing the compiledinformation of folders hierarchy
 * @return The name of the table
 */
static private String getTableNameFoldLev()
{
return("PD_FOLD_LEV");
}
//-------------------------------------------------------------------------
/**
 * The install method is generic because for instantiate a object, the class
 * need to access to the tables for definition
 * @param Drv Genereric driver
 * @throws PDException in any error
 */
static public void Install(DriverGeneric Drv) throws PDException
{
Drv.CreateTable(getTableName(), getRecordStructPDFolder());
}
//-------------------------------------------------------------------------
/**
 * Constructs a record with all the attributes of the folder type and the values
 * of the attributes asigned using the setter of the class.
 * @throws PDException in any error
 * @return a record with all the attributes of the folder type
 */
@Override
synchronized public Record getRecord() throws PDException
{
return(getRecSum());
}
//-------------------------------------------------------------------------
/**
 * Return a copy of the static structure of attributes for the class
 * @throws PDException in any error
 * @return a copy of the static structure of attributes for the class
 */
@Override
protected Record getRecordStruct() throws PDException
{
return( getRecSum().Copy());
}
//-------------------------------------------------------------------------
/**
 * Return a copy of the static structure of attributes for the class
 * @throws PDException In any error
 * @return a copy of the static structure of attributes for the class
 */
static public Record getRecordStructPDFolder() throws PDException
{
if (FoldersStruct==null)
    FoldersStruct=CreateRecordStructPDFolder();
return(FoldersStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the structure of the default folder type, creating it if neeeded
 * @return a Record with all the Attributes
 * @throws PDException In any error
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
 * Returns the fixed structure for levels/hierachy of folders
 * @return a Record with all the Attributes
 * @throws PDException In any error
 */
static protected Record getRecordStructPDFolderLev() throws PDException
{
if (FoldersLevStruct==null)
    FoldersLevStruct=CreateRecordStructPDFolderLev();
return(FoldersLevStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the structure of the folder levels, creating it if neeeded
 * @return a Record with all the Attributes
 * @throws PDException In any error
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
 * Construct the conditions for a query to retrieve the actual object from the database
 * the constructed query searchs by the unique id/index PDID
 * @throws PDException if there is no PDID value
 * @return the constructed conditions
 */
@Override
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
 * @throws PDException in any error
 */
protected Conditions getConditionsMaint() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Creates a contions object for searching with like criteria
 * @param Name value of title to search containing wildcards
 * @return List of conditions
 * @throws PDException In any error
 */
@Override
protected Conditions getConditionsLike(String Name) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fTITLE, Condition.cLIKE, VerifyWildCards(Name)));
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ListCond.addCondition(CondAcl);
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Assign the unique identifier (PDId in foldrs) to the object
 * @param Ident value to assign
 * @throws PDExceptionFunc in any error
 */
@Override
protected void AsignKey(String Ident) throws PDExceptionFunc
{
setPDId(Ident);
}
//-------------------------------------------------------------------------
/**
* @return the ACL
*/
public String getACL()
{
return ACL;
}
//-------------------------------------------------------------------------
/**
 * Sets the name of the ACL
 * @param pACL ACL to assign
 */
public void setACL(String pACL)
{
this.ACL = pACL;
}
//-------------------------------------------------------------------------
/**
* @return the FolderType
*/
public String getFolderType()
{
return FolderType;
}
//-------------------------------------------------------------------------
/**
 * Assigns the Folder Type, forcing to reload the definition of type and metadata
 * @param pFolderType Type to assign
 */
public void setFolderType(String pFolderType) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.setFolderType:"+FolderType);
if (!FolderType.equalsIgnoreCase(pFolderType))
    {
    FolderType = pFolderType;
    LoadDef(FolderType);
    }
//this.TypeDefs=null;
//this.TypeRecs=null;
}
//-------------------------------------------------------------------------
/**
* The install method is generic because for instantiate a object, the class
* need to access to the tables for definition
* @param Drv Drive to use
* @throws PDException in any error
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
//-------------------------------------------------------------------------
/**
 * Creates a folder
 * @throws PDException In any error
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
if (PDLog.isInfo())
    PDLog.Info("PDFolders.insert:"+getPDId());
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
        Parent.TouchDate(); // due the nulls update
        }
    }
AddLogFields();
getRecSum().CheckDef();
MonoInsert();
MultiInsert(getRecSum());
if (!IsRootFolder)
    ActFoldLev();
ExecuteTransThreads(PDTasksDefEvent.fMODEINS);
GenerateNoTransThreads(PDTasksDefEvent.fMODEINS);
if (MustTrace(fOPERINS))
    Trace(fOPERINS, true);
getObjCache().put(getKey(), getRecord());
} catch (Exception Ex)
    {
    getDrv().AnularTrans();
    if (MustTrace(fOPERINS))
       Trace(fOPERINS, false);
    PDException.GenPDException("Error_creating_folder",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.Insert<");
}
//-------------------------------------------------------------------------
/**
 * Insert in all the tables the monovalued atributes
 * @throws PDException in any error
 */
protected void MonoInsert()  throws PDException
{
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=getTypeDefs().get(i);
    Record DatParc=(getTypeRecs().get(i)).CopyMono();
//    if (i!=getTypeDefs().size()-1)
    if (!DatParc.ContainsAttr(fPDID))
        {
        DatParc.addAttr(getRecSum().getAttr(fPDID));
        }
    DatParc.assign(getRecSum().CopyMono());
    getDrv().InsertRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc.CopyMono());
    }
}
//-------------------------------------------------------------------------
/**
 * Inserts all the multivalued attributed during the Insert
 * @param Rec Record with the sum of attributes
 * @throws PDException in any error
 */
protected void MultiInsert(Record Rec) throws PDException
{
Record TypDef;    
String MultiName;
Attribute AtrOrig, Atr2Ins;
TreeSet Values;
Record RecSave=new Record();
Object Val2Ins;
for (int NumDefTyp = 0; NumDefTyp<getTypeDefs().size(); NumDefTyp++)
    {
    TypDef=(getTypeRecs().get(NumDefTyp)).CopyMulti();
    String TabName=(String) (getTypeDefs().get(NumDefTyp)).getAttr(PDObjDefs.fNAME).getValue();
    TypDef.initList();
    for (int NumAttr = 0; NumAttr < TypDef.NumAttr(); NumAttr++)
        {
        AtrOrig=TypDef.nextAttr();
        Atr2Ins=Rec.getAttr(AtrOrig.getName());
//        if (Atr2Ins==null || Atr2Ins.getValuesList()==null || Atr2Ins.getValuesList().isEmpty())
        if (Atr2Ins.getValuesList()==null || Atr2Ins.getValuesList().isEmpty())
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
/**
 * Checks if the user is granted to create a folder (by role limitations of by ACL of parent folder)
 * @throws PDException If he user is nos allowed
 */
@Override
protected void VerifyAllowedIns() throws PDException
{
if (getDrv().getUser().getName().equalsIgnoreCase("Install"))  
    return;
if (!getDrv().getUser().getRol().isAllowCreateFolder() )
   PDExceptionFunc.GenPDException("Folder_creation_not_allowed_to_user", null);
PDObjDefs D=new PDObjDefs(getDrv());
D.Load(getFolderType());    
if (!getDrv().getUser().getAclList().containsKey(D.getACL()))
    PDExceptionFunc.GenPDException("Folder_creation_not_allowed_to_user",getDrv().getUser().getName()+" / "+getFolderType());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(D.getACL());
if (Perm<PDACL.pUPDATE)
    PDExceptionFunc.GenPDException("Folder_creation_not_allowed_to_user",getDrv().getUser().getName()+" / "+getFolderType());
}
//-------------------------------------------------------------------------
/**
 * Checks if the user is granted to DELETE a folder (by role limitations of by ACL of parent folder)
 * @throws PDException If he user is nos allowed
 */
@Override
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainFolder() )
   PDExceptionFunc.GenPDException("Folder_delete_not_allowed_to_user", null);
PDFolders TobeDeleted=new PDFolders(getDrv());
TobeDeleted.Load(getPDId());
if (!getDrv().getUser().getAclList().containsKey(TobeDeleted.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_over_folder", null);
Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeDeleted.getACL());
if (Perm!=PDACL.pDELETE)
   PDExceptionFunc.GenPDException("User_without_permissions_over_folder", null);
}
//-------------------------------------------------------------------------
/**
 * Checks if the user is granted to UPDATE a folder (by role limitations of by ACL of parent folder)
 * @throws PDException If he user is nos allowed
 */
@Override
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainFolder() )
   PDExceptionFunc.GenPDException("Folder_update_not_allowed_to_user", null);
PDFolders TobeUpdated=new PDFolders(getDrv());
TobeUpdated.Load(getPDId());
if (!getDrv().getUser().getAclList().containsKey(TobeUpdated.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_over_folder", null);
Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeUpdated.getACL());
if (Perm<=PDACL.pREAD)
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
 * Sets the parent folder of the current object
 * @param pParentId Id of parent folder
 */
public void setParentId(String pParentId)
{
this.ParentId = pParentId;
}
//-------------------------------------------------------------------------
/**
 * Creates the root folder, base of all the other folders
 * @throws PDException In any error
 */
private void CreateRootFolder() throws PDException
{
IsRootFolder=true;
insert();
}
//-------------------------------------------------------------------------
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
 * Loads the definition of a folder type
 * @param tableName name of folder type
 * @throws PDException in any error
 */
private void LoadDef(String tableName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.LoadDef");
setFolderType(tableName);
TypeDefs=new ArrayList<Record>();
TypeRecs=new ArrayList<Record>();
getDrv().LoadDef(tableName, getTypeDefs(), getTypeRecs());
RecSum=new Record();
for (int i = 0; i < getTypeRecs().size(); i++)
    {
    RecSum.addRecord((Record)getTypeRecs().get(i));
    }
RecSum.getAttr(fFOLDTYPE).setValue(getFolderType());
}
//-------------------------------------------------------------------------
/** Return an ordered list of the hierarchy of folder types from whom this
 * folder type inherit its attributes
 * @return the TypeDefs the array list with the names of folder types
 * @throws PDException if there is a problem comunicating with the server
*/
public ArrayList<Record> getTypeDefs() throws PDException
{
if (TypeDefs==null)
    LoadDef(getFolderType());
return TypeDefs;
}
//-------------------------------------------------------------------------
/**
 * Returns the collection of attributes of the folder type and its ancestors
 *   loads the definition if there are not loaded
 * @return the TypeRecs Array with all the definitions
 * @throws PDException In any error
*/
public ArrayList<Record> getTypeRecs() throws PDException
{
if (TypeRecs==null)
    LoadDef(getFolderType());
return TypeRecs;
}
//-------------------------------------------------------------------------
/**
 * Insert a set of records in folder level table publising the "hierachy of folders of current folder
 * @throws PDException In any error
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
 * Search the list of folders that contain the specified folder
 * @param Parent Folder Id to lok for the hierachy
 * @return a Set containing the parents of Parent
 * @throws PDException In any error
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
Cursor CursorId=null;
try {
CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String FId=(String) Res.getAttr(fGRANTPARENTID).getValue();
    Result.add(FId);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
} catch (Exception Ex)
    {
    if (CursorId!=null)
        getDrv().CloseCursor(CursorId);
    PDException.GenPDException("Error_retrieving_folder_hierarchy", Ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListGrandParentList<:"+Parent);
return(Result);
}
//-------------------------------------------------------------------------
/**
 * return a list of folders contained (at any level) in the forlder specified
 * @param PDId Identifier of folder to look for descendant
 * @return a Set containing the descendant of PDId folder
 * @throws PDException In any error
 */
public HashSet getListDescendList(String PDId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDescendList>:"+PDId);
HashSet Result=new HashSet();
//Condition CondParents=new Condition( fGRANTPARENTID, Condition.cEQUAL, PDId);
//Conditions Conds=new Conditions();
//Conds.addCondition(CondParents);
//Query Q=new Query(getTableNameFoldLev(), getRecordStructPDFolderLev(), Conds);
Query Q=getQueryListDescendList(PDId);
Cursor CursorId=null;
try {
CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String FId=(String) Res.getAttr(fPDID).getValue();
    Result.add(FId);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
} catch (Exception Ex)
    {
    if (CursorId!=null)
        getDrv().CloseCursor(CursorId);
    PDException.GenPDException("Error_retrieving_folder_hierarchy", Ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDescendList<:"+PDId);
return(Result);
}
//-------------------------------------------------------------------------
protected Query getQueryListDescendList(String PDId) throws PDException
{
Condition CondParents=new Condition( fGRANTPARENTID, Condition.cEQUAL, PDId);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Record R=getRecordStructPDFolderLev().Copy();
R.delAttr(fGRANTPARENTID);
return(new Query(getTableNameFoldLev(), R, Conds));
}
//-------------------------------------------------------------------------
/**
 * Return all the Attributes for the current folder type
 *   empty or filled (depending if it has been used)
 *   if needed loads the definition
 * @return the Record with all the attributes
 * @throws PDException in any error
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
 * @throws PDException in any problem and cancels the Transaction. When not enough permissions throws PDFuncException.
 */
@Override
public void delete() throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("PDFolders.delete>:"+getPDId());
boolean InTransLocal;
Load(getPDId());
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
ExecuteTransThreads(PDTasksDefEvent.fMODEDEL);
PDFolders Par=new PDFolders(getDrv());
Par.setPDId(getParentId());
Par.TouchDate();
DeleteFoldersInFolder();
DeleteDocsInFolder();
DeleteFoldLevelParents();
MultiDelete(getPDId());
DelFoldMetadata();
GenerateNoTransThreads(PDTasksDefEvent.fMODEDEL);
if (MustTrace(fOPERDEL))
    Trace(fOPERDEL, true);
getObjCache().remove(getKey());
} catch (PDException Ex)
    {  
    getDrv().AnularTrans();
    if (MustTrace(fOPERDEL))
       Trace(fOPERDEL, false);
    PDException.GenPDException("Error_deleting_folder",Ex.getLocalizedMessage());
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
 * @throws PDException In any error
 */
private void MultiDelete(String Id2Del) throws PDException
{
Record TypDef;    
String MultiName;
Attribute Atr;
Conditions Conds;
for (int NumDefTyp = 0; NumDefTyp<getTypeDefs().size(); NumDefTyp++)
    {
    TypDef=(getTypeRecs().get(NumDefTyp)).CopyMulti();
    String TabName=(String)(getTypeDefs().get(NumDefTyp)).getAttr(PDObjDefs.fNAME).getValue();
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
Record r=new Record();
r.addAttr(PDDocs.getRecordStructPDDocs().getAttr(PDDocs.fPDID));
Query Q=new Query(PDDocs.getTableName(), r, Conds);   
Cursor ListDocsContained=getDrv().OpenCursor(Q);
Record NextDoc=getDrv().NextRec(ListDocsContained);
PDDocs Doc2del=new PDDocs(getDrv());
try {
while (NextDoc!=null)
    {
    Doc2del.setPDId((String)NextDoc.getAttr(PDDocs.fPDID).getValue()); 
    Doc2del.delete();
    NextDoc=getDrv().NextRec(ListDocsContained);
    }
getDrv().CloseCursor(ListDocsContained);
} catch (PDException ex)
    {
    getDrv().CloseCursor(ListDocsContained);
    throw ex;
    }
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
Record r=new Record();
r.addAttr(PDFolders.getRecordStructPDFolder().getAttr(PDFolders.fPDID));
Query Q=new Query(PDFolders.getTableName(), r, Conds);   
Cursor ListFoldersContained=getDrv().OpenCursor(Q);
Record NextFold=getDrv().NextRec(ListFoldersContained);
PDFolders Fold2del=new PDFolders(getDrv());
try {
while (NextFold!=null)
    {
    Fold2del.setPDId((String)NextFold.getAttr(PDFolders.fPDID).getValue());
    Fold2del.delete();
    NextFold=getDrv().NextRec(ListFoldersContained);
    }
getDrv().CloseCursor(ListFoldersContained);
} catch (PDException ex)
    {
    getDrv().CloseCursor(ListFoldersContained);
    throw ex;
    }
}
//-------------------------------------------------------------------------
/**
 * Deletes all the Attbitues of the folder in all the tables
 * @throws PDException In any error 
 */
private void DelFoldMetadata() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.DelFoldMetadata:"+getPDId());  
for (int i = 0; i < getTypeDefs().size(); i++)
    {
    Record TypDef=getTypeDefs().get(i);
    getDrv().DeleteRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), getConditionsMaint());
    }
}
//-------------------------------------------------------------------------
/**
 * Deletes all the references in upper levels to this folder
 * @throws PDException In any error
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
 * @throws PDException In any error
 */
@Override
public void update()  throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("PDFolders.update:"+getPDId());
boolean InTransLocal;
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
AddLogFields();
//getRecSum().CheckDef();
Record R=getRecord().Copy();
R.delAttr(fPARENTID);
R.delAttr(fFOLDTYPE);
MonoUpdate();
MultiDelete(this.getPDId());
MultiInsert(R);
ExecuteTransThreads(PDTasksDefEvent.fMODEUPD);
GenerateNoTransThreads(PDTasksDefEvent.fMODEUPD);
if (MustTrace(fOPERUPD))
    Trace(fOPERUPD, true);
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    if (MustTrace(fOPERUPD))
       Trace(fOPERUPD, false);
    PDException.GenPDException("Error_updating_folder",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
}
//-------------------------------------------------------------------------
/**
 * Updates all the record of monovalued attributes
 * @throws PDException in any error
 */
protected void MonoUpdate()  throws PDException
{
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=getTypeDefs().get(i);
    Record DatParc=getTypeRecs().get(i);
    if (i!=getTypeDefs().size()-1)
        {
        if (DatParc.getAttr(fPDID)==null)    
            DatParc.addAttr(getRecSum().getAttr(fPDID));
        }
    DatParc.assign(this.getRecSum());
    if (PDLog.isDebug())
        PDLog.Debug("PDFolders.update2:"+(String)TypDef.getAttr(PDObjDefs.fNAME).getValue()+ ">"+DatParc);
//    if (DatParc.NumAttrFilled()>1) // Id + some value
    if (DatParc.NumAttr()>1)
        getDrv().UpdateRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc.CopyMono(), getConditionsMaint());
    }
}
//-------------------------------------------------------------------------
/**
 * Creates the main folder used by all the elements as "infraestructure", 
 * the "Users" folder and the "System" folder
 * @param Drv to be used
 * @throws PDException In any error
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
PDFolders f3=new PDFolders(Drv);
f3.setFolderType(getTableName());
f3.setPDId(SYSTEMFOLDER);
f3.setTitle(SYSTEMFOLDER);
f3.setParentId(ROOTFOLDER);
f3.setACL("Public");
f3.insert();
}
//-------------------------------------------------------------------------
/**
 * Return a list of all the folders of any type whose DIRECT parent is PDId
 * @param PDId Id of the parent folder
 * @return a hashset with all the DirectDescend
 * @throws PDException in any error
 */
public HashSet<String> getListDirectDescendList(String PDId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDirectDescendList>:"+PDId);
LinkedHashSet Result=new LinkedHashSet();
Condition CondParents=new Condition( fPARENTID, Condition.cEQUAL, PDId);
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Conds.addCondition(CondAcl);
Record r=new Record();
r.addAttr(PDFolders.getRecordStructPDFolder().getAttr(PDFolders.fPDID));
Query Q=new Query(getTabName(), r, Conds, fTITLE);
Cursor CursorId=getDrv().OpenCursor(Q);
try {
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String FId=(String) Res.getAttr(fPDID).getValue();
    Result.add(FId);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
} catch (Exception Ex)
    {
    getDrv().CloseCursor(CursorId);
    PDException.GenPDException("Error_retrieving_folder_hierarchy", Ex.getLocalizedMessage());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.getListDirectDescendList<:"+PDId);
return(Result);
}
//-------------------------------------------------------------------------
/**
 * Loads the standard attributes of folder identified by Ident
 * @param Ident Identifier of folder to load
 * @return A record with the loaded values
 * @throws PDException In any error
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
    try {
    r=getDrv().NextRec(Cur);
    } finally
        {
        getDrv().CloseCursor(Cur);
        }
    getObjCache().put(Ident, r);
    }
if (r==null)
    PDExceptionFunc.GenPDException("Folder_do_not_exist",Ident);
String ActACL=(String)r.getAttr(fACL).getValue();
if (!getDrv().getUser().getName().equals("Install") && !getDrv().getUser().getAclList().containsKey(ActACL))
    PDExceptionFunc.GenPDException("User_without_permissions_over_folder",Ident);
assignValues(r);
return(r);
}
//-------------------------------------------------------------------------
/**
 * Loads the standard attributes of folder identified by Ident
 * @param Ident Identifier of folder to load
 * @return A record with the loaded values
 * @throws PDException In any error
 */
public Record LoadRefresh(String Ident)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.LoadRefresh:"+Ident);
AsignKey(Ident);
Record r;
Query LoadAct=new Query(getTabName(), getRecordStructPDFolder(),getConditions());
Cursor Cur=getDrv().OpenCursor(LoadAct);
try {
r=getDrv().NextRec(Cur);
} finally
    {
    getDrv().CloseCursor(Cur);
    }
getObjCache().put(Ident, r);
if (r==null)
    PDExceptionFunc.GenPDException("Folder_do_not_exist",Ident);
String ActACL=(String)r.getAttr(fACL).getValue();
if (!getDrv().getUser().getAclList().containsKey(ActACL))
    PDExceptionFunc.GenPDException("User_without_permissions_over_folder",Ident);
assignValues(r);
return(r);
}
//-------------------------------------------------------------------------
/**
 * Load to memory all the elements of a Folder, including all the inherited attributes and multivalued
 * @param Ident Identifier (PDId) of Folder
 * @return a record with ALL the Attributes of the Folder type
 * @throws PDException In any error
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
        Record TypDef=getTypeDefs().get(i);
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
    try {
    r=getDrv().NextRec(Cur);
    } finally 
        {
        getDrv().CloseCursor(Cur);
        }
    if (r!=null)
        {
        MultiLoad(r);    
        assignValues(r);
        }
    }
if (r==null)
    PDExceptionFunc.GenPDException("Folder_do_not_exist",Ident);
if (MustTrace(fOPERVIE))
    Trace(fOPERVIE, true);
String ActACL=(String)r.getAttr(fACL).getValue();
if (!getDrv().getUser().getAclList().containsKey(ActACL))
    {
    PDExceptionFunc.GenPDException("User_without_permissions_over_folder",Ident);
    }
return(r);
}
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
    TypDef=getTypeRecs().get(NumDefTyp).CopyMulti();
    String TabName=(String)getTypeDefs().get(NumDefTyp).getAttr(PDObjDefs.fNAME).getValue();
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
        try {
        r=getDrv().NextRec(Cur);
        while (r!=null)
            {
            Rec.getAttr(Atr.getName()).AddValue(r.getAttr(Atr.getName()).getValue());    
            r=getDrv().NextRec(Cur);            
            }
        } finally 
            {
            getDrv().CloseCursor(Cur);
            }
        }
     }
}
//-------------------------------------------------------------------------
/**
 * Returns the default order for queries
 * @return The deafult order (TITLE)
 */
@Override
protected String getDefaultOrder()
{
return(fTITLE);
}
//-------------------------------------------------------------------------
/**
 * Overloaded method for converting to String
 * @return the string representing Folder (title)
 */
@Override
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
 * @throws PDException In any error
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
Conditions Conds=new Conditions();
Conds.addCondition(CT);
Conds.addCondition(CP);
if (!getDrv().getUser().getName().equals("Install")) 
    {
    Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
    Conds.addCondition(CondAcl);
    }
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
String Id=PDFolders.ROOTFOLDER;
PDFolders F=new PDFolders(getDrv());
String[] Folders = FoldName.split("/");
for (int i = 0; i < Folders.length; i++)
    {
    String FoldN = Folders[i];
    if (FoldN.length()!=0)
        Id=F.GetIdChild(Id, FoldN);
    }
return(Id);
}
//-------------------------------------------------------------------------
/**
 * From a Id generates the "path" as a Filesystem
 * @param Id PDID of child folder
 * @return the String representing the complete path excludig rootfolder
 * @throws PDException if the folder doesn't exist or the user it'snt allowed
 */
public String getPathId(String Id) throws PDException
{
String CompPath="";    
PDFolders F=new PDFolders(getDrv());
while (!Id.equals(PDFolders.ROOTFOLDER))
        {
        F.Load(Id);
        Id=F.getParentId();
        CompPath="/"+F.getTitle()+CompPath;
        }
return(CompPath);
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
 * @param Ord Vector of String with the ascending order
 * @return a Cursor with the results of the query to use o send to NextFold()
 * @throws PDException when occurs any problem
 */
public Vector<Record> SearchV(String FolderType, Conditions AttrConds, boolean SubTypes, boolean SubFolders, String IdActFold, Vector Ord) throws PDException
{
Vector<Record> ListRes=new Vector<Record>();
Cursor CursorId = null;
try {
CursorId = Search(FolderType, AttrConds, SubTypes, SubFolders, IdActFold, Ord);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    ListRes.add(Res);
    Res=getDrv().NextRec(CursorId);
    }
} catch (Exception Ex)
    {
    PDException.GenPDException("Error_searching_folder", Ex.getLocalizedMessage());
    }
finally 
    {
    if (CursorId!=null)
        getDrv().CloseCursor(CursorId);    
    }
return(ListRes);
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
 * @param Ord Vector of String with the ascending order
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
    ArrayList ListAttr=F.getTypeRecs();
    for (int NumTabsDef = 0; NumTabsDef < ListTip.size(); NumTabsDef++)
            {
            Record R= (Record)ListTip.get(NumTabsDef);
            Attribute AttrNomTab=R.getAttr(PDObjDefs.fNAME);
            String Typ =(String) AttrNomTab.getValue();
            if (!Typ.equalsIgnoreCase(getTableName()))
                {
                Condition Con=new Condition(getTableName()+"."+fPDID, Typ+"."+fPDID);
                CondTyps.addCondition(Con);
                }
            if (!Typ.equalsIgnoreCase(FolderType))
                TypList.add(Typ);
            Record AttrsTab= ((Record)ListAttr.get(NumTabsDef)).Copy();
            AttrsTab.initList();
            Attribute Attr;
            for (int i = 0; i < AttrsTab.NumAttr(); i++)
                {
                Attr=AttrsTab.nextAttr();
                if (Attr.isMultivalued())
                    {
                    if (ComposedConds.UsedAttr(Attr.getName()))
                        {
                        String MultiName=PDObjDefs.genMultValNam(Typ, Attr.getName());
                        Condition Con=new Condition(getTableName()+"."+fPDID, MultiName+"."+fPDID);
                        CondTyps.addCondition(Con);
                        TypList.add(MultiName);
                        }                            
                    }                    
                }
            }
    ComposedConds.addCondition(CondTyps);
    }
if (SubFolders)
    {
    if (!(IdActFold==null || IdActFold.equalsIgnoreCase(PDFolders.ROOTFOLDER)))
        { // add list to conditions
//        Condition C=new Condition(PDFolders.fPDID, F.getQueryListDescendList(IdActFold));
        ComposedConds.addCondition(Condition.genInTreeCond(IdActFold, getDrv()));
        }
    }
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
@Override
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
 * Import a Folder(s) described by an XML with content referenced
 * @param OPDObject XMLNode to process
 * @param ParentFolderId OPD destination folder
 * @param MaintainId When true, the Original Id is maintained, else a new one is assigned
 * @return The PDFolder of the imported Folder
 * @throws PDException In any error
 */
public PDFolders ImportXMLNode(Node OPDObject, String ParentFolderId, boolean MaintainId) throws PDException
{
NodeList childNodes = OPDObject.getChildNodes();
PDFolders NewFold=null;
PDObjDefs DefDoc=new PDObjDefs(getDrv());
for (int i = 0; i < childNodes.getLength(); i++)
    {
    Node item = childNodes.item(i);
    if (item.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(item, getRecord());
        String FoldTypReaded=(String)r.getAttr(PDFolders.fFOLDTYPE).getValue();
        if (DefDoc.Load(FoldTypReaded)==null)
           throw new PDException("Unknown_FoldType"+":"+FoldTypReaded);          
        NewFold=new PDFolders(getDrv(), FoldTypReaded); // to be improved to analize the type BEFORE
        r=Record.FillFromXML(item, NewFold.getRecSum());
        NewFold.assignValues(r);
        if (!MaintainId && ExistId(NewFold.getPDId()))
            NewFold.setPDId(null);
        NewFold.setParentId(ParentFolderId);
        }
    }
NewFold.insert();
return NewFold;
}
//---------------------------------------------------------------------
/**
 * Process and XML file
 * @param XMLFile File to process
 * @param ParentFolderId Flder where the new Folde()s) will be created
 * @return the last object created
 * @throws PDException In any error
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
 * Exports all the folders contained under IdTopLevel
 * @param IdTopLevel Identifier in OpenProdoc of top level folder where start 
 * @param Path path to the place to start exporting
 * @throws PDException In any error
 */
void ExportPath(String IdTopLevel, String Path) throws Exception
{
ArrayList Family=OrderedGrandParents(getPDId());
PDFolders Fold=new PDFolders(getDrv());
File SOFolder;
Path=DriverGeneric.FixPath(Path, true);
for (int i = Family.size()-1; i >=0; i--)
    {
    String Id = (String)Family.get(i);
    if (Id.equals(IdTopLevel) || Id.equals(ROOTFOLDER))
            continue;
    Fold.Load(Id);
//    Path+=File.separatorChar+Fold.getTitle();
    Path+=Fold.getTitle();
    SOFolder=new File(Path);
    if (!SOFolder.exists())
        SOFolder.mkdir();
    }
//String Destpath=Path+File.separatorChar+getTitle();    
String Destpath=Path+getTitle();    
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
 *     Excluding Id
 * @param Id Identifier of folder
 * @return list of ancestors
 * @throws PDException In any error
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
 * @throws PDException In any error
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
/** Executes, in the current folder, all the transactional defined threads for a specific MODE (INS, UPD, DEL) 
 * @param MODE CKind of operation (INSert, UPDater, DELete)
 * @throws PDException In any error
 */
private void ExecuteTransThreads(String MODE) throws PDException
{
ArrayList<PDTasksDefEvent> L =getDrv().getFoldTransThreads(this.getFolderType(), MODE); 
if (!L.isEmpty())
    LoadFull(getPDId());
for (PDTasksDefEvent L1 : L)
    L1.Execute(this);
}
//---------------------------------------------------------------------
/** Generates all the NO transactional defined threads
 * @param MODE Kind of operation (INSert, UPDater, DELete)
 * @throws PDException in any error
 */
private void GenerateNoTransThreads(String MODE) throws PDException
{
ArrayList<PDTasksDefEvent> L =getDrv().getFoldNoTransThreads(this.getFolderType(), MODE); 
PDTasksDefEvent T;
PDTasksExec TE;
for (PDTasksDefEvent L1 : L)
    {
    TE=new PDTasksExec(getDrv());
    TE.GenFromDef(L1, this);
    if (!TE.MeetsReq(this)) // under folder or future checks
        continue;
    TE.setNextDate(new Date());
    TE.insert();
    }
}
//-------------------------------------------------------------------------
/**
 * Return true if the current folder Object is under ParentId
 * @param ParentId PDID of poytential ancestor
 * @return true if the current folder Object is under ParentId
 * @throws PDException in any error
 */
public boolean IsUnder(String ParentId) throws PDException
{
if (ParentId.equals(getPDId()))  // optimization
    return (true);
boolean Is;    
Condition CondParents=new Condition( fGRANTPARENTID, Condition.cEQUAL, ParentId);
Condition CondThis=new Condition( fPDID, Condition.cEQUAL, getPDId());
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Conds.addCondition(CondThis);
Query Q=new Query(getTableNameFoldLev(), getRecordStructPDFolderLev(), Conds);
Cursor CursorId=getDrv().OpenCursor(Q);
Is=(getDrv().NextRec(CursorId)!=null);
getDrv().CloseCursor(CursorId); 
return(Is);
}   
//-------------------------------------------------------------------------
/**
 * Returns all the data of the doc as html
 * @return String with the html
 * @throws prodoc.PDException in any error
 */
public String toHtml() throws PDException
{
StringBuilder SHtml=new StringBuilder("<p>");
Record R=getRecSum();
R.initList();
Attribute Attr;
PDFolders f=new PDFolders(getDrv());
for (int i = 0; i < R.NumAttr(); i++)
    {
    Attr=R.nextAttr();
    if (Attr.getName().equals(PDDocs.fPARENTID))
        SHtml.append("<b>").append(Attr.getUserName()).append(":</b>").append(f.getPathId((String)Attr.getValue())).append("<br>");
    else if (Attr.getName().equals(PDDocs.fSTATUS))
          ;
    else if (Attr.getName().equals(PDDocs.fLOCKEDBY))
        {
        if (Attr.getValue()!=null)
            SHtml.append("<b>").append(Attr.getUserName()).append(":</b>").append(Attr.Export()).append("<br>");
        }
    else
        SHtml.append("<b>").append(Attr.getUserName()).append(":</b>").append(Attr.Export()).append("<br>");
    }
SHtml.append("</p>");
return(SHtml.toString());
}
//---------------------------------------------------------------------
/**
 * Overrided method in generic OpenProdoc object.
 * @return null always
 * @throws PDException  Always
 */
@Override
public Cursor getAll() throws PDException
{
PDException.GenPDException("ERROR", null);
return(null);
}
//-------------------------------------------------------------------------
/** updates the "uodated date" of the folder after adding a folder or document.
 * Id must be assigned
 * @throws prodoc.PDException
 */
protected void TouchDate() throws PDException
{
if (PDLog.isInfo())
    PDLog.Info("PDFolders.TouchDate:"+getPDId());
Record r=new Record();
Attribute A=this.getRecord().getAttr(fPDDATE );
A.setValue(new Date());
r.addAttr(A);
getDrv().UpdateRecord(this.getTabName(), r, getConditionsMaint());
}
//-------------------------------------------------------------------------
/**
 * Check if the folder identified by the parameter exists
 * @param pdId Identifier of folder to check
 * @return true if the forlder exist
 */
private boolean ExistId(String pdId)
{
if (getObjCache().get(pdId)!=null)
    return(true);
Cursor Cur=null;
Record r=null;
try {
Query LoadAct=new Query(getTabName(), getRecordStruct(),getConditions());
Cur=getDrv().OpenCursor(LoadAct);
r=getDrv().NextRec(Cur);
} catch (Exception Ex)
    {
    PDLog.Error("ExistId."+pdId+"="+Ex.getLocalizedMessage());        
    }
finally
    {
    if (Cur!=null)
       try {
       getDrv().CloseCursor(Cur);
       } catch (Exception Ex)
        {}
    }
if (r!=null)
    return(true);
else
    return(false);
}
//-------------------------------------------------------------------------
/**
 * Move a Folder from its current forlder to another one
 * @param NewParentId Id of the target folder
 * @return true if the document has been moved
 * @throws PDExceptionFunc In any error
 */
public boolean Move(String NewParentId) throws PDExceptionFunc
{
DriverGeneric drv=null;    
try {    
drv = getDrv();  
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.Move>:"+getPDId()+ ">>"+NewParentId);
getObjCache().remove(getKey());
VerifyAllowedUpd();
InTransLocal=!drv.isInTransaction();
if (InTransLocal)
    drv.IniciarTrans();
PDFolders TobeMoved=new PDFolders(getDrv());
TobeMoved.Load(getPDId()); 
if (!getDrv().getUser().getAclList().containsKey(TobeMoved.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_to_move_folder", getPDId());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeMoved.getACL());
if (Perm.intValue()<=PDACL.pUPDATE)
   PDExceptionFunc.GenPDException("User_without_permissions_to_move_folder", getPDId());
PDFolders ToDest=new PDFolders(getDrv());
ToDest.Load(NewParentId); 
if (!getDrv().getUser().getAclList().containsKey(ToDest.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_to_move_folder", getPDId());
Perm=(Integer)getDrv().getUser().getAclList().get(ToDest.getACL());
if (Perm.intValue()<=PDACL.pREAD)
   PDExceptionFunc.GenPDException("User_without_permissions_to_move_folder", getPDId());
HashSet listGrandParentList = ToDest.getListGrandParentList(NewParentId);
if (listGrandParentList.contains(getPDId()))
   PDExceptionFunc.GenPDException("Parent_folder_moved_to_subfolder_not_allowed", getPDId());    
//Record R =getRecordStruct();
Record R =new Record();
R.addAttr(getRecordStruct().getAttr(fPARENTID));
R.getAttr(fPARENTID).setValue(NewParentId);
R.addAttr(getRecordStruct().getAttr(fPDDATE));
R.getAttr(fPDDATE).setValue(new Date());
getDrv().UpdateRecord(getTableName(), R,  getConditions());
setParentId(NewParentId);
DeleteFoldLevelParents();
ActFoldLev();
UpdateParentChilds(getPDId());
if (InTransLocal)
    drv.CerrarTrans();
getObjCache().remove(getKey());
if (PDLog.isDebug())
    PDLog.Debug("PDFolders.Move<:"+getPDId());
} catch (Exception Ex)
    {
    PDLog.Error("PDFolders.Move ("+NewParentId+")="+Ex.getLocalizedMessage());       
    if (drv!=null && drv.isInTransaction())
        try {
        drv.AnularTrans();
        } catch (Exception E)
            {
            PDLog.Error("PDFolders.Move ("+NewParentId+")="+E.getLocalizedMessage());       
            }
    PDExceptionFunc.GenPDException("Parent_folder_moved_to_subfolder_not_allowed", getPDId());
    return(false);    
    }
return(true);    
}
//-------------------------------------------------------------------------
/**
 * Updates all the levels of childs of a Folder
 * @param pdId identifer of foler to update
 * @throws PDException in any error
 */
private void UpdateParentChilds(String pdId) throws PDException
{
PDFolders TmpFold=new PDFolders(getDrv());
HashSet listDirectDescendList = TmpFold.getListDirectDescendList(pdId);
for (Iterator iterator = listDirectDescendList.iterator(); iterator.hasNext();)
    {
    String NextId = (String)iterator.next();
    PDFolders Tmp2Fold=new PDFolders(getDrv()); 
    Tmp2Fold.Load(NextId);
    Tmp2Fold.DeleteFoldLevelParents();
    Tmp2Fold.ActFoldLev();
    Tmp2Fold.UpdateParentChilds(NextId);
    }
}
//-------------------------------------------------------------------------
/**
 * Return true if the configuration of the document types includes functional trace
 * @param Oper Operation to verify the trace requirement
 * @return true when the operaion must be traced
 * @throws PDException In any error
 */
private boolean MustTrace(String Oper) throws PDException
{
PDObjDefs Def=new PDObjDefs(getDrv());
Def.Load(getFolderType());
if (Oper.equals(fOPERVIE))
    return(Def.isTraceView());
if (Oper.equals(fOPERDEL))
    return(Def.isTraceDel());
if (Oper.equals(fOPERINS))
    return(Def.isTraceAdd());
if (Oper.equals(fOPERUPD))
    return(Def.isTraceMod());
return(false);
}
//---------------------------------------------------------------------
/**
 * Saves the functional trace for the current document
 * @param Oper Operation 
 * @param Allowed boolean indication, true when teh opeeration ws allowed
 * @throws PDException In any error
 */
private void Trace(String Oper, boolean Allowed) throws PDException
{
PDTrace tr=new PDTrace(getDrv());
tr.setObjectType(getFolderType());
tr.setName(getPDId());
tr.setOperation(Oper);
tr.setResult(Allowed);
tr.insert();
}
//-------------------------------------------------------------------------
/**
 *
 * @param tableList
 * @return
 * @throws prodoc.PDException
 */
@Override
protected Vector<String> CalculateTabs(List<String> tableList) throws PDException
{
if (tableList.get(0).equalsIgnoreCase("this"))
    tableList.set(0, getTabName());
Vector <String> Tabs=new Vector<String>();
Tabs.add(tableList.get(0));
if (!tableList.get(0).equalsIgnoreCase(getTableName())) // Not PDFolders
    {
    PDFolders f=new PDFolders(getDrv(),tableList.get(0) );
    ArrayList ListTip=f.getTypeDefs();
    ArrayList ListAttr=f.getTypeRecs();
    for (int NumTabsDef = 0; NumTabsDef < ListTip.size(); NumTabsDef++)
        {
        Record R= (Record)ListTip.get(NumTabsDef);
        Attribute AttrNomTab=R.getAttr(PDObjDefs.fNAME);
        String Typ =(String) AttrNomTab.getValue();
        if (!Typ.equalsIgnoreCase(tableList.get(0)))
            Tabs.add(Typ);
//        Record AttrsTab= ((Record)ListAttr.get(NumTabsDef)).Copy();
//        AttrsTab.initList();
//        Attribute Attr;
//        for (int i = 0; i < AttrsTab.NumAttr(); i++)
//            {
//            Attr=AttrsTab.nextAttr();
//            if (Attr.isMultivalued())
//                {
//                Tabs.add(PDObjDefs.genMultValNam(Typ, Attr.getName()));                            
//                }                    
//            }
        }
    }
return(Tabs);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Fields
 * @param Tabs
 * @return
 * @throws PDException
 */
@Override
protected Record CalculateRec(Vector<String> Fields, Vector <String> Tabs) throws PDException
{
PDFolders F=new PDFolders(getDrv(), Tabs.get(0));
Record R=F.getRecSum().CopyMono();   
if (Fields.isEmpty())    
    return R;
Record R2=new Record();
//R.initList();
//Attribute nextAttr = R.nextAttr();
//while (nextAttr!=null)
//    {
//    if (Fields.contains(nextAttr.getName()))
//        R2.addAttr(nextAttr);
//    nextAttr = R.nextAttr();
//    }
Attribute attr;
for (int i = 0; i < Fields.size(); i++)
    {
    attr = R.getAttr(Fields.elementAt(i));
    if (attr!=null)
        R2.addAttr(attr);
    }
if (R2.NumAttr()==0)
    PDException.GenPDException("Empty_or_Erroneus_list_of_Fields", null);
if (R2.ContainsAttr(fPDID))
    {
    R2.getAttr(fPDID).setName(Tabs.get(0)+"."+fPDID);
    }
else
    {
    Attribute Atr=getRecord().getAttr(fPDID).Copy();
    Atr.setName(Tabs.get(0)+"."+fPDID);
    R2.addAttr(Atr);
    }
return(R2);
    
//PDFolders F=new PDFolders(getDrv(), Tabs.get(0));
//Record R2=F.getRecSum().CopyMono();
//if (R2.ContainsAttr(fPDID))
//    {
//    R2.getAttr(fPDID).setName(Tabs.get(0)+"."+fPDID);
//    }
//else
//    {
//    Attribute Atr=getRecord().getAttr(fPDID).Copy();
//    Atr.setName(Tabs.get(0)+"."+fPDID);
//    R2.addAttr(Atr);
//    }
//return(R2);
}
//-------------------------------------------------------------------------
/**
 *
 * @param tableListSQL
 * @param OPDTabs
 * @return
 * @throws prodoc.PDException
 */
@Override
protected Conditions NeededMoreConds(List<String> tableListSQL, Vector <String> OPDTabs) throws PDException
{
Conditions Cs=new Conditions(); 
if (!tableListSQL.contains("SUBTYPES"))
    {
    Condition C=new Condition(PDFolders.fFOLDTYPE, Condition.cEQUAL, OPDTabs.get(0));
    Cs.addCondition(C);
    }
Condition CondAcl=new Condition(PDFolders.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
Cs.addCondition(CondAcl);
if (OPDTabs.size()!=1)
    { 
    PDFolders F=new PDFolders(getDrv(), OPDTabs.get(0));    
    Conditions CondTyps=new Conditions();
    ArrayList ListTip=F.getTypeDefs();
    ArrayList ListAttr=F.getTypeRecs();
    for (int NumTabsDef = 0; NumTabsDef < ListTip.size(); NumTabsDef++)
            {
            Record R= (Record)ListTip.get(NumTabsDef);
            Attribute AttrNomTab=R.getAttr(PDObjDefs.fNAME);
            String Typ =(String) AttrNomTab.getValue();
            if (!Typ.equalsIgnoreCase(getTableName()))
                {
                Condition Con=new Condition(getTableName()+"."+fPDID, Typ+"."+fPDID);
                CondTyps.addCondition(Con);
                }
            Record AttrsTab= ((Record)ListAttr.get(NumTabsDef)).Copy();
            AttrsTab.initList();
            Attribute Attr;
            for (int i = 0; i < AttrsTab.NumAttr(); i++)
                {
                Attr=AttrsTab.nextAttr();
                if (Attr.isMultivalued())
                    {
                    if (Cs.UsedAttr(Attr.getName()))
                        {
                        String MultiName=PDObjDefs.genMultValNam(Typ, Attr.getName());
                        Condition Con=new Condition(getTableName()+"."+fPDID, MultiName+"."+fPDID);
                        CondTyps.addCondition(Con);
                        OPDTabs.add(MultiName);
                        }                            
                    }                    
                }
            }
    Cs.addCondition(CondTyps);
    }
return(Cs);
}
//-------------------------------------------------------------------------
}
