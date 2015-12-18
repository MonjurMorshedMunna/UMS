package org.ums.academic.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.ums.academic.model.PersistentProgramType;
import org.ums.domain.model.MutableProgramType;
import org.ums.domain.model.ProgramType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProgramTypeDao extends ContentDaoDecorator<ProgramType, MutableProgramType, Integer> {
  static String SELECT_ALL = "SELECT TYPE_ID, TYPE_NAME FROM MST_PROGRAM_TYPE ";
  static String UPDATE_ONE = "UPDATE MST_PROGRAM_TYPE SET TYPE_NAME = ? ";
  static String DELETE_ONE = "DELETE FROM MST_PROGRAM_TYPE ";
  static String INSERT_ONE = "INSERT INTO MST_PROGRAM_TYPE(TYPE_ID, TYPE_NAME) VALUES(?, ?) ";

  private JdbcTemplate mJdbcTemplate;

  public ProgramTypeDao(final JdbcTemplate pJdbcTemplate) {
    mJdbcTemplate = pJdbcTemplate;
  }

  public ProgramType get(final Integer pId) throws Exception {
    String query = SELECT_ALL + "WHERE TYPE_ID = ?";
    return mJdbcTemplate.queryForObject(query, new Object[]{pId}, new ProgramTypeRowMapper());
  }

  public List<ProgramType> getAll() throws Exception {
    String query = SELECT_ALL;
    return mJdbcTemplate.query(query, new ProgramTypeRowMapper());
  }

  public void update(final MutableProgramType pProgramType) throws Exception {
    String query = UPDATE_ONE + "WHERE TYPE_ID = ?";
    mJdbcTemplate.update(query, pProgramType.getName(), pProgramType.getId());
  }

  public void delete(final MutableProgramType pProgramType) throws Exception {
    String query = DELETE_ONE + "WHERE TYPE_ID = ?";
    mJdbcTemplate.update(query, pProgramType.getId());
  }

  public void create(final MutableProgramType pProgramType) throws Exception {
    mJdbcTemplate.update(INSERT_ONE, pProgramType.getId(), pProgramType.getName());
  }

  class ProgramTypeRowMapper implements RowMapper<ProgramType> {
    @Override
    public ProgramType mapRow(ResultSet resultSet, int i) throws SQLException {
      PersistentProgramType programType = new PersistentProgramType();
      programType.setId(resultSet.getInt("TYPE_ID"));
      programType.setName(resultSet.getString("TYPE_NAME"));
      AtomicReference<ProgramType> atomicReference = new AtomicReference<>(programType);
      return atomicReference.get();
    }
  }
}
