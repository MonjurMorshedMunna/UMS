package org.ums.academic.resource.fee.semesterfee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.domain.model.immutable.StudentRecord;
import org.ums.manager.StudentRecordManager;

@Component
class UGSemesterFeeFactory {
  @Autowired
  private UGRegularSemesterFee mUGRegularSemesterFee;

  @Autowired
  private UGReadmissionFee mUGReadmissionFee;

  @Autowired
  private StudentRecordManager mStudentRecordManager;

  UGSemesterFee getSemesterFeeType(String pStudentId, Integer pSemesterId) {
    return mStudentRecordManager.getStudentRecord(pStudentId, pSemesterId).getType() == StudentRecord.Type.READMISSION_REQUIRED ? mUGReadmissionFee
        : mUGRegularSemesterFee;
  }
}
