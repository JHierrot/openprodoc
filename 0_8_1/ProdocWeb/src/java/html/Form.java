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
public class Form extends Container
{
private String Url="";
private String Titulo="";
private boolean ModoEnvioMultipart=false;
//-----------------------------------------------------------------------------------------------

/** Creates a new instance of Form */
public Form(String pUrl, String pTitulo)
{
setUrl(pUrl);
Titulo=pTitulo;    
}
//-----------------------------------------------------------------------------------------------
protected String StartCont()
{
String retValue="<form method=\"post\" action=\""+getUrl()+ "\" name=\""+Titulo+"\"";
if (getCSSClass()!=null)
    retValue+=" class=\""+getCSSClass()+"\" ";
else if (f!=null)
    retValue+=" style=\""+f.ToHtml()+"\" "; 
if (ModoEnvioMultipart)
    retValue+=" enctype=\"multipart/form-data\" ";
return(retValue+" >\n");
}
//-----------------------------------------------------------------------------------------------
protected String EndCont()
{
return("</form>\n");    
}

//-----------------------------------------------------------------------------------------------    
public String getUrl()
{
return Url;
}

//-----------------------------------------------------------------------------------------------    
public void setUrl(String pUrl)
{
this.Url = pUrl;
}
//-----------------------------------------------------------------------------------------------    

public boolean isModoEnvioMultipart()
{
return ModoEnvioMultipart;
}
//-----------------------------------------------------------------------------------------------    

public void setModoEnvioMultipart(boolean pModoEnvio)
{
ModoEnvioMultipart = pModoEnvio;
}
//-----------------------------------------------------------------------------------------------    
}
