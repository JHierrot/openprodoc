

&lt;H4&gt;

ACL Maintenance

&lt;/H4&gt;



&lt;hr&gt;


<p>When selected in the form <a href='EN_ListACL.md'>ACL List</a> one of the options to add, delete, modify or copy the ACL will appear this form with different fields on or off.</p>
<p>This form contains the information:</p>
<ul>
<blockquote><li><b>ACL Name</b>: ACL identifier whose value can not be repeated or changed once assigned. You can have a maximum length of 32 characters. (Eg "Top_Secret_Docs")</li>
<li><b>ACL Description</b>: ACL Description for understanding and remembering the meaning of the security policy. You can have a maximum length of 128 characters. (Eg "Documents reserved for HR direction") </li>
<li><b>List of permissions associated with groups</b>: The Groups tab contains a list of permissions associated with each group. You can add, delete or modify the permissions associated with a group using the corresponding buttons.</li>
<li><b>List of permissions associated with users</b>: The Users tab contains a list of permissions associated with each user. You can add, delete or modify the permissions associated with a user through the appropriate button.</li>
</ul>
<p>During the creation of ACL can only be enter name and description. To add the permissions should be saved and modified later. </p>
<p>During the modification is not possible to change the name of the ACL, as it is the element that uniquely identifies it.</p>
<p>When you delete the ACL, all elements are disabled. Only appear to allow verification of information prior to deletion.</p>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/MantACL.jpg' /> </p>
<p> The permissions can have one of the values: </p>
<ul>
<li><b>READ</b>: Allows you to read the object</li>
<li><b>CATALOG</b>: Allows you to modify metadata item </li>
<li><b>VERSION</b>: Allows you to modify the metadata and the document </li>
<li><b>UPDATE</b>: Edit the metadata and the document without having to create version </li>
<li><b>DELETE</b>: To delete the item </li>
</ul>
<p>Values ​​are additive, so that each contains the previous one.</p>
<p> If a user or group does not appear in an ACL, he will not have any access to the item, not even will appear in a query.<br>
If a user appears explicitly with a permission in an ACL, but it belongs to one or more groups that have been assigned a different permission, the permission assigned is the greatest of all.</p>
<p>Ver: <a href='EN_ListACL.md'>ACL List</a>, <a href='EN_ListGroups.md'>Groups List</a> y <a href='EN_ListUsers.md'>Users List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>