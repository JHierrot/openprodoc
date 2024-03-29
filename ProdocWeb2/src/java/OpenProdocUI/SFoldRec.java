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

package OpenProdocUI;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDThesaur;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class SFoldRec extends SParent
{

private static final String ListInternalFields="/"+PDFolders.fACL+"/"+PDFolders.fFOLDTYPE+"/"+PDFolders.fPARENTID+"/"+PDFolders.fPDID+"/"+PDFolders.fTITLE+"/"+PDFolders.fPDAUTOR+"/"+PDFolders.fPDDATE+"/";

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
DriverGeneric PDSession=getSessOPD(Req);
String FoldId=Req.getParameter("FoldId");
setActFoldId(Req, FoldId);
PDFolders Fold=new PDFolders(PDSession);
Fold.LoadFull(FoldId);
out.println(CreateForm(Req, Fold));
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "SFoldRec Servlet";
}
//-----------------------------------------------------------------------------------------------
private String CreateForm(HttpServletRequest Req, PDFolders FoldAct) throws PDException
{  
StringBuilder Html=new StringBuilder("<div class=\"OPDForm\"><b>"+FoldAct.getTitle()+"</b> ("+FoldAct.getFolderType() +") : "+FormatDate(Req, FoldAct.getPDDate())+ "<hr> ACL="+FoldAct.getACL());
Record Rec=FoldAct.getRecSum();
Rec.initList();
Attribute Attr=Rec.nextAttr();
while (Attr!=null)
    {
    if (!ListInternalFields.contains("/"+Attr.getName()+"/"))
        {
        Html.append("<br><b>").append(TT(Req, Attr.getUserName())).append("= </b>");
        if (Attr.getType()==Attribute.tTHES)
            {
            if (Attr.isMultivalued())
                {
                TreeSet valuesList = Attr.getValuesList();
                String Sum="";
                for (Iterator iterator = valuesList.iterator(); iterator.hasNext();)
                    {
                    String Val = (String)iterator.next();
                    PDThesaur Term=new PDThesaur(FoldAct.getDrv());
                    if (Val!=null)
                        {
                        Term.Load(Val);
                        if (Sum.length()!=0)
                            Sum+="|";
                        Sum+=Term.getName();
                        }  
                    }
                Html.append(Sum);
                }
            else
                {
                PDThesaur Term=new PDThesaur(FoldAct.getDrv());
                if (Attr.getValue()!=null)
                    {
                    Term.Load((String)Attr.getValue());
                    Html.append(Term.getName());
                    }
                }
            }
        else if (Attr.getType()==Attribute.tSTRING && Attr.getLongStr()>1000)
            Html.append(Attr.Export());
        else
            Html.append(Attr.ExportXML());
        }
    Attr=Rec.nextAttr();
    }
Html.append("</div>");
return(Html.toString());


}
//-----------------------------------------------------------------------------------------------
}
