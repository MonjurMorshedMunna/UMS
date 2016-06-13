package org.ums.common.academic.resource.helper;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.common.academic.resource.ResourceHelper;
import org.ums.common.academic.resource.SemesterResource;
import org.ums.common.builder.ExamGradeBuilder;
import org.ums.common.builder.ExamRoutineBuilder;
import org.ums.domain.model.dto.*;
import org.ums.domain.model.immutable.Course;
import org.ums.domain.model.immutable.ExamGrade;
import org.ums.domain.model.immutable.ExamRoutine;
import org.ums.domain.model.immutable.User;
import org.ums.domain.model.mutable.MutableClassRoom;
import org.ums.domain.model.mutable.MutableExamGrade;
import org.ums.domain.model.mutable.MutableExamRoutine;
import org.ums.domain.model.mutable.MutableUser;
import org.ums.enums.CourseMarksSubmissionStatus;
import org.ums.enums.RecheckStatus;
import org.ums.enums.StudentMarksSubmissionStatus;
import org.ums.manager.ExamGradeManager;
import org.ums.manager.ExamRoutineManager;
import org.ums.manager.UserManager;
import org.ums.persistent.model.PersistentClassRoom;
import org.ums.persistent.model.PersistentExamRoutine;
import org.ums.persistent.model.PersistentUser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.json.*;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ikh on 4/29/2016.
 */
@Component
public class GradeSubmissionResourceHelper extends ResourceHelper<ExamGrade, MutableExamGrade, Object> {

    @Autowired
    private ExamGradeManager mManager;

    @Autowired
    private ExamGradeBuilder mBuilder;

    @Autowired
    private  UserManager mUserManager;

    @Override
    protected Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
        throw new NotImplementedException();
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


