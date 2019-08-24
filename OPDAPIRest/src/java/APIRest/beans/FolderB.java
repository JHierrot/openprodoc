/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIRest.beans;

import APIRest.APICore;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import prodoc.Attribute;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.Record;


/**
 *
 * @author jhierrot
 */
public class FolderB
{

private String Id;
    
private String Name;
private String ACL;
private String Idparent;
private String Pathparent;
private String Type;
private String PDDate;
private String PDAuthor;

private ArrayList<Attr> ListAttr=new ArrayList();

static private TreeSet<String> IntFields=null;

public static FolderB CreateFolder(String json)
{
Gson g = new Gson();
return(g.fromJson(json, FolderB.class));    
}
//--------------------------------------------------------------------------    
public String getJSON()
{
Gson g = new Gson();
return(g.toJson(this));    
}
//--------------------------------------------------------------------------    
/**
* @return the Id
*/
public String getId()
{
return Id;
}

/**
* @param Id the Id to set
*/
public void setId(String Id)
{
this.Id = Id;
}

/**
* @return the Name
*/
public String getName()
{
return Name;
}

/**
* @param Name the Name to set
*/
public void setName(String Name)
{
this.Name = Name;
}

/**
* @return the ACL
*/
public String getACL()
{
return ACL;
}

/**
* @param ACL the ACL to set
*/
public void setACL(String ACL)
{
this.ACL = ACL;
}

/**
* @return the Idparent
*/
public String getIdparent()
{
return Idparent;
}

/**
* @param Idparent the Idparent to set
*/
public void setIdparent(String Idparent)
{
this.Idparent = Idparent;
}

/**
* @return the Type
*/
public String getType()
{
return Type;
}

/**
* @param Type the Type to set
*/
public void setType(String Type)
{
this.Type = Type;
}

/**
* @return the ListAttr
*/
public ArrayList<Attr> getListAttr()
{
return ListAttr;
}

/**
* @param ListAttr the ListAttr to set
*/
public void setListAttr(ArrayList<Attr> ListAttr)
{
this.ListAttr = ListAttr;
}

/**
* @return the Pathparent
*/
public String getPathparent()
{
return Pathparent;
}

/**
* @param Pathparent the Pathparent to set
*/
public void setPathparent(String Pathparent)
{
this.Pathparent = Pathparent;
}
//--------------------------------------------------------------------------    
public static FolderB CreateFolder(PDFolders Fold) throws PDException
{
FolderB f=new FolderB();
f.setId(Fold.getPDId());
f.setACL(Fold.getACL());
f.setIdparent(Fold.getParentId());
f.setType(Fold.getFolderType());
f.setName(Fold.getTitle());
f.setPDDate(Fold.getPDDate());
f.setPDAuthor(Fold.getPDAutor());
ArrayList<Attr> LA=new ArrayList();
Record recSum = Fold.getRecSum();
recSum.initList();
Attribute Attri;
for (int NumAttr = 0; NumAttr < recSum.NumAttr(); NumAttr++)
    {
    Attri=recSum.nextAttr();
    if (!IsInternalField(Attri.getName()))
        {
        Attr AttrD=new Attr(Attri);
        LA.add(AttrD);
        }
    }
f.setListAttr(LA);
return(f);
}
//--------------------------------------------------------------------------    
public void Assign(PDFolders Fold) throws PDException
{
if (getACL()!=null && getACL().length()!=0)
    Fold.setACL(getACL());
if (getName()!=null && getName().length()!=0)
    Fold.setTitle(getName());
ArrayList<Attr> listAttr = getListAttr();
Record recSum = Fold.getRecSum();
for (int i = 0; i < listAttr.size(); i++)
    {
    Attr At = listAttr.get(i);
    recSum.getAttr(At.getName()).Import(At.getValues().get(0));
    }
}
//--------------------------------------------------------------------------    
static private synchronized boolean IsInternalField(String FieldName) throws PDException
{
if (IntFields==null)
    {
    IntFields=new TreeSet();
    Record DefPDFold = PDFolders.getRecordStructPDFolder();
    DefPDFold.initList();
    for (int NumAttr = 0; NumAttr < DefPDFold.NumAttr(); NumAttr++)
        IntFields.add(DefPDFold.nextAttr().getName());
    }
return(IntFields.contains(FieldName));
}
//--------------------------------------------------------------------------  
//--------------------------------------------------------------------------    
/**
* @return the PDDate
*/
public String getPDDate()
{
return PDDate;
}
//--------------------------------------------------------------------------    
/**
* @param pPDDate the PDDate to set
*/
public void setPDDate(Date pPDDate)
{
PDDate = APICore.TS2Str(pPDDate);
}
//--------------------------------------------------------------------------    
/**
* @return the PDAuthor
*/
public String getPDAuthor()
{
return PDAuthor;
}
//--------------------------------------------------------------------------    
/**
* @param PDAuthor the PDAuthor to set
*/
public void setPDAuthor(String PDAuthor)
{
this.PDAuthor = PDAuthor;
}
//--------------------------------------------------------------------------    

}
