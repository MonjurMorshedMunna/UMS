package org.ums.academic.resource.student.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.domain.model.immutable.Parameter;
import org.ums.domain.model.immutable.Semester;
import org.ums.domain.model.immutable.Student;
import org.ums.domain.model.immutable.StudentRecord;
import org.ums.enums.CourseType;
import org.ums.fee.*;
import org.ums.fee.latefee.UGLateFee;
import org.ums.fee.latefee.UGLateFeeManager;
import org.ums.fee.payment.StudentPaymentManager;
import org.ums.fee.semesterfee.InstallmentSettingsManager;
import org.ums.fee.semesterfee.InstallmentStatusManager;
import org.ums.fee.semesterfee.SemesterAdmissionStatus;
import org.ums.fee.semesterfee.SemesterAdmissionStatusManager;
import org.ums.manager.ParameterSettingManager;
import org.ums.manager.SemesterManager;
import org.ums.manager.StudentManager;
import org.ums.manager.StudentRecordManager;
import org.ums.readmission.ReadmissionApplication;
import org.ums.readmission.ReadmissionApplicationManager;

@Component
class UGReadmissionFee extends AbstractUGSemesterFee {
  @Autowired
  StudentPaymentManager mStudentPaymentManager;
  @Autowired
  FeeTypeManager mFeeTypeManager;
  @Autowired
  private ParameterSettingManager mParameterSettingManager;
  @Autowired
  private UGLateFeeManager mUGLateFeeManager;
  @Autowired
  private SemesterAdmissionStatusManager mSemesterAdmissionStatusManager;
  @Autowired
  private InstallmentSettingsManager mInstallmentSettingsManager;
  @Autowired
  private InstallmentStatusManager mInstallmentStatusManager;
  @Autowired
  private StudentRecordManager mStudentRecordManager;
  @Autowired
  UGFeeManager mUgFeeManager;
  @Autowired
  UGLateFeeManager mUgLateFeeManager;
  @Autowired
  StudentManager mStudentManager;
  @Autowired
  private ReadmissionApplicationManager mReadmissionApplicationManager;
  @Autowired
  private UGRegularSemesterFee mUgRegularSemesterFee;
  @Autowired
  private FeeCategoryManager mFeeCategoryManager;
  @Autowired
  private SemesterManager mSemesterManager;

  @Override
  public boolean withinFirstInstallmentSlot(Integer pSemesterId) {
    return withInSlot(Parameter.ParameterName.READMISSION_FIRST_INSTALLMENT,
        UGLateFee.AdmissionType.READMISSION_FIRST_INSTALLMENT, pSemesterId);
  }

  @Override
  public boolean withinSecondInstallmentSlot(Integer pSemesterId) {
    return withInSlot(Parameter.ParameterName.READMISSION_SECOND_INSTALLMENT,
        UGLateFee.AdmissionType.READMISSION_SECOND_INSTALLMENT, pSemesterId);
  }

