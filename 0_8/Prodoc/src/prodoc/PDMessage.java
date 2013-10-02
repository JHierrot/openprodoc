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

import java.util.Date;

/**
 *
 * @author jhierrot
 */
public class PDMessage extends ObjPD
{
/**
 *
 */
public static final String fMSGID="MsgId";
/**
 *
 */
public static final String fSENDER="Sender";
/**
 *
 */
public static final String fDESTINATION="Destination";
/**
 *
 */
public static final String fTITLE="Title";
/**
 *
 */
public static final String fDESCRIPTION="Description";
/**
 *
 */
public static final String fMSGDATE="MsgDate";

/**
 *
 */
static private Record MessageStruct=null;
/**
 *
 */
private String MsgId;
/**
 *
 */
private String Sender;
/**
 *
 */
private String Destination;
/**
 *
 */
private String Title;
/**
 *
 */
private String Description;
/**
 *
 */
private Date   MsgDate;

static private ObjectsCache MesgObjectsCache = null;

//-------------------------------------------------------------------------
/**
 *
 * @param Drv
 */
public PDMessage(DriverGeneric Drv)
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
setMsgId((String) Rec.getAttr(fMSGID).getValue());
setSender((String) Rec.getAttr(fSENDER).getValue());
setDestination((String) Rec.getAttr(fDESTINATION).getValue());
setTitle((String) Rec.getAttr(fTITLE).getValue());
setDescription((String) Rec.getAttr(fDESCRIPTION).getValue());
setMsgDate((Date) Rec.getAttr(fMSGDATE).getValue());
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
Rec.getAttr(fMSGID).setValue(getMsgId());
Rec.getAttr(fSENDER).setValue(getSender());
Rec.getAttr(fDESTINATION).setValue(getDestination());
Rec.getAttr(fDESCRIPTION).setValue(getDescription());
Rec.getAttr(fTITLE).setValue(getTitle());
Rec.getAttr(fMSGDATE).setValue(getMsgDate());
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
ListCond.addCondition(new Condition(fMSGID, Condition.cEQUAL, getMsgId()));
return(ListCond);
}
//-------------------------------------------------------------------------
protected Conditions getConditionsLike(String Name) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fDESTINATION, Condition.cLIKE, VerifyWildCards(Name)));
return(ListCond);
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
public String getTabName()
{
return (getTableNameIn());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static public String getTableNameOut()
{
return ("PD_MSG_OUT");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static public String getTableNameIn()
{
return ("PD_MSG_IN");
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
Record getRecordStruct() throws PDException
{
if (MessageStruct==null)
    MessageStruct=CreateRecordStruct();
return(MessageStruct.Copy());
}
//-------------------------------------------------------------------------
/**
*
* @return
*/
static synchronized private Record CreateRecordStruct() throws PDException
{
if (MessageStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fMSGID, "Message_Id", "Unique_identifier_of_message", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fSENDER, "From", "From", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fDESTINATION, "To", "To", Attribute.tSTRING, true, null, 32, false, false, false));
    R.addAttr( new Attribute(fDESCRIPTION, "Message_text", "Message_text", Attribute.tSTRING, true, null, 1024, false, false, true));
    R.addAttr( new Attribute(fTITLE, "Subject", "Subject", Attribute.tSTRING, false, null, 128, false, false, true));
    R.addAttr( new Attribute(fMSGDATE, "Date", "Date", Attribute.tTIMESTAMP, true, null, 32, false, false, false));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(MessageStruct);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 */
protected void AsignKey(String Ident)
{
setDestination(Ident);
}
//-------------------------------------------------------------------------
/**
* @return the Name
*/
public String getDestination()
{
return Destination;
}
//-------------------------------------------------------------------------
/**
 * @param pDestination
*/
public void setDestination(String pDestination)
{
Destination = pDestination;
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
 * @param pDescription
 */
public void setDescription(String pDescription)
{
Description = pDescription;
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
// TODO: review VerifyAllowedDel in PDMessage
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
throw new PDException("Modificación Trazas no permitido al usuario");
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
}
//-------------------------------------------------------------------------
/**
* @return the ObjectType
*/
public String getSender()
{
return Sender;
}
//-------------------------------------------------------------------------
/**
* @param pSender the Sender to set
*/
public void setSender(String pSender)
{
Sender = pSender;
}

/**
* @return the MsgDate
*/
public Date getMsgDate()
{
return MsgDate;
}

/**
* @param MsgDate the MsgDate to set
*/
public void setMsgDate(Date MsgDate)
{
this.MsgDate = MsgDate;
}

/**
* @return the MsgId
*/
public String getMsgId()
{
return MsgId;
}

/**
* @param MsgId the MsgId to set
*/
public void setMsgId(String MsgId)
{
this.MsgId = MsgId;
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
* @param pTitle the Title to set
*/
public void setTitle(String pTitle)
{
Title = pTitle;
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
@Override
protected  void InstallMulti()  throws PDException
{
getDrv().CreateTable(getTableNameOut(),  getRecordStruct());
getDrv().AddIntegrity(getTableNameIn(), fDESTINATION, PDUser.getTableName(), PDUser.fNAME);
getDrv().AddIntegrity(getTableNameOut(), fSENDER, PDUser.getTableName(), PDUser.fNAME);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getDefaultOrder()
{
return("");
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (MesgObjectsCache==null)
    MesgObjectsCache=new ObjectsCache("Mesg");
return(MesgObjectsCache);    
}
//-------------------------------------------------------------------------
/**
 * Returns the value/field used as key of the object (Name) to ve used in Cache index
 * @return the value of the  Name field
 */
@Override
protected String getKey()
{
return(getMsgId());
}
//-------------------------------------------------------------------------
}
