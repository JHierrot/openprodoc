

&lt;H4&gt;

Lista Definiciones de Objetos

&lt;/H4&gt;



&lt;hr&gt;


<p>Al seleccionar la opción de administrar Definiciones de Objetos, aparecerá la pantalla de listado de Definiciones de Objetos. Esta pantalla permite realizar el mantenimiento de todos los Definiciones de Objetos del sistema.</p>
<p>En OPD podemos definir diversos tipos de carpetas (contenedores) y tipos documentales para modelar la estructura documental de la organización. A partir de los tipos de objectos base (PD_DOCS o PD_FOLDERS) se puede crear un número ilimitado de subtipos que heredarán los metadatos definidos en los tipos de los que descienden.<br>
<blockquote>Para manipular o buscar documentos podemos tratar cada tipo de forma independiente o incluir los subtipos en la operación.</p>
<p>Las carpetas o contenedores pueden definirse también con diversos metadatos y con una estructura jerárquica que refleje las necesidades documentales. </p>
<p>Para filtrar los Definiciones de Objetos que se desea revisar, basta introducir parte del nombre en el cuadro de texto y pulsar el botón. La lista de Definiciones de Objetos que cumplen las condiciones aparecerá en la tabla de resultados.<br>
Si se pulsa el botón sin introducir ningún valor se visualizará todos los elementos sobre los que tenga permiso el usuario. La tabla de resultados muestra los datos:</p>
<ul>
<li>Nombre identificativo</li>
<li>Tipo de Objeto (FOLDER o DOCUMENT), es decir contenedor o documento con metadatos y un archivo/contenido</li>
<li>Descripción del tipo de objeto</li>
<li>Indicador booleano que indica si el tipo de objeto está activo</li>
<li>ACL de la definición. Según el ACL, será posible para el usuario modificar la definición del objeto (Ver: <a href='ES_ListACL.md'>ACL</a>)</li>
<li>Definición "padre" de la que se heredan los atributos (y sucesivamente hasta el nivel superior de los tipos "base" PD_DOCS o PD_FOLDERS)</li>
<li>Repositorio en que se almacenarán los documentos de este tipo (Ver: <a href='ES_ListRepositories.md'>Repositorios</a>)</li>
<li>Indicadores booleanos de si debe trazarse las operaciones de Alta, baja, modificación y consulta</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/ES/Img/ListObjDef.jpg' /> </p>
<p>Esa tabla de resultados puede ordenarse seleccionando la cabecera de cada columna. Puede también cambiarse el tamaño de cada columna arrastrando la línea separadora de las cabeceras</p>
<p>Sobre la tabla de resultados se dispone de varios botones que permiten realizar operaciones sobre el elemento seleccionado.</p>
<p><b>SIEMPRE</b> debe seleccionarse un elemento, incluso para crear un nuevo tipo, ya que el elemento seleccionado se tomara como clase padre del nuevo tipo documental. Si el seleccionado es una carpeta, el nuevo tipo es una carpeta, si es un documento, el nuevo tipo es un documento. Las operaciones disponibles son:</p>
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
<p>Ver: <a href='ES_MantObjDef.md'>Mantenimiento Definiciones de Objetos</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='ES_HelpIndex.md'>Índice Ayuda OpenProdoc</a>