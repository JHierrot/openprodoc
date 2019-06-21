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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.codec.binary.Base64InputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Main public class that manages all the operations for Documents in OpenProdoc
 * @author jhierrot
 */
public class PDDocs extends ObjPD
{
/**
 * Default table name for defauls document type
 */    
private static final String DEFTABNAME="PD_DOCS";
/**
 * Name of the attribute identifier of the document
 */
public static final String fPDID="PDId";
/**
 * Name of the attribute containing the "name" of the document
 */
public static final String fTITLE="Title";
/**
 * Name of the attribute containing the functional date of the document
 */
public static final String fDOCDATE="DocDate";
/**
 * Name of the attribute containing the name of de user currently locking of the document (checked out)
 */
public static final String fLOCKEDBY="LockedBy";
/**
 * Name of the attribute containing the date for purge of the document
 */
public static final String fPURGEDATE="PurgeDate";
/**
 * Name of the attribute containing the document type of the document
 */
public static final String fDOCTYPE="DocType";
/**
 * Name of the attribute containing the name of the reposit containing the binary of each document
 */
public static final String fREPOSIT="Reposit";
/**
 * Name of the attribute containing the ACL of the document
 */
public static final String fACL="ACL";
/**
 * Name of the attribute containing the Mime Type of the document
 */
public static final String fMIMETYPE="MimeType";
/**
 * Name of the attribute containing the File name of the document
 */
public static final String fNAME="Name";
/**
 * Name of the attribute containing the Identifier of the folder that "contains" the document
 */
public static final String fPARENTID="ParentId";
/**
 * Name of the attribute containing the current version of the document
 */
public static final String fVERSION="Version";
/**
 * Name of the attribute used internally for managing versions, cheking out the document and deleting
 */
public static final String fSTATUS="Status";
/**
 * Status constant for delete
 */
public static final String fSTATUS_DEL="DELETED";
/**
 * Status constant for delete all
 */
public static final String fSTATUS_LASTDEL="DELETE_A";
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
/**
 * Internal Record for managing a copy of the structure of a document
 */
static private Record DocsStruct=null;
/**
 * Internal Record for managing a copy of the structure of a document version
 */
static private Record RecVer=null;
/**
 *Field identifier
 */
private String PDId;
/**
 * Field Title (public name)
 */
private String Title;
/**
 * Field Document date
 */
private Date   DocDate;
/**
 * Field User of locking
 */
private String LockedBy;
/**
 * Field date for deleting the document
 */
private Date   PurgeDate;
/**
 * Field Docuemnt ACL
 */
private String ACL;
/**
 * Field Document Type
 */
private String DocType=DEFTABNAME;
/**
 * Field Reposit
 */
private String Reposit;
/**
 * Field Mime type
 */
private String MimeType;
/**
 * Field File name of the document
 */
private String Name;
/**
 * Field identifier of Folder containing the document
 */
private String ParentId;
/**
 * Field containing the document version
 */
private String Version;
/**
 * Field status for internal processing
 */
private String Status;
/**
 * Collection of type definitions that compound the current document type
 */
private ArrayList<Record> TypeDefs=null;
/**
 * Collection of type definitions Attributes that compound the current document type
 */
private ArrayList<Record> TypeRecs=null;
/**
 * Collection of ALL the attributes defined for a document type (including inherited Attributes)
 */
private Record RecSum=null;
/**
 * path of the file too be imported
 */
private String FilePath=null;
/**
 * InputStream of the file to be imported
 */
private InputStream FileStream=null;
/**
 * Cache of PDDocs managed in JVM the last minutes
 */
static private ObjectsCache DocsObjectsCache = null;
/**
 * Constant for Import/Export in XML
 */
static public final String XML_CONTENT="OPD_CONTENT";

//-------------------------------------------------------------------------
/**
 * Default constructor that creates a document of the default doctype
 * @param Drv Driver generic to use for reading and writing
 * @throws PDException In any error
 */
public PDDocs(DriverGeneric Drv) throws PDException
{
super(Drv);
setDocType(getTableName());
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
 * @throws PDExceptionFunc in any error
 */
public void setPDId(String pPDId) throws PDExceptionFunc
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
 * @throws PDException in any error
 * @return a record with all the attributes of the document type
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
synchronized public Record getRecordStruct() throws PDException
{
if (DocsStruct==null)
    DocsStruct=CreateRecordStructPDDocs();
return(DocsStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Return a copy of the static structure of attributes for the class
 * @throws PDException In any error
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
@Override
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
protected void AsignKey(String Ident) throws PDExceptionFunc
{
setPDId(Ident);
}
//-------------------------------------------------------------------------
/**
* @return the DocDate
*/
public Date getDocDate()
{
return DocDate;
}
//-------------------------------------------------------------------------
/**
* @param DocDate the DocDate to set
*/
public void setDocDate(Date DocDate)
{
this.DocDate = DocDate;
}
//-------------------------------------------------------------------------
/**
* @return the LockedBy
*/
public String getLockedBy()
{
return LockedBy;
}
//-------------------------------------------------------------------------
/**
* @param LockedBy the LockedBy to set
*/
public void setLockedBy(String LockedBy)
{
this.LockedBy = LockedBy;
}
//-------------------------------------------------------------------------
/**
* @return the PurgeDate
*/
public Date getPurgeDate()
{
return PurgeDate;
}
//-------------------------------------------------------------------------
/**
* @param PurgeDate the PurgeDate to set
*/
public void setPurgeDate(Date PurgeDate)
{
this.PurgeDate = PurgeDate;
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
* @param ACL the ACL to set
*/
public void setACL(String ACL)
{
this.ACL = ACL;
}
//-------------------------------------------------------------------------
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
//-------------------------------------------------------------------------
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
* @param pName the Name to set
*/
public void setName(String pName)
{
if (pName==null)
    Name=null;
else
    Name = pName.trim().replace('\\', '/');
}
//-------------------------------------------------------------------------
/**
 * This method verifies if the user has permissions to create documents in general.
 * The permision to store the document in a folder depends of the folder's ACL
 * @throws PDException is the user can't create documents
 */
@Override
protected void VerifyAllowedIns() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.VerifyAllowedIns>");    
if (getDrv().getUser().getName().equals("Install"))  
    return;
if (!getDrv().getUser().getRol().isAllowCreateDoc() )
   PDExceptionFunc.GenPDException("Document_creation_not_allowed_to_user",getName());
PDObjDefs D=new PDObjDefs(getDrv());
D.Load(getDocType());    
if (!getDrv().getUser().getAclList().containsKey(D.getACL()))
    PDExceptionFunc.GenPDException("Document_creation_not_allowed_to_user",getDrv().getUser().getName()+" / "+getDocType());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(D.getACL());
if (Perm.intValue()<PDACL.pUPDATE)
    PDExceptionFunc.GenPDException("Document_creation_not_allowed_to_user",getDrv().getUser().getName()+" / "+getDocType());
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.VerifyAllowedIns<");    
}
//-------------------------------------------------------------------------
/**
 * This method verifies if the user has permissions to delete documents in general.
 * The permision to delete the document from a folder depends of the foilder's ACL
 * @throws PDException is the user can't delete documents
 */
@Override
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
@Override
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
}
//-------------------------------------------------------------------------
/** Return an ordered list of the hierarchy of document types from whom this
 * document type inherit its attributes
 * @return the TypeDefs the array list with the names of document types
 * @throws PDException if there is a problem comunicating with the server
*/
protected ArrayList<Record> getTypeDefs() throws PDException
{
if (TypeDefs==null)
    LoadDef(getDocType());
return TypeDefs;
}
//-------------------------------------------------------------------------
/**
 * Returns the collection of attributes of the document type and its ancestors
 *   loads the definition if there are not loaded
 * @return the TypeRecs Array with all the definitions
 * @throws PDException In any error
*/
private ArrayList<Record> getTypeRecs() throws PDException
{
if (TypeRecs==null)
    LoadDef(getDocType());
return TypeRecs;
}
//-------------------------------------------------------------------------
/**
 * Loads the definition of a document type
 * @param tableName name of document type
 * @throws PDException in any error
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
    RecSum.addRecord((getTypeRecs().get(i)).Copy());
    }
RecSum.getAttr(fDOCTYPE).setValue(getDocType());
if (PDLog.isDebug())
    PDLog.Debug("PdDocs.LoadDef<:"+RecSum);
}
//-------------------------------------------------------------------------
/**
 * Return all the Attributes for the current document type
 *   empty or filled (depending if it has been used)
 *   if needed loads the definition
 * @return the Record with all the attributes
 * @throws PDException in any error
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
 * Returns the structure of the default document type, creating it if neeeded
 * @return a Record with all the Attributes
 * @throws PDException In any error
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
 * @param PDIdFold Folder to look for
 * @return a Cursor with the docs contained in the Folder
 * @throws PDException In any error
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
 * @param pFilePath path to assign
 * @throws PDException  In any error
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
 * @throws PDException  In any error
 */
public String getFile(String FolderPath) throws PDException
{
return(getFileOpt(FolderPath, true));
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID. Optimized so that if file exist, don't download
 * @param FolderPath path to recover/download the file
 * @param Overwrite If true, the content is overwrited, else maintain the existing file
 * @return The complete name of the downloaded file
 * @throws PDException  In any error
 */
public String getFileOpt(String FolderPath, boolean Overwrite) throws PDException
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
    FolderPath+=File.separatorChar+getPDId()+d.getVersion().replace(' ', '_')+d.getName().replace(' ', '_');
else
    FolderPath+=getPDId()+d.getVersion().replace(' ', '_')+d.getName().replace(' ', '_');
PDRepository Rep=new PDRepository(getDrv());
Rep.Load(d.getReposit());
if (Rep.IsRef())
    throw new UnsupportedOperationException("Not supported.");   
File NewF=new File(FolderPath);
if (!Overwrite && NewF.exists())
    return(FolderPath);
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
    if (MustTrace(fOPERVIE))
       Trace(fOPERVIE, false); 
    throw new PDException(ex.getLocalizedMessage());
    }
try {
OutCont.close();
} catch (IOException ex)
    {
    throw new PDException(ex.getLocalizedMessage());
    }
return(FolderPath);
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID-
 * @param FolderPath path to recover/download the file
 * @param Ver  versión identifier
 * @return The complete name of the downloaded file
 * @throws PDException  In any error
 */
public String getFileVer(String FolderPath, String Ver) throws PDException
{
return(getFileVerOpt(FolderPath, Ver, true));
}
//-------------------------------------------------------------------------
/**
 * "Download" a file version referenced by the current PDID and document version. 
 * Optimized so that if file exist, don't download
 * @param FolderPath path to recover/download the file
 * @param Ver Version of the document to download
 * @param Overwrite If true, the content is overwrited, else maintain the existing file
 * @return The complete name of the downloaded file
 * @throws PDException  In any error
 */
public String getFileVerOpt(String FolderPath, String Ver, boolean Overwrite) throws PDException
{
PDDocs d=new PDDocs(getDrv());
d.LoadVersion(getPDId(), Ver);
if (FolderPath.charAt(FolderPath.length()-1)!=File.separatorChar)
    FolderPath+=File.separatorChar+getPDId()+d.getVersion().replace(' ', '_')+d.getName().replace(' ', '_');
else
    FolderPath+=getPDId()+d.getVersion().replace(' ', '_')+d.getName().replace(' ', '_');
//StoreGeneric Rep=getDrv().getRepository(d.getReposit());
PDRepository Rep=new PDRepository(getDrv());
Rep.Load(d.getReposit());
if (Rep.IsRef())
    throw new UnsupportedOperationException("Not supported.");   
//Rep.Connect();
File NewF=new File(FolderPath);
if (!Overwrite && NewF.exists())
    return(FolderPath);
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
    if (MustTrace(fOPERVIE))
       Trace(fOPERVIE, false);
    throw new PDException(ex.getLocalizedMessage());
    }
try {
OutCont.close();
} catch (IOException ex)
    {
    throw new PDException(ex.getLocalizedMessage());
    }
//Rep.Disconnect();
return(FolderPath);
}
//-------------------------------------------------------------------------
/**
 * Check if the kind of Repository of the current document is referenced
 * @return true when referenced ( doo nnot contains buinary, just url)
 * @throws PDException In any error
 */
public boolean IsUrl() throws PDException
{
PDDocs d=new PDDocs(getDrv());
d.Load(getPDId());
PDRepository Rep=new PDRepository(getDrv());
Rep.Load(d.getReposit());
return(Rep.IsRef());
}
//-------------------------------------------------------------------------
/**
 * Build the url of a document combining with reference repository
 * @return the complete URL
 * @throws PDException  In any error
 */
public String getUrl() throws PDException
{
PDDocs d=new PDDocs(getDrv());
d.Load(getPDId());
PDRepository Rep=new PDRepository(getDrv());
Rep.Load(d.getReposit());
return(Rep.GetUrl(d.getName()));
}
//-------------------------------------------------------------------------
/**
 * Build the url of a document combining with reference repository
 * @param Ver Version of the the document to retrieve
 * @return the complete URL
 * @throws PDException  In any error
 */
public String getUrlVer(String Ver) throws PDException
{
PDDocs d=new PDDocs(getDrv());
d.LoadVersion(getPDId(), Ver);
PDRepository Rep=new PDRepository(getDrv());
Rep.Load(d.getReposit());
//StoreGeneric Rep=getDrv().getRepository(d.getReposit());
return(Rep.GetUrl(d.getName()));
}
//-------------------------------------------------------------------------
/**
 * Assign the file to be uploaded when called insert or update.
 * @param Bytes  Binary content of the document to import
 * @throws PDException In any error
 */
public void setStream(InputStream Bytes) throws PDException
{
FileStream=Bytes;
}
//-------------------------------------------------------------------------
/**
 * Assign the file IN BASE 64 to be uploaded when called insert or update.
 * @param B64InputStream  Binary content in Base64 of the document to import
 * @throws PDException In any error
 */
private void setStreamB64(InputStream B64InputStream)
{
FileStream=new Base64InputStream(B64InputStream,false);
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID-
 * the OutputStream will be closed at the end
 * @param OutBytes OutputStream where OpenProdoc will write the binary content
 * @throws PDException  In any error
 */
public void getStream(OutputStream OutBytes) throws PDException
{
LoadCurrent(getPDId());
StoreGeneric Rep=getDrv().getRepository(getReposit());
PDRepository Rep1=new PDRepository(getDrv());
Rep1.Load(getReposit());
if (Rep1.IsRef())
    throw new UnsupportedOperationException("Not supported.");   
try {    
Rep.Connect();
Rep.Retrieve(getPDId(), getVersion(), OutBytes, getRecSum());
Rep.Disconnect();
Rep=null;
if (MustTrace(fOPERVIE))
    Trace(fOPERVIE, true);
} catch(Exception ex)
    {
    Rep.Disconnect();
    if (MustTrace(fOPERVIE))
        Trace(fOPERVIE, false);
    throw new PDException(ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * "Download" AS BASE64 file referenced by the PDID
 * the OutputStream will be closed at the end
 * @param OutBytes OutputStream where OpenProdoc will write the binary content AS BASE64
 * @throws PDException  In any error
 */
public void getStreamB64(OutputStream OutBytes) throws PDException
{
LoadCurrent(getPDId());
StoreGeneric Rep=getDrv().getRepository(getReposit());
PDRepository Rep1=new PDRepository(getDrv());
Rep1.Load(getReposit());
if (Rep1.IsRef())
    throw new UnsupportedOperationException("Not supported.");   
try {    
Rep.Connect();
Rep.RetrieveB64(getPDId(), getVersion(), OutBytes, getRecSum());
Rep.Disconnect();
Rep=null;
if (MustTrace(fOPERVIE))
    Trace(fOPERVIE, true);
} catch(Exception ex)
    {
    Rep.Disconnect();
    if (MustTrace(fOPERVIE))
        Trace(fOPERVIE, false);
    throw new PDException(ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * "Download" a file referenced by the PDID and the current assigned version
 * the OutputStream will be closed at the end
 * @param OutBytes OutputStream where OpenProdoc will write the binary content
 * @throws PDException  In any error
 */
public void getStreamVer(OutputStream OutBytes) throws PDException
{
LoadVersion(getPDId(), getVersion());
StoreGeneric Rep=getDrv().getRepository(getReposit());
PDRepository Rep1=new PDRepository(getDrv());
Rep1.Load(getReposit());
if (Rep1.IsRef())
    throw new UnsupportedOperationException("Not supported.");   
try {    
Rep.Connect();
Rep.Retrieve(getPDId(), getVersion(), OutBytes, getRecSum());
Rep.Disconnect();
if (MustTrace(fOPERVIE))
    Trace(fOPERVIE, true);
} catch(Exception ex)
    {
    Rep.Disconnect();
    if (MustTrace(fOPERVIE))
       Trace(fOPERVIE, false);
    throw new PDException(ex.getLocalizedMessage());
    }
}
//--------------------------------------------------------------------------
/**
 * "Download" AS BASE64 file referenced by the PDID and assigned version
 * the OutputStream will be closed at the end
 * @param OutBytes OutputStream where OpenProdoc will write the binary content AS BASE64
 * @throws PDException  In any error
 */
public void getStreamVerB64(OutputStream OutBytes) throws PDException
{
LoadVersion(getPDId(), getVersion());
StoreGeneric Rep=getDrv().getRepository(getReposit());
PDRepository Rep1=new PDRepository(getDrv());
Rep1.Load(getReposit());
if (Rep1.IsRef())
    throw new UnsupportedOperationException("Not supported.");   
try {    
Rep.Connect();
Rep.RetrieveB64(getPDId(), getVersion(), OutBytes, getRecSum());
Rep.Disconnect();
if (MustTrace(fOPERVIE))
    Trace(fOPERVIE, true);
} catch(Exception ex)
    {
    Rep.Disconnect();
    if (MustTrace(fOPERVIE))
       Trace(fOPERVIE, false);
    throw new PDException(ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * Checkin the document, commiting the private working copy of he user, with the temporal metadata and content,
 * unloking the document and publisihng a new version with the specified version label
 * @param VersionName Label for the new version
 * @throws PDException In any error
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
if (!Rep.IsRef())
    {
    Rep.Connect();
    Rep.Rename(PDId, getDrv().getUser().getName(), PDId, VersionName);
    Rep.Disconnect();
    }
updateFragments(RecTot.CopyMono(), Id);
UpdateVersion(Id, getDrv().getUser().getName(), RecTot.CopyMono());
MultiDelete(Id, getDrv().getUser().getName());
MultiInsert(RecTot);
getObjCache().remove(getKey());
ExecuteTransThreads(PDTasksDefEvent.fMODEUPD);
GenerateNoTransThreads(PDTasksDefEvent.fMODEUPD);
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
 * Locks the current document, creating a private working copy 
 * @throws PDException In any error
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
Record Rec=getRecordStruct();
Attribute Attr=Rec.getAttr(fPDID);
Attr.setValue(Id);
Attr=Rec.getAttr(fVERSION);
Attr.setValue(Vers);
Attr=Rec.getAttr(fLOCKEDBY);
Attr.setValue(getDrv().getUser().getName());
Attr=Rec.getAttr(fDOCTYPE);
Attr.setValue(TobeUpdated.getDocType());
Rec.DelNull();   //
getDrv().UpdateRecord(getTabName(), Rec, getConditionsVer());
Record Mult=TobeUpdated.getRecSum().Copy();
Mult.getAttr(fVERSION).setValue(getDrv().getUser().getName());
TobeUpdated.MultiInsert(Mult);
StoreGeneric Rep=getDrv().getRepository(TobeUpdated.getReposit());
if (!Rep.IsRef())
    {
    Rep.Connect();
    PDFolders F=new PDFolders(getDrv());
    Rep.Copy(Id, Vers, Id, getDrv().getUser().getName(), Mult, F.getPathId((String)Mult.getAttr(fPARENTID).getValue())); 
    Rep.Disconnect();
    }
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
 * Unlocks the document, deleting the PWC and all the changes made
 * @throws PDException In any error
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
if (TobeUpdated.getLockedBy()==null || TobeUpdated.getLockedBy().length()==0)
   PDExceptionFunc.GenPDException("Document_not_locked_by_user",getPDId());
if (TobeUpdated.getLockedBy()==null || !TobeUpdated.getLockedBy().equalsIgnoreCase(getDrv().getUser().getName()))
   PDExceptionFunc.GenPDException("Document_locked_by_another_user",getPDId());
Record r=getRecordStruct();
Attribute Attr=r.getAttr(fLOCKEDBY);
Attr.setValue("");
Attr=r.getAttr(fDOCTYPE);
Attr.setValue(TobeUpdated.getDocType());
//setLockedBy("");
r.DelNull();
Conditions Cond=getConditions();
getDrv().UpdateRecord(getTabName(), r, Cond);
setDocType(TobeUpdated.getDocType());
StoreGeneric Rep=getDrv().getRepository(TobeUpdated.getReposit());
if (!Rep.IsRef())
    {
    Rep.Connect();
    MultiDelete(getPDId(), getDrv().getUser().getName());
    Rep.Delete(PDId, getDrv().getUser().getName(), TobeUpdated.getRecSum());
    Rep.Disconnect();
    }
DeleteVersion(getDocType(), Id, getDrv().getUser().getName());
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
 * @throws PDException in any error
 */
private void MultiDelete(String Id2Del, String Vers) throws PDException
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
        if (Vers!=null)
            Conds.addCondition(new Condition(fVERSION, Condition.cEQUAL, Vers));
        getDrv().DeleteRecord(MultiName, Conds);
        }
     }
}
//-------------------------------------------------------------------------
/**
 * Create a document, including metadata and the path o strean asigned previously
 * @throws PDException In any error
 */
@Override
public void insert() throws PDException
{
boolean InTransLocal;
if (PDLog.isInfo())
    PDLog.Debug("PDDocs.insert>: File="+FilePath+" R="+getRecSum());
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
if (!(getDrv().getUser().getName().equals("Install") && getDrv().getUser().getAclList()==null)) //just for installation tasks
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
//    Parent.update();
    }
if (getReposit()==null || getReposit().length()==0 )
    setReposit(getDrv().getAssignedRepos(getDocType()));
PDRepository FinalRep=new PDRepository(getDrv());
FinalRep.Load(getReposit());
AddLogFields();
if (getVersion()==null || getVersion().length()==0)
    setVersion("1.0");
getRecSum().CheckDef();
StoreGeneric Rep=getDrv().getRepository(getReposit());
if (getName()==null || getName().length()==0)
    {
//    if (Rep.IsRef())
    if (FinalRep.IsRef())
        {
        setName(FinalRep.ObtainName(FilePath));
        }
    else
        {
        if (FilePath!=null)
            {
            File f=new File(FilePath);
            setName(f.getName());
            }
        else
            setName(getPDId());
        }
    }
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
if (!FinalRep.IsRef())
   {
    Rep.Connect();
    if (FileStream!=null)
        Rep.Insert(PDId, getVersion(), FileStream, getRecSum(), null);
    else if (FilePath!=null)
        Rep.Insert(PDId, getVersion(), FilePath, getRecSum(), null);
    Rep.Disconnect();
   }
ExecuteTransThreads(PDTasksDefEvent.fMODEINS);
FilePath=null;
FileStream=null;
GenerateNoTransThreads(PDTasksDefEvent.fMODEINS);
if (MustTrace(fOPERINS))
    Trace(fOPERINS, true);
getObjCache().put(getKey(), getRecord());
} catch (Exception Ex)
    {
    getDrv().AnularTrans();
    if (MustTrace(fOPERINS))
        Trace(fOPERINS, false);
    if (PDLog.isDebug())
        Ex.printStackTrace();
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
    TypDef=(getTypeRecs().get(NumDefTyp)).CopyMulti();
    String TabName=(String) (getTypeDefs().get(NumDefTyp)).getAttr(PDObjDefs.fNAME).getValue();
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
 * @throws PDException In any error
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
 * Loads a document identified by the PDId
 * for the user that locked the document, loads the PWC
 * @param Ident identifier (PDID) of the document to load
 * @return a Record with the COMMON attributes of all documents (attributes of base docuemnt type)
 * @throws PDException In any error
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
    try {
    r=getDrv().NextRec(Cur);
    } finally
        {
        getDrv().CloseCursor(Cur);
        }
    getObjCache().put(Ident, r);
    }
String ActACL=(String)r.getAttr(fACL).getValue();
if (!getDrv().getUser().getAclList().containsKey(ActACL))
    {
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",Ident);
    }
Attribute UsuBloq=r.getAttr(fLOCKEDBY);
if (UsuBloq.getValue()!=null && ((String)UsuBloq.getValue()).length()!=0 && ((String)UsuBloq.getValue()).equalsIgnoreCase(getDrv().getUser().getName()))
    {// locked by actual user, return in-edition metadata
//    LoadAct=new Query(getTabNameVer(), getRecordStruct(), getConditions(), null);
    Conditions Cond=getConditions();
    Cond.addCondition(new Condition(fVERSION, Condition.cEQUAL, getDrv().getUser().getName()));
    Attribute Attr=r.getAttr(fDOCTYPE);
    LoadAct=new Query(getTabNameVer((String)Attr.getValue()), getRecordStruct(), Cond, null);
    Cur=getDrv().OpenCursor(LoadAct);
    try {
    r=getDrv().NextRec(Cur);
    } finally
        {
        getDrv().CloseCursor(Cur);
        }
    }
if (r!=null)
    assignValues(r);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Load<");
return(r);
}
//-------------------------------------------------------------------------
/**
 * Loads a document identified by the PDId. The version returned is always the publisehd/current one
 * @param Ident identifier (PDID) of the document to load
 * @return a Record with the COMMON attributes of all documents (attributes of base docuemnt type)
 * @throws PDException In any error
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
 * Loads a document version identified by the PDId.
 * @param Ident identifier (PDID) of the document to load
 * @param Vers Version to load
 * @return a Record with ALL the  attributes of document type)
 * @throws PDException In any error
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
 * Load to memory all the elements of a Doc, including all the inherited attributes and multivalued
 * @param Ident Identifier (PDId) of document
 * @return a record with ALL the Attributes of the document type
 * @throws PDException In any error
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
        Record TypDef=getTypeDefs().get(i);
        ListTabs.add((String)TypDef.getAttr(PDObjDefs.fNAME).getValue());
        if (! ((String)ListTabs.elementAt(i)).equals(PDDocs.getTableName()))
            {
            Condition Con=new Condition(PDDocs.getTableName()+"."+PDFolders.fPDID,
                                        (String)ListTabs.elementAt(i)+"."+PDFolders.fPDID ) ;
            Conds.addCondition(Con);
            }
        }
    Record Rec=getRecSum().CopyMono();
    Rec.getAttr(fPDID).setName(PDDocs.getTableName()+"."+fPDID);
    Query LoadAct=new Query(ListTabs, Rec, Conds, null);
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
    String TabName=(String)(getTypeDefs().get(NumDefTyp)).getAttr(PDObjDefs.fNAME).getValue();
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
 * @param Id PDId of Document to Update
 * @param Ver Version of Document to Update
 * @param Rec Complete Record of metadata to Update
 * @throws PDException In any error
 */
protected void UpdateVersion(String Id, String Ver, Record Rec) throws PDException
{
// Not using getConditionsVer() because version pased  as parameter (User lock sometimes)
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.UpdateVersion >:"+Id+"/"+Ver);
Rec=Rec.CopyMono();
Conditions ListCond=new Conditions();
if (Id==null || Id.length()==0 )
    PDExceptionFunc.GenPDException("Required_document_Id", null);
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, Id));
if (Ver!=null)
    ListCond.addCondition(new Condition(fVERSION, Condition.cEQUAL, Ver));
Attribute Attr=Rec.getAttr(fDOCTYPE);
Rec.getAttr(fPDDATE).setValue(new Date());
getDrv().UpdateRecord(getTabNameVer((String)Attr.getValue()), Rec, ListCond);
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.UpdateVersion <");
}
//-------------------------------------------------------------------------
/**
 * Deletes a new Version in the version table
 * @param Id PDId of Document to delete
 * @param Ver Version of Document to delete
 * @throws PDException In any error
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
 * Clculates the conditions for searching the current Id and version
 * @return Created conditions
 * @throws PDException in any error
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
 * update a locked document, including metadata and the path o stream asigned previously
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
TobeUpdated.LoadCurrent(Id);
if (TobeUpdated.getLockedBy()==null || !TobeUpdated.getLockedBy().equalsIgnoreCase(getDrv().getUser().getName()))
   PDExceptionFunc.GenPDException("Document_not_locked_by_user:", getPDId());
AddLogFields();
setReposit(TobeUpdated.getReposit());
PDRepository FinalRep=new PDRepository(getDrv());
FinalRep.Load(getReposit());
getRecSum().CheckDef();
Record Rec=getRecSum().Copy();
Rec.delAttr(fVERSION);
Attribute Attr=Rec.getAttr(fDOCTYPE);
Attr.setValue(TobeUpdated.getDocType());
Attr=Rec.getAttr(fNAME);
StoreGeneric Rep=getDrv().getRepository(getReposit());
if (FinalRep.IsRef())
    {
    if (FilePath!=null)
        Attr.setValue(FinalRep.ObtainName(FilePath));
    }
else
    {
    if (FilePath!=null)
        {
        File f=new File(FilePath);
        Attr.setValue(f.getName());
        }
    else
        {
        if (Attr.getValue()==null || ((String)Attr.getValue()).length()==0)
            Attr.setValue(TobeUpdated.getName());
        }
    }
if (FilePath!=null)
    {
    PDMimeType MT=new PDMimeType(getDrv());   
    MT.SolveExt(FilePath.substring(FilePath.lastIndexOf('.')+1));
    Rec.getAttr(fMIMETYPE).setValue(MT.getName());
    }
UpdateVersion(getPDId(), getDrv().getUser().getName(), Rec);
MultiDelete(Id, getDrv().getUser().getName());
Rec=getRecSum().Copy();
Rec.getAttr(fVERSION).setValue(getDrv().getUser().getName());
MultiInsert(Rec);
if (!Rep.IsRef())
    {
    Rep.Connect();
    if (FileStream!=null)
        Rep.Insert(PDId, getDrv().getUser().getName(), FileStream, getRecSum(), null);
    else if (FilePath!=null)
        Rep.Insert(PDId, getDrv().getUser().getName(), FilePath, getRecSum(), null);
    else
        {
        String OriginalVers=TobeUpdated.getVersion();
        PDFolders F=new PDFolders(getDrv());
        Rep.Copy(Id, OriginalVers, Id, getDrv().getUser().getName(), Rec, F.getPathId((String)Rec.getAttr(fPARENTID).getValue()));
        }
    Rep.Disconnect();
    }
FilePath=null;
FileStream=null;
if (MustTrace(fOPERUPD))
    Trace(fOPERUPD, true);
getObjCache().put(getKey(), getRecord());
} catch (Exception Ex)
    {
    getDrv().AnularTrans();
    if (MustTrace(fOPERUPD))
        Trace(fOPERUPD, false);
    if (PDLog.isDebug())
        Ex.printStackTrace();
    PDExceptionFunc.GenPDException("Error_updating_Document",Ex.getLocalizedMessage());
    }
finally 
    {
if (InTransLocal)
    getDrv().CerrarTrans();
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.update <");
}
//-------------------------------------------------------------------------
/**
 * Receives a Record and inserts each fragment of the record in the table of each corresponding document type (ancestors oof the current document type)
 * @param RecTot Record to save
 * @throws PDException In any error
 */
private void insertFragments(Record RecTot)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.insertFragments>:"+RecTot);
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=getTypeDefs().get(i);
    Record DatParc=getTypeRecs().get(i);
    if (DatParc.getAttr(fPDID)==null)
        {
        DatParc.addAttr(RecTot.getAttr(fPDID));
        }
    DatParc.assign(RecTot);
    getDrv().InsertRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc.CopyMono());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.insertFragments<");
}
//-------------------------------------------------------------------------
/**
 * Deletes all the references to an Id in all the tables
 * @param Tables Collection of tables where deleting
 * @param Id Identifier of document to delete
 * @throws PDException In any error
 */
private void DeleteFragments(ArrayList Tables, String Id) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.DeleteFragments>:"+Tables+"-"+Id);
for (int i = 0; i <getTypeDefs().size(); i++)
    {
    Record TypDef=getTypeDefs().get(i);
    getDrv().DeleteRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), getConditions());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.DeleteFragments <");
}
//-------------------------------------------------------------------------
/**
 * Updates all the references to an Id in all the tables
 * @param RecTot Record with ALL he attributes of the document type
 * @param Id  Identifier of document to update
 * @throws PDException In any error
 */
protected void updateFragments(Record RecTot, String Id) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.updateFragments>:"+RecTot+"-"+Id);
for (int i = getTypeDefs().size()-1; i >=0; i--)
    {
    Record TypDef=getTypeDefs().get(i);
    Record DatParc=getTypeRecs().get(i);
//    if (i!=getTypeDefs().size()-1) // Base
    if (!DatParc.ContainsAttr(fPDID))
        {
        DatParc.addAttr(RecTot.getAttr(fPDID));
        }
    DatParc.assign(RecTot);
    if (DatParc.NumAttr()>1) // PDID alone
        getDrv().UpdateRecord((String)TypDef.getAttr(PDObjDefs.fNAME).getValue(), DatParc.CopyMono(),  getConditions());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.updateFragments<");
}
//-------------------------------------------------------------------------
/**
 * Logically deletes the actual (with actual Id) document
 * Move the documents to trashbin
 * @throws PDException In any error
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
if (Perm<PDACL.pDELETE)
    PDExceptionFunc.GenPDException("User_without_permissions_over_document",getPDId());
ExecuteTransThreads(PDTasksDefEvent.fMODEDEL);
PDFolders Par=new PDFolders(getDrv());
Par.setPDId(getParentId());
Par.TouchDate();
String Vers=getVersion();
Record R =getRecordStruct();
Attribute Attr=R.getAttr(fDOCTYPE);
Attr.setValue(getDocType());
Attr=R.getAttr(fPDDATE);
Attr.setValue(new Date());
Attr=R.getAttr(fSTATUS);
Attr.setValue(fSTATUS_DEL);
R.DelNull();
UpdateVersion(Id, null, R);
Attr.setValue(fSTATUS_LASTDEL); // actual version must be indicated
AddLogFields(); // for tracing delete
UpdateVersion(Id, Vers, R);
DeleteFragments(TypeDefs, Id);
GenerateNoTransThreads(PDTasksDefEvent.fMODEDEL);
if (MustTrace(fOPERDEL))
    Trace(fOPERDEL, true);
getObjCache().remove(getKey());
} catch (Exception Ex)
    {
    getDrv().AnularTrans();
    if (MustTrace(fOPERDEL))
        Trace(fOPERDEL, false);
    if (PDLog.isDebug())
        Ex.printStackTrace();    
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
 * @param Id Identifier of document to undelete
 * @param DocTypename Document type
 * @throws PDException in any error
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
PDDocs D2U=new PDDocs(getDrv(), DocTypename);
Conditions Cond=new Conditions();
Cond.addCondition(new Condition(fPDID, Condition.cEQUAL, Id));
Cond.addCondition(new Condition(fSTATUS, Condition.cEQUAL, fSTATUS_LASTDEL));
Query LoadAct=new Query(getTabNameVer(DocTypename), D2U.getRecSum().CopyMono(), Cond, null);
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
D2U.insertFragments(Rec);
Record R =getRecordStruct();
Attr=R.getAttr(fDOCTYPE);
Attr.setValue(Rec.getAttr(fDOCTYPE).getValue());
Attr=R.getAttr(fPDDATE);
Attr.setValue(new Date());
Attr=R.getAttr(fSTATUS);
Attr.setValue("");
R.DelNull();
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
//------------------------------------------------------------------------------
/**
 * Load to memory the elements of a deleted Doc, only to obtain a complete definition
 * @param DocTypename Document type to load
 * @param Ident  identifier of the deleted document type to load
 * @return ALL the attributes of the deleted document
 * @throws PDException In any error
 */
private Record LoadDeleted(String DocTypename, String Ident) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.LoadDeleted>:"+DocTypename+"/"+Ident);
Conditions Cond=new Conditions();
Cond.addCondition(new Condition(fPDID, Condition.cEQUAL, Ident));
Cond.addCondition(new Condition(fVERSION, Condition.cEQUAL, fSTATUS_LASTDEL));
Record r;
Query LoadAct = new Query(getTabNameVer(DocTypename), getRecordStruct(), Cond, null);
Cursor Cur = getDrv().OpenCursor(LoadAct);
r=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
if (r!=null)
    assignValues(r);
getTypeDefs();
if (PDLog.isDebug())
   PDLog.Debug("PDDocs.LoadDeleted<:"+DocTypename+"/"+Ident);
return(r);
}
//-------------------------------------------------------------------------
/**
 * Permanently Destroy document and metadata
 * @param Id Identifier of document to destroy
 * @param DocTypename Document type of document to destroy
 * @throws PDException In any error
 */
