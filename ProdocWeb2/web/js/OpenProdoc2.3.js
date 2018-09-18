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
var FormSearchFold; 
var FormSearchDoc; 
var TabBar;
var GridResults;
var ToolBar;
var GridReports;
var MantFromGrid=false;
var TmpLayout;
var WinElem;
var WinListElem=null;
var FormElem;
var ELEMACL="ACL";
var ELEMMIME="MimeTypes";
var ELEMGROUPS="Groups";
var ELEMREPOS="Repositories";
var ELEMAUTH="Authenticators";
var ELEMOBJ="ObjDef";
var ELEMTASKCRON="TaskCron";
var ELEMTASKEVENT="TaskEvents";
var ELEMPENDTASK="PendTaskLog";
var ELEMTASKEND="EndTaskLogs";
var ELEMTRACELOGS="TraceLogs"
var ELEMCUST="Customizations";
var ELEMUSERS="Users";
var FiltExp="";
var ElemTabBar;
var EOPERNEW="New";
var EOPERMOD="Modif";
var EOPERDEL="Delete";
var EOPERCOP="Copy";
var EOPEREXP="Export";
var EOPEREXA="ExportAll";
var EOPERIMP="Import";
var EOPERRELAUN="Relaunch";
var EOPEREXPCSV="ExportCSV";
var PERMISSIONS=["NOTHING", "READ","CATALOG","VERSION","UPDATE","DELETE"];
var ElemLayout;
var ElemTabBar;
var GUsers;
var GGroups;
var GMetadata;
var GInherited;
var TBG;
var TBU;
var WinAttr;
var FormAttr;
var AT_TYPESTRING="String";
var AT_TYPETHES="Thesaur";
var COMBOFOLDER="ObjType";       
var COMBODOCS="ObjType2";  
var CSVFORMAT="CSV";
var T_EDIT;
var T_DEL;
var CpDoc="";
var CpFold="";
var CpParentFold="";
var CurrTitle="";
var CurrFoldTitle="";
var BUTFT="FT";
       
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
                window.location.reload(true);    
            else
                alert("["+response+"]");
            });
        }
    });
		   
}
//--------------------------------------------------------------
function Init()
{   
if (window.dhx.isIE) 
    {
    alert("Unsupported browser / navegador no soportado");
    }    
document.title="OpenProdoc2 "+LocaleTrans("_User");
T_EDIT=LocaleTrans("Update");
T_DEL=LocaleTrans("Delete");
}
//--------------------------------------------------------------
function doOnLoadMain() 
{                   
Init();    
layout = new dhtmlXLayoutObject(document.body,"3L");         
layout.cells("a").setText(LocaleTrans("Folders_Tree")); 
layout.cells("a").setWidth(300);
layout.cells("b").setText(LocaleTrans("Current_Folder"));   
layout.cells("c").setText(LocaleTrans("Folder_Documents"));   
menu = layout.attachMenu();
menu.loadStruct("Menu", function(){});
menu.attachEvent("onClick", function(id, zoneId, cas)
    {
    ExecMenu(id);    
    });
DocsGrid = layout.cells("c").attachGrid();
DocsGrid.setHeader(LocaleTrans("Document_Type")+","+LocaleTrans("Document_Title")+","+LocaleTrans("Document_Date")+","+LocaleTrans("Lock_user")+","+LocaleTrans("Date"));   //sets the headers of columns
DocsGrid.setColumnIds("Type,Title,Date,Lock,RepDate");         
DocsGrid.setInitWidths("120,460,120,80,*");   
DocsGrid.setColAlign("left,left,left,left,left");    
DocsGrid.setColTypes("ro,link,ro,ro,ro");            
DocsGrid.setColSorting("str,str,str,str,str"); 
DocsGrid.load("DocList?FoldId="+ROOTFOLD);
DocsGrid.init();
DocsGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrDoc=rowId;    
    CurrTitle=DocsGrid.cells(rowId,1).getValue();
    var n = CurrTitle.indexOf("^");
    CurrTitle=CurrTitle.substr(0, n);
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
    CurrFoldTitle=DocsTree.getItemText(id);
    layout.cells("b").attachURL("SFoldRec", true, {FoldId: CurrFold});
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold, function ()
        {
        if (DocsGrid.getRowsNum()>0)
            DocsGrid.selectRow(0, true, false, true);
        });
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
    case "ExportFold": ExportFold(CurrFold);   
        Break;
    case "ImportFold": ImportFold(CurrFold); 
        Break;
    case "CopyFold": if (CurrFold!="") 
                        CopyF(CurrFold);
        break;
    case "PasteFold": if (CpFold!="") 
                        PasteF(CpFold);
        break;
    case "AddDoc": AddDoc(CurrFold);
        break;
    case "AddExtDoc": AddExtDoc(CurrFold);
        break;
    case "ModExtDoc" : if (CurrDoc!="") 
                        ModDoc(CurrDoc, false, "");
        break;
    case "ChangeACL" : if (CurrDoc!="") 
                        ModACLDoc(CurrDoc);
        break;   
    case "DocMetadata": if (CurrDoc!="") 
                        ModDoc(CurrDoc, true, "");
        break;
    case "DelDoc": if (CurrDoc!="") 
                        DelDoc(CurrDoc);
        break;
    case "CheckOut": if (CurrDoc!="") 
                        CheckOut(CurrDoc);
        break;
    case "CheckIn": if (CurrDoc!="") 
                        CheckIn(CurrDoc);
        break;
    case "CancelCheckOut": if (CurrDoc!="") 
                        CancelCheckOut(CurrDoc);
        break;
    case "SearchDoc": SearchDoc();
        break;
    case "ImportRIS": ImportRIS(CurrFold);
        break;
    case "ExportDoc": if (CurrDoc!="") 
                        ExportDoc(CurrDoc);   
        break;            
    case "ImportDoc": ImportDoc(CurrFold);   
        break;            
    case "ListVer": if (CurrDoc!="") 
                        ListVer(CurrDoc);
        break;
    case "CopyDoc": if (CurrDoc!="") 
                        Copy(CurrDoc);
        break;
    case "PasteDoc": if (CpDoc!="") 
                        Paste(CpDoc);
        break;
    case "PasswordChange": PassChange();
        break;
    case "TrashBin": TrashBin();
        break;
    case "Thesaurus": OpenThes();
        break;
    case "ACL": Admin(ELEMACL);
        break;
    case "Groups": Admin(ELEMGROUPS);
        break;
    case "Users": Admin(ELEMUSERS);
        break;
    case "Roles": Admin("Roles");
        break;
    case "MimeTypes": Admin(ELEMMIME);
        break;
    case "Repositories": Admin(ELEMREPOS);
        break;
    case "ObjDef": Admin(ELEMOBJ);
        break;
    case "Authenticators": Admin(ELEMAUTH);
        break;
    case "Customizations": Admin(ELEMCUST);
        break;
    case "TaskCron": Admin(ELEMTASKCRON);
        break;
    case "TaskEvents": Admin(ELEMTASKEVENT);
        break;
    case "PendTasklog": Admin(ELEMPENDTASK);
        break;
    case "EndTasksLogs": Admin(ELEMTASKEND);
        break;
    case "TraceLogs": Admin(ELEMTRACELOGS);
        break;
    case "ReportingBugs": window.open("https://docs.google.com/spreadsheet/viewform?usp=drive_web&formkey=dEpsRzZzSmlaQVZET0g2NDdsM0ZRaEE6MA#gid=0");
        break;
    case "Contents": window.open(LocaleTrans("_Help"));
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
    }
}
//----------------------------------
function doOnLoadThes() 
{            
Init();    
layoutThes = new dhtmlXLayoutObject(document.body,"2U");         
layoutThes.cells("a").setText("Thesaurus"); 
layoutThes.cells("a").setWidth(400);
layoutThes.cells("b").setText(LocaleTrans("Current_Term"));    
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
function ExportDoc(CurrDoc)
{
window.open("SendDocXML?Id="+CurrDoc);    
}
//-----------------------------------
function ExportFold(CurrFold)
{
window.open("SendFoldXML?Id="+CurrFold);    
}
//-----------------------------------
function ImportDoc(CurrFold)
{
//var Url="ImpElem";
WinAF=myWins.createWindow({
id:"AddDoc",
left:20,
top:1,
width:450,
height:220,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormImpElem=WinAF.attachForm();
FormImpElem.loadStruct("ImpDoc?F="+CurrFold, function(){
    FormImpElem.setFocusOnFirstActive();
    });
FormImpElem.attachEvent("onButtonClick", function (name)
    {if (name==CANCEL || name==OK)
        {   
        FormImpElem.unload();
        WinAF.close();
        }
     });
FormImpElem.attachEvent("onUploadFile",function(realName,serverName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    WinAF.close();
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
    });
    });
FormImpElem.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });  
}
//-----------------------------------
function ImportFold(CurrFold)
{
//var Url="ImpElem";
WinAF=myWins.createWindow({
id:"AddDoc",
left:20,
top:1,
width:450,
height:220,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormImpElem=WinAF.attachForm();
FormImpElem.loadStruct("ImpDoc?F="+CurrFold, function(){
    FormImpElem.setFocusOnFirstActive();
    });
FormImpElem.attachEvent("onButtonClick", function (name)
    {if (name==CANCEL || name==OK)
        {   
        FormImpElem.unload();
        WinAF.close();
        }
     });
FormImpElem.attachEvent("onUploadFile",function(realName,serverName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    WinAF.close();
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
    });
    });
FormImpElem.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });  
}
//-----------------------------------
function AddThes()
{
var Url="AddThes";
WinAF=myWins.createWindow({
id:"AddThes",
left:20,
top:1,
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
FormAddThes.enableLiveValidation(true);     
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
    });
}
//-----------------------------------
function MantTerm(Url, idCurrTerm)
{
if (idCurrTerm==ROOTTHES || idCurrTerm==null || idCurrTerm=="") 
    return;
WinMT=myWins.createWindow({
id:"mantTerm",
left:20,
top:1,
width:600,
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
    RTTB.addButton("Add", 0, LocaleTrans("Add"), "img/add.png", "img/add.png");
    RTTB.addButton("Del", 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    RTTB.attachEvent("onClick", function(id)
        {
        CurrThes=FormAddTerm.getUserData("TES_USE", "ThesId");          
        TermManRel("RT", id, RTGrid);    
        });
    }
var RTGrid=TermTabBar.tabs("RT").attachGrid();
PrepTermGrid(RTGrid);
RTGrid.init();
TermTabBar.addTab("Lang", LocaleTrans("Language"));
if (Url!="DelTerm")
    {
    var LangTB=TermTabBar.tabs("Lang").attachToolbar();
    LangTB.addButton("Add", 0, LocaleTrans("Add"), "img/add.png", "img/add.png");
    LangTB.addButton("Del", 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
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
FormAddTerm.enableLiveValidation(true);    
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
//                                ThesTree.insertNewChild(idCurrTerm, NewThesId, NewThesName);
                                if (ThesTree.getOpenState(idCurrTerm))
                                    ThesTree.insertNewChild(idCurrTerm, NewThesId, NewThesName);
                                else 
                                    ThesTree.refreshItem(idCurrTerm);
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
     else if (name.substring(0,3)=="TD_") 
        DelTerm(FormAddTerm, name.substring(3)); 
     else 
        {   
        FormAddTerm.unload();
        WinMT.close();
        }
     });
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
TermGrid.setHeader(LocaleTrans("Term_Name")+","+LocaleTrans("Description")+","+LocaleTrans("USE")+","+LocaleTrans("Scope_Note")+","+LocaleTrans("Language"));   
TermGrid.setColumnIds("Name,Def,Use,Note,Lang"); 
TermGrid.setInitWidths("100,100,60,60,*");  
TermGrid.setColAlign("left,left,left,left,left");  
TermGrid.setColTypes("ro,ro,ro,ro,ro");      
TermGrid.setColSorting("str,str,str,str,str");  
}
//-----------------------------------
function UpdThes()
{
var Url="ModThes";
WinAF=myWins.createWindow({
id:"UpdThes",
left:20,
top:1,
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
FormAddThes.enableLiveValidation(true); 
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
     });
}
//-----------------------------------
function DelThes()
{
var Url="DelThes";
WinAF=myWins.createWindow({
id:"DelThes",
left:20,
top:1,
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
FormAddThes.enableLiveValidation(true);     
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
    });
    
}
//-----------------------------------
function SearchTerm()
{
var Url="SearchTerm";
WinAF=myWins.createWindow({
id:"SearchTerm",
left:20,
top:1,
width:600,
height:500,
center:true,
modal:true,
resize:true}); 
WinAF.setText("OpenProdoc");
TabBar=WinAF.attachTabbar();
TabBar.addTab("Search", LocaleTrans("Search"), null, null, true);
TabBar.addTab("Results", LocaleTrans("Search_Results"));
ToolBar = TabBar.tabs("Results").attachToolbar();
ToolBar.addButton(T_EDIT, 0, LocaleTrans("Edit"), "img/edit.png", "img/edit.png");
ToolBar.addButton(T_DEL, 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
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
    });           
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
TabBar.tabs("Results").setActive();  
}
//-----------------------------------
function TermResProc(Order, idTerm)
{
MantFromGrid=true;    
if (Order==T_EDIT)  
    { 
    MantTerm("UpdTerm", idTerm);
    }
else if (Order==T_DEL)  
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
top:1,
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
    });    
}
//-----------------------------------
function ImportThes()
{   
WinAF=myWins.createWindow({
id:"ImportThes",
left:20,
top:1,
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
    });    
FormImpThes.attachEvent("onUploadFile",function(realName,serverName)
    {
    ThesTree.refreshItem(ROOTTHES);
    FormImpThes.unload();
    WinAF.attachHTMLString(serverName);
    });
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
    top:1,
    width:540,
    height:420,
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
top:1,
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
FormAddFold.enableLiveValidation(true);     
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
                            if (DocsTree.getOpenState(CurrFold))
                                DocsTree.insertNewChild(CurrFold, NewFoldId, NewFoldName);
                            else 
                                DocsTree.refreshItem(CurrFold);
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
top:1,
width:750,
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
FormAddFold.enableLiveValidation(true);     
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
                            if (DocsTree.getOpenState(CurrFold))
                                DocsTree.insertNewChild(CurrFold, NewFoldId, NewFoldName);
                            else 
                                DocsTree.refreshItem(CurrFold);
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
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormAddFold, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormAddFold, name.substring(3)); 
    });   
}
//------------------------------------------------------------
function ModFoldExt(EditFold)
{
var Url="ModFoldExt";
WinAF=myWins.createWindow({
id:"ModFold",
left:20,
top:1,
width:600,
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
FormAddFold.enableLiveValidation(true); 
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
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormAddFold, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormAddFold, name.substring(3)); 
    else 
        ShowMulti(FormAddFold, name.substring(2));    
    });   
}
//----------------------------------
function ShowMultiThes(Form, AttName)
{
var WinMulti=myWins.createWindow({
id:"MultiThes",
left:20,
top:1,
width:300,
height:260,
center:true,
modal:true,
resize:true});   
WinMulti.setText("OpenProdoc");  
var MultiFormData = [
				{type: "settings", position: "label-left", labelWidth: 100, inputWidth: 250},
                {type: "block", width: 250, list:[
                    {type: "button", name: "add", value: "+"},
                    {type: "newcolumn", offset:20 },
                    {type: "button", name: "del", value: "-"}
                    ]},
                {type: "multiselect", name: "Vals", size:6},
                {type: "block", width: 250, list:[
                    {type: "button", name: "OK", value: "Ok"},
                    {type: "newcolumn", offset:20 },
                    {type: "button", name: "CANCEL", value: "Cancel"}]
			        }];
var MultiForm=WinMulti.attachForm(MultiFormData);
var Vals=MultiForm.getSelect("Vals");
if (Form.getItemValue(AttName).trim().length!=0)
    {
    var CurNames=Form.getItemValue(AttName).split("|"); 
    var CurVals=Form.getItemValue("TH_"+AttName).split("|"); 
    for (var i=0; i<CurVals.length; i++)
        Vals.options.add(new Option(CurNames[i], CurVals[i]));
    }
MultiForm.setItemFocus("Val");    
MultiForm.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {
        var SumId="";
        var SumText="";
        for (var i=0; i<Vals.options.length; i++)
            {
            if (i!=0)    
                {
                SumId+="|";
                SumText+="|";
                }
            SumId+=Vals.options[i].value;
            SumText+=Vals.options[i].text;
            }
        Form.setItemValue("TH_"+AttName, SumId);    
        Form.setItemValue(AttName, SumText);    
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
        SelectThesMult(Form.getUserData(AttName, "ThesId"), MultiForm)
        }
    else if (name=='del') 
        { 
        var ListO=Vals.options;  
        ListO.remove(ListO.selectedIndex);
        }      
    });        
}
//----------------------------------
function SelectThesMult(ThesId, Form)
{   
var TermId="";    
var WinSelThes=myWins.createWindow({
id:"SelThes",
left:20,
top:1,
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
STToolBar.addButton("Select", 0, LocaleTrans("Selection"), "img/select.png", "img/select.png");
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
    var Text=SelThesTree.getSelectedItemText();
    var Vals=Form.getSelect("Vals");
    var ListO=Vals.options;    
    ListO.add(new Option(Text, TermId));
    Vals.options.selectedIndex=ListO.length-1;
    WinSelThes.close();
    WinSelThes.unload();
    });
}
//----------------------------------
function ShowMulti(Form, AttName)
{
var WinMulti=myWins.createWindow({
id:"Multi",
left:20,
top:1,
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
    });
}
//----------------------------------
function DelTerm(Form, AttName)
{ 
Form.setItemValue("TH_"+AttName, ""); 
Form.setItemValue(AttName, "");
}
//----------------------------------
function ShowThes(Form, AttName)
{
var ThesId=Form.getUserData(AttName, "ThesId");   
var TermId=Form.getItemValue("TH_"+AttName);   
var WinSelThes=myWins.createWindow({
id:"SelThes",
left:20,
top:1,
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
STToolBar.addButton("Select", 0, LocaleTrans("Selection"), "img/select.png", "img/select.png");
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
var WinSelTerm=myWins.createWindow({
id:"SelThes",
left:20,
top:1,
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
STToolBar.addButton("Select", 0, LocaleTrans("Selection"), "img/select.png", "img/select.png");
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
    top:1,
    width:750,
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
                        });
        }
     else 
        {   
        FormDelFold.unload();
        WinDF.close();
        }
    });
}
//----------------------------------
function UpdFold(EditFold)
{
var WinUF=myWins.createWindow({
    id:"UpdFold",
    left:20,
    top:1,
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
                        });
        }
     else 
        {   
        FormUpdFold.unload();
        WinUF.close();
        }
    });
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
left:100,
top:1,
width:750,
height:650,
center:false,
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
TabBar.addTab("Search", LocaleTrans("Search_Folders"), null, null, true);
TabBar.addTab("Results", LocaleTrans("Search_Results"));
TabBar.addTab("Reports", LocaleTrans("Reports_Generation"));
GridReports=TabBar.tabs("Reports").attachGrid();
GridReports.setHeader(LocaleTrans("Report_Title")+","+LocaleTrans("MimeType")+","+LocaleTrans("Docs_per_Page")+","+LocaleTrans("Pages_per_File"));   //sets the headers of columns
GridReports.enableAutoWidth(true);
GridReports.setColumnIds("Title,Mime,DocsPage,PagesArch");
GridReports.setInitWidths("300,100,100, *"); 
GridReports.setColAlign("left,left,left,left"); 
GridReports.setColTypes("link,ro,ro,ro"); 
GridReports.setColSorting("str,str,int,int");
GridReports.load("RepList?Type=Fold");
GridReports.init();
TabBar.tabs("Reports").disable();
FormSearchFold = TabBar.tabs("Search").attachForm();
ToolBar = TabBar.tabs("Results").attachToolbar();
ToolBar.addButton(T_EDIT, 0, LocaleTrans("Edit"), "img/edit.png", "img/edit.png");
ToolBar.addButton(T_DEL, 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
ToolBar.addButton(CSVFORMAT, 2, "CSV", "img/expCSV.png", "img/expCSV.png");
ToolBar.attachEvent("onClick", function(id)
    {
    if (id==CSVFORMAT && GridResults.getRowsNum()>0)  
        ExporGenCSV();
    else if (GridResults.getSelectedRowId()!=null)    
        FoldResProc(id, GridResults.getSelectedRowId());    
    });
formCombo.attachEvent("onChange", function(name, value, is_checked){
    FormSearchFold.unload();
    FormSearchFold = TabBar.tabs("Search").attachForm();
    SearchFoldMain(Url, value);
    });
SearchFoldMain(Url, "PD_FOLDERS");       
}
//----------------------------------
function SearchFoldMain(Url, Type)
{
FormSearchFold.loadStruct(Url+"?F="+CurrFold+"&Ty="+Type, function(){
    FormAddFold.setFocusOnFirstActive();
    });    
FormSearchFold.enableLiveValidation(true);     
FormSearchFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormSearchFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ShowFoldResults(response.substring(2));  
                            }
                        });
        }
     else if (name==CANCEL) 
        {   
        FormSearchFold.unload();
        WinAF.close();
        }   
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormSearchFold, name.substring(2));    
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormSearchFold, name.substring(3)); 
    });   
}
//------------------------------------------------------------
function ShowFoldResults(Result)
{
GridResults=TabBar.tabs("Results").attachGrid();
var ListPar=Result.split("\n");
GridResults.setHeader(ListPar[0]);
GridResults.setColTypes(ListPar[1]);   
GridResults.setColSorting(ListPar[2]);
GridResults.init();
GridResults.parse(ListPar[3],"json");
TabBar.tabs("Reports").enable();
TabBar.tabs("Results").show(true);
TabBar.tabs("Results").setActive();
}
//------------------------------------------------------------
function FoldResProc(Order, SelFoldId)
{
if (Order==T_EDIT)  
    ModFoldExt(SelFoldId);
else if (Order==T_DEL)  
    DelFold(SelFoldId);    
}
//------------------------------------------------------------
function AddDoc(CurrFold)
{
var Url="AddDoc";
WinAF=myWins.createWindow({
id:"AddDoc",
left:20,
top:1,
width:550,
height:300,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormAddDoc=WinAF.attachForm();
FormAddDoc.loadStruct(Url+"?F="+CurrFold, function(){
    FormAddDoc.setFocusOnFirstActive();
    });
FormAddDoc.enableLiveValidation(true);      
FormAddDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormAddDoc.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            FormAddDoc.enableItem("UpFile");                            
                        else 
                            alert(response.substring(2)); 
                        });
        }
     else 
        {   
        FormAddDoc.unload();
        WinAF.close();
        }
    });
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
    top:1,
    width:600,
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
                        });
        }
     else 
        {   
        FormDelDoc.unload();
        WinDD.close();
        }
    });
}
//------------------------------------------------------------
function ModDoc(Doc2Mod, ReadOnly, Vers)
{
var WinMD=myWins.createWindow({
    id:"ModDoc",
    left:20,
    top:1,
    width:600,
    height:420,
    center:true,
    modal:true,
    resize:true
});  
WinMD.setText("OpenProdoc");
var FormModDoc=WinMD.attachForm();
FormModDoc.loadStruct("ModDoc?D="+Doc2Mod+"&RO="+ReadOnly+"&Vers="+Vers, function(){
    FormModDoc.setFocusOnFirstActive();
    });
FormModDoc.enableLiveValidation(true);     
FormModDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormModDoc.send("ModDoc", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)  
                            {
                            if (!FormModDoc.isItem("UpFile")) 
                                {
                                FormModDoc.unload();
                                WinMD.close();
                                }
                            else  
                                FormModDoc.enableItem("UpFile");   
                            }
                        else 
                            alert(response.substring(2)); 
                        });
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
                        });
        }
     else if (name.substring(0,2)=="M_") 
        ShowMulti(FormModDoc, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormModDoc, name.substring(2));  
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormModDoc, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormModDoc, name.substring(3)); 
    else if (name==CANCEL) 
        {   
        FormModDoc.unload();
        WinMD.close();
        }
    });
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
            Cell=DocsGrid.cellById(Doc2CheckOut, 3);    
        Cell.setValue(nodes[0].textContent.substring(2)); 
        }
    });    
}
//------------------------------------------------------------
function CancelCheckOut (Doc2CancelCheck)
{
dhtmlx.confirm({text:LocaleTrans("Do_you_want_to_cancel_edition_and_lost_changes"), callback: function(result)
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
                    Cell=DocsGrid.cellById(Doc2CancelCheck, 3); 
                Cell.setValue(""); 
                }
            });   
        }
    }}); 
}
//------------------------------------------------------------
function CheckIn (Doc2CheckIn)
{
var Url="CheckIn";
WinAF=myWins.createWindow({
id:"CheckInDoc",
left:20,
top:1,
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
                        });
        }
     else 
        {   
        FormCheckIn.unload();
        WinAF.close();
        }
    });
}
//------------------------------------------------------------
function AddExtDoc()
{
var Url="AddDocExt";
WinAF=myWins.createWindow({
id:"AddExtDoc",
left:20,
top:1,
width:750,
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
FormAddFold.enableLiveValidation(true);    
FormAddFold.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormAddFold.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            if (!FormAddFold.isItem("UpFile")) 
                                {
                                FormAddFold.unload();
                                WinAF.close();
                                }
                            else    
                                FormAddFold.enableItem("UpFile"); 
                            }
                        });
        }
     else if (name==CANCEL) 
        {   
        FormAddFold.unload();
        WinAF.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormAddFold, name.substring(2));    
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormAddFold, name.substring(3));  
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddFold, name.substring(2));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormAddFold, name.substring(3)); 
    });   
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
left:100,
top:1,
width:750,
height:680,
center:false,
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
TabBar.addTab("Search", LocaleTrans("Search_Documents"), null, null, true);
TabBar.addTab("Results", LocaleTrans("Search_Results"));
TabBar.addTab("Reports", LocaleTrans("Reports_Generation"));
GridReports=TabBar.tabs("Reports").attachGrid();
GridReports.setHeader(LocaleTrans("Report_Title")+","+LocaleTrans("MimeType")+","+LocaleTrans("Docs_per_Page")+","+LocaleTrans("Pages_per_File"));   //sets the headers of columns
GridReports.setColumnIds("Title,Mime,DocsPage,PagesArch");   
GridReports.setInitWidths("300,100,100,*"); 
GridReports.setColAlign("left,left,left,left");   
GridReports.setColTypes("link,ro,ro,ro");  
GridReports.setColSorting("str,str,int,int"); 
GridReports.load("RepList?Type=Fold");
GridReports.init();
TabBar.tabs("Reports").disable();
ToolBar = TabBar.tabs("Results").attachToolbar();
ToolBar.addButton(T_EDIT, 0, LocaleTrans("Edit"), "img/edit.png", "img/edit.png");
ToolBar.addButton(T_DEL, 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
ToolBar.addButton("CheckOut", 2, "CheckOut", "img/checkout.png", "img/checkout.png");
ToolBar.addButton("CheckIn", 3, "CheckIn", "img/checkin.png", "img/checkin.png");
ToolBar.addButton("CancelCheckOut", 4, "CancelCheckOut", "img/cancelcheckout.png", "img/cancelcheckout.png");
ToolBar.addButton(CSVFORMAT, 5, "CSV", "img/expCSV.png", "img/expCSV.png");
ToolBar.attachEvent("onClick", function(id)
    {
    if (id==CSVFORMAT && GridResults.getRowsNum()>0)  
        ExporGenCSV();
    else if (GridResults.getSelectedRowId()!=null)    
        DocResProc(id, GridResults.getSelectedRowId());    
    });
FormSearchDoc = TabBar.tabs("Search").attachForm();
formCombo.attachEvent("onChange", function(name, value, is_checked){
    FormSearchDoc.unload();
    FormSearchDoc = TabBar.tabs("Search").attachForm();
    SearchDocMain(Url, value);
    });
SearchDocMain(Url, "PD_DOCS");       
}
//----------------------------------
function SearchDocMain(Url, Type)
{
FormSearchDoc.loadStruct(Url+"?F="+CurrFold+"&Ty="+Type, function(){
    FormSearchDoc.setFocusOnFirstActive();
    });       
FormSearchDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        TabBar.tabs("Results").disable();  
        TabBar.tabs("Reports").disable();  
        FormSearchDoc.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            ShowDocResults(response.substring(2));  
                            }
                        });
        }
     else if (name==CANCEL) 
        {   
        FormSearchDoc.unload();
        WinAF.close();
        }    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormSearchDoc, name.substring(2));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormSearchDoc, name.substring(3)); 
    });   
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
TabBar.tabs("Results").setActive();
}
//------------------------------------------------------------
function DocResProc(Order, SelDocId)
{
MantFromGrid=true;
if (Order==T_EDIT)  
    ModDoc(SelDocId, false, "");
else if (Order==T_DEL)   
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
top:1,
width:650,
height:300,
center:true,
modal:true,
resize:true});   
WinAF.setText("OpenProdoc");
var TB=WinAF.attachToolbar();
TB.addButton("Data", 0, LocaleTrans("Document_Attributes"), "img/data.png", "img/data.png");
var GR=WinAF.attachGrid();
GR.load("ListVerDoc?Id="+Doc2List);
GR.setSizes();
TB.attachEvent("onClick", function(Order)
    {
    if (GR.getSelectedRowId()!=null)    
        {
        var PartsId = GR.getSelectedRowId().split('|');     
        if (Order=="Data")  
            ModDoc(PartsId[0], true, PartsId[1]);
        }
    });
}
//------------------------------------------------------------
function PassChange()
{
WinAF=myWins.createWindow({
id:"PassChange",
left:20,
top:1,
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
                        });
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
top:1,
width:600,
height:500,
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
GridResults.enableMultiselect(true);
ToolBar.addButton("Undelete", 0, "Undelete", "img/undel.png", "img/undel.png");
ToolBar.addButton(T_DEL, 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
ToolBar.attachEvent("onClick", function(id)
    {
    if (GridResults.getSelectedRowId()!=null)    
        UndelPurge(id, GridResults.getSelectedRowId());    
    });
formCombo.attachEvent("onChange", function(name, DocType, is_checked){
    GridResults.load("TrashBin?DT="+DocType);
    });     
}
//------------------------------------------------------------
function UndelPurge(Order, DocId)
{
if (Order=="Undelete")  
    Undel(DocId);
else if (Order==T_DEL)   
    Purge(DocId);        
}
//------------------------------------------------------------
function Undel(DocId)
{
var ListId=DocId.split(",");  
for (var i=0; i<ListId.length; i++)
    {   
    window.dhx4.ajax.get("Undel?Id="+ListId[i], function(r)
        {
        var xml = r.xmlDoc.responseXML;
        var nodes = xml.getElementsByTagName("status");
        if (nodes[0].textContent.substring(0,2)!=OK)
            alert(nodes[0].textContent);
        else
            GridResults.deleteRow(nodes[0].textContent.substring(2));
        });
    }    
}
//------------------------------------------------------------
function Purge(DocId)
{
var ListId=DocId.split(",");  
for (var i=0; i<ListId.length; i++)
    {
    window.dhx4.ajax.get("Purge?Id="+ListId[i], function(r)
        {
        var xml = r.xmlDoc.responseXML;
        var nodes = xml.getElementsByTagName("status");
        if (nodes[0].textContent.substring(0,2)!=OK)
            alert(nodes[0].textContent);
        else
            GridResults.deleteRow(nodes[0].textContent.substring(2));
        });
    }     
}
//------------------------------------------------------------
function Admin(TypeElem)
{
WinListElem=myWins.createWindow({
id:"Admin",
left:100,
top:1,
width:((TypeElem==ELEMOBJ||TypeElem==ELEMTASKCRON||TypeElem==ELEMTASKEVENT||TypeElem==ELEMPENDTASK||TypeElem==ELEMTASKEND)?1000:700),
height:600,
center:false,
modal:true,
resize:true}); 
WinListElem.setText(TypeElem);
TmpLayout=WinListElem.attachLayout('2E');
var a = TmpLayout.cells('a');
a.hideHeader();
if (TypeElem==ELEMPENDTASK || TypeElem==ELEMTASKEND || TypeElem==ELEMTRACELOGS)
    a.setHeight(110);
else
    a.setHeight(50);
var formFilter = a.attachForm();
formFilter.loadStruct("ElemFilter?TE="+TypeElem);
var b = TmpLayout.cells('b');
b.hideHeader();
ToolBar = b.attachToolbar();
GridResults = b.attachGrid();
GridResults.load("ListElem?TE="+TypeElem);
GridResults.setSizes();
FiltExp="";
if (TypeElem==ELEMTASKEND)
   GridResults.enableMultiselect(true);
formFilter.attachEvent("onButtonClick",  function(name)
    {
    RefreshAdminGrid(TypeElem, formFilter, "");    
    });        
if (TypeElem!=ELEMPENDTASK && TypeElem!=ELEMTASKEND && TypeElem!=ELEMTRACELOGS)
    {
    ToolBar.addButton(EOPERNEW, 0, LocaleTrans("New"), "img/add.png", "img/add.png");
    ToolBar.addButton(EOPERMOD, 1, LocaleTrans("Edit"), "img/edit.png", "img/edit.png");
    ToolBar.addButton(EOPERDEL, 2, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    ToolBar.addButton(EOPERCOP, 3, LocaleTrans("Copy"), "img/copy.png", "img/copy.png");
    ToolBar.addButton(EOPEREXP, 4, LocaleTrans("Export"), "img/export.png", "img/export.png");
    ToolBar.addButton(EOPEREXA, 5, LocaleTrans("Export_All"), "img/export_all.png", "img/export_all.png");
    ToolBar.addButton(EOPERIMP, 6, LocaleTrans("Import"), "img/import.png", "img/import.png");
    ToolBar.addButton(EOPEREXPCSV, 7, "CSV", "img/expCSV.png", "img/expCSV.png");
    }
else if ( TypeElem==ELEMTASKEND)
    {
    ToolBar.addButton(EOPERRELAUN, 0, LocaleTrans("Relaunch"), "img/relaunch.png", "img/relaunch.png"); 
    ToolBar.addButton(EOPERDEL, 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    ToolBar.addButton(EOPEREXPCSV, 2, "CSV", "img/expCSV.png", "img/expCSV.png");
    }    
else if ( TypeElem==ELEMTRACELOGS)
    {
    ToolBar.addButton(EOPERDEL, 0, LocaleTrans("Delete"), "img/del.png", "img/del.png");    
    ToolBar.addButton(EOPEREXPCSV, 1, "CSV", "img/expCSV.png", "img/expCSV.png");    
    }
else
    ToolBar.addButton(EOPEREXPCSV, 0, "CSV", "img/expCSV.png", "img/expCSV.png");    
GridResults.attachEvent("onRowDblClicked", function(rId,cInd){
    GridResults.selectRow(rId);
    ElemMod(TypeElem, GridResults.getSelectedRowId());
});
ToolBar.attachEvent("onClick", function(Oper)
    {  
    if (Oper==EOPEREXPCSV)    
        ExportCSV(TypeElem, formFilter);
    else if (GridResults.getSelectedRowId()!=null && Oper==EOPERRELAUN)
        window.dhx4.ajax.get("Relaunch?Id="+GridResults.getSelectedRowId(), function(r)
            {
            var xml = r.xmlDoc.responseXML;
            var nodes = xml.getElementsByTagName("status");
            alert(nodes[0].textContent);                                       
            });  
    else if (GridResults.getSelectedRowId()!=null || Oper==EOPERNEW || Oper==EOPEREXA || Oper==EOPERIMP 
            || Oper== EOPERDEL && TypeElem==ELEMTRACELOGS )     
        {
        if (Oper==EOPERNEW)  
            {
            if (TypeElem!=ELEMOBJ)    
                ElemNew(TypeElem);
            else{
                var KK=GridResults.getSelectedRowId(); 
                if (KK!=null)
                   ElemNewObj(TypeElem, GridResults.getSelectedRowId());
                }
            }
        else if (Oper==EOPERMOD)
            ElemMod(TypeElem, GridResults.getSelectedRowId());
        else if (Oper==EOPERDEL)
            {
            if (TypeElem==ELEMTASKEND)    
                ElemDelTaskEnd(TypeElem, GridResults.getSelectedRowId());
            else if (TypeElem==ELEMTRACELOGS)    
                ElemDelLogs(TypeElem, formFilter);
            else
                ElemDel(TypeElem, GridResults.getSelectedRowId());
            }
        else if (Oper==EOPERCOP)
            ElemCopy(TypeElem, GridResults.getSelectedRowId());
        else if (Oper==EOPEREXP)
            ElemExport(TypeElem, GridResults.getSelectedRowId());
        else if (Oper==EOPEREXA)
            ElemExpAll(TypeElem, FiltExp);
        else if (Oper==EOPERIMP)
            ElemImp(TypeElem);
        }
    });
}
//------------------------------------------------------------
function RefreshAdminGrid(TypeElem, formFilter, Format)
{
if (TypeElem!=ELEMPENDTASK && TypeElem!=ELEMTASKEND && TypeElem!=ELEMTRACELOGS)
    {
    FiltExp=formFilter.getItemValue("filter");
    if (Format==CSVFORMAT)
        window.open("ListElem?CSV=1&TE="+TypeElem+"&F="+FiltExp);
    else    
        GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);
    }
else
    {
    var Cat="";    
    if (TypeElem==ELEMTRACELOGS)
        Cat=formFilter.getItemValue("DocType");
    else
        Cat=formFilter.getItemValue("Category");
    var Fec1=formFilter.getItemValue("Fec1");
    if (Fec1!=null && Fec1!="")
        Fec1=Fec1.getTime();
    else
        Fec1="";
    var Fec2=formFilter.getItemValue("Fec2");
    if (Fec2!=null && Fec2!="")
        Fec2=Fec2.getTime();
    else
        Fec2="";
    if (Format==CSVFORMAT)
        window.open("ListElem?CSV=1&TE="+TypeElem+"&Cat="+Cat+"&Fec1="+Fec1+"&Fec2="+Fec2);
    else    
        GridResults.load("ListElem?TE="+TypeElem+"&Cat="+Cat+"&Fec1="+Fec1+"&Fec2="+Fec2);
    }    
}
//------------------------------------------------------------
function ElemNewObj(TypeElem, Id)
{
CreateWin(TypeElem);
FormElem.loadStruct("MantElem?Oper=New&Ty="+TypeElem+"&ObjId="+Id, function(){
    FormElem.setFocusOnFirstActive();
    CompleteForm(EOPERNEW, TypeElem);    
    });
FormElem.enableLiveValidation(true);
FormElem.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FillHideFields(TypeElem);    
        FormElem.send("MantElem?Oper=New", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            {   
                            GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);    
                            FormElem.unload();
                            WinElem.close();
                            }
                        else 
                            alert(response); 
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormElem.unload();
        WinElem.close();
        }
     }
             );    
}

