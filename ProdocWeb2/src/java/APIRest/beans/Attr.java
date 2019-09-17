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

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import prodoc.Attribute;

/**
 *
 * @author jhier
 */
public class Attr
{
private String Name;
private String Type;
private final ArrayList<String> Values=new ArrayList();

static private HashMap<Integer, String> TypsI2S=null;
public Attr(Attribute At)
{
setName(At.getName());
setType(At.getType());
String[] ListVals = At.Export().split("\\|");
Values.addAll(Arrays.asList(ListVals));
}

//--------------------------------------------------------------------------    
public static Attr CreateAttr(String json)
{
Gson g = new Gson();
return(g.fromJson(json, Attr.class));    
}
//--------------------------------------------------------------------------    
/**
* @return the Name
*/
public String getName()
{
return Name;
}
//--------------------------------------------------------------------------    
/**
* @param pName the Name to set
*/
public final void setName(String pName)
{
Name = pName;
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
* @param pType the Type to set
*/
public void setType(String pType)
{
Type = pType;
}
//--------------------------------------------------------------------------    
/**
* @param pType the Type to set
*/
public final void setType(int pType)
{
Type = getTypsI2S().get(pType);
}
//--------------------------------------------------------------------------    
/**
* @return the Values
*/
public ArrayList<String> getValues()
{
return Values;
}
//--------------------------------------------------------------------------    
/**
* @param Val Value to add
*/
public void addValue(String Val)
{
Values.add(Val);
}
//-------------------------------------------------------------------------- 
static private synchronized HashMap<Integer, String> getTypsI2S()
{
if (TypsI2S==null)
    {
    TypsI2S=new HashMap();   
    TypsI2S.put(Attribute.tSTRING   ,"String"   );
    TypsI2S.put(Attribute.tDATE     ,"Date"     );
    TypsI2S.put(Attribute.tINTEGER  ,"Integer"  );
    TypsI2S.put(Attribute.tTIMESTAMP,"TimeStamp");
    TypsI2S.put(Attribute.tFLOAT    ,"Decimal"  );
    TypsI2S.put(Attribute.tBOOLEAN  ,"Boolean"  );
    TypsI2S.put(Attribute.tTHES     ,"Thesaur"  );
    }
return (TypsI2S);
}
//-------------------------------------------------------------------------- 
}
