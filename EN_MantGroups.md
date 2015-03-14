

&lt;H4&gt;

Groups Maintenance

&lt;/H4&gt;



&lt;hr&gt;


<p>When selected in the form <a href='EN_ListGroups.md'>Groups List</a> one of the options to add, delete, modify or copy the Group will appear this form with different fields on or off.</p>
<p>This form contains the information:</p>
<ul>
<blockquote><li><b>Group Name</b>: Group identifier whose value can not be repeated or changed once assigned. You can have a maximum length of 32 characters. (Eg "Auditors")</li>
<li><b>Group Description</b>: Group description for understanding and remembering the meaning of the group. You can have a maximum length of 128 characters. (Eg, "External auditors reviewing the QA processes")</li>
<li><b>Group ACL</b>: Security policy associated with the group that allows access restriction. This allows user to assign a group management role but limited to a few specific groups. For example you can create a role "administrator of delegations" and create different groups for each delegation, each with an ACL</li>
<li><b>List of Groups</b>: The groups tab contains a list of the groups contained in the current group. You can add or delete groups using the corresponding buttons.</li>
<li><b>List of Users</b>: The users tab contains a list of users contained in the current group. You can add or delete users through the appropriate button.</li>
</ul>
<p>During the creation of Group can only be enter name and description. To add users or groups should be saved and modified later.</p>
<p>During the modification is not possible to change the name of the Group, as it is the element that uniquely identifies it.</p>
<p>When you delete the Group, all elements are disabled. Only appear to allow verification of information prior to deletion.</p>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/MantGroups.jpg' /> </p>
<p>Should be noted that a group can contains other groups and so on indefinitely, so that it can be structured the representation of the organization. You can also include a group in several groups, so that for example can be a group "Consultants in Cambridge" and included in the groups "Cambridge" and "Consultants".<br>
<blockquote>Similarly you can create a group "Cambridge Managers" is included in the groups "Managers" and "Cambridge". This allows you to assign permissions and access to the necessary elements in a simple and flexible way.</p>
<p>View <a href='EN_ListGroups.md'>Groups List</a> y <a href='EN_ListUsers.md'>Users List</a> </p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>