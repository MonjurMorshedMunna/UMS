package org.ums.domain.model.dto;

import org.ums.domain.model.readOnly.Program;
import org.ums.domain.model.readOnly.Semester;

/**
 * Created by User on 1/16/2016.
 */
public interface MutableSemesterSyllabusMapDto extends SemesterSyllabusMapDto {
  void setAcademicSemester(Semester semester);

  void setProgram(Program program);

  void setCopySemester(final Semester pSemester) throws Exception;
}
