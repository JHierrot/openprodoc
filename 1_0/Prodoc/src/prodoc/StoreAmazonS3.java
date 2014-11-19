/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU GPL v3 http://www.gnu.org/licenses/gpl.html
 * 
 * you may not use this file except in compliance with the License.  
 * Unless agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Joaquin Hierro      2011
 * 
 */

package prodoc;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import java.io.InputStream;
import java.io.OutputStream;


/**
 *
 * @author jhierrot
 */
public class StoreAmazonS3 extends StoreGeneric
{
String AccessKeyId=null;

String SecretAccessKey=null;
String BucketName=null;    
AmazonS3 s3 = null;

//-----------------------------------------------------------------
/**
 *
 * @param pServer
 * @param pUser
 * @param pPassword
 * @param pParam
 */
protected StoreAmazonS3(String pServer, String pUser, String pPassword, String pParam, boolean pEncrypt)
{
super(pServer, pUser, pPassword, pParam, pEncrypt);
BucketName=pServer;
AccessKeyId=pUser;
SecretAccessKey=pPassword;
if (isEncript())
    setEncriptPass(pParam);
}

//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Create() throws PDException
{
try {
s3.createBucket(BucketName);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_creating_S3_Bucket",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Destroy() throws PDException
{
try {
s3.deleteBucket(BucketName);
} catch (Exception ex)
    {
    PDException.GenPDException("Error_deleting_S3_Bucket",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Connect() throws PDException
{
try {
s3 = new AmazonS3Client(new BasicAWSCredentials(AccessKeyId, SecretAccessKey));
} catch (Exception ex)
    {
    PDException.GenPDException("Error_connecting_trough_AmazonS3",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected void Disconnect() throws PDException
{
}
//-----------------------------------------------------------------
/**
 * 
 * @param Id
 * @param Ver
 * @param Bytes
 * @return
 * @throws PDException
 */
protected int Insert(String Id, String Ver, InputStream Bytes) throws PDException
{
try {
s3.putObject(BucketName, GenKey(Id, Ver), Bytes, new ObjectMetadata());
} catch (Exception ex)
    {
    PDException.GenPDException("Error_inserting_content",ex.getLocalizedMessage());
    }
return (-1); 
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @throws PDException
 */
protected void Delete(String Id, String Ver) throws PDException
{
try {
s3.deleteObject(BucketName, GenKey(Id, Ver));
} catch (Exception ex)
    {
    PDException.GenPDException("Error_deleting_content",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @return
 * @throws PDException
 */
protected InputStream Retrieve(String Id, String Ver) throws PDException
{
try {
S3Object object = s3.getObject(new GetObjectRequest(BucketName, GenKey(Id, Ver)));
return(object.getObjectContent());
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_content", ex.getLocalizedMessage());
    }
return(null);
}
//-----------------------------------------------------------------
/**
 *
 * @param Id
 * @param Ver
 * @return
 * @throws PDException
 */
protected int Retrieve(String Id, String Ver, OutputStream fo) throws PDException
{
VerifyId(Id);
int Tot=0;
try {
S3Object object = s3.getObject(new GetObjectRequest(BucketName, GenKey(Id, Ver)));
InputStream in=object.getObjectContent();
int readed=in.read(Buffer);
while (readed!=-1)
    {
    if (isEncript())
       DecriptPass(Buffer, readed);
    fo.write(Buffer, 0, readed);
    Tot+=readed;
    readed=in.read(Buffer);
    }
in.close();
fo.flush();
fo.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_retrieving_content", ex.getLocalizedMessage());
    }
return(Tot);
}
//-----------------------------------------------------------------
/**
 * 
 * @param Id1 Original Identifier
 * @param Ver1 Original Version
 * @param Id2  New Identifier
 * @param Ver2  New Version
 * @throws PDException
 */
@Override
protected void Rename(String Id1, String Ver1, String Id2, String Ver2) throws PDException
{
try {
s3.copyObject(BucketName, GenKey(Id1, Ver1), BucketName, GenKey(Id2, Ver2));
s3.deleteObject(BucketName, GenKey(Id1, Ver1));
} catch (Exception ex)
    {
    PDException.GenPDException("Error_renaming_content:",ex.getLocalizedMessage());
    }
}
//-----------------------------------------------------------------
private String GenKey(String Id, String Ver) throws PDException
{
VerifyId(Id);
return(Id+"_"+Ver);
}
//-----------------------------------------------------------------
}
