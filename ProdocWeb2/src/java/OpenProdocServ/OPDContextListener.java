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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import prodoc.ProdocFW;

/**
 *
 * @author jhierrot 2019
 */
@WebListener
public class OPDContextListener implements ServletContextListener 
{
//---------------------------------------------------------------------------
@Override
public void contextInitialized(ServletContextEvent sce)
{
try { 
ProdocFW.InitProdoc("PD", SParent.getProdocProperRef());    
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
}
//---------------------------------------------------------------------------
@Override
public void contextDestroyed(ServletContextEvent sce)
{
try {     
ProdocFW.ShutdownProdoc("PD");
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
}
    
}
