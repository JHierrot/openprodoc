/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author jhier
 */
public class SoftManOPDConfig
{

private String DepartsRoot;
private String DepartmentType;
private final ArrayList<FieldsDef> DepartmentFields=new ArrayList();
private Vector<String> DepartFields=null;
private Vector<String> DepartFieldsFilter=null;
private String SoftProvRoot;
private String SoftProviderType;
private final ArrayList<FieldsDef> SoftProviderFields=new ArrayList();
private Vector<String> SoftProvFields=null;
private Vector<String> SoftProvFieldsFilter=null;
private String ProductType;
private final ArrayList<FieldsDef> ProductFields=new ArrayList();
private Vector<String> ProdFields=null;
private Vector<String> ProdFieldsFilter=null;
private String ProductsVersType;
private final ArrayList<FieldsDef> ProductsVersFields=new ArrayList();
private Vector<String> ProdVersFields=null;
private Vector<String> ProdVersFieldsFilter=null;
private String IssueType;
private final ArrayList<FieldsDef> IssuesFields=new ArrayList();
private Vector<String> IssueFields=null;
private Vector<String> IssueFieldsFilter=null;
private String RelationsThes;
//-----------------------------------------------------------
public static SoftManOPDConfig CreateConfig(String json)
{
Gson g = new Gson();
return(g.fromJson(json, SoftManOPDConfig.class));    
}
//-----------------------------------------------------------
/**
 * @return the DepartFields
 */
public synchronized  Vector<String> getDepartFields()
{
if (DepartFields==null)    
    {
    DepartFields=new Vector();    
    for (int i = 0; i < DepartmentFields.size(); i++)
        DepartFields.add(DepartmentFields.get(i).getName());    
    }
return DepartFields;
}
//-----------------------------------------------------------
/**
 * @return the DepartFieldsFilter
 */
public synchronized  Vector<String> getDepartFieldsFilter()
{
if (DepartFieldsFilter==null)    
    {
    DepartFieldsFilter=new Vector();    
    for (int i = 0; i < DepartmentFields.size(); i++)
        if (DepartmentFields.get(i).isFilter())
            DepartFieldsFilter.add(DepartmentFields.get(i).getName());    
    }
return DepartFieldsFilter;
}
//-----------------------------------------------------------
/**
 * @return the SoftProvFields
 */
public synchronized Vector<String> getSoftProvFields()
{
if (SoftProvFields==null)    
    {
    SoftProvFields=new Vector();    
    for (int i = 0; i < SoftProviderFields.size(); i++)
        SoftProvFields.add(SoftProviderFields.get(i).getName());    
    }
return SoftProvFields;
}
//-----------------------------------------------------------
/**
 * @return the SoftProvFieldsFilter
 */
public synchronized Vector<String> getSoftProvFieldsFilter()
{
if (SoftProvFieldsFilter==null)    
    {
    SoftProvFieldsFilter=new Vector();    
    for (int i = 0; i < SoftProviderFields.size(); i++)
        if (SoftProviderFields.get(i).isFilter())
            SoftProvFieldsFilter.add(SoftProviderFields.get(i).getName());    
    }
return SoftProvFieldsFilter;
}
//-----------------------------------------------------------
/**
 * @return the ProdFields
 */
public synchronized Vector<String> getProdFields()
{
if (ProdFields==null)    
    {
    ProdFields=new Vector();    
    for (int i = 0; i < ProductFields.size(); i++)
        ProdFields.add(ProductFields.get(i).getName());    
    }
return ProdFields;
}
//-----------------------------------------------------------
/**
 * @return the ProdFieldsFilter
 */
public synchronized Vector<String> getProdFieldsFilter()
{
if (ProdFieldsFilter==null)    
    {
    ProdFieldsFilter=new Vector();    
    for (int i = 0; i < ProductFields.size(); i++)
        if (ProductFields.get(i).isFilter())
            ProdFieldsFilter.add(ProductFields.get(i).getName());    
    }
return ProdFieldsFilter;
}
//-----------------------------------------------------------
/**
 * @return the ProdFields
 */
public synchronized Vector<String> getProdVersFields()
{
if (ProdVersFields==null)    
    {
    ProdVersFields=new Vector();    
    for (int i = 0; i < ProductsVersFields.size(); i++)
        ProdVersFields.add(ProductsVersFields.get(i).getName());    
    }
return ProdVersFields;
}
//-----------------------------------------------------------
/**
 * @return the ProdFieldsFilter
 */
public synchronized Vector<String> getProdVersFieldsFilter()
{
if (ProdVersFieldsFilter==null)    
    {
    ProdVersFieldsFilter=new Vector();    
    for (int i = 0; i < ProductsVersFields.size(); i++)
        if (ProductsVersFields.get(i).isFilter())
            ProdVersFieldsFilter.add(ProductsVersFields.get(i).getName());    
    }
return ProdVersFieldsFilter;
}
//-----------------------------------------------------------
/**
 * @return the ProdFields
 */
public synchronized Vector<String> getIssueFields()
{
if (IssueFields==null)    
    {
    IssueFields=new Vector();    
    for (int i = 0; i < IssuesFields.size(); i++)
        IssueFields.add(IssuesFields.get(i).getName());    
    }
return IssueFields;
}
//-----------------------------------------------------------
/**
 * @return the ProdFieldsFilter
 */
public synchronized Vector<String> getIssueFieldsFilter()
{
if (IssueFieldsFilter==null)    
    {
    IssueFieldsFilter=new Vector();    
    for (int i = 0; i < IssuesFields.size(); i++)
        if (IssuesFields.get(i).isFilter())
            IssueFieldsFilter.add(IssuesFields.get(i).getName());    
    }
return IssueFieldsFilter;
}
//-----------------------------------------------------------
/**
 * @return the DepartsRoot
 */
public String getDepartsRoot()
{
return DepartsRoot;
}
//-----------------------------------------------------------
/**
 * @return the DepartmentType
 */
public String getDepartmentType()
{
return DepartmentType;
}
//-----------------------------------------------------------
/**
 * @return the DepartmentFields
 */
private ArrayList<FieldsDef> getDepartmentFields()
{
return DepartmentFields;
}
//-----------------------------------------------------------
/**
 * @return the SoftProvRoot
 */
public String getSoftProvRoot()
{
return SoftProvRoot;
}
//-----------------------------------------------------------
/**
 * @return the SoftProviderType
 */
public String getSoftProviderType()
{
return SoftProviderType;
}
//-----------------------------------------------------------
/**
 * @return the SoftProviderFields
 */
private ArrayList<FieldsDef> getSoftProviderFields()
{
return SoftProviderFields;
}
//-----------------------------------------------------------
/**
 * @return the ProductType
 */
public String getProductType()
{
return ProductType;
}
//-----------------------------------------------------------
/**
 * @return the ProductFields
 */
private ArrayList<FieldsDef> getProductFields()
{
return ProductFields;
}
//-----------------------------------------------------------
/**
 * @return the ProductsVersType
 */
public String getProductsVersType()
{
return ProductsVersType;
}
//-----------------------------------------------------------
/**
 * @return the ProductsVersFields
 */
private ArrayList<FieldsDef> getProductsVersFields()
{
return ProductsVersFields;
}
//-----------------------------------------------------------
/**
 * @return the IssueType
 */
public String getIssueType()
{
return IssueType;
}
//-----------------------------------------------------------
/**
 * @return the IssuesFields
 */
private ArrayList<FieldsDef> getIssuesFields()
{
return IssuesFields;
}
//-----------------------------------------------------------
/**
 * @return the RelationsThes
 */
public String getRelationsThes()
{
return RelationsThes;
}
//-----------------------------------------------------------


//*************************************************************************
private class FieldsDef
{
private String Name;
private boolean Filter;
//-----------------------------------------------------------
/**
 * @return the Name
 */
public String getName()
{
return Name;
}
//-----------------------------------------------------------
/**
 * @return the Filter
 */
public boolean isFilter()
{
return Filter;
}
//-----------------------------------------------------------
}    
//*************************************************************************
}
