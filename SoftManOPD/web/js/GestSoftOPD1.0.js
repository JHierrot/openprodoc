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

var layout;
var ListGrid;
var FilterForm=null;
var FilterStr="";
var CurrProduct;
var CurrDepartment;
var CurrSoftCompany;
var CurrIssue;
var myWins;
var SoftManOPDSidebar;
var Toolbar;
var WinMant;
var ADD="Add";
var COPY="Copy";
var CHECKOUT="CheckOut";
var CHECKIN="CheckIn";
var CANCELCHECKOUT="CancelCheckOut";
var LISTVER="ListVer";
var SEPAR="Sep";
var UPD="Upd";
var DEL="Del";
var ADDFOLD="AddFold";
var UPDFOLD="UpdFold";
var DELFOLD="DelFold";
var VER="Ver";
var ADDPROD="AddProd";
var LISTDOCS="ListDocs";
var LISTISSUES="ListIssues";
var DEPENDENCIES="Dependencies";
var DEPENDTREE="DependTree";
var IMPACTTREE="ImpactTree";
var OK="OK";
var CANCEL="CANCEL";
var CurrentGrid="";
var TBEvent=null;
var WinVers;
var TITLE_COL=1;
var VERTITLE_COL=0;
var VersGrid;
var WinVers;
var WinDep;
var DepGrid;
var WinTree;
var WinListDoc;
var WinDoc;
var IssuesGrid;
var CurrDoc;
var CurrFold;
var WinMantDoc;
var FoldsTree;
var DocsGrid;
var T_EDIT="Update";
var T_DEL="Delete";
var CSVFORMAT="CSV";
var GridResults;


var FiltFieldsProd=null;
var FiltFieldsIssue=null;
var FiltFieldsDep=null;
var FiltFieldsSoft=null;

//--------------------------------------------------------------
function Init()
{   
if (window.dhx.isIE) 
    {
    alert("Unsupported browser / navegador no soportado");
    }    
document.title="Software Management OpenProdoc 1.0 ("+LocaleTrans("_User")+")";
}
//-----------------------------------------------------------------------
function doOnLoadMain() 
{                   
Init();    
layout = new dhtmlXLayoutObject(document.body,"3L");         
layout.cells("a").setText(LocaleTrans("Menu")); 
layout.cells("a").setWidth(120);
SoftManOPDSidebar = layout.cells("a").attachSidebar({
    template: "icons_text",
    width: 120
});
SoftManOPDSidebar.loadStruct("Menu");
SoftManOPDSidebar.attachEvent("onSelect", function(id, lastId){
    ExecMenu(id);
});
ListGrid = layout.cells("c").attachGrid();
Toolbar=layout.cells("c").attachToolbar();
ShowListProducts()
if (myWins==null)
    myWins = new dhtmlXWindows();
};
//-----------------------------------------------------------------------
function ExecMenu(IdMenu)
{  
document.body.style.cursor = 'default';
switch (IdMenu)    
    {
    case "Products": ShowListProducts();
        break;
    case "Departments": ShowListDepartments();
        break;
    case "SoftCompanies": ShowListSoftCompanies();
        break;
    case "Issues": ShowListIssues();
        break;
    case "Search": CurrFold="RootFolder";
        SearchDoc();
        break;
    case "Help": window.open(LocaleTrans("_Help")); // actually not translation but select of language
        // TODO: Help  
        break;
    case "About": About();    
        break;
    case "Exit": window.location.assign("Logout");
        layout.unload();
        layout=null;
        myWins.unload();
        myWins=null;
        break;
    }
}
//-----------------------------------------------------------------------
function SearchDoc()
{
var Url="SearchDoc";
WinAF=myWins.createWindow({
id:"SearchDoc",
left:100,
top:1,
width:800,
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
TabBar.addTab("SQL", LocaleTrans("Advanced_Search"));
TabBar.addTab("Results", LocaleTrans("Search_Results"));
TabBar.addTab("Reports", LocaleTrans("Reports_Generation"));
GridReports=TabBar.tabs("Reports").attachGrid();
GridReports.setHeader(LocaleTrans("Report_Title")+","+LocaleTrans("MimeType")+","+LocaleTrans("Docs_per_Page")+","+LocaleTrans("Pages_per_File"));   //sets the headers of columns
GridReports.setColumnIds("Title,Mime,DocsPage,PagesArch");   
GridReports.setInitWidths("300,100,100,*"); 
GridReports.setColAlign("left,left,left,left");   
GridReports.setColTypes("link,ro,ro,ro");  
GridReports.setColSorting("str,str,int,int"); 
GridReports.load("RepList");
GridReports.init();
TabBar.tabs("Reports").disable();
ToolBar = TabBar.tabs("Results").attachToolbar();
ToolBar.addButton(T_EDIT, 0, LocaleTrans("Edit"), "img/DocEdit.png", "img/DocEdit.png");
ToolBar.addButton(T_DEL, 1, LocaleTrans("Delete"), "img/DocDel.png", "img/DocDel.png");
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
FormSQLSearchDoc = TabBar.tabs("SQL").attachForm();
FormSQLSearchDoc.load("FormSQL?Type=DOC");
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
        FormSQLSearchDoc.unload();
        WinAF.close();
        }    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormSearchDoc, name.substring(2));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormSearchDoc, name.substring(3)); 
    }); 
