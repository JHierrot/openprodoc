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
 * author: Joaquin Hierro      2012
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
 * Class Thesaur for managong the thesaurus in Database
 * @author jhierrot
 */
public class PDThesaur extends ObjPD
{
/**
 *
 */
public static final String fPDID="PDId";
/**
 *
 */
public static final String fNAME="Name";
/**
 *
 */
public static final String fDESCRIP="Definition";
/**
 *
 */
public static final String fUSE="TES_USE";
/**
 *
 */
public static final String fLANG="TES_LANG";
/**
 *
 */
public static final String fSCN="SCN";
/**
 *
 */
public static final String fPDID2="PDId2";

/**
 *
 */
public static final String ROOTTERM="OPD_Thesaurus";
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
static private Record ThesaurStruct=null;
/**
 *
 */
static private Record ThesaurLevStruct=null;
static private Record ThesaurRTStruct=null;
static private Record ThesaurLangStruct=null;
private static String SKOS_CONCEPT="skos:concept";
private static String SKOS_CONCEPTSCHEME="skos:ConceptScheme";
private static String DC_TITLE="dc:title";
private static String SKOS_DEFINITION="skos:definition";
private static String SKOS_NOTE="skos:note";
private static String SKOS_SCNNOTE="skos:scopeNote";
private static String SKOS_TOPCONCEPT="skos:hasTopConcept";
private static String RDF_RESOURCE="rdf:resource";
private static String RDF_ABOUT="rdf:about";
private static String RDF_NODEID="rdf:nodeID";
private static String XML_LANG="xml:lang";
private static String SKOS_PREFLABEL="skos:prefLabel";
private static String SKOS_BROADER="skos:broader";
private static String SKOS_NARROWER="skos:narrower";
private static String SKOS_RELATED="skos:related";
private static String SKOS_ALTLABEL="skos:altLabel";
private static TreeMap TermEquiv=new TreeMap();
private static HashSet TermExp=new HashSet();
private static TreeMap TermCache=new TreeMap();
private static HashSet TermRT=new HashSet();
private static HashSet TermLang=new HashSet();
private static HashMap SubTermByLang=new HashMap();
private static HashSet UseTermsByLang=new HashSet();

/**
 *
 */
private String PDId=null;
/**
 *
 */
private String Name=null;
private String Description=null;
private String Use=null;
private String SCN=null;
private String Lang=null;
/**
 *
 */
private String ParentId=null;
/**
 *
 */
private boolean IsRootThesaur=false;
/**
 *
 */

static private ObjectsCache ThesaurObjectsCache = null;
static private HashSet ExportedTerms=new HashSet(100);
static private String ImportReport;

//-------------------------------------------------------------------------
/**
 *
 * @param Drv
 * @throws PDException
 */
public PDThesaur(DriverGeneric Drv) throws PDException
{
super(Drv);
}
//-------------------------------------------------------------------------
@Override
public void assignValues(Record Rec) throws PDException
{
setPDId((String) Rec.getAttr(fPDID).getValue());
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIP).getValue());
setUse((String) Rec.getAttr(fUSE).getValue());
setParentId((String) Rec.getAttr(fPARENTID).getValue());
setSCN((String) Rec.getAttr(fSCN).getValue());
setLang((String) Rec.getAttr(fLANG).getValue());
assignCommonValues(Rec);
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
public String getName()
{
return Name;
}
//-------------------------------------------------------------------------
/**
 * @param pName
 * @throws PDExceptionFunc  
*/
public void setName(String pName) throws PDExceptionFunc
{
if (pName!=null)    
    {
    pName=pName.trim();
    if (pName.length()==0)   
        PDExceptionFunc.GenPDException("Empty_Name_not_allowed",pName);
    if (pName.length()>128)   
        PDExceptionFunc.GenPDException("Name_longer_than_allowed",pName);
    }
Name = pName;
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
return("PD_THESAUR");
}
//-------------------------------------------------------------------------
/**
 * static method
 * @return
 */
static public String getTableNameThesLev()
{
return("PD_THES_LEV");
}
//-------------------------------------------------------------------------
/**
 * static method
 * @return
 */
static public String getTableNameThesRT()
{
return("PD_THES_RT");
}
//-------------------------------------------------------------------------
/**
 * static method
 * @return
 */
