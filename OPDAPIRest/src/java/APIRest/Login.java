/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIRest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jhier
 */
@Path("/session")
public class Login extends APICore
{
//-------------------------------------------------------------------------
/**
* Creates a new instance of Login
*/
public Login()
{
}
//-------------------------------------------------------------------------
/**
 * Deletes session
 * http://localhost:8080/TestAPIRest/APIRest/login Content-Type: application/json  
 * @param request HttpServletRequest
 * @return Response
 */
@DELETE
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response Disconnect(@Context HttpServletRequest request)
{
if (isLogDebug())    
    Debug("Disconnecting=");    
return(CloseSession(request));
}
//-------------------------------------------------------------------------
/**
 * PUT method for creating a session
 * http://localhost:8080/TestAPIRest/APIRest/login Content-Type: application/json  {"Name":"pep", "Password":"ll"}
 * @param Credentials representation for the resource
 * @param request
 * @return 
 */
@PUT
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response Connect(String Credentials, @Context HttpServletRequest request)
{
if (isLogDebug())    
    Debug("Login="+Credentials);
String Token=CanCreateSess(Credentials, request);
if (Token==null)
    return(returnUnathorize());
return(NewOKSesion(Token));
}
//-------------------------------------------------------------------------
}
