package org.ums.domain.model.mutable;

import org.ums.domain.model.common.Mutable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.Course;
import org.ums.domain.model.immutable.CourseTeacher;
import org.ums.domain.model.immutable.Semester;
import org.ums.domain.model.immutable.Teacher;

public interface MutableCourseTeacher extends CourseTeacher, Mutable, MutableLastModifier, MutableIdentifier<String> {
  void setSemester(final Semester pSemester);

  void setSemesterId(final Integer pSemesterId);

  void setCourse(final Course pCourse);

  void setCourseId(final String pCourseId);

  void setTeacher(final Teacher pTeacher);

  void setTeacherId(final String pTeacherId);

  void setSection(final String pSection);
}
