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

//----------------------------------------------
var LASTLEVEL=0;
var COLLAPSED=1;
var EXPANDED=2;
var UNKNOW=3;
var REFRESH=4;

var READY_STATE_UNINITIALIZED=0;
var READY_STATE_LOADING=1;
var READY_STATE_LOADED=2;
var READY_STATE_INTERACTIVE=3;
var READY_STATE_COMPLETE=4;
var Url="RefreshDocs";
var Target="MainFrame";
//----------------------------------------------
function ListType(FoldType)
{
var http_con=NewAjaxCon();
http_con.onreadystatechange = ObtainDefFold;
http_con.open('GET', 'ListTypeFolds?Type='+FoldType, true);
http_con.send(null);
var ServAns="";

    function ObtainDefFold()
    {
    if (http_con.readyState == READY_STATE_COMPLETE)
        {
        if (http_con.status == 200)
           {
           ServAns=http_con.responseText;
           FillFields(ServAns);
           http_con=null;
           }
        }
    }
}
//----------------------------------------------
function FillFields(htmlFields)
{
//alert("htmlFields:"+htmlFields);
document.getElementById("BordTab").rows[2].cells[0].innerHTML=htmlFields;
}
//----------------------------------------------
function ListTypeDoc(FoldType)
{
var http_con=NewAjaxCon();
http_con.onreadystatechange = ObtainDefDoc;
http_con.open('GET', 'ListTypeDocs?Type='+FoldType, true);
http_con.send(null);
var ServAns="";

var http_con2=NewAjaxCon();
http_con2.onreadystatechange = ObtainDefRep;
http_con2.open('GET', 'ListTypeRep?Type='+FoldType, true);
http_con2.send(null);
var ServAns2="";

function ObtainDefDoc()
{
if (http_con.readyState == READY_STATE_COMPLETE)
    {
    if (http_con.status == 200)
       {
       ServAns=http_con.responseText;
       FillFieldsDocs(ServAns);
       http_con=null;
       }
    }
}
function ObtainDefRep()
{
if (http_con2.readyState == READY_STATE_COMPLETE)
    {
    if (http_con2.status == 200)
       {
       ServAns2=http_con2.responseText;
       FillRep(ServAns2);
       http_con2=null;
       }
    }
}
}
//----------------------------------------------
function FillRep(RepIsUrl)
{
var F1=document.getElementById("IdFile");
var F2=document.getElementById("IdFile2");    
if (RepIsUrl=="REFURL")
    {
    F1.className="FFormInputHide";
    F2.className="FFormInput";    
    }
else    
    {
    F1.className="FFormInput";
    F2.className="FFormInputHide";    
    }
}
//----------------------------------------------
function FillFieldsDocs(htmlFields)
{
//alert("htmlFields:"+htmlFields);
document.getElementById("BordTab").rows[2].cells[0].innerHTML=htmlFields;
}
//----------------------------------------------
function SelectRow(DocId)
{
var Act=getCookie("DocId");
if (Act!=null)
    {
    try {
    document.getElementById(Act).className='ListDocs';
    }catch (err)
        {}
    }
document.cookie = "DocId="+DocId;
document.getElementById(DocId).className='ListDocsSel';
}
//----------------------------------------------
function SelectRowFold(DocId)
{
var Act=getCookie("FoldId");
if (Act!=null)
    {
    try {
    document.getElementById(Act).className='ListDocs';
    }catch (err)
        {}
    }
document.cookie = "FoldId="+DocId;
document.getElementById(DocId).className='ListDocsSel';
}
//----------------------------------------------
function getCookie(CookieName)
{
var nameEQ = CookieName + "=";
var CookieList = document.cookie.split(';');
for (var i=0; i<CookieList.length;i++)
    {
    var c = CookieList[i];
    while (c.charAt(0)==' ')
        c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) == 0)
        return c.substring(nameEQ.length, c.length);
    }
