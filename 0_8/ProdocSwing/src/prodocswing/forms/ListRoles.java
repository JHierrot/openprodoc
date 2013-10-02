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

package prodocswing.forms;

import javax.swing.JDialog;
import prodoc.ObjPD;
import prodoc.PDException;

/**
 *
 * @author jhierrot
 */
public class ListRoles extends ListObjects
{
/**
 * 
 * @param parent
 * @param modal
 * @param pPDObject
 */
public ListRoles(java.awt.Frame parent, boolean modal, ObjPD pPDObject)
{
super(parent, modal, pPDObject);
}
//--------------------------------------------------------------------
/**
 * 
 */
@Override
protected void RefreshTable()
{
super.RefreshTable();
for (int i = 0; i < 18; i++)
    {
    getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(2));
    }
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(3));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(4));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(5));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(6));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(7));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(8));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(9));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(10));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(11));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(12));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(13));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(14));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(15));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(16));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(17));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(18));
//getObjectsTable().getColumnModel().removeColumn(getObjectsTable().getColumnModel().getColumn(10));
}
//--------------------------------------------------------------------
@Override
protected javax.swing.JDialog AddMode() throws PDException
{
MantRoles MU = new MantRoles(Fparent, true);
MU.setRecord(PDObject.getRecord());
MU.AddMode();
return(MU);
}
//--------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
@Override
protected boolean AddModeOk()  throws PDException
{
MantRoles MU=(MantRoles)MantForm;
if (MU.isCancel())
    return(false);
PDObject.assignValues(MU.getRecord());
return(true);
}
//--------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
@Override
protected JDialog DelMode() throws PDException
{
MantRoles MU = new MantRoles(Fparent, true);
MU.setRecord(getPDTableModel().getElement(getSelectedRow()));
MU.DelMode();
return(MU);
}
//--------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
@Override
protected boolean DelModeOk() throws PDException
{
MantRoles MU=(MantRoles)MantForm;
if (MU.isCancel())
    return(false);
PDObject.assignValues(MU.getRecord());
return(true);
}
//--------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
@Override
protected JDialog EditMode() throws PDException
{
MantRoles MU = new MantRoles(Fparent, true);
MU.setRecord(getPDTableModel().getElement(getSelectedRow()));
MU.EditMode();
return(MU);
}
//--------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
@Override
protected boolean EditModeOk() throws PDException
{
MantRoles MU=(MantRoles)MantForm;
if (MU.isCancel())
    return(false);
//MU.getRecord().Verify();
PDObject.assignValues(MU.getRecord());
return(true);
}
//--------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
@Override
protected JDialog CopyMode() throws PDException
{
MantRoles MU = new MantRoles(Fparent, true);
MU.setRecord(getPDTableModel().getElement(getSelectedRow()));
MU.CopyMode();
return(MU);
}
//--------------------------------------------------------------------
/**
 * 
 * @return
 * @throws PDException
 */
@Override
protected boolean CopyModeOk() throws PDException
{
MantRoles MU=(MantRoles)MantForm;
if (MU.isCancel())
    return(false);
PDObject.assignValues(MU.getRecord());
return(true);
}
//--------------------------------------------------------------------
/**
 * 
 */
@Override
protected void AssignLabels()
{
setTitle(MainWin.TT("Roles_Maintenance"));
getAddButton().setToolTipText(MainWin.TT("Add_Role"));
getDelButton().setToolTipText(MainWin.TT("Delete_Role"));
getEditButton().setToolTipText(MainWin.TT("Update_Role"));
getCopyButton().setToolTipText(MainWin.TT("Copy_Role"));
getExportButton().setToolTipText(MainWin.TT("Export_Role"));
getExportAllButton().setToolTipText(MainWin.TT("Export_All"));
getImportButton().setToolTipText(MainWin.TT("Import_Role"));
getjLabel1().setText(MainWin.TT("Filter_Role"));
getUserFilter().setToolTipText(MainWin.TT("Type_partial_or_complete_Role_name"));
}
//--------------------------------------------------------------------
}
