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
package APIRest.beans;

import APIRest.APICore;
import com.google.gson.Gson;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import prodoc.Attribute;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDMimeType;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class DocB
{

private String Id;
    
private String Title;
private String ACL;
private String Idparent;
private String Pathparent;
private String Type;
private String VerLabel;
private String MimeType;
private String LockedBy;
private String DocDate;
private String PDDate;
private String PDAuthor;

private ArrayList<Attr> ListAttr=new ArrayList();

static private TreeSet<String> IntFields=null;
//--------------------------------------------------------------------------    
public static DocB CreateDoc(String json)
{
Gson g = new Gson();
return(g.fromJson(json, DocB.class));    
}
//--------------------------------------------------------------------------    
public String getJSON()
{
Gson g = new Gson();
return(g.toJson(this));    
}
//--------------------------------------------------------------------------    
/**
* @return the Id
*/
public String getId()
{
return Id;
}
//--------------------------------------------------------------------------    
/**
* @param Id the Id to set
*/
public void setId(String Id)
{
this.Id = Id;
}
//--------------------------------------------------------------------------    
/**
* @return the Title
*/
public String getTitle()
{
return Title;
}
//--------------------------------------------------------------------------    
/**
* @param Title the Title to set
*/
public void setTitle(String Title)
{
this.Title = Title;
}
//--------------------------------------------------------------------------    
/**
* @return the ACL
*/
public String getACL()
{
return ACL;
}
//--------------------------------------------------------------------------    
/**
* @param ACL the ACL to set
*/
public void setACL(String ACL)
{
this.ACL = ACL;
}
//--------------------------------------------------------------------------    
/**
* @return the Idparent
*/
public String getIdparent()
{
return Idparent;
}
//--------------------------------------------------------------------------    
/**
* @param Idparent the Idparent to set
*/
public void setIdparent(String Idparent)
{
this.Idparent = Idparent;
}
//--------------------------------------------------------------------------    
/**
* @return the Type
*/
public String getType()
{
return Type;
}
//--------------------------------------------------------------------------    
/**
* @param Type the Type to set
*/
public void setType(String Type)
{
this.Type = Type;
}
//--------------------------------------------------------------------------    
/**
* @return the ListAttr
*/
public ArrayList<Attr> getListAttr()
{
return ListAttr;
}
//--------------------------------------------------------------------------    
/**
* @param ListAttr the ListAttr to set
*/
public void setListAttr(ArrayList<Attr> ListAttr)
{
this.ListAttr = ListAttr;
}
//--------------------------------------------------------------------------    
/**
* @return the Pathparent
*/
public String getPathparent()
{
return Pathparent;
}
//--------------------------------------------------------------------------    
/**
* @param Pathparent the Pathparent to set
*/
public void setPathparent(String Pathparent)
{
this.Pathparent = Pathparent;
}
//--------------------------------------------------------------------------    
/**
 *
 * @param Doc
 * @return
 * @throws PDException
 */
public static DocB CreateDoc(PDDocs Doc) throws PDException
{
DocB D=new DocB();
D.setId(Doc.getPDId());
D.setACL(Doc.getACL());
D.setIdparent(Doc.getParentId());
D.setType(Doc.getDocType());
D.setTitle(Doc.getTitle());
PDMimeType M=new PDMimeType(Doc.getDrv());
D.setMimeType(M.Ext2Mime(Doc.getMimeType()));
D.setDocDate(Doc.getDocDate());
D.setLockedBy(Doc.getLockedBy());
D.setPDAuthor(Doc.getPDAutor());
D.setPDDate(Doc.getPDDate());
D.setVerLabel(Doc.getVersion());
ArrayList<Attr> LA=new ArrayList();
Record recSum = Doc.getRecSum();
recSum.initList();
Attribute Attri;
for (int NumAttr = 0; NumAttr < recSum.NumAttr(); NumAttr++)
    {
    Attri=recSum.nextAttr();
    if (!IsInternalField(Attri.getName()))
        {
        Attr AttrD=new Attr(Attri);
        LA.add(AttrD);
        }
    }
D.setListAttr(LA);
return(D);
}
//--------------------------------------------------------------------------    
public void Assign(PDDocs Doc) throws Exception
{
if (getACL()!=null && getACL().length()!=0)
    Doc.setACL(getACL());
if (getTitle()!=null && getTitle().length()!=0)
    Doc.setTitle(getTitle());
Doc.setParentId(getIdparent());
if (getMimeType()!=null && getMimeType().length()!=0)
    {
    PDMimeType M=new PDMimeType(Doc.getDrv());
    Doc.setMimeType(M.Mime2Ext(getMimeType()));
    }
Doc.setDocDate(getDocDate());
ArrayList<Attr> listAttr = getListAttr();
Record recSum = Doc.getRecSum();
for (int i = 0; i < listAttr.size(); i++)
    {
    Attr At = listAttr.get(i);
    if (recSum.getAttr(At.getName()).isMultivalued())
        {
        ArrayList<String> values = At.getValues();
        for (int j = 0; j < values.size(); j++)
            recSum.getAttr(At.getName()).AddValue(values.get(j));
        }
    else
        recSum.getAttr(At.getName()).Import(At.getValues().get(0));
    }
}
//--------------------------------------------------------------------------    
static private synchronized boolean IsInternalField(String FieldName) throws PDException
{
if (IntFields==null)
    {
    IntFields=new TreeSet();
    Record DefPDDoc = PDDocs.getRecordStructPDDocs();
    DefPDDoc.initList();
    for (int NumAttr = 0; NumAttr < DefPDDoc.NumAttr(); NumAttr++)
        IntFields.add(DefPDDoc.nextAttr().getName());
    }
return(IntFields.contains(FieldName));
}
//--------------------------------------------------------------------------    
/**
 * @return the VerLabel
 */
public String getVerLabel()
{
return VerLabel;
}
//--------------------------------------------------------------------------    
/**
 * @param VerLabel the VerLabel to set
 */
public void setVerLabel(String pVerLabel)
{
VerLabel = pVerLabel;
}
//--------------------------------------------------------------------------    
/**
 * @return the DocDate
 */
public Date getDocDate() throws ParseException
{
return APICore.Str2Date(DocDate);
}
//--------------------------------------------------------------------------    
/**
 * @param pDocDate the DocDate to set
 */
public void setDocDate(Date pDocDate)
{
DocDate = APICore.Date2Str(pDocDate);
}
//--------------------------------------------------------------------------    
/**
* @return the MimeType
*/
public String getMimeType()
{
return MimeType;
}
//--------------------------------------------------------------------------    
/**
* @param MimeType the MimeType to set
*/
public void setMimeType(String MimeType)
{
this.MimeType = MimeType;
}
//--------------------------------------------------------------------------    
/**
* @return the LockedBy
*/
public String getLockedBy()
{
return LockedBy;
}
//--------------------------------------------------------------------------    
/**
* @param LockedBy the LockedBy to set
*/
public void setLockedBy(String LockedBy)
{
this.LockedBy = LockedBy;
}
//--------------------------------------------------------------------------    
/**
* @return the PDDate
*/
public String getPDDate()
{
return PDDate;
}
//--------------------------------------------------------------------------    
/**
* @param pPDDate the PDDate to set
*/
public void setPDDate(Date pPDDate)
{
PDDate = APICore.TS2Str(pPDDate);
}
//--------------------------------------------------------------------------    
/**
* @return the PDAuthor
*/
public String getPDAuthor()
{
return PDAuthor;
}
//--------------------------------------------------------------------------    
/**
* @param pPDAuthor the PDAuthor to set
*/
public void setPDAuthor(String pPDAuthor)
{
PDAuthor = pPDAuthor;
}
//--------------------------------------------------------------------------    
}
