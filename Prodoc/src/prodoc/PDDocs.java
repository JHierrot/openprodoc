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

import java.io.*;
import java.util.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jhierrot
 */
public class PDDocs extends ObjPD
{
final int BUFFSIZE=204800;
private static final String DEFTABNAME="PD_DOCS";
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
public static final String fDOCDATE="DocDate";
/**
 *
 */
public static final String fLOCKEDBY="LockedBy";
/**
 *
 */
public static final String fPURGEDATE="PurgeDate";
/**
 *
 */
public static final String fDOCTYPE="DocType";
/**
 *
 */
public static final String fREPOSIT="Reposit";
/**
 *
 */
public static final String fACL="ACL";
/**
 *
 */
public static final String fMIMETYPE="MimeType";
/**
 *
 */
public static final String fNAME="Name";
/**
 *
 */
public static final String fPARENTID="ParentId";
/**
 *
 */
public static final String fVERSION="Version";
/**
 *
 */
public static final String fSTATUS="Status";
/**
 *
 */
public static final String fSTATUS_DEL="DELETED";
/**
 *
 */
public static final String fSTATUS_LASTDEL="DELETE_A";

/**
 *
 */
private PDDocs PreviousVals=null;
/**
 *
 */
static private Record DocsStruct=null;
/**
 *
 */
static private Record RecVer=null;
/**
 *
 */
private String PDId;
/**
 *
 */
private String Title;
/**
 *
 */
private Date   DocDate;
/**
 *
 */
private Date   PDDate;
/**
 *
 */
private String LockedBy;
/**
 *
 */
private Date   PurgeDate;
/**
 *
 */
private String ACL;
/**
 *
 */
private String DocType=DEFTABNAME;
/**
 *
 */
private String Reposit;
/**
 *
 */
private String MimeType;
/**
 *
 */
private String Name;
/**
 *
 */
private String ParentId;
/**
 *
 */
private String Version;
/**
 *
 */
private String Status;

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
/**
 *
 */
private String FilePath=null;
/**
 *
 */
private InputStream FileStream=null;

static private ObjectsCache DocsObjectsCache = null;

//-------------------------------------------------------------------------
/**
 *
 * @param Drv
 * @throws PDException  
 */
public PDDocs(DriverGeneric Drv) throws PDException
{
super(Drv);
setDocType(getTableName());
}
//-------------------------------------------------------------------------
/**
 * Assign the values of the record to the fields of the object
 */
public void assignValues(Record Rec) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.assignValues>:"+Rec);
setDocType((String) Rec.getAttr(fDOCTYPE).getValue());
setPDId((String) Rec.getAttr(fPDID).getValue());
setTitle((String) Rec.getAttr(fTITLE).getValue());
setDocDate((Date) Rec.getAttr(fDOCDATE).getValue());
setPDDate((Date) Rec.getAttr(fPDDATE).getValue());
setPDAutor((String) Rec.getAttr(fPDAUTOR).getValue());
setLockedBy((String) Rec.getAttr(fLOCKEDBY).getValue());
setPurgeDate((Date) Rec.getAttr(fPURGEDATE).getValue());
setACL((String) Rec.getAttr(fACL).getValue());
setReposit((String) Rec.getAttr(fREPOSIT).getValue());
setMimeType((String) Rec.getAttr(fMIMETYPE).getValue());
setName((String) Rec.getAttr(fNAME).getValue());
setParentId((String) Rec.getAttr(fPARENTID).getValue());
setVersion((String) Rec.getAttr(fVERSION).getValue());
setStatus((String) Rec.getAttr(fSTATUS).getValue());
if (!getDocType().equalsIgnoreCase(getTableName()))
    getRecSum().assign(Rec);
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.assignValues<");
}
//-------------------------------------------------------------------------
/**
 * returns the unique identifier of the document
* @return the PDId
*/
public String getPDId()
{
return PDId;
}
//-------------------------------------------------------------------------
/**
 * Sets the unique identifier of the document
 * @param pPDId the new identifier to set.
 */
public void setPDId(String pPDId)
{
this.PDId = pPDId;
}
//-------------------------------------------------------------------------
/**
 * returns the title of the document
* @return the Title
*/
public String getTitle()
{
return Title;
}
//-------------------------------------------------------------------------
/**
 * sets the title of the document
 * @param pTitle the new title to set
*/
public void setTitle(String pTitle)
{
this.Title = pTitle;
}
//-------------------------------------------------------------------------
/**
 * return the name of the table used to store the metadata for this object
 * object "method" needed because static overloading doesn't work in java
 * @return the name of the table
 */
@Override
public String getTabName()
{
return(getTableName());
}
//-------------------------------------------------------------------------
/**
 * Generates de Name for de Versions Table
 * @return the generated name
 */
public String getTabNameVer()
{
//return(getTableName()+"_V");
return(getDocType()+"_V");
}
//-------------------------------------------------------------------------
/**
 * Generates de Name for de Versions Table using the parameter
 * @param DocTyp
 * @return the generated name
 */
static protected String getTabNameVer(String DocTyp)
{
return(DocTyp+"_V");
}
//-------------------------------------------------------------------------
/**
 * return the name of the table used to store the metadata for this object
 * static equivalent method
 * @return the name of the table
 */
static public String getTableName()
{
return(DEFTABNAME);
}
//-------------------------------------------------------------------------
/**
 * Constructs a record with all the attributes of the document type and the values
 * of the attributes asigned using the setter of the class.
 * @throws PDException
 * @return a record with all the attributes of the document type
 */
@Override
public Record getRecord() throws PDException
{
return(getRecSum());
}
//-------------------------------------------------------------------------
/**
 * Return a copy of the static structure of attributes for the class
 * @throws PDException
 * @return a copy of the static structure of attributes for the class
 */
