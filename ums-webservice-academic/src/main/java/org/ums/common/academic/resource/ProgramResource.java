package org.ums.common.academic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.domain.model.immutable.Program;
import org.ums.domain.model.mutable.MutableProgram;
import org.ums.manager.ProgramManager;
import org.ums.resource.Resource;
import org.ums.resource.ResourceHelper;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Component
@Path("/academic/program")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class ProgramResource extends Resource {
  @Autowired
  ResourceHelper<Program, MutableProgram, Integer> mResourceHelper;

  @Autowired
  ProgramManager mManager;

  @GET
  @Path("/all")
  public JsonObject getAll() throws Exception {
    return mResourceHelper.getAll(mUriInfo);
  }

  @GET
  @Path(PATH_PARAM_OBJECT_ID)
  public Response get(final @Context Request pRequest, final @PathParam("object-id") int pObjectId)
      throws Exception {
    return mResourceHelper.get(pObjectId, pRequest, mUriInfo);
  }
}
