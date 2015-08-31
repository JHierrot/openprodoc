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
 * author: Joaquin Hierro      2015
 * 
 */

package html;

import prodoc.Condition;
/**
 *
 * @author Joaquin
 */
public class FieldComboOper extends FieldCombo
{

public FieldComboOper(String pNombre)
{
super(pNombre);
AddOpcion(""+Condition.cEQUAL, "=");
AddOpcion(""+Condition.cGT,   ">");
AddOpcion(""+Condition.cLT,   "<");
AddOpcion(""+Condition.cGET,  ">=");
AddOpcion(""+Condition.cLET,  "<=");
AddOpcion(""+Condition.cNE,   "<>");
AddOpcion(""+Condition.cLIKE, "Cont");
}
    
}
