package org.ums.persistent.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.ums.decorator.ExamRoutineDaoDecorator;
import org.ums.domain.model.dto.ExamRoutineDto;
import org.ums.domain.model.mutable.MutableExamRoutine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ifti on 26-Feb-16.
 */
public class PersistentExamRoutineDao  extends ExamRoutineDaoDecorator {

  static String SELECT_ALL="Select TO_CHAR(EXAM_DATE,'DD/MM/YYYY') EXAM_DATE,EXAM_TIME,MST_PROGRAM.PROGRAM_ID,PROGRAM_SHORT_NAME, " +
      "YEAR,MST_COURSE.SEMESTER,COURSE_NO,COURSE_TITLE,MST_COURSE.COURSE_ID " +
      "From EXAM_ROUTINE,MST_COURSE,MST_SYLLABUS,MST_PROGRAM " +
      "Where EXAM_ROUTINE.SEMESTER=? " +
      "And Exam_Type=? " +
      "And EXAM_ROUTINE.COURSE_ID=MST_COURSE.COURSE_ID " +
      "And MST_COURSE.SYLLABUS_ID=MST_SYLLABUS.SYLLABUS_ID " +
      "And MST_SYLLABUS.PROGRAM_ID=MST_PROGRAM.PROGRAM_ID " +
      "Order By to_date(EXAM_DATE,'DD/MM/YYYY'),Exam_Time,Program_Id,Year,Semester,Course_No ";

  static String INSERT_ONE = "INSERT INTO EXAM_ROUTINE(SEMESTER,EXAM_TYPE,EXAM_DATE,EXAM_TIME,PROGRAM_ID,COURSE_ID) " +
      "VALUES(?,?,to_date(?,'dd/MM/YYYY'),?,?,?)";

  static String DELETE="DELETE EXAM_ROUTINE Where SEMESTER=? And Exam_Type=? ";


  private JdbcTemplate mJdbcTemplate;

  public PersistentExamRoutineDao(final JdbcTemplate pJdbcTemplate) {
    mJdbcTemplate = pJdbcTemplate;
  }

  @Override
  public List<ExamRoutineDto> getExamRoutine(int semesterId, int examTypeId) throws Exception {
    String query = SELECT_ALL;
    return mJdbcTemplate.query(query, new Object[]{semesterId, examTypeId}, new ExamRoutineRowMapper());
  }

  public int delete(final MutableExamRoutine pExamRoutine) throws Exception {
    if(pExamRoutine.getInsertType().equalsIgnoreCase("byProgram") ){
      String query = DELETE + " And  Exam_Date= to_date(?,'dd/MM/YYYY') And Exam_Time =?  and  Program_Id =?";
      String examDate= pExamRoutine.getRoutine().get(0).getExamDate();
      String examTime= pExamRoutine.getRoutine().get(0).getExamTime();
      int programId=  pExamRoutine.getRoutine().get(0).getProgramId();

      return mJdbcTemplate.update(query, pExamRoutine.getSemesterId(), pExamRoutine.getExamTypeId(),examDate,examTime,programId);
    }
    else if(pExamRoutine.getInsertType().equalsIgnoreCase("byDateTime") ){
      String query = DELETE + " And  Exam_Date= to_date(?,'dd/MM/YYYY') And Exam_Time = ?  ";
      String examDate= pExamRoutine.getRoutine().get(0).getExamDate();
      String examTime= pExamRoutine.getRoutine().get(0).getExamTime();
      return mJdbcTemplate.update(query, pExamRoutine.getSemesterId(), pExamRoutine.getExamTypeId(),examDate,examTime);
    }
    else if(pExamRoutine.getInsertType().equalsIgnoreCase("all") ){
      String query = DELETE ;
      return mJdbcTemplate.update(query, pExamRoutine.getSemesterId(), pExamRoutine.getExamTypeId());
    }
    return 0;
  }


  public int create(final MutableExamRoutine pExamRoutine) throws Exception {
    insertBatch(pExamRoutine);
    return 1;
  }
  public void insertBatch(final MutableExamRoutine pExamRoutine){
    List<ExamRoutineDto> routineList=pExamRoutine.getRoutine();
    String sql = INSERT_ONE;

    mJdbcTemplate .batchUpdate(sql, new BatchPreparedStatementSetter() {

      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ExamRoutineDto routine = routineList.get(i);
        ps.setInt(1, pExamRoutine.getSemesterId());
        ps.setInt(2, pExamRoutine.getExamTypeId());
        ps.setString(3,routine.getExamDate());
        ps.setString(4, routine.getExamTime());
        ps.setInt(5, routine.getProgramId());
        ps.setString(6, routine.getCourseId());

      }

      @Override
      public int getBatchSize() {
        return routineList.size();
      }

    });
  }

  class ExamRoutineRowMapper implements RowMapper<ExamRoutineDto> {
    @Override
    public ExamRoutineDto mapRow(ResultSet resultSet, int i) throws SQLException {
      ExamRoutineDto routine= new ExamRoutineDto();
      routine.setExamDate(resultSet.getString("EXAM_DATE"));
      routine.setExamTime(resultSet.getString("EXAM_TIME"));
      routine.setProgramId(resultSet.getInt("PROGRAM_ID"));
      routine.setProgramName(resultSet.getString("PROGRAM_SHORT_NAME"));
      routine.setCourseYear(resultSet.getInt("YEAR"));
      routine.setCourseSemester(resultSet.getInt("SEMESTER"));
      routine.setCourseNumber(resultSet.getString("COURSE_NO"));
      routine.setCourseId(resultSet.getString("COURSE_ID"));
      routine.setCourseTitle(resultSet.getString("COURSE_TITLE"));
      AtomicReference<ExamRoutineDto> atomicReference = new AtomicReference<>(routine);
      return atomicReference.get();
    }
  }
}