public void Purge(String DocTypename, String Id) throws PDException
{
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Purge>:"+DocTypename+"/"+Id);
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
PDDocs Doc2Purge=new PDDocs(getDrv(), DocTypename);
Doc2Purge.LoadDeleted(DocTypename, Id);
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
    if (!Rep.IsRef())
        {
        Rep.Connect();
        if (Rep instanceof StoreRem)
            ((StoreRem)Rep).Delete((String)Rec.getAttr(fREPOSIT).getValue(), Id, Ver2Del, Rec);
        else        
           Rep.Delete(Id, Ver2Del, Rec);
        Rep.Disconnect();
        }
    
    DeleteVersion(DocTypename, Id, Ver2Del);
    Rec=getDrv().NextRec(Cur);
    }
getDrv().CloseCursor(Cur);
} catch (Exception Ex)
    {
    Ex.printStackTrace();    
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
 * Generates a Cursor containing all the Attributes of all the versions of a Document
 * The returned versions are ORDERED by date and FILTERED by ACL so certain user would NOT retrieve ALL the versions
 * @param DocTypename Dcument Type
 * @param Id   Identifier PDId
 * @return  Created cursor
 * @throws PDException In any error
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
 * Creates a cursor with documents deleted of DocTypeName 
 * FILTERED by ACL so certain user would NOT retrieve ALL the deleted documents
 * @param DocTypename Document type to look for
 * @return Created Cursor
 * @throws PDException In any error
 */
public Cursor ListDeleted(String DocTypename) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListDeleted>:"+DocTypename);
Conditions Cond= new Conditions();
Cond.addCondition(new Condition(fSTATUS, Condition.cEQUAL, fSTATUS_LASTDEL));
Cond.addCondition(new Condition(fACL, new HashSet(getDrv().getUser().getAclList().keySet())));
Query LoadAct=new Query(getTabNameVer(DocTypename), getRecSum().CopyMono(), Cond, null);
Cursor Cur=getDrv().OpenCursor(LoadAct);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListDeleted <");
return(Cur);
}
//-------------------------------------------------------------------------
/**
 * Creates a cursor with documents deleted of DocTypeName BEFORE a date
 * FILTERED by ACL so certain user would NOT retrieve ALL the deleted documents
 * @param DocTypename Document type to look for
 * @param DateBefore Date of deleting limit for retrieving documents
 * @return Created Cursor
 * @throws PDException In any error
 */
