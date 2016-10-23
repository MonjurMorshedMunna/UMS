package org.ums.manager;

import org.ums.domain.model.immutable.SpStudent;
import org.ums.domain.model.mutable.MutableSpStudent;

import java.util.List;

/**
 * Created by My Pc on 4/27/2016.
 */
public interface SpStudentManager extends ContentManager<SpStudent, MutableSpStudent, String> {
  public List<SpStudent> getStudentByProgramYearSemesterStatus(int pProgramId, int pYear,
      int pSemester, int pStatus);

  public List<SpStudent> getStudentByCourseIdAndSemesterIdForSeatPlanForCCI(String pCourseId,
      Integer pSemesterId);

  public List<SpStudent> getStudentBySemesterIdAndExamDateForCCI(Integer pSemesterId,
      String pExamDate);
}
