

&lt;H4&gt;

Authentication Systems Maintenance

&lt;/H4&gt;



&lt;hr&gt;


<p>When selected in the form <a href='EN_ListSistAuth.md'>Authentication Systems List</a> one of the options to add, delete, modify or copy the Authentication System will appear this form with different fields on or off.</p>
<p>This form contains the information:</p>
<ul>
<blockquote><li><b>Authentication System Name</b>: Authentication system identifier whose value can not be repeated or changed once assigned. You can have a maximum length of 32 characters. (Eg "Corporate_LDAP")</li>
<li><b>Authentication System Description</b>: escription for understanding the authentication system. You can have a maximum length of 128 characters. (Eg "LDAP Delegation Brazil")</li>
<li><b>Type of Authentication System</b>: Indicates the type of authentication system. You can implement new types if needed. Currently available are:<br>
<blockquote><ul>
<li><b>OPD</b>: OpenProdoc. In this mode, both the user and the password (encrypted) is stored along with the rest of the OPD data. Entered password is verified against the data stored in OPD.</li>
<li><b>DDBB</b>: Data Base. This form of authentication makes a connection to the database defined in url and entered the username and password to connect. If the DB accepts the connection, OPD recognized as authenticated the user.</li>
<li><b>LDAP</b>: The user is checked against the LDAP indicated in the url. If authentication against LDAP is correct, OPD recognized as authenticated the user.</li>
<li><b>OS</b>: Operative System. This form should only be used to boot server processes or for use in environments with highly controlled PCs. OPD recognizes as a valid user if your login name matches the current operating system user <b>without</b> checking the key.</li>
</ul>
</li>
</blockquote><li><b>URL o reference to Authentication System</b>:The URI or URL reference to the authentication system. It is necessary to LDAP and DB.</li>
<li><b>Additional Parameters (specific for some types)</b>: In the case of DB is the jdbc driver ,in the case of LDAP is a parameter that could be used in a function call such as: <i> "env.put(Context.SECURITY_PRINCIPAL,"cn = "+User+" "+getParam())"</i>.</li>
<li><b>Connection user to Authentication System</b>: User need to connect to the system. Not currently used.</li>
<li><b>Connection password to Authentication System </b>: password required to connect to the system. Not currently used.</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/MantSistAuth.jpg' /> </p>
<p>Ver: <a href='EN_ListSistAuth.md'>Authentication Systems List</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>