public Cursor ListDeletedBefore(String DocTypename, Date DateBefore) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListDeletedBefore>:"+DocTypename+"/"+DateBefore);
Conditions Cond= new Conditions();
Cond.addCondition(new Condition(fSTATUS, Condition.cEQUAL, fSTATUS_LASTDEL));
Cond.addCondition(new Condition(fPDDATE, Condition.cLET, DateBefore));
Cond.addCondition(new Condition(fACL, new HashSet(getDrv().getUser().getAclList().keySet())));
Query LoadAct=new Query(getTabNameVer(DocTypename), getRecSum().CopyMono(), Cond, null);
Cursor Cur=getDrv().OpenCursor(LoadAct);
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ListDeletedBefore <");
return(Cur);
}
//-------------------------------------------------------------------------
/**
 * Search for Documents returning a cursor with the results of Documents with the
 * indicated values of fields. Only returns the folders allowed for the user, as defined by ACL.
 * @param FTQuery Fultext search criteria
 * @param DocType Type of Document to search. Can return Documents of subtypes.
 * @param AttrConds Conditions over the fields of the Document Type
 * @param SubTypes if true, returns results of the indicated type AND susbtipes
 * @param SubFolders if true seach in actual folder AND subfolders, if false, search in ALL the structure
 * @param IncludeVers if true, includes in the searching ALL versions of documents. Not posible with subtypes
 * @param IdActFold Folder to start the search. if null, start in the root level
 * @param Ord Vector of Strings with the ASCENDING order
 * @return a Cursor with the results of the query to use o send to {@link #NextDoc(prodoc.Cursor)}
 * @throws PDException when occurs any problem
 */
