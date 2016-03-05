package org.ums.academic.model;


import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.mutable.MutableEnrollmentFromTo;
import org.ums.domain.model.mutable.MutableProgram;
import org.ums.domain.model.readOnly.Program;
import org.ums.manager.ContentManager;
import org.ums.manager.EnrollmentFromToManager;

public class PersistentEnrollmentFromTo implements MutableEnrollmentFromTo {
  private static EnrollmentFromToManager sEnrollmentFromToManager;
  private static ContentManager<Program, MutableProgram, Integer> sProgramManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sProgramManager = (ContentManager<Program, MutableProgram, Integer>) applicationContext.getBean("programManager");
    sEnrollmentFromToManager = applicationContext.getBean("enrollmentFromToManager", EnrollmentFromToManager.class);
  }

  private Integer mId;
  private Integer mProgramId;
  private Program mProgram;
  private Integer mFromYear;
  private Integer mFromSemester;
  private Integer mToYear;
  private Integer mToSemester;
  private String mLastModified;

  public PersistentEnrollmentFromTo() {
  }

  public PersistentEnrollmentFromTo(final PersistentEnrollmentFromTo pEnrollmentFromTo) {
    setId(pEnrollmentFromTo.getId());
    setProgramId(pEnrollmentFromTo.getProgramId());
    setFromYear(pEnrollmentFromTo.getFromYear());
    setFromSemester(pEnrollmentFromTo.getFromSemester());
    setToYear(pEnrollmentFromTo.getToYear());
    setToSemester(pEnrollmentFromTo.getToSemester());
    setLastModified(pEnrollmentFromTo.getLastModified());
  }

  @Override
  public void setProgramId(Integer pProgramId) {
    mProgramId = pProgramId;
  }

  @Override
  public void setProgram(Program pProgram) {
    mProgram = pProgram;
  }

  @Override
  public void setFromYear(Integer pFromYear) {
    mFromYear = pFromYear;
  }

  @Override
  public void setFromSemester(Integer pFromSemester) {
    mFromSemester = pFromSemester;
  }

  @Override
  public void setToYear(Integer pToYear) {
    mToYear = pToYear;
  }

  @Override
  public void setToSemester(Integer pToSemester) {
    mToSemester = pToSemester;
  }

  @Override
  public Integer getProgramId() {
    return mProgramId;
  }

  @Override
  public Program getProgram() throws Exception {
    return mProgram == null ? sProgramManager.get(mProgramId) : sProgramManager.validate(mProgram);
  }

  @Override
  public Integer getFromYear() {
    return mFromYear;
  }

  @Override
  public Integer getFromSemester() {
    return mFromSemester;
  }

  @Override
  public Integer getToYear() {
    return mToYear;
  }

  @Override
  public Integer getToSemester() {
    return mToSemester;
  }

  @Override
  public MutableEnrollmentFromTo edit() throws Exception {
    return new PersistentEnrollmentFromTo(this);
  }

  @Override
  public Integer getId() {
    return mId;
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public void setId(Integer pId) {
    mId = pId;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }

  @Override
  public void commit(boolean update) throws Exception {
    if (update) {
      sEnrollmentFromToManager.update(this);
    } else {
      sEnrollmentFromToManager.create(this);
    }
  }

  @Override
  public void delete() throws Exception {
    sEnrollmentFromToManager.delete(this);
  }
}
