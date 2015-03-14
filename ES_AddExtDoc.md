

&lt;H4&gt;

Añadir en modo Extendido Documento OpenProdoc

&lt;/H4&gt;



&lt;hr&gt;


<p>Este formulario permite añadir documentos al repositorio introduciendo todos los metadatos y los propios del tipo documental elegido.<br>
Permite elegir el tipo documental, que no podrá cambiarse posteriormente, y el ACL.<br>
<blockquote>Al elegir el tipo documental, se mostrarán automáticamente los metadatos específicos de ese tipo en otra solapa (en la versión Swing) o en una zona por debajo (en la versión Web), además de los metadatos comunes a todos los documentos OPD.</p>
<p>El documento se añade en la carpeta actual.</p>
<p>El usuario debe tener permisos de escritura sobre la carpeta actual, en otro caso no podrá insertar documentos.</p>
<p>La lista de opciones a elegir para los distintos metadatos (como tipos documentales) puede variar según los permisos del usuario.</p>
<p>Debe introducirse la información siguiente:</p>
<ul>
<blockquote><li><b>Tipo Documental</b>: Tipo del documento a introducir</li>
<li><b>PDId</b>: Codigo único identificador del documento (asignado por OPD)</li>
<li><b>ACL</b>: (Lista de control de accesos) Nombre de la politica de seguridad aplicable al Documento</li>
<li><b>Título</b>: Nombre descriptivo del documento</li>
<li><b>Fecha</b>: Fecha del documento (Generación/firma/edición/promulgación, etc) en el formato de la personalización del usuario</li>
<li><b>Archivo</b>: Path local del archivo a cargar</li>
<li><b>Tipo Mime</b>: Formato físico del documento a introducir según el estandar MIME (PDF, jpg, Tiff, XML)</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/ES/Img/AddDocExt.jpg' /> </p>
<p>Los campos obligatorios para cada tipo documental apareceran resaltados. Al situarse el cursor o ratón sobre cada campo, aparecerá un mensaje emergente o un texto en la banda de inferior del formulario con información sobre el campo y (en el caso de los campos de tipo fecha u hora) el formato esperado. Los campos de tipo fecha/hora deben ser valores válidos y seguir el formato esperado, en otro caso el campo se limpia y el valor se rechaza.</p>
<p>En caso de producirse un error (carencia de permisos del usuario, error de comunicaciones, fecha incorrecta, etc), la operación se cancela y se comunicará al usuario el motivo del error.</p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='ES_HelpIndex.md'>Índice Ayuda OpenProdoc</a>