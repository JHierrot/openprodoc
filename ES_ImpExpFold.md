

&lt;H4&gt;

Exportar e Importar Carpetas

&lt;/H4&gt;




&lt;hr&gt;


<p>Cuando se desee importar o exportar una estructura completa de carpetas, debe utilizarse las opciones Carpetas->Importar o Carpetas->Exportar</p>
<p>La opción <b>Exportar</b> exporta la carpeta OPD <b>actual</b> y crea en el sistema de archivos una estructura de carpetas equivalente a la existente en OPD. Antes de iniciar la exportación, OPD presentará una pantalla donde definir distintas características de la exportación. Las opciones posibles y sus efectos son:</p>
<ul>
<blockquote><li><b>Un nivel</b>: Si se selecciona esta opción, solo se exportará la carpeta elegida, en otro caso, se continuará recursivamente hasta recorrer todo la estructura por debajo de la carpeta actual.</li>
<li><b>Incluir documentos</b>: Si se selecciona esta opción, la exportación incluirá los documentos contenidos de cada una de las carpetas. En otro caso, solo exportará las carpetas.</li>
<li><b>Incluir Metadatos</b>: Si se selecciona esta opción, la exportación generará, al mismo nivel y con el mismo nombre que el elemento, un archivo ".opd" con los metadatos del objeto (carpeta o documento). En otro caso, solo se crea en el sistema de archivos la carpeta o documento.</li>
<li><b>La carpeta destino</b> indica la carpeta del sistema de archivos por debajo de la cual se creará la estructura de carpetas y documentos.</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/ES/Img/Export.jpg' /> </p>
<p>La Exportación solamente incluirá los documentos sobre los que el usuario tenga permisos de acceso, por tanto no debe asumirse que se ha exportado el árbol completo salvo que el usuario tenga permisos de acceso a todas las ramas y documentos</p>
<p>Al finalizar la exportación, OPD presentará un resumen conteniendo el número de carpetas exportadas y el número de documentos. Si se produce un error se presentará un mensaje con el motivo del error.</p>
<p>Debe tenerse en cuenta que, para evitar efectos indeseados al importar, no se arrastra definiciones al exportar. Por tanto, las definiciones de elementos como el tipo documental (con los metadatos correspondentes), ACL, Tipo Mime, etc. deberán existir en el momento de importar (en el sistema actual u en otro diferente). Para exportar explicitamente las definiciones debe utilizarse las funciones de mantenimiento de las pantallas de administración.</p>
<p>La opción <b>Importar</b> importa una estructura del sistema de archivos EN la carpeta OPD <b>actual</b> y crea en OPD una estructura de carpetas equivalente a la existente en el sistema de archivos. Antes de iniciar la imortación, OPD presentará una pantalla donde definir distintas características de la Importación. Las opciones posibles y sus efectos son:</p>
<ul>
<li><b>Un nivel</b>: Si se selecciona esta opción, solo se importará la carpeta elegida, en otro caso, se continuará recursivamente hasta recorrer todo la estructura por debajo de la carpeta actual.</li>
<li><b>Incluir documentos</b>: Si se selecciona esta opción, la importación incluirá los documentos contenidos de cada una de las carpetas. En otro caso, solo importará las carpetas.</li>
<li><b>Incluir Metadatos</b>: Si se selecciona esta opción, la importación utilizará, al mismo nivel y con el mismo nombre que el elemento, un archivo ".opd" con los metadatos del objeto (carpeta o documento). En otro caso, se utilizará el nombre del archivo como título, y la fecha del archivo como fecha del documento.</li>
<li><b>Tipo Carpeta:</b> Si no se importan metadatos (por elección o porque es un sistema de archivos que no los contiene), este parámetro indica el tipo de carpeta que se asignará a las carpetas importadas. El tipo asignado no puede tener metadatos obligatorios, pues en otro caso fallará la importación.</li>
<li><b>Tipo Documental:</b> Si no se importan metadatos (por elección o porque es un sistema de archivos que no los contiene), este parámetro indica el tipo documental que se asignará a los documentos importados. El tipo asignado no puede tener metadatos obligatorios, pues en otro caso fallará la importación.</li>
<li><b>La carpeta Origen</b> indica la carpeta del sistema de archivos a partir de la cual se recorrerá la estructura de carpetas y documentos.</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/ES/Img/Import.jpg' /> </p>
<p>Al finalizar la Importación, OPD presentará un resumen conteniendo el número de carpetas importadas y el número de doucmentos. Si se produce un error se presentará un mensaje con el motivo del error.</p>
<p>En el momento de importar deben existir las definiciones de:</p>
<ul>
<li><b>Tipos documentales y tipos de carpeta</b>: Aunque no necesariamente debe ser idénticos, debe contener, al menos, los mismos metadatos.</li>
<li><b>ACLs</b>: Similarmente deben existir aunque no tienen porqué coincidir la definición.</li>
<li><b>Tipo Mime</b>: Similarmente debe existir aunque no tiene porqué coincidir exactamente la definición.</li>
</ul>
<p>Durante la importación, se ignorarán los valores:</p>
<ul>
<li><b>Usuario Modificación</b>: Se asignará el usuario que realiza la importación.</li>
<li><b>Fecha Modificación</b>: Se asignará la fecha actual.</li>
<li><b>Repositorio</b>: Se asignará y utilizará el correspondiente al tipo documental en el momento de la importación.</li>
<li><b>Identificador Único (PDId)</b>: Se asignará una nueva identificación.</li>
<li><b>Carpeta</b>: Se asignará la carpeta actual en el momento de la importación.</li>
</ul>
<br>
<br>
<hr><br>
<br>
<br>
<a href='ES_HelpIndex.md'>Índice Ayuda OpenProdoc</a>