public Vector<Record> SearchV(String FTQuery, String DocType, Conditions AttrConds, boolean SubTypes, boolean SubFolders, boolean IncludeVers, String IdActFold, Vector Ord) throws PDException
{
Vector<Record> ListRes=new Vector();
Cursor CursorId = null;
try {
CursorId = Search(FTQuery, DocType, AttrConds, SubTypes, SubFolders, IncludeVers, IdActFold, Ord);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    ListRes.add(Res);
    Res=getDrv().NextRec(CursorId);
    }
} catch (Exception Ex)
    {
    PDException.GenPDException("Error_searching_Document", Ex.getLocalizedMessage());
    }
finally 
    {
    if (CursorId!=null)
        getDrv().CloseCursor(CursorId);    
    }
return(ListRes);   
}
/**
 * Search for Documents returning a cursor with the results of Documents with the
 * indicated values of fields. Only returns the folders allowed for the user, as defined by ACL.
 * @param FTQuery Fultext search criteria
 * @param DocType Type of Document to search. Can return Documents of subtypes.
 * @param AttrConds Conditions over the fields of the Document Type
 * @param SubTypes if true, returns results of the indicated type AND susbtipes
 * @param SubFolders if true seach in actual folder AND subfolders, if false, search in ALL the structure
 * @param IncludeVers if true, includes in the searching ALL versions of documents. Not posible with subtypes
 * @param IdActFold Folder to start the search. if null, start in the root level
 * @param Ord Vector of Strings with the ASCENDING order
 * @return a Cursor with the results of the query to use o send to {@link #NextDoc(prodoc.Cursor)}
 * @throws PDException when occurs any problem
 */
