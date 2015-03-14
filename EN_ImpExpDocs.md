

&lt;H4&gt;

Export and Import Documents

&lt;/H4&gt;




&lt;hr&gt;


<p>When you want to import or export a <b>single</b> document, use the options Documents-> Import or Documents-> Export </p>
<p>The option <b>Export</b>, exports the current document with all its metadata. The metadata is exported in opd format (an XML text file with UTF-8) and the document in its original format. </p>
<p>OPD presents a dialog box to choose the directory where you store both the document and the metadata. </p>
<p>Should be noted that to avoid unwanted effects on import, does not feed definitions to export. Therefore, the definitions of elements such as document type (with metadata correspondence), ACL, mime type, etc.. must exist at the time of import (in the current system or at a different location). To export the definitions should be used explicitly maintenance functions of the administration screens. </p>
<p>The option <b>Import</b> load a document with all its metadata in the current folder. OPD presents a dialog box to choose the file of type".Opd" where metadata is stored and which contains the file reference to the document. At the time of import must exist the following definitions: </p>
<ul>
<li><b>Document Type</b>: Although not necessarily be identical, must contain at least the same metadata.</li>
<li><b>ACL</b>: Similarly should exist but does not have to match the definition.</li>
<li><b>Mime Type</b>: Similarly must exist but need not exactly match the definition.</li>
</ul>
<p>During the import, will ignored the following values: </p>
<ul>
<li><b>Modification User</b>: will be assigned the user performing the import.</li>
<li><b>Modification Date</b>: will be assigned the current date.</li>
<li><b>Repository</b>: will be assigned and use the documentation for the type at the time of importation.</li>
<li><b>Unique Identifier (PDID)</b>: will be assigned a new ID.</li>
<li><b>Folder</b>: current folder will be assigned at the time of importation.</li>
</ul>


&lt;hr&gt;


[Help Index OpenProdoc](EN_HelpIndex.md)