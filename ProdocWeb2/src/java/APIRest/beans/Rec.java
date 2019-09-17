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

import java.util.ArrayList;
import prodoc.Attribute;
import prodoc.Record;

/**
 *
 * @author jhier
 */
public class Rec
{
ArrayList<Attr> Attrs=new ArrayList();
//--------------------------------------------------------------------------    
public Rec(Record Rec)
{
Rec.initList();
Attribute Attrib=Rec.nextAttr();
while (Attrib!=null)
    {
    Attrs.add(new Attr(Attrib));
    Attrib=Rec.nextAttr();
    }  
}
//--------------------------------------------------------------------------    
}
