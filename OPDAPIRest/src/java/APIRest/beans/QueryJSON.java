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
 * author: Joaquin Hierro      2019
 * 
 */
package APIRest.beans;

import com.google.gson.Gson;

/**
 *
 * @author jhier
 */
public class QueryJSON
{

private String Query=null;
private int Initial=0;
private int Final=100;

public static QueryJSON CreateQuery(String QueryJson)
{
Gson g = new Gson();
return(g.fromJson(QueryJson, QueryJSON.class));    
}
//-------------------------------------------------------------------------
/**
 * @return the Query
 */
public String getQuery()
{
return Query;
}  
//-------------------------------------------------------------------------

    /**
     * @return the Initial
     */
    public int getInitial()
    {
        return Initial;
    }

    /**
     * @return the Final
     */
    public int getFinal()
    {
        return Final;
    }

}
