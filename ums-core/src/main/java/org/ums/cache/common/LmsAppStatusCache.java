package org.ums.cache.common;

import org.ums.cache.ContentCache;
import org.ums.domain.model.immutable.Role;
import org.ums.domain.model.immutable.User;
import org.ums.domain.model.immutable.common.LmsAppStatus;
import org.ums.domain.model.mutable.common.MutableLmsAppStatus;
import org.ums.enums.common.LeaveApprovalStatus;
import org.ums.manager.CacheManager;
import org.ums.manager.common.LmsAppStatusManager;

import java.util.List;

/**
 * Created by Monjur-E-Morshed on 06-May-17.
 */
public class LmsAppStatusCache extends ContentCache<LmsAppStatus, MutableLmsAppStatus, Long, LmsAppStatusManager>
    implements LmsAppStatusManager {

  private CacheManager<LmsAppStatus, Long> mCacheManager;

  public LmsAppStatusCache(CacheManager<LmsAppStatus, Long> pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager<LmsAppStatus, Long> getCacheManager() {
    return mCacheManager;
  }

  @Override
  public List<LmsAppStatus> getAppStatus(Long pApplicationId) {
    return getManager().getAppStatus(pApplicationId);
  }

  @Override
  public List<LmsAppStatus> getLmsAppStatusList(String pEmployeeId) {
    return getManager().getLmsAppStatusList(pEmployeeId);
  }

  @Override
  public List<LmsAppStatus> getLmsAppStatusList(LeaveApprovalStatus pLeaveApplicationStatus, Role pRole, User pUser,
      int pageNumber, int pageSize) {
    return getManager().getLmsAppStatusList(pLeaveApplicationStatus, pRole, pUser, pageNumber, pageSize);
  }
}
