package org.ums.domain.model.mutable;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.Parameter;
import org.ums.domain.model.immutable.ParameterSetting;
import org.ums.domain.model.immutable.Semester;

/**
 * Created by My Pc on 3/14/2016.
 */
public interface MutableParameterSetting extends ParameterSetting, Editable<Long>,
    MutableLastModifier, MutableIdentifier<Long> {
  void setSemester(Semester pSemester);

  void setParameter(Parameter pParameter);

  void setStartDate(String pStartDate);

  void setEndDate(String pEndDate);
}
