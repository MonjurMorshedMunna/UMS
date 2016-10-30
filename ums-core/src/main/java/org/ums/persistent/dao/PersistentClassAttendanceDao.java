package org.ums.persistent.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.ums.decorator.ExamGradeDaoDecorator;
import org.ums.domain.model.dto.*;
import org.ums.domain.model.mutable.MutableExaminer;
import org.ums.enums.CourseMarksSubmissionStatus;
import org.ums.enums.CourseType;
import org.ums.enums.ExamType;
import org.ums.manager.ClassAttendanceManager;
import org.ums.util.Constants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ifti on 29-Oct-16.
 */
public class PersistentClassAttendanceDao implements ClassAttendanceManager {

  String ATTENDANCE_QUERY = "Select Course_Id,Class_Date,Serial,Student_Id,Attendance From  "
      + "(Select Course_id,Id,to_char(Class_Date,'DDMMYYYY') class_date,serial,Teacher_Id  "
      + "From MST_CLASS_ATTENDANCE Where Semester_Id=? And Course_id=? )tmp1  "
      + "Left Outer Join DTL_CLASS_ATTENDANCE "
      + "on tmp1.Id = DTL_CLASS_ATTENDANCE.Attendance_Id ";

  String ATTENDANCE_ALL = ATTENDANCE_QUERY + " Order by StudentId";

  String ATTENDANCE_ONLY_REGISTERED = "Select * From ( " + ATTENDANCE_QUERY
      + ")test1,UG_Registration_Result reg " + "Where test1.student_id=reg.student_id "
      + "And test1.course_id=reg.course_id " + "Order by StudentId";

  String ATTENDANCE_DATE_QUERY =
      "Select TO_Char(Class_Date,'DD MON, YY') Class_Date,To_Char(Class_Date,'DDMMYYYY') CLASS_DATE_F1,Serial,Teacher_Id  "
          + "From MST_CLASS_ATTENDANCE Where Semester_Id= ? And Course_id=? "
          + "Order by Class_Date,Serial ";

  String ATTENDANCE_STUDENTS_ALL =
      "Select * From ( "
          + "Select Students.Student_Id,Full_Name From ( "
          + "Select distinct Students.Student_Id from UG_Registration_Result,Students Where  "
          + "UG_Registration_Result.Semester_Id=? and Course_Id=? and exam_type=1 "
          + "And Students.student_id=UG_Registration_Result.student_id and Theory_Section='A' "
          + "union "
          + "Select Student_Id From DTL_CLASS_ATTENDANCE,MST_CLASS_ATTENDANCE Where  MST_CLASS_ATTENDANCE.id=DTL_CLASS_ATTENDANCE.ATTENDANCE_ID "
          + "And Semester_Id=? and Course_id=? and Section='A' "
          + ")tmp1,Students Where tmp1.Student_Id=Students.Student_id "
          + ")tmp2  "
          + "Where Student_Id in (Select Student_Id From UG_Registration_Result Where Semester_Id=? and Course_Id=? and exam_type=1) "
          + "Order by Student_Id ";

  String ATTENDANCE_STUDENTS_ENROLLED = "Select * From ( "
      + "Select Students.Student_Id,Full_Name From ( "
      + "Select distinct Students.Student_Id from UG_Registration_Result,Students Where  "
      + "UG_Registration_Result.Semester_Id=? and Course_Id=? and exam_type=1 "
      + "And Students.student_id=UG_Registration_Result.student_id and Theory_Section='A' "
      + ")tmp1,Students Where tmp1.Student_Id=Students.Student_id " + ")tmp2 "
      + "Order by Student_Id ";

  private JdbcTemplate mJdbcTemplate;

  public PersistentClassAttendanceDao(final JdbcTemplate pJdbcTemplate) {
    mJdbcTemplate = pJdbcTemplate;
  }

  @Override
  public List<ClassAttendanceDto> getDateList(int pSemesterId, String pCourseId) throws Exception {
    String query = ATTENDANCE_DATE_QUERY;
    return mJdbcTemplate.query(query, new Object[] {pSemesterId, pCourseId},
        new AttendanceDateRowMapper());
  }

  @Override
  public List<ClassAttendanceDto> getStudentList(int pSemesterId, String pCourseId)
      throws Exception {
    String query = ATTENDANCE_STUDENTS_ENROLLED;
    return mJdbcTemplate.query(query, new Object[] {pSemesterId, pCourseId},
        new AttendanceStudentRowMapper());
  }

  @Override
  public Map getAttendance(int pSemesterId, String pCourseId) throws Exception {
    String query = ATTENDANCE_QUERY;
    return mJdbcTemplate.query(query, new Object[] {pSemesterId, pCourseId},
        new AttendanceRowMapper());
  }

