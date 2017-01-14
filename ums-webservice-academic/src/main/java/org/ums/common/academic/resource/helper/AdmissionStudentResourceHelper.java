package org.ums.common.academic.resource.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ums.cache.LocalCache;
import org.ums.common.builder.AdmissionStudentBuilder;
import org.ums.common.report.generator.AdmissionStudentGenerator;
import org.ums.domain.model.immutable.AdmissionStudent;
import org.ums.domain.model.immutable.AdmissionStudentCertificate;
import org.ums.domain.model.immutable.Faculty;
import org.ums.domain.model.mutable.MutableAdmissionStudent;
import org.ums.enums.FacultyType;
import org.ums.enums.ProgramType;
import org.ums.enums.QuotaType;
import org.ums.manager.AdmissionStudentManager;
import org.ums.manager.FacultyManager;
import org.ums.persistent.model.PersistentAdmissionStudent;
import org.ums.resource.ResourceHelper;

import javax.json.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monjur-E-Morshed on 17-Dec-16.
 */
@Component
public class AdmissionStudentResourceHelper extends
    ResourceHelper<AdmissionStudent, MutableAdmissionStudent, String> {

  private static final Logger mLogger = LoggerFactory
      .getLogger(AdmissionStudentResourceHelper.class);

  @Autowired
  AdmissionStudentManager mManager;

  @Autowired
  AdmissionStudentBuilder mBuilder;

  @Autowired
  FacultyManager mFacultyManager;

  @Autowired
  AdmissionStudentGenerator mGenerator;

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    return null;
  }

  @Transactional
  public Response postTaletalkData(JsonObject pJsonObject, final int pSemesterId,
      final ProgramType pProgramType, UriInfo pUriInfo) throws Exception {

    int dataSize = getContentManager().getDataSize(pSemesterId, pProgramType);
    if(dataSize == 0) {
      List<MutableAdmissionStudent> students = new ArrayList<>();
      JsonArray entries = pJsonObject.getJsonArray("entries");

      for(int i = 0; i < entries.size(); i++) {
        LocalCache localCache = new LocalCache();
        JsonObject jsonObject = entries.getJsonObject(i);
        PersistentAdmissionStudent student = new PersistentAdmissionStudent();
        getBuilder().build(student, jsonObject, localCache);
        students.add(student);
      }

      getContentManager().saveTaletalkData(students);
    }
    URI contextURI = null;
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  @Transactional
  public Response saveMeritListData(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    List<MutableAdmissionStudent> students = new ArrayList<>();
    JsonArray entries = pJsonObject.getJsonArray("entries");

    for(int i = 0; i < entries.size(); i++) {
      LocalCache localCache = new LocalCache();
      JsonObject jsonObject = entries.getJsonObject(i);
      PersistentAdmissionStudent student = new PersistentAdmissionStudent();
      getBuilder().build(student, jsonObject, "meritList", localCache);
      students.add(student);
    }
    getContentManager().saveMeritList(students);
    URI contextURI = null;
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();

  }

  @Transactional
  public Response putVerificationStatus(JsonObject pJsonObject, UriInfo pUriInfo) {
    MutableAdmissionStudent student = new PersistentAdmissionStudent();
    JsonArray entries = pJsonObject.getJsonArray("entries");

    LocalCache localCache = new LocalCache();
    JsonObject jsonObject = entries.getJsonObject(0);
    getBuilder().setVerificationStatus(student, jsonObject, localCache);

    getContentManager().saveVerificationStatus(student);
    URI contextURI = null;
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  public JsonObject getTaletalkData(final int pSemesterId, final ProgramType pProgramType,
      final UriInfo pUriInfo) {
    List<AdmissionStudent> students;
    try {
      students = getContentManager().getTaletalkData(pSemesterId, pProgramType);
    } catch(EmptyResultDataAccessException e) {
      mLogger.error(e.getMessage());
      students = new ArrayList<>(); // just for skipping while we have no data in the db.
    }

    return jsonCreator(students, "taletalkData", pUriInfo);
  }

  public JsonObject getAdmissionMeritList(final int pSemesterId, final ProgramType pProgramType,
      final QuotaType pQuotaType, String pUnit, final UriInfo pUriInfo) {
    List<AdmissionStudent> students =
        getContentManager().getMeritList(pSemesterId, pQuotaType, pUnit, pProgramType);
    return jsonCreator(students, "meritList", pUriInfo);
  }

  public JsonObject getTaletalkData(final int pSemesterId, final ProgramType pProgramType,
      final QuotaType pQuotaType, final String pUnit, final UriInfo pUriInfo) {
    List<AdmissionStudent> students =
        getContentManager().getTaletalkData(pSemesterId, pQuotaType, pUnit, pProgramType);
    return jsonCreator(students, "taletalkData", pUriInfo);
  }

  public List<AdmissionStudent> getTaletalkData(final int pSemesterId,
      final ProgramType pProgramType) {
    List<AdmissionStudent> students = new ArrayList<>();
    // students = getContentManager().getTaletalkData(pSemesterId, pProgramType);
    try {
      students = getContentManager().getTaletalkData(pSemesterId, pProgramType);
    } catch(EmptyResultDataAccessException e) {
      mLogger.error(e.getMessage());
      students = new ArrayList<>(); // just for skipping while we have no data in the db.
    }

    return students;
  }

  public void getTaletalkDataXlesFormat(final OutputStream pOutputStream, int pSemesterId)
      throws Exception {
    mGenerator.createABlankTaletalkDataFormatFile(pOutputStream, pSemesterId);
  }

  public void getMeritLisXlesFormat(final OutputStream pOutputStream, int pSemesterId)
      throws Exception {
    mGenerator.createABlankMeritListUploadFormatFile(pOutputStream, pSemesterId);
  }

  public JsonObject getAdmissionStudentByReceiptId(final String pProgramType,
      final int pSemesterId, final String pReceiptId, final UriInfo pUriInfo) {

    List<AdmissionStudent> student =
        getContentManager().getNewStudentByReceiptId(pProgramType, pSemesterId, pReceiptId);
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    for(AdmissionStudent admissionStudent : student) {
      JsonObjectBuilder jsonObject = Json.createObjectBuilder();
      getBuilder().getAdmissionStudentByReceiptIdBuilder(jsonObject, admissionStudent, pUriInfo,
          localCache);
      children.add(jsonObject);
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  private JsonObject jsonCreator(List<AdmissionStudent> pStudentLIst, String pType, UriInfo pUriInfo) {
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    for(AdmissionStudent student : pStudentLIst) {
      JsonObjectBuilder jsonObject = Json.createObjectBuilder();
      getBuilder().admissionStudentBuilder(jsonObject, student, pUriInfo, localCache, pType);
      children.add(jsonObject);
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  @Override
  protected AdmissionStudentManager getContentManager() {
    return mManager;
  }

  @Override
  protected AdmissionStudentBuilder getBuilder() {
    return mBuilder;
  }

  @Override
  protected String getEtag(AdmissionStudent pReadonly) {
    return pReadonly.getLastModified();
  }
}