@Override
public Record getRecordStruct() throws PDException
{
if (DocsStruct==null)
    DocsStruct=CreateRecordStructPDDocs();
return(DocsStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Return a copy of the static structure of attributes for the class
 * @throws PDException
 * @return a copy of the static structure of attributes for the class
 */
static public Record getRecordStructPDDocs() throws PDException
{
if (DocsStruct==null)
    DocsStruct=CreateRecordStructPDDocs();
return(DocsStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Return a Copy of structure of standard version table
 * @return a Copy of structure of standard version table
 * @throws PDException
 */
public Record getRecordStructVer() throws PDException
{
if (RecVer==null)
    {
    RecVer=getRecordStruct();
    Attribute A=RecVer.getAttr(fVERSION);
    A.setPrimKey(true);
    }
return(RecVer.Copy());
}
//-------------------------------------------------------------------------
/**
 * Construct the conditions for a query to retrieve the actual object from the database
 * the constructed query searchs by the unique id/index PDID
 * @throws PDException if there is no PDID value
 * @return the constructed conditions
 */
protected Conditions getConditions() throws PDException
{
Conditions ListCond=new Conditions();
if (getPDId()==null || getPDId().length()==0 )
    throw new PDException("Id documento requerido");
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Construct the conditions for a query to retrieve all the objects from the database
 * whith the indicated name (including wildcards ( * and ? )
 * @throws PDException if there is no PDID value
 * @return the constructed conditions
 */
protected Conditions getConditionsLike(String Name) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cLIKE, VerifyWildCards(Name)));
Condition CondAcl=new Condition(PDGroups.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ListCond.addCondition(CondAcl);
return(ListCond);
}
//-------------------------------------------------------------------------
/*
 * Assign the identifier for this object (pdid)
 * @param Ident
 */
@Override
protected void AsignKey(String Ident)
{
setPDId(Ident);
}
/**
* @return the DocDate
*/
public Date getDocDate()
{
return DocDate;
}
/**
* @param DocDate the DocDate to set
*/
public void setDocDate(Date DocDate)
{
this.DocDate = DocDate;
}
/**
* @return the LockedBy
*/
public String getLockedBy()
{
return LockedBy;
}
/**
* @param LockedBy the LockedBy to set
*/
public void setLockedBy(String LockedBy)
{
this.LockedBy = LockedBy;
}
/**
* @return the PurgeDate
*/
public Date getPurgeDate()
{
return PurgeDate;
}
/**
* @param PurgeDate the PurgeDate to set
*/
public void setPurgeDate(Date PurgeDate)
{
this.PurgeDate = PurgeDate;
}
/**
* @return the ACL
*/
public String getACL()
{
return ACL;
}
/**
* @param ACL the ACL to set
*/
public void setACL(String ACL)
{
this.ACL = ACL;
}
/**
* @return the DocType
*/
public String getDocType()
{
return DocType;
}
//-------------------------------------------------------------------------
/**
* This method creates all the aditional elements in the database to store the default
* information for the documents and al the integrity elements.
* @throws PDException
*/
@Override
protected void InstallMulti() throws PDException
{
getDrv().AddIntegrity(getTabName(), fACL,     PDACL.getTableName(),        PDACL.fNAME);
getDrv().AddIntegrity(getTabName(), fDOCTYPE, PDObjDefs.getTableName(),    PDObjDefs.fNAME);
getDrv().AddIntegrity(getTabName(), fREPOSIT, PDRepository.getTableName(), PDRepository.fNAME);
getDrv().AddIntegrity(getTabName(), fMIMETYPE, PDMimeType.getTableName(),  PDMimeType.fNAME);
getDrv().AddIntegrity(getTabName(), fPARENTID, PDFolders.getTableName(),   PDFolders.fPDID);
getDrv().CreateTable(getTabNameVer(), getRecordStructVer());
//getDrv().AddIntegrity(getTabNameVer(), fPDID,  getTabName(),   fPDID);
getDrv().AddIntegrity(getTabNameVer(), fACL,     PDACL.getTableName(),        PDACL.fNAME);
getDrv().AddIntegrity(getTabNameVer(), fDOCTYPE, PDObjDefs.getTableName(),    PDObjDefs.fNAME);
getDrv().AddIntegrity(getTabNameVer(), fREPOSIT, PDRepository.getTableName(), PDRepository.fNAME);
getDrv().AddIntegrity(getTabNameVer(), fMIMETYPE, PDMimeType.getTableName(),  PDMimeType.fNAME);
// integrity with container excluded to allow delete and moving of documents and folder
}
//-------------------------------------------------------------------------
/**
* This method destroy all the aditional elements to store metadata
* @throws PDException
*/
@Override
protected void unInstallMulti() throws PDException
{
getDrv().DropTable(getTabNameVer());
}
//-------------------------------------------------------------------------
/**
* @return the Reposit
*/
public String getReposit()
{
return Reposit;
}
/**
* @param Reposit the Reposit to set
*/
public void setReposit(String Reposit)
{
this.Reposit = Reposit;
}
//-------------------------------------------------------------------------
/**
 * @return the MimeType
 */
public String getMimeType()
{
return MimeType;
}
//-------------------------------------------------------------------------
/**
* @param MimeType the MimeType to set
*/
public void setMimeType(String MimeType)
{
this.MimeType = MimeType;
}
//-------------------------------------------------------------------------
/**
* @return the Name
*/
public String getName()
{
return Name;
}
//-------------------------------------------------------------------------
/**
* @param Name the Name to set
*/
public void setName(String Name)
{
this.Name = Name;
}
//-------------------------------------------------------------------------
/**
 * This method verifies if the user has permissions to create documents in general.
 * The permision to store the document in a folder depends of the foilder's ACL
 * @throws PDException is the user can't create documents
 */
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getRol().isAllowCreateDoc() )
   PDExceptionFunc.GenPDException("Document_creation_not_allowed_to_user",getName());
PDObjDefs D=new PDObjDefs(getDrv());
D.Load(getDocType());    
if (!getDrv().getUser().getAclList().containsKey(D.getACL()))
    PDExceptionFunc.GenPDException("Document_creation_not_allowed_to_user",getDocType());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(D.getACL());
if (Perm.intValue()<PDACL.pUPDATE)
    PDExceptionFunc.GenPDException("Document_creation_not_allowed_to_user",getDocType());
}
//-------------------------------------------------------------------------
/**
 * This method verifies if the user has permissions to delete documents in general.
 * The permision to delete the document from a folder depends of the foilder's ACL
 * @throws PDException is the user can't delete documents
 */
protected void VerifyAllowedDel() throws PDException
{
// TOD?O: to control permisions over document types
if (!getDrv().getUser().getRol().isAllowMaintainDoc() )
   PDExceptionFunc.GenPDException("Document_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
/**
 * This method verifies if the user has permissions to modify documents in general.
 * @throws PDException is the user can't modify documents
 */
protected void VerifyAllowedUpd() throws PDException
{
// TOD?O: to control permisions over document types
if (!getDrv().getUser().getRol().isAllowMaintainDoc() )
   PDExceptionFunc.GenPDException("Document_modification_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
/** Returns the code of the folder in wich it is stored the document
* @return the ParentId
*/
public String getParentId()
{
return ParentId;
}
//-------------------------------------------------------------------------
/** Set the code of the folder in wich it is stored the document.
 * the user must have permision over the folder to be allowed to store de document
* @param ParentId the ParentId to set
*/
public void setParentId(String ParentId)
{
this.ParentId = ParentId;
}
//-------------------------------------------------------------------------
/** returns the default order for querys for this object
 * @return the default order for querys for this object
 */
protected String getDefaultOrder()
{
return(fTITLE);
}
//-------------------------------------------------------------------------
/**
 * Constructor indicating the document type.
 * @param Drv the DriverGeneric used to retrive and store information and definitions
 *        for this document type
 * @param pDocType The name of the document type defined in the database.
 * @throws PDException if the document type doesn't exist or there is a comunication problem
 */
public PDDocs(DriverGeneric Drv, String pDocType) throws PDException
{
super(Drv);
setDocType(pDocType);
}
//-------------------------------------------------------------------------
/**
 * Assign the name of the type used. Deletes the information about attributes structure, repository,etc.
 * @param pDocType The name of the document type defined in the database.
 * @throws PDException  
*/
public final void setDocType(String pDocType) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.setDocType:"+DocType);
if (!DocType.equalsIgnoreCase(pDocType))
    {
    DocType = pDocType;
    LoadDef(DocType);
    }
//this.TypeDefs=null;
//this.TypeRecs=null;
}
//-------------------------------------------------------------------------
/** Return an ordered list of the hierarchy of document types from whom this
 * document type inherit its attributes
 * @return the TypeDefs the array list with the names of document types
 * @throws PDException if there is a problem comunicating with the server
*/
public ArrayList getTypeDefs() throws PDException
{
if (TypeDefs==null)
    LoadDef(getDocType());
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
    LoadDef(getDocType());
return TypeRecs;
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
    PDLog.Debug("PdDocs.LoadDef>:"+tableName);
//setDocType(tableName);
TypeDefs=new ArrayList();
TypeRecs=new ArrayList();
getDrv().LoadDef(tableName, getTypeDefs(), getTypeRecs());
RecSum=new Record();
for (int i = 0; i < getTypeRecs().size(); i++)
    {
    RecSum.addRecord(((Record)getTypeRecs().get(i)).Copy());
    }
RecSum.getAttr(fDOCTYPE).setValue(getDocType());
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.LoadDef<:"+RecSum);
}
//-------------------------------------------------------------------------
/**
 * @return the RecSum
 * @throws PDException
*/
public Record getRecSum() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.getRecSum>:"+getDocType());
if (RecSum==null)
   LoadDef(getDocType());
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.getRecSum="+RecSum);
RecSum.getAttr(fPDID).setValue(getPDId());
RecSum.getAttr(fTITLE).setValue(getTitle());
RecSum.getAttr(fDOCDATE).setValue(getDocDate());
RecSum.getAttr(fLOCKEDBY).setValue(getLockedBy());
RecSum.getAttr(fPURGEDATE).setValue(getPurgeDate());
RecSum.getAttr(fACL).setValue(getACL());
RecSum.getAttr(fDOCTYPE).setValue(getDocType());
RecSum.getAttr(fREPOSIT).setValue(getReposit());
RecSum.getAttr(fMIMETYPE).setValue(getMimeType());
RecSum.getAttr(fNAME).setValue(getName());
RecSum.getAttr(fVERSION).setValue(getVersion());
RecSum.getAttr(fSTATUS).setValue(getStatus());
RecSum.getAttr(fPARENTID).setValue(getParentId());
getCommonValues(RecSum);
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.getRecSum<");
return RecSum;
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDDocs() throws PDException
{
if (DocsStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDID, fPDID, "Unique_identifier", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDOCTYPE, "Document_Type", "Document_Type", Attribute.tSTRING, true, getTableName(), 32, false, false, false));
    R.addAttr( new Attribute(fDOCDATE, "Document_Date", "Document_Date", Attribute.tDATE, false, null, 0, false, false, true));
    R.addAttr( new Attribute(fTITLE, "Document_Title", "Document_Title", Attribute.tSTRING, true, null, 254, false, false, true));
    R.addAttr( new Attribute(fLOCKEDBY, "Lock_user", "User_who_locks_the_document_to_Edit", Attribute.tSTRING, false, null, 32, false, false, false));
    R.addAttr( new Attribute(fPURGEDATE, "Purge_date", "Date_to_destroy_the_document", Attribute.tDATE, false, null, 0, false, false, true));
    R.addAttr( new Attribute(fACL, "Document_ACL", "Document_ACL", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fREPOSIT, "Repository", "Repository_to_store_documents", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fMIMETYPE, "MimeType", "Document_MimeType", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fNAME, "File_name", "File_name", Attribute.tSTRING, true, null, 254, false, false, false));
    R.addAttr( new Attribute(fPARENTID, "Folder", "Folder", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fVERSION, "Version", "Name_or_Identifier_of_version", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fSTATUS, "Status", "Internal_Status", Attribute.tSTRING, false, null, 8, false, false, false));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(DocsStruct);
}
//-------------------------------------------------------------------------
/**
 * Return the docs in a folder
 * @param PDIdFold
 * @return a Cursor with the docs contained in the Folder
 * @throws PDException
 */
public Cursor getListContainedDocs(String PDIdFold) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.getListContainedDocs>:"+PDIdFold);
Condition CondParents=new Condition( fPARENTID, Condition.cEQUAL, PDIdFold);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTableName(), getRecordStruct(), Conds);
Cursor CursorId=getDrv().OpenCursor(Q);
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.getListContainedDocs<");
return(CursorId);
}
//-------------------------------------------------------------------------
/**
 * Assign the file to be uploaded when called insert or update.
 * @param pFilePath
 * @throws PDException
 */
public void setFile(String pFilePath) throws PDException
{
FilePath=pFilePath;
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID-
 * @param FolderPath path to recover/download the file
 * @return The complete name of the downloaded file
 * @throws PDException
 */
public String getFile(String FolderPath) throws PDException
{
PDDocs d=new PDDocs(getDrv());
d.LoadCurrent(getPDId());
if (d.getName()==null || d.getName().length()==0)
    {
    PDMimeType MT=new PDMimeType(getDrv());
    MT.Load(d.getMimeType());
    d.setName(getPDId()+"."+MT.getMimeCode());    
    }
if (FolderPath.charAt(FolderPath.length()-1)!=File.separatorChar)
    FolderPath+=File.separatorChar+getPDId()+d.getVersion()+d.getName();
else
    FolderPath+=getPDId()+d.getVersion()+d.getName();
StoreGeneric Rep=getDrv().getRepository(d.getReposit());
Rep.Connect();
File NewF=new File(FolderPath);
FileOutputStream OutCont=null;
try {
NewF.createNewFile();
OutCont=new FileOutputStream(NewF); 
d.getStream(OutCont);
} catch (IOException ex)
    {
     if (OutCont != null)
        {
        try {
        OutCont.close();
        } catch (IOException ex1)
            {}
        }
    throw new PDException(ex.getLocalizedMessage());
    }
try {
OutCont.close();
} catch (IOException ex)
    {
    throw new PDException(ex.getLocalizedMessage());
    }
Rep.Disconnect();
return(FolderPath);
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID-
 * @param FolderPath path to recover/download the file
 * @return The complete name of the downloaded file
 * @throws PDException
 */
public String getFileVer(String FolderPath, String Ver) throws PDException
{
PDDocs d=new PDDocs(getDrv());
d.LoadVersion(getPDId(), Ver);
if (FolderPath.charAt(FolderPath.length()-1)!=File.separatorChar)
    FolderPath+=File.separatorChar+getPDId()+d.getVersion()+d.getName();
else
    FolderPath+=getPDId()+d.getVersion()+d.getName();
StoreGeneric Rep=getDrv().getRepository(d.getReposit());
Rep.Connect();
File NewF=new File(FolderPath);
FileOutputStream OutCont=null;
try {
NewF.createNewFile();
OutCont=new FileOutputStream(NewF); 
d.getStreamVer(OutCont);
} catch (IOException ex)
    {
     if (OutCont != null)
        {
        try {
        OutCont.close();
        } catch (IOException ex1)
            {}
        }
    throw new PDException(ex.getLocalizedMessage());
    }
try {
OutCont.close();
} catch (IOException ex)
    {
    throw new PDException(ex.getLocalizedMessage());
    }
Rep.Disconnect();
return(FolderPath);
}
//-------------------------------------------------------------------------
/**
 * Assign the file to be uploaded when called insert or update.
 * @param Bytes
 * @throws PDException
 */
public void setStream(InputStream Bytes) throws PDException
{
FileStream=Bytes;
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID-
 * @param OutBytes 
 * @throws PDException
 */
public void getStream(OutputStream OutBytes) throws PDException
{
InputStream InBytes=null;
LoadCurrent(getPDId());
StoreGeneric Rep=getDrv().getRepository(getReposit());
Rep.Connect();
InBytes=Rep.Retrieve(getPDId(), getVersion());
byte[] buffer = new byte[BUFFSIZE];
int bytesLeidos;
try {
while ((bytesLeidos = InBytes.read(buffer,0,BUFFSIZE)) != -1)
    {
    OutBytes.write(buffer,0,bytesLeidos);
    }
InBytes.close();
OutBytes.flush();
OutBytes.close();
} catch(Exception ex)
    {
    Rep.Disconnect();
    throw new PDException(ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID-
 * @param OutBytes 
 * @throws PDException
 */
public void getStreamVer(OutputStream OutBytes) throws PDException
{
InputStream InBytes=null;
LoadVersion(getPDId(), getVersion());
StoreGeneric Rep=getDrv().getRepository(getReposit());
Rep.Connect();
InBytes=Rep.Retrieve(getPDId(), getVersion());
byte[] buffer = new byte[BUFFSIZE];
int bytesLeidos;
try {
while ((bytesLeidos = InBytes.read(buffer,0,BUFFSIZE)) != -1)
    {
    OutBytes.write(buffer,0,bytesLeidos);
    }
InBytes.close();
OutBytes.flush();
OutBytes.close();
} catch(Exception ex)
    {
    Rep.Disconnect();
    throw new PDException(ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 *
 * @param VersionName
 * @throws PDException
 */
public void Checkin(String VersionName) throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Checkin>:"+getPDId());
if (VersionName==null || VersionName.length()==0 )
    PDExceptionFunc.GenPDException("Version_identifier_required", getPDId());
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
String Id=getPDId();
this.Clear();
setPDId(Id);
PDDocs TobeUpdated=new PDDocs(getDrv());
TobeUpdated.LoadFull(Id);
if (TobeUpdated.getLockedBy()!=null && !TobeUpdated.getLockedBy().equalsIgnoreCase(getDrv().getUser().getName()))
   PDExceptionFunc.GenPDException("Document_locked_by_another_user", getPDId());
Record RecTot=TobeUpdated.getRecSum();
assignValues(RecTot);
Attribute Attr=RecTot.getAttr(fVERSION);
Attr.setValue(VersionName);
Attr=RecTot.getAttr(fLOCKEDBY);
Attr.setValue("");
StoreGeneric Rep=getDrv().getRepository(getReposit());
Rep.Connect();
Rep.Rename(PDId, getDrv().getUser().getName(), PDId, VersionName);
Rep.Disconnect();
updateFragments(RecTot, Id);
UpdateVersion(Id, getDrv().getUser().getName(), RecTot);
MultiDelete(Id, getDrv().getUser().getName());
MultiInsert(RecTot);
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_checkin_Document",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Checkin<:"+getPDId());
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
public void Checkout() throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Checkout>:"+getPDId());
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
String Id=getPDId();
Clear();
setPDId(Id);
PDDocs TobeUpdated=new PDDocs(getDrv());
TobeUpdated.LoadFull(getPDId()); // select for update to avoid cocurrency ?
if (!getDrv().getUser().getAclList().containsKey(TobeUpdated.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_to_checkout_document", getPDId());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeUpdated.getACL());
if (Perm.intValue()<=PDACL.pREAD)
   PDExceptionFunc.GenPDException("User_without_permissions_to_checkout_document", getPDId());
if (TobeUpdated.getLockedBy()!=null && TobeUpdated.getLockedBy().length()!=0)
   PDExceptionFunc.GenPDException("Document_previously_checkout", getPDId());
TobeUpdated.setLockedBy(getDrv().getUser().getName());
InsertVersion(Id, getDrv().getUser().getName(), TobeUpdated.getRecSum());
String Vers=TobeUpdated.getVersion();
setPDId(Id);
setVersion(Vers);
//setLockedBy(getDrv().getUser().getName());
Record Rec=getRecordStruct();
Attribute Attr=Rec.getAttr(fPDID);
Attr.setValue(Id);
Attr=Rec.getAttr(fVERSION);
Attr.setValue(Vers);
Attr=Rec.getAttr(fLOCKEDBY);
Attr.setValue(getDrv().getUser().getName());
Attr=Rec.getAttr(fDOCTYPE);
Attr.setValue(TobeUpdated.getDocType());
getDrv().UpdateRecord(getTabName(), Rec, getConditionsVer());
Record Mult=TobeUpdated.getRecSum().Copy();
Mult.getAttr(fVERSION).setValue(getDrv().getUser().getName());
TobeUpdated.MultiInsert(Mult);
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_during_checkout_Document", Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Checkout<:"+getPDId());
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
public void CancelCheckout() throws PDException
{
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.CancelCheckout>:"+getPDId());
try {
VerifyAllowedUpd();
if (InTransLocal)
    getDrv().IniciarTrans();
String Id=getPDId();
this.Clear();
setPDId(Id);
PDDocs TobeUpdated=new PDDocs(getDrv());
TobeUpdated.Load(getPDId());
if (TobeUpdated.getLockedBy()==null || !TobeUpdated.getLockedBy().equalsIgnoreCase(getDrv().getUser().getName()))
   PDExceptionFunc.GenPDException("Document_locked_by_another_user",getPDId());
Record r=getRecordStruct();
Attribute Attr=r.getAttr(fLOCKEDBY);
Attr.setValue("");
Attr=r.getAttr(fDOCTYPE);
Attr.setValue(TobeUpdated.getDocType());
//setLockedBy("");
Conditions Cond=getConditions();
getDrv().UpdateRecord(getTabName(), r, Cond);
setDocType(TobeUpdated.getDocType());
DeleteVersion(getTabName(), Id, getDrv().getUser().getName());
StoreGeneric Rep=getDrv().getRepository(TobeUpdated.getReposit());
Rep.Connect();
MultiDelete(getPDId(), getDrv().getUser().getName());
Rep.Delete(PDId, getDrv().getUser().getName());
Rep.Disconnect();
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDExceptionFunc.GenPDException("Error_during_cancelcheckout_Document",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.CancelCheckout<:"+getPDId());
}
//-------------------------------------------------------------------------
/**
 * Deletes all the multivalued atributes of the current element
 * @param Id2Del PDId of document to delete
 * @param Vers  Version of Document to delete. When null, deletes ALL versions
 * @throws PDException
 */
private void MultiDelete(String Id2Del, String Vers) throws PDException
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
        if (Vers!=null)
            Conds.addCondition(new Condition(fVERSION, Condition.cEQUAL, Vers));
        getDrv().DeleteRecord(MultiName, Conds);
        }
     }
}
//-------------------------------------------------------------------------
/**
 * Create a document, including metadata and the path o strean asigned previously
 * @throws PDException
 */
@Override
public void insert() throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.insert>:"+getPDId());
VerifyAllowedIns();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
if (getPDId()==null || getPDId().length()==0)
    setPDId(GenerateId());
String Fold=getParentId();
if (Fold==null || Fold.length()==0)
    setParentId(getDrv().getUser().getUserFolder());
if (!(getDrv().getUser().getName().equals("Install") && getDrv().getUser().getAclList()==null))
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
if (getReposit()==null || getReposit().length()==0 )
    setReposit(getDrv().getAssignedRepos(getDocType()));
AddLogFields();
setVersion("1.0");
if (getName()==null || getName().length()==0)
    if (FilePath!=null)
        {
        File f=new File(FilePath);
        setName(f.getName());
        }
    else
        setName(getPDId());
if (getMimeType()==null || getMimeType().length()==0)
    {
    PDMimeType MT=new PDMimeType(getDrv());   
    MT.SolveExt(getName().substring(getName().lastIndexOf('.')+1));
    setMimeType(MT.getName());
    }
Record Rec=getRecSum();
insertFragments(Rec);
InsertVersion(getPDId(), getVersion(), Rec);
MultiInsert(Rec);
StoreGeneric Rep=getDrv().getRepository(getReposit());
Rep.Connect();
if (FileStream!=null)
    Rep.Insert(PDId, getVersion(), FileStream);
else if (FilePath!=null)
    Rep.Insert(PDId, getVersion(), FilePath);
Rep.Disconnect();
FilePath=null;
FileStream=null;
getObjCache().put(getKey(), getRecord());
} catch (Exception Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_inserting_Document", Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.insert<:"+getPDId());
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
        RecSave.addAttr(Rec.getAttr(fVERSION));
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
 * Creates a new Version in the version table
 * @param Id PDId of Document to insert
 * @param Ver Version of Document to insert
 * @param Rec Complete Record of metadata to insert
 * @throws PDException
 */
private void InsertVersion(String Id, String Ver, Record Rec) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.InsertVersion>:"+Id+"/"+Ver+"="+Rec);
Record RecV=Rec.Copy();
Attribute Attr=RecV.getAttr(fVERSION);
Attr.setValue(Ver);
Attr=RecV.getAttr(fDOCTYPE);
getDrv().InsertRecord(getTabNameVer((String)Attr.getValue()), RecV.CopyMono());
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.InsertVersion<:"+Id+"/"+Ver+"="+Rec);
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
 *
 * @param Ident
 * @return
 * @throws PDException
 */
@Override
public Record Load(String Ident)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Load>:"+Ident);
AsignKey(Ident);
Query LoadAct;
Cursor Cur;
Record r=(Record)getObjCache().get(Ident);
if (r==null) // maintained in cache only the "common" values, not the "under edition"
    {
    LoadAct=new Query(getTabName(), getRecordStruct(),getConditions());
    Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    getObjCache().put(Ident, r);
    }
String ActACL=(String)r.getAttr(fACL).getValue();
if (!getDrv().getUser().getAclList().containsKey(ActACL))
    {
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",Ident);
    }
Attribute UsuBloq=r.getAttr(fLOCKEDBY);
if (UsuBloq.getValue()!=null && ((String)UsuBloq.getValue()).equalsIgnoreCase(getDrv().getUser().getName()))
    {// locked by actual user, return in-edition metadata
//    LoadAct=new Query(getTabNameVer(), getRecordStruct(), getConditions(), null);
    Conditions Cond=getConditions();
    Cond.addCondition(new Condition(fVERSION, Condition.cEQUAL, getDrv().getUser().getName()));
    Attribute Attr=r.getAttr(fDOCTYPE);
    LoadAct=new Query(getTabNameVer((String)Attr.getValue()), getRecordStruct(), Cond, null);
    Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    }
if (r!=null)
    assignValues(r);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Load<");
return(r);
}
//-------------------------------------------------------------------------
/**
 * Load the published document, in edition or not
 * @param Ident
 * @return
 * @throws PDException
 */
public Record LoadCurrent(String Ident)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.LoadCurrent>:"+Ident);
AsignKey(Ident);
Query LoadAct=new Query(getTabName(), getRecordStruct(),getConditions());
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record r=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
String ActACL=(String)r.getAttr(fACL).getValue();
if (!getDrv().getUser().getAclList().containsKey(ActACL))
    {
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",Ident);
    }
if (r!=null)
    assignValues(r);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.LoadCurrent<:"+Ident);
return(r);
}
//-------------------------------------------------------------------------
/**
 * Load the published document, in edition or not
 * @param Ident
 * @param Vers 
 * @return
 * @throws PDException
 */
public Record LoadVersion(String Ident, String Vers)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.LoadVersion>:"+Ident+"/"+Vers);
Record r=Load(Ident);
Conditions Cond=getConditions();
Cond.addCondition(new Condition(fVERSION, Condition.cEQUAL, Vers));
Attribute Attr=r.getAttr(fDOCTYPE);
Query LoadAct=new Query(getTabNameVer((String)Attr.getValue()), getRecSum().CopyMono(), Cond, null);
Cursor Cur=getDrv().OpenCursor(LoadAct);
r=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
String ActACL=(String)r.getAttr(fACL).getValue();
if (!getDrv().getUser().getAclList().containsKey(ActACL))
    {
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",Ident);
    }
if (r!=null)
    {
    assignValues(r);
    MultiLoad(getRecSum());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.LoadVersion<:"+Ident+"/"+Vers);
return(r);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @return
 * @throws PDException
 */
public Record LoadFull(String Ident) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.LoadFull>:"+Ident);
Record r=Load(Ident);
getTypeDefs();
Attribute UsuBloq=r.getAttr(fLOCKEDBY);
if (UsuBloq.getValue()!=null &&  ((String)UsuBloq.getValue()).equalsIgnoreCase(getDrv().getUser().getName()))
    {// locked by actual user, return in-edition metadata
    Conditions Cond=getConditions();
    Cond.addCondition(new Condition(fVERSION, Condition.cEQUAL, getDrv().getUser().getName()));
     Attribute Attr=r.getAttr(fDOCTYPE);
    Query LoadAct=new Query(getTabNameVer((String)Attr.getValue()), getRecSum().CopyMono(), Cond, null);
    Cursor Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    if (r!=null)
        {
        MultiLoad(r);
        assignValues(r);
        }
    if (PDLog.isDebug())
       PDLog.Debug("PDDocs.LoadFull<:"+Ident);
    return(r);
    }
if (getTypeDefs().size()>1) // If size==1, Load is enough
    {
    Conditions Conds=getConditions();
    Vector ListTabs=new Vector();
    for (int i = 0; i<getTypeDefs().size(); i++)
        {
        Record TypDef=(Record)getTypeDefs().get(i);
        ListTabs.add((String)TypDef.getAttr(PDObjDefs.fNAME).getValue());
        if (! ((String)ListTabs.elementAt(i)).equals(PDDocs.getTableName()))
            {
            Condition Con=new Condition(PDDocs.getTableName()+"."+PDFolders.fPDID,
                                        (String)ListTabs.elementAt(i)+"."+PDFolders.fPDID ) ;
            Conds.addCondition(Con);
            }
        }
    Query LoadAct=new Query(ListTabs, getRecSum().CopyMono(), Conds, null);
    Cursor Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    if (r!=null)
        {
        MultiLoad(r);
        assignValues(r);
        }
    }
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.LoadFull<:"+Ident);
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
String Ver=(String)Rec.getAttr(fVERSION).getValue();
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
        Conds.addCondition(new Condition(fVERSION, Condition.cEQUAL, Ver));
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
 * @return the Version
 */
public String getVersion()
{
return Version;
}
//-------------------------------------------------------------------------
/**
 * @param pVersion the Version to set
 */
private void setVersion(String pVersion)
{
Version = pVersion;
}
//-------------------------------------------------------------------------
/**
 * Updates a new Version in the version table
 * @param Id PDIe of Document to Update
 * @param Ver Version of Document to Update
 * @param Rec Complete Record of metadata to Update
 * @throws PDException
 */
private void UpdateVersion(String Id, String Ver, Record Rec) throws PDException
{
// Not using getConditionsVer() because version pased  as parameter (User lock sometimes)
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.UpdateVersion >:"+Id+"/"+Ver);
Conditions ListCond=new Conditions();
if (Id==null || Id.length()==0 )
    PDExceptionFunc.GenPDException("Required_document_Id", null);
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, Id));
if (Ver!=null)
    ListCond.addCondition(new Condition(fVERSION, Condition.cEQUAL, Ver));
Attribute Attr=Rec.getAttr(fDOCTYPE);
getDrv().UpdateRecord(getTabNameVer((String)Attr.getValue()), Rec, ListCond);
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.UpdateVersion <");
}
//-------------------------------------------------------------------------
/**
 * Deletes a new Version in the version table
 * @param Id PDId of Document to delete
 * @param Ver Version of Document to delete
 * @throws PDException
 */
private void DeleteVersion(String DocTypeName, String Id, String Ver) throws PDException
{
// Not using getConditionsVer() because version pased  as parameter (User lock sometimes)
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.DeleteVersion >:"+Id+"/"+Ver);
Conditions ListCond=new Conditions();
if (Id==null || Id.length()==0 ||Ver==null || Ver.length()==0 )
    PDExceptionFunc.GenPDException("Required_document_Id", null);
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, Id));
ListCond.addCondition(new Condition(fVERSION, Condition.cEQUAL, Ver));
getDrv().DeleteRecord(getTabNameVer(DocTypeName), ListCond);
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.DeleteVersion <");
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
private Conditions getConditionsVer() throws PDException
{
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.getConditionsVer >");
Conditions ListCond=new Conditions();
if (getPDId()==null || getPDId().length()==0 )
    PDExceptionFunc.GenPDException("Required_document_Id", null);
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
ListCond.addCondition(new Condition(fVERSION, Condition.cEQUAL, getVersion()));
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.getConditionsVer <:"+ListCond);
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * update a locked document, including metadata and the path o strean asigned previously
 * @throws PDException
 */
@Override
public void update() throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.update>:"+getPDId());
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
String Id=getPDId();
PDDocs TobeUpdated=new PDDocs(getDrv());
//TobeUpdated.Load(Id);
TobeUpdated.LoadCurrent(Id);
if (TobeUpdated.getLockedBy()==null || !TobeUpdated.getLockedBy().equalsIgnoreCase(getDrv().getUser().getName()))
   PDExceptionFunc.GenPDException("Document_not_locked_by_user", getPDId());
AddLogFields();
setReposit(TobeUpdated.getReposit());
Record Rec=getRecSum().Copy();
Rec.delAttr(fVERSION);
Attribute Attr=Rec.getAttr(fDOCTYPE);
Attr.setValue(TobeUpdated.getDocType());
Attr=Rec.getAttr(fNAME);
if (FilePath!=null)
    {
    File f=new File(FilePath);
    Attr.setValue(f.getName());
    }
else
    {
    Attr.setValue(TobeUpdated.getName());
    }
UpdateVersion(getPDId(), getDrv().getUser().getName(), Rec);
MultiDelete(Id, getDrv().getUser().getName());
Rec=getRecSum().Copy();
Rec.getAttr(fVERSION).setValue(getDrv().getUser().getName());
MultiInsert(Rec);
StoreGeneric Rep=getDrv().getRepository(getReposit());
Rep.Connect();
if (FileStream!=null)
    Rep.Insert(PDId, getDrv().getUser().getName(), FileStream);
else if (FilePath!=null)
    Rep.Insert(PDId, getDrv().getUser().getName(), FilePath);
else
    {
//    PDDocs PublicDoc=new PDDocs(getDrv());
//    PublicDoc.LoadCurrent(Id);
    String OriginalVers=TobeUpdated.getVersion();
    Rep.Copy(Id, OriginalVers, Id, getDrv().getUser().getName());
    }
Rep.Disconnect();
FilePath=null;
FileStream=null;
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDExceptionFunc.GenPDException("Error_updating_Document",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.update <");
}
//-------------------------------------------------------------------------
/**
 *
 * @param RecTot
 * @throws PDException
 */
private void insertFragments(Record RecTot)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.insertFragments>:"+RecTot);
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=(Record)getTypeDefs().get(i);
    Record DatParc=(Record)getTypeRecs().get(i);
    if (i!=getTypeDefs().size()-1)
        {
        DatParc.addAttr(RecTot.getAttr(fPDID));
        }
    DatParc.assign(RecTot);
    getDrv().InsertRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc);
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.insertFragments<");
}
//-------------------------------------------------------------------------
/**
 *
 * @param Tables
 * @param Id
 * @throws PDException
 */
private void DeleteFragments(ArrayList Tables, String Id) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.DeleteFragments>:"+Tables+"-"+Id);
for (int i = 0; i <getTypeDefs().size(); i++)
    {
    Record TypDef=(Record)getTypeDefs().get(i);
    getDrv().DeleteRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), getConditions());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.DeleteFragments <");
}
//-------------------------------------------------------------------------
/**
 *
 * @param RecTot
 * @param Id
 * @throws PDException
 */
private void updateFragments(Record RecTot, String Id) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.updateFragments>:"+RecTot+"-"+Id);
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=(Record)getTypeDefs().get(i);
    Record DatParc=(Record)getTypeRecs().get(i);
    if (i!=getTypeDefs().size()-1)
        {
        DatParc.addAttr(RecTot.getAttr(fPDID));
        }
    DatParc.assign(RecTot);
    getDrv().UpdateRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc,  getConditions());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.updateFragments<");
}
//-------------------------------------------------------------------------
/**
 * Logically deletes de actual (with actual Id) documents
 * Move the documents to trashbin
 * @throws PDException
 */