    public JsonObject getGradeList(final String pRequestedRoleId,final Integer pSemesterId, final String pCourseId,final Integer pExamType) throws Exception {
        List<StudentGradeDto> examGradeList = getContentManager().getAllGradeForTheoryCourse(pSemesterId,pCourseId, pExamType);

        MarksSubmissionStatusDto marksSubmissionStatusDto = getContentManager().getMarksSubmissionStatus(pSemesterId, pCourseId, pExamType);
        JsonReader jsonReader = Json.createReader(new StringReader(marksSubmissionStatusDto.toString()));
        JsonObject object1 = jsonReader.readObject();
        JsonObjectBuilder object = Json.createObjectBuilder();
        jsonReader.close();
        object.add("part_info",object1);

        String currentActor=getActorForCurrentUser(SecurityUtils.getSubject().getPrincipal().toString(),pRequestedRoleId,pSemesterId,pCourseId);
        object.add("current_actor",currentActor);
        object.add("current_course_status",marksSubmissionStatusDto.getStatusId());



        JsonArrayBuilder noneAndSubmitArrayBuilder = Json.createArrayBuilder();


        JsonArrayBuilder scrutinizeCandidatesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder approveCandidatesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder acceptCandidatesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recheckCandidatesArrayBuilder = Json.createArrayBuilder();

        JsonArrayBuilder submittedArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder scrutinizedArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder approvedArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder acceptedArrayBuilder = Json.createArrayBuilder();

        JsonArrayBuilder recheckAcceptedArrayBuilder = Json.createArrayBuilder();

        StudentMarksSubmissionStatus gradeStatus;
        CourseMarksSubmissionStatus courseStatus;

        for (StudentGradeDto gradeDto : examGradeList) {
            jsonReader = Json.createReader(new StringReader(gradeDto.toString()));
            object1 = jsonReader.readObject();
            jsonReader.close();
            gradeStatus=gradeDto.getStatus();
            courseStatus=CourseMarksSubmissionStatus.values()[marksSubmissionStatusDto.getStatusId()];


            /*
            if(gradeStatus == StudentMarksSubmissionStatus.NONE  && gradeDto.getRecheckStatus()==0 )
                noneSubmittedArrayBuilder.add(object1);
            else if((courseStatus== CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_SCRUTINIZER ||
                courseStatus== CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_HEAD ||
                courseStatus== CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_COE )
                &&
                gradeStatus == StudentMarksSubmissionStatus.DO_RECHECK  && gradeStatus == StudentMarksSubmissionStatus.RECHECK )
                recheckListArrayBuilder.add(object1);

            else if(courseStatus== CourseMarksSubmissionStatus.WAITING_FOR_SCRUTINY  && (gradeStatus == StudentMarksSubmissionStatus.RECHECKED || gradeStatus == StudentMarksSubmissionStatus.SCRUTINIZED || gradeStatus == StudentMarksSubmissionStatus.SUBMITTED) )
                waitingForScrutinyArrayBuilder.add(object1);
            else if(courseStatus== CourseMarksSubmissionStatus.WAITING_FOR_HEAD_APPROVAL && gradeStatus==StudentMarksSubmissionStatus.APPROVE && gradeStatus==StudentMarksSubmissionStatus.SCRUTINIZED)
                waitingForHeadApprovalArrayBuilder.add(object1);
            else if(courseStatus== CourseMarksSubmissionStatus.WAITING_FOR_COE_APPROVAL && gradeStatus==StudentMarksSubmissionStatus.ACCEPT && gradeStatus==StudentMarksSubmissionStatus.APPROVED)
                waitingForCoEApprovalArrayBuilder.add(object1);
            */
            if(gradeStatus == StudentMarksSubmissionStatus.NONE || gradeStatus == StudentMarksSubmissionStatus.SUBMIT  && gradeDto.getRecheckStatusId()==0  && currentActor.equalsIgnoreCase("preparer"))
                noneAndSubmitArrayBuilder.add(object1);
            else if( (gradeStatus == StudentMarksSubmissionStatus.SUBMITTED  || gradeStatus == StudentMarksSubmissionStatus.SCRUTINIZE ) && currentActor.equalsIgnoreCase("scrutinizer"))
                scrutinizeCandidatesArrayBuilder.add(object1);
            else if( (gradeStatus == StudentMarksSubmissionStatus.SCRUTINIZED  || gradeStatus == StudentMarksSubmissionStatus.APPROVE ) && currentActor.equalsIgnoreCase("head"))
                approveCandidatesArrayBuilder.add(object1);
            else if( (gradeStatus == StudentMarksSubmissionStatus.APPROVED  || gradeStatus == StudentMarksSubmissionStatus.ACCEPT ) && currentActor.equalsIgnoreCase("coe"))
                acceptCandidatesArrayBuilder.add(object1);

            if((gradeStatus == StudentMarksSubmissionStatus.SUBMITTED ||  gradeStatus == StudentMarksSubmissionStatus.SCRUTINIZE)  )//&& !currentActor.equalsIgnoreCase("scrutinizer")
                submittedArrayBuilder.add(object1);
            else if((gradeStatus == StudentMarksSubmissionStatus.SCRUTINIZED ||  gradeStatus == StudentMarksSubmissionStatus.APPROVE) ) // && !currentActor.equalsIgnoreCase("head")
                scrutinizedArrayBuilder.add(object1);
            else if((gradeStatus == StudentMarksSubmissionStatus.APPROVED  ||  gradeStatus == StudentMarksSubmissionStatus.ACCEPT)  ) //&& !currentActor.equalsIgnoreCase("coe")
                approvedArrayBuilder.add(object1);
            else if(gradeStatus == StudentMarksSubmissionStatus.ACCEPTED ) {
                acceptedArrayBuilder.add(object1);
                if(gradeDto.getRecheckStatus()==RecheckStatus.RECHECK_TRUE)
                    recheckAcceptedArrayBuilder.add(object1);
            }
            else if(gradeStatus == StudentMarksSubmissionStatus.ACCEPTED )
                acceptedArrayBuilder.add(object1);

            if((courseStatus==CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_SCRUTINIZER || courseStatus==CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_HEAD
                || courseStatus==CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_COE) &&  gradeDto.getRecheckStatus()== RecheckStatus.RECHECK_TRUE){
                recheckCandidatesArrayBuilder.add(object1);
            }
        }
//        object.add("none_submitted_grades", noneSubmittedArrayBuilder);
//        object.add("recheck_grades", recheckListArrayBuilder);
//        object.add("waiting_for_scrutiny_grades", waitingForScrutinyArrayBuilder);
//        object.add("waiting_for_head_approval_grades", waitingForHeadApprovalArrayBuilder);
//        object.add("waiting_for_CoE_approval_grades", waitingForCoEApprovalArrayBuilder);

        object.add("none_and_submit_grades", noneAndSubmitArrayBuilder);



        object.add("scrutinize_candidates_grades", scrutinizeCandidatesArrayBuilder);
        object.add("approve_candidates_grades", approveCandidatesArrayBuilder);
        object.add("accept_candidates_grades", acceptCandidatesArrayBuilder);
        object.add("recheck_candidates_grades", recheckCandidatesArrayBuilder);

        object.add("submitted_grades", submittedArrayBuilder);
        object.add("scrutinized_grades", scrutinizedArrayBuilder);
        object.add("approved_grades", approvedArrayBuilder);
        object.add("accepted_grades", acceptedArrayBuilder);
        object.add("recheck_accepted_grades", recheckAcceptedArrayBuilder);




        return object.build();
    }