static public String getTableNameThesLang()
{
return("PD_THES_LANG");
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
@Override
synchronized public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();    
Rec.getAttr(fPDID).setValue(getPDId());
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fDESCRIP).setValue(getDescription());
Rec.getAttr(fUSE).setValue(getUse());
Rec.getAttr(fPDDATE).setValue(getPDDate());
Rec.getAttr(fSCN).setValue(getSCN());
Rec.getAttr(fLANG).setValue(getLang());
Rec.getAttr(fPARENTID).setValue(getParentId());
getCommonValues(Rec);
return Rec;

}
//-------------------------------------------------------------------------
@Override
protected Record getRecordStruct() throws PDException
{
if (ThesaurStruct==null)
    ThesaurStruct=CreateRecordStructPDThesaur();
return( ThesaurStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure
 * @return
 * @throws PDException
 */
static public Record getRecordStructPDThesaur() throws PDException
{
if (ThesaurStruct==null)
    ThesaurStruct=CreateRecordStructPDThesaur();
return(ThesaurStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDThesaur() throws PDException
{
if (ThesaurStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDID, "PDID","Unique_identifier", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fNAME, "Term_Name","Term_Name", Attribute.tSTRING, true, null, 128, false, true, true));
    R.addAttr( new Attribute(fDESCRIP, "Definition","Definition", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fUSE, "USE","Use_this_term", Attribute.tSTRING, false, null, 32, false, false, true));
    R.addAttr( new Attribute(fSCN, "Scope_Note","Scope_Note", Attribute.tSTRING, false, null, 254, false, false, true));
    R.addAttr( new Attribute(fLANG, "Language","Term Language", Attribute.tSTRING, false, null, 2, false, true, true));
    R.addAttr( new Attribute(fPARENTID, "Parent_Term","Parent_Term", Attribute.tSTRING, true, null, 32, false, true, false));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(ThesaurStruct);
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static protected Record getRecordStructPDThesaurLev() throws PDException
{
if (ThesaurLevStruct==null)
    ThesaurLevStruct=CreateRecordStructPDThesaurLev();
return(ThesaurLevStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDThesaurLev() throws PDException
{
if (ThesaurLevStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fGRANTPARENTID,fGRANTPARENTID, fGRANTPARENTID, Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPDID, fPDID, fPDID, Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(ThesaurLevStruct);
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static protected Record getRecordStructPDThesaurRT() throws PDException
{
if (ThesaurRTStruct==null)
    ThesaurRTStruct=CreateRecordStructPDThesaurRT();
return(ThesaurRTStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDThesaurRT() throws PDException
{
if (ThesaurRTStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDID2,fPDID2, fPDID2, Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPDID, fPDID, fPDID, Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(ThesaurRTStruct);
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static protected Record getRecordStructPDThesaurLang() throws PDException
{
if (ThesaurLangStruct==null)
    ThesaurLangStruct=CreateRecordStructPDThesaurLang();
return(ThesaurLangStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDThesaurLang() throws PDException
{
if (ThesaurLangStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDID, fPDID, fPDID, Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPDID2,fPDID2, fPDID2, Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(ThesaurLangStruct);
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
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Builds the default conditions identifying the Thesaur: Id="id"
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
ListCond.addCondition(new Condition(fNAME, Condition.cLIKE, VerifyWildCards(Name)));
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
//-------------------------------------------------------------------------
/**
* The install method is generic because for instantiate a object, the class
* need to access to the tables for definition
 * @param Drv
 * @throws PDException
*/
static protected void InstallMulti(DriverGeneric Drv) throws PDException
{
Drv.AddIntegrity(getTableName(), fPARENTID, getTableName(),           fPDID);
Drv.CreateTable(getTableNameThesLev(), getRecordStructPDThesaurLev());
Drv.AddIntegrity(getTableNameThesLev(), fPDID,          getTableName(), fPDID);
Drv.AddIntegrity(getTableNameThesLev(), fGRANTPARENTID, getTableName(), fPDID);
Drv.CreateTable(getTableNameThesRT(), getRecordStructPDThesaurRT());
Drv.AddIntegrity(getTableNameThesRT(), fPDID,          getTableName(), fPDID);
Drv.AddIntegrity(getTableNameThesRT(), fPDID2, getTableName(), fPDID);
Drv.CreateTable(getTableNameThesLang(), getRecordStructPDThesaurLang());
Drv.AddIntegrity(getTableNameThesLang(), fPDID,  getTableName(), fPDID);
Drv.AddIntegrity(getTableNameThesLang(), fPDID2, getTableName(), fPDID);
}
//-------------------------------------------------------------------------
/**
 * creates a new term of the Thesaurus
 * @throws PDException
 */
@Override
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
else if (!IsRootThesaur && getParentId().equalsIgnoreCase(getPDId()))
       PDException.GenPDException("Parent_Term_equals_to_actual_Term", getParentId());
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.insert:"+getPDId());
AddLogFields();
getRecord().Verify();
getDrv().InsertRecord(getTabName(), getRecord());
if (!IsRootThesaur)
    ActFoldLev();
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_creating_Thesaur",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Insert<");
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))  
if (!getDrv().getUser().getRol().isAllowCreateThesaur() )
   PDExceptionFunc.GenPDException("Thesaur_creation_not_allowed_to_user", null);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainThesaur() )
    PDExceptionFunc.GenPDException("Thesaur_delete_not_allowed_to_user", null);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainThesaur() )
   PDExceptionFunc.GenPDException("Thesaur_update_not_allowed_to_user", null);
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
protected void CreateRootThesaur() throws PDException
{
IsRootThesaur=true;
setPDId(ROOTTERM);
setName(ROOTTERM);
setDescription(ROOTTERM);
setParentId(ROOTTERM);
insert();
IsRootThesaur=false;
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
public String GenerateId()
{
StringBuilder genId = new StringBuilder(29); // so it can be possible to add _XX for language during import
genId.append(Long.toHexString(System.currentTimeMillis()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
return genId.toString();
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
private void ActFoldLev() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ActFoldLev");
HashSet GrandParentList=getListGrandParentList(getParentId());
GrandParentList.add(getParentId());
Record RecFL=getRecordStructPDThesaurLev();
RecFL.getAttr(fPDID).setValue(getPDId());
for (Iterator it = GrandParentList.iterator(); it.hasNext();)
    {
    Object GP = it.next();
    RecFL.getAttr(fGRANTPARENTID).setValue((String)GP);
    getDrv().InsertRecord(getTableNameThesLev(), RecFL);
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
    PDLog.Debug("PDThesaurs.getListGrandParentList>:"+Parent);
HashSet Result=new HashSet(4);
Condition CondParents=new Condition( fPDID, Condition.cEQUAL, Parent);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTableNameThesLev(), getRecordStructPDThesaurLev(), Conds);
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
    PDLog.Debug("PDThesaurs.getListGrandParentList<:"+Parent);
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
    PDLog.Debug("PDThesaurs.getListDescendList>:"+PDId);
HashSet Result=new HashSet();
Condition CondParents=new Condition( fGRANTPARENTID, Condition.cEQUAL, PDId);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTableNameThesLev(), getRecordStructPDThesaurLev(), Conds);
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
    PDLog.Debug("PDThesaurs.getListDescendList<:"+PDId);
return(Result);
}
//-------------------------------------------------------------------------
/**
 * Deletes the Thesaur and all the documents and subThesaurs contained recursively
 * If the number of Thesaurs, documents and levels is too big, this method can 
 * create problems in the rollback of the database storing the metadata
 * @throws PDException in any problem and cancels the Transaction. When enough permissions throws PDFuncException.
 */
@Override
public void delete() throws PDException
{
if (PDLog.isInfo())
    PDLog.Debug("PDThesaurs.delete>:"+getPDId());
boolean InTransLocal;
Load(getPDId());
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
DeleteTermRT();    
DeleteTermLang();    
DeleteTermsInTerm();
DeleteFoldLevelParents();
getDrv().DeleteRecord(getTabName(), getConditions());
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_deleting_Thesaur",Ex.getLocalizedMessage());
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.delete<:"+getPDId());
}
//-------------------------------------------------------------------------
    /**
     *
     * @throws PDException
     */
    public void DeleteTermRT() throws PDException
{   
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.DeleteTermRT:"+getPDId());     
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(PDThesaur.fPDID, Condition.cEQUAL, getPDId()));
Conds.addCondition(new Condition(PDThesaur.fPDID2, Condition.cEQUAL, getPDId()));
Conds.setOperatorAnd(false);
getDrv().DeleteRecord(getTableNameThesRT(), Conds);
}
//-------------------------------------------------------------------------
    /**
     *
     * @throws PDException
     */
    public void DeleteTermLang() throws PDException
{   
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.DeleteTermLang:"+getPDId());     
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(PDThesaur.fPDID, Condition.cEQUAL, getPDId()));
Conds.addCondition(new Condition(PDThesaur.fPDID2, Condition.cEQUAL, getPDId()));
Conds.setOperatorAnd(false);
getDrv().DeleteRecord(getTableNameThesLang(), Conds);
}
//-------------------------------------------------------------------------
/**
 * Recursive Deletes all the Thesaurs in actual Thesaur. 
 * Used as part of Thesaur delete
 */
private void DeleteTermsInTerm() throws PDException
{   
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.DeleteTermsInTerm:"+getPDId());     
Conditions Conds=new Conditions();
Condition CondChilds=new Condition(PDThesaur.fPARENTID, Condition.cEQUAL, getPDId());
Conds.addCondition(CondChilds);
Query Q=new Query(PDThesaur.getTableName(), PDThesaur.getRecordStructPDThesaur(), Conds);   
Cursor ListThesaursContained=getDrv().OpenCursor(Q);
Record NextFold=getDrv().NextRec(ListThesaursContained);
PDThesaur Fold2del=new PDThesaur(getDrv());
while (NextFold!=null)
    {
    Fold2del.assignValues(NextFold);  
    Fold2del.delete();
    NextFold=getDrv().NextRec(ListThesaursContained);
    }
getDrv().CloseCursor(ListThesaursContained);
}
//-------------------------------------------------------------------------
/**
 * Deletes all the references in upper levels to this Thesaur
 * @throws PDException
 */
private void DeleteFoldLevelParents()  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.DeleteFoldLevelParents:"+getPDId());    
Condition CondChilds=new Condition( fPDID, Condition.cEQUAL, getPDId());
Conditions Conds=new Conditions();
Conds.addCondition(CondChilds);
getDrv().DeleteRecord(getTableNameThesLev(), Conds);
}
//-------------------------------------------------------------------------
/**
 * Updates the metadata of the Thesaur
 * @throws PDException
 */
@Override
public void update()  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.update:"+getPDId());
boolean InTransLocal;
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
AddLogFields();
getDrv().UpdateRecord(getTabName(), getRecord(), getConditions());
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_updating_Thesaur",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
}
//-------------------------------------------------------------------------
/**
 * Return a list of all the Thesaurs of any type whose DIRECT parent is PDId
 * @param PDId Id of the parent Thesaur
 * @return a hashset with all the DirectDescend
 * @throws PDException in any error
 */
public HashSet getListDirectDescendList(String PDId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListDirectDescendList>:"+PDId);
LinkedHashSet Result=new LinkedHashSet(5);
Condition CondParents=new Condition( fPARENTID, Condition.cEQUAL, PDId);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Id=(String) Res.getAttr(fPDID).getValue();
    Result.add(Id);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListDirectDescendList<:"+PDId);
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
    PDLog.Debug("PDThesaurs.Load:"+Ident);
AsignKey(Ident);
Record r=(Record)getObjCache().get(Ident);
if (r==null) 
    {
    Query LoadAct=new Query(getTabName(), getRecordStructPDThesaur(),getConditions());
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
 * @return
 */
protected String getDefaultOrder()
{
return(fNAME);
}
//-------------------------------------------------------------------------
public String toString()
{
return this.getName();
}
//-------------------------------------------------------------------------
/**
 * Returns the Id of a Term with name TermName child of Term ParentId
 * @param ParentId id of parent Term
 * @param TermName name of child Term
 * @return Id of the found Term
 * @throws PDException if the Thesaur dosen't exist or the user it'snt allowed
 */
public String GetIdChild(String ParentId, String TermName) throws PDException
{
Condition CT=new Condition(fNAME, Condition.cEQUAL, TermName);
Condition CP=new Condition(fPARENTID, Condition.cEQUAL, ParentId);
Conditions Conds=new Conditions();
Conds.addCondition(CT);
Conds.addCondition(CP);
Query LoadAct=new Query(getTabName(), getRecordStructPDThesaur(), Conds);
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record r=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
if (r==null)
    PDExceptionFunc.GenPDException("do_not_exist_Term_under_Term", ParentId+"/"+TermName);
Attribute A=r.getAttr(fPDID);
return((String)A.getValue());
}
//-------------------------------------------------------------------------
/**
 * Search for Thesaurs returning a cursor with the results of Thesaurs with the
 * indicated values of fields. 
 * @param AttrConds Conditions over the fields ofthe ThesaurType
 * @param SubThesaurs if true seach in actual Thesaur AND subThesaurs, if false, serach in ALL the structure
 * @param IdActFold Thesaur to start the search. if null, start in the root level
 * @param Ord
 * @return a Cursor with the results of the query to use o send to NextTerm()
 * @throws PDException when occurs any problem
 */
public Cursor Search(Conditions AttrConds, boolean SubThesaurs, String IdActFold, Vector Ord) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Search >: {"+AttrConds+"} SubThesaurs:"+SubThesaurs+" IdActFold:"+IdActFold+" Ord:"+Ord);
PDThesaur F=new PDThesaur(getDrv());
Conditions ComposedConds=new Conditions();
ComposedConds.addCondition(AttrConds);
Vector TypList=new Vector();
TypList.add(getTabName());
if (SubThesaurs)
    {
    if (!(IdActFold==null) )
        {
        Condition C=new Condition(PDThesaur.fPDID, F.getListDescendList(IdActFold));
        ComposedConds.addCondition(C);
        }
    }
Record RecSearch=F.getRecord().CopyMono();
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
Query FoldSearch=new Query(TypList, RecSearch, ComposedConds, Ord);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Search <");
return(getDrv().OpenCursor(FoldSearch));
}
//-------------------------------------------------------------------------
/**
 * Receives a cursor created with method Search and returns the next Term or null if
 * there are no more.
 * @param Res Cursor created with method Search and serevral parameters
 * @return A new Object created with the data of the next row
 * @throws PDException when occurs any problem
 */
public PDThesaur NextTerm(Cursor Res) throws PDException
{
PDThesaur NextF=null;
Record Rec=getDrv().NextRec(Res);
if (Rec==null)
    return(NextF);
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
if (ThesaurObjectsCache==null)
    ThesaurObjectsCache=new ObjectsCache("Thes");
return(ThesaurObjectsCache);    
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
Record RFull=getRecord().Copy();
RFull.delRecord(getRecord());
return(RFull.toXML()+"</ListAttr>");    
}
//-------------------------------------------------------------------------
/**
 *
 * @param OPDObject
 * @param ParentThesaurId
 * @param MaintainId
 * @return
 * @throws PDException
 */
public PDThesaur ImportXMLNode(Node OPDObject, String ParentThesaurId, boolean MaintainId) throws PDException
{
NodeList childNodes = OPDObject.getChildNodes();
PDThesaur NewFold=null;
for (int i = 0; i < childNodes.getLength(); i++)
    {
    Node item = childNodes.item(i);
    if (item.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(item, getRecord());
        NewFold=new PDThesaur(getDrv()); 
        NewFold.assignValues(r);
        if (!MaintainId)
            NewFold.setPDId(null);
        NewFold.setParentId(ParentThesaurId);
        }
    }
NewFold.insert();
return NewFold;
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLFile
 * @param ParentThesaurId
 * @return 
 * @throws PDException
 */
public PDThesaur ProcessXML(File XMLFile, String ParentThesaurId) throws PDException
{
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject = null;
PDThesaur NewFold=null;
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    NewFold=ImportXMLNode(OPDObject, ParentThesaurId, false);
    }
return(NewFold); // returned LAST Thesaur when opd file contains several.
}catch(Exception ex)
    {
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
/**
* @return the Description
*/
public String getDescription()
{
return Description;
}
//---------------------------------------------------------------------
/**
 * @param pDescription
 * @throws PDExceptionFunc  
 */
public void setDescription(String pDescription) throws PDExceptionFunc
{
if (pDescription!=null && pDescription.length()>254)
    PDExceptionFunc.GenPDException("Description_longer_than_allowed", pDescription);
Description = pDescription;
}
//---------------------------------------------------------------------
/**
* @return the Use
*/
public String getUse()
{
return Use;
}
//---------------------------------------------------------------------
/**
* @param Use the Use to set
*/
public void setUse(String Use)
{
this.Use = Use;
}
//---------------------------------------------------------------------
/**
 * List all the Thesaurs
 * @return hashset containing all the thesaurs Id
 * @throws PDException 
 */
public HashSet ListThes() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListThes>:"+PDId);
LinkedHashSet Result=new LinkedHashSet(5);
Condition CondParents=new Condition(fPARENTID, fPDID);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
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
    PDLog.Debug("PDThesaurs.ListThes<:"+PDId);
return(Result);
}
//---------------------------------------------------------------------
/** Obtain a list of the Codes of related terms
 * 
 * @param TermId
 * @return
 * @throws PDException
 */
public HashSet getListRT(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListRT>:"+TermId);
HashSet ListRT=new HashSet();
Condition CondRT1=new Condition(fPDID, Condition.cEQUAL, TermId);
Condition CondRT2=new Condition(fPDID2, Condition.cEQUAL, TermId);
Conditions Conds=new Conditions();
Conds.setOperatorAnd(false);
Conds.addCondition(CondRT1);
Conds.addCondition(CondRT2);
Query Q=new Query(getTableNameThesRT(), getRecordStructPDThesaurRT(), Conds, null);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String PD1=(String) Res.getAttr(fPDID).getValue();
    if (!PD1.equalsIgnoreCase(TermId))
        ListRT.add(PD1);
    else
        {
        ListRT.add((String) Res.getAttr(fPDID2).getValue());
        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListRT<:"+TermId);
return(ListRT);
}
//---------------------------------------------------------------------
/** Obtain a list of the Codes of related terms
 * 
 * @param TermId
 * @return
 * @throws PDException
 */
public HashSet getListLang(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListLang>:"+TermId);
HashSet ListRT=new HashSet();
Condition CondRT1=new Condition(fPDID, Condition.cEQUAL, TermId);
Condition CondRT2=new Condition(fPDID2, Condition.cEQUAL, TermId);
Conditions Conds=new Conditions();
Conds.setOperatorAnd(false);
Conds.addCondition(CondRT1);
Conds.addCondition(CondRT2);
Query Q=new Query(getTableNameThesLang(), getRecordStructPDThesaurLang(), Conds, null);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String PD1=(String) Res.getAttr(fPDID).getValue();
    if (!PD1.equalsIgnoreCase(TermId))
        ListRT.add(PD1);
    else
        {
        ListRT.add((String) Res.getAttr(fPDID2).getValue());
        }
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListLang<:"+TermId);
return(ListRT);
}
//---------------------------------------------------------------------
/** Obtain a CURSOR of the <b>Records<b> of narrow terms
 * 
 * @param TermId
 * @return
 * @throws PDException
 */
public Cursor ListNT(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListRT>:"+TermId);
Cursor CursorId=LoadList(getListDirectDescendList(TermId));
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListRT<:"+TermId);
return(CursorId);
}
//---------------------------------------------------------------------
/** Obtain a CURSOR of the <b>Records<b> of related terms
 * 
 * @param TermId
 * @return
 * @throws PDException
 */
public Cursor ListRT(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListRT>:"+TermId);
Cursor CursorId=LoadList(getListRT(TermId));
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListRT<:"+TermId);
return(CursorId);
}
//---------------------------------------------------------------------
/** Obtain a CURSOR of the <b>Records<b> of related terms
 * 
 * @param TermId
 * @return
 * @throws PDException
 */
public Cursor ListLang(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListLang>:"+TermId);
Cursor CursorId=LoadList(getListLang(TermId));
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListLang<:"+TermId);
return(CursorId);
}
//---------------------------------------------------------------------
/** Obtain a list of the Codes of Used For terms
 * 
 * @param TermId
 * @return
 * @throws PDException
 */
public HashSet getListUF(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListUF>:"+TermId);
HashSet ListUF=new HashSet();
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(fUSE, Condition.cEQUAL, TermId));
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String PD1=(String) Res.getAttr(fPDID).getValue();
    ListUF.add(PD1);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListUF<:"+TermId);
return(ListUF);
}
//---------------------------------------------------------------------
/* Obtain a CURSOR  of the records of Used For terms
 * 
 * @param TermId
 * @return
 * @throws PDException
 */
    /**
     *
     * @param TermId
     * @return
     * @throws PDException
     */
    public Cursor ListUF(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListUF>:"+PDId);
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(fUSE, Condition.cEQUAL, TermId));
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListUF<:"+PDId);
return(CursorId);
}
//---------------------------------------------------------------------
/**
 * Creates a Cursot containing all the termm with the PDID included in IdList
 * @param IdList List of Ids
 * @return Cursor Opened
 * @throws PDException in an error
 */
public Cursor LoadList(HashSet IdList) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.LoadList<:"+IdList);
Condition CondRT1=new Condition(fPDID, IdList);
Conditions Conds=new Conditions();
Conds.addCondition(CondRT1);
Query Q=new Query(getTableName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.LoadList<");
return(CursorId);   
}
//---------------------------------------------------------------------
/**
 * Creates a Cursot containing all the termm with the PDID included in IdList
 * @param IdList List of Ids
 * @return Cursor Opened
 * @throws PDException in an error
 */
public Vector getList(HashSet IdList) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getList<:"+IdList);
PDThesaur Term=new PDThesaur(getDrv());
Vector ListAtr=new Vector(IdList.size());
for (Iterator it = IdList.iterator(); it.hasNext();)
    {
    Term.Load((String)it.next());
    ListAtr.add(Term.getRecord());    
    }
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getList<");
return(ListAtr);   
}
//---------------------------------------------------------------------
/* For the current PDID of a term, return the Id of the container thesaur.
 * 
 */
    /**
     *
     * @return
     * @throws PDException
     */
    public String getIDThesaur() throws PDException
{
if (getParentId().equals(PDThesaur.ROOTTERM))
    return(getPDId());
PDThesaur Term=new PDThesaur(getDrv());
Term.Load(getParentId());
return(Term.getIDThesaur());
}
//---------------------------------------------------------------------
    /**
     *
     * @param memRT
     * @throws PDException
     */
    public void AddRT(HashSet memRT) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.AddRT:"+getPDId()+">"+memRT);
boolean InTransLocal;
VerifyAllowedIns();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
Record R=getRecordStructPDThesaurRT();
try {
for (Iterator it = memRT.iterator(); it.hasNext();)
    {
    String IdRT=(String)it.next();
    if (IdRT.compareTo(getPDId())>0)
        {
        R.getAttr(fPDID).setValue(IdRT);
        R.getAttr(fPDID2).setValue(getPDId());
        }
    else if (IdRT.compareTo(getPDId())<0) // necesary to ifnore Id2==Id
        {
        R.getAttr(fPDID).setValue(getPDId());        
        R.getAttr(fPDID2).setValue(IdRT);
        }
    getDrv().InsertRecord(getTableNameThesRT(), R);
    }
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_creating_Thesaur",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.AddRT:"+getPDId()+"<");
}
//---------------------------------------------------------------------
    /**
     *
     * @param memLang
     * @throws PDException
     */
    public void AddLang(HashSet memLang) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.AddLang:"+getPDId()+">"+memLang);
boolean InTransLocal;
VerifyAllowedIns();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
Record R=getRecordStructPDThesaurLang();
try {
for (Iterator it = memLang.iterator(); it.hasNext();)
    {
    String IdRT=(String)it.next();
    if (IdRT.compareTo(getPDId())>0)
        {
        R.getAttr(fPDID).setValue(IdRT);
        R.getAttr(fPDID2).setValue(getPDId());
        }
    else if (IdRT.compareTo(getPDId())<0) // necesary to ignore Id2==Id
        {
        R.getAttr(fPDID).setValue(getPDId());        
        R.getAttr(fPDID2).setValue(IdRT);
        }
    getDrv().InsertRecord(getTableNameThesLang(), R);
    }
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_creating_Thesaur",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.AddLang:"+getPDId()+"<");
}
//---------------------------------------------------------------------
/**
 * @return the SCN
 */
public String getSCN()
{
return SCN;
}
//---------------------------------------------------------------------
/**
 * @param SCN the SCN to set
 */
public void setSCN(String SCN)
{
this.SCN = SCN;
}
//---------------------------------------------------------------------
/**
 * @return the Lang
 */
public String getLang()
{
return Lang;
}
//---------------------------------------------------------------------
/**
 * @param Lang the Lang to set
 */
public void setLang(String pLang)
{
if (pLang!=null)    
    Lang = pLang.trim().toUpperCase();
else
    Lang = pLang;
}
//---------------------------------------------------------------------
/**
 * Exports a thesaurus to RDF-XML format
 * @param ExpThesId thesaurus Id
 * @param Path Complete name of file to export
 * @param Root Root to be included in the RDF (i.e. :http://metadataregistry.org/uri/FTWG/ )
 * @param MainLang Language that defines the ID of terms and "walking" of thesaur
 * @throws PDException  
 */
public void Export(String ExpThesId, String Path, String Root, String MainLang) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Export:"+ExpThesId+"|"+Path+"|"+Root+"|"+MainLang+">");    
PrintWriter PW=null;     
try {    
PW = new PrintWriter(Path);
PW.println(StartSKOSXML());    
Load(ExpThesId);
WriteThes(this, PW, Root, MainLang);
PW.println(EndSKOSXML());  
PW.flush();
} catch(Exception ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(), "");
    }
finally
    {
    if (PW!=null)    
        PW.close();        
    }
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Export:"+"<");    
}
//---------------------------------------------------------------------
private static String StartSKOSXML()
{
return("<?xml version=\"1.0\" encoding = \"UTF-8\"?>"
    +"<rdf:RDF "
+"xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" "
+"xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\" "
+"xmlns:dc=\"http://purl.org/dc/elements/1.1#\" >");      
}
//---------------------------------------------------------------------
private static String EndSKOSXML()
{
return("</rdf:RDF>");    
}
//---------------------------------------------------------------------
private void WriteThes(PDThesaur Thes, PrintWriter PW, String Root, String MainLang) throws PDException
{
ExportedTerms.clear();    
PW.print(StartSKOSXMLConceptSchema());    
PW.print(Root); 
PW.print(Thes.getIDThesaur()+"\">"); 
PW.println("<dc:title xml:lang=\""+Thes.getLang()+"\">"+Thes.getName()+"</dc:title>");
PW.println("<dc:creator xml:lang=\""+Thes.getLang()+"\">"+getDrv().getUser().getName()+"</dc:creator>");
if (Thes.getDescription()!=null && Thes.getDescription().length()>0)
    PW.println("<skos:definition xml:lang=\""+Thes.getLang()+"\">"+Thes.getDescription()+"</skos:definition>");
if (Thes.getSCN()!=null && Thes.getSCN().length()>0)
    PW.println("<skos:note xml:lang=\""+Thes.getLang()+"\">"+Thes.getSCN()+"</skos:note>");
HashSet List=Thes.getListDirectDescendList(Thes.getPDId());
HashSet ListLang=new HashSet(List);
PDThesaur Child=new PDThesaur(this.getDrv());
for (Iterator it = List.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();    
    Child.Load(Id);
    if (Child.Lang==null || !Child.Lang.equalsIgnoreCase(MainLang) || ( Child.getUse()!=null && Child.getUse().length()!=0))
        {
        ListLang.remove(Id);    
        continue;
        }
    PW.println("<skos:hasTopConcept rdf:resource=\""+Root+Id+"\"/>");    
    }
PW.println(EndSKOSXMLConceptSchema());  
ExportedTerms.add(Thes.getPDId());
for (Iterator it = ListLang.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();  
    Child.Load(Id);
    WriteTerm(Child, PW, Root, MainLang, Thes.getPDId());    
    }
ExportedTerms.clear();    
}
//---------------------------------------------------------------------
private static String StartSKOSXMLConceptSchema()
{
return("<skos:ConceptScheme rdf:about=\"");        
}
//---------------------------------------------------------------------
private static String EndSKOSXMLConceptSchema()
{
return("</skos:ConceptScheme>");    
}
//---------------------------------------------------------------------
private static String StartSKOSXMLConcept()
{
return("<skos:Concept rdf:about=\"");        
}
//---------------------------------------------------------------------
private static String EndSKOSXMLConcept()
{
return("</skos:Concept>");    
}
//---------------------------------------------------------------------

private void WriteTerm(PDThesaur Term, PrintWriter PW, String Root, String MainLang, String ThesId) throws PDException
{
if (ExportedTerms.contains(Term.getPDId())) 
    return;
if (Term.getUse()!=null && !Term.getUse().isEmpty())
    return;
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.WriteTerm:"+Term.getPDId()+">");    
PW.print(StartSKOSXMLConcept());    
PW.print(Root); 
PW.println(Term.getPDId()+"\" xml:lang=\""+Term.getLang()+"\">"); 
PW.println("<skos:prefLabel xml:lang=\""+Term.getLang()+"\">"+Term.getName()+"</skos:prefLabel>");
PW.println("<skos:inScheme rdf:resource=\""+Root+ThesId+"\"/>");
if (Term.getDescription()!=null && Term.getDescription().length()>0)
    PW.println("<skos:definition xml:lang=\""+Term.getLang()+"\">"+Term.getDescription()+"</skos:definition>");
if (Term.getSCN()!=null && Term.getSCN().length()>0)
    PW.println("<skos:scopeNote xml:lang=\""+Term.getLang()+"\">"+Term.getSCN()+"</skos:scopeNote>");
if (Term.getParentId().equals(ThesId))
    PW.println("<skos:topConceptOf rdf:resource=\""+Root+Term.getParentId()+"\"/>");    
else
    PW.println("<skos:broader rdf:resource=\""+Root+Term.getParentId()+"\"/>");    
PDThesaur AuxTerm=new PDThesaur(this.getDrv());
HashSet ListLang=Term.getListLang(Term.getPDId()); // -------------
for (Iterator it = ListLang.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();    
    AuxTerm.Load(Id);
    PW.println("<skos:prefLabel xml:lang=\""+AuxTerm.getLang()+"\">"+AuxTerm.getName()+"</skos:prefLabel>");
    if (AuxTerm.getDescription()!=null && AuxTerm.getDescription().length()>0)
        PW.println("<skos:definition xml:lang=\""+AuxTerm.getLang()+"\">"+AuxTerm.getDescription()+"</skos:definition>");
    if (AuxTerm.getSCN()!=null && AuxTerm.getSCN().length()>0)
        PW.println("<skos:scopeNote xml:lang=\""+AuxTerm.getLang()+"\">"+AuxTerm.getSCN()+"</skos:scopeNote>");
    HashSet ListUF=Term.getListUF(AuxTerm.getPDId()); // -------------
    PDThesaur AuxTerm2=new PDThesaur(this.getDrv());
    for (Iterator itU = ListUF.iterator(); itU.hasNext();)
        {
        String IdU=(String)itU.next();    
        AuxTerm2.Load(IdU);
        PW.println("<skos:altLabel xml:lang=\""+AuxTerm2.getLang()+"\">"+AuxTerm2.getName()+"</skos:altLabel>");    
        }    
    }
HashSet ListUF=Term.getListUF(Term.getPDId()); // -------------
for (Iterator it = ListUF.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();    
    AuxTerm.Load(Id);
    PW.println("<skos:altLabel xml:lang=\""+AuxTerm.getLang()+"\">"+AuxTerm.getName()+"</skos:altLabel>");    
    }
HashSet ListDChild=Term.getListDirectDescendList(Term.getPDId()); // -------------
HashSet ListChild=new HashSet(ListDChild);
for (Iterator it = ListDChild.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();    
    AuxTerm.Load(Id);
    if (AuxTerm.Lang==null || !AuxTerm.Lang.equalsIgnoreCase(MainLang) || ( AuxTerm.getUse()!=null && AuxTerm.getUse().length()!=0))
        {
        ListLang.remove(Id);    
        continue;
        }
    PW.println("<skos:narrower rdf:resource=\""+Root+Id+"\"/>");    
    }
HashSet ListRT=Term.getListRT(Term.getPDId()); // -------------
HashSet ListRTerms=new HashSet(ListRT);
for (Iterator it = ListRT.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();    
    AuxTerm.Load(Id);
    if (AuxTerm.Lang==null || !AuxTerm.Lang.equalsIgnoreCase(MainLang))
        {
        ListRTerms.remove(Id);    
        continue;
        }
    PW.println("<skos:related rdf:resource=\""+Root+Id+"\"/>");    
    }
PW.println(EndSKOSXMLConcept());  
ExportedTerms.add(Term.getPDId());
for (Iterator it = ListRTerms.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();  
    AuxTerm.Load(Id);
    WriteTerm(AuxTerm, PW, Root, MainLang, ThesId);    
    }
for (Iterator it = ListChild.iterator(); it.hasNext();)
    {
    String Id=(String)it.next();  
    AuxTerm.Load(Id);
    WriteTerm(AuxTerm, PW, Root, MainLang, ThesId);    
    }
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.WriteTerm:"+Term.getPDId()+"<");    
}
//---------------------------------------------------------------------
/**
 * Import a thesaurus in RDF-XML format
 * @param ThesName 
 * @param ImpThesId thesaurus Id
 * @param XMLFile 
 * @param Root Root to be included in the RDF (i.e. :http://metadataregistry.org/uri/FTWG/ )
 * @param MainLang Language that defines the ID of terms and "walking" of thesaur
 * @return 
 * @throws PDException  
 */
synchronized public int Import(String ThesName, String ImpThesId, File XMLFile, String MainLang, String Root, boolean SubThesLang, boolean Transact) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Import:"+ThesName+"|"+ImpThesId+"|"+Root+"|"+MainLang+">");        
try {
Date t1=new Date();    
ImportReport="";    
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
Node SkosObjectConcept;
Node SkosObjectSub;
TermEquiv.clear();
TermExp.clear();
TermCache.clear();
TermRT.clear();
TermLang.clear();
int Tot=0;
PDThesaur Thes=new PDThesaur(getDrv());
PDThesaur Term;
Thes.setPDId(ImpThesId);
Thes.setName(ThesName);
Thes.setParentId(PDThesaur.ROOTTERM);
Thes.setLang(MainLang);
if (Transact)
   getDrv().IniciarTrans();
Thes.insert(); Tot++;
TermExp.add(Thes.getPDId());
NodeList ConceptObjectList = XMLObjects.getElementsByTagName("*");
NodeList SubConceptObjectList;
for (int NumConc=0; NumConc<ConceptObjectList.getLength(); NumConc++)
    {
    SkosObjectConcept = ConceptObjectList.item(NumConc);   
    if (SkosObjectConcept.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_CONCEPTSCHEME))
        {
        SubConceptObjectList=SkosObjectConcept.getChildNodes();
        for (int NumNod=0; NumNod<SubConceptObjectList.getLength(); NumNod++)
            {
            SkosObjectSub = SubConceptObjectList.item(NumNod);
            if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.DC_TITLE))
                {
                Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.XML_LANG);    
                if (Res==null || Res.getNodeValue().equalsIgnoreCase(MainLang))  
                   Thes.setName(SkosObjectSub.getTextContent());
                }
            else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_DEFINITION))
                {
                Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.XML_LANG);    
                if (Res==null || Res.getNodeValue().equalsIgnoreCase(MainLang))  
                    Thes.setDescription(SkosObjectSub.getTextContent());
                }
            else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_NOTE) || SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_SCNNOTE) )
                {
                Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.XML_LANG);    
                if (Res==null || Res.getNodeValue().equalsIgnoreCase(MainLang))  
                    Thes.setSCN(SkosObjectSub.getTextContent());    
                }
            else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_TOPCONCEPT))
                {
                Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.RDF_RESOURCE); 
                if (Res!=null && Res.getNodeValue().length()>=Root.length())  
                    {
                    String SourceId=Res.getNodeValue().substring(Root.length()); 
                    Term=ObtainTerm(SourceId);                    
                    Term.setName(SourceId); // provisional
                    Term.setLang(MainLang);
                    Term.setParentId(Thes.getPDId());
                    StoreTerm(SourceId, Term);
                    }        
                }   
            }
        Thes.update();
        }
    else if (SkosObjectConcept.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_CONCEPT))
        {
        String SourceId=GetOrigId(SkosObjectConcept, Root);    
        if (SourceId==null) // no way to match references in Skos file
            continue;
        Term=ObtainTerm(SourceId);
        FillTerm(Term, SkosObjectConcept, MainLang, Root, Thes, SubThesLang);
        StoreTerm(SourceId, Term);
        Tot++;
        }
    }
