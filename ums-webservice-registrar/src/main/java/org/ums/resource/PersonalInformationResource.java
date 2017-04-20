package org.ums.resource;

import org.springframework.stereotype.Component;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Component
@Path("registrar/employee/personal")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class PersonalInformationResource extends MutablePersonalInformationResource {

  @GET
  @Path("/getPersonalInformation/{employee-id}")
  public JsonObject getPersonalInformation(final @Context Request pRequest,
      final @PathParam("employee-id") String pEmployeeId) throws Exception {
    return mPersonalInformationResourceHelper.getPersonalInformation(pEmployeeId, mUriInfo);
  }
}