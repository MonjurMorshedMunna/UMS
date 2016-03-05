package org.ums.academic.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.ums.academic.model.PersistentEnrollmentFromTo;
import org.ums.domain.model.mutable.MutableEnrollmentFromTo;
import org.ums.domain.model.readOnly.EnrollmentFromTo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PersistentEnrollmentFromToDao extends EnrollmentFromToDaoDecorator {
  private String SELECT_ALL = "SELECT PROGRAM_ID, FROM_YEAR, FROM_SEMESTER, TO_YEAR, TO_SEMESTER, LAST_MODIFIED, ID FROM ENROLLMENT_FROM_TO ";
  private String INSERT_ALL = "INSERT INTO ENROLLMENT_FROM_TO(PROGRAM_ID, FROM_YEAR, FROM_SEMESTER, TO_YEAR, TO_SEMESTER, LAST_MODIFIED) VALUES" +
      "(?, ?, ?, ?, ?, " + getLastModifiedSql() + ")";
  private String UPDATE_ALL = "UPDATE ENROLLMENT_FROM_TO SET " +
      "PROGRAM_ID = ?," +
      "FROM_YEAR = ?," +
      "FROM_SEMESTER = ?," +
      "TO_YEAR = ?," +
      "TO_SEMESTER = ?," +
      "LAST_MODIFIED = " + getLastModifiedSql() + " ";
  private String DELETE_ALL = "DELETE FROM ENROLLMENT_FROM_TO ";
  private JdbcTemplate mJdbcTemplate;

  public PersistentEnrollmentFromToDao(JdbcTemplate pJdbcTemplate) {
    mJdbcTemplate = pJdbcTemplate;
  }

  @Override
  public int create(MutableEnrollmentFromTo pMutable) throws Exception {
    return mJdbcTemplate.update(INSERT_ALL,
        pMutable.getProgram().getId(),
        pMutable.getFromYear(),
        pMutable.getFromSemester(),
        pMutable.getToYear(),
        pMutable.getToSemester());
  }

  @Override
  public int delete(MutableEnrollmentFromTo pMutable) throws Exception {
    String query = DELETE_ALL + "WHERE ID = ?";
    return mJdbcTemplate.update(query, pMutable.getId());
  }

  @Override
  public int update(MutableEnrollmentFromTo pMutable) throws Exception {
    String query = UPDATE_ALL + "WHERE ID = ?";
    return mJdbcTemplate.update(query,
        pMutable.getProgram().getId(),
        pMutable.getFromYear(),
        pMutable.getFromSemester(),
        pMutable.getToYear(),
        pMutable.getToSemester(),
        pMutable.getId());
  }

  @Override
  public EnrollmentFromTo get(Integer pId) throws Exception {
    String query = SELECT_ALL + "WHERE ID = ?";
    return mJdbcTemplate.queryForObject(query, new Object[]{pId}, new EnrollmentFromToRowMapper());
  }

  @Override
  public List<EnrollmentFromTo> getAll() throws Exception {
    return mJdbcTemplate.query(SELECT_ALL, new EnrollmentFromToRowMapper());
  }

  class EnrollmentFromToRowMapper implements RowMapper<EnrollmentFromTo> {
    @Override
    public EnrollmentFromTo mapRow(ResultSet rs, int rowNum) throws SQLException {
      MutableEnrollmentFromTo enrollment = new PersistentEnrollmentFromTo();
      enrollment.setProgramId(rs.getInt("PROGRAM_ID"));
      enrollment.setFromYear(rs.getInt("FROM_YEAR"));
      enrollment.setFromSemester(rs.getInt("FROM_SEMESTER"));
      enrollment.setToYear(rs.getInt("TO_YEAR"));
      enrollment.setToSemester(rs.getInt("TO_SEMESTER"));
      enrollment.setLastModified(rs.getString("LAST_MODIFIED"));
      enrollment.setId(rs.getInt("ID"));
      AtomicReference<EnrollmentFromTo> atomicReference = new AtomicReference<>(enrollment);
      return atomicReference.get();
    }
  }
}