return null;
}
//----------------------------------------------
function ShowDoc(DocId)
{
var http_con=NewAjaxCon();
http_con.onreadystatechange = ObtainDocFields;
http_con.open('GET', 'ListDoc?DocId='+DocId, true);
http_con.send(null);
var ServAns="";

    function ObtainDocFields()
    {
    if (http_con.readyState == READY_STATE_COMPLETE)
        {
        if (http_con.status == 200)
           {
           ServAns=http_con.responseText;
           document.getElementById("HeaderRight").innerHTML=ServAns;
           http_con=null;
           }
        }
    }
}
//----------------------------------------------
function ShowDocDel(DocId)
{
var http_con=NewAjaxCon();
http_con.onreadystatechange = ObtainDocFields;
http_con.open('GET', 'ListDocDel?DocId='+DocId, true);
http_con.send(null);
var ServAns="";

    function ObtainDocFields()
    {
    if (http_con.readyState == READY_STATE_COMPLETE)
        {
        if (http_con.status == 200)
           {
           ServAns=http_con.responseText;
           document.getElementById("HeaderRight").rows[1].cells[0].innerHTML=ServAns;
           http_con=null;
           }
        }
    }
}
//----------------------------------------------
function NewAjaxCon()
{
var http_con=null;
if(window.XMLHttpRequest)
  try{
  http_con=new XMLHttpRequest();
  }catch(ex){}
if(http_con==null)
    for(var IEO=["MSXML2.XMLHTTP.6.0","MSXML2.XMLHTTP.3.0","MSXML2.XMLHTTP","Microsoft.XMLHTTP"],n=0,To;To=IEO[n++];)
       try{
       http_con=new ActiveXObject(To);
       break
       }catch(f){}
if (http_con==null)
    alert("XMLHttpRequest fail");
return http_con;
}
//------------------------------------------------------------
function Ver(layerName)
{    
var List_M=document.getElementById(layerName+"S");
var Tot=List_M.length;
for (i=0 ; i < Tot; i++)
    List_M.remove(0);
var Field_M=document.getElementById(layerName).value;
var Terms = Field_M.split("|"); 
for (i=0 ; i < Terms.length ; i++)
    {
    var NewOpt = document.createElement('option');
    NewOpt.text = Terms[i];
    NewOpt.value = Terms[i];    
    List_M.add(NewOpt);
    }
var Panel_M=document.getElementById(layerName+"P");
Panel_M.style.visibility="visible";
}
//------------------------------------------------------------
function NoVer(layerName)
{
document.getElementById(layerName+"P").style.visibility="hidden";
var Attr=document.getElementById(layerName);
var List_M=document.getElementById(layerName+"S");
var Sum="";
for (i=0 ; i < List_M.length; i++)
    {
    if (Sum.length!=0)
        Sum=Sum+"|";
    Sum=Sum+List_M.options[i].text;
    }
Attr.value=Sum;    
}
//------------------------------------------------------------
function SelOption(ElemName)
{
var Sel=document.getElementById(ElemName+"S");   
document.getElementById(ElemName+"F").value=Sel.options[Sel.selectedIndex].text;
}
//------------------------------------------------------------
function Add(ElemName)
{
var NewOpt = document.createElement('option');
var NewTxt=document.getElementById(ElemName+"F").value;
NewOpt.text = NewTxt;
NewOpt.value = NewTxt;    
var Sel=document.getElementById(ElemName+"S");   
Sel.add(NewOpt);
}
//------------------------------------------------------------
function Del(ElemName)
{
var Sel=document.getElementById(ElemName+"S"); 
document.getElementById(ElemName+"F").value="";
Sel.remove(Sel.selectedIndex);
}
//------------------------------------------------------------
function Mod(ElemName)
{
var NewTxt=document.getElementById(ElemName+"F").value;
var Sel=document.getElementById(ElemName+"S");  
Sel.options[Sel.selectedIndex].value=NewTxt;    
Sel.options[Sel.selectedIndex].text=NewTxt;    
}
//------------------------------------------------------------
function SelectRowTerm(DocId)
{
var Act=getCookie("TermId");
if (Act!=null)
    {
    try {
    document.getElementById(Act).className='ListDocs';
    }catch (err)
        {}
    }
document.cookie = "TermId="+DocId;
document.getElementById(DocId).className='ListDocsSel';
}
//------------------------------------------------------------


