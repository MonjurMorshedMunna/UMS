package org.ums.common.academic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.manager.SemesterSyllabusMapManager;
import org.ums.resource.Resource;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * Created by Ifti on 08-Jan-16.
 */
@Component
@Path("/academic/ssmap")  //SemesterSyllabusMap (ssmap)
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class SemesterSyllabusMapResource extends MutableSemesterSyllabusMapResource {
  @Autowired
  SemesterSyllabusMapManager mManager;

  @GET
  @Path("/program/{program-id}/semester/{semester-id}")
  public JsonObject getMapsByProgramSemester(final @Context Request pRequest, final @PathParam("program-id") int pProgramId,
                                   final @PathParam("semester-id") int pSemesterId)
      throws Exception {
        return mResourceHelper.buildMaps(mManager.getMapsByProgramSemester(pProgramId, pSemesterId), mUriInfo);
  }


  @GET
  @Path("/{map-id}")
  public Response get(final @Context Request pRequest, final @PathParam("map-id") Integer pMapId)
      throws Exception {
    return mResourceHelper.get(pMapId, pRequest, mUriInfo);
  }


}
