package org.ums.persistent.model;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.immutable.Program;
import org.ums.domain.model.immutable.Semester;
import org.ums.domain.model.immutable.SemesterWithdrawalLog;
import org.ums.domain.model.immutable.Student;
import org.ums.domain.model.mutable.MutableSemesterWithdrawal;
import org.ums.manager.ProgramManager;
import org.ums.manager.SemesterManager;
import org.ums.manager.SemesterWithDrawalManager;
import org.ums.manager.StudentManager;

import java.util.ArrayList;
import java.util.List;

public class PersistentSemesterWithdrawal implements MutableSemesterWithdrawal {

  private static SemesterManager sSemesterManager;
  private static ProgramManager sProgramManager;
  private static StudentManager sStudentManager;
  private static SemesterWithDrawalManager sSemesterWithDrawalManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sSemesterManager = applicationContext.getBean("semesterManager", SemesterManager.class);
    sProgramManager = applicationContext.getBean("programManager", ProgramManager.class);
    sStudentManager = applicationContext.getBean("studentManager", StudentManager.class);
    sSemesterWithDrawalManager =
        applicationContext.getBean("semesterWithdrawalManager", SemesterWithDrawalManager.class);
  }

  private Long mId;
  private Semester mSemester;
  private Program mProgram;
  private int mProgramId;
  private Student mStudent;
  private String mStudentId;
  private int mAcademicYear;
  private int mAcademicSemester;
  private int mSemesterId;
  private String mCause;
  private int mStatus;
  private String mAppDate;
  private String mLastModified;
  private String mComment;
  private List<SemesterWithdrawalLog> mLogs = new ArrayList<>();

  public PersistentSemesterWithdrawal() {

  }

  public PersistentSemesterWithdrawal(final PersistentSemesterWithdrawal pPersistentSemesterWithdrawal) {
    mId = pPersistentSemesterWithdrawal.getId();

    mSemester = pPersistentSemesterWithdrawal.getSemester();
    mSemesterId = pPersistentSemesterWithdrawal.getSemesterId();
    mProgram = pPersistentSemesterWithdrawal.getProgram();
    mProgramId = pPersistentSemesterWithdrawal.getProgramId();
    mStudent = pPersistentSemesterWithdrawal.getStudent();
    mStudentId = pPersistentSemesterWithdrawal.getStudentId();
    mAcademicYear = pPersistentSemesterWithdrawal.getAcademicYear();
    mAcademicSemester = pPersistentSemesterWithdrawal.getAcademicSemester();
    mCause = pPersistentSemesterWithdrawal.getCause();
    mStatus = pPersistentSemesterWithdrawal.getStatus();
    mAppDate = pPersistentSemesterWithdrawal.getAppDate();
    mLastModified = pPersistentSemesterWithdrawal.getLastModified();
    mComment = pPersistentSemesterWithdrawal.getComment();
  }

  @Override
  public void setComments(String pComments) {
    mComment = pComments;
  }

  @Override
  public String getComment() {
    return mComment;
  }

  @Override
  public void setAppDate(String pAppDate) {
    mAppDate = pAppDate;
  }

  @Override
  public String getAppDate() {
    return mAppDate;
  }

  @Override
  public void setStatus(int pStatus) {
    mStatus = pStatus;
  }

  @Override
  public int getStatus() {
    return mStatus;
  }

  public int getAcademicYear() {
    return mAcademicYear;
  }

  public void setAcademicYear(int pAcademicYear) {
    mAcademicYear = pAcademicYear;
  }

  public int getAcademicSemester() {
    return mAcademicSemester;
  }

  public void setAcademicSemester(int pAcademicSemester) {
    mAcademicSemester = pAcademicSemester;
  }

  public String getStudentId() {
    return mStudentId;
  }

  public void setStudentId(String pStudentId) {
    mStudentId = pStudentId;
  }

  @Override
  public void setStudent(Student pStudent) {
    mStudent = pStudent;
  }

  @Override
  public Student getStudent() {
    return mStudent == null ? sStudentManager.get(mStudentId) : sStudentManager.validate(mStudent);
  }

  public int getProgramId() {
    return mProgramId;
  }

  public void setProgramId(int pProgramId) {
    mProgramId = pProgramId;
  }

  @Override
  public void setProgram(Program pProgram) {
    mProgram = pProgram;
  }

  @Override
  public Program getProgram() {
    return mProgram == null ? sProgramManager.get(mProgramId) : sProgramManager.validate(mProgram);
  }

  public int getSemesterId() {
    return mSemesterId;
  }

  public void setSemesterId(int pSemesterId) {
    mSemesterId = pSemesterId;
  }

  @Override
  public void setSemester(Semester pSemester) {
    mSemester = pSemester;
  }

  @Override
  public void setCause(String pCause) {
    mCause = pCause;
  }

  @Override
  public Semester getSemester() {
    return mSemester == null ? sSemesterManager.get(mSemesterId) : sSemesterManager.validate(mSemester);
  }

  @Override
  public String getCause() {
    return mCause;
  }

  @Override
  public MutableSemesterWithdrawal edit() {
    return new PersistentSemesterWithdrawal(this);
  }

  @Override
  public Long getId() {
    return mId;
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public Long create() {
    return sSemesterWithDrawalManager.create(this);
  }

  @Override
  public void update() {
    sSemesterWithDrawalManager.update(this);
  }

  @Override
  public void delete() {
    sSemesterWithDrawalManager.delete(this);
  }

  @Override
  public void setId(Long pId) {
    mId = pId;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }
}