@Override
public void delete() throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.delete>:"+getPDId());
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
String Id=getPDId();
Clear();
LoadFull(Id); 
if (!getDrv().getUser().getAclList().containsKey(getACL()))
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",getPDId());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(getACL());
if (Perm.intValue()<PDACL.pDELETE)
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",getPDId());
String Vers=getVersion();
Record R =getRecordStruct();
Attribute Attr=R.getAttr(fDOCTYPE);
Attr.setValue(getDocType());
Attr=R.getAttr(fSTATUS);
Attr.setValue(fSTATUS_DEL);
UpdateVersion(Id, null, R);
Attr.setValue(fSTATUS_LASTDEL); // actual version muts be indicated
UpdateVersion(Id, Vers, R);
DeleteFragments(TypeDefs, Id);
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_deleting_Document",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.delete <");

}
//-------------------------------------------------------------------------
/**
 * Logically UNdeletes de actual (with actual Id) documents
 * Move the documents FROM trashbin to original folder (o user folder if original folder deleted)
 * @param Id
 * @param DocTypename
 * @throws PDException
 */
public void UnDelete(String DocTypename, String Id) throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.UnDelete>:"+getPDId());
VerifyAllowedIns();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
Conditions Cond=new Conditions();
Cond.addCondition(new Condition(fPDID, Condition.cEQUAL, Id));
Cond.addCondition(new Condition(fSTATUS, Condition.cEQUAL, fSTATUS_LASTDEL));
Query LoadAct=new Query(getTabNameVer(DocTypename), getRecSum(), Cond, null);
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record Rec=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
if (Rec==null)
    PDExceptionFunc.GenPDException("Document_do_not_exist",Id);
