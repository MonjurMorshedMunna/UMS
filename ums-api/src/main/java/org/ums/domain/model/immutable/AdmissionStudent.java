package org.ums.domain.model.immutable;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.MutableAdmissionStudent;
import org.ums.enums.MigrationStatus;

import java.io.Serializable;

/**
 * Created by Monjur-E-Morshed on 12-Dec-16.
 */
public interface AdmissionStudent extends Serializable, EditType<MutableAdmissionStudent>,
    Identifier<String>, LastModifier {
  Semester getSemester();

  int getSemesterId();

  String getReceiptId();

  String getPin();

  String getHSCBoard();

  String getHSCRoll();

  String getHSCRegNo();

  int getHSCYear();

  String getHSCGroup();

  Double getHSCGpa();

  String getSSCBoard();

  String getSSCRoll();

  String getSSCRegNo();

  int getSSCYear();

  String getSSCGroup();

  Double getSSCGpa();

  String getGender();

  String getBirthDate();

  String getStudentName();

  String getFatherName();

  String getMotherName();

  String getQuota();

  String getAdmissionRoll();

  Integer getMeritSerialNo();

  String getStudentId();

  Program getAllocatedProgram();

  int getAllocatedProgramId();

  MigrationStatus getMigrationStatus();
}