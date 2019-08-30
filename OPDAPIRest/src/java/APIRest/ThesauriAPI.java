/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU Affero GPL v3 http://www.gnu.org/licenses/agpl.html
 * 
 * you may not use this file except in compliance with the License.  
 * Unless agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Joaquin Hierro      2019
 * 
 */
package APIRest;

import APIRest.beans.ThesB;
import APIRest.beans.QueryJSON;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.PDThesaur;

/**
 * REST Web Service
 *
 * @author jhier
 */
@Path("/thesauri")
public class ThesauriAPI extends APICore
{
/**
 * Creates a new instance of FoldAPI
 */
public ThesauriAPI()
{
}
//-------------------------------------------------------------------------
/**
 * Retrieves representation of an instance of APIRest.FoldersAPI
 * @param ThesId
 * @param request
 * @return an instance of java.lang.String
 */
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/ById/{ThesId}")
public Response getThesById(@PathParam("ThesId") String ThesId, @Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (!Valid(ThesId))
    return ErrorParam("{ThesId}");
if (isLogDebug())
    Debug("getThesById="+ThesId);    
try {
DriverGeneric sessOPD = getSessOPD(request);
PDThesaur Thes=new PDThesaur(sessOPD);
Thes.Load(ThesId);
ThesB f=ThesB.CreateThes(Thes);
return (Response.ok(f.getJSON()).build());
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnErrorInternal(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * POST method for creating an instance of FoldersAPI
 * @param NewThes representation for the resource
 * @param request
 * @return 
 */
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response Insert(String NewThes, @Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (!Valid(NewThes))
    return ErrorParam("Body");
if (isLogDebug())
    Debug("Insert Thes="+NewThes);
ThesB TB;
try {
TB=ThesB.CreateThes(NewThes);
} catch (Exception Ex)
    {
    return(returnErrorInput(Ex.getLocalizedMessage()));
    }
try {
DriverGeneric sessOPD = getSessOPD(request);
PDThesaur Thes=new PDThesaur(sessOPD);
TB.Assign(Thes);
if (TB.getId()!=null && TB.getId().length()!=0)
    Thes.setPDId(TB.getId());
Thes.insert();
return (returnOK("Creado="+Thes.getPDId()));
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnErrorInternal(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * PUT method for updating an instance of FoldersAPI
 * @param ThesId
 * @param UpdThes representation for the resource
 * @param request
 * @return 
 */
@PUT
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/ById/{ThesId}")
public Response UpdateById(@PathParam("ThesId") String ThesId, String UpdThes, @Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (!Valid(ThesId))
    return ErrorParam("{ThesId]");
if (isLogDebug())
    Debug("Thes UpdateById="+UpdThes);
ThesB TB;
try {
TB=ThesB.CreateThes(UpdThes);
} catch (Exception Ex)
    {
    return(returnErrorInput(Ex.getLocalizedMessage()));
    }
try {
DriverGeneric sessOPD = getSessOPD(request);
PDThesaur Thes=new PDThesaur(sessOPD);
Thes.Load(ThesId);
TB.Assign(Thes);
Thes.update();
return (returnOK("Updated="+Thes.getPDId()));
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnErrorInternal(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * Retrieves representation of an instance of APIRest.FoldersAPI
 * @param ThesId
 * @param Initial
 * @param Final
 * @param request
 * @return an instance of java.lang.String
 */
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/SubThesById/{ThesId}")
public Response getSubThesById(@PathParam("ThesId") String ThesId, @DefaultValue("0") @QueryParam("Initial") int Initial, @DefaultValue("100") @QueryParam("Final") int Final, @Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (!Valid(ThesId))
    return ErrorParam("{ThesId]");
if (isLogDebug())
    Debug("getSubFoldsbyId="+ThesId+ ",Initial="+Initial+ ",Final="+Final);    
try {
DriverGeneric sessOPD = getSessOPD(request);
PDThesaur Fold=new PDThesaur(sessOPD);
return (Response.ok(GenSubThesList(Fold, ThesId, Initial, Final)).build());
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnErrorInternal(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
private String GenSubThesList(PDThesaur Thes, String Id, int Initial, int Final) throws PDException
{
HashSet<String> ChildFolds = Thes.getListDirectDescendList(Id);
ArrayList<ThesB> L=new ArrayList();
int count=0;
for (String ChildFold : ChildFolds)
    {
    Thes.Load(ChildFold);
    ThesB TB=ThesB.CreateThes(Thes);
    if (count++>=Initial)
        L.add(TB);
    if (count>=Final)
        break;
    }
Gson g = new Gson();
return g.toJson(L);
}
//-------------------------------------------------------------------------
/**
 * DELETE method for deleting an instance of FoldersAPI
 * @param ThesId
 * @param request
 * @return 
 */
@DELETE
@Produces(MediaType.APPLICATION_JSON)
@Path("/ById/{ThesId}")
public Response DeleteById(@PathParam("ThesId") String ThesId,@Context HttpServletRequest request)
{
if (!IsConnected(request))    
    return(returnUnathorize());
if (!Valid(ThesId))
    return ErrorParam("{ThesId]");
if (isLogDebug())
    Debug("Thes DeleteById="+ThesId);
try {
DriverGeneric sessOPD = getSessOPD(request);
PDThesaur Thes=new PDThesaur(sessOPD);
Thes.Load(ThesId);
Thes.delete();
return (returnOK("Deleted="+Thes.getPDId()));
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnErrorInternal(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
/**
 * Retrieves representation of an instance of APIRest.FoldersAPI
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
if (!Valid(QueryParams))
    return ErrorParam("Body");
if (isLogDebug())
    Debug("Thes Search=["+QueryParams+ "]");  
QueryJSON RcvQuery;
try {
RcvQuery = QueryJSON.CreateQuery(QueryParams);   
} catch (Exception Ex)
    {
    return(returnErrorInput(Ex.getLocalizedMessage()));
    }
try {
DriverGeneric sessOPD = getSessOPD(request);
PDThesaur Fold=new PDThesaur(sessOPD);
Cursor SearchFold = Fold.SearchSelect(RcvQuery.getQuery());
return (Response.ok(genCursor(sessOPD, SearchFold, RcvQuery.getInitial(), RcvQuery.getFinal())).build());
} catch (Exception Ex)
    {
    Ex.printStackTrace();
    return(returnErrorInternal(Ex.getLocalizedMessage()));
    }
}
//-------------------------------------------------------------------------
}
