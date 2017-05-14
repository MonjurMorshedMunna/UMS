package org.ums.academic.resource.fee.semesterfee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.ums.domain.model.immutable.Parameter;
import org.ums.domain.model.immutable.ParameterSetting;
import org.ums.fee.FeeCategory;
import org.ums.fee.FeeType;
import org.ums.fee.FeeTypeManager;
import org.ums.fee.UGFee;
import org.ums.fee.latefee.UGLateFee;
import org.ums.fee.latefee.UGLateFeeManager;
import org.ums.fee.payment.MutableStudentPayment;
import org.ums.fee.payment.PersistentStudentPayment;
import org.ums.fee.payment.StudentPayment;
import org.ums.fee.payment.StudentPaymentManager;
import org.ums.fee.semesterfee.*;
import org.ums.manager.ParameterSettingManager;
import org.ums.manager.StudentRecordManager;

abstract class AbstractUGSemesterFee implements UGSemesterFee {
  boolean withInSlot(Parameter.ParameterName parameterName, UGLateFee.AdmissionType pLateFeeFor, Integer pSemesterId) {
    Date today = new Date();
    ParameterSetting semesterAdmissionDate =
        getParameterSettingManager().getByParameterAndSemesterId(parameterName.getLabel(), pSemesterId);
    if(semesterAdmissionDate.getStartDate().after(today) && semesterAdmissionDate.getEndDate().before(today)) {
      return true;
    }
    else {
      Optional<UGLateFee> ugLateFee = getLateFee(pSemesterId, pLateFeeFor);
      return ugLateFee.isPresent();
    }
  }

  Optional<UGLateFee> getLateFee(Integer pSemesterId, UGLateFee.AdmissionType pLateFeeFor) {
    Date today = new Date();
    List<UGLateFee> lateFees = getUGLateFeeManager().getLateFees(pSemesterId);
    for(UGLateFee fee : lateFees) {
      if(fee.getFrom().after(today) && fee.getTo().before(today) && fee.getAdmissionType() == pLateFeeFor) {
        return Optional.of(fee);
      }
    }
    return Optional.empty();
  }

  private boolean hasAppliedForPayment(String pStudentId, Integer pSemesterId) {
    List<StudentPayment> payments =
        getStudentPaymentManager().getPayments(pStudentId, pSemesterId,
            getFeeTypeManager().get(FeeType.Types.SEMESTER_FEE.getId()));
    if(payments.size() > 0) {
      StudentPayment payment = payments.get(0);
      return payment.getStatus() == StudentPayment.Status.APPLIED;
    }
    return false;
  }

  public UGSemesterFeeResponse getInstallmentStatus(Integer pSemesterId) {
    return getInstallmentSettingsManager().getInstallmentSettings(pSemesterId).isPresent() ? UGSemesterFeeResponse.INSTALLMENT_AVAILABLE
        : UGSemesterFeeResponse.INSTALLMENT_NOT_AVAILABLE;
  }

  @Override
  public UGSemesterFeeResponse getSemesterFeeStatus(String pStudentId, Integer pSemesterId) {
    if(hasAppliedForPayment(pStudentId, pSemesterId)) {
      return UGSemesterFeeResponse.APPLIED;
    }
    else {
      Optional<SemesterAdmissionStatus> admissionStatus =
          getSemesterAdmissionStatusManager().getAdmissionStatus(pStudentId, pSemesterId);
      if(admissionStatus.isPresent() && admissionStatus.get().isAdmitted()) {
        return UGSemesterFeeResponse.ADMITTED;
      }
      return !withInAdmissionSlot(pSemesterId) ? UGSemesterFeeResponse.NOT_WITHIN_SLOT : UGSemesterFeeResponse.ALLOWED;
    }
  }

  @Override
  public UGSemesterFeeResponse getInstallmentStatus(String pStudentId, Integer pSemesterId) {
    List<InstallmentStatus> installmentStatuses =
        getInstallmentStatusManager().getInstallmentStatus(pStudentId, pSemesterId);
    if(installmentStatuses != null) {
      if(installmentStatuses.size() > 1 && installmentStatuses.get(1).isPaymentCompleted()) {
        return UGSemesterFeeResponse.ADMITTED;
      }
      else if(installmentStatuses.size() > 0 && installmentStatuses.get(0).isPaymentCompleted()) {
        return UGSemesterFeeResponse.FIRST_INSTALLMENT_PAID;
      }
    }
    return UGSemesterFeeResponse.INSTALLMENT_NOT_TAKEN;
  }

