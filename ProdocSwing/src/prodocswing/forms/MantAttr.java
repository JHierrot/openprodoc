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
 * author: Joaquin Hierro      2011
 * 
 */

/*
 * MantUsers.java
 *
 * Created on 17-feb-2010, 21:16:33
 */

package prodocswing.forms;

import prodoc.Attribute;
import prodoc.PDException;
import prodoc.PDObjDefs;
import prodoc.PDThesaur;
import prodoc.Record;
import prodocswing.ThesField;

/**
 *
 * @author jhierrot
 */
public class MantAttr extends javax.swing.JDialog
{
private Record AttrEdit;
private boolean Cancel;
private ThesField ThesSelect=null;
private PDThesaur SelectedThes=null;
private String MaxLongLabel;
private boolean ModeDelete=false;

/** Creates new form MantUsers
 * @param parent
 * @param modal
 * @param pModeGrp  
 */
public MantAttr(java.awt.Frame parent, boolean modal, boolean pModeGrp)
{
super(parent, modal);
initComponents();
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        LabelOperation = new javax.swing.JLabel();
        AttrNameLabel = new javax.swing.JLabel();
        AttrUserNameLabel = new javax.swing.JLabel();
        AttrNameTextField = new javax.swing.JTextField();
        AttrUserNameTextField = new javax.swing.JTextField();
        DescripLabel = new javax.swing.JLabel();
        DescripTextField = new javax.swing.JTextField();
        TypeLabel = new javax.swing.JLabel();
        TypeComboBox = new javax.swing.JComboBox();
        LongLabel = new javax.swing.JLabel();
        LongMaxTextField = new javax.swing.JTextField();
        ReqLabel = new javax.swing.JLabel();
        ReqCheckBox = new javax.swing.JCheckBox();
        UniqueLabel = new javax.swing.JLabel();
        UniqueCheckBox = new javax.swing.JCheckBox();
        AllowModLabel = new javax.swing.JLabel();
        AllowModCheckBox = new javax.swing.JCheckBox();
        MultivalLabel = new javax.swing.JLabel();
        MultivalCheckBox = new javax.swing.JCheckBox();
        ButtonAcept = new javax.swing.JButton();
        ButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(MainWin.TT("Attributes_Maintenance"));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        LabelOperation.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        LabelOperation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelOperation.setText("jLabel1");

        AttrNameLabel.setFont(MainWin.getFontDialog());
        AttrNameLabel.setText(MainWin.TT("Name"));

        AttrUserNameLabel.setFont(MainWin.getFontDialog());
        AttrUserNameLabel.setText(MainWin.TT("Visible_Name_of_attribute"));

        AttrNameTextField.setFont(MainWin.getFontDialog());

        AttrUserNameTextField.setFont(MainWin.getFontDialog());

        DescripLabel.setFont(MainWin.getFontDialog());
        DescripLabel.setText(MainWin.TT("Description"));

        DescripTextField.setFont(MainWin.getFontDialog());

        TypeLabel.setFont(MainWin.getFontDialog());
        TypeLabel.setText(MainWin.TT("attribute_type"));

        TypeComboBox.setFont(MainWin.getFontDialog());
        TypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Integer", "Float", "String", "Date", "Boolean", "TimeStamp", "Thesaur" }));
        TypeComboBox.setSelectedIndex(2);
        TypeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                TypeComboBoxActionPerformed(evt);
            }
        });

        LongLabel.setFont(MainWin.getFontDialog());
        LongLabel.setText(MainWin.TT("Length"));

        LongMaxTextField.setFont(MainWin.getFontDialog());

        ReqLabel.setFont(MainWin.getFontDialog());
        ReqLabel.setText(MainWin.TT("Required"));

        ReqCheckBox.setFont(MainWin.getFontDialog());

        UniqueLabel.setFont(MainWin.getFontDialog());
        UniqueLabel.setText(MainWin.TT("Unique_value"));

        UniqueCheckBox.setFont(MainWin.getFontDialog());

        AllowModLabel.setFont(MainWin.getFontDialog());
        AllowModLabel.setText(MainWin.TT("Modifiable"));

        AllowModCheckBox.setFont(MainWin.getFontDialog());

        MultivalLabel.setFont(MainWin.getFontDialog());
        MultivalLabel.setText(MainWin.TT("Modifiable"));

        MultivalCheckBox.setFont(MainWin.getFontDialog());
        MultivalCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                MultivalCheckBoxActionPerformed(evt);
            }
        });

        ButtonAcept.setFont(MainWin.getFontDialog());
        ButtonAcept.setText(MainWin.TT("Ok"));
        ButtonAcept.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonAceptActionPerformed(evt);
            }
        });

        ButtonCancel.setFont(MainWin.getFontDialog());
        ButtonCancel.setText(MainWin.TT("Cancel"));
        ButtonCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DescripLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LongLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AttrNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(369, 369, 369)
                        .addComponent(ButtonAcept)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ButtonCancel)))
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelOperation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ReqLabel)
                            .addComponent(AllowModLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UniqueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MultivalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AttrUserNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TypeComboBox, 0, 370, Short.MAX_VALUE)
                            .addComponent(DescripTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                            .addComponent(AttrUserNameTextField)
                            .addComponent(MultivalCheckBox)
                            .addComponent(ReqCheckBox)
                            .addComponent(AllowModCheckBox)
                            .addComponent(UniqueCheckBox)
                            .addComponent(LongMaxTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AttrNameTextField))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {AllowModLabel, AttrNameLabel, AttrUserNameLabel, DescripLabel, LongLabel, ReqLabel, TypeLabel, UniqueLabel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelOperation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AttrNameLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AttrNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(AttrUserNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AttrUserNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DescripTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DescripLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TypeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LongMaxTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LongLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ReqCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(ReqLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(UniqueCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(UniqueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(AllowModCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(AllowModLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(MultivalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(5, 5, 5))
                                    .addComponent(MultivalCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(34, 34, 34))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ButtonCancel)
                                    .addComponent(ButtonAcept))))))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ButtonCancelActionPerformed
    {//GEN-HEADEREND:event_ButtonCancelActionPerformed
Cancel=true;
this.dispose();
    }//GEN-LAST:event_ButtonCancelActionPerformed

    private void ButtonAceptActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ButtonAceptActionPerformed
    {//GEN-HEADEREND:event_ButtonAceptActionPerformed
if (isModeDelete())
    {
    Cancel = false;
    this.dispose();    
    return;
    }
try {
Attribute Attr;
Attr = AttrEdit.getAttr(PDObjDefs.fATTRNAME);
if (AttrNameTextField.getText().length()==0 || AttrNameTextField.getText().length()>Attr.getLongStr())
    throw new PDException("Incorrect_attribute_name");
Attr.setValue(AttrNameTextField.getText());
Attr = AttrEdit.getAttr(PDObjDefs.fATTRUSERNAME);
if (AttrUserNameTextField.getText().length()==0 || AttrUserNameTextField.getText().length()>Attr.getLongStr())
    throw new PDException("Incorrect_attribute_name");
Attr.setValue(AttrUserNameTextField.getText());
Attr = AttrEdit.getAttr(PDObjDefs.fATTRDESCRIPTION);
if (DescripTextField.getText().length()==0 || DescripTextField.getText().length()>Attr.getLongStr())
    throw new PDException("Description_Length_Incorrect");
Attr.setValue(DescripTextField.getText());
Attr = AttrEdit.getAttr(PDObjDefs.fATTRTYPE);
Attr.setValue(TypeComboBox.getSelectedIndex());
Attr = AttrEdit.getAttr(PDObjDefs.fATTRLONG);
if (TypeComboBox.getSelectedIndex()==Attribute.tTHES)
    {
    Attr.setValue(new Integer(ThesSelect.getUseTerm().getPDId()));
    }
else if (LongMaxTextField.getText().length() > 0 && ((String) TypeComboBox.getSelectedItem()).equalsIgnoreCase("String"))
    {
    Attr.setValue(new Integer(LongMaxTextField.getText()));
    }
else
    {
    Attr.setValue(new Integer(0));
    }
if (TypeComboBox.getSelectedIndex()==Attribute.tTHES && (Integer)Attr.getValue()==0)
    throw new PDException("Thesaur_unselected");
if (TypeComboBox.getSelectedIndex()==Attribute.tSTRING && (Integer)Attr.getValue()==0)
    throw new PDException("String_long_unselected");    
Attr = AttrEdit.getAttr(PDObjDefs.fATTRREQUIRED);
Boolean Act;
if (ReqCheckBox.isSelected())
    {
    Act =true;
    }
else
    {
    Act =false;
    }
Attr.setValue(Act);
Attr = AttrEdit.getAttr(PDObjDefs.fATTRUNIQUE);
if (UniqueCheckBox.isSelected())
    {
    Act =true;
    }
else
    {
    Act =false;
    }
Attr.setValue(Act);
Attr = AttrEdit.getAttr(PDObjDefs.fATTRPRIMKEY);
//if (PrimKeyCheckBox.isSelected())
//    {
//    Act =true;
//    }
//else
//    {
//    Act =false;
//    }
if (MultivalCheckBox.isSelected())  // no es error, debe ser primary key 
    {
    Act =true;
    }
else
    {
    Act =false;
    }
Attr.setValue(Act);
Attr = AttrEdit.getAttr(PDObjDefs.fATTRMODALLOW);
if (AllowModCheckBox.isSelected())
    {
    Act =true;
    }
else
    {
    Act =false;
    }
Attr.setValue(Act);
Attr = AttrEdit.getAttr(PDObjDefs.fATTRMULTIVALUED);
if (MultivalCheckBox.isSelected())
    {
    Act =true;
    }
else
    {
    Act =false;
    }
Attr.setValue(Act);
Cancel = false;
this.dispose();
} catch (PDException ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_ButtonAceptActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
Cancel=true;
    }//GEN-LAST:event_formWindowClosing

    private void MultivalCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_MultivalCheckBoxActionPerformed
    {//GEN-HEADEREND:event_MultivalCheckBoxActionPerformed
if (MultivalCheckBox.isSelected())
    {
    if (!TypeComboBox.getSelectedItem().equals("String") && !TypeComboBox.getSelectedItem().equals("Thesaur"))    
        TypeComboBox.setSelectedItem("String");    
    UniqueCheckBox.setSelected(false);
    UniqueCheckBox.setEnabled(false);
    ReqCheckBox.setSelected(false);
    ReqCheckBox.setEnabled(false);
    }
else
    {
    UniqueCheckBox.setEnabled(true);
    ReqCheckBox.setEnabled(true);
    }
    }//GEN-LAST:event_MultivalCheckBoxActionPerformed

    private void TypeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_TypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_TypeComboBoxActionPerformed
int SelType=TypeComboBox.getSelectedIndex();
AdaptForm2Type(SelType);
    }//GEN-LAST:event_TypeComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox AllowModCheckBox;
    private javax.swing.JLabel AllowModLabel;
    private javax.swing.JLabel AttrNameLabel;
    private javax.swing.JTextField AttrNameTextField;
    private javax.swing.JLabel AttrUserNameLabel;
    private javax.swing.JTextField AttrUserNameTextField;
    private javax.swing.JButton ButtonAcept;
    private javax.swing.JButton ButtonCancel;
    private javax.swing.JLabel DescripLabel;
    private javax.swing.JTextField DescripTextField;
    private javax.swing.JLabel LabelOperation;
    private javax.swing.JLabel LongLabel;
    private javax.swing.JTextField LongMaxTextField;
    private javax.swing.JCheckBox MultivalCheckBox;
    private javax.swing.JLabel MultivalLabel;
    private javax.swing.JCheckBox ReqCheckBox;
    private javax.swing.JLabel ReqLabel;
    private javax.swing.JComboBox TypeComboBox;
    private javax.swing.JLabel TypeLabel;
    private javax.swing.JCheckBox UniqueCheckBox;
    private javax.swing.JLabel UniqueLabel;
    // End of variables declaration//GEN-END:variables

/**
*
*/
public void AddMode()
{
LabelOperation.setText(MainWin.TT("Add_Attribute"));
}
//----------------------------------------------------------------
/**
*
*/
public void DelMode()
{
setModeDelete(true);
LabelOperation.setText(MainWin.TT("Delete_Attribute"));
AttrNameTextField.setEditable(false);
AttrUserNameTextField.setEditable(false);
DescripTextField.setEditable(false);
TypeComboBox.setEnabled(false);
AllowModCheckBox.setEnabled(false);
UniqueCheckBox.setEnabled(false);
//PrimKeyCheckBox.setEnabled(false);
ReqCheckBox.setEnabled(false);
MultivalCheckBox.setEnabled(false);
}
//----------------------------------------------------------------
/**
*
*/
public void EditMode()
{
LabelOperation.setText(MainWin.TT("Update_Attribute"));
AttrNameTextField.setEditable(false);
}
//----------------------------------------------------------------
/**
* @return the Atribute defined
*/
public Record getRecord()
{
return AttrEdit;
}
//----------------------------------------------------------------
/**
 * @param pAttrEdit
*/
public void setRecord(Record pAttrEdit) throws PDException
{
int SelType=2;    
AttrEdit = pAttrEdit;
Attribute Attr;
Attr=AttrEdit.getAttr(PDObjDefs.fATTRNAME); //-----------------------------
AttrNameLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue() != null)
    AttrNameTextField.setText((String) Attr.getValue());
AttrNameTextField.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRUSERNAME); //-----------------------------
AttrUserNameLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue() != null)
    AttrUserNameTextField.setText((String) Attr.getValue());
