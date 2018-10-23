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
 * author: Joaquin Hierro      2018
 * 
 */
package OpenProdocUI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import prodoc.Attribute;
import prodoc.ContribConf;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDObjDefs;
import prodoc.ProdocFW;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ContribRes extends SParent
{
private static final String HtmlBase="<!DOCTYPE html>\n" +
"<html>" +
    "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
        "<title>OpenProdoc2 Web Contrib Result</title>\n" +
        "<link rel=\"shortcut icon\" href=\"img/OpenProdoc.ico\" type=\"image/x-icon\"/>\n" +       
        "@CSS@"+
    "</head>\n" +
    "<body class=\"CONTRIBBODY\" >\n" +
    "<p>Document Upload</p>"+
    "@RESULT@"+
    "<a class=\"CONTRIBLISTURL\" href=\"ContribList\" >List</a><br>"+   
    "<a href=\"javascript: history.go(-1)\">Retry</a>"+
    "</body>" +
"</html>";


//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws javax.servlet.ServletException
 * @throws java.io.IOException
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
DriverGeneric LocalSess=null;
try {
ContribConf ConfContr=getContribConf(Req);
LocalSess=ProdocFW.getSession(getConnector(), ConfContr.getUser(), ConfContr.getPass()); 
PDFolders F=getContribFolder(Req);
response.setContentType("text/html;charset=UTF-8");
PrintWriter out = response.getWriter(); 
F.setDrv(LocalSess);
out.println(GenHtml(Req, LocalSess, ConfContr, F)); 
ProdocFW.freeSesion(getConnector(), LocalSess);
} catch (Exception Ex)
    {
    if (LocalSess!=null)  
        try {
        ProdocFW.freeSesion(getConnector(), LocalSess);
        } catch (Exception e){}
    ServletException SE= new ServletException(Ex.getLocalizedMessage());
    SE.setStackTrace(Ex.getStackTrace());
    throw SE;
    }
}
//-----------------------------------------------------------------------------------------------
/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ContribRes Servlet";
}
//-----------------------------------------------------------------------------------------------
static synchronized private String GenHtml(HttpServletRequest Req, DriverGeneric LocalSess, ContribConf ConfContrib, PDFolders FoldUser) throws Exception
{
String HtmlFinal;   
String Agent=Req.getHeader("User-Agent");
String DimHtml=ConfContrib.SolveHtmlRes(Agent);
if (DimHtml!=null) 
    {
    HtmlFinal=getHtml(LocalSess, DimHtml);
    }
else
    HtmlFinal=HtmlBase;
if (ConfContrib.getFormContribCSS()!=null)
    {
    if (ConfContrib.getFormContribCSS().startsWith("http"))    
       HtmlFinal=HtmlFinal.replace("@CSS@", "<link rel=\"STYLESHEET\" type=\"text/css\" href=\""+ConfContrib.getFormContribCSS()+"\"/>");
    else
       HtmlFinal=HtmlFinal.replace("@CSS@", GenCSS(LocalSess, ConfContrib.getFormContribCSS()));
    }
else
    HtmlFinal=HtmlFinal.replace("@CSS@", "");
if (!ServletFileUpload.isMultipartContent(Req))
    {
    HtmlFinal=HtmlFinal.replace("@RESULT@", "<div class=\"CONTRIBRESKO\">ERROR:NO File<div>");    
    return(HtmlFinal);
    }
String NameDocT=null;
String FileName=null;
InputStream FileData=null;
HashMap <String, String>ListFields=new HashMap();
DiskFileItemFactory factory = new DiskFileItemFactory();
factory.setSizeThreshold(1000000);
ServletFileUpload upload = new ServletFileUpload(factory);
upload.setFileSizeMax(ConfContrib.getMaxSize());
List items = upload.parseRequest(Req);
Iterator iter = items.iterator();
while (iter.hasNext())
    {
    FileItem item = (FileItem) iter.next();
    if (item.isFormField())
        {
        if (item.getFieldName().equals("CONTRIB_DT"))    
            NameDocT=item.getString();
        else
            {
            ListFields.put(item.getFieldName(), item.getString());
            }
        }
    else 
        {
        FileName=item.getName();
        FileData=item.getInputStream();
        }
    }   
if (!ConfContrib.IsAllowedExt(FileName.substring(FileName.lastIndexOf(".")+1)))
    {
    HtmlFinal=HtmlFinal.replace("@RESULT@", "<div class=\"CONTRIBRESKO\">ERROR:Not Allowed extension<div>");    
    return(HtmlFinal);
    }
PDDocs DocTmp=new PDDocs(LocalSess, NameDocT);
DocTmp.setName(FileName);
DocTmp.setStream(FileData);
Record AttrDef = DocTmp.getRecSum();
for (Map.Entry<String, String> entry : ListFields.entrySet())
    {
    if (AttrDef.getAttr(entry.getKey())!=null);
        AttrDef.getAttr(entry.getKey()).Import(entry.getValue());
    }
DocTmp.assignValues(AttrDef);
DocTmp.setParentId(FoldUser.getPDId());
DocTmp.setACL(FoldUser.getACL());
try {
DocTmp.insert();
HtmlFinal=HtmlFinal.replace("@RESULT@", "<div class=\"CONTRIBRESOK\">OK</div>");
} catch (Exception Ex)
    {
    HtmlFinal=HtmlFinal.replace("@RESULT@", "<div class=\"CONTRIBRESKO\">ERROR:"+Ex.getLocalizedMessage()+"<div>");    
    }
return(HtmlFinal);
}
//-----------------------------------------------------------------------------------------------
private static String GenCSS(DriverGeneric sessOPD, String formSearchCSS)
{
StringBuilder CSS=new StringBuilder();
CSS.append("<style>\n");
try {
PDDocs DocCSS=new PDDocs(sessOPD);
DocCSS.setPDId(formSearchCSS);
ByteArrayOutputStream OutBytes = new ByteArrayOutputStream();
DocCSS.getStream(OutBytes);
CSS.append(OutBytes.toString());
    } catch (Exception Ex)
        {        
        }
CSS.append("</style>\n");
return(CSS.toString());
}
//-----------------------------------------------------------------------------------------------
}
