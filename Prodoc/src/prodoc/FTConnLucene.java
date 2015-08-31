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
 * author: Joaquin Hierro      2015
 * 
 */
package prodoc;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Joaquín Hierro
 */
public class FTConnLucene extends FTConnector
{
static private IndexWriter iwriter;
static private Directory directory;
private IndexSearcher isearcher;
static private Analyzer analyzer;
static private IndexWriterConfig iwc;
static private SearcherManager SM=null;
static private String DirPath;

//-------------------------------------------------------------------------
public FTConnLucene(String pServer, String pUser, String pPassword, String pParam)
{
super(pServer, pUser, pPassword, pParam);
DirPath=pServer;
}
//-------------------------------------------------------------------------
@Override
protected void Create() throws PDException
{
Connect();
Disconnect();
}
//-------------------------------------------------------------------------
@Override
protected void Destroy() throws PDException
{
File F=new File(getServer());
try {
F.delete();
} catch (Exception ex)
    {
    F.deleteOnExit();
    }
}
//-------------------------------------------------------------------------
static synchronized private void Initialize() throws Exception
{
if (SM!=null)
    return;
analyzer = CreateAnalizer();
Path SerPath=Paths.get(DirPath);
directory = FSDirectory.open(SerPath);
iwc = new IndexWriterConfig(analyzer);
iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
iwriter = new IndexWriter(directory, iwc);
SM=new SearcherManager(iwriter, false, null);    
}
//-------------------------------------------------------------------------
@Override
protected void Connect() throws PDException
{
try {    
if (SM==null)
   Initialize(); 
} catch (Exception ex)
    {
    PDException.GenPDException("Error_Connecting_FT_Index", ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
@Override
protected void Disconnect() throws PDException
{
try {        
//SM.close();
//iwriter.close();
//directory.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_Disconnecting_FT_Index", ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
@Override
protected int Insert(String Type, String Id, InputStream Bytes, Record pMetadata) throws PDException
{
try {       
Document doc = new Document();
doc.add(new StringField(F_TYPE.toLowerCase(), Type, Field.Store.YES));
doc.add(new StringField(F_ID, Id.toLowerCase(), Field.Store.YES));
pMetadata.initList();
for (int NumAttr = 0; NumAttr < pMetadata.NumAttr(); NumAttr++)
    {
    Attribute Attr=pMetadata.nextAttr();
    doc.add(new StringField(Attr.getName().toLowerCase(), Attr.Export().toLowerCase(), Field.Store.NO));    
    }
Convert(Bytes);
doc.add(new TextField( F_DOCMETADATA, getFileMetadata().toLowerCase(), Field.Store.NO));
doc.add(new TextField( F_FULLTEXT, getFullText(), Field.Store.NO));
iwriter.addDocument(doc);
iwriter.commit();
SM.maybeRefresh();
//iwriter.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_inserting_doc_FT", ex.getLocalizedMessage());
    }
return(0);
}
//-------------------------------------------------------------------------
@Override
protected int Update(String Type, String Id, InputStream Bytes, Record pMetadata) throws PDException
{
try {       
//iwc = new IndexWriterConfig(analyzer);
//iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
//iwriter = new IndexWriter(directory, iwc);
iwriter.deleteDocuments(new Term(F_ID,Id));
Document doc = new Document();
doc.add(new StringField(F_TYPE, Type, Field.Store.YES));
doc.add(new StringField(F_ID, Id, Field.Store.YES));
pMetadata.initList();
for (int NumAttr = 0; NumAttr < pMetadata.NumAttr(); NumAttr++)
    {
    Attribute Attr=pMetadata.nextAttr();
    doc.add(new StringField(Attr.getName(), Attr.Export(), Field.Store.NO));    
    }
Convert(Bytes);
doc.add(new TextField( F_DOCMETADATA, getFileMetadata(), Field.Store.NO));
doc.add(new TextField( F_FULLTEXT, getFullText(), Field.Store.NO));
iwriter.addDocument(doc);
iwriter.commit();
SM.maybeRefresh();
//iwriter.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_inserting_doc_FT", ex.getLocalizedMessage());
    }
return(0);
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Id
 * @throws PDException 
 */
@Override
protected void Delete(String Id) throws PDException
{
try {       
//iwc = new IndexWriterConfig(analyzer);
//iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
//iwriter = new IndexWriter(directory, iwc);    
iwriter.deleteDocuments(new Term(F_ID,Id));
iwriter.commit();
SM.maybeRefresh();
//iwriter.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_deleting_doc_FT", ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Type
 * @param Id
 * @param sDocMetadata
 * @param sBody
 * @param sMetadata
 * @return
 * @throws PDException 
 */
@Override
protected ArrayList<String> Search(String Type, String sDocMetadata, String sBody, String sMetadata) throws PDException
{
ArrayList<String> Res=new ArrayList();   
try {  
//ireader = DirectoryReader.open(directory);
//isearcher = new IndexSearcher(ireader);
isearcher=SM.acquire();
sBody=sBody.toLowerCase();
//System.out.println("Buscando="+sBody);
Query query = new QueryParser(F_FULLTEXT,analyzer).parse(sBody);
//Query query = new StandardQueryParser(analyzer).parse(sBody, F_FULLTEXT);
ScoreDoc[] hits = isearcher.search(query, MAXRESULTS).scoreDocs;
for (ScoreDoc hit : hits)
    Res.add(isearcher.doc(hit.doc).get(F_ID));
SM.release(isearcher);
//ireader.close();
//directory.close();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_Searching_doc_FT:", ex.getLocalizedMessage());
    }
return(Res);
}
//-------------------------------------------------------------------------
/**
 * Creates a new analyzer ¿in a language and with stop words?
 * @return a new Analyzer
 */
static private Analyzer CreateAnalizer()
{
return(new StandardAnalyzer());
}
//-------------------------------------------------------------------------
}