AttrUserNameTextField.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRDESCRIPTION); //-----------------------------
DescripLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue() != null)
    DescripTextField.setText((String) Attr.getValue());
DescripTextField.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRTYPE); //-------------------
TypeLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue()!=null)
    {
    SelType=(Integer)Attr.getValue();
    TypeComboBox.setSelectedIndex(SelType);
    }
TypeComboBox.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRLONG); //-----------------------------
LongLabel.setText(MainWin.DrvTT(Attr.getUserName()));
MaxLongLabel=LongLabel.getText();
if (Attr.getValue() != null)
    {
    if (SelType==Attribute.tTHES)   
        {
        SelectedThes=new PDThesaur(MainWin.getSession());
        SelectedThes.Load(((Integer) Attr.getValue()).toString());
        getThesSelect().setUseTerm(SelectedThes);
        }
    else
        LongMaxTextField.setText(((Integer) Attr.getValue()).toString());
    }
LongMaxTextField.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRREQUIRED); //-----------------------------
ReqLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue()!=null)
    ReqCheckBox.setSelected((Boolean)Attr.getValue());
ReqCheckBox.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRUNIQUE); //-----------------------------
UniqueLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue()!=null)
    UniqueCheckBox.setSelected((Boolean)Attr.getValue());
