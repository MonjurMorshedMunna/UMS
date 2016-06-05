package org.ums.persistent.model;


import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.immutable.CourseTeacher;
import org.ums.domain.model.immutable.Teacher;
import org.ums.domain.model.mutable.MutableCourseTeacher;
import org.ums.manager.AssignedTeacherManager;

public class PersistentCourseTeacher extends AbstractAssignedTeacher implements MutableCourseTeacher {
  private static AssignedTeacherManager<CourseTeacher, MutableCourseTeacher, Integer> sCourseTeacherManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sCourseTeacherManager = applicationContext.getBean("courseTeacherManager", AssignedTeacherManager.class);
  }

  private String mSection;
  private Teacher mTeacher;
  private String mTeacherId;

  public PersistentCourseTeacher() {
  }

  public PersistentCourseTeacher(final PersistentCourseTeacher pPersistentCourseTeacher) throws Exception {
    this.mId = pPersistentCourseTeacher.getId();
    this.mSemester = pPersistentCourseTeacher.getSemester();
    this.mTeacher = pPersistentCourseTeacher.getTeacher();
    this.mCourse = pPersistentCourseTeacher.getCourse();
    this.mSection = pPersistentCourseTeacher.getSection();
    this.mLastModified = pPersistentCourseTeacher.getLastModified();
  }

  @Override
  public Teacher getTeacher() throws Exception {
    return mTeacher == null ? sTeacherManager.get(mTeacherId) : sTeacherManager.validate(mTeacher);
  }

  @Override
  public void setTeacher(Teacher pTeacher) {
    mTeacher = pTeacher;
  }

  @Override
  public String getTeacherId() {
    return mTeacherId;
  }

  @Override
  public void setTeacherId(String pTeacherId) {
    mTeacherId = pTeacherId;
  }

  @Override
  public String getSection() {
    return mSection;
  }

  @Override
  public void setSection(String pSection) {
    mSection = pSection;
  }

  @Override
  public MutableCourseTeacher edit() throws Exception {
    return new PersistentCourseTeacher(this);
  }

  @Override
  public void delete() throws Exception {
    sCourseTeacherManager.delete(this);
  }

  @Override
  public void commit(boolean update) throws Exception {
    if (update) {
      sCourseTeacherManager.update(this);
    } else {
      sCourseTeacherManager.create(this);
    }
  }
}
