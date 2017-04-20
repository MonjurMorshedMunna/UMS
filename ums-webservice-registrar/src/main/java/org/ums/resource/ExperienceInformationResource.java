package org.ums.resource;

import org.springframework.stereotype.Component;
import org.ums.domain.model.mutable.registrar.MutableExperienceInformation;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

@Component
@Path("registrar/employee/experience")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class ExperienceInformationResource extends MutableExperienceInformationResource {

  @GET
  @Path("/getExperienceInformation/{employee-id}")
  public JsonObject getExperienceInformation(final @Context Request pRequest,
      final @PathParam("employee-id") String pEmployeeId) throws Exception {
    return mExperienceInformationResourceHelper.getExperienceInformation(pEmployeeId, mUriInfo);
  }
}