Attribute Attr=Rec.getAttr(fACL);
String AclRec=(String)Attr.getValue();
if (!getDrv().getUser().getAclList().containsKey(AclRec))
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",Id);
Attr=Rec.getAttr(fSTATUS);
Attr.setValue("");
insertFragments(Rec);
Record R =getRecordStruct();
Attr=R.getAttr(fDOCTYPE);
Attr.setValue(Rec.getAttr(fDOCTYPE).getValue());
Attr=R.getAttr(fSTATUS);
Attr.setValue("");
UpdateVersion(Id, null, R);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_undeleting_Document",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.UnDelete <");
}
//-------------------------------------------------------------------------
/**
 * Permanently Destroy document and metadata
 * @param Id
 * @param DocTypename
 * @throws PDException
 */
public void Purge(String DocTypename, String Id) throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Purge>:"+getPDId());
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
PDDocs Doc2Purge=new PDDocs(getDrv());
Doc2Purge.LoadFull(Id);
Doc2Purge.MultiDelete(Id, null);
Cursor Cur=ListVersions(DocTypename, Id);
Record Rec=getDrv().NextRec(Cur);
while (Rec!=null)
    {
    String VerifDel=(String)Rec.getAttr(fSTATUS).getValue();
    if (!( VerifDel.equals(fSTATUS_DEL) || VerifDel.equals(fSTATUS_LASTDEL)) )
       PDException.GenPDException("Error_during_purge_of_document", Id);
    String Ver2Del=(String)Rec.getAttr(fVERSION).getValue();
    StoreGeneric Rep=getDrv().getRepository((String)Rec.getAttr(fREPOSIT).getValue());
    Rep.Connect();
    Rep.Delete(Id, Ver2Del);
    Rep.Disconnect();
    DeleteVersion(DocTypename, Id, Ver2Del);
    Rec=getDrv().NextRec(Cur);
    }
