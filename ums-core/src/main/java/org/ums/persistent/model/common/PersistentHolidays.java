package org.ums.persistent.model.common;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.immutable.common.HolidayType;
import org.ums.domain.model.immutable.common.Holidays;
import org.ums.domain.model.mutable.common.MutableHolidays;
import org.ums.manager.common.HolidayTypeManager;
import org.ums.manager.common.HolidaysManager;

import java.util.Date;

/**
 * Created by Monjur-E-Morshed on 15-Jun-17.
 */
public class PersistentHolidays implements MutableHolidays {

  private static HolidayTypeManager sHolidayTypeManager;
  private static HolidaysManager sHolidaysManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sHolidayTypeManager = applicationContext.getBean("holidayTypeManager", HolidayTypeManager.class);
    sHolidaysManager = applicationContext.getBean("holidaysManager", HolidaysManager.class);
  }

  private Long mId;
  private Long mHolidayTypeId;
  private HolidayType mHolidayType;
  private int mYear;
  private Date mFromDate;
  private Date mToDate;
  private Holidays.HolidayEnableStatus mHolidayEnableStatus;
  private String mLastModified;

  public PersistentHolidays() {

  }

  public PersistentHolidays(final PersistentHolidays pPersistentHolidays) {
    mId = pPersistentHolidays.getId();
    mHolidayType = pPersistentHolidays.getHolidayType();
    mYear = pPersistentHolidays.getYear();
    mFromDate = pPersistentHolidays.getFromDate();
    mToDate = pPersistentHolidays.getToDate();
    mHolidayEnableStatus = pPersistentHolidays.getEnableStatus();
    mLastModified = pPersistentHolidays.getLastModified();
  }

  @Override
  public void setEnableStatus(HolidayEnableStatus pEnableStatus) {
    mHolidayEnableStatus = pEnableStatus;
  }

  @Override
  public HolidayEnableStatus getEnableStatus() {
    return mHolidayEnableStatus;
  }

  @Override
  public Long create() {
    return sHolidaysManager.create(this);
  }

  @Override
  public MutableHolidays edit() {
    return new PersistentHolidays(this);
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public Long getId() {
    return mId;
  }

  @Override
  public void setId(Long pId) {
    mId = pId;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }

  @Override
  public void update() {
    sHolidaysManager.update(this);
  }

  @Override
  public void delete() {
    sHolidaysManager.delete(this);
  }

  @Override
  public void setHolidayTypeId(Long pHolidayTypeId) {
    mHolidayTypeId = pHolidayTypeId;
  }

  @Override
  public HolidayType getHolidayType() {
    return mHolidayType == null ? sHolidayTypeManager.get(mHolidayTypeId) : sHolidayTypeManager.validate(mHolidayType);
  }

  @Override
  public int getYear() {
    return mYear;
  }

  @Override
  public void setYear(int pYear) {
    mYear = pYear;
  }

  @Override
  public Date getFromDate() {
    return mFromDate;
  }

  @Override
  public void setFromDate(Date pFromDate) {
    mFromDate = pFromDate;
  }

  @Override
  public Date getToDate() {
    return mToDate;
  }

  @Override
  public void setToDate(Date pToDate) {
    mToDate = pToDate;
  }
}
