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
 * author: Joaquin Hierro      2019
 * 
 */

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ListVers extends SParent
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
return "ListVers Servlet";
}
//-----------------------------------------------------------------------------------------------
private String GenListDoc(HttpServletRequest Req) 
{
StringBuilder ListProducts=new StringBuilder(5000);
ListProducts.append("<rows>");
DriverGeneric PDSession=getSessOPD(Req);
Attribute AttrD;
try {
PDFolders Fold=new PDFolders(PDSession, getProductsVersType(Req));
PDFolders FoldTemp=new PDFolders(PDSession, getProductsVersType(Req));
PDThesaur TmpTerm=new PDThesaur(PDSession);
String LicTemp;
String IdProd=Req.getParameter("IdProd");
HashSet<String> ListVers = Fold.getListDirectDescendList(IdProd);
Calendar NextMonth = Calendar.getInstance(); 
NextMonth.add(Calendar.MONTH, 1);
Date Now=new Date();
for (Iterator<String> iterator = ListVers.iterator(); iterator.hasNext();)
    { // Title,DateInit,DateSup,DateSupExt,Notes
    FoldTemp.LoadFull(iterator.next());
    if (FoldTemp.getFolderType().equals(getProductsVersType(Req)))
        {
        String Icon="";
        Record Rec=FoldTemp.getRecSum();
        ListProducts.append("<row id=\"").append(FoldTemp.getPDId()).append("\">");       
        AttrD=Rec.getAttr(PDFolders.fTITLE);
        ListProducts.append("<cell>").append(AttrD.Export()).append("</cell>");       
        AttrD=Rec.getAttr("DateInit");
        ListProducts.append("<cell>").append(AttrD.Export()).append("</cell>");       
        AttrD=Rec.getAttr("DateSup");
//        if (AttrD.getValue()!=null && Now.after((Date)AttrD.getValue()))
//           Icon="img/02_Alert.gif";
        if (AttrD.getValue()!=null && NextMonth.getTime().after((Date)AttrD.getValue()))
           Icon="img/Alert.png";
        ListProducts.append("<cell>").append(AttrD.Export()).append("</cell>");
        AttrD=Rec.getAttr("DateSupExt");
        if (AttrD.getValue()!=null && Now.after((Date)AttrD.getValue()))
           Icon="img/Critic.png";
        ListProducts.append("<cell>").append(AttrD.Export()).append("</cell>");
        ListProducts.append("<cell>").append(Icon).append("</cell>");        
        if ((String)Rec.getAttr("License").getValue()!=null)
            {
            TmpTerm.Load((String)Rec.getAttr("License").getValue());
            LicTemp=TmpTerm.getName();
            }
        else
            LicTemp="";
        ListProducts.append("<cell>").append(LicTemp).append("</cell>");
        AttrD=Rec.getAttr("Notes");
        ListProducts.append("<cell>").append(AttrD.Export()).append("</cell></row>");
        }
    }
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    }
ListProducts.append("</rows>");
return(ListProducts.toString());
}
//-----------------------------------------------------------------------------------------------
}
