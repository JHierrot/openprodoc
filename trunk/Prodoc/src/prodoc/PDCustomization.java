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

/**
 *
 * @author jhierrot
 */
public class PDCustomization extends ObjPD
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
public static final String fLANGUAGE="Language";
/**
 *
 */
public static final String fTIMEFORMAT="TimeFormat";
/**
 *
 */
public static final String fDATEFORMAT="DateFormat";
/**
 *
 */
public static final String fSTYLE="Style";
/**
 * Contains the secuence of font's formas used in Swing as groups of tree element without spaces
 * getFontMenu() -> new java.awt.Font("Arial", 0, 12);
 * getFontTree()
 * getFontList()
 * getFontDialog()
 * Example: "Arial", 0, 12, "Arial", 0, 12, "Arial", 0, 12, "Arial", 0, 12
 */
public static final String fSWINGSTYLE="SwingStyle";

/**
 *
 */
static private Record CustomStruct=null;
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
private String Language;
/**
 *
 */
private String TimeFormat;
/**
 *
 */
private String DateFormat;
/**
 *
 */
private String Style;
/**
 *
 */
private String SwingStyle;

static private ObjectsCache CustomObjectsCache = null;

//-------------------------------------------------------------------------
/**
 * 
 * @param Drv
 * @throws PDException
 */
public PDCustomization(DriverGeneric Drv)  throws PDException
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
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
setLanguage((String) Rec.getAttr(fLANGUAGE).getValue());
setTimeFormat((String) Rec.getAttr(fTIMEFORMAT).getValue());
setDateFormat((String) Rec.getAttr(fDATEFORMAT).getValue());
setStyle((String) Rec.getAttr(fSTYLE).getValue());
setSwingStyle((String) Rec.getAttr(fSWINGSTYLE).getValue());
assignCommonValues(Rec);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
@Override
public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fLANGUAGE).setValue(getLanguage());
Rec.getAttr(fTIMEFORMAT).setValue(getTimeFormat());
Rec.getAttr(fDATEFORMAT).setValue(getDateFormat());
Rec.getAttr(fSTYLE).setValue(getStyle());
Rec.getAttr(fSWINGSTYLE).setValue(getSwingStyle());
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
return ("PD_CUSTOMIZE");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
@Override
Record getRecordStruct() throws PDException
{
if (CustomStruct==null)
    CustomStruct=CreateRecordStruct();
return(CustomStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (CustomStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fNAME, fNAME, "Customization_name", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, fDESCRIPTION, "Customization_description", Attribute.tSTRING, true, null, 128, false, false, true));
    R.addAttr( new Attribute(fLANGUAGE, "Language", "ISO_639_1_Code_2_characters_of_Language", Attribute.tSTRING, false, null, 6, false, false, true));
    R.addAttr( new Attribute(fDATEFORMAT, "Date_format", "Date_format_used_by_java_formatter", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fTIMEFORMAT, "TimeStamp_format", "TimeStamp_format_used_by_java_formatter", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fSTYLE, "Path", "Relative_path_in_web_application_of_images_and_elements", Attribute.tSTRING, false, null, 32, false, false, false));
    R.addAttr( new Attribute(fSWINGSTYLE, "Style", "Thick_client_Style", Attribute.tSTRING, false, null, 255, false, false, false));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(CustomStruct);
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
* @return the Extension
*/
public String getLanguage()
{
return Language;
}
//-------------------------------------------------------------------------
/**
* @param pLanguage the Extension to set
*/
public void setLanguage(String pLanguage)
{
Language = pLanguage;
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getName().equals("Install"))     
    if (!getDrv().getUser().getRol().isAllowCreateCustom() )
       PDExceptionFunc.GenPDException("Customization_creation_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainCustom() )
   PDExceptionFunc.GenPDException("Customization_delete_not_allowed_to_user",getName());
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainCustom() )
   PDExceptionFunc.GenPDException("Customization_modification_not_allowed_to_user",getName());
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
* @return the TimeFormat
*/
public String getTimeFormat()
{
return TimeFormat;
}
//-------------------------------------------------------------------------
/**
 * @param TimeFormat the TimeFormat to set
 */
public void setTimeFormat(String TimeFormat)
{
this.TimeFormat = TimeFormat;
}
//-------------------------------------------------------------------------
/**
 * @return the Style
 */
public String getStyle()
{
return Style;
}
//-------------------------------------------------------------------------
/**
 * @param Style the Style to set
 */
public void setStyle(String Style)
{
this.Style = Style;
}
//-------------------------------------------------------------------------
/**
 * @return the DateFormat
 */
public String getDateFormat()
{
return DateFormat;
}
//-------------------------------------------------------------------------
/**
 * @param DateFormat the DateFormat to set
 */
public void setDateFormat(String DateFormat)
{
this.DateFormat = DateFormat;
}
//-------------------------------------------------------------------------
/**
 * @return the SwingStyle
 */
public String getSwingStyle()
{
return SwingStyle;
}
//-------------------------------------------------------------------------
/**
 * @param SwingStyle the SwingStyle to set
 */
public void setSwingStyle(String SwingStyle)
{
this.SwingStyle = SwingStyle;
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (CustomObjectsCache==null)
    CustomObjectsCache=new ObjectsCache("Custom");
return(CustomObjectsCache);    
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
}
