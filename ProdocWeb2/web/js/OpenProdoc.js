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

var layout;
var menu;
var DocsTree;
var DocsGrid;
var layoutThes;
var menuThes;
var ThesTree;
var ThesGrid;

var ROOTFOLD="RootFolder";
var ROOTTHES="OPD_Thesaurus";
var FoldForm;
var myWins=null;
var CurrFold=ROOTFOLD;
var CurrThes=ROOTTHES;
var CurrTerm="";
var CurrDoc="";
var OK="OK";
var CANCEL="CANCEL";
var NewFoldId="";
var NewFoldName="";
var NewThesId="";
var NewThesName="";
var WinAF;
var WinMT;
var WinRes;
var FormAddFold; 
var TabBar;
var GridResults;
var ToolBar;
var GridReports;
var MantFromGrid=false;
var TmpLayout;

function doOnLoadLogin() 
{   
var myForm;    
var formData = [
                {type: "settings", position: "label-left" , inputWidth:"auto", labelWidth: 80},
                {type: "fieldset",name:"Login", label: "Login", width:300, 
                    list:[
                    {type: "input", name:"User", label: "User", value: "", required:true },
                    {type: "password", name:"Password", label: "Password", value: "", required:true },
                    {type: "button", value: "Ok", name: "Send"}
                    ]}
		];
myForm = new dhtmlXForm("MainBody", formData);
myForm.attachEvent("onButtonClick", function(id)
    {
    if (id == "Send") 
        {
        myForm.send("Login", function(loader, response)
            {
            if (response.slice(0,2) == OK)
               // alert("Conectado");
                window.location.reload(true);    
                // window.setTimeout(function(){ window.location = "SMain"; },1000);
            else
                alert("["+response+"]");
            });
        }
    });
		   
}
//--------------------------------------------------------------
function doOnLoadMain() 
{                      
layout = new dhtmlXLayoutObject(document.body,"3L");         
layout.cells("a").setText("Folders Tree"); 
layout.cells("a").setWidth(300);
layout.cells("b").setText("Current Folder");   
layout.cells("c").setText("Folder Documents");   
menu = layout.attachMenu();
menu.loadStruct("Menu", function(){});
menu.attachEvent("onClick", function(id, zoneId, cas)
    {
    ExecMenu(id);    
    });
DocsGrid = layout.cells("c").attachGrid();
DocsGrid.setHeader("Type,Title,Lock,Date");   //sets the headers of columns
DocsGrid.setColumnIds("Type,Title,Lock,Date");         //sets the columns' ids
DocsGrid.setInitWidths("100,*,60,80");   //sets the initial widths of columns
DocsGrid.setColAlign("left,left,left,left");     //sets the alignment of columns
DocsGrid.setColTypes("ro,link,ro,ro");               //sets the types of columns
DocsGrid.setColSorting("str,str,str,str");  //sets the sorting types of columns
DocsGrid.load("DocList?FoldId="+ROOTFOLD);
DocsGrid.init();
DocsGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrDoc=rowId;    
//    printLog("<b>Row </b> id="+rowId+"<br>");
    return(true);
    });
DocsGrid.selectRow(0, true, false, true);
DocsTree = layout.cells("a").attachTree();
DocsTree.setImagePath("js/imgs/dhxtree_skyblue/");
DocsTree.setXMLAutoLoading("Tree");
DocsTree.load("Tree");
DocsTree.showItemSign("System",true);
DocsTree.attachEvent("onClick",function(id)
    {
    CurrFold=id;
    layout.cells("b").attachURL("SFoldRec", true, {FoldId: CurrFold});
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
    });
