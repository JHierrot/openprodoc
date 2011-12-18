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

package prodocUI.forms;


import html.*;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDACL;
import prodoc.PDException;
import prodoc.PDMimeType;
import prodoc.PDObjDefs;
import prodoc.Record;
import prodocUI.servlet.SParent;

/**
 *
 * @author jhierrot
 */
public class FFormBase extends Page
{
static final public int ADDMOD=0;
static final public int DELMOD=1;
static final public int EDIMOD=2;
public int Mode=0;
private Record Rec;
public FieldButton2 OkButton;
public FieldButton2 CancelButton;
DriverGeneric Session;
public FieldText Status;
public HiperlinkImag HHelp;

/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pTitulo
 * @param pMode
 * @param pRec 
 */
public FFormBase(HttpServletRequest Req, String pTitulo, int pMode, Record pRec)
{
super(Req, pTitulo, "");
Session = getSession();
Mode=pMode;
Rec=pRec;
AddCSS("prodoc.css");
OkButton=new FieldButton2(TT("Ok"), "BOk");
OkButton.setCSSClass("FFormInputButton");
CancelButton=new FieldButton2(TT("Cancel"), "BCancel");
CancelButton.setCSSClass("FFormInputButton");
Status=new FieldText("Status");
Status.setCSSClass("FFormularios");
Status.setActivado(false);
Status.setSize(128);
HHelp=new HiperlinkImag("img/"+getStyle()+"help.jpg", "Help", "help/"+SParent.getLang(Req)+"/"+getFormHelp()+".html", "");
HHelp.setTarget("_blank");
}
//------------------------------------------------------------------------------
static public Record getRecord(HttpServletRequest Req)
{
return null;
}
//------------------------------------------------------------------------------
/*
 *
 */
protected void FillAcl(FieldCombo ListACL) throws PDException
{
PDACL LAcl=new PDACL(Session);
Cursor CursorAcl=LAcl.getAll();
Record Res=Session.NextRec(CursorAcl);
while (Res!=null)
    {
    Attribute Name=Res.getAttr(PDACL.fNAME);
    Attribute Desc=Res.getAttr(PDACL.fDESCRIPTION);
    ListACL.AddOpcion((String) Name.getValue(), (String) Desc.getValue());
    Res=Session.NextRec(CursorAcl);
    }
Session.CloseCursor(CursorAcl);
}
//------------------------------------------------------------------------------
/*
 *
 */
protected void FillMime(FieldCombo ListMime) throws PDException
{
PDMimeType LMime=new PDMimeType(Session);
Cursor CursorMime=LMime.getAll();
Record Res=Session.NextRec(CursorMime);
while (Res!=null)
    {
    Attribute Name=Res.getAttr(PDACL.fNAME);
    Attribute Desc=Res.getAttr(PDACL.fDESCRIPTION);
    ListMime.AddOpcion((String) Name.getValue(), (String) Desc.getValue());
    Res=Session.NextRec(CursorMime);
    }
Session.CloseCursor(CursorMime);
}
//------------------------------------------------------------------------------
/**
 * Fills the combo with a list of Folder types or doc types (allowed to the user
 * @param ListTip Field to be filled
 * @param Fold true when searching for folder types.
 * @throws PDException in any error.
 */
protected void FillTip(FieldCombo ListTip, boolean Fold) throws PDException
{
PDObjDefs Defs=new PDObjDefs(Session);
Cursor CursorId;
if (Fold)
    CursorId=Defs.getListFold();
else
    CursorId=Defs.getListDocs();
Record Res=Session.NextRec(CursorId);
while (Res!=null)
    {
    Attribute Name=Res.getAttr(PDObjDefs.fNAME);
    Attribute Desc=Res.getAttr(PDObjDefs.fDESCRIPTION);
    ListTip.AddOpcion((String) Name.getValue(), (String) Desc.getValue());
    Res=Session.NextRec(CursorId);
    }
Session.CloseCursor(CursorId);
}
//------------------------------------------------------------------------------
protected String getFormHelp()
{
return("HelpIndex");
}
//------------------------------------------------------------------------------
}
