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

import java.util.Iterator;

/**
 *
 * @author jhierrot
 */
public class PDMimeType extends ObjPD
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
public static final String fEXTENSION="Extension";

/**
 *
 */
static private Record MimeTypeStruct=null;
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
private String Extension;

static private ObjectsCache MTypeObjectsCache = null;

//-------------------------------------------------------------------------
/**
 * 
 * @param Drv
 * @throws PDException
 */
public PDMimeType(DriverGeneric Drv)  throws PDException
{
super(Drv);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Rec
 * @throws PDException
 */
public void assignValues(Record Rec) throws PDException
{
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
setExtension((String) Rec.getAttr(fEXTENSION).getValue());
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
Rec.getAttr(fEXTENSION).setValue(getExtension());
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
return ("PD_MIMETYPES");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
Record getRecordStruct() throws PDException
{
if (MimeTypeStruct==null)
    MimeTypeStruct=CreateRecordStruct();
return(MimeTypeStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (MimeTypeStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, "Name", "Standar_Mime_name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, "Description", "Description", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fEXTENSION, "Extensión", "Standard_Extension_of_file", Attribute.tSTRING, true, null, 32, false, false, true));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(MimeTypeStruct);
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
* @return the Name
*/
public String getName()
{
return Name;
}

/**
* @param Name the Name to set
*/
public void setName(String Name)
{
this.Name = Name;
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
* @return the Extension
*/
public String getExtension()
{
return Extension;
}

/**
* @param Extension the Extension to set
*/
public void setExtension(String Extension)
{
this.Extension = Extension;
}
//-----------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))  
    if (!getDrv().getUser().getRol().isAllowCreateMime() )
   PDException.GenPDException("MimeType_creation_not_allowed_to_user",getName());
}
//-----------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainMime() )
   PDException.GenPDException("MimeType_delete_not_allowed_to_user",getName());
}
//-----------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainMime() )
   PDException.GenPDException("MimeType_modification_not_allowed_to_user",getName());
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
if (MTypeObjectsCache==null)
    MTypeObjectsCache=new ObjectsCache("MType");
return(MTypeObjectsCache);    
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
public PDMimeType SolveExt(String Ext) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDMimeType.SolveExt>:"+Ext);
Record Rec = null;
for (Iterator Iter = getObjCache().getIter(); Iter.hasNext(); )
    {
    String Val=(String) Iter.next();    
    Rec = (Record)getObjCache().get(Val);
    if ( ((String)Rec.getAttr(fEXTENSION).getValue()).equalsIgnoreCase(Ext))
        {
        assignValues(Rec);
        if (PDLog.isDebug())
            PDLog.Debug("PDMimeType SolveExt <");
        return(this);
        }
    }
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fEXTENSION, Condition.cEQUAL, Ext));
Query LoadAct=new Query(getTabName(), getRecordStruct(),ListCond);
Cursor Cur=getDrv().OpenCursor(LoadAct);
Rec=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
getObjCache().put((String)Rec.getAttr(fNAME).getValue(), Rec);
assignValues(Rec);
if (PDLog.isDebug())
    PDLog.Debug("PDMimeType SolveExt <");
return(this);
}
//-------------------------------------------------------------------------
}