//FormSQLSearchDoc.enableLiveValidation(true);     
FormSQLSearchDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormSQLSearchDoc.send(Url, function(loader, response)
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
        FormSQLSearchDoc.unload();
        WinAF.close();
        }   
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
GridResults.setColTypes(ListPar[1]);   
GridResults.setColSorting(ListPar[2]);
GridResults.init();
GridResults.parse(ListPar[3],"xml");
TabBar.tabs("Results").show(true);
TabBar.tabs("Results").setActive();
}
//------------------------------------------------------------
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
WinA.setText("About Soft. Management OpenProdoc");
var FormAbout=WinA.attachForm();
FormAbout.loadStruct("About");   
if (CurrentGrid=="ListProducts")
    SoftManOPDSidebar.items("Products").setActive();
else if (CurrentGrid=="ListDepartments")
    SoftManOPDSidebar.items("Departments").setActive();
else if (CurrentGrid=="ListSoftCompanies")
    SoftManOPDSidebar.items("SoftCompanies").setActive();
else if (CurrentGrid=="ListIssues")
    SoftManOPDSidebar.items("Issues").setActive();
}
//-----------------------------------------------------------------------
function ShowListProducts()
{
// TODO: Form Filter Products    
layout.cells("b").setText(LocaleTrans("Products Filter"));   
layout.cells("b").setHeight(240);
LoadFilterForm("FilterForm?Filt=Products");
layout.cells("c").setText(LocaleTrans("Products-Projects")); 
Toolbar.clearAll();
Toolbar.addButton(UPD, 0, LocaleTrans("Update"), "img/icons8-edit-property-144.png", "img/icons8-edit-property-144.png");
Toolbar.addButton(VER, 1, LocaleTrans("Versions"), "img/icons8-versions-48.png", "img/icons8-versions-48.png");
Toolbar.addButton(DEL, 2, LocaleTrans("Delete"), "img/icons8-delete-document-144.png", "img/icons8-delete-document-144.png");
Toolbar.addButton(LISTDOCS, 3, LocaleTrans("List Docs"), "img/icons8-list-48.png", "img/icons8-list-48.png");
if (TBEvent!=null)
    Toolbar.detachEvent(TBEvent);
TBEvent=Toolbar.attachEvent("onClick", function(id)
    {         
    if (id==VER)
        MantVer();
    else if (id==LISTDOCS)
        ListDocs(ListGrid.getSelectedRowId());
    else
        MantProduct(id);    
    });
RefreshGrid("ListProducts");    
ListGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrProduct=rowId;    
    });
