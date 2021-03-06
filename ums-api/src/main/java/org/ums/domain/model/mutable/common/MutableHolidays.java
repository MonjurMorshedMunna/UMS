package org.ums.domain.model.mutable.common;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.common.Holidays;
import org.ums.domain.model.mutable.MutableLastModifier;

import java.util.Date;

/**
 * Created by Monjur-E-Morshed on 15-Jun-17.
 */
public interface MutableHolidays extends Holidays, Editable<Long>, MutableLastModifier, MutableIdentifier<Long> {

  void setHolidayTypeId(Long pHolidayTypeId);

  void setYear(int pYear);

  void setFromDate(Date pFromDate);

  void setToDate(Date pToDate);

  void setEnableStatus(Holidays.HolidayEnableStatus pEnableStatus);

}
