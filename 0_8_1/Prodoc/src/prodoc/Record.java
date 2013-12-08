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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jhierrot
 */
public class Record
{

/**
 *
 */
private ArrayList VAttr=new ArrayList();
/**
 *
 */
private int Pos;
//--------------------------------------------------------------------------
/**
 * 
 */
public void Clear()
{
VAttr.clear();
}
//--------------------------------------------------------------------------
/**
 *
 * @param rec
 * @return
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
 *
 * @param rec
 * @return
 * @throws PDException
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
 *
 * @param newAttr
 * @throws PDException
 */
public void addAttr(Attribute newAttr) throws PDException
{
if (this.ContainsAttr(newAttr.getName()))
   PDException.GenPDException("Duplicated_attribute",newAttr.getName());
VAttr.add(newAttr.Copy());
}
//--------------------------------------------------------------------------
/**
 *
 * @param DelAttr
 */
public void delAttr(Attribute DelAttr)
{
delAttr(DelAttr.getName());
}
//--------------------------------------------------------------------------
/**
 *
 * @param NameDelAttr
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
 *
 * @param NameAttr
 * @return
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
 *
 * @param NameAttr
 * @return
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
 *
 * @param NumAttr
 * @return
 * @throws PDException
 */
public Attribute getAttr(int NumAttr)  throws PDException
{
if (NumAttr<0||NumAttr>=VAttr.size())
    PDException.GenPDException("Incorrect_attribute_number", ""+NumAttr);
return((Attribute)VAttr.get(NumAttr));
}
//--------------------------------------------------------------------------
/**
 *
 * @return
 */
public int NumAttr()
{
return(VAttr.size());
}
//--------------------------------------------------------------------------
/**
 *
 * @return the number of attibutes with value
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
 *
 */
public void initList()
{
Pos=0;
}
//--------------------------------------------------------------------------
/**
 *
 * @return
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
 * 
 * @return
 * @throws PDException 
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
 * Returns a Copy of non Multivalued Attributes
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
 * Returns a Copy of non Multivalued Attributes
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
 *
 * @return
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
 * 
 * @param newRec
 * @throws PDException
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
 * 
 * @param substRec
 * @throws PDException
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
 *
 * @throws PDException
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
 * Converts all the attributes of the record to XML
 * @return the XML with the attributes.
 * @throws PDException  
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
static Record FillFromXML(Node AttrsNode, Record R) throws PDException
{
NodeList AttrLst = AttrsNode.getChildNodes();
for (int j = 0; j < AttrLst.getLength(); j++)
    {
    Node Attr = AttrLst.item(j);
    if (Attr.getNodeName().equalsIgnoreCase("attr"))   
        {
        NamedNodeMap XMLattributes = Attr.getAttributes();
        Node XMLAttrName = XMLattributes.getNamedItem("Name");
        String AttrName=XMLAttrName.getNodeValue();
        String Value=Attr.getTextContent();        
        Attribute At=R.getAttr(AttrName);
        if (At!=null)
            At.Import(Value);
        }
    }
return(R);
}
//--------------------------------------------------------------------------
}
