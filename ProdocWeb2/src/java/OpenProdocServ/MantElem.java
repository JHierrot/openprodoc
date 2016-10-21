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
import prodoc.PDAuthenticators;
import prodoc.PDCustomization;
import prodoc.PDException;
import prodoc.PDGroups;
import prodoc.PDLog;
import prodoc.PDMimeType;
import prodoc.PDObjDefs;
import prodoc.PDRepository;
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
//String Filter=Req.getParameter("Fil");
if (TypeElem!=null && TypeElem.length()!=0)
    {
//    if (Oper.equals(OPERNEW))  
//        out.println(ElemNew(Req, TypeElem));
//    else if (Oper.equals(OPERMODIF))
//        out.println(ElemMod(Req, TypeElem, Id));
//    else if (Oper.equals(OPERDELETE))
//        out.println(ElemDel(Req, TypeElem, Id));
//    else if (Oper.equals(OPERCOPY))
//        out.println(ElemCopy(Req, TypeElem, Id));
//    else if (Oper.equals("Import"))
//        out.println(ElemImport(Req, TypeElem));
    if (Oper.equals("Import"))  
        out.println(ElemImport(Req, TypeElem));
    else
        {
        DriverGeneric PDSession=SParent.getSessOPD(Req);        
        ObjPD Obj=GenObj(TypeElem, PDSession, Id);    
        out.println(ElemMant(Req, Oper, TypeElem, Obj, Id));
        }
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
        else if (TypeElem.equals(ListElem.MANTGROUPS))            
            InsertGroupMembers((PDGroups)Obj,Req);
        else if (TypeElem.equals(ListElem.MANTOBJ))            
            InsertObjAttrs((PDObjDefs)Obj,Req);
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
        else if (TypeElem.equals(ListElem.MANTGROUPS)) 
            {
            PDGroups G=((PDGroups)Obj);    
            G.DelAllSubGroups();
            G.DelAllUsers();
            InsertGroupMembers((PDGroups)Obj,Req);
            }
        else if (TypeElem.equals(ListElem.MANTOBJ))            
            InsertObjAttrs((PDObjDefs)Obj,Req);
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
private String ElemMant(HttpServletRequest Req,String Oper, String TypeElem, ObjPD Obj, String Id) throws PDException
{
StringBuilder SB=new StringBuilder(1000);
String Title;
if (Oper.equals(OPERNEW))  
    Title=GetTitleNew(Req, TypeElem);
else if (Oper.equals(OPERMODIF))
    Title=GetTitleModif(Req, TypeElem);
else if (Oper.equals(OPERDELETE))
    Title=GetTitleDel(Req, TypeElem);
else if (Oper.equals(OPERCOPY))
    {
    Title=GetTitleCopy(Req, TypeElem);
    Id+="1";
    }
else
    Title="";
SB.append("[  {type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 230},");
SB.append("{type: \"label\", label: \"").append(Title).append("\"},");
SB.append(getBody(Oper, Req, TypeElem, Obj, Id));
if (TypeElem.equals(ListElem.MANTOBJ))
    SB.append(OkBlockExt(Req, Oper, TypeElem, (PDObjDefs)Obj, Id));
else
    SB.append(OkBlock(Req, TypeElem, Id));
return(SB.toString());
}



/**
//-----------------------------------------------------------------------------------------------
private String ElemNew(HttpServletRequest Req, String TypeElem) throws PDException
{
StringBuilder SB=new StringBuilder(1000);
SB.append("[{type: \"settings\", position: \"label-left\", labelWidth: 150, inputWidth: 230},");
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
* ***/
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

private StringBuilder OkBlockExt(HttpServletRequest Req, String Oper, String TypeElem, PDObjDefs Obj, String Id)
{
StringBuilder SB=new StringBuilder(500);    
SB.append("{type: \"block\", width: 520, list:[");
SB.append("{type: \"button\", name: \"OK\", value: \"").append(TT(Req, "Ok")).append("\"},");
SB.append("{type: \"newcolumn\", offset:20 },");
SB.append("{type: \"button\", name: \"CANCEL\", value: \"").append(TT(Req, "Cancel")).append("\"},");
SB.append("{type: \"hidden\", name:\"Type\", value: \"").append(TypeElem).append("\"}");
if (Id!=null)
    SB.append(",{type: \"hidden\", name:\"Id\", value: \"").append(Id).append("\"}");
if (Oper.equals(OPERMODIF))
    {    
    SB.append(",{type: \"newcolumn\", offset:20 },");
    SB.append("{type: \"button\", name: \"CreateObj\", value: \"").append(TT(Req, "CreateObj")).append("\", disabled:").append(Obj.isCreated()?"true":"false").append("},");
    SB.append("{type: \"newcolumn\", offset:20 },");
    SB.append("{type: \"button\", name: \"DeleteObj\", value: \"").append(TT(Req, "DeleteObj")).append("\", disabled:").append(Obj.isCreated()?"false":"true").append("}");
    }
SB.append("]} ];");
return(SB);
}

//-----------------------------------------------------------------------------------------------
private StringBuilder getBody(String Oper, HttpServletRequest Req, String ElemType, ObjPD Obj, String Id) throws PDException
{
StringBuilder SB=new StringBuilder(2000);
DriverGeneric PDSession=SParent.getSessOPD(Req);    
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
    Attr=Rec.getAttr(PDGroups.fNAME);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif));
    Attr=Rec.getAttr(PDGroups.fDESCRIPTION);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif)); 
    Attr=Rec.getAttr(PDGroups.fACL);
    SB.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" options:[");
    SB.append(getComboModel("ACL",PDSession, (String)Attr.getValue()) );
    SB.append("]},"); 
    SB.append("{type: \"hidden\", name:\"Users\", value: \"").append(GenGUsers(PDSession, Id)).append("\"},");
    SB.append("{type: \"hidden\", name:\"Groups\", value: \"").append(GenGGroups(PDSession, Id)).append("\"},");
    }
