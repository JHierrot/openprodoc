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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for managing the attributes in any point of OpenProdoc
 * An attribute contains name, type of value, description, an optional value (if it has been filled) and other parammeters
 * @author jhierrot
 */
public final class Attribute
{
/**
 * Technical/interal name of the Attribute
 */
private String Name;
/**
 * Public name of the Attribute
 */
private String UserName;
/**
 * Description and comments for the Attribute
 */
private String Description;
/**
 * Physical type 8String, Integer, Date,..)
 */
private int Type;
/**
 * Constant for Type Integer
 */
public static final int tINTEGER  =0;
/**
 * Constant for Type "float" (BigDecimal actually for portability)
 */
public static final int tFLOAT    =1;
/**
 * Constant for Type String
 */
public static final int tSTRING   =2;
/**
 * Constant for Type Date
 */
public static final int tDATE     =3;
/**
 * Constant for Type Boolean
 */
public static final int tBOOLEAN  =4;
/**
 * Constant for Type TimeStamp
 */
public static final int tTIMESTAMP=5;
/**
 * Constant for Type reference to a Thesaurus Term
 */
public static final int tTHES=6;
/**
 * Minimum value of types
 */
public static final int tMIN  =tINTEGER;
/**
 * Maximum value of types
 */
public static final int tMAX  =tTHES;
/**
 * When true, the Attribute must be filled before saving to any Object (Doc, Folder, User, etc.)
 */
private boolean Required = false;
/**
 * Value of the ttribute
 */
private Object Value = null;
/**
 * Max Length for String or number of Thesarus for tipe tTHES
 */
private int LongStr=0;
/**
 * When true the Attribute is part of the Primary Key
 */
private boolean PrimKey=false;
/**
 * When true the Attribute has unique value in the database
 */
private boolean Unique=false;
/**
 * When true the Attribute can be modified after inserted, otherwise becomes fixed
 */
private boolean ModifAllowed=true;
/**
 * When true the Attribute allows to add multiple values (Keywords, authors, etc)
 */
private boolean Multivalued=false;
/**
 * Constants for message error
 */
static final String Attribute_is_not_Multivalued="Attribute_is_not_Multivalued";
/**
 * Constants for message error
 */
static final String Incorrect_attribute_length="Incorrect_attribute_length";
/**
 * Separator when exporting and importing multiple values of an Attribute
 */
static public final String StringListSeparator="|";
/**
 * Set of values to assign to an Attribute.
 */
private TreeSet ValuesList=null;
/**
 * Default formater, used to store in DDBB, export, etc
 */
//final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/**
 * Default formater, used to store in DDBB, export, etc
 */
//final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
/**
 * Formatters for reading, importing, exporting and storing BigDecimal
 */
static public final String DDBB_DECIMALPATTERN="_0000000000.00;-#";
/**
 * Formatters for reading, importing, exporting and storing BigDecimal
 */
static public final String DECIMALPATTERN="0000000000.00;-#";

    /**
     *
     */
    static public final BigDecimal MINIMUMBD=new BigDecimal("-9999999999.99");

