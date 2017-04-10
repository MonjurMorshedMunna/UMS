package org.ums.domain.model.immutable.registrar.employee;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.registrar.employee.MutableEmployeeInformation;

import java.io.Serializable;

public interface EmployeeInformation extends Serializable, EditType<MutableEmployeeInformation>, Identifier<Integer>,
    LastModifier {

  int getEmployeeId();

  String getEmploymentType();

  int getDesignation();

  String getDeptOffice();

  String getJoiningDate();

  String getJobPermanentDate();

  String getJobTerminationDate();

  int getExtNo();

  String getShortName();

  String getRoomNo();
}
