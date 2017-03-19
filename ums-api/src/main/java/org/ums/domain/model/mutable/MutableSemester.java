package org.ums.domain.model.mutable;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.ProgramType;
import org.ums.domain.model.immutable.Semester;

import java.util.Date;

public interface MutableSemester extends Semester, Editable<Integer>, MutableLastModifier, MutableIdentifier<Integer> {

  void setName(final String pName);

  void setStartDate(final Date pDate);

  void setEndDate(final Date pDate);

  void setStatus(final Status pStatus);

  void setProgramType(final ProgramType pProgram);

  void setProgramTypeId(final int pProgramTypeId);
}
