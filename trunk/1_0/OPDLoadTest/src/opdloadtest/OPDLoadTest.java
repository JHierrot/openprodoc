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
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
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
static final SimpleDateFormat formatterTS = new SimpleDateFormat("yyyyMMddHHmmss");


//static final int NumTask=20;
//static final int NumFolders=50;
//static final int NumDocs=10;

static int MaxThreads=0;
static  int MaxDocs=0;
static ArrayList NumThreads=new ArrayList();
static ArrayList NumDocs=new ArrayList();
static ArrayList DocType=new ArrayList();
// static String DocType="PD_DOCS";
static private LoadTask lt[];  
static String FileOrig="Image100k.jpg"; 
static String FileLog="logs/Log"+formatterTS.format(new Date())+".log"; 
//------------------------------------------------
/**
 *
 * @param args
 */
public static void main(String[] args)
{
try {
System.out.println("Test Started:"+new Date());
System.out.println("**************************************************");
LoadConf();
PrepareFiles(FileOrig, MaxThreads, MaxDocs);
InitAll();
long t1;
long t2;
long t3;
long t4;
long t5;
for (int i = 0; i < DocType.size(); i++)
    {
    t1=new Date().getTime();
    InsertOPD((String)DocType.get(i), (Integer)NumThreads.get(i), (Integer)NumDocs.get(i));
    System.out.println("Inserted:"+new Date());
    t2=new Date().getTime();
    DownOPD((String)DocType.get(i), (Integer)NumThreads.get(i), (Integer)NumDocs.get(i));
    System.out.println("Down:"+new Date());
    t3=new Date().getTime();
    DeleteOPD((String)DocType.get(i), (Integer)NumThreads.get(i), (Integer)NumDocs.get(i));
    System.out.println("Deleted:"+new Date());
    t4=new Date().getTime();   
    PurgeOPD((String)DocType.get(i), (Integer)NumThreads.get(i), (Integer)NumDocs.get(i));
    System.out.println("Purged:"+new Date());
    t5=new Date().getTime();   
    Trace(FileOrig, (String)DocType.get(i), (Integer)NumThreads.get(i), (Integer)NumDocs.get(i), (t2-t1)/1000, (t3-t2)/1000, (t4-t3)/1000, (t5-t4)/1000);
    DeleteDownFiles((Integer)NumThreads.get(i), (Integer)NumDocs.get(i));
    }
EndAll();
DeleteFiles(MaxThreads, MaxDocs);
System.out.println("Test Ended:"+new Date());
System.out.println("**************************************************");
System.exit(0);
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
StressFold.delete();    
System.out.println("Disconnecting .... ");    
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
System.out.println("Deleting in:"+pMaxTask+" task * "+ pMaxDocs+ " docs ="+ (pMaxTask*pMaxDocs) );    
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
/**
 * Delets all files created to be inserted
 * @param pMaxTask
 * @param pMaxDocs
 * @throws Exception 
 */
private static void DeleteDownFiles( int pMaxTask, int pMaxDocs) throws Exception
{
File DirOut=new File("out");
File LisOut[]=DirOut.listFiles();
System.out.println("Deleting down:"+pMaxTask+" task * "+ pMaxDocs+ " docs ="+ (pMaxTask*pMaxDocs)+" ********** "+LisOut.length);    
File file2del;
for (int i = 0; i < LisOut.length; i++)
    {
    file2del = LisOut[i];
    file2del.delete();    
    }
}
//------------------------------------------------
private static void InsertOPD(String DocType, int NumThreads, int NumDocs)
{
Date t1=new Date();    
for (int i = 0; i < NumThreads; i++)
    {
    lt[i]=new LoadTask(i, DocType, NumDocs);   
    lt[i].ModeIns();
    lt[i].start();
    }
boolean Salir=false;    
while (!Salir)
    {
    Salir=true;
    for (int i = 0; i < NumThreads; i++)
        {
        if (lt[i]!=null)  
            if (lt[i].Running)
                Salir=false;
            else
                lt[i]=null;
        }
    }
Date t2=new Date();
System.out.println("Insert"+FileOrig+";"+DocType+";"+NumThreads+";"+MaxDocs+";"+ (t2.getTime()-t1.getTime()) );
}
//------------------------------------------------
private static void DeleteOPD(String DocType, int NumThreads, int NumDocs)  throws Exception
{
Date t1=new Date();    
for (int i = 0; i < NumThreads; i++)
    {
    lt[i]=new LoadTask(i, DocType, NumDocs);   
    lt[i].ModeDel();
    lt[i].start();
    }
boolean Salir=false;    
while (!Salir)
    {
    Salir=true;
    for (int i = 0; i < NumThreads; i++)
        {
        if (lt[i]!=null)  
            if (lt[i].Running)
                Salir=false;
            else
                lt[i]=null;
        }
    }
Date t2=new Date();
System.out.println("Delete"+FileOrig+";"+DocType+";"+NumThreads+";"+NumDocs+";"+ (t2.getTime()-t1.getTime()) );
}
//------------------------------------------------
private static void DownOPD(String DocType, int NumThreads, int NumDocs)  throws Exception
{
Date t1=new Date();    
for (int i = 0; i < NumThreads; i++)
    {
    lt[i]=new LoadTask(i, DocType, NumDocs);   
    lt[i].ModeDown();
    lt[i].start();
    }
boolean Salir=false;    
while (!Salir)
    {
    Salir=true;
    for (int i = 0; i < NumThreads; i++)
        {
        if (lt[i]!=null)  
            if (lt[i].Running)
                Salir=false;
            else
                lt[i]=null;
        }
    }
Date t2=new Date();
System.out.println("Down"+FileOrig+";"+DocType+";"+NumThreads+";"+NumDocs+";"+ (t2.getTime()-t1.getTime()) );
}
//------------------------------------------------
private static void PurgeOPD(String DocType, int NumThreads, int NumDocs)  throws Exception
{
Date t1=new Date();    
for (int i = 0; i < NumThreads; i++)
    {
    lt[i]=new LoadTask(i, DocType, NumDocs);   
    lt[i].ModePurge();
    lt[i].start();
    }
boolean Salir=false;    
while (!Salir)
    {
    Salir=true;
    for (int i = 0; i < NumThreads; i++)
        {
        if (lt[i]!=null)  
            if (lt[i].Running)
                Salir=false;
            else
                lt[i]=null;
        }
    }
Date t2=new Date();
System.out.println("Purge"+FileOrig+";"+DocType+";"+NumThreads+";"+NumDocs+";"+ (t2.getTime()-t1.getTime()) );
}
//------------------------------------------------
/**
 * Load de list of test to execute
 */
private static void LoadConf() throws Exception
{
Properties LoadProperties = new Properties();
LoadProperties.load(new FileInputStream("load.props"));
int Total;
Total=new Integer(LoadProperties.getProperty("Total")).intValue();
FileOrig=LoadProperties.getProperty("FileOrig");
Integer NumT;
Integer NumD;
for (int i = 0; i < Total; i++)
    {
    String[] Test=LoadProperties.getProperty("Test"+i).split(";");
    DocType.add(Test[0]);    
    NumT=new Integer(Test[1]);
    NumThreads.add(NumT);   
    if (NumT>MaxThreads)
        MaxThreads=NumT;
    NumD=new Integer(Test[2]);
    NumDocs.add(NumD);    
    if (NumD>MaxDocs)
        MaxDocs=NumD;
    }
lt=new LoadTask[MaxThreads];
System.out.println("Total:"+Total+" FileOrig:"+FileOrig+" DocType:"+DocType+" NumThreads:"+NumThreads+" NumDocs:"+NumDocs);
FileWriter F=new FileWriter(FileLog, true);
F.write("FileSize;DocType;NumThreads;NumDocs;tInsert;tDown;tDel;tPurge\n");
F.close();

}
//------------------------------------------------
/**
 * Saves all the data collected
 * @param FileOrig
 * @param string
 * @param integer
 * @param integer0
 * @param t1
 * @param t2
 * @param t3 
 */
private static void Trace(String FileOrig, String pDocType, Integer pNumThreads, Integer pNumDocs, long tIns, long tDown, long tDel, long tPurge)  throws Exception
{
StringBuilder S=new StringBuilder(100);    
FileWriter F=new FileWriter(FileLog, true);
S.append(FileOrig).append(";").append(pDocType).append(";").append(pNumThreads).append(";").append(pNumDocs).append(";").append(tIns).append(";").append(tDown).append(";").append(tDel).append(";").append(tPurge).append("\n");
F.write(S.toString());
F.close();
}
//------------------------------------------------

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
private DriverGeneric Session=null;
public boolean Running=true;
private static final int INSERT=1;
private static final int DOWN=2;
private static final int DELETE=3;
private static final int PURGE=4;
private int Status=0;
//------------------------------------------------
/**
 *
 * @param Id
 */
private LoadTask(int Id, String pDocType, int pMaxDocs)
{
this.setName("LT_"+Id);    
TaskId=Id;    
DocType=pDocType;
NumDocs=pMaxDocs;
}
//------------------------------------------------
@Override
public void run()
{
try {       
//System.out.println("Task Started:"+TaskId);
//System.out.println("------------------------");    
Session = ProdocFW.getSession(ConectorName, "root", "root");  
switch(Status)
    {case INSERT:
        GenLoadFolders();
        GenLoadDocs();    
        break;
     case DOWN:
        DownFiles();
        break;
     case DELETE:
        DelFolders();
        break;
     case PURGE:
        PurgeFiles();
        break;
    }
ProdocFW.freeSesion(ConectorName, Session);
//System.out.println("Task Ended:"+TaskId);
//System.out.println("------------------------");    
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
//System.out.println("Task Docs Startd:"+TaskId+"  "+new Date());
for (int NFold = 0; NFold < NumFolders; NFold++)
    {
    for (int NDoc = 0; NDoc < NumDocs; NDoc++)
        {
        PDDocs Doc=new PDDocs(Session, DocType);
        Doc.setPDId("Doc_"+TaskId+"_"+NFold+"_"+NDoc);
        Doc.setTitle("Doc_"+TaskId+"_"+NFold+"_"+NDoc);
        Doc.setParentId("TestStress_"+TaskId+"_"+NFold);
        Doc.setFile("in/TaskFold"+TaskId+"/Test"+TaskId+"_"+NDoc+".jpg");
        Doc.insert();            
        }
    }
//System.out.println("Task Docs Ended:"+TaskId+"  "+new Date());
}
//------------------------------------------------
private void DownFiles() throws PDException
{
for (int NFold = 0; NFold < NumFolders; NFold++)
    {
    for (int NDoc = 0; NDoc < NumDocs; NDoc++)
        {
        PDDocs Doc=new PDDocs(Session, DocType);
        Doc.setPDId("Doc_"+TaskId+"_"+NFold+"_"+NDoc);
        Doc.getFile("out");
        }
    }
//System.out.println("Purge Docs Ended:"+TaskId+"  "+new Date());
}
//------------------------------------------------
private void DelFolders() throws PDException
{
for (int i = 0; i < NumFolders; i++)
    {
    PDFolders Fold=new PDFolders(Session);
    Fold.setPDId("TestStress_"+TaskId+"_"+i);
    Fold.delete();
    }
//System.out.println("Del Folders Ended:"+TaskId+"  "+new Date());
}
//------------------------------------------------
private void PurgeFiles() throws PDException
{
for (int NFold = 0; NFold < NumFolders; NFold++)
    {
    for (int NDoc = 0; NDoc < NumDocs; NDoc++)
        {
        PDDocs Doc=new PDDocs(Session, DocType);
        Doc.Purge(DocType,"Doc_"+TaskId+"_"+NFold+"_"+NDoc);
        }
    }
//System.out.println("Purge Docs Ended:"+TaskId+"  "+new Date());
}
//------------------------------------------------
private void ModeIns()
{
Status=INSERT;
}
//------------------------------------------------
private void ModeDel()
{
Status=DELETE;
}
//------------------------------------------------
private void ModePurge()
{
Status=PURGE;
}
//------------------------------------------------
private void ModeDown()
{
Status=DOWN;
}
//------------------------------------------------
}
//***************************************************************************

}
