<%-- 
    Document   : index
    Created on : 02-mar-2016, 19:44:11
    Author     : jhierrot
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--META http-equiv="refresh" content="0; URL=SMain"-->
        <title>OpenProdoc Web2</title>
    <!-- script src="js/Openprodoc.js" type="text/javascript"></script -->     
    <script src="js/dhtmlx.js" type="text/javascript"></script>     
    <link rel="STYLESHEET" type="text/css" href="js/dhtmlx.css"> 
    </head>
    <body>
    <style>
    html, body {
        width: 100%;      /*provides the correct work of a full-screen layout*/ 
        height: 100%;     /*provides the correct work of a full-screen layout*/
        overflow: hidden; /*hides the default body's space*/
        margin: 0px;      /*hides the body's scrolls*/
    }
    </style>
    <script type="text/javascript">
    var layout;
    var menu;
    var DocsTree;
    var DocsGrid;
    dhtmlxEvent( window,"load",function(){             
    layout = new dhtmlXLayoutObject(document.body,"3L");         
    layout.cells("a").setText("Folders Tree"); 
    layout.cells("a").setWidth(300);
    layout.cells("b").setText("Current Folder");   
    layout.cells("c").setText("Folder Documents");   
    menu = layout.attachMenu();
    //menu.setTopText("<b><I>OpenProdoc</I></b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
    menu.loadStruct("test/menu.xml");
    menu.attachEvent("onClick", function(id, zoneId, cas){
            printLog("<b>Menu onClick</b> id="+id+"<br>");
    });
    DocsGrid = layout.cells("c").attachGrid();
    DocsGrid.setHeader("Type,Title,Lock,Date");   //sets the headers of columns
    DocsGrid.setColumnIds("Type,Title,Lock,Date");         //sets the columns' ids
    DocsGrid.setInitWidths("100,*,60,80");   //sets the initial widths of columns
    DocsGrid.setColAlign("left,left,left,left");     //sets the alignment of columns
    DocsGrid.setColTypes("ro,ro,ro,ro");               //sets the types of columns
    DocsGrid.setColSorting("str,str,str,str");  //sets the sorting types of columns
    DocsGrid.load("test/grid.xml");
    DocsGrid.init();
     DocsGrid.attachEvent("onRowSelect",function(rowId,cellIndex){
    printLog("<b>Row </b> id="+rowId+"<br>");
    return(true);
    });
    DocsGrid.selectRow(0, true, false, true);
    DocsTree = layout.cells("a").attachTree();
    // DocsTree.enableTreeImages(false);
    DocsTree.setImagePath("js/imgs/dhxtree_skyblue/");
    DocsTree.load("test/tree.xml");
    DocsTree.showItemSign("System",true);
    DocsTree.attachEvent("onOpenStart", function(id, state){
    if (state==0)
        printLog("<b>Expand sin hijos</b> id="+id+"<br>");
    else
        {
        printLog("<b>Expand </b> id="+id+"<br>");
        return(true);
        }
    });
    DocsTree.attachEvent("onClick",function(id){
    printLog("<b>Tree onClick</b> id="+id+"<br>");
    });
    });   
    DocsTree.selectItem("RootFolder", true, false);
    //----------------------------------
    function printLog(text) 
    {
    //document.getElementById("ta").innerHTML = text+document.getElementById("ta").innerHTML;
    //menu.setTopText(text+"/"+DocsTree.getSelectedItemId()+">>"+DocsGrid.getSelectedRowId());
    layout.cells("b").attachHTMLString(text+"/"+DocsTree.getSelectedItemId()+">>"+DocsGrid.getSelectedRowId());
    }
    //----------------------------------

    </script>
    </body>
</html>
