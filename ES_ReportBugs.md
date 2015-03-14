

&lt;H4&gt;

Cómo reportar Fallos

&lt;/H4&gt;




&lt;hr&gt;


<p>Durante el uso de cualquier sistema informático puede producirse errores debidos a diversas causas. Los sistemas actuales son cada vez más complejos y en ellos interviene múltiples programas y máquinas.</p>
<p>Como ejemplo, un problema surgido al intentar visualizar un documento puede deberse, entre otros a los motivos siguientes:</p>
<ul>
<blockquote><li>El <b>ordenador</b> utilizado no es capaz de manejar ese formato de imagen</li>
<li>El <b>navegador</b> utilizado no es capaz de manejar ese formato de imagen</li>
<li>Un <b>problema de comunicaciones</b> impide al ordenador comunicarse con el servidor</li>
<li>El <b>servidor</b> tiene exceso de usuarios</li>
<li>El <b>programa utilizado</b> (OPD en este caso) tiene un error en su construcción</li>
<li>Un <b>problema de comunicaciones</b> impide al servidor comunicarse con el lugar donde se almacenó el archivo</li>
<li>.....</li>
</ul>
<p>Por todo ello, en caso de error debe recogerse toda la información posible y sobre todo intentar analizar e identificar las operaciones que provocan el error (Ej: "una operación concreta sobre un tipo documental dado", "Cualquier operación sobre una carpeta concreta", "La secuencia: añadir un documento e inmediatamente despues borrarlo", etc.  )</p>
<p>Aportando la información recogida sobre secuencia de las operaciones y el entorno en que se produce el fallo, este puede diagnosticarse y, o bien mejorar la documentación o corregir el código erroneo.</p>
<p>Para recoger los errores se ha incluido un formulario Web. Este formulario permite informar de un error o fallo en el funcionamiento de OpenProdoc. Por favor incluya tanto detalle como sea posible, para facilitar reproducir el error y corregirlo. </p>
<a href='https://docs.google.com/spreadsheet/viewform?formkey=dFF6ZndKWXFUQnJ0MWtVZWdUWk10X2c6MQ'>Formulario de introducción de errores</a>
<p>Como se especifica en la licencia, el autor no se responsabiliza en ningun aspecto por el uso de OpenProdoc, pero nosotros intentaremos corregir todos los errores localizados. Se agradece su colaboración.</p>
<br>
<br>
<H4><br>
<br>
Cómo identificar Fallos<br>
<br>
</H4><br>
<br>
<br>
<br>
<br>
<hr><br>
<br>
<br>
<p>Cuando se produzcan errores, es necesario recoger información de las operaciones realizadas y los errores encontrados. Esta información puede recogerse automáticamente.</p>
<p>El fichero de configuración de OpenProdoc "Prodoc.properties" contiene dos parámetros relacionados con la traza:</p>
<ul>
<li>TRACELEVEL</li>
<li>TRACECONF</li>
</ul>
<p>TRACELEVEL hace referencia al nivel de traza deseado:</p>
<ul>
<li>LOGLEVELERROR=0: Solo se registrarán los errores.</li>
<li>LOGLEVELINFO=1: Se registrará información adicional.</li>
<li>LOGLEVELDEBUG=2: Se registrará información detallada de todas las operaciones.</li>
</ul>
<p>Por defecto debería tenerse siempre el nivel a 0 (Ej. TRACELEVEL=0), activando temporalmente solo el nivel 2 cuando se desea detectar un error</p>
<p>TRACECONF indica el camino donde se encuentra el fichero de configuración de la traza (Ej.: TRACECONF=/home/OPD/log4j.properties).</p>
<p>Por defecto se suministra un fichero pero puede modificarse a voluntad según el formato de log4j, la herramienta utilizada. Para los conocedores de log4j debe indicarse que el nivel indicado en TRACELEVEL predomina sobre el indicado en el fichero de configuración log4j.properties</p></blockquote>



&lt;hr&gt;


[Indice General](ES_HelpIndex.md)