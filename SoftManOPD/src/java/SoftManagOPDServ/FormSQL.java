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
 * author: Joaquin Hierro      2019
 * 
 */

package SoftManagOPDServ;

import SoftManagOPDUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jhierrot
 */
public class FormSQL extends SParent
{
final static String ExampleFoldSQL="<b>Select</b> PDId, Title, FolderType, ACL <br> <b>from</b> PD_FOLDERS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
final static String HelpFoldSQL="<i>Select_Expression</i> ::= <b>SELECT</b> <i>Columns_List</i>" +
" <b>FROM</b> <i>Object_Name</i>" +
" [ <b>WHERE</b> <i>Search_Condition</i> ]" +
" [ <b>ORDER BY</b> <i>Sort_Description</i> ]<br>" +
"<i>Columns_List</i> ::= * | <i>ColumnName</i> [ , <i>ColumnName</i> ]<br>" +
"<i>Object_Name</i> ::= this | <i>FolderTypeName</i> [,SUBTYPES]<br>" +
"<i>Search_Condition</i> ::= [ <b>NOT</b> ] [ ( ]<i>Bool_Term</i> | <i>Search_Condition</i> <b>OR</b> <i>Bool_Term</i> [ ) ]<br>" +
"<i>Bool_Term</i> ::= <i>Bool_Factor</i> | <i>Bool_Term</i> <b>AND</b> <i>Bool_Factor</i><br>" +
"<i>Bool_Factor</i> ::= <i>Exp_Comp</i> | <i>Exp_In</i> | <i>Exp_Func</i><br>" +
"<i>Exp_Comp</i> ::= <i>FieldName</i> <i>Comparator</i> [ <i>FieldName</i> | <i>Value</i> ]<br>" +
"<i>Comparator</i> ::=    <b>= | <> | <= | >= | > | <   </b> <br>" +
"<i>Value</i> ::= <i>String</i> | <i>Date</i> | <i>Integer</i> | <i>TimeStamp</i> | <i>Boolean</i> | <i>Decimal</i><br>" +
"<i>Exp_In</i> ::= <i>FieldName</i> <b>IN</b> <i>In_List</i><br>" +
"<i>In_List</i> ::= (<i>Value</i> [ , <i>Value</i> ]) | <i>Select_Expression</i><br>" +
"<i>Exp_Func</i> ::= <b>IN_TREE</b>( <i>FolderId</i> ) | <b>IN_FOLDER</b>( <i>FolderId</i> )<br>" +
"<i>Sort_Description</i> ::= <i>FieldName</i> <b>ASC</b> | <b>DESC</b>";
final static String ExampleDocSQL="<b>Select</b> PDId, Title, DocType, ACL <br> <b>from</b> PD_DOCS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
final static String HelpDocSQL="<i>Select_Expression</i> ::= <b>SELECT</b> <i>Columns_List</i>" +
" <b>FROM</b> <i>Object_Name</i>" +
" [ <b>WHERE</b> <i>Search_Condition</i> ]" +
" [ <b>ORDER BY</b> <i>Sort_Description</i> ]<br>" +
"<i>Columns_List</i> ::= * | <i>ColumnName</i> [ , <i>ColumnName</i> ]<br>" +
"<i>Object_Name</i> ::= this | <i>FolderTypeName</i> [,SUBTYPES]<br>" +
"<i>Search_Condition</i> ::= [ <b>NOT</b> ] [ ( ]<i>Bool_Term</i> | <i>Search_Condition</i> <b>OR</b> <i>Bool_Term</i> [ ) ]<br>" +
"<i>Bool_Term</i> ::= <i>Bool_Factor</i> | <i>Bool_Term</i> <b>AND</b> <i>Bool_Factor</i><br>" +
"<i>Bool_Factor</i> ::= <i>Exp_Comp</i> | <i>Exp_In</i> | <i>Exp_Func</i><br>" +
"<i>Exp_Comp</i> ::= <i>FieldName</i> <i>Comparator</i> [ <i>FieldName</i> | <i>Value</i> ]<br>" +
"<i>Comparator</i> ::=    <b>= | <> | <= | >= | > | <   </b> <br>" +
"<i>Value</i> ::= <i>String</i> | <i>Date</i> | <i>Integer</i> | <i>TimeStamp</i> | <i>Boolean</i> | <i>Decimal</i><br>" +
"<i>Exp_In</i> ::= <i>FieldName</i> <b>IN</b> <i>In_List</i><br> " +
"<i>In_List</i> ::= (<i>Value</i> [ , <i>Value</i> ]) | <i>Select_Expression</i><br>" +
"<i>Exp_Func</i> ::= <b>CONTAINS</b>(‘<i>FullText_Search</i>‘) | <b>IN_TREE</b>( <i>FolderId</i> ) | <b>IN_FOLDER</b>( <i>FolderId</i> )<br>" +
"<i>Sort_Description</i> ::= <i>FieldName</i> <b>ASC</b> | <b>DESC</b>";
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @throws Exception
 */
@Override
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{   
String ObjType=Req.getParameter("Type");  
boolean IsFold=false;
if (ObjType!=null && ObjType.equals("FOLD"))
    IsFold=true;
out.println("[" +
    "{type: \"label\", label: \""+TT(Req, IsFold?"Advanced_Folder_Search":"Advanced_Doc_Search")+"\"}," +
    "{type:\"editor\", name:\"SQLF\", label:\""+TT(Req, "OPD_SQL_Expresion")+"\", value:\""+(IsFold?ExampleFoldSQL:ExampleDocSQL)+"\", position:'label-top', offsetLeft:20, required:true, inputWidth:600, inputHeight:140}," +
    "{type:\"block\", width: 250, list:[" +
        "{type: \"button\", name: \"OK\", value: \""+TT(Req, "Ok")+"\"}," +
        "{type: \"newcolumn\", offset:20 }," +
        "{type: \"button\", name: \"CANCEL\", value: \""+TT(Req, "Cancel")+"\"}," +
        "{type: \"hidden\", name:\"CurrFold\", value: \"RootFolder\"}]},"+
    "{type:\"editor\", name:\"SQLJELP\", label:\""+TT(Req, "OPD_SQL_Help")+"\", value:\""+(IsFold?HelpFoldSQL:HelpDocSQL)+"\", disabled:true, position:'label-top', offsetLeft:20, inputWidth:600, inputHeight:240}" +
     "];");

}
//-----------------------------------------------------------------------------------------------

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "FormSQL Servlet";
}
//-----------------------------------------------------------------------------------------------
}
