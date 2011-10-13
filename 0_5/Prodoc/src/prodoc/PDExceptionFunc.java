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

/**
 *
 * @author jhierrot
 */
public class PDExceptionFunc extends PDException
{

/**
 *
 * @param TextExcep 
 */
public PDExceptionFunc(String TextExcep)
{
super(TextExcep);
}
//----------------------------------------------------------------
/**
 * 
 * @param TextExcep
 * @param OptParam
 * @throws PDExceptionFunc
 */
public static void GenPDException(String TextExcep, String OptParam) throws PDExceptionFunc
{
String M;
if (OptParam==null || OptParam.length()==0)
    M=TextExcep;
else
    M=TextExcep+":"+OptParam;
if (PDLog.isInfo())
   PDLog.Info(M);
throw new PDExceptionFunc(M);
}

}
