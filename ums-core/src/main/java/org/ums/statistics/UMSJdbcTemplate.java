package org.ums.statistics;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class UMSJdbcTemplate extends JdbcTemplate {
  @Autowired
  @Qualifier("loggerFactory")
  LoggerFactory mQueryLoggerFactory;

  @Override
  public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
    // Do some logging
    long currentTime = System.currentTimeMillis();
    List<T> returned = super.query(sql, args, rowMapper);
    long afterTime = System.currentTimeMillis();
    String userName = SecurityUtils.getSubject().getPrincipal() == null ? "Login Request" : SecurityUtils.getSubject().getPrincipal().toString();
    mQueryLoggerFactory.getQueryLogger().log(sql, args, userName, (afterTime - currentTime));
    return returned;
  }

  @Override
  public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
    long currentTime = System.currentTimeMillis();
    List<T> returned = super.query(sql, rowMapper);
    long afterTime = System.currentTimeMillis();
    String userName = SecurityUtils.getSubject().getPrincipal() == null ? "Login Request" : SecurityUtils.getSubject().getPrincipal().toString();
    mQueryLoggerFactory.getQueryLogger().log(sql, userName, (afterTime - currentTime));
    return returned;
  }

  @Override
  public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
    long currentTime = System.currentTimeMillis();
    T returned = super.queryForObject(sql, args, rowMapper);
    long afterTime = System.currentTimeMillis();
    String userName = SecurityUtils.getSubject().getPrincipal() == null ? "Login Request" : SecurityUtils.getSubject().getPrincipal().toString();
    mQueryLoggerFactory.getQueryLogger().log(sql, args, userName, (afterTime - currentTime));
    return returned;
  }

  @Override
  public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
    long currentTime = System.currentTimeMillis();
    T returned = super.queryForObject(sql, rowMapper);
    long afterTime = System.currentTimeMillis();
    String userName = SecurityUtils.getSubject().getPrincipal() == null ? "Login Request" : SecurityUtils.getSubject().getPrincipal().toString();
    mQueryLoggerFactory.getQueryLogger().log(sql, userName, (afterTime - currentTime));
    return returned;
  }

  @Override
  public int update(String sql, Object... args) throws DataAccessException {
    long currentTime = System.currentTimeMillis();
    int updated = super.update(sql, args);
    long afterTime = System.currentTimeMillis();
    String userName = SecurityUtils.getSubject().getPrincipal() == null ? "Login Request" : SecurityUtils.getSubject().getPrincipal().toString();
    mQueryLoggerFactory.getQueryLogger().log(sql, args, userName, (afterTime - currentTime));
    return updated;
  }

  @Override
  public int update(String sql) throws DataAccessException {
    long currentTime = System.currentTimeMillis();
    int updated = super.update(sql);
    long afterTime = System.currentTimeMillis();
    String userName = SecurityUtils.getSubject().getPrincipal() == null ? "Login Request" : SecurityUtils.getSubject().getPrincipal().toString();
    mQueryLoggerFactory.getQueryLogger().log(sql, userName, (afterTime - currentTime));
    return updated;
  }

  @Override
  public int[] batchUpdate(String sql, List<Object[]> batchArgs) throws DataAccessException {
    long currentTime = System.currentTimeMillis();
    int[] updated = super.batchUpdate(sql, batchArgs);
    long afterTime = System.currentTimeMillis();
    String userName = SecurityUtils.getSubject().getPrincipal() == null ? "Login Request" : SecurityUtils.getSubject().getPrincipal().toString();
    mQueryLoggerFactory.getQueryLogger().log(sql, batchArgs, userName, (afterTime - currentTime));
    return updated;
  }
}
