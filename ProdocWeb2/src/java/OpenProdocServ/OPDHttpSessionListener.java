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
package OpenProdocServ;

import OpenProdocUI.SParent;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author jhierrot
 */
@WebListener
public class OPDHttpSessionListener implements HttpSessionListener 
{
//-----------------------------------------------------------------------------------------------
@Override
public void sessionCreated(HttpSessionEvent se)
{
}
//-----------------------------------------------------------------------------------------------
@Override
public void sessionDestroyed(HttpSessionEvent HttpSesEvent)
{
SParent.ClearSessOPD(HttpSesEvent.getSession());
}
//-----------------------------------------------------------------------------------------------    
}
