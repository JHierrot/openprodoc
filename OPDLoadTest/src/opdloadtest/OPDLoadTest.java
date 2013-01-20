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
 * author: Joaquin Hierro      2012
 * 
 */

package opdloadtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import prodoc.*;

/**
 * Executes a set of load test in OpenProdoc
 * @author jhierrot
 */
public class OPDLoadTest
{
static private DriverGeneric MainSession = null;  
static private PDFolders StressFold=null;
static final String ConectorName="PD";


//static final int NumTask=20;
//static final int NumFolders=50;
//static final int NumDocs=10;

static int MaxThreads=5;
static  int MaxDocs=100;
static private LoadTask lt[]=new LoadTask[MaxThreads];  
//------------------------------------------------
/**
 *
 * @param args
 */
public static void main(String[] args)
{
try {
String FileOrig="Image100k.jpg"; // String FileOrig="Image200k.jpg";
String DocType="PD_DOCS";
System.out.println("Test Started:"+new Date());
System.out.println("**************************************************");
PrepareFiles(FileOrig, MaxThreads, MaxDocs);
InitAll();
InsertOPD(FileOrig, "PD_DOCS", 2, 3);
DeleteOPD("PD_DOCS", 2, 3);
EndAll();
DeleteFiles(MaxThreads, MaxDocs);
System.out.println("Test Ended:"+new Date());
System.out.println("**************************************************");
} catch(Exception ex)
    {
    System.out.println(ex.getLocalizedMessage());
    ex.printStackTrace();
    }
}
//------------------------------------------------
/*
 * Init Connection and OPD Environ
 */
private static void InitAll() throws PDException
{
System.out.println("Connecting .... ");    
ProdocFW.InitProdoc(ConectorName, "Prodoc.properties");
MainSession = ProdocFW.getSession(ConectorName, "root", "root"); 
StressFold=new PDFolders(MainSession);
StressFold.setPDId("TestStress");
StressFold.setTitle("TestStress");
StressFold.setParentId(PDFolders.ROOTFOLDER);
StressFold.insert();
}
//------------------------------------------------
/*
 * Clean OPD Environ and Closes Connection
 */
private static void EndAll() throws PDException
{
System.out.println("Disconnecting .... ");    
StressFold=new PDFolders(MainSession);
StressFold.setPDId("TestStress");
StressFold.delete();
ProdocFW.freeSesion(ConectorName, MainSession); 
ProdocFW.ShutdownProdoc(ConectorName);

}

//------------------------------------------------
/**
 * Creates a copy of the files to be inserted in OPD 
 * it's more real to insert diferente files in Server than the same file
 * @param pFileOrig Original file 
 * @param pMaxThreads Maximun num of thread
 * @param pMaxDocs Maximun num of docs / thread
 * @throws Exception 
 */
private static void PrepareFiles(String pFileOrig, int pMaxThreads, int pMaxDocs)  throws Exception
{
System.out.println("Copying:"+pFileOrig + " to "+pMaxThreads+" task * "+ pMaxDocs+ " docs ="+ (pMaxThreads*pMaxDocs) );    
byte[] buf = new byte[102400];
for (int i = 0; i < pMaxThreads; i++)
    {
    File TaskFold=new File("in/TaskFold"+i);
    if (!TaskFold.mkdirs())  
        {
        System.out.println("error creating:[in/TaskFold"+i+"]");
        return;
        }
    for (int j = 0; j < pMaxDocs; j++)
        {
        String dest = "in/TaskFold"+i+"/Test"+i+"_"+j+".jpg";
	InputStream in = new FileInputStream(pFileOrig);
	OutputStream out = new FileOutputStream(dest);
	int len;
	while ((len = in.read(buf)) > 0) 
            {
	    out.write(buf, 0, len);
            }
	in.close();
	out.close();     
        }    
    }
}
//------------------------------------------------
/**
 * Delets all files created to be inserted
 * @param pMaxTask
 * @param pMaxDocs
 * @throws Exception 
 */
private static void DeleteFiles( int pMaxTask, int pMaxDocs) throws Exception
{
System.out.println("Deleting:"+pMaxTask+" task * "+ pMaxDocs+ " docs ="+ (pMaxTask*pMaxDocs) );    
for (int i = 0; i < pMaxTask; i++)
    {
    File TaskFold=new File("in/TaskFold"+i);
    for (int j = 0; j < pMaxDocs; j++)
        {
        File File2del=new File("in/TaskFold"+i+"/Test"+i+"_"+j+".jpg");
        File2del.delete();        
        }    
    TaskFold.delete();   
    }
}
//------------------------------------------------
private static void InsertOPD(String FileOrig, String DocType, int NumThreads, int MaxDocs)
{
Date t1=new Date();    
for (int i = 0; i < NumThreads; i++)
    {
    lt[i]=new LoadTask(i, FileOrig, DocType, MaxDocs);   
    lt[i].start();
    }
boolean Salir=false;    
while (!Salir)
    {
    Salir=true;
    for (int i = 0; i < NumThreads; i++)
        {
        if (lt[i].Running)
            Salir=false;
        }
    }
Date t2=new Date();
System.out.println(FileOrig+";"+DocType+";"+NumThreads+";"+MaxDocs+";"+ (t2.getTime()-t1.getTime()) );
}
//------------------------------------------------
private static void DeleteOPD(String DocType, int NumThreads, int NumDocs)  throws Exception
{
StressFold.delete();
for (int TaskId = 0; TaskId < NumThreads; TaskId++)
    {
    for (int NFold = 0; NFold < 1; NFold++)
        {
        for (int NDoc = 0; NDoc < NumDocs; NDoc++)
            {
            PDDocs Doc=new PDDocs(MainSession);
            Doc.Purge(DocType, "Doc_"+TaskId+"_"+NFold+"_"+NDoc);
            }
        }
    }
}

//***************************************************************************
/**
 *
 */
static public class LoadTask extends Thread
{
private int TaskId=0;
private String DocType;
private int NumDocs;
private int NumFolders=1;
private String FileOrig;
private DriverGeneric Session=null;
public boolean Running=true;
//------------------------------------------------
/**
 *
 * @param Id
 */
private LoadTask(int Id, String pFileOrig, String pDocType, int pMaxDocs)
{
TaskId=Id;    
DocType=pDocType;
NumDocs=pMaxDocs;
FileOrig=pFileOrig;
}
//------------------------------------------------
@Override
public void run()
{
try {       
System.out.println("Task Started:"+TaskId);
System.out.println("------------------------");    
Session = ProdocFW.getSession(ConectorName, "root", "root");  
GenLoadFolders();
GenLoadDocs();
ProdocFW.freeSesion(ConectorName, Session);
System.out.println("Task Ended:"+TaskId);
System.out.println("------------------------");    
Running=false;
} catch(Exception ex)
    {
    System.out.println(ex.getLocalizedMessage());
    ex.printStackTrace();
    }
}
//------------------------------------------------
private void GenLoadFolders() throws PDException
{
for (int i = 0; i < NumFolders; i++)
    {
    PDFolders Fold=new PDFolders(Session);
    Fold.setPDId("TestStress_"+TaskId+"_"+i);
    Fold.setTitle("TestStress_"+TaskId+"_"+i);
    Fold.setParentId("TestStress");
    Fold.insert();
    }
//System.out.println("Task Folders Ended:"+TaskId);
}
//------------------------------------------------
private void GenLoadDocs() throws PDException
{
System.out.println("Task Docs Startd:"+TaskId+"  "+new Date());
for (int NFold = 0; NFold < NumFolders; NFold++)
    {
    for (int NDoc = 0; NDoc < NumDocs; NDoc++)
        {
        PDDocs Doc=new PDDocs(Session, DocType);
        Doc.setPDId("Doc_"+TaskId+"_"+NFold+"_"+NDoc);
        Doc.setTitle("Doc_"+TaskId+"_"+NFold+"_"+NDoc);
        Doc.setParentId("TestStress_"+TaskId+"_"+NFold);
        // Doc.getRecSum().getAttr("At1").setValue("At1_"+TaskId+"_"+NFold+"_"+NDoc);
        Doc.setFile(FileOrig);
        Doc.insert();            
        }
    }
//System.out.println("Task Docs Ended:"+TaskId+"  "+new Date());
}
//------------------------------------------------
 }
//***************************************************************************

}
