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
 * author: Joaquin Hierro      2012
 * 
 */

//----------------------------------------------
var LASTLEVEL=0;
var COLLAPSED=1;
var EXPANDED=2;
var UNKNOW=3;
var REFRESH=4;

var IMGLASTLEVEL="img/lastlevel.gif";
var IMGCOLLAPS="img/collapse.gif";
var IMGEXPAND="img/expand.gif";
var IMGUNKNOW="img/unkown.gif";

var READY_STATE_UNINITIALIZED=0;
var READY_STATE_LOADING=1;
var READY_STATE_LOADED=2;
var READY_STATE_INTERACTIVE=3;
var READY_STATE_COMPLETE=4;
var MainUrl="RefreshThesSel";
var Target="ThesSelMainFrame";
var Node=null;
//---------------------------------------------------
function ThesSelExpandContract(Id)
{
try {
document.body.style.cursor = 'wait';
var Status=ThesSelgetStatus(Id);
//alert ("Status="+Status);
switch(Status)
    {case 0: // LASTLEVEL
         document.body.style.cursor = 'default';
         return;
     case 1:  // CONTRACTED
         ThesSelExpand(Id);
         break;
     case 2:   // EXPANDED
         ThesSelContract(Id);
         break;
     case 3: // UNKNOW
         ThesSelObtainList(Id);
         break;
     case 4: // REFRESH
         ThesSelObtainList(Id);
         break;
    }
}catch (err)
    {
    alert ("Error="+err);
    document.body.style.cursor = 'default';
    }
document.body.style.cursor = 'default';
}
//---------------------------------------------------
function ThesSelgetStatus(Id)
{
Node=document.getElementById(Id);
var ImgName=Node.getElementsByTagName('img')[0].alt;
if (ImgName=="?" )
    return(UNKNOW);
else if (ImgName=="-")
    return(EXPANDED);
else if (ImgName=="+" )
    return(COLLAPSED);
else 
    return(LASTLEVEL);
}
//--- class Term -----------
function Term(pName, pId)
{
this.Name=pName;
this.Id=pId;
}
//---------------------------------------------------
function ThesSelListCarp(Id, xmlDoc)
{
Err=xmlDoc.getElementsByTagName("error");
if (Err.length!=0)
    {
    alert("Error:"+Err[0].childNodes[0].nodevalue)
    return;
    }
ListTerm=xmlDoc.getElementsByTagName("Term");
var LC=new Array();
for (i=0;i<ListTerm.length;i++)
    {
    LC[i]=new Term (ListTerm[i].getElementsByTagName("name")[0].childNodes[0].nodeValue, ListTerm[i].getElementsByTagName("id")[0].childNodes[0].nodeValue);
    }
if (LC.length==0)
    {   
    ThesSelLastLevel(Id);
    }
else
    {
    AddSubTermSel(LC);
    ThesSelExpand(Id);
    }
}
//----------------------------------------------
function ThesSelObtainList(Id)
{
var http_con=NewAjaxCon();
http_con.onreadystatechange = muestraContenido;
document.cookie = "ActTermIdSel="+Id;
http_con.open('GET', 'ListTerm?Id='+Id, true);
http_con.send(null);
function muestraContenido()
{
if (http_con.readyState == READY_STATE_COMPLETE)
    {
    if (http_con.status == 200)
        {
       ServAns=http_con.responseXML;
       ThesSelListCarp(Id, ServAns);
       ServAns=null; // ¿Potential leaking in IE?
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
//----------------------------------------------
function ThesSelLastLevel(Id)
{
var Img=Node.getElementsByTagName('img')[0];
Img.alt=".";
Img.src=IMGLASTLEVEL;
}
//----------------------------------------------
function ThesSelExpand(Id)
{
var Img=Node.getElementsByTagName('img')[0];
Img.alt="-";
Img.src=IMGCOLLAPS;    
var List=Node.getElementsByTagName('ul')[0];
if (List==null)
    return;
List.className="Showed";
}
//----------------------------------------------
function ThesSelContract(Id)
{
var Img=Node.getElementsByTagName('img')[0];
Img.alt="+";
Img.src=IMGEXPAND;    
var List=Node.getElementsByTagName('ul')[0];
if (List==null)
    return;
List.className="Hided";
}
//----------------------------------------------
function AddSubTermSel(LC)
{
var List=document.createElement("ul");
for(var i=0;i<LC.length; i++)
    {
    List.appendChild(GentTermSel(LC[i]));    
    }
Node.appendChild(List);   
}
//----------------------------------------------
function GentTermSel(Term)    
{
var Line=document.createElement("li");
Line.id=Term.Id;
var Img=document.createElement("img");
Img.src=IMGUNKNOW;
Img.alt="?";
Img.onclick=Function("ThesSelExpandContract('"+Term.Id+"')");
Line.appendChild(Img);
var Url=document.createElement("a");
Url.href=MainUrl+"?ThesSelId="+Term.Id;
Url.target=Target;
Url.appendChild(document.createTextNode(" "+Term.Name));
Line.appendChild(Url);
return(Line);
}
//----------------------------------------------
function ThesSelUpdate(Id, Name)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0]; 
Ref.replaceChild(document.createTextNode(Name), Ref.firstChild);
}
//----------------------------------------------
function ThesSelAppend(ParentId, Id, Name)
{
Node=document.getElementById(ParentId);
var List=Node.getElementsByTagName('ul')[0]; 
if (List==null)
    {
    List=document.createElement("ul");
    Node.appendChild(List);  
    ThesSelExpand(ParentId);
    }
List.appendChild(GentTermSel(new Term(Name, Id)));
}
//----------------------------------------------
function ThesSelDelete(Id)
{
Node=document.getElementById(Id);
Node.parentNode.removeChild(Node);
}
//----------------------------------------------
function ThesSelMark(Id)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0];
Ref.className="Selected";
}
//----------------------------------------------
function ThesSelUnMark(Id)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0];
Ref.className="";
}
//------------------------------------------------------------
function VerSelThes(layerName, Level)
{    
FillListTerm(layerName, Level);
var Panel_M=document.getElementById(layerName+"P");    
Panel_M.style.visibility="visible";
}
//------------------------------------------------------------
function FillListTerm(layerName, Level)
{
var List_M=document.getElementById(layerName+"S");
var Tot=List_M.length;
for (i=0 ; i < Tot; i++)
    List_M.remove(0);
var TermSearch=document.getElementById(layerName+"F").value;
var http_con=NewAjaxCon();
http_con.onreadystatechange = Search;
http_con.open('GET', 'SearchTerm?IdThes='+Level+"&SearchTerm="+TermSearch, true);
http_con.send(null);

function Search()
{
if (http_con.readyState == READY_STATE_COMPLETE)
    {
    if (http_con.status == 200)
        {
        xmlDoc=http_con.responseXML;
        Err=xmlDoc.getElementsByTagName("error");
        if (Err.length!=0)
            {
            alert("Error:"+Err[0].childNodes[0].nodevalue)
            ServAns=null; 
            http_con=null;
            return;
            }
        ListTerm=xmlDoc.getElementsByTagName("Term");
        for (i=0;i<ListTerm.length;i++)
            {
            var NewOpt = document.createElement('option');
            NewOpt.text = ListTerm[i].getElementsByTagName("name")[0].childNodes[0].nodeValue;
            NewOpt.value = ListTerm[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;    
            List_M.add(NewOpt);
            }
        ServAns=null; 
        http_con=null;
        }
    }
}
}
//------------------------------------------------------------
function NoVerSelThes(layerName, Ok)
{
document.getElementById(layerName+"P").style.visibility="hidden";
var TermTxt=document.getElementById(layerName+"_");
var TermId=document.getElementById(layerName);
var List_M=document.getElementById(layerName+"S");
if (Ok=='1')
    {
    for (i=0 ; i < List_M.length; i++)
        {
        if (List_M.options[i].selected==true)    
            {
            TermTxt.value=List_M.options[i].text; 
            TermId.value=List_M.options[i].value; 
            return;
            }
       }
    }
else if (Ok=='2')
   {
   TermTxt.value=""; 
   TermId.value="";       
   }
//TermTxt.value="";
}
//----------------------------------------------
function InfoTerm(layerName)
{
var TermId="";
var List_M=document.getElementById(layerName+"S");
for (i=0 ; i < List_M.length; i++)
    {
    if (List_M.options[i].selected==true)    
        {
        TermId=List_M.options[i].value; 
        break;
        }
   }
//ShowInfoTerm(layerName, TermId+"_Info Detallada");  
var http_con=NewAjaxCon();
http_con.onreadystatechange = ObtainInfoTerm;
http_con.open('GET', 'InfoTerm?Term='+TermId, true);
http_con.send(null);
var ServAns="";

function ObtainInfoTerm()
{
if (http_con.readyState == READY_STATE_COMPLETE)
    {
    if (http_con.status == 200)
       {
       ServAns=http_con.responseText;
       ShowInfoTerm(layerName, ServAns);
       http_con=null;
       }
    }
}
}
//------------------------------------------------------------
function ShowInfoTerm(layerName, InfoTerm)
{
document.getElementById(layerName+"T1").rows[0].cells[0].innerHTML=InfoTerm; 
document.getElementById(layerName+"P2").style.visibility="visible";
}
//------------------------------------------------------------
function NoVerInfoTerm(layerName)
{
document.getElementById(layerName+"P2").style.visibility="hidden";
}
//------------------------------------------------------------
function AddListTerm(TabId, ElemId)
{

}
//------------------------------------------------------------
function DelListTerm(TabId, ElemId)
{
var RowSel=document.getElementById(TabId+ElemId);
RowSel.parentNode.removeChild(RowSel);
}
//------------------------------------------------------------
