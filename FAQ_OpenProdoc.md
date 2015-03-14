# FAQ #

## General Questions: ##

  * What is OpenProdoc?
  * OpenProdoc is a document management solution (http://en.wikipedia.org/wiki/Document_management) that enables features such as:
    * Ability to define types of folders / files to structure documents according to the organizational structure, the processes of the business, etc...
    * Define document types for each project or area
    * Store documents in a folder structure
    * Assign metadata to facilitate business use of the documents and subsequent search
    * Create multiple versions of documents
    * Share documents among groups of users, permitting or restricting access based on permissions

  * What are the system requirements for OpenProdoc?
  * Depending on the version:
    * The portable version runs on Windows, Linux and Mac, and only requires Java 1.5 or higher
    * Swing Client version also requires a database.
    * The web version requires installation on an application server and a database. Once installed it can be accessed from most browsers.

  * What does it cost?
  * The OpenProdoc software is completely free and can be used by individuals, institutions or companies.  As with any product, the total cost to be taken into consideration includes the necessary computers, licenses for other products where appropriate, storage, services, etc…

  * Is it difficult to install?
  * OpenProdoc is simple to install:
    * The portable version is distributed in a zip that can be decompressed in any folder on computer disk or a USB. It can be used in any of the supported systems (Linux, Windows, Mac).
    * The Swing version requires a database (such as MySQL, Derby, HSQLDB), the creation of a user in the database and then the installation of the OpenProdoc client.
    * The Web client is installed like any other J2EE application (WAR) in an application server (http://en.wikipedia.org/wiki/Application_server). In addition, the Swing client must be installed for management functions.

  * Are all versions the same?
  * The Portable version and Swing Client versions are functionally identical. The only difference is that the portable version includes embedded a HSQLDB database server (http://hsqldb.org/).  It doesn’t require the database server, but it only allows access by one user at a time. Multiple users can be defined and access the system one after other though. The Web version doesn’t include management functions, but the rest is the same as the other two. In all cases the engine is the same only the interface changes of course.

  * Does OpenProdoc include all the features of a commercial document management system?
  * Yes, although it is important to note that other products are structured differently. Some cover all the functions of the ECM world, others have strong products but they are separate for each function, some only cover their specialty (Capture, Document Management, BPM, etc..). OpenProdoc do not currently include features like BPM / Workflow and Collaboration. It has been developed with the goal of making it easy to use and effectively cover all core functions of document management without overloading it with functions used just occasionally.

  * Is OpenProdoc based on an existing document management product?
  * No, all development has been done from scratch, leading to its optimised design. Only some Apache libraries (http://www.apache.org/) are used for communication and ftp file upload in the J2EE environment.

  * Will it work on a small or old computer?
  * The CPU and memory requirements are VERY low. It is a much optimized application and the portable version can be used without any problems with less than 1G memory.

  * Why use a document management software (DMS) when the files can be saved to disk?
  * There are many reasons for not using a shared disk (for either an isolated user or a group):
    * Security: Not all users have access to all documents. In a shared disk it is difficult to limit access to archives. A DMS can limit permissions for each document individually, even when they are in the same folder.
    * Risk of virus: A shared folder facilitates the spread of viruses that can infect documents. In a DMS such as OpenProdoc documents can be encrypted and it is not necessary to share the storage folder, which reduces the risk of spreading a virus.
    * Structuring of information: In a folder structure only the folder name can be used to organize information.  In a DMS it is possible to create folders / files with an attributes structure in order to identify or locate the documents.
    * Description of documents: Similarly, a file system relies on the name and perhaps a text search with additional tools (like Google Desktop) for non-image formats. In a DMS it is possible to assign categories and metadata to each document to ease finding them regardless of the physical format.
    * Version control: In a DMS documents are handled consistently. Instead of having multiple disk files with names like "Report v 1.0.doc", Report v 1.1.doc"... documents have a type with a name such as "Report 2012" in the current version, which is much more clear. You can always recover any previous version, but the default is always to deal with the latest version.
    * Concurrency: When several people intend to work on a document, it can be locked, avoiding inadvertent overwriting someone else's work.
    * Publication: Through an application server, multiple users can access documents without installing a product or sharing folders.

  * Why another document manager?
  * Indeed there are many (even more than when I started development). But I think there are several shortcomings with other products. With OpenProdoc I have tried to make a product that will avoid the shortcomings of existing products as it is focused on the core functions of a document management system, avoiding functions infrequently used that overload the system. The highlights of OpenProdoc are:
    * Document Model. The OpenProdoc data model is highly optimized to avoid limitations in the model found in some products. Some of the problems that are solved in OpenProdoc are:
    * [Inheritance](http://en.wikipedia.org/wiki/Inheritance_%28computer_science%29) and [Polymorphism](http://en.wikipedia.org/wiki/Polymorphism_%28computer_science%29): Many products have no inheritance or polymorphism, which is almost essential to simplify the search, support documents of various kinds or evolve classification. In a company of medium / large size is almost impossible to make a complete classification scheme, it is necessary to establish a base model and gradually modify (through subtypes). Of course the products that have a fixed structure are only applicable to limited and specific scenarios.
    * Integrity: In some products, given the data model used to store the metadata, you cannot enforce relational integrity for metadata.
    * Uniqueness: Similarly, it is not always possible to define unique keys for specific metadata (eg file codes, input registers, etc.). Forcing complex developments or allowing inconsistencies.
    * Optimization: Due to the structure of some data models, you cannot create indexes to optimize queries, limiting scalability
    * Performance and scalability. The architecture is designed with scalability in mind, with a model that is comparable in some ways to the Risc CPU, i.e. a small set of highly optimized instructions that combine to allow the functions required to run quickly. The code is constructed using strong inheritance and polymorphism both in logic and in the user interface. The result is:
      * The "weight": The engine with all the functionality occupies less than 1 M, so it can be used on any computer, consumes few resources and is fast.
      * The architecture is open and ready for cloud. Actually there is not a single server, there is an engine that can be deployed in distributed instances in N servers. In fact the portable version embeds the engine and the HSQLDB database server.
      * Speed: Tests with an application developed using the API of the product that starts 10 threads accessing a local server, creates 1,000 folders, inserts 10,000 documents, deletes them (moves to trash) and purges the documents run in less than 3 minutes.
      * The engine can be embedded in any product that handles documents.
      * In addition to storing several types of repositories (there are systems that can store only File System, or just as blob) connectors can be easily created to store documents in different systems (email, WebDav, etc..).
    * Simplicity: The solution is very simple to use and set up, but allows an expert user to take advantage of the functionality.
      * Management: In some systems it is very difficult to define document types, you have to manipulate XML files and know the product fairly. The same goes for different elements (users, profiles, repositories ...). In this product the administration is very homogeneous, all elements are similarly defined in an easy way.
      * Also use of some products is complex for some users. In OpenProdoc the model is similar to using a file structure on disk.
      * Related to the above, the fact that it is cross-platform and portable allows it to be used anywhere and without installation (via USB for example). Also you can use the Web client with the Java Swing client.
      * Adaptable to the user: Each user / group can have a different interface in a different language. It can thus be arranged in a low vision accessible interface (not AAA because it requires JavaScript). This enables its use also in a model of Software As A Service where each company has its logo, interface users, etc…
    * Security:
      * Granularity: In many other products, the administration is "all or nothing". In OpenProdoc it is possible to create profiles and assign administrative roles to each profile so that for example a user (Documentary / archivist) can only create document types, a security responsible defines ldap instances, groups and users and a technical user defines repositories and warehousing. You can even delegate the administration of a single group of users to a person. In another scenario, the administrator of an “outsourced” company can allow an internal user to manage some aspects of client users, reserving the administration more "technical".
      * Authentication against multiple systems: Each user can be validated in different ways, even with several different LDAPs or validating users against the system or DB, which facilitates integration.
      * Encryption: Although with a simple system, documents can be encrypted before being stored in the repository, providing additional security without additional software.

## Functions: ##

  * Can I define document types according to my needs?
  * Yes. With the Swing client, which is also the management client, you can define any number of document types. Each document type is defined as a subtype of an existing one. By default there is a basic type of folder and a basic type of document with a minimum of metadata. All types are subtypes of documents and are created as a subtype of the basic type or other subtype. Metadata and behaviour are inherited, i.e. a document type is the sum of all the attributes of each of its predecessors, in an object-oriented model.

  * Why the documents types are object oriented?
  * It has several advantages:
    * Allows a gradual evolution of the model. It is very complex to define a document structure for a large organization with several departments. Expecting to have a full classification scheme can delay the startup. Additionally, it is usual to evolve the model for organizational and legal reasons.
    * It's more powerful, because you can look for a document or a type and its subtypes (e.g. Search by criteria "Reports" or "Reports" and all its subtypes). This also applies to other operations defined.
    * It is simpler and avoids having to define a "flat structure", an analysis phase that generates a hierarchical structure. Metadata or properties defined for a type are inherited for subtypes.
    * Is a more "real" representation because the definitions and types of documents are not fully independent.

  * Can you exchange OpenProdoc documents and their metadata with other users?
  * Yes, Just export the document, or part of the folder structure in OpenProdoc to disk. The export operation dumps file to disk and also generates a ".OPD" containing the metadata assigned. If you exported a structure (e.g. a complete folder) it will generate the corresponding folders on disk with their metadata as an ".OPD" file. In the destination system you can import the compete document.  For a correct import, the document type must exist on the target computer. Type definitions can be exchanged between users or environments, exporting and importing the definition as a ".OPD".

  * Can definitions be exchanged between environments or with other institutions?
  * Yes, just export the desired items with the thick client (as OPD files) and import them into the target system. It should be noted that there may be dependencies and the import must be ordered. (e.g. a group can contain other groups and those groups must previously be imported, so the “parent” group don’t detect inconsistencies)

  * If you have a complete project structure and files in a file system on the local disk is it possible to import it at once?
  * Yes, you can import a folder tree within a folder in OpenProdoc, recreating the existing structure on the same disk. You should select the destination document type (the same for all documents) that logically shouldn’t have mandatory metadata. The title of the document will be the file name, and the date the file’s date. The permissions (ACL) will be inherited from the folder in which it is inserted. Once imported, you can later change the metadata or the security of each of the elements.

  * Does OpenProdoc manage thesauri?
  * Yes (from version 0.8). Multiple thesauri can be created structured according to your needs. Each thesaurus can be used as a complete thesaurus (NT, BT, RT, UF) or simply as a list of contents or a list of terms, which can be hierarchical. The metadata of the types of documents can be associated with a concrete thesauri, so as to choose the value of the metadata within in the thesaurus and check integrity. For example you can define a metadata "Country" to be validated against a list of countries or a metadata "Subject of the report" is validated against a thesaurus of subjects.

  * Can thesauri be imported or exported?
  * Yes (from version 0.8). It uses the standard SKOS-RDF. However, as in any "standard", it should be noted that there are various interpretations and uses (http://lab.usgin.org/book/usgin-skos-vocabulary-service-profile-home/examples-skos-concept-encoding), so although efforts have been made to support different interpretations, it cannot ensure 100% compatibility with thesauri generated outside OpenProdoc. If during the import of a thesaurus a problem arises, it will traced and log as error. Additionally, OpenProdoc don’t use poly-hierarchical thesaurus, so that as the second RT relationship is imported as BT, the model will not be equal.

  * Does include functions BPM?
  * No. As of version 0.8 include the ability to define and assign simple processes (which can be programmed), but not a full BPM.

  * Does include Collaboration functions?
  * No. It is not in the near roadmap.

  * Does include Capture functions?
  * No, although they will be incorporated in later versions. However you can import documents generated by Abby FineReader or Kofax Capture.

  * What are for the “reference repositories” (REFURL, REFFS)?
  * They allow to catalog and include documents that are not stored in OpenProdoc but in other systems or the Web. This eliminates the need to include a local copy. You can catalog and reference document URLs that can be searched and opened like "local". When deleted from OpenProdoc, there is simply deleted the cataloging and reference, keeping the content in its original place.

  * Is it possible to mix in a folder/file "normal" documents and referenced?
  * Yes. This allows, for example, a folder with documents of a project or person and also to include references to laws, internal regulations or bibliography to supplement the information.

## User Management: ##

  * What information is needed to create a user?
  * You must enter at least:
    * The short name / login,
    * The authentication system (choice of some of the systems defined by the administrator)
    * The role (choice of some of the systems defined by the administrator)
    * The customization (choice of some of those defined by the administrator)

  * What are the customizations?
  * They are sets of elements that determine the appearance of OpenProdoc clients. You can define any number of customizations. The customization includes the display language, date format and appearance of the interface. For the Swing client this is limited to character fonts (both to suit the tastes and for visually impaired people). For the Web client, each customization includes CSS and JavaScript, so the appearance of screens and even behaviour at events can be changed greatly (although JavaScript methods and basic behaviour should continue to work as expected).

  * If the user authenticates against another location, such as LDAP, you need to create it?
  * Yes, it must be created, and be indicated, among other information, how to authenticate.

  * What is the advantage of authenticating against another system when you need to create a user?
  * The advantage is that the user does not have to remember multiple passwords or synchronize if the rules of the institution require the password to change periodically. It also simplifies administration work.

  * It’s possible to deactivate a user?
  * Yes, that allows interim deletes and avoids losing information because for some time there will be documents created or modified by the user. This makes it advisable to have tracking user data.

  * Can synchronize LDAP users or groups?
  * No. In any case, assigning users or groups to ACLs always require manual operation, and the fact that users replicate doesn’t implies that they can access the documents. If they are not defined in any ACL, they will not have access to documents that have that ACL.

  * What is the advantage of including groups within groups?
  * It greatly simplifies the maintenance of users and groups. In an institution with a hierarchical structure by different criteria (as departments and geographical) is almost essential. Otherwise you need to create large numbers of user groups and assign each user to many groups (eg London Users, UK Users, Europe Users, World Users). If nested groups are available, the user is assigned to one or two groups which in turn are contained in other groups according to a structure (eg, the user is assigned to the group London, which is already contained in the remaining groups in a 'geographical' structure).

  * Why multiple authentication modes coexist?
  * For ease of use and for allowing adaptation to multiple scenarios OpenProdoc offer different ways to authenticate (verify identity) of users. In a large institution, it is common to have a centralized LDAP for authentication and defining users, however it makes no sense to require installation in a small organization to use only OpenProdoc. In that case you can use the OpenProdoc "native" authentication. Moreover it is common for users to have and email account or to use available cloud services. In that case you can use authentication against a mail server. On the other hand, you may have developed processes and integration modules with applications running with generic user (or application). In that case, instead of creating fictitious users can use an authentication based on the user logged on the computer. This same alternative can be used in controlled corporate environments where the user of the equipment is fixed by administrators. Another additional option is that users are defined in a database. In that case you can choose to validate against the database. Finally, in many cases all these scenarios occur together. For example a school may have permanent staff employees (who will be defined in different systems), students (who are not employed and have no “system user” but may need to share documents and maybe got and email account) and provisional teachers (which may require access for a limited time).

## Security: ##

  * What is the security model in OpenProdoc?
  * The model is based on the usual model of ACL (Access Control Lists), i.e. permission sets with an identification name, assigned to the various elements (documents, folders, groups, definitions). Each ACL contains a list of groups or users, indicating the permission assigned to each.
    * Ex: ACL: Private Documents
    * Administrators Group: Delete Allowed
    * Directors group: Write allowed.
  * If a user or group does not appear in an ACL, he does not have any permission on the ACL objects assigned. The elements even don’t appear in searches or listings for the user.
  * If a user or group is referenced several times in an ACL (because it is contained in another group that also appears in the ACL), have the highest permissions all references.
  * The permissions are always checked by OpenProdoc engine when the user attempts to perform the operation. So, if a user attempts to delete a document when only have write permissions, he cannot delete it. If he try to change the definition of a document type without have permission, he cannot do it.

  * What are the Roles for?
  * Roles are a supplement to the ACL and another way of approaching security. They refer to the ability to perform groups of operations. Each role includes a name and the ability to perform various operations (create users, delete users, create document types, delete document types, etc.). Each user has a unique role assigned and may perform functions permitted to that role. The interface clients Swing and Web adapts to roles (if a user is not able to manage users never appear that option in the menu).

  * How are structured the Permissions?
  * From the viewpoint of the ACL, basically an element can be readable, modifiable and erasable. Permissions are additive and each level includes the previous ones. From the point of view of the roles, the options are: create items and modify or delete items. It has been structured in this way because the change in the definition of a group or a document type, is equivalent to delete it, since you can completely alter its essence and content.

  * Is it possible to delegate management of OpenProdoc to other users?
  * Yes, it's one of the points that have been more emphasis on design. For starters, there is no concept of “main” administrator or “root”. The default defined “root” has essentially no function that cannot be assigned to another user. Any user can perform administrative functions provided the permissions set in the Role permit them. If a user has in his role the function "Add User" activated, he can create users. This allows you to create profiles/roles grouped by areas. So it is possible to create a role of "Documentalist" who is allowed to create and maintain definitions of the types of documents and files / folders, the role of "Head of Security" that can maintain users, groups and ACL. By default there is an “Administrator” role that can administer all the elements. You can assign this role to any user or create separate roles. Additionally, sometimes it is not enough and we need to work distributed by region, branch offices or departments. In that case you can also use ACL assigned to definitions. You can define a user group for each delegation and assign an ACL to each group so that different users with Security Officer Role could only keep the group they belong to. Another complementary option is to allow a user to insert but not modify or delete elements. So, if necessary you can create a user or an additional element in an emergency, but not modify existing ones.
  * All this facilitates OpenProdoc used as SaaS, allowing local managers to each customer entity with bounded functions. Similarmente, una empresa que gestiona la explotación y administración de servidores otra puede cederle parte de las funciones de administración. Similarly, a company that manages the operation and administration of servers of another company can give to the customer employees part of the management functions.

  * How can be managed "Guests" users?
  * There are several elements to look for in user "guest", understood as a provisional or temporary user, usually without permission. This usually presents different problems:
    * Who would create the user? (It can be made by any user that in his role has permissions to add users, as discussed in the previous section. You do not need to "bother" a central administrator; it’s possible to assign permissions to users or area managers groups to (only) create users.
    * Where is authenticated? (Sometimes users are authenticated against an entity Ldap or other central servers and must pass a complex process and register with various systems. In OpenProdoc you can be defined a user to authenticate against OpenProdoc, although the rest of the users authenticate against a central LDAP, or use a different guest Ldap)
    * How to ensure that the guest user do not change anything? (Using roles. It can be defined roles without modification permissions of any kind, so even though the guest can see public documents, he cannot change anything)
    * How to limit access to the elements? (Through the ACLs. You can include the user in the selected groups and ACLs so he can access and, if necessary, modify the selected documentation)
    * How to access public documents? (By default there is a group which includes all users automatically, so that the documents with an ACL containing that group will be available)
    * What if they need to generate documents? (When you create a user automatically creates a home folder for the user and a user ACL so that it can handle documents in a controlled manner without further operation)

  * Is it possible to save documents to disk encrypted so they can not be seen even if you have access to the folder?
  * Yes, if the repository is defined as encrypted, documents will be stored encrypted. This can be useful when using the portable version on a USB installed, so that in case of losing the USB, information is not disseminated. However it should be noted that the encryption system used is simple and fast, so if you want to store documentation really sensitive, it’s recommend using a specialized product (or build an adapter to make encryption more sophisticated).


## Architecture: ##

  * Why several types of repository?
  * Because each type has its advantages and can be used according to the needs of each project, department or documentary. Storing in a file system is the most common and simple also when stored in SAN / NAS, allows the use of multiple servers. Blob storage is slower however for small files (eg SMS or Twitter) may be effective. Finally the ftp can be used as slower storage for backup, disaster recovery, second level, access, etc.

  * Is it possible to develop a new type of connector to repository (eg for a legacy system)?
  * Yes, it's pretty easy, you just have to implement a minimum set of methods (basically: connect, insert file, recover file, delete file and disconnect).

  * Is it scalable?
  * The scalability is only limited by the scalability of the database. The engine is embedded in each client, a client Swing or J2EE application, and has a cache of objects to ease the access to the database server. So it is possible to increase the number of J2EE servers or increased the power of these. For thick client users, each user has his own local engine. Each document type can be stored in a separate repository if desired, so that the storage and retrieval of documents can also be grown as needed.

  * Why to include thick client and not web only?
  * Additionally to allowing a portable document management system, a thick client is faster and more efficient than a thin client, uses less bandwidth and allows operations such as import or export folder structures. Although you can argue that with AJAX and HTML 5 can be achieved similar results, development is much more expensive, depends on the supported browser versions it and the result is not as comfortable. The main problem with a thick client is installation, but in the case of OpenProdoc, it’s not required.

## Development: ##

  * How can you develop with OpenProdoc?
  * Swing Simply download the Swing client (or the portable version) and add the jar in the lib folder included in the classpath of the project to develop. It is also necessary to include the configuration file created after installation.

  * Besides OpenProdoc jar, what is needed?
  * It’s required the jar listed in the "about" the product. Basically, ftp client, log4j for log and file upload in J2EE environment, all of Apache.

  * What version to use?
  * It is recommended to always use one of the "stable" branches of the development, discharging the existing thick client and using the included jar. Although generally are committed coherent sets to the repository, developing elements may have undetected problems or still not fully covered functions. It is not recommended to download the code from the development version for use in a development, or for other uses (such as educational use).

  * Supports CMIS?
  * No. Although there are plans to support in future versions.

  * Supports Webdav?
  * No. It is not in the roadmap.

  * How can I collaborate with the project?
  * In addition to talking about the project if deemed a good product ;-), you can collaborate currently three ways:
    * Translating into other languages (currently covered Castilian, English and translated into Portuguese only program without manuals).
    * Defining alternative CSS (current examples are "manifestly improvable") for the web version.
    * Defining specialized packages for specific areas or sectors. These packages may contain definitions of types of documents, folders / files, permissions / ACL, user groups and roles. So that usars can found a specialized package that can be imported and start work without wasting time parameterizing, because it would have required common definitions. For example a package for law firms or schools.
  * Collaboration and package received will be published on the downloads page, with reference to the author.
  * Currently I cannot offer any compensation for the collaboration.


## Licensing and Support: ##

  * Can be used in a development or integrated into an application?
  * Yes, provided it respects the license (GPL3). Simplifying, It can be used in another open source product (open source). For use in a commercial project, it is possible to agree on a specific license as OpenProdoc is a Multilicense product.

  * Is there support?
  * Currently there is no formal support in place but there is an issues page (https://docs.google.com/spreadsheet/viewform?formkey=dFF6ZndKWXFUQnJ0MWtVZWdUWk10X2c6MQ) which is currently free where you can open an incident that will responded as soon as possible.
