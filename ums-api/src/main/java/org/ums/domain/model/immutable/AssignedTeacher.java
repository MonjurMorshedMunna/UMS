package org.ums.domain.model.immutable;

import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;

import java.io.Serializable;

public interface AssignedTeacher extends Serializable, LastModifier, Identifier<Integer> {
  Semester getSemester() throws Exception;

  Integer getSemesterId();

  Course getCourse() throws Exception;

  String getCourseId();
}
