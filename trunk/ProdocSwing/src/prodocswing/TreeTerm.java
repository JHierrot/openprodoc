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

package prodocswing;

import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class TreeTerm
{
private PDThesaur Term;
private boolean expanded = false;

/**
 * 
 * @param pFold
 */
public TreeTerm(PDThesaur pTerm)
{
Term=pTerm;    
}

//------------------------------------------------------------------------
/**
* @return the Fold
*/
public PDThesaur getFold()
{
return Term;
}
//------------------------------------------------------------------------
/**
* @return the expanded
*/
public boolean isExpanded()
{
return expanded;
}
//------------------------------------------------------------------------
/**
* @param pExpanded the expanded to set
*/
public void setExpanded(boolean pExpanded)
{
expanded = pExpanded;
}
//------------------------------------------------------------------------
public String toString()
{
return Term.getName();
}
//--------------------------------------------

}