StoreCache();
if (Transact)
   getDrv().CerrarTrans();
Date t2=new Date();    
ImportReport="<html>Import Start:"+t1+"<br>Concepts= <b>"+Tot+"</b> >>> OPD Terms= <b>"+TermCache.size()+"</b><br>"+"Relations= <b>"+TermRT.size()+"</b><br><b>Warnings:</b><br>"+ImportReport+"<br>Import End:"+t2+"</html>";
TermEquiv.clear();
TermExp.clear();
TermCache.clear();
TermRT.clear();
TermLang.clear();
SubTermByLang.clear();
UseTermsByLang.clear();
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Import:"+"<");    
return(Tot);
} catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    if (getDrv().isInTransaction())
        getDrv().AnularTrans();
    ex.printStackTrace();
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
/**
 * Extracts the original Id from the XML
 * @param SkosObjectConcept
 * @param Root URL base
 * @return Id in Origin
 */
private String GetOrigId(Node SkosObjectConcept, String Root)
{
String SourceId=null;
Node Res=SkosObjectConcept.getAttributes().getNamedItem(PDThesaur.RDF_ABOUT); 
if (Res!=null && Res.getNodeValue().length()>=Root.length())  
    {
    SourceId=Res.getNodeValue().substring(Root.length());                       
    }
if (SourceId==null)
    {
    Res=SkosObjectConcept.getAttributes().getNamedItem(PDThesaur.RDF_NODEID); 
    if (Res!=null && Res.getNodeValue().length()!=0) 
        {
        SourceId=Res.getNodeValue();                       
        }
    }
return(SourceId);
}
//---------------------------------------------------------------------
/**
 * Returns a Term (a new one, from cache or Database)
 * @param SourceId Original Id of the Term
 * @return  
 * @throws PDException in any error
 */
