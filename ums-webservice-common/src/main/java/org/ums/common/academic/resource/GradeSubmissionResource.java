package org.ums.common.academic.resource;

import org.springframework.stereotype.Component;
import org.ums.common.Resource;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

/**
 * Created by ikh on 4/29/2016.
 */
@Component
@Path("/academic/gradeSubmission")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class GradeSubmissionResource extends MutableGradeSubmissionResource {

    @GET
    @Path("/semester/{semester-id}/courseid/{coruse-id}/examtype/{exam-type}")
    public JsonObject getExamGrade(final @Context Request pRequest,
                                     final @PathParam("semester-id") Integer pSemesterId,
                                     final @PathParam("exam-type") Integer pExamTypeId) throws Exception {
        return mResourceHelper.getGradeList(pSemesterId,pExamTypeId,"");

    }

    @GET
    @Path("/semester/{semester-id}/examtype/{exam-type}/dept/{dept-id}/role/{user-role}")
    public JsonObject getGradeSubmissionStatus(final @Context Request pRequest,
                                   final @PathParam("semester-id") Integer pSemesterId,
                                   final @PathParam("exam-type") Integer pExamTypeId,
                                   final @PathParam("dept-id") String pDeptId,
                                   final @PathParam("user-role") String pUserRole) throws Exception {
        return mResourceHelper.getGradeSubmissionStatus( pSemesterId, pExamTypeId,pDeptId,pUserRole);

    }





}