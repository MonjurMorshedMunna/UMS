package org.ums.academic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.ums.manager.BinaryContentManager;
import org.ums.resource.Resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;

@Component
@Path("/academic/userImage")
@Produces("image/png")
public class ImageResource extends Resource {

  @Autowired
  @Qualifier("fileContentManager")
  BinaryContentManager<byte[]> mBinaryContentManager;

  @GET
  @Path(PATH_PARAM_OBJECT_ID)
  public Response get(final @Context Request pRequest, final @PathParam("object-id") String pObjectId) throws Exception {
    byte[] imageData = mBinaryContentManager.get(pObjectId, BinaryContentManager.Domain.PICTURE);
    return Response.ok(new ByteArrayInputStream(imageData)).build();
  }
}
