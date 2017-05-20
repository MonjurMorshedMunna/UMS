package org.ums.cache.common;

import org.ums.cache.ContentCache;
import org.ums.domain.model.immutable.common.LmsApplication;
import org.ums.domain.model.mutable.common.MutableLmsApplication;
import org.ums.manager.CacheManager;
import org.ums.manager.common.LmsApplicationManager;

import java.util.List;

/**
 * Created by Monjur-E-Morshed on 06-May-17.
 */
public class LmsApplicationCache extends
    ContentCache<LmsApplication, MutableLmsApplication, Long, LmsApplicationManager> implements LmsApplicationManager {

  CacheManager<LmsApplication, Long> mCacheManager;

  public LmsApplicationCache(CacheManager<LmsApplication, Long> pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager<LmsApplication, Long> getCacheManager() {
    return mCacheManager;
  }

  @Override
  public List<LmsApplication> getLmsApplication(String pEmployeeId, int pYear) {
    return getManager().getLmsApplication(pEmployeeId, pYear);
  }

  @Override
  public List<LmsApplication> getPendingLmsApplication(String pEmployeeId) {
    return getManager().getPendingLmsApplication(pEmployeeId);
  }

}
