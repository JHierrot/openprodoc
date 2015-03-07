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
 * author: Joaquin Hierro      2014
 *
 */
package prodoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for generating "reports"
 * @author Joaquin
 */
public class PDReport extends PDDocs
{
 /**
 *
 */
private static final String REPTABNAME="PD_REPORT";
private static final String R_LOOPDOCS_S="@OPD_DOCSLOOP_S";
private static final String R_LOOPDOCS_E="@OPD_DOCSLOOP_E";
private static final String R_LOOPATTR_S="@OPD_ATTRLOOP_S";
private static final String R_LOOPATTR_E="@OPD_ATTRLOOP_E";
private static final String R_GLOBPARENT="@OPD_GLOBPARENT";
private static final String R_PARENT="@OPD_PARENT";
private static final String R_NAME_ATTR="@OPD_NAME_ATTR";
private static final String R_UNAME_ATTR="@OPD_UNAME_ATTR";
private static final String R_VAL_ATTR="@OPD_VAL_ATTR";
private static final String R_REF_ATTR="@OPD_REF_ATTR";
private static final String R_RECCOUNT="@OPD_RECCOUNT";
private static final String R_PAGCOUNT="@OPD_PAGCOUNT";
String IdParent=null;
Cursor ListDocs=null;
private final ArrayList<String> RepLines=new ArrayList();
private int RecLoopStart=0;
private int RecLoopEnd=0;
private int AttrLoopStart=-1;
private int AttrLoopEnd=-1;
PrintWriter FRepDoc = null;
private int RecsPag; 
private int PagsDoc;
private int TotalRecsCount=0;
private int RecsInPageCount=0;
private int PagesCount=1;
private int FilesCount=0;
private Record Res=null;
private Attribute Attr=null;
private boolean FirstLine=false;
private boolean ExpandObject=false;
private boolean DelNull=false;
private HashSet<String> ListIgnTypes=null; 
private HashSet<String> ListIgnFields=null; 
public static final String fDOCSPAGE="DOCSPAGE";
public static final String fPAGESDOC="PAGESDOCS";
/**
 * Default constructor
 * @param pDrv Generic sesion to be used
 * @throws PDException in any error
 */
public PDReport(DriverGeneric pDrv) throws PDException
{
super(pDrv,getTableName());
}
//-------------------------------------------------------------------------
/**
 * generates a report with the current PDId
 * @param pIdParent Parent of the "cursor". Can be null
 * @param pListDocs Cursor with the list of docs
 * @param pRecsPag Number oc record by page
 * @param pPagsDoc Number of pages by Archive
 * @return path to the generated Report.
 * @throws prodoc.PDException
 */
public ArrayList<String> GenerateRep(String pIdParent, Cursor pListDocs, int pRecsPag, int pPagsDoc, String OSFolder) throws PDException
{
ArrayList<String> ListFiles=new ArrayList();
Load(getPDId());  
IdParent=pIdParent;
ListDocs=pListDocs;
RecsPag=pRecsPag!=0?pRecsPag:99999999;
PagsDoc=pPagsDoc!=0?pPagsDoc:99999999;
String GenRep = OSFolder;
if (!GenRep. endsWith(File.separator))
    GenRep+=File.separator;
GenRep+=GenerateId();
ListFiles.add(GenRep+"_0."+getMimeType());
String OrigRep=GenRep+".org";
try {
ReadTemplate(OrigRep);
FRepDoc = new PrintWriter(GenRep+"_0."+getMimeType(), "UTF-8");
PrintHeader();
Res=getDrv().NextRec(ListDocs);
while (Res!=null)    
    {
    if (CanInclude(Res))
        {
        TotalRecsCount++;
        RecsInPageCount++;
        PrintRec();
        }
    Res=getDrv().NextRec(ListDocs);
    if (RecsInPageCount>=RecsPag)
        {
        if (Res!=null)
            {
            PrintFooter();
            if (PagesCount>=PagsDoc)
                {
                FRepDoc.close();
                FilesCount++;
                FRepDoc = new PrintWriter(GenRep+"_"+FilesCount+"."+getMimeType(), "UTF-8");
                ListFiles.add(GenRep+"_"+FilesCount+"."+getMimeType());
                PagesCount=0;
                }
            PrintHeader();
            RecsInPageCount=0;
            }
        PagesCount++;
        }   
    }
getDrv().CloseCursor(ListDocs);
PrintFooter();
FRepDoc.close();
} catch (Exception Ex)
    {
    if (FRepDoc!=null)
        FRepDoc.close();
    getDrv().CloseCursor(ListDocs);
    PDException.GenPDException(Ex.getLocalizedMessage() , getPDId()+"/"+getTitle());
    }
return(ListFiles);
}
//-------------------------------------------------------------------------
/**
 * Downloads and load to memory the template
 * @param OrigRep Name to be usrd for download
 * @throws PDException 
 */
private void ReadTemplate(String OrigRep)  throws PDException
{
FileOutputStream FO=null;
BufferedReader BR = null;
File Template=null;
try {
FO=new FileOutputStream(OrigRep); // download the original
this.getStream(FO);
FO.close();
Template=new File(OrigRep); // read the original
BR=new BufferedReader(new FileReader(Template));
String Line=BR.readLine();
while (Line!=null)
    {
    if (Line.startsWith(R_LOOPDOCS_S))
        {
        RecLoopStart=RepLines.size();
        EvalIgTypes(Line);
        }
    else if (Line.equals(R_LOOPDOCS_E))
        RecLoopEnd=RepLines.size();
    else if (Line.startsWith(R_LOOPATTR_S))
        {
        AttrLoopStart=RepLines.size();
        if (Line.contains("*"))
            ExpandObject=true;
        else if (Line.contains("?"))
            {
            ExpandObject=true;
            DelNull=true;
            }
        EvalIgFields(Line);
        }
    else if (Line.equals(R_LOOPATTR_E))
        AttrLoopEnd=RepLines.size();
    else
        RepLines.add(Line);
    Line=BR.readLine();
    }    
BR.close();
Template.delete();
if (AttrLoopStart==-1)
    AttrLoopStart=RecLoopStart;
if (AttrLoopEnd==-1)
   AttrLoopEnd=RecLoopStart; 
} catch (Exception Ex)
    {
    if (BR!=null)
        try {
            BR.close();
        } catch (IOException ex) {  }
    Template.delete();
    if (FO!=null)
        try {
            FO.close();
        } catch (IOException ex) {  }
    PDException.GenPDException(Ex.getLocalizedMessage() , getPDId()+"/"+getTitle());
    }
}
//-------------------------------------------------------------------------
/**
 * Process the line, returning the string to be printed
 * @param Line
 * @return 
 */
private void ProcessLine(String Line) throws PDException
{   
if (Line.startsWith("+"))
    Line=Line.substring(1);
else if (!FirstLine)
    FRepDoc.println("");
if (Line.startsWith("@OPD"))
    {
    if (Line.startsWith(R_GLOBPARENT))
        {
        if (IdParent!=null)
            {
            PDFolders Fold=new PDFolders(getDrv());
            FRepDoc.print(Fold.getPathId(IdParent));
            }
        }
    else if (Line.startsWith(R_NAME_ATTR))
        FRepDoc.print(NameAttr(Line));
    else if (Line.startsWith(R_UNAME_ATTR))
        FRepDoc.print(UNameAttr(Line));
    else if (Line.startsWith(R_VAL_ATTR))
        FRepDoc.print(ValAttr(Line));
    else if (Line.startsWith(R_REF_ATTR))
        FRepDoc.print(RefAttr(Line));
    else if (Line.startsWith(R_PARENT))
        {
        FRepDoc.print(ParentAttr());
        }
    else if (Line.startsWith(R_RECCOUNT))
        FRepDoc.print(TotalRecsCount);
    else if (Line.startsWith(R_PAGCOUNT))
        FRepDoc.print(PagesCount);
    }
else
    FRepDoc.print(Line);    
}
//-------------------------------------------------------------------------
 /**
 * return the name of the table used to store the metadata for this object
 * static equivalent method
 * @return the name of the table
 */
static public String getTableName()
{
return(REPTABNAME);
}
//-------------------------------------------------------------------------
/**
 * Returns de Exported value of Attribute
 * @param Line R_VAL_ATTR+name atribute
 * @return Exported value of Attribute
 */
private String ValAttr(String Line)
{
if (Res==null)
    return("Res==null");
Attribute Attr1;
int ElemSize=0;
int PosSize;
PosSize=Line.indexOf(':');
if (PosSize!=-1)
    {
    ElemSize=Integer.parseInt(Line.substring(PosSize+1));
    Line=Line.substring(0, PosSize);
    } 
if (Line.substring(R_VAL_ATTR.length()+1).startsWith("*")) // @OPD_VAL_ATTR_*
   Attr1=Attr;
else
    Attr1=Res.getAttr(Line.substring(R_VAL_ATTR.length()+1)); // @OPD_VAL_ATTR_TITLE
if (Attr1==null)
    return("");
String Res=Attr1.Export();
if (ElemSize==0)
    return(Res); 
else if (Res.length()>=ElemSize)
    return(Res.substring(0, ElemSize));
else if (Attr1.getType()==Attribute.tSTRING || Attr1.getType()==Attribute.tTHES)
   return(Res+GetSpaces(ElemSize-Res.length()));  
else    
   return(GetSpaces(ElemSize-Res.length())+Res);  
}
//-------------------------------------------------------------------------
/**
 * Returns de Internal Name of Attribute
 * @param Line R_VAL_ATTR+name atribute
 * @return Exported value of Attribute
 */
private String NameAttr(String Line)
{
if (Res==null)
    return("Res==null");
Attribute Attr1;
int ElemSize=0;
int PosSize;
PosSize=Line.indexOf(':');
if (PosSize!=-1)
    {
    ElemSize=Integer.parseInt(Line.substring(PosSize+1));
    Line=Line.substring(0, PosSize);
    } 
if (Line.substring(R_NAME_ATTR.length()+1).startsWith("*")) // @OPD_NAME_ATTR_*
   Attr1=Attr;
else
    Attr1=Res.getAttr(Line.substring(R_NAME_ATTR.length()+1)); // @OPD_NAME_ATTR_TITLE
if (Attr1==null)
    return("");
String Res=Attr1.getName();
if (ElemSize==0)
    return(Res); 
else if (Res.length()>=ElemSize)
    return(Res.substring(0, ElemSize));
else
   return(Res+GetSpaces(ElemSize-Res.length())); 
}
//-------------------------------------------------------------------------
/**
 * Returns de Internal Name of Attribute
 * @param Line R_VAL_ATTR+name atribute
 * @return Exported value of Attribute
 */
private String UNameAttr(String Line)
{
if (Res==null)
    return("Res==null");
Attribute Attr1;
int ElemSize=0;
int PosSize;
PosSize=Line.indexOf(':');
if (PosSize!=-1)
    {
    ElemSize=Integer.parseInt(Line.substring(PosSize+1));
    Line=Line.substring(0, PosSize);
    } 
if (Line.substring(R_UNAME_ATTR.length()+1).startsWith("*")) // @OPD_UNAME_ATTR_*
   Attr1=Attr;
else
    Attr1=Res.getAttr(Line.substring(R_UNAME_ATTR.length()+1)); // @OPD_UNAME_ATTR_TITLE
if (Attr1==null)
    return("");
String Res=Attr1.getUserName();
if (ElemSize==0)
    return(Res); 
else if (Res.length()>=ElemSize)
    return(Res.substring(0, ElemSize));
else
   return(Res+GetSpaces(ElemSize-Res.length())); 
}
//-------------------------------------------------------------------------
/**
 * Returns de Referenced value of Attribute
 * @param Line R_REF_ATTR+name atribute
 * @return Parent name, Parent path or Thes name
 */
private String RefAttr(String Line) throws PDException
{
if (Res==null)
    return("Res==null");
Attribute Attr1;
int ElemSize=0;
int PosSize;
PosSize=Line.indexOf(':');
if (PosSize!=-1)
    {
    ElemSize=Integer.parseInt(Line.substring(PosSize+1));
    Line=Line.substring(0, PosSize);
    } 
if (Line.substring(R_REF_ATTR.length()+1).startsWith("*")) // @OPD_REF_ATTR_*
   Attr1=Attr;
else
    Attr1=Res.getAttr(Line.substring(R_REF_ATTR.length()+1)); // @OPD_REF_ATTR_TITLE
if (Attr1==null)
    return("");
String AttrName = Attr1.getName();
String Res;
if (AttrName.equals(PDDocs.fPARENTID))
    {
    PDFolders Fold=new PDFolders(getDrv());
    Fold.Load((String)Attr1.getValue());
    Res=Fold.getTitle();
    } 
else if (Attr1.getType()==Attribute.tTHES)
    {
    PDThesaur Thes=new PDThesaur(getDrv());
    Thes.Load((String)Attr1.getValue());
    Res=Thes.getName();
    }
else if (AttrName.equals(PDDocs.fMIMETYPE))
    {
    PDMimeType MT=new PDMimeType(getDrv());
    MT.Load((String)Attr1.getValue());
    Res=MT.getDescription();
    }
else
    Res=Attr1.Export();  
if (ElemSize==0)
    return(Res); 
else if (Res.length()>=ElemSize)
    return(Res.substring(0, ElemSize));
else
   return(Res+GetSpaces(ElemSize-Res.length())); 
}
//-------------------------------------------------------------------------
/**
 * Obtains the path of the Parent folder
 * @return returns the parent path (i.e. "/RootFolder/Marketing")
 * @throws PDException in any error
 */
private String ParentAttr() throws PDException
{
if (Res==null)
    return("Res==null");
Attribute Attr=Res.getAttr(PDDocs.fPARENTID);
PDFolders Fold=new PDFolders(getDrv());
return(Fold.getPathId((String)Attr.getValue())); 
}
//-------------------------------------------------------------------------
/**
 * Prints the Header, from line 0 to Start of Record area
 * @throws PDException in any error
 */
private void PrintHeader() throws PDException
{
for (int i = 0; i < RecLoopStart; i++)
    {
    if (i==0)
        FirstLine=true;
    else
        FirstLine=false;
    ProcessLine(RepLines.get(i));
    }
}
//-------------------------------------------------------------------------
/**
 * Prints the Footer, from End of Record area to end of Array
 * @throws PDException in any error
 */
private void PrintFooter() throws PDException
{
for (int i = RecLoopEnd; i < RepLines.size(); i++)
    {
    ProcessLine(RepLines.get(i));
    }
FRepDoc.println("");
}
//-------------------------------------------------------------------------
/**
 * Prints the area of the Record
 * @throws PDException  in any error
 */
private void PrintRec() throws PDException
{
for (int i = RecLoopStart; i < AttrLoopStart; i++)
    {
    ProcessLine(RepLines.get(i));
    }
if (Res==null)
    FRepDoc.println("Res==null");
if (ExpandObject)
    {
    Attr=Res.getAttr(PDDocs.fDOCTYPE);
    if (Attr==null) // is Folder
        {
        PDFolders F=new PDFolders(getDrv());
        F.LoadFull((String)Res.getAttr(PDFolders.fPDID).getValue());
        Res=F.getRecSum();
        }
    else
        {
        PDDocs D=new PDDocs(getDrv());
        D.LoadFull((String)Res.getAttr(PDDocs.fPDID).getValue());
        Res=D.getRecSum();
        }
    }
TreeMap<String, Attribute> AttrList=new TreeMap();
Res.initList();
Attr=Res.nextAttr();
while (Attr!=null)
    {
    if (CanInclude(Attr))
        AttrList.put(Attr.getName().toUpperCase(), Attr);
    Attr=Res.nextAttr();
    }
for (Map.Entry<String, Attribute> entrySet : AttrList.entrySet())
    {
    Attr = entrySet.getValue();
    for (int i = AttrLoopStart; i < AttrLoopEnd; i++)
        {
        ProcessLine(RepLines.get(i));
        }
    }
for (int i = AttrLoopEnd; i < RecLoopEnd; i++)
    {
    ProcessLine(RepLines.get(i));
    }
}
//-------------------------------------------------------------------------
/**
 * Return a Cursor of visible Reports
 * @return Cursor with the allowed Reports doocuments
 * @throws PDException in any error
 */
public Cursor GetListReports() throws PDException
{
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(PDDocs.fPDID, Condition.cNE, "*"));
return(Search(getTableName(), Conds,  true, false, false, PDFolders.ROOTFOLDER, null));
}
//-------------------------------------------------------------------------
/**
 * generates a string of ' ' of a size
 * @param SSize Number of chars to generate
 * @return generated string
 */
