package org.ums.indexer;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.indexer.manager.IndexManager;
import org.ums.indexer.model.MutableIndex;

public class PersistentIndex implements MutableIndex {

  private static IndexManager sIndexManager;
  private Long mId;
  private String mEntityId;
  private String mEntityType;
  private Boolean mIsDeleted;
  private Date mModified;
  private String mLastModified;

  @Override
  public Long getId() {
    return mId;
  }

  @Override
  public void setId(Long pId) {
    this.mId = pId;
  }

  @Override
  public String getEntityId() {
    return mEntityId;
  }

  @Override
  public void setEntityId(String pEntityId) {
    this.mEntityId = pEntityId;
  }

  @Override
  public String getEntityType() {
    return mEntityType;
  }

  @Override
  public void setEntityType(String pEntityType) {
    this.mEntityType = pEntityType;
  }

  @Override
  public Boolean getIsDeleted() {
    return mIsDeleted;
  }

  @Override
  public void setIsDeleted(Boolean pIsDeleted) {
    this.mIsDeleted = pIsDeleted;
  }

  @Override
  public Date getModified() {
    return mModified;
  }

  @Override
  public void setModified(Date pModified) {
    this.mModified = pModified;
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public void setLastModified(String pLastModified) {
    this.mLastModified = pLastModified;
  }

  @Override
  public void commit(boolean update) {
    if(update) {
      sIndexManager.update(this);
    }
    else {
      sIndexManager.create(this);
    }
  }

  @Override
  public MutableIndex edit() {
    return new PersistentIndex(this);
  }

  @Override
  public void delete() {
    sIndexManager.delete(this);
  }

  public PersistentIndex() {}

  public PersistentIndex(MutableIndex pIndexer) {
    setId(pIndexer.getId());
    setEntityId(pIndexer.getEntityId());
    setEntityType(pIndexer.getEntityType());
    setIsDeleted(pIndexer.getIsDeleted());
    setModified(pIndexer.getModified());
    setLastModified(pIndexer.getLastModified());
  }

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sIndexManager = applicationContext.getBean("indexerManager", IndexManager.class);
  }
}
