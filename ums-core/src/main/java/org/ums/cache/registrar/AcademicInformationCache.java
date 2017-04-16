package org.ums.cache.registrar;

import org.ums.cache.ContentCache;
import org.ums.domain.model.immutable.registrar.AcademicInformation;
import org.ums.domain.model.mutable.registrar.MutableAcademicInformation;
import org.ums.manager.CacheManager;
import org.ums.manager.registrar.AcademicInformationManager;

import java.util.List;

public class AcademicInformationCache extends
    ContentCache<AcademicInformation, MutableAcademicInformation, Integer, AcademicInformationManager> implements
    AcademicInformationManager {

  private CacheManager<AcademicInformation, Integer> mCacheManager;

  public AcademicInformationCache(CacheManager<AcademicInformation, Integer> pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager<AcademicInformation, Integer> getCacheManager() {
    return mCacheManager;
  }

  @Override
  public int saveAcademicInformation(List<MutableAcademicInformation> pMutableAcademicInformation) {
    return getManager().saveAcademicInformation(pMutableAcademicInformation);
  }

  @Override
  public List<AcademicInformation> getEmployeeAcademicInformation(int pEmployeeId) {
    return getManager().getEmployeeAcademicInformation(pEmployeeId);
  }
}