DocsTree.selectItem(ROOTFOLD);
layout.cells("b").attachURL("SFoldRec", true, {FoldId: CurrFold});
if (myWins==null)
    myWins = new dhtmlXWindows();
};
//----------------------------------
function ExecMenu(IdMenu)
{  
document.body.style.cursor = 'default';
switch (IdMenu)    
    {
    case "AddFold": AddFold(false);
        break;
    case "DelFold": DelFold(CurrFold);
        break;
    case "UpdFold": UpdFold(CurrFold);
        break;
    case "AddExtF": AddFoldExt();
        break;
    case "ModExtF": ModFoldExt(CurrFold);
        break;
    case "SearchFold": SearchFold();
        break;
    case "RefreshFold": DocsTree.refreshItem(CurrFold);
        break;
    case "AddDoc": AddDoc(CurrFold);
        break;
    case "AddExtDoc": AddExtDoc(CurrFold);
        break;
    case "ModExtDoc" : ModDoc(CurrDoc, false, "");
        break;
    case "DocMetadata": ModDoc(CurrDoc, true, "");
        break;
    case "DelDoc": DelDoc(CurrDoc);
        break;
    case "CheckOut": CheckOut(CurrDoc);
        break;
    case "CheckIn": CheckIn(CurrDoc);
        break;
    case "CancelCheckOut": CancelCheckOut(CurrDoc);
        break;
    case "SearchDoc": SearchDoc();
        break;
    case "ListVer": ListVer(CurrDoc);
        break;
    case "PasswordChange": PassChange();
        break;
    case "TrashBin": TrashBin();
        break;
    case "Thesaurus": OpenThes();
        break;
    case "ReportingBugs": window.open("https://docs.google.com/spreadsheet/viewform?usp=drive_web&formkey=dEpsRzZzSmlaQVZET0g2NDdsM0ZRaEE6MA#gid=0");
        break;
    case "Exit": window.location.assign("Logout");
        layoutThes.unload();
        layoutThes=null;
        layout.unload();
        layout=null;
        myWins.unload();
        myWins=null;
        break;
    case "About": About();    
        break;
    default:     
        printLog("<b>Menu onClick</b> id="+IdMenu+"<br>");
    }
}
//----------------------------------
function doOnLoadThes() 
{                      
layoutThes = new dhtmlXLayoutObject(document.body,"2U");         
layoutThes.cells("a").setText("Thesaurus"); 
layoutThes.cells("a").setWidth(400);
layoutThes.cells("b").setText("Current Term");    
menuThes = layoutThes.attachMenu();
menuThes.loadStruct("MenuThes", function(){});
menuThes.attachEvent("onClick", function(id, zoneId, cas)
    {
    ExecMenuThes(id);    
    });
ThesTree = layoutThes.cells("a").attachTree();
ThesTree.setImagePath("js/imgs/dhxtree_skyblue/");
ThesTree.setXMLAutoLoading("ThesTree");
ThesTree.load("ThesTree?ThesId="+ROOTTHES);
ThesTree.showItemSign("System",true);
ThesTree.attachEvent("onClick",function(id)
    {
    if (ThesTree.getParentId(id)==ROOTTHES)   
        {
        CurrThes=id;
        CurrTerm=id;
        }
    else
        CurrTerm=id;
    layoutThes.cells("b").attachURL("SThesRec", true, {ThesId: CurrTerm});
    });
layoutThes.cells("b").attachURL("SThesRec?ThesId="+ROOTTHES);
if (myWins==null)
    myWins = new dhtmlXWindows();
};
//----------------------------------
function ExecMenuThes(IdMenu)
{  
MantFromGrid=false;  
document.body.style.cursor = 'default';
switch (IdMenu)    
    {
    case "CloseWindow": window.close();
        break;
    case "CreateTheusurus": AddThes();
        break;
    case "UpdateThesaurus": UpdThes();
        break;
    case "DeleteThesaurus": DelThes();
        break;
    case "AddTerm": MantTerm("AddTerm", CurrTerm);
        break;
    case "UpdateTerm": MantTerm("UpdTerm", CurrTerm);
        break;
    case "DeleteTerm": MantTerm("DelTerm", CurrTerm);
        break;
    case "SearchTerms": SearchTerm();
        break;
    case "RefreshThesaurus": ThesTree.refreshItem(CurrTerm);
        break;
    case "ExportThesaurus": ExportThes();
        break;
    case "ImportThesaurus": ImportThes();
        break;
    default:     
        printLog("<b>Menu onClick</b> id="+IdMenu+"<br>");
    }
}
//-----------------------------------
function AddThes()
{
var Url="AddThes";
WinAF=myWins.createWindow({
id:"AddThes",
left:20,
top:30,
width:500,
height:300,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormAddThes=WinAF.attachForm();
FormAddThes.loadStruct(Url+"?T="+CurrThes, function(){
FormAddThes.setFocusOnFirstActive();
});
FormAddThes.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        NewThesName=FormAddThes.getItemValue("Name");
        NewThesId=FormAddThes.getItemValue("PDId");
        FormAddThes.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ThesTree.insertNewChild(ROOTTHES, NewThesId, NewThesName);
                            FormAddThes.unload();
                            WinAF.close();   
                            }
                        } );
        }
     else 
        {   
        FormAddThes.unload();
        WinAF.close();
        }
     }
             );
}
//-----------------------------------
function MantTerm(Url, idCurrTerm)
{
if (idCurrTerm==ROOTTHES || idCurrTerm==null || idCurrTerm=="") 
    return;
WinMT=myWins.createWindow({
id:"mantTerm",
left:20,
top:30,
width:500,
height:600,
center:true,
modal:true,
resize:true});  
WinMT.setText("OpenProdoc");
var TermLa=WinMT.attachLayout("2E");
var FormAddTerm=TermLa.cells("a").attachForm();
TermLa.cells("a").hideHeader();
TermLa.cells("b").hideHeader();
var TermTabBar=TermLa.cells("b").attachTabbar();
TermTabBar.addTab("RT", "RT", null, null, true);
if (Url!="DelTerm")
    {
    var RTTB=TermTabBar.tabs("RT").attachToolbar();
    RTTB.addButton("Add", 0, "Add", "Add.png", "Add.png");
    RTTB.addButton("Del", 1, "Del", "Del.png", "Del.png");
    RTTB.attachEvent("onClick", function(id)
        {
        CurrThes=FormAddTerm.getUserData("TES_USE", "ThesId");          
        TermManRel("RT", id, RTGrid);    
        });
    }
var RTGrid=TermTabBar.tabs("RT").attachGrid();
PrepTermGrid(RTGrid);
RTGrid.init();
TermTabBar.addTab("Lang", "Lang");
if (Url!="DelTerm")
    {
    var LangTB=TermTabBar.tabs("Lang").attachToolbar();
    LangTB.addButton("Add", 0, "Add", "Add.png", "Add.png");
    LangTB.addButton("Del", 1, "Del", "Del.png", "Del.png");
    LangTB.attachEvent("onClick", function(id)
        {
        CurrThes=FormAddTerm.getUserData("TES_USE", "ThesId");          
        TermManRel("Lang", id, LangGrid);    
        });
    }
var LangGrid=TermTabBar.tabs("Lang").attachGrid();
PrepTermGrid(LangGrid);
LangGrid.init();
TermTabBar.addTab("UF", "UF");
var UFGrid=TermTabBar.tabs("UF").attachGrid();
PrepTermGrid(UFGrid); 
UFGrid.init();
TermTabBar.addTab("NT", "NT");
var NTGrid=TermTabBar.tabs("NT").attachGrid();
PrepTermGrid(NTGrid);
if (Url!="AddTerm")
    {
    RTGrid.load("TermRT?Id="+idCurrTerm);
    LangGrid.load("TermLang?Id="+idCurrTerm);
    UFGrid.load("TermUF?Id="+idCurrTerm);
    NTGrid.load("TermNT?Id="+idCurrTerm);
    }
NTGrid.init();
FormAddTerm.loadStruct(Url+"?T="+idCurrTerm+"&Tes="+CurrThes, function(){
FormAddTerm.setFocusOnFirstActive();
});
FormAddTerm.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        NewThesName=FormAddTerm.getItemValue("Name");
        UpdateTerms(FormAddTerm, "H_RT", RTGrid);
        UpdateTerms(FormAddTerm, "H_Lang", LangGrid);        
        FormAddTerm.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            NewThesId=response.substring(2,30);    
                            if (Url=="AddTerm")
                                {
                                NewThesId=response.substring(2,30);
                                ThesTree.insertNewChild(idCurrTerm, NewThesId, NewThesName);
                                }
                            else if (Url=="UpdTerm")
                                {
                                if (MantFromGrid)
                                    {
                                    GridResults.cells(idCurrTerm, 1).setValue(FormAddTerm.getItemValue("Name"));
                                    GridResults.cells(idCurrTerm, 2).setValue(FormAddTerm.getItemValue("Definition"));
                                    GridResults.cells(idCurrTerm, 3).setValue(FormAddTerm.getItemValue("TES_USE"));
                                    GridResults.cells(idCurrTerm, 4).setValue(FormAddTerm.getItemValue("SCN"));
                                    GridResults.cells(idCurrTerm, 5).setValue(FormAddTerm.getItemValue("TES_LANG"));
                                    }
                                else    
                                    layoutThes.cells("b").attachURL("SThesRec", true, {ThesId: idCurrTerm});                                
                                ThesTree.setItemText(NewThesId, NewThesName);
                                }
                            else if (Url=="DelTerm")
                                {
                                if (MantFromGrid)
                                    GridResults.deleteRow(idCurrTerm);
                                ThesTree.deleteItem(idCurrTerm, true);
                                }
                            FormAddTerm.unload();
                            WinMT.close();    
                            }
                        } );
        }
     else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddTerm, name.substring(2)); 
     else 
        {   
        FormAddTerm.unload();
        WinMT.close();
        }
     }
             );
}
//-----------------------------------
function UpdateTerms(FormAddTerm, field, Grid)
{
var Sum="";
for (var i=0; i<Grid.getRowsNum(); i++)
    {
    Sum+=Grid.getRowId(i)+"|"; 
    }    
FormAddTerm.setItemValue(field, Sum);    
}
//-----------------------------------
function TermManRel(TypeRel, idButton, Grid)
{
if (idButton=="Del")
    Grid.deleteRow(Grid.getSelectedRowId());
else if (idButton=="Add")
    ShowThesGrid(Grid);
}
//-----------------------------------
function PrepTermGrid(TermGrid)
{
TermGrid.setHeader("Name,Def,Use,Note,Lang");   //sets the headers of columns
TermGrid.setColumnIds("Name,Def,Use,Note,Lang");         //sets the columns' ids
TermGrid.setInitWidths("*,60,60,60,60");   //sets the initial widths of columns
TermGrid.setColAlign("left,left,left,left,left");     //sets the alignment of columns
TermGrid.setColTypes("ro,ro,ro,ro,ro");               //sets the types of columns
TermGrid.setColSorting("str,str,str,str,str");  //sets the sorting types of columns    
}
//-----------------------------------
function UpdThes()
{
var Url="ModThes";
WinAF=myWins.createWindow({
id:"UpdThes",
left:20,
top:30,
width:500,
height:300,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormAddThes=WinAF.attachForm();
FormAddThes.loadStruct(Url+"?T="+CurrThes, function(){
FormAddThes.setFocusOnFirstActive();
});
FormAddThes.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        NewThesName=FormAddThes.getItemValue("Name");
        NewThesId=FormAddThes.getItemValue("PDId");
        FormAddThes.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ThesTree.setItemText(NewThesId, NewThesName);
                            layoutThes.cells("b").attachURL("SThesRec", true, {ThesId: CurrTerm});
                            FormAddThes.unload();
                            WinAF.close();    
                            }
                        } );
        }
     else 
        {   
        FormAddThes.unload();
        WinAF.close();
        }
     }
             );
    
}
//-----------------------------------
function DelThes()
{
var Url="DelThes";
WinAF=myWins.createWindow({
id:"DelThes",
left:20,
top:30,
width:500,
height:300,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormAddThes=WinAF.attachForm();
FormAddThes.loadStruct(Url+"?T="+CurrThes, function(){
FormAddThes.setFocusOnFirstActive();
});
FormAddThes.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        NewThesId=FormAddThes.getItemValue("PDId");
        FormAddThes.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ThesTree.deleteItem(NewThesId, true);
                            CurrThes="OPD_Thesaurus";
                            FormAddThes.unload();
                            WinAF.close();   
                            }
                        } );
        }
     else 
        {   
        FormAddThes.unload();
        WinAF.close();
        }
     }
              );
    
}
//-----------------------------------
function SearchTerm()
{
var Url="SearchTerm";
WinAF=myWins.createWindow({
id:"SearchTerm",
left:20,
top:30,
width:600,
height:500,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
TabBar=WinAF.attachTabbar();
TabBar.addTab("Search", "Search", null, null, true);
TabBar.addTab("Results", "Results");
ToolBar = TabBar.tabs("Results").attachToolbar();
ToolBar.addButton("Edit", 0, "Edit", "Edit.png", "Edit.png");
ToolBar.addButton("Delete", 1, "Delete", "Edit.png", "Edit.png");
ToolBar.attachEvent("onClick", function(id)
    {
    if (GridResults.getSelectedRowId()!=null)     
        {
        TermResProc(id, GridResults.getSelectedRowId());   
        }
    });
var FormSearchTerm = TabBar.tabs("Search").attachForm();
FormSearchTerm.loadStruct("SearchTerm?Thes="+CurrThes+"&Term="+CurrTerm, function(){
FormSearchTerm.setFocusOnFirstActive();
});    
FormSearchTerm.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        document.body.style.cursor = 'wait';    
        FormSearchTerm.send("SearchTerm", function(loader, response)
                        { // Asynchronous 
                        document.body.style.cursor = 'default';
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ShowTermResults(response.substring(2));  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormSearchTerm.unload();
        WinAF.close();
        }  
    }
    );           
}
//-----------------------------------
function ShowTermResults(Result)
{
GridResults=TabBar.tabs("Results").attachGrid();
var ListPar=Result.split("\n");
GridResults.setHeader(ListPar[0]);
//ResGrid.setInitWidths("70,150,*");
GridResults.setColTypes(ListPar[1]);   
GridResults.setColSorting(ListPar[2]);
GridResults.init();
GridResults.parse(ListPar[3],"json");
TabBar.tabs("Results").show(true);
//setTimeout( "TabBar.tabs('Results').show(true)" , 4000);    
}
//-----------------------------------
function TermResProc(Order, idTerm)
{
MantFromGrid=true;    
if (Order=="Edit")  
    { 
    MantTerm("UpdTerm", idTerm);
    }
else if (Order=="Delete")  
    { 
    MantTerm("DelTerm", idTerm);
    }     
}
//-----------------------------------
function ExportThes()
{
Url="ExportThes";    
WinAF=myWins.createWindow({
id:"AddThes",
left:20,
top:30,
width:500,
height:250,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormExpThes=WinAF.attachForm();
FormExpThes.loadStruct(Url+"?CurrThesId="+CurrThes, function(){
FormExpThes.setFocusOnFirstActive();
});
FormExpThes.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        window.open(Url+"?RootText="+FormExpThes.getItemValue("RootText")+"&MainLanguage="+FormExpThes.getItemValue("MainLanguage")+"&HideThesId="+FormExpThes.getItemValue("HideThesId"));    
        }
    FormExpThes.unload();
    WinAF.close();
    }
    );    
}
//-----------------------------------
function ImportThes()
{   
WinAF=myWins.createWindow({
id:"ImportThes",
left:20,
top:30,
width:500,
height:450,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormImpThes=WinAF.attachForm();
FormImpThes.loadStruct("ImportThes0", function(){
FormImpThes.setFocusOnFirstActive();
});
FormImpThes.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormImpThes.send("ImportThes", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            FormImpThes.enableItem("UpFile");                            
                        else 
                            alert(response.substring(2)); 
                        });
        }
      else if (name==CANCEL)  
        {
        FormImpThes.unload();
        WinAF.close();
        }
    }
    );    
