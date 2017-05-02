package org.ums.fee.semesterfee;

import org.ums.manager.ContentManager;

public interface SemesterAdmissionStatusManager extends
    ContentManager<SemesterAdmissionStatus, MutableSemesterAdmissionStatus, Long> {
  SemesterAdmissionStatus getAdmissionStatus(String pStudentId, Integer pSemesterId);
}
