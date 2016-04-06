package org.ums.persistent.dao;

import com.sun.rowset.internal.Row;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.ums.decorator.SemesterWithdrawalDaoDecorator;
import org.ums.domain.model.immutable.SemesterWithdrawal;
import org.ums.domain.model.immutable.SemesterWithdrawalLog;
import org.ums.domain.model.mutable.MutableSemesterWithdrawal;
import org.ums.enums.ApplicationStatus;
import org.ums.persistent.model.PersistentSemesterWithdrawal;
import org.ums.persistent.model.PersistentSemesterWithdrawalLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class PersistentSemesterWithdrawalDao extends SemesterWithdrawalDaoDecorator {

  static String SELECT_ALL = "SELECT SW_ID,SEMESTER_ID,PROGRAM_ID,STUDENT_ID,YEAR,SEMESTER,CAUSE,STATUS,APP_DATE,LAST_MODIFIED FROM SEMESTER_WITHDRAW";
  static String INSERT_ONE = "INSERT INTO SEMESTER_WITHDRAW(SEMESTER_ID,PROGRAM_ID,STUDENT_ID,YEAR,SEMESTER,CAUSE,STATUS,APP_DATE,LAST_MODIFIED) VALUES(?,?,?,?,?,?,?,to_date(to_char(sysdate,'DD MM YYYY HH:MM')),"+getLastModifiedSql()+") ";
  static String UPDATE_ONE = "UPDATE SEMESTER_WITHDRAW SET SEMESTER_ID=?,PROGRAM_ID=?,STUDENT_ID=?,YEAR=?,SEMESTER=?,CAUSE=?,STATUS=?,APP_DATE=to_date(to_char(sysdate,'DD MM YYYY HH:MM')),LAST_MODIFIED="+getLastModifiedSql()+" ";
  static String DELETE_ONE = "DELETE FROM SEMESTER_WITHDRAW ";



  private JdbcTemplate mJdbcTemplate;

  public PersistentSemesterWithdrawalDao(JdbcTemplate pJdbcTemplate){
    mJdbcTemplate = pJdbcTemplate;
  }



  @Override
  public SemesterWithdrawal getStudentsRecord(String studentId,int semesterId,int year,int semester) {
    String query = SELECT_ALL+" WHERE STUDENT_ID=? AND SEMESTER_ID=? AND YEAR=? AND SEMESTER=?";
    return mJdbcTemplate.queryForObject(query,new Object[]{studentId,semesterId,year,semester},new SemesterWithdrawalRowMapper());
  }

  @Override
  public List<SemesterWithdrawal> getSemesterWithdrawalForHead(String teacherId) {
    return super.getSemesterWithdrawalForHead(teacherId);
  }

  @Override
  public List<SemesterWithdrawal> getSemesterWithdrawalForAAO(String employeeId) {
    return super.getSemesterWithdrawalForAAO(employeeId);
  }

  @Override
  public List<SemesterWithdrawal> getSemesterWithdrawalForVC(String employeeId) {
    return super.getSemesterWithdrawalForVC(employeeId);
  }

  @Override
  public List<SemesterWithdrawal> getSemesterWithdrawalForRegistrar(String employeeId) {
    return super.getSemesterWithdrawalForRegistrar(employeeId);
  }

  @Override
  public List<SemesterWithdrawal> getSemesterWithdrawalForDeputyRegistrar(String employeeId) {
    return super.getSemesterWithdrawalForDeputyRegistrar(employeeId);
  }

  @Override
  public int update(MutableSemesterWithdrawal pMutable) throws Exception {
    String query = UPDATE_ONE+" WHERE SW_ID=?" ;
    return mJdbcTemplate.update(query,
          pMutable.getSemester().getId(),
          pMutable.getProgram().getId(),
          pMutable.getStudent().getId(),
          pMutable.getCause(),
          pMutable.getId()
        );
  }

  @Override
  public int delete(MutableSemesterWithdrawal pMutable) throws Exception {
    String query = DELETE_ONE+" WHERE SW_ID=?";
    return mJdbcTemplate.update(query,pMutable.getId());
  }

  @Override
  public SemesterWithdrawal get(Integer pId) throws Exception {
    String query = SELECT_ALL+" WHERE SW_ID=?";
    return mJdbcTemplate.queryForObject(query,new Object[]{pId},new SemesterWithdrawalRowMapper());
  }

  @Override
  public List<SemesterWithdrawal> getAll() throws Exception {
    String query = SELECT_ALL;
    return mJdbcTemplate.query(query,new SemesterWithdrawalRowMapper());
  }

  @Override
  public int create(MutableSemesterWithdrawal pMutable) throws Exception {
    String query = INSERT_ONE;
    return mJdbcTemplate.update(query,
        pMutable.getSemester().getId(),
        pMutable.getProgram().getId(),
        pMutable.getStudent().getId(),
        pMutable.getCause()
        );
  }





  class SemesterWithdrawalRowMapper implements RowMapper<SemesterWithdrawal>{
    @Override
    public SemesterWithdrawal mapRow(ResultSet pResultSet, int pI) throws SQLException {
      PersistentSemesterWithdrawal persistenceSW = new PersistentSemesterWithdrawal();
      persistenceSW.setId(pResultSet.getInt("SW_ID"));
      persistenceSW.setSemesterId(pResultSet.getInt("SEMESTER_ID"));
      persistenceSW.setStudentId(pResultSet.getString("STUDENT_ID"));
      persistenceSW.setAcademicYear(pResultSet.getInt("YEAR"));
      persistenceSW.setAcademicSemester(pResultSet.getInt("SEMESTER"));
      persistenceSW.setCause(pResultSet.getString("CAUSE"));
      persistenceSW.setProgramId(pResultSet.getInt("PROGRAM_ID"));
      persistenceSW.setStatus(pResultSet.getInt("STATUS"));
      persistenceSW.setAppDate(pResultSet.getString("APP_DATE"));
      persistenceSW.setLastModified(pResultSet.getString("LAST_MODIFIED"));
      return persistenceSW;
    }
  }

  /*class SemesterWithdrawalLogRowMapper implements RowMapper<SemesterWithdrawalLog>{
    @Override
    public SemesterWithdrawalLog mapRow(ResultSet pResultSet, int pI) throws SQLException {
      PersistentSemesterWithdrawalLog persistentSemesterWithdrawalLog = new PersistentSemesterWithdrawalLog();
      persistentSemesterWithdrawalLog.setId(pResultSet.getInt("SWL_ID"));
      persistentSemesterWithdrawalLog.setSemesterWithdrawalId(pResultSet.getInt("SW_ID"));
      persistentSemesterWithdrawalLog.setSemesterId(pResultSet.getInt("SEMESTER_ID"));
      persistentSemesterWithdrawalLog.setActor(pResultSet.getString("ACTOR"));
      persistentSemesterWithdrawalLog.setAction(pResultSet.getInt("ACTION"));
      persistentSemesterWithdrawalLog.setComments(pResultSet.getString("COMMENTS"));
      return persistentSemesterWithdrawalLog;
    }
  }*/
}