getDrv().CloseCursor(Cur);
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_during_purge_of_document", Id);
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Purge <");
}
//-------------------------------------------------------------------------
/**
* @return the Status
*/
public String getStatus()
{
return Status;
}
//-------------------------------------------------------------------------

/**
* @param Status the Status to set
*/
public void setStatus(String Status)
{
this.Status = Status;
}
//-------------------------------------------------------------------------
/**
 *
 * @param DocTypename
 * @param Id
 * @return
 * @throws PDException
 */
public Cursor ListVersions(String DocTypename, String Id) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListVersions>:"+DocTypename+"/"+Id);
Conditions Cond= new Conditions();
Cond.addCondition(new Condition(fPDID, Condition.cEQUAL, Id));
Cond.addCondition(new Condition(fACL, new HashSet(getDrv().getUser().getAclList().keySet())));
PDDocs Doc=new PDDocs(getDrv(), DocTypename);
Query LoadAct=new Query(getTabNameVer(DocTypename), Doc.getRecSum().CopyMono(), Cond, PDDocs.fPDDATE);
Cursor Cur=getDrv().OpenCursor(LoadAct);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListVersions <");
return(Cur);
}
//-------------------------------------------------------------------------
/**
 *
 * @param DocTypename
 * @return
 * @throws PDException
 */
