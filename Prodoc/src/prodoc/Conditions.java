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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Manages sets of Conditions and/or Condition {@link prodoc.Condition}. 
 * The Condition elements are simple expresions, the Conditions are composed expresions.
 * @author jhierrot
 */
public class Conditions
{

/**
 * When true al the condition(s) in the current object has operator AND between them, otherwise, is OR
 */
private boolean OperatorAnd=true;
private boolean Invert=false;
/**
 * List of condition(s) included in the currnt condition
 */
private ArrayList CondList=new ArrayList();
//-------------------------------------------------------------------------
/**
 * Default constructor
 */
public Conditions()
{
super();
}
//-------------------------------------------------------------------------
/**
 * Adds a condition to the current level/set
 * @param Cond Condition to add see {@link prodoc.Condition}
 */
public final void addCondition(Condition Cond)
{
CondList.add(Cond);
}
//-------------------------------------------------------------------------
/**
 * Adds  Conditions to the current level/set
 * @param ListCond Conditions to add
 */
public final void addCondition(Conditions ListCond)
{
CondList.add(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Returns the number of condition(s) in the current level/set
 * @return Number of elements (Condition or Conditions) in teh current level
 */
public int NumCond()
{
return(CondList.size());
}
//-------------------------------------------------------------------------
/**
 * Return the condition n of the current set
 * @param n Number of condition to return
 * @return The Condition or Contidions Object in position n
 */
public Object Cond(int n)
{
return(CondList.get(n));
}
//-------------------------------------------------------------------------
/** 
 * Returns if all the set of Condition(s) at the current level has operator AND or OR.
 * @return the OperatorAnd
 */
public boolean isOperatorAnd()
{
return OperatorAnd;
}
//-------------------------------------------------------------------------
/** 
 * When true all the set of Condition(s) at the current level has operator AND otherwise they have OR between Conditions
 * @param OperatorAnd the OperatorAnd to set. 
 */
public final void setOperatorAnd(boolean OperatorAnd)
{
this.OperatorAnd = OperatorAnd;
}
//-------------------------------------------------------------------------
/**
 * Check if any Condition or Conditions at ANY level includes the column Attrname as search criteria
 * @param Attrname name to ccheck
 * @return true when is used in any expresion.
 */
public boolean UsedAttr(String Attrname)
{
for (Iterator it = CondList.iterator(); it.hasNext();)
    {
    Object object = it.next();   
    if (object instanceof Conditions)
        {
        if (((Conditions)object).UsedAttr(Attrname))
            return(true);
        }
    else
        {
        if (((Condition)object).getField().equalsIgnoreCase(Attrname))
            return(true);
        }
    }
return(false);    
}
//-------------------------------------------------------------------------
/**
 * Converts the Conditions in XML so can be reconstructed in remote
 * @return String with the XML
 * @throws prodoc.PDException in any error
 */
String toXML()
{
StringBuilder XML=new StringBuilder("<Conds>");
XML.append("<And>").append(isOperatorAnd()?"1":"0").append("</And>");
for (Object CondList1 : CondList)
    {
    if (CondList1 instanceof Conditions)    
        {
        Conditions C = (Conditions) CondList1;
        XML.append(C.toXML());
        } 
    else
        {
        Condition C = (Condition) CondList1;
        XML.append(C.toXML());
        }
    }
XML.append("</Conds>");
return (XML.toString());
}
//-------------------------------------------------------------------------
/**
 * Buils a Conditions object from XML
 * @param XMLConds with XML conditions
 * @throws PDException in any error
 */
public Conditions(Node XMLConds) throws PDException
{
NodeList OPDObjectList = XMLConds.getChildNodes();
for (int i=0; i<OPDObjectList.getLength(); i++)
    {   
    Node OPDObject = OPDObjectList.item(i);
    if (OPDObject.getNodeName().equals("And"))
        {
        setOperatorAnd(OPDObject.getTextContent().equals("1"));
        }
    else if (OPDObject.getNodeName().equals("Cond"))
        {
        addCondition(new Condition(OPDObject));
        }
    else if (OPDObject.getNodeName().equals("Conds"))
        {
        addCondition(new Conditions(OPDObject));
        }
    }
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
 * @param pInvert when true, the next conditions are inverted: NOT (cond)
 */
public void setInvert(boolean pInvert)
{
Invert = pInvert;
}
//-------------------------------------------------------------------------
}
