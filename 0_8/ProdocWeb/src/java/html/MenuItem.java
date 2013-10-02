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
public class MenuItem 
{
private String Text;
private String Url;
private String Target;
private boolean Separator=false;
static private MenuItem MISeparator=null;

//----------------------------------------------------------------------------
public MenuItem(String pText, String pUrl)
{
Text=pText;
Url=pUrl;
Target=null;
}
//----------------------------------------------------------------------------
public MenuItem()
{
Separator=true;
}
//----------------------------------------------------------------------------
public MenuItem(String pText, String pUrl, String pTarget)
{
Text=pText;
Url=pUrl;
Target=pTarget;
}
//----------------------------------------------------------------------------
protected String HtmlPropio()
{
if (Separator)
	return("<hr>");
else
	return("<a href=\""+Url+"\" "+((Target==null)?"":"target=\""+Target+"\"")+"onMouseOver=\"stopTime();\" onMouseOut=\"startTime();\">"+Text+"</a><br>\n");
}
//-----------------------------------------------------------------------------------------------
/**
 * @return the MISeparator
 */
public static MenuItem getMISeparator()
{
if (MISeparator==null)
    MISeparator=new MenuItem();
return MISeparator;
}
//-----------------------------------------------------------------------------------------------
}
