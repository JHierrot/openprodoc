

&lt;H4&gt;

Instalación OpenProdoc

&lt;/H4&gt;



&lt;hr&gt;


<p>Se describe a continuación los distintos pasos de instalación de Prodoc.<br>
Los pasos a a seguir son:</p>
<ol>
<blockquote><li><a href='#1'>Creación de usuario en Base de datos</a></li>
<li><a href='#2'>Descompresión de archivos de OpenProdoc</a></li>
<li><a href='#3'>Instalación Cliente ProdocSwing</a></li>
<li><a href='#4'>Creación estructura de datos</a></li>
<li><a href='#5'>Instalación resto de clientes</a></li>
</ol>
<h4>1 - Creación de usuario en Base de datos</h4>
<p>
Primero debe elegirse la forma de almacenamiento de toda la información de metadatos a almacenar.<br>
Para ello debe elegirse el sistema de base de datos a utilizar y crearse un usuario en el mismo con capacidad de:</p>
<ul>
</blockquote><blockquote><li>Creación, modificación y borrado de Tablas</li>
<li>Creación, modificación y borrado de Índices</li>
<li>Creación, modificación y borrado de Registros</li>
<li>y por supuesto conexión y consulta.</li>
</ul>
<h4>2 - Descompresión OpenProdoc</h4>
<p>Debe descomprimirse el archivo zip en el lugar del disco deseado, manteniendo la estructura de carpetas interna.</p></blockquote>

<h4>3 - Instalación Cliente OpenProdoc Swing</h4>
<p>Este cliente debe instalarse al menos una vez para la administración del sistema y además para todos los usuarios que utilicen esta forma de acceso.<br>
<blockquote>Para ello debe iniciar el script de sistema <b>Install.sh</b> o <b>Install.bat</b> (según el sistema operativo) y cumplimentar los datos requeridos en pantalla.</p>
<p>En el caso del Linux, dependiendo de la configuración y modelo de seguridad, puede ser necesario cambiar los permisos de Install.sh o de Setup.sh (una vez creado) asignando permisos de ejecución: "chmod 777 Install.sh"</p>
<p>Por defecto se elegirá el idioma del equipo, siempre que esté soportado, en otro caso se utilizará inglés (EN). Si se desea forzar otro idioma puede pasarse como parámetro de dos letras a la aplicación (Ej. Install.sh EN). Ver <a href='ES_Language.md'>Idiomas</a></p>
<ul>
</blockquote><blockquote><li><b>Usuario de conexión a BBDD</b>: Identificación del usuario de BBDD creado en el apartado anterior. (requerido)</li>
<li><b>Clave usuario BBDD</b>: Clave del usuario de BBDD. (requerido)</li>
<li><b>Url del servidor de BBDD</b>: Url en formato JDBC del servidor de BBDD (Ej.:<i>"jdbc:mysql://127.0.0.1:3306/Prodoc", "jdbc:derby://localhost:1527/Prodoc", "jdbc:oracle:thin:@127.0.0.1:1521:Prodoc"</i>). (requerido)</li>
<li><b>Tipo de conexión</b>: Indicar el tipo de conexión a utilizar. (Actualmente solo soportado JDBC). (requerido)</li>
<li><b>Clases java a utilizar</b>: Indicar las clases de conexión (JDBC) a utilizar (Ej.:<i>"org.apache.derby.jdbc.ClientDriver", "com.mysql.jdbc.Driver"</i>). (requerido)</li>
<li><b>Classpath adicional</b>: Localización de las clases adicionales a las instaladas en la carpeta ".\lib". Debe especificarse el classpath de las clases del driver JDBC.</li>
</ul>
<p>Con los datos suministrados, el programa de instalación creará un fichero de configuración y un script de arranque Prodoc.sh o Prodoc.bat según el sistema operativo.<br>
Estos ficheros pueden copiarse y usarse en cualquier equipo siempre que: </p>
<ul>
<li>Los equipos se conecten directamente a la BBDD y no a través de ProdocWeb y</li>
<li>La configuración relativa al classpath sea la misma</li>
</ul>
<p>en otro caso debe utilizase el programa de instalación o modificar manualmente el archivo de configuración.</p>
<p>IMPORTANTE: Tanto para la instalación como para el funcionamiento normal, es importante que el servidor de BBDD (o al menos la sesión "Usuario de conexión a BBDD" con la que se conecta OPD) funcione en modo AutoCommit=off, es decir, hasta que no se confirme o cancele la transacción por parte de OPD no se debe consolidarse información alguna a la BBDD</p></blockquote>

