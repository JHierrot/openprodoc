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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.StringTokenizer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static prodoc.Attribute.StringListSeparator;
import static prodoc.PDFolders.fGRANTPARENTID;
import static prodoc.PDFolders.getRecordStructPDFolderLev;

/**
 * Class for managing simple search conditions in OpenProdoc
 * @author jhierrot
 */
public class Condition
{
/**
 * Kind of condition
 */
private int cType=0;
/**
 * a standard comparation with attibute, comparator and value
 */
static final int ctNORMAL     =1;
/**
 * A range condition
 */
static final int ctBETWEEN    =2;
/**
 * Field in a list of values
 */
static final int ctIN         =3;
/**
 * Two fields equal
 */
static final int cEQUALFIELDS =4;
/**
 * Field to compare
 */
private String Field=null;
/**
 * Kind of comparation
 */
private int Comparation=cEQUAL;
/**
 * Constant Comparation: Equal =
 */
public static final int cEQUAL   =0;
/**
 * Constant Comparation: Greater Than 
 */
public static final int cGT      =1;
/**
 * Constant Comparation: Lest Than 
 */
public static final int cLT      =2;
/**
 * Constant Comparation: Greater or Equal Than 
 */
public static final int cGET     =3;
/**
 * Constant Comparation: Lest or Equal Than 
 */
public static final int cLET     =4;
/**
 * Constant Comparation: Distinct 
 */
public static final int cNE      =5;
/**
 * Constant Comparation: Like/Contains %
 */
public static final int cLIKE     =8;
/**
 * Constant: condition In list of values
 */
public static final int cINList   =6;
/**
 * Constant: condition In list of results of a Query
 */
public static final int cINQuery =7;
/**
 * Valur to compare
 */
private Object Value=null;
/**
 * Condition inverted when true (NOT (Condition) )
 */
private boolean Invert=false;
/**
 * Formatter for searching (an storing) timestamp
 */
final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyyMMddHHmmss");
/**
 * Formatter for searching (an storing) Date
 */
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyyMMdd");
/**
 * Kind of Object value
 */
private int TypeVal=-1;

public final static String CONTAINS="CONTAINS";

public final static String INTREE="IN_TREE";

public final static String INFOLDER="IN_FOLDER";

//-------------------------------------------------------------------------
/**
 * Constructor that creates a condition where name_of_attribute = "value of attribute"
 * @param Attr Attribute {@link prodoc.Attribute} to use for comparation
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
 * Constructor that creates a condition where name_of_attribute COMPARATION value_of_attribute
 * @param Attr Attribute {@link prodoc.Attribute} to use for comparation
 * @param pComparation Kind of comparation
 * cEQUAL   =0;
 * cGT      =1;
 * cLT      =2;
 * cGET     =3;
 * cLET     =4;
 * cNE      =5;
 * cLIKE    =8;
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
 * Constructor that creates a condition where pFied COMPARATION pValue
 * @param pField name of field to compare
 * @param pComparation Kind of comparation
 * cEQUAL   =0;
 * cGT      =1;
 * cLT      =2;
 * cGET     =3;
 * cLET     =4;
 * cNE      =5;
 * cLIKE    =8;
 * @param pValue Object to compare
 * @throws PDException in any error
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
if (pValue instanceof String)
    TypeVal=Attribute.tSTRING;
else if (pValue instanceof Boolean)
    TypeVal=Attribute.tBOOLEAN;
else if (pValue instanceof Date)
    TypeVal=Attribute.tDATE;
else if (pValue instanceof Integer)
    TypeVal=Attribute.tINTEGER;    
else if (pValue instanceof BigDecimal)
    TypeVal=Attribute.tFLOAT;    
}
//-------------------------------------------------------------------------
/**
 * Constructor that creates a condition where pFied COMPARATION pValue
 * @param pField name of field to compare
 * @param pComparation Kind of comparation
 * cEQUAL   =0;
 * cGT      =1;
 * cLT      =2;
 * cGET     =3;
 * cLET     =4;
 * cNE      =5;
 * cLIKE    =8;
 * @param pValue Object to compare
 * @param pTypeVal Phisical type of value
 * @throws PDException in any error
 */
public Condition(String pField, int pComparation, Object pValue, int pTypeVal) throws PDException
{
cType=ctNORMAL;
Field=pField;
Comparation=pComparation;
Value=pValue;
TypeVal=pTypeVal;    
}
//-------------------------------------------------------------------------
/**
 * Compare two columns    ( Attr1=Attr2 )
 * @param pField name of first column
 * @param pField2 naame of the second column
 * @throws PDException in any error
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
 * Compare the value of a field with a list of values (pFiled in (ListVal) )
 * @param pField name of field to compare
 * @param ListVal List of values
 * @throws PDException in any error
 */
public Condition(String pField, HashSet ListVal) throws PDException
{
if (ListVal==null || ListVal.isEmpty())
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
 * Compare the value of a field with the result of  query (pFiled in (select ) )
 * @param pField name of field to compare
 * @param Search Query {@link prodoc.Query}
 * @throws PDException  in any error
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
* @return the cType , the type of condition
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
* @return the Invert , that is if wur condition has a "previous" NOT
*/
public boolean isInvert()
{
return Invert;
}
//-------------------------------------------------------------------------
/**
 * @return the TypeVal, the kind of value (String, Date,..)
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
XML.append("<cType>").append(cType).append("</cType>");
XML.append("<Field>").append(Field).append("</Field>");
switch (cType)
    {case ctNORMAL:
        XML.append("<TypeVal>").append(TypeVal).append("</TypeVal>");
        XML.append("<Comp>").append(Comparation).append("</Comp>");
        XML.append("<Val>");
        if (TypeVal==Attribute.tSTRING)
            XML.append(DriverGeneric.Codif((String)Value)); // to avoid false XML tags
        else if (TypeVal==Attribute.tTIMESTAMP)
            XML.append(formatterTS.format((Date)Value));
        else if (TypeVal==Attribute.tDATE)
            XML.append(formatterDate.format((Date)Value));
        else if (TypeVal==Attribute.tBOOLEAN)
            XML.append(((Boolean)Value)?"1":"0");
        else if (TypeVal==Attribute.tFLOAT)
            XML.append(Attribute.BD2String((BigDecimal)Value));
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
                XML.append(DriverGeneric.Codif((String)object)).append(StringListSeparator); // to avoid false XML tags
            else if (object instanceof Date)
                XML.append(formatterDate.format((Date)object)).append(StringListSeparator);
            else if (object instanceof Boolean)
                XML.append(((Boolean)object)?"1|":"0|");
            else
                XML.append(object).append(StringListSeparator);
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
 * @param XMLConds with condition
 * @throws prodoc.PDException in any error
 */
public Condition(Node XMLConds) throws PDException
{
NodeList OPDObjectList = XMLConds.getChildNodes();
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    Node OPDObject = OPDObjectList.item(i);
    String NodeName=OPDObject.getNodeName();
    String Cont=OPDObject.getTextContent();
    if (NodeName.equals("cType"))
        {
        cType=Integer.parseInt(Cont);
        }
    else if (NodeName.equals("Field"))
        {
        Field=Cont;
        }
    else if (NodeName.equals("Field2"))
        {
        Value=Cont;
        }
    else if (NodeName.equals("TypeVal"))
        {
        TypeVal=Integer.parseInt(Cont);
        }
    else if (NodeName.equals("Comp"))
        {
        Comparation=Integer.parseInt(Cont);
        }
    else if (NodeName.equals("Val"))
        {
        if (cType==ctIN)  
            {
            HashSet List=new HashSet();
            Comparation=cINList;
            if (TypeVal<0)
                TypeVal=Attribute.tSTRING;
            StringTokenizer St=new StringTokenizer(Cont, StringListSeparator);
            while (St.hasMoreTokens())
                {
                String ValS=St.nextToken();    
                if (TypeVal==Attribute.tSTRING)
                    List.add(DriverGeneric.DeCodif(ValS)); // to avoid false XML tags
                else if (TypeVal==Attribute.tTIMESTAMP && ValS.length()!=0)
                    {
                    try {
                        List.add(formatterTS.parse(ValS));
                    } catch (ParseException ex)
                        {
                        PDException.GenPDException(ex.getLocalizedMessage(), ValS) ;
                        }
                    }
                else if (TypeVal==Attribute.tDATE&& ValS.length()!=0)
                    {
                    try {
                        List.add(formatterDate.parse(ValS));
                    } catch (ParseException ex)
                        {
                        PDException.GenPDException(ex.getLocalizedMessage(), ValS) ;
                        }
                    }
                else if (TypeVal==Attribute.tBOOLEAN)
                    List.add(ValS.equals("1"));
                 else if (TypeVal==Attribute.tFLOAT && ValS.length()!=0)
                    try {
                    List.add(Attribute.String2BD(ValS));
                    } catch (Exception ex)
                        {
                        PDException.GenPDException(ex.getLocalizedMessage(), ValS) ;
                        }
                else
                    List.add(new Integer(ValS));
                }
            Value=List;
            }
        else
            {
            if (TypeVal==Attribute.tSTRING)
                Value=DriverGeneric.DeCodif(Cont); // to avoid false XML tags
            else if (TypeVal==Attribute.tTIMESTAMP && Cont.length()!=0)
                {
                try {
                    Value=formatterTS.parse(Cont);
                } catch (ParseException ex)
                    {
                    PDException.GenPDException(ex.getLocalizedMessage(), Cont) ;
                    }
                }
            else if (TypeVal==Attribute.tDATE&& Cont.length()!=0)
                {
                try {
                    Value=formatterDate.parse(Cont);
                } catch (ParseException ex)
                    {
                    PDException.GenPDException(ex.getLocalizedMessage(), Cont) ;
                    }
                }
            else if (TypeVal==Attribute.tFLOAT&& Cont.length()!=0)
                {
                try {
                    Value=Attribute.String2BD(Cont);
                } catch (Exception ex)
                    {
                    PDException.GenPDException(ex.getLocalizedMessage(), Cont) ;
                    }
                }
            else if (TypeVal==Attribute.tBOOLEAN)
                Value=OPDObject.getTextContent().equals("1");
            else if (TypeVal==Attribute.tTHES)
                Value=Cont;
            else
                Value=new Integer(Cont);
            }
        }
    }
}
//-------------------------------------------------------------------------
public static Condition genContainsCond(String TabName, String Arg, DriverGeneric Drv) throws PDException
{
PDDocs D=new PDDocs(Drv);
ArrayList SearchFT = D.SearchFT(TabName, true, Arg);
HashSet<String> L=new HashSet<>(SearchFT);
return(new Condition(PDDocs.fPDID, L));
}
//-------------------------------------------------------------------------
public static Conditions genInTreeCond(String Arg, DriverGeneric Drv) throws PDException
{
PDFolders F=new PDFolders(Drv);  
Conditions Cs=new Conditions();
Cs.addCondition(new Condition(PDFolders.fPARENTID, F.getQueryListDescendList(Arg)));
Cs.addCondition(new Condition(PDFolders.fPARENTID, cEQUAL, Arg));
Cs.setOperatorAnd(false);
return(Cs);
}
//-------------------------------------------------------------------------
public static Condition genInFolder(String Arg) throws PDException
{
return(new Condition(PDFolders.fPARENTID, Condition.cEQUAL, Arg));
}
//-------------------------------------------------------------------------
}
