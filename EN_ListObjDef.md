

&lt;H4&gt;

Object Definitions List

&lt;/H4&gt;



&lt;hr&gt;


<p>By selecting the option to manage Object Definitions, will appear the form "list of Object Definitions". This form allows you to maintain all the system Object Definitions.</p>
<p>In OPD we can define different types of folders (containers) and types of documents to model the documentary structure of the organization. From basic types of objects (or PD_FOLDERS PD_DOCS) it is possible create an unlimited number of subtypes each of whom will inherit the metadata defined in "father" types.<br>
To handle or search for documents we can consider independently each type or subtypes can be included in the operation.</p>
<p>The folders or containers can also be defined with different metadata and with a hierarchical structure that reflects the desired model.</p>
<p>To filter object definitions that you want to review, just enter part of the name in the text box and press the button. The list of object definitions that meet the conditions appear in the results table.<br>
<blockquote>Pressing the button without entering any value you see all the elements on which the user has permission. The results table shows the data:</p>
<ul>
<li>Identification name</li>
<li>Object Type (DOCUMENT or FOLDER), ie container or document with metadata and a file / content</li>
<li>Description of the Object Definition</li>
<li>Boolean flag indicating whether the object type is active</li>
<li>ACL of the object definition. According to the ACL, it will be possible for the user to modify the definition of the object (View <a href='EN_ListACL.md'>ACL</a>)</li>
<li>"parent" Definition from which inherit attributes (and on until the top level of the types "base" PD_FOLDERS or PD_DOCS)</li>
<li>Repository to store the documents of this type (View <a href='EN_ListRepositories.md'>Repositories</a>)</li>
<li>Boolean flag indicating whether to register the operations of Add, Delete, Update and Search</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/ListObjDef.jpg' /> </p>
<p>This results table can be sorted by selecting the header of each column. You can also change the size of each column by dragging the separator line in the headers.</p>
<p>Above the table of results there are several buttons for performing operations on the selected item.</p>
<p><b>ALWAYS</b> should be selected an item, even to create a new type as the selected item is taken as the parent class of the new type documentary. If a folder is selected, the new type is a folder, if a document, the new type is a document. The available operations are:</p>
<ul>
<li>Add a new element</li>
<li>Delete the selected element (if it is not used in some document or folder)</li>
<li>Modify the selected element</li>
<li>Create a new item as a copy of the selected item</li>
<li>Export the selected item</li>
<li>Export all items listed</li>
<li>Import from file one or more previously saved items</li>
</ul>
<p>It should be noted on export and import, that some elements may have dependency on others, so you must export all related and imported at the time of it in the proper order.</p>
<p>In the event of an error (lack of user permissions, data inconsistency, etc.), the operation is canceled and will present the reason for the error to the user.</p>
<p>View <a href='EN_MantObjDef.md'>Objects Definition Maintenance</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>