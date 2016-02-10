package org.ums.academic.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.ums.academic.model.PersistentUser;
import org.ums.domain.model.mutable.MutableUser;
import org.ums.domain.model.readOnly.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PersistentUserDao extends UserDaoDecorator {
  static String SELECT_ALL = "SELECT USER_ID, PASSWORD, ROLE_ID, STATUS, TEMP_PASSWORD,PR_TOKEN,TOKEN_GENERATED_ON FROM USERS ";
  static String UPDATE_ALL = "UPDATE USERS SET PASSWORD = ?, ROLE_ID = ?, STATUS = ?, TEMP_PASSWORD = ? ";
  static String UPDATE_PASSWORD = "UPDATE USERS SET PASSWORD=? ";
  static String CLEAR_PASSWORD_RESET_TOKEN = "UPDATE USERS SET PR_TOKEN=NULL,TOKEN_GENERATED_ON=NULL  ";
  static String DELETE_ALL = "DELETE FROM USERS ";
  static String INSERT_ALL = "INSERT INTO USERS(USER_ID, PASSWORD, ROLE_ID, STATUS, TEMP_PASSWORD) VALUES " +
      "(?, ?, ?, ?, ?)";
  static String UPDATE_PASSWORD_RESET_TOKEN = "Update USERS Set PR_TOKEN=?,TOKEN_GENERATED_ON=SYSDATE Where User_Id=? ";

  private JdbcTemplate mJdbcTemplate;

  public PersistentUserDao(final JdbcTemplate pJdbcTemplate) {
    mJdbcTemplate = pJdbcTemplate;
  }

  @Override
  public List<User> getAll() throws Exception {
    String query = SELECT_ALL;
    return mJdbcTemplate.query(query, new UserRowMapper());
  }

  @Override
  public User get(String pId) throws Exception {
    String query = SELECT_ALL + "WHERE USER_ID = ?";
    return mJdbcTemplate.queryForObject(query, new Object[]{pId}, new UserRowMapper());
  }

  @Override
  public int update(MutableUser pMutable) throws Exception {
    String query = UPDATE_ALL + "WHERE USER_ID = ?";
    return mJdbcTemplate.update(query, pMutable.getPassword() == null ? "" : String.valueOf(pMutable.getPassword()),
        pMutable.getRole().getId(), pMutable.isActive(),
        pMutable.getTemporaryPassword() == null ? "" : String.valueOf(pMutable.getPassword()), pMutable.getId());
  }

  @Override
  public int updatePassword(final String pUserId, final String pPassword) throws Exception {
    String query = UPDATE_PASSWORD + "WHERE USER_ID = ?";
    return mJdbcTemplate.update(query, pPassword, pUserId);
  }

  @Override
  public int clearPasswordResetToken(final String pUserId) throws Exception {
    String query = CLEAR_PASSWORD_RESET_TOKEN + "WHERE USER_ID = ?";
    return mJdbcTemplate.update(query, pUserId);
  }


  @Override
  public int delete(MutableUser pMutable) throws Exception {
    String query = DELETE_ALL + "WHERE USER_ID = ?";
    return mJdbcTemplate.update(query, pMutable.getId());
  }

  @Override
  public int setPasswordResetToken(String pToken, String pUserId) throws Exception {
    return mJdbcTemplate.update(UPDATE_PASSWORD_RESET_TOKEN, pToken, pUserId);
  }

  @Override
  public int create(MutableUser pMutable) throws Exception {
    return mJdbcTemplate.update(INSERT_ALL, pMutable.getId(), pMutable.getPassword() == null ? "" : String.valueOf(pMutable.getPassword()),
        pMutable.getRole().getId(), pMutable.isActive(), pMutable.getTemporaryPassword() == null ? "" : String.valueOf(pMutable.getTemporaryPassword()));
  }

  class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      MutableUser user = new PersistentUser();
      user.setId(rs.getString("USER_ID"));
      user.setPassword(rs.getString("PASSWORD") == null ? null : rs.getString("PASSWORD").toCharArray());
      user.setRoleId(rs.getInt("ROLE_ID"));
      user.setActive(rs.getBoolean("STATUS"));
      user.setTemporaryPassword((rs.getString("TEMP_PASSWORD") == null ? null : rs.getString("TEMP_PASSWORD").toCharArray()));
      user.setPasswordResetToken(rs.getString("PR_TOKEN"));

      Timestamp timestamp = rs.getTimestamp("TOKEN_GENERATED_ON");
      if (timestamp != null)
        user.setPasswordTokenGenerateDateTime(new java.util.Date(timestamp.getTime()));

      AtomicReference<User> atomicReference = new AtomicReference<>(user);
      return atomicReference.get();
    }
  }
}