FormImpThes.attachEvent("onUploadFile",function(realName,serverName)
    {
    ThesTree.refreshItem(ROOTTHES);
    FormImpThes.unload();
    WinAF.attachHTMLString(serverName);
    });
//    });
FormImpThes.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });
}
//-----------------------------------
function About()
{
var WinA=myWins.createWindow({
    id:"About",
    left:20,
    top:30,
    width:500,
    height:360,
    center:true,
    modal:true,
    resize:false
});  
WinA.setText("About OpenProdoc");
var FormAbout=WinA.attachForm();
FormAbout.loadStruct("About");
}
//----------------------------------
function AddFold()
{
var Url="AddFold";
WinAF=myWins.createWindow({
id:"AddFold",
left:20,
top:30,
width:500,
height:160,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormAddFold=WinAF.attachForm();
FormAddFold.loadStruct(Url+"?F="+CurrFold, function(){
FormAddFold.setFocusOnFirstActive();
});
FormAddFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        NewFoldName=FormAddFold.getItemValue("Title");
        FormAddFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            NewFoldId=response.substring(2,30);
                            DocsTree.insertNewChild(CurrFold, NewFoldId, NewFoldName);
                            FormAddFold.unload();
                            WinAF.close();  
                            }
                        } );
        }
     else 
        {   
        FormAddFold.unload();
        WinAF.close();
        }
     });
}
//------------------------------------------------------------
function AddFoldExt()
{
var Url="AddFoldExt";
WinAF=myWins.createWindow({
id:"AddFold",
left:20,
top:30,
width:500,
height:500,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
var LayoutFold=WinAF.attachLayout('2E');
var a = LayoutFold.cells('a');
a.hideHeader();
a.setHeight(50);
var formCombo = a.attachForm();
formCombo.loadStruct('formCombo');
var b = LayoutFold.cells('b');
b.hideHeader();
FormAddFold = b.attachForm();  
formCombo.attachEvent("onChange", function(name, value, is_checked){
    FormAddFold.unload();
    FormAddFold = b.attachForm();
    CreaFoldMain(Url, value);
    });
CreaFoldMain(Url, "PD_FOLDERS");   
}
//----------------------------------
function CreaFoldMain(Url, Type)
{
FormAddFold.loadStruct(Url+"?F="+CurrFold+"&Ty="+Type, function(){
FormAddFold.setFocusOnFirstActive();
});    
FormAddFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        NewFoldName=FormAddFold.getItemValue("Title");
        FormAddFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            NewFoldId=response.substring(2,30);
                            DocsTree.insertNewChild(CurrFold, NewFoldId, NewFoldName);
                            FormAddFold.unload();
                            WinAF.close();    
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormAddFold.unload();
        WinAF.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormAddFold, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddFold, name.substring(2));  
    }
    );   
}
//------------------------------------------------------------
function ModFoldExt(EditFold)
{
var Url="ModFoldExt";
WinAF=myWins.createWindow({
id:"ModFold",
left:20,
top:30,
width:500,
height:420,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
FormAddFold=WinAF.attachForm();
ModFoldMain(EditFold, Url, "");   
}
//----------------------------------
function ModFoldMain(EditFold, Url, Type)
{
FormAddFold.loadStruct(Url+"?F="+EditFold+"&Ty="+Type, function(){
FormAddFold.setFocusOnFirstActive();
});    
FormAddFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        NewFoldName=FormAddFold.getItemValue("Title");
        FormAddFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            DocsTree.setItemText(EditFold, NewFoldName);
                            if (CurrFold==EditFold)
                               layout.cells("b").attachURL("SFoldRec", true, {FoldId: CurrFold});
                            FormAddFold.unload();
                            WinAF.close();  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormAddFold.unload();
        WinAF.close();
        }
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddFold, name.substring(2));  
    else 
        ShowMulti(FormAddFold, name.substring(2));    
    }
    );   
}
//----------------------------------
function ShowMulti(Form, AttName)
{
var WinMulti=myWins.createWindow({
id:"Multi",
left:20,
top:30,
width:300,
height:250,
center:true,
modal:true,
resize:true});   
WinMulti.setText("OpenProdoc");  
var MultiFormData = [
				{type: "settings", position: "label-left", labelWidth: 100, inputWidth: 250},
                {type: "input", name: "Val"},
                {type: "block", width: 250, list:[
                    {type: "button", name: "add", value: "+"},
                    {type: "newcolumn", offset:2 },
                    {type: "button", name: "del", value: "-"},
                    {type: "newcolumn", offset:2 },
                    {type: "button", name: "mod", value: "/"}
                    ]},
                {type: "multiselect", name: "Vals"},
                {type: "block", width: 250, list:[
                    {type: "button", name: "OK", value: "Ok"},
                    {type: "newcolumn", offset:20 },
                    {type: "button", name: "CANCEL", value: "Cancel"}]
			        }];
var MultiForm=WinMulti.attachForm(MultiFormData);
var Vals=MultiForm.getSelect("Vals");
if (Form.getItemValue(AttName).trim().length!=0)
    {
    var CurVals=Form.getItemValue(AttName).split("|"); 
    CurVals.sort();
    for (var i=0; i<CurVals.length; i++)
        Vals.options.add(new Option(CurVals[i],CurVals[i]));
    }
MultiForm.setItemFocus("Val");    
MultiForm.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {
//        Form.setItemValue(AttName, MultiForm.getItemValue("Vals")); 
        var Sum="";
        for (var i=0; i<Vals.options.length; i++)
            {
            if (i!=0)    
                Sum+="|";
            Sum+=Vals.options[i].value;
            }
        Form.setItemValue(AttName, Sum);    
        MultiForm.unload();
        WinMulti.close();
        }
    else if (name==CANCEL) 
        {   
        MultiForm.unload();
        WinMulti.close();
        }
    else if (name=='add') 
        {   
        var Entry=MultiForm.getItemValue("Val").trim(); 
        if (Entry.length!=0)
            Vals.options.add(new Option(Entry,Entry));
        MultiForm.setItemValue("Val", "");
        MultiForm.setItemFocus("Val");
        }
    else if (name=='del') 
        { 
        for (var i=0; i<Vals.options.length; i++)    
            {   
			if (Vals.options[i].value==MultiForm.getItemValue("Vals"))
                {
				Vals.options.remove(i);
                break;
                }  
			}
        } 
    else if (name=='mod') 
        { 
        var Entry=MultiForm.getItemValue("Val").trim(); 
        if (Entry.length!=0)
            {
            for (var i=0; i<Vals.options.length; i++)    
                {   
                if (Vals.options[i].value==MultiForm.getItemValue("Vals"))
                    {
                    Vals.options.remove(i);
                    Vals.options.add(new Option(Entry,Entry));
                    break;
                    }  
                }
            MultiForm.setItemValue("Val", "");
            MultiForm.setItemFocus("Val");    
            }
        }     
    });        