else if (ElemType.equals(ListElem.MANTUSERS))
    {
    Attr=Rec.getAttr(PDUser.fNAME);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif));
    Attr=Rec.getAttr(PDUser.fDESCRIPTION);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif)); 
    Attr=Rec.getAttr(PDUser.fEMAIL);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif)); 
    Attr=Rec.getAttr(PDUser.fVALIDATION);
    SB.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", options:[");
    SB.append(getComboModel("Authenticators",PDSession, (String)Attr.getValue()) );
    SB.append("]},"); 
    Attr=Rec.getAttr(PDUser.fACTIVE);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif)); 
    Attr=Rec.getAttr(PDUser.fROLE);
    SB.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", options:[");
    SB.append(getComboModel("Roles",PDSession, (String)Attr.getValue()) );
    SB.append("]},"); 
    Attr=Rec.getAttr(PDUser.fPASSWORD);
    SB.append(GenInput(Req, Attr, ReadOnly, Modif)); 
    Attr=Rec.getAttr(PDUser.fCUSTOM);
    SB.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\", options:[");
    SB.append(getComboModel("Customizers",PDSession,(String)Attr.getValue()) );
    SB.append("]},"); 
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
    Attr=Rec.getAttr(PDRepository.fNAME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRepository.fDESCRIPTION);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRepository.fREPTYPE);
    String Value=(String)Attr.getValue();
    SB.append("{type: \"combo\", label: \"").append(TT(Req, Attr.getUserName())).append("\", name: \"").append(Attr.getName()).append("\",").append((Oper.equals(OPERMODIF)|| Oper.equals(OPERDELETE))?"disabled:1,":"").append(" inputWidth:\"auto\", options:[");
    SB.append("{text:\"" + PDRepository.tBBDD + "\", value:\"" + PDRepository.tBBDD + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tBBDD))?", selected:true":"").append("},");
    SB.append("{text:\"" + PDRepository.tFS + "\", value:\"" + PDRepository.tFS + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tFS))?", selected:true":"").append("},");
    SB.append("{text:\"" + PDRepository.tFTP + "\", value:\"" + PDRepository.tFTP + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tFTP))?", selected:true":"").append("},");
    SB.append("{text:\"" + PDRepository.tREFURL + "\", value:\"" + PDRepository.tREFURL + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tREFURL))?", selected: true":"").append("},");
    SB.append("{text:\"" + PDRepository.tS3 + "\", value:\"" + PDRepository.tS3 + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tS3))?", selected:true":"").append("}");
    SB.append("]},");
    Attr=Rec.getAttr(PDRepository.fURL);
    if (Attr.getValue()!=null)
        Attr.setValue(((String)Attr.getValue()).replace("\\", "/"));
    SB.append(GenInput(Req, Attr,  (Oper.equals(OPERMODIF)|| Oper.equals(OPERDELETE)) , Modif));
    Attr=Rec.getAttr(PDRepository.fPARAM);
    SB.append(GenInput(Req, Attr,  (Oper.equals(OPERMODIF)|| Oper.equals(OPERDELETE)), Modif));
    Attr=Rec.getAttr(PDRepository.fUSERNAME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRepository.fPASSWORD);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDRepository.fENCRYPT);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    }
