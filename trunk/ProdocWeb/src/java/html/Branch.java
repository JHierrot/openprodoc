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
public class Branch extends Element
{
private String Name="";
private String Id="";
private boolean SelectedBr=false;
private String Style="";
//----------------------------------------------------------------------------
public Branch(String pName, String pId)
{
Name=pName;
Id=pId;
}
//----------------------------------------------------------------------------
public Branch(String pName, String pId, boolean Selected, String pStyle)
{
Name=pName;
Id=pId;
SelectedBr=Selected;
Style=pStyle;
}
//----------------------------------------------------------------------------
protected String HtmlPropio(String Url, String Target, String Order, String ElemType)
{
StringBuilder Cad=new StringBuilder("<li id=\"");
Cad.append(Id).append("\"><img src=\"img/"+Style+"unkown.gif\" alt=\"?\" onclick=\""+Order+"('").append(Id).append("');\" /> <a href=\"");
Cad.append(Url).append("?"+ElemType+"=").append(Id).append("\" ").append((Target==null)?"":"target=\""+Target+"\"").append(">").append(Name).append("</a> </li>");       
return(Cad.toString());
}
//----------------------------------------------------------------------------

}
