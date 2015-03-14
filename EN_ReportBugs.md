

&lt;H4&gt;

How to report bugs

&lt;/H4&gt;




&lt;hr&gt;


<p> While using any computer errors can occur due to various causes. Current systems are becoming more complex and involved multiple programs and their machines. </p>
<p> As an example, a problem encountered when attempting to view a document may be due, among others the following reasons: </p>
<ul>
<blockquote><li>The <b>computer</b> used is not able to handle the image format </li>
<li>The <b>browser</b> used is not able to handle the image format </li>
<li>A <b>communications problem</b> prevents your computer communicate with the server </li>
<li>The <b>server</b> has too much users </li>
<li>The <b>program used</b> (OPD in this case) has an error in its construction </li>
<li>A <b>communications problem</b> prevents the server to communicate with the site you stored the file </li>
<li>.....</li>
</ul>
<p>Therefore, in case of error should be collected much information as possible and try to analyze and identify all operations that cause the error (eg, "a specific operation on a given document type," "Any operation on a folder concrete "," The sequence: adding a document and immediately after delete ", etc..)</p>
<p>Providing the information collected on the sequence of operations and the environment in which failure occurs, this can be diagnosed and, or improve the documentation or correct the code erroneous. </p>
<p>To collect the errors OpenProdoc includes a web form. This form allows you to report an error or malfunction of OpenProdoc. Please include as much detail as possible, to help reproduce the error and correct it.</p>
<a href='https://docs.google.com/spreadsheet/viewform?formkey=dFF6ZndKWXFUQnJ0MWtVZWdUWk10X2c6MQ'>Errors Report Form</a>
<p>As specified in the license, the author is not liable in any respect by the use of OpenProdoc, but we will try to correct any errors found. Thank you for your cooperation.</p>
<br>
<br>
<H4><br>
<br>
How to identify bugs<br>
<br>
</H4><br>
<br>
<br>
<br>
<br>
<hr><br>
<br>
<br>
<p>When errors occur, it is necessary to collect information on the operations and errors encountered. This information may be collected automatically.</p>
<p> OpenProdoc configuration file "Prodoc.properties" contains two parameters related to the trace: </p>
<ul>
<blockquote><li>TRACELEVEL</li>
<li>TRACECONF</li>
</ul>
<p>TRACELEVEL refers to the desired trace level:</p>
<ul>
<li>LOGLEVELERROR=0: Only errors are logged.</li>
<li>LOGLEVELINFO=1: Additional information is recorded.</li>
<li>LOGLEVELDEBUG=2: Details of all operations will be recorded</li>
</ul>
<p>Always default should be level 0 (ie TraceLevel = 0), level 2 temporarily activated when you want to detect an error.</p>
<p>TRACECONF  indicates the path where is stored the file of the trace configuration (eg TRACECONF = /home/OPD/log4j.properties).</p>
<p> By default, a file is provided but can be modified at will according to the log4j format, the tool used to log. For those who know log4j, should be noted that the log level parameter assigned in TraceLevel takes precedence over the value assigned in the configuration file log4j.properties </p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index</a>