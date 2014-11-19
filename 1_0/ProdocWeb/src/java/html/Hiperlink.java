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
public class Hiperlink  extends Container
{

private String Url="";
private boolean UsarSpan=false;
private String Target=null;
private String Title=null;
/** Creates a new instance of Hiperlink */
public Hiperlink(String pUrl)
{
Url=pUrl;    
}
//-----------------------------------------------------------------------------------------------
@Override
protected String StartCont()
{
String retVal="<a href=\""+Url+ "\" ";
if (Target!=null)
    retVal+="target=\""+Target+ "\" ";
if (Title!=null)
    retVal+="title=\""+Title+ "\" ";
if (Html!=null && Html.length()!=0)
    {
    retVal+=" "+Html+" ";
    }
retVal+=">";
String Est=CalcStyle();
if (Est.length()>0)
    {
    UsarSpan=true;
    retVal+="<span "+Est+">";
    }
return(retVal);
}
//-----------------------------------------------------------------------------------------------
@Override
protected String EndCont()
{
String retVal="";
if (UsarSpan)
    retVal+="</span>";
return(retVal+"</a>");    
}
//-----------------------------------------------------------------------------------------------

public String getTarget()
{
return Target;
}
//-----------------------------------------------------------------------------------------------

public void setTarget(String pTarget)
{
Target = pTarget;
}
//-----------------------------------------------------------------------------------------------

public String getTitle()
{
return Title;
}
//-----------------------------------------------------------------------------------------------

public void setTitle(String pTitle)
{
Title = pTitle;
}
//-----------------------------------------------------------------------------------------------

}