public Cursor ListDeleted(String DocTypename) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListDeleted>:"+DocTypename);
Conditions Cond= new Conditions();
Cond.addCondition(new Condition(fSTATUS, Condition.cEQUAL, fSTATUS_LASTDEL));
Cond.addCondition(new Condition(fACL, new HashSet(getDrv().getUser().getAclList().keySet())));
Query LoadAct=new Query(getTabNameVer(DocTypename), getRecSum(), Cond, null);
Cursor Cur=getDrv().OpenCursor(LoadAct);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListDeleted <");
return(Cur);
}
//-------------------------------------------------------------------------
//-------------------------------------------------------------------------
/**
 * Search for Folders returning a cursor with the results of folders with the
 * indicated values of fields. Only return the folders alowed for the user, as defined by ACL.
 * @param DocType Type of folder to search. Can return folders of subtype.
 * @param AttrConds Conditions over the fields ofthe FolderType
 * @param SubTypes if true, returns results of the indicated type AND susbtipes
 * @param SubFolders if true seach in actual folder AND subfolders, if false, serach in ALL the structure
 * @param IncludeVers if true, includes in the searching ALL versions of documents. Not posible with subtypes
 * @param IdActFold Folder to start the search. if null, start in the root level
 * @param Ord 
 * @return a Cursor with the results of the query to use o send to NextFold()
 * @throws PDException when occurs any problem
 */