private String GetSpaces(int SSize)
{
StringBuilder S=new StringBuilder(SSize);
for (int i = 0; i < SSize; i++)
    {
    S.append(' ');
    }
return(S.toString());
}
//-------------------------------------------------------------------------
/** 
 * Evaluates the DocTypes to be ignored
 * @param Line Line in the template containing "-list": -Report, Remplate
 */
private void EvalIgTypes(String Line)
{
int PosSize;
PosSize=Line.indexOf('-');
if (PosSize==-1) 
    return;
String[] ListTypes = Line.substring(PosSize+1).split(",");
ListIgnTypes= new HashSet(ListTypes.length);
for (String ListType : ListTypes)
    ListIgnTypes.add(ListType.trim().toUpperCase());
}
//-------------------------------------------------------------------------
/** 
 * Evaluates the Fields to be ignored
 * @param Line Line in the template containing "-list": -PDID, PDDDATE
 */
private void EvalIgFields(String Line)
{
int PosSize;
PosSize=Line.indexOf('-');
if (PosSize==-1) 
    return;
String[] ListFields = Line.substring(PosSize+1).split(",");
ListIgnFields= new HashSet(ListFields.length);
for (String ListField : ListFields)
    ListIgnFields.add(ListField.trim().toUpperCase());
}
//-------------------------------------------------------------------------
/**
 * Checks if the Attr can be include in the list
 * @param Attr Attribute to check
 * @return true if can be included
 */
private boolean CanInclude(Attribute Attr)
{
if (ListIgnFields!=null && ListIgnFields.contains(Attr.getName().toUpperCase()))
    return(false);
if (DelNull)
    {
    if (Attr.getValue()==null || Attr.Export().length()==0)
        return(false);
    }
return(true);
}
//-------------------------------------------------------------------------
/**
 * Checks if the DocType/FoldType can be included in list
 * @param Rec record btained of a Cursor
 * @return 
 */
private boolean CanInclude(Record Rec)
{
if (ListIgnTypes==null)    
    return(true);
Attribute Attr=Rec.getAttr(PDDocs.fDOCTYPE);
if (Attr!=null)
    {
    return (!ListIgnTypes.contains(((String)Attr.getValue()).toUpperCase()));
    }
Attr=Rec.getAttr(PDFolders.fFOLDTYPE);
if (Attr!=null)
    {
    return (!ListIgnTypes.contains(((String)Attr.getValue()).toUpperCase()));
    }
return(true);
}    
//-------------------------------------------------------------------------
}
