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
 * author: Joaquin Hierro      2011
 * 
 */

package prodoc;

/**
 * OpenProdoc Exceptions
 * @author jhierrot
 */
public class PDException extends Exception
{

/**
 * constructor of the exception
 * @param TextExcep text of the exception
 */
public PDException(String TextExcep)
{
super(TextExcep);
}
//----------------------------------------------------------------
/**
 * creates and thows a new PDException with the information provided
 * @param TextExcep Text of the exception
 * @param OptParam Optional parameter to be added to the text of exception
 * @throws PDException the created PDException
 */
public static void GenPDException(String TextExcep, String OptParam) throws PDException
{
String M;
if (OptParam==null || OptParam.length()==0)
    M=TextExcep;
else
    M=TextExcep+":"+OptParam;
PDLog.Error(M);
throw new PDException(M);
}
//----------------------------------------------------------------
}
