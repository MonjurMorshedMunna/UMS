package org.ums.academic.model;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.ums.domain.model.CourseGroup;
import org.ums.domain.model.MutableCourseGroup;
import org.ums.domain.model.MutableSyllabus;
import org.ums.domain.model.Syllabus;
import org.ums.manager.ContentManager;
import org.ums.util.Constants;

public class PersistentCourseGroup implements MutableCourseGroup {
  private static ContentManager<Syllabus, MutableSyllabus, String> sSyllabusManager;
  private static ContentManager<CourseGroup, MutableCourseGroup, Integer> sCourseGroupManager;

  static {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Constants.SERVICE_CONTEXT);
    sSyllabusManager = (ContentManager) applicationContext.getBean("syllabusManager");
    sCourseGroupManager = (ContentManager) applicationContext.getBean("courseGroupManager");
  }

  private int mId;
  private String mName;
  private Syllabus mSyllabus;
  private String mSyllabusId;
  private String mLastModified;

  public PersistentCourseGroup() {

  }

  public PersistentCourseGroup(final PersistentCourseGroup pPersistentCourseGroup) throws Exception {
    this.mId = pPersistentCourseGroup.getId();
    this.mName = pPersistentCourseGroup.getName();
    this.mSyllabus = pPersistentCourseGroup.getSyllabus();
  }

  @Override
  public Integer getId() {
    return mId;
  }

  @Override
  public void setId(Integer pId) {
    mId = pId;
  }

  @Override
  public String getName() {
    return mName;
  }

  @Override
  public void setName(String pName) {
    mName = pName;
  }

  @Override
  public Syllabus getSyllabus() throws Exception {
    return mSyllabus == null ? sSyllabusManager.get(mSyllabusId) : mSyllabus;
  }

  @Override
  public void setSyllabus(Syllabus pSyllabus) {
    mSyllabus = pSyllabus;
  }

  @Override
  public String getSyllabusId() {
    return mSyllabusId;
  }

  public void setSyllabusId(String pSyllabusId) {
    mSyllabusId = pSyllabusId;
  }

  @Override
  public MutableCourseGroup edit() throws Exception {
    return new PersistentCourseGroup(this);
  }

  @Override
  public void commit(boolean update) throws Exception {
    if (update) {
      sCourseGroupManager.update(this);
    } else {
      sCourseGroupManager.create(this);
    }
  }

  @Override
  public void delete() throws Exception {
    sCourseGroupManager.delete(this);
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }

}