MultiForm.attachEvent("onChange", function (name, value)
    {if (name=="Vals")
        MultiForm.setItemValue("Val", value);   
//        MultiForm.setItemValue("Val", MultiForm.getItemValue("Vals"));   
    });
}
//----------------------------------
function ShowThes(Form, AttName)
{
var ThesId=Form.getUserData(AttName, "ThesId");   
var TermId=Form.getItemValue("TH_"+AttName);   
var WinSelThes=myWins.createWindow({
id:"SelThes",
left:20,
top:30,
width:400,
height:350,
center:true,
modal:true,
resize:true});   
WinSelThes.setText("OpenProdoc");  
var Theslayout=WinSelThes.attachLayout("2U");
Theslayout.cells("a").setText("Thesaurus"); 
Theslayout.cells("a").setWidth(200);
Theslayout.cells("b").setText("Current Term"); 
var STToolBar = Theslayout.attachToolbar();
STToolBar.addButton("Select", 0, "Select", "Edit.png", "Edit.png");
var SelThesTree = Theslayout.cells("a").attachTree();
SelThesTree.setImagePath("js/imgs/dhxtree_skyblue/");
SelThesTree.setXMLAutoLoading("ThesTree");
SelThesTree.load("ThesTree?ThesId="+ThesId);
SelThesTree.showItemSign("System",true);
var SelTerm;
SelThesTree.attachEvent("onClick",function(id)
    {
    SelTerm=id;
    Theslayout.cells("b").attachURL("SThesRec", true, {ThesId: SelTerm});
    });
if (TermId!="")    
    Theslayout.cells("b").attachURL("SThesRec?ThesId="+TermId);
else
    Theslayout.cells("b").attachURL("SThesRec?ThesId="+ThesId);
STToolBar.attachEvent("onClick", function(id)
    {
    TermId=SelThesTree.getSelectedItemId();  
    Form.setItemValue("TH_"+AttName, TermId); 
    var Text=SelThesTree.getSelectedItemText();
    Form.setItemValue(AttName, Text);
    WinSelThes.close();
    WinSelThes.unload();
    });
}
//----------------------------------
function ShowThesGrid(Grid)
{
var ThesId=CurrThes;   
//var TermId=Form.getItemValue("TH_"+AttName);   
var WinSelTerm=myWins.createWindow({
id:"SelThes",
left:20,
top:30,
width:400,
height:350,
center:true,
modal:true,
resize:true});   
WinSelTerm.setText("OpenProdoc");  
var Theslayout=WinSelTerm.attachLayout("2U");
Theslayout.cells("a").setText("Thesaurus"); 
Theslayout.cells("a").setWidth(200);
Theslayout.cells("b").setText("Current Term"); 
var STToolBar = Theslayout.attachToolbar();
STToolBar.addButton("Select", 0, "Select", "Edit.png", "Edit.png");
var SelThesTree = Theslayout.cells("a").attachTree();
SelThesTree.setImagePath("js/imgs/dhxtree_skyblue/");
SelThesTree.setXMLAutoLoading("ThesTree");
SelThesTree.load("ThesTree?ThesId="+ThesId);
SelThesTree.showItemSign("System",true);
var SelTerm;
SelThesTree.attachEvent("onClick",function(id)
    {
    SelTerm=id;
    Theslayout.cells("b").attachURL("SThesRec", true, {ThesId: SelTerm});
    });
Theslayout.cells("b").attachURL("SThesRec?ThesId="+ThesId);
STToolBar.attachEvent("onClick", function(id)
    {
    TermId=SelThesTree.getSelectedItemId();  
    window.dhx4.ajax.get("TermDetails?Term="+TermId, function(r){
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("col");
    var response="";
    for (var q=0; q<nodes.length; q++) 
        response+=nodes[q].textContent+","; 
    Grid.addRow(TermId, response);
    WinSelTerm.close();
    WinSelTerm.unload();
    });
    });
}
//----------------------------------
function DelFold(DelFold)
{
var WinDF=myWins.createWindow({
    id:"DelFold",
    left:20,
    top:30,
    width:500,
    height:420,
    center:true,
    modal:true,
    resize:false
});  
WinDF.setText("OpenProdoc");
var FormDelFold=WinDF.attachForm();
FormDelFold.loadStruct("DelFold?F="+DelFold);
FormDelFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormDelFold.send("DelFold", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            DocsTree.deleteItem(DelFold,true);
                            if (CurrFold=DelFold)
                                CurrFold=DocsTree.getSelectedItemId();
                            FormDelFold.unload();
                            WinDF.close();   
                            }
                        } );
        }
     else 
        {   
        FormDelFold.unload();
        WinDF.close();
        }
     }
             );
}
//----------------------------------
function UpdFold(EditFold)
{
var WinUF=myWins.createWindow({
    id:"UpdFold",
    left:20,
    top:30,
    width:500,
    height:160,
    center:true,
    modal:true,
    resize:false
});  
WinUF.setText("OpenProdoc");
var FormUpdFold=WinUF.attachForm();
FormUpdFold.loadStruct("UpdFold?F="+EditFold, function(){
FormUpdFold.setFocusOnFirstActive();
});
FormUpdFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        NewFoldName=FormUpdFold.getItemValue("Title");
        FormUpdFold.send("UpdFold", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            DocsTree.setItemText(EditFold, NewFoldName);
                            layout.cells("b").attachURL("SFoldRec", true, {FoldId: CurrFold});
                            FormUpdFold.unload();
                            WinUF.close();  
                            }
                        } );
        }
     else 
        {   
        FormUpdFold.unload();
        WinUF.close();
        }
     }
             );
}
//----------------------------------
function printLog(text) 
{
layout.cells("b").attachHTMLString(text+"/"+DocsTree.getSelectedItemId()+">>"+DocsGrid.getSelectedRowId());
}
//----------------------------------
function OpenThes()
{
window.open("SThesaurMain");    
}
//----------------------------------
function SearchFold()
{
var Url="SearchFold";
WinAF=myWins.createWindow({
id:"SearchFold",
left:20,
top:30,
width:600,
height:650,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
var LayoutFold=WinAF.attachLayout('2E');
var a = LayoutFold.cells('a');
a.hideHeader();
a.setHeight(50);
var formCombo = a.attachForm();
formCombo.loadStruct('formCombo');
var b = LayoutFold.cells('b');
b.hideHeader();
TabBar=b.attachTabbar();
TabBar.addTab("Search", "Search", null, null, true);
TabBar.addTab("Results", "Results");
TabBar.addTab("Reports", "Reports");
GridReports=TabBar.tabs("Reports").attachGrid();
GridReports.setHeader("Title,Mime,Docs/page, Pages/arch");   //sets the headers of columns
GridReports.setColumnIds("Title,Mime,DocsPage, PagesArch");         //sets the columns' ids
GridReports.setInitWidths("*,60,60,60");   //sets the initial widths of columns
GridReports.setColAlign("left,left,left,left");     //sets the alignment of columns
GridReports.setColTypes("link,ro,ro,ro");               //sets the types of columns
GridReports.setColSorting("str,str,int,int");  //sets the sorting types of columns
GridReports.load("RepList?Type=Fold");
GridReports.init();
TabBar.tabs("Reports").disable();
ToolBar = TabBar.tabs("Results").attachToolbar();
ToolBar.addButton("Edit", 0, "Edit", "Edit.png", "Edit.png");
ToolBar.addButton("Delete", 1, "Delete", "Edit.png", "Edit.png");
//ToolBar.addButton("CSV", 2, "CSV", "Edit.png", "Edit.png");
//ToolBar.addButton("Report", 2, "Report", "Edit.png", "Edit.png");
ToolBar.attachEvent("onClick", function(id)
    {
    if (GridResults.getSelectedRowId()!=null)    
        FoldResProc(id, GridResults.getSelectedRowId());    
//    alert("Boton="+id+" Linea="+GridResults.getSelectedRowId());
    });
FormAddFold = TabBar.tabs("Search").attachForm();
formCombo.attachEvent("onChange", function(name, value, is_checked){
    FormAddFold.unload();
    //FormAddFold = b.attachForm();
    FormAddFold = TabBar.tabs("Search").attachForm();
    SearchFoldMain(Url, value);
    });
SearchFoldMain(Url, "PD_FOLDERS");       
}
//----------------------------------
function SearchFoldMain(Url, Type)
{
FormAddFold.loadStruct(Url+"?F="+CurrFold+"&Ty="+Type, function(){
FormAddFold.setFocusOnFirstActive();
});    
FormAddFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
//        NewFoldName=FormAddFold.getItemValue("Title");
        FormAddFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ShowFoldResults(response.substring(2));  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormAddFold.unload();
        WinAF.close();
        }
//    else if (name.substring(0,2)=="M_") 
//        ShowMulti(FormAddFold, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddFold, name.substring(2));    
    }
    );   
}
//------------------------------------------------------------
function ShowFoldResults(Result)
{
GridResults=TabBar.tabs("Results").attachGrid();
var ListPar=Result.split("\n");
GridResults.setHeader(ListPar[0]);
//ResGrid.setInitWidths("70,150,*");
GridResults.setColTypes(ListPar[1]);   
GridResults.setColSorting(ListPar[2]);
GridResults.init();
GridResults.parse(ListPar[3],"json");
TabBar.tabs("Reports").enable();
TabBar.tabs("Results").show(true);
//setTimeout( "TabBar.tabs('Results').show(true)" , 4000);
}
//------------------------------------------------------------
function FoldResProc(Order, SelFoldId)
{
if (Order=="Edit")  
    ModFoldExt(SelFoldId);
else if (Order=="Delete")  
    DelFold(SelFoldId);    
}
//------------------------------------------------------------
function AddDoc(CurrFold)
{
var Url="AddDoc";
WinAF=myWins.createWindow({
id:"AddDoc",
left:20,
top:30,
width:500,
height:300,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormAddDoc=WinAF.attachForm();
FormAddDoc.loadStruct(Url+"?F="+CurrFold, function(){
FormAddDoc.setFocusOnFirstActive();
});
FormAddDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormAddDoc.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            FormAddDoc.enableItem("UpFile");                            
                        else 
                            alert(response.substring(2)); 
                        } );
        }
     else 
        {   
        FormAddDoc.unload();
        WinAF.close();
        }
     }
             );