public Cursor Search(String DocType, Conditions AttrConds, boolean SubTypes, boolean SubFolders, boolean IncludeVers, String IdActFold, Vector Ord) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Search >:"+DocType+" {"+AttrConds+"} SubTypes:"+SubTypes+" SubFolders:"+SubFolders+" IdActFold:"+IdActFold+" Ord:"+Ord);
if (SubTypes && IncludeVers)
    throw new PDException("SubTypes and IncludeVers not allowed");
PDDocs Doc=new PDDocs(getDrv(), DocType);
Conditions ComposedConds=new Conditions();
ComposedConds.addCondition(AttrConds);
Vector TypList=new Vector();
if (!SubTypes)
    {
    Condition C=new Condition(PDDocs.fDOCTYPE, Condition.cEQUAL, DocType);
    ComposedConds.addCondition(C);
    }
if (/*!SubTypes ||*/ IncludeVers) //!SubTypes valid if adding condition limiting to actual version
    {
    TypList.add(getTabNameVer(DocType));
    }
else 
    {
    TypList.add(DocType);
    if (!DocType.equalsIgnoreCase(getTableName()))
        {
        Conditions CondTyps=new Conditions();
        ArrayList ListTip=Doc.getTypeDefs();
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
            if (!Typ.equalsIgnoreCase(DocType))
                TypList.add(Typ);
            }
        ComposedConds.addCondition(CondTyps);
        }
    }
