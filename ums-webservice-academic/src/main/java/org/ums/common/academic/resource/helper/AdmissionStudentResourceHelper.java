package org.ums.common.academic.resource.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.common.builder.AdmissionStudentBuilder;
import org.ums.common.report.generator.AdmissionStudentGenerator;
import org.ums.domain.model.immutable.AdmissionStudent;
import org.ums.domain.model.mutable.MutableAdmissionStudent;
import org.ums.manager.AdmissionStudentManager;
import org.ums.resource.ResourceHelper;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.OutputStream;
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
  AdmissionStudentGenerator mGenerator;

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    return null;
  }

  public Response postTaletalkData(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {

    return null;
  }

  public JsonObject getTaletalkData(final int pSemesterId, final UriInfo pUriInfo) {
    List<AdmissionStudent> students;
    students = getContentManager().getTaletalkData(pSemesterId);
    try {
      students = getContentManager().getTaletalkData(pSemesterId);
    } catch(EmptyResultDataAccessException e) {
      mLogger.error(e.getMessage());
      students = new ArrayList<>(); // just for skipping while we have no data in the db.
    }

    return jsonCreator(students, pUriInfo);
  }

  public List<AdmissionStudent> getTaletalkData(final int pSemesterId) {
    List<AdmissionStudent> students;
    students = getContentManager().getTaletalkData(pSemesterId);
    try {
      students = getContentManager().getTaletalkData(pSemesterId);
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

  private JsonObject jsonCreator(List<AdmissionStudent> pStudentLIst, UriInfo pUriInfo) {
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    for(AdmissionStudent student : pStudentLIst) {
      children.add(toJson(student, pUriInfo, localCache));
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