FormAddDoc.attachEvent("onUploadFile",function(realName,serverName)
    {
    FormAddDoc.unload();
    WinAF.close();
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
    });
FormAddDoc.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });
    
}
//----------------------------------
function DelDoc(Doc2Del)
{
var WinDD=myWins.createWindow({
    id:"DelDoc",
    left:20,
    top:30,
    width:500,
    height:420,
    center:true,
    modal:true,
    resize:false
});  
WinDD.setText("OpenProdoc");
var FormDelDoc=WinDD.attachForm();
FormDelDoc.loadStruct("DelDoc?D="+Doc2Del);
FormDelDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormDelDoc.send("DelDoc", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            FormDelDoc.unload();
                            WinDD.close();   
                            if (MantFromGrid)
                                GridResults.deleteRow(Doc2Del);
                            else
                                DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
                            }
                        } );
        }
     else 
        {   
        FormDelDoc.unload();
        WinDD.close();
        }
     }
             );
}
//------------------------------------------------------------
function ModDoc(Doc2Mod, ReadOnly, Vers)
{
var WinMD=myWins.createWindow({
    id:"ModDoc",
    left:20,
    top:30,
    width:500,
    height:420,
    center:true,
    modal:true,
    resize:false
});  
WinMD.setText("OpenProdoc");
var FormModDoc=WinMD.attachForm();
FormModDoc.loadStruct("ModDoc?D="+Doc2Mod+"&RO="+ReadOnly+"&Vers="+Vers, function(){
FormModDoc.setFocusOnFirstActive();
});
FormModDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormModDoc.send("ModDoc", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            FormModDoc.enableItem("UpFile");                            
                        else 
                            alert(response.substring(2)); 
                        } );
        }
     else if (name=="OK2")
        {    
        FormModDoc.send("ModDoc?EndMod=1", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            {
                            FormModDoc.unload();
                            WinMD.close();    
                            }                          
                        else 
                            alert(response.substring(2)); 
                        } );
        }
     else if (name.substring(0,2)=="M_") 
        ShowMulti(FormModDoc, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormModDoc, name.substring(2));  
    else if (name==CANCEL) 
        {   
        FormModDoc.unload();
        WinMD.close();
        }
     }
             );
