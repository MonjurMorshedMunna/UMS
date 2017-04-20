package org.ums.fee.payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.ums.generator.IdGenerator;

import com.google.common.collect.Lists;

public class StudentPaymentDao extends StudentPaymentDaoDecorator {
  String SELECT_ALL = "SELECT ID, TRANSACTION_ID, STUDENT_ID, SEMESTER_ID, AMOUNT, STATUS, APPLIED_ON, VERIFIED_ON,"
      + " LAST_MODIFIED FROM STUDENT_PAYMENT ";
  String INSERT_ALL =
      "INSERT INTO STUDENT_PAYMENT (ID, TRANSACTION_ID, STUDENT_ID, SEMESTER_ID, AMOUNT, STATUS, APPLIED_ON, "
          + "LAST_MODIFIED) VALUES (?, ?, ?, ?, ?, ?, SYSDATE " + getLastModifiedSql() + ") ";
  String UPDATE_ALL = "UPDATE STUDENT_PAYMENT SET STATUS = ?, VERIFIED_ON = SYSDATE, LAST_MODIFIED = "
      + getLastModifiedSql() + " ";

  String DELETE_ALL = "DELETE FROM STUDENT_PAYMENT ";

  private JdbcTemplate mJdbcTemplate;
  private IdGenerator mIdGenerator;

  public StudentPaymentDao(JdbcTemplate mJdbcTemplate, IdGenerator mIdGenerator) {
    this.mJdbcTemplate = mJdbcTemplate;
    this.mIdGenerator = mIdGenerator;
  }

  @Override
  public StudentPayment get(Long pId) {
    String query = SELECT_ALL + "WHERE ID = ?";
    return mJdbcTemplate.queryForObject(query, new Object[] {pId}, new StudentPaymentRowMapper());
  }

  @Override
  public int update(MutableStudentPayment pMutable) {
    String query = UPDATE_ALL + "WHERE ID = ?";
    return mJdbcTemplate.update(query, pMutable.getStatus().getValue(), pMutable.getId());
  }

  @Override
  public int update(List<MutableStudentPayment> pMutableList) {
    List<Object[]> params = getUpdateParamArray(pMutableList);
    return mJdbcTemplate.batchUpdate(UPDATE_ALL, params).length;
  }

  @Override
  public Long create(MutableStudentPayment pMutable) {
    return create(Lists.newArrayList(pMutable)).get(0);
  }

  @Override
  public List<Long> create(List<MutableStudentPayment> pMutableList) {
    List<Object[]> params = getInsertParamArray(pMutableList);
    mJdbcTemplate.batchUpdate(INSERT_ALL, params);
    return params.stream().map(param -> (Long) param[0]).collect(Collectors.toList());
  }

  @Override
  public int delete(MutableStudentPayment pMutable) {
    String query = DELETE_ALL + "WHERE ID = ?";
    return mJdbcTemplate.update(query);
  }

  private List<Object[]> getUpdateParamArray(List<MutableStudentPayment> pStudentPayments) {
    List<Object[]> params = new ArrayList<>();
    for(StudentPayment studentPayment : pStudentPayments) {
      params.add(new Object[] {studentPayment.getStatus().getValue(), studentPayment.getId()});
    }
    return params;
  }

  private List<Object[]> getInsertParamArray(List<MutableStudentPayment> pStudentPayments) {
    List<Object[]> params = new ArrayList<>();
    for(StudentPayment studentPayment : pStudentPayments) {
      params.add(new Object[] {mIdGenerator.getNumericId(), mIdGenerator.getAlphaNumericId(),
          studentPayment.getStudentId(), studentPayment.getSemesterId(), studentPayment.getAmount(),
          studentPayment.getStatus().getValue()});
    }
    return params;
  }

  class StudentPaymentRowMapper implements RowMapper<StudentPayment> {
    @Override
    public StudentPayment mapRow(ResultSet rs, int rowNum) throws SQLException {
      MutableStudentPayment studentPayment = new PersistentStudentPayment();
      studentPayment.setId(rs.getLong("ID"));
      studentPayment.setTransactionId(rs.getString("TRANSACTION_ID"));
      studentPayment.setSemesterId(rs.getInt("SEMESTER_ID"));
      studentPayment.setStudentId(rs.getString("STUDENT_ID"));
      studentPayment.setAmount(rs.getDouble("AMOUNT"));
      studentPayment.setStatus(StudentPayment.Status.get(rs.getInt("STATUS")));
      studentPayment.setAppliedOn(rs.getTimestamp("APPLIED_ON"));
      studentPayment.setVerifiedOn(rs.getTimestamp("VERIFIED_ON"));
      studentPayment.setLastModified(rs.getString("LAST_MODIFIED"));
      AtomicReference<StudentPayment> atomicReference = new AtomicReference<>(studentPayment);
      return atomicReference.get();
    }
  }
}