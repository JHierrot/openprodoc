

&lt;H4&gt;

Attributes Maintenance

&lt;/H4&gt;



&lt;hr&gt;


<p>When selected in the form <a href='EN_MantObjDef.md'>Object Definition Maintenance</a> one of the options to add, delete, modify or copy the Metadata, will appear this form with different fields on or off.</p>
<p>This form contains the information:</p>
<ul>
<blockquote><li><b>Attribute Name</b>: Metadata identifier whose value can not be repeated or changed once assigned. You can have a maximum length of 32 characters. (Eg "ProjectCode"). Is the name to be used internally by integrating through the API.</li>
<li><b>Public Attribute Name</b>: Is the name that users will see in the different forms and lists of OPD. It can have a maximum length of 32 characters. (Eg "Code of the Project").</li>
<li><b>Attribute Description</b>: Description that allows more information about the use and meaning of the metadata. Appears as an aid in the forms that display the metadata. You can have a maximum length of 128 characters. (Eg "Project Code under corporate regulations XCP")</li>
<li><b>Type of Metadata </b>: The metadata type can be: String, Date, Date-Time, integer, float, or logical.</li>
<li><b>Length</b>: In case of be of type string, maximum length that will hold the metadata.</li>
<li><b>Required</b>: Boolean value indicating whether the metadata is mandatory when creating a document.</li>
<li><b>Unique</b>: A Boolean value that indicates whether values ​​can not be repeated in the metadata for documents of this type.</li>
<li><b>Modifiable</b>: A Boolean value that indicates the metadata be changed after creating the document.</li>
<li><b>Multivalued</b>: A Boolean value that indicates that you can enter a list of values ​​in the metadata (eg a list of authors or keywords).</li>
</ul>
<p>According to the characteristics defined in metadata, when a user inserts or modifies documents, OPD will enforce the conditions.</p>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/MantAttr.jpg' /> </p>
<p>Ver: <a href='EN_MantObjDef.md'>Object Definition Maintenance</a></p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>