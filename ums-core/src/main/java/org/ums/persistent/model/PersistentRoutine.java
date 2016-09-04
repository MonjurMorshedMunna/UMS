package org.ums.persistent.model;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.immutable.Program;
import org.ums.domain.model.immutable.Semester;
import org.ums.domain.model.mutable.MutableRoutine;
import org.ums.manager.ProgramManager;
import org.ums.manager.RoutineManager;
import org.ums.manager.SemesterManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PersistentRoutine implements MutableRoutine {
  private static SemesterManager sSemesterManager;
  private static ProgramManager sProgramManager;
  private static RoutineManager sRoutineManager;

  static{
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sSemesterManager = applicationContext.getBean("semesterManager", SemesterManager.class);
    sProgramManager = applicationContext.getBean("programManager", ProgramManager.class);
    sRoutineManager = applicationContext.getBean("routineManager", RoutineManager.class);
  }

  private String mId;
  private Semester mSemester;
  private Program mProgram;
  private String mSection;
  private int mAcademicYear;
  private int mAcademicSemester;
  private String mStartTime;
  private String mEndTime;
  private String mLastModified;
  private Integer mSemesterId;
  private String mCourseId;
  private String mCourseNo;
  private int mDuration;
  private String mRoomNo;
  private int mProgramId;
  private int mDay;

  public PersistentRoutine(){

  }

  public PersistentRoutine(final PersistentRoutine pPersistentRoutine) throws Exception{
    mId = pPersistentRoutine.getId();
    mSemester = pPersistentRoutine.getSemester();
    mProgram = pPersistentRoutine.getProgram();
    mCourseId = pPersistentRoutine.getCourseId();
    mSection = pPersistentRoutine.getSection();
    mAcademicYear = pPersistentRoutine.getAcademicYear();
    mAcademicSemester = pPersistentRoutine.getAcademicSemester();
    mStartTime = pPersistentRoutine.getStartTime();
    mEndTime = pPersistentRoutine.getEndTime();
    mLastModified = pPersistentRoutine.getLastModified();
    mSemesterId = pPersistentRoutine.getSemesterId();
    mCourseId = pPersistentRoutine.getCourseId();
    mCourseNo = pPersistentRoutine.getCourseNo();
    mDuration = pPersistentRoutine.getDuration();
    mRoomNo = pPersistentRoutine.getRoomNo();
    mDay= pPersistentRoutine.getDay();
  }




  @Override
  public int getDuration() throws Exception{
    char[] startTimeArray= mStartTime.toCharArray();
    startTimeArray[2]=':';
    String startTimeString= String.valueOf(startTimeArray);
    char[] endTimeArray = mEndTime.toCharArray();
    endTimeArray[2]=':';
    String endTimeString = String.valueOf(endTimeArray);
    SimpleDateFormat format = new SimpleDateFormat("h:mm a");
    Date date1 = format.parse(startTimeString);
    Date date2 = format.parse(endTimeString);
    long difference = date2.getTime() - date1.getTime();
    long diffMinutes = (difference / 60000)/50;
    String duration = String.valueOf(diffMinutes);
    char[] durationArray = duration.toCharArray();

    mDuration = Integer.valueOf(duration);
    return mDuration;
  }

  @Override
  public void setDuration(int pDuration) {
    mDuration = pDuration;
  }

  @Override
  public void setCourseNo(String pCourseNo) {
    mCourseNo = pCourseNo;
  }

  @Override
  public String getCourseNo() {
    return mCourseNo;
  }

  @Override
  public int getDay() {
    return mDay;
  }

  @Override
  public void setDay(int pDay) {
    mDay = pDay;
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
  public Program getProgram() throws Exception {
    return mProgram == null ? sProgramManager.get(mProgramId) : sProgramManager.validate(mProgram);

  }

  @Override
  public String getRoomNo() {
    return mRoomNo;
  }

  @Override
  public void setRoomNo(String pRoomNo) {
    mRoomNo = pRoomNo;
  }


  public String getCourseId() {
    return mCourseId;
  }

  public void setCourseId(String pCourseId) {
    mCourseId = pCourseId;
  }

  public Integer getSemesterId() {
    return mSemesterId;
  }

  public void setSemesterId(Integer pSemesterId) {
    mSemesterId = pSemesterId;
  }

  @Override
  public void setSemester(Semester pSemester) {
    mSemester = pSemester;
  }



  @Override
  public void setSection(String pSection) {
    mSection = pSection;
  }

  @Override
  public void setAcademicYear(int pAcademicYear) {
    mAcademicYear = pAcademicYear;
  }

  @Override
  public void setAcademicSemester(int pAcademicSemester) {
    mAcademicSemester = pAcademicSemester;
  }

  @Override
  public void setStartTime(String pStartTime) {
    mStartTime = pStartTime;
  }

  @Override
  public void setEndTime(String pEndTime) {
    mEndTime = pEndTime;
  }

  @Override
  public void commit(boolean update) throws Exception {
    if (update) {
      sRoutineManager.update(this);
    } else {
      sRoutineManager.create(this);
    }
  }

  @Override
  public void delete() throws Exception {
      sRoutineManager.delete(this);
  }

  @Override
  public void setId(String pId) {
    mId = pId;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }

  @Override
  public Semester getSemester()throws Exception {
    return mSemester==null?sSemesterManager.get(mSemesterId): sSemesterManager.validate(mSemester);
  }



  @Override
  public String getSection() {
    return mSection;
  }

  @Override
  public int getAcademicYear() {
    return mAcademicYear;
  }

  @Override
  public int getAcademicSemester() {
    return mAcademicSemester;
  }

  @Override
  public String getStartTime() {
    return mStartTime;
  }

  @Override
  public String getEndTime() {
    return mEndTime;
  }

  @Override
  public MutableRoutine edit() throws Exception {
    return new PersistentRoutine(this);
  }

  @Override
  public String getId() {
    return mId;
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }
}