private PDThesaur ObtainTerm(String SourceId) throws PDException
{
PDThesaur Term;    
String NewId=(String)TermEquiv.get(SourceId); 
if (NewId==null)
    {
    Term=new PDThesaur(getDrv());
    NewId=GenerateId();
    Term.setPDId(NewId);
    Term.setName(SourceId);
    StoreTerm(SourceId, Term);
    }
return (PDThesaur)TermCache.get(NewId);
}
//---------------------------------------------------------------------
/**
 * Fill the term with al the information in XML node, including to create references term
 * @param Term Term to be filled
 * @param SkosObjectConcept XML object
 * @param MainLang Primary languag during import
 * @param Root Base URL
 * @param Thes Thesaurus
 */
private void FillTerm(PDThesaur Term, Node SkosObjectConcept, String MainLang, String Root, PDThesaur Thes, boolean SubThesLang) throws PDExceptionFunc, PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.FillTerm:"+Term.getPDId()+">");        
NodeList SubConceptObjectList=SkosObjectConcept.getChildNodes();
Node SkosObjectSub;
SubTermByLang.clear();
UseTermsByLang.clear();
boolean BTAsigned=(Term.getParentId()!=null);
HashSet LangList=new HashSet();
LangList.add(Term.getPDId());
for (int NumNod=0; NumNod<SubConceptObjectList.getLength(); NumNod++)
    {
    SkosObjectSub = SubConceptObjectList.item(NumNod);
    if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_PREFLABEL))
        {
        Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.XML_LANG);  
        if (Res==null)
            {
            Term.setName(SkosObjectSub.getTextContent());
            Term.setLang(MainLang);
            }
        else if (Res.getNodeValue().equalsIgnoreCase(MainLang))  
            {
            Term.setName(SkosObjectSub.getTextContent());
            Term.setLang(MainLang);
            }
        else 
            {
            String SourceId=SkosObjectSub.getTextContent();  
            String LangNode=Res.getNodeValue().toUpperCase();
            PDThesaur ChildTerm;
            String PrevLangTerm=(String)SubTermByLang.get(LangNode);
            if (PrevLangTerm!=null && PrevLangTerm.length()!=0)
                {
                ChildTerm=ObtainTerm(PrevLangTerm);
                }
            else
                {
                ChildTerm=new PDThesaur(getDrv());
                ChildTerm.setLang(LangNode);
                ChildTerm.setPDId(Term.getPDId()+"_"+ChildTerm.getLang());    
                PrevLangTerm=SourceId+"_"+ChildTerm.getLang();
                SubTermByLang.put(ChildTerm.getLang(), PrevLangTerm);
                }
            ChildTerm.setName(SourceId);
            StoreTerm(PrevLangTerm, ChildTerm);
            LangList.add(ChildTerm.getPDId());
            }
        }
    else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_DEFINITION))
        {
        Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.XML_LANG);    
        String SourceId=SkosObjectSub.getTextContent();  
        String LangNode=Res.getNodeValue().toUpperCase();
        if (SourceId.length()>254)
            SourceId=SourceId.substring(0, 253);
        if (Res==null)  
           Term.setDescription(SourceId);
        else if (LangNode.equalsIgnoreCase(MainLang))  
           Term.setDescription(SourceId);
        else
            {
            PDThesaur ChildTerm;
            String PrevLangTerm=(String)SubTermByLang.get(LangNode);
            if (PrevLangTerm!=null && PrevLangTerm.length()!=0)
                {
                ChildTerm=ObtainTerm(PrevLangTerm);
                }
            else
                {
                ChildTerm=new PDThesaur(getDrv());
                ChildTerm.setLang(LangNode);
                ChildTerm.setPDId(Term.getPDId()+"_"+ChildTerm.getLang());    
                PrevLangTerm=SourceId+"_"+ChildTerm.getLang();
                SubTermByLang.put(ChildTerm.getLang(), PrevLangTerm);
                }
            ChildTerm.setDescription(SourceId);
            StoreTerm(PrevLangTerm, ChildTerm);
            LangList.add(ChildTerm.getPDId());
            }
        }
    else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_NOTE) || SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_SCNNOTE) )
        {
        Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.XML_LANG);    
        String SourceId=SkosObjectSub.getTextContent();  
        String LangNode=Res.getNodeValue().toUpperCase();
        if (SourceId.length()>254)
            SourceId=SourceId.substring(0, 253);
        if (Res==null)  
            Term.setSCN(SourceId);    
        else if (LangNode.equalsIgnoreCase(MainLang))  
            Term.setSCN(SourceId);    
        else
            {
            PDThesaur ChildTerm;
            String PrevLangTerm=(String)SubTermByLang.get(LangNode);
            if (PrevLangTerm!=null && PrevLangTerm.length()!=0)
                {
                ChildTerm=ObtainTerm(PrevLangTerm);
                }
            else
                {
                ChildTerm=new PDThesaur(getDrv());
                ChildTerm.setLang(LangNode);
                ChildTerm.setPDId(Term.getPDId()+"_"+ChildTerm.getLang());    
                PrevLangTerm=SourceId+"_"+ChildTerm.getLang();
                SubTermByLang.put(ChildTerm.getLang(), PrevLangTerm);
                }
            ChildTerm.setSCN(SourceId);
            StoreTerm(PrevLangTerm, ChildTerm);
            LangList.add(ChildTerm.getPDId());
            }
        }
    else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_BROADER))
        {
        Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.RDF_RESOURCE); 
        String SourceId=Res.getNodeValue().substring(Root.length());   
        PDThesaur ParentTerm=ObtainTerm(SourceId);                    
        StoreTerm(SourceId, ParentTerm);
        Term.setParentId(ParentTerm.getPDId());
        if (BTAsigned)
           ImportReport+="Lost PolyHierarchy="+Term.getRecord()+"<br>";
        BTAsigned=true;    
        }
    else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_NARROWER))
        {
        Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.RDF_RESOURCE); 
        String SourceId=Res.getNodeValue().substring(Root.length());   
        PDThesaur ChildTerm=ObtainTerm(SourceId);                    
        ChildTerm.setParentId(Term.getPDId());
        StoreTerm(SourceId, ChildTerm);
        }
     else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_RELATED))
        {
        Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.RDF_RESOURCE); 
        String SourceId=Res.getNodeValue().substring(Root.length());   
        PDThesaur RtTerm=ObtainTerm(SourceId);                    
        AddImportRT(Term.getPDId(),RtTerm.getPDId());   
        }
    else if (SkosObjectSub.getNodeName().equalsIgnoreCase(PDThesaur.SKOS_ALTLABEL))
        {
        String SourceId=SkosObjectSub.getTextContent();    
        PDThesaur UseTerm=ObtainTerm(SourceId);     
        UseTerm.setName(SkosObjectSub.getTextContent());    
        Node Res=SkosObjectSub.getAttributes().getNamedItem(PDThesaur.XML_LANG);    
        if (Res!=null)  
            UseTerm.setLang(Res.getNodeValue());
        else
            UseTerm.setLang(MainLang);
        if (UseTerm.getLang().equalsIgnoreCase(MainLang))
            {
            UseTerm.setUse(Term.getPDId());
            }
        else
            {
            UseTerm.setUse(Term.getPDId()+"_"+UseTerm.getLang());
            }
        StoreTerm(SourceId, UseTerm);
        UseTermsByLang.add(SourceId);
        }
    }
