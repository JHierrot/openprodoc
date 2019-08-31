/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Status;

import APIRest.APICore;
import APIRest.beans.CurrentSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jhier
 */
public class Status extends HttpServlet
{
/**
 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
 * methods.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
{
response.setContentType("text/html;charset=UTF-8");
try (PrintWriter out = response.getWriter())
{
out.println("<!DOCTYPE html>");
out.println("<html>");
out.println("<head>");
out.println("<title>OpenProdoc Servlet Status</title>");            
out.println("</head>");
out.println("<body>");
out.println("<h1>Current Connection</h1>");
Hashtable<String, CurrentSession> listOPSess = APICore.getListOPSess();
for (Map.Entry<String, CurrentSession> entry : listOPSess.entrySet())
    {
//    String key = entry.getKey();
    CurrentSession CS = entry.getValue();
    out.println(CS.getHost()+"/");
    out.println(CS.getUserName()+"/");
    out.println(CS.getLoginTime()+"/");
    out.println(CS.getLastUse());
    out.println("<br>");      
    }
out.println("</body>");
out.println("</html>");
}
}

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
/**
 * Handles the HTTP <code>GET</code> method.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException
{
processRequest(request, response);
}
/**
 * Returns a short description of the servlet.
 *
 * @return a String containing servlet description
 */
@Override
public String getServletInfo()
{
return "OpenProdoc API REST Status";
}// </editor-fold>

}
