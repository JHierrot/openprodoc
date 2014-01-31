/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prodocswing.forms;

/**
 *
 * @author jhierrot
 */
public class TCBase extends javax.swing.JDialog
{
private boolean Cancel;
private String Param;
private String Param2;
private String Param3;
private String Param4;

/** Creates new form MantUsers
 * @param parent 
 * @param modal
 */
public TCBase(javax.swing.JDialog parent, boolean modal)
{
super(parent, modal);
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
 * @param Cancel the Cancel to set
 */
public void setCancel(boolean Cancel)
{
this.Cancel = Cancel;
}
//----------------------------------------------------------------
/**
 * @return the Param
 */
public String getParam()
{
return Param;
}
//------------------------------------------------   
/**
 * @param Param the Param to set
 */
public void setParam(String Param)
{
this.Param = Param;
}
//------------------------------------------------   
/**
 * @return the Param2
 */
public String getParam2()
{
return Param2;
}
//------------------------------------------------   
/**
 * @param Param2 the Param2 to set
 */
public void setParam2(String Param2)
{
this.Param2 = Param2;
}
//------------------------------------------------   
/**
 * @return the Param3
 */
public String getParam3()
{
return Param3;
}
//------------------------------------------------   
/**
 * @param Param3 the Param3 to set
 */
public void setParam3(String Param3)
{
this.Param3 = Param3;
}
//------------------------------------------------   
/**
 * @return the Param4
 */
public String getParam4()
{
return Param4;
}
//------------------------------------------------   
/**
 * @param Param4 the Param4 to set
 */
public void setParam4(String Param4)
{
this.Param4 = Param4;
}
//------------------------------------------------   
}