if (Term.getParentId()==null || Term.getParentId().length()==0)
    PDExceptionFunc.GenPDException("Term_without_BT", Term.getName());
if (PDLog.isDebug())
    PDLog.Debug("Term Record="+Term.getRecord());
PDThesaur TermTemp;    
for (Iterator it = UseTermsByLang.iterator(); it.hasNext();)
    {
    String Id = (String)it.next();
    TermTemp=ObtainTerm(Id);
    if (Term.getParentId().equalsIgnoreCase(Thes.getPDId()))
        {
        if (SubThesLang)               
            TermTemp.setParentId(ObtainSubThes(Thes.getPDId(), TermTemp.getLang()));
        else
            TermTemp.setParentId(Thes.getPDId());
        }
    else
        {
        if (TermTemp.getLang().equalsIgnoreCase(MainLang))
           TermTemp.setParentId(Term.getParentId());
        else
           TermTemp.setParentId(Term.getParentId()+"_"+TermTemp.getLang());
        }
    if (PDLog.isDebug())
        PDLog.Debug("UseTermsByLang TermTemp Record="+TermTemp.getRecord());
    }
for (Iterator it = SubTermByLang.keySet().iterator(); it.hasNext();)
    {
    String Id = (String)SubTermByLang.get((String)it.next());
    TermTemp=ObtainTerm(Id);
    if (Term.getParentId().equalsIgnoreCase(Thes.getPDId()))
        {
        if (SubThesLang)               
            TermTemp.setParentId(ObtainSubThes(Thes.getPDId(), TermTemp.getLang()));
        else
            TermTemp.setParentId(Thes.getPDId());
        }
    else
        {
        if (TermTemp.getLang().equalsIgnoreCase(MainLang))
           TermTemp.setParentId(Term.getParentId());
        else
           TermTemp.setParentId(Term.getParentId()+"_"+TermTemp.getLang());
        }
    if (PDLog.isDebug())
        PDLog.Debug("SubTermByLang TermTemp Record="+TermTemp.getRecord());
    }
