

&lt;H4&gt;

Repositories Maintenace

&lt;/H4&gt;



&lt;hr&gt;


<p>When selected in the form <a href='EN_ListRepositories.md'>Repositories List</a> one of the options to add, delete, modify or copy the Repository will appear this form with different fields on or off.</p>
<p>This form contains the information:</p>
<ul>
<blockquote><li><b>Repository name</b>: Repository identifier whose value can not be repeated or changed once assigned. You can have a maximum length of 32 characters. (Eg "FS_Server1")</li>
<li><b>Description of the Repository</b>: Description that clearly identifies the repository. You can have a maximum length of 128 characters. (Eg, "File System on Server1 with 2 Teras")</li>
<li><b>Kind of Repository</b>: Indicates the type of repository. You can implement new types if needed. Currently available are:<br>
<blockquote><ul>
</blockquote><li><b>FS</b>: FileSystem. Storage system in a folder accessible to Swing or Web clients.</li>
<li><b>BBDD</b>: Data Base. Storage in a table as BLOB attribute. Recommended for small files or types of documents with low volume.</li>
<li><b>FTP</b>: Storage on an ftp server. Recommended for types of documents with limited access.</li>
<li><b>REFURL</b>: Reference to documents/pages existing in Internet/Intranet. Allows to classify and catalog documents of interest without having them stored locally. (The documents are not deleted when you delete the record)</li>
<blockquote></ul>
</blockquote></li>
<li><b>URL or reference to the Repository</b>:The URI or URL that references the repository. For FS will be a path accessible to the computer or computers that use it, in the case of database will be a JDBC url, in the case of FTP, name or ip address.</li>
<li><b>Additional parameters of the Repository (specific for every type)</b>: In the case of databases, the additional parameter is "driver; table_name". The rest does not require parameters.</li>
<li><b>Repository User.</b>: User required to connect to remote server, not required for FS.</li>
<li><b>Repository Password</b>: Password required to connect to remote server, not required for FS.</li>
<li><b>Flag indicating whether the repository is encrypted</b>: If this option is checked, documents will be encrypted when stored and decrypted when recover. This ensures that even if someone has access to documents through the disc, the documents are readable only through OPD. Keep in mind that this process introduces a small overhead and OPD do not use a sophisticated encryption system, so if there are highly confidential documents should be evaluated using additional measures.</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/MantRepositories.jpg' /> </p>
<p>View: <a href='EN_ListRepositories.md'>Repositories List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>