<h4>4 - Creación estructura de datos</h4>
<p>Una vez instalado el cliente, debe ejecutarse la aplicación de creación de la estructura de datos <b>Setup.sh</b> o <b>Setup.bat</b>, contestando a las preguntas de la aplicación.</p>
<p>Por defecto se elegirá el idioma del equipo, siempre que esté soportado, en otro caso se utilizará inglés (EN). Si se desea forzar otro idioma puede pasarse como parámetro de dos letras a la aplicación (Ej. Setup.sh EN). Ver <a href='ES_Language.md'>Idiomas</a></p>

> <li><b>Password Administrador</b>: Clave de acceso del usuario administrador "root" creado al instalar. Otros usuarios pueden tener los mismos permisos, "root" se crea por defecto</li>
> <li><b>Código del Lenguaje por defecto</b>: Código ISO del lenguaje por defecto para la parametrización por defecto.</li>
> <li><b>Formato Fecha/Hora</b>: Formato de presentación y lectura de los campos fecha-hora según formatter java. (Ej. yyyy-MM-dd HH:mm:ss )</li>
> <li><b>Formato Fecha</b>: Formato de presentación y lectura de los campos fecha según formatter java. (Ej. dd/MM/yyyy )</li>
> <li><b>Clave Principal</b>: Clave principal de encriptación del servidor para los repositorios que no soporten encriptación de forma nativa. Debe mezclar mayúsculas, minúsculas y números con un tamaño razonablemente grande.</li>
> <li><b>Nombre del Repositorio</b>: Nombre identificativo del Repositorio de documentos por defecto. Podrá crearse posteriomente otros adicionales.</li>
> <li><b>Repositorio Encriptado</b>: Indica si el repositorio de archivos estará encriptado</li>
> <li><b>Url Repositorio</b>: IP o localización del repositorio. Debe ser visible e igual para todos los clientes que no se conecten a través del servidor J2EE.</li>
> <li><b>Repository_User</b>: Usuario con permisos de acceso al repositorio de archivos</li>
> <li><b>Password usuario Repositorio</b>: Clave del Usuario con permisos de acceso al repositorio de archivos. Ambos campos estarán vacios si se trata de un sistema de archivos.</li>
> <li><b>Tipo de Repositorio</b>: Tipo de repositorio<br>
<blockquote><ul>
<blockquote><li>FS: Sistema de archivos indicado por un path</li>
<li>FTP: Servidor ftp</li>
<li>BLOB: Almacenamiento como campos BLOD en una BBDD que puede ser la misma o diferente a aquella en que se almacenan los metadatos.</li>
</blockquote></ul>
<li><b>-Parámetros adicionales del Repositorio</b>: Parámetros adicionales que variarán según el tipo de repositorio. En el caso de BLOB, deberá ser:<br>
"Nombre del driver;Nombre tabla"<br>
Ej.:<br>
com.mysql.jdbc.Driver;TablaBlob</li></blockquote></li></ul>

<p>Este proceso creará toda la estructura de tablas y elementos necesaria para OPD. Posteriormente, al definir nuevos tipos de Documento o Carpeta se crearán tablas y elementos auxiliares.</p>
<h4>5 - Instalación resto de clientes</h4>
<p>
Una vez creada la estructura básica, deberá instalarse los clientes de los usuarios.
Si el acceso para todos los usuarios es a través del cliente Web, o si hay usuarios que utilizarán conexión cliente pesado servidor Web, deberá instalarse la aplicación J2EE. Para ello, en un servidor J2EE soportado deberá desplegarse el WAR de la aplicación.
A continuación debe configurarse el fichero de configuración <b>Prodoc.properties</b> que utilizará la aplicación J2EE con la herramienta Install o copiando un properties ya creado.<p>
<p>Por ultimo, en el directorio home (ej.: /home/J_Smith) del usuario con que se ejecuta el servidor J2EE debe copiarse el archivo <b>OPDWeb.properties</b>, modificando la entrada para que referencie la ubicación del fichero <b>Prodoc.properties</b></p>

<br>
<br>
<hr><br>
<br>
<br>
<a href='ES_HelpIndex.md'>Indice general</a>