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

var IMGLASTLEVEL="img/adm/lastlevel.gif";
var IMGCOLLAPS="img/adm/collapse.gif";
var IMGEXPAND="img/adm/expand.gif";
var IMGUNKNOW="img/adm/unkown.gif";

var READY_STATE_UNINITIALIZED=0;
var READY_STATE_LOADING=1;
var READY_STATE_LOADED=2;
var READY_STATE_INTERACTIVE=3;
var READY_STATE_COMPLETE=4;
var MainUrl="RefreshDocs";
var Target="MainFrame";
var Node=null;
//---------------------------------------------------
function ExpandContract(Id)
{
try {
document.body.style.cursor = 'wait';
var Status=getStatus(Id);
//alert ("Status="+Status);
switch(Status)
    {case 0: // LASTLEVEL
         document.body.style.cursor = 'default';
         return;
     case 1:  // CONTRACTED
         Expand(Id);
         break;
     case 2:   // EXPANDED
         Contract(Id);
         break;
     case 3: // UNKNOW
         ObtainList(Id);
         break;
     case 4: // REFRESH
         ObtainList(Id);
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
function getStatus(Id)
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
//--- class folder -----------
function Folder(pName, pId)
{
this.Name=pName;
this.Id=pId;
}
//---------------------------------------------------
function ListCarp(Id, xmlDoc)
{
Err=xmlDoc.getElementsByTagName("error");
if (Err.length!=0)
    {
    alert("Error:"+Err[0].childNodes[0].nodevalue)
    return;
    }
Fold=xmlDoc.getElementsByTagName("Fold");
var LC=new Array();
for (i=0;i<Fold.length;i++)
    {
    LC[i]=new Folder (Fold[i].getElementsByTagName("name")[0].childNodes[0].nodeValue, Fold[i].getElementsByTagName("id")[0].childNodes[0].nodeValue);
    }
if (LC.length==0)
    {   
    LastLevel(Id);
    }
else
    {
    AddSubFold(LC);
    Expand(Id);
    }
}
//----------------------------------------------
function ObtainList(Id)
{
var http_con=NewAjaxCon();
http_con.onreadystatechange = muestraContenido;
document.cookie = "ActFolderId="+Id;
document.cookie = "DocId= A";
http_con.open('GET', 'ListFolders?Id='+Id, true);
http_con.send(null);
function muestraContenido()
{
if (http_con.readyState == READY_STATE_COMPLETE)
    {
    if (http_con.status == 200)
        {
       ServAns=http_con.responseXML;
       ListCarp(Id, ServAns);
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
function LastLevel(Id)
{
var Img=Node.getElementsByTagName('img')[0];
Img.alt=".";
Img.src=IMGLASTLEVEL;
}
//----------------------------------------------
function Expand(Id)
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
function Contract(Id)
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
function AddSubFold(LC)
{
var List=document.createElement("ul");
for(var i=0;i<LC.length; i++)
    {
    List.appendChild(GentFold(LC[i]));    
    }
Node.appendChild(List);   
}
//----------------------------------------------
function GentFold(Fold)    
{
var Line=document.createElement("li");
Line.id=Fold.Id;
var Img=document.createElement("img");
Img.src=IMGUNKNOW;
Img.alt="?";
Img.onclick=Function("ExpandContract('"+Fold.Id+"')");
Line.appendChild(Img);
var Url=document.createElement("a");
Url.href=MainUrl+"?FoldId="+Fold.Id;
Url.target=Target;
Url.appendChild(document.createTextNode(" "+Fold.Name));
Line.appendChild(Url);
return(Line);
}
//----------------------------------------------
function Update(Id, Name)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0]; 
Ref.replaceChild(document.createTextNode(Name), Ref.firstChild);
}
//----------------------------------------------
function Append(ParentId, Id, Name)
{
Node=document.getElementById(ParentId);
var List=Node.getElementsByTagName('ul')[0]; 
if (List==null)
    {
    List=document.createElement("ul");
    Node.appendChild(List);  
    Expand(ParentId);
    }
List.appendChild(GentFold(new Folder(Name, Id)));
}
//----------------------------------------------
function Delete(Id)
{
Node=document.getElementById(Id);
Node.parentNode.removeChild(Node);
}
//----------------------------------------------
function Mark(Id)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0];
Ref.className="Selected";
}
//----------------------------------------------
function UnMark(Id)
{
Node=document.getElementById(Id);
var Ref=Node.getElementsByTagName('a')[0];
Ref.className="";
}
//----------------------------------------------
