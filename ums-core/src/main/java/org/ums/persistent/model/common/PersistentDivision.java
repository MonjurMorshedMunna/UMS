package org.ums.persistent.model.common;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.mutable.common.MutableDivision;
import org.ums.manager.common.DivisionManager;

public class PersistentDivision implements MutableDivision {

  private static DivisionManager sDivisionManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sDivisionManager = applicationContext.getBean("divisionManager", DivisionManager.class);
  }

  private Integer mId;
  private String mDivisionName;
  private String mLastModified;

  public PersistentDivision() {}

  public PersistentDivision(PersistentDivision pPersistentDivision) {
    mId = pPersistentDivision.getId();
    mDivisionName = pPersistentDivision.getDivisionName();
    mLastModified = pPersistentDivision.getLastModified();
  }

  @Override
  public MutableDivision edit() {
    return new PersistentDivision(this);
  }

  @Override
  public Integer create() {
    return sDivisionManager.create(this);
  }

  @Override
  public void update() {
    sDivisionManager.update(this);
  }

  @Override
  public void delete() {
    sDivisionManager.delete(this);
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public Integer getId() {
    return mId;
  }

  @Override
  public void setId(Integer pId) {
    mId = pId;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }

  @Override
  public void setDivisionName(String pDivisionName) {
    mDivisionName = pDivisionName;
  }

  @Override
  public String getDivisionName() {
    return mDivisionName;
  }
}
