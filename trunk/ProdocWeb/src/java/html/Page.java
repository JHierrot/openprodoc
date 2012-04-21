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
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class Page extends Container
{
private String Titulo;
private String Cabecera;
private String StyleRef=null;
private String StyleMenu=null;
private String OnLoadJS=null;
private ArrayList ListCSS=null;
private ArrayList ListJS=null;
protected HttpServletRequest HttpReq;
private boolean Strict=false;

/** Creates a new instance of Page
 * @param Req
 * @param pTitulo
 * @param pCabecera 
 */
public Page(HttpServletRequest Req, String pTitulo, String pCabecera)
{
HttpReq=Req;
Titulo=pTitulo;
Cabecera=pCabecera;
}
//-----------------------------------------------------------------------------------------------

@Override
protected String StartCont()
{
StringBuilder retValue=new StringBuilder(1000);
if (isStrict())
    retValue.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
else
    retValue.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
retValue.append("\n<html><head>\n" + "<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\">").append(Cabecera).append("<title>").append(Titulo).append("</title>\n");
if (ListCSS!=null)
    {for (int i = 0; i<ListCSS.size(); i++)
        {
        String CSS=(String) ListCSS.get(i);
        retValue.append("<link rel=\"stylesheet\"  type=\"text/css\" href=\"css/").append(CSS).append("\" />\n");
        }
    }
if (ListJS!=null)
    {for (int i = 0; i<ListJS.size(); i++)
        {
        String JS=(String) ListJS.get(i);
        retValue.append("<script language=\"JavaScript\" type=\"text/javascript\" src=\"js/").append(JS).append("\"></script>\n");
        }
    }
retValue.append("</head>\n<body").append((OnLoadJS==null)?"":" onLoad=\""+OnLoadJS+";\"").append(">\n");
return retValue.toString();
}
//-----------------------------------------------------------------------------------------------

@Override
protected String EndCont()
{
return ("</body>\n</html>\n");
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param pOnLoadJS
 */
public void AddOnLoad(String pOnLoadJS)
{
OnLoadJS=pOnLoadJS;
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param NewCSS
 */
public void AddCSS(String NewCSS)
{
if (ListCSS==null)
    ListCSS=new ArrayList();
ListCSS.add(getStyle()+NewCSS);
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param NewJS
 */
public void AddJS(String NewJS)
{
if (ListJS==null)
    ListJS=new ArrayList();
ListJS.add(getStyle()+NewJS);
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @return
 */
public DriverGeneric getSession()
{
return (SParent.getSessOPD(HttpReq));
}
//----------------------------------------------------------
/**
 *
 * @param Text
 * @return
 */
public String TT( String Text)
{
return (SParent.TT(HttpReq, Text));
}
//----------------------------------------------------------
/**
 * 
 * @return
 */
public String getStyle()
{
try {
if (getSession()==null)
   return("");
String s=getSession().getPDCust().getStyle();
if (s==null || s.length()==0)
    return("");
else
    return (s+"/");
} catch (PDException ex)
    {
    return("");
    }
}
//----------------------------------------------------------

/**
* @return the Strict
*/
public boolean isStrict()
{
return Strict;
}

/**
* @param Strict the Strict to set
*/
public void setStrict(boolean Strict)
{
this.Strict = Strict;
}
}