else if (ElemType.equals(ListElem.MANTOBJ))
    {
    String ObjId=Req.getParameter("ObjId");
    PDObjDefs Parent=null;
    if (ObjId!=null)
        {
        Parent=new PDObjDefs(PDSession);
        Parent.Load(ObjId);
        } 
    Attr=Rec.getAttr(PDObjDefs.fNAME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDObjDefs.fDESCRIPTION);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDObjDefs.fACL);
    SB.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" options:[");
    if (ObjId!=null)
        SB.append(getComboModel("ACL",PDSession, Parent.getACL()) );
    else
        SB.append(getComboModel("ACL",PDSession, (String)Attr.getValue()) );
    SB.append("]},"); 
    if (ObjId!=null && Parent.getClassType().equals(PDObjDefs.CT_DOC) 
             || ((String)Rec.getAttr(PDObjDefs.fCLASSTYPE).getValue()).equals(PDObjDefs.CT_DOC))
        {
        Attr=Rec.getAttr(PDObjDefs.fREPOSIT);
        SB.append("{type: \"combo\", name: \"").append(Attr.getName()).append("\", label: \"").append(TT(Req, Attr.getUserName())).append("\",").append(ReadOnly?"readonly:1,":"").append(" required: true, tooltip:\"").append(TT(Req, Attr.getDescription())).append("\",").append(Attr.getValue()!=null?("value:\""+Attr.Export()+"\","):"").append(" options:[");
        if (ObjId!=null)
            SB.append(getComboModel("Reposit",PDSession, Parent.getReposit()) );
        else
            SB.append(getComboModel("Reposit",PDSession, (String)Attr.getValue()) );
        SB.append("]},");    
        }
    Attr=Rec.getAttr(PDObjDefs.fACTIVE);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    SB.append("{type: \"block\", width: 500, list:[");
    Attr=Rec.getAttr(PDObjDefs.fTRACEADD);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    SB.append("{type: \"newcolumn\", offset:5 },");
    Attr=Rec.getAttr(PDObjDefs.fTRACEMOD);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    SB.append("{type: \"newcolumn\", offset:0 },");
    Attr=Rec.getAttr(PDObjDefs.fTRACEDEL);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    SB.append("{type: \"newcolumn\", offset:5 },");
    Attr=Rec.getAttr(PDObjDefs.fTRACEVIEW);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDObjDefs.fCLASSTYPE);
    if (ObjId!=null)
        SB.append("{type: \"hidden\", name:\"").append(Attr.getName()).append("\", value: \"").append(Parent.getClassType()).append("\"},");
    else
        SB.append("{type: \"hidden\", name:\"").append(Attr.getName()).append("\", value: \"").append(Attr.getValue()).append("\"},");
    Attr=Rec.getAttr(PDObjDefs.fPARENT);
    if (ObjId!=null)
        SB.append("{type: \"hidden\", name:\"").append(Attr.getName()).append("\", value: \"").append(Parent.getName()).append("\"},");
    else
        SB.append("{type: \"hidden\", name:\"").append(Attr.getName()).append("\", value: \"").append(Attr.getValue()).append("\"},");
    SB.append("{type: \"hidden\", name:\"ATTRS\", value: \"\"}");
    SB.append("]},");
    }
else if (ElemType.equals(ListElem.MANTAUTH))
    {
    Attr=Rec.getAttr(PDAuthenticators.fNAME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDAuthenticators.fDESCRIPTION);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDAuthenticators.fAUTHTYPE);
    String Value=(String)Attr.getValue();
    SB.append("{type: \"combo\", label: \"").append(TT(Req, Attr.getUserName())).append("\", name: \"").append(Attr.getName()).append("\",").append((Oper.equals(OPERMODIF)|| Oper.equals(OPERDELETE))?"disabled:1,":"").append(" inputWidth:\"auto\", options:[");
    SB.append("{text:\"" + PDAuthenticators.tOPD + "\", value:\"" + PDAuthenticators.tOPD + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tBBDD))?", selected:true":"").append("},");
    SB.append("{text:\"" + PDAuthenticators.tLDAP + "\", value:\"" + PDAuthenticators.tLDAP + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tFTP))?", selected:true":"").append("},");
    SB.append("{text:\"" + PDAuthenticators.tBBDD + "\", value:\"" + PDAuthenticators.tBBDD + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tFS))?", selected:true":"").append("},");
    SB.append("{text:\"" + PDAuthenticators.tSO + "\", value:\"" + PDAuthenticators.tSO + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tREFURL))?", selected: true":"").append("},");
    SB.append("{text:\"" + PDAuthenticators.tCUSTOM + "\", value:\"" + PDAuthenticators.tCUSTOM + "\"").append((Value!=null&&Value.equalsIgnoreCase(PDRepository.tS3))?", selected:true":"").append("}");
    SB.append("]},");
    Attr=Rec.getAttr(PDAuthenticators.fURL);
    if (Attr.getValue()!=null)
        Attr.setValue(((String)Attr.getValue()).replace("\\", "/"));
    SB.append(GenInput(Req, Attr,  (Oper.equals(OPERMODIF)|| Oper.equals(OPERDELETE)) , Modif));
    Attr=Rec.getAttr(PDAuthenticators.fPARAM);
    SB.append(GenInput(Req, Attr,  (Oper.equals(OPERMODIF)|| Oper.equals(OPERDELETE)), Modif));
    Attr=Rec.getAttr(PDAuthenticators.fUSERNAME);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
    Attr=Rec.getAttr(PDAuthenticators.fPASSWORD);
    SB.append(GenInput(Req, Attr,  ReadOnly, Modif));
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
    if (Attr.getValue()!=null)
        Attr.setValue(((String)Attr.getValue()).replace("\"", "'"));
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
PDSession.CloseCursor(ListUsersPerm);
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
    String Gr=(String)NextGP.getAttr(PDACL.fGROUPNAME).getValue();
    int Perm=(Integer)NextGP.getAttr(PDACL.fPERMISION).getValue();
    SB.append("|").append(Gr).append("/").append(Perm);
    NextGP=PDSession.NextRec(ListGroupsPerm);
    }   