if (SubFolders)
    {
    if (!(IdActFold==null || IdActFold.equalsIgnoreCase(PDFolders.ROOTFOLDER)))
        { // add list to conditions
        PDFolders Fold=new PDFolders(getDrv());
        Condition C=new Condition(PDDocs.fPARENTID, Fold.getListDescendList(IdActFold));
        ComposedConds.addCondition(C);
        }
    }
Condition CondAcl=new Condition(PDDocs.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ComposedConds.addCondition(CondAcl);
Query DocSearch=new Query(TypList, Doc.getRecSum().CopyMono(), ComposedConds, Ord);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Search <");
return(getDrv().OpenCursor(DocSearch));
}
//-------------------------------------------------------------------------
/**
 * Receives a cursor created with method Search and returns the next folder or null if
 * there are no more.
 * @param Res Cursor created with method Search and serevral parameters
 * @return A new Object created with the data of the next row
 * @throws PDException when occurs any problem
 */
public PDDocs NextDoc(Cursor Res) throws PDException
{
PDDocs NextD=null;
Record Rec=getDrv().NextRec(Res);
if (Rec==null)
    return(NextD);
String Typ=(String)Rec.getAttr(fDOCTYPE).getValue();
NextD=new PDDocs(getDrv(), Typ);
NextD.assignValues(Rec);
return(NextD);
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (DocsObjectsCache==null)
    DocsObjectsCache=new ObjectsCache("Docs");
return(DocsObjectsCache);    
}
//-------------------------------------------------------------------------
/**
 * Returns the value/field used as key of the object (Id) to ve used in Cache index
 * @return the value of the Id field
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
 * Process the object definition inserting a new object
 * @param OPDObject XML node containing theobject data
 * @throws PDException if object name/index duplicated or in any error
 */
@Override
public void ProcesXMLNode(Node OPDObject) throws PDException
{
throw new UnsupportedOperationException("Not Supported. Use ImportXMLNode");
}    
//-------------------------------------------------------------------------
/**
 * Builds an XML of the object including the fileto be printed or exported
 * @param FolderPath Path to store Metadata and Document
 * @param AbsPath If tru, include fullpath (FolderPath) in the XML, otherwirse, only name
 * @throws PDException in any error 
 */
public void ExportXML(String FolderPath, boolean AbsPath) throws PDException
{
PrintWriter FMetadataXML = null;
if (FolderPath.charAt(FolderPath.length()-1)!=File.separatorChar)
    FolderPath+=File.separatorChar;
LoadFull(getPDId());
String PathContent=getFile(FolderPath);
try {
FMetadataXML = new PrintWriter(FolderPath+getPDId()+".opd", "UTF-8");
String OrigName=getName(); //Name can be empty or relative. A tmp copy is needed.
if (AbsPath)
    setName(PathContent);
else
    {
    int StartName=PathContent.lastIndexOf(File.separatorChar);
    if (StartName==-1)
        setName(PathContent);
    else
        setName(PathContent.substring(StartName+1));
    }
FMetadataXML.print(toXML());
setName(OrigName);
FMetadataXML.close();
FMetadataXML=null;
} catch (Exception e)
    {
    if (FMetadataXML!=null)
        FMetadataXML.close();
    throw new PDException(e.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * Import a Doc described by an XML with content referenced
 * @param OPDObject XMLNode to process
 * @param FolderPath Path where the original xlml file was readed. Use to resolve the absolute file position
 * @param DestFold OPD destination folder
 * @param MaintainId When true, the Original Id is maintained, else a new one is assigned
 * @throws PDException
 */
public void ImportXMLNode(Node OPDObject, String FolderPath, String DestFold, boolean MaintainId) throws PDException
{
if (FolderPath.charAt(FolderPath.length()-1)!=File.separatorChar)
    FolderPath+=File.separatorChar; 
NodeList childNodes = OPDObject.getChildNodes();
PDDocs NewDoc=null;
for (int i = 0; i < childNodes.getLength(); i++)
    {
    Node item = childNodes.item(i);
    if (item.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(item, getRecord());
        String DocTypReaded=(String)r.getAttr(PDDocs.fDOCTYPE).getValue();
        NewDoc=new PDDocs(getDrv(), DocTypReaded); // to be improved to analize the type BEFORE
        r=Record.FillFromXML(item, NewDoc.getRecSum());
        NewDoc.assignValues(r);
         if (!MaintainId)
            NewDoc.setPDId(null);
        NewDoc.setParentId(DestFold);
        Attribute DocName=r.getAttr(fNAME);
        String Path=(String)DocName.getValue();
        if (Path.contains(File.separator)) // if absolute reference, maintain
            NewDoc.setFile(Path);
        else
            NewDoc.setFile(FolderPath+Path);
        NewDoc.setName(null); // calculated by when inserting
        }
    }
NewDoc.insert();
}    
//-------------------------------------------------------------------------

}
