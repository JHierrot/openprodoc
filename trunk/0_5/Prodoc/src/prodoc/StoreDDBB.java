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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author jhierrot
 */
public class StoreDDBB extends StoreGeneric
{
/**
 *
 */
private Connection con=null;
/**
 *
 */
private Statement stmt=null;
/**
 *
 */
private String Table=null;
/**
 *
 */
private String Driver=null;


//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
protected StoreDDBB(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
setDriver(pParam);
setTable(pParam);
}

//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Create() throws PDException
{
    // PdId includes PDID+Version
String SQL="CREATE TABLE "+getTable()+"(PDId varchar(32) PRIMARY KEY NOT NULL, PDVersion varchar(32) PRIMARY KEY NOT NULL, PDDATE timestamp NOT NULL, PDCONT BLOB NOT NULL)";
try {
stmt. execute(SQL);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_creating_StoreDDBB",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Destroy() throws PDException
{
String SQL="DROP TABLE "+getTable();
try {
stmt. execute(SQL);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_deleting_StoreDDBB",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Connect() throws PDException
{
try {
getDriver();
Class.forName(getDriver());
} catch (ClassNotFoundException ex)
    {
    PDException.GenPDException("Driver_JDBC_not_found",getDriver()+"="+ex.getLocalizedMessage());
    }
try {
con = DriverManager.getConnection(getServer(), getUser(), getPassword());
stmt = con.createStatement();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_connecting_trough_JDBC",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Disconnect() throws PDException
{
try {
stmt.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_closing_JDBC_Sentence", ex.getLocalizedMessage());
    }
try {
con.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_closing_JDBC_connection", ex.getLocalizedMessage());
    }

}
//-----------------------------------------------------------------
/**
 * 
 * @param Id
 * @param Ver
 * @param Bytes
 * @return
 * @throws PDException
 */
protected int Insert(String Id, String Ver, InputStream Bytes) throws PDException
{
VerifyId(Id);
try {
con.setAutoCommit(false);
String SQL = "INSERT INTO "+getTable()+" (PDId, PDVersion, PDDATE, PDCONT) VALUES (?, ?, ?, ?)";
PreparedStatement BlobStmt = con.prepareStatement(SQL);
BlobStmt.setString(1, Id);
BlobStmt.setString(2, Ver);
BlobStmt.setTimestamp(3,  new Timestamp(System.currentTimeMillis()));
BlobStmt.setBinaryStream(4, Bytes);
BlobStmt.execute();
con.commit();
con.setAutoCommit(true);
Bytes.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_inserting_content",ex.getLocalizedMessage());
    }
return (-1); 
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @throws PDException
 */
protected void Delete(String Id, String Ver) throws PDException
{
VerifyId(Id);
String SQL="delete from "+getTable()+" where PDId='"+Id+"' and PDVersion='"+Ver+"'";
try {
stmt. execute(SQL);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_deleting_content",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @return
 * @throws PDException
 */
protected InputStream Retrieve(String Id, String Ver) throws PDException
{
VerifyId(Id);
try {
String SQL = "SELECT PDCONT FROM "+getTable()+" where PDId='"+Id+"' and PDVersion='"+Ver+"'";
PreparedStatement BlobStmt = con.prepareStatement(SQL);
ResultSet resultSet = BlobStmt.executeQuery();
while (resultSet.next())
    {
    return (resultSet.getBinaryStream(1));
    }
PDException.GenPDException("Inexistent_content", Id+"/"+Ver);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_content", ex.getLocalizedMessage());
    }
return(null);
}
//-----------------------------------------------------------------

/**
 *
 * @return
 */
protected String getDriver()
{
return(Driver);
}
//-----------------------------------------------------------------
/**
* @return the Table
*/
protected String getTable()
{
return Table;
}
//-----------------------------------------------------------------
/**
* @param pParam the Table to set
*/
protected void setTable(String pParam)
{
Table =pParam.substring(pParam.lastIndexOf(";")+1);
}
//-----------------------------------------------------------------
/**
* @param pParam the Driver to set
*/
protected void setDriver(String pParam)
{
Driver =pParam.substring(0, pParam.lastIndexOf(";"));
}
//-----------------------------------------------------------------
/**
 * 
 * @param Id1 Original Identifier
 * @param Ver1 Original Version
 * @param Id2  New Identifier
 * @param Ver2  New Version
 * @throws PDException
 */
@Override
protected void Rename(String Id1, String Ver1, String Id2, String Ver2) throws PDException
{
VerifyId(Id1);
String SQL="update "+getTable()+" set PDId='"+Id2+"', PDVersion='"+Ver2+"' where PDId='"+Id1+"' and PDVersion='"+Ver1+"'";
try {
stmt. execute(SQL);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_renaming_content:",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------

}
