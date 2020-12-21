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

import java.util.Vector;

/**
 * Manages the "cursors" generated when searchin in any object through ny kind of driver
 * @author jhierrot
 */
public class Cursor
{
/**
 * Cursor Identifier
 */
private String CursorId;
/**
 * Records of Fields included in Cursor result
 */
private Record FieldsCur;
/**
 * ResulSet containing the results (Vectod, JDBC resulSet,...)
 */
private Object ResultSet;
/**
 * Max size of cursor (temporal solution due non standard sql
 */
private int MaxResults=1000000;
/**
 * Returned results
 */
private int ReturnedResults=0;

//--------------------------------------------------------------------
/**
 * Default constructor
 * @param pCursorId  Cursor Identifier
 * @param pFieldsCur Record of Atributes returned by Cursor
 * @param pResultSet ResultSet Object
 */
public Cursor (String pCursorId, Record pFieldsCur, Object pResultSet, int pMaxResults)
{
setCursorId(pCursorId);
setFieldsCur(pFieldsCur);
setResultSet(pResultSet);
MaxResults=pMaxResults;
}
//--------------------------------------------------------------------
/**
 * Default constructor
 * @param pCursorId  Cursor Identifier
 * @param pFieldsCur Record of Atributes returned by Cursor
 * @param pResultSet ResultSet Object
 */
public Cursor (String pCursorId, Record pFieldsCur, Object pResultSet)
{
setCursorId(pCursorId);
setFieldsCur(pFieldsCur);
setResultSet(pResultSet);
}
//------------------------------------------------------------------
/** 
 * Returns the Cusor Identifier
 * @return the CursorId
 */
protected String getCursorId()
{
return CursorId;
}
//-----------------------------------------------------------
/**
 * Sets the Cursor Identifier
 * @param pCursorId the CursorId to set
 */
private void setCursorId(String pCursorId)
{
CursorId = pCursorId;
}
//-----------------------------------------------------------
/**
 * Returns the fields in teh cursor
 * @return the Record {@link prodoc.Record} with the fields defined (select *)
 */
public Record getFieldsCur()
{
return FieldsCur;
}
//-----------------------------------------------------------
/**
 * Assign the fields in the cursor
 * @param FieldsCur the Record {@link prodoc.Record} to set
 */
private void setFieldsCur(Record FieldsCur)
{
this.FieldsCur = FieldsCur;
}
//-----------------------------------------------------------
/**
 * Returns the object containing the resulset
 * @return the ResultSet with the list of records obtained
 */
protected Object getResultSet()
{
return ResultSet;
}
//-----------------------------------------------------------
/**
 * Sets the resulset
 * @param ResultSet the ResultSet to set
 */
private void setResultSet(Object ResultSet)
{
this.ResultSet = ResultSet;
}
//-----------------------------------------------------------
/**
 * Traceability method
 * @return a String representation of Cursor
 */
@Override
public String toString()
{
return("CursorId:["+CursorId+"] Fields:["+FieldsCur+"]");
}
//-----------------------------------------------------------
/**
 * Method that cleans the resultset when it's a Vector in memory
 */
protected void CleanResultSet()
{
if (getResultSet()!=null&& getResultSet() instanceof Vector)
    {
    ((Vector)getResultSet()).clear();
    ResultSet=null;  //  for helping GC
    }
}
//-----------------------------------------------------------
/**
 * @return the MaxResults
 */
public int getMaxResults()
{
return MaxResults;
}
//-----------------------------------------------------------
/**
 * Increments the returned Results
 */
public int IncResults()
{
return (++ReturnedResults);
}
//-----------------------------------------------------------
/**
 * @return the ReturnedResults
 */
public int getReturnedResults()
{
return ReturnedResults;
}
//-----------------------------------------------------------

}