FormModDoc.attachEvent("onUploadFile",function(realName,serverName)
    {
    FormModDoc.unload();
    WinMD.close();
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
    });
FormModDoc.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });
 }
//------------------------------------------------------------
function CheckOut (Doc2CheckOut)
{   
window.dhx4.ajax.get("CheckOut?D="+Doc2CheckOut, function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    if (nodes[0].textContent.substring(0,2)!=OK)
        {
        alert(nodes[0].textContent);    
        return;
        }
    else   
        {
        var Cell;
        if (MantFromGrid) 
            {
            for (i=0; i<GridResults.getColumnsNum(); i++)  
                if (GridResults.getColType(i)=="rotxt")
                    break;
            Cell=GridResults.cellById(Doc2CheckOut, i);  
            }
        else
            Cell=DocsGrid.cellById(Doc2CheckOut, 2);    
        Cell.setValue(nodes[0].textContent.substring(2)); 
        }
    });    
}
//------------------------------------------------------------
function CancelCheckOut (Doc2CancelCheck)
{
dhtmlx.confirm({text:"Desea cancelar CheckOut", callback: function(result)
    {
    if (result==true)    
        {
        window.dhx4.ajax.get("CancelCheckOut?D="+Doc2CancelCheck, function(r)
            {
            var xml = r.xmlDoc.responseXML;
            var nodes = xml.getElementsByTagName("status");
            if (nodes[0].textContent.substring(0,2)!=OK)
                {
                alert(nodes[0].textContent);    
                return;
                }
            else   
                {
                var Cell;
                if (MantFromGrid) 
                    {
                    for (i=0; i<GridResults.getColumnsNum(); i++)  
                        if (GridResults.getColType(i)=="rotxt")
                            break;
                    Cell=GridResults.cellById(Doc2CancelCheck, i);  
                    }
                else
                    Cell=DocsGrid.cellById(Doc2CancelCheck, 2); 
                Cell.setValue(""); 
                }
            });   
        }
    } }); 
}
//------------------------------------------------------------
function CheckIn (Doc2CheckIn)
{
var Url="CheckIn";
WinAF=myWins.createWindow({
id:"CheckInDoc",
left:20,
top:30,
width:400,
height:230,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormCheckIn=WinAF.attachForm();
FormCheckIn.loadStruct(Url+"?D="+Doc2CheckIn, function(){
FormCheckIn.setFocusOnFirstActive();
});
FormCheckIn.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormCheckIn.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else 
                            {
                            if (MantFromGrid) 
                                {
                                for (i=0; i<GridResults.getColumnsNum(); i++)  
                                    if (GridResults.getColType(i)=="rotxt")
                                        break;
                                var Cell=GridResults.cellById(Doc2CheckIn, i);
                                Cell.setValue(""); 
                                } 
                            else    
                                DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);    
                            FormCheckIn.unload();
                            WinAF.close();
                            }
                        } );
        }
     else 
        {   
        FormCheckIn.unload();
        WinAF.close();
        }
     }
             );
}
//------------------------------------------------------------
function AddExtDoc()
{
var Url="AddDocExt";
WinAF=myWins.createWindow({
id:"AddExtDoc",
left:20,
top:30,
width:500,
height:500,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
var LayoutFold=WinAF.attachLayout('2E');
var a = LayoutFold.cells('a');
a.hideHeader();
a.setHeight(50);
var formCombo = a.attachForm();
formCombo.loadStruct('DocCombo');
var b = LayoutFold.cells('b');
b.hideHeader();
FormAddFold = b.attachForm();  
formCombo.attachEvent("onChange", function(name, value, is_checked){
    FormAddFold.unload();
    FormAddFold = b.attachForm();
    CreaDocMain(Url, value);
    });
CreaDocMain(Url, "PD_DOCS");   
}
//----------------------------------
function CreaDocMain(Url, Type)
{
FormAddFold.loadStruct(Url+"?F="+CurrFold+"&Ty="+Type, function(){
FormAddFold.setFocusOnFirstActive();
});    
FormAddFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormAddFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            FormAddFold.enableItem("UpFile"); 
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormAddFold.unload();
        WinAF.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormAddFold, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddFold, name.substring(2));  
    }
    );   
