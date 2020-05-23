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
 * author: Joaquin Hierro      2020
 * 
 */
package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;

/**
 *
 * @author jhierrot
 */
public class DependTree extends SParent
{
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
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
out.println(GenDependTree(Req));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "DependTree Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenDependTree(HttpServletRequest Req) 
{
StringBuilder DepTree=new StringBuilder(5000);
DepTree.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n").append("<tree id=\"0\">");
DriverGeneric PDSession=getSessOPD(Req);
String IdVersProdSec=Req.getParameter("IdVers");
try {
PDFolders F=new PDFolders(PDSession);
F.LoadFull(IdVersProdSec);
DepTree.append("<item id=\"").append(F.getPDId()).append("\" text=\"").append(F.getTitle()).append("\" open=\"1\">");
TreeSet<String> ListDep = F.getRecSum().getAttr(DEPENDENCIES).getValuesList();
for (Iterator<String> iterator = ListDep.iterator(); iterator.hasNext();)
    {
    String Rel = iterator.next();
    DepTree.append(SubDependTree(PDSession, Rel));
    }
DepTree.append("</item>");
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
DepTree.append("</tree>");
return(DepTree.toString());
}
//-----------------------------------------------------------------------------------------------

private StringBuilder SubDependTree(DriverGeneric PDSession, String IdRel) throws PDException
{
StringBuilder DepTree=new StringBuilder(5000);
PDFolders F=new PDFolders(PDSession);
PDThesaur TDep=new PDThesaur(PDSession);
PDThesaur TLic=new PDThesaur(PDSession);
String[] split = IdRel.split(REL_SEP);
String ProdVersPrim=split[0];
String RelationSecPrim=split[1];
F.LoadFull(ProdVersPrim);
TDep.Load(RelationSecPrim);
String LicType;
try {
TLic.Load((String)F.getRecSum().getAttr("License").getValue());
LicType=TLic.getName();
} catch (Exception e)
    {
    LicType="";
    }
String EOS;
Attribute AttrD=F.getRecSum().getAttr("DateSup");
Attribute AttrDES=F.getRecSum().getAttr("DateSupExt");
try {
EOS=formatterDate.format((Date)AttrD.getValue());
} catch (Exception e)
    {
    EOS="";
    }
//String IconsAlert="<icons file=\"icon_fileAlert\" folder_opened=\"icon_openedAlert\" folder_closed=\"icon_closedAlert\"/>";
//String IconsCritic="<icons file=\"icon_fileCritic\" folder_opened=\"icon_openedCritic\" folder_closed=\"icon_closedCritic\"/>";
//String Icons="";
String ColorStart="";
String ColorEnd="";
Calendar NextMonth = Calendar.getInstance(); 
NextMonth.add(Calendar.MONTH, 1);
Date Now=new Date();
if (AttrD.getValue()!=null && NextMonth.getTime().after((Date)AttrD.getValue()))
    {
    ColorStart="&lt;span style='color: #ff9900'&gt;";
    ColorEnd="&lt;/span&gt;";
    }
if (AttrDES.getValue()!=null && Now.after((Date)AttrDES.getValue()))
    {
    ColorStart="&lt;span style='color: #ff0000'&gt;";
    ColorEnd="&lt;/span&gt;";
    }
DepTree.append("<item id=\"").append(IdRel).append("\" text=\"").append(ColorStart).append(TDep.getName()).append(ColorEnd).append(" --> ").append(F.getTitle()).append("   [ EOS:").append(EOS).append(" / Lic: ").append(LicType).append(" ]").append("\" open=\"1\">");
TreeSet<String> ListDep = F.getRecSum().getAttr(DEPENDENCIES).getValuesList();
for (Iterator<String> iterator = ListDep.iterator(); iterator.hasNext();)
    {
    String Rel = iterator.next();
    DepTree.append(SubDependTree(PDSession, Rel));
    }
DepTree.append("</item>");
return(DepTree);
}
//-----------------------------------------------------------------------------------------------
}
