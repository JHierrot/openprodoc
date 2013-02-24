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
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

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
public static final int tTHES=6;
/**
 *
 */
public static final int tMIN  =tINTEGER;
/**
 *
 */
public static final int tMAX  =tTHES;
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
 *
 */
private boolean Multivalued=false;

/**
 * 
 */
static final String Attribute_is_not_Multivalued="Attribute_is_not_Multivalued";
/**
 * 
 */
static final String Incorrect_attribute_length="Incorrect_attribute_length";
static final char ListSeparator='|';
static final String StringListSeparator="|";

/**
 * 
 */
private TreeSet ValuesList=null;
/**
 * Default formater, used to store in DDBB, export, etc
 */
final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/**
 * Default formater, used to store in DDBB, export, etc
 */
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
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
 * @param pMultivalued 
 * @throws PDException
 */
public Attribute(String pName, String pUserName, String pDescription, int pType,
                boolean pRequired, Object pValue, int pLongStr,
                boolean pPrimKey, boolean pUnique, boolean pModifAllowed, boolean pMultivalued)  throws PDException
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
setMultivalued(pMultivalued);
}
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
Attribute Attr=new Attribute(Name, UserName, Description, Type, Required, Value, LongStr, PrimKey, Unique, ModifAllowed, Multivalued);
if (Attr.isMultivalued() && ValuesList!=null)
    {
    Attr.ClearValues();
    for (Iterator it = ValuesList.iterator(); it.hasNext();)
        {
        Attr.AddValue(it.next());
        }
    }
return(Attr);
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
    && getLongStr()==Attr.getLongStr()
    && isPrimKey()==Attr.isPrimKey()
    && isUnique()==Attr.isUnique()
    && isModifAllowed()==Attr.isModifAllowed() 
    && isMultivalued()==Attr.isMultivalued() )
    {
    if (!isMultivalued())
        {
        if (getValue().toString().equals(Attr.getValue().toString()))
          return (true);    
        }
    else
        try {
            if (getValuesList().equals(Attr.getValuesList()))
                return (true);
        } catch (PDException ex)
            {
            }    
    }
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
    && isModifAllowed()==Attr.isModifAllowed() 
    && isMultivalued()==Attr.isMultivalued() )
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
if (isMultivalued())    
   PDException.GenPDException("Attribute_is_Multivalued",getName());        
if (Type==tSTRING)
    {
    if ( getLongStr()!=0 && pValue!=null && ((String)pValue).length()>getLongStr())
        {
        PDExceptionFunc.GenPDException(Incorrect_attribute_length,getName());
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
        PDExceptionFunc.GenPDException(Incorrect_attribute_length,getName());
        }
    }
}
//--------------------------------------------------------------------------
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
 * @throws PDException  
