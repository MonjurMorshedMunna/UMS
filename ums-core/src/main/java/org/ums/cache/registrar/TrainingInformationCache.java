package org.ums.cache.registrar;

import org.ums.cache.ContentCache;
import org.ums.domain.model.immutable.registrar.TrainingInformation;
import org.ums.domain.model.mutable.registrar.MutableTrainingInformation;
import org.ums.manager.CacheManager;
import org.ums.manager.registrar.TrainingInformationManager;

import java.util.List;

public class TrainingInformationCache extends
    ContentCache<TrainingInformation, MutableTrainingInformation, Integer, TrainingInformationManager> implements
    TrainingInformationManager {

  private CacheManager<TrainingInformation, Integer> mCacheManager;

  public TrainingInformationCache(CacheManager<TrainingInformation, Integer> pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager<TrainingInformation, Integer> getCacheManager() {
    return mCacheManager;
  }

  @Override
  public int saveTrainingInformation(List<MutableTrainingInformation> pMutableTrainingInformation) {
    return getManager().saveTrainingInformation(pMutableTrainingInformation);
  }

  @Override
  public List<TrainingInformation> getEmployeeTrainingInformation(int pEmployeeId) {
    return getManager().getEmployeeTrainingInformation(pEmployeeId);
  }
}
