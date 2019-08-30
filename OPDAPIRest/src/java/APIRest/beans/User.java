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

/**
 *
 * @author jhier
 */
public class User
{
private String Name;
private String Password;

public static User CreateUser(String json)
{
Gson g = new Gson();
return(g.fromJson(json, User.class));    
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
 * @return the Password
 */
public String getPassword()
{
return Password;
}

/**
 * @param Password the Password to set
 */
public void setPassword(String Password)
{
this.Password = Password;
}
}