public Cursor Search(String FTQuery, String DocType, Conditions AttrConds, boolean SubTypes, boolean SubFolders, boolean IncludeVers, String IdActFold, Vector Ord) throws PDException
{
if (FTQuery==null || FTQuery.length()==0)
    return(Search(DocType, AttrConds, SubTypes, SubFolders, IncludeVers, IdActFold, Ord));
ArrayList FTRes=SearchFT(DocType, SubTypes, FTQuery);
Condition Cond;
if (!FTRes.isEmpty())
    {
    HashSet IdList=new HashSet(FTRes);
    Cond=new Condition(PDDocs.fPDID,IdList);    
    }
else
    { //to force an empty curor
    Cond=new Condition(PDDocs.fPDID, Condition.cEQUAL, "az");
    }
Conditions WithFT=new Conditions();
WithFT.addCondition(Cond);
if (AttrConds.NumCond()>0)
    WithFT.addCondition(AttrConds);
return(Search(DocType, WithFT, SubTypes, SubFolders, IncludeVers, IdActFold, Ord));
}
//-------------------------------------------------------------------------
/**
 * Search for Documents returning a cursor with the results of Documents with the
 * indicated values of fields. Only return the folders allowed for the user, as defined by ACL.
 * @param DocType Type of Documents to search. Can return Documents of subtype.
 * @param AttrConds Conditions over the fields of the Document Type
 * @param SubTypes if true, returns results of the indicated type AND susbtipes
 * @param SubFolders if true seach in actual folder AND subfolders, if false, serach in ALL the structure
 * @param IncludeVers if true, includes in the searching ALL versions of documents. Not posible with subtypes
 * @param IdActFold Folder to start the search. if null, start in the root level
 * @param Ord Vector of Strings with the ASCENDING order
 * @return a Cursor with the results of the query to use o send to {@link #NextDoc(prodoc.Cursor)}
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
    Conditions CondTyps=new Conditions();
    ArrayList<Record> ListTip=Doc.getTypeDefs();
    ArrayList<Record> ListAttr=Doc.getTypeRecs();
    for (int NumTabsDef = 0; NumTabsDef < ListTip.size(); NumTabsDef++)
        { 
        Record AttrsTab= (ListAttr.get(NumTabsDef)).Copy();
        AttrsTab.initList();
        Attribute Attr;
        for (int i = 0; i < AttrsTab.NumAttr(); i++)
            {
            Attr=AttrsTab.nextAttr();
            if (Attr.isMultivalued())
                {
                if (ComposedConds.UsedAttr(Attr.getName()))
                    {
                    Record R= (Record)ListTip.get(NumTabsDef);
                    Attribute AttrNomTab=R.getAttr(PDObjDefs.fNAME);
                    String Typ =(String) AttrNomTab.getValue();
                    String MultiName=PDObjDefs.genMultValNam(Typ, Attr.getName());
                    Condition Con=new Condition(getTabNameVer(DocType)+"."+fPDID, MultiName+"."+fPDID);
                    CondTyps.addCondition(Con);
                    Con=new Condition(getTabNameVer(DocType)+"."+fVERSION, MultiName+"."+fVERSION);
                    CondTyps.addCondition(Con);
                    TypList.add(MultiName);
                    }                            
                }                    
            }
        }
    if (CondTyps.NumCond()>0)
        ComposedConds.addCondition(CondTyps);
    }
else 
    {
    TypList.add(DocType);
    if (!DocType.equalsIgnoreCase(getTableName()))
        {
        Conditions CondTyps=new Conditions();
        ArrayList<Record> ListTip=Doc.getTypeDefs();
        ArrayList<Record> ListAttr=Doc.getTypeRecs();
        for (int NumTabsDef = 0; NumTabsDef < ListTip.size(); NumTabsDef++)
            {
            Record R= ListTip.get(NumTabsDef);
            Attribute AttrNomTab=R.getAttr(PDObjDefs.fNAME);
            String Typ =(String) AttrNomTab.getValue();
            if (!Typ.equalsIgnoreCase(getTableName()))
                {
                Condition Con=new Condition(getTableName()+"."+fPDID, Typ+"."+fPDID);
                CondTyps.addCondition(Con);
                }
            if (!Typ.equalsIgnoreCase(DocType))
                TypList.add(Typ);
            Record AttrsTab= (ListAttr.get(NumTabsDef)).Copy();
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
                        Con=new Condition(getTableName()+"."+fVERSION, MultiName+"."+fVERSION);
                        CondTyps.addCondition(Con);
                        TypList.add(MultiName);
                        }                            
                    }                    
                }
            }
        ComposedConds.addCondition(CondTyps);
        }
    }
if (SubFolders)
    {
    if (!(IdActFold==null || IdActFold.equalsIgnoreCase(PDFolders.ROOTFOLDER)))
        { // add list to conditions
        PDFolders F=new PDFolders(getDrv());
//        HashSet listDescendList = Fold.getListDescendList(IdActFold);
//        if (listDescendList==null)
//            listDescendList=new HashSet();
//        listDescendList.add(IdActFold);
//        Condition C=new Condition(PDDocs.fPARENTID, listDescendList);
//        Condition C=new Condition(PDFolders.fPARENTID, F.getQueryListDescendList(IdActFold));
        ComposedConds.addCondition(Condition.genInTreeCond(IdActFold, getDrv()));
        }
    }
Condition CondAcl=new Condition(PDDocs.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ComposedConds.addCondition(CondAcl);
Record RecSearch=Doc.getRecSum().CopyMono();
// not very "clean" options, but working
if (!IncludeVers)
    RecSearch.getAttr(fVERSION).setName(getTableName()+"."+fVERSION);
else
    RecSearch.getAttr(fVERSION).setName(getTabNameVer(DocType)+"."+fVERSION);
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
Query DocSearch=new Query(TypList, RecSearch, ComposedConds, Ord);
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
 * @throws PDException In any error
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
 * @param OPDObject XML node containing the object data
 * @throws PDException allways
 */
