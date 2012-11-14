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
 * MainWin.java
 *
 * Created on 29-jul-2012, 9:52:00
 */

package prodocswing.forms;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import prodoc.*;
import prodocswing.TreeTerm;

/**
 *
 * @author jhierrot
 */
public class MainThesaur extends javax.swing.JFrame
{
private static DriverGeneric Session=null;
static private DefaultTreeModel TermTreeModel=null;
static private String ActTermId=null;
private PDThesaur TermAct=null;
//static private String List=PDThesaur.fACL+"/"+PDThesaur.fFOLDTYPE+"/"+PDThesaur.fPARENTID+"/"+PDThesaur.fPDID+"/"+PDThesaur.fTITLE+"/"+PDThesaur.fPDAUTOR+"/"+PDThesaur.fPDDATE;

//--------------------------------------------------------
/**
* @return the Session
*/
public static DriverGeneric getSession()
{
return Session;
}
//--------------------------------------------------------
/**
 * @param pSess Session to be assigned
 */
public static void setSession(DriverGeneric pSess)
{
Session=pSess;
}
//--------------------------------------------------------
/** Creates new form MainWin
 * @param pSess 
 */
public MainThesaur(DriverGeneric pSess)
{
setSession(pSess);    
initComponents();
TreeTerm.setPreferredSize(null);
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        TreeTerm = new javax.swing.JTree();
        jSplitPane2 = new javax.swing.JSplitPane();
        TermAttr = new javax.swing.JPanel();
        NameLabel = new javax.swing.JLabel();
        NameTextField = new javax.swing.JTextField();
        DescripLabel = new javax.swing.JLabel();
        DescripTextField = new javax.swing.JTextField();
        UseLabel = new javax.swing.JLabel();
        UseTextField = new javax.swing.JTextField();
        Relations = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        NTjTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        RTjTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        UFjTable = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        ThesaurMenu = new javax.swing.JMenu();
        AddThesaur = new javax.swing.JMenuItem();
        DelThesaur = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        AddTerm = new javax.swing.JMenuItem();
        ModTerm = new javax.swing.JMenuItem();
        DelTerm = new javax.swing.JMenuItem();
        RefreshTerm = new javax.swing.JMenuItem();
        SearchTerm = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        ThesaurHelp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OpenProdoc Thesaurus");
        setIconImage(getIcon());

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setDividerSize(4);

        TreeTerm.setFont(getFontTree());
        TreeTerm.setModel(getTreeModel());
        TreeTerm.setAlignmentX(0.3F);
        TreeTerm.setAutoscrolls(true);
        TreeTerm.setComponentPopupMenu(ThesaurMenu.getComponentPopupMenu());
        TreeTerm.setMaximumSize(new java.awt.Dimension(400, 76));
        TreeTerm.setMinimumSize(new java.awt.Dimension(200, 60));
        TreeTerm.setPreferredSize(new java.awt.Dimension(200, 76));
        TreeTerm.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener()
        {
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt)
            {
            }
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt)
            {
                TreeTermTreeExpanded(evt);
            }
        });
        TreeTerm.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener()
        {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt)
            {
                TreeTermValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(TreeTerm);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jSplitPane2.setDividerLocation(160);
        jSplitPane2.setDividerSize(4);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        TermAttr.setMinimumSize(new java.awt.Dimension(100, 100));

        NameLabel.setFont(MainWin.getFontDialog());
        NameLabel.setText("Name");

        NameTextField.setEditable(false);
        NameTextField.setFont(MainWin.getFontDialog());

        DescripLabel.setFont(MainWin.getFontDialog());
        DescripLabel.setText("Descrip");

        DescripTextField.setEditable(false);
        DescripTextField.setFont(MainWin.getFontDialog());

        UseLabel.setFont(MainWin.getFontDialog());
        UseLabel.setText("Use");

        UseTextField.setEditable(false);
        UseTextField.setFont(MainWin.getFontDialog());

        javax.swing.GroupLayout TermAttrLayout = new javax.swing.GroupLayout(TermAttr);
        TermAttr.setLayout(TermAttrLayout);
        TermAttrLayout.setHorizontalGroup(
            TermAttrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TermAttrLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TermAttrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TermAttrLayout.createSequentialGroup()
                        .addComponent(NameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(TermAttrLayout.createSequentialGroup()
                        .addComponent(DescripLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DescripTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(TermAttrLayout.createSequentialGroup()
                        .addComponent(UseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        TermAttrLayout.setVerticalGroup(
            TermAttrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TermAttrLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(TermAttrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NameLabel))
                .addGap(18, 18, 18)
                .addGroup(TermAttrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DescripTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DescripLabel))
                .addGap(18, 18, 18)
                .addGroup(TermAttrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UseLabel))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(TermAttr);

        jPanel1.setFont(MainWin.getFontDialog());

        NTjTable.setFont(MainWin.getFontList());
        NTjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(NTjTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        );

        Relations.addTab("NT", jPanel1);

        jPanel2.setFont(MainWin.getFontDialog());

        RTjTable.setFont(MainWin.getFontList());
        RTjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(RTjTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        );

        Relations.addTab("RT", jPanel2);

        jPanel3.setFont(MainWin.getFontDialog());

        UFjTable.setFont(MainWin.getFontList());
        UFjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(UFjTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        );

        Relations.addTab("UF", jPanel3);

        jSplitPane2.setRightComponent(Relations);

        jSplitPane1.setRightComponent(jSplitPane2);

        menuBar.setFont(getFontMenu());

        ThesaurMenu.setText(MainWin.DrvTT("Thesaurus"));
        ThesaurMenu.setFont(getFontMenu());

        AddThesaur.setFont(getFontMenu());
        AddThesaur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png"))); // NOI18N
        AddThesaur.setText(MainWin.DrvTT("Create_Theusurus"));
        AddThesaur.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddThesaurActionPerformed(evt);
            }
        });
        ThesaurMenu.add(AddThesaur);

        DelThesaur.setFont(getFontMenu());
        DelThesaur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/del.png"))); // NOI18N
        DelThesaur.setText(MainWin.DrvTT("Delete_Thesaurus"));
        DelThesaur.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                DelThesaurActionPerformed(evt);
            }
        });
        ThesaurMenu.add(DelThesaur);
        ThesaurMenu.add(jSeparator2);

        AddTerm.setFont(getFontMenu());
        AddTerm.setText(MainWin.DrvTT("Add_Term"));
        AddTerm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddTermActionPerformed(evt);
            }
        });
        ThesaurMenu.add(AddTerm);

        ModTerm.setFont(getFontMenu());
        ModTerm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/edit.png"))); // NOI18N
        ModTerm.setText(MainWin.DrvTT("Update_Term"));
        ModTerm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ModTermActionPerformed(evt);
            }
        });
        ThesaurMenu.add(ModTerm);

        DelTerm.setFont(getFontMenu());
        DelTerm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/del.png"))); // NOI18N
        DelTerm.setText(MainWin.DrvTT("Delete_Term"));
        DelTerm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                DelTermActionPerformed(evt);
            }
        });
        ThesaurMenu.add(DelTerm);

        RefreshTerm.setFont(getFontMenu());
        RefreshTerm.setText(MainWin.DrvTT("Refresh"));
        RefreshTerm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                RefreshTermActionPerformed(evt);
            }
        });
        ThesaurMenu.add(RefreshTerm);

        SearchTerm.setFont(getFontMenu());
        SearchTerm.setText(MainWin.DrvTT("Search_Terms"));
        SearchTerm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                SearchTermActionPerformed(evt);
            }
        });
        ThesaurMenu.add(SearchTerm);
        ThesaurMenu.add(jSeparator6);

        exitMenuItem.setFont(getFontMenu());
        exitMenuItem.setText(MainWin.DrvTT("Close_Window"));
        exitMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitMenuItemActionPerformed(evt);
            }
        });
        ThesaurMenu.add(exitMenuItem);

        menuBar.add(ThesaurMenu);

        helpMenu.setText(MainWin.DrvTT("Help"));
        helpMenu.setFont(getFontMenu());

        ThesaurHelp.setFont(getFontMenu());
        ThesaurHelp.setText(MainWin.DrvTT("Thesaurus_MainWin"));
        ThesaurHelp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ThesaurHelpActionPerformed(evt);
            }
        });
        helpMenu.add(ThesaurHelp);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
