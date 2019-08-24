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

import java.util.Date;


public class CurrentSession
{
private final String UserName;  
private final Date LoginTime;
private Date LastUse;
private final String Host;
public CurrentSession(String pUserName,Date pLoginTime, String pHost)
{
UserName=pUserName;  
LoginTime=pLoginTime;
LastUse=pLoginTime;
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

    /**
     * @return the LastUse
     */
    public Date getLastUse()
    {
        return LastUse;
    }

    /**
     * @param LastUse the LastUse to set
     */
    public void setLastUse(Date LastUse)
    {
        this.LastUse = LastUse;
    }
}
