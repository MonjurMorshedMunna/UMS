package org.ums.builder;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.Employee;
import org.ums.domain.model.mutable.MutableEmployee;
import org.ums.formatter.DateFormat;
import org.ums.persistent.model.PersistentDepartment;

@Component
public class EmployeeBuilder implements Builder<Employee, MutableEmployee> {
  @Autowired
  private DateFormat mDateFormat;

  @Override
  public void build(JsonObjectBuilder pBuilder, Employee pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) {
    pBuilder.add("id", pReadOnly.getId());
    pBuilder.add("employeeName", pReadOnly.getEmployeeName());
    pBuilder.add("designation", pReadOnly.getDesignation());
    pBuilder.add("employmentType", pReadOnly.getEmploymentType());
    pBuilder.add("deptOfficeId", pReadOnly.getDepartment().getId());
    pBuilder.add("fatherName", pReadOnly.getFatherName());
    pBuilder.add("motherName", pReadOnly.getMotherName());
    pBuilder.add("birthDate", mDateFormat.format(pReadOnly.getBirthDate()));
    pBuilder.add("gender", pReadOnly.getGender());
    pBuilder.add("bloodGroup", pReadOnly.getBloodGroup());
    pBuilder.add("presentAddress", pReadOnly.getPresentAddress());
    pBuilder.add("permanentAddress", pReadOnly.getPermanentAddress());
    pBuilder.add("mobileNumber", pReadOnly.getMobileNumber());
    pBuilder.add("phoneNumber", pReadOnly.getPhoneNumber());
    pBuilder.add("emailAddress", pReadOnly.getEmailAddress() == null ? "-" : pReadOnly.getEmailAddress());
    pBuilder.add("joiningDate", mDateFormat.format(pReadOnly.getJoiningDate()));
    pBuilder.add("jobPermanentDate",
        pReadOnly.getJobPermanentDate() == null ? "-" : mDateFormat.format(pReadOnly.getJobPermanentDate()));
    pBuilder.add("status", pReadOnly.getStatus());
    pBuilder.add("shortName", pReadOnly.getShortName());
    pBuilder.add("self",
        pUriInfo.getBaseUriBuilder().path("academic").path("semesterWithdraw").path(pReadOnly.getId().toString())
            .build().toString());
  }

  @Override
  public void build(MutableEmployee pMutable, JsonObject pJsonObject, LocalCache pLocalCache) {
    pMutable.setId(pJsonObject.getString("id"));
    pMutable.setEmployeeName(pJsonObject.getString("employeeName"));
    pMutable.setDesignation(pJsonObject.getInt("designation"));
    pMutable.setEmploymentType(pJsonObject.getString("employmentType"));
    PersistentDepartment dept = new PersistentDepartment();
    dept.setId(pJsonObject.getString("deptOfficeId"));
    pMutable.setDepartment(dept);
    pMutable.setFatherName(pJsonObject.getString("fatherName"));
    pMutable.setMotherName(pJsonObject.getString("motherName"));
    pMutable.setBirthDate(mDateFormat.parse(pJsonObject.getString("birthDate")));
    pMutable.setGender(pJsonObject.getString("gender"));
    pMutable.setBloodGroup(pJsonObject.getString("bloodGroup"));
    pMutable.setPresentAddress(pJsonObject.getString("presentAddress"));
    pMutable.setPermanentAddress(pJsonObject.getString("permanentAddress"));
    pMutable.setMobileNumber(pJsonObject.getString("mobileNumber"));
    pMutable.setPhoneNumber(pJsonObject.getString("phoneNumber"));
    pMutable.setEmailAddress(pJsonObject.getString("emailAddress"));
    pMutable.setJoiningDate(mDateFormat.parse(pJsonObject.getString("joiningDate")));
    pMutable.setJobParmanentDate(mDateFormat.parse(pJsonObject.getString("jobPermanentDate")));
    pMutable.setStatus(pJsonObject.getInt("status"));
    pMutable.setShortName(pJsonObject.getString("shortName"));
  }
}