PDSession.CloseCursor(ListGroupsPerm);
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
private StringBuilder GenGUsers(DriverGeneric PDSession, String Id)
{
StringBuilder SB=new StringBuilder();
try {
PDGroups Gr=new PDGroups(PDSession);
Cursor ListUsersPerm = Gr.ListUsers(Id);
Record NextUP=PDSession.NextRec(ListUsersPerm);
while (NextUP!=null)
    {   
    String User=(String)NextUP.getAttr(PDGroups.fUSERNAME).getValue();
    SB.append("|").append(User);
    NextUP=PDSession.NextRec(ListUsersPerm);
    }   
PDSession.CloseCursor(ListUsersPerm);
} catch (Exception Ex)
    {
    PDLog.Error(Ex.getLocalizedMessage());
    }
return(SB);
}
//-----------------------------------------------------------------------------------------------
private StringBuilder GenGGroups(DriverGeneric PDSession, String Id)
{
StringBuilder SB=new StringBuilder();
try {
PDGroups Gr=new PDGroups(PDSession);
Cursor ListGroupsPerm = Gr.ListGroups(Id);
Record NextGP=PDSession.NextRec(ListGroupsPerm);
while (NextGP!=null)
    {   
    String SubGr=(String)NextGP.getAttr(PDGroups.fMEMBERNAME).getValue();
    SB.append("|").append(SubGr);
    NextGP=PDSession.NextRec(ListGroupsPerm);
    }   
PDSession.CloseCursor(ListGroupsPerm);
} catch (Exception Ex)
    {
    PDLog.Error(Ex.getLocalizedMessage());
    }
return(SB);
}
//-----------------------------------------------------------------------------------------------
private void InsertGroupMembers(PDGroups Gr, HttpServletRequest Req)
{
String[] UPairs;    
String Users=Req.getParameter("Users");
if (Users!=null && Users.length()>0)
    {
    UPairs = Users.split("\\|");
    for (int i = 0; i < UPairs.length; i++)
        {
        try {
        if (UPairs[i].length()!=0)    
            Gr.addUser( UPairs[i]);
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
        try {
        if (UPairs[i].length()!=0)    
            Gr.addGroup(UPairs[i]);
        } catch(Exception Ex)
            {
            }
        }
    }
}
//-----------------------------------------------------------------------------------------------
private void InsertObjAttrs(PDObjDefs pdObjDefs, HttpServletRequest Req) throws PDException
{
String AllAttrs=Req.getParameter("ATTRS");
String[] Attrs = AllAttrs.split("¬");
pdObjDefs.DelAtributes();
for (String Attr : Attrs)
    {
    String[] AttrDef = Attr.split("\\|");   
    String Name=AttrDef[1];
    String Username=AttrDef[2];
    String Descrip=AttrDef[3];
    String Type=AttrDef[4];
    int TypeN=2;
    for (int i = 0; i < ListAttrInherited.LTyp.length; i++)
        {
        if (Type.equals(ListAttrInherited.LTyp[i]))  
            {
            TypeN=i;
            break;
            }
        }
    String Required=AttrDef[5];
    String LongStr=AttrDef[6];   
    if (LongStr.equals("_"))
        LongStr="0";
    String Unique=AttrDef[7];
    String ModifAllow=AttrDef[8];
    String Multival=AttrDef[9];
    Attribute At=new Attribute(Name,Username, Descrip, TypeN, Required.equals("1"), null, Integer.parseInt(LongStr), false, Unique.equals("1"),ModifAllow.equals("1"),Multival.equals("1"));
    pdObjDefs.addAtribute(At);
    }
}
//-----------------------------------------------------------------------------------------------
}
