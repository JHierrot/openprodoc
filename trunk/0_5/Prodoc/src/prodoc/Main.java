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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;


/**
 *
 * @author jhierrot
 */
public class Main
{
//--------------------------------------------------------------------------
    /**
     *
     * @param Sesion
     * @throws PDException
     */
    static void TestUser(DriverGeneric Sesion)  throws PDException
{
System.out.println("Test Usuarios ----------------------");
PDUser Usu=new PDUser(Sesion);
Usu.setName("Usu1");
Usu.setPassword("Juan");
Usu.setDescription("Es Juan");
Usu.setActive(true);
Usu.seteMail("Juan@juan.es");
Usu.setValidation("DDBB");
Usu.setRole("Administrators");
Usu.insert();
PDUser Usu2=new PDUser(Sesion);
Usu2.Load("Usu1");
if (Usu.getRecord().equals(Usu2.getRecord()))
   System.out.println("Alta OK");
else
   System.out.println("Alta Error");
//------
Usu.setName("Usu1");
Usu.setPassword("Juan2");
Usu.setDescription("Es Juan2");
Usu.setActive(false);
Usu.seteMail("Juan2@juan.es");
Usu.setValidation("LDAP");
Usu.setValidation("LDAP");
Usu.setRole("Users");
Usu.update();
Usu2.Load("Usu1");
if (Usu.getRecord().equals(Usu2.getRecord()))
   System.out.println("Modificación OK");
else
   System.out.println("Modificación Error");
Usu.delete();
if (Usu2.Load("Usu1")==null)
   System.out.println("Baja OK");
else
   System.out.println("Baja Error");
System.out.println("Fin Test Usuarios ----------------------");
}
//--------------------------------------------------------------------------
    /**
     *
     * @param Sesion
     * @throws PDException
     */
    static void TestGroup(DriverGeneric Sesion)  throws PDException
{
PDACL Ac=new PDACL(Sesion);
Ac.setName("tmp");
Ac.insert();
System.out.println("Test Grupos ----------------------");
PDGroups Grp1=new PDGroups(Sesion);
Grp1.setName("Grupo1");
Grp1.setDescription("Es Juan");
Grp1.setAcl("Administrators");
Grp1.insert();
PDGroups GrpT=new PDGroups(Sesion);
GrpT.Load("Grupo1");
if (Grp1.getRecord().equals(GrpT.getRecord()))
   System.out.println("Alta OK");
else
   System.out.println("Alta Error");
//------
Grp1.setName("Grupo1");
Grp1.setDescription("Es Juan2");
Grp1.setAcl("tmp");
Grp1.update();
GrpT.Load("Grupo1");
if (Grp1.getRecord().equals(GrpT.getRecord()))
   System.out.println("Modificación OK");
else
   System.out.println("Modificación Error");
//------
PDUser Usu=new PDUser(Sesion);
Usu.setName("Usu1");
Usu.setPassword("Juan");
Usu.setDescription("Es Juan");
Usu.setActive(true);
Usu.seteMail("Juan@juan.es");
Usu.setValidation("DDBB");
Usu.setRole("Users");
Usu.insert();
Grp1.addUser("Usu1");
Grp1.InitListUsers();
String User=Grp1.NextUsers();
if (User!=null && User.equals(Usu.getName()))
   System.out.println("AddUser OK");
else
   System.out.println("AddUser Error");
User=Grp1.NextUsers();
if (User!=null)
   System.out.println("AddUser Error múltiples usuarios");
//--------
Grp1.delUser("Usu1");
Grp1.InitListUsers();
User=Grp1.NextUsers();
if (User==null)
   System.out.println("delUser OK");
else
   System.out.println("delUser Error");
//------
PDGroups Grp2=new PDGroups(Sesion);
Grp2.setName("Grp2");
Grp2.setDescription("Es Grp2");
Grp2.setAcl("Administrators");
Grp2.insert();
Grp1.addGroup(Grp2.getName());
Grp1.InitListMembers();
String Gr=Grp1.NextMember();
if (Gr!=null && Gr.equals(Grp2.getName()))
   System.out.println("AddGrp OK");
else
   System.out.println("AddGrp Error");
Gr=Grp1.NextMember();
if (Gr!=null)
   System.out.println("AddGrp Error múltiples Grupos");
//--------
Grp2.InitGroupMemberShip();
Gr=Grp2.NextGroupMemberShip();
if (Gr!=null && Gr.equals(Grp1.getName()))
   System.out.println("AddGrp2 OK");
else
   System.out.println("AddGrp2 Error");
//--------
PDGroups Grp3=new PDGroups(Sesion);
Grp3.setName("Grp3");
Grp3.setDescription("Es Grp3");
Grp3.setAcl("Administrators");
Grp3.insert();
Grp2.addGroup(Grp3.getName());
Grp3.addUser(Usu.getName());
if (Grp3.DirectUserMemberShip(Usu.getName()).size()==1)
   System.out.println("Partial MS OK");
else
   System.out.println("Partial MS Error");
//System.out.println("PartialMS"+Grp3.DirectUserMemberShip(Usu.getName()));
if (Grp3.FullUserMemberShip(Usu.getName()).size()==3)
   System.out.println("Full MS OK");
else
   System.out.println("Full MS Error");
//System.out.println("FullMS"+Grp3.FullUserMemberShip(Usu.getName()));
//--------
Grp1.delGroup(Grp2.getName());
Grp1.InitListMembers();
Gr=Grp1.NextMember();
if (Gr==null)
   System.out.println("delGrp OK");
else
   System.out.println("delGrp Error");
Grp2.delete();
Grp3.delete();
Usu.delete();
//-----
Grp1.delete();
if (Grp2.Load("Grupo1")==null)
   System.out.println("Baja OK");
else
   System.out.println("Baja Error");
System.out.println("Fin Test Grupos ----------------------");
Ac.delete();
}
//--------------------------------------------------------------------------
    /**
     *
     * @param Sesion
     * @throws PDException
     */
    static void TestRepository(DriverGeneric Sesion)  throws PDException
{
System.out.println("Test Repositorio ----------------------");
PDRepository Rep=new PDRepository(Sesion);
Rep.setName("Repos1");
Rep.setUser("Juan");
Rep.setPassword("JuanPass");
Rep.setDescription("Es Repos1");
Rep.setURL("Url1");
Rep.setParam("Param1");
Rep.insert();
PDRepository Rep2=new PDRepository(Sesion);
Rep2.Load(Rep.getName());
if (Rep.getRecord().equals(Rep2.getRecord()))
   System.out.println("Alta OK");
else
   System.out.println("Alta Error");
//------
Rep.setUser("Juan2");
Rep.setPassword("JuanPass2");
Rep.setDescription("Es Repos1-2");
Rep.setURL("Url1-2");
Rep.setParam("Param1-2");
Rep.update();
Rep2.Load(Rep.getName());
if (Rep.getRecord().equals(Rep2.getRecord()))
   System.out.println("Modificación OK");
else
   System.out.println("Modificación Error");
Rep.delete();
if (Rep2.Load(Rep.getName())==null)
   System.out.println("Baja OK");
else
   System.out.println("Baja Error");
System.out.println("Fin Test Repositorio ----------------------");
}
//--------------------------------------------------------------------------
/**
 *
 * @param Sesion
 * @throws PDException
 */
static void TestMimeType(DriverGeneric Sesion)  throws PDException
{
System.out.println("Test Tipos Mime ----------------------");
PDMimeType Mime=new PDMimeType(Sesion);
Mime.setName("app/doc");
Mime.setDescription("Es doc");
Mime.setExtension("doc");
Mime.insert();
PDMimeType Mime2=new PDMimeType(Sesion);
Mime2.Load(Mime.getName());
if (Mime.getRecord().equals(Mime2.getRecord()))
   System.out.println("Alta OK");
else
   System.out.println("Alta Error");
//------
Mime.setDescription("Es doc2");
Mime.setExtension("doc2");
Mime.update();
Mime2.Load(Mime.getName());
if (Mime.getRecord().equals(Mime2.getRecord()))
   System.out.println("Modificación OK");
else
   System.out.println("Modificación Error");
Mime.delete();
if (Mime2.Load(Mime.getName())==null)
   System.out.println("Baja OK");
else
   System.out.println("Baja Error");
System.out.println("Fin Test Tipos Mime ----------------------");
}
//--------------------------------------------------------------------------
/**
 *
 * @param Sesion
 * @throws PDException
 */
static void TestFolder(DriverGeneric Sesion)  throws PDException
{
System.out.println("Test Carpetas ----------------------");
PDFolders f=new PDFolders(Sesion);
f.setTitle("Carpeta Padre");
f.setACL("Public");
f.setPDId("Carp1");
f.CreateRootFolder();
PDFolders f2=new PDFolders(Sesion);
f2.Load(f.getPDId());
System.out.println("F:"+f.getRecord());
System.out.println("F2:"+f2.getRecord());
if (f.getRecord().equals(f2.getRecord()))
   System.out.println("Alta OK");
else
   System.out.println("Alta Error");
f.setTitle("Carpeta Padre modificado");
f.setACL("Administrators");
f.update();
f2.Load(f.getPDId());
System.out.println("F:"+f.getRecord());
System.out.println("F2:"+f2.getRecord());
if (f.getRecord().equals(f2.getRecord()))
   System.out.println("Update OK");
else
   System.out.println("Update Error");
PDFolders f1=new PDFolders(Sesion);
for (int i = 2; i < 6; i++)
    {
    f1.setPDId("Carp"+i);
    f1.setTitle("Carpeta Hija"+i);
    f1.setACL("Public");
    f1.setParentId("Carp"+(i-1) );
    f1.insert();
    }
HashSet h=f1.getListGrandParentList(f1.getPDId());
if (h.size()!=5)
   System.out.println("Jerarquia 1 OK");
else
   System.out.println("Jerarquia 1 Error");
HashSet hh=f1.getListDirectDescendList(f1.getPDId());
if (hh.size()!=1)
   System.out.println("Jerarquia 2 OK");
else
   System.out.println("Jerarquia 2 Error");
HashSet hd=f.getListDescendList(f.getPDId());
if (hd.size()!=5)
   System.out.println("Jerarquia 3 OK");
else
   System.out.println("Jerarquia 3 Error");
//f.delete();
System.out.println("Fin Test Carpetas ----------------------");
}

//--------------------------------------------------------------------------
/**
 *
 * @param DB
 * @throws PDException
 */
static void Instalar(String DB)  throws PDException
{
DriverGeneric Sesion=null;
Sesion=ProdocFW.getSession(DB, "Install", "Install");
//Sesion.Uninstall();
//Sesion.Install();
ProdocFW.freeSesion(DB, Sesion);
}
//--------------------------------------------------------------------------

/**
 *
 * @param Sesion
 * @throws PDException
 */
private static void TestDefFolder(DriverGeneric Sesion) throws PDException
{
PDObjDefs Def=new PDObjDefs(Sesion);
Def.setName("Expediente");
Def.setParent(PDFolders.getTableName());
Def.setClassType(PDObjDefs.CT_FOLDER);
Def.setACL("Administrators");
Def.setActive(true);
Def.setDescription("Expediente de puebas");
Def.insert();
Def.addAtribute( new Attribute("DOCEXP","DOCEXP", "Código del Expediente", Attribute.tSTRING, true, null, 32, false, true, true));
Def.CreateObjectTables("Expediente", true);
PDFolders FolDef=new PDFolders(Sesion, "Expediente");
FolDef.setTitle("Carpeta prueba");
FolDef.setACL("Public");
// FolDef.setPDId("CarpetaExp");
FolDef.setTitle("CarpetaExp");
FolDef.setParentId("RootFolder");
Record r=FolDef.getRecSum();
Attribute A=r.getAttr("DOCEXP");
A.setValue("CodExp1");
FolDef.assignValues(r);
System.out.println("Record FolDef:"+FolDef.getRecSum());
FolDef.insert();
PDFolders FolDef2=new PDFolders(Sesion);
FolDef2.Load(FolDef.getPDId());
System.out.println("Record FolDef2:"+FolDef2.getRecSum());
FolDef2.LoadFull(FolDef.getPDId());
System.out.println("Record FolDef2 Full:"+FolDef2.getRecSum());
//if (FolDef2.getRecSum().equals(FolDef.getRecSum()))
//    System.out.println("Definición: OK");
//else
//    System.out.println("Definición: ERROR");
FolDef.delete();
Def.DeleteObjectTables("Expediente");
Def.delete();
}
//--------------------------------------------------------------------------
/**
 *
 * @param Sesion
 * @throws PDException
 */
private static void TextExportImport(DriverGeneric Sesion) throws PDException
{
PDUser User=new PDUser(Sesion);
Cursor CursorId=User.SearchLike("*");
String TipoObj="PDUser";
PrintWriter PW;
try {
PW = new PrintWriter("/tmp/Userlist.txt");
Sesion.Export(PW, TipoObj, CursorId);
PW.flush();
PW.close();
System.out.println("Export Cursor 1: OK");
//------------------------------------------
Vector V=new Vector();
CursorId=User.SearchLike("*");
Record Rec=Sesion.NextRec(CursorId);
while (Rec!=null)
    {
    V.add(Rec);
    System.out.println("Record"+Rec);
    Rec=Sesion.NextRec(CursorId);
    }
PW = new PrintWriter("/tmp/Userlist2.txt");
Sesion.Export(PW, TipoObj, V);
PW.flush();
PW.close();
System.out.println("Export Cursor 2: OK");
//------------------------------------------
BufferedReader BR=new BufferedReader(new FileReader("/tmp/Userlist.txt"));
String Linea=BR.readLine();
Record R1=User.getRecord();
while (Linea!=null)
    {  
    Sesion.Import(BR, R1);
    System.out.println("Record"+R1);
    Linea=BR.readLine();
    }
} catch (Exception ex)
    {
    ex.printStackTrace();
    }
}
//--------------------------------------------------------------------------
/**
 *
 */
public static void Tmp()
{
StringBuffer genId = new StringBuffer();
try {
Date d = new Date();
SimpleDateFormat formatterTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
d = formatterTS.parse("2010-03-09 10:11:12");
genId.append(Long.toHexString(d.getTime()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
System.out.println("Fecha:" + d+ " /  "+ genId); genId.delete(0, 32);
d = formatterTS.parse("2010-03-10 10:11:12");
genId.append(Long.toHexString(d.getTime()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
System.out.println("Fecha:" + d+ " /  "+ genId); genId.delete(0, 32);
d = formatterTS.parse("2010-03-15 10:11:12");
genId.append(Long.toHexString(d.getTime()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
System.out.println("Fecha:" + d+ " /  "+ genId); genId.delete(0, 32);
d = formatterTS.parse("2010-04-10 10:11:12");
genId.append(Long.toHexString(d.getTime()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
System.out.println("Fecha:" + d+ " /  "+ genId); genId.delete(0, 32);
} catch (Exception ex)
    {System.out.println("Ex" + ex.getLocalizedMessage());
    }
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 * @throws Exception
 */
public static void TestFSStore() throws PDException, Exception
{
StoreFS SF=new StoreFS("", "", "", "/tmp/pd");
//SF.Insert("123456789Copia", SF.Retrieve("123456710"));
//SF.Create();
FileInputStream in= new FileInputStream("/tmp/readme.htm");
SF.Insert("12345678-1","1.0", in);
in.close();
 in= new FileInputStream("/tmp/workplace_xt_docs.zip");
SF.Insert("12345678_2", "1.0", in);
in.close();
//-------------
//int TAMBUFF=1024*64;
//byte Buffer[]=new byte[TAMBUFF];
//InputStream Bytes;
//File f;
//Bytes= SF.Retrieve("12345679");
//FileOutputStream fo=new FileOutputStream("/tmp/12345679");
//int readed=Bytes.read(Buffer);
//while (readed!=-1)
//    {
//    fo.write(Buffer, 0, readed);
//    readed=Bytes.read(Buffer);
//    }
//Bytes.close();
//fo.close();
//Bytes= SF.Retrieve("123456710");
//fo = new FileOutputStream("/tmp/123456710");
//readed=Bytes.read(Buffer);
//while (readed!=-1)
//    {
//    fo.write(Buffer, 0, readed);
//    readed=Bytes.read(Buffer);
//    }
//Bytes.close();
//fo.close();
}

//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 * @throws Exception
 */
public static void TestDBStore() throws PDException, Exception
{
StoreDDBB StDB=new StoreDDBB("jdbc:derby://localhost:1527/Prodoc", "Prodoc", "Prodoc", "org.apache.derby.jdbc.ClientDriver;STBLOB");
System.out.println("Driver[" +StDB.getDriver()+"] Tabla  ["+ StDB.getTable()+"]");
StDB.Connect();
FileInputStream in= new FileInputStream("/tmp/readme.htm");
StDB.Insert("12345678-1","1.0", in);
int TAMBUFF=1024*64;
byte Buffer[]=new byte[TAMBUFF];
InputStream Bytes;
Bytes= StDB.Retrieve("12345678-1", "1.0");
FileOutputStream fo=new FileOutputStream("/tmp/12345679.htm");
int readed=Bytes.read(Buffer);
while (readed!=-1)
    {
    fo.write(Buffer, 0, readed);
    readed=Bytes.read(Buffer);
    }
Bytes.close();
fo.close();
StDB.Delete("12345678-1","1.0");
StDB.Disconnect();
}
//--------------------------------------------------------------------------
/**
 *
 * @param Sesion
 * @throws PDException
 * @throws IOException
 */
private static void TestVersioning(DriverGeneric Sesion)  throws PDException, IOException
{
PDDocs D=new PDDocs(Sesion);
D.setTitle("Titulo 1 "+new Date());
D.setParentId("Users/root");
D.setFile("/tmp/1.pdf");
D.setDocDate(new Date());
D.setMimeType("application/pdf");
D.insert();
System.out.println("Insert:"); System.in.read();
D.Checkout();
System.out.println("Checkout 1:"); System.in.read();
D.setTitle("Titulo 2 "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/2.pdf");
D.update();
System.out.println("update 1:");System.in.read();
D.Checkin("2.0");
System.out.println("Checkin 1:");System.in.read();
D.Checkout();
System.out.println("Checkout 2:");System.in.read();
D.setTitle("Titulo 3 "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/3.pdf");
D.update();
System.out.println("update 2:"); System.in.read();
D.Checkin("3.0");
System.out.println("Checkin 2:"); System.in.read();
D.Checkout();
System.out.println("Checkout 3:");System.in.read();
D.setTitle("Titulo 4 "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/1.pdf");
D.update();
System.out.println("update 3:"); System.in.read();
D.CancelCheckout();
System.out.println("CancelCheckout 3:"); System.in.read();
D.Checkout();
System.out.println("Checkout 4:");System.in.read();
D.setTitle("Titulo 4b "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/1.pdf");
D.update();
System.out.println("update 4:"); System.in.read();
D.Checkin("3.0B");
System.out.println("Checkin 4:"); System.in.read();
}
//--------------------------------------------------------------------------
/**
 *
 * @param Sesion
 * @throws PDException
 * @throws IOException
 */
private static void TestVersioning2(DriverGeneric Sesion)  throws PDException, IOException
{
PDDocs D=new PDDocs(Sesion, "Prueba");
D.setTitle("Titulo 1 "+new Date());
D.setParentId("Users/root");
D.setFile("/tmp/1.pdf");
D.setDocDate(new Date());
D.setMimeType("application/pdf");
Record R=D.getRecSum();
Attribute A=R.getAttr("AttrDate");
A.setValue(new Date());
A=R.getAttr("AttrString");
A.setValue("AttrString 0");
A=R.getAttr("AttrNum");
A.setValue(0);
D.insert();
System.out.println("Insert:"); System.in.read();
D.Checkout();
System.out.println("Checkout 1:"); System.in.read();
D.setTitle("Titulo 2 "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/2.pdf");
R=D.getRecSum();
A=R.getAttr("AttrString");
A.setValue("AttrString 1");
A=R.getAttr("AttrNum");
A.setValue(1);
D.update();
System.out.println("update 1:");System.in.read();
D.Checkin("2.0");
System.out.println("Checkin 1:");System.in.read();
D.Checkout();
System.out.println("Checkout 2:");System.in.read();
D.setTitle("Titulo 3 "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/3.pdf");
R=D.getRecSum();
A=R.getAttr("AttrString");
A.setValue("AttrString 2");
A=R.getAttr("AttrNum");
A.setValue(2);
D.update();
System.out.println("update 2:"); System.in.read();
D.Checkin("3.0");
System.out.println("Checkin 2:"); System.in.read();
D.Checkout();
System.out.println("Checkout 3:");System.in.read();
D.setTitle("Titulo 4 "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/1.pdf");
R=D.getRecSum();
A=R.getAttr("AttrString");
A.setValue("AttrString 3");
A=R.getAttr("AttrNum");
A.setValue(3);
D.update();
System.out.println("update 3:"); System.in.read();
D.CancelCheckout();
System.out.println("CancelCheckout 3:"); System.in.read();
D.Checkout();
System.out.println("Checkout 4:");System.in.read();
D.setTitle("Titulo 4b "+new Date());
D.setDocDate(new Date());
D.setFile("/tmp/1.pdf");
R=D.getRecSum();
A=R.getAttr("AttrString");
A.setValue("AttrString 4b");
A=R.getAttr("AttrNum");
A.setValue(4);
D.update();
System.out.println("update 4:"); System.in.read();
D.Checkin("3.0B");
System.out.println("Checkin 4:"); System.in.read();
}
//--------------------------------------------------------------------------
/**
 *
 * @param Sesion
 * @throws PDException
 * @throws IOException
 */
private static void TestVersioning3(DriverGeneric Sesion)  throws PDException, IOException
{
PDDocs D=new PDDocs(Sesion);
D.setTitle("Titulo 1 "+new Date());
D.setParentId("Users/root");
D.setFile("/tmp/1.pdf");
D.setDocDate(new Date());
D.setMimeType("application/pdf");
D.insert();
//System.out.println("Insert:"); System.in.read();
D.Checkout();
//System.out.println("Checkout 1:"); System.in.read();
D.setTitle("Titulo 2 "+new Date());
D.setDocDate(new Date());
//D.setFile("/tmp/2.pdf");
D.update();
//System.out.println("update 1:");System.in.read();
D.Checkin("2.0");
D.delete();
System.out.println("Borrado:");System.in.read();
D.UnDelete(D.getDocType(), D.getPDId());
}
//--------------------------------------------------------------------------
/**
 *
 * @param Sesion
 * @throws PDException
 */
private static void Undelete(DriverGeneric Sesion) throws PDException
{
PDDocs D=new PDDocs(Sesion, "Prueba");
D.setTitle("Titulo 1 "+new Date());
D.setParentId("Users/root");
D.setFile("/tmp/1.pdf");
D.setDocDate(new Date());
D.setMimeType("application/pdf");
Record R=D.getRecSum();
Attribute Attr=R.getAttr("AttrString");
Attr.setValue("Valor");
Attr=R.getAttr("AttrNum");
Attr.setValue(new Integer(15));
D.insert();
D.Checkout();
D.LoadFull(D.getPDId());
R=D.getRecSum();
Attr=R.getAttr("AttrString");
Attr.setValue("Valor 2");
Attr=R.getAttr("AttrNum");
Attr.setValue(new Integer(16));
Attr=R.getAttr("AttrDate");
Attr.setValue(new Date());
D.assignValues(R);
D.update();
D.Checkin("2.0");
D.delete();
D.UnDelete( D.getDocType(), D.getPDId());
}
//--------------------------------------------------------------------------
/**
 *
 * @param Doc
 * @return
 */
static int Execute(String Doc)
{
try {
String Order="";
String OS=System.getProperty("os.name");
//System.out.println("OS:"+OS);

// WINDOWS << explorer "path">>
// gnome << gnome-open "path">>
// KDE <<kde-open "path">>
// X <<xdg-open  "path">>
if (OS.contains("Win"))
    Order="explore ";
else
    Order="gnome-open ";
 Process Pr=Runtime.getRuntime().exec(Order+" "+Doc,null, new File("/"));
//Process Pr=Runtime.getRuntime().exec(Order+"\""+Doc+"\"",null, new File("/"));
/* */
System.out.println("Ejecutando["+Order+"'"+Doc+"'"+"]");
String s = null;
BufferedReader stdInput = new BufferedReader(new InputStreamReader(Pr.getInputStream()));
BufferedReader stdError = new BufferedReader(new InputStreamReader(Pr.getErrorStream()));
System.out.println("Here is the standard output of the command:");
while ((s = stdInput.readLine()) != null)
    {
    System.out.println(s);
    }
System.out.println("Here is the standard error of the command (if any):");
while ((s = stdError.readLine()) != null)
    {
    System.out.println(s);
    }
/* */
} catch (Exception ex)
    {
    System.out.println("Error"+ex.getLocalizedMessage());
    }
return(0);
}
//---------------------------------------------------------------------

/**
 *
 * @param Sesion
 * @throws PDException
 */
private static void FoldSearch(DriverGeneric Sesion) throws PDException
{
PDFolders f=new PDFolders(Sesion);
Conditions Conds=new Conditions();
Condition c=new Condition(PDFolders.fPDID, Condition.cNE, "Imposible");
Conds.addCondition(c);
boolean SubTypes=true;
boolean SubFolders=true;
// String RootFold=PDFolders.ROOTFOLDER;
String RootFold="Carp2";
 Cursor Cur=f.Search("PD_FOLDERS", Conds, SubTypes, SubFolders, RootFold, null);
//Cursor Cur=f.Search("Expediente", Conds, SubTypes, SubFolders, RootFold, null);
Record Res=Sesion.NextRec(Cur);
while (Res!=null)
    {
    System.out.println("Res:"+Res);
    Res=Sesion.NextRec(Cur);
    }
//PDFolders FRes=f.NextFold(Cur);
//while (FRes!=null)
//    {
//    System.out.println("Res:"+FRes);
//    FRes=f.NextFold(Cur);
//    }
}
//---------------------------------------------------------------------
private static void TestCache() throws InterruptedException
{
ObjectsCache OC=new ObjectsCache("Test");
for (int i = 0; i < 600; i++)
    {
    OC.put("Key"+i,"Fecha "+new Date());    
    Thread.sleep(50);    
    }
Thread.sleep(10*1000);   
//OC.put("uno","Fecha1 "+new Date());
//System.out.println("uno:"+OC.get("uno"));
//System.in.read();
//OC.put("dos","Fecha2 "+new Date());
//System.out.println("dos:"+OC.get("dos"));
//System.in.read();
//OC.put("tres","Fecha3 "+new Date());
//System.out.println("tres:"+OC.get("tres"));
//System.in.read();
//System.out.println("----------------------");
//System.out.println("uno:"+OC.get("uno"));
//System.out.println("dos:"+OC.get("dos"));
//System.out.println("tres:"+OC.get("tres"));
ObjectsCache.EndCleaner();
}
//---------------------------------------------------------------------


/**
 * @param args the command line arguments
 */
public static void main(String[] args)
{
String DB="PD";
DriverGeneric Sesion=null;
try
{
System.out.println("Inicio --------"+new Date());
TestCache();
//ProdocFW.InitProdoc(DB, "/media/Iomega/Prodoc/Prodoc/src/prodoc/Prodoc.properties");
//Instalar(DB);
//Sesion=ProdocFW.getSession(DB, "root", "root");
////Sesion.IniciarTrans();
////TestUser(Sesion);
////TestGroup(Sesion);
////TestRepository(Sesion);
////TestMimeType(Sesion);
////TestFolder(Sesion);
////TestDefFolder(Sesion);
//////TextExportImport(Sesion);
////Sesion.CerrarTrans();
////TestFSStore();
////TestDBStore();
////TestVersioning(Sesion);
////PDObjDefs DF=new PDObjDefs(Sesion);
////DF.DeleteObjectTables("Prueba");
////DF.CreateObjectTables("Prueba",false);
////TestVersioning2(Sesion);
////TestVersioning3(Sesion);
////Undelete(Sesion);
////Execute("/tmp/1%201.pdf");
////FoldSearch(Sesion);
//ProdocFW.freeSesion(DB, Sesion);
//ProdocFW.ShutdownProdoc(DB);
System.out.println("Fin --------"+new Date());
} catch (Exception ex)
    {
    ex.printStackTrace();
    if (Sesion.isInTransaction())
        {
        try {
            Sesion.AnularTrans();
        } catch (PDException ex1)
            {
            ex1.printStackTrace();
            }
        }
    try {
    ProdocFW.freeSesion(DB, Sesion);
    ProdocFW.ShutdownProdoc(DB);
        } catch (PDException ex1)
            {
            ex1.printStackTrace();
            }
    }
}
}
