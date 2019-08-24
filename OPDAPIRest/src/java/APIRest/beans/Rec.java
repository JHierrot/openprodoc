/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIRest.beans;

import java.util.ArrayList;
import prodoc.Attribute;
import prodoc.Record;

/**
 *
 * @author jhier
 */
public class Rec
{
ArrayList<Attr> Attrs=new ArrayList();
//--------------------------------------------------------------------------    
public Rec(Record Rec)
{
Rec.initList();
Attribute Attrib=Rec.nextAttr();
while (Attrib!=null)
    {
    Attrs.add(new Attr(Attrib));
    Attrib=Rec.nextAttr();
    }  
}
//--------------------------------------------------------------------------    
}
