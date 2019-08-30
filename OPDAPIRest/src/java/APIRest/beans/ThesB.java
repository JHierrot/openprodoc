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
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import prodoc.Attribute;
import prodoc.PDException;
import prodoc.PDThesaur;
import prodoc.Record;


/**
 *
 * @author jhierrot
 */
public class ThesB
{

private String Id;
    
private String Name;
private String Descrip;
private String Lang;
private String SCN;
private String Use;
private String ParentId;
private String PDDate;
private String PDAuthor;

public static ThesB CreateThes(String json)
{
Gson g = new Gson();
return(g.fromJson(json, ThesB.class));    
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

/**
* @param Id the Id to set
*/
public void setId(String Id)
{
this.Id = Id;
}

/**
* @return the Name
*/
public String getName()
{
return Name;
}

/**
* @param Name the Name to set
*/
public void setName(String Name)
{
this.Name = Name;
}

/**
* @return the Descrip
*/
public String getDescrip()
{
return Descrip;
}

/**
* @param Descrip the Descrip to set
*/
public void setDescrip(String Descrip)
{
this.Descrip = Descrip;
}

/**
* @return the Lang
*/
public String getLang()
{
return Lang;
}

/**
* @param Lang the Lang to set
*/
public void setLang(String Lang)
{
this.Lang = Lang;
}

/**
* @return the Use
*/
public String getUse()
{
return Use;
}

/**
* @param Use the Use to set
*/
public void setUse(String Use)
{
this.Use = Use;
}

/**
* @return the SCN
*/
public String getSCN()
{
return SCN;
}

/**
* @param SCN the SCN to set
*/
public void setSCN(String SCN)
{
this.SCN = SCN;
}
//--------------------------------------------------------------------------    
public static ThesB CreateThes(PDThesaur Thes) throws PDException
{
ThesB TB=new ThesB();
TB.setId(Thes.getPDId());
TB.setName(Thes.getName());
TB.setDescrip(Thes.getDescription());
TB.setLang(Thes.getLang());
TB.setParentId(Thes.getParentId());
TB.setUse(Thes.getUse());
TB.setSCN(Thes.getSCN());
TB.setPDDate(Thes.getPDDate());
TB.setPDAuthor(Thes.getPDAutor());
return(TB);
}
//--------------------------------------------------------------------------    
public void Assign(PDThesaur Thes) throws PDException
{
Thes.setName(getName());
Thes.setDescription(getDescrip());
Thes.setLang(getLang());
Thes.setParentId(getParentId());
Thes.setUse(getUse());
Thes.setSCN(getSCN());
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
* @param PDAuthor the PDAuthor to set
*/
public void setPDAuthor(String PDAuthor)
{
this.PDAuthor = PDAuthor;
}
//--------------------------------------------------------------------------    

    /**
     * @return the ParentId
     */
    public String getParentId()
    {
        return ParentId;
    }

    /**
     * @param ParentId the ParentId to set
     */
    public void setParentId(String ParentId)
    {
        this.ParentId = ParentId;
    }

}
