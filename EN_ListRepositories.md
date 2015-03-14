

&lt;H4&gt;

Repositories List

&lt;/H4&gt;



&lt;hr&gt;


<p>By selecting the option to manage Repositories, will appear the form "list of Repositories". This form allows you to maintain all the system Repositories.</p>
<p>All OPD documents are stored in a repository. You can use the same repository for all types of documents or create and assign different repositories for storing different types of documents, according to the needs of space, speed and cost of storage. OPD supports the types of repository:</p>
<ul>
<blockquote><li><b>FS</b>: FileSystem. Storage system in a folder accessible to Swing or Web clients.</li>
<li><b>BBDD</b>: Data Base. Storage in a table as BLOB attribute. Recommended for small files or types of documents with low volume.</li>
<li><b>FTP</b>: Storage on an ftp server. Recommended for types of documents with limited access.</li>
<li><b>REFURL</b>: Reference to documents/pages existing in Internet/Intranet. Allows to classify and catalog documents of interest without having them stored locally. (The documents are not deleted when you delete the record)</li>
</ul>
<p>When you define or create the repositories, you can encrypt them, so that even if a user to access the file system or database, can not view the documents unless is through OPD.</p>
<p>To filter the Repository or list of Repositories you want to review, just enter part of the name in the text box and press "ok". The list of Repositories that meet the conditions will be shown on the results table. Pressing the button without entering any value you see all the elements on which the user has permission. The results table shows the data:</p>
<ul>
<li>Identification name</li>
<li>Description of the Repository</li>
<li>Kind of Repository</li>
<li>URL or reference to the Repository</li>
<li>Additional parameters of the Repository (specific for every type)</li>
<li>Repository User. User with permissions on the repository to store or delete documents. Required for ftp or BLOB)</li>
<li>Repository Password</li>
<li>Flag indicating whether the repository is encrypted</li>
<li>Name of last user who modified the Repository</li>
<li>Date and time of last modification</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/ListRepositories.jpg' /> </p>
<p>This results table can be sorted by selecting the header of each column. You can also change the size of each column by dragging the separator line in the headers.</p>
<p>Above the table of results there are several buttons for performing operations on the selected item. The available operations are:</p>
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
<p>View <a href='EN_MantRepositories.md'>Repositories Maintenance</a> and <a href='EN_ListObjDef.md'>Object Definitions List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>