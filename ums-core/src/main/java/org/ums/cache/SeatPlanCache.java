package org.ums.cache;

import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.immutable.SeatPlan;
import org.ums.domain.model.mutable.MutableSeatPlan;
import org.ums.manager.CacheManager;
import org.ums.manager.SeatPlanManager;
import org.ums.util.CacheUtil;

import java.util.List;

/**
 * Created by My Pc on 5/8/2016.
 */
public class SeatPlanCache extends ContentCache<SeatPlan, MutableSeatPlan, Integer, SeatPlanManager> implements SeatPlanManager {

  private CacheManager mCacheManager;

  public SeatPlanCache(final CacheManager pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager getCacheManager() {
    return mCacheManager;
  }

  @Override
  protected String getCacheKey(Integer pId) {
    return CacheUtil.getCacheKey(SeatPlan.class, pId);
  }

  @Override
  public List<SeatPlan> getBySemesterAndGroupAndExamType(int pSemesterId, int pGropNo, int pExamType) {
    return getManager().getBySemesterAndGroupAndExamType(pSemesterId, pGropNo, pExamType);
  }

  @Override
  public List<SeatPlan> getByRoomSemesterGroupExamType(int pRoomId, int pSemesterId, int pGroupNo, int pExamType) {
    return getManager().getByRoomSemesterGroupExamType(pRoomId, pSemesterId, pGroupNo, pExamType);
  }

  @Override
  public int deleteBySemesterGroupExamType(int pSemesterId, int pGroupNo, int pExamType) {
    return getManager().deleteBySemesterGroupExamType(pSemesterId, pGroupNo, pExamType);
  }

  @Override
  public List<SeatPlan> getBySemesterGroupTypeRoomRowAndCol(int pSemesterId, int pGroupNo, int pType, int pRoomId, int pRow, int pCol) {
    return getManager().getBySemesterGroupTypeRoomRowAndCol(pSemesterId, pGroupNo, pType, pRoomId, pRow, pCol);
  }

  @Override
  public int checkIfExistsBySemesterGroupTypeRoomRowAndCol(int pSemesterId, int pGroupNo, int pType, int pRoomId, int pRow, int pCol) {
    return getManager().checkIfExistsBySemesterGroupTypeRoomRowAndCol(pSemesterId, pGroupNo, pType, pRoomId, pRow, pCol);
  }

  @Override
  public int checkIfExistsByRoomSemesterGroupExamType(int pRoomId, int pSemesterId, int pGroupNo, int pExamType) throws Exception {
    /*long pCacheKey = generateCacheKeyForRoomSemesterGroupExamType(pRoomId, pSemesterId, pGroupNo, pExamType);
    String cacheKey = CacheUtil.getCacheKey(SeatPlan.class, pCacheKey);
    Object pReadonly = getCacheManager().get(cacheKey);
    if (pReadonly == null) {
      pReadonly = getManager().checkIfExistsByRoomSemesterGroupExamType(pRoomId, pSemesterId, pGroupNo, pExamType);
      TemporaryContainer temporaryContainer = new TemporaryContainer(pReadonly);
      getCacheManager().put(cacheKey, temporaryContainer);
    }
    return (Integer) ((TemporaryContainer)pReadonly).getContain();*/
    return getManager().checkIfExistsByRoomSemesterGroupExamType(pRoomId,pSemesterId,pGroupNo,pExamType);
  }

  protected long generateCacheKeyForRoomSemesterGroupExamType(int pRoomId, int pSemesterId, int pGroupNo, int pExamType) {
    StringBuilder builder = new StringBuilder();
    builder.append(pRoomId).append(pSemesterId).append(pGroupNo).append(pExamType);
    return Long.parseLong(builder.toString());
  }

  class TemporaryContainer implements LastModifier {
    private Object contain;

    public TemporaryContainer(Object pContain) {
      contain = pContain;
    }

    public Object getContain() {
      return contain;
    }

    @Override
    public String getLastModified() {
      return "";
    }
  }
}
