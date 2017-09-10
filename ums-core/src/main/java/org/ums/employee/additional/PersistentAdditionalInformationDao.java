package org.ums.employee.additional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersistentAdditionalInformationDao extends AdditionalInformationDaoDecorator {

  static String INSERT_ONE =
      "INSERT INTO EMP_ADDITIONAL_INFO (EMPLOYEE_ID, ROOM_NO, EXT_NO, ACADEMIC_INITIAL, LAST_MODIFIED) VALUES (?, ?, ?, ?, "
          + getLastModifiedSql() + ")";

  static String GET_ONE = "SELECT EMPLOYEE_ID, ROOM_NO, EXT_NO, ACADEMIC_INITIAL FROM EMP_ADDITIONAL_INFO ";

  static String DELETE_ONE = "DELETE FROM EMP_ADDITIONAL_INFO ";

  private JdbcTemplate mJdbcTemplate;

  public PersistentAdditionalInformationDao(final JdbcTemplate pJdbcTemplate) {
    mJdbcTemplate = pJdbcTemplate;
  }

  @Override
  public int saveAdditionalInformation(MutableAdditionalInformation pMutableAdditionalInformation) {
    String query = INSERT_ONE;
    return mJdbcTemplate.update(query, pMutableAdditionalInformation.getId(),
        pMutableAdditionalInformation.getRoomNo(), pMutableAdditionalInformation.getExtNo(),
        pMutableAdditionalInformation.getAcademicInitial());
  }

  @Override
  public AdditionalInformation getAdditionalInformation(String pEmployeeId) {
    String query = GET_ONE + " WHERE EMPLOYEE_ID = ?";
    return mJdbcTemplate.queryForObject(query, new Object[] {pEmployeeId},
        new PersistentAdditionalInformationDao.RoleRowMapper());
  }

  @Override
  public int deleteAdditionalInformation(MutableAdditionalInformation pMutableAdditionalInformation) {
    String query = DELETE_ONE + " WHERE EMPLOYEE_ID = ?";
    return mJdbcTemplate.update(query, pMutableAdditionalInformation.getId());
  }

  class RoleRowMapper implements RowMapper<AdditionalInformation> {

    @Override
    public AdditionalInformation mapRow(ResultSet resultSet, int i) throws SQLException {
      PersistentAdditionalInformation persistentAdditionalInformation = new PersistentAdditionalInformation();
      persistentAdditionalInformation.setId(resultSet.getString("EMPLOYEE_ID"));
      persistentAdditionalInformation.setRoomNo(resultSet.getString("ROOM_NO"));
      persistentAdditionalInformation.setExtNo(resultSet.getString("EXT_NO"));
      persistentAdditionalInformation.setAcademicInitial(resultSet.getString("ACADEMIC_INITIAL"));
      return persistentAdditionalInformation;
    }
  }
}
