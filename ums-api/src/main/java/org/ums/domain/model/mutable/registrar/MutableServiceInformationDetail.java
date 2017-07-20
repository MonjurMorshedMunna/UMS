package org.ums.domain.model.mutable.registrar;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.registrar.ServiceInformationDetail;
import org.ums.domain.model.mutable.MutableLastModifier;
import org.ums.enums.common.EmploymentPeriod;

import java.util.Date;

public interface MutableServiceInformationDetail extends ServiceInformationDetail, Editable<Integer>,
    MutableIdentifier<Integer>, MutableLastModifier {

  void setEmploymentPeriod(final EmploymentPeriod pEmploymentPeriod);

  void setEmploymentPeriodId(final int pEmploymentPeriodId);

  void setStartDate(final Date pStartDate);

  void setEndDate(final Date pEndDate);

  void setServiceId(final Long pServiceId);
}
