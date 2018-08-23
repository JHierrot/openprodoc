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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for managing the Records, sets of {@link Attribute} used for storing and saving metadata
 * @author jhierrot
 */
public class Record
{
/**
 * Internal storage of the Atributes of a Record
 */
private final ArrayList<Attribute> VAttr=new ArrayList();
/**
 * Position used for traveling fr the set of Atributes
 */
private int Pos=0;
//--------------------------------------------------------------------------
/**
 * Removes ALL the Attributes of the Record
 */
public void Clear()
{
VAttr.clear();
Pos=0;
}
//--------------------------------------------------------------------------
/**
 * Compares a Record with the current record
 * @param rec Record to compare
 * @return true when ALL the Attributes (with its types and values are equal
 */
public boolean equals(Record rec)
{
if (NumAttr()!=rec.NumAttr())
    return(false);
Attribute Attr1;
Attribute Attr2;
initList();
for (int i = 0; i < VAttr.size(); i++)
    {
    Attr1=nextAttr();
    Attr2=rec.getAttr(Attr1.getName());
    if (Attr2==null)
        return(false);
    if (!Attr1.equals(Attr2))
        return(false);
    }
return(true);
}
//--------------------------------------------------------------------------
/**
 * Assígn the values Attributes of a Record to the existing identical Attributes of current Record
 * @param rec Record containing values to assign
 * @return true always
 * @throws PDException In any error
 */
public boolean assign(Record rec) throws PDException
{
Attribute AttrOrig;
Attribute AttrDest;
rec.initList();
for (int i = 0; i < rec.NumAttr(); i++)
    {
    AttrOrig=rec.nextAttr();
    AttrDest=getAttr(AttrOrig.getName());
    if (AttrDest!=null && AttrDest.equalDef(AttrOrig))
        {
        if (AttrDest.isMultivalued())
            {
            if (AttrDest!=AttrOrig) // to avoid problems when using the same record
                {
                AttrDest.ClearValues();
                TreeSet V=AttrOrig.getValuesList();    
                for (Iterator it = V.iterator(); it.hasNext();)
                    {
                    AttrDest.AddValue(it.next());                    
                    }
                }
            }
        else
            AttrDest.setValue(AttrOrig.getValue());
        }
    }
return(true);
}
//--------------------------------------------------------------------------
/**
 * Assígn the values Attributes of a Record to the existing SIMILAR (equal name) Attributes of current Record
 * @param rec Record containing values to assign
 * @return true always
 * @throws PDException In any error
 */
protected boolean assignSimil(Record rec) throws PDException
{
Attribute AttrOrig;
Attribute AttrDest;
rec.initList();
for (int i = 0; i < rec.NumAttr(); i++)
    {
    AttrOrig=rec.nextAttr();
    AttrDest=getAttr(AttrOrig.getName());
    if (AttrDest!=null)
        {
        if (AttrDest.isMultivalued())
            {
            if (AttrDest!=AttrOrig) // to avoid problems when using the same record
                {
                AttrDest.ClearValues();
                TreeSet V=AttrOrig.getValuesList();    
                for (Iterator it = V.iterator(); it.hasNext();)
                    {
                    AttrDest.AddValue(it.next());                    
                    }
                }
            }
        else
            AttrDest.setValue(AttrOrig.getValue());
        }
    }
return(true);
}
//--------------------------------------------------------------------------
/**
 * Adds an attribute to the current record
 * @param newAttr Attribute to add
 * @throws PDException In any Error (or duplicated Atribute name)
 */
public void addAttr(Attribute newAttr) throws PDException
{
if (this.ContainsAttr(newAttr.getName()))
   PDException.GenPDException("Duplicated_attribute",newAttr.getName());
VAttr.add(newAttr.Copy());
}
//--------------------------------------------------------------------------
/**
 * Removes an Attribute from the current Record
 * @param DelAttr Attribute to remove
 */
public void delAttr(Attribute DelAttr)
{
delAttr(DelAttr.getName());
}
//--------------------------------------------------------------------------
/**
 * Removes an Attribute from the current Record
 * @param NameDelAttr name of the Attribute to remove
 */
public void delAttr(String NameDelAttr)
{
Attribute Attrdel;
for (int i = 0; i < VAttr.size(); i++)
    {
    Attrdel = (Attribute)VAttr.get(i);
    if (Attrdel.getName().equals(NameDelAttr))
        {
        VAttr.remove(i);
        break;
        }
    }
}
//--------------------------------------------------------------------------
/**
 * Checks if an Attribute with the specified name exist in the current Record
 * @param NameAttr name of the Attribute to check
 * @return true if the Attribute Exist
 */
public boolean ContainsAttr(String NameAttr)
{
Attribute Attrdel;
for (int i = 0; i < VAttr.size(); i++)
    {
    Attrdel = (Attribute)VAttr.get(i);
    if (Attrdel.getName().equalsIgnoreCase(NameAttr))
        return(true);
    }
return(false);
}
//--------------------------------------------------------------------------
/**
 * Returns the Attribute of the Record with the specified name
 * @param NameAttr Name of the Attribute to look for
 * @return The Atrribute or null if the Record don't contains an Attribute with NameAttr name
 */
public Attribute getAttr(String NameAttr)
{
Attribute Attr;
for (int i = 0; i < VAttr.size(); i++)
    {
    Attr = (Attribute)VAttr.get(i);
    if (Attr.getName().equalsIgnoreCase(NameAttr))
        {
        return Attr;
        }
    }
return(null);
}
//--------------------------------------------------------------------------
/**
 * Return the Attribute with the position specified in the record
 * @param NumAttr Number of Attribute
 * @return The Attribute
 * @throws PDException If the position specified is less that 0 or bigger than the number of Attributes
 */
public Attribute getAttr(int NumAttr)  throws PDException
{
if (NumAttr<0||NumAttr>=VAttr.size())
    PDException.GenPDException("Incorrect_attribute_number", ""+NumAttr);
return((Attribute)VAttr.get(NumAttr));
}
//--------------------------------------------------------------------------
/**
 * Returns the current number of Attributes of the Record
 * @return the current number of Attributes of the Record
 */
public int NumAttr()
{
return(VAttr.size());
}
//--------------------------------------------------------------------------
/**
 * Returns the number of Attributes with value
 * @return the number of Attributes with value
 */
public int NumAttrFilled()
{
int N=0;
Attribute Attr;
for (int i = 0; i < VAttr.size(); i++)
    {
    Attr = (Attribute)VAttr.get(i);
    if (Attr.isMultivalued())
        {
        try {
            if (!Attr.getValuesList().isEmpty())
                N++;
        } catch (PDException ex)
            {
            }
        }
    else
        {
        if (Attr.getValue()!=null)
            N++;
        }
    }
return(N);
}
//--------------------------------------------------------------------------
/**
 * Starts the counter so a program can travel by the set of Attributes with {@link #nextAttr()}
 */
public void initList()
{
Pos=0;
}
//--------------------------------------------------------------------------
/**
 * After initialization with {@link #initList()}, allows to travel by the set of Attributes of the Record
 * @return The next Attribute or null if the End of the list is reached
 */
public Attribute nextAttr()
{
if (Pos<NumAttr())
    return(((Attribute)VAttr.get(Pos++)));
else
    return(null);
}
//--------------------------------------------------------------------------
/**
 * Creates a identical copy of he record with the same Attributes and values
 * @return The created copy of the Record 
 * @throws PDException In any error
 */
synchronized public Record Copy() throws PDException
{
Record Copy=new Record();
initList();
for (int i = 0; i < NumAttr(); i++)
    {
    Copy.addAttr(nextAttr().Copy());
    }
return(Copy);
}
//--------------------------------------------------------------------------
/**
 * Returns a Copy of NON Multivalued Attributes
 * @return a new Record with non Multivalued Attributes
 * @throws PDException in any error
 */
synchronized public Record CopyMono() throws PDException
{
Record Copy=new Record();
initList();
for (int i = 0; i < NumAttr(); i++)
    {
    Attribute Atr=nextAttr();
    if (!Atr.isMultivalued())
        Copy.addAttr(Atr.Copy());
    }
return(Copy);
}
//--------------------------------------------------------------------------
/**
 * Returns a Copy of Multivalued Attributes
 * @return a new Record with non Multivalued Attributes
 * @throws PDException in any error
 */
synchronized public Record CopyMulti() throws PDException
{
Record Copy=new Record();
initList();
for (int i = 0; i < NumAttr(); i++)
    {
    Attribute Atr=nextAttr();
    if (Atr.isMultivalued())
        Copy.addAttr(Atr.Copy());
    }
return(Copy);
}
//--------------------------------------------------------------------------
/**
 * Returns an String showing all the Attributes of the Record
 * @return an String showing all the Attributes of the Record
 */
@Override
public String toString()
{
String S="[";
initList();
for (int i = 0; i < NumAttr(); i++)
    S+=nextAttr();
S+="]";
return(S);
}
//--------------------------------------------------------------------------
/**
 * Add all the Attributes of the parameter that DON'T exist in the current Record
 * @param newRec Record with the Attributes to add
 * @throws PDException In any error
 */
public void addRecord(Record newRec) throws PDException
{
newRec.initList();
for (int i = 0; i < newRec.NumAttr(); i++)
    {
    Attribute A=newRec.nextAttr();
    if (!this.ContainsAttr(A.getName()))
        addAttr(A.Copy());
    }
}
//--------------------------------------------------------------------------
/**
 * Removes from the current Record all the (existing) Attributes included in the parameter
 * @param substRec Record with the Attributs to remove
 * @throws PDException In any Error
 */
public void delRecord(Record substRec) throws PDException
{
substRec.initList();
for (int i = 0; i < substRec.NumAttr(); i++)
    {
    delAttr(substRec.nextAttr());
    }
}
//--------------------------------------------------------------------------
/**
 * Checks if the Values in the Record follow the restrictions defined for each Attribute (mainly length or required)
 * @throws PDException If any Attribute don't match the restrictions
 */
public void Verify() throws PDException
{
initList();
for (int i = 0; i < NumAttr(); i++)
    {
    nextAttr().Verify();
    }
}
//--------------------------------------------------------------------------
/**
 * Converts all the attributes of the record to XML in a short format
 * @return the XML with the attributes.
 * @throws PDException in any error
 */
public String toXML() throws PDException
{
StringBuilder S=new StringBuilder(500);
initList();
for (int i = 0; i < NumAttr(); i++)
    S.append(nextAttr().toXML());
return(S.toString());
}
//--------------------------------------------------------------------------
/**
 * Converts all the attributes of the record to XML with ALL tags and elements
 * @return the XML with the attributes.
 * @throws PDException in any error 
 */
public String toXMLt() throws PDException
{
StringBuilder S=new StringBuilder(500);
S.append("<Rec>");
initList();
for (int i = 0; i < NumAttr(); i++)
    S.append(nextAttr().toXMLt());
S.append("</Rec>");
return(S.toString());
}
//--------------------------------------------------------------------------
/**
 * Converts all the NON null attributes of the record to XML with ALL tags and elements
 * @return the XML with the attributes.
 * @throws PDException in any error   
 */
public String toXMLtNotNull() throws PDException
{
StringBuilder S=new StringBuilder(500);
S.append("<Rec>");
initList();
for (int i = 0; i < NumAttr(); i++)
    {
    Attribute At=nextAttr();
    if (At.getValue()!=null)
        S.append(At.toXMLt());
    }
S.append("</Rec>");
return(S.toString());
}
//--------------------------------------------------------------------------
/**
 * Fills the Atributes of an EXISTING Record with the values received in XML
 * @param AttrsNode XML Node to import
 * @param R Record to fill
 * @return the Filled Record
 * @throws PDException  In any error
 */
static Record FillFromXML(Node AttrsNode, Record R) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("Record.FillFromXML>:R="+R+"AttrsNode="+AttrsNode);        
NodeList AttrLst = AttrsNode.getChildNodes();
for (int j = 0; j < AttrLst.getLength(); j++)
    {
    Node Attr = AttrLst.item(j);
    if (Attr.getNodeName().equalsIgnoreCase("attr"))  
        {
        NamedNodeMap XMLattributes = Attr.getAttributes();
        Node XMLAttrName = XMLattributes.getNamedItem("Name");
        String AttrName=XMLAttrName.getNodeValue();
        String Value=DriverGeneric.DeCodif(Attr.getTextContent());        
        Attribute At=R.getAttr(AttrName);
        if (At!=null)
            At.ImportXML(Value);
        }
    }
if (PDLog.isDebug())
    PDLog.Debug("Record.FillFromXML<");        
return(R);
}
//--------------------------------------------------------------------------
/**
 * Creates a NEW Record importing a XML node
 * @param AttrsNode Node to import
 * @return The created and filled record
 * @throws PDException In any error
 */
