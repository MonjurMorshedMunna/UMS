package org.ums.academic.resource.helper;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.academic.resource.ApplicationCCIResource;
import org.ums.builder.ApplicationCCIBuilder;
import org.ums.builder.Builder;
import org.ums.builder.UGRegistrationResultBuilder;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.ApplicationCCI;
import org.ums.domain.model.immutable.Student;
import org.ums.domain.model.immutable.UGRegistrationResult;
import org.ums.domain.model.mutable.MutableApplicationCCI;
import org.ums.manager.ApplicationCCIManager;
import org.ums.manager.StudentManager;
import org.ums.manager.UGRegistrationResultManager;
import org.ums.persistent.model.PersistentApplicationCCI;
import org.ums.persistent.model.PersistentUGRegistrationResult;
import org.ums.resource.ResourceHelper;
import org.ums.services.academic.ApplicationCCIService;

import javax.json.*;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by My Pc on 7/14/2016.
 */
@Component
public class ApplicationCCIResourceHelper extends ResourceHelper<ApplicationCCI, MutableApplicationCCI, Long> {

  @Autowired
  ApplicationCCIManager mManager;

  @Autowired
  StudentManager mStudentManager;

  @Autowired
  UGRegistrationResultManager mResultManager;

  @Autowired
  ApplicationCCIBuilder mBuilder;

  @Autowired
  UGRegistrationResultBuilder mResultBuilder;

  @Autowired
  ApplicationCCIService mApplicationCCIService;

  @Autowired
  UGRegistrationResultResourceHelper mResultHelper;

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) {
    /*
     * Validator validate = new ApplicationCCIValidator(); validate.validate(pJsonObject);
     */

    String studentId = SecurityUtils.getSubject().getPrincipal().toString();
    Student student = mStudentManager.get(studentId);
    List<ApplicationCCI> aplicationListAll = getContentManager().getAll();
    if(aplicationListAll.size() > 0) {
      mManager.deleteByStudentId(studentId);
    }

    List<MutableApplicationCCI> applications = new ArrayList<>();

    JsonArray entries = pJsonObject.getJsonArray("entries");

    for(int i = 0; i < entries.size(); i++) {
      LocalCache localCache = new LocalCache();
      JsonObject jsonObject = entries.getJsonObject(i);
      PersistentApplicationCCI application = new PersistentApplicationCCI();
      getBuilder().build(application, jsonObject, localCache);
      applications.add(application);
    }

    mManager.create(applications);
    URI contextURI = null;
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  public JsonObject saveAndReturn(JsonObject pJsonObject, UriInfo pUriInfo) {

    String studentId = SecurityUtils.getSubject().getPrincipal().toString();
    Student student = mStudentManager.get(studentId);
    List<ApplicationCCI> aplicationListAll = getContentManager().getAll();
    if(aplicationListAll.size() > 0) {
      mManager.deleteByStudentId(studentId);
    }

    List<MutableApplicationCCI> applications = new ArrayList<>();
    List<PersistentApplicationCCI> persistentApplicationCCIs = new ArrayList<>();

    JsonArray entries = pJsonObject.getJsonArray("entries");

    List<UGRegistrationResult> results =
        mResultManager.getCarryClearanceImprovementCoursesByStudent(student.getSemesterId(), studentId);

    for(int i = 0; i < entries.size(); i++) {
      LocalCache localCache = new LocalCache();
      JsonObject jsonObject = entries.getJsonObject(i);
      PersistentApplicationCCI application = new PersistentApplicationCCI();
      getBuilder().build(application, jsonObject, localCache);

      for(UGRegistrationResult rrx : results) {
        if(application.getCourseId().equals(rrx.getCourseId())) {
          application.setExamDate(rrx.getExamDate());
          break;
        }
      }
      applications.add(application);
      persistentApplicationCCIs.add(application);

    }

    List<PersistentApplicationCCI> applicationAfterValidationByService =
        mApplicationCCIService.validateForAnomalies(persistentApplicationCCIs, results, student);
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    List<UGRegistrationResult> resultForWorkingAsResponse = new ArrayList<>();
    if(applicationAfterValidationByService.size() == 0) {
      mManager.create(applications);
    }
    else {

      for(ApplicationCCI appsForJsonIterator : applicationAfterValidationByService) {

        for(UGRegistrationResult r : results) {
          if(r.getCourseId().equals(appsForJsonIterator.getCourseId())) {
            PersistentUGRegistrationResult rr = new PersistentUGRegistrationResult();
            rr.setStudentId(r.getStudentId());
            rr.setCourseId(r.getCourseId());
            rr.setGradeLetter(r.getGradeLetter());
            rr.setExamType(r.getExamType());
            rr.setType(r.getType());
            rr.setCourseNo(r.getCourseNo());
            rr.setCourseTitle(r.getCourseTitle());
            rr.setExamDate(r.getExamDate());
            rr.setMessage(appsForJsonIterator.getMessage());

            UGRegistrationResult rResult = rr;
            resultForWorkingAsResponse.add(rResult);
          }
        }
      }
    }

    object.add("entries", children);
    localCache.invalidate();
    return mResultHelper.getResultForApplicationCCIOfCarryClearanceAndImprovement(resultForWorkingAsResponse, pUriInfo);
  }

  public Response deleteByStudentId(UriInfo pUriInfo) {
    String studentId = SecurityUtils.getSubject().getPrincipal().toString();
    getContentManager().deleteByStudentId(studentId);
    URI contextURI =
        pUriInfo.getBaseUriBuilder().path(ApplicationCCIResource.class).path(ApplicationCCIResource.class, "get")
            .build();
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  public JsonObject getApplicationCCIInfoForStudent(final Request pRequest, final UriInfo pUriInfo) {
    String studentId = SecurityUtils.getSubject().getPrincipal().toString();
    Student student = mStudentManager.get(studentId);
    List<ApplicationCCI> applictionAll = getContentManager().getAll();
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    if(applictionAll.size() > 0) {
      List<ApplicationCCI> applications =
          getContentManager().getByStudentIdAndSemester(studentId, student.getSemesterId());
      for(ApplicationCCI app : applications) {
        children.add(toJson(app, pUriInfo, localCache));
      }
    }
    else {
      List<ApplicationCCI> applications = getContentManager().getAll();
      for(ApplicationCCI app : applications) {
        children.add(toJson(app, pUriInfo, localCache));
      }
    }

    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  public JsonObject getApplicationCCIForSeatPlanViewingOfStudent(final UriInfo pUriInfo) {
    String studentId = SecurityUtils.getSubject().getPrincipal().toString();
    Student student = mStudentManager.get(studentId);
    List<ApplicationCCI> applications =
        getContentManager().getByStudentIdAndSemesterForSeatPlanView(studentId, student.getCurrentEnrolledSemesterId());

    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for(ApplicationCCI app : applications) {
      children.add(toJson(app, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  public JsonObject getApplicationCCIForSeatPlan(final Integer pSemesterId, final String pExamDate,
      final Request pRequest, final UriInfo pUriInfo) {

    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    List<ApplicationCCI> applications = getContentManager().getBySemesterAndExamDate(pSemesterId, pExamDate);

    for(ApplicationCCI app : applications) {
      children.add(toJson(app, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  @Override
  protected ApplicationCCIManager getContentManager() {
    return mManager;
  }

  @Override
  protected Builder<ApplicationCCI, MutableApplicationCCI> getBuilder() {
    return mBuilder;
  }

  @Override
  protected String getETag(ApplicationCCI pReadonly) {
    return pReadonly.getApplicationDate();
  }
}