  @Override
  public int updateAttendanceMaster(ClassAttendanceDto classAttendanceDto) throws Exception {
    String query =
        "Update MST_CLASS_ATTENDANCE Set Class_Date=To_Date(?, 'DD Mon, YY'),Serial=?  Where Id=?";
    return mJdbcTemplate.update(query, classAttendanceDto.getClassDate(),
        classAttendanceDto.getSerial(), classAttendanceDto.getId());
  }

  @Override
  public int insertAttendanceMaster(Integer pId, Integer pSemesterId, String pCourseId,
      String pSection, String pClassDate, Integer pSerial, String pTeacherId) {
    String query =
        "Insert InTo MST_CLASS_ATTENDANCE(ID,SEMESTER_ID,COURSE_ID,SECTION,CLASS_DATE,SERIAL,TEACHER_ID) "
            + "Values(?,?,?,?,To_Date(?, 'DD Mon, YY'),?,?) ";
    return mJdbcTemplate.update(query, pId, pSemesterId, pCourseId, pSection, pClassDate, pSerial,
        pTeacherId);
  }

  @Override
  public boolean upsertAttendanceDtl(Integer id, List<ClassAttendanceDto> attendanceList)
      throws Exception {
    batchInsertAttendanceDtl(id, attendanceList);
    return true;
  }

  public void batchInsertAttendanceDtl(Integer id, List<ClassAttendanceDto> attendanceList) {
    String sql =
        "merge into DTL_CLASS_ATTENDANCE "
            + "using (  "
            + "  select ? as Attendance_Id, "
            + "         ? as Student_Id, "
            + "         ? as Attendance "
            + "   from dual "
            + ")dt2 on (DTL_CLASS_ATTENDANCE.Attendance_Id = dt2.Attendance_Id And DTL_CLASS_ATTENDANCE.Student_Id = dt2.Student_Id) "
            + "when matched then  "
            + "update set DTL_CLASS_ATTENDANCE.Attendance =  dt2.Attendance "
            + "when not matched then "
            + "Insert values (dt2.Attendance_Id,dt2.Student_Id, dt2.Attendance)";

    mJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ClassAttendanceDto classAttendanceDto = attendanceList.get(i);
        ps.setInt(1, id);
        ps.setString(2, classAttendanceDto.getStudentId());
        ps.setInt(3, classAttendanceDto.getAttendance());
      }

      @Override
      public int getBatchSize() {
        return attendanceList.size();
      }
    });
  }

  @Override
  public int deleteAttendanceDtl(Integer attendanceId) throws Exception {
    String query = "Delete DTL_CLASS_ATTENDANCE Where Id=? ";
    return mJdbcTemplate.update(query, attendanceId);
  }

  @Override
  public int deleteAttendanceMaster(Integer attendanceId) throws Exception {
    String query = "Delete MST_CLASS_ATTENDANCE Where Attendance_Id=? ";
    return mJdbcTemplate.update(query, attendanceId);
  }

  class AttendanceStudentRowMapper implements RowMapper<ClassAttendanceDto> {
    @Override
    public ClassAttendanceDto mapRow(ResultSet resultSet, int i) throws SQLException {
      ClassAttendanceDto attendanceDto = new ClassAttendanceDto();
      attendanceDto.setStudentId(resultSet.getString("STUDENT_ID"));
      attendanceDto.setStudentName(resultSet.getString("FULL_NAME"));
      AtomicReference<ClassAttendanceDto> atomicReference = new AtomicReference<>(attendanceDto);
      return atomicReference.get();
    }
  }

  class AttendanceDateRowMapper implements RowMapper<ClassAttendanceDto> {
    @Override
    public ClassAttendanceDto mapRow(ResultSet resultSet, int i) throws SQLException {
      ClassAttendanceDto attendanceDto = new ClassAttendanceDto();
      attendanceDto.setClassDate(resultSet.getString("CLASS_DATE"));
      attendanceDto.setClassDateFormat1(resultSet.getString("CLASS_DATE_F1"));
      attendanceDto.setSerial(resultSet.getInt("SERIAL"));
      attendanceDto.setTeacherId(resultSet.getString("TEACHER_ID"));
      AtomicReference<ClassAttendanceDto> atomicReference = new AtomicReference<>(attendanceDto);
      return atomicReference.get();
    }
  }

  class AttendanceRowMapper implements ResultSetExtractor<Map> {
    @Override
    public Map extractData(ResultSet rs) throws SQLException, DataAccessException {
      HashMap<String, String> mapRet = new HashMap<String, String>();
      String key = "";
      while(rs.next()) {
        key = rs.getString("CLASS_DATE") + rs.getString("SERIAL") + rs.getString("STUDENT_ID");
        mapRet.put(key, rs.getString("ATTENDANCE"));
      }
      return mapRet;
    }

  }
}