if (LangList.size()>1)
    {
    String []ListId=new String[LangList.size()];
    int N=0;
    for (Iterator it = LangList.iterator(); it.hasNext();)
        {
        ListId[N++]=(String)it.next();            
        }
    for (int i = 0; i < ListId.length; i++)
        {
        for (int j = i+1; j < ListId.length; j++)
            {
            AddImportLang(ListId[i], ListId[j]);
            }
        }
    }
if (SubThesLang && Term.getParentId().equalsIgnoreCase(Thes.getPDId())  )               
    Term.setParentId(ObtainSubThes(Thes.getPDId(), Term.getLang()));
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.FillTerm:"+Term.getPDId()+"<");        
}
//---------------------------------------------------------------------
/**
 * "Store" the term, updating if previously stored, inserting in DB if posiblo 
 * or inserting in cache
 * @param SourceId Original Id
 * @param Term Term to be stored
 * @throws PDException in any error
 */
private void StoreTerm(String SourceId, PDThesaur Term) throws PDException
{
TermCache.put(Term.getPDId(), Term);
TermEquiv.put(SourceId, Term.getPDId());
}
//---------------------------------------------------------------------
/**
 * Checks if the insertion in DB is posible due the relations
 * @param Term Element to be inserted
 * @return 
 */
private boolean CanInsert(PDThesaur Term)
{
if (Term.getParentId()!=null && !TermExp.contains(Term.getParentId()))
    return(false);
if (Term.getUse()!=null && !TermExp.contains(Term.getUse()))
    return(false);
return(true);
}
//---------------------------------------------------------------------
/**
 * Dumps all the elements in cache doing severals loops until the list is empty 
 * or exists a deadlock by circular relations (detected by loops with zero insertions)
 */
