<%-- 
    Document   : index
    Created on : 02-mar-2019, 19:44:11
    Author     : jhierrot
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Software Managent OpenProdoc</title>
<script src="js/OpenProdoc.js" type="text/javascript"></script>
<script src="js/dhtmlx.js" type="text/javascript"/></script>
<link rel="shortcut icon" href="img/OpenProdoc.ico" type="image/x-icon">      
<link rel="STYLESHEET" type="text/css" href="js/dhtmlx.css">
<link rel="STYLESHEET" type="text/css" href="css/OpenProdoc.css">
<style>
html, body {
width: 100%;
height: 100%;
margin: 0px;
padding: 0px;
overflow: hidden;
}
</style>
</head>
<body>
<form action="Login"  method="post">
<table align="center"  width="300" class="OPDForm">
<tr><td>&nbsp</td></tr>
<tr><td><img src="img/Logo48.jpg"><H3>Software Managent OpenProdoc</H3></td></tr>
<tr><td>
<fieldset class="dhxform_fs"><legend class="fs_legend">Login OpenProdoc:</legend><table>
<tr><td width="100"><div class="dhxform_label dhxform_label_align_left" >User:</div></td>
<td><div class="dhxform_control"><input class="dhxform_textarea" type="text" name="User"></div></td></tr>
<tr><td><div class="dhxform_label dhxform_label_align_left">Password:</div></td>
<td><div class="dhxform_control"><input class="dhxform_textarea" type="password" name="Password"></div></td></tr>       
<tr><td></td><td><input  class="dhxform_btn_filler" type="submit" value="  Ok  "></td></tr>
</table></fieldset>
</td></tr>
</table>     
</form>
</body>
</html>
