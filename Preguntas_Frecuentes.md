# Preguntas frecuentes: #

## Preguntas generales: ##

  * ¿Qué es OpenProdoc?
  * Es un software gestor documental (http://es.wikipedia.org/wiki/Software_de_gesti%C3%B3n_documental), es decir, un producto que permite funciones como:
    * Definir tipos de carpetas/expedientes para estructurar los documentos de acuerdo a la estructura organizativa, los procesos de la entidad, etc.
    * definir tipos de documentos para cada proyecto o área,
    * almacenar documentos en una estructura de carpetas/expedientes,
    * asignar metadatos para facilitar su organización y búsqueda posterior
    * crear versiones de los documentos,
    * Compartir documentación entre grupos de usuarios, restringiendo el acceso de acuerdo a permisos
    * ..

  * ¿Qué se necesita para utilizar OpenProdoc
  * Depende de la versión:
    * La versión Portable funciona en Windows, Linux y Mac, y solo requiere tener instalada una versión de Java igual o superior a la 1.5
    * La versión Cliente Swing requiere además disponer de una base de datos.
    * La versión Web requiere su instalación en un servidor de aplicaciones y de una base de datos. Una vez instalado, se accede con la mayoría de los navegadores.

  * ¿Cuánto cuesta?
  * El uso de OpenProdoc es gratuito en cuanto a licencias. Puede utilizarse por particulares, instituciones o empresas sin pago de licencias. Como cualquier producto, para el coste total debe tenerse en cuenta además los ordenadores necesarios, licencias de otros productos en su caso, almacenamiento, servicios, etc.

  * ¿Es complicado instalarlo?
  * No, aunque depende de la versión.
    * La versión Portable se distribuye en un zip que puede descomprimirse en cualquier carpeta del disco del ordenador o en un USB. A continuación puede utilizarse en cualquiera de los sistemas soportados (Linux, Windows, Mac).
    * La versión Swing requiere instalar una base de datos (como MySql, Derby, HSQLDB), crear un usuario de acceso y luego instalar el cliente.
    * Por último, el cliente Web se instala como cualquier otra aplicación (WAR) en un servidor de aplicaciones J2EE  (http:). Además, debe instalarse el cliente Swing para las funciones de administración.

  * ¿Son iguales las versiones?
  * La versión Portable y la versión Cliente Swing son idénticas. La única diferencia es que la versión portable incluye embebido el servidor de base de datos HSQLDB (http://hsqldb.org/). Esto hace que no requiera servidor de base de datos, pero no permite el uso simultáneo por más de un usuario. Sí puede tener definidos varios usuarios y usarse sucesivamente. La versión Web no incluye las funciones de administración, pero el resto es igual a las otras dos. En todos los casos el motor incluido es el mismo, cambiando solo el interfaz.

  * ¿Incluye todas las funciones de un gestor documental comercial?
  * Si, aunque es importante destacar que cada producto existente se estructura de una forma diferente. Algunos cubren ligeramente todas las funciones del mundo ECM, otros disponen de productos potentes y separados para cada función, algunos solo cubren su especialidad (Captura, Gestor Documental, BPM, etc.). OpenProdoc no incluye por el momento funciones adicionales como BPM/Workflow o Colaboración. Se ha desarrollado con el objetivo de que sea muy sencillo de utilizar y de cubrir eficazmente todas las funciones centrales de la gestión documental (http:wiki) sin sobrecargar con funciones usadas esporádicamente.

  * ¿El desarrollo se basa en algún producto de gestión documental?
  * No, todo el desarrollo se ha realizado desde cero, permitiendo optimizar el diseño. Únicamente se utilizan algunas librerías Apache (http://www.apache.org/) para la comunicación ftp y la subida de ficheros en entorno J2EE.

  * ¿Funcionará en un equipo pequeño/antiguo?
  * Los requerimientos de memoria y CPU son MUY bajos. Es un programa muy optimizado y con menos de 1G de memoria puede utilizarse la versión portable sin problemas.

  * ¿Por qué utilizar un software gestor documental (SGM), cuando puede guardarse en disco los archivos?
  * Hay múltiples motivos para no usar un disco compartido (tanto para un usuario aislado como para un grupo):
    * Seguridad: No todos los usuarios deben acceder a todos los documentos. En un disco compartido están muy limitadas las posibilidades estructurar el acceso. En un SGM se puede limitar los permisos a nivel de cada documento, aunque estén en la misma carpeta.
    * Peligro de virus: Una carpeta compartida facilita que se propaguen virus que infecten los documentos. En un SGM como OpenProdoc los documentos pueden estar encriptados y además no es necesario compartir una carpeta, lo que reduce las posibilidades de propagación.
    * Estructuración de la información: En una estructura de carpetas solo se puede utilizar al nombre de carpeta para organizar la información, en un SGM puede crearse carpetas/expedientes con una estructura de atributos que permitan identificar o localizar los expedientes,
    * Descripción de los documentos: Similarmente, en un sistema de archivos solo podemos basarnos en el nombre y quizá en una búsqueda textual con herramientas complementarias (como Google Desktop) para formatos que no sean imagen. En un SGM podemos asignar categorías y metadatos a cada documento para localizarlos, independientemente del formato físico.
    * Control de versiones: En un SGM se maneja los documentos de forma coherente. En lugar de tener en disco múltiples archivos con nombres como “Informe v 1.0.doc”, Informe v 1.1.doc”,.. puede tenerse un documento de tipo informe con un nombre “Resumen 2012” en su versión vigente, lo que deja mucho más claro y limpio el manejo. Siempre puede recuperarse cualquier versión anterior, pero por defecto se maneja siempre la última.
    * Concurrencia: Cuando varias personas pretenden trabajar sobre un documento, este se bloques, evitando que inadvertidamente se sobrescriba el trabajo de otra persona.
    * Publicación: Por medio de un servidor de aplicaciones, múltiples usuarios pueden acceder a los documentos sin necesidad de instalar ningún producto o compartir carpetas.

  * ¿Por qué otro gestor documental? ¿No hay demasiados?
  * Efectivamente hay bastantes (más que cuando empecé el desarrollo). Sin embargo pienso que hay diversas carencias. Con OpenProdoc se ha procurado hacer un producto que cubra carencias que tienen algunos de los productos existentes y sobre todo centrado en el núcleo de las funciones de un gestor documental, evitando incluir demasiadas funciones poco usadas que sobrecarguen el sistema. Lo más destacable de OpenProdoc es:
    * Modelo documental. El modelo de datos de OpenProdoc está muy optimizado, para evitar limitaciones en el modelo documental que se encuentran en algunos productos. Algunos de los problemas que se solventan en OpenProdoc son:
      * [Herencia](http://es.wikipedia.org/wiki/Herencia_%28inform%C3%A1tica%29) y [polimorfismo](http://es.wikipedia.org/wiki/Polimorfismo_%28programaci%C3%B3n_orientada_a_objetos%29): Muchos productos no tienen Herencia o Polimorfismo, cosa que es casi imprescindible para simplificar la búsqueda, admitir documentos de diverso tipo o evolucionar una clasificación. En una empresa de tamaño mediano/grande es casi imposible hacer un cuadro de clasificación completo, es necesario establecer un modelo base y modificarlo gradualmente (por medio de subtipos). Por supuesto los productos que tiene una estructura fija solo son aplicables a escenarios limitados y concretos.
      * Integridad: En algunos productos, dado el modelo de datos utilizado para guardar los metadatos, no es posible imponer integridad relacional para los metadatos.
      * Unicidad: Similarmente, no siempre es posible definir claves únicas para metadatos concretos (ej. Códigos de expediente, registros entrada, etc.) lo que obliga a desarrollos complejos o deja abierta la puerta a inconsistencias.
      * Optimización: Debido a la estructura de algunos modelos de datos no puede crearse índices para optimizar las búsquedas, lo que limita su escalabilidad
    * Rendimiento y escalabilidad. La arquitectura se OpenProdoc está diseñada pensando en la escalabilidad, con un modelo que en cierto modo puede compararse a las CPU Risc, es decir un conjunto pequeño de instrucciones muy optimizadas que, combinadas, permiten ejecutar las funciones solicitadas rápidamente. El código está construido utilizando fuertemente la herencia y el polimorfismo tanto en la lógica como el interfaz de usuario. El resultado es:
      * El "peso": El motor con toda la funcionalidad ocupa menos de 1 M, por lo que puede usarse en cualquier equipo consumiendo pocos recursos y es bastante rápido.
      * La arquitectura es muy abierta y está preparada para cloud. Realmente no existe un servidor, existe un motor que se puede desplegar en las instancias que se requiera distribuidas en N servidores. De hecho la versión portable embebe el motor, así como la BBDD HSQLDB.
      * Velocidad: Pruebas con una aplicación desarrollada con el API del producto que inicia 10 threads atacando a un servidor local crea 1.000 carpetas e inserta 10.000 documentos, los borra (mueve a la papelera) y los purga (borrar definitivamente) en menos de 3 minutos.
      * El motor se puede embeber en cualquier producto que maneje documentos.
      * Además de almacenar en varios tipos de repositorios (hay sistemas que solo permiten almacenar en Sistema de archivos o solo como blob), puede crearse fácilmente conectores para almacenar los documentos en distintos sistemas (correo, WebDav, etc.).
    * Sencillez: Se ha intentado que el uso sea muy sencillo, pero permita a un usuario profesional sacarle partido.
      * Administración: En algunos sistemas es muy complicado definir tipos documentales, hay que manipular archivos XML y conocer bastante el producto. Lo mismo ocurre para diferentes elementos (usuarios, perfiles, repositorios,...). En este caso la administración es muy homogénea, todos los elementos se definen de forma similar.
      * Igualmente el manejo para los usuarios es complejo. En este caso el modelo es similar a utilizar una estructura de archivos en disco.
      * Relacionado con lo anterior, el hecho de que sea multiplataforma y portable facilita que un usuario lo utilice en cualquier lugar y sin instalación (en un USB). Puede compatibilizarse el uso del cliente Web con el cliente Java Swing.
      * Adaptable al usuario: Cada usuario/grupo puede tener un interfaz diferente en un idioma diferente. Puede así disponerse de un interfaz accesible para baja visión (no es AAA ya que requiere JavaScript). Esto permite su uso también en un modelo de Softare As A Service donde cada empresa tenga su logo, interfaz usuarios, etc.
    * Seguridad:
      * Granularidad: En muchas ocasiones,  la administración es "todo o nada". En OPD se permite crear perfiles y asignar funciones administrativas a cada perfil de forma que por ejemplo un usuario (Documentalista/archivero) pueda solo crear tipos documentales, un usuario de seguridad definir solo instancias ldap, grupos y usuarios y un usuario de producción definir repositorios, almacenamiento, etc. Puede incluso delegarse la administración de un único grupo de usuarios en una persona. En otro escenario, un administrador de una empresa externa puede permitir administrar algunos aspectos a usuarios en cliente, reservándose la administración más "técnica".
      * Autenticación contra múltiples sistemas: Cada usuario puede validarse de forma distinta, incluso teniendo varios Ldap distintos, o validando usuarios contra el sistema o una BBDD, lo que facilita la integración.
      * Encriptación: Aunque con un sistema sencillo, puede encriptarse los documentos antes de almacenarse en el repositorio, proporcionando una seguridad complementaria sin software adicional.

## Funciones: ##

  * ¿Puede definirse tipos documentales de acuerdo a mis necesidades?
  * Sí. Con el cliente Swing, que además es cliente de administración, puede definirse cualquier número de tipos documentales. Cada tipo documental se define como subtipo de uno ya existente. Por defecto existe un tipo básico de carpeta y un tipo básico de documento con un mínimo de metadatos. Todos los tipos documentales creados serán subtipos del  tipo básico o de otro subtipo. Los metadatos y comportamiento se heredan; es decir un tipo documental tiene la suma de todos los atributos de cada uno de sus antecesores, en un modelo orientado a objetos.

  * ¿Para qué sirve que los tipos documentales sigan una orientación a objetos?
  * Tiene varias ventajas:
    * Permite una evolución gradual del modelo. Es muy complejo definir una estructura de tipos documentales para una organización grande con diversos departamentos. Esperar a tener un cuadro de clasificación completo retrasaría el arranque mucho tiempo. Adicionalmente es habitual una evolución del modelo por causas organizativas o legales.
    * Es más potente, ya que puede buscarse por un tipo documental o por un tipo y sus subtipos (Ej. Buscar por unos criterios en “Informes” o en “Informes” y todos sus subtipos). Esto aplica a también a otras operaciones definidas.
    * Es más sencilla y evita tener que definir una “estructura plana”, se realiza un análisis gradual que genera una estructura jerárquica. Los metadatos o propiedades definidas para un tipo se heredan para los subtipos.
    * Es una representación más “real”, ya que las definiciones y tipologías documentales no son totalmente independientes.

  * ¿Puede intercambiarse documentos con sus metadatos con otros usuarios de OpenProdoc?
  * Sí. Basta exportar el documento, o parte de la estructura de carpetas en OpenProdoc, a disco. La exportación vuelca a disco el archivo y además genera un archivo “.OPD” que contiene la catalogación realizada. Si se ha exportado una estructura (ej. Un expediente completo) se generan las carpetas correspondientes en el disco con su catalogación como “.OPD”. En destino puede importarse el documento o el expediente completo. Para que la importación sea correcta, debe existir el tipo documental en el equipo destino. Las definiciones de tipos puede intercambiarse igualmente entre usuarios o entornos, exportando e importando la definición como archivo “.OPD”.

  * ¿Puede intercambiarse definiciones de elementos entre entornos o con otras instituciones?
  * Sí. Basta exportar los elementos deseados desde el cliente pesado (como archivos OPD) e importarlos en el sistema destino. Debe tenerse en cuenta que puede existir dependencias y que en ese caso debe realizarse la importación ordenada. (Ej. Un grupo puede contener a otros grupos y deben importarse previamente los grupos contenidos, para que el grupo contenedor no detecte incoherencias)

  * Si se dispone de una estructura completa de proyectos y expedientes en un sistema de archivos en el disco local ¿es posible importarla de una vez?
  * Sí. Puede importarse un árbol de carpetas completo y cargarlo dentro de una carpeta OpenProdoc, recreando la misma estructura existente en disco. Esta asignación automática permite elegir el tipo documental destino (el mismo tipo para todos los documentos) que lógicamente no debe tener metadatos obligatorios. El título del documento será el nombre de archivo, y la fecha, la del archivo. Los permisos (ACL) se heredarán de la carpeta en que se inserte. Una vez importado, posteriormente puede modificarse los metadatos o la seguridad de cada uno de los elementos.

  * ¿Permite OpenProdoc gestionar Tesauros?
  * Sí (a partir de la versión 0.8). Puede crearse múltiples tesauros con estructurados de acuerdo a las necesidades. Cada tesauro puede utilizarse como un tesauro completo (NT, BT, RT, UF) o simplemente como una lista de materias o una lista de términos, que puede ser jerárquica. Los metadatos de los tipos documentales pueden asociarse a tesauros concretos, de forma que se elija el valor del metadato dentro de los disponibles en el tesauro y se controle la integridad. Por ejemplo puede así definirse un metadato “País” que se valide contra una lista de países o un metadato “Tema del informe” que se valide contra un tesauro de materias.

  * ¿Puede importarse o exportarse los Tesauros?
  * Sí (a partir de la versión 0.8). Se utiliza el estándar SKOS-RDF. No obstante, como en cualquier “estándar”, hay que resaltar que existen diversas interpretaciones y formas de uso (http://lab.usgin.org/book/usgin-skos-vocabulary-service-profile-home/examples-skos-concept-encoding), por lo que aunque se ha hecho esfuerzos para soportar las distintas interpretaciones no puede asegurarse compatibilidad 100% con los tesauros generados fuera OpenProdoc. Si al importar un tesauro surge un problema, se trazará y registrará el error. Adicionalmente, OPD no maneja tesauros poli-jerárquicos, por lo que, aunque se importará sustituyendo la segunda relación BT por una RT, el modelo no será igual.

  * ¿Incluye funciones de BPM?
  * No. A partir de la versión 0.8 se incluyen la posibilidad de definir y asignar procesos simples (que pueden programarse), pero no BPM.

  * ¿Incluye funciones de Colaboración?
  * No. No está previsto.

  * ¿Incluye funciones de Captura?
  * No. Aunque se incorporarán en versiones posteriores.

  * ¿Para qué sirve los repositorios referenciados (REFURL, REFFS)?
  * Permiten catalogar e incluir documentos que no están almacenados en OpenProdoc sino en otros sistemas o en la Web. De esta forma se elimina la necesidad de incluir una copia. Se cataloga y referencia la URL del documento remoto y se puede buscar y abrir como si fuera “local”. Cuando se borra de OpenProdoc, simplemente se elimina la catalogación y referencia, manteniéndose el contenido en su lugar origen.

  * ¿Puede mezclarse en una carpeta/expediente documentos “normales” y referenciados?
  * Sí. Esto permite por ejemplo tener una carpeta con los documentos de un proyecto o persona y además incluir referencias a leyes, normativas internas o bibliografía que complementen la información.


## Gestión de usuarios: ##

  * ¿Qué información se necesita para crear un usuario?
  * Debe introducirse al menos:
    * el nombre corto/login,
    * la forma de autenticación (a elegir entre algunos de los sistemas definidos por el administrador)
    * El rol (a elegir entre algunos de los sistemas definidos por el administrador)
    * La personalización (a elegir entre algunos de las definidas por el administrador)

  * ¿Qué son las personalizaciones?
  * Son conjuntos de elementos que determinan la apariencia de los clientes OpenProdoc. Puede definirse todas las personalizaciones que se desee. La personalización incluye el idioma de presentación, formato de fechas y la apariencia del interfaz. En el caso del cliente Swing esto se limita a las fuentes de caracteres (tanto para adaptarlo a los gustos como para personas con baja visión. En el caso del cliente Web, cada personalización incluye unos CSS y unos JavaScript diferentes, por lo que puede cambiarse mucho la apariencia de las pantallas e incluso el comportamiento ante eventos (aunque debe mantenerse los métodos JavaScript y comportamiento básico para que funcione tal como se espera).

  * ¿Aunque el usuario se autentique contra otro lugar, como Ldap, es necesario crearlo?
  * Si, debe crearse, ya que debe indicarse entre otra información, la forma de autenticarse.

  * ¿Qué ventaja tiene entonces el autenticar contra otro sistema si hay que crear el usuario?
  * La ventaja es que el usuario no tiene que recordar múltiples password de acceso ni sincronizarlas si las normas de la institución obligan a cambiarlas con periodicidad. Además simplifica el trabajo de administración.

  * ¿Puede desactivarse un usuario?
  * Sí. Eso permite bajas provisionales y además evita perder información, ya que durante cierto tiempo existirán documentos creados o modificados por el usuario. Esa trazabilidad hace recomendable tener los datos del usuario.

  * ¿Puede sincronizarse usuarios o grupos con Ldap?
  * No. En cualquier caso, la asignación de usuarios o grupos a ACL exigiría siempre una operativa manual, ya que por el hecho de replicar los usuarios no implica que tengan acceso. Si no están definidos en algún ACL, no tendrán acceso a los documentos que tengan ese ACL. .

  * ¿Para qué sirve poder incluir grupos dentro de otros grupos?
  * Simplifica mucho el mantenimiento de usuarios y grupos. En una institución con una estructura jerárquica por diversos (como departamentos y geográfica) es casi imprescindible. En otro caso es necesario crear gran cantidad de grupos y asignar cada usuario a varios (Ej. Usuarios Madrid, Usuarios España, Usuarios Europa, Usuarios Institución). Si disponemos de grupos anidados, se asigna el usuario a uno o dos grupos que a su vez están contenidos en el resto de grupos de acuerdo a una estructura (Ej. Se asigna el usuario al grupo Madrid, que ya está contenido en el resto de los grupos “geográficos”).

  * ¿Por qué varios tipos de autenticación coexistiendo?
  * Para facilitar su uso y permitir la adaptación a múltiples escenarios se ofrecen distintas formas de autenticar (comprobar identidad) a los usuarios. En una gran institución, es habitual disponer de un Ldap para centralizar  la autenticación y definición de usuarios, sin embargo para no tiene sentido exigir su instalación en una pequeña organización solo para usar OpenProdoc. En ese caso puede utilizarse la autenticación “nativa” OpenProdoc. Por otra parte cada vez es más habitual que los usuarios tengan cuenta de correo o que se disponga de servicios en la nube como GMail. En ese caso puede utilizarse autenticación contra un servidor de correo. Por otra parte, puede haberse desarrollado procesos o integración con aplicaciones que se ejecutan con usuarios genéricos (o de aplicación). En ese caso, en lugar de crear usuarios ficticios puede utilizarse un usuario de login basado en el equipo. Esta misma alternativa puede utilizarse en entornos corporativos controlados, donde el usuario del equipo está fijado por unos administradores. Otra opción adicional es que los usuarios estén dados de alta en una base de datos. En ese caso se puede elegir la validación contra la base de datos. Por último, en muchos casos se producen todos estos escenarios conjuntamente. Por ejemplo en un centro docente puede haber personal fijo empleado (que estarán dados de alta en los distintos sistemas), alumnos (que no son empleados y no tendrán usuario de ciertos sistemas pero que pueden necesitar compartir documentación y quizá tiene correo) y profesores eventuales (que pueden requerir acceso puntual).

## Seguridad: ##

  * ¿Cuál es el modelo de seguridad de OpenProdoc?
  * El modelo se basa en el modelo habitual de ACL (Listas de Control de Acceso), es decir conjuntos de permisos, con un nombre identificativo que se asigna a los distintos elementos (documentos, carpetas, grupos, definiciones). Cada ACL contiene una lista de grupos o usuarios, indicando el permiso asignado a cada uno.
    * Ej: ACL:  Documentos privados
      * Grupo Administradores: Permiso Borrado
      * Grupo Directores: Permiso Escritura
  * Si un usuario o grupo no aparece en un ACL, no tiene ningún permiso sobre los objetos con ese ACL asignado, ni siquiera aparecerá en las búsquedas o listados del usuario.
  * Si un usuario o grupo se referencia varias veces en un ACL (porque está contenido en otro grupo que también aparece en el ACL), tendrá los permisos más altos de todas las referencias.
  * Los permisos se comprueban siempre en el motor OpenProdoc cuando el usuario intenta realizar la operación. Así, si un usuario intenta borrar un documento sobre el que solo tiene permisos de escritura, no podrá hacerlo. Si intenta modificar la definición de un tipo documental del que no tiene permisos, no podrá hacerlo.

  * ¿Para qué sirven los Roles?
  * Los Roles son un complemento de los ACL y otra forma de enfocar la seguridad. Hacen referencia a la capacidad de realizar grupos de operaciones. Cada Rol incluye un nombre un la capacidad de realizar diversas operaciones (crear usuarios, borrar usuarios, crear tipos documentales, borrar tipos documentales, etc.). Cada usuario tendrá un único rol asignado y podrá realizar las funciones permitidas a ese rol. El interfaz de los clientes Swing y Web se adapta a los Roles (si un usuario no tiene capacidad para manejar usuarios nunca le aparecerá esa opción en el menú).

  * ¿Cómo están estructurados los Permisos?
  * Desde el punto de vista de los ACL, básicamente sobre un elemento puede tenerse permisos de lectura, modificación y borrado. Los permisos son aditivos y cada uno incluye los anteriores. Desde el punto de vista de los Roles, se permite, o bien crear elementos o bien modificarlos y borrarlos. Se ha estructurado de esa forma porque el modificar la definición de un grupo o de un tipo documental, equivale a borrarlo, ya que se puede alterar totalmente su esencia y contenido.

  * ¿Es posible delegar la administración de OpenProdoc a otros usuarios?
  * Sí, es uno de los puntos en que se ha hecho más hincapié en el diseño. Para empezar, no existe un concepto de administrador único. El usuario root definido por defecto no tiene básicamente ninguna función que no pueda asignarse a otro usuario. Cualquier usuario puede realizar funciones de administración siempre que los permisos definidos en su Rol lo permitan. Si un usuario tiene activada en su Rol la función de “Insertar Usuarios”, podrá crear usuarios. Esto permite poder crear roles agrupados por perfiles. Puede así crearse un rol de “Documentalista” al que se permita crear y mantener las definiciones de los tipos documentales y los expedientes/carpetas, un Rol de “Responsable de Seguridad” al que se permita mantener los usuarios, los grupos y los ACL. Por defecto hay un rol de administración que permite mantener todos los elementos. Puede asignarse ese rol a cualquier usuario o crearse roles separados. Adicionalmente, en ocasiones no es bastante y es necesario que se reparta el trabajo por regiones, delegaciones o departamentos. En ese caso se puede utilizar además loa ACL asignados a las definiciones. Puede definirse un grupo de usuarios para cada delegación y asignar un ACL a cada grupo de forma que distintos usuarios con Role Responsable de seguridad solo podrían mantener el grupo que les corresponde. Otra opción complementaria es el permitir que un usuario inserte pero no modifique o borre elementos. Así, si es necesario puede crear un usuario o un elemento adicional en caso de urgencia, pero no modificar los existentes.
  * Todo lo anterior facilita que OpenProdoc se utilice como SaaS, permitiendo administradores locales a cada entidad usuaria con funciones acotadas. Similarmente, una empresa que gestiona la explotación y administración de servidores otra puede cederle parte de las funciones de administración.

  * ¿Cómo puede gestionarse usuarios “Invitados”?
  * Hay varios aspectos a contemplar para usuarios “invitados”, entendido como un usuario provisional o temporal, generalmente sin permisos. Esto suele presentar distintos problemas:
    * ¿Quién lo da de alta?  (Puede hacerlo cualquier usuario que en su rol tenga permisos para insertar usuarios, tal como se comenta en el punto anterior. Si se desea no es necesario “molestar” a un administrador central, pudiendo darse permisos solo de inserción a usuarios responsables de áreas o grupos.)
¿Dónde se autentica? (en ocasiones, los usuarios de una entidad se autentican contra Ldap u otros servidores centrales y deben pasar un proceso complejo y darse de alta en diversos sistemas. En OpenProdoc puede definirse un usuario que se autentique contra OpenProdoc, aunque el resto se autentiquen contra un Ldap central, o utilizar un Ldap de invitados diferente del otro)
    * ¿Cómo asegurar que no modifique nada? (Utilizando roles puede definirse un rol sin permisos de modificación de ningún tipo, por lo que aunque lo aunque pueda ver documentos públicos, no podrá modificar nada)
    * ¿Cómo limitar el acceso a los elementos? (Por medio de las ACL. Puede incluirse al usuario en los grupos y ACL deseados para que pueda acceder y en su caso modificar la documentación necesaria)
    * ¿Cómo accede a la documentación pública? (Por defecto hay un grupo donde se incluye automáticamente a todos los usuarios, por lo que los documentos que incluyan ese grupo podrán consultarse)
    * ¿Y si requiere generar documentos? (Cuando se crea un usuario se crea automáticamente una carpeta personal y un ACL personal de forma que pueda manejar documentos de forma controlada sin tener que realizar más operaciones)

  * ¿Es posible guardar los documentos encriptados en disco de forma que no puedan verse aunque se disponga de acceso a la carpeta?
  * Sí, si el repositorio se define como encriptado (sea del tipo que sea) los documentos se almacenarán encriptados. Esto puede ser útil si se utiliza la versión portable instalada en un USB, de forma que en caso de pérdida no se difunde información. No obstante hay que resaltar que el sistema de encriptación utilizado es sencillo y rápido, de forma que si se desea almacenar documentación realmente sensible se recomienda utilizar un producto especializado (o construir un adaptador que realice una encriptación más sofisticado).


## Arquitectura: ##

  * ¿Por qué varios tipos de repositorio?
  * Porque cada tipo tiene sus ventajas y puede utilizarse de acuerdo a las necesidades de cada proyecto, departamento o tipo documental. El almacenamiento en un sistema de archivos es el más habitual y rápido, además, si se almacena en SAN/NAS, permite el uso de múltiples servidores. El almacenamiento como blob es más lento (como pueden testificar los usuarios de algún conocido sistema), sin embargo para archivos pequeños (ej SMS o Twitter) puede ser eficaz. Por último el ftp puede utilizarse como almacenamiento más lento, para realizar copias ante desastres, etc.

  * ¿Puede desarrollarse un nuevo tipo de conector a repositorio (por ejemplo para un sistema legado)?
  * Sí, es bastante fácil, solo hay que implementar unos métodos mínimos (básicamente: conectar, insertar archivo, recuperar archivo, borrar archivo y desconectar).

  * ¿Es escalable?
  * La escalabilidad está únicamente limitada por la escalabilidad de la base de datos. El motor está embebido en cada cliente, sea cliente Swing o aplicación J2EE, y dispone de un caché de objetos para aligerar los accesos al servidor de base de datos. Por tanto puede ampliarse el número de servidores J2EE o aumentarse la potencia de estos. Para los usuarios de cliente pesado, cada usuario tiene su propio motor local. Cada tipo documental puede estar almacenado en un repositorio distinto si se desea, de forma que la recuperación y almacenamiento de los documentos puede crecer también según sea necesario.

  * ¿Por qué incluir cliente pesado y no únicamente Web?
  * Además de que permite disponer de un Gestor Documental portable, un cliente pesado es más rápido y eficaz que un cliente ligero, utiliza menos ancho de banda y permite operaciones como importar o exportar estructuras de carpetas. Aunque se puede argumentar que con AJAX y Html 5 puede conseguirse resultados similares, el desarrollo es mucho más costoso, depende de que las versiones de navegador lo soporten y el resultado no es tan cómodo. A cambio, no requiere instalación, pero en el caso de OpenProdoc, tampoco se requiere.

## Desarrollo: ##

  * ¿Cómo puede desarrollarse con OpenProdoc?
  * Basta descargar el cliente Swing (o la versión portable) y añadir el jar incluido en la carpeta lib en el classpath del proyecto a desarrollar. Además es necesario incluir el fichero de configuración creado tras la instalación.

  * ¿Además del código de OpenProdoc, que se necesita?
  * Se requiere los jar indicados en el “acerca de” del producto. Básicamente, cliente ftp, traza con log4j y subida de ficheros en entorno J2EE, todo ello de Apache.

  * ¿Qué versión utilizar?
  * Se recomienda utilizar siempre una de las ramas “estables” del desarrollo, descargando el cliente pesado vigente y utilizando el jar incluido. Aunque en general se procura subir al repositorio conjuntos coherentes, los elementos en desarrollo que pueden tener aún problemas no detectados o funciones no cubiertas totalmente (ej. Solo alta o baja de un elemento, sin modificación; funciones cubiertas en el motor pero no en los clientes,…). No es recomendable descargar el código de la versión en desarrollo para utilizarla en un desarrollo, ni para otros usos (como un uso didáctico).

  * ¿Admite CMIS?
  * No. Aunque está previsto su soporte en versiones futuras.

  * ¿Admite Webdav?
  * No. No se prevé su implementación.

  * ¿Cómo puede colaborarse con el proyecto?
  * Además de difundir el proyecto si se considera un buen producto ;-) , actualmente puede colaborarse de tres formas:
    * Traduciendo a otros idiomas (actualmente cubierto castellano, inglés y traducido a portugués solo el programa, sin manuales).
    * Definiendo CSS alternativos (los actuales son ejemplos “manifiestamente mejorables”) para la versión web.
    * Definiendo paquetes especializados para áreas o sectores concretos. Esos paquetes podrían contener definiciones de tipos documentales, carpetas/expedientes, permisos/ACL, grupos de usuarios y roles. De esta forma, puede contarse con un paquete especializado que puede importarse y empezar a trabajar sin necesidad de perder tiempo parametrizando, pues se contaría con las definiciones habituales necesarias. Por ejemplo un paquete para despachos de abogados o para centros docentes.
  * La colaboración recibida se empaquetaría y se publicaría en la página de descargas, con la referencia al autor.
  * Actualmente no se puede ofrecer ninguna remuneración por la colaboración.


## Licenciamiento y soporte: ##

  * ¿Puede utilizarse en un desarrollo o integrado en una aplicación?
  * Sí, siempre que respete la licencia (GPL3). Simplificando, puede utilizarse en otro producto de código abierto (open source). Si desea utilizarse en un proyecto comercial, es posible acordar una licencia específica, ya que OpenProdoc está planteado como un producto multilicencia.

  * ¿Se dispone de soporte?
  * Por el momento, no hay soporte in situ. Sin ningún compromiso de plazo (y gratuitamente por el momento), se da soporte por medio de la página de incidencias (https://docs.google.com/spreadsheet/viewform?formkey=dFF6ZndKWXFUQnJ0MWtVZWdUWk10X2c6MQ), donde se puede abrir una incidencia que se responderá tan pronto sea posible.