static Record CreateFromXML(Node AttrsNode) throws PDException
{
Record R=new Record();    
NodeList AttrLst = AttrsNode.getChildNodes();
for (int j = 0; j < AttrLst.getLength(); j++)
    {
    Node Attr = AttrLst.item(j);
    if (Attr.getNodeName().equalsIgnoreCase("attr"))  
        {
        NamedNodeMap XMLattributes = Attr.getAttributes();
        Node XMLAttrName = XMLattributes.getNamedItem("Name");
        String AttrName=XMLAttrName.getNodeValue();
        XMLAttrName = XMLattributes.getNamedItem("Multi");
        boolean Multi=Integer.parseInt(XMLAttrName.getNodeValue())==1;
        XMLAttrName = XMLattributes.getNamedItem("Type");
        int Type=Integer.parseInt(XMLAttrName.getNodeValue());
        XMLAttrName = XMLattributes.getNamedItem("LongStr");
        int LongStr=Integer.parseInt(XMLAttrName.getNodeValue());
        XMLAttrName = XMLattributes.getNamedItem("Req");
        boolean Req=Integer.parseInt(XMLAttrName.getNodeValue())==1;
        XMLAttrName = XMLattributes.getNamedItem("ModAllow");
        boolean ModAllow=Integer.parseInt(XMLAttrName.getNodeValue())==1;
        XMLAttrName = XMLattributes.getNamedItem("UniKey");
        boolean UniKey=Integer.parseInt(XMLAttrName.getNodeValue())==1;
        String Value=DriverGeneric.DeCodif(Attr.getTextContent());
        Attribute At=new Attribute(AttrName, "", "", Type, Req, null, LongStr, false, UniKey, ModAllow, Multi);
        // if (Type==Attribute.tSTRING || Value.length()!=0)
        if (Value.length()!=0 || (Type==Attribute.tSTRING && Value.length()==0))
            At.ImportXML(Value);
        R.addAttr(At);
        }
    }
return(R);
}
//--------------------------------------------------------------------------
/**
 * Checks the set of Attributes of a Record BUT with some exceptions
 * ssimilar to {@link #Verify()}
 * @throws PDExceptionFunc In any error
 */
public void CheckDef() throws PDExceptionFunc
{
initList();
for (int i = 0; i < NumAttr(); i++)
    {
    Attribute At=nextAttr();
    if (At.getName().equals(PDDocs.fNAME) || At.getName().equals(PDDocs.fMIMETYPE))
        continue;
    if (At.isRequired() && (At.getValue()==null || (At.getType()==Attribute.tSTRING && ((String)At.getValue()).length()==0)))
        PDExceptionFunc.GenPDException("A_value_is_required", At.getUserName());
    }
}
//--------------------------------------------------------------------------
/**
 * Removes ALL the atrributes without value
 */
public void DelNull()
{
for (int i = VAttr.size()-1; i >=0; i--)
        {
        Attribute At = (Attribute)VAttr.get(i);
        if (!At.isMultivalued() && At.getValue()==null)
            VAttr.remove(i);
        }
}
//--------------------------------------------------------------------------
}