//------------------------------------------------------------
function ElemNew(TypeElem)
{
CreateWin(TypeElem);
FormElem.loadStruct("MantElem?Oper=New&Ty="+TypeElem, function(){
    FormElem.setFocusOnFirstActive();
    CompleteForm(EOPERNEW, TypeElem);    
    });
FormElem.enableLiveValidation(true);    
FormElem.attachEvent("onChange", function(name, value, is_checked){
    if (TypeElem==ELEMTASKCRON)
        {
        if (name=="TaskType") 
            {
            if (value==3 || value==7)    
                FormElem.disableItem("TTEST");
            else    
                FormElem.enableItem("TTEST");
            if (value==1 ||value==2 ||value==5 )
                {
                FormElem.hideItem(COMBOFOLDER);
                FormElem.showItem(COMBODOCS);
                }
            else
                {
                FormElem.hideItem(COMBODOCS);
                FormElem.showItem(COMBOFOLDER);                
                }
            }
        }
    else if (TypeElem==ELEMTASKEVENT)
        {
        if (name=="TaskType") 
            {
            if (value==200 || value==202 || value==204 || value==206|| value==207|| value==208|| value==209 )
                {
                FormElem.hideItem(COMBOFOLDER);
                FormElem.showItem(COMBODOCS);
                }
            else
                {
                FormElem.hideItem(COMBODOCS);
                FormElem.showItem(COMBOFOLDER);                
                }
            }
        }
    else if (TypeElem==ELEMREPOS)  
        {
        if (name=="Name")
            {if (value=="PD_FTRep")
                FormElem.showItem(BUTFT);  
            else
                FormElem.hideItem(BUTFT);  
            }
        }
    }); 
FormElem.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FillHideFields(TypeElem);    
        FormElem.send("MantElem?Oper=New", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            {   
                            GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);    
                            FormElem.unload();
                            WinElem.close();
                            }
                        else 
                            alert(response); 
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormElem.unload();
        WinElem.close();
        }
     else if (TypeElem==ELEMTASKCRON || TypeElem==ELEMTASKEVENT )  
        ProcessWizard(FormElem, TypeElem, name);
     else if (name==BUTFT)
        {
        ProcessFT(FormElem);    
        }
     });    
}
//------------------------------------------------------------
function ProcessFT(FormRep)
{
var Url="RepFT";
var vals=FormRep.getItemValue("Param").split("|");
var WinRPFT=myWins.createWindow({
id:"RPFT",
left:20,
top:1,
width:500,
height:200,
center:true,
modal:true,
resize:false});  
WinRPFT.setText("OpenProdoc");
var FormRPFT=WinRPFT.attachForm();
FormRPFT.loadStruct(Url+"?F="+CurrFold, function(){
    FormRPFT.setFocusOnFirstActive();
    if (vals.length>0)
       FormRPFT.setItemValue("GlobLang", vals[0]);
    if (vals.length>1)
       FormRPFT.setItemValue("SW", vals[1]);
    }); 
FormRPFT.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormRep.setItemValue("Param", FormRPFT.getItemValue("GlobLang")+"|"+FormRPFT.getItemValue("SW"));
        }
    FormRPFT.unload();
    WinRPFT.close();
     });    
}
//------------------------------------------------------------
function ProcessWizard(FormEl, TypeElem, ButtName)
{
var TaskType=FormEl.getItemValue("TaskType");
if (ButtName=="TEDIT")
   EditWiz(FormEl, TypeElem, TaskType);
else if (ButtName=="TTEST")
    ShowTest(FormEl, TypeElem, TaskType);
else if (ButtName=="TRUN")
    RunTask(FormEl, TypeElem, TaskType);
}
//------------------------------------------------------------
function EditWiz(FormEl, TypeElem, TaskType)
{
var WinWizard=myWins.createWindow({
id:"WinWizard",
left:20,
top:1,
width:500,
height:260,
center:true,
modal:true,
resize:true});  
WinWizard.setText(TypeElem);
var  FormWiz=WinWizard.attachForm();   
FormWiz.loadStruct("TaskWiz?TE="+TypeElem+"&TT="+TaskType, function(){
        FormWiz.setItemValue("TaskParam",FormEl.getItemValue("TaskParam") );    
        FormWiz.setItemValue("TaskParam2",FormEl.getItemValue("TaskParam2") );    
        FormWiz.setItemValue("TaskParam3",FormEl.getItemValue("TaskParam3") );    
        FormWiz.setItemValue("TaskParam4",FormEl.getItemValue("TaskParam4") ); 
    });
FormWiz.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {
        FormEl.setItemValue("TaskParam",FormWiz.getItemValue("TaskParam") );    
        FormEl.setItemValue("TaskParam2",FormWiz.getItemValue("TaskParam2") );    
        FormEl.setItemValue("TaskParam3",FormWiz.getItemValue("TaskParam3") );    
        FormEl.setItemValue("TaskParam4",FormWiz.getItemValue("TaskParam4") ); 
        }
    FormWiz.unload();
    WinWizard.close();
    });    

}
//------------------------------------------------------------
function ShowTest(FormEl, TypeElem, TaskType)
{
var WinWizard=myWins.createWindow({
id:"WinWizard",
left:20,
top:1,
width:500,
height:260,
center:true,
modal:true,
resize:true});  
WinWizard.setText("Test:"+TypeElem);    
var GridWiz=WinWizard.attachGrid(); 
var Fec1=FormEl.getItemValue("NextDate");
if (Fec1!=null && Fec1!="")
    Fec1=Fec1.getTime();
else
    Fec1="";
GridWiz.load("TestProcess?T1="+FormEl.getItemValue("TaskParam")
        +"&T2="+FormEl.getItemValue("TaskParam2")
        +"&T3="+FormEl.getItemValue("TaskParam3")
        +"&T4="+FormEl.getItemValue("TaskParam4")
        +"&Task="+FormEl.getItemValue("TaskType")
        +"&Filter="+FormEl.getItemValue("ObjFilter")
        +"&Item="+FormEl.getItemValue("ObjType")
        +"&Item2="+FormEl.getItemValue("ObjType2")
        +"&NextDate="+Fec1
        );
}
//------------------------------------------------------------
function RunTask(FormEl, TypeElem, TaskType)
{
var Fec1=FormEl.getItemValue("NextDate");
if (Fec1!=null && Fec1!="")
    Fec1=Fec1.getTime();
else
    Fec1="";    
var Url="RunProcess?T1="+FormEl.getItemValue("TaskParam")
        +"&T2="+FormEl.getItemValue("TaskParam2")
        +"&T3="+FormEl.getItemValue("TaskParam3")
        +"&T4="+FormEl.getItemValue("TaskParam4")
        +"&Task="+FormEl.getItemValue("TaskType")
        +"&Filter="+FormEl.getItemValue("ObjFilter")
        +"&Item="+FormEl.getItemValue("ObjType")
        +"&Item2="+FormEl.getItemValue("ObjType2")
        +"&NextDate="+Fec1;    
dhtmlx.confirm(LocaleTrans("Do_you_really_want_to_execute_the_task")+"? ("
            +LocaleTrans("Beware_that_several_elements_can_be_altered_or_deleted")+")", function(result)
    {    
    if (result==true)   
        {
        var r=window.dhx4.ajax.getSync(Url);
        var xml = r.xmlDoc.responseXML;
        var nodes = xml.getElementsByTagName("status");
        alert(nodes[0].textContent);                                   
        }
    });
}
//------------------------------------------------------------
function FillHideFields(TypeElem)
{
if (TypeElem==ELEMACL)    
    {
    var AllPerm="";    
    for (var NumG=0; NumG<GGroups.getRowsNum(); NumG++)   
        {
        var P;    
        for (P=0; P<PERMISSIONS.length; P++) 
            if (PERMISSIONS[P]==GGroups.cellByIndex(NumG, 1).getValue())
                break;
        AllPerm=AllPerm+"|"+GGroups.cellByIndex(NumG, 0).getValue()+"/"+P;
        }
    FormElem.setItemValue("Groups", AllPerm);    
    AllPerm="";    
    for (var NumU=0; NumU<GUsers.getRowsNum(); NumU++)   
        {
        var P;    
        for (P=0; P<PERMISSIONS.length; P++) 
            if (PERMISSIONS[P]==GUsers.cellByIndex(NumU, 1).getValue())
                break;
        AllPerm=AllPerm+"|"+GUsers.cellByIndex(NumU, 0).getValue()+"/"+P;
        }
    FormElem.setItemValue("Users", AllPerm);    
    }
else if (TypeElem==ELEMGROUPS)    
    {
    var AllPerm="";    
    for (var NumG=0; NumG<GGroups.getRowsNum(); NumG++)   
        AllPerm=AllPerm+"|"+GGroups.cellByIndex(NumG, 0).getValue();
    FormElem.setItemValue("Groups", AllPerm);    
    AllPerm="";    
    for (var NumU=0; NumU<GUsers.getRowsNum(); NumU++)   
        AllPerm=AllPerm+"|"+GUsers.cellByIndex(NumU, 0).getValue();
    FormElem.setItemValue("Users", AllPerm);    
    }   
else if (TypeElem==ELEMOBJ)    
    {
    var Tot="";    
    for (var NumA=0; NumA<GMetadata.getRowsNum(); NumA++)   
        {
        for (var NC=0; NC<9; NC++)
            {
            var Val=GMetadata.cells2(NumA, NC).getValue();
            if (Val=="")
                Val="_";
            else
                Val.replace("|", "-");
            Tot=Tot+"|"+Val;
            }
        Tot=Tot+"¬";   
        }
    FormElem.setItemValue("ATTRS",Tot);     
    }    
}
//------------------------------------------------------------
function ElemMod(TypeElem, ElemId)
{
CreateWin(TypeElem);
FormElem.loadStruct("MantElem?Oper=Modif&Ty="+TypeElem+"&Id="+ElemId, function(){
    FormElem.setFocusOnFirstActive();
    CompleteForm(EOPERMOD, TypeElem);    
    });
FormElem.enableLiveValidation(true);    
FormElem.attachEvent("onChange", function(name, value, is_checked){
    if (TypeElem==ELEMTASKCRON)
        {
        if (name=="TaskType") 
            {
            if (value==3 || value==7)    
                FormElem.disableItem("TTEST");
            else    
                FormElem.enableItem("TTEST");
            if (value==1 ||value==2 ||value==5 )
                {
                FormElem.hideItem(COMBOFOLDER);
                FormElem.showItem(COMBODOCS);
                }
            else
                {
                FormElem.hideItem(COMBODOCS);
                FormElem.showItem(COMBOFOLDER);                
                }
            }
        }
    else if (TypeElem==ELEMTASKEVENT)
        {
        if (name=="TaskType") 
            {
            if (value==200 || value==202 || value==204 || value==206|| value==207|| value==208|| value==209 )
                {
                FormElem.hideItem(COMBOFOLDER);
                FormElem.showItem(COMBODOCS);
                }
            else
                {
                FormElem.hideItem(COMBODOCS);
                FormElem.showItem(COMBOFOLDER);                
                }
            }
        }
    }); 
FormElem.attachEvent("onButtonClick", function (name)
    {
    if (name==OK || name=="CreateObj" || name=="DeleteObj" )
        {    
        FillHideFields(TypeElem);    
        FormElem.send("MantElem?Oper=Modif", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            {   
                            if (name=="CreateObj")    
                                window.dhx4.ajax.get("CreateObj?Id="+ElemId, function(r)
                                {
                                var xml = r.xmlDoc.responseXML;
                                var nodes = xml.getElementsByTagName("status");
                                if (nodes[0].textContent!=OK)
                                    alert(nodes[0].textContent);
                                else  // refresh the created status
                                    GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);                                        
                                });   
                            else if (name=="DeleteObj")  
                                window.dhx4.ajax.get("DeleteObj?Id="+ElemId, function(r)
                                {
                                var xml = r.xmlDoc.responseXML;
                                var nodes = xml.getElementsByTagName("status");
                                if (nodes[0].textContent!=OK)
                                    alert(nodes[0].textContent);
                                else  // refresh the created status
                                    GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);   
                                });  
                            else    
                                GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);    
                            FormElem.unload();
                            WinElem.close();
                            }
                        else 
                            alert(response); 
                        } );
        }
     else if (name==CANCEL)  
        {   
        FormElem.unload();
        WinElem.close();
        }
     else if (TypeElem==ELEMTASKCRON || TypeElem==ELEMTASKEVENT )  
        ProcessWizard(FormElem, TypeElem, name);
     else if (name==BUTFT)
        {
        ProcessFT(FormElem);    
        }
     });       
}
//------------------------------------------------------------
function ElemDelLogs(TypeElem, formFilter)
{
var Cat="";    
Cat=formFilter.getItemValue("DocType");
var Fec1=formFilter.getItemValue("Fec1");
if (Fec1!=null && Fec1!="")
    Fec1=Fec1.getTime();
else
    Fec1="";
var Fec2=formFilter.getItemValue("Fec2");
if (Fec2!=null && Fec2!="")
    Fec2=Fec2.getTime();
else
    Fec2="";
window.dhx4.ajax.get("DelLogs?Cat="+Cat+"&Fec1="+Fec1+"&Fec2="+Fec2, function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    if (nodes[0].textContent!="")
        alert(nodes[0].textContent);
    GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);
    });
}
//------------------------------------------------------------
function ElemDelTaskEnd(TypeElem, ElemId)
{
window.dhx4.ajax.post("DelElem", "ElemId="+ElemId, function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    if (nodes[0].textContent!="")
        alert(nodes[0].textContent);
    GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);
    });
}
//------------------------------------------------------------
function ElemDel(TypeElem, ElemId)
{    
CreateWin(TypeElem);
FormElem.loadStruct("MantElem?Oper=Delete&Ty="+TypeElem+"&Id="+ElemId, function(){
    FormElem.setFocusOnFirstActive();
    CompleteForm(EOPERDEL, TypeElem);    
    });
FormElem.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormElem.send("MantElem?Oper=Delete", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            {   
                            GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);    
                            FormElem.unload();
                            WinElem.close();
                            }
                        else 
                            alert(response); 
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormElem.unload();
        WinElem.close();
        }
     }
             );       
}
//------------------------------------------------------------
function ElemCopy(TypeElem, ElemId)
{
CreateWin(TypeElem);
FormElem.loadStruct("MantElem?Oper=Copy&Ty="+TypeElem+"&Id="+ElemId, function(){
    FormElem.setFocusOnFirstActive();
    CompleteForm(EOPERCOP, TypeElem);    
    });
FormElem.enableLiveValidation(true); 
FormElem.attachEvent("onChange", function(name, value, is_checked){
    if (TypeElem==ELEMTASKCRON)
        {
        if (name=="TaskType") 
            {
            if (value==3 || value==7)    
                FormElem.disableItem("TTEST");
            else    
                FormElem.enableItem("TTEST");
            if (value==1 ||value==2 ||value==5 )
                {
                FormElem.hideItem(COMBOFOLDER);
                FormElem.showItem(COMBODOCS);
                }
            else
                {
                FormElem.hideItem(COMBODOCS);
                FormElem.showItem(COMBOFOLDER);                
                }
            }
        }
    else if (TypeElem==ELEMTASKEVENT)
        {
        if (name=="TaskType") 
            {
            if (value==200 || value==202 || value==204 || value==206|| value==207|| value==208|| value==209 )
                {
                FormElem.hideItem(COMBOFOLDER);
                FormElem.showItem(COMBODOCS);
                }
            else
                {
                FormElem.hideItem(COMBODOCS);
                FormElem.showItem(COMBOFOLDER);                
                }
            }
        }
    }); 

FormElem.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FillHideFields(TypeElem);    
        FormElem.send("MantElem?Oper=Copy", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            {   
                            GridResults.load("ListElem?TE="+TypeElem+"&F="+FiltExp);    
                            FormElem.unload();
                            WinElem.close();
                            }
                        else 
                            alert(response); 
                        } );
        }
     else if (name==CANCEL)  
        {   
        FormElem.unload();
        WinElem.close();
        }
     else if (TypeElem==ELEMTASKCRON || TypeElem==ELEMTASKEVENT )  
        ProcessWizard(FormElem, TypeElem, name);
     }
             );           
}
//------------------------------------------------------------
function ElemExport(TypeElem, ElemId)
{
window.open("ElemExport?Ty="+TypeElem+"&Id="+ElemId);        
}
//------------------------------------------------------------
function ElemExpAll(TypeElem, FiltExp)
{
window.open("ElemExport?Ty="+TypeElem+"&F="+FiltExp);            
}
//------------------------------------------------------------
function ElemImp(TypeElem)
{
var Url="ImpElem";
WinAF=myWins.createWindow({
id:"AddDoc",
left:20,
top:1,
width:450,
height:220,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormImpElem=WinAF.attachForm();
FormImpElem.loadStruct("ImpElem", function(){
    FormImpElem.setFocusOnFirstActive();
    });
FormImpElem.attachEvent("onButtonClick", function (name)
    {if (name==CANCEL || name==OK)
        {   
        FormImpElem.unload();
        WinAF.close();
        }
     });
FormImpElem.attachEvent("onUploadFile",function(realName,serverName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });
FormImpElem.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    alert(nodes[0].textContent);
    });
    });
     
}
//------------------------------------------------------------
function CreateWin(TypeElem)
{
WinElem=myWins.createWindow({
id:"WinElem",
left:100,
top:1,
width:600,
height:600,
center:false,
modal:true,
resize:true});  
WinElem.setText(TypeElem);
if (TypeElem==ELEMACL)
    {
    ElemLayout=WinElem.attachLayout('2E');   
    ElemLayout.cells('a').hideHeader();
    FormElem=ElemLayout.cells('a').attachForm(); 
    ElemLayout.cells('a').setHeight(180);
    ElemTabBar=ElemLayout.cells('b').attachTabbar();
    ElemTabBar.addTab("Groups", LocaleTrans("Groups"), null, null, true); //---
    TBG = ElemTabBar.tabs("Groups").attachToolbar();
    TBG.addButton(EOPERNEW, 0, LocaleTrans("New"), "img/add.png", "img/add.png");
    TBG.addButton(EOPERMOD, 1, LocaleTrans("Edit"), "img/edit.png", "img/edit.png");
    TBG.addButton(EOPERDEL, 2, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    GGroups = ElemTabBar.tabs("Groups").attachGrid();
    GGroups.setHeader("Groups, Perm");
    GGroups.setColAlign("left,left");     
    GGroups.setColTypes("ro,ro");              
    GGroups.setColSorting("str,str");
    GGroups.init();
    ElemTabBar.addTab("Users", LocaleTrans("Users"));                    //---
    TBU = ElemTabBar.tabs("Users").attachToolbar();
    TBU.addButton(EOPERNEW, 0, LocaleTrans("New"), "img/add.png", "img/add.png");
    TBU.addButton(EOPERMOD, 1, LocaleTrans("Edit"), "img/edit.png", "img/edit.png");
    TBU.addButton(EOPERDEL, 2, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    GUsers = ElemTabBar.tabs("Users").attachGrid();
    GUsers.setHeader("Users, Perm");
    GUsers.setColAlign("left,left");     
    GUsers.setColTypes("ro,ro");              
    GUsers.setColSorting("str,str");
    GUsers.init();
    }
else if (TypeElem==ELEMGROUPS )
    {
    ElemLayout=WinElem.attachLayout('2E');   
    ElemLayout.cells('a').hideHeader();
    FormElem=ElemLayout.cells('a').attachForm(); 
    ElemLayout.cells('a').setHeight(180);
    ElemTabBar=ElemLayout.cells('b').attachTabbar();
    ElemTabBar.addTab("Groups", LocaleTrans("Groups"), null, null, true); //---
    TBG = ElemTabBar.tabs("Groups").attachToolbar();
    TBG.addButton(EOPERNEW, 0, LocaleTrans("New"), "img/add.png", "img/add.png");
    TBG.addButton(EOPERDEL, 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    GGroups = ElemTabBar.tabs("Groups").attachGrid();
    GGroups.setHeader("Groups");
    GGroups.setColAlign("left");     
    GGroups.setColTypes("ro");              
    GGroups.setColSorting("str");
    GGroups.init();
    ElemTabBar.addTab("Users", LocaleTrans("Users"));                    //---
    TBU = ElemTabBar.tabs("Users").attachToolbar();
    TBU.addButton(EOPERNEW, 0, LocaleTrans("New"), "img/add.png", "img/add.png");
    TBU.addButton(EOPERDEL, 1, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    GUsers = ElemTabBar.tabs("Users").attachGrid();
    GUsers.setHeader("Users");
    GUsers.setColAlign("left");     
    GUsers.setColTypes("ro");              
    GUsers.setColSorting("str");
    GUsers.init();
    }
else if (TypeElem==ELEMOBJ)
    {
    ElemLayout=WinElem.attachLayout('2E');   
    ElemLayout.cells('a').hideHeader();
    FormElem=ElemLayout.cells('a').attachForm(); 
    ElemLayout.cells('a').setHeight(325);
    ElemTabBar=ElemLayout.cells('b').attachTabbar();
    ElemTabBar.addTab("Metadata", "Metadata", null, null, true); //---
    TBG = ElemTabBar.tabs("Metadata").attachToolbar();
    TBG.addButton(EOPERNEW, 0, LocaleTrans("New"), "img/add.png", "img/add.png");
    TBG.addButton(EOPERMOD, 1, LocaleTrans("Edit"), "img/edit.png", "img/edit.png");
    TBG.addButton(EOPERDEL, 2, LocaleTrans("Delete"), "img/del.png", "img/del.png");
    GMetadata = ElemTabBar.tabs("Metadata").attachGrid();
    ElemTabBar.addTab("Inherited", LocaleTrans("Inherited_Attributes"));                    //---
    GInherited = ElemTabBar.tabs("Inherited").attachGrid();
    }
else
    FormElem=WinElem.attachForm();   
if (TypeElem==ELEMMIME)
    {
    WinElem.setDimension(500, 250);
    WinElem.center();
    }
else if (TypeElem==ELEMREPOS)
    {
    WinElem.setDimension(500, 430);
    WinElem.center();    
    }    
else if ( TypeElem==ELEMAUTH || TypeElem==ELEMCUST || TypeElem==ELEMUSERS )
    {
    WinElem.setDimension(500, 400);
    WinElem.center();
    }
else if (TypeElem==ELEMTASKCRON)
        WinElem.setDimension(800, 660);
else if (TypeElem==ELEMTASKEVENT)
        WinElem.setDimension(800, 580);
//WinElem.center();
}
//--------------------------------------------------------------
function CompleteForm(Oper, TypeElem)
{
if (TypeElem==ELEMACL)
    CompleteFormAcl(Oper);       
else if (TypeElem==ELEMGROUPS)
    CompleteFormGroup(Oper); 
else if (TypeElem==ELEMOBJ)
    CompleteFormObjDef(Oper, GridResults.getSelectedRowId());
}
//--------------------------------------------------------------
function CompleteFormObjDef(Oper, Id)
{
GInherited.clearAndLoad("ListAttrInherited?Id="+Id+"&Oper="+Oper);  
GMetadata.clearAndLoad("ListAttrInherited?Id="+Id+"&Oper="+Oper+"&Own=1");  
if (Oper==EOPERDEL)
    TBG.clearAll();    
else if(Oper==EOPERMOD)
    {
    if (FormElem.isItem("DeleteObj") && FormElem.isItemEnabled("DeleteObj"))
        {
        TBG.clearAll(); 
        TBG.addButton("AddAttr", 0, LocaleTrans("Add_Attribute"), "img/add.png", "img/add.png");
        TBG.addButton("DelAttr", 1, LocaleTrans("Del_Attribute"), "img/del.png", "img/del.png"); 
        }
    }
TBG.attachEvent("onClick", function(OperA)  
    {
    if (OperA==EOPERNEW || OperA=="AddAttr")    
        MantAttr(OperA, "");
    else if (GMetadata.getSelectedRowId()!=null)     
        MantAttr(OperA, GMetadata.getSelectedRowId()); 
    });
}
//--------------------------------------------------------------
function MantAttr(OperA, Id)
{
WinAttr=myWins.createWindow({
id:"WinAttr",
left:20,
top:1,
width:500,
height:420,
center:true,
modal:true,
resize:false}); 
WinAttr.setText("Attributes");  
FormAttr=WinAttr.attachForm(); 
FormAttr.loadStruct("FormAttr?Oper="+OperA, function(){
    FormAttr.setFocusOnFirstActive(); 
    FormAttr.enableLiveValidation(true);
    if (OperA!=EOPERNEW)
        {
        var T=GMetadata.cellById(Id,1).getValue();    
        FormAttr.setItemValue("Name",    GMetadata.cellById(Id,0).getValue());
        FormAttr.setItemValue("UserName",GMetadata.cellById(Id,1).getValue());
        FormAttr.setItemValue("Descrip", GMetadata.cellById(Id,2).getValue());
        FormAttr.setItemValue("Type",    GMetadata.cellById(Id,3).getValue());
        FormAttr.setItemValue("Req",     GMetadata.cellById(Id,4).getValue());
        FormAttr.setItemValue("LongStr", GMetadata.cellById(Id,5).getValue());
        FormAttr.setItemValue("UniKey",  GMetadata.cellById(Id,6).getValue());
        FormAttr.setItemValue("ModAllow",GMetadata.cellById(Id,7).getValue());
        FormAttr.setItemValue("Multi",   GMetadata.cellById(Id,8).getValue());
        }
    if (FormAttr.getItemValue("Type")==AT_TYPESTRING)  
        {
        FormAttr.showItem("LongStr");
        FormAttr.setRequired("LongStr",true);
        }
    else
        {        
        FormAttr.hideItem("LongStr");
        FormAttr.setRequired("LongStr",false); 
        }
    if (FormAttr.getItemValue("Type")!=AT_TYPETHES)
        FormAttr.hideItem("ThesSel");
    else
        {
        FormAttr.showItem("ThesSel");    
        FormAttr.setItemValue("ThesSel", FormAttr.getItemValue("LongStr"));
        }
    });
FormAttr.attachEvent("onChange", function(name, value, is_checked){
    if (name=="Type")
        {
        if (value==AT_TYPESTRING)  
            {
            FormAttr.showItem("LongStr");
            FormAttr.setRequired("LongStr",true);
            }
        else
            {        
            FormAttr.hideItem("LongStr");
            FormAttr.setRequired("LongStr",false);
            }
        if (value!=AT_TYPETHES)
            FormAttr.hideItem("ThesSel");
        else
            FormAttr.showItem("ThesSel");
        }
    else if (name=="Req") 
        {
        if (FormAttr.isItemChecked("Req"))
            FormAttr.uncheckItem("Multi");
        }
    else if (name=="UniKey") 
        {
        if (FormAttr.isItemChecked("UniKey"))
            FormAttr.uncheckItem("Multi");
        }
    else if (name=="Multi") 
        {
        if (FormAttr.isItemChecked("Multi"))
            {
            FormAttr.uncheckItem("Req");
            FormAttr.uncheckItem("UniKey");
            }
        }
    }); 
FormAttr.attachEvent("onButtonClick", function (name)
    {
    if (FormAttr.getItemValue("Type")==AT_TYPESTRING && ( isNaN(parseInt(FormAttr.getItemValue("LongStr"))) || parseInt(FormAttr.getItemValue("LongStr"))==0 ) )
        alert("String Length required");
    else
        {
        if (name==OK)
            {   
            if (OperA==EOPERDEL)    
                GMetadata.deleteRow(Id);
            else if (OperA==EOPERMOD || OperA==EOPERNEW)
                {
                Id=FormAttr.getItemValue("Name");    
                if (OperA==EOPERNEW)
                    GMetadata.addRow(Id,"");
                GMetadata.cellById(Id,0).setValue(FormAttr.getItemValue("Name"));
                GMetadata.cellById(Id,1).setValue(FormAttr.getItemValue("UserName"));
                GMetadata.cellById(Id,2).setValue(FormAttr.getItemValue("Descrip"));
                GMetadata.cellById(Id,3).setValue(FormAttr.getItemValue("Type"));
                GMetadata.cellById(Id,4).setValue(FormAttr.getItemValue("Req"));
                GMetadata.cellById(Id,5).setValue(FormAttr.getItemValue("LongStr"));
                GMetadata.cellById(Id,6).setValue(FormAttr.getItemValue("UniKey"));
                GMetadata.cellById(Id,7).setValue(FormAttr.getItemValue("ModAllow"));
                GMetadata.cellById(Id,8).setValue(FormAttr.getItemValue("Multi"));  
                if (FormAttr.getItemValue("Type")=="Thesaur")
                    GMetadata.cellById(Id,5).setValue(FormAttr.getItemValue("ThesSel")); 
                }
            else if (OperA=="DelAttr" || OperA=="AddAttr" )    
                {
                var Type=FormElem.getItemValue("Name");    
                FormAttr.send(OperA+"?ObjType="+Type, function(loader, response)
                    { // Asynchronous 
                    if (response.substring(0,2)==OK)    
                        {      
                        FormElem.unload();
                        WinElem.close();
                        }
                    else 
                        alert(response); 
                    } );
                }
            }
        FormAttr.unload();
        WinAttr.close();  
        }
    });
}
//--------------------------------------------------------------
function CompleteFormAcl(Oper)   
{
if (Oper==EOPERDEL)
    {
    TBG.clearAll();
    TBU.clearAll();
    }    
var ListUP=FormElem.getItemValue("Users");    
var UP=ListUP.split("|");
for (var i=0; i<UP.length; i++)
    {
    if (UP[i]!="")    
        {
        var U_P=UP[i].split("/"); 
        GUsers.addRow(U_P[0], U_P[0]+","+PERMISSIONS[U_P[1]]);
        }
    }    
ListUP=FormElem.getItemValue("Groups");    
UP=ListUP.split("|");
for (var i=0; i<UP.length; i++)
    {
    if (UP[i]!="")    
        {
        var U_P=UP[i].split("/"); 
        GGroups.addRow(U_P[0], U_P[0]+","+PERMISSIONS[U_P[1]]);
        }
    } 
TBG.attachEvent("onClick", function(Oper)  
    {
    if (Oper==EOPERNEW)    
        GroupPerm(Oper, "Groups");
    else if (GGroups.getSelectedRowId()!=null && Oper==EOPERMOD)     
        GroupPerm(Oper, "Groups");
    else if (GGroups.getSelectedRowId()!=null && Oper==EOPERDEL)     
        GGroups.deleteRow(GGroups.getSelectedRowId());   
    });
TBU.attachEvent("onClick", function(Oper)  
    {
    if (Oper==EOPERNEW)    
        GroupPerm(Oper, "Users");
    else if (GUsers.getSelectedRowId()!=null && Oper==EOPERMOD)     
        GroupPerm(Oper, "Users");
    else if (GUsers.getSelectedRowId()!=null && Oper==EOPERDEL)     
        GUsers.deleteRow(GUsers.getSelectedRowId());   
    });
}    
//--------------------------------------------------------------
function CompleteFormGroup(Oper)   
{
if (Oper==EOPERDEL)
    {
    TBG.clearAll();
    TBU.clearAll();
    }    
var ListUP=FormElem.getItemValue("Users");    
var UP=ListUP.split("|");
for (var i=0; i<UP.length; i++)
    {
    if (UP[i]!="")    
        GUsers.addRow(UP[i], UP[i]);
    }    
ListUP=FormElem.getItemValue("Groups");    
UP=ListUP.split("|");
for (var i=0; i<UP.length; i++)
    {
    if (UP[i]!="")    
        GGroups.addRow(UP[i], UP[i]);
    } 
TBG.attachEvent("onClick", function(Oper)  
    {
    if (Oper==EOPERNEW)    
        GroupAddE(Oper, "Groups");
    else if (GGroups.getSelectedRowId()!=null && Oper==EOPERDEL)     
        GGroups.deleteRow(GGroups.getSelectedRowId());   
    });
TBU.attachEvent("onClick", function(Oper)  
    {
    if (Oper==EOPERNEW)    
        GroupAddE(Oper, "Users");
    else if (GUsers.getSelectedRowId()!=null && Oper==EOPERDEL)     
        GUsers.deleteRow(GUsers.getSelectedRowId());   
    });
}    
//--------------------------------------------------------------
function GroupPerm(Oper, Type)
{
var WEPerm=myWins.createWindow({
id:"WEPerm",
left:20,
top:1,
width:500,
height:150,
center:true,
modal:true,
resize:true});  
WEPerm.setText(Type);
var FormEPerm=WEPerm.attachForm();  
var Grid;
if (Type=="Groups")
    Grid=GGroups;
else
    Grid=GUsers;
FormEPerm.loadStruct("FormEPerm?Type="+Type+"&Oper="+Oper+"&Id="+GGroups.getSelectedRowId());
FormEPerm.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        if (Oper==EOPERMOD)    
            Grid.cellById(Grid.getSelectedRowId(), 1).setValue(PERMISSIONS[FormEPerm.getItemValue("Permission")]);
        else
            Grid.addRow(FormEPerm.getItemValue(Type),FormEPerm.getItemValue(Type)+","+PERMISSIONS[FormEPerm.getItemValue("Permission")]) 
        }
    FormEPerm.unload();
    WEPerm.close();   
    });
}
//--------------------------------------------------------------
function GroupAddE(Oper, Type)
{
var WEPerm=myWins.createWindow({
id:"WEPerm",
left:20,
top:1,
width:500,
height:150,
center:true,
modal:true,
resize:true});  
WEPerm.setText(Type);
var FormEPerm=WEPerm.attachForm();  
var Grid;
if (Type=="Groups")
    Grid=GGroups;
else
    Grid=GUsers;
FormEPerm.loadStruct("GroupAddE?Type="+Type+"&Oper="+Oper+"&Id="+GGroups.getSelectedRowId());
FormEPerm.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        Grid.addRow(FormEPerm.getItemValue(Type),FormEPerm.getItemValue(Type)) 
    FormEPerm.unload();
    WEPerm.close();   
    });
}
//--------------------------------------------------------------
function ExportCSV(TypeElem, formFilter)
{
RefreshAdminGrid(TypeElem, formFilter, CSVFORMAT);
}
//--------------------------------------------------------------
function ExporGenCSV()
{
window.open("ExportCSV");    
}
//--------------------------------------------------------------
var Orig=[];
var Trans=[];
function LocaleTrans(Term)
{
for (var i=0; i<Orig.length; i++)  
    {
    if (Orig[i]==Term)
        return(Trans[i]);
    }
var r=window.dhx4.ajax.getSync("LocaleTrans?Par="+Term);
var xml = r.xmlDoc.responseXML;
var nodes = xml.getElementsByTagName("tr");
Orig.push(Term);
var Trad=nodes[0].textContent;
Trans.push(Trad);
return(Trad);        
}
//--------------------------------------------------
function ImportRIS(CurrFold)
{
var Url="ImportRIS";
WinAF=myWins.createWindow({
id:"ImportRIS",
left:20,
top:1,
width:500,
height:300,
center:true,
modal:true,
resize:false});  
WinAF.setText("OpenProdoc");
var FormImportRIS=WinAF.attachForm();
FormImportRIS.loadStruct(Url+"?F="+CurrFold, function(){
    FormImportRIS.setFocusOnFirstActive();
    });
FormImportRIS.enableLiveValidation(true);      
FormImportRIS.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormImportRIS.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)==OK)    
                            FormImportRIS.enableItem("UpFile");                            
                        else 
                            alert(response.substring(2)); 
                        });
        }
     else 
        {   
        FormImportRIS.unload();
        WinAF.close();
        }
    });
