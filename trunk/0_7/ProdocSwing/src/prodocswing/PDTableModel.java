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

package prodocswing;

import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import prodoc.*;
import prodocswing.forms.MainWin;

/**
 *
 * @author jhierrot
 */
public class PDTableModel implements TableModel
{
private DriverGeneric Drv=null;
private Record ListFields = null;
private Cursor CursorId = null;
private int NumRows=0;
private Vector ListRes=new Vector();
private SimpleDateFormat formatterTS = MainWin.getFormatterTS();
private SimpleDateFormat formatterDate = MainWin.getFormatterDate();

//-------------------------------------------------------------
/**
 *
 * @param col
 * @return
 */
public String getColumnName(int col)
{
if (ListFields == null)
    return "Col"+col;
else
    {
    try{
    return MainWin.DrvTT(ListFields.getAttr(col).getUserName());
    } catch (PDException ex)
        {
        return "Error"+ex.getLocalizedMessage();
        }
    }
}
//-------------------------------------------------------------
public int getRowCount()
{
return(NumRows);
}
//-------------------------------------------------------------
public int getColumnCount()
{
if (ListFields == null)
    return 1;
else
    return(ListFields.NumAttr());
}
//-------------------------------------------------------------
public Object getValueAt(int row, int col)
{
if (NumRows==0)
    return "";
else
    {
    try {
    Record Rec=(Record) ListRes.elementAt(row);
    Attribute Attr=(Attribute) Rec.getAttr(col);
    if (Attr.getValue()==null)
        return("");
    if (Attr.getType()==Attribute.tTIMESTAMP)
        return( getFormatterTS().format(Attr.getValue()));
    if (Attr.getType()==Attribute.tDATE)
        return( getFormatterDate().format(Attr.getValue()));
    if (Attr.getName().equals(PDACL.fPERMISION))
        return(Permision2String(((Integer)Attr.getValue()).intValue()));
    if (Attr.getName().equals(PDObjDefs.fATTRTYPE))
        return(Atribute2String(((Integer)Attr.getValue()).intValue()));
    return (Attr.getValue().toString());
    } catch (PDException ex)
        {
        return(ex.getLocalizedMessage());
        }
    }
}
//-------------------------------------------------------------
public boolean isCellEditable(int row, int col)
{
return false;
}
//-------------------------------------------------------------
public void setValueAt(Object value, int row, int col)
{

}
//-------------------------------------------------------------
public Class getColumnClass(int col)
{
return String.class;
}
//-------------------------------------------------------------
public void addTableModelListener(TableModelListener l)
{
//throw new UnsupportedOperationException("Not supported yet.");
}
//-------------------------------------------------------------    
public void removeTableModelListener(TableModelListener l)
{
//throw new UnsupportedOperationException("Not supported yet.");
}
//-------------------------------------------------------------
/**
* @return the Drv
*/
public DriverGeneric getDrv()
{
return Drv;
}
//-------------------------------------------------------------
/**
* @param pDrv the Drv to set
*/
public void setDrv(DriverGeneric pDrv)
{
Drv = pDrv;
}
//-------------------------------------------------------------
/**
* @return the ListFields
*/
public Record getListFields()
{
return ListFields;
}
//-------------------------------------------------------------
/**
* @param pListFields the ListFields to set
*/
public void setListFields(Record pListFields)
{
ListFields = pListFields;
}
//-------------------------------------------------------------
/**
* @return the Cursor
*/
public Cursor getCursor()
{
return CursorId;
}
//-------------------------------------------------------------    
/**
 * @param pCursor the Cursor to set
 * @throws PDException 
*/
public void setCursor(Cursor pCursor) throws PDException
{
CursorId = pCursor;
ListRes.clear();
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    ListRes.add(Res);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
NumRows=ListRes.size();
}
//-------------------------------------------------------------
/**
 * 
 * @param n
 * @return
 */
public Record getElement(int n)
{
return((Record)ListRes.elementAt(n));
}
//-------------------------------------------------------------
private Object Permision2String(int Perm)
{
if (Perm==PDACL.pREAD)
    return("READ");
if (Perm==PDACL.pCATALOG)
    return("CATALOG");
if (Perm==PDACL.pVERSION)
    return("VERSION");
if (Perm==PDACL.pUPDATE)
    return("UPDATE");
if (Perm==PDACL.pDELETE)
    return("DELETE");
return("");
}
//---------------------------------------------------------
private Object Atribute2String(int AttrType)
{
if (AttrType==Attribute.tSTRING)
    return("String");
if (AttrType==Attribute.tDATE)
    return("Date");
if (AttrType==Attribute.tTIMESTAMP)
    return("TimeStamp");
if (AttrType==Attribute.tINTEGER)
    return("Integer");
if (AttrType==Attribute.tBOOLEAN)
    return("Boolean");
if (AttrType==Attribute.tFLOAT)
    return("Float");
return("");
}
//---------------------------------------------------------
/**
* @return the formatterTS
*/
public SimpleDateFormat getFormatterTS()
{
return formatterTS;
}
//---------------------------------------------------------
/**
 * @param formatterTS the formatterTS to set
 */
public void setFormatterTS(SimpleDateFormat formatterTS)
{
this.formatterTS = formatterTS;
}
//---------------------------------------------------------
/**
 * @return the formatterDate
 */
public SimpleDateFormat getFormatterDate()
{
return formatterDate;
}
//---------------------------------------------------------
/**
 * @param formatterDate the formatterDate to set
 */
public void setFormatterDate(SimpleDateFormat formatterDate)
{
this.formatterDate = formatterDate;
}
//---------------------------------------------------------
/**
 * Used to edit in-memory list
 * @param AttrList 
 */
public void setVector(Vector AttrList)
{
//ListRes.clear();
//for (int i = 0; i < AttrList.size(); i++)
//    {
//    ListRes.add(AttrList.elementAt(i));
//    }
ListRes=AttrList;
NumRows=ListRes.size();
}
//---------------------------------------------------------
}
