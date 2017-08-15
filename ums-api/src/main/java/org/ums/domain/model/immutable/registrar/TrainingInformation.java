package org.ums.domain.model.immutable.registrar;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.registrar.MutableTrainingInformation;

import java.io.Serializable;

public interface TrainingInformation extends Serializable, EditType<MutableTrainingInformation>, Identifier<Long>,
    LastModifier {

  String getEmployeeId();

  String getTrainingName();

  String getTrainingInstitute();

  String getTrainingFromDate();

  String getTrainingToDate();
}
