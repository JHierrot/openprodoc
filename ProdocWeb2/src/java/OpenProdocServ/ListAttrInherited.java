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

import OpenProdocUI.SParent;
import static OpenProdocUI.SParent.TT;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDObjDefs;
import prodoc.Record;

/**
 *
 * @author jhierrot
 */
public class ListAttrInherited extends SParent
{

    /**
     *
     */
    public static final String[] LTyp={"Integer", "Float", "String", "Date", "Boolean", "TimeStamp", "Thesaur"};

//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
 * @throws Exception
 */
@Override
protected void processRequest(HttpServletRequest Req, HttpServletResponse response) throws ServletException, IOException
{   
DriverGeneric PDSession=SParent.getSessOPD(Req);
response.setContentType("text/xml;charset=UTF-8");
response.setStatus(HttpServletResponse.SC_OK);
PrintWriter out = response.getWriter();  
StringBuilder Resp=new StringBuilder(3000);
Resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><rows><head>");
Cursor listParentAttr=null;
try {
//Record Rec;
String ObjDefId=Req.getParameter("Id");
String Oper=Req.getParameter("Oper");
String Own=Req.getParameter("Own");
PDObjDefs Def=new PDObjDefs(getSessOPD(Req));
if (Own!=null)
    {
    if (Oper.equals("New"))
        listParentAttr = Def.getListAttr(PDDocs.getTableName());
    else    
        listParentAttr = Def.getListAttr(ObjDefId);
    }
else
    {
    if (Oper.equals("New"))
        listParentAttr = Def.getListParentAttr2(ObjDefId);
    else
        listParentAttr = Def.getListParentAttr(ObjDefId);
    }
Record NextObj=PDSession.NextRec(listParentAttr);
boolean AttrEmpty=false;
if (NextObj==null)
    {
    AttrEmpty=true;
    NextObj=PDObjDefs.getRecordAttrsStruct();
    } 
NextObj.delAttr(PDObjDefs.fATTRPRIMKEY);
if (Own!=null)
    NextObj.delAttr(PDObjDefs.fTYPNAME);
NextObj.initList();
Attribute Attr=NextObj.nextAttr();
int Count=1;
while (Attr!=null)
    {
    Resp.append("<column width=\"").append(Count==NextObj.NumAttr()?"*":600/NextObj.NumAttr()).append("\" type=\"ro\" align=\"left\" sort=\"str\">").append(TT(Req,Attr.getUserName())).append("</column>");
    Count++;
    Attr=NextObj.nextAttr();
    }
Resp.append("</head>");
if (Own==null || !Oper.equals("New"))
    {   
    while (NextObj!=null && !AttrEmpty)
        {
        String Id=(String)NextObj.getAttr(PDObjDefs.fNAME).getValue();
        NextObj.getAttr(PDObjDefs.fATTRTYPE).setValue(LTyp[(Integer)NextObj.getAttr(PDObjDefs.fATTRTYPE).getValue()]);
        Resp.append(SParent.GenRowGrid(Req, Id, NextObj, true));    
        NextObj=PDSession.NextRec(listParentAttr);
        if (NextObj!=null)
            {
            if (Own!=null)
                NextObj.delAttr(PDObjDefs.fTYPNAME);    
            NextObj.delAttr(PDObjDefs.fATTRPRIMKEY);
            }
        }   
    }
PDSession.CloseCursor(listParentAttr);
Resp.append("</rows>");
} catch (PDException ex)
    {
    try{    
    if (listParentAttr!=null)
        PDSession.CloseCursor(listParentAttr);
    } catch (Exception E) {}
    Resp.append("<LV>Error</LV></row>");
    }
out.println( Resp.toString().replace("&", "&amp;") );   
out.close();
}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "ListAttrInherited Servlet";
}
//-----------------------------------------------------------------------------------------------

}