package org.ums.decorator;

import org.ums.domain.model.mutable.MutableCourseGroup;
import org.ums.domain.model.immutable.CourseGroup;
import org.ums.manager.CourseGroupManager;

public class CourseGroupDaoDecorator extends
    ContentDaoDecorator<CourseGroup, MutableCourseGroup, Integer, CourseGroupManager> implements CourseGroupManager {
  @Override
  public CourseGroup getBySyllabus(Integer pCourseGroupId, String pSyllabusId) {
    return getManager().getBySyllabus(pCourseGroupId, pSyllabusId);
  }
}
