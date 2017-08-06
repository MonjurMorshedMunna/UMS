package org.ums.persistent.model;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.mutable.MutableBearerAccessToken;
import org.ums.manager.BearerAccessTokenManager;

public class PersistentBearerAccessToken implements MutableBearerAccessToken {
  private static BearerAccessTokenManager sBearerAccessTokenManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sBearerAccessTokenManager = applicationContext.getBean("bearerAccessTokenManager", BearerAccessTokenManager.class);
  }

  private String mId;
  private String mUserId;
  private String mRefreshToken;
  private Date mLastAccessTime;
  private String mLastModified;

  public PersistentBearerAccessToken() {}

  public PersistentBearerAccessToken(final PersistentBearerAccessToken pPersistentBearerAccessToken) {
    setId(pPersistentBearerAccessToken.getId());
    setUserId(pPersistentBearerAccessToken.getUserId());
    setLastAccessedTime(pPersistentBearerAccessToken.getLastAccessTime());
  }

  @Override
  public void setUserId(String pUserId) {
    mUserId = pUserId;
  }

  @Override
  public void setLastAccessedTime(Date pDate) {
    mLastAccessTime = pDate;
  }

  @Override
  public String getUserId() {
    return mUserId;
  }

  @Override
  public void setRefreshToken(String pRefreshToken) {
    mRefreshToken = pRefreshToken;
  }

  @Override
  public String getRefreshToken() {
    return mRefreshToken;
  }

  @Override
  public Date getLastAccessTime() {
    return mLastAccessTime;
  }

  @Override
  public MutableBearerAccessToken edit() {
    return new PersistentBearerAccessToken(this);
  }

  @Override
  public String getId() {
    return mId;
  }

  @Override
  public void setId(String pId) {
    mId = pId;
  }

  @Override
  public String create() {
    return sBearerAccessTokenManager.create(this);
  }

  @Override
  public void update() {
    sBearerAccessTokenManager.update(this);
  }

  @Override
  public void delete() {
    sBearerAccessTokenManager.delete(this);
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }
}
