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
private Vector<String> OrderList=null;
private Vector<Boolean> OrderAsc=null;
//-------------------------------------------------------------------------
/**
 * Constructs a Query 
 * @param pTable Table to search for
 * @param pFields Fileds to include in the result Cursor
 * @param pWhere Conditions of the query
 */
public Query(String pTable, Record pFields, Conditions pWhere)
{
Table=pTable;
RetrieveFields=pFields;
Where=pWhere;
}
//-------------------------------------------------------------------------
/**
 * Constructs a Query 
 * @param pTable Table to search for
 * @param pFields Fileds to include in the result Cursor
 * @param pWhere Conditions of the query
 * @param pOrder Order in which return the results
 */
public Query(String pTable, Record pFields, Conditions pWhere, String pOrder)
{
Table=pTable;
RetrieveFields=pFields;
Where=pWhere;
OrderList=new Vector();
OrderList.add(pOrder);
OrderAsc=new Vector();
OrderAsc.add(true);
}
//-------------------------------------------------------------------------
/**
 * Constructs a Query 
 * @param pTables Tables to search for
 * @param pFields Fileds to include in the result Cursor
 * @param pWhere Conditions of the query
 * @param pOrderList Order in which return the results
 */
public Query(Vector pTables, Record pFields, Conditions pWhere, Vector<String> pOrderList)
{
Tables=pTables;
RetrieveFields=pFields;
Where=pWhere;
OrderList=pOrderList;
if (pOrderList!=null)
    {
    OrderAsc=new Vector();
    for (int i = 0; i < pOrderList.size(); i++)
        OrderAsc.add(true); 
    }
}
//-------------------------------------------------------------------------
/**
 * Constructs a Query 
 * @param pTables Tables to search for
 * @param pFields Fileds to include in the result Cursor
 * @param pWhere Conditions of the query
 * @param pOrderList Order in which return the results
 * @param pOrderAsc indicates if the order is Ascending (true) or descending (false)
 */
public Query(Vector pTables, Record pFields, Conditions pWhere, Vector<String> pOrderList, Vector<Boolean> pOrderAsc)
{
Tables=pTables;
RetrieveFields=pFields;
Where=pWhere;
OrderList=pOrderList;
OrderAsc=pOrderAsc; 
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
* @return the OrderList
*/
public Vector<String> getOrderList()
{
return OrderList;
}
//-------------------------------------------------------------------------
/**
* @return the OrderList
*/
public Vector<Boolean> getOrderAscList()
{
return OrderAsc;
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
        XOrders+=OrderList.elementAt(i)+StringListSeparator;
        }
    XOrders+="</Ord>";
    if (OrderAsc!=null)
        {
        XOrders="<OrdAsc>";
        for (int i = 0; i < OrderAsc.size(); i++)
            {
            XOrders+=OrderAsc.elementAt(i)+StringListSeparator;
            }
        XOrders+="</OrdAsc>";
        }
    }          
    XOrders="";
return("<Q><Tab>"+XTabs+"</Tab>"+RetrieveFields.toXMLt()+XWhere+XOrders+"</Q>");
}
//-------------------------------------------------------------------------
/**
 * Constructs a Query from an XML document
 * @param XMLObjects XML Document with the query
 * @throws PDException in any error
 */
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
RetrieveFields=Record.CreateFromXML(OPDObject);
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
    StringTokenizer St=new StringTokenizer(Ord, StringListSeparator);
    OrderList=new Vector();
    while (St.hasMoreTokens())
        OrderList.add(St.nextToken());
    OPDObjectList = XMLObjects.getElementsByTagName("OrdAsc");
    OrderAsc=new Vector();
    if (OPDObjectList.getLength()>0)
        {
        OPDObject = OPDObjectList.item(0);
        String OrdAsc=OPDObject.getTextContent();
        OrderAsc=null;
        StringTokenizer StOA=new StringTokenizer(OrdAsc, StringListSeparator);
        while (StOA.hasMoreTokens())
            OrderAsc.add(Boolean.parseBoolean(StOA.nextToken()));
        }
    else
        {
        for (int i = 0; i < OrderList.size(); i++)
            OrderAsc.add(true);
        }
    }
}
//-------------------------------------------------------------------------
}
