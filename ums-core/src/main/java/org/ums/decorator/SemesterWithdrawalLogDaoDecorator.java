package org.ums.decorator;

import org.ums.cache.ContentDaoDecorator;
import org.ums.domain.model.immutable.SemesterWithdrawalLog;
import org.ums.domain.model.mutable.MutableSemesterWithdrawalLog;
import org.ums.manager.SemesterWithdrawalLogManager;


public class SemesterWithdrawalLogDaoDecorator extends ContentDaoDecorator<SemesterWithdrawalLog,MutableSemesterWithdrawalLog,Integer,SemesterWithdrawalLogManager> implements SemesterWithdrawalLogManager {

  @Override
  public SemesterWithdrawalLog getBySemesterWithdrawalId(int pSemesterWithdrawalId) {
    return getManager().getBySemesterWithdrawalId(pSemesterWithdrawalId);
  }
}
