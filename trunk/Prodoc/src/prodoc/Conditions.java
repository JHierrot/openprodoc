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
import java.util.ArrayList;
import java.util.Iterator;
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
public class Conditions
{
/**
 *
 */
private boolean OperatorAnd=true;
/**
 *
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
 * Buils a Conditions object from XML
 * @param XML with a condition
 */
public Conditions(String XML) throws Exception
{
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(new ByteArrayInputStream(XML.getBytes("UTF-8")));
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Conds");
}
//-------------------------------------------------------------------------
/**
 * Buils a Conditions object from XML
 * @param Conds with XML conditions
 * @throws java.lang.Exception
 */
public Conditions(Node Conds) throws Exception
{

}
//-------------------------------------------------------------------------
/**
 *
 * @param Cond
 */
public void addCondition(Condition Cond)
{
CondList.add(Cond);
}
//-------------------------------------------------------------------------
/**
 *
 * @param ListCond
 */
public void addCondition(Conditions ListCond)
{
CondList.add(ListCond);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
public int NumCond()
{
return(CondList.size());
}
//-------------------------------------------------------------------------
/**
 * 
 * @param n
 * @return
 */
public Object Cond(int n)
{
return(CondList.get(n));
}
//-------------------------------------------------------------------------
/**
* @return the OperatorAnd
*/
public boolean isOperatorAnd()
{
return OperatorAnd;
}
//-------------------------------------------------------------------------
/**
* @param OperatorAnd the OperatorAnd to set
*/
public void setOperatorAnd(boolean OperatorAnd)
{
this.OperatorAnd = OperatorAnd;
}
//-------------------------------------------------------------------------
/**
 *
 * @param Attrname
 * @return
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
}
