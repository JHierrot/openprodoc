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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jhierrot
 */
public class Attribute
{
/**
 *
 */
private String Name;
/**
 *
 */
private String UserName;
/**
 *
 */
private String Description;
/**
 *
 */
private int Type;
/**
 *
 */
public static final int tINTEGER  =0;
/**
 *
 */
public static final int tFLOAT    =1;
/**
 *
 */
public static final int tSTRING   =2;
/**
 *
 */
public static final int tDATE     =3;
/**
 *
 */
public static final int tBOOLEAN  =4;
/**
 *
 */
public static final int tTIMESTAMP=5;
/**
 *
 */
public static final int tMIN  =tINTEGER;
/**
 *
 */
public static final int tMAX  =tTIMESTAMP;
/**
 *
 */
private boolean Required = false;
/**
 *
 */
private Object Value = null;
/**
 *
 */
private int LongStr=0;
/**
 *
 */
private boolean PrimKey=false;
/**
 *
 */
private boolean Unique=false;
/**
 *
 */
private boolean ModifAllowed=true;

/**
 * Default formater, used to store in DDBB, export, etc
 */
static final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/**
 * Default formater, used to store in DDBB, export, etc
 */
static final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
//--------------------------------------------------------------------------
/**
 *
 * @param pName
 * @param pUserName
 * @param pDescription
 * @param pType
 * @param pRequired
 * @param pValue
 * @param pLongStr
 * @param pPrimKey
 * @param pUnique 
 * @param pModifAllowed
 * @throws PDException
 */
public Attribute(String pName, String pUserName, String pDescription, int pType,
                boolean pRequired, Object pValue, int pLongStr,
                boolean pPrimKey, boolean pUnique, boolean pModifAllowed)  throws PDException
{
setName(pName);
setUserName(pUserName);
setDescription(pDescription);
setType(pType);
setRequired(pRequired);
setLongStr(pLongStr);
setValue(pValue);
setPrimKey(pPrimKey);
setUnique(pUnique);
setModifAllowed(pModifAllowed);
}
//--------------------------------------------------------------------------
/**
 *
 * @return
 */
public Attribute Copy()
{
try {
return new Attribute(Name, UserName, Description, Type, Required, Value, LongStr, PrimKey, Unique, ModifAllowed);
} catch (PDException ex)
    {
    PDLog.Error("Error_copying_Attribute"+":"+ex.getLocalizedMessage());
    return(null);
    }
}
//--------------------------------------------------------------------------
/**
 *
 * @param Attr
 * @return
 */
public boolean equals(Attribute Attr)
{
if (getName().equals(Attr.getName())
    && getUserName().equals(Attr.getUserName())
    && getDescription().equals(Attr.getDescription())
    && getType()==Attr.getType()
    && isRequired()==Attr.isRequired()
    && getValue().toString().equals(Attr.getValue().toString())
    && getLongStr()==Attr.getLongStr()
    && isPrimKey()==Attr.isPrimKey()
    && isUnique()==Attr.isUnique()
    && isModifAllowed()==Attr.isModifAllowed() )
    return (true);
else
    return (false);
}
//--------------------------------------------------------------------------
/**
 *
 * @param Attr
 * @return
 */
public boolean equalDef(Attribute Attr)
{
if (getName().equals(Attr.getName())
    && getUserName().equals(Attr.getUserName())
    && getDescription().equals(Attr.getDescription())
    && getType()==Attr.getType()
    && isRequired()==Attr.isRequired()
    && getLongStr()==Attr.getLongStr()
    && isPrimKey()==Attr.isPrimKey()
    && isUnique()==Attr.isUnique()
    && isModifAllowed()==Attr.isModifAllowed() )
    return (true);
else
    return (false);
}
//--------------------------------------------------------------------------
/**
* @return the Name
*/
public String getName()
{
return Name;
}
//--------------------------------------------------------------------------
/**
 * @param pName the Name to set
 * @throws PDException
*/
public void setName(String pName) throws PDException
{
if (pName==null || pName.length()<=0)
    {
    PDExceptionFunc.GenPDException("Incorrect_attribute_name", null);
    }
Name = pName;
}
//--------------------------------------------------------------------------
/**
* @return the Description
*/
public String getDescription()
{
return Description;
}
//--------------------------------------------------------------------------
/**
* @param pDescription the Description to set
*/
public void setDescription(String pDescription)
{
Description = pDescription;
}
//--------------------------------------------------------------------------
/**
* @return the Type
*/
public int getType()
{
return Type;
}
//--------------------------------------------------------------------------
/**
 * @param pType the Type to set
 * @throws PDException
*/
public void setType(int pType)  throws PDException
{
if (pType<tMIN ||  pType>tMAX)
    {
    PDException.GenPDException("Incorrect_attribute_type", ""+pType);
    }
else
    Type = pType;
}
//--------------------------------------------------------------------------
/**
* @return the Required
*/
public boolean isRequired()
{
return Required;
}
//--------------------------------------------------------------------------
/**
* @param pRequired the Required to set
*/
public void setRequired(boolean pRequired)
{
Required = pRequired;
}
//--------------------------------------------------------------------------
/**
* @return the Value
*/
public Object getValue()
{
return Value;
}
//--------------------------------------------------------------------------
/**
 * @param pValue the Value to set
 * @throws PDException
*/
public void setValue(Object pValue) throws PDException
{
if (Type==tSTRING)
    {
    if ( getLongStr()!=0 && pValue!=null && ((String)pValue).length()>getLongStr())
        {
        PDExceptionFunc.GenPDException("Incorrect_attribute_length",getName());
        }
    if (pValue!=null)
        this.Value = pValue.toString().trim();
    else
        Value=null;
    }
else
    this.Value = pValue;
}
//--------------------------------------------------------------------------
/**
* @return the LongStr
*/
public int getLongStr()
{
return LongStr;
}
//--------------------------------------------------------------------------
/**
 * @param pLongStr the LongStr to set
 * @throws PDException
*/
public void setLongStr(int pLongStr) throws PDException
{
this.LongStr = pLongStr;
if (Type==tSTRING)
    {
    if (getLongStr()!=0 && getValue()!=null && ((String)Value).length()>getLongStr())
        {
        PDExceptionFunc.GenPDException("Incorrect_attribute_length",getName());
        }
    }
}
/**
* @return the PrimKey
*/
public boolean isPrimKey()
{
return PrimKey;
}
//--------------------------------------------------------------------------
/**
* @param PrimKey the PrimKey to set
*/
public void setPrimKey(boolean PrimKey)
{
this.PrimKey = PrimKey;
}
//--------------------------------------------------------------------------
/**
* @return the Unique
*/
public boolean isUnique()
{
return Unique;
}
//--------------------------------------------------------------------------
/**
* @param Unique the Unique to set
*/
public void setUnique(boolean Unique)
{
this.Unique = Unique;
}
//--------------------------------------------------------------------------
public String toString()
{
if (getValue()==null)
    return("{"+getName()+"("+getType()+")"+"=Nulo}");
else
    return("{"+getName()+"("+getType()+")"+"="+getValue()+"("+getValue().getClass().getName()+")}");
}
//--------------------------------------------------------------------------
/**
* @return the ModifAllowed
*/
public boolean isModifAllowed()
{
return ModifAllowed;
}
//--------------------------------------------------------------------------
/**
* @param pModifAllowed the ModifAllowed to set
*/
public void setModifAllowed(boolean pModifAllowed)
{
ModifAllowed = pModifAllowed;
}
//--------------------------------------------------------------------------
/**
* @return the UserName
*/
public String getUserName()
{
return UserName;
}
//--------------------------------------------------------------------------
/**
* @param pUserName the UserName to set
*/
public void setUserName(String pUserName)
{
UserName = pUserName;
}
//--------------------------------------------------------------------------
/**
 *
 * @return
 * @throws PDException
 */
public String Export() throws PDException
{
if (getValue()==null)
    return("");
if (getType()==Attribute.tSTRING)
    return((String)getValue());
else if (getType()==Attribute.tINTEGER)
    return(""+getValue());
else if (getType()==Attribute.tBOOLEAN)
    return(toBooleanString((Boolean)getValue()));
else if (getType()==Attribute.tDATE)
    return(formatterDate.format((Date)getValue()));
else if (getType()==Attribute.tTIMESTAMP)
    return(formatterTS.format((Date)getValue()));
else
    return(getValue().toString());
}
//--------------------------------------------------------------------------
/**
 *
 * @param Val
 * @throws PDException
 */
public void Import(String Val) throws PDException
{
if (getType()==Attribute.tSTRING)
    setValue(Val);
else if (getType()==Attribute.tINTEGER)
    setValue(new Integer(Integer.parseInt(Val)));
else if (getType()==Attribute.tBOOLEAN)
    {
    if (Val.equals("1"))
        setValue(true);
    else
        setValue(false);
    }
else if (getType()==Attribute.tDATE)
    {
    try {
        setValue(formatterDate.parse(Val));
    } catch (ParseException ex)
        {
        PDException.GenPDException(ex.getLocalizedMessage(), Val);
        }
    }
else if (getType()==Attribute.tTIMESTAMP)
    {
    try {
    setValue(formatterTS.parse(Val));
    } catch (ParseException ex)
        {
        PDException.GenPDException(ex.getLocalizedMessage(), Val);
        }
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Val
 * @return
 */
protected String toBooleanString(Boolean Val)
{
if (Val.booleanValue())
    return("1");
else
    return("0");
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
public void Verify() throws PDException
{
if ( isRequired() && getValue()==null)
    {
    PDExceptionFunc.GenPDException("A_value_is_required",getUserName());
    }
if ( isRequired() && getType()==Attribute.tSTRING && ((String)getValue()).length()==0)
    {
    PDExceptionFunc.GenPDException("A_value_is_required",getUserName());
    }
if (getValue()!=null && getType()==Attribute.tSTRING && getLongStr()<((String)getValue()).length())
    {
    PDExceptionFunc.GenPDException("Too_long",getUserName());
    }
}
//--------------------------------------------------------------------------
/**
 * Converts all the attributes of the record to XML
 * @return the XML with the elements.
 * @throws PDException
 */
public String toXML() throws PDException
{
StringBuilder S=new StringBuilder(300);
S.append("<"+ObjPD.XML_Attr+" Name=\"");
S.append(getName());
S.append("\">");
S.append(Export());
S.append("</"+ObjPD.XML_Attr+">");
return(S.toString());
}
//--------------------------------------------------------------------------
/**
 * Converts all the attributes of the record to XML
 * @return the XML with the elements.
 * @throws PDException
 */
public String toXMLFull() throws PDException
{
StringBuilder S=new StringBuilder(300);
S.append("<attr><Name>");
S.append(getName());
S.append("</Name>");
S.append("<UserName>");
S.append(getUserName());
S.append("</UserName>");
S.append("<Descrip>");
S.append(getDescription());
S.append("</Descrip>");
S.append("<Type>");
S.append(getType());
S.append("</Type>");
S.append("<Req>");
S.append(isRequired());
S.append("</Req>");
S.append("<LongStr>");
S.append(getLongStr());
S.append("</LongStr>");
S.append("<Value>");
S.append(Export());
S.append("</Value>");
S.append("<PrimKey>");
S.append(isPrimKey());
S.append("</PrimKey>");
S.append("<UniKey>");
S.append(isUnique());
S.append("</UniKey>");
S.append("<ModAllow>");
S.append(isModifAllowed());
S.append("</ModAllow>");
S.append("</attr>");
return(S.toString());
}
//--------------------------------------------------------------------------
}
