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

package OpenProdocServ;

import OpenProdocUI.SParent;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jhierrot
 */
public class FormSQL extends SParent
{
final static String ExampleFoldSQL="<b>Select</b> PDId, Title, FolderType, ACL <br> <b>from</b> PD_FOLDERS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
final static String HelpFoldSQL="&lt;Select_Expression&gt; ::= SELECT &lt;Columns_List&gt; " +
" FROM &lt;Object_Name&gt; " +
" [ WHERE &lt;Search_Condition&gt; ] " +
" [ ORDER BY &lt;Sort_Description&gt; ]<br>" +
"&lt;Columns_List&gt; ::= * | &lt;ColumnName&gt; [ { , &lt;ColumnName&gt; } ]<br>" +
"&lt;Object_Name&gt; ::= this | &lt;FolderTypeName&gt; [,SUBTYPES]<br>" +
"&lt;Search_Condition&gt; ::= [ NOT ] [ ( ]&lt;Bool_Term&gt; | &lt;Search_Condition&gt; OR &lt;Bool_Term&gt; [ ) ]<br>" +
"&lt;Bool_Term&gt; ::= &lt;Bool_Factor&gt; | &lt;Bool_Term&gt; AND &lt;Bool_Factor&gt;<br>" +
"&lt;Bool_Factor&gt; ::= &lt;Exp_Comp&gt; | &lt;Exp_In&gt; | &lt;Exp_Func&gt;<br>" +
"&lt;Exp_Comp&gt; ::= &lt;FieldName&gt; &lt;Comparator&gt; [ &lt;FieldName&gt; | &lt;Value&gt; ]<br>" +
"&lt;Comparator&gt; ::=  = | &lt;&gt; | &gt;= | &lt;= | &gt; | &lt;<br>" +
"&lt;Value&gt; ::= &lt;String&gt; | &lt;Date&gt; | &lt;Integer&gt; | &lt;TimeStamp&gt; | &lt;Boolean&gt; | &lt;Decimal&gt;<br>" +
"&lt;Exp_In&gt; ::= &lt;FieldName&gt; IN &lt;In_List&gt;<br>" +
"&lt;In_List&gt; ::= (&lt;Value&gt; [ { , &lt;Value&gt; } ]) | &lt;Select_Expression&gt;<br>" +
"&lt;Exp_Func&gt; ::= IN_TREE( &lt;FolderId&gt; ) | IN_FOLDER( &lt;FolderId&gt; )<br>" +
"&lt;Sort_Description&gt; ::= &lt;FieldName&gt; ASC | DESC";
final static String ExampleDocSQL="<b>Select</b> PDId, Title, DocType, ACL <br> <b>from</b> PD_DOCS, SUBTYPES <br> <b>where</b> Title=' ' and PDDate>'2000-01-01 09:10:11' <br> <b>order by</b> PDDate <b>DESC</b>";
final static String HelpDocSQL="&lt;Select_Expression&gt; ::= SELECT &lt;Columns_List&gt; " +
" FROM &lt;Object_Name&gt; " +
" [ WHERE &lt;Search_Condition&gt; ] " +
" [ ORDER BY &lt;Sort_Description&gt; ] <br>" +
"&lt;Columns_List&gt; ::= * | &lt;ColumnName&gt; [ { , &lt;ColumnName&gt; } ]<br>" +
"&lt;Object_Name&gt; ::= this | &lt;DocumentTypeName&gt; [,SUBTYPES]<br>" +
"&lt;Search_Condition&gt; ::= [ NOT ] [ ( ]&lt;Bool_Term&gt; | &lt;Search_Condition&gt; OR &lt;Bool_Term&gt; [ ) ]<br>" +
"&lt;Bool_Term&gt; ::= &lt;Bool_Factor&gt; | &lt;Bool_Term&gt; AND &lt;Bool_Factor&gt;<br>" +
"&lt;Bool_Factor&gt; ::= &lt;Exp_Comp&gt; | &lt;Exp_In&gt; | &lt;Exp_Func&gt;<br>" +
"&lt;Exp_Comp&gt; ::= &lt;FieldName&gt; &lt;Comparator&gt; [ &lt;FieldName&gt; | &lt;Value&gt; ]<br>" +
"&lt;Comparator&gt; ::=  = | &lt;&gt; | &gt;= | &lt;= | &gt; | &lt;<br>" +
"&lt;Value&gt; ::= &lt;String&gt; | &lt;Date&gt; | &lt;Integer&gt; | &lt;TimeStamp&gt; | &lt;Boolean&gt; | &lt;Decimal&gt;<br>" +
"&lt;Exp_In&gt; ::= &lt;FieldName&gt; IN &lt;In_List&gt;<br>" +
"&lt;In_List&gt; ::= (&lt;Value&gt; [ { , &lt;Value&gt; } ]) | &lt;Select_Expression&gt;<br>" +
"&lt;Exp_Func&gt; ::= CONTAINS(‘&lt;FullText_Search&gt;‘) | IN_TREE( &lt;FolderId&gt; ) | IN_FOLDER( &lt;FolderId&gt; )<br>" +
"&lt;Sort_Description&gt; ::= &lt;FieldName&gt; ASC | DESC";
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