ListGrid.selectRow(0, true, false, true);    
}
//-----------------------------------------------------------------------
function ListDocs(IdFolder)
{
WinListDoc=myWins.createWindow({
id:"ListDocs",
left:20,
top:1,
width:900,
height:400,
center:true,
modal:true,
resize:true});  
WinListDoc.setText("List Docs");
var TBDocs=WinListDoc.attachToolbar();
TBDocs.addButton(ADDFOLD, 0, LocaleTrans("Add Fold"), "img/FoldAdd.png", "img/FoldAdd.png");
TBDocs.addButton(UPDFOLD, 1, LocaleTrans("Update Fold"), "img/FoldEdit.png", "img/FoldEdit.png");
TBDocs.addButton(DELFOLD, 2, LocaleTrans("Delete Fold"), "img/FoldDel.png", "img/FoldDel.png");
TBDocs.addSeparator(SEPAR, 3);
TBDocs.addButton(ADD, 4, LocaleTrans("Add"), "img/DocAdd.png", "img/DocAdd.png");
TBDocs.addButton(CHECKOUT, 5, LocaleTrans("CheckOut"), "img/checkout.png", "img/checkout.png");
TBDocs.addButton(UPD, 6, LocaleTrans("Update"), "img/DocEdit.png", "img/DocEdit.png");
TBDocs.addButton(CHECKIN, 7, LocaleTrans("CheckIn"), "img/checkin.png", "img/checkin.png");
TBDocs.addButton(CANCELCHECKOUT, 8, LocaleTrans("Cancel CheckOut"), "img/cancelcheckout.png", "img/cancelcheckout.png");
TBDocs.addButton(LISTVER, 9, LocaleTrans("List_Versions"), "img/ListVers.png", "img/ListVers.png");
TBDocs.addButton(DEL, 10, LocaleTrans("Delete"), "img/DocDel.png", "img/DocDel.png");
var DocsLay=WinListDoc.attachLayout("2U");
DocsLay.cells("a").setText("Folders")
DocsLay.cells("a").setWidth(200);
DocsLay.cells("b").setText("Documents")
FoldsTree=DocsLay.cells("a").attachTree();
DocsGrid=DocsLay.cells("b").attachGrid();
RefreshFoldsTree(IdFolder, FoldsTree, DocsGrid)
RefreshDocsGrid(IdFolder, DocsGrid)
TBDocs.attachEvent("onClick", function(id)
    {         
    if (id==ADDFOLD || id==UPDFOLD || id==DELFOLD)    
        MantFold(id);
    else
        MantDoc(id, DocsGrid.getSelectedRowId());
    });
}
//-----------------------------------------------------------------------
function MantFold(Oper)
{
if (Oper!=ADD && CurrFold==null)
    return;    
    
}
//-----------------------------------------------------------------------
function MantDoc(Oper, IdDoc)
{
if (Oper!=ADD && IdDoc==null)
    return;    
if (Oper==ADD)
    AddExtDoc();
else if (Oper==CHECKOUT)
    CheckOut(IdDoc);
else if (Oper==CHECKIN)
    CheckIn(IdDoc);
else if (Oper==CANCELCHECKOUT)
        CancelCheckOut(IdDoc);
else if (Oper==UPD)
    ModDoc(IdDoc, false, "");
else if (Oper==LISTVER)
    ListVer(IdDoc);
else if (Oper==DEL)
    DelDoc(IdDoc);
}
//-----------------------------------------------------------------------
function AddExtDoc()
{
var Url="AddDocExt";
WinMantDoc=myWins.createWindow({
id:"AddExtDoc",
left:20,
top:1,
width:750,
height:500,
center:true,
modal:true,
resize:true}); 
WinMantDoc.setText("OpenProdoc");
var LayoutFold=WinMantDoc.attachLayout('2E');
var a = LayoutFold.cells('a');
a.hideHeader();
a.setHeight(50);
var formCombo = a.attachForm();
formCombo.loadStruct('DocCombo?A=1');
var b = LayoutFold.cells('b');
b.hideHeader();
FormAddDoc = b.attachForm();  
formCombo.attachEvent("onChange", function(name, value, is_checked){
    FormAddDoc.unload();
    FormAddDoc = b.attachForm();
    CreaDocMain(Url, value);
    });
CreaDocMain(Url, "PD_DOCS");   
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
//                            if (MantFromGrid)
//                                GridResults.deleteRow(Doc2Del);
//                            else
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
function ListVer(Doc2List)
{
WinAF=myWins.createWindow({
id:"ListVerDoc",
left:20,
top:1,
width:800,
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
        var PartsId = GR.getSelectedRowId().split('%7C');     
        if (Order=="Data")  
            ModDoc(PartsId[0], true, PartsId[1]);
        }
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
//        if (MantFromGrid) 
//            {
//            for (i=0; i<GridResults.getColumnsNum(); i++)  
//                if (GridResults.getColType(i)=="rotxt")
//                    break;
//            Cell=GridResults.cellById(Doc2CheckOut, i);  
//            }
//        else
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
//                if (MantFromGrid) 
//                    {
//                    for (i=0; i<GridResults.getColumnsNum(); i++)  
//                        if (GridResults.getColType(i)=="rotxt")
//                            break;
//                    Cell=GridResults.cellById(Doc2CancelCheck, i);  
//                    }
//                else
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
//                            if (MantFromGrid) 
//                                {
//                                for (i=0; i<GridResults.getColumnsNum(); i++)  
//                                    if (GridResults.getColType(i)=="rotxt")
//                                        break;
//                                var Cell=GridResults.cellById(Doc2CheckIn, i);
//                                Cell.setValue(""); 
//                                } 
//                            else    
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
//----------------------------------
function CreaDocMain(Url, Type)
{
FormAddDoc.loadStruct(Url+"?F="+CurrFold+"&Ty="+Type, function(){
    FormAddDoc.setFocusOnFirstActive();
    });    
FormAddDoc.enableLiveValidation(true);    
FormAddDoc.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormAddDoc.send(Url, function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            if (!FormAddDoc.isItem("UpFile")) 
                                {
                                FormAddDoc.unload();
                                WinMantDoc.close();
                                }
                            else    
                                FormAddDoc.enableItem("UpFile"); 
                            }
                        });
        }
     else if (name==CANCEL) 
        {   
        FormAddDoc.unload();
        WinMantDoc.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormAddDoc, name.substring(2));    
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormAddDoc, name.substring(3));  
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormAddDoc, name.substring(2));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormAddDoc, name.substring(3)); 
    });   
