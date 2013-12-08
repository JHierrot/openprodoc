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
import prodoc.Attribute;
import prodoc.ObjPD;
import prodoc.PDException;
import prodoc.PDUser;

/**
 *
 * @author jhierrot
 */
public class ListUsers extends ListObjects
{
/**
 * 
 * @param parent
 * @param modal
 * @param pPDObject
 */
public ListUsers(java.awt.Frame parent, boolean modal, ObjPD pPDObject)
{
super(parent, modal, pPDObject);
}
//--------------------------------------------------------------------
@Override
protected javax.swing.JDialog AddMode() throws PDException
{
MantUsers MU = new MantUsers(Fparent, true);
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
MantUsers MU=(MantUsers)MantForm;
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
MantUsers MU = new MantUsers(Fparent, true);
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
MantUsers MU=(MantUsers)MantForm;
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
MantUsers MU = new MantUsers(Fparent, true);
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
MantUsers MU=(MantUsers)MantForm;
if (MU.isCancel())
    return(false);
//MU.getRecord().Verify();
PDObject.assignValues(MU.getRecord());
PDUser U=(PDUser)PDObject;
if (U.getPassword()!=null && U.getPassword().length()==0 )
    U.setPassword(null);
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
MantUsers MU = new MantUsers(Fparent, true);
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
MantUsers MU=(MantUsers)MantForm;
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
setTitle(MainWin.TT("Users_Maintenance"));
getAddButton().setToolTipText(MainWin.TT("Add_User"));
getDelButton().setToolTipText(MainWin.TT("Delete_User"));
getEditButton().setToolTipText(MainWin.TT("Update_User"));
getCopyButton().setToolTipText(MainWin.TT("Copy_User"));
getExportButton().setToolTipText(MainWin.TT("Export_User"));
getExportAllButton().setToolTipText(MainWin.TT("Export_All"));
getImportButton().setToolTipText(MainWin.TT("Import_User"));
getjLabel1().setText(MainWin.TT("Filter_User"));
getUserFilter().setToolTipText(MainWin.TT("Type_partial_or_complete_User_name"));
}
//--------------------------------------------------------------------
/* method to overwrite to do a postinsert operation
 *
 * @throws PDException
 */
@Override
protected void PostInsert(JDialog D) throws PDException
{
Attribute Attr = PDObject.getRecord().getAttr(PDUser.fPASSWORD);
String Pass=(String)Attr.getValue();
if (Pass==null || Pass.length()==0)
    return;
Attr.setValue("");
PDObject.update(); // to ensure not to store the password if autenticator !=OPD
Attr = PDObject.getRecord().getAttr(PDUser.fNAME);
String Name=(String)Attr.getValue();
MainWin.getSession().SetPassword(Name, Pass);
}
//--------------------------------------------------------------------
/* method to overwrite to do a postinsert operation
 *
 * @throws PDException
 */
@Override
protected void PostEdit(JDialog D) throws PDException
{
Attribute Attr = PDObject.getRecord().getAttr(PDUser.fPASSWORD);
String Pass=(String)Attr.getValue();
if (Pass==null || Pass.length()==0)
    return;
Attr.setValue("");
PDObject.update(); // to ensure not to store the password if autenticator !=OPD
Attr = PDObject.getRecord().getAttr(PDUser.fNAME);
String Name=(String)Attr.getValue();
MainWin.getSession().SetPassword(Name, Pass);
}
//--------------------------------------------------------------------
}