private void StoreCache() throws PDException
{
boolean StoredTerm;    
boolean SomeNotNull;    
if (PDLog.isDebug())
    {
    PDLog.Debug("PDThesaurs.StoreCache:"+TermCache.keySet()+">");        
    PDLog.Debug("PDThesaurs.StoreCache:"+TermEquiv+">");        
    }
do {  
StoredTerm=false;        
SomeNotNull=false;    
for (Iterator it = TermCache.keySet().iterator(); it.hasNext();)
    {
    String TermId= (String) it.next();
    PDThesaur Term= (PDThesaur) TermCache.get(TermId);
    if (Term!=null)
        {
        SomeNotNull=true;   
        if (Term.getParentId()==null || Term.getParentId().length()==0)
            PDExceptionFunc.GenPDException("Term_without_BT", Term.getName());
        if (CanInsert(Term))    
            {
            if (PDLog.isDebug())
                {
                PDLog.Debug("TermId="+TermId+">>"+Term.getRecord());
                }
            try {
            Term.insert();
            } catch (Exception ex)
                {
                try {
                 Term.setName("_"+Term.getName());   
                 Term.insert();
                 ImportReport+="Renamed="+Term.getRecord()+"<br>";
                 } catch (Exception ex1)
                    {
                    PDExceptionFunc.GenPDException(ex1.getLocalizedMessage(), "="+Term.getRecord() );
                    }
                }
            TermExp.add(Term.getPDId());
            TermCache.put(TermId, null);
            StoredTerm=true;
            }
        }
    }
} while (StoredTerm && SomeNotNull);  
if (SomeNotNull)
    PDExceptionFunc.GenPDException("Circular_Reference_importing_Thesaurus", fPDID);
PDThesaur Term=new PDThesaur(getDrv());
HashSet HSLang=new HashSet(2);
if (PDLog.isDebug())
    PDLog.Debug("TermLang="+TermLang);
for (Iterator it = TermLang.iterator(); it.hasNext();)
    {
    StringTokenizer st = new StringTokenizer((String) it.next(), "|");    
    Term.Load(st.nextToken());
    HSLang.clear();
    HSLang.add(st.nextToken());
    Term.AddLang(HSLang);
    }
HashSet HSRT=new HashSet(2);
if (PDLog.isDebug())
    PDLog.Debug("TermRT="+TermRT);
for (Iterator it = TermRT.iterator(); it.hasNext();)
    {
    StringTokenizer st = new StringTokenizer((String) it.next(), "|");    
    Term.Load(st.nextToken());
    HSRT.clear();
    HSRT.add(st.nextToken());
    Term.AddRT(HSRT);
    }
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.StoreCache: <<");        
}
//---------------------------------------------------------------------
/**
 * Stores 2 terms as related during importation
 * @param pdId
 * @param pdId0 
 */
