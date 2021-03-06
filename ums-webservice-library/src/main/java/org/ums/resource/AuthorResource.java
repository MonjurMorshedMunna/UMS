package org.ums.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.resource.helper.AuthorResourceHelper;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * Created by Ifti on 30-Jan-17.
 */
@Component
@Path("author")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class AuthorResource extends MutableAuthorResource {
  @Autowired
  AuthorResourceHelper mResourceHelper;

  @GET
  @Path("/all")
  public JsonObject getAll() throws Exception {
    return mResourceHelper.getAll(mUriInfo);
  }

  @GET
  @Path(PATH_PARAM_OBJECT_ID)
  public Response get(final @Context Request pRequest, final @PathParam("object-id") Integer pObjectId)
      throws Exception {
    return mResourceHelper.get(pObjectId, pRequest, mUriInfo);
  }
}
