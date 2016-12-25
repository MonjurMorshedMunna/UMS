package org.ums.common.academic.resource.student.gradesheet;

import java.util.List;
import java.util.Map;

import javax.json.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.ums.builder.Builder;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.StudentRecord;
import org.ums.domain.model.immutable.UGRegistrationResult;
import org.ums.domain.model.mutable.MutableUGRegistrationResult;
import org.ums.enums.CourseRegType;
import org.ums.manager.StudentRecordManager;
import org.ums.manager.UGRegistrationResultManager;
import org.ums.resource.ResourceHelper;

@Component
public class GradeSheetResourceHelper extends
    ResourceHelper<UGRegistrationResult, MutableUGRegistrationResult, Integer> {
  @Autowired
  UGRegistrationResultManager mUGRegistrationResultManager;

  @Autowired
  GradeSheetBuilder mGradeSheetBuilder;

  @Autowired
  StudentRecordManager mStudentRecordManager;

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    throw new NoSuchMethodError("Not Implemented");
  }

  @Override
  protected UGRegistrationResultManager getContentManager() {
    return mUGRegistrationResultManager;
  }

  @Override
  protected Builder<UGRegistrationResult, MutableUGRegistrationResult> getBuilder() {
    return mGradeSheetBuilder;
  }

  @Override
  protected String getEtag(UGRegistrationResult pReadonly) {
    return pReadonly.getLastModified();
  }

  JsonObject getResults(final String pStudentId, final Integer pSemesterId, final UriInfo pUriInfo) {
    List<UGRegistrationResult> results = getContentManager().getResults(pStudentId, pSemesterId);

    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for(UGRegistrationResult result : results) {
      JsonObject jsonObject = toJson(result, pUriInfo, localCache);
      if(result.getSemesterId().intValue() != pSemesterId
          || (result.getType() == CourseRegType.CARRY)) {
        jsonObject = jsonObjectToBuilder(jsonObject).add("carryOver", true).build();
      }
      children.add(jsonObject);
    }
    object.add("entries", children);
    localCache.invalidate();
    StudentRecord studentRecord = mStudentRecordManager.getStudentRecord(pStudentId, pSemesterId);
    object.add("gpa", studentRecord.getGPA());
    object.add("cgpa", studentRecord.getCGPA());
    object.add("remarks", getRemarks(results, studentRecord, pSemesterId));
    return object.build();
  }

  private JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
    JsonObjectBuilder job = Json.createObjectBuilder();
    for(Map.Entry<String, JsonValue> entry : jo.entrySet()) {
      job.add(entry.getKey(), entry.getValue());
    }
    return job;
  }

  private String getRemarks(List<UGRegistrationResult> pResults, StudentRecord pStudentRecord,
      Integer pSemesterId) {
    String passFailStatus = "";
    if(pStudentRecord.getStatus() == StudentRecord.Status.FAILED) {
      passFailStatus = "Failed";
    }
    else if(pStudentRecord.getStatus() == StudentRecord.Status.PASSED) {
      passFailStatus = "Passed";
    }
    String carryOverText = getCarryOverText(pResults, pSemesterId);
    return String.format("%s %s %s", passFailStatus, StringUtils.isEmpty(carryOverText) ? ""
        : "with carrry over in", carryOverText);
  }

  private String getCarryOverText(List<UGRegistrationResult> pResults, final Integer pSemesterId) {
    StringBuilder builder = new StringBuilder();
    for(UGRegistrationResult result : pResults) {
      if(result.getSemesterId().intValue() != pSemesterId
          || (result.getType() == CourseRegType.CARRY && result.getGradeLetter().equalsIgnoreCase(
              "F"))) {
        if(builder.length() != 0) {
          builder.append(", ");
        }
        builder.append(result.getCourse().getNo());
      }
    }
    return builder.toString();
  }
}