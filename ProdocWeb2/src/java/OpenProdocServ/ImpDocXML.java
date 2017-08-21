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
 * author: Joaquin Hierro      2017
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.InputStream;
import java.io.PrintWriter;
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
import prodoc.PDFolders;


/**
 *
 * @author jhierrot
 */
public class ImpDocXML extends SParent
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
String CurrFold=PDFolders.ROOTFOLDER;
while (iter.hasNext())
    {
    FileItem item = (FileItem) iter.next();
    if (!item.isFormField())
        {
        FileName=item.getName();
        FileData=item.getInputStream();
        }
    else if (item.getName().equalsIgnoreCase("CurrFold"))
        {
        CurrFold=item.getString();
        }
    }        
DriverGeneric PDSession=SParent.getSessOPD(Req);  
int Tot=PDSession.ProcessXML(FileData, CurrFold);
FileData.close();
out.println(UpFileStatus.SetResultOk(Req, "Total="+Tot));
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
return "ImpDocXML Servlet";
}
//-----------------------------------------------------------------------------------------------
}