  @Override
  public UGSemesterFeeResponse pay(String pStudentId, Integer pSemesterId) {
    // TODO: Validate payment request
    return !withInAdmissionSlot(pSemesterId) ? UGSemesterFeeResponse.NOT_WITHIN_SLOT : payFee(
        getFee(pStudentId, pSemesterId), pStudentId, pSemesterId);
  }

  @Override
  public UGSemesterFeeResponse payFirstInstallment(String pStudentId, Integer pSemesterId) {
    // TODO: Validate payment request
    return !withinFirstInstallmentSlot(pSemesterId) ? UGSemesterFeeResponse.NOT_WITHIN_SLOT : payFee(
        firstInstallment(pStudentId, pSemesterId), pStudentId, pSemesterId);
  }

  @Override
  public UGSemesterFeeResponse paySecondInstallment(String pStudentId, Integer pSemesterId) {
    // TODO: Validate payment request
    return !withinSecondInstallmentSlot(pSemesterId) ? UGSemesterFeeResponse.NOT_WITHIN_SLOT : payFee(
        secondInstallment(pStudentId, pSemesterId), pStudentId, pSemesterId);
  }

  @Transactional
  private UGSemesterFeeResponse payFee(UGFees fees, String pStudentId, Integer pSemesterId) {
    Optional<UGLateFee> lateFee = fees.getUGLateFee();
    Date transactionValidTill =
        lateFee.isPresent() ? lateFee.get().getTo() : getTransactionValidTill(
            Parameter.ParameterName.REGUALR_ADMISSION, pSemesterId);
    List<MutableStudentPayment> payments = new ArrayList<>();
    for(UGFee fee : fees.getUGFees()) {
      payments.add(createPayment(fee, pStudentId, pSemesterId, transactionValidTill));
    }
    if(lateFee.isPresent()) {
      payments.add(createPayment(lateFee.get(), pStudentId, pSemesterId, transactionValidTill));
    }
    getStudentPaymentManager().create(payments);
    return UGSemesterFeeResponse.APPLIED;
  }

  private MutableStudentPayment createPayment(UGFee fee, String pStudentId, Integer pSemesterId, Date pValidTill) {
    MutableStudentPayment payment = new PersistentStudentPayment();
    payment.setFeeCategoryId(fee.getFeeCategoryId());
    payment.setStudentId(pStudentId);
    payment.setSemesterId(pSemesterId);
    payment.setAmount(fee.getAmount());
    payment.setTransactionValidTill(pValidTill);
    return payment;
  }

  private MutableStudentPayment createPayment(UGLateFee fee, String pStudentId, Integer pSemesterId, Date pValidTill) {
    MutableStudentPayment payment = new PersistentStudentPayment();
    payment.setFeeCategoryId(FeeCategory.Categories.LATE_FEE.toString());
    payment.setStudentId(pStudentId);
    payment.setSemesterId(pSemesterId);
    payment.setAmount(fee.getFee());
    payment.setTransactionValidTill(pValidTill);
    return payment;
  }

  private Date getTransactionValidTill(Parameter.ParameterName pParameterName, Integer pSemesterId) {
    ParameterSetting parameterSetting =
        getParameterSettingManager().getByParameterAndSemesterId(pParameterName.getLabel(), pSemesterId);
    return parameterSetting.getEndDate();
  }

  abstract ParameterSettingManager getParameterSettingManager();

  abstract UGLateFeeManager getUGLateFeeManager();

  abstract StudentRecordManager getStudentRecordManager();

  abstract InstallmentSettingsManager getInstallmentSettingsManager();

  abstract StudentPaymentManager getStudentPaymentManager();

  abstract FeeTypeManager getFeeTypeManager();

  abstract SemesterAdmissionStatusManager getSemesterAdmissionStatusManager();

  abstract InstallmentStatusManager getInstallmentStatusManager();
}