FormAddDoc.attachEvent("onUploadFile",function(realName,serverName)
    {
    FormAddDoc.unload();
    WinMantDoc.close();
    RefreshDocsGrid(CurrFold, DocsGrid);
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
function RefreshFoldsTree(IdFolder, FoldsTree, DocsGrid)
{
FoldsTree.setImagePath("js/imgs/dhxtree_skyblue/");
FoldsTree.setXMLAutoLoading("FoldTree");
FoldsTree.load("FoldTree?FoldId="+IdFolder);
FoldsTree.showItemSign("System",true);
FoldsTree.selectItem(IdFolder);
CurrFold=IdFolder;
FoldsTree.attachEvent("onClick",function(id)
    {
    CurrFold=id;
    DocsGrid.clearAndLoad("DocList?FoldId="+CurrFold, function ()
        {
        if (DocsGrid.getRowsNum()>0)
            DocsGrid.selectRow(0, true, false, true);
        });
    });
    
}
//-----------------------------------------------------------------------
function RefreshDocsGrid(IdFolder, DocsGrid)
{
DocsGrid.clearAll(true); 
DocsGrid.setHeader(LocaleTrans("Document_Type")+","+LocaleTrans("Document_Title")+","+LocaleTrans("Document_Date")+","+LocaleTrans("Lock_user")+","+LocaleTrans("Date"));   //sets the headers of columns
DocsGrid.setColumnIds("Type,Title,Date,Lock,RepDate");         
DocsGrid.setInitWidths("120,250,100,80,*");   
DocsGrid.setColAlign("left,left,left,left,left");    
DocsGrid.setColTypes("ro,link,ro,ro,ro");            
DocsGrid.setColSorting("str,str,str,str,str"); 
DocsGrid.load("DocList?FoldId="+IdFolder);
DocsGrid.init();
DocsGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrDoc=rowId;    
    });
}
//-----------------------------------------------------------------------
function MantProduct(Oper)
{
if (ListGrid.getSelectedRowId()==null)
    return;
WinMant=myWins.createWindow({
id:"MantProducts",
left:20,
top:1,
width:600,
height:600,
center:true,
modal:true,
resize:true});  
WinMant.setText("Products Maintenance");
var FormMantProduct=WinMant.attachForm();
FormMantProduct.loadStruct("MantProducts?D="+ListGrid.getSelectedRowId()+"&Oper="+Oper, function(){
    FormMantProduct.setFocusOnFirstActive();
    });
FormMantProduct.enableLiveValidation(true);     
FormMantProduct.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormMantProduct.send("MantProducts", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            RefreshGrid("");    
                            FormMantProduct.unload();
                            WinMant.close();  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormMantProduct.unload();
        WinMant.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormMantProduct, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormMantProduct, name.substring(2));  
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormMantProduct, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormMantProduct, name.substring(3)); 
    });   
}
//-----------------------------------------------------------------------
function MantVer()
{
if (ListGrid.getSelectedRowId()==null)
    return;
WinVers=myWins.createWindow({
id:"MantVer",
left:20,
top:1,
width:840,
height:320,
center:true,
modal:true,
resize:true});  
WinVers.setText("Versions Maintenance: "+ListGrid.cellById(ListGrid.getSelectedRowId(), TITLE_COL).getValue());
var VersTB=WinVers.attachToolbar();
VersTB.addButton(ADD, 0, LocaleTrans("Add"), "img/icons8-insert-clip-40.png", "img/icons8-insert-clip-40.png");
VersTB.addButton(COPY, 1, LocaleTrans("Copy"), "img/icons8-copy-48.png", "img/icons8-copy-48.png");
VersTB.addButton(UPD, 2, LocaleTrans("Update"), "img/icons8-edit-property-144.png", "img/icons8-edit-property-144.png");
VersTB.addButton(DEL, 3, LocaleTrans("Delete"), "img/icons8-delete-document-144.png", "img/icons8-delete-document-144.png");
VersTB.addButton(LISTDOCS, 4, LocaleTrans("List Docs"), "img/icons8-list-48.png", "img/icons8-list-48.png");
VersTB.addButton(LISTISSUES, 5, LocaleTrans("List Issues"), "img/icons8-show-property-48.png", "img/icons8-show-property-48.png");
VersTB.addButton(DEPENDENCIES, 6, LocaleTrans("Manage Dependencies"), "img/icons8-related-companies-40.png", "img/icons8-related-companies-40.png");
VersTB.addButton(DEPENDTREE, 7, LocaleTrans("Dependencies Tree"), "img/icons8-tree-structure-40.png", "img/icons8-tree-structure-40.png");
VersTB.addButton(IMPACTTREE, 8, LocaleTrans("Impact Tree"), "img/icons8-tree-structure-40(1).png", "img/icons8-tree-structure-40(1).png");
VersTB.attachEvent("onClick", function(id)
    {         
    if (id==LISTDOCS)
        ListDocs(VersGrid.getSelectedRowId());
    else if (id==LISTISSUES)
        ListIssuesVer(VersGrid.getSelectedRowId())
    else if (id==DEPENDENCIES)
        ListDepend(VersGrid.getSelectedRowId());
    else if (id==DEPENDTREE)
        DependTree(VersGrid.getSelectedRowId());
    else if (id==IMPACTTREE)
        ImpactTree(VersGrid.getSelectedRowId());
    else
        MantVersions(id);
    });
VersGrid=WinVers.attachGrid(); 
RefreshVersGrid(ListGrid.getSelectedRowId());
}
//-----------------------------------------------------------------------
function ListIssuesVer(IdVers)
{
if (IdVers==null)
    return;
WinVers=myWins.createWindow({
id:"ListIssuesVer",
left:20,
top:1,
width:800,
height:400,
center:true,
modal:true,
resize:true});  
WinVers.setText("Issues Maintenance: "+VersGrid.cellById(VersGrid.getSelectedRowId(), 0).getValue());
var VersTB=WinVers.attachToolbar();
VersTB.addButton(ADD, 0, LocaleTrans("Add"), "img/icons8-insert-clip-40.png", "img/icons8-insert-clip-40.png");
VersTB.addButton(UPD, 1, LocaleTrans("Update"), "img/icons8-edit-property-144.png", "img/icons8-edit-property-144.png");
VersTB.addButton(DEL, 2, LocaleTrans("Delete"), "img/icons8-delete-document-144.png", "img/icons8-delete-document-144.png");
VersTB.attachEvent("onClick", function(id)
    {         
    MantIssue(id, IdVers);
    });
IssuesGrid=WinVers.attachGrid(); 
//RefreshIssuesGrid(VersGrid.getSelectedRowId());    
RefreshIssuesGrid(IdVers);    
}
//-----------------------------------------------------------------------
function DependTree(IdVers)
{
if (VersGrid.getSelectedRowId()==null)
    return;
WinTree=myWins.createWindow({
id:"WinTree",
left:20,
top:1,
width:800,
height:500,
center:true,
modal:true,
resize:true});  
WinTree.setText("Dependencies Tree: "+VersGrid.cellById(VersGrid.getSelectedRowId(), VERTITLE_COL).getValue());   
var L=WinTree.attachLayout("1C");
var DepTree=L.cells("a").attachTree();
L.cells("a").setText("Dependencies");
DepTree.setImagePath("js/imgs/dhxtree_skyblue/");
DepTree.load("DependTree?IdVers="+IdVers);
}
//-----------------------------------------------------------------------
function ImpactTree(IdVers)
{ 
if (VersGrid.getSelectedRowId()==null)
    return;
WinTree=myWins.createWindow({
id:"WinTree",
left:20,
top:1,
width:800,
height:500,
center:true,
modal:true,
resize:true});  
WinTree.setText("Impact Tree: "+VersGrid.cellById(VersGrid.getSelectedRowId(), VERTITLE_COL).getValue());   
var L=WinTree.attachLayout("1C");;
var DepTree=L.cells("a").attachTree();
L.cells("a").setText("Impact");
DepTree.setImagePath("js/imgs/dhxtree_skyblue/");
DepTree.load("ImpactTree?IdVers="+IdVers);
}
//-----------------------------------------------------------------------
function ListDepend(IdVers)
{
if (IdVers==null)
    return;
WinDep=myWins.createWindow({
id:"ListDepend",
left:20,
top:1,
width:600,
height:400,
center:true,
modal:true,
resize:true});  
WinDep.setText("Versions Maintenance: "+VersGrid.cellById(IdVers, 0).getValue());
var DepTB=WinDep.attachToolbar();
DepTB.addButton(ADD, 0, LocaleTrans("Add"), "img/icons8-insert-clip-40.png", "img/icons8-insert-clip-40.png");
DepTB.addButton(UPD, 1, LocaleTrans("Update"), "img/icons8-edit-property-144.png", "img/icons8-edit-property-144.png");
DepTB.addButton(DEL, 2, LocaleTrans("Delete"), "img/icons8-delete-document-144.png", "img/icons8-delete-document-144.png");
DepTB.attachEvent("onClick", function(id)
    {         
    MantDep(id, IdVers, DepGrid.getSelectedRowId());
    });
DepGrid=WinDep.attachGrid(); 
RefreshDepGrid(IdVers); 
}
//-----------------------------------------------------------------------
function MantDep(Oper, IdVers, IdRel)
{
if (IdRel==null && Oper!=ADD)
    return;
var WinMantDep=myWins.createWindow({
id:"MantDepend",
left:20,
top:1,
width:600,
height:(Oper==ADD?300:200),
center:true,
modal:true,
resize:false});  
WinMantDep.setText("Dependencies Maintenance");
var FormMantDepend;
if (Oper==ADD)
    {
    var LY=WinMantDep.attachLayout("2E");
    LY.cells('a').setText("Product Filter");
    LY.cells('a').setHeight(80);
    LY.cells('b').setText("Product Version Relationship");
    FormMantDepend=LY.cells('b').attachForm();
    }
else
    {
    FormMantDepend=WinMantDep.attachForm();
    }    
FormMantDepend.loadStruct("MantDepend?IdVers="+IdVers+"&Oper="+Oper+"&IdRel="+IdRel, function(){
    FormMantDepend.setFocusOnFirstActive();
    });
FormMantDepend.enableLiveValidation(true);   
if (Oper==ADD)
    {
    var FormFilterProd=LY.cells('a').attachForm();
    FormFilterProd.loadStruct("MantDependFilter?IdVers="+IdVers+"&Oper="+Oper, function(){
        FormFilterProd.setFocusOnFirstActive();
        });
    FormFilterProd.attachEvent("onChange", function (name, value)
        {
        if (name=="IdProd")
            {
            FormMantDepend.unload();
            FormMantDepend=LY.cells('b').attachForm();
            FormMantDepend.load("MantDepend?IdVers="+IdVers+"&Oper="+Oper+"&IdRel="+IdRel+"&IdProd="+value, function(){
                FormMantDepend.setFocusOnFirstActive();
                });
            FormMantDepend.attachEvent("onButtonClick", function (name)
            {if (name==OK)
                {   
                FormMantDepend.send("MantDepend", function(loader, response)
                                { // Asynchronous 
                                if (response.substring(0,2)!=OK)    
                                    alert(response); 
                                else
                                    {
                                    RefreshDepGrid(IdVers);     
                                    }
                                } );
                }
            FormMantDepend.unload();
            WinMantDep.close();  
            });           
            }
        });
    }
FormMantDepend.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormMantDepend.send("MantDepend", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            RefreshDepGrid(IdVers);     
                            }
                        } );
        }
    FormMantDepend.unload();
    WinMantDep.close();  
    });           
}
//-----------------------------------------------------------------------
function RefreshDepGrid(Idvers)
{
DepGrid.clearAll(true); 
DepGrid.setHeader("Product,Version,Relation");   
DepGrid.setColumnIds("Product,Version,Relation");         
DepGrid.setInitWidths("100,100,*");   
DepGrid.setColAlign("left,left,left");    
DepGrid.setColTypes("ro,ro,ro");            
DepGrid.setColSorting("str,str,str"); 
DepGrid.load("ListDep?IdVers="+Idvers);
DepGrid.init();  
}
//-----------------------------------------------------------------------
function MantVersions(Oper)
{
if (VersGrid.getSelectedRowId()==null && Oper!=ADD)
    return;
WinVers=myWins.createWindow({
id:"MantVersions",
left:20,
top:1,
width:680,
height:360,
center:true,
modal:true,
resize:true});  
WinVers.setText("Versions Maintenance");
var FormMantVers=WinVers.attachForm();
FormMantVers.loadStruct("MantVersions?D="+VersGrid.getSelectedRowId()+"&Oper="+Oper+"&Prod="+ListGrid.getSelectedRowId(), function(){
    FormMantVers.setFocusOnFirstActive();
    });
FormMantVers.enableLiveValidation(true);     
FormMantVers.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormMantVers.send("MantVersions", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            RefreshVersGrid(ListGrid.getSelectedRowId());    
                            FormMantVers.unload();
                            WinVers.close();  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormMantVers.unload();
        WinVers.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormMantVers, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormMantVers, name.substring(2));  
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormMantVers, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormMantVers, name.substring(3)); 
    });       
}
//-----------------------------------------------------------------------
function RefreshIssuesGrid(IdVers)
{
IssuesGrid.clearAll(true); 
IssuesGrid.setHeader("Code,Title,Env,Status,Criticity,DateOpen,DateClosed,Solver");   
IssuesGrid.setColumnIds("Code,Title,Env,Status,Criticity,DateOpen,DateClosed,Solver");         
IssuesGrid.setInitWidths("80,120,80,100,100,100,100,*");   
IssuesGrid.setColAlign("left,left,left,left,left,left,left,left");    
IssuesGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro");            
IssuesGrid.setColSorting("str,str,str,str,str,str,str,str"); 
IssuesGrid.load("ListIsuesVer?IdVers="+IdVers);
IssuesGrid.init();
IssuesGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrIssue=rowId;    
    });
}
//-----------------------------------------------------------------------
function RefreshVersGrid(IdProd)
{
VersGrid.clearAll(true); 
VersGrid.setHeader("Title,DateInit,DateSup,DateSupExt,Status,License,Notes");   
VersGrid.setColumnIds("Title,DateInit,DateSup,DateSupExt,Status,License,Notes");         
VersGrid.setInitWidths("140,80,80,80,50,80,*");   
VersGrid.setColAlign("left,left,left,left,center,left,left");    
VersGrid.setColTypes("ro,ro,ro,ro,img,ro,ro");            
VersGrid.setColSorting("str,str,str,str,str,str,str"); 
VersGrid.load("ListVers?IdProd="+IdProd);
VersGrid.init();
}
//-----------------------------------------------------------------------
function ShowListDepartments()
{
// TODO: Form Filter Departments        
layout.cells("b").setText(LocaleTrans("Departments Filter"));   
layout.cells("b").setHeight(220);
LoadFilterForm("FilterForm?Filt=Depart");
layout.cells("c").setText(LocaleTrans("Departments"));   
Toolbar.clearAll();
Toolbar.addButton(ADD, 0, LocaleTrans("Add"), "img/icons8-insert-clip-40.png", "img/icons8-insert-clip-40.png");
Toolbar.addButton(UPD, 1, LocaleTrans("Update"), "img/icons8-edit-property-144.png", "img/icons8-edit-property-144.png");
Toolbar.addButton(DEL, 2, LocaleTrans("Delete"), "img/icons8-delete-document-144.png", "img/icons8-delete-document-144.png");
Toolbar.addButton(ADDPROD, 3, LocaleTrans("Add Product"), "img/icons8-add-image-40.png", "img/icons8-add-image-40.png");
Toolbar.addButton(LISTDOCS, 4, LocaleTrans("List Docs"), "img/icons8-list-48.png", "img/icons8-list-48.png");
if (TBEvent!=null)
    Toolbar.detachEvent(TBEvent);
TBEvent=Toolbar.attachEvent("onClick", function(id)
    {         
    if (id==ADDPROD) 
        MantProduct(ADD);
    else if (id==LISTDOCS)
        ListDocs(ListGrid.getSelectedRowId());
    else
        MantDepartment(id);    
    });
RefreshGrid("ListDepartments");    
ListGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrDepartment=rowId;    
    });
