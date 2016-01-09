package org.ums.domain.model.regular;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.MutableCourse;
import org.ums.enums.CourseCategory;
import org.ums.enums.CourseType;

import java.io.Serializable;

public interface Course extends Serializable, LastModifier, EditType<MutableCourse>, Identifier<String> {

  String getNo();

  String getTitle();

  float getCrHr();

  int getOfferedDepartmentId();

  Department getOfferedBy() throws Exception;

  Department getOfferedTo() throws Exception;

  int getYear();

  int getSemester();

  int getViewOrder();

  int getCourseGroupId();

  CourseGroup getCourseGroup(final String pSyllabusId) throws Exception;

  String getSyllabusId();

  Syllabus getSyllabus() throws Exception;

  CourseType getCourseType();

  CourseCategory getCourseCategory();

}
