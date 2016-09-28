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
import static OpenProdocUI.SParent.TT;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prodoc.DriverGeneric;
import prodoc.ObjPD;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDLog;


/**
 *
 * @author jhierrot
 */
public class ImpElemF extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
String FileName=null;
InputStream FileData=null;
try {
DiskFileItemFactory factory = new DiskFileItemFactory();
factory.setSizeThreshold(1000000);
ServletFileUpload upload = new ServletFileUpload(factory);
boolean isMultipart = ServletFileUpload.isMultipartContent(Req);
List items = upload.parseRequest(Req);
Iterator iter = items.iterator();
while (iter.hasNext())
    {
    FileItem item = (FileItem) iter.next();
    if (!item.isFormField())
        {
        FileName=item.getName();
        FileData=item.getInputStream();
        break;
        }
    }        
DriverGeneric PDSession=SParent.getSessOPD(Req);       
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(FileData);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject;
ObjPD Obj2Build;
int Tot=0;
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    Obj2Build=PDSession.BuildObj(OPDObject);
    Obj2Build.ProcesXMLNode(OPDObject);
    Tot++;
    }
DB.reset();    
out.println(UpFileStatus.SetResultOk(Req, "Total="+Tot));
FileData.close();
} catch (Exception e)
    {
    out.println(UpFileStatus.SetResultKo(Req, e.getLocalizedMessage()));
    throw e;
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
return "ImpElemF Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ImpElemF");
}
//-----------------------------------------------------------------------------------------------
}
