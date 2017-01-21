package org.ums.academic.resource;

import org.springframework.stereotype.Component;
import org.ums.resource.Resource;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kawsu on 1/11/2017.
 */
@Component
@Path("/academic/students/certificateHistory")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class AdmissionCertificatesOfStudentResource extends
    MutableAdmissionCertificatesOfStudentResource {

  @GET
  @Path("/savedCertificates/semesterId/{semester-id}/receiptId/{receipt-Id}")
  public JsonObject getSavedCertificates(final @Context Request pRequest,
      final @PathParam("semester-id") int pSemesterId,
      final @PathParam("receipt-Id") String pReceiptId) {
    return mHelper.getStudentsSavedCertificates(pSemesterId, pReceiptId, mUriInfo);
  }
}