    /**
     *
     */
    static protected final String XML_NULL="[{NULL}]";
//--------------------------------------------------------------------------
/**
 * Default constructor of an Attribute for MONO or MULTI valued Attributes
 * @param pName        Internal/Technical name used for importing, exporting and table columns
 * @param pUserName    Name visible for user
 * @param pDescription Description, used also as toooltip in user interface
 * @param pType        Physical tipe of attribute. One of the internal types tSTRING, tDATE,..
 * @param pRequired    Required when creating or modifying any object
 * @param pValue       optional value
 * @param pLongStr     Max length for String, Id Thesaur for tTHES type
 * @param pPrimKey     Is primary key
 * @param pUnique      Is unique value
 * @param pModifAllowed Is Allowed to modify after interting element 
 * @param pMultivalued Allows to store many values
 * @throws PDException In any error
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
 * Constructor of an Attribute for MONO valued Attributes
 * @param pName        Internal/Technical name used for importing, exporting and table columns
 * @param pUserName    Name visible for user
 * @param pDescription Description, used also as toooltip in user interface
 * @param pType        Physical tipe of attribute. One of the internal types tSTRING, tDATE,..
 * @param pRequired    Required when creating or modifying any object
 * @param pValue       optional value
 * @param pLongStr     Max length for String, Id Thesaur for tTHES type
 * @param pPrimKey     Is primary key
 * @param pUnique      Is unique value
 * @param pModifAllowed Is Allowed to modify after interting element 
 * @throws PDException In any error
 */
public Attribute(String pName, String pUserName, String pDescription, int pType,
                boolean pRequired, Object pValue, int pLongStr,
                boolean pPrimKey, boolean pUnique, boolean pModifAllowed) throws PDException
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
 * Constructs an attribute from a XML node.
 * Used when importing from an .OPD file
 * @param OPDObject XML Node form org.w3c.dom
 * @throws PDException in any error in parsing oor values
 */
public Attribute(Node OPDObject) throws PDException
{
NodeList OPDObjectList = OPDObject.getChildNodes();
for (int i = 0; i < OPDObjectList.getLength(); i++)
    {
    Node AttrPart = OPDObjectList.item(i);        
    if (AttrPart.getNodeName().equals("Name"))
        setName(AttrPart.getTextContent());
    else if (AttrPart.getNodeName().equals("UserName"))
        setUserName(AttrPart.getTextContent());
    else if (AttrPart.getNodeName().equals("Descrip"))
        setDescription(AttrPart.getTextContent());
    else if (AttrPart.getNodeName().equals("Type"))
        setType(Integer.parseInt(AttrPart.getTextContent()));
    else if (AttrPart.getNodeName().equals("Req"))
        setRequired(AttrPart.getTextContent().equals("1"));
    else if (AttrPart.getNodeName().equals("LongStr"))
        setLongStr(Integer.parseInt(AttrPart.getTextContent()));
    else if (AttrPart.getNodeName().equals("Value"))
        Import(AttrPart.getTextContent());
    else if (AttrPart.getNodeName().equals("PrimKey"))
        setPrimKey(AttrPart.getTextContent().equals("1"));
    else if (AttrPart.getNodeName().equals("UniKey"))
        setUnique(AttrPart.getTextContent().equals("1"));
    else if (AttrPart.getNodeName().equals("ModAllow"))
        setModifAllowed(AttrPart.getTextContent().equals("1"));
    else if (AttrPart.getNodeName().equals("Multi"))
        setMultivalued(AttrPart.getTextContent().equals("1"));
    }
}
//--------------------------------------------------------------------------
/**
 * Creates a copy of the current Attribute. 
 * If the atrribute is multivalued, creates a new collection for the values
 * @return the a copy of the current Attribute or null if there is any error
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
 * Compares if the current Attribute is equals in all elements (including values an definition) to the Attribute parameter
 * @param Attr Attribute to compare with
 * @return True if Attributes are equal, False otherwise.
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
            {ex.printStackTrace();
            }    
    }
return (false);
}
//--------------------------------------------------------------------------
/**
 * Compares if the current Attribute is equals in DEFINITION to the Attribute parameter
 * @param Attr Attribute to compare definition with
 * @return True if Attributes are equal in definition, indepedently of values, False otherwise.
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
 * @throws PDException in any error
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
 * @throws PDException  inf the type is not a correct one
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
 * @param pValue the Value 
 * @throws prodoc.PDException if Attribute is multivalued
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
 * @throws PDException  if parameter is longer that current value
*/
public void setLongStr(int pLongStr) throws PDException
{
this.LongStr = pLongStr;
if (Type==tSTRING)
    {
    if (getLongStr()==0 || getLongStr()!=0 && getValue()!=null && ((String)Value).length()>getLongStr())
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
 * @throws PDException  in any error
*/
public void setPrimKey(boolean PrimKey) throws PDException
{
//if (PrimKey && isMultivalued())    
//   PDException.GenPDException("Attribute_is_Multivalued",getName());           
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
 * @throws PDException if Attribute is multivalued 
*/
public void setUnique(boolean Unique) throws PDException
{
if (Unique && isMultivalued())    
   PDException.GenPDException("Attribute_is_Multivalued",getName());           
this.Unique = Unique;
}
//--------------------------------------------------------------------------
/**
 * Overrides default toString method, returning 
 * @return "{"+getName()+"("+getType()+")"+"="+Export()+"}"
 */
@Override
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
 * Exports the Attribute value(s) in a fixed format so it can be included in any export process
 * and imported later (in example to/from an .opd file)
 * @return Formated value
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
            Tot.append(StringListSeparator);
        Object Val = it.next();
        Tot.append(FormatExport(Val));
        }
    return(Tot.toString());
    }
else
    {
    if (getValue()==null)
        return("");
    return(FormatExport(getValue()));
    }
}
//--------------------------------------------------------------------------
/**
 * Exports the Attribute to XML (for inclusion in .opd file or otherexport processes)
 * @return The Attribute in XML format
 */
public String ExportXML()
{
if (isMultivalued())    
    {
    if (ValuesList==null)
        return("");
    StringBuilder Tot=new StringBuilder();
    for (Iterator it = ValuesList.iterator(); it.hasNext();)
        {
        if (Tot.length()!=0)
            Tot.append(StringListSeparator);
        Object Val = it.next();
        Tot.append(FormatExportXML(Val));
        }
    return(Tot.toString());
    }
else
    {
    if (getValue()==null)
        return("");
    return(FormatExportXML(getValue()));
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
    return(DriverGeneric.DeCodif((String)Val));
else if (getType()==Attribute.tTHES)
    return((String)Val);
else if (getType()==Attribute.tINTEGER)
    return(""+Val);
else if (getType()==Attribute.tBOOLEAN)
    return(toBooleanString((Boolean)Val));
else if (getType()==Attribute.tDATE)
    {
    SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");    
    return(formatterDate.format((Date)Val));
    }
else if (getType()==Attribute.tTIMESTAMP)
    {
    SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
    return(formatterTS.format((Date)Val));
    }
else if (getType()==Attribute.tFLOAT)
    return(BD2String((BigDecimal)Val));
else
    return(Val.toString());            
}
//--------------------------------------------------------------------------
/**
 * Formats values for export
 * @param Val Object to be formated
 * @return String containing the formated object
 */
private String FormatExportXML(Object Val)
{
if (getType()==Attribute.tSTRING)
    return(((String)Val).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
else 
    return(FormatExport(Val));            
}
//--------------------------------------------------------------------------
/**
 * Parses a SINGLE String value depending of the Attribute type and returns the Value as object
 * @param Val String to be parsed
 * @return  A consructed object of the correct type
 * @throws PDException in any parsing error
 */
private Object FormatImport(String Val) throws PDException
{
if (getType()==Attribute.tSTRING)
    return(DriverGeneric.Codif(Val));
else if (getType()==Attribute.tTHES)
    return(Val);
else if (getType()==Attribute.tINTEGER)
    return(Integer.parseInt(Val));
else if (getType()==Attribute.tBOOLEAN)
    {
    if (Val.equals("1")||Val.substring(0,1).equalsIgnoreCase("S")||Val.substring(0,1).equalsIgnoreCase("Y")||Val.equalsIgnoreCase("ON"))
        return(true);
    else
        return(false);
    }
else if (getType()==Attribute.tDATE)
    {
    try {
    if (Val!=null && Val.length()!=0)
        {
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");    
        return(formatterDate.parse(Val));
        }
    } catch (ParseException ex)
        {
        PDException.GenPDException(ex.getLocalizedMessage(), Val);
        }
    }
else if (getType()==Attribute.tTIMESTAMP)
    {
    try {
    if (Val!=null && Val.length()!=0)
        {
        SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
        return(formatterTS.parse(Val));
        }
    } catch (ParseException ex)
        {
        PDException.GenPDException(ex.getLocalizedMessage(), Val);
        }
    }
else if (getType()==Attribute.tFLOAT)
    return(String2BD(Val));
return(null);
}
//--------------------------------------------------------------------------
/**
 * Parses a SINGLE String value dependng of the Attribute type and retuns the Value as object
 * The strings will come from an XML, so the can have escaped chars
 * @param Val String to be parsed
 * @return  A consructed object of the correct type
 * @throws PDException in any parsing error
 */
private Object FormatImportXML(String Val) throws PDException
{
if (Val.equalsIgnoreCase(XML_NULL))    
    return(null);
if (getType()==Attribute.tSTRING)
    return(Val.replace("&lt;", "<").replace("&gt;", ">" ).replace("&amp;", "&"));
else 
    return(FormatImport(Val));
}
//--------------------------------------------------------------------------
/**
 * Parses a MULTIPLE or SINGLE String value depending of the Attribute type and ASSIGNS the Value to the object
 * @param Val String to be parsed
 * @throws PDException in any parsing error
 */
public void Import(String Val) throws PDException
{
if (isMultivalued())    
    {
    ClearValues();
    if (Val==null)
        return;
    StringTokenizer St=new StringTokenizer(Val, StringListSeparator);
    while (St.hasMoreTokens())
        {
        AddValue(FormatImport(St.nextToken()));
        }
    }   
else
    {
    setValue(FormatImport(Val));    
    }
}
//-----------------------------------------------------------------------------------
/**
 * Parses a MULTIPLE or SINGLE String value depending of the Attribute type and ASSIGNS the Value to the object
 * The strings will come from an XML, so the can have escaped chars
 * @param Val String to be parsed
 * @throws PDException in any parsing error
 */
public void ImportXML(String Val) throws PDException
{
if (isMultivalued())    
    {
    ClearValues();
    StringTokenizer St=new StringTokenizer(Val, StringListSeparator);
    while (St.hasMoreTokens())
        {
        AddValue(FormatImportXML(St.nextToken()));
        }
    }   
else
    {
    setValue(FormatImportXML(Val));    
    }
}
//-----------------------------------------------------------------------------------
/**
 * Converts a Boolean value to String
 * @param Val Boolean 
 * @return "1" (true) or "0" (false)
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
 * Checks if the current values of the object fullfil the definition
 * @throws PDException if the values don't fullfil the Attribute definition
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
 * @throws PDException In any error
 */
public String toXML() throws PDException
{
StringBuilder S=new StringBuilder(200);
S.append("<"+ObjPD.XML_Attr+" Name=\"");
S.append(getName());
S.append("\">");
S.append(ExportXML());
S.append("</"+ObjPD.XML_Attr+">\n");
return(S.toString());
}
//--------------------------------------------------------------------------
/**
 * Converts all the definition of the attribute to XML
 * @return All the definition of the attribute, including value as ONE Tag
 * @throws PDException in any error
 */
public String toXMLt() throws PDException
{
StringBuilder S=new StringBuilder(200);
S.append("<"+ObjPD.XML_Attr+" Name=\"");
S.append(getName());
S.append("\" Type=\"");
S.append(getType());
S.append("\" Req=\"");
S.append(isRequired()?"1":"0");
S.append("\" LongStr=\"");
S.append(getLongStr());
S.append("\" Multi=\"");
S.append(isMultivalued()?"1":"0");
S.append("\" UniKey=\"");
S.append(isUnique()?"1":"0");
S.append("\" ModAllow=\"");
S.append(isModifAllowed()?"1":"0");
S.append("\">");
if (!isMultivalued() && getValue()==null)
    S.append(XML_NULL);
S.append(ExportXML());
S.append("</"+ObjPD.XML_Attr+">\n");
return(S.toString());
}
//--------------------------------------------------------------------------
/**
 * Converts all the definition of the attribute to XML
 * @return All the definition of the attribute, including value as MANY Tags
 * @throws PDException in any error
 */
public String toXMLFull() throws PDException
{
StringBuilder S=new StringBuilder(300);
S.append("<Attr><Name>");
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
S.append(isRequired()?"1":"0");
S.append("</Req>");
S.append("<LongStr>");
S.append(getLongStr());
S.append("</LongStr>");
S.append("<Value>");
S.append(ExportXML());
S.append("</Value>");
S.append("<PrimKey>");
S.append(isPrimKey()?"1":"0");
S.append("</PrimKey>");
S.append("<UniKey>");
S.append(isUnique()?"1":"0");
S.append("</UniKey>");
S.append("<ModAllow>");
S.append(isModifAllowed()?"1":"0");
S.append("</ModAllow>");
S.append("<Multi>");
S.append(isMultivalued()?"1":"0");
S.append("</Multi>");
S.append("</Attr>\n");
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
 * @throws PDException  In any error
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
 * @return true if it's not null and is correctly added
 * @throws PDException in ay error
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
 * @throws PDException in ay error
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
 * @throws PDException If it's not multivalued Attribute
 */
public void ClearValues() throws PDException
{
if (!isMultivalued())    
   PDException.GenPDException(Attribute_is_not_Multivalued,getName());        
if (ValuesList!=null)   
    ValuesList.clear();
}        
//--------------------------------------------------------------------------
/**
 * Returns the Value formated for CSV export
 * @return String with value formated
 */
public String ToCSV()
{
if (getValue()==null)
    return("");    
if (getType()==Attribute.tSTRING)    
    return("\""+FormatExport(getValue())+"\"");
else
    return(FormatExport(getValue()));
}
//--------------------------------------------------------------------------
/**
 * Converts a BigDecimal to a OPD formatted String
 * @param BD value t be converted
 * @return formated string
 */
public static String BD2String(BigDecimal BD)
{
DecimalFormat DF=new DecimalFormat(DECIMALPATTERN);
return(DF.format(BD));
}
//--------------------------------------------------------------------------
/**
 * Converts a BigDecimal to a OPD formatted String
 * @param BD value t be converted
 * @return formated string
 */
public static String BD2StringDDBB(BigDecimal BD)
{
DecimalFormat DF=new DecimalFormat(DDBB_DECIMALPATTERN);
if (BD.signum()!=-1)
    return(DF.format(BD));
else
    return(DF.format(MINIMUMBD.subtract(BD)));
}
//--------------------------------------------------------------------------
/**
 * Evaluates a string in any format with decimals to a DBigDecimal in OPD format
 * @param SBD String to e evaluated
 * @return a new BigDecimal
 */
public static BigDecimal String2BD(String SBD)
{
DecimalFormat DF=new DecimalFormat(DECIMALPATTERN);
return(new BigDecimal(DF.format(new BigDecimal(SBD.replace(',','.').replace("_", ""))).replace(',','.').replace("_", "")));
}
//--------------------------------------------------------------------------
/**
 * Evaluates a string in any format with decimals to a DBigDecimal in OPD format
 * @param SBD String to e evaluated
 * @return a new BigDecimal
 */
public static BigDecimal String2BDDDBB(String SBD)
{
DecimalFormat DF=new DecimalFormat(DDBB_DECIMALPATTERN);
BigDecimal BD=new BigDecimal(DF.format(new BigDecimal(SBD.replace(',','.').replace("_", ""))).replace(',','.').replace("_", ""));
if (BD.signum()==-1)
   return(MINIMUMBD.subtract(BD));
else
    return(BD);
}
//--------------------------------------------------------------------------
}
