package org.ums.common.academic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.ums.common.Resource;
import org.ums.domain.model.MutableSyllabus;
import org.ums.domain.model.Syllabus;
import org.ums.manager.ContentManager;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Component
@Path("/academic/syllabus")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class SyllabusResource extends MutableSyllabusResource {
  @Autowired
  @Qualifier("syllabusManager")
  ContentManager<Syllabus, MutableSyllabus, String> mManager;

  @GET
  @Path("/all")
  public JsonObject getAll() throws Exception {
    return mResourceHelper.getAll(mUriInfo);
  }

  @GET
  @Path(PATH_PARAM_OBJECT_ID)
  public Response get(final @Context Request pRequest, final @PathParam("object-id") String pObjectId) throws Exception {
    return mResourceHelper.get(pObjectId, pRequest, mUriInfo);
  }
}

