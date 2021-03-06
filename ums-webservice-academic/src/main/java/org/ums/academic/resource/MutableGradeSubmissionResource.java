package org.ums.academic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.ums.academic.resource.helper.GradeSubmissionResourceHelper;
import org.ums.resource.Resource;

import javax.json.JsonObject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by ikh on 4/29/2016.
 */
public class MutableGradeSubmissionResource extends Resource {

  @Autowired
  GradeSubmissionResourceHelper mResourceHelper;

  @PUT
  public Response saveGradeSheet(final JsonObject pJsonObject) {
    return mResourceHelper.saveGradeSheet(pJsonObject);
  }

  @PUT
  @Path("/recheckApprove")
  public Response recheckApprove(final JsonObject pJsonObject) {
    return mResourceHelper.updateGradeStatus(pJsonObject);
  }

  @PUT
  @Path("/vc/recheckApprove")
  public Response recheckRequestApprove(final JsonObject pJsonObject) {
    return mResourceHelper.recheckRequestApprove(pJsonObject);
  }

  @PUT
  @Path("/gradeSubmissionDeadLine")
  public Response updateGradeSubmissionDeadLine(final JsonObject pJsonObject) {
    return mResourceHelper.updateGradeSubmissionDeadLine(pJsonObject, mUriInfo);
  }

}
