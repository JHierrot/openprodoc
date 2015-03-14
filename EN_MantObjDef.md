

&lt;H4&gt;

Objects Definition Maintenance

&lt;/H4&gt;



&lt;hr&gt;


<p>When selected in the form  <a href='EN_ListObjDef.md'>Objects Definition List</a> one of the options to add, delete, modify or copy the Object Definition will appear this form with different fields on or off.</p>
<p>The information is structured in 3 tabs. The first contains the document type definition, the second, the metadata assigned to this type, and the third one this type document metadata inherited from all ancestors. Metadata is the sum total of the type-specific metadata and inherited. In turn, all these metadata are transmitted to the descendants of the current types of documents.</p>
<p>The type definition consists of several stages. You must first create the base definition, metadata can be added later (in the second tab), and finally, after confirming the definition, create the type in the repository (via the Create button) to store documents of that type.</p>
<p>This form contains the information:</p>
<ul>
<blockquote><li><b>Type Name</b>: Object type identifier whose value can not be repeated or changed once assigned. You can have a maximum length of 32 characters. (Eg "Wills")</li>
<li><b>Type description</b>: Description that allows more information about the use and meaning of the object type. You can have a maximum length of 128 characters. (Eg, "Sentencing, appeals and other legal document")</li>
<li><b>ACL of the definition</b>: According to the ACL assigned, it will be possible for the user to modify the definition of the object (See: <a href='EN_ListACL.md'>ACL List</a>). This ACL does not refer to an ACL assigned to the documents of this type, which by default inherit the ACL of the folder in which they reside if they are not explicitly assign one.</li>
<li><b>Object Family (FOLDER or DOCUMENT)</b>: that is container / folder or document with metadata and a file / content. Never will be editable as selecting the parent type in the list is determined the value.</li>
<li><b>Parent Class</b>: "Parent" definition from which to inherit attributes (and on until the top level of the types "base" or PD_FOLDERS PD_DOCS). Never will be editable as selecting the parent type in the list determined the value.</li>
<li><b>Repository name</b>:Repository to store the documents of this type (see: <a href='EN_ListRepositories.md'>Repositories </a>)</li>
<li><b>Active</b>:Boolean flag indicating whether the object type is active. If it is not active can not create documents of this type, although you can do other operations, such as searching.</li>
<li>Boolean flag indicating whether to log the operations to add, delete, modify and query.</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/MantObjDef.jpg' /> </p>
<p>To keep the definition of a documentary type in the second tab you can add, delete or modify the definition of metadata <a href='EN_MantAttr.md'>Metadata Maintenance</a>. After creating the document type in the repository is not currently possible to change the definition.</p>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/MantObjDef2.jpg' /> </p>
<p>See: <a href='EN_ListObjDef.md'>Objects Definitions List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>