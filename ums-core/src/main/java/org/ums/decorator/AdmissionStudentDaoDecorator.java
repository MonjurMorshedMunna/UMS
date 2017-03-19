package org.ums.decorator;

import org.ums.domain.model.immutable.AdmissionStudent;
import org.ums.domain.model.mutable.MutableAdmissionStudent;
import org.ums.enums.DepartmentSelectionType;
import org.ums.enums.MigrationStatus;
import org.ums.enums.ProgramType;
import org.ums.enums.QuotaType;
import org.ums.manager.AdmissionStudentManager;

import java.util.List;

public class AdmissionStudentDaoDecorator extends
    ContentDaoDecorator<AdmissionStudent, MutableAdmissionStudent, String, AdmissionStudentManager> implements
    AdmissionStudentManager {

  @Override
  public List<AdmissionStudent> getTaletalkData(int pSemesterId, ProgramType pProgramType) {
    return getManager().getTaletalkData(pSemesterId, pProgramType);
  }

  @Override
  public int saveTaletalkData(List<MutableAdmissionStudent> students) {
    return getManager().saveTaletalkData(students);
  }

  @Override
  public int getDataSize(int pSemesterId, ProgramType pProgramType) {
    return getManager().getDataSize(pSemesterId, pProgramType);
  }

  @Override
  public List<AdmissionStudent> getMeritList(int pSemesterId, QuotaType pQuotaType, String pUnit,
      ProgramType pProgramType) {
    return getManager().getMeritList(pSemesterId, pQuotaType, pUnit, pProgramType);
  }

  @Override
  public List<AdmissionStudent> getTaletalkData(int pSemesterId, QuotaType pQuotaType, String unit,
      ProgramType pProgramType) {
    return getManager().getTaletalkData(pSemesterId, pQuotaType, unit, pProgramType);
  }

  @Override
  public int saveMeritList(List<MutableAdmissionStudent> pStudents) {
    return getManager().saveMeritList(pStudents);
  }

  @Override
  public AdmissionStudent getAdmissionStudent(int pSemesterId, ProgramType pProgramType, String pReceiptId) {
    return getManager().getAdmissionStudent(pSemesterId, pProgramType, pReceiptId);
  }

  @Override
  public int updateDepartmentSelection(MutableAdmissionStudent pStudent,
      DepartmentSelectionType pDepartmentSelectionType) {
    return getManager().updateDepartmentSelection(pStudent, pDepartmentSelectionType);
  }

  @Override
  public List<AdmissionStudent> getNewStudentByReceiptId(int pSemesterId, String receiptId) {
    return getManager().getNewStudentByReceiptId(pSemesterId, receiptId);
  }

  @Override
  public AdmissionStudent getNextStudentForDepartmentSelection(int pSemesterId, ProgramType pProgramType, String pUnit,
      String pQuota, int pMeritSerialNo) {
    return getNextStudentForDepartmentSelection(pSemesterId, pProgramType, pUnit, pQuota, pMeritSerialNo);
  }

  // kawsurilu

  @Override
  public int setVerificationStatusAndUndertakenDate(final MutableAdmissionStudent pStudent) {
    return getManager().setVerificationStatusAndUndertakenDate(pStudent);
  }

  @Override
  public List<AdmissionStudent> getAllCandidates(ProgramType pProgramType, int pSemesterId) {
    return getManager().getAllCandidates(pProgramType, pSemesterId);
  }

  @Override
  public int updateStudentsAllocatedProgram(AdmissionStudent pAdmissionStudent, MigrationStatus pMigrationStatus) {
    return getManager().updateStudentsAllocatedProgram(pAdmissionStudent, pMigrationStatus);
  }

  @Override
  public AdmissionStudent getByStudentId(String pStudentId) {
    return getManager().getByStudentId(pStudentId);
  }

  @Override
  public List<AdmissionStudent> getAdmissionStudent(int pSemesterId, MigrationStatus pMigrationStatus) {
    return getManager().getAdmissionStudent(pSemesterId, pMigrationStatus);
  }

  @Override
  public int updateAdmissionMigrationStatus(List<MutableAdmissionStudent> pStudents) {
    return getManager().updateAdmissionMigrationStatus(pStudents);
  }

  @Override
  public AdmissionStudent getAdmissionStudent(int pSemesterId, QuotaType pQuotaType, int pMeritSerialNo) {
    return getManager().getAdmissionStudent(pSemesterId, pQuotaType, pMeritSerialNo);
  }

  @Override
  public List<AdmissionStudent> getTaletalkData(int pSemesterId, QuotaType pQuotaType, int fromMeritSerialNumber,
      int toMeritSerialNumber) {
    return getManager().getTaletalkData(pSemesterId, pQuotaType, fromMeritSerialNumber, toMeritSerialNumber);
  }

  //
}
