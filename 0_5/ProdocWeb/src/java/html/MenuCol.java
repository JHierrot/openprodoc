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

/**
 *
 * @author jhierrot
 */
public class MenuCol extends Container
{
private String Text;
private int Position;
private ArrayList Items=new ArrayList();
//----------------------------------------------------------------------------

public MenuCol(String pText, int pPosition)
{
Text=pText;
Position=pPosition;
}
//----------------------------------------------------------------------------
protected String HtmlPropio()
{
String CadHtml="<div id=\"MCol"+Position+"\">";
for (int i = 0; i < Items.size(); i++)
    {
    CadHtml+=((MenuItem)Items.get(i)).HtmlPropio();
    }
CadHtml+="</div>\n";
return(CadHtml);
}
//----------------------------------------------------------------------------
/**
 * @return the Text
 */
public String getText()
{
return Text;
}
//----------------------------------------------------------------------------
public void add(MenuItem NewMI)
{
Items.add(NewMI);
}
//----------------------------------------------------------------------------

}
