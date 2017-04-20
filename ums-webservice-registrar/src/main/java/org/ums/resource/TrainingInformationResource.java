package org.ums.resource;

import org.springframework.stereotype.Component;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

@Component
@Path("registrar/employee/training")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class TrainingInformationResource extends MutableTrainingInformationResource {
  @GET
  @Path("/getTrainingInformation/{employee-id}")
  public JsonObject getTrainingInformation(final @Context Request pRequest,
      final @PathParam("employee-id") String pEmployeeId) throws Exception {
    return mTrainingInformationResourceHelper.getTrainingInformation(pEmployeeId, mUriInfo);
  }
}