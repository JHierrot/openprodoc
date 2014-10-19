/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prodoc;

import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author root
 */
public class OrdRecord extends Record
{

private TreeMap<String, Attribute> VAttr=new TreeMap();
private Iterator <String> IterAttr;
/**
 *
 * @param newAttr
 * @throws PDException
 */
@Override
public void addAttr(Attribute newAttr) throws PDException
{
if (this.ContainsAttr(newAttr.getName()))
   PDException.GenPDException("Duplicated_attribute",newAttr.getName());
VAttr.put(newAttr.getName(), newAttr.Copy());
}
//--------------------------------------------------------------------------
/**
 *
 * @param NameDelAttr
 */
@Override
public void delAttr(String NameDelAttr)
{
VAttr.remove(NameDelAttr);
}
//--------------------------------------------------------------------------
/**
 *
 * @param NameAttr
 * @return
 */
@Override
public boolean ContainsAttr(String NameAttr)
{
return(VAttr.containsKey(NameAttr));
}
//--------------------------------------------------------------------------
/**
 *
 * @param NameAttr
 * @return
 */
@Override
public Attribute getAttr(String NameAttr)
{
return(VAttr.get(NameAttr));
}
//--------------------------------------------------------------------------
/**
 *
 * @param NumAttr
 * @return
 * @throws PDException
 */
@Override
public Attribute getAttr(int NumAttr)  throws PDException
{
if (NumAttr<0||NumAttr>=VAttr.size())
    PDException.GenPDException("Incorrect_attribute_number", ""+NumAttr);
int Count=0;
for (Iterator<String> iterator1 = VAttr.keySet().iterator(); iterator1.hasNext();Count++)
    {
    String next = iterator1.next();
    if (Count==NumAttr)    
        return(VAttr.get(next));
    }
return(null);
}
//--------------------------------------------------------------------------
/**
 *
 * @return
 */
@Override
public int NumAttr()
{
return(VAttr.size());
}
//--------------------------------------------------------------------------
/**
 *
 * @return the number of attibutes with value
 */
@Override
public int NumAttrFilled()
{
int N=0;
initList();
for (int i = 0; i < NumAttr(); i++)
    {
    Attribute Attr=nextAttr(); 
    if (Attr.isMultivalued())
        {
        try {
            if (!Attr.getValuesList().isEmpty())
                N++;
        } catch (PDException ex)
            {
            }
        }
    else
        {
        if (Attr.getValue()!=null)
            N++;
        }
    }
return(N);
}
//--------------------------------------------------------------------------
/**
 *
 */
@Override
public void initList()
{ 
IterAttr = VAttr.keySet().iterator();
}
//--------------------------------------------------------------------------
/**
 *
 * @return
 */
@Override
public Attribute nextAttr()
{
if (IterAttr.hasNext())    
   return(VAttr.get(IterAttr.next()));
else
    return(null);
}
//--------------------------------------------------------------------------    
    
}
