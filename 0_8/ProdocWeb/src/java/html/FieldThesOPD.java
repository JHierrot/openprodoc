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
package html;

/**
 *
 * @author jhierrot
 */
public class FieldThesOPD extends FieldThes
{
/** Creates a new instance of Literal
 * @param pNombre 
 */
public FieldThesOPD(String pNombre, String pStyle, String TreeRef)
{
super(pNombre);
setUrlImagArrowD("img/"+pStyle+"ArrowD.png");
setUrlImgSearch("img/"+pStyle+"Search.gif");
setAltImgSearch("Search");
setUrlImgInfo("img/"+pStyle+"Info.gif");
setAltImgInfo("Inf");
setUrlImgExit("img/"+pStyle+"Exit.gif");
setAltImgExit("Exit");
setUrlImgOk("img/"+pStyle+"Ok.png");
setAltImgOk("Ok");
setUrlImgDel("img/"+pStyle+"del.png");
setAltImgDel("Del");
setIdTes(TreeRef);
}
//-----------------------------------------------------------------------------------------------    
}
