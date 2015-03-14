

&lt;H4&gt;

Interfaz  de usuario OpenProdoc

&lt;/H4&gt;




&lt;hr&gt;


<p>El interfaz de usuario de OPD se ha definido con los siguientes criterios:</p>
<ul>
<blockquote><li>Hacer un interfaz sencillo e intuitivo, de forma que se minimice la necesidad de utilizar manuales.</li>
<li>Hacerlo lo mas homogeneo en ambos clientes (Web y Swing).</li>
<li>Aprovechar hasta donde se pueda las características de cada entorno.</li>
<li>Permitir que pueda parametrizarse para adaptarse a las necesidades de cada entidad o persona</li>
</ul>
<p>Como ocurre en general, los requisitos son en parte contradictorios, por lo que se ha buscado una solución de compromiso. Como consecuencia, los clientes OPD tienen las siguientes características y comportamiento:</p>
<ul>
<li>El comportamiento de botones y opciones de menú es siempre sobre el elemento seleccionado, sea del tipo que sea, en funciones de consulta o administración.</li>
<li>Dada la ventaja que representa utilizar tablas que puedan ordenarse y ajustarse, se ha definido así en el cliente Swing (se incluirá en futuras versiones del cliente Web).</li>
<li>Igualmente se ha aprovechado el uso de ventanas emergentes en Swing, pero no en modo web por los problemas de accesibilidad y comportamiento "no estandar" que tiene.</li>
<li>No se ha incluido funciones de administración en esta primera versión del cliente web pero se incluirá en futuras.</li>
<li>En el caso de cliente Swing, más orientado a administración, las únicas parametrizaciones admitidas son el cambio en las fuentes de caracteres</li>
<li>En el cliente web puede cambiarse el las hojas de estilo CSS, las imágenes e incluso los javascript, lo que permite un cambio de apariencia casi completo.</li>
<li>Dada por una parte la similitud de formularios Web y Swing y por otra parte la posible diferencia que puede haber debido al cambio de CSS, se ha optado por incluir principalmente formularios Swing en la documentación, ya que cambiarán menos.</li>
<li>Para facilitar la adaptacion y entender el efecto de cada elemento, se incluye en el Cliente Web una serie de parametrizaciones ejemplo. Para crear una nueva, basta copiar las carpetas correspondientes de uno de los ejemplos (contenidas en css, img y js) y crear otro estilo. El hecho de que el Javascript sea distintos, permite incluso modificar o crear elementos de forma que no sería posible solo con css.</li>
</ul>
<br>
<br>
<hr><br>
<br>
<br>
<a href='ES_HelpIndex.md'>Índice Ayuda OpenProdoc</a>