package org.ums.domain.model.immutable;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.MutableEquivalentCourse;

import java.io.Serializable;

public interface EquivalentCourse extends Serializable, Identifier<Integer>,
    EditType<MutableEquivalentCourse>, LastModifier {
  String getOldCourseId();

  Course getOldCourse() throws Exception;

  String getNewCourseId();

  Course getNewCourse() throws Exception;
}