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
 * author: Joaquin Hierro      2016
 * 
 */

package OpenProdocServ;

import static OpenProdocServ.ListElem.GenObj;
import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.TT;
import java.io.PrintWriter;
import javax.servlet.http.*;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.ObjPD;
import prodoc.PDACL;
import prodoc.PDCustomization;
import prodoc.PDException;
import prodoc.PDLog;
import prodoc.PDMimeType;
import prodoc.PDRoles;
import prodoc.PDUser;
import prodoc.Record;


/**
 *
 * @author jhierrot
 */
public class MantElem extends SParent
{
public static final String OPERNEW="New";   
public static final String OPERMODIF="Modif";
public static final String OPERDELETE="Delete";
public static final String OPERCOPY="Copy";

private static final String List2=ObjPD.fPDAUTOR+"/"+ObjPD.fPDDATE;

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req 
 * @param response
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
String Oper=Req.getParameter("Oper");
String TypeElem=Req.getParameter("Ty");
String Id=Req.getParameter("Id");
String Filter=Req.getParameter("Fil");
if (TypeElem!=null && TypeElem.length()!=0)
    {
    if (Oper.equals(OPERNEW))  
        out.println(ElemNew(Req, TypeElem));
    else if (Oper.equals(OPERMODIF))
        out.println(ElemMod(Req, TypeElem, Id));
    else if (Oper.equals(OPERDELETE))
        out.println(ElemDel(Req, TypeElem, Id));
    else if (Oper.equals(OPERCOPY))
        out.println(ElemCopy(Req, TypeElem, Id));
    else if (Oper.equals("Import"))
        out.println(ElemImport(Req, TypeElem));
    }
else
    {
    DriverGeneric PDSession=null;    
    try {    
    TypeElem=Req.getParameter("Type");
    Id=Req.getParameter("Id");
    PDSession=SParent.getSessOPD(Req);    
    ObjPD Obj=GenObj(TypeElem, PDSession, Id);
    Record Rec=Obj.getRecord();
    Rec.initList();
    Attribute Attr=Rec.nextAttr();
    while (Attr!=null)
        {
        if (!List2.contains(Attr.getName()))
            {
            String Val=Req.getParameter(Attr.getName());
            if (Attr.getType()==Attribute.tBOOLEAN)
                {
                if(Val == null || Val.length()==0 || Val.equals("0"))
                    Attr.setValue(false);
                else
                    Attr.setValue(true);
                }
            else if (Attr.getType()==Attribute.tTHES)
                {
                Val=Req.getParameter("TH_"+Attr.getName());    
                SParent.FillAttr(Req, Attr, Val, false);
                }
            else if(Val != null)
                {
                SParent.FillAttr(Req, Attr, Val, false);
                }
            }
        Attr=Rec.nextAttr();
        }
    Obj.assignValues(Rec);
    PDSession.IniciarTrans();
    if (Oper.equals(OPERNEW) || Oper.equals(OPERCOPY)) 
        {
        Obj.insert();
        if (TypeElem.equals(ListElem.MANTACL))            
            InsertACLMembers((PDACL)Obj,Req);
        }
    else if (Oper.equals(OPERMODIF))
        {
        Obj.update();
        if (TypeElem.equals(ListElem.MANTACL)) 
            {
            PDACL Acl=((PDACL)Obj);    
            Acl.DelAllGroups();
            Acl.DelAllUsers();
            InsertACLMembers((PDACL)Obj,Req);
            }
        }
    else if (Oper.equals(OPERDELETE))
        Obj.delete();
    PDSession.CerrarTrans();
    out.println("OK");
    } catch(Exception Ex)
        {
        if (PDSession!=null)    
            PDSession.AnularTrans();
        out.println(Ex.getLocalizedMessage());
        }
    }
}
//-----------------------------------------------------------------------------------------------