    public JsonObject getGradeSubmissionStatus(final Integer pSemesterId, final Integer pExamType,final String deptId,final String pUserRole) throws Exception {

        String userId = "";
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            userId = subject.getPrincipal().toString();
        }
        User user=mUserManager.get(userId);
        List<MarksSubmissionStatusDto> examGradeStatusList = getContentManager().getMarksSubmissionStatus(pSemesterId,pExamType,user.getEmployeeId(), deptId,pUserRole);

        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder children = Json.createArrayBuilder();

        JsonReader jsonReader;
        JsonObject object1;

        for (MarksSubmissionStatusDto statusDto : examGradeStatusList) {
            jsonReader = Json.createReader(new StringReader(statusDto.toString()));
            object1 = jsonReader.readObject();
            jsonReader.close();
            children.add(object1);
        }
        object.add("entries", children);

        return object.build();
    }


    public Response saveGradeSheet(final JsonObject pJsonObject) throws Exception {

        //Should do the validation
        //. Is he the right person
        //. have the time period to do the operation
        //. other validations...
        List<StudentGradeDto> gradeList=getBuilder().build(pJsonObject);
        MarksSubmissionStatusDto partInfoDto=new MarksSubmissionStatusDto();
        getBuilder().build(partInfoDto,pJsonObject);

        String action=pJsonObject.getString("action");


        int aa= getContentManager().updatePartInfo(partInfoDto.getSemesterId(),partInfoDto.getCourseId(),partInfoDto.getExamType(),partInfoDto.getTotal_part(),partInfoDto.getPart_a_total(),partInfoDto.getPart_b_total());
        boolean updateStatus = getContentManager().saveGradeSheet(11012016, "EEE1101_S2014_110500",1, gradeList);

        if(action.equalsIgnoreCase("submit")){
            //do the status update work here....
            int bb = getContentManager().updateCourseMarksSubmissionStatus(11012016, "EEE1101_S2014_110500",1, CourseMarksSubmissionStatus.WAITING_FOR_SCRUTINY);
        }

        Response.ResponseBuilder builder = Response.created(null);
        builder.status(Response.Status.CREATED);

        return builder.build();
    }

    public Response updateGradeStatus(final JsonObject pJsonObject) throws Exception {

        //Should do the validation
        //. Is he the right person
        //. have the time period to do the operation
        //. other validations...
        String action=pJsonObject.getString("action");
        String actor=pJsonObject.getString("actor");
        int current_course_status=pJsonObject.getInt("course_current_status");

        ArrayList<List<StudentGradeDto>> gradeList=getBuilder().buildForRecheckApproveGrade(action,actor,pJsonObject);
        MarksSubmissionStatusDto partInfoDto=new MarksSubmissionStatusDto();
        getBuilder().build(partInfoDto,pJsonObject);



        boolean updateStatus;
        //Need to improve this if else logic here....
        if(action.equals("save"))
            updateStatus=getContentManager().updateGradeStatus_Save(11012016, "EEE1101_S2014_110500", 1, gradeList.get(0),gradeList.get(1));
        else if(action.equals("recheck")) {
            updateStatus = getContentManager().updateGradeStatus_Recheck(11012016, "EEE1101_S2014_110500", 1, gradeList.get(0), gradeList.get(1));
            int bb = getContentManager().updateCourseMarksSubmissionStatus(11012016, "EEE1101_S2014_110500",1, getCourseMarksSubmissionNextStatus(actor,action,CourseMarksSubmissionStatus.values()[current_course_status]));
        }
        else if(action.equals("approve")) {
            updateStatus = getContentManager().updateGradeStatus_Approve(11012016, "EEE1101_S2014_110500", 1, gradeList.get(0), gradeList.get(1));
            int bb = getContentManager().updateCourseMarksSubmissionStatus(11012016, "EEE1101_S2014_110500",1, getCourseMarksSubmissionNextStatus(actor,action,CourseMarksSubmissionStatus.values()[current_course_status]));
        }

        Response.ResponseBuilder builder = Response.created(null);
        builder.status(Response.Status.CREATED);

        return builder.build();
    }

    public CourseMarksSubmissionStatus getCourseMarksSubmissionNextStatus(String actor,String action,CourseMarksSubmissionStatus currentStatus){
        CourseMarksSubmissionStatus nextStatus=null;
        if(actor.equalsIgnoreCase("scrutinizer")){
            if(action.equalsIgnoreCase("recheck") && currentStatus==CourseMarksSubmissionStatus.WAITING_FOR_SCRUTINY)
                nextStatus=CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_SCRUTINIZER;
            else if(action.equalsIgnoreCase("approve") && currentStatus==CourseMarksSubmissionStatus.WAITING_FOR_SCRUTINY)
                nextStatus=CourseMarksSubmissionStatus.WAITING_FOR_HEAD_APPROVAL;
        }
        else if(actor.equalsIgnoreCase("head")){
            if(action.equalsIgnoreCase("recheck") && currentStatus==CourseMarksSubmissionStatus.WAITING_FOR_HEAD_APPROVAL)
                nextStatus=CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_HEAD;
            else if(action.equalsIgnoreCase("approve") && currentStatus==CourseMarksSubmissionStatus.WAITING_FOR_HEAD_APPROVAL)
                nextStatus=CourseMarksSubmissionStatus.WAITING_FOR_COE_APPROVAL;
        }
        else if(actor.equalsIgnoreCase("coe")){
            if(action.equalsIgnoreCase("recheck") && currentStatus==CourseMarksSubmissionStatus.WAITING_FOR_COE_APPROVAL)
                nextStatus=CourseMarksSubmissionStatus.REQUESTED_FOR_RECHECK_BY_COE;
            else if(action.equalsIgnoreCase("approve") && currentStatus==CourseMarksSubmissionStatus.WAITING_FOR_COE_APPROVAL)
                nextStatus=CourseMarksSubmissionStatus.ACCEPTED_BY_COE;
        }

        return nextStatus;
    }

    public String getActorForCurrentUser(String userId,String requestedRole,int semesterId,String courseId) throws Exception {
        if(requestedRole.equalsIgnoreCase("T")) {
            User user=mUserManager.get(userId);
            List<String> roleList = mManager.getRoleForTeacher(user.getEmployeeId(), semesterId, courseId);
            if(roleList.size()==0)
                return "Invalid";
            else
                return roleList.get(0);
        }
        else if(requestedRole.equalsIgnoreCase("H")) {
            List<String> roleList = mManager.getRoleForHead(userId);
            if(roleList.size()==0)
                return "Invalid";
            else
                return roleList.get(0);
        }
        else if(requestedRole.equalsIgnoreCase("C")) {
            List<String> roleList = mManager.getRoleForCoE(userId);
            if(roleList.size()==0)
                return "Invalid";
            else
                return roleList.get(0);
        }
        return "Invalid";
    }


}
