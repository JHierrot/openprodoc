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
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ObjPD;
import prodoc.PDACL;
import prodoc.PDAuthenticators;
import prodoc.PDCustomization;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDGroups;
import prodoc.PDMimeType;
import prodoc.PDObjDefs;
import prodoc.PDRepository;
import prodoc.PDRoles;
import prodoc.PDUser;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ListElem extends SParent
{

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
DriverGeneric PDSession=SParent.getSessOPD(Req);
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(3000);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows><head>");
try {
Record Rec;
ObjPD Obj=null;
String ElemType=Req.getParameter("TE");
String Filter=Req.getParameter("F");
if (Filter==null)
    Filter="";
if (ElemType.equals("ACL"))
    Obj=new PDACL(PDSession);
else if (ElemType.equals("Groups"))
    Obj=new PDGroups(PDSession);
else if (ElemType.equals("Users"))
    Obj=new PDUser(PDSession);
else if (ElemType.equals("Roles"))
    Obj=new PDRoles(PDSession);
else if (ElemType.equals("MimeTypes"))
    Obj=new PDMimeType(PDSession);
else if (ElemType.equals("Repositories"))
    Obj=new PDRepository(PDSession);
else if (ElemType.equals("ObjDef"))
    Obj=new PDObjDefs(PDSession);
else if (ElemType.equals("Authenticators"))
    Obj=new PDAuthenticators(PDSession);
else if (ElemType.equals("Customizations"))
    Obj=new PDCustomization(PDSession);
Rec=Obj.getRecord();
StringBuilder Head=new StringBuilder(300);
StringBuilder Edit=new StringBuilder(300);
StringBuilder Type=new StringBuilder(300);
Rec.initList();
Attribute Attr=Rec.nextAttr();
int Count=1;
while (Attr!=null)
    {
    if (!Attr.isMultivalued()) 
        {
        Resp.append("<column width=\""+(Count==Rec.NumAttr()?"*":600/Rec.NumAttr())+"\" type=\"ro\" align=\"left\" sort=\"str\">"+TT(Req,Attr.getUserName())+"</column>");
        }
    Count++;
    Attr=Rec.nextAttr();
    }
Resp.append("</head>");
Cursor ListObj=Obj.SearchLike(Filter);
Record NextObj=PDSession.NextRec(ListObj);
while (NextObj!=null)
    {
    String Id=(String)NextObj.getAttr(PDACL.fNAME).getValue();
    Resp.append(SParent.GenRowGrid(Req, Id, NextObj, true));    
    NextObj=PDSession.NextRec(ListObj);
    }          
Resp.append("</rows>");
} catch (PDException ex)
    {
    Resp.append("<LV>Error</LV></row>");
    }
out.println( Resp );   
out.close();
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ListElem Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ListElem");
}
//-----------------------------------------------------------------------------------------------
}
/***
 * 
 * http://dhtmlx.com/docs/products/dhtmlxGrid/samples/12_initialization_loading/01_grid_config_xml.html
 * 
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<rows>\n" +
"    <head>\n" +
"        <column width=\"*\" type=\"dyn\" align=\"right\" sort=\"int\">Sales</column>\n" +
"        <column width=\"*\" type=\"ed\" align=\"left\" sort=\"str\">Book Title</column>\n" +
"        <column width=\"*\" type=\"ed\" align=\"left\" sort=\"str\">Author</column>\n" +
"        <column width=\"*\" type=\"price\" align=\"right\" sort=\"int\">Price</column>\n" +
"        <column width=\"*\" type=\"ch\" align=\"center\" sort=\"str\">In Store</column>\n" +
"        <column width=\"*\" type=\"co\" align=\"left\" sort=\"str\">Shipping\n" +
"            <option value=\"1\">1</option>\n" +
"            <option value=\"2\">2</option>\n" +
"            <option value=\"3\">3</option>\n" +
"            <option value=\"4\">10</option>\n" +
"            <option value=\"5\">20</option>\n" +
"            <option value=\"6\">30</option>\n" +
"        </column>\n" +
"        <column width=\"*\" type=\"ra\" align=\"center\" sort=\"str\">Bestseller</column>\n" +
"        <column width=\"*\" type=\"ro\" align=\"center\" sort=\"str\">Date of Publication</column>\n" +
"        <settings>\n" +
"            <colwidth>px</colwidth>\n" +
"        </settings>\n" +
"    </head>\n" +
" 	<row id=\"1\">\n" +
"		<cell>-1500</cell>\n" +
"		<cell>A Time to Kill</cell>\n" +
"		<cell>John Grisham</cell>\n" +
"		<cell>12.99</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>24</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>05/01/1998</cell>\n" +
"	</row>\n" +
"	<row id=\"2\">\n" +
"		<cell>1000</cell>\n" +
"		<cell>Blood and Smoke</cell>\n" +
"		<cell>Stephen King</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>24</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>01/01/2000</cell>\n" +
"	</row>\n" +
"	<row id=\"3\" selected=\"1\">\n" +
"		<cell>-200</cell>\n" +
"		<cell>The Rainmaker</cell>\n" +
"		<cell>John Grisham</cell>\n" +
"		<cell>7.99</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>48</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>12/01/2001</cell>\n" +
"	</row>\n" +
"	<row id=\"4\">\n" +
"		<cell>350</cell>\n" +
"		<cell>The Green Mile</cell>\n" +
"		<cell>Stephen King</cell>\n" +
"		<cell>11.10</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>24</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>01/01/1992</cell>\n" +
"	</row>\n" +
"	<row id=\"5\">\n" +
"		<cell>700</cell>\n" +
"		<cell>Misery</cell>\n" +
"		<cell>Stephen King</cell>\n" +
"		<cell>7.70</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>na</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>01/01/2003</cell>\n" +
"	</row>\n" +
"	<row id=\"6\">\n" +
"		<cell>-1200</cell>\n" +
"		<cell>The Dark Half</cell>\n" +
"		<cell>Stephen King</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>48</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>10/30/1999</cell>\n" +
"	</row>\n" +
"	<row id=\"7\">\n" +
"		<cell>1500</cell>\n" +
"		<cell>The Partner</cell>\n" +
"		<cell>John Grisham</cell>\n" +
"		<cell>12.99</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>48</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>01/01/2005</cell>\n" +
"	</row>\n" +
"	<row id=\"8\">\n" +
"		<cell>500</cell>\n" +
"		<cell>It</cell>\n" +
"		<cell>Stephen King</cell>\n" +
"		<cell>9.70</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>na</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>10/15/2001</cell>\n" +
"	</row>\n" +
"	<row id=\"9\">\n" +
"		<cell>400</cell>\n" +
"		<cell>Cousin Bette</cell>\n" +
"		<cell>Honore de Balzac</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>12/01/1991</cell>\n" +
"	</row>\n" +
"	<row id=\"10\">\n" +
"		<cell>-100</cell>\n" +
"		<cell>Boris Godunov</cell>\n" +
"		<cell>Alexandr Pushkin</cell>\n" +
"		<cell>7.15</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>01/01/1999</cell>\n" +
"	</row>\n" +
"	<row id=\"11\">\n" +
"		<cell>-150</cell>\n" +
"		<cell>Alice in Wonderland</cell>\n" +
"		<cell>Lewis Carroll</cell>\n" +
"		<cell>4.15</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>1</cell>\n" +
"		<cell>0</cell>\n" +
"		<cell>01/01/1999</cell>\n" +
"	</row>\n" +
"</rows>");
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
try {
Record Rec;
ObjPD Obj=null;
String ElemType=Req.getParameter("TE");
String Filter=Req.getParameter("F");
if (Filter==null)
    Filter="";
if (ElemType.equals("ACL"))
    Obj=new PDACL(PDSession);
else if (ElemType.equals("Groups"))
    Obj=new PDGroups(PDSession);
else if (ElemType.equals("Users"))
    Obj=new PDUser(PDSession);
else if (ElemType.equals("Roles"))
    Obj=new PDRoles(PDSession);
else if (ElemType.equals("MimeTypes"))
    Obj=new PDMimeType(PDSession);
else if (ElemType.equals("Repositories"))
    Obj=new PDRepository(PDSession);
else if (ElemType.equals("ObjDef"))
    Obj=new PDObjDefs(PDSession);
else if (ElemType.equals("Authenticators"))
    Obj=new PDAuthenticators(PDSession);
else if (ElemType.equals("Customizations"))
    Obj=new PDCustomization(PDSession);
Rec=Obj.getRecord();
StringBuilder Head=new StringBuilder(300);
StringBuilder Edit=new StringBuilder(300);
StringBuilder Type=new StringBuilder(300);
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    if (!Attr.isMultivalued()) 
        {
        Head.append(TT(Req,Attr.getUserName())).append(",");
        Edit.append("ro,");
        Type.append("str,");
        }
    Attr=Rec.nextAttr();
    }
Head.deleteCharAt(Head.length()-1);
Edit.deleteCharAt(Edit.length()-1);
Type.deleteCharAt(Type.length()-1);
StringBuilder VersionsData=new StringBuilder(1000);
VersionsData.append("data={ rows:[");
Cursor ListObj=Obj.SearchLike(Filter);
Record NextObj=PDSession.NextRec(ListObj);
while (NextObj!=null)
    {
    String Id=(String)NextObj.getAttr(PDACL.fNAME).getValue();
    VersionsData.append(SParent.GenRowGrid(Req, Id, NextObj, false));    
    NextObj=PDSession.NextRec(ListObj);
    if (NextObj!=null)
        VersionsData.append(",");
    }
VersionsData.append("] };");
Resp.append("<LV>").append(Head).append("</LV>");       
Resp.append("<LV>").append(Edit).append("</LV>");       
Resp.append("<LV>").append(Type).append("</LV>");           
Resp.append("<LV>").append(VersionsData).append("</LV></row>");
} catch (PDException ex)
    {
    Resp.append("<LV>Error</LV></row>");
    }
out.println( Resp ); 
 * ***/