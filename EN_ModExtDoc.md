

&lt;H4&gt;

Extended Modify Document OpenProdoc

&lt;/H4&gt;



&lt;hr&gt;


<p>This form allows you to modify the document selected by entering all the common metadata and document type-specific metadata .<br>
This form does not choose the document type, which can not be changed after inserting the document and allows you to modify security (ACL).<br>
<blockquote>In addition to the metadata common to all OPD documents, metadata specific to that type appear in another tab (in version Swing) or in an area below (in the Web version). </p>
<p> The user must have blocked (Checkout) document for editing. The edition includes changes to the metadata and update the document. When editing is complete, you must unlock (Checkin) to publish the new values ​​of the document and save a new version.<br>
<blockquote>Until then, only the user who locked will see the draft. If you wish to cancel everything done, just cancel the lock (Cancel Checkout), returning to the existing values at the time of checkout. </p><p>La lista de opciones a elegir para los distintos metadatos puede variar según los permisos del usuario.</p>
<p>You can enter/modify the following information:</p>
<ul>
</blockquote><blockquote><li><b>ACL</b>: (Access Control List) Name of the security policy applicable to the document</li>
<li><b>Title</b>: Descriptive name for the document</li>
<li><b>Date</b>: Document's date (The date in which the document was signed, generated, etc.) in the localized format for the user</li>
<li><b>File</b>: Local Path of the file</li>
<li><b>Mime Tpe</b>: Document's Mime Type of the file to insert acording to the MIME Standerd (PDF, jpg, Tiff, XML, ..)</li>
</ul>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/ModDocExt.jpg' /> </p>
<p> <img src='http://dl.dropbox.com/u/49603479/OpenProdoc/EN/Img/ModDocExt2.jpg' /> </p>
<p>Required fields for each document type are highlighted. By placing the cursor or mouse over each field, it whill be showed a tiptool or a text message in the bottom of the form with information on the field and (in the case of fields of type date or time) the expected format. The fields of type date / time values ​​must be valid and follow the expected format, otherwise the field is cleared and the value is rejected. </p>
<p>In the event of an error (lack of user permissions, communication error, incorrect date, etc.), the operation is canceled and the user will communicate the reason for the error.</p>
<p>View: <a href='EN_CheckIn.md'>CheckIn</a> (Confirms changes in the locked document),  <a href='EN_Checkout.md'>CheckOut</a> (Locks a Document for editing) and <a href='EN_CancelCheckout.md'>Cancel Checkout</a> (Cancels changes in the document)</p>
<br>
<br>
<hr><br>
<br>
<br>
<a href='EN_HelpIndex.md'>Help Index OpenProdoc</a>