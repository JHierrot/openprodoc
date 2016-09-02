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
import static OpenProdocUI.SParent.GetDat;
import static OpenProdocUI.SParent.getSessOPD;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.*;
import prodoc.DriverGeneric;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import prodoc.Attribute;
import prodoc.PDDocs;
import prodoc.PDMimeType;
import prodoc.Record;


/**
 *
 * @author jhierrot
 */
public class ImportDocF extends SParent
{
final static private String List=PDDocs.fPARENTID+"/"+PDDocs.fPDID
                    +"/"+PDDocs.fPDAUTOR+"/"+PDDocs.fPDDATE
                    +"/"+PDDocs.fLOCKEDBY+"/"+PDDocs.fVERSION+"/"+PDDocs.fPURGEDATE
                    +"/"+PDDocs.fREPOSIT+"/"+PDDocs.fSTATUS;
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
HashMap <String, String>ListFields=new HashMap();
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
    PDDocs Doc;
    DriverGeneric PDSession = getSessOPD(Req); 
    String DType=(String) ListFields.get(PDDocs.fDOCTYPE);
    if (DType==null)
        Doc = new PDDocs(PDSession);
    else
        Doc = new PDDocs(PDSession, DType);
    Record Rec=Doc.getRecSum();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (!List.contains(Attr.getName()))
            {
            String Val=(String) ListFields.get(Attr.getName());
            if (Attr.getType()==Attribute.tBOOLEAN)
                {
                if(Val == null || Val.length()==0 || Val.equals("0"))
                    Attr.setValue(false);
                else
                    Attr.setValue(true);
                }
            else if(Val != null)
                {
                SParent.FillAttr(Req, Attr, Val, false);
                }
            }
        Attr=Rec.nextAttr();
        }
    Doc.assignValues(Rec);
    Doc.setParentId(ListFields.get("CurrFold"));
    Doc.setName(FileName);
    PDMimeType mt=new PDMimeType(PDSession);
    Doc.setMimeType(mt.SolveName(FileName));
    Doc.setStream(FileData);
    Doc.insert();
    out.println(UpFileStatus.SetResultOk(Req, ""));
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
return "ImportDocF Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ImportDocF");
}
//-----------------------------------------------------------------------------------------------
}
