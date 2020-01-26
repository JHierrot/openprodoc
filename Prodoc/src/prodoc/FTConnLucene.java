/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU Affero GPL v3 http://www.gnu.org/licenses/agpl.html
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.ca.CatalanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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
static private Analyzer analyzer;
static private IndexWriterConfig iwc;
static private SearcherManager SM=null;
static private String DirPath;
static private String GlobLang=null;
static private CharArraySet SW=null;
//-------------------------------------------------------------------------
/**
 * Constructor
 * @param pServer path to Lucene indexes
 * @param pUser currently not used for this connection
 * @param pPassword currently not used for this connection
 * @param pParam currently not used for this connection
 */
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
SM=new SearcherManager(iwriter, false, false, null);    
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
} catch (Exception ex)
    {
    PDException.GenPDException("Error_Disconnecting_FT_Index", ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
@Override
protected int Insert(String Type, String Id, InputStream Bytes, Record pMetadata) throws PDException
{
StringBuilder S=new StringBuilder(1000);    
try {       
Document doc = new Document();
doc.add(new StringField(F_TYPE.toLowerCase(), Type, Field.Store.YES));
doc.add(new StringField(F_ID, Id.toLowerCase(), Field.Store.YES));
pMetadata.initList();
for (int NumAttr = 0; NumAttr < pMetadata.NumAttr(); NumAttr++)
    {
    Attribute Attr=pMetadata.nextAttr();
    if (Attr.getValue()!=null || Attr.isMultivalued() && !Attr.getValuesList().isEmpty())
        S.append(Attr.getName().toLowerCase()).append("=").append(Attr.Export().toLowerCase()).append("\n");
    }
if (Bytes!=null)
    {
    Convert(Bytes);
    doc.add(new TextField( F_FULLTEXT, S.toString()+getFileMetadata().toLowerCase()+"\n"+getFullText(), Field.Store.NO));
    }
else
    doc.add(new TextField( F_FULLTEXT, S.toString(), Field.Store.NO));
iwriter.addDocument(doc);
iwriter.commit();
SM.maybeRefresh();
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
StringBuilder S=new StringBuilder(1000);    
try {       
iwriter.deleteDocuments(new Term(F_ID,Id));
Document doc = new Document();
doc.add(new StringField(F_TYPE, Type, Field.Store.YES));
doc.add(new StringField(F_ID, Id, Field.Store.YES));
pMetadata.initList();
for (int NumAttr = 0; NumAttr < pMetadata.NumAttr(); NumAttr++)
    {
    Attribute Attr=pMetadata.nextAttr();
    if (Attr.getValue()!=null || Attr.isMultivalued() && !Attr.getValuesList().isEmpty())
        S.append(Attr.getName().toLowerCase()).append("=").append(Attr.Export().toLowerCase()).append("\n");
    }
if (Bytes!=null)
    {
    Convert(Bytes);
    doc.add(new TextField( F_FULLTEXT, S.toString()+getFileMetadata().toLowerCase()+"\n"+getFullText(), Field.Store.NO));
    }
else
    doc.add(new TextField( F_FULLTEXT, S.toString(), Field.Store.NO));
iwriter.addDocument(doc);
iwriter.commit();
SM.maybeRefresh();
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
iwriter.deleteDocuments(new Term(F_ID,Id));
iwriter.commit();
SM.maybeRefresh();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_deleting_doc_FT", ex.getLocalizedMessage());
    }
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Type
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
IndexSearcher isearcher=null;
try {  
isearcher=SM.acquire();
sBody=sBody.toLowerCase();
Query query = new QueryParser(F_FULLTEXT,analyzer).parse(sBody);
ScoreDoc[] hits = isearcher.search(query, MAXRESULTS).scoreDocs;
for (ScoreDoc hit : hits)
    Res.add(isearcher.doc(hit.doc).get(F_ID));
SM.release(isearcher);
//ireader.close();
//directory.close();
} catch (Exception ex)
    {
    try {    
    SM.release(isearcher);   
    } catch (Exception e)
    {}
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
if (GlobLang==null)     
    return(new StandardAnalyzer());
switch (GlobLang)
    {
    case "ES":  if (SW==null)
                   return(new SpanishAnalyzer());
                else
                    return(new SpanishAnalyzer(SW));
    case "EN":  if (SW==null)
                   return(new EnglishAnalyzer());
                else
                    return(new EnglishAnalyzer(SW));
    case "PT":  if (SW==null)
                   return(new PortugueseAnalyzer());
                else
                    return(new PortugueseAnalyzer(SW));
    case "CT":  if (SW==null)
                   return(new CatalanAnalyzer());
                else
                    return(new CatalanAnalyzer(SW));
    default:    if (SW==null)
                   return(new StandardAnalyzer());
                else
                    return(new StandardAnalyzer(SW));
    }
}
//-------------------------------------------------------------------------
@Override
protected void setLang(String pLang)
{
if (pLang!=null)    
    {
    GlobLang = pLang.toUpperCase();
    super.setLang(pLang);
    }
}
//-------------------------------------------------------------------------
@Override
void setSWFile(String FileSW) throws PDException
{
BufferedReader br =null;    
try {    
SW= new CharArraySet(100, true);
br = new BufferedReader(new FileReader(FileSW));
String strLine;
while ((strLine = br.readLine()) != null)   
    {
    if (!strLine.trim().startsWith("#"))    
       SW.add(strLine.trim().toLowerCase());
    }
} catch (Exception Ex)
    {
    SW=null;
    PDException.GenPDException(Ex.getMessage(), FileSW);
    }
finally
    {
    if (br!=null)
        try {
        br.close();
        } catch (Exception e)
        {}
    }
}
//-------------------------------------------------------------------------    
}