this.dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void TreeTermTreeExpanded(javax.swing.event.TreeExpansionEvent evt)//GEN-FIRST:event_TreeTermTreeExpanded
    {//GEN-HEADEREND:event_TreeTermTreeExpanded
DefaultMutableTreeNode TreeFold = (DefaultMutableTreeNode) evt.getPath().getLastPathComponent();
if ( ((TreeTerm) TreeFold.getUserObject()).isExpanded())
    return;
ExpandFold(TreeFold);
    }//GEN-LAST:event_TreeTermTreeExpanded

    private void TreeTermValueChanged(javax.swing.event.TreeSelectionEvent evt)//GEN-FIRST:event_TreeTermValueChanged
    {//GEN-HEADEREND:event_TreeTermValueChanged
try {
DefaultMutableTreeNode TreeFold = (DefaultMutableTreeNode) evt.getPath().getLastPathComponent();
TermAct= ((TreeTerm) TreeFold.getUserObject()).getTerm();
ActTermId=TermAct.getPDId();
this.NameTextField.setText(TermAct.getName());
this.DescripTextField.setText(TermAct.getDescription());
this.UseTextField.setText(TermAct.getUse());
} catch (Exception ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_TreeTermValueChanged

    private void AddThesaurActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddThesaurActionPerformed
    {//GEN-HEADEREND:event_AddThesaurActionPerformed
try {
MantThes MTF = new MantThes(this, true);
MTF.setLocationRelativeTo(null);
MTF.setRecord(PDThesaur.getRecordStructPDThesaur());
MTF.AddMode();
MTF.setVisible(true);
if (MTF.isCancel())
    return;
PDThesaur Term=new PDThesaur(Session);
Term.assignValues(MTF.getRecord());
Term.setParentId(PDThesaur.ROOTTERM);
Term.insert();
TreePath ActualPath = TreeTerm.getSelectionPath();
ExpandFold((DefaultMutableTreeNode)ActualPath.getLastPathComponent());
TreeTerm.setSelectionPath(ActualPath);
} catch (Exception ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_AddThesaurActionPerformed

    private void DelThesaurActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_DelThesaurActionPerformed
    {//GEN-HEADEREND:event_DelThesaurActionPerformed
try {
TreePath selectionPath = TreeTerm.getSelectionPath();
DefaultMutableTreeNode TreeFold = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
PDThesaur Term= ((TreeTerm) TreeFold.getUserObject()).getTerm();
MantThes MTF = new MantThes(this, true);
MTF.setLocationRelativeTo(null);
MTF.DelMode();
Term.Load(ActTermId);
MTF.setRecord(Term.getRecord());
MTF.setVisible(true);
if (MTF.isCancel())
    return;
ActTermId=Term.getParentId();
Term.delete();
TreePath ParentFold = (TreePath) TreeTerm.getSelectionPath().getParentPath();
ExpandFold((DefaultMutableTreeNode)ParentFold.getLastPathComponent());
TreeTerm.setSelectionPath(selectionPath.getParentPath());
TreeFold = (DefaultMutableTreeNode) selectionPath.getParentPath().getLastPathComponent();
TermAct= ((TreeTerm) TreeFold.getUserObject()).getTerm();
} catch (Exception ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_DelThesaurActionPerformed

    private void AddTermActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddTermActionPerformed
    {//GEN-HEADEREND:event_AddTermActionPerformed
try {
MantTerm MTF = new MantTerm(this, true);
MTF.setLocationRelativeTo(null);
MTF.setRecord(PDThesaur.getRecordStructPDThesaur());
MTF.AddMode();
MTF.setVisible(true);
if (MTF.isCancel())
    return;
PDThesaur Term=new PDThesaur(Session);
Term.assignValues(MTF.getRecord());
Term.setParentId(ActTermId);
Term.insert();
TreePath ActualPath = TreeTerm.getSelectionPath();
ExpandFold((DefaultMutableTreeNode)ActualPath.getLastPathComponent());
TreeTerm.setSelectionPath(ActualPath);
} catch (Exception ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_AddTermActionPerformed

    private void ModTermActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ModTermActionPerformed
    {//GEN-HEADEREND:event_ModTermActionPerformed
try {
TreePath selectionPath = TreeTerm.getSelectionPath();
DefaultMutableTreeNode TreeFold = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
PDThesaur Fold= ((TreeTerm) TreeFold.getUserObject()).getTerm();
MantTerm MTF = new MantTerm(this, true);
MTF.setLocationRelativeTo(null);
MTF.EditMode();
Fold.Load(Fold.getPDId());
MTF.setRecord(Fold.getRecord());
MTF.setVisible(true);
if (MTF.isCancel())
    return;
Fold.assignValues(MTF.getRecord());
Fold.update();
TreePath ParentFold = (TreePath) TreeTerm.getSelectionPath().getParentPath();
ExpandFold((DefaultMutableTreeNode)ParentFold.getLastPathComponent());
TreeTerm.setSelectionPath(selectionPath);
} catch (Exception ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
    }//GEN-LAST:event_ModTermActionPerformed

    private void RefreshTermActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_RefreshTermActionPerformed
    {//GEN-HEADEREND:event_RefreshTermActionPerformed
DefaultMutableTreeNode TreeFold = (DefaultMutableTreeNode) TreeTerm.getSelectionPath().getLastPathComponent();
ExpandFold(TreeFold);
    }//GEN-LAST:event_RefreshTermActionPerformed

    private void SearchTermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchTermActionPerformed
SearchFold SF = new SearchFold(this, true);
SF.setLocationRelativeTo(null);
SF.setFoldAct(ActTermId);
SF.setVisible(true);     
    }//GEN-LAST:event_SearchTermActionPerformed

    private void ThesaurHelpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ThesaurHelpActionPerformed
    {//GEN-HEADEREND:event_ThesaurHelpActionPerformed
MainWin.Execute("doc"+MainWin.OSSep()+MainWin.getLang()+MainWin.OSSep()+"ThesaurWin.html");   
    }//GEN-LAST:event_ThesaurHelpActionPerformed

private void DelTermActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_DelTermActionPerformed
{//GEN-HEADEREND:event_DelTermActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_DelTermActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AddTerm;
    private javax.swing.JMenuItem AddThesaur;
    private javax.swing.JMenuItem DelTerm;
    private javax.swing.JMenuItem DelThesaur;
    private javax.swing.JLabel DescripLabel;
    private javax.swing.JTextField DescripTextField;
    private javax.swing.JMenuItem ModTerm;
    private javax.swing.JTable NTjTable;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JTextField NameTextField;
    private javax.swing.JTable RTjTable;
    private javax.swing.JMenuItem RefreshTerm;
    private javax.swing.JTabbedPane Relations;
    private javax.swing.JMenuItem SearchTerm;
    private javax.swing.JPanel TermAttr;
    private javax.swing.JMenuItem ThesaurHelp;
    private javax.swing.JMenu ThesaurMenu;
    private javax.swing.JTree TreeTerm;
    private javax.swing.JTable UFjTable;
    private javax.swing.JLabel UseLabel;
    private javax.swing.JTextField UseTextField;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

//---------------------------------------------------------------------
/**
 * 
 * @return
 */
static public Font getFontMenu()
{
return (MainWin.getFontMenu());
}
//---------------------------------------------------------------------
/**
 * 
 * @return
 */
static public Font getFontTree()
{
return (MainWin.getFontTree());
}
//---------------------------------------------------------------------
/**
 * 
 * @return
 */
static public Font getFontList()
{
return (MainWin.getFontList());
}
//---------------------------------------------------------------------
/**
 * 
 * @return
 */
static public Font getFontDialog()
{
return (MainWin.getFontDialog());
}
//---------------------------------------------------------------------
/**
 * 
 * @param RecomFileName
 * @param Ext
 * @param Save
 * @return
 */
static public String SelectDestination(String RecomFileName, String Ext, boolean Save)
{
JFileChooser fc = new JFileChooser();
//fc.setDialogTitle("Seleccionar Archivo");
if (Ext!=null)
    fc.setFileFilter(new FileNameExtensionFilter("file "+Ext, Ext));
if (RecomFileName!=null)
    fc.setSelectedFile(new File(RecomFileName));
if (!Save)
    {
    if (fc.showOpenDialog(null)!=JFileChooser.APPROVE_OPTION)
        return("");
    }
else
    {
    if (fc.showSaveDialog(null)!=JFileChooser.APPROVE_OPTION)
        return("");
    }
return(fc.getSelectedFile().getAbsolutePath());
}
//---------------------------------------------------------------------
/**
 * 
 * @param RecomFileName
 * @return
 */
static public String SelectFolderDestination(String RecomFileName)
{
JFileChooser fc = new JFileChooser();
if (RecomFileName!=null)
    fc.setSelectedFile(new File(RecomFileName));
fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
if (fc.showSaveDialog(null)!=JFileChooser.APPROVE_OPTION)
    return("");
return(fc.getSelectedFile().getAbsolutePath());
}
//---------------------------------------------------------------------
private TreeModel getTreeModel()
{
if (TermTreeModel==null)
    {
    PDThesaur RootFolder=null;
    try {
    RootFolder = new PDThesaur(Session);
    RootFolder.Load(PDThesaur.ROOTTERM);
    TreeTerm TF=new TreeTerm(RootFolder);
    DefaultMutableTreeNode RootTreeFolder = new DefaultMutableTreeNode(TF);
    TermTreeModel=new DefaultTreeModel(RootTreeFolder);
    ExpandFold(RootTreeFolder);
    } catch (PDException ex)
        {
        MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
        return(null);
        }
    }
return(TermTreeModel);
}
//---------------------------------------------------------------------
private void ExpandFold(DefaultMutableTreeNode ChildTreeFolder)
{
try {
ChildTreeFolder.removeAllChildren();
PDThesaur Fold= ((TreeTerm) ChildTreeFolder.getUserObject()).getTerm();
HashSet Child =Fold.getListDirectDescendList(Fold.getPDId());
for (Iterator it = Child.iterator(); it.hasNext();)
    {
    String ChildId=(String)it.next();
    if (ChildId.compareTo(Fold.getPDId())==0)
        continue;
    PDThesaur ChildFolder=new PDThesaur(Session);
    ChildFolder.Load(ChildId);
    TreeTerm TFc=new TreeTerm(ChildFolder);
    DefaultMutableTreeNode ChildTreeFolder2=new DefaultMutableTreeNode(TFc);
    DefaultMutableTreeNode ChildTreeFolder3=new DefaultMutableTreeNode(null);
    ChildTreeFolder2.add(ChildTreeFolder3);
    ChildTreeFolder.add(ChildTreeFolder2);
    }
(((TreeTerm) ChildTreeFolder.getUserObject())).setExpanded(true);
TermTreeModel.reload(ChildTreeFolder);
TreeTerm.setPreferredSize(null);
} catch (PDException ex)
    {
    MainWin.Message(MainWin.DrvTT(ex.getLocalizedMessage()));
    }
}
//---------------------------------------------------------------------
/**
 * 
 * @param Title
 * @param FieldName
 * @param ToolTip
 * @param FieldText
 * @return
 */
static public String DialogReadString(String Title, String FieldName, String ToolTip, String FieldText)
{
DialogReadString dialog = new DialogReadString(new javax.swing.JFrame(), true);
dialog.setLocationRelativeTo(null);
dialog.SetVals(Title, FieldName, ToolTip, FieldText);
dialog.setVisible(true);
if (dialog.getReturnStatus() == DialogReadString.RET_CANCEL)
    return null;
else
    return(dialog.getField());
}
//---------------------------------------------------------------------
static private Image getIcon()
{
ImageIcon PDIcon=new ImageIcon("resources/LogoProdoc.jpg");
return PDIcon.getImage();
}
//---------------------------------------------------------------------
}
