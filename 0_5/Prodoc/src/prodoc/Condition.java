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


package prodoc;

import java.util.HashSet;

/**
 *
 * @author jhierrot
 */
public class Condition
{
/**
 *
 */
private int cType=0;
// a standar comparation with attibute, comparator and value
/**
 *
 */
static final int ctNORMAL     =1;
/**
 *
 */
static final int ctBETWEEN    =2;
/**
 *
 */
static final int ctIN         =3;
/**
 *
 */
static final int cEQUALFIELDS =4;
/**
 *
 */
private String Field=null;
/**
 *
 */
private int Comparation=cEQUAL;
/**
 *
 */
public static final int cEQUAL   =0;
/**
 *
 */
public static final int cGT      =1;
/**
 *
 */
public static final int cLT      =2;
/**
 *
 */
public static final int cGET     =3;
/**
 *
 */
public static final int cLET     =4;
/**
 *
 */
public static final int cNE      =5;
/**
 *
 */
public static final int cLIKE     =8;
/**
 *
 */
public static final int cINList   =6;
/**
 * TODO: implement "selec in" in driverJDBC
 */
public static final int cINQuery =7;

/**
 *
 */
private Object Value=null;
/**
 *
 */
private boolean Invert=false;

//-------------------------------------------------------------------------
/**
 *
 * @param Attr
 */
public Condition(Attribute Attr)
{
cType=ctNORMAL;
Field=Attr.getName();
Value=Attr.getValue();
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param pComparation
 * @param pValue
 * @throws PDException
 */
public Condition(String pField, int pComparation, Object pValue) throws PDException
{
if (pValue==null || (pValue instanceof String && ((String)pValue).length()==0 ))
    {
    PDExceptionFunc.GenPDException("null_value_of_condition",pField);
    }
cType=ctNORMAL;
Field=pField;
Comparation=pComparation;
Value=pValue;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param pField2
 * @throws PDException
 */
public Condition(String pField, String pField2) throws PDException
{
cType=cEQUALFIELDS;
Field=pField;
Comparation=cEQUAL;
Value=pField2;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param ListVal
 * @throws PDException
 */
public Condition(String pField, HashSet ListVal) throws PDException
{
if (ListVal==null || ListVal.size()==0)
    {
    PDExceptionFunc.GenPDException("null_value_of_condition",pField);
    }
cType=ctIN;
Field=pField;
Comparation=cINList;
Value=ListVal;
}
//-------------------------------------------------------------------------
/**
 *
 * @param pField
 * @param Search
 * @throws PDException
 */
public Condition(String pField, Query Search) throws PDException
{
if (Search==null)
    {
    PDExceptionFunc.GenPDException("null_value_of_condition",pField);
    }
cType=ctIN;
Field=pField;
Comparation=cINQuery;
Value=Search;
}
//-------------------------------------------------------------------------
/**
* @return the cType
*/
public int getcType()
{
return cType;
}
//-------------------------------------------------------------------------
/**
* @return the Field
*/
public String getField()
{
return Field;
}
//-------------------------------------------------------------------------
/**
* @return the Value
*/
public Object getValue()
{
return Value;
}
//-------------------------------------------------------------------------
/**
* @return the Comparation
*/
public int getComparation()
{
return Comparation;
}
//-------------------------------------------------------------------------
/**
* @return the Invert
*/
public boolean isInvert()
{
return Invert;
}
//-------------------------------------------------------------------------
}
