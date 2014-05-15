/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package OpenProdocServ;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.ProdocFW;


/**
 *
 * @author jhierrot
 */
public class Oper extends HttpServlet
{

protected static String ProdocProperRef=null;

/** Initializes the servlet.
 * @param config 
 * @throws ServletException 
 */
public void init(ServletConfig config) throws ServletException
{
super.init(config);
}
//-----------------------------------------------------------------------------------------------

/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException
*/
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
response.setContentType("text/xml;charset=UTF-8");
PrintWriter out = response.getWriter();
try {
if (Connected(request) || request.getParameter("Oper").equals(DriverGeneric.S_LOGIN)) 
    {
    ProcessPage(request, out);
    }
else
    {
    Answer(request, out, false, "<OPD><Result>KO</Result><Msg>Disconnected</Msg></OPD>", null);
    }
} catch (Exception e)
    {
    Answer(request, out, false, e.getMessage(), null);
    AddLog(e.getMessage());
    }
finally {
        out.close();
        }
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @param Ok Result of operation
 * @param Message Optional Message
 * @param Data Optional XML Data
 */
static public void Answer(HttpServletRequest Req, PrintWriter out, boolean Ok, String Message, String Data)
{
out.println("<OPD>");
out.println("<Result>"+(Ok?"OK":"KO")+"</Result>");
if (Message!=null && Message.length()!=0)
    out.println("<Msg>"+Message+"</Msg>");
if (Data!=null && Data.length()!=0)
    out.println("<Data>"+Data+"</Data>");    
out.println("</OPD>");
out.flush();
}
//-----------------------------------------------------------------------------------------------
/**
 *
 * @param Req
 * @param out
 * @param AllMessage
 */
static public void Answer(HttpServletRequest Req, PrintWriter out, String AllMessage)
{
out.println(AllMessage);
out.flush();
}
//-----------------------------------------------------------------------------------------------

/**
 * 
 * @param Texto
 */
protected void AddLog(String Texto)
{
System.out.println(this.getServletName()+":"+new Date()+"="+Texto);
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @return
 * @throws PDException
 */
protected boolean Connected(HttpServletRequest Req) throws Exception
{
if (getSessOPD(Req)==null)
    return(false);
else
    return(true);
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @param Req
 * @param out
 * @throws Exception
 */
protected void ProcessPage(HttpServletRequest Req, PrintWriter out) throws Exception
{
String Order=Req.getParameter("Oper");   
String Param=Req.getParameter("Param");   // TODO decode
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(new ByteArrayInputStream(Param.getBytes("UTF-8")));
if (Req.getParameter("Oper").equals(DriverGeneric.S_LOGIN)) 
    {
    NodeList OPDObjectList = XMLObjects.getElementsByTagName("U");
    Node OPDObject = OPDObjectList.item(0);
    String User=OPDObject.getTextContent(); 
    OPDObjectList = XMLObjects.getElementsByTagName("P");
    OPDObject = OPDObjectList.item(0);
    String Pass=OPDObject.getTextContent(); 
    ProdocFW.InitProdoc("PD", getProdocProperRef());
    DriverGeneric D=ProdocFW.getSession("PD", User, Pass);
    Req.getSession().setAttribute("PRODOC_SESS", D);   
    Answer(Req, out, true, null, null);
    return;    
    }
DriverGeneric D=getSessOPD(Req);
Answer(Req, out, D.RemoteOrder(Order, XMLObjects));
}
//-----------------------------------------------------------------------------------------------

/** Handles the HTTP <code>GET</code> method.
* @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException  
*/
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
processRequest(request, response);
}
//-----------------------------------------------------------------------------------------------

/** Handles the HTTP <code>POST</code> method.
* @param request servlet request
 * @param response servlet response
 * @throws ServletException
 * @throws IOException
*/
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException
{
processRequest(request, response);
}
//-----------------------------------------------------------------------------------------------

/** Returns a short description of the servlet.
 * @return 
 */
@Override
public String getServletInfo()
{
return "Servlet for Oper";
}
//-----------------------------------------------------------------------------------------------
/**
 * 
 * @return
 */
static public String getUrlServlet()
{
return("Oper");
}
//----------------------------------------------------------
/**
 *
 * @param Req
 * @return
 */
public static DriverGeneric getSessOPD(HttpServletRequest Req)
{
return (DriverGeneric)Req.getSession(true).getAttribute("PRODOC_SESS");
}
//--------------------------------------------------------------
/**
 *
 * @param Req
 * @param OPDSess
 */
public static void setSessOPD(HttpServletRequest Req, DriverGeneric OPDSess)
{
Req.getSession().setAttribute("PRODOC_SESS", OPDSess);
}
//--------------------------------------------------------------
/**
 * 
 * @return
 */
static public String getVersion()
{
return("0.9");
}
//-----------------------------------------------------------------------------------------------
public static String getProdocProperRef() throws Exception
{
if (ProdocProperRef==null)
    {
    InputStream Is;    
    String Path=System.getProperty("user.home");    
    try {
    Is  = new FileInputStream(Path+File.separator+"OPDWeb.properties");        
    } catch (FileNotFoundException ex)
        {
        Is=null;    
        }
    if (Is==null)
        {
        Path=System.getenv("OPDWeb");
        Is  = new FileInputStream(Path+File.separator+"OPDWeb.properties");            
        }
    Properties p= new Properties();
    p.load(Is);
    Is.close();
    ProdocProperRef=p.getProperty("OPDConfig");
    }
return(ProdocProperRef);
}
//----------------------------------------------------------   
}