/**
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "MantElem Servlet";
}
//-----------------------------------------------------------------------------------------------
static public String getUrlServlet()
{
return("MantElem");
}
//-----------------------------------------------------------------------------------------------
private String ElemNew(HttpServletRequest Req, String TypeElem) throws PDException
{
StringBuilder SB=new StringBuilder(1000);
SB.append("[  {type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 230},");
SB.append("{type: \"label\", label: \"").append(TT(Req, GetTitleNew(Req, TypeElem))).append("\"},");
SB.append(getBody("New", Req, TypeElem, null));
SB.append(OkBlock(Req, TypeElem, null));
return(SB.toString());
}
//-----------------------------------------------------------------------------------------------

private String ElemMod(HttpServletRequest Req, String TypeElem, String Id) throws PDException
{
StringBuilder SB=new StringBuilder(1000);
SB.append("[  {type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 230},");
SB.append("{type: \"label\", label: \"").append(TT(Req, GetTitleModif(Req, TypeElem))).append("\"},");
SB.append(getBody("Modif", Req, TypeElem, Id));
SB.append(OkBlock(Req, TypeElem, Id));
return(SB.toString());
}
//-----------------------------------------------------------------------------------------------

private String ElemDel(HttpServletRequest Req, String TypeElem, String Id) throws PDException
{
StringBuilder SB=new StringBuilder(1000);
SB.append("[  {type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 230},");
SB.append("{type: \"label\", label: \"").append(TT(Req, GetTitleDel(Req, TypeElem))).append("\"},");
SB.append(getBody("Delete", Req, TypeElem, Id));
SB.append(OkBlock(Req, TypeElem, Id));
return(SB.toString());
}
//-----------------------------------------------------------------------------------------------

private String ElemCopy(HttpServletRequest Req, String TypeElem, String Id) throws PDException
{
StringBuilder SB=new StringBuilder(1000);
SB.append("[  {type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 230},");
SB.append("{type: \"label\", label: \"").append(TT(Req, GetTitleCopy(Req, TypeElem))).append("\"},");
SB.append(getBody("Copy", Req, TypeElem, Id));
SB.append(OkBlock(Req, TypeElem, Id+"1"));
return(SB.toString());
}
//-----------------------------------------------------------------------------------------------
private String ElemImport(HttpServletRequest Req, String TypeElem)
{
throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}
//-----------------------------------------------------------------------------------------------
private String GetTitleNew(HttpServletRequest Req,String ElemType)
{
if (ElemType.equals(ListElem.MANTACL))
    return(TT(Req, "Add_ACL"));
else if (ElemType.equals(ListElem.MANTGROUPS))
    return(TT(Req, "Add_Group"));
else if (ElemType.equals(ListElem.MANTUSERS))
    return(TT(Req, "Add_User"));
else if (ElemType.equals(ListElem.MANTROLES))
    return(TT(Req, "Add_Role"));
else if (ElemType.equals(ListElem.MANTMIME))
    return(TT(Req, "Add_Mime_Type"));
else if (ElemType.equals(ListElem.MANTREPO))
    return(TT(Req, "Add_Repository"));
else if (ElemType.equals(ListElem.MANTOBJ))
    return(TT(Req, "Add_Object_definition"));
else if (ElemType.equals(ListElem.MANTAUTH))
    return(TT(Req, "Add_Authenticator"));
else if (ElemType.equals(ListElem.MANTCUST))
    return(TT(Req, "Add_Customization"));
return("Error");
}
//-----------------------------------------------------------------------------------------------
private StringBuilder OkBlock(HttpServletRequest Req,String TypeElem, String Id)
{
StringBuilder SB=new StringBuilder(500);    
SB.append("{type: \"block\", width: 250, list:[");
SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
SB.append("{type: \"newcolumn\", offset:20 },");
SB.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
SB.append("{type: \"hidden\", name:\"Type\", value: \"").append(TypeElem).append("\"}");
if (Id!=null)
    SB.append(",{type: \"hidden\", name:\"Id\", value: \"").append(Id).append("\"}");
SB.append("]} ];");
return(SB);
}
//-----------------------------------------------------------------------------------------------
private StringBuilder getBody(String Oper, HttpServletRequest Req, String ElemType, String Id) throws PDException
{
StringBuilder SB=new StringBuilder(2000);
DriverGeneric PDSession=SParent.getSessOPD(Req);    
ObjPD Obj=GenObj(ElemType, PDSession, Id);
Record Rec=Obj.getRecord();
Attribute Attr;
if (Oper.equals(OPERCOPY))
    {
    Attr=Rec.getAttr(PDUser.fNAME);
    Attr.setValue(((String)Attr.getValue())+"1");
    }
boolean Modif=(Oper.equals(OPERMODIF));
boolean ReadOnly=(Oper.equals(OPERDELETE));
if (ElemType.equals(ListElem.MANTACL))
    {
    Attr=Rec.getAttr(PDACL.fNAME);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif));
    Attr=Rec.getAttr(PDACL.fDESCRIPTION);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif)); 
    SB.append("{type: \"hidden\", name:\"Users\", value: \"").append(GenACLUsers(PDSession, Id)).append("\"},");
    SB.append("{type: \"hidden\", name:\"Groups\", value: \"").append(GenACLGroups(PDSession, Id)).append("\"},");
    }
else if (ElemType.equals(ListElem.MANTGROUPS))
    {
    
    }
else if (ElemType.equals(ListElem.MANTUSERS))
    {
    
    }
else if (ElemType.equals(ListElem.MANTROLES))
    {
    Attr=Rec.getAttr(PDRoles.fNAME);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fDESCRIPTION);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    SB.append("{type: \"block\", width: 550, list:[");
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEACL);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEAUTH);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATECUSTOM);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEDOC);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEFOLD);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEGROUP);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEMIME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEOBJECT);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEREPOS);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEROLE);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATETASK);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATETHESAUR);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWCREATEUSER);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    SB.append("{type: \"newcolumn\"},");
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINACL);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINAUTH);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINCUSTOM);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINDOC);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINFOLD);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINGROUP);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINMIME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINOBJECT);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINREPOS);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINROLE);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINTASK);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINTHESAUR);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRoles.fALLOWMAINTAINUSER);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));    
    SB.deleteCharAt(SB.length()-1);
    SB.append("]},");
    }
else if (ElemType.equals(ListElem.MANTMIME))
    {
    Attr=Rec.getAttr(PDMimeType.fNAME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDMimeType.fDESCRIPTION);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDMimeType.fMIMECODE);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    }
else if (ElemType.equals(ListElem.MANTREPO))
    {
    
    }
else if (ElemType.equals(ListElem.MANTOBJ))
    {
    
    }
else if (ElemType.equals(ListElem.MANTAUTH))
    {
    
    }
else if (ElemType.equals(ListElem.MANTCUST))
    {
    Attr=Rec.getAttr(PDCustomization.fNAME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDCustomization.fDESCRIPTION);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDCustomization.fLANGUAGE);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDCustomization.fDATEFORMAT);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDCustomization.fTIMEFORMAT);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDCustomization.fSTYLE);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDCustomization.fSWINGSTYLE);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    }
return(SB);
}
//-----------------------------------------------------------------------------------------------    
private String GetTitleModif(HttpServletRequest Req, String ElemType)
{
if (ElemType.equals(ListElem.MANTACL))
    return(TT(Req, "Update_ACL"));
else if (ElemType.equals(ListElem.MANTGROUPS))
    return(TT(Req, "Update_Group"));
else if (ElemType.equals(ListElem.MANTUSERS))
    return(TT(Req, "Update_User"));
else if (ElemType.equals(ListElem.MANTROLES))
    return(TT(Req, "Update_Role"));
else if (ElemType.equals(ListElem.MANTMIME))
    return(TT(Req, "Update_Mime_Type"));
else if (ElemType.equals(ListElem.MANTREPO))
    return(TT(Req, "Update_Repository"));
else if (ElemType.equals(ListElem.MANTOBJ))
    return(TT(Req, "Update_Object_definition"));
else if (ElemType.equals(ListElem.MANTAUTH))
    return(TT(Req, "Update_Authenticator"));
else if (ElemType.equals(ListElem.MANTCUST))
    return(TT(Req, "Update_Customization"));
return("Error");
}
//-----------------------------------------------------------------------------------------------
private String GetTitleDel(HttpServletRequest Req, String ElemType)
{
if (ElemType.equals(ListElem.MANTACL))
    return(TT(Req, "Delete_ACL"));
else if (ElemType.equals(ListElem.MANTGROUPS))
    return(TT(Req, "Delete_Group"));
else if (ElemType.equals(ListElem.MANTUSERS))
    return(TT(Req, "Delete_User"));
else if (ElemType.equals(ListElem.MANTROLES))
    return(TT(Req, "Delete_Role"));
else if (ElemType.equals(ListElem.MANTMIME))
    return(TT(Req, "Delete_Mime_Type"));
else if (ElemType.equals(ListElem.MANTREPO))
    return(TT(Req, "Delete_Repository"));
else if (ElemType.equals(ListElem.MANTOBJ))
    return(TT(Req, "Delete_Object_definition"));
else if (ElemType.equals(ListElem.MANTAUTH))
    return(TT(Req, "Delete_Authenticator"));
else if (ElemType.equals(ListElem.MANTCUST))
    return(TT(Req, "Delete_Customization"));
return("Error");
}
//-----------------------------------------------------------------------------------------------
private String GetTitleCopy(HttpServletRequest Req, String ElemType)
{
if (ElemType.equals(ListElem.MANTACL))
    return(TT(Req, "Copy_ACL"));
else if (ElemType.equals(ListElem.MANTGROUPS))
    return(TT(Req, "Copy_Group"));
else if (ElemType.equals(ListElem.MANTUSERS))
    return(TT(Req, "Copy_User"));
else if (ElemType.equals(ListElem.MANTROLES))
    return(TT(Req, "Copy_Role"));
else if (ElemType.equals(ListElem.MANTMIME))
    return(TT(Req, "Copy_Mime_Type"));
else if (ElemType.equals(ListElem.MANTREPO))
    return(TT(Req, "Copy_Repository"));
else if (ElemType.equals(ListElem.MANTOBJ))
    return(TT(Req, "Copy_Object_definition"));
else if (ElemType.equals(ListElem.MANTAUTH))
    return(TT(Req, "Copy_Authenticator"));
else if (ElemType.equals(ListElem.MANTCUST))
    return(TT(Req, "Copy_Customization"));
return("Error");
}
//-----------------------------------------------------------------------------------------------
private StringBuilder GenACLUsers(DriverGeneric PDSession, String Id)
{
StringBuilder SB=new StringBuilder();
try {
PDACL Acl=new PDACL(PDSession);
Cursor ListUsersPerm = Acl.ListUsers(Id);
Record NextUP=PDSession.NextRec(ListUsersPerm);
while (NextUP!=null)
    {   
    String User=(String)NextUP.getAttr(PDACL.fUSERNAME).getValue();
    int Perm=(Integer)NextUP.getAttr(PDACL.fPERMISION).getValue();
    SB.append("|").append(User).append("/").append(Perm);
    NextUP=PDSession.NextRec(ListUsersPerm);
    }   
} catch (Exception Ex)
    {
    PDLog.Error(Ex.getLocalizedMessage());
    }
return(SB);
}
//-----------------------------------------------------------------------------------------------
private StringBuilder GenACLGroups(DriverGeneric PDSession, String Id)
{
StringBuilder SB=new StringBuilder();
try {
PDACL Acl=new PDACL(PDSession);
Cursor ListGroupsPerm = Acl.ListGroups(Id);
Record NextGP=PDSession.NextRec(ListGroupsPerm);
while (NextGP!=null)
    {   
    String User=(String)NextGP.getAttr(PDACL.fGROUPNAME).getValue();
    int Perm=(Integer)NextGP.getAttr(PDACL.fPERMISION).getValue();
    SB.append("|").append(User).append("/").append(Perm);
    NextGP=PDSession.NextRec(ListGroupsPerm);
    }   
} catch (Exception Ex)
    {
    PDLog.Error(Ex.getLocalizedMessage());
    }
return(SB);
}
//-----------------------------------------------------------------------------------------------
private void InsertACLMembers(PDACL Acl, HttpServletRequest Req)
{
String[] UPairs;    
String Users=Req.getParameter("Users");
if (Users!=null && Users.length()>0)
    {
    UPairs = Users.split("\\|");
    for (int i = 0; i < UPairs.length; i++)
        {
        String UPair = UPairs[i];
        String[] U_P = UPair.split("/");
        try {
        Acl.addUser(U_P[0], Integer.parseInt(U_P[1]));
        } catch(Exception Ex)
            {
            }
        }
    }
String Groups=Req.getParameter("Groups");
if (Groups!=null && Groups.length()>0)
    {
    UPairs = Groups.split("\\|");
    for (int i = 0; i < UPairs.length; i++)
        {
        String UPair = UPairs[i];
        String[] U_P = UPair.split("/");
        try {
        Acl.addGroup(U_P[0], Integer.parseInt(U_P[1]));
        } catch(Exception Ex)
            {
            }
        }
    }
}
//-----------------------------------------------------------------------------------------------
private void UpdateACLMembers(PDACL Acl, HttpServletRequest Req)
{
String[] UPairs;    
String Users=Req.getParameter("Users");
if (Users!=null && Users.length()>0)
    {
    UPairs = Users.split("\\|");
    for (int i = 0; i < UPairs.length; i++)
        {
        String UPair = UPairs[i];
        String[] U_P = UPair.split("/");
        try {
        Acl.addUser(U_P[0], Integer.parseInt(U_P[1]));
        } catch(Exception Ex)
            {
            }
        }
    }
String Groups=Req.getParameter("Groups");
if (Groups!=null && Groups.length()>0)
    {
    UPairs = Groups.split("\\|");
    for (int i = 0; i < UPairs.length; i++)
        {
        String UPair = UPairs[i];
        String[] U_P = UPair.split("/");
        try {
        Acl.addGroup(U_P[0], Integer.parseInt(U_P[1]));
        } catch(Exception Ex)
            {
            }
        }
    }
}
//-----------------------------------------------------------------------------------------------

}
