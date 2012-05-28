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
 * This class implements a remote conection to a Prodoc Server
 * @author jhierrot
 */
public class DriverRemote  extends DriverGeneric
{
/**
* 
* @param pURL
* @param pPARAM
* @param pUser
* @param pPassword
*/
public DriverRemote(String pURL, String pPARAM, String pUser, String pPassword)
{
super(pURL, pPARAM, pUser, pPassword);
}
//--------------------------------------------------------------------------

public void delete() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//--------------------------------------------------------------------------
public boolean isConnected()
{
throw new UnsupportedOperationException("Not supported yet.");
}

protected void CreateTable(String TableName, Record Fields) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

protected void DropTable(String TableName) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
/**
*
* @param TableName
* @param Fields New fields to add
* @throws PDException
*/
protected void AlterTable(String TableName, Record Fields) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

protected void InsertRecord(String TableName, Record Fields) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

protected void DeleteRecord(String TableName, Conditions DelConds) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

protected void UpdateRecord(String TableName, Record NewFields, Conditions UpConds) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

public void IniciarTrans() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

public void CerrarTrans() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

public void AnularTrans() throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//--------------------------------------------------------------------------
/**
 *
 * @param TableName1
 * @param Field1
 * @param TableName2
 * @param Field2
 * @throws PDException
 */
protected void AddIntegrity(String TableName1, String Field1, String TableName2, String Field2) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//--------------------------------------------------------------------------
public Cursor OpenCursor(Query Search) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//--------------------------------------------------------------------------
public Record NextRec(Cursor CursorIdent) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}

public void CloseCursor(Cursor CursorIdent) throws PDException
{
throw new UnsupportedOperationException("Not supported yet.");
}
//--------------------------------------------------------------------------

    @Override
    protected void AddIntegrity(String TableName1, String Field11, String Field12, String TableName2, String Field21, String Field22) throws PDException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
