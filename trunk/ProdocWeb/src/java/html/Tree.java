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

import java.util.*;
import javax.servlet.http.*;

/**
 *
 * @author jhierrot
 */
public class Tree extends Element
{

private ArrayList ListRows=new ArrayList();
private String ShowContent="RefreshDocs";
private String TargetFrame="MainFrame";
private String Order="ExpandContract";
private String ElemType="FoldId";

/** Creates a new instance of Tree */
public Tree()
{
}
/** Creates a new instance of Tree */
public Tree(String pShowContent, String pTargetFrame, String pOrder, String pElemType)
{
ShowContent=pShowContent;
TargetFrame=pTargetFrame;  
Order=pOrder;
ElemType=pElemType;
}
//----------------------------------------------------------------------------
public String ToHtml(HttpSession Sess)
{
String CadHtml=" <ul class=\"Showed\">";
for (int i = 0; i < ListRows.size(); i++)
    {
    CadHtml+=((Branch)ListRows.get(i)).HtmlPropio(ShowContent, TargetFrame, Order, ElemType);
    }
return(CadHtml+"</ul>");
}
//----------------------------------------------------------------------------
public void add(Branch NewBr)
{
ListRows.add(NewBr);
}
//----------------------------------------------------------------------------

}
