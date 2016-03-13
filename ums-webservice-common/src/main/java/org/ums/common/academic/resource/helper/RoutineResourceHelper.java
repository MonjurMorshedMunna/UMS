package org.ums.common.academic.resource.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.ums.common.builder.Builder;
import org.ums.common.builder.RoutineBuilder;
import org.ums.persistent.model.PersistentProgram;
import org.ums.persistent.model.PersistentRoutine;
import org.ums.cache.LocalCache;
import org.ums.common.academic.resource.ResourceHelper;
import org.ums.common.academic.resource.RoutineResource;
import org.ums.domain.model.mutable.MutableProgram;
import org.ums.domain.model.mutable.MutableRoutine;
import org.ums.domain.model.mutable.MutableSemester;
import org.ums.domain.model.immutable.Routine;
import org.ums.manager.RoutineManager;
import org.ums.persistent.model.PersistentSemester;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by My Pc on 3/6/2016.
 */
@Component
public class RoutineResourceHelper extends ResourceHelper<Routine, MutableRoutine, String> {

  @Autowired
  @Qualifier("routineManager")
  private RoutineManager mManager;

  @Autowired
  private RoutineBuilder mBuilder;


  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    MutableRoutine mutableRoutine = new PersistentRoutine();
    LocalCache localCache = new LocalCache();
    getBuilder().build(mutableRoutine, pJsonObject, localCache);
    mutableRoutine.commit(false);
    URI contextURI = pUriInfo.getBaseUriBuilder().path(RoutineResource.class).path(RoutineResource.class, "get").build(mutableRoutine.getId());
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  public Response post(final int semesterId, final int programId, final int academicYear, final int academicSemester, JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    MutableRoutine mutableRoutine = new PersistentRoutine();
    MutableSemester semester = new PersistentSemester();
    semester.setId(semesterId);
    mutableRoutine.setSemester(semester);
    MutableProgram program = new PersistentProgram();
    program.setId(programId);
    mutableRoutine.setProgram(program);
    mutableRoutine.setAcademicYear(academicYear);
    mutableRoutine.setAcademicSemester(academicSemester);
    LocalCache localCache = new LocalCache();
    getBuilder().build(mutableRoutine, pJsonObject, localCache);

    mutableRoutine.commit(false);
    URI contextURI = pUriInfo.getBaseUriBuilder().path(RoutineResource.class).path(RoutineResource.class, "get").build(mutableRoutine.getId());
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }


  public JsonObject buildRoutines(final List<Routine> pRoutines, final UriInfo pUriInfo) throws Exception {
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for (Routine readOnly : pRoutines) {
      children.add(toJson(readOnly, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  public JsonObject getRoutineForTeacher(final String teacherId, final Request pRequest, final UriInfo pUriInfo) throws Exception {
    List<Routine> routines = getContentManager().getTeacherRoutine(teacherId);
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for (Routine routine : routines) {
      children.add(toJson(routine, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  public JsonObject getRoutineForStudent(final String semesterId, final String programId, final Request pRequest, final UriInfo pUriInfo) throws Exception {
    List<Routine> routines = getContentManager().getStudentRoutine(Integer.parseInt(semesterId), Integer.parseInt(programId));
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for (Routine routine : routines) {
      children.add(toJson(routine, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  public JsonObject getRoutineForEmployee(final int semesterId, final int programId, final int academicYear, final int academicSemester, final Request pRequest, final UriInfo pUriInfo) throws Exception {
    List<Routine> routines = getContentManager().getEmployeeRoutine(semesterId, programId, academicYear, academicSemester);
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    int counter = 0;
    for (Routine routine : routines) {
      counter++;
      children.add(toJson(routine, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }


  @Override
  public RoutineManager getContentManager() {
    return mManager;
  }

  @Override
  public Builder<Routine, MutableRoutine> getBuilder() {
    return mBuilder;
  }

  @Override
  public String getEtag(Routine pReadonly) {
    return null;
  }
}
