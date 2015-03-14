

&lt;H4&gt;

Lista Repositorios

&lt;/H4&gt;



&lt;hr&gt;


<p>Al seleccionar la opción de administrar Repositorios, aparecerá la pantalla de listado de Repositorios. Esta pantalla permite realizar el mantenimiento de todos los Repositorios del sistema.</p>
<p>Todos los documentos en OPD se guardan en un repositorio. Puede utilizarse el mismo repositorio para todos los tipos documentales o crearse diversos repositorios y asignarlos para el almacenamiento de tipos documentales diferentes, de acuerdo a las necesidades de espacio, velocidad y coste de almacenamiento. OPD soporta los tipos de repositorio:</p>
<ul>
<blockquote><li><b>FS</b>: FileSystem. almacenamiento en una carpeta de sistema accesible para los clientes Web o Swing.</li>
<li><b>BBDD</b>: Base de Datos. Almacenamiento en una tabla como atributo BLOB. Recomendable para archivos pequeños o tipos documentales con pocos ejemplares.</li>
<li><b>FTP</b>: Almacenamiento en un servidor ftp. Recomendable para tipos documentales con poco acceso.</li>
<li><b>REFURL</b>: Referencia a documentos/páginas existente en Internet/Intranet. Permite clasificar y catalogar documentos de interés sin tenerlos localmente almacenados.(Los documentos no se borran al borrar el registro)</li>
</ul>
<p>Al definir o crear lo repositorios, es posible encriptar los mismos, de forma que aunque un usuario tenga acceso al sistema de archivos o Base de datos, no pueda visualizar los documentos sino es a través de OPD.</p>
<p>Para filtrar los Repositorios que se desea revisar, basta introducir parte del nombre en el cuadro de texto y pulsar el botón. La lista de Repositorios que cumplen las condiciones aparecerá en la tabla de resultados. Si se pulsa el botón sin introducir ningún valor se visualizará todos los elementos sobre los que tenga permiso el usuario.La tabla de resultados muestra los datos:</p>
<ul>
<li>Nombre identificativo</li>
<li>Descripción del Repositorio</li>
<li>Tipo de repositorio</li>
<li>URL o referencia al repositorio</li>
<li>Parámetros adicionales del repositorio (específicos de cada tipo)</li>
<li>Usuario de conexión al repositorio</li>
<li>Password de conexión al repositorio</li>
<li>Indicador de si el repositorio está encriptado</li>
<li>Nombre del último usuario que modificó la definición del Repositorio</li>
<li>Fecha y hora de la última modificación</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/ES/Img/ListRepositories.jpg' /> </p>
<p>Esa tabla de resultados puede ordenarse seleccionando la cabecera de cada columna. Puede también cambiarse el tamaño de cada columna arrastrando la línea separadora de las cabeceras</p>
<p>Sobre la tabla de resultados se dispone de varios botones que permiten realizar operaciones sobre el elemento seleccionado. Las operaciones disponibles son:</p>
<ul>
<li>Añadir un nuevo elemento</li>
<li>Eliminar el elemento seleccionado (Siempre que no se esté utilizando)</li>
<li>Modificar el elemento seleccionado</li>
<li>Crear un nuevo elemento como copia del elemento seleccionado</li>
<li>Exportar el elemento seleccionado</li>
<li>Exportar todos los elementos listados</li>
<li>Importar desde archivo uno o más elementos previamente guardados</li>
</ul>
<p>Debe tenerse en cuenta al exportar e importar que algunos elementos pueden tener dependencia de otros, por lo que debe exportarse todos los elementos relacionados y en el momento de importarse hacerlo en el orden adecuado</p>
<p>En caso de producirse un error (carencia de permisos del usuario, incoherencia de los datos, etc), la operación se cancela y se comunicará al usuario el motivo del error.</p>
<p>Ver: <a href='ES_MantRepositories.md'>Mantenimiento Repositorios</a> y <a href='ES_ListObjDef.md'>Lista de Definiciones de Objetos</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='ES_HelpIndex.md'>Índice Ayuda OpenProdoc</a>