FormAddFold.attachEvent("onUploadFile",function(realName,serverName)
    {
    FormAddFold.unload();
    WinAF.close();
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
    });
FormAddFold.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });
}
//----------------------------------
function SearchDoc()
{
var Url="SearchDoc";
WinAF=myWins.createWindow({
id:"SearchDoc",
left:20,
top:30,
width:650,
height:680,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
var LayoutFold=WinAF.attachLayout('2E');
var a = LayoutFold.cells('a');
a.hideHeader();
a.setHeight(50);
var formCombo = a.attachForm();
formCombo.loadStruct('DocCombo');
var b = LayoutFold.cells('b');
b.hideHeader();
TabBar=b.attachTabbar();
TabBar.addTab("Search", "Search", null, null, true);
TabBar.addTab("Results", "Results");
TabBar.addTab("Reports", "Reports");
GridReports=TabBar.tabs("Reports").attachGrid();
GridReports.setHeader("Title,Mime,Docs/page, Pages/arch");   //sets the headers of columns
GridReports.setColumnIds("Title,Mime,DocsPage, PagesArch");         //sets the columns' ids
GridReports.setInitWidths("*,60,60,60");   //sets the initial widths of columns
GridReports.setColAlign("left,left,left,left");     //sets the alignment of columns
GridReports.setColTypes("link,ro,ro,ro");               //sets the types of columns
GridReports.setColSorting("str,str,int,int");  //sets the sorting types of columns
GridReports.load("RepList?Type=Fold");
GridReports.init();
TabBar.tabs("Reports").disable();
ToolBar = TabBar.tabs("Results").attachToolbar();
ToolBar.addButton("Edit", 0, "Edit", "Edit.png", "Edit.png");
ToolBar.addButton("Delete", 1, "Delete", "Edit.png", "Edit.png");
ToolBar.addButton("CheckOut", 2, "CheckOut", "Edit.png", "Edit.png");
ToolBar.addButton("CheckIn", 3, "CheckIn", "Edit.png", "Edit.png");
ToolBar.addButton("CancelCheckOut", 4, "CancelCheckOut", "Edit.png", "Edit.png");
ToolBar.attachEvent("onClick", function(id)
    {
    if (GridResults.getSelectedRowId()!=null)    
        DocResProc(id, GridResults.getSelectedRowId());    
    });
FormAddFold = TabBar.tabs("Search").attachForm();
formCombo.attachEvent("onChange", function(name, value, is_checked){
    FormAddFold.unload();
    FormAddFold = TabBar.tabs("Search").attachForm();
    SearchDocMain(Url, value);
    });
SearchDocMain(Url, "PD_DOCS");       
}
//----------------------------------
function SearchDocMain(Url, Type)
{
FormAddFold.loadStruct(Url+"?F="+CurrFold+"&Ty="+Type, function(){
FormAddFold.setFocusOnFirstActive();
});    
FormAddFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        TabBar.tabs("Results").disable();  
        TabBar.tabs("Reports").disable();  
        FormAddFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ShowDocResults(response.substring(2));  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormAddFold.unload();
        WinAF.close();
        }    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddFold, name.substring(2));  
    }
    );   
}
//------------------------------------------------------------
function ShowDocResults(Result)
{
TabBar.tabs("Results").enable();  
TabBar.tabs("Reports").enable();
GridResults=TabBar.tabs("Results").attachGrid();
var ListPar=Result.split("\n");
GridResults.setHeader(ListPar[0]);
//ResGrid.setInitWidths("70,150,*");
GridResults.setColTypes(ListPar[1]);   
GridResults.setColSorting(ListPar[2]);
GridResults.init();
GridResults.parse(ListPar[3],"xml");
TabBar.tabs("Results").show(true);
//setTimeout( "TabBar.tabs('Results').show(true)" , 4000);
}
//------------------------------------------------------------
function DocResProc(Order, SelDocId)
{
MantFromGrid=true;
if (Order=="Edit")  
    ModDoc(SelDocId, false, "");
else if (Order=="Delete")   
    DelDoc(SelDocId);    
else if (Order=="CheckOut")   
    CheckOut(SelDocId);    
else if (Order=="CheckIn")   
    CheckIn(SelDocId);    
else if (Order=="CancelCheckOut")  
    CancelCheckOut(SelDocId);   
}
//------------------------------------------------------------
function ListVer(Doc2List)
{
WinAF=myWins.createWindow({
id:"ListVerDoc",
left:20,
top:30,
width:650,
height:500,
center:true,
modal:true,
resize:true});   
WinAF.setText("OpenProdoc");
var TB=WinAF.attachToolbar();
TB.addButton("Data", 0, "Data", "Data.png", "Data.png");
TB.addButton("View", 1, "View", "View.png", "View.png");
var GR=WinAF.attachGrid();
window.dhx4.ajax.get("ListVerDoc?Id="+Doc2List, function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("LV");
    GR.setHeader(nodes[0].textContent);
    GR.setColTypes(nodes[1].textContent);   
    GR.setColSorting(nodes[2].textContent);
    GR.init();
    var Data=nodes[3].textContent;
    GR.parse(Data, "json");
    });
TB.attachEvent("onClick", function(Order)
    {
    if (GR.getSelectedRowId()!=null)    
        {
        var PartsId = GR.getSelectedRowId().split('/');     
        if (Order=="Data")  
            {    
            ModDoc(PartsId[0], true, PartsId[1]);
            }
        else if (Order=="View")  
            {    
            window.open("SendDoc?Id="+PartsId[0]+"&Ver="+PartsId[1]);
            }  
        }
    });
}
//------------------------------------------------------------
function PassChange()
{
WinAF=myWins.createWindow({
id:"PassChange",
left:20,
top:30,
width:350,
height:230,
center:true,
modal:true,
resize:false});   
WinAF.setText("OpenProdoc"); 
var FormChangePass=WinAF.attachForm();
FormChangePass.loadStruct("PassChange", function(){
    FormChangePass.setFocusOnFirstActive();
    });
FormChangePass.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormChangePass.send("PassChange", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            FormChangePass.unload();
                            WinAF.close();  
                            }
                        } );
        }
     else 
        {   
        FormChangePass.unload();
        WinAF.close();
        }
     });
}
//------------------------------------------------------------
function TrashBin()
{
WinAF=myWins.createWindow({
id:"TrashBin",
left:20,
top:30,
width:600,
height:600,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
TmpLayout=WinAF.attachLayout('2E');
var a = TmpLayout.cells('a');
a.hideHeader();
a.setHeight(50);
var formCombo = a.attachForm();
formCombo.loadStruct('DocCombo');
var b = TmpLayout.cells('b');
b.hideHeader();
ToolBar = b.attachToolbar();
GridResults = b.attachGrid();
ToolBar.addButton("Undelete", 0, "Undelete", "Undelete.png", "Undelete.png");
ToolBar.addButton("Delete", 1, "Delete", "Edit.png", "Edit.png");
ToolBar.attachEvent("onClick", function(id)
    {
    if (GridResults.getSelectedRowId()!=null)    
        UndelPurge(id, GridResults.getSelectedRowId());    
    });
formCombo.attachEvent("onChange", function(name, value, is_checked){
    ShowTrashBin(value);
    });     
}
//------------------------------------------------------------
function UndelPurge(Order, DocId)
{
if (Order=="Undelete")  
    Undel(DocId);
else if (Order=="Delete")   
    Purge(DocId);        
}
//------------------------------------------------------------
function ShowTrashBin(DocType)
{
window.dhx4.ajax.get("TrashBin?DT="+DocType, function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("LV");
    GridResults.destructor();
    GridResults = TmpLayout.cells('b').attachGrid();
    GridResults.enableMultiselect(true);
    GridResults.setHeader(nodes[0].textContent);
    GridResults.setColTypes(nodes[1].textContent);   
    GridResults.setColSorting(nodes[2].textContent);
    GridResults.init();
    var Data=nodes[3].textContent;
    GridResults.parse(Data, "json");
    });    
}
//------------------------------------------------------------
