package org.ums.common.academic.resource.helper;

import org.apache.commons.lang.NotImplementedException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ums.cache.LocalCache;
import org.ums.common.builder.ExamGradeBuilder;
import org.ums.domain.model.dto.*;
import org.ums.domain.model.immutable.ExamGrade;
import org.ums.domain.model.immutable.Semester;
import org.ums.domain.model.immutable.User;
import org.ums.domain.model.mutable.MutableExamGrade;
import org.ums.enums.CourseMarksSubmissionStatus;
import org.ums.enums.CourseType;
import org.ums.exceptions.ValidationException;
import org.ums.manager.ExamGradeManager;
import org.ums.manager.SemesterManager;
import org.ums.manager.UserManager;
import org.ums.persistent.model.PersistentExamGrade;
import org.ums.resource.ResourceHelper;
import org.ums.services.academic.GradeSubmissionService;
import org.ums.util.Constants;

import javax.json.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class GradeSubmissionResourceHelper extends ResourceHelper<ExamGrade, MutableExamGrade, Object> {

  @Autowired
  private ExamGradeManager mManager;

  @Autowired
  private ExamGradeBuilder mBuilder;

  @Autowired
  private UserManager mUserManager;

  @Autowired
  private SemesterManager mSemesterManager;

  @Autowired
  private GradeSubmissionService gradeSubmissionService;

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    throw new NotImplementedException("Post method is not implemented for GradeSubmissionResourceHelper");
  }

  @Override
  public ExamGradeManager getContentManager() {
    return mManager;
  }

  @Override
  public ExamGradeBuilder getBuilder() {
    return mBuilder;
  }

  @Override
  protected String getEtag(ExamGrade pReadonly) {
    return null;
  }


  public JsonObject getGradeList(final String pRequestedRoleId, final Integer pSemesterId, final String pCourseId, final Integer pExamType) throws Exception {

    MarksSubmissionStatusDto marksSubmissionStatusDto = getContentManager().getMarksSubmissionStatus(pSemesterId, pCourseId, pExamType);
    String currentActor = gradeSubmissionService.getActorForCurrentUser(SecurityUtils.getSubject().getPrincipal().toString(), pRequestedRoleId, pSemesterId, pCourseId);

    JsonReader jsonReader = Json.createReader(new StringReader(marksSubmissionStatusDto.toString()));
    JsonObject jsonObject = jsonReader.readObject();
    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    jsonReader.close();
    objectBuilder.add("part_info", jsonObject);
    objectBuilder.add("current_actor", currentActor);
    objectBuilder.add("current_course_status", marksSubmissionStatusDto.getStatusId());

    gradeSubmissionService.prepareGradeGroups(objectBuilder,getContentManager().getAllGrades(pSemesterId, pCourseId, pExamType, marksSubmissionStatusDto.getCourseType()),
        CourseMarksSubmissionStatus.values()[marksSubmissionStatusDto.getStatusId()],currentActor );

    return objectBuilder.build();
  }


  public JsonObject getGradeSubmissionStatus(final Integer pSemesterId, final Integer pExamType,final Integer pProgramId, final String deptId, final String pUserRole,final int pStatus) throws Exception {
    User user = mUserManager.get(SecurityUtils.getSubject().getPrincipal().toString());
    List<MarksSubmissionStatusDto> examGradeStatusList = getContentManager().getMarksSubmissionStatus(pSemesterId, pExamType,pProgramId, user.getEmployeeId(), deptId, pUserRole,pStatus);
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    JsonReader jsonReader;
    JsonObject jsonObject;

    for (MarksSubmissionStatusDto statusDto : examGradeStatusList) {
      jsonReader = Json.createReader(new StringReader(statusDto.toString()));
      jsonObject = jsonReader.readObject();
      jsonReader.close();
      children.add(jsonObject);
    }
    object.add("entries", children);
    return object.build();
  }


   //This method will only be used by Grade Sheet Preparer during saving or submitting grades.
  @Transactional
  public Response saveGradeSheet(final JsonObject pJsonObject) throws Exception {
    List<StudentGradeDto> gradeList = getBuilder().build(pJsonObject);
    MarksSubmissionStatusDto partInfoDto = new MarksSubmissionStatusDto();
    getBuilder().build(partInfoDto, pJsonObject);
    String action = pJsonObject.getString("action");
    String userRole=pJsonObject.getString("role");

    MarksSubmissionStatusDto marksSubmissionStatus = getContentManager().getMarksSubmissionStatus(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType());
    //User Role Validation
    String currentActorRole=gradeSubmissionService.getActorForCurrentUser(SecurityUtils.getSubject().getPrincipal().toString(), userRole, partInfoDto.getSemesterId(), partInfoDto.getCourseId());
    if(!currentActorRole.equalsIgnoreCase(Constants.GRADE_PREPARER)){
      throw new ValidationException("Invalid Role");
    }
    // Deadline Validation
    gradeSubmissionService.validateGradeSubmissionDeadline(marksSubmissionStatus.getLastSubmissionDate());

    //Need to validate grade here

    if ((partInfoDto.getCourseType() == CourseType.THEORY )
        &&
        (marksSubmissionStatus.getStatus()==CourseMarksSubmissionStatus.NOT_SUBMITTED
//            || marksSubmissionStatus.getStatus()==CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_SCRUTINIZER
//            || marksSubmissionStatus.getStatus()==CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_HEAD
//            || marksSubmissionStatus.getStatus()==CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_COE)
        )) {

        //Validate Part-Info
        gradeSubmissionService.validatePartInfo(partInfoDto.getTotal_part(), partInfoDto.getPart_a_total(), partInfoDto.getPart_b_total());
        getContentManager().updatePartInfo(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getTotal_part(), partInfoDto.getPart_a_total(), partInfoDto.getPart_b_total());
    }

    if (action.equalsIgnoreCase("submit")) {
      //Validate Submitted Grades
      gradeSubmissionService.validateSubmittedGrades(partInfoDto.getSemesterId(),partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(),partInfoDto,gradeList);

      getContentManager().updateCourseMarksSubmissionStatus(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), CourseMarksSubmissionStatus.WAITING_FOR_SCRUTINY);
      getContentManager().insertGradeLog(SecurityUtils.getSubject().getPrincipal().toString(), currentActorRole,partInfoDto.getSemesterId(), partInfoDto.getCourseId(),
          partInfoDto.getExamType(), partInfoDto.getCourseType(), marksSubmissionStatus.getStatus(),CourseMarksSubmissionStatus.WAITING_FOR_SCRUTINY, gradeList);
      getContentManager().insertMarksSubmissionStatusLog(SecurityUtils.getSubject().getPrincipal().toString(), currentActorRole,partInfoDto.getSemesterId(),
          partInfoDto.getCourseId(), partInfoDto.getExamType(),  CourseMarksSubmissionStatus.WAITING_FOR_SCRUTINY);
    }
    getContentManager().saveGradeSheet(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), gradeList);

    gradeSubmissionService.sendNotification();
    Response.ResponseBuilder builder = Response.created(null);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  @Transactional
  public Response updateGradeStatus(final JsonObject pJsonObject) throws Exception {

    String action = pJsonObject.getString("action");
    String actor = pJsonObject.getString("actor");

    ArrayList<List<StudentGradeDto>> gradeList = getBuilder().buildForRecheckApproveGrade(action, actor, pJsonObject);
    MarksSubmissionStatusDto partInfoDto = new MarksSubmissionStatusDto();
    getBuilder().build(partInfoDto, pJsonObject);

    MarksSubmissionStatusDto marksSubmissionStatus = getContentManager().getMarksSubmissionStatus(partInfoDto.getSemesterId(),partInfoDto.getCourseId(),partInfoDto.getExamType());

    List<StudentGradeDto> recheckList=null;
    List<StudentGradeDto> approveList=null;

    CourseMarksSubmissionStatus nextStatus=gradeSubmissionService.getCourseMarksSubmissionNextStatus(actor, action, marksSubmissionStatus.getStatus());

    //Need to improve this if else logic here....
    if (action.equals("save"))  // Scrutinizer, Head, CoE  Press the Save Button
      getContentManager().updateGradeStatus_Save(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), gradeList.get(0), gradeList.get(1));
    else {
      if (action.equals("recheck")) {  //Scrutinizer, Head, CoE Press the Recheck Button
        recheckList=gradeList.get(0);
        getContentManager().updateGradeStatus_Recheck(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), gradeList.get(0), gradeList.get(1));
        getContentManager().updateCourseMarksSubmissionStatus(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), nextStatus);
      } else if (action.equals("approve")) { //Scrutinizer, Head, CoE Press the Approve Button
        approveList=gradeList.get(1);
        getContentManager().updateGradeStatus_Approve(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), gradeList.get(0), gradeList.get(1));
        getContentManager().updateCourseMarksSubmissionStatus(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(),nextStatus);
      } else if (action.equals("recheck_request_submit")) { // CoE Press the "Send Recheck Request to VC" Button
        recheckList=gradeList.get(0);
        getContentManager().updateGradeStatus_Recheck(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), gradeList.get(0), gradeList.get(1));
        getContentManager().updateCourseMarksSubmissionStatus(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), nextStatus);
      }

      if(recheckList!=null)recheckList.stream().forEach(g -> {g.setRecheckStatusId(1);});
      if(approveList!=null)approveList.stream().forEach(g -> {g.setRecheckStatusId(0);});
      List<StudentGradeDto> allGradeList =
          Stream.concat(recheckList==null?Stream.empty():recheckList.stream(), approveList==null?Stream.empty():approveList.stream()).collect(Collectors.toList());


      getContentManager().insertGradeLog(SecurityUtils.getSubject().getPrincipal().toString(),actor, partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), marksSubmissionStatus.getStatus(), nextStatus,allGradeList);
      getContentManager().insertMarksSubmissionStatusLog(SecurityUtils.getSubject().getPrincipal().toString(),actor,partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), nextStatus);
    }

    Response.ResponseBuilder builder = Response.created(null);
    builder.status(Response.Status.CREATED);

    return builder.build();
  }

  public Response recheckRequestApprove(final JsonObject pJsonObject) throws Exception {

    //Should do the validation
    //. Is he the right person
    //. have the time period to do the operation

    String action = pJsonObject.getString("action");
    String actor = pJsonObject.getString("actor");

    MarksSubmissionStatusDto partInfoDto = new MarksSubmissionStatusDto();
    getBuilder().build(partInfoDto, pJsonObject);

    int current_course_status = pJsonObject.getInt("course_current_status");

    boolean updateStatus;
    //Need to improve this if else logic here....
    if (action.equals("recheck_request_rejected")) {// VC sir Rejected the whole recheck request
      int a = getContentManager().rejectRecheckRequest(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType());
    } else if (action.equals("recheck_request_approved")) {  // VC Sir Approved the whole recheck request
      int b = getContentManager().approveRecheckRequest(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType());
    }
    int bb = getContentManager().updateCourseMarksSubmissionStatus(partInfoDto.getSemesterId(), partInfoDto.getCourseId(), partInfoDto.getExamType(), partInfoDto.getCourseType(), gradeSubmissionService.getCourseMarksSubmissionNextStatus(actor, action, CourseMarksSubmissionStatus.values()[current_course_status]));

    Response.ResponseBuilder builder = Response.created(null);
    builder.status(Response.Status.CREATED);

    return builder.build();
  }



  public JsonObject getChartData(final Integer pSemesterId, final String pCourseId, final Integer pExamType, final Integer courseType) throws Exception {

    User user = mUserManager.get( SecurityUtils.getSubject().getPrincipal().toString());
    //Need to check User Role
    //Need to check Course Type - Theory or Sessional
    List<GradeChartDataDto> examGradeStatusList = getContentManager().getChartData(pSemesterId, pCourseId, pExamType, CourseType.get(courseType));

    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();

    JsonReader jsonReader;
    JsonObject object1;

    for (GradeChartDataDto chartDto : examGradeStatusList) {
      jsonReader = Json.createReader(new StringReader(chartDto.toString()));
      object1 = jsonReader.readObject();
      jsonReader.close();
      children.add(object1);
    }
    object.add("entries", children);

    return object.build();
  }

  public JsonObject getGradeSubmissionDeadline(final Integer pSemesterId, final Integer pExamType, final String pExamDate, final UriInfo pUriInfo) throws Exception{

    List<MarksSubmissionStatusDto> marksSubmissionStatusDtoList = new ArrayList<>();
    Integer size = getContentManager().checkSize(pSemesterId,pExamType,pExamDate);

    if(size==0){
      getContentManager().insertGradeSubmissionDeadLineInfo(pSemesterId,pExamType,pExamDate);
      marksSubmissionStatusDtoList = getContentManager().getGradeSubmissionDeadLine(pSemesterId,pExamType,pExamDate);
    }else{
      marksSubmissionStatusDtoList = mManager.getGradeSubmissionDeadLine(pSemesterId,pExamType,pExamDate);
    }

    Collections.sort(marksSubmissionStatusDtoList, new Comparator<MarksSubmissionStatusDto>() {

      @Override
      public int compare(MarksSubmissionStatusDto o1, MarksSubmissionStatusDto o2) {
        int c;
        c = o1.getProgramShortname().compareTo(o2.getProgramShortname());
        if(c==0)
          c=o1.getCourseNo().compareTo(o2.getCourseNo());
        return c;
      }
    });

    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    for(MarksSubmissionStatusDto marksSubmissionStatusDto: marksSubmissionStatusDtoList){
      PersistentExamGrade examGrade= new PersistentExamGrade();
      examGrade.setExamDate(marksSubmissionStatusDto.getExamDate());
      examGrade.setProgramShortName(marksSubmissionStatusDto.getProgramShortname());
      examGrade.setCourseId(marksSubmissionStatusDto.getCourseId());
      examGrade.setCourseNo(marksSubmissionStatusDto.getCourseNo());
      examGrade.setCourseTitle(marksSubmissionStatusDto.getCourseTitle());
      examGrade.setCourseCreditHour(marksSubmissionStatusDto.getCourseCreditHour());
      examGrade.setTotalStudents(marksSubmissionStatusDto.getTotalStudents());
      if(marksSubmissionStatusDto.getLastSubmissionDate()!=null){
        examGrade.setLastSubmissionDate(marksSubmissionStatusDto.getLastSubmissionDate());
      }
      ExamGrade immutableExamGrade = examGrade;
      children.add(toJson(immutableExamGrade,pUriInfo,localCache));
    }

    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }



  public Response updateGradeSubmissionDeadLine(JsonObject pJsonObject,UriInfo pUriInfo) throws Exception{

    JsonArray entries = pJsonObject.getJsonArray("entries");
    List<MarksSubmissionStatusDto> deadlineList = new ArrayList<>();

    boolean isSemesterValid=true;
    for(int i=0;i<entries.size();i++){
      JsonObject jsonObject = entries.getJsonObject(i);
      MutableExamGrade examGrade = new PersistentExamGrade();
      LocalCache  localCache = new LocalCache();
      getBuilder().build(examGrade,jsonObject,localCache);
      if(i==0){
        Semester semester = mSemesterManager.get(examGrade.getSemesterId());
        if(semester.getStatus().getValue()!=1 ){
          isSemesterValid=false;
          break;
        }
      }

      MarksSubmissionStatusDto marksSubmissionStatusDto = new MarksSubmissionStatusDto();
      marksSubmissionStatusDto.setExamDate(examGrade.getExamDate());
      marksSubmissionStatusDto.setSemesterId(examGrade.getSemesterId());
      marksSubmissionStatusDto.setExamType(examGrade.getExamTypeId());
      marksSubmissionStatusDto.setCourseId(examGrade.getCourseId());
      deadlineList.add(marksSubmissionStatusDto);
    }

    if(isSemesterValid){
      getContentManager().updateForGradeSubmissionDeadLine(deadlineList);

    }

    URI contextURI = null;
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  //Get Course-wise marks submission status log
  public JsonObject getMarksSubmissionLogs(final Integer pSemesterId,final String pCourseId,final Integer pExamType) throws Exception {

    List<MarksSubmissionStatusLogDto> logList = getContentManager().getMarksSubmissionLogs(pSemesterId, pCourseId, pExamType);
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    JsonReader jsonReader;
    JsonObject jsonObject;

    for (MarksSubmissionStatusLogDto log : logList) {
      jsonReader = Json.createReader(new StringReader(log.toString()));
      jsonObject = jsonReader.readObject();
      jsonReader.close();
      children.add(jsonObject);
    }
    object.add("entries", children);
    return object.build();
  }

  //Get Student-wise log for Grade Submission
  public JsonObject getMarksLogs(final Integer pSemesterId,final String pCourseId,final Integer pExamType,final String pStudentId) throws Exception {
    MarksSubmissionStatusDto marksSubmissionStatusDto = getContentManager().getMarksSubmissionStatus(pSemesterId, pCourseId, pExamType);

    List<MarksLogDto> logList = getContentManager().getMarksLogs(pSemesterId, pCourseId, pExamType, pStudentId, marksSubmissionStatusDto.getCourseType());
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    JsonReader jsonReader;
    JsonObject jsonObject;

    for (MarksLogDto log : logList) {
      jsonReader = Json.createReader(new StringReader(log.toString()));
      jsonObject = jsonReader.readObject();
      jsonReader.close();
      children.add(jsonObject);
    }
    object.add("entries", children);
    return object.build();
  }
}
