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

import java.util.Vector;
import javax.swing.JDialog;
import prodoc.*;

/**
 *
 * @author jhierrot
 */
public class ListObjDef extends ListObjects
{
/**
 * 
 * @param parent
 * @param modal
 * @param pPDObject
 */
public ListObjDef(java.awt.Frame parent, boolean modal, ObjPD pPDObject)
{
super(parent, modal, pPDObject);
}
//--------------------------------------------------------------------
@Override
protected javax.swing.JDialog AddMode() throws PDException
{
MantObjDefs MU = new MantObjDefs(Fparent, true);
Record Parent=getPDTableModel().getElement(getSelectedRow());
Record Child=PDObject.getRecord();
Child.getAttr(PDObjDefs.fCLASSTYPE).setValue(Parent.getAttr(PDObjDefs.fCLASSTYPE).getValue());
Child.getAttr(PDObjDefs.fPARENT).setValue(Parent.getAttr(PDObjDefs.fNAME).getValue());
Child.getAttr(PDObjDefs.fACL).setValue(Parent.getAttr(PDObjDefs.fACL).getValue());
Child.getAttr(PDObjDefs.fREPOSIT).setValue(Parent.getAttr(PDObjDefs.fREPOSIT).getValue());
Child.getAttr(PDObjDefs.fACTIVE).setValue(Parent.getAttr(PDObjDefs.fACTIVE).getValue());
Child.getAttr(PDObjDefs.fCREATED).setValue(false);
MU.setRecord(Child);
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
MantObjDefs MU=(MantObjDefs)MantForm;
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
MantObjDefs MU = new MantObjDefs(Fparent, true);
Record r=getPDTableModel().getElement(getSelectedRow());
if ((Boolean)r.getAttr(PDObjDefs.fCREATED).getValue())
    PDExceptionFunc.GenPDException(MainWin.DrvTT("Destroy_object_before_deleting_definition"), null);
MU.setRecord(r);
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
MantObjDefs MU=(MantObjDefs)MantForm;
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
MantObjDefs MU = new MantObjDefs(Fparent, true);
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
MantObjDefs MU=(MantObjDefs)MantForm;
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
MantObjDefs MU = new MantObjDefs(Fparent, true);
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
MantObjDefs MU=(MantObjDefs)MantForm;
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
setTitle(MainWin.TT("Object_definitions_Maintenance"));
getAddButton().setToolTipText(MainWin.TT("Add_Object_definition"));
getDelButton().setToolTipText(MainWin.TT("Delete_Object_definition"));
getEditButton().setToolTipText(MainWin.TT("Update_Object_definition"));
getCopyButton().setToolTipText(MainWin.TT("Copy_Object_definition"));
getExportButton().setToolTipText(MainWin.TT("Export_Object_definition"));
getExportAllButton().setToolTipText(MainWin.TT("Export_All"));
getImportButton().setToolTipText(MainWin.TT("Import_Object_definition"));
getjLabel1().setText(MainWin.TT("Filter_Object_definition"));
getUserFilter().setToolTipText(MainWin.TT("Type_partial_or_complete_Object_definition_name"));
}
//--------------------------------------------------------------------
/* method to overwrite to do a postinsert operation
 *
 * @throws PDException
 */
@Override
protected void PostInsert(JDialog D) throws PDException
{
PDObjDefs Def=(PDObjDefs)PDObject;    
Attribute Attr = PDObject.getRecord().getAttr(PDObjDefs.fNAME);
String Name=(String)Attr.getValue();    
Vector ListAtt=((MantObjDefs)D).getListRes();
for (int i = 0; i < ListAtt.size(); i++)
    {
    Def.addAtribute(Def.ConvertRec((Record)ListAtt.elementAt(i)));
    }
}
//--------------------------------------------------------------------
/* method to overwrite to do a postEdit operation
 *
 * @throws PDException
 */
@Override
protected void PostEdit(JDialog D) throws PDException
{
PDObjDefs Def=(PDObjDefs)PDObject;    
Def.DelAtributes();
PostInsert(D);
}
//--------------------------------------------------------------------

}
