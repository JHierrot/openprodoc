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

var IMGLASTLEVEL="img/lastlevel.gif";
var IMGCOLLAPS="img/collapse.gif";
var IMGEXPAND="img/expand.gif";
var IMGUNKNOW="img/unkown.gif";

var READY_STATE_UNINITIALIZED=0;
var READY_STATE_LOADING=1;
var READY_STATE_LOADED=2;
var READY_STATE_INTERACTIVE=3;
var READY_STATE_COMPLETE=4;
var MainUrl="RefreshThes";
var Target="ThesMainFrame";
var Node=null;
//---------------------------------------------------
function ThesExpandContract(Id)
{
try {
document.body.style.cursor = 'wait';
var Status=ThesgetStatus(Id);
//alert ("Status="+Status);
switch(Status)
    {case 0: // LASTLEVEL
         document.body.style.cursor = 'default';
         return;
     case 1:  // CONTRACTED
         ThesExpand(Id);
         break;
     case 2:   // EXPANDED
         ThesContract(Id);
         break;
     case 3: // UNKNOW
         ThesObtainList(Id);
         break;
     case 4: // REFRESH
         ThesObtainList(Id);
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
function ThesgetStatus(Id)
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
function ThesListCarp(Id, xmlDoc)
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
    ThesLastLevel(Id);
    }
else
    {
    AddSubTerm(LC);
    ThesExpand(Id);
    }
}
//----------------------------------------------
function ThesObtainList(Id)
{
var http_con=NewAjaxCon();
http_con.onreadystatechange = muestraContenido;
document.cookie = "ActTermId="+Id;
document.cookie = "DocId= A";
http_con.open('GET', 'ListTerm?Id='+Id, true);
http_con.send(null);
function muestraContenido()
{
if (http_con.readyState == READY_STATE_COMPLETE)
    {
    if (http_con.status == 200)
        {
       ServAns=http_con.responseXML;
       ThesListCarp(Id, ServAns);
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
function ThesLastLevel(Id)
{
var Img=Node.getElementsByTagName('img')[0];
Img.alt=".";
Img.src=IMGLASTLEVEL;
}
//----------------------------------------------
function ThesExpand(Id)
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
function ThesContract(Id)
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
function AddSubTerm(LC)
{
var List=document.createElement("ul");
for(var i=0;i<LC.length; i++)
    {
    List.appendChild(GentTerm(LC[i]));    
    }
Node.appendChild(List);   
}
//----------------------------------------------
function GentTerm(Term)    
{
var Line=document.createElement("li");
Line.id=Term.Id;
var Img=document.createElement("img");
Img.src=IMGUNKNOW;
Img.alt="?";
Img.onclick=Function("ThesExpandContract('"+Term.Id+"')");
Line.appendChild(Img);
var Url=document.createElement("a");
Url.href=MainUrl+"?ThesId="+Term.Id;
Url.target=Target;
Url.appendChild(document.createTextNode(" "+Term.Name));
Line.appendChild(Url);
return(Line);
}
//----------------------------------------------
function ThesUpdate(Id, Name)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0]; 
Ref.replaceChild(document.createTextNode(Name), Ref.firstChild);
}
//----------------------------------------------
function ThesAppend(ParentId, Id, Name)
{
Node=document.getElementById(ParentId);
var List=Node.getElementsByTagName('ul')[0]; 
if (List==null)
    {
    List=document.createElement("ul");
    Node.appendChild(List);  
    ThesExpand(ParentId);
    }
List.appendChild(GentTerm(new Term(Name, Id)));
}
//----------------------------------------------
function ThesDelete(Id)
{
Node=document.getElementById(Id);
Node.parentNode.removeChild(Node);
}
//----------------------------------------------
function ThesMark(Id)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0];
Ref.className="Selected";
}
//----------------------------------------------
function ThesUnMark(Id)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0];
Ref.className="";
}
//----------------------------------------------
