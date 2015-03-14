

&lt;H4&gt;

Install OpenProdoc

&lt;/H4&gt;



&lt;hr&gt;


<p>In order to install OpenProdoc you should follow the next steps:</p>
<ol>
<blockquote><li><a href='#1'>Create an user in the database</a></li>
<li><a href='#2'>Uncompress OpenProdoc files</a></li>
<li><a href='#3'>Install OpenProdoc Swing Client for administration</a></li>
<li><a href='#4'>Create data OPD data structure</a></li>
<li><a href='#5'>Install any other client required</a></li>
</ol>
<h4>1 - Create an user in the database</h4>
<p>
First, should be selected the kind of system to store all the OPD structure and metadata of the documents.<br>
On the selected database, after install it if is a new installation, it should be created an user with the permissions:</p>
<ul>
<li>Create, delete and alter table</li>
<li>Create, delete and alter indexes</li>
<li>Insert, delete and update records on the tables</li>
<li>and of course, connect and search.</li>
</ul>
<h4>2 - Uncompress OpenProdoc files</h4>
<p>It should be uncompresed the zip files in a the selectd folder of the computer, with the folders structure of the zip file.</p></blockquote>

<h4>3 - Install OpenProdoc Swing Client for administration</h4>
<p>This OPD client must be installed at least one time for administration and additionally, be installed for the users requiring a thick client.<br>
It's necessary to start the shell script <b>Install.sh</b> or <b>Install.bat</b> (Depending of the operative System) and fill the data asked in the form.</p>
<p>The program will assign the language of the computer if it's supported in the version, otherwise  will be used english (EN). It's possible to force a language, using as parameter the two characters language code when starting the application (IE. Install.sh EN). View <a href='EN_Language.md'>Language</a></p>
<ul>
<blockquote><li><b>DDBB connection user</b>: Name of the user used to connect to the DDBB and created in the previous step. (required)</li>
<li><b>BBDD user password</b>: Password of the user in the DDBB. (required)</li>
<li><b>DDBB server url</b>: Url in JDBC format of the database server (IE.:<i>"jdbc:mysql://127.0.0.1:3306/Prodoc", "jdbc:derby://localhost:1527/Prodoc"</i>). (required)</li>
<li><b>Kind of conection</b>: Kind of connection to use. (In this version only JDBC, in the future, other systems as non SQL DDBB). (required)</li>
<li><b>Java class to use</b>: Name of the classes used for connection (JDBC) (IE.:<i>"org.apache.derby.jdbc.ClientDriver", "com.mysql.jdbc.Driver"</i>). (required)</li>
<li><b>Aditional Classpath</b>: Classpath where the connection classes and any required library is stored besides the folder ".\lib". The classpath must include the path to the JDBC driver libraries.</li>
</ul>
<p>With the previous data, the install program will create a config file (Prodoc.properties) and a script Prodoc.sh or Prodoc.bat depending on Operative System.<br>
This files can be copied and used in any computer with the conditions: </p>
<ul>
<li>The computers have connection to the DDBB</li>
<li>The computers have the same classpath and drivers installed.</li>
</ul>
<p>otherwise it will be necessary to use the install program or change manually the configuration file (Prodoc.properties).</p>
<p>IMPORTANT: For the instalation and for normal operation, it's important than the DDBB server (or at least the session "DDBB connection user" used to connect OPD) be in mode "AutoCommit=off", that is, there is no information commited or canceled while OPD not send an order "commit transaction" o "cancel transaction".</p></blockquote>

<h4>4 - Create data OPD data structure</h4>
<p>After the instalation of the Swing Client, it must be started the script for creation of the data structure <b>Setup.sh</b> or <b>Setup.bat</b> (Depending of the operative System), filling the form displayed by the application.</p>
<p>The program will assign the language of the computer if it's supported in the version, otherwise  will be used english (EN). It's possible to force a language, using as parameter the two characters language code when starting the application (IE. Setup.sh ES). View <a href='EN_Language.md'>Language</a></p>
<ul>
<blockquote><li><b>Administrator Password</b>: Password for user administrator "root" created during installation. It is possible to define several users with several administrator permissions, "root" is only the default administrator. (required)</li>
<li><b>Default Language Code</b>: ISO code two character for language for the default customization. (IE: PT, ES, EN) (required)</li>
<li><b>Timestamp Format</b>: Format to display and read timestamp fields in default customization, according to formats of the Java formatter. (IE. "yyyy-MM-dd HH:mm:ss" )(required)</li>
<li><b>Date Format</b>: Format to display and read date fields in default customization, according to formats of the Java formatter. (IE. "dd/MM/yyyy" )(required)</li>
<li><b>Main key</b>: Main key used to encrypt several elements in OPD, including document repositories without native support for encription. It should contain characters with lower and upper case and numbers, with a size big enough to offer a reasonable security. (required)</li>
<li><b>Repository Name</b>: Descriptive name of the default documents repository. Later it will be possible to create additional repositories with different characteristics. (required)</li>
<li><b>Encrypted Repository</b>: Boolean value, when checked, the documents in the repository will be encrypted.</li>
<li><b>Url Repositorio</b>: IP or URI of the repository (depending on the kind of repository can be a local mounted path, a database, and ftp, etc..). It must be visible using the same name(URI) for any "client"(that is installation of a Web o Swing Client.(required)</li>
<li><b>Repository User</b>: User with permissions for the repository.(required depending on Repository type)</li>
<li><b>Repository Password</b>: Password of the User with permissions for the repository. Both fields will be empty if the repository is a Filesystem. (required depending on Repository type)</li>
<li><b>Repository Type</b>: One of the supported Repository Types  (required)<br>
<blockquote><ul>
<blockquote><li>FS: Filesystem (can be a mounted disk or server) identified by a path</li>
<li>FTP: Ftp Server</li>
<li>BLOB: Storing of documents as BLOB field in a Database that can be the same used to store metadata or another one.</li>
</blockquote></ul>
</blockquote><li><b>-Additional parameters</b>: Additional parameters that can be different depending on the repository type (required depending on Repository type). For BLOB it is:<br>
"JDBC Driver name;Table Name"<br>
Ie.:<br>
<blockquote>com.mysql.jdbc.Driver;TableBlob</li>
<br>
<br>
Unknown end tag for </ul><br>
<br>
<br>
<p>This proccess will create all the tables structure and elements necessary for OPD. Later, it will be possible to define new document types or folders, creating the required elements.</p>
<h4>5 - Instalación resto de clientes</h4>
<p>
After the creation of the core structure, it's necessary to install OPD clients for the users.<br>
If the access for all the users is through the Web Client, it is necessary to install the Web Client in a J2EE server. This client is deployed as a WAR application. The process for install the WAR aplication can be different for every J2EE server.<br>
After the installation of the War application, it's necessary to edit the configuration file: <b>Prodoc.properties</b> (that will use the Web client) with the <b>Install</b> program or copying and editing an existing properties file.<p>
<p>As a last step, in the home directory of the user (Ie.: /home/J_Smith) with which it's executed the J2EE server it must be copied the file <b>OPDWeb.properties</b>, editing the line that contains the path to <b>Prodoc.properties</b></p></blockquote></blockquote>

<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index</a>
