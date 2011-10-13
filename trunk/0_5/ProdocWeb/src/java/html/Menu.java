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

import java.util.ArrayList;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jhierrot
 */
public class Menu extends Container
{
private ArrayList MenCols=new ArrayList();
public Menu()
{

}
//----------------------------------------------------------------------------
// protected String HtmlPropio()
@Override
public String ToHtml(HttpSession Sess)
{
String CadHtml="<table cellpadding=\"2\" cellspacing=\"0\"  id=\"MenuLine\"><tr>";
for (int i = 0; i < MenCols.size(); i++)
    {
    CadHtml+="<td width=\"100px\" ><div onMouseOver=\"hideAll(); showLayer('MCol"+i+"'); stopTime()\" onMouseOut=\"startTime();\">"+((MenuCol)MenCols.get(i)).getText()+"</div></td>\n";
    }
CadHtml+="</tr></table>";
for (int i = 0; i < MenCols.size(); i++)
    {
    CadHtml+=((MenuCol)MenCols.get(i)).HtmlPropio();
    }
return(CadHtml);
}
//----------------------------------------------------------------------------
public void add(MenuCol NewMC)
{
MenCols.add(NewMC);
}
//----------------------------------------------------------------------------

}
