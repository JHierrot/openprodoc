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

import java.util.HashSet;
import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jhierrot
 */
public class PDObjDefs extends ObjPD
{
/**
 *
 */
public static final String fNAME="Name";
/**
 *
 */
public static final String fCLASSTYPE="ClassType";
/**
 *
 */
public static final String fDESCRIPTION="Description";
/**
 *
 */
public static final String fACTIVE="Active";
/**
 *
 */
public static final String fACL="ACL";
/**
 *
 */
public static final String fPARENT="Parent";
/**
 *
 */
public static final String fREPOSIT="Reposit";
/**
 *
 */
public static final String fTRACEADD="TraceAdd";
/**
 *
 */
public static final String fTRACEDEL="TraceDel";
/**
 *
 */
public static final String fTRACEMOD="TraceMod";
/**
 *
 */
public static final String fTRACEVIEW="TraceView";

/**
 *
 */
public static final String fTYPNAME="TypName";
/**
 *
 */
public static final String fATTRNAME="Name";
/**
 *
 */
public static final String fATTRUSERNAME="UserName";
/**
 *
 */
public static final String fATTRDESCRIPTION="Description";
/**
 *
 */
public static final String fATTRTYPE="Type";
/**
 *
 */
public static final String fATTRREQUIRED="Required";
/**
 *
 */
public static final String fATTRLONG="LongAttr";
/**
 *
 */
public static final String fATTRPRIMKEY="PrimaryKey";
/**
 *
 */
public static final String fATTRUNIQUE="UniqueKey";
/**
 *
 */
public static final String fATTRMODALLOW="ModifAllowed";

/**
 *
 */
public static final String CT_FOLDER="FOLDER";
/**
 *
 */
public static final String CT_DOC="DOCUMENT";

/**
 *
 */
public static final String TABVERSUFIX="_V";

/**
 *
 */
static private Record DocsDefStruct=null;
/**
 *
 */
static private Record DocsDefAttrsStruct=null;
/**
 *
 */
private String Name=null;
/**
 *
 */
private String ClassType=null;
/**
 *
 */
private String Description;
/**
 *
 */
private boolean Active=true;
/**
 *
 */
private String ACL=null;
/**
 *
 */
private String Parent=null;
/**
 *
 */
private String Reposit=null;
/**
 *
 */
private boolean TraceAdd=false;
/**
 *
 */
private boolean TraceDel=false;
/**
 *
 */
private boolean TraceMod=false;
/**
 *
 */
private boolean TraceView=false;

static private ObjectsCache ObjDefsObjectsCache = null;

/**
 *
 * @param Drv
 */
public PDObjDefs(DriverGeneric Drv)
{
super(Drv);
}
//-------------------------------------------------------------------------
public void assignValues(Record Rec) throws PDException
{
setName((String) Rec.getAttr(fNAME).getValue());
setClassType((String) Rec.getAttr(fCLASSTYPE).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
if (Rec.getAttr(fACTIVE).getValue()!=null)
    setActive(((Boolean)Rec.getAttr(fACTIVE).getValue()).booleanValue());
setACL((String)  Rec.getAttr(fACL).getValue());
setParent((String)  Rec.getAttr(fPARENT).getValue());
setReposit((String) Rec.getAttr(fREPOSIT).getValue());
if (Rec.getAttr(fTRACEADD).getValue()!=null)
    setTraceAdd(((Boolean)Rec.getAttr(fTRACEADD).getValue()).booleanValue());
if (Rec.getAttr(fTRACEDEL).getValue()!=null)
    setTraceDel(((Boolean)Rec.getAttr(fTRACEDEL).getValue()).booleanValue());
if (Rec.getAttr(fTRACEMOD).getValue()!=null)
    setTraceMod(((Boolean)Rec.getAttr(fTRACEMOD).getValue()).booleanValue());
if (Rec.getAttr(fTRACEVIEW).getValue()!=null)
    setTraceView(((Boolean)Rec.getAttr(fTRACEVIEW).getValue()).booleanValue());
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
* @return the Description
*/
public String getDescription()
{
return Description;
}
//-------------------------------------------------------------------------
/**
* @param Description the Description to set
*/
public void setDescription(String Description)
{
this.Description = Description;
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
 * object "method" needed because static overloading doesn't work in java
 * @return
 */
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
return("PD_DOCDEFS");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
public String getTabNameAttrs()
{
return ("PD_DOCDEFS_ATTR");
}
//-------------------------------------------------------------------------
/**
*
* @throws PDException
*/
protected void unInstallMulti() throws PDException
{
getDrv().DropTable(getTabNameAttrs());
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fCLASSTYPE).setValue(getClassType());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fACTIVE).setValue(new Boolean(isActive()));
Rec.getAttr(fACL).setValue(getACL());
Rec.getAttr(fPARENT).setValue(getParent());
Rec.getAttr(fREPOSIT).setValue(getReposit());
Rec.getAttr(fTRACEADD).setValue(new Boolean(isTraceAdd()));
Rec.getAttr(fTRACEDEL).setValue(new Boolean(isTraceDel()));
Rec.getAttr(fTRACEMOD).setValue(new Boolean(isTraceMod()));
Rec.getAttr(fTRACEVIEW).setValue(new Boolean(isTraceView()));
return(Rec);
}
//-------------------------------------------------------------------------
/**
 *
 */
Record getRecordStruct() throws PDException
{
if (DocsDefStruct==null)
    DocsDefStruct=CreateRecordStruct();
return(DocsDefStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 *
 */
static private synchronized Record CreateRecordStruct() throws PDException
{
if (DocsDefStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, "Name", "Name_of_class", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fCLASSTYPE, "Object_type", "Object_type", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, "Description", "Description", Attribute.tSTRING, false, null, 128, false, false, true));
    R.addAttr( new Attribute(fACTIVE, "Active", "When_true_it_is_allowed_to_create_elements_of_the_type", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fACL, "ACL", "Default_ACL_and_definition_ACL", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addAttr( new Attribute(fPARENT, "Parent_Class", "Parent_Class", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fREPOSIT, "Repository", "Repository_to_store_documents", Attribute.tSTRING, false, null, 32, false, false, true));
    R.addAttr( new Attribute(fTRACEADD, "Log_Insert", "Log_Insert", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fTRACEDEL, "Log_Delete", "Log_Delete", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fTRACEMOD, "Log_Update", "Log_Update", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    R.addAttr( new Attribute(fTRACEVIEW, "Log_Query", "Log_Query", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    return(R);
    }
else
    return(DocsDefStruct);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
public Record getRecordAttrsStruct()  throws PDException
{
if (DocsDefAttrsStruct==null)
    DocsDefAttrsStruct=CreateRecordAttrsStruct();
return(DocsDefAttrsStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordAttrsStruct()  throws PDException
{
if (DocsDefAttrsStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fTYPNAME, "Name_of_class", "Name_of_class", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fATTRNAME, "Name_of_attribute", "Name_of_attribute", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fATTRUSERNAME, "Visible_Name_of_attribute", "Visible_Name_of_attribute", Attribute.tSTRING, false, null, 32, true, false, true));
    R.addAttr( new Attribute(fATTRDESCRIPTION, "Description", "Description", Attribute.tSTRING, false, null, 128, false, false, true));
    R.addAttr( new Attribute(fATTRTYPE, "attribute_type", "attribute_type", Attribute.tINTEGER, true, null, 0, false, false, false));
    R.addAttr( new Attribute(fATTRREQUIRED, "Required", "When_true_the_attribute_MUST_be_informed", Attribute.tBOOLEAN, true, null, 0, false, false, false));
    R.addAttr( new Attribute(fATTRLONG, "Length", "Length_for_string_attributes", Attribute.tINTEGER, false, null, 0, false, false, false));
    R.addAttr( new Attribute(fATTRPRIMKEY, "Primary_key", "When_true_the_attribute_it_is_included_in_primary_key", Attribute.tBOOLEAN, true, null, 0, false, false, false));
    R.addAttr( new Attribute(fATTRUNIQUE, "Unique_value", "Unique_value", Attribute.tBOOLEAN, true, null, 0, false, false, false));
    R.addAttr( new Attribute(fATTRMODALLOW, "Modifiable", "When_true_the_attribute_can_be_modified_after_creation", Attribute.tBOOLEAN, true, null, 0, false, false, true));
    return(R);
    }
else
    return(DocsDefAttrsStruct);
}
//-------------------------------------------------------------------------
/**
 *
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
Condition CondAcl=new Condition(PDObjDefs.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
ListCond.addCondition(CondAcl);
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 */
protected void AsignKey(String Ident)
{
setName(Ident);
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
*
* @throws PDException
*/
protected void InstallMulti() throws PDException
{
getDrv().CreateTable(getTabNameAttrs(), getRecordAttrsStruct());
getDrv().AddIntegrity(getTabNameAttrs(), fTYPNAME, getTableName(),               fNAME);
getDrv().AddIntegrity(getTabName(),      fACL,     PDACL.getTableName(),         PDACL.fNAME);
getDrv().AddIntegrity(getTabName(),      fPARENT,  getTableName(),               fNAME);
getDrv().AddIntegrity(getTabName(),      fREPOSIT, PDRepository.getTableName(),  PDRepository.fNAME);
}
//-------------------------------------------------------------------------
/**
* @return the Parent
*/
public String getParent()
{
return Parent;
}
//-------------------------------------------------------------------------
/**
* @param Parent the Parent to set
*/
public void setParent(String Parent)
{
this.Parent = Parent;
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
 *
 * @param Attr
 * @throws PDException
 */
public void addAtribute(Attribute Attr) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.addAtribute>:"+Attr.getName());
VerifyAllowedUpd();
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().InsertRecord(getTabNameAttrs(), ConvertRec(Attr));
} catch (PDException Ex)
    {
    if (InTransLocal)
        getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.addAtribute<:"+Attr.getName());
}

//-------------------------------------------------------------------------
/**
 *
 * @param AttrName
 * @throws PDException
 */
public void delAtribute(String AttrName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.delAtribute>:"+AttrName);
VerifyAllowedUpd();
boolean InTransLocal;
InTransLocal=!getDrv().isInTransaction();
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fTYPNAME, Condition.cEQUAL, getName()));
ListCond.addCondition(new Condition(fATTRNAME, Condition.cEQUAL, AttrName));
if (InTransLocal)
    getDrv().IniciarTrans();
try {
getDrv().DeleteRecord(getTabNameAttrs(), ListCond);
} catch (PDException Ex)
    {
    if (InTransLocal)
        getDrv().AnularTrans();
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.delAtribute>:"+AttrName);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Attr
 * @return
 * @throws PDException
 */
protected Record ConvertRec(Attribute Attr) throws PDException
{
Record Rec=getRecordAttrsStruct();
Rec.getAttr(fTYPNAME).setValue(getName());
Rec.getAttr(fATTRNAME).setValue(Attr.getName());
Rec.getAttr(fATTRUSERNAME).setValue(Attr.getUserName());
Rec.getAttr(fATTRDESCRIPTION).setValue(Attr.getDescription());
Rec.getAttr(fATTRTYPE).setValue(new Integer(Attr.getType()));
Rec.getAttr(fATTRREQUIRED).setValue(new Boolean(Attr.isRequired()));
Rec.getAttr(fATTRLONG).setValue(new Integer(Attr.getLongStr()));
Rec.getAttr(fATTRPRIMKEY).setValue(new Boolean(Attr.isPrimKey()));
Rec.getAttr(fATTRUNIQUE).setValue(new Boolean(Attr.isUnique()));
Rec.getAttr(fATTRMODALLOW).setValue(new Boolean(Attr.isModifAllowed()));
return(Rec);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Rec
 * @return
 * @throws PDException
 */
public Attribute ConvertRec(Record Rec) throws PDException
{
boolean Required=((Boolean)Rec.getAttr(fATTRREQUIRED).getValue()).booleanValue();
boolean Primkey=((Boolean)Rec.getAttr(fATTRPRIMKEY).getValue()).booleanValue();
int Type=((Integer)Rec.getAttr(fATTRTYPE).getValue()).intValue();
int LongStr=((Integer)Rec.getAttr(fATTRLONG).getValue()).intValue();
String UserName=(String)Rec.getAttr(fATTRUSERNAME).getValue();
boolean Unique=((Boolean)Rec.getAttr(fATTRUNIQUE).getValue()).booleanValue();
boolean ModifAlllowed=((Boolean)Rec.getAttr(fATTRMODALLOW).getValue()).booleanValue();
Attribute Attr=new Attribute((String)Rec.getAttr(fATTRNAME).getValue(), UserName,
                             (String)Rec.getAttr(fATTRDESCRIPTION).getValue(),
                             Type,
                             Required,
                             null,
                             LongStr,
                             Primkey,
                             Unique,
                             ModifAlllowed);
return(Attr);
}
//-------------------------------------------------------------------------
/**
* @return the ClassType
*/
public String getClassType()
{
return ClassType;
}
//-------------------------------------------------------------------------
/**
 * The class can be only DOCUMENT o FOLDER
 * @param pClassType
 * @throws PDException
*/
public void setClassType(String pClassType) throws PDException
{
//if (pClassType.equals(CT_FOLDER) || pClassType.equals(CT_DOC))
    ClassType = pClassType;
//else
//    throw new PDException("Classtype no soportado");
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))         
    if (!getDrv().getUser().getRol().isAllowCreateObject() )
       PDExceptionFunc.GenPDException("Object_definition_creation_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainObject() )
   PDExceptionFunc.GenPDException("Object_definition_delete_not_allowed_to_user",getName());
PDObjDefs D=new PDObjDefs(getDrv());
D.Load(getName());    
if (!getDrv().getUser().getAclList().containsKey(D.getACL()))
    PDExceptionFunc.GenPDException("Object_definition_delete_not_allowed_to_user",getName());
Integer Perm=(Integer)getDrv().getUser().getAclList().get(D.getACL());
if (Perm.intValue()<PDACL.pDELETE)
    PDExceptionFunc.GenPDException("Object_definition_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))    
    {
    if (!getDrv().getUser().getRol().isAllowMaintainObject() )
       PDExceptionFunc.GenPDException("Object_definition_modification_not_allowed_to_user",getName());
    PDObjDefs D=new PDObjDefs(getDrv());
    D.Load(getName());    
    if (!getDrv().getUser().getAclList().containsKey(D.getACL()))
        PDExceptionFunc.GenPDException("Object_definition_modification_not_allowed_to_user",getName());
    Integer Perm=(Integer)getDrv().getUser().getAclList().get(D.getACL());
    if (Perm.intValue()<PDACL.pUPDATE)
        PDExceptionFunc.GenPDException("Object_definition_modification_not_allowed_to_user",getName());
    }
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
protected Record GetAttrDef() throws PDException
{
Record AttrsDef=new Record();
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(fTYPNAME, Condition.cEQUAL, getName()));
Query LoadAct=new Query(getTabNameAttrs(), getRecordAttrsStruct(), Conds);
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record r=getDrv().NextRec(Cur);
while (r!=null)
    {
    AttrsDef.addAttr(ConvertRec(r));
    r=getDrv().NextRec(Cur);
    }
getDrv().CloseCursor(Cur);
return(AttrsDef);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Name
 * @param isFolder
 * @throws PDException
 */
public void CreateObjectTables(String Name, boolean isFolder)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.CreateObjectTables>:"+Name);
PDObjDefs Def=new PDObjDefs(getDrv());
Def.Load(Name);
Record Rec=Def.GetAttrDef();
Rec.addAttr(new Attribute(PDFolders.fPDID, "PDIP", "Identificador único", Attribute.tSTRING, true, null, 32, true, false, false));
getDrv().CreateTable(Def.getName(), Rec);
if (isFolder)
    {
    getDrv().AddIntegrity(Def.getName(), PDFolders.fPDID, PDFolders.getTableName(), PDFolders.fPDID);
    PDFolders FolDef=new PDFolders(getDrv(), Name);
    Record R=FolDef.getRecSum();
    getDrv().CreateTable(GenVerTabName(Def.getName()), R);
    getDrv().AddIntegrity(GenVerTabName(Def.getName()), PDFolders.fPDID, PDFolders.getTableName(), PDFolders.fPDID);
    }
else
    {
    getDrv().AddIntegrity(Def.getName(), PDDocs.fPDID, PDDocs.getTableName(), PDDocs.fPDID);
    PDDocs DocDef=new PDDocs(getDrv(), Name);
    Record RecVer=DocDef.getRecSum().Copy();
    Attribute A=RecVer.getAttr(PDDocs.fVERSION);
    A.setPrimKey(true);
    getDrv().CreateTable(GenVerTabName(Def.getName()), RecVer);
//    getDrv().AddIntegrity(GenVerTabName(Def.getName()), PDDocs.fPDID, PDDocs.getTableName(), PDDocs.fPDID);
    }
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.CreateObjectTables<:"+Name);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Name
 * @throws PDException
 */
public void DeleteObjectTables(String Name)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.DeleteObjectTables:"+Name);
PDObjDefs Def=new PDObjDefs(getDrv());
Def.Load(Name);
getDrv().DropTable(Def.getName());
getDrv().DropTable(GenVerTabName(Def.getName()));
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void DeleteMulti() throws PDException
{
Conditions CondDelAttrs=new Conditions();
Condition CondDelAttr=new Condition(fTYPNAME, Condition.cEQUAL, getName());
CondDelAttrs.addCondition(CondDelAttr);
getDrv().DeleteRecord(getTabNameAttrs(), CondDelAttrs);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Name
 * @return
 */
private String GenVerTabName(String Name)
{
return(Name+TABVERSUFIX);
}
//-------------------------------------------------------------------------
/**
* @return the TraceAdd
*/
public boolean isTraceAdd()
{
return TraceAdd;
}
//-------------------------------------------------------------------------
/**
* @param TraceAdd the TraceAdd to set
*/
public void setTraceAdd(boolean TraceAdd)
{
this.TraceAdd = TraceAdd;
}
//-------------------------------------------------------------------------
/**
* @return the TraceDel
*/
public boolean isTraceDel()
{
return TraceDel;
}
//-------------------------------------------------------------------------
/**
* @param TraceDel the TraceDel to set
*/
public void setTraceDel(boolean TraceDel)
{
this.TraceDel = TraceDel;
}
//-------------------------------------------------------------------------
/**
* @return the TraceMod
*/
public boolean isTraceMod()
{
return TraceMod;
}
//-------------------------------------------------------------------------
/**
* @param TraceMod the TraceMod to set
*/
public void setTraceMod(boolean TraceMod)
{
this.TraceMod = TraceMod;
}
//-------------------------------------------------------------------------
/**
* @return the TraceView
*/
public boolean isTraceView()
{
return TraceView;
}
//-------------------------------------------------------------------------
/**
* @param TraceView the TraceView to set
*/
public void setTraceView(boolean TraceView)
{
this.TraceView = TraceView;
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
 * Search for accesible definitions of clases from family CT_FOLDER
 * @return an Opened cursor with Clases of family CT_FOLDER
 * @throws PDException on any error
 */
public Cursor getListFold() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListFold>");
Condition CondType=new Condition(fCLASSTYPE, Condition.cEQUAL, CT_FOLDER);
Condition CondAcl=new Condition(PDObjDefs.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
Conditions Conds=new Conditions();
Conds.addCondition(CondType);
Conds.addCondition(CondAcl);
Query ListFold=new Query(getTabName(), getRecordStruct(), Conds);
Cursor Cur=getDrv().OpenCursor(ListFold);
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListFold<:"+Cur);
return(Cur);
}
//-------------------------------------------------------------------------
/**
 * Search for accesible definitions of clases from family CT_DOC
 * @return an Opened cursor with Clases of family CT_DOC
 * @throws PDException on any error
 */
public Cursor getListDocs() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListDocs>");
Condition CondType=new Condition(fCLASSTYPE, Condition.cEQUAL, CT_DOC);
Condition CondAcl=new Condition(PDObjDefs.fACL, new HashSet(getDrv().getUser().getAclList().keySet()));
Conditions Conds=new Conditions();
Conds.addCondition(CondType);
Conds.addCondition(CondAcl);
Query ListFold=new Query(getTabName(), getRecordStruct(), Conds);
Cursor Cur=getDrv().OpenCursor(ListFold);
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListDocs<:"+Cur);
return(Cur);
}
//-------------------------------------------------------------------------
/**
 * Return a Cursor with the list of the attibutes of ClassName
 * @param ClassName
 * @return Cursor with list of attributes definition
 * @throws PDException in any error
 */
public Cursor getListAttr(String ClassName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListAttr>:"+ClassName);
Condition CondType=new Condition(fTYPNAME, Condition.cEQUAL, ClassName);
Conditions Conds=new Conditions();
Conds.addCondition(CondType);
Query ListAttr=new Query(getTabNameAttrs(), getRecordAttrsStruct(), Conds);
Cursor Cur=getDrv().OpenCursor(ListAttr);
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListAttr<:"+Cur);
return(Cur);
}
//-------------------------------------------------------------------------
/**
 * return a Cursor with the list of atributes of parent class of ClassName
 * including inherited.
 * @param ClassName name of the Class whom parent are searched
 * @return Cursor with list of attributes definition
 * @throws PDException in any error
 */
public Cursor getListParentAttr(String ClassName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListParentAttr>:"+ClassName);
Condition CondType=new Condition(fTYPNAME, new HashSet(getListParentClases(ClassName)));
Conditions Conds=new Conditions();
Conds.addCondition(CondType);
Query ListAttr=new Query(getTabNameAttrs(), getRecordAttrsStruct(), Conds);
Cursor Cur=getDrv().OpenCursor(ListAttr);
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListParentAttr<:"+Cur);
return(Cur);
}
//-----------------------------------------------------------------------
/**
 * return an ORDERED Vector of clases parent of ClassName (excluded)
 * @param ClassName Class to bew searched
 * @return return an ORDERED Vector of clases: last the first parent
 * @throws PDException in any error
 */
public Vector getListParentClases(String ClassName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListParentClases>:"+ClassName);
Vector v=new Vector();
PDObjDefs Def=new PDObjDefs(getDrv());
Def.Load(ClassName);
while (!Def.getParent().equalsIgnoreCase(Def.getName()))
    {
    Def.Load(Def.getParent());
    v.add(Def.getName());
    }
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListParentClases<:"+v);
return(v);
}
//-----------------------------------------------------------------------
/**
 * return an  Vector of Subclases of ClassName (excluded)
 * @param ClassName Class to bew searched
 * @return return an ORDERED Vector of clases: last the first parent
 * @throws PDException in any error
 */
public Vector getListSubClases(String ClassName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListSubClases>:"+ClassName);
Vector v=new Vector();
// TODO: getListSubClases ?


if (PDLog.isDebug())
    PDLog.Debug("PDObjDefs.getListSubClases<:"+v);
return(v);
}
//-----------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (ObjDefsObjectsCache==null)
    ObjDefsObjectsCache=new ObjectsCache("ObjDefs");
return(ObjDefsObjectsCache);    
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
 * Add aditional information, oriented a "extended" object with childrn nodes
 * @return The aditional XML
 * @throws PDException
 */
@Override
protected String toXML2() throws PDException
{
StringBuilder XML=new StringBuilder("</"+XML_ListAttr+">");  
XML.append("<"+XML_Metadata+">");
Cursor LstGrps=getListAttr(getName());
Record FieldDef=getDrv().NextRec(LstGrps);
while (FieldDef!=null)
    {
    XML.append("<"+XML_Field+">");
    XML.append(FieldDef.toXML());       
    XML.append("</"+XML_Field+">");
    FieldDef=getDrv().NextRec(LstGrps);
    }
XML.append("</"+XML_Metadata+">");
return(XML.toString());      
}
//-------------------------------------------------------------------------
/**
 * Process the object definition inserting a new object
 * @param OPDObject XML node containing the object data
 * @throws PDException if object name/index duplicated or in any error
 */
@Override
public void ProcesXMLNode(Node OPDObject) throws PDException
{
NodeList childNodes = OPDObject.getChildNodes();
for (int NumNodes = 0; NumNodes < childNodes.getLength(); NumNodes++)
    {
    Node ListElements = childNodes.item(NumNodes);
    if (ListElements.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(ListElements, getRecord());
        assignValues(r);
        insert();
        }
    else if (ListElements.getNodeName().equalsIgnoreCase(XML_Metadata)) 
        {
        NodeList ListFields = ListElements.getChildNodes();
        for (int NumGrp = 0; NumGrp < ListFields.getLength(); NumGrp++)
            {
            Node Field = ListFields.item(NumGrp);
            if (Field.getNodeName().equalsIgnoreCase(XML_Field))
                {
                Record r=getRecordAttrsStruct();
                r=Record.FillFromXML(Field, r);
                Attribute Attr=ConvertRec(r);
                addAtribute(Attr);
                }
            }
        }
    }
}    
//-------------------------------------------------------------------------
}
