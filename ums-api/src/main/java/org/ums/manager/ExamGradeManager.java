package org.ums.manager;

import org.ums.domain.model.dto.*;
import org.ums.domain.model.immutable.ExamGrade;
import org.ums.domain.model.mutable.MutableExamGrade;
import org.ums.enums.CourseMarksSubmissionStatus;
import org.ums.enums.CourseType;
import org.ums.enums.ExamType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ikh on 4/30/2016.
 */
public interface ExamGradeManager extends ContentManager<ExamGrade, MutableExamGrade, Object> {
    public List<GradeChartDataDto> getChartData(int semesterId,String courseId,ExamType examType,CourseType courseType) throws Exception;
    public List<StudentGradeDto> getAllGrades(int semesterId,String courseId,ExamType examType,CourseType courseType) throws Exception;

    public MarksSubmissionStatusDto getMarksSubmissionStatus(int semesterId,String courseId,ExamType examType) throws Exception;
    public List<MarksSubmissionStatusDto> getMarksSubmissionStatus(Integer pSemesterId,Integer pExamType,Integer pProgramId,Integer year, Integer semester,String teacherId,String deptId,String userRole,Integer status) throws Exception;
    public boolean saveGradeSheet(MarksSubmissionStatusDto actualStatusDTO,List<StudentGradeDto> gradeList) throws Exception;
    public boolean insertGradeLog(String userId,String userRole,MarksSubmissionStatusDto actualStatusDTO,CourseMarksSubmissionStatus nextStatus,List<StudentGradeDto> gradeList) throws Exception;
    public int insertMarksSubmissionStatusLog(String userId,String userRole,MarksSubmissionStatusDto actualStatusDTO,CourseMarksSubmissionStatus status) throws Exception ;
    public boolean updateGradeStatus_Save(MarksSubmissionStatusDto actualStatusDTO,List<StudentGradeDto> recheckList,List<StudentGradeDto> approveList) throws Exception ;
    public boolean updateGradeStatus_Recheck(MarksSubmissionStatusDto actualStatusDTO,List<StudentGradeDto> recheckList,List<StudentGradeDto> approveList) throws Exception ;
    public boolean updateGradeStatus_Approve(MarksSubmissionStatusDto actualStatusDTO,List<StudentGradeDto> recheckList,List<StudentGradeDto> approveList) throws Exception ;
    public int updateCourseMarksSubmissionStatus(MarksSubmissionStatusDto actualStatusDTO,CourseMarksSubmissionStatus status) throws Exception;
    public int updatePartInfo(MarksSubmissionStatusDto requestedStatusDTO) throws Exception;


    public int approveRecheckRequest(MarksSubmissionStatusDto actualStatusDTO) throws Exception ;
    public int rejectRecheckRequest(MarksSubmissionStatusDto actualStatusDTO) throws Exception ;

    public List<String> getRoleForTeacher(String pTeacherId,int  pSemesterId,String pCourseId) throws Exception;
    public List<String> getRoleForHead(String pUserId) throws Exception;
    public List<String> getRoleForCoE(String pUserId) throws Exception;
    public List<String> getRoleForVC(String pUserId) throws Exception;


    public int checkSize(Integer pSemesterId,ExamType pExamType, String pExamDate);
    public int insertGradeSubmissionDeadLineInfo(Integer pSemesterId,ExamType pExamType, String pExamDate);
    public List<MarksSubmissionStatusDto> getGradeSubmissionDeadLine(Integer pSemesterId, ExamType pExamType, String pExamDate);
    public int updateForGradeSubmissionDeadLine(List<MarksSubmissionStatusDto> pMarksSubmissionStatusDtos) throws Exception;
    public int getTotalStudentCount(MarksSubmissionStatusDto actualStatusDTO) throws Exception;
    public List<MarksSubmissionStatusLogDto> getMarksSubmissionLogs(Integer pSemesterId, String pCourseId,Integer pExamType) throws Exception ;
    public List<MarksLogDto> getMarksLogs(Integer pSemesterId,String pCourseId,ExamType pExamType,String pStudentId, CourseType pCourseType) throws Exception ;
    public Map getUserRoleList(Integer pSemesterId, String pCourseId) throws Exception;

}