UniqueCheckBox.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
//Attr=AttrEdit.getAttr(PDObjDefs.fATTRPRIMKEY); //-----------------------------
//PrimKeyLabel.setText(MainWin.DrvTT(Attr.getUserName()));
//if (Attr.getValue()!=null)
//    PrimKeyCheckBox.setSelected((Boolean)Attr.getValue());
//PrimKeyCheckBox.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRMODALLOW); //-----------------------------
AllowModLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue()!=null)
    AllowModCheckBox.setSelected((Boolean)Attr.getValue());
else
    AllowModCheckBox.setSelected(true);
AllowModCheckBox.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
Attr=AttrEdit.getAttr(PDObjDefs.fATTRMULTIVALUED); //-----------------------------
MultivalLabel.setText(MainWin.DrvTT(Attr.getUserName()));
if (Attr.getValue()!=null)
    MultivalCheckBox.setSelected((Boolean)Attr.getValue());
MultivalCheckBox.setToolTipText(MainWin.DrvTT(Attr.getDescription()));
if (MultivalCheckBox.isSelected())
    {
    TypeComboBox.setSelectedItem("String");    
//    PrimKeyCheckBox.setSelected(true);
//    PrimKeyCheckBox.setEnabled(false);
    UniqueCheckBox.setSelected(true);
    UniqueCheckBox.setEnabled(false);
    ReqCheckBox.setSelected(false);
    ReqCheckBox.setEnabled(false);
    }
