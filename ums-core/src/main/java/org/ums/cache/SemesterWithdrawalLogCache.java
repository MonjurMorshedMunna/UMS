package org.ums.cache;

import org.ums.domain.model.immutable.SemesterWithdrawalLog;
import org.ums.domain.model.mutable.MutableSemesterWithdrawalLog;
import org.ums.manager.CacheManager;
import org.ums.manager.SemesterWithdrawalLogManager;
import org.ums.util.CacheUtil;

public class SemesterWithdrawalLogCache
    extends
    ContentCache<SemesterWithdrawalLog, MutableSemesterWithdrawalLog, Integer, SemesterWithdrawalLogManager>
    implements SemesterWithdrawalLogManager {

  CacheManager<SemesterWithdrawalLog, Integer> mCacheManager;

  public SemesterWithdrawalLogCache(CacheManager<SemesterWithdrawalLog, Integer> pCachemanager) {
    mCacheManager = pCachemanager;
  }

  @Override
  public SemesterWithdrawalLog getBySemesterWithdrawalId(int pSemesterWithdrawalId) {
    return getManager().getBySemesterWithdrawalId(pSemesterWithdrawalId);
  }

  @Override
  protected CacheManager<SemesterWithdrawalLog, Integer> getCacheManager() {
    return mCacheManager;
  }
}
