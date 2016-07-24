package org.ums.persistent.model;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.dto.ExamRoutineDto;
import org.ums.domain.model.mutable.MutableExamRoutine;
import org.ums.manager.ExamRoutineManager;

import java.util.List;

public class PersistentExamRoutine implements MutableExamRoutine {

  private static ExamRoutineManager sExamRoutineManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sExamRoutineManager = applicationContext.getBean("examRoutineManager", ExamRoutineManager.class);
  }


  private List<ExamRoutineDto> mExamRoutine;
  private String mInsertType;
  private Integer mSemesterId;
  private String mSemesterName;
  private Integer mExamTypeId;
  private String mExamTypeName;
  private Integer mTotalStudent;
  private String mExamDate;
  private String mExamDateOriginal;


  public PersistentExamRoutine() {
  }

  @Override
  public String getExamDate() {
    return mExamDate;
  }

  public void setExamDate(String pExamDate) {
    mExamDate = pExamDate;
  }

  public PersistentExamRoutine(final MutableExamRoutine pOriginal) throws Exception {
    mExamRoutine = pOriginal.getRoutine();
  }

  public void setExamDateOriginal(String pExamDateOriginal){
    mExamDateOriginal = pExamDateOriginal;
  }
  @Override
  public String getExamDateOriginal() {
    return mExamDateOriginal;
  }

  @Override
  public void setRoutine(List<ExamRoutineDto> pRoutineList) {
    mExamRoutine = pRoutineList;
  }

  @Override
  public List<ExamRoutineDto> getRoutine() {
    return mExamRoutine;
  }

  public void save() throws Exception {

    sExamRoutineManager.create(this);

  }

  @Override
  public void delete() throws Exception {
    sExamRoutineManager.delete(this);

  }


  @Override
  public void commit(boolean update) throws Exception {
  }

  @Override
  public MutableExamRoutine edit() throws Exception {
    return null;
  }


  @Override
  public void setTotalStudent(Integer pTotalStudent) {
    mTotalStudent = pTotalStudent;
  }

  @Override
  public Integer getTotalStudent() {
    return mTotalStudent;
  }

  @Override
  public String getInsertType() {
    return mInsertType;
  }

  @Override
  public void setInsertType(String mInsertType) {
    this.mInsertType = mInsertType;
  }

  @Override

  public Integer getSemesterId() {
    return mSemesterId;
  }

  @Override
  public void setSemesterId(Integer mSemesterId) {
    this.mSemesterId = mSemesterId;
  }

  @Override
  public String getSemesterName() {
    return mSemesterName;
  }

  @Override
  public void setSemesterName(String mSemesterName) {
    this.mSemesterName = mSemesterName;
  }

  @Override
  public Integer getExamTypeId() {
    return mExamTypeId;
  }



  @Override
  public void setExamTypeId(Integer mExamTypeId) {
    this.mExamTypeId = mExamTypeId;
  }

  @Override
  public String getExamTypeName() {
    return mExamTypeName;
  }

  @Override
  public void setExamTypeName(String mExamTypeName) {
    this.mExamTypeName = mExamTypeName;
  }
}