@Override
public void ProcesXMLNode(Node OPDObject) throws PDException
{
throw new UnsupportedOperationException("Not Supported. Use ImportXMLNode");
}    
//-------------------------------------------------------------------------
/**
 * Builds an XML of the object including the file to be printed or exported
 * @param FolderPath Path to store Metadata and Document
 * @param AbsPath If true, include fullpath (FolderPath) in the XML, otherwise, only name
 * @throws PDException in any error 
 */
public void ExportXML(String FolderPath, boolean AbsPath) throws PDException
{
PrintWriter FMetadataXML = null;
if (FolderPath.charAt(FolderPath.length()-1)!=File.separatorChar)
    FolderPath+=File.separatorChar;
LoadFull(getPDId());
try {
String PathContent="";
if (!IsUrl())
   PathContent=getFile(FolderPath);
FMetadataXML = new PrintWriter(FolderPath+getPDId()+".opd", "UTF-8");
String OrigName=getName(); //Name can be empty or relative. A tmp copy is needed.
if (!IsUrl())
    {
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
    }
FMetadataXML.print(toXML());
if (!IsUrl())
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
 * Builds an XML of the object to be printed or exported
 * @param IncludeContent When true, the content of the doc will be included
 * @return the XML with the metadata and, optionally the contet
 * @throws PDException In any error
 */
public String toXML(boolean IncludeContent) throws PDException
{   
StringBuilder XML=new StringBuilder("<"+XML_OPDObject+" type=\""+getTabName()+"\">\n");
XML.append("<").append(XML_ListAttr).append(">\n");
XML.append(getRecord().toXML());
XML.append(toXML2());
if (IncludeContent)
    {
    try {    
    XML.append("<").append(XML_CONTENT).append(">\n");
    ByteArrayOutputStream Bytes=new ByteArrayOutputStream();
    this.getStreamB64(Bytes);
    XML.append(Bytes.toString("UTF-8"));
    XML.append("</").append(XML_CONTENT).append(">\n");
    } catch (Exception Ex)
        {
        PDLog.Error(Ex.getLocalizedMessage());
        }
    }
XML.append("</").append(XML_OPDObject).append(">\n");
return XML.toString();
}
//-------------------------------------------------------------------------
/**
 * Import a Doc described by an XML with content referenced
 * @param OPDObject XMLNode to process
 * @param FolderPath Path where the original xlml file was readed. Use to resolve the absolute file position
 * @param DestFold OPD destination folder
 * @param MaintainId When true, the Original Id is maintained, else a new one is assigned
 * @throws PDException In any error
 */
public void ImportXMLNode(Node OPDObject, String FolderPath, String DestFold, boolean MaintainId) throws PDException
{
if (PDLog.isInfo())
    PDLog.Debug("PDDocs.ImportXMLNode>:FolderPath="+FolderPath+" DestFold="+DestFold+" MaintainId="+MaintainId+" OPDObject="+OPDObject);    
if (FolderPath.charAt(FolderPath.length()-1)!=File.separatorChar)
    FolderPath+=File.separatorChar; 
NodeList childNodes = OPDObject.getChildNodes();
PDDocs NewDoc=null;
PDObjDefs DefDoc=new PDObjDefs(getDrv());
for (int i = 0; i < childNodes.getLength(); i++)
    {
    Node item = childNodes.item(i);
    if (item.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(item, getRecord());
        String DocTypReaded=(String)r.getAttr(PDDocs.fDOCTYPE).getValue();
        if (DefDoc.Load(DocTypReaded)==null)
           throw new PDException("Unknown_DocType"+":"+DocTypReaded);  
        NewDoc=new PDDocs(getDrv(), DocTypReaded); // to be improved to analize the type BEFORE
        r=Record.FillFromXML(item, NewDoc.getRecSum());
        NewDoc.assignValues(r);
        if (!MaintainId)
            NewDoc.setPDId(null);
        NewDoc.setParentId(DestFold);
        PDRepository Rep=new PDRepository(getDrv());
        Rep.Load(NewDoc.getReposit());
        if (!Rep.IsRef())
            {
            Attribute DocName=r.getAttr(fNAME);
            String Path=(String)DocName.getValue();
            if (Path.contains(File.separator)) // if absolute reference, maintain
                NewDoc.setFile(Path);
            else
                {
                if (Path.contains("&amp;") || Path.contains("&gt;") || Path.contains("&lt;") ) 
                    { // Testing name conversion
                    File f=new File(FolderPath+Path);
                    if (!f.exists()) // perhaps converted
                        {
                        Path=Path.replace("&lt;", "<").replace("&gt;", ">" ).replace("&amp;", "&");   
                        }
                    }
                NewDoc.setFile(FolderPath+Path);
                }
            NewDoc.setName(null); // calculated by when inserting
            }
        }
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ImportXMLNode<");    
NewDoc.insert();
}    
//-------------------------------------------------------------------------
/**
 * Import a Doc described by an XML with content referenced
 * @param OPDObject XMLNode to process
 * @param DestFold OPD destination folder
 * @param MaintainId When true, the Original Id is maintained, else a new one is assigned
 * @return The Id of the imported document
 * @throws PDException In any error
 */
public String ImportXMLNode(Node OPDObject, String DestFold, boolean MaintainId) throws PDException
{
if (PDLog.isInfo())
    PDLog.Debug("PDDocs.ImportXMLNode>:B64. DestFold="+DestFold+" MaintainId="+MaintainId+" OPDObject="+OPDObject);    
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
        if (!MaintainId && ExistId(NewDoc.getPDId()))
           NewDoc.setPDId(null);
        NewDoc.setParentId(DestFold);
        PDRepository Rep=new PDRepository(getDrv());
        Rep.Load(NewDoc.getReposit());
        if (!Rep.IsRef())
            {
            Attribute DocName=r.getAttr(fNAME);
            String Path=(String)DocName.getValue();
            if (!Path.contains(File.separator)) // if absolute reference, maintain
                NewDoc.setName(null); // calculated by when inserting
            }
        }
    else if (item.getNodeName().equalsIgnoreCase(XML_CONTENT)) 
        {
        NewDoc.setStreamB64(new ByteArrayInputStream(item.getTextContent().getBytes()));
        }
    }
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ImportXMLNode<");    
NewDoc.insert();
return(NewDoc.getPDId());
}    
//-------------------------------------------------------------------------
/**
 * Imports all the documents referenced in a XML file in ABBY FlexyCapture format
 * @param Sess OpenProdoc session
 * @param XMLFile XML File
 * @param ParentFoldId OpenProdoc folder where do th import
 * @param DateFormat format of dates in the XML
 * @param TimeStampFormat Format of timestamps in the XML
 * @return The File object of the (last) "OCRed" image referenced in the XML
 * @throws PDException In any Error
 */
static public File ProcessXMLAbby(DriverGeneric Sess, File XMLFile, String ParentFoldId, String DateFormat, String TimeStampFormat) throws PDException
{
try {    
File ImageFile=null;
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList DocList = XMLObjects.getElementsByTagName("form:Documents");
NodeList DocElem;
NodeList SectElem;
NodeList AttrElem;
PDObjDefs DefTyp=new PDObjDefs(Sess);
for (int NumDocs=0; NumDocs<DocList.getLength(); NumDocs++)
    {
    DocElem=DocList.item(NumDocs).getChildNodes(); 
    for (int NumDoc = 0; NumDoc < DocElem.getLength(); NumDoc++)
        {
        if (DocElem.item(NumDoc).getNodeType()==Node.ELEMENT_NODE)
            {
            String TypeName=DocElem.item(NumDoc).getNodeName();
            TypeName=TypeName.substring(1, TypeName.indexOf(":"));
            DefTyp.Clear();
            DefTyp.Load(TypeName);
            if (DefTyp.getParent()==null || DefTyp.getParent().length()==0)
                throw new PDException("Incorrect_Document_Type:"+TypeName);
            String FileName=DocElem.item(NumDoc).getAttributes().getNamedItem("addData:ImagePath").getNodeValue();    
            SectElem=DocElem.item(NumDoc).getChildNodes(); 
            for (int NumSect = 0; NumSect < SectElem.getLength(); NumSect++)
                {
                if (SectElem.item(NumSect).getNodeType()==Node.ELEMENT_NODE)
                    {
                    PDDocs Doc=new PDDocs(Sess, TypeName);
                    if (FileName.charAt(0)==File.separatorChar || FileName.contains(":") && File.separatorChar=='\\')
                        ImageFile=new File(FileName);
                    else
                        ImageFile=new File(XMLFile.getParent()+File.separator+FileName);
                    Doc.setFile(ImageFile.getAbsolutePath());
                    Doc.setParentId(ParentFoldId);
                    Record Rec=Doc.getRecSum();
                    AttrElem=SectElem.item(NumSect).getChildNodes(); 
                    for (int NumAttr = 0; NumAttr < AttrElem.getLength(); NumAttr++)
                        {
                        if (AttrElem.item(NumAttr).getNodeType()==Node.ELEMENT_NODE)
                            {
                            String NameAttr=AttrElem.item(NumAttr).getNodeName().substring(1);
                            String Val=AttrElem.item(NumAttr).getTextContent();
                            if (Rec.ContainsAttr(NameAttr))
                                {
                                int Type=Rec.getAttr(NameAttr).getType();    
                                if (Type==Attribute.tDATE)  
                                    {
                                    SimpleDateFormat formatterDate = new SimpleDateFormat(DateFormat);
                                    Rec.getAttr(NameAttr).setValue(formatterDate.parse(Val));
                                    }
                                else if (Type==Attribute.tTIMESTAMP) 
                                    {
                                    SimpleDateFormat formatterTS = new SimpleDateFormat(TimeStampFormat);
                                    Rec.getAttr(NameAttr).setValue(formatterTS.parse(Val));
                                    }
                                else
                                    Rec.getAttr(NameAttr).Import(Val);
                                }
                            }
                        }
                    Doc.assignValues(Rec);
                    if (Doc.getTitle()==null)
                        Doc.setTitle(FileName);
                    Doc.insert();
                    }                    
                }
            }
        }
    }
DB.reset();
return(ImageFile);
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    throw new PDException(ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * Imports all the documents referenced in a Text file in Kofax Capture format
 * @param Sess OpenProdoc session
 * @param TxtFile Text File in Kofax format
 * @param ParentFoldId OpenProdoc folder where do th import
 * @param DateFormat format of dates in the XML
 * @param TimeStampFormat Format of timestamps in the XML
 * @return The File object of the (last) "OCRed" image referenced in the XML
 * @throws PDException In any Error
 */
public static File ProcessXMLKofax(DriverGeneric Sess, File TxtFile, String ParentFoldId, String DateFormat, String TimeStampFormat)  throws PDException
{
try {    
File ImageFile=null;
BufferedReader Metadata = new BufferedReader(new FileReader(TxtFile));
String DocMeta=Metadata.readLine();
PDObjDefs DefTyp=new PDObjDefs(Sess);
while (DocMeta!=null &&DocMeta.length()!=0)
    {
    String[] ListElem=DocMeta.split("\"");
    DefTyp.Clear();
    DefTyp.Load(ListElem[3]);
    if (DefTyp.getParent()==null || DefTyp.getParent().length()==0)
        throw new PDException("Incorrect_Document_Type:"+ListElem[3]);
    PDDocs Doc=new PDDocs(Sess, ListElem[3]);
    ImageFile=new File(ListElem[ListElem.length-1]);
    Doc.setFile(ImageFile.getAbsolutePath());
    Doc.setParentId(ParentFoldId);
    Record Rec=Doc.getRecSum();
    for (int i = 5; i < ListElem.length-1; i+=4)
        {
        String NameAttr=ListElem[i];
        String Val=ListElem[i+2];
        if (Rec.ContainsAttr(NameAttr))
            {
            int Type=Rec.getAttr(NameAttr).getType();    
            if (Type==Attribute.tDATE)  
                {
                SimpleDateFormat formatterDate = new SimpleDateFormat(DateFormat);
                Rec.getAttr(NameAttr).setValue(formatterDate.parse(Val));
                }
            else if (Type==Attribute.tTIMESTAMP) 
                {
                SimpleDateFormat formatterTS = new SimpleDateFormat(TimeStampFormat);
                Rec.getAttr(NameAttr).setValue(formatterTS.parse(Val));
                }
            else
                Rec.getAttr(NameAttr).Import(Val);
            }
        }
    Doc.assignValues(Rec);
    if (Doc.getTitle()==null)
       Doc.setTitle(ListElem[ListElem.length-1].substring(ListElem[ListElem.length-1].lastIndexOf(File.separatorChar)));
    Doc.insert();
    DocMeta=Metadata.readLine();
    }
Metadata.close();
return(ImageFile);
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    throw new PDException(ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/** Executes, in the current document, all the transactional defined threads for a specific MODE (INS, UPD, DEL) 
 * @param MODE CKind of operation (INSert, UPDater, DELete)
 * @throws PDException In any error
 */
private void ExecuteTransThreads(String MODE) throws PDException
{
ArrayList<PDTasksDefEvent> L =getDrv().getDocTransThreads(this.getDocType(), MODE); 
if (!L.isEmpty())
    LoadFull(getPDId());
for (PDTasksDefEvent L1 : L)
    L1.Execute(this);
}
//---------------------------------------------------------------------
/** Generates, for the current document, all the NO transactional defined threads
 * @param MODE Kind of operation (INSert, UPDater, DELete)
 * @throws PDException in any error
 */
private void GenerateNoTransThreads(String MODE) throws PDException
{
ArrayList<PDTasksDefEvent> L =getDrv().getDocNoTransThreads(this.getDocType(), MODE); 
PDTasksDefEvent T;
PDTasksExec TE;
for (PDTasksDefEvent L1 : L)
    {
    TE=new PDTasksExec(getDrv());
    TE.GenFromDef(L1, this);
    if (!TE.MeetsReq(this)) // under folder or future checks
        continue;
    TE.setNextDate(new Date());
    TE.setGenAuto(true);
    TE.insert();
    }
}
//---------------------------------------------------------------------
/**
 * Returns all the data of the doc as html
 * @return String with the html
 * @throws PDException in any error
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
 * Return true if the configuration of the document types includes functional trace
 * @param Oper Operation to verify the trace requirement
 * @return true when the operaion must be traced
 * @throws PDException In any error
 */
private boolean MustTrace(String Oper) throws PDException
{
PDObjDefs Def=new PDObjDefs(getDrv());
Def.Load(getDocType());
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
tr.setObjectType(getDocType());
tr.setName(getPDId());
tr.setOperation(Oper);
tr.setResult(Allowed);
tr.insert();
}
//---------------------------------------------------------------------
/**
 * Overloading of abstract method. Not valid for documents
 * @return null Always
 * @throws PDException Always
 */
@Override
public Cursor getAll() throws PDException
{
PDException.GenPDException("ERROR", null);
return(null);
}
//-------------------------------------------------------------------------
/**
 * Executes the Full Text indexing on the current document
 * @throws PDException In any error
 */
protected void ExecuteFTAdd()  throws PDException
{
LoadFull(getPDId());
StoreGeneric Rep=getDrv().getRepository(getReposit()); 
InputStream Is=null;
try {    
FTConnector FTConn=getDrv().getFTRepository(getDocType());
FTConn.Connect();
PDRepository Rep1=new PDRepository(getDrv());
Rep1.Load(getReposit());
if (!Rep1.IsRef())
    {
    Rep.Connect();
    // Is=Rep.Retrieve(getPDId(), getVersion());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Rep.Retrieve(getPDId(), getVersion(), baos, getRecSum());
    Is=new ByteArrayInputStream(baos.toByteArray());
    FTConn.Insert(getDocType(), getPDId(), Is, getRecSum());
    Is.close();
    Rep.Disconnect();
    }
else
    {
    try {
    Is=new FileInputStream(getName());    
    FTConn.Insert(getDocType(), getPDId(), Is, getRecSum()); 
    Is.close();
    } catch (Exception Ex)
        { // Metadata Alone
        FTConn.Insert(getDocType(), getPDId(), null, getRecSum());        
        }
    }
FTConn.Disconnect();
} catch (Exception Ex)
    {
    if (Is!=null)
        {
        try {
        Is.close();
        } catch (IOException ex) {}
        Rep.Disconnect();
        }
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }
}
//-------------------------------------------------------------------------
/**
 * Executes the Full Text updating of the index on the current document
 * @throws PDException In any error
 */
protected void ExecuteFTUpd() throws PDException
{
LoadFull(getPDId());
StoreGeneric Rep=getDrv().getRepository(getReposit());   
InputStream Is=null;
try {    
FTConnector FTConn=getDrv().getFTRepository(getDocType());
FTConn.Connect();
PDRepository Rep1=new PDRepository(getDrv());
Rep1.Load(getReposit());
if (!Rep1.IsRef())
//if (!Rep.IsURL())
    {
    Rep.Connect();
    // Is=Rep.Retrieve(getPDId(), getVersion());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Rep.Retrieve(getPDId(), getVersion(), baos, getRecSum());
    Is=new ByteArrayInputStream(baos.toByteArray());
    FTConn.Update(getDocType(), getPDId(), Is, getRecSum());
    Is.close();
    Rep.Disconnect();
    }
else
    FTConn.Update(getDocType(), getPDId(), null, getRecSum());
FTConn.Disconnect();
} catch (Exception Ex)
    {
    if (Is!=null)
        {
        try {
        Is.close();
        } catch (IOException ex) {}
        Rep.Disconnect();
        }
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }
}
//-------------------------------------------------------------------------
/**
 * Executes the deleting from the Full Text index of the current document
 * @throws PDException In any error
 */
protected void ExecuteFTDel() throws PDException
{
FTConnector FTConn=getDrv().getFTRepository(getDocType());
FTConn.Connect();
try {
FTConn.Delete(getPDId());
FTConn.Disconnect();
} catch (Exception Ex)
    {
    FTConn.Disconnect();
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }
}
//-------------------------------------------------------------------------
/**
 * Search in thee Fulltexxt repository using the received query
 * @param pDocType Currently ignored (but filtered when joined with metadata query)
 * @param SubTypes Currently ignored (but filtered when joined with metadata query)
 * @param FTQuery Full text quury using standar Lucene notation
 * @return An arrayList with the Id of the documents
 * @throws PDException In any error
 */
protected ArrayList SearchFT(String pDocType, boolean SubTypes, String FTQuery) throws PDException
{
ArrayList FTRes=null;    
FTConnector FTConn=getDrv().getFTRepository(pDocType);
FTConn.Connect();
try {
FTRes=FTConn.Search(pDocType, null, FTQuery, null);
FTConn.Disconnect();
} catch (Exception Ex)
    {
    FTConn.Disconnect();
    PDException.GenPDException(Ex.getLocalizedMessage(), "");
    }   
return (FTRes);
}
//-------------------------------------------------------------------------
/**
 * Checks if any document exist with the specified Id
 * @param pdId Id of document to check
 * @return true if the document exist
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
 * Move a document from its current folder to another one
 * @param NewParentId Id of the target folder
 * @return true if the document has been moved
 */
public boolean Move(String NewParentId)
{
DriverGeneric drv=null;    
try {    
drv = getDrv();  
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Move>:"+getPDId()+ ">>"+NewParentId);
getObjCache().remove(getKey());
VerifyAllowedUpd();
InTransLocal=!drv.isInTransaction();
if (InTransLocal)
    drv.IniciarTrans();
PDDocs TobeUpdated=new PDDocs(getDrv());
TobeUpdated.Load(getPDId()); 
if (!getDrv().getUser().getAclList().containsKey(TobeUpdated.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_to_move_document", getPDId());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeUpdated.getACL());
if (Perm<=PDACL.pREAD)
   PDExceptionFunc.GenPDException("User_without_permissions_to_move_document", getPDId());
if (TobeUpdated.getLockedBy()!=null && TobeUpdated.getLockedBy().length()!=0)
   PDExceptionFunc.GenPDException("Document_previously_checkout", getPDId());
Record R =new Record();
R.addAttr(getRecordStruct().getAttr(fPARENTID));
R.addAttr(getRecordStruct().getAttr(fPDDATE));
R.addAttr(getRecordStruct().getAttr(fDOCTYPE));
R.getAttr(fPARENTID).setValue(NewParentId);
R.getAttr(fPDDATE).setValue(new Date());
R.getAttr(fDOCTYPE).setValue(TobeUpdated.getDocType());
getDrv().UpdateRecord(DEFTABNAME, R,  getConditions());
UpdateVersion(getPDId(), TobeUpdated.getVersion(), R);
if (InTransLocal)
    drv.CerrarTrans();
getObjCache().remove(getKey());
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.Move<:"+getPDId());
} catch (Exception Ex)
    {
    PDLog.Error("PDDocs.Move ("+NewParentId+")="+Ex.getLocalizedMessage());       
    if (drv!=null && drv.isInTransaction())
        try {
        drv.AnularTrans();
        } catch (Exception E)
            {
            PDLog.Error("PDDocs.Move ("+NewParentId+")="+E.getLocalizedMessage());       
            }
    return(false);    
    }
return(true);    
}
//-------------------------------------------------------------------------
/**
 * Changes the ACL of the document
 * @param NewACL New ACL to apply
 * @return true if the ACL has been changed
 */
public boolean ChangeACL(String NewACL)
{
DriverGeneric drv=null;    
try {    
drv = getDrv();  
boolean InTransLocal;
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ChangeACL>:"+getPDId()+ ">>"+NewACL);
getObjCache().remove(getKey());
VerifyAllowedUpd();
InTransLocal=!drv.isInTransaction();
if (InTransLocal)
    drv.IniciarTrans();
PDDocs TobeUpdated=new PDDocs(getDrv());
TobeUpdated.Load(getPDId()); 
if (!getDrv().getUser().getAclList().containsKey(TobeUpdated.getACL()))
   PDExceptionFunc.GenPDException("User_without_permissions_to_change_document", getPDId());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(TobeUpdated.getACL());
if (Perm<=PDACL.pUPDATE)
   PDExceptionFunc.GenPDException("User_without_permissions_to_change_document", getPDId());
if (TobeUpdated.getLockedBy()!=null && TobeUpdated.getLockedBy().length()!=0)
   PDExceptionFunc.GenPDException("Document_previously_checkout", getPDId());
Record R =new Record();
R.addAttr(getRecordStruct().getAttr(fACL));
R.addAttr(getRecordStruct().getAttr(fDOCTYPE));
R.addAttr(getRecordStruct().getAttr(fPDDATE));
R.getAttr(fACL).setValue(NewACL);
R.getAttr(fDOCTYPE).setValue(TobeUpdated.getDocType());
R.getAttr(fPDDATE).setValue(new Date());
getDrv().UpdateRecord(DEFTABNAME, R,  getConditions());
UpdateVersion(getPDId(), TobeUpdated.getVersion(), R);
if (InTransLocal)
    drv.CerrarTrans();
getObjCache().remove(getKey());
if (PDLog.isDebug())
    PDLog.Debug("PDDocs.ChangeACL<:"+getPDId());
} catch (Exception Ex)
    {
    PDLog.Error("PDDocs.ChangeACL ("+NewACL+")="+Ex.getLocalizedMessage());       
    if (drv!=null && drv.isInTransaction())
        try {
        drv.AnularTrans();
        } catch (Exception E)
            {
            PDLog.Error("PDDocs.ChangeACL ("+NewACL+")="+E.getLocalizedMessage());       
            }
    return(false);    
    }
return(true);    
}
//-------------------------------------------------------------------------
}