ListGrid.selectRow(0, true, false, true);        
}
//-----------------------------------------------------------------------
function MantDepartment(Oper)
{
if (ListGrid.getSelectedRowId()==null && Oper!=ADD)
    return;
WinMant=myWins.createWindow({
id:"MantDepart",
left:20,
top:1,
width:600,
height:300,
center:true,
modal:true,
resize:false});  
WinMant.setText("Department Maintenance");
var FormMantDepart=WinMant.attachForm();
FormMantDepart.loadStruct("MantDepart?D="+CurrDepartment+"&Oper="+Oper, function(){
    FormMantDepart.setFocusOnFirstActive();
    });
FormMantDepart.enableLiveValidation(true);     
FormMantDepart.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormMantDepart.send("MantDepart", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            RefreshGrid("");    
                            FormMantDepart.unload();
                            WinMant.close();  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormMantDepart.unload();
        WinMant.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormMantDepart, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormMantDepart, name.substring(2));  
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormMantDepart, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormMantDepart, name.substring(3)); 
    });   
}
//-----------------------------------------------------------------------
function ShowListSoftCompanies()
{
// TODO: Form Filter Soft Companies        
layout.cells("b").setText(LocaleTrans("Soft Companies Filter"));   
layout.cells("b").setHeight(240);
LoadFilterForm("FilterForm?Filt=SoftProv");
layout.cells("c").setText(LocaleTrans("Soft Companies"));   
Toolbar.clearAll();
Toolbar.addButton(ADD, 0, LocaleTrans("Add"), "img/icons8-insert-clip-40.png", "img/icons8-insert-clip-40.png");
Toolbar.addButton(UPD, 1, LocaleTrans("Update"), "img/icons8-edit-property-144.png", "img/icons8-edit-property-144.png");
Toolbar.addButton(DEL, 2, LocaleTrans("Delete"), "img/icons8-delete-document-144.png", "img/icons8-delete-document-144.png");
Toolbar.addButton(ADDPROD, 3, LocaleTrans("Add Product"), "img/icons8-add-image-40.png", "img/icons8-add-image-40.png");
Toolbar.addButton(LISTDOCS, 4, LocaleTrans("List Docs"), "img/icons8-list-48.png", "img/icons8-list-48.png");
if (TBEvent!=null)
    Toolbar.detachEvent(TBEvent);
TBEvent=Toolbar.attachEvent("onClick", function(id)
    {       
    if (id==ADDPROD) 
        MantProduct(ADD);
    else if (id==LISTDOCS)
        ListDocs(ListGrid.getSelectedRowId());
    else
        MantSoftCompanies(id);    
    });
RefreshGrid("ListSoftCompanies");    
ListGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrSoftCompany=rowId;    
    });
