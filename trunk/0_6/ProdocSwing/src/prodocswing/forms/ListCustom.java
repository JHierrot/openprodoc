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
public class ListCustom extends ListObjects
{
/**
 * 
 * @param parent
 * @param modal
 * @param pPDObject
 */
public ListCustom(java.awt.Frame parent, boolean modal, ObjPD pPDObject)
{
super(parent, modal, pPDObject);
}
//--------------------------------------------------------------------
@Override
protected javax.swing.JDialog AddMode() throws PDException
{
MantCustom MC = new MantCustom(Fparent, true);
MC.setRecord(PDObject.getRecord());
MC.AddMode();
return(MC);
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
MantCustom MC=(MantCustom)MantForm;
if (MC.isCancel())
    return(false);
PDObject.assignValues(MC.getRecord());
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
MantCustom MC = new MantCustom(Fparent, true);
MC.setRecord(getPDTableModel().getElement(getSelectedRow()));
MC.DelMode();
return(MC);
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
MantCustom MC=(MantCustom)MantForm;
if (MC.isCancel())
    return(false);
PDObject.assignValues(MC.getRecord());
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
MantCustom MC = new MantCustom(Fparent, true);
MC.setRecord(getPDTableModel().getElement(getSelectedRow()));
MC.EditMode();
return(MC);
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
MantCustom MC=(MantCustom)MantForm;
if (MC.isCancel())
    return(false);
//MC.getRecord().Verify();
PDObject.assignValues(MC.getRecord());
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
MantCustom MC = new MantCustom(Fparent, true);
MC.setRecord(getPDTableModel().getElement(getSelectedRow()));
MC.CopyMode();
return(MC);
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
MantCustom MC=(MantCustom)MantForm;
if (MC.isCancel())
    return(false);
PDObject.assignValues(MC.getRecord());
return(true);
}
//--------------------------------------------------------------------
/**
 * 
 */
@Override
protected void AssignLabels()
{
setTitle(MainWin.TT("Customization_Maintenance"));
getAddButton().setToolTipText(MainWin.TT("Add_Customization"));
getDelButton().setToolTipText(MainWin.TT("Delete_Customization"));
getEditButton().setToolTipText(MainWin.TT("Update_Customization"));
getCopyButton().setToolTipText(MainWin.TT("Copy_Customization"));
getExportButton().setToolTipText(MainWin.TT("Export_Customization"));
getExportAllButton().setToolTipText(MainWin.TT("Export_All"));
getImportButton().setToolTipText(MainWin.TT("Import_Customization"));
getjLabel1().setText(MainWin.TT("Filter_Customization"));
getUserFilter().setToolTipText(MainWin.TT("Type_partial_or_complete_Customization_name"));
}
//--------------------------------------------------------------------
}