private void AddImportRT(String pdId, String pdId0)
{
if (pdId.compareTo(pdId0)>0)    
    TermRT.add(pdId+"|"+pdId0);
else
    TermRT.add(pdId0+"|"+pdId);
}
//---------------------------------------------------------------------
/**
 * Stores 2 terms as translation during importation
 * @param pdId
 * @param pdId0 
 */
private void AddImportLang(String pdId, String pdId0)
{
if (pdId.compareTo(pdId0)>0)    
    TermLang.add(pdId+"|"+pdId0);
else
    TermLang.add(pdId0+"|"+pdId);
}
//---------------------------------------------------------------------

/**
 * Returns a Term (a new one, from cache or Database)
 * @param SourceId Original Id of the Term
 * @return  
 * @throws PDException in any error
 */
private String ObtainSubThes(String ThesId, String TermLang) throws PDException
{
PDThesaur Term;    
String SourceId=ThesId+"_"+TermLang;
String NewId=(String)TermEquiv.get(SourceId); 
if (NewId==null)
    {
    Term=new PDThesaur(getDrv());
    NewId=ThesId+"_"+TermLang;
    Term.setPDId(NewId);
    Term.setName(SourceId);
    Term.setParentId(ThesId);
    Term.setLang(TermLang);
    StoreTerm(SourceId, Term);
    }
return (NewId);
}
//---------------------------------------------------------------------

static public String getImportReport()
{
return(ImportReport);
}
//---------------------------------------------------------------------
}
