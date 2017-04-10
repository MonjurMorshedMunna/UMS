package org.ums.resource.helper.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ums.builder.employee.*;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.registrar.employee.AcademicInformation;
import org.ums.domain.model.immutable.registrar.employee.EmployeeInformation;
import org.ums.domain.model.mutable.registrar.employee.*;
import org.ums.manager.registrar.employee.*;
import org.ums.persistent.model.registrar.employee.*;
import org.ums.resource.ResourceHelper;

import javax.json.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Component
public class EmployeeInformationResourceHelper extends
        ResourceHelper<EmployeeInformation, MutableEmployeeInformation, Integer> {

  private static final Logger mLoger = LoggerFactory.getLogger(EmployeeInformationResourceHelper.class);

  @Autowired
  AcademicInformationManager mAcademicInformationManager;

  @Autowired
  AwardInformationManager mAwardInformationManager;

  @Autowired
  EmployeeInformationManager mEmployeeInformationManager;

  @Autowired
  ExperienceInformationManager mExperienceInformationManager;

  @Autowired
  PersonalInformationManager mPersonalInformationManager;

  @Autowired
  PublicationInformationManager mPublicationInformationManager;

  @Autowired
  TrainingInformationManager mTrainingInformationManager;

  @Autowired
  EmployeeInformationBuilder mEmployeeInformationBuilder;

  public JsonObject getEmployeeAcademicInformation(final int pEmployeeId, final UriInfo pUriInfo) {
    List<AcademicInformation> pAcademicInformation =
            mAcademicInformationManager.getEmployeeAcademicInformation(pEmployeeId);
    return jsonCreator(pAcademicInformation, pUriInfo);
  }

  public JsonObject getEmployeeAwardInformation(final int pEmployeeId, final UriInfo pUriInfo) {
    List<AcademicInformation> pAcademicInformation =
            mAcademicInformationManager.getEmployeeAcademicInformation(pEmployeeId);
    return jsonCreator(pAcademicInformation, pUriInfo);
  }

  public JsonObject getEmployeeInformation(final int pEmployeeId, final UriInfo pUriInfo) {
    List<AcademicInformation> pAcademicInformation =
            mAcademicInformationManager.getEmployeeAcademicInformation(pEmployeeId);
    return jsonCreator(pAcademicInformation, pUriInfo);
  }

  public JsonObject getEmployeeExperienceInformation(final int pEmployeeId, final UriInfo pUriInfo) {
    List<AcademicInformation> pAcademicInformation =
            mAcademicInformationManager.getEmployeeAcademicInformation(pEmployeeId);
    return jsonCreator(pAcademicInformation, pUriInfo);
  }

  public JsonObject getEmployeePersonalInformation(final int pEmployeeId, final UriInfo pUriInfo) {
    List<AcademicInformation> pAcademicInformation =
            mAcademicInformationManager.getEmployeeAcademicInformation(pEmployeeId);
    return jsonCreator(pAcademicInformation, pUriInfo);
  }

  public JsonObject getEmployeePublicationInformation(final int pEmployeeId, final UriInfo pUriInfo) {
    List<AcademicInformation> pAcademicInformation =
            mAcademicInformationManager.getEmployeeAcademicInformation(pEmployeeId);
    return jsonCreator(pAcademicInformation, pUriInfo);
  }

  public JsonObject getEmployeeTrainingInformation(final int pEmployeeId, final UriInfo pUriInfo) {
    List<AcademicInformation> pAcademicInformation =
            mAcademicInformationManager.getEmployeeAcademicInformation(pEmployeeId);
    return jsonCreator(pAcademicInformation, pUriInfo);
  }

  @Transactional
  public Response saveEmployeeInformation(JsonObject pJsonObject, UriInfo pUriInfo) {
    System.out.println("I am in saveEmployeeInformation(). And it is a helper Helper");

    LocalCache localCache = new LocalCache();
    JsonArray entries = pJsonObject.getJsonArray("entries");

    // MutableAcademicInformation academicInformation = new PersistentAcademicInformation();
    // JsonObject academicJsonObject = entries.getJsonObject(0).getJsonObject("academic");
    // mEmployeeInformationBuilder.build(academicInformation, academicJsonObject, localCache);
    // mAcademicInformationManager.saveAcademicInformation(academicInformation);

    // MutableAwardInformation awardInformation = new PersistentAwardInformation();
    // JsonObject awardJsonObject = entries.getJsonObject(0).getJsonObject("award");
    // mEmployeeInformationBuilder.build(awardInformation, awardJsonObject, localCache);
    // mAwardInformationManager.saveAwardInformation(awardInformation);
    //
    // MutableEmployeeInformation employeeInformation = new PersistentEmployeeInformation();
    // JsonObject employeeJsonObject = entries.getJsonObject(0).getJsonObject("employee");
    // mEmployeeInformationBuilder.build(employeeInformation, employeeJsonObject, localCache);
    // mEmployeeInformationManager.saveEmployeeInformation(employeeInformation);
    //
    // MutableExperienceInformation experienceInformation = new PersistentExperienceInformation();
    // JsonObject experienceJsonObject = entries.getJsonObject(0).getJsonObject("experience");
    // mEmployeeInformationBuilder.build(experienceInformation, experienceJsonObject, localCache);
    // mExperienceInformationManager.saveExperienceInformation(experienceInformation);
    //
    MutablePersonalInformation personalInformation = new PersistentPersonalInformation();
    JsonObject personalJsonObject = entries.getJsonObject(0).getJsonObject("personal");
    mEmployeeInformationBuilder.build(personalInformation, personalJsonObject, localCache);
    mPersonalInformationManager.savePersonalInformation(personalInformation);
    //
    // MutablePublicationInformation publicationInformation = new
    // PersistentPublicationInformation();
    // JsonObject publicationJsonObject = entries.getJsonObject(0).getJsonObject("publication");
    // mEmployeeInformationBuilder.build(publicationInformation, publicationJsonObject,
    // localCache);
    // mPublicationInformationManager.savePublicationInformation(publicationInformation);
    //
    // MutableTrainingInformation trainingInformation = new PersistentTrainingInformation();
    // JsonObject trainingJsonObject = entries.getJsonObject(0).getJsonObject("training");
    // mEmployeeInformationBuilder.build(trainingInformation, trainingJsonObject, localCache);
    // mTrainingInformationManager.saveTrainingInformation(trainingInformation);

    Response.ResponseBuilder builder = Response.created(null);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  private JsonObject jsonCreator(List<AcademicInformation> pAcademicInformation, UriInfo pUriInfo) {
    JsonObjectBuilder jObject = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();

    LocalCache localCache = new LocalCache();

    for(AcademicInformation academicInformation : pAcademicInformation) {
      JsonObjectBuilder jsonObject = Json.createObjectBuilder();
      mEmployeeInformationBuilder.build(jsonObject, academicInformation, pUriInfo, localCache);
      children.add(jsonObject);
    }
    jObject.add("entries", children);
    localCache.invalidate();
    return jObject.build();
  }

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    return null;
  }

  @Override
  protected EmployeeInformationManager getContentManager() {
    return mEmployeeInformationManager;
  }

  @Override
  protected EmployeeInformationBuilder getBuilder() {
    return mEmployeeInformationBuilder;
  }

  @Override
  protected String getETag(EmployeeInformation pReadonly) {
    return pReadonly.getLastModified();
  }
}
