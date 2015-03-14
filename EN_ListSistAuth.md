

&lt;H4&gt;

Authentication Systems List

&lt;/H4&gt;



&lt;hr&gt;


<p>By selecting the option to manage Authentication Systems, will appear the form "list of Authentication Systems". This form allows you to maintain all the Authentication Systems.</p>
<p>Each time a user logs on, their identity is checked by calling the system assigned to the user authentication. All users can use the same system or different systems can be defined for different user profiles.<br>
Regardless of the authentication system, the user must be registered in the system, since it must be assigned permissions and preferences and be a registered in groups and ACL, because otherwise you can not view or modify any documents. OPD system supports the types of authentication:</p>
<ul>
<blockquote><li><b>OPD</b>: OpenProdoc. In this mode, both the user and the password (encrypted) is stored along with the rest of the OPD data. Entered password is verified against the data stored in OPD.</li>
<li><b>DDBB</b>: Data Base. This form of authentication makes a connection to the database defined in url and entered the username and password to connect. If the DB accepts the connection, OPD recognized as authenticated the user.</li>
<li><b>LDAP</b>: The user is checked against the LDAP indicated in the url. If authentication against LDAP is correct, OPD recognized as authenticated the user.</li>
<li><b>OS</b>: Operative System. This form should only be used to boot server processes or for use in environments with highly controlled PCs. OPD recognizes as a valid user if your login name matches the current operating system user <b>without</b> checking the key.</li>
</ul>
<p>To filter the Authentication System or list of Authentication Systems you want to review, just enter part of the name in the text box and press "ok". The list of Authentication Systems that meet the conditions will be shown on the results table. Pressing the button without entering any value you see all the elements on which the user has permission. The results table shows the data:</p>
<ul>
<li>Identification name</li>
<li>Description of the Authentication System</li>
<li>Type of Authentication System</li>
<li>URL o reference to Authentication System</li>
<li>Additional Parameters (specific for some types)</li>
<li>Connection user to Authentication System (not used in the existing system, but useful for custom or future systems)</li>
<li>Connection password to Authentication System (not used in the existing system, but useful for custom or future systems)</li>
<li>Name of last user who modified the Authentication System</li>
<li>Date and time of last modification</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/ListSistAuth.jpg' /> </p>
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
<p>View: <a href='EN_MantSistAuth.md'>Authentication System Maintenance</a> and <a href='EN_ListUsers.md'>Users List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>