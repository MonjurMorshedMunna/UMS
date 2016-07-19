package org.ums.cache;

import org.ums.domain.model.immutable.Course;
import org.ums.domain.model.mutable.MutableCourse;
import org.ums.manager.CacheManager;
import org.ums.manager.CourseManager;
import org.ums.util.CacheUtil;

import java.util.List;


public class CourseCache extends ContentCache<Course, MutableCourse, String, CourseManager> implements CourseManager {
  CacheManager<Course, String> mCacheManager;


  public CourseCache(CacheManager<Course, String> pCacheManager) {
    mCacheManager = pCacheManager;
  }

  @Override
  protected CacheManager<Course, String> getCacheManager() {
    return mCacheManager;
  }

  @Override
  protected String getCacheKey(String pId) {
    return CacheUtil.getCacheKey(Course.class, pId);
  }

  @Override
  public List<Course> getBySyllabus(String pSyllabusId) {
    return getManager().getBySyllabus(pSyllabusId);
  }

  @Override
  public List<Course> getByYearSemester(String pSemesterId, String pProgramId, int year, int semester) {
    return getManager().getByYearSemester(pSemesterId, pProgramId, year, semester);
  }

  @Override
  public List<Course> getBySemesterProgram(String pSemesterId, String pProgramId) {
    return getManager().getBySemesterProgram(pSemesterId, pProgramId);
  }

  @Override
  public List<Course> getOptionalCourseList(String pSyllabusId, Integer pYear, Integer pSemester) {
    return getManager().getOptionalCourseList(pSyllabusId, pYear, pSemester);
  }

  @Override
  public List<Course> getOfferedCourseList(Integer pSemesterId, Integer pProgramId, Integer pYear, Integer pSemester) {
    return getManager().getOfferedCourseList(pSemesterId, pProgramId, pYear, pSemester);
  }

  @Override
  public List<Course> getCallForApplicationCourseList(Integer pSemesterId, Integer pProgramId, Integer pYear, Integer pSemester) {
    return getManager().getCallForApplicationCourseList(pSemesterId, pProgramId, pYear, pSemester);
  }


  @Override
  public List<Course> getApprovedCourseList(Integer pSemesterId, Integer pProgramId, Integer pYear, Integer pSemester) {
    return getManager().getApprovedCourseList(pSemesterId, pProgramId, pYear, pSemester);
  }

  @Override
  public List<Course> getApprovedCallForApplicationCourseList(Integer pSemesterId, Integer pProgramId, Integer pYear, Integer pSemester) {
    return getManager().getApprovedCallForApplicationCourseList(pSemesterId, pProgramId, pYear, pSemester);
  }

  @Override
  public List<Course> getMandatoryCourses(String pSyllabusId, final Integer pYear, final Integer pSemester) {
    return getManager().getMandatoryCourses(pSyllabusId, pYear, pSemester);
  }

  @Override
  public List<Course> getAll() throws Exception {
    return super.getAll();
  }

  @Override
  public List<Course> getMandatorySesssionalCourses(String pSyllabusId, final Integer pYear, final Integer pSemester) {
    return getManager().getMandatorySesssionalCourses(pSyllabusId, pYear, pSemester);
  }

  @Override
  public List<Course> getMandatoryTheoryCourses(String pSyllabusId, final Integer pYear, final Integer pSemester) {
    return getManager().getMandatoryTheoryCourses(pSyllabusId, pYear, pSemester);
  }

  @Override
  public Course getByCourseName(String pCourseName, String pSyllabusId) {
    return getManager().getByCourseName(pCourseName, pSyllabusId);
  }
}
