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

import java.util.Vector;

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
}
