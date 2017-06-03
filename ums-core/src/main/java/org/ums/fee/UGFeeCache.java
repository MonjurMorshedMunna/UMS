package org.ums.fee;

import java.util.List;

import org.ums.cache.ContentCache;
import org.ums.manager.CacheManager;
import org.ums.util.CacheUtil;

public class UGFeeCache extends ContentCache<UGFee, MutableUGFee, Long, UGFeeManager> implements UGFeeManager {
  private CacheManager<UGFee, Long> mCacheManager;

  public UGFeeCache(final CacheManager<UGFee, Long> pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager<UGFee, Long> getCacheManager() {
    return mCacheManager;
  }

  @Override
  protected String getCacheKey(Long pId) {
    return CacheUtil.getCacheKey(FeeCategory.class, pId);
  }

  @Override
  public List<UGFee> getFee(Integer pFacultyId, Integer pSemesterId) {
    return getManager().getFee(pFacultyId, pSemesterId);
  }

  @Override
  public List<UGFee> getFee(Integer pFacultyId, Integer pSemesterId, List<FeeCategory> pCategories) {
    return getManager().getFee(pFacultyId, pSemesterId, pCategories);
  }

  @Override
  public List<UGFee> getLatestFee(Integer pFacultyId, Integer pSemesterId) {
    return getManager().getLatestFee(pFacultyId, pSemesterId);
  }

  @Override
  public List<Integer> getDistinctSemesterIds(Integer pFacultyId) {
    return getManager().getDistinctSemesterIds(pFacultyId);
  }
}
