

&lt;H4&gt;

Import and Export Folders

&lt;/H4&gt;




&lt;hr&gt;


<p> When you want to import or export an entire structure of folders, use the Options Folders-> Import or Folders-> Export </p>
<p> The Option <b>Export</b> will export OPD <b>current</b> folder and creates a file system folder structure equivalent to that in OPD. Before starting the export, OPD will present a screen to define different characteristics of the export. The possible options and their effects are: </p>
<ul>
<li><b>One level</b>: If you select this option, only the selected folder will be exported, otherwise, OPD will continue recursively until all the structure go below the current folder. </li>
<li><b>Include documents</b>: If you select this option, the export documents include the contents of each of the folders. Otherwise, just export your folders. </li>
<li><b>Include Metadata</b>: If you select this option, the export will generate the same level and with the same name as the element, a file ". opd" with the object's metadata (folder or document ). Otherwise, just is created in the file system the folder or document. </li>
<li><b>The destination folder</b> indicates the file system folder under which to create the structure of folders and documents. </li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/Export.jpg' /> </p>
<p>Export only include the documents for which the user has access permissions, therefore should not be assumed that the entire tree has exported unless the user has access permissions to all branches and documents.</p>
<p>After the export, OPD will present a summary containing the number of folders exported and the number of documents. If an error occurs, a message will be presented with the reason for the error.</p>
<p>Should be noted that to avoid unwanted effects on import, OPD does not include definitions during export. Therefore, the definitions of elements such as document type (with metadata correspondence), ACL, mime type, etc.. must exist at the time of import (in the current system or at a different location). To export the definitions should be used explicitly maintenance functions of the administration screens.</p>
<p>The Option <b>Import</b> imports a structure of the filesystem in the <b>current</b> OPD folder and creates a folder structure equivalent to that in the file system. Before starting the imortación, OPD will display a screen where to define different characteristics of the import. The possible options and their effects are:</p>
<ul>
<li><b>One level</b>: If you select this option, only the selected folder will be imported, otherwise, OPD will continue recursively to go all the structure below the current folder.</li>
<li><b>Include documents</b>: If you select this option, import the documents included in each of the folders. In another case, only folders will be imported.</li>
<li><b>Include Metadata</b>: If you select this option, the import will use, at the same level and with the same name as the element, an ". opd" file with the object's metadata (folder or document). Otherwise, will be used file name as a title and file date as the date of the document.</li>
<li><b>Folder Type:</b> If you do not import metadata (by choice or because it is a file system that does not contain them), this parameter indicates the type of folder to be assigned to folders imported. The type can not be include mandatory metadata, since the import will fail.</li>
<li><b>Document Type:</b> If you do not import metadata (by choice or because it is a file system that does not contain them), this parameter indicates the document type  to be assigned to imported documents. The type can not be include mandatory metadata, since the import will fail.</li>
<li><b>source folder:</b> indicates the file system folder from which you will start to import the structure of folders and documents. </li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/Import.jpg' /> </p>
<p>When ends importing, OPD will present a summary the number of folders containing imported and the number of documents. If an error occurs, a message will be presented with the reason for the error. </p>
<p>At the time of import, should exist the definitions of: </p>
<ul>
<li><b>Documents Types and folder types</b>: Although not necessarily be identical, must contain at least the same metadata. </li>
<li><b>ACLs</b>: Similarly must exist but need not match the definition. </li>
<li><b>Mime Type</b>: Similarly must exist but need not exactly match the definition. </li>
</ul>
<p> During import, OPD will ignore the following values: </p>
<ul>
<li><b>Modification User</b>: will be assigned the user performing the import. </li>
<li><b>Modification Date</b>: will be assigned the current date. </li>
<li><b>Repository</b>: will be assigned and use the repository that corresponds to the document type at the time of importation. </li>
<li><b>Unique Identifier (PDID)</b>: will be assigned a new ID. </li>
<li><b>Folder</b>: current folder will be assigned at the time of importation. </li>
</ul>


&lt;hr&gt;


[Help Index OpenProdoc](EN_HelpIndex.md)