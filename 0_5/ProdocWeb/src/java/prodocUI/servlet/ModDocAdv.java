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

package prodocUI.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.Record;
import prodocUI.forms.FMantDocAdv;

/**
 *
 * @author jhierrot
 */
public class ModDocAdv extends SParent
{

static private String List=PDDocs.fDOCTYPE+"/"+PDDocs.fPARENTID+"/"+PDDocs.fPDID
                        +"/"+PDDocs.fPDAUTOR+"/"+PDDocs.fPDDATE
                        +"/"+PDDocs.fLOCKEDBY+"/"+PDDocs.fVERSION+"/"+PDDocs.fPURGEDATE
                        +"/"+PDDocs.fREPOSIT+"/"+PDDocs.fSTATUS;

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
if (!Reading(Req))
    {
    DriverGeneric PDSession=SParent.getSessOPD(Req)  ;
    PDDocs F = new PDDocs(PDSession);
    F.LoadFull(getActDocId(Req));
    Record Rec=F.getRecSum();
    FMantDocAdv f=new FMantDocAdv(Req, FMantDocAdv.EDIMOD, Rec, getUrlServlet());
    out.println(f.ToHtml(Req.getSession()));
    return;
    }
else
    {
    try {
    ModDoc(Req);
    GenListForm(Req, out, LAST_FORM, null, null);
//    FListDocs fm=new FListDocs(Req, getActFolderId(Req));
//    out.println(fm.ToHtml(Req.getSession()));
    } catch (PDException ex)
        {
        ShowMessage( Req,  out, SParent.TT(Req, ex.getLocalizedMessage()));
        }
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
return "ModDocAdv Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("ModDocAdv");
}
//-----------------------------------------------------------------------------------------------

private boolean ModDoc(HttpServletRequest Req) throws PDException, FileUploadException, IOException
{
PDDocs Doc;
Record Rec;
String FileName=null;
InputStream FileData=null;
HashMap ListFields=new HashMap();
DiskFileItemFactory factory = new DiskFileItemFactory();
factory.setSizeThreshold(1000000);
ServletFileUpload upload = new ServletFileUpload(factory);
boolean isMultipart = ServletFileUpload.isMultipartContent(Req);
if (isMultipart)
    {
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
    String Acept=(String) ListFields.get("BOk");
    if (Acept==null || Acept.length()==0)
        return(false);
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    String DType=(String) ListFields.get(PDDocs.fDOCTYPE);
    Doc = new PDDocs(PDSession);
    Doc.LoadFull(getActDocId(Req));
    Rec=Doc.getRecSum();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (!List.contains(Attr.getName()))
            {
            String Val=(String) ListFields.get(Attr.getName());
            if (Attr.getType()==Attribute.tBOOLEAN)
                {
                if(Val == null)
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
    }
else
    {
    String Acept=Req.getParameter("BOk");
    if (Acept==null || Acept.length()==0)
        return(false);
    DriverGeneric PDSession=SParent.getSessOPD(Req);
    Doc = new PDDocs(PDSession);
    Doc.LoadFull(getActDocId(Req));
    Rec=Doc.getRecSum();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (!List.contains(Attr.getName()))
            {
            String Val=Req.getParameter(Attr.getName());
            if (Attr.getType()==Attribute.tBOOLEAN)
                {
                if(Val == null)
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
    }
Doc.assignValues(Rec);
Doc.setParentId(getActFolderId(Req));
if (FileName!=null)
    Doc.setName(FileName);
if (FileData!=null)
    Doc.setStream(FileData);
Doc.update();
return(true);
}
//-----------------------------------------------------------------------------------------------
}