*/
public void setPrimKey(boolean PrimKey) throws PDException
{
if (PrimKey && isMultivalued())    
   PDException.GenPDException("Attribute_is_Multivalued",getName());           
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
 * @throws PDException  
*/
public void setUnique(boolean Unique) throws PDException
{
if (Unique && isMultivalued())    
   PDException.GenPDException("Attribute_is_Multivalued",getName());           
this.Unique = Unique;
}
//--------------------------------------------------------------------------
/**
 * 
 * @return
 */
public String toString()
{
if (getValue()==null)
    return("{"+getName()+"("+getType()+")"+"=Nulo}");
else
    return("{"+getName()+"("+getType()+")"+"="+Export()+"}");
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
 */
public String Export()
{
if (isMultivalued())    
    {
    if (ValuesList==null)
        return("");
    StringBuilder Tot=new StringBuilder();
    for (Iterator it = ValuesList.iterator(); it.hasNext();)
        {
        if (Tot.length()!=0)
            Tot.append(ListSeparator);
        Object Val = it.next();
        Tot.append(FormatExport(Val));
        }
//    if (PDLog.isDebug())
//        PDLog.Debug("Attribute.Export:"+getValue()+"-->"+Tot.toString());
    return(Tot.toString());
    }
else
    {
    if (getValue()==null)
        return("");
    if (PDLog.isDebug())
        PDLog.Debug("Attribute.Export:"+getValue()+"-->"+FormatExport(getValue()));
    return(FormatExport(getValue()));
    }
}
//--------------------------------------------------------------------------
/**
 * Formats values for export
 * @param Val Object to be formated
 * @return String containing the formated object
 */
private String FormatExport(Object Val)
{
if (getType()==Attribute.tSTRING)
    return((String)Val);
else if (getType()==Attribute.tTHES)
    return((String)Val);
else if (getType()==Attribute.tINTEGER)
    return(""+Val);
else if (getType()==Attribute.tBOOLEAN)
    return(toBooleanString((Boolean)Val));
else if (getType()==Attribute.tDATE)
    return(formatterDate.format((Date)Val));
else if (getType()==Attribute.tTIMESTAMP)
    return(formatterTS.format((Date)Val));
else
    return(Val.toString());            
}
//--------------------------------------------------------------------------
private Object FormatImport(String Val) throws PDException
{
if (getType()==Attribute.tSTRING)
    return(Val);
else if (getType()==Attribute.tTHES)
    return(Val);
else if (getType()==Attribute.tINTEGER)
    return(new Integer(Integer.parseInt(Val)));
else if (getType()==Attribute.tBOOLEAN)
    {
    if (Val.equals("1"))
        return(true);
    else
        return(false);
    }
else if (getType()==Attribute.tDATE)
    {
    try {
    if (Val!=null && Val.length()!=0)
        return(formatterDate.parse(Val));
    } catch (ParseException ex)
        {
        PDException.GenPDException(ex.getLocalizedMessage(), Val);
        }
    }
else if (getType()==Attribute.tTIMESTAMP)
    {
    try {
    if (Val!=null && Val.length()!=0)
        return(formatterTS.parse(Val));
    } catch (ParseException ex)
        {
        PDException.GenPDException(ex.getLocalizedMessage(), Val);
        }
    }
return(null);
}
//--------------------------------------------------------------------------
/**
 *
 * @param Val
 * @throws PDException
 */
public void Import(String Val) throws PDException
{
if (isMultivalued())    
    {
    StringTokenizer St=new StringTokenizer(Val, StringListSeparator);
    while (St.hasMoreTokens())
        {
        AddValue(FormatImport(St.nextToken()));
        }
//    if (PDLog.isDebug())
//        PDLog.Debug("Attribute.Import:"+Val+"-->"+Export());
    }   
else
    {
//    if (PDLog.isDebug())
//        PDLog.Debug("Attribute.Import:"+Val+"-->"+FormatImport(Val));
    setValue(FormatImport(Val));    
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
if (isMultivalued())  
    {
    if ( isRequired() && getValuesList().isEmpty())
        {
        PDExceptionFunc.GenPDException("A_value_is_required",getUserName());
        }
    if ( isRequired() && getType()==Attribute.tSTRING)
        {
        for (Iterator it = getValuesList().iterator(); it.hasNext();)
            {
            String Val = (String)it.next();
            if (getLongStr()<Val.length())    
                PDExceptionFunc.GenPDException(Incorrect_attribute_length,getUserName());
            }
        }
    }
else
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
        PDExceptionFunc.GenPDException(Incorrect_attribute_length,getUserName());
        }
    }
}
//--------------------------------------------------------------------------
/**
 * Converts name and value of the attribute to XML
 * @return the XML with the elements Name and value.
 * @throws PDException
 */
public String toXML() throws PDException
{
StringBuilder S=new StringBuilder(300);
S.append("<"+ObjPD.XML_Attr+" Name=\"");
S.append(getName());
S.append("\">");
S.append(Export());
S.append("</"+ObjPD.XML_Attr+">\n");
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
S.append("<Multi>");
S.append(isMultivalued());
S.append("</Multi>");
S.append("</attr>\n");
return(S.toString());
}
//--------------------------------------------------------------------------
/**
* @return the Multivalued
*/
public boolean isMultivalued()
{
return Multivalued;
}
//--------------------------------------------------------------------------
/**
* @param Multivalued the Multivalued to set
*/
public void setMultivalued(boolean Multivalued)
{
this.Multivalued = Multivalued;
}
//--------------------------------------------------------------------------
/**
 * @return the ValuesList
 * @throws PDException  
*/
public TreeSet getValuesList() throws PDException
{
if (!isMultivalued())    
   PDException.GenPDException(Attribute_is_not_Multivalued,getName());    
if (ValuesList==null)   
    ValuesList=new TreeSet();
return ValuesList;
}
//--------------------------------------------------------------------------
/**
 * Add a value to the set of values
 * @param pValue new value to add
 * @return true if it's not null correctly added
 * @throws PDException
 */
public boolean AddValue(Object pValue) throws PDException
{
if (!isMultivalued())    
   PDException.GenPDException(Attribute_is_not_Multivalued,getName());        
if (Type==tSTRING)
    {
    if ( getLongStr()!=0 && pValue!=null && ((String)pValue).length()>getLongStr())
        {
        PDExceptionFunc.GenPDException(Incorrect_attribute_length,getName());
        }
    if (pValue!=null)
        return(getValuesList().add(pValue.toString().trim()));
    return(false);
    }
else
    {
    if (pValue==null)    
        return(false);        
    return(getValuesList().add(pValue));
    }
}        
//--------------------------------------------------------------------------
/**
 * Remove a value from the set of values
 * @param pValue to remove
 * @return true if the value was in the set and is removed
 * @throws PDException
 */
public boolean RemoveValue(Object pValue) throws PDException
{
if (!isMultivalued())    
   PDException.GenPDException(Attribute_is_not_Multivalued,getName());        
if (ValuesList!=null)   
    return( ValuesList.remove(pValue));    
return(false);
}        
//--------------------------------------------------------------------------
/**
 * Clear of the values in multivalued attributes
 * @throws PDException
 */
public void ClearValues() throws PDException
{
if (!isMultivalued())    
   PDException.GenPDException(Attribute_is_not_Multivalued,getName());        
if (ValuesList!=null)   
    ValuesList.clear();
}        
//--------------------------------------------------------------------------
}