  @Override
  public UGFees firstInstallment(String pStudentId, Integer pSemesterId) {
    List<UGFee> ugFees = getFee(pStudentId, pSemesterId).getUGFees();
    ugFees = ugFees.stream().filter(
        (fee) -> !fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.THEORY_REPEATER.toString()))
        .collect(Collectors.toList());
    Optional<UGFee> dropFee = dropFee(pStudentId, pSemesterId);
    if(dropFee.isPresent()) {
      ugFees.add(dropFee.get());
    }
    UGFees fees = new UGFees(ugFees);
    fees.setUGLateFee(getLateFee(pSemesterId, UGLateFee.AdmissionType.READMISSION_FIRST_INSTALLMENT));
    return fees;
  }

  @Override
  public UGFees secondInstallment(String pStudentId, Integer pSemesterId) {
    Student student = mStudentManager.get(pStudentId);
    List<UGFee> ugFees = mUgFeeManager.getFee(student.getProgram().getFaculty().getId(), student.getSemesterId());
    List<UGFee> installmentFees = ugFees.stream().filter(
        (fee) -> fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.INSTALLMENT_CHARGE.toString()))
        .collect(Collectors.toList());

    List<UGFee> totalReadmissionFees = getFee(pStudentId, pSemesterId).getUGFees();
    List<UGFee> readmissionFees = totalReadmissionFees.stream().filter(
        (fee) -> fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.THEORY_REPEATER.toString()))
        .collect(Collectors.toList());
    readmissionFees.addAll(installmentFees);
    UGFees fees = new UGFees(readmissionFees);
    fees.setUGLateFee(getLateFee(pSemesterId, UGLateFee.AdmissionType.READMISSION_SECOND_INSTALLMENT));
    return fees;
  }

  @Override
  public UGFees getFee(String pStudentId, Integer pSemesterId) {
    Student student = mStudentManager.get(pStudentId);
    List<UGFee> semesterFees = mUgFeeManager.getFee(student.getProgram().getFaculty().getId(), student.getSemesterId(),
        mFeeCategoryManager.getFeeCategories(FeeType.Types.SEMESTER_FEE.getId()));
    List<UGFee> ugFees = semesterFees.stream()
        .filter((fee) -> fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.READMISSION.toString())
            || fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.ESTABLISHMENT.toString()))
        .collect(Collectors.toList());

    Optional<UGFee> dropFee = dropFee(pStudentId, pSemesterId);
    if(dropFee.isPresent()) {
      ugFees.add(dropFee.get());
    }

    List<ReadmissionApplication> applications =
        mReadmissionApplicationManager.getReadmissionApplication(pSemesterId, pStudentId);
    List<ReadmissionApplication> theory =
        applications.stream().filter((application) -> application.getCourse().getCourseType().equals(CourseType.THEORY))
            .collect(Collectors.toList());
    List<ReadmissionApplication> sessional = applications.stream()
        .filter((application) -> application.getCourse().getCourseType().equals(CourseType.SESSIONAL))
        .collect(Collectors.toList());

    Optional<UGFee> theoryFee = ugFees.stream().filter(
        (fee) -> fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.THEORY_REPEATER.toString()))
        .findFirst();
    Optional<UGFee> sessionalFee = ugFees.stream().filter(
        (fee) -> fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.SESSIONAL_REPEATER.toString()))
        .findFirst();
    if(theoryFee.isPresent()) {
      theoryFee.get().edit().setAmount(theoryFee.get().getAmount().multiply(new BigDecimal(theory.size())));
      ugFees.add(theoryFee.get());
    }
    if(sessionalFee.isPresent()) {
      sessionalFee.get().edit().setAmount(sessionalFee.get().getAmount().multiply(new BigDecimal(sessional.size())));
      ugFees.add(sessionalFee.get());
    }
    UGFees fees = new UGFees(ugFees);
    fees.setUGLateFee(getLateFee(pSemesterId, UGLateFee.AdmissionType.READMISSION));
    return fees;
  }

  @Override
  public boolean withInAdmissionSlot(Integer pSemesterId) {
    UGSemesterFeeResponse installmentStatus = getInstallmentStatus(pSemesterId);
    return withInSlot(Parameter.ParameterName.READMISSION, UGLateFee.AdmissionType.READMISSION, pSemesterId)
        || (installmentStatus == UGSemesterFeeResponse.INSTALLMENT_AVAILABLE && (withinFirstInstallmentSlot(pSemesterId) || withinSecondInstallmentSlot(pSemesterId)));
  }

  @Override
  public boolean installmentAvailable(String pStudentId, Integer pSemesterId) {
    List<UGFee> regularAdmissionFees = mUgRegularSemesterFee.getFee(pStudentId, pSemesterId).getUGFees();
    List<UGFee> readmissionFees = getFee(pStudentId, pSemesterId).getUGFees();

    BigDecimal totalRegularAdmissionFee = new BigDecimal(0);
    BigDecimal totalReadmissionFee = new BigDecimal(0);
    for(UGFee fee : regularAdmissionFees) {
      totalRegularAdmissionFee = totalRegularAdmissionFee.add(fee.getAmount());
    }
    for(UGFee fee : readmissionFees) {
      totalReadmissionFee = totalReadmissionFee.add(fee.getAmount());
    }

    return totalReadmissionFee.compareTo(totalRegularAdmissionFee.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)) > 0;
  }

  @Override
  public UGSemesterFeeResponse getAdmissionStatus(String pStudentId, Integer pSemesterId) {
    List<ReadmissionApplication> applications =
        mReadmissionApplicationManager.getReadmissionApplication(pSemesterId, pStudentId);
    return applications != null && applications.size() > 0 ? UGSemesterFeeResponse.READMISSION_APPLIED
        : UGSemesterFeeResponse.READMISSION_NOT_APPLIED;
  }

  private Optional<UGFee> dropFee(String pStudentId, Integer pSemesterId) {
    Student student = mStudentManager.get(pStudentId);
    List<UGFee> semesterFees = mUgFeeManager.getFee(student.getProgram().getFaculty().getId(), student.getSemesterId(),
        mFeeCategoryManager.getFeeCategories(FeeType.Types.SEMESTER_FEE.getId()));

    Integer droppedSemester = noOfDroppedSemester(pStudentId, pSemesterId);
    if(droppedSemester > 0 && droppedSemester <= 2) {
      List<UGFee> penaltyFees = semesterFees.stream()
          .filter(
              (fee) -> fee.getFeeCategory().getFeeId().equalsIgnoreCase(FeeCategory.Categories.DROP_PENALTY.toString()))
          .collect(Collectors.toList());
      UGFee penaltyFee = penaltyFees.get(0);
      penaltyFee.edit().setAmount(penaltyFee.getAmount().multiply(new BigDecimal(droppedSemester)));
      return Optional.of(penaltyFee);
    }
    return Optional.empty();
  }

  private Integer noOfDroppedSemester(String pStudentId, Integer pSemesterId) {
    Student student = mStudentManager.get(pStudentId);
    Optional<SemesterAdmissionStatus> status = mSemesterAdmissionStatusManager.lastAdmissionStatus(pStudentId);
    if(status.isPresent()) {
      Semester lastAdmittedSemester = status.get().getSemester();
      List<Semester> noOfSemesterSince =
          mSemesterManager.semestersAfter(lastAdmittedSemester.getId(), pSemesterId, student.getProgram()
              .getProgramTypeId());
      int dropCount = 0;
      for(Semester semester : noOfSemesterSince) {
        StudentRecord studentRecord = mStudentRecordManager.getStudentRecord(pStudentId, semester.getId());
        if(studentRecord != null && studentRecord.getType() == StudentRecord.Type.DROPPED) {
          dropCount++;
        }
      }
      return dropCount;
    }
    return 0;
  }

  @Override
  ParameterSettingManager getParameterSettingManager() {
    return mParameterSettingManager;
  }

  @Override
  UGLateFeeManager getUGLateFeeManager() {
    return mUGLateFeeManager;
  }

  @Override
  StudentRecordManager getStudentRecordManager() {
    return mStudentRecordManager;
  }

  @Override
  InstallmentSettingsManager getInstallmentSettingsManager() {
    return mInstallmentSettingsManager;
  }

  @Override
  StudentPaymentManager getStudentPaymentManager() {
    return mStudentPaymentManager;
  }

  @Override
  FeeTypeManager getFeeTypeManager() {
    return mFeeTypeManager;
  }

  @Override
  SemesterAdmissionStatusManager getSemesterAdmissionStatusManager() {
    return mSemesterAdmissionStatusManager;
  }

  @Override
  InstallmentStatusManager getInstallmentStatusManager() {
    return mInstallmentStatusManager;
  }
}
