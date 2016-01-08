package org.ums.common.academic.resource.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.ums.academic.builder.Builder;
import org.ums.academic.model.PersistentCourse;
import org.ums.academic.model.PersistentSemester;
import org.ums.cache.LocalCache;
import org.ums.common.academic.resource.ResourceHelper;
import org.ums.common.academic.resource.SemesterResource;
import org.ums.domain.model.Course;
import org.ums.domain.model.MutableCourse;
import org.ums.domain.model.MutableSemester;
import org.ums.domain.model.Semester;
import org.ums.manager.ContentManager;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Component
public class CourseResourceHelper extends ResourceHelper<Course, MutableCourse, String> {
  @Autowired
  @Qualifier("courseManager")
  private ContentManager<Course, MutableCourse, String> mManager;

  @Autowired
  private List<Builder<Course, MutableCourse>> mBuilders;

  @Override
  public ContentManager<Course, MutableCourse, String> getContentManager() {
    return mManager;
  }

  @Override
  public List<Builder<Course, MutableCourse>> getBuilders() {
    return mBuilders;
  }

  @Override
  public Response post(final JsonObject pJsonObject, final UriInfo pUriInfo) throws Exception {
    MutableCourse mutableCourse = new PersistentCourse();
    LocalCache localCache = new LocalCache();
    for (Builder<Course, MutableCourse> builder : mBuilders) {
      builder.build(mutableCourse, pJsonObject, localCache);
    }
    mutableCourse.commit(false);

    URI contextURI = pUriInfo.getBaseUriBuilder().path(SemesterResource.class).path(SemesterResource.class, "get").build(mutableCourse.getId());
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);

    return builder.build();
  }

  @Override
  protected String getEtag(Course pReadonly) {
    return "";
  }

  public JsonObject buildCourses(final List<Course> pCourses, final UriInfo pUriInfo) throws Exception {
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for (Course readOnly : pCourses) {
      children.add(toJson(readOnly, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }
}
