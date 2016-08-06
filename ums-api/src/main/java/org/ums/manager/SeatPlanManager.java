package org.ums.manager;

import org.ums.domain.model.immutable.SeatPlan;
import org.ums.domain.model.mutable.MutableSeatPlan;

import java.util.List;

/**
 * Created by My Pc on 5/8/2016.
 */
public interface SeatPlanManager extends ContentManager<SeatPlan,MutableSeatPlan,Integer> {
  int createSeatPlanForCCI(final List<MutableSeatPlan> pSeatPlans) throws Exception;
  List<SeatPlan> getBySemesterAndGroupAndExamType(final int pSemesterId, final int pGropNo,final int pExamType);
  List<SeatPlan> getBySemesterAndGroupAndExamTypeAndExamDate(final int pSemesterId,final int pGropuNo,final int pExamType,String pExamDate);
  List<SeatPlan> getByRoomSemesterGroupExamType(final int pRoomId,final int pSemesterId,final int pGroupNo,final int pExamType);
  int deleteBySemesterGroupExamType(final int pSemesterId,final int pGroupNo,final int pExamType);
  int deleteBySemesterGroupExamTypeAndExamDate(final int pSemesterId,final int pGroupNo,final int pExamType,final String pExamDate);
  List<SeatPlan> getBySemesterGroupTypeRoomRowAndCol(final int pSemesterId,final int pGroupNo,final int pType,int pRoomId,int pRow,int pCol);
  int checkIfExistsBySemesterGroupTypeRoomRowAndCol(final int pSemesterId,final int pGroupNo,final int pType,int pRoomId,int pRow,int pCol);
  int checkIfExistsBySemesterGroupTypeExamDateRoomRowAndCol(final int pSemesterId,final int pGroupNo,final int pType,final String pExamDate, final int pRoomId, final int pRow,final int pCol);
  int checkIfExistsByRoomSemesterGroupExamType(final int pRoomId,final int pSemesterId,final int pGroupNo,final int pExamType) throws Exception;
  List<SeatPlan> getForStudent(String pStudentId,Integer pSemesterId);
}
