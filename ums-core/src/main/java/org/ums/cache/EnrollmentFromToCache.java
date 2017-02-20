package org.ums.cache;

import org.ums.domain.model.immutable.EnrollmentFromTo;
import org.ums.domain.model.mutable.MutableEnrollmentFromTo;
import org.ums.manager.CacheManager;
import org.ums.manager.EnrollmentFromToManager;
import org.ums.util.CacheUtil;

import java.util.List;

public class EnrollmentFromToCache extends
    ContentCache<EnrollmentFromTo, MutableEnrollmentFromTo, Long, EnrollmentFromToManager>
    implements EnrollmentFromToManager {
  private CacheManager<EnrollmentFromTo, Long> mCacheManager;

  public EnrollmentFromToCache(final CacheManager<EnrollmentFromTo, Long> pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager<EnrollmentFromTo, Long> getCacheManager() {
    return mCacheManager;
  }

  @Override
  public List<EnrollmentFromTo> getEnrollmentFromTo(Integer pProgramId) {
    return getManager().getEnrollmentFromTo(pProgramId);
  }
}
