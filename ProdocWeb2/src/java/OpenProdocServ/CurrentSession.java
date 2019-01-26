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
package OpenProdocServ;

import java.util.Date;


public class CurrentSession
{
private String UserName;  
private Date LoginTime;
private String Host;
public CurrentSession(String pUserName,Date pLoginTime, String pHost)
{
UserName=pUserName;  
LoginTime=pLoginTime;
Host=pHost; 
}
//-----------------------------------------------------------------------------------------------

/**
* @return the UserName
*/
public String getUserName()
{
return UserName;
}

/**
* @return the LoginTime
*/
public Date getLoginTime()
{
return LoginTime;
}

/**
* @return the Host
*/
public String getHost()
{
return Host;
}
}
