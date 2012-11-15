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
}
