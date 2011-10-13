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
public class FieldFile extends Field
{
//-----------------------------------------------------------------------------------------------    

/** Creates a new instance of FieldFile */
public FieldFile(String pTexto, String pNombre)
{
super(pNombre);    
Value=pTexto;
}
//-----------------------------------------------------------------------------------------------    
protected String HtmlPropio()    
{
return(" type=\"file\" ");    
}
//-----------------------------------------------------------------------------------------------    
    
}
