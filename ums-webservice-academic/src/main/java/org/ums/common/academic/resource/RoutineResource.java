package org.ums.common.academic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.manager.RoutineManager;
import org.ums.resource.Resource;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;


@Component
@Path("academic/routine")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class RoutineResource extends MutableRoutineResource{

  @Autowired
  RoutineManager mManager;


  @GET
  @Path("/all")
  public JsonObject getAll() throws Exception{
    return mRoutineResourceHelper.getAll(mUriInfo);
  }

  @GET
  @Path("/routineForTeacher/{teacherId}")
  public JsonObject getRoutineForTeachers(final @Context Request pRequest, final @PathParam("teacherId") String teacherId) throws Exception{
    return mRoutineResourceHelper.getRoutineForTeacher(teacherId,pRequest,mUriInfo);
  }

  @GET
  @Path("/routineForStudent")
  public JsonObject getRoutineForStudents(final @Context Request pRequest, final @PathParam("semesterId") String semesterId,final @PathParam("programId") String programId)throws Exception{
    return mRoutineResourceHelper.getRoutineForStudent();
  }

  @GET
  @Path("/routineForEmployee/semester/{semesterId}/program/{programId}/year/{year}/semester/{semester}")
  public JsonObject getRoutineForEmployee(final @Context Request pRequest,
                                          final @PathParam("semesterId") String semesterId,
                                          final @PathParam("programId") String programId,
                                          final @PathParam("year") String year,
                                          final @PathParam("semester") String semester)throws Exception{
    return mRoutineResourceHelper.getRoutineForEmployee(Integer.parseInt(semesterId),Integer.parseInt(programId),Integer.parseInt(year),Integer.parseInt(semester),pRequest,mUriInfo);
  }

  @GET
  @Path(PATH_PARAM_OBJECT_ID)
  public Response get(final @Context Request pRequest,final @PathParam("object-id") String pObjectId)throws Exception{
    return mRoutineResourceHelper.get(pObjectId,pRequest,mUriInfo);
  }
}
