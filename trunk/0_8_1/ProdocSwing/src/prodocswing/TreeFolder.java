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

import prodoc.PDFolders;

/**
 *
 * @author jhierrot
 */
public class TreeFolder
{
private PDFolders Fold;
private boolean expanded = false;

/**
 * 
 * @param pFold
 */
public TreeFolder(PDFolders pFold)
{
Fold=pFold;    
}

//------------------------------------------------------------------------
/**
* @return the Fold
*/
public PDFolders getFold()
{
return Fold;
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
return Fold.getTitle();
}
//--------------------------------------------

}
