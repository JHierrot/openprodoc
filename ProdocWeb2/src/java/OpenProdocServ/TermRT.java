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
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import prodoc.DriverGeneric;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class TermRT extends SParent
{

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
out.println(GenListDoc(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "TermRT Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListDoc(HttpServletRequest Req) 
{
StringBuilder FolderDocs=new StringBuilder(3000);
//FolderDocs.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows>");
FolderDocs.append("<rows>");
DriverGeneric PDSession=getSessOPD(Req);
String TermId=Req.getParameter("Id");
try {
PDThesaur Term=new PDThesaur(PDSession);
HashSet ListDocs=Term.getListRT(TermId);
for (Iterator iterator = ListDocs.iterator(); iterator.hasNext();)
    {
    String RelId = (String) iterator.next();
    Term.Load(RelId);
    FolderDocs.append("<row id=\"").append(RelId).append("\">");       
    FolderDocs.append("<cell>").append(Term.getName()).append("</cell>");       
    FolderDocs.append("<cell>").append(Term.getDescription()==null?"":Term.getDescription()).append("</cell>");    
    if (Term.getUse()!=null && Term.getUse().length()!=0)
        {
        PDThesaur TermU=new PDThesaur(PDSession); 
        TermU.Load(Term.getUse());
        FolderDocs.append("<cell>").append(TermU.getName()).append("</cell>");  
        }
    else
        FolderDocs.append("<cell></cell>");       
    FolderDocs.append("<cell>").append(Term.getSCN()==null?"":Term.getSCN()).append("</cell>");       
    FolderDocs.append("<cell>").append(Term.getLang()).append("</cell></row>");
    }
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
FolderDocs.append("</rows>");
return(FolderDocs.toString());
}
//-----------------------------------------------------------------------------------------------

}
