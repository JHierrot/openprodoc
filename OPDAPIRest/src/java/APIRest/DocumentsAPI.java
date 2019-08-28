/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIRest;

import APIRest.beans.DocB;
import APIRest.beans.QueryJSON;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDFolders;
import prodoc.PDMimeType;


/**
 * REST Web Service
 *
 * @author jhier
 */
@Path("/documents")
public class DocumentsAPI extends APICore
{
  
/**
 * Creates a new instance of FoldAPI
 */
public DocumentsAPI()
{
}
//-------------------------------------------------------------------------
/**
 * Retrieves representation of an instance of APIRest.Docs
 * @param DocId
 * @param request
 * @return an instance of java.lang.String
 */
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/ById/{docId}")
public Response getDocById(@PathParam("docId") String DocId,@Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (isLogDebug())
    Debug("getDocById="+DocId);    
try {
DriverGeneric sessOPD = getSessOPD(request);
PDDocs Doc=new PDDocs(sessOPD);
Doc.LoadFull(DocId);
DocB f=DocB.CreateDoc(Doc);
return (Response.ok(f.getJSON()).build());
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnERROR(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * Retrieves representation of an instance of APIRest.Docs
 * @param DocId
 * @param request
 * @return an instance of java.lang.String
 */
@GET
@Produces(MediaType.APPLICATION_OCTET_STREAM)
@Path("/ContentById/{docId}")
public Response getDocContentById(@PathParam("docId") String DocId,@Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (isLogDebug())
    Debug("getDocContentById="+DocId);    
try {
DriverGeneric sessOPD = getSessOPD(request);
PDDocs Doc=new PDDocs(sessOPD);
Doc.Load(DocId);
PDMimeType M=new PDMimeType(sessOPD);
String Mime = M.Ext2Mime(Doc.getMimeType());
StreamingOutput output = new StreamingOutput() 
    {
    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException 
    {
    try {                   
    Doc.getStream(output);
        } catch (Exception e) 
            {
            throw new WebApplicationException(e.getLocalizedMessage());
            }
      }
    };
return (Response.ok().entity(output).header("Content-Disposition", "attachment; filename=" + Doc.getName()).header("Content-Type", Mime).build());
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnERROR(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * POST method for creating an instance of Folders
 * @param uploadedInputStream
 * @param fileMetaData
 * @param NewDoc representation for the resource
 * @param request
 * @return 
 */
@POST
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public Response Insert(@FormDataParam("Binary") InputStream uploadedInputStream, 
                       @FormDataParam("Binary") FormDataContentDisposition fileMetaData, 
                       @FormDataParam("Metadata") String NewDoc,
                       @Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
try {
DocB D=DocB.CreateDoc(NewDoc);
if (isLogDebug())
    Debug("NewDoc="+NewDoc);
DriverGeneric sessOPD = getSessOPD(request);
PDDocs Doc=new PDDocs(sessOPD, D.getType());
D.Assign(Doc);
if (D.getId()!=null && D.getId().length()!=0)
    Doc.setPDId(D.getId());
if (D.getIdparent()!=null && D.getIdparent().length()!=0)
    Doc.setParentId(D.getIdparent());
Doc.setName(fileMetaData.getFileName());
Doc.setStream(uploadedInputStream);
Doc.insert();
return (returnOK("Creado="+Doc.getPDId()));
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnERROR(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * PUT method for updating an instance of Folders
 * @param DocId
 * @param UpdFold representation for the resource
 * @param request
 * @return 
 */
@PUT
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
@Path("/ById/{DocId}")
public Response UpdateById(@PathParam("DocId") String DocId, 
                       @FormDataParam("Binary") InputStream uploadedInputStream, 
                       @FormDataParam("Binary") FormDataContentDisposition fileMetaData, 
                       @FormDataParam("Metadata") String UpdDoc, @Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
DriverGeneric sessOPD = getSessOPD(request);
try {
sessOPD.IniciarTrans();
if (isLogDebug())
    Debug("Docs UpdateById="+DocId+"/"+UpdDoc);
DocB D=DocB.CreateDoc(UpdDoc);
PDDocs Doc=new PDDocs(sessOPD, D.getType());
Doc.Load(DocId);
Doc.Checkout();
Doc.LoadFull(DocId);
D.Assign(Doc);
if (D.getId()!=null && D.getId().length()!=0)
    Doc.setPDId(D.getId());
if (D.getIdparent()!=null && D.getIdparent().length()!=0)
    Doc.setParentId(D.getIdparent());
Doc.setName(fileMetaData.getFileName());
Doc.setStream(uploadedInputStream);
Doc.update();
Doc.Checkin(D.getVerLabel());
sessOPD.CerrarTrans();
return (returnOK("Updated="+Doc.getPDId()));
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    try {
    sessOPD.AnularTrans();
    } catch (Exception E){E.printStackTrace();}
    return(returnERROR(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * DELETE method for deleting an instance of Folders
 * @param DocId
 * @param request
 * @return 
 */
@DELETE
@Produces(MediaType.APPLICATION_JSON)
@Path("/ById/{DocId}")
public Response DeleteById(@PathParam("DocId") String DocId,@Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
try {
if (isLogDebug())
    Debug("Docs DeleteById="+DocId);
DriverGeneric sessOPD = getSessOPD(request);
PDDocs Doc=new PDDocs(sessOPD);
Doc.Load(DocId);
Doc.delete();
return (returnOK("Deleted="+Doc.getPDId()));
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnERROR(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * Retrieves representation of an instance of APIRest.Folders
 * @param QueryParams
 * @param request
 * @return an instance of java.lang.String
 */
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/Search")
public Response Search(String QueryParams, @Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (isLogDebug())
    Debug("Docs Search=["+QueryParams+ "]");  
try { // TODO: Check empty
QueryJSON RcvQuery = QueryJSON.CreateQuery(QueryParams);   
DriverGeneric sessOPD = getSessOPD(request);
PDFolders Fold=new PDFolders(sessOPD);
Cursor SearchFold = Fold.SearchSelect(RcvQuery.getQuery());
return (Response.ok(genCursor(sessOPD, SearchFold, RcvQuery.getInitial(), RcvQuery.getFinal())).build());
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnERROR(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
}
