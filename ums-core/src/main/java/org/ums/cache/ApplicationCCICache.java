package org.ums.cache;

import javafx.application.Application;
import org.springframework.context.ApplicationContextInitializer;
import org.ums.domain.model.immutable.ApplicationCCI;
import org.ums.domain.model.mutable.MutableApplicationCCI;
import org.ums.manager.ApplicationCCIManager;
import org.ums.manager.CacheManager;
import org.ums.util.CacheUtil;

import java.util.List;

/**
 * Created by My Pc on 7/11/2016.
 */
public class ApplicationCCICache extends ContentCache<ApplicationCCI,MutableApplicationCCI,Integer,ApplicationCCIManager> implements ApplicationCCIManager {

  CacheManager<ApplicationCCI,Integer> mCacheManager;

  public ApplicationCCICache(CacheManager<ApplicationCCI,Integer> pCacheManager){
    mCacheManager = pCacheManager;
  }


  @Override
  public List<ApplicationCCI> getByStudentIdAndSemesterAndType(String pStudentId, int pSemesterId, int pExamType) {
    return getManager().getByStudentIdAndSemesterAndType(pStudentId,pSemesterId,pExamType);
  }

  @Override
  public List<ApplicationCCI> getBySemesterAndType(int pSemesterId, int pExamType) {
    return getManager().getBySemesterAndType(pSemesterId,pExamType);
  }

  @Override
  public List<ApplicationCCI> getByProgramAndSemesterAndType(int pProgramId, int pSemesterId, int pExamType) {
    return getManager().getByProgramAndSemesterAndType(pProgramId,pSemesterId,pExamType);
  }

  @Override
  public List<ApplicationCCI> getByStudentIdAndSemester(String pStudentId, int pSemesterId) {
    return getManager().getByStudentIdAndSemester(pStudentId,pSemesterId);
  }
  @Override
  public List<ApplicationCCI> getBySemesterAndExamDate(Integer pSemesterId, String pExamDate) throws Exception{
    return getManager().getBySemesterAndExamDate(pSemesterId,pExamDate);
  }
  @Override
  public int deleteByStudentId(String pStudentId) {
    return getManager().deleteByStudentId(pStudentId);
  }

  @Override
  protected CacheManager<ApplicationCCI, Integer> getCacheManager() {
    return mCacheManager;
  }

  @Override
  protected String getCacheKey(Integer pId) {
    return CacheUtil.getCacheKey(ApplicationCCI.class,pId);
  }

  @Override
  public List<ApplicationCCI> getByStudentIdAndSemesterForSeatPlanView(String pStudentId, Integer pSemesterId) {
    return getManager().getByStudentIdAndSemesterForSeatPlanView(pStudentId,pSemesterId);
  }
}