ListGrid.selectRow(0, true, false, true);        
    
}
//-----------------------------------------------------------------------
function MantSoftCompanies(Oper)
{
if (ListGrid.getSelectedRowId()==null && Oper!=ADD)
    return;
WinMant=myWins.createWindow({
id:"MantSoftComp",
left:20,
top:1,
width:600,
height:360,
center:true,
modal:true,
resize:true});  
WinMant.setText("Software Companies Maintenance");
var FormMantSoftComp=WinMant.attachForm();
FormMantSoftComp.loadStruct("MantSoftComp?D="+CurrSoftCompany+"&Oper="+Oper, function(){
    FormMantSoftComp.setFocusOnFirstActive();
    });
FormMantSoftComp.enableLiveValidation(true);     
FormMantSoftComp.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormMantSoftComp.send("MantSoftComp", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            RefreshGrid("");    
                            FormMantSoftComp.unload();
                            WinMant.close();  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormMantSoftComp.unload();
        WinMant.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormMantSoftComp, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormMantSoftComp, name.substring(2));  
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormMantSoftComp, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormMantSoftComp, name.substring(3)); 
    });   
}
//-----------------------------------------------------------------------
function ShowListIssues()
{
// TODO: Form Filter Products    
layout.cells("b").setText(LocaleTrans("Issues Filter"));   
layout.cells("b").setHeight(300);
LoadFilterForm("FilterForm?Filt=Issues");
layout.cells("c").setText(LocaleTrans("Issues")); 
Toolbar.clearAll();
Toolbar.addButton(ADD, 0, LocaleTrans("Add"), "img/icons8-insert-clip-40.png", "img/icons8-insert-clip-40.png");
Toolbar.addButton(UPD, 1, LocaleTrans("Update"), "img/icons8-edit-property-144.png", "img/icons8-edit-property-144.png");
Toolbar.addButton(DEL, 2, LocaleTrans("Delete"), "img/icons8-delete-document-144.png", "img/icons8-delete-document-144.png");
Toolbar.addButton(LISTDOCS, 3, LocaleTrans("List Docs"), "img/icons8-list-48.png", "img/icons8-list-48.png");
if (TBEvent!=null)
    Toolbar.detachEvent(TBEvent);
TBEvent=Toolbar.attachEvent("onClick", function(idOper)
    {         
    if (idOper==LISTDOCS)
        ListDocs(ListGrid.getSelectedRowId());
    else
        {    
        MantIssue(idOper, null);  
        }
    });
RefreshGrid("ListIssues");    
ListGrid.attachEvent("onRowSelect",function(rowId,cellIndex)
    {
    CurrIssue=rowId;    
    });
ListGrid.selectRow(0, true, false, true);       
}
//-----------------------------------------------------------------------
function MantIssue(Oper, IdVers)
{
if (CurrIssue==null && Oper!=ADD)
    return;
WinMant=myWins.createWindow({
id:"MantIssue",
left:20,
top:1,
width:600,
height:((IdVers!=null&&Oper!=DEL)?650:700),
center:true,
modal:true,
resize:false});  
WinMant.setText("Issues Maintenance");
var FormMantIssue=WinMant.attachForm();
FormMantIssue.loadStruct("MantIssue?D="+CurrIssue+"&Oper="+Oper+"&IdVers="+IdVers, function(){
    FormMantIssue.setFocusOnFirstActive();
    });
FormMantIssue.enableLiveValidation(true);     
FormMantIssue.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        {   
        FormMantIssue.send("MantIssue", function(loader, response)
                        { // Asynchronous 
                        if (response.substring(0,2)!=OK)    
                            alert(response); 
                        else
                            {
                            if (IdVers!=null)
                                RefreshIssuesGrid(IdVers);
                            else
                                RefreshGrid("");    
                            FormMantIssue.unload();
                            WinMant.close();  
                            }
                        } );
        }
     else if (name==CANCEL) 
        {   
        FormMantIssue.unload();
        WinMant.close();
        }
    else if (name.substring(0,2)=="M_") 
        ShowMulti(FormMantIssue, name.substring(2));    
    else if (name.substring(0,2)=="T_") 
        ShowThes(FormMantIssue, name.substring(2));  
    else if (name.substring(0,3)=="MT_") 
        ShowMultiThes(FormMantIssue, name.substring(3));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FormMantIssue, name.substring(3)); 
    });   
}
//-----------------------------------------------------------------------
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
    });
}
//-----------------------------------------------------------------------
function RefreshGrid(TipeGrid) 
{
ListGrid.clearAll(true); 
var Filt="";
if (TipeGrid!="")
    CurrentGrid=TipeGrid;
if (CurrentGrid=="ListProducts")
    {  
    ListGrid.setHeader("Code,Title,Current Version,Department-Company,Family,License,Technology");   
    ListGrid.setColumnIds("Code,Title,Version,Department,Family,License,Technology");         
    ListGrid.setInitWidths("100,200,80,180,140,140,*");   
    ListGrid.setColAlign("left,left,left,left,left,left,left");    
    ListGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro");            
    ListGrid.setColSorting("str,str,str,str,str,str,str"); 
    }
else if (CurrentGrid=="ListDepartments")
    {
    ListGrid.setHeader("Title,Responsible,Description");   
    ListGrid.setColumnIds("Title,Responsible,Description");         
    ListGrid.setInitWidths("200,200,*");   
    ListGrid.setColAlign("left,left,left");    
    ListGrid.setColTypes("ro,ro,ro");            
    ListGrid.setColSorting("str,str,str"); 
    }    
else if (CurrentGrid=="ListSoftCompanies")
    {       
    ListGrid.setHeader("Title,Contact,Mail,Phone,Url,Description");   
    ListGrid.setColumnIds("Title,Contact,Mail,Phone,Url,Description");         
    ListGrid.setInitWidths("100,120,120,120,140,*");   
    ListGrid.setColAlign("left,left,left,left,left,left");    
    ListGrid.setColTypes("ro,ro,ro,ro,ro,ro");            
    ListGrid.setColSorting("str,str,str,str,str,str"); 
    }    
else if (CurrentGrid=="ListIssues")
    {       
    ListGrid.setHeader("Code,Title,Produc Version,Env,Status,Criticity,DateOpen,DateClosed,Solver");   
    ListGrid.setColumnIds("Code,Title,ProducVer,Env,Status,Criticity,DateOpen,DateClosed,Solver");         
    ListGrid.setInitWidths("100,160,120,120,140,140,100,100,*");   
    ListGrid.setColAlign("left,left,left,left,left,left,left,left,left");    
    ListGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro");            
    ListGrid.setColSorting("str,str,str,str,str,str,str,str,str"); 
    }    
FilterStr=getUpdFilter(CurrentGrid);
ListGrid.load(CurrentGrid+FilterStr);
ListGrid.init();
}
//----------------------------------
function getFiltFields(Grid2Upd)
{
    
if (Grid2Upd=="ListProducts")
    {
    if (FiltFieldsProd==null)
        FiltFieldsProd=["TH_Family","TH_License","TH_Technology"];
    return(FiltFieldsProd)    
    }
if (Grid2Upd=="ListIssues")
    {
    if (FiltFieldsIssue==null)
        FiltFieldsIssue=["Code","TH_Env","TH_IssueCrit","TH_IssueStatus","TH_IssueSolver"];
    return(FiltFieldsIssue)    
    }
if (Grid2Upd=="ListDepartments")  
    {
    if (FiltFieldsDep==null)
        FiltFieldsDep=["Title","Description","TH_Responsible"];
    return(FiltFieldsDep)    
    
    }
if (Grid2Upd=="ListSoftCompanies")  
    {
    if (FiltFieldsSoft==null)
        FiltFieldsSoft=["Title","Description","Contact","Url"];
    return(FiltFieldsSoft)    
    
    }
return(new Array());    
}
//----------------------------------
function getUpdFilter(Grid2Upd)
{
var FiltStr="";    
var Filt="";
var ListFields=getFiltFields(Grid2Upd);
for (i = 0; i < ListFields.length; i++) 
    {
    Filt=FilterForm.getItemValue(ListFields[i]); 
    if (Filt!=null && Filt!="")
        {
        if (FiltStr.length!=0)
            FiltStr+="&";
        if (ListFields[i].startsWith("TH_"))
           FiltStr+=ListFields[i].slice(3)+"=";
        else
           FiltStr+=ListFields[i]+"=";       
        FiltStr+=Filt;
        }
    }
if (FiltStr!="")
    FiltStr="?"+FiltStr;    
return(FiltStr);    
}
//----------------------------------
function LoadFilterForm(TipeForm)
{
var Url=TipeForm;    
if (FilterForm!=null)
    FilterForm.unload(); 
FilterForm=layout.cells("b").attachForm();
FilterForm.loadStruct(Url);   
FilterForm.attachEvent("onButtonClick", function (name)
    {if (name==OK)
        RefreshGrid("");
    else if (name.substring(0,2)=="T_") 
        ShowThes(FilterForm, name.substring(2));  
    else if (name.substring(0,3)=="TD_") 
        DelTerm(FilterForm, name.substring(3)); 
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
WinMulti.setText("Values of"+AttName);  
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


