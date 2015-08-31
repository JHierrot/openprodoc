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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author Joaquin
 */
public class PDDocsRIS extends PDDocs
{
static private TreeSet<String> TagList=null;
static private final String START_REC="TY  -";
static private final String END_REC="ER  -";
static private final String URL_REC="UR";
static private final String RIS="RIS_";
static private final int TAG_LENGTH=5;
final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy/MM/dd");

private HashMap<String, String> ListEquiv=null; 
//-------------------------------------------------------------------------    
public PDDocsRIS(DriverGeneric Drv, String pDocType) throws PDException
{
super(Drv, pDocType);
}
public int ImportFileRIS(String ActFolderId, String RISFilepath) throws PDException
{
int NumDocs=0;
BufferedReader Metadata=null;
TreeSet<String> LT=getTagList();
try {
Metadata = new BufferedReader(new FileReader(RISFilepath));
String DocMeta=Metadata.readLine();
String CurVal="";
String CurField="";
PDDocs Doc=null;
Record R=null;
Attribute Attr;
while (DocMeta!=null)
    {
    DocMeta=DocMeta.trim();
    if (DocMeta.length()!=0)
        {
        if (!LT.contains(DocMeta.substring(0, 4)) && !LT.contains(DocMeta.substring(0, 5)))
            CurVal+=DocMeta;
        else
            {
            if (DocMeta.substring(0, TAG_LENGTH).equalsIgnoreCase(START_REC))
                {
                Doc=new PDDocs(getDrv(), getDocType());
                R=Doc.getRecSum();
                SaveAttr(R, START_REC.substring(0,2), DocMeta.substring(DocMeta.indexOf('-')+1).trim());
                }
            else if (DocMeta.substring(0, TAG_LENGTH).equalsIgnoreCase(END_REC))
                {
                if (Doc!=null)
                    {
                    if (CurField.length()!=0 && CurVal.length()!=0)
                       SaveAttr(R, CurField, CurVal);
                    Doc.assignValues(R);
                    Doc.setParentId(ActFolderId);
                    if (Doc.getName()==null || Doc.getName().length()==0)
                        Doc.setFile("http://www.wikipedia.org/");
                    else 
                        {
                        Doc.setFile(Doc.getName()); // so the "url base" is managed
                        Doc.setName("");
                        }
                    Doc.insert();
                    }
                }
            else 
                {
                SaveAttr(R, CurField, CurVal);
                CurField=DocMeta.substring(0, 2);
                CurVal=DocMeta.substring(DocMeta.indexOf('-')+1).trim();
                }
            }
        }
    DocMeta=Metadata.readLine();
    }
Metadata.close();
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    if (Metadata!=null)
        try {
            Metadata.close();
        } catch (IOException ex1) 
            {
            }
    throw new PDException(ex.getLocalizedMessage());
    }
return(NumDocs);
}
//-------------------------------------------------------------------------    
private TreeSet<String> getTagList()
{
if (TagList==null)
    {
    TagList=new TreeSet();
    TagList.add("TY  -");
    TagList.add("A1  -");
    TagList.add("A2  -");
    TagList.add("A3  -");
    TagList.add("A4  -");
    TagList.add("AB  -");
    TagList.add("AD  -");
    TagList.add("AN  -");
    TagList.add("AU  -");
    TagList.add("C1  -");
    TagList.add("C2  -");
    TagList.add("C3  -");
    TagList.add("C4  -");
    TagList.add("C5  -");
    TagList.add("C6  -");
    TagList.add("C7  -");
    TagList.add("C8  -");
    TagList.add("CA  -");
    TagList.add("CN  -");
    TagList.add("CY  -");
    TagList.add("DA  -");
    TagList.add("DB  -");
    TagList.add("DO  -");
    TagList.add("DP  -");
    TagList.add("EP  -");
    TagList.add("ET  -");
    TagList.add("ID  -");
    TagList.add("IS  -");
    TagList.add("JA  -");
    TagList.add("JF  -");
    TagList.add("JO  -");
    TagList.add("J1  -");
    TagList.add("J2  -");
    TagList.add("KW  -");
    TagList.add("L1  -");
    TagList.add("L2  -");
    TagList.add("L3  -");
    TagList.add("L4  -");
    TagList.add("LA  -");
    TagList.add("LB  -");
    TagList.add("M1  -");
    TagList.add("M3  -");
    TagList.add("N1  -");
    TagList.add("N2  -");
    TagList.add("NV  -");
    TagList.add("OP  -");
    TagList.add("PB  -");
    TagList.add("PY  -");
    TagList.add("RI  -");
    TagList.add("RN  -");
    TagList.add("RP  -");
    TagList.add("SE  -");
    TagList.add("SN  -");
    TagList.add("SP  -");
    TagList.add("ST  -");
    TagList.add("T1  -");
    TagList.add("T2  -");
    TagList.add("T3  -");
    TagList.add("TA  -");
    TagList.add("TI  -");
    TagList.add("TT  -");
    TagList.add("UR  -");
    TagList.add("VL  -");
    TagList.add("Y1  -");
    TagList.add("Y2  -");
    TagList.add("ER  -");
    // duplicated without 1 space for more resiliance
    TagList.add("TY -");
    TagList.add("A1 -");
    TagList.add("A2 -");
    TagList.add("A3 -");
    TagList.add("A4 -");
    TagList.add("AB -");
    TagList.add("AD -");
    TagList.add("AN -");
    TagList.add("AU -");
    TagList.add("C1 -");
    TagList.add("C2 -");
    TagList.add("C3 -");
    TagList.add("C4 -");
    TagList.add("C5 -");
    TagList.add("C6 -");
    TagList.add("C7 -");
    TagList.add("C8 -");
    TagList.add("CA -");
    TagList.add("CN -");
    TagList.add("CY -");
    TagList.add("DA -");
    TagList.add("DB -");
    TagList.add("DO -");
    TagList.add("DP -");
    TagList.add("EP -");
    TagList.add("ET -");
    TagList.add("ID -");
    TagList.add("IS -");
    TagList.add("JF -");
    TagList.add("JA -");
    TagList.add("JO -");
    TagList.add("J1 -");
    TagList.add("J2 -");
    TagList.add("KW -");
    TagList.add("L1 -");
    TagList.add("L2 -");
    TagList.add("L3 -");
    TagList.add("L4 -");
    TagList.add("LA -");
    TagList.add("LB -");
    TagList.add("M1 -");
    TagList.add("M3 -");
    TagList.add("N1 -");
    TagList.add("N2 -");
    TagList.add("NV -");
    TagList.add("OP -");
    TagList.add("PB -");
    TagList.add("PY -");
    TagList.add("RI -");
    TagList.add("RN -");
    TagList.add("RP -");
    TagList.add("SE -");
    TagList.add("SN -");
    TagList.add("SP -");
    TagList.add("ST -");
    TagList.add("T1 -");
    TagList.add("T2 -");
    TagList.add("T3 -");
    TagList.add("TA -");
    TagList.add("TI -");
    TagList.add("TT -");
    TagList.add("UR -");
    TagList.add("VL -");
    TagList.add("Y1 -");
    TagList.add("Y2 -");
    TagList.add("ER -");
    }
return(TagList);
}
//-------------------------------------------------------------------------    
/**
 * Parses an author's field cutting by " and "
 * @param CurVal current field
 * @return a list of authors
 */
private  String[] ParseAuthors(String CurVal)
{
return (CurVal.split(" and "));       
}
//-------------------------------------------------------------------------    

private void SaveAttr(Record R, String CurField, String CurVal) throws PDException
{
String DestField=CalculateField(R, CurField);   
if (DestField==null || CurVal.length()==0)
    return;
if (!R.getAttr(DestField).isMultivalued())
    {
    if (R.getAttr(DestField).getType()==Attribute.tDATE)
        {try { 
        R.getAttr(DestField).setValue(formatterDate.parse(CurVal));
        } catch (Exception Ex) {}
        }
    else 
        {
        R.getAttr(DestField).setValue(R.getAttr(DestField).getValue()==null?CurVal:R.getAttr(DestField).getValue()+"/"+CurVal); 
        if ((CurField.equalsIgnoreCase("TI") || CurField.equalsIgnoreCase("T1") || CurField.equalsIgnoreCase("T2")) 
            && (R.getAttr(PDDocs.fTITLE).getValue()==null || ((String)R.getAttr(PDDocs.fTITLE).getValue()).length()==0))
            R.getAttr(PDDocs.fTITLE).setValue(CurVal);
        }
    }
else
    {
    if ( (CurField.equalsIgnoreCase("A1") || CurField.equalsIgnoreCase("A2")
       || CurField.equalsIgnoreCase("A3") || CurField.equalsIgnoreCase("A4")
       || CurField.equalsIgnoreCase("AU")) && CurVal.length()> 254)
        {
        String[] ListAU=ParseAuthors(CurVal);
        for (String ListAU1 : ListAU)
            R.getAttr(DestField).AddValue(ListAU1);
        }
    else        
        R.getAttr(DestField).AddValue(CurVal);
    }
}
//-------------------------------------------------------------------------    
/**
 * Calculate the field to store the readed metadata
 * @param R
 * @param CurField
 * @return 
 */
private String CalculateField(Record R, String CurField)
{
if (CurField.equalsIgnoreCase("TI"))
    return(PDDocs.fTITLE);
HashMap<String, String> ListFieldEquiv=getEquiv(R);    
String DestField;
if (CurField.equalsIgnoreCase(URL_REC))
   return(PDDocs.fNAME);
if (ListFieldEquiv.size()>0)
    DestField=ListFieldEquiv.get(CurField);
else
    DestField=RIS+CurField;
if (R.ContainsAttr(DestField))
    return(DestField);
else if (CurField.equalsIgnoreCase("T1") || CurField.equalsIgnoreCase("T2"))
        return(PDDocs.fTITLE);
else if (CurField.equalsIgnoreCase("Y1") || CurField.equalsIgnoreCase("Y2")
        || CurField.equalsIgnoreCase("PY"))
    return(PDDocs.fDOCDATE);
else
    return(null);
}
//-------------------------------------------------------------------------    

private HashMap<String, String> getEquiv(Record R)
{
if (ListEquiv!=null)
    return(ListEquiv);
String DescAttr;
int Sp, Ep;
Attribute Attr;
String ListAttr;
ListEquiv=new HashMap();
R.initList();
for (int i = 0; i < R.NumAttr(); i++)
    {
    Attr=R.nextAttr();
    DescAttr=Attr.getDescription();
    Sp=DescAttr.indexOf('(');
    Ep=DescAttr.indexOf(')');
    if (Sp<Ep && Sp!=-1 && Ep!=-1)
        {
        ListAttr=DescAttr.substring(Sp+1, Ep);
        String[] split = ListAttr.split(",");
        for (String split1 : split)
            ListEquiv.put(split1.trim(), Attr.getName());
        }
    }
return(ListEquiv);
}
//-------------------------------------------------------------------------    
}
