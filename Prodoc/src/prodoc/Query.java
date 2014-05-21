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

import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static prodoc.Attribute.StringListSeparator;

/**
 *
 * @author jhierrot
 */
public class Query
{
/**
 *
 */
private Vector Tables=null;
/**
 *
 */
private String Table=null;
/**
 *
 */
private Record RetrieveFields=null;
/**
 *
 */
private Conditions Where=null;
/**
 *
 */
private String Order=null;
/**
 *
 */
private Vector OrderList=null;
//-------------------------------------------------------------------------
/**
 *
 * @param pTable
 * @param pFields
 * @param pWhere
 */
public Query(String pTable, Record pFields, Conditions pWhere)
{
Table=pTable;
RetrieveFields=pFields;
Where=pWhere;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pTable
 * @param pFields
 * @param pOrder
 * @param pWhere
 */
public Query(String pTable, Record pFields, Conditions pWhere, String pOrder)
{
Table=pTable;
RetrieveFields=pFields;
Where=pWhere;
Order=pOrder;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pTables
 * @param pFields
 * @param pWhere
 * @param pOrderList
 */
public Query(Vector pTables, Record pFields, Conditions pWhere, Vector pOrderList)
{
Tables=pTables;
RetrieveFields=pFields;
Where=pWhere;
OrderList=pOrderList;
}
//-------------------------------------------------------------------------
/**
* @return the Tables
*/
public Vector getTables()
{
return Tables;
}
//-------------------------------------------------------------------------
/**
* @return the RetrieveFields
*/
public Record getRetrieveFields()
{
return RetrieveFields;
}
//-------------------------------------------------------------------------
/**
* @return the Where
*/
public Conditions getWhere()
{
return Where;
}
//-------------------------------------------------------------------------
/**
* @return the Table
*/
public String getTable()
{
return Table;
}
//-------------------------------------------------------------------------
/**
* @return the Order
*/
public String getOrder()
{
return Order;
}
//-------------------------------------------------------------------------
/**
* @return the OrderList
*/
public Vector getOrderList()
{
return OrderList;
}
//-------------------------------------------------------------------------
/**
 * Converts the Query in XML so can be reconstructed in remote
 * @return String with the XML
 * @throws prodoc.PDException in any error
 */
public String toXML() throws PDException
{
String XTabs;
if (Tables!=null)
    {
    XTabs="";    
    for (int i = 0; i < Tables.size(); i++)
        {
        XTabs+=(String)Tables.elementAt(i)+StringListSeparator;
        }
    }
else
    XTabs=getTable();
String XWhere;
if (Where!=null)
    XWhere=Where.toXML();
else
    XWhere="";
String XOrders;
if (OrderList!=null)
    {
    XOrders="<Ord>";
    for (int i = 0; i < OrderList.size(); i++)
        {
        XOrders+=(String)OrderList.elementAt(i)+StringListSeparator;
        }
    XOrders="</Ord>";
    }
else if (Order!=null)
    XOrders="<Ord>Order</Ord>";
else            
    XOrders="";
return("<Q><Tab>"+XTabs+"</Tab><Rec>"+RetrieveFields.toXMLt()+"</Rec>"+XWhere+XOrders+"</Q>");
}
//-------------------------------------------------------------------------
public Query(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tabs=OPDObject.getTextContent();
if (Tabs.contains(StringListSeparator))
    {
    StringTokenizer St=new StringTokenizer(Tabs, StringListSeparator);
    Tables=new Vector();
    while (St.hasMoreTokens())
        {
        Tables.add(St.nextToken());
        }
    }
else
    {
    Table=Tabs;
    }
OPDObjectList = XMLObjects.getElementsByTagName("Rec");
OPDObject = OPDObjectList.item(0);
RetrieveFields=new Record();
Record.FillFromXML(OPDObject, RetrieveFields);
OPDObjectList = XMLObjects.getElementsByTagName("Conds");
if (OPDObjectList.getLength()>0)
    {
    OPDObject = OPDObjectList.item(0);
    Where=new Conditions(OPDObject);
    }
OPDObjectList = XMLObjects.getElementsByTagName("Ord");
if (OPDObjectList.getLength()>0)
    {
    OPDObject = OPDObjectList.item(0);
    String Ord=OPDObject.getTextContent();
    OrderList=null;
    if (Ord.contains(StringListSeparator))
        {
        StringTokenizer St=new StringTokenizer(Ord, StringListSeparator);
        OrderList=new Vector();
        while (St.hasMoreTokens())
            {
            OrderList.add(St.nextToken());
            }
        }
    }
}
//-------------------------------------------------------------------------
}
