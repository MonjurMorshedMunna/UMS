package org.ums.domain.model.mutable;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.Program;
import org.ums.domain.model.immutable.Semester;
import org.ums.domain.model.immutable.SpStudent;

/**
 * Created by My Pc on 4/27/2016.
 */
public interface MutableSpStudent extends SpStudent, Editable<String>, MutableLastModifier,
    MutableIdentifier<String> {
  void setProgram(final Program pProgram);

  void setSemester(final Semester pSemester);

  void setAcademicYear(final int pAcademicYear);

  void setAcademicSemester(final int pAcademicSemester);

  void setStatus(final int pStatus);

  void setProgramShortName(final String pProgramShortName);

  void setApplicationType(final Integer pApplicationType);
}
