

&lt;H4&gt;

Roles List

&lt;/H4&gt;



&lt;hr&gt;


<p>By selecting the option to manage Roles, will appear the form "list of Roles". This form allows you to maintain all the system Roles.</p>
<p>The role allows you to set permissions for a user to perform activities on types of items. Permits are complementary and additional to the ACL.<br>
Users are assigned a role, indicating what kind of operations can be performed. Although authorized to perform these operations, access to each particular item depends on the object's ACL.<br>
That is, if a user can not modify groups, you can not even try to modify one. If you have permission to change groups, you can change one and not others depending on the specific ACL in each group.<br>
This allows to distribute the work delegating different tasks in different people according to their profiles. Thus, a user with knowledge of document management can keep the definitions of the types of documents and other user may be responsible for maintaining the security of users and groups (or even a single group) to avoid collisions.<br>
A user query may have limited all permits to ensure that errors in the definition of ACL does not grant permission unexpected </p>
<p> The permits included in each role are:</p>
<ul>
<blockquote><li><b>AllowCreateUser</b>: Enables you to create a user but not to modify existing users</li>
<li><b>AllowMaintainUser</b>: Allows you to modify and delete users</li>
<li><b>AllowCreateGroup</b>: Enables you to create a group but not to modify existing groups</li>
<li><b>AllowMaintainGroup</b>: Allows you to modify and delete groups</li>
<li><b>AllowCreateAcl</b>: Enables you to create an ACL but not modify existing ACLs</li>
<li><b>AllowMaintainAcl</b>: Allows you to modify and delete ACL</li>
<li><b>AllowCreateRole</b>: Enables you to create a role but not modify existing roles</li>
<li><b>AllowMaintainRole</b>: Allows you to modify and delete roles</li>
<li><b>AllowCreateObject</b>: Enables you to create an object definition but not modify existing definitions</li>
<li><b>AllowMaintainObject</b>: Allows you to modify and delete the object definitions</li>
<li><b>AllowCreateRepos</b>: Enables you to create a repository but not modify existing repositories</li>
<li><b>AllowMaintainRepos</b>: Allows you to modify and delete repositories</li>
<li><b>AllowCreateFolder</b>: Enables you to create a folder but not to modify existing foldesr</li>
<li><b>AllowMaintainFolder</b>: Allows to modify and delete folders</li>
<li><b>AllowCreateDoc</b>: Enables you to create a document but not to modify existing documents</li>
<li><b>AllowMaintainDoc</b>: Allows you to modify and delete documents</li>
<li><b>AllowCreateMime</b>: Enables you to create a mime type but not modify existing mime types</li>
<li><b>AllowMaintainMime</b>: Allows you to modify and delete mime types</li>
<li><b>AllowCreateAuth</b>: Enables you to create an authentication system but not modify existing systems</li>
<li><b>AllowMaintainAuth</b>: Allows you to modify and delete authentication systems</li>
<li><b>AllowCreateCustom</b>: Enables you to create a user customization user but not to modify existing ones</li>
<li><b>AllowMaintainCustom</b>: Allows you to modify and delete user customizations</li>
</ul>
<p>To filter the Role or list of Roles you want to review, just enter part of the name in the text box and press "ok". The list of Roles that meet the conditions will be shown on the results table. Pressing the button without entering any value you see all the elements on which the user has permission. The results table shows the data:</p>
<ul>
<li>Identification name</li>
<li>Description of the Role</li>
<li>Assigned permissions</li>
<li>Name of last user who modified the Role</li>
<li>Date and time of last modification</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/ListRoles.jpg' /> </p>
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
<p> View <a href='EN_MantRoles.md'>Roles Maintenance</a> and <a href='EN_ListUsers.md'>Users List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>