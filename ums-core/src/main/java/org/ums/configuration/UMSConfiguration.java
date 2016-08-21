package org.ums.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UMSConfiguration {
  @Value("${course.material.owner.only.modification}")
  private boolean mOwnerOnlyModification;

  @Value("${binary.storageRoot}")
  private String mStorageRoot;

  @Value("${enable.objectDb}")
  private boolean mEnableObjectDb;

  public boolean isOwnerOnlyModification() {
    return mOwnerOnlyModification;
  }

  public String getStorageRoot() {
    return mStorageRoot;
  }

  public boolean isEnableObjectDb() {
    return mEnableObjectDb;
  }

  public void setEnableObjectDb(boolean pEnableObjectDb) {
    mEnableObjectDb = pEnableObjectDb;
  }
}
