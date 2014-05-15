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

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jhierrot
 */
public class Condition
{
/**
 *
 */
private int cType=0;
// a standar comparation with attibute, comparator and value
/**
 *
 */
static final int ctNORMAL     =1;
/**
 *
 */
static final int ctBETWEEN    =2;
/**
 *
 */
static final int ctIN         =3;
/**
 *
 */
static final int cEQUALFIELDS =4;

static final int cATTROPER =5;
/**
 *
 */
private String Field=null;
/**
 *
 */
private int Comparation=cEQUAL;
/**
 *
 */
public static final int cEQUAL   =0;
/**
 *
 */
public static final int cGT      =1;
/**
 *
 */
public static final int cLT      =2;
/**
 *
 */
public static final int cGET     =3;
/**
 *
 */
public static final int cLET     =4;
/**
 *
 */
public static final int cNE      =5;
/**
 *
 */
public static final int cLIKE     =8;
/**
 *
 */
public static final int cINList   =6;
/**
 * TODO: implement "selec in" in driverJDBC
 */
public static final int cINQuery =7;

/**
 *
 */
private Object Value=null;
/**
 *
 */
private boolean Invert=false;

final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyyMMddHHmmss");
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyyMMdd");


private int TypeVal=-1;
//-------------------------------------------------------------------------
/**
 *
 * @param Attr
 */
public Condition(Attribute Attr)
{
cType=ctNORMAL;
Field=Attr.getName();
Value=Attr.getValue();
TypeVal=Attr.getType();
}
//-------------------------------------------------------------------------
/**
 *
 * @param Attr
 */
public Condition(Attribute Attr, int pComparation)
{
cType=ctNORMAL;
Field=Attr.getName();
Value=Attr.getValue();
Comparation=pComparation;
TypeVal=Attr.getType();
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param pComparation
 * @param pValue
 * @throws PDException
 */
public Condition(String pField, int pComparation, Object pValue) throws PDException
{
if (pValue==null || (pValue instanceof String && ((String)pValue).length()==0 ))
    {
    PDExceptionFunc.GenPDException("null_value_of_condition",pField);
    }
cType=ctNORMAL;
Field=pField;
Comparation=pComparation;
Value=pValue;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param pField2
 * @throws PDException
 */
public Condition(String pField, String pField2) throws PDException
{
cType=cEQUALFIELDS;
Field=pField;
Comparation=cEQUAL;
Value=pField2;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param ListVal
 * @throws PDException
 */
public Condition(String pField, HashSet ListVal) throws PDException
{
if (ListVal==null || ListVal.size()==0)
    {
    PDExceptionFunc.GenPDException("null_value_of_condition",pField);
    }
cType=ctIN;
Field=pField;
Comparation=cINList;
Value=ListVal;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param Search
 * @throws PDException
 */
public Condition(String pField, Query Search) throws PDException
{
if (Search==null)
    {
    PDExceptionFunc.GenPDException("null_value_of_condition",pField);
    }
cType=ctIN;
Field=pField;
Comparation=cINQuery;
Value=Search;
}
//-------------------------------------------------------------------------
/**
* @return the cType
*/
public int getcType()
{
return cType;
}
//-------------------------------------------------------------------------
/**
* @return the Field
*/
public String getField()
{
return Field;
}
//-------------------------------------------------------------------------
/**
* @return the Value
*/
public Object getValue()
{
return Value;
}
//-------------------------------------------------------------------------
/**
* @return the Comparation
*/
public int getComparation()
{
return Comparation;
}
//-------------------------------------------------------------------------
/**
* @return the Invert
*/
public boolean isInvert()
{
return Invert;
}
//-------------------------------------------------------------------------
/**
 * @return the TypeVal
 */
public int getTypeVal()
{
return TypeVal;
}
//-------------------------------------------------------------------------
/**
 * Converts the Conditions in XML so can be reconstructed in remote
 * @return String with the XML
 * @throws prodoc.PDException in any error
 */
String toXML()
{
StringBuilder XML=new StringBuilder("<Cond>");
XML.append("cType>").append(cType).append("</cType>");
XML.append("<Field>").append(Field).append("</Field>");
switch (cType)
    {case ctNORMAL:
        XML.append("<TypeVal>").append(TypeVal).append("</TypeVal>");
        XML.append("<Comp>").append(Comparation).append("</Comp>");
        XML.append("<Val>");
        if (TypeVal==Attribute.tSTRING)
            XML.append(((String)Value).replace('<', '^')); // to avoid false XML tags
        else if (TypeVal==Attribute.tTIMESTAMP)
            XML.append(formatterTS.format((Date)Value));
        else if (TypeVal==Attribute.tDATE)
            XML.append(formatterDate.format((Date)Value));
        else if (TypeVal==Attribute.tBOOLEAN)
            XML.append(((Boolean)Value)?"1":"0");
        else
            XML.append(Value);
        XML.append("</Val>");
        break;
     case ctIN:
        XML.append("<Val>|");
        HashSet List=(HashSet)Value;
        Object l[]=List.toArray();
        for (Object object : l)
            {
            if (object instanceof String)
                XML.append(((String)object).replace('<', '^')).append("|"); // to avoid false XML tags
            else if (object instanceof Date)
                XML.append(formatterDate.format((Date)object)).append("|");
            else if (object instanceof Boolean)
                XML.append(((Boolean)object)?"1|":"0|");
            else
                XML.append(object).append("|");
            }
        XML.append("</Val>");
        break;
     case cEQUALFIELDS:
        XML.append("<Field2>").append(Value).append("</Field2>");
        break;
    }
XML.append(isInvert()?"<Inv>1</Inv>":"<Inv>0</Inv>");
XML.append("</Cond>");
return (XML.toString());
}
//-------------------------------------------------------------------------
/**
 * Buils a Condition object from XML
 * @param XML with a condition
 */
public Condition(String XML) throws Exception
{
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(new ByteArrayInputStream(XML.getBytes("UTF-8")));
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Field");
Node OPDObject = OPDObjectList.item(0);
Field=OPDObject.getTextContent(); 
OPDObjectList = XMLObjects.getElementsByTagName("P");
OPDObject = OPDObjectList.item(0);
String Pass=OPDObject.getTextContent(); 

}
//-------------------------------------------------------------------------

}
