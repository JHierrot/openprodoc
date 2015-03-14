

&lt;H4&gt;

ACL List

&lt;/H4&gt;



&lt;hr&gt;


<p>By selecting the option to manage ACL, will appear the form "list of ACL". This form allows you to maintain all the system ACLs.</p>
<p>The Access Control Lists (ACLs) are the core of security in OPD. The documents, folders, and most types of objects has an ACL assigned. Depending on the user or groups listed in the ACL and permissions assigned, the user can perform some operations or other.</p>
<p>An ACL is composed of:</p>
<ul>
<blockquote><li>Identification name</li>
<li>Description of the ACL</li>
<li>A list of groups and permissions associated with each group</li>
<li>A list of users and the permissions associated with each user</li>
</ul>
<p>When a user attempts to access an object, the system checks the ACL for that object, checking the users and groups that appear and assigning the maximum allowable. That is, if the user appears in the ACL with a permit, and also belongs to two groups, each with a different permit, permission it is assigned the highest of the three. If a user is not declared on an ACL, directly or indirectly as a user through one of the groups to which he belongs, he can not see the object, nor find, nor appear on any list</p>
<p>To filter the ACL or list of ACL you want to review, just enter part of the name in the text box and press "ok". The list of ACL that meet the conditions will be shown on the results table. Pressing the button without entering any value you see all the elements on which the user has permission. The results table shows the data:</p>
<ul>
<li>Identification name</li>
<li>Description of the ACL</li>
<li>Name of last user who modified the ACL</li>
<li>Date and time of last modification</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/ListACL.jpg' /> </p>
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
<p>View: <a href='EN_MantACL.md'>ACL Maintenance</a> y <a href='EN_ListGroups.md'>Groups List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>