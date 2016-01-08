package org.ums.domain.model.mutable;


import org.ums.domain.model.regular.ProgramType;
import org.ums.domain.model.regular.Semester;
import org.ums.domain.model.common.Mutable;
import org.ums.domain.model.common.MutableIdentifier;

import java.util.Date;

public interface MutableSemester extends Semester, Mutable, MutableLastModifier, MutableIdentifier<Integer> {

  void setName(final String pName);

  void setStartDate(final Date pDate);

  void setEndDate(final Date pDate);

  void setStatus(final boolean pStatus);

  void setProgramType(final ProgramType pProgram);

  void setProgramTypeId(final int pProgramTypeId);
}
