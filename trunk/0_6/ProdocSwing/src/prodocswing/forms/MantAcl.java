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

/*
 * MantUsers.java
 *
 * Created on 17-feb-2010, 21:16:33
 */

package prodocswing.forms;

import java.awt.Frame;
import prodoc.Attribute;
import prodoc.PDACL;
import prodoc.PDException;
import prodoc.Record;
import prodocswing.PDTableModel;

/**
 *
 * @author jhierrot
 */
public class MantAcl extends javax.swing.JDialog
{
private Record ACL;
private boolean Cancel;
private PDACL EditACL;
Frame Fparent;
private PDTableModel GroupsMembers;
private PDTableModel UsersMembers;

/** Creates new form MantUsers
 * @param parent 
 * @param modal  
 */
public MantAcl(java.awt.Frame parent, boolean modal)
{
super(parent, modal);
Fparent=parent;
try {
initComponents();
EditACL = new PDACL(MainWin.getSession());
GroupsTable.setAutoCreateRowSorter(true);
GroupsTable.setAutoCreateColumnsFromModel(true);
UsersTable.setAutoCreateRowSorter(true);
UsersTable.setAutoCreateColumnsFromModel(true);
} catch (PDException ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LabelOperation = new javax.swing.JLabel();
        ACLNameLabel = new javax.swing.JLabel();
        ACLDescripLabel = new javax.swing.JLabel();
        ACLNameTextField = new javax.swing.JTextField();
        ACLDescripTextField = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        PanelGrupos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        GroupsTable = new javax.swing.JTable();
        ToolBarGroups = new javax.swing.JToolBar();
        AddButtonG = new javax.swing.JButton();
        DelButtonG = new javax.swing.JButton();
        EditButtonG = new javax.swing.JButton();
        PanelUsuarios = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        UsersTable = new javax.swing.JTable();
        ToolBarUsers = new javax.swing.JToolBar();
        AddButtonU = new javax.swing.JButton();
        DelButtonU = new javax.swing.JButton();
        EditButtonU = new javax.swing.JButton();
        ButtonAcept = new javax.swing.JButton();
        ButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(MainWin.TT("ACL_Maintenance"));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        LabelOperation.setFont(new java.awt.Font("DejaVu Sans", 1, 14));
        LabelOperation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelOperation.setText("jLabel1");

        ACLNameLabel.setFont(MainWin.getFontDialog());
        ACLNameLabel.setText("jLabel1");

        ACLDescripLabel.setFont(MainWin.getFontDialog());
        ACLDescripLabel.setText("jLabel1");

        ACLNameTextField.setFont(MainWin.getFontDialog());

        ACLDescripTextField.setFont(MainWin.getFontDialog());

        jScrollPane1.setViewportView(GroupsTable);

        ToolBarGroups.setRollover(true);

        AddButtonG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png"))); // NOI18N
        AddButtonG.setToolTipText(MainWin.TT("Add_group_to_ACL"));
        AddButtonG.setFocusable(false);
        AddButtonG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddButtonG.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        AddButtonG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonGActionPerformed(evt);
            }
        });
        ToolBarGroups.add(AddButtonG);

        DelButtonG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/del.png"))); // NOI18N
        DelButtonG.setToolTipText(MainWin.TT("Delete_group_from_ACL"));
        DelButtonG.setFocusable(false);
        DelButtonG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DelButtonG.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DelButtonG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelButtonGActionPerformed(evt);
            }
        });
        ToolBarGroups.add(DelButtonG);

        EditButtonG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/edit.png"))); // NOI18N
        EditButtonG.setToolTipText(MainWin.TT("Edit_permisions_of_group_in_ACL"));
        EditButtonG.setFocusable(false);
        EditButtonG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditButtonG.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        EditButtonG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButtonGActionPerformed(evt);
            }
        });
        ToolBarGroups.add(EditButtonG);

        javax.swing.GroupLayout PanelGruposLayout = new javax.swing.GroupLayout(PanelGrupos);
        PanelGrupos.setLayout(PanelGruposLayout);
        PanelGruposLayout.setHorizontalGroup(
            PanelGruposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGruposLayout.createSequentialGroup()
                .addGroup(PanelGruposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGruposLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(ToolBarGroups, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGruposLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelGruposLayout.setVerticalGroup(
            PanelGruposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGruposLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ToolBarGroups, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(MainWin.TT("Groups"), PanelGrupos);

        jScrollPane2.setViewportView(UsersTable);

        ToolBarUsers.setRollover(true);

        AddButtonU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png"))); // NOI18N
        AddButtonU.setToolTipText(MainWin.TT("Add_user_to_ACL"));
        AddButtonU.setFocusable(false);
        AddButtonU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddButtonU.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        AddButtonU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonUActionPerformed(evt);
            }
        });
        ToolBarUsers.add(AddButtonU);

        DelButtonU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/del.png"))); // NOI18N
        DelButtonU.setToolTipText(MainWin.TT("Delete_user_from_ACL"));
        DelButtonU.setFocusable(false);
        DelButtonU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DelButtonU.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DelButtonU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelButtonUActionPerformed(evt);
            }
        });
        ToolBarUsers.add(DelButtonU);

        EditButtonU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/edit.png"))); // NOI18N
        EditButtonU.setToolTipText(MainWin.TT("Edit_permisions_of_user_in_ACL"));
        EditButtonU.setFocusable(false);
        EditButtonU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EditButtonU.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        EditButtonU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButtonUActionPerformed(evt);
            }
        });
        ToolBarUsers.add(EditButtonU);

        javax.swing.GroupLayout PanelUsuariosLayout = new javax.swing.GroupLayout(PanelUsuarios);
        PanelUsuarios.setLayout(PanelUsuariosLayout);
        PanelUsuariosLayout.setHorizontalGroup(
            PanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelUsuariosLayout.createSequentialGroup()
                .addGroup(PanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelUsuariosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(ToolBarUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelUsuariosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelUsuariosLayout.setVerticalGroup(
            PanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ToolBarUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(MainWin.TT("Users"), PanelUsuarios);

        ButtonAcept.setFont(MainWin.getFontDialog());
        ButtonAcept.setText(MainWin.TT("Ok"));
        ButtonAcept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAceptActionPerformed(evt);
            }
        });

        ButtonCancel.setFont(MainWin.getFontDialog());
        ButtonCancel.setText(MainWin.TT("Cancel"));
        ButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addComponent(LabelOperation, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ACLNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ACLNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(ButtonAcept)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ButtonCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ACLDescripLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ACLDescripTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelOperation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ACLNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ACLNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ACLDescripTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ACLDescripLabel))
                .addGap(13, 13, 13)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonCancel)
                    .addComponent(ButtonAcept))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ButtonCancelActionPerformed
    {//GEN-HEADEREND:event_ButtonCancelActionPerformed
Cancel=true;
try {
if (MainWin.getSession().isInTransaction())
        MainWin.getSession().AnularTrans();
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
this.dispose();
    }//GEN-LAST:event_ButtonCancelActionPerformed

    private void ButtonAceptActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ButtonAceptActionPerformed
    {//GEN-HEADEREND:event_ButtonAceptActionPerformed
try {
Attribute Attr=ACL.getAttr(PDACL.fNAME);
Attr.setValue(ACLNameTextField.getText());
Attr=ACL.getAttr(PDACL.fDESCRIPTION);
Attr.setValue(ACLDescripTextField.getText());
if (MainWin.getSession().isInTransaction())
        MainWin.getSession().CerrarTrans();
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
Cancel=false;
this.dispose();
    }//GEN-LAST:event_ButtonAceptActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
Cancel=true;
    }//GEN-LAST:event_formWindowClosing

    private void AddButtonGActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddButtonGActionPerformed
    {//GEN-HEADEREND:event_AddButtonGActionPerformed
try {
PDACL acl=new PDACL((MainWin.getSession()));
acl.setName((String)ACL.getAttr(PDACL.fNAME).getValue());
Record r=acl.getRecordAclGroupsStruct();
MantPermision MP = new MantPermision(Fparent, true, true);
MP.setRecord(r);
MP.AddMode();
MP.setLocationRelativeTo(null);
MP.setVisible(true);
if (MP.isCancel())
    return;
r.assign(MP.getRecord());
String Grp=(String)r.getAttr(PDACL.fGROUPNAME).getValue();
int Level=(Integer)r.getAttr(PDACL.fPERMISION).getValue();
acl.addGroup(Grp, Level);
RefreshGroups(acl.getName());
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
}//GEN-LAST:event_AddButtonGActionPerformed

    private void DelButtonGActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_DelButtonGActionPerformed
    {//GEN-HEADEREND:event_DelButtonGActionPerformed
if (GroupsTable.getSelectedRow()==-1)
    return;
try {
PDACL acl=new PDACL((MainWin.getSession()));
acl.setName((String)ACL.getAttr(PDACL.fNAME).getValue());
MantPermision MP = new MantPermision(Fparent, true, true);
MP.setRecord(GroupsMembers.getElement(GroupsTable.convertRowIndexToModel(GroupsTable.getSelectedRow())));
MP.DelMode();
MP.setLocationRelativeTo(null);
MP.setVisible(true);
if (MP.isCancel())
    return;
String Grp=(String) MP.getRecord().getAttr(PDACL.fGROUPNAME).getValue();
acl.delGroup(Grp);
RefreshGroups(acl.getName());
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
}//GEN-LAST:event_DelButtonGActionPerformed

    private void EditButtonGActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_EditButtonGActionPerformed
    {//GEN-HEADEREND:event_EditButtonGActionPerformed
if (GroupsTable.getSelectedRow()==-1)
    return;
try {
PDACL acl=new PDACL((MainWin.getSession()));
acl.setName((String)ACL.getAttr(PDACL.fNAME).getValue());
MantPermision MP = new MantPermision(Fparent, true, true);
MP.setRecord(GroupsMembers.getElement(GroupsTable.convertRowIndexToModel(GroupsTable.getSelectedRow())));
MP.EditMode();
MP.setLocationRelativeTo(null);
MP.setVisible(true);
if (MP.isCancel())
    return;
Record r=MP.getRecord();
String Grp=(String)r.getAttr(PDACL.fGROUPNAME).getValue();
int Level=(Integer)r.getAttr(PDACL.fPERMISION).getValue();
acl.delGroup(Grp);
acl.addGroup(Grp, Level);
RefreshGroups(acl.getName());
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
}//GEN-LAST:event_EditButtonGActionPerformed

    private void AddButtonUActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddButtonUActionPerformed
    {//GEN-HEADEREND:event_AddButtonUActionPerformed
try {
PDACL acl=new PDACL((MainWin.getSession()));
acl.setName((String)ACL.getAttr(PDACL.fNAME).getValue());
Record r=acl.getRecordAclUsersStruct();
MantPermision MP = new MantPermision(Fparent, true, false);
MP.setRecord(r);
MP.AddMode();
MP.setLocationRelativeTo(null);
MP.setVisible(true);
if (MP.isCancel())
    return;
r.assign(MP.getRecord());
String User=(String)r.getAttr(PDACL.fUSERNAME).getValue();
int Level=(Integer)r.getAttr(PDACL.fPERMISION).getValue();
acl.addUser(User, Level);
RefreshUsers(acl.getName());
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_AddButtonUActionPerformed

    private void DelButtonUActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_DelButtonUActionPerformed
    {//GEN-HEADEREND:event_DelButtonUActionPerformed
if (UsersTable.getSelectedRow()==-1)
    return;
try {
PDACL acl=new PDACL((MainWin.getSession()));
acl.setName((String)ACL.getAttr(PDACL.fNAME).getValue());
//Record r=acl.getRecordAclUsersStruct();
MantPermision MP = new MantPermision(Fparent, true, false);
MP.setRecord(UsersMembers.getElement(UsersTable.convertRowIndexToModel(UsersTable.getSelectedRow())));
MP.DelMode();
MP.setLocationRelativeTo(null);
MP.setVisible(true);
if (MP.isCancel())
    return;
//r.assign(MP.getRecord());
String User=(String) MP.getRecord().getAttr(PDACL.fUSERNAME).getValue();
acl.delUser(User);
RefreshUsers(acl.getName());
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_DelButtonUActionPerformed

    private void EditButtonUActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_EditButtonUActionPerformed
    {//GEN-HEADEREND:event_EditButtonUActionPerformed
if (UsersTable.getSelectedRow()==-1)
    return;
try {
PDACL acl=new PDACL((MainWin.getSession()));
acl.setName((String)ACL.getAttr(PDACL.fNAME).getValue());
MantPermision MP = new MantPermision(Fparent, true, false);
MP.setRecord(UsersMembers.getElement(UsersTable.convertRowIndexToModel(UsersTable.getSelectedRow())));
MP.EditMode();
MP.setLocationRelativeTo(null);
MP.setVisible(true);
if (MP.isCancel())
    return;
Record r=MP.getRecord();
String User=(String)r.getAttr(PDACL.fUSERNAME).getValue();
int Level=(Integer)r.getAttr(PDACL.fPERMISION).getValue();
acl.delUser(User);
acl.addUser(User, Level);
RefreshUsers(acl.getName());
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_EditButtonUActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ACLDescripLabel;
    private javax.swing.JTextField ACLDescripTextField;
    private javax.swing.JLabel ACLNameLabel;
    private javax.swing.JTextField ACLNameTextField;
    private javax.swing.JButton AddButtonG;
    private javax.swing.JButton AddButtonU;
    private javax.swing.JButton ButtonAcept;
    private javax.swing.JButton ButtonCancel;
    private javax.swing.JButton DelButtonG;
    private javax.swing.JButton DelButtonU;
    private javax.swing.JButton EditButtonG;
    private javax.swing.JButton EditButtonU;
    private javax.swing.JTable GroupsTable;
    private javax.swing.JLabel LabelOperation;
    private javax.swing.JPanel PanelGrupos;
    private javax.swing.JPanel PanelUsuarios;
    private javax.swing.JToolBar ToolBarGroups;
    private javax.swing.JToolBar ToolBarUsers;
    private javax.swing.JTable UsersTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

/**
*
*/
public void AddMode()
{
LabelOperation.setText(MainWin.TT("Add_ACL"));
DelButtonG.setEnabled(false);
EditButtonG.setEnabled(false);
}
//----------------------------------------------------------------
/**
*
*/
public void DelMode()
{
LabelOperation.setText(MainWin.TT("Delete_Acl"));
ACLNameTextField.setEditable(false);
ACLDescripTextField.setEditable(false);
}
//----------------------------------------------------------------
/**
*
*/
public void EditMode()
{
LabelOperation.setText(MainWin.TT("Update_ACL"));
ACLNameTextField.setEditable(false);
try {
MainWin.getSession().IniciarTrans();
} catch (PDException ex)
    {MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
}
//----------------------------------------------------------------
/**
*
*/
public void CopyMode()
{
LabelOperation.setText(MainWin.TT("Copy_ACL"));
ACLNameTextField.setText(ACLNameTextField.getText()+"1");
}
//----------------------------------------------------------------

/**
* @return the ACL
*/
public Record getRecord()
{
return ACL;
}

/**
 * @param pACL
*/
public void setRecord(Record pACL)
{
try {
ACL = pACL;
Attribute Attr = ACL.getAttr(PDACL.fNAME);
ACLNameLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue() != null)
    {
    ACLNameTextField.setText((String) Attr.getValue());
    RefreshUsers((String)Attr.getValue());
    RefreshGroups((String)Attr.getValue());
    }
ACLNameTextField.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr = ACL.getAttr(PDACL.fDESCRIPTION);
ACLDescripLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue() != null)
    {
    ACLDescripTextField.setText((String) Attr.getValue());
    }
ACLDescripTextField.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
} catch (PDException ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
}
//----------------------------------------------------------------
private void RefreshUsers(String AclName) throws PDException
{
UsersMembers = new PDTableModel();
UsersMembers.setDrv(MainWin.getSession());
UsersMembers.setListFields(EditACL.getRecordAclUsersStruct());
UsersMembers.setCursor(EditACL.ListUsers(AclName ));
UsersTable.setModel(UsersMembers);
UsersTable.getColumnModel().getColumn(0).setMaxWidth(0);
UsersTable.getColumnModel().removeColumn(UsersTable.getColumnModel().getColumn(0));
}
//----------------------------------------------------------------
private void RefreshGroups(String AclName) throws PDException
{
GroupsMembers = new PDTableModel();
GroupsMembers.setDrv(MainWin.getSession());
GroupsMembers.setListFields(EditACL.getRecordAclGroupsStruct());
GroupsMembers.setCursor(EditACL.ListGroups(AclName));
GroupsTable.setModel(GroupsMembers);
GroupsTable.getColumnModel().getColumn(0).setMaxWidth(0);
GroupsTable.getColumnModel().removeColumn(GroupsTable.getColumnModel().getColumn(0));
}
//----------------------------------------------------------------
/**
* @return the Cancel
*/
public boolean isCancel()
{
return Cancel;
}
//----------------------------------------------------------------
}
