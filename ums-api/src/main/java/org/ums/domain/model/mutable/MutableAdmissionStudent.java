package org.ums.domain.model.mutable;

import org.ums.domain.model.common.Mutable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.AdmissionStudent;
import org.ums.domain.model.immutable.Program;
import org.ums.domain.model.immutable.Semester;
import org.ums.enums.MigrationStatus;

/**
 * Created by Monjur-E-Morshed on 12-Dec-16.
 */
public interface MutableAdmissionStudent extends AdmissionStudent, Mutable,
    MutableIdentifier<String>, MutableLastModifier {
  void setSemester(final Semester pSemester);

  void setSemesterId(final int pSemesterId);

  void setPin(final String pPin);

  void setHSCBoard(final String pHSCBoard);

  void setHSCRoll(final String pHSCRoll);

  void setHSCRegNo(final String pHSCRegNo);

  void setHSCYear(final int pHSCYear);

  void setHSCGroup(final String pHSCGroup);

  void setHSCGpa(final double pHSCGpa);

  void setSSCBoard(final String pSSCBoard);

  void setSSCRoll(final String pSSCRoll);

  void setSSCYear(final int pSSCYear);

  void setSSCGroup(final String pSSCGroup);

  void setSSCGpa(final double pSSCGpa);

  void setGender(final String pGender);

  void setDateOfBirth(final String pDateOfBirth);

  void setStudentName(final String pStudentName);

  void setFatherName(final String pFatherName);

  void setMotherName(final String pMotherName);

  void setQuota(final String pQuota);

  void setUnit(final String pUnit);

  void setAdmissionRoll(final String pAdmissionRoll);

  void setMeritSerialNo(final int pMeritSerialNo);

  void setStudentId(final String pStudentId);

  void setAllocatedProgram(final Program pAllocatedProgram);

  void setAllocatedProgramId(final int pAllocatedProgramId);

  void setMigrationStatus(final MigrationStatus pMigrationStatus);
}
