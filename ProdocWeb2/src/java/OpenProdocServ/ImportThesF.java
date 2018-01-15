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
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.getSessOPD;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.*;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 *
 * @author jhierrot
 */
public class ImportThesF extends SParent
{
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
String FileName=null;
InputStream FileData=null;
HashMap <String, String>ListFields=new HashMap();
try {
DiskFileItemFactory factory = new DiskFileItemFactory();
ServletFileUpload upload = new ServletFileUpload(factory);
boolean isMultipart = ServletFileUpload.isMultipartContent(Req);
List items = upload.parseRequest(Req);
Iterator iter = items.iterator();
while (iter.hasNext())
    {
    FileItem item = (FileItem) iter.next();
    if (item.isFormField())
        ListFields.put(item.getFieldName(), item.getString());
    else 
        {
        FileName=item.getName();
        FileData=item.getInputStream();
        }
    }   
if (!isMultipart || FileData==null)
    {
    out.println("KO");
    }
else
    { 
    ListFields=GetDat(Req);  
    String NewThesId=ListFields.get("ThesNum");
    String ThesName=ListFields.get("ThesName");
    String RootText=ListFields.get("RootText");
    String MainLanguage=ListFields.get("MainLanguage");
    String SubByLang=ListFields.get("SubByLang");
    String Transact=ListFields.get("Transact");
    String RetainCodes=ListFields.get("RetainCodes");
    DriverGeneric PDSession = getSessOPD(Req); 
    PDThesaur Thes=new PDThesaur(PDSession);
    Thes.Import(ThesName, NewThesId, FileData, MainLanguage, RootText, SubByLang.equals("1"), Transact.equals("1"), RetainCodes.equals("1"));
    FileData.close();
    out.println(UpFileStatus.SetResultOk(Req, Thes.getImportReport()));
    }
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
return "ImportThesF Servlet";
}
//-----------------------------------------------------------------------------------------------
}