else
    {
//    PrimKeyCheckBox.setEnabled(true);
    UniqueCheckBox.setEnabled(true);
    ReqCheckBox.setEnabled(true);
    }
AdaptForm2Type(SelType);
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
/**
 * @return the ThesSelect
 */
public ThesField getThesSelect()
{
if (ThesSelect==null)    
    {
    try {    
        if (SelectedThes==null)
            SelectedThes=new PDThesaur(MainWin.getSession());
        ThesSelect=new ThesField(this, SelectedThes, PDThesaur.ROOTTERM);
        ThesSelect.setBounds(LongMaxTextField.getBounds());
        ThesSelect.setSize(ThesSelect.getWidth()+15, ThesSelect.getHeight()+5);
        this.add(ThesSelect);
    } catch (PDException ex)
        {
        MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
        }
    }
return(ThesSelect);
}
//----------------------------------------------------------------

private void AdaptForm2Type(int SelType)
{
if (SelType==Attribute.tSTRING)
    {
    MultivalCheckBox.setEnabled(true);
    LongMaxTextField.setEnabled(true);
    LongMaxTextField.setVisible(true);
    LongLabel.setVisible(true);
    LongLabel.setText(MaxLongLabel);
    getThesSelect().setVisible(false);
    }
else
    {
    LongMaxTextField.setText("");
    LongMaxTextField.setEnabled(false);   
    LongMaxTextField.setVisible(false);
    getThesSelect().setVisible(false);
    if (SelType==Attribute.tTHES)
        {
        getThesSelect().setVisible(true);
        LongLabel.setVisible(true);
        LongLabel.setText("Thesaur");
        }
    else
        {
        LongLabel.setVisible(false);
        }
    }
}
//----------------------------------------------------------------

    /**
     * @return the ModeDelete
     */
    public boolean isModeDelete()
    {
        return ModeDelete;
    }

    /**
     * @param ModeDelete the ModeDelete to set
     */
    public void setModeDelete(boolean ModeDelete)
    {
        this.ModeDelete = ModeDelete;
    }

}