FormImportRIS.attachEvent("onUploadFile",function(realName,serverName)
    {
    FormImportRIS.unload();
    WinAF.close();
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold);
    });
FormImportRIS.attachEvent("onUploadFail",function(realName){
    window.dhx4.ajax.get("UpFileStatus", function(r)
        {
        var xml = r.xmlDoc.responseXML;
        var nodes = xml.getElementsByTagName("status");
        alert(nodes[0].textContent);
        });
    });    
    
}
//--------------------------------------------------
function Copy(CurrDoc)
{
CpDoc=CurrDoc;
menu.setItemEnabled("PasteDoc");
menu.setItemText("PasteDoc", LocaleTrans("Paste_Doc")+"="+CurrTitle+" ("+CpDoc+")");
}
//--------------------------------------------------
function Paste(CpDoc)
{
window.dhx4.ajax.get("MoveDoc?D="+CpDoc+"&F="+CurrFold, function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    if (nodes[0].textContent.substring(0,2)!=OK)
        {
        alert(nodes[0].textContent);    
        }
    else
        {
        DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold, function ()
            {
            if (DocsGrid.getRowsNum()>0)
                DocsGrid.selectRow(0, true, false, true);
            });
        }
    });    
CpDoc="";
menu.setItemText("PasteDoc", LocaleTrans("Paste_Doc"));
menu.setItemDisabled("PasteDoc");
}
//--------------------------------------------------
function CopyF(CurrFold)
{
CpFold=CurrFold;
CpParentFold=DocsTree.getParentId(CpFold);
menu.setItemEnabled("PasteFold");
menu.setItemText("PasteFold", LocaleTrans("Paste_Fold")+"="+CurrFoldTitle+" ("+CpFold+")");
}
//--------------------------------------------------
function PasteF(CpFold)
{
window.dhx4.ajax.get("MoveFold?F1="+CpFold+"&F="+CurrFold, function(r)
    {
    var xml = r.xmlDoc.responseXML;
    var nodes = xml.getElementsByTagName("status");
    if (nodes[0].textContent.substring(0,2)!=OK)
        {
        alert(nodes[0].textContent);    
        }
    else
        {
        DocsTree.refreshItem(CpParentFold);
        DocsTree.refreshItem(CurrFold);
        }
    });    
CpDoc="";
menu.setItemText("PasteFold", LocaleTrans("Paste_Fold"));
menu.setItemDisabled("PasteFold");
}
//--------------------------------------------------
function ModACLDoc(Doc2Change)
{
var WinMD=myWins.createWindow({
    id:"ModACLDoc",
    left:20,
    top:1,
    width:600,
    height:420,
    center:true,
    modal:true,
    resize:true
});  
WinMD.setText("OpenProdoc");
var FormModDoc=WinMD.attachForm();
FormModDoc.loadStruct("ModDoc?D="+Doc2Change+"&AclMode=1", function(){
    FormModDoc.setFocusOnFirstActive();
    });
FormModDoc.enableLiveValidation(true);     
FormModDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {    
        FormModDoc.send("ModACLDoc", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)  
                            alert(response.substring(2)); 
                        FormModDoc.unload();
                        WinMD.close();
                        });
        }
    else if (name==CANCEL) 
        {   
        FormModDoc.unload();
        WinMD.close();
        }
    });
}
//--------------------------------------------------

