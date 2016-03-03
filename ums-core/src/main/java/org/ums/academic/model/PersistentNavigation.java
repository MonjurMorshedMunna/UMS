package org.ums.academic.model;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.mutable.MutableNavigation;
import org.ums.domain.model.readOnly.Navigation;
import org.ums.manager.NavigationManager;


public class PersistentNavigation implements MutableNavigation {
  private static NavigationManager sNavigationManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sNavigationManager = applicationContext.getBean("navigationManager", NavigationManager.class);
  }

  private Integer mId;
  private String mTitle;
  private String mPermission;
  private Navigation mNavigation;
  private int mViewOrder;
  private String mLocation;
  private String mIconImgClass;
  private String mIconColorClass;
  private boolean mActive;
  private String mLastModified;
  private Integer mParentId;

  public PersistentNavigation() {
  }

  public PersistentNavigation(final Navigation pNavigation) {
    setId(pNavigation.getId());
    setTitle(pNavigation.getTitle());
    setPermission(pNavigation.getPermission());
    setParentId(pNavigation.getParentId());
    setViewOrder(pNavigation.getViewOrder());
    setLocation(pNavigation.getLocation());
    setIconImgClass(pNavigation.getIconImgClass());
    setIconColorClass(pNavigation.getIconColorClass());
    setActive(pNavigation.isActive());
    setLastModified(pNavigation.getLastModified());
  }

  @Override
  public void setTitle(String pTitle) {
    mTitle = pTitle;
  }

  @Override
  public void setPermission(String pPermission) {
    mPermission = pPermission;
  }

  @Override
  public void setParent(Navigation pNavigation) {
    mNavigation = pNavigation;
  }

  @Override
  public void setViewOrder(Integer pViewOrder) {
    mViewOrder = pViewOrder;
  }

  @Override
  public void setLocation(String pLocation) {
    mLocation = pLocation;
  }

  @Override
  public void setIconImgClass(String iIconImgClass) {
    mIconImgClass = iIconImgClass;
  }

  @Override
  public void setIconColorClass(String iIconColorClass) {
    mIconColorClass = iIconColorClass;
  }

  @Override
  public void setActive(boolean pActive) {
    mActive = pActive;
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
  public void setParentId(Integer pParentId) {
    mParentId = pParentId;
  }

  @Override
  public Integer getParentId() {
    return mParentId;
  }

  @Override
  public Integer getId() {
    return mId;
  }

  @Override
  public String getTitle() {
    return mTitle;
  }

  @Override
  public String getPermission() {
    return mPermission;
  }

  @Override
  public Navigation getParent() throws Exception {
    return mNavigation == null ? mParentId > 0 ? sNavigationManager.get(mParentId) : null : sNavigationManager.validate(mNavigation);
  }

  @Override
  public Integer getViewOrder() {
    return mViewOrder;
  }

  @Override
  public String getLocation() {
    return mLocation;
  }

  @Override
  public String getIconImgClass() {
    return mIconImgClass;
  }

  @Override
  public String getIconColorClass() {
    return mIconColorClass;
  }

  @Override
  public boolean isActive() {
    return mActive;
  }

  @Override
  public void commit(boolean update) throws Exception {
    if (update) {
      sNavigationManager.update(this);
    } else {
      sNavigationManager.create(this);
    }
  }

  @Override
  public void delete() throws Exception {
    sNavigationManager.delete(this);
  }

  @Override
  public MutableNavigation edit() throws Exception {
    return new PersistentNavigation(this);
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }
}
