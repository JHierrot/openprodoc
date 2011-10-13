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

/**
 *
 * @author jhierrot
 */
public class Cursor
{
/**
 *
 */
private String CursorId;
/**
 *
 */
private Record FieldsCur;
/**
 *
 */
private Object ResultSet;
/**
* @return the CursorId
*/
public String getCursorId()
{
return CursorId;
}
//-----------------------------------------------------------
/**
* @param pCursorId the CursorId to set
*/
public void setCursorId(String pCursorId)
{
CursorId = pCursorId;
}
//-----------------------------------------------------------
/**
* @return the FieldsCur
*/
public Record getFieldsCur()
{
return FieldsCur;
}
//-----------------------------------------------------------
/**
* @param FieldsCur the FieldsCur to set
*/
public void setFieldsCur(Record FieldsCur)
{
this.FieldsCur = FieldsCur;
}
//-----------------------------------------------------------
/**
* @return the ResultSet
*/
public Object getResultSet()
{
return ResultSet;
}
//-----------------------------------------------------------
/**
* @param ResultSet the ResultSet to set
*/
public void setResultSet(Object ResultSet)
{
this.ResultSet = ResultSet;
}
//-----------------------------------------------------------
@Override
public String toString()
{
return("CursorId:["+CursorId+"] Fields:["+FieldsCur+"]");
}
//-----------------------------------------------------------
}
