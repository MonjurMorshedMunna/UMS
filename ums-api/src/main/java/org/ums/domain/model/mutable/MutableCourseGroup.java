package org.ums.domain.model.mutable;


import org.ums.domain.model.readOnly.CourseGroup;
import org.ums.domain.model.readOnly.Syllabus;
import org.ums.domain.model.common.Mutable;
import org.ums.domain.model.common.MutableIdentifier;

public interface MutableCourseGroup extends CourseGroup, Mutable, MutableLastModifier, MutableIdentifier<Integer> {
  void setName(final String pName);

  void setSyllabus(final Syllabus pSyllabus);

  void setSyllabusId(final String pSyllabusId);
}
