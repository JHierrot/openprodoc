

&lt;H4&gt;

Exportación e Importación Documentos

&lt;/H4&gt;




&lt;hr&gt;


<p>Cuando se desee importar o exportar un documento <b>aislado</b>, debe utilizarse las opciones Documentos->Importar o Documentos->Exportar</p>
<p>La opción <b>Exportar</b> exporta el documento actual con todos sus metadatos. Los metadatos se exportan en formato opd (un archivo de texto XML con codificación utf-8) y el documento en su formato original.</p>
<p>OPD presenta un cuadro de diálogo para elegir el directorio donde se almacenará tanto el documento, como los metadatos.</p>
<p>Debe tenerse en cuenta que, para evitar efectos indeseados al importar, no se arrastra definiciones al exportar. Por tanto, las definiciones de elementos como el tipo documental (con los metadatos correspondentes), ACL, Tipo Mime, etc. deberán existir en el momento de importar (en el sistema actual u en otro diferente). Para exportar explicitamente las definiciones debe utilizarse las funciones de mantenimiento de las pantallas de administración.</p>
<p>La opción <b>Importar</b> carga un documento con todos sus metadatos en la carpeta actual. OPD presenta un cuadro de diálogo para elegir el archivo de tipo ".opd" donde se almacenan los metadatos y que contiene la referencia al archivo con el documento. En el momento de importar deben existir las definiciones de:</p>
<ul>
<li><b>Tipo documental</b>: Aunque no necesariamente debe ser idéntico, debe contener, al menos, los mismos metadatos.</li>
<li><b>ACL</b>: Similarmente debe existir aunque no tiene porqué coincidir la definición.</li>
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


&lt;hr&gt;


[Índice Ayuda OpenProdoc](ES_HelpIndex.md)