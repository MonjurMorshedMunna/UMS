package org.ums.persistent.model.registrar;

import org.springframework.context.ApplicationContext;
import org.ums.context.AppContext;
import org.ums.domain.model.immutable.Department;
import org.ums.domain.model.immutable.Designation;
import org.ums.domain.model.immutable.EmploymentType;
import org.ums.domain.model.immutable.registrar.ServiceInformation;
import org.ums.domain.model.mutable.registrar.MutableServiceInformation;
import org.ums.manager.registrar.ServiceInformationManager;

import java.util.Date;

public class PersistentServiceInformation implements MutableServiceInformation {

  private static ServiceInformationManager sServiceInformationManager;

  static {
    ApplicationContext applicationContext = AppContext.getApplicationContext();
    sServiceInformationManager =
        applicationContext.getBean("serviceInformationManager", ServiceInformationManager.class);
  }

  private int mId;
  private String mEmployeeId;
  private Department mDepartment;
  private String mDepartmentId;
  private Designation mDesignation;
  private int mDesignationId;
  private EmploymentType mEmployment;
  private int mEmploymentId;
  private Date mJoiningDate;
  private Date mResignDate;
  private String mRoomNo;
  private String mExtNo;
  private String mAcademicInitial;
  private int mCurrentStatus;
  private String mLastModified;

  public PersistentServiceInformation() {}

  public PersistentServiceInformation(PersistentServiceInformation pPersistentServiceInformation) {
    mId = pPersistentServiceInformation.getId();
    mEmployeeId = pPersistentServiceInformation.getEmployeeId();
    mDepartment = pPersistentServiceInformation.getDepartment();
    mDepartmentId = pPersistentServiceInformation.getDepartmentId();
    mDesignation = pPersistentServiceInformation.getDesignation();
    mDesignationId = pPersistentServiceInformation.getDesignationId();
    mEmployment = pPersistentServiceInformation.getEmployment();
    mEmploymentId = pPersistentServiceInformation.getEmploymentId();
    mJoiningDate = pPersistentServiceInformation.getJoiningDate();
    mResignDate = pPersistentServiceInformation.getResignDate();
    mRoomNo = pPersistentServiceInformation.getRoomNo();
    mExtNo = pPersistentServiceInformation.getExtNo();
    mAcademicInitial = pPersistentServiceInformation.getAcademicInitial();
    mCurrentStatus = pPersistentServiceInformation.getCurrentStatus();
    mLastModified = pPersistentServiceInformation.getLastModified();
  }

  @Override
  public MutableServiceInformation edit() {
    return new PersistentServiceInformation(this);
  }

  @Override
  public Integer create() {
    return sServiceInformationManager.create(this);
  }

  @Override
  public void update() {
    sServiceInformationManager.update(this);
  }

  @Override
  public void delete() {
    sServiceInformationManager.delete(this);
  }

  @Override
  public String getLastModified() {
    return mLastModified;
  }

  @Override
  public Integer getId() {
    return mId;
  }

  @Override
  public void setId(Integer pId) {
    mId = pId;
  }

  @Override
  public void setLastModified(String pLastModified) {
    mLastModified = pLastModified;
  }

  @Override
  public void setEmployeeId(String pEmployeeId) {
    mEmployeeId = pEmployeeId;
  }

  @Override
  public void setDepartment(Department pDepartment) {
    mDepartment = pDepartment;
  }

  @Override
  public void setDepartmentId(String pDepartmentId) {
    mDepartmentId = pDepartmentId;
  }

  @Override
  public void setDesignation(Designation pDesignation) {
    mDesignation = pDesignation;
  }

  @Override
  public void setDesignationId(int pDesignationId) {
    mDesignationId = pDesignationId;
  }

  @Override
  public void setEmployment(EmploymentType pEmployment) {
    mEmployment = pEmployment;
  }

  @Override
  public void setEmploymentId(int pEmploymentId) {
    mEmploymentId = pEmploymentId;
  }

  @Override
  public void setJoiningDate(Date pJoiningDate) {
    mJoiningDate = pJoiningDate;
  }

  @Override
  public void setResignDate(Date pResignDate) {
    mResignDate = pResignDate;
  }

  @Override
  public void setRoomNo(String pRoomNo) {
    mRoomNo = pRoomNo;
  }

  @Override
  public void setExtNo(String pExtNo) {
    mExtNo = pExtNo;
  }

  @Override
  public void setAcademicInitial(String pAcademicInitial) {
    mAcademicInitial = pAcademicInitial;
  }

  @Override
  public void setCurrentStatus(int pCurrentStatus) {
    mCurrentStatus = pCurrentStatus;
  }

  @Override
  public String getEmployeeId() {
    return mEmployeeId;
  }

  @Override
  public Department getDepartment() {
    return mDepartment;
  }

  @Override
  public String getDepartmentId() {
    return mDepartmentId;
  }

  @Override
  public Designation getDesignation() {
    return mDesignation;
  }

  @Override
  public int getDesignationId() {
    return mDesignationId;
  }

  @Override
  public EmploymentType getEmployment() {
    return mEmployment;
  }

  @Override
  public int getEmploymentId() {
    return mEmploymentId;
  }

  @Override
  public Date getJoiningDate() {
    return mJoiningDate;
  }

  @Override
  public Date getResignDate() {
    return mResignDate;
  }

  @Override
  public String getRoomNo() {
    return mRoomNo;
  }

  @Override
  public String getExtNo() {
    return mExtNo;
  }

  @Override
  public String getAcademicInitial() {
    return mAcademicInitial;
  }

  @Override
  public int getCurrentStatus() {
    return mCurrentStatus;
  }
}
