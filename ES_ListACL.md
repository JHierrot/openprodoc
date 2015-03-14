

&lt;H4&gt;

Lista ACL

&lt;/H4&gt;



&lt;hr&gt;


<p>Al seleccionar la opción de administrar ACL, aparecerá el formulario de "listado de ACL". Este formulario permite realizar el mantenimiento de todos los ACL del sistema.</p>
<p>Las Listas de Control de Acceso (ACL Access Control List) son el nucleo de la seguridad en OPD. Los documentos, carpetas y la mayoría de los tipos de objetos tiene un ACL asignado. En función de los usuario o grupos que aparezcan en el ACL y de los permisos asignados, el usuario podrá realizar unas operaciones u otras.</p>
<p> Un ACL estará constituido por:</p>
<ul>
<blockquote><li>Nombre identificativo</li>
<li>Descripción del ACL</li>
<li>Una lista de grupos y los permisos asociados a cada grupo</li>
<li>Una lista de usuarios y los permisos asociados a cada usuario</li>
</ul>
<p>Cuando un usuario intenta acceder a un objeto, se comprueba el ACL de ese objeto, revisando los usuarios y grupos que aparecen y asignando el máximo permiso. Es decir si el usuario aparece en el ACL con un permiso, y pertenece además a dos grupos, cada uno de ellos con un permiso diferente, se le asigna el permiso más alto de los tres. Si un usuario no está declarado en un ACL, directamente como usuario o indirectamente a través de alguno de los grupos a los que pertenece, no podrá ver el objeto, recuperarlo ni le aparecerá en ningún listado</p>
<p>Para filtrar el ACL o lista de ACL que se desea revisar, basta introducir parte del nombre en el cuadro de texto y pulsar el botón.  La lista de ACL que cumplen las condiciones aparecerá en la tabla de resultados. Si se pulsa el botón sin introducir ningún valor se visualizará todos los elementos sobre los que tenga permiso el usuario. La tabla de resultados muestra los datos:</p>
<ul>
<li>Nombre identificativo</li>
<li>Descripción del ACL</li>
<li>Nombre del último usuario que modificó el ACL</li>
<li>Fecha y hora de la última modificación</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/ES/Img/ListACL.jpg' /> </p>
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
<p>Ver: <a href='ES_MantACL.md'>Mantenimiento de ACL</a> y <a href='ES_ListGroups.md'>Lista de Grupos</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='ES_HelpIndex.md'>Índice Ayuda OpenProdoc</a>