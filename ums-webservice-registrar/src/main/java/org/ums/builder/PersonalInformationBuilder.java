package org.ums.builder;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.registrar.PersonalInformation;
import org.ums.domain.model.mutable.registrar.MutablePersonalInformation;
import org.ums.manager.UserManager;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.ldap.PagedResultsControl;
import javax.ws.rs.core.UriInfo;

@Component
public class PersonalInformationBuilder implements Builder<PersonalInformation, MutablePersonalInformation> {

  @Autowired
  UserManager userManager;

  @Override
  public void build(JsonObjectBuilder pBuilder, PersonalInformation pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) {
    pBuilder.add("employeeId", pReadOnly.getEmployeeId());
    pBuilder.add("firstName", pReadOnly.getFirstName());
    pBuilder.add("lastName", pReadOnly.getLastName());
    pBuilder.add("fatherName", pReadOnly.getFatherName());
    pBuilder.add("motherName", pReadOnly.getMotherName());
    pBuilder.add("gender", pReadOnly.getGender());
    pBuilder.add("birthday", pReadOnly.getDateOfBirth());
    pBuilder.add("nationality", pReadOnly.getNationality());
    pBuilder.add("religion", pReadOnly.getReligion());
    pBuilder.add("maritalStatus", pReadOnly.getMaritalStatus());
    if(pReadOnly.getSpouseName() != null) {
      pBuilder.add("spouseName", pReadOnly.getSpouseName());
    }
    else {
      pBuilder.add("spouseName", "");
    }
    if(pReadOnly.getNationalId() != null) {
      pBuilder.add("nationalIdNo", pReadOnly.getNationalId());
    }
    else {
      pBuilder.add("nationalIdNo", "");
    }
    pBuilder.add("bloodGroup", pReadOnly.getBloodGroup());
    if(pReadOnly.getSpouseNationalId() != null) {
      pBuilder.add("spouseNationalIdNo", pReadOnly.getSpouseNationalId());
    }
    else {
      pBuilder.add("spouseNationalIdNo", "");
    }
    if(pReadOnly.getWebsite() != null) {
      pBuilder.add("website", pReadOnly.getWebsite());
    }
    else {
      pBuilder.add("website", "");
    }
    if(pReadOnly.getOrganizationalEmail() != null) {
      pBuilder.add("organizationalEmail", pReadOnly.getOrganizationalEmail());
    }
    else {
      pBuilder.add("organizationalEmail", "");
    }
    pBuilder.add("personalEmail", pReadOnly.getPersonalEmail());
    pBuilder.add("mobile", pReadOnly.getMobileNumber());
    if(pReadOnly.getPhoneNumber() != null) {
      pBuilder.add("phone", pReadOnly.getPhoneNumber());
    }
    else {
      pBuilder.add("phone", "");
    }
    pBuilder.add("presentAddressHouse", pReadOnly.getPresentAddressHouse());
    if(pReadOnly.getPresentAddressRoad() != null) {
      pBuilder.add("presentAddressRoad", pReadOnly.getPresentAddressRoad());
    }
    else {
      pBuilder.add("presentAddressRoad", "");
    }
    if(pReadOnly.getPresentAddressThana() != null) {
      pBuilder.add("presentAddressPoliceStation", pReadOnly.getPresentAddressThana());
    }
    else {
      pBuilder.add("presentAddressPoliceStation", "");
    }
    if(pReadOnly.getPresentAddressZip() != null) {
      pBuilder.add("presentAddressPostalCode", pReadOnly.getPresentAddressZip());
    }
    else {
      pBuilder.add("presentAddressPostalCode", "");
    }
    if(pReadOnly.getPresentAddressDistrict() != null) {
      pBuilder.add("presentAddressDistrict", pReadOnly.getPresentAddressDistrict());
    }
    else {
      pBuilder.add("presentAddressDistrict", "");
    }
    if(pReadOnly.getPresentAddressDivision() != null) {
      pBuilder.add("presentAddressDivision", pReadOnly.getPresentAddressDivision());
    }
    else {
      pBuilder.add("presentAddressDivision", "");
    }
    pBuilder.add("presentAddressCountry", pReadOnly.getPresentAddressCountry());
    pBuilder.add("permanentAddressHouse", pReadOnly.getPermanentAddressHouse());
    if(pReadOnly.getPermanentAddressRoad() != null) {
      pBuilder.add("permanentAddressRoad", pReadOnly.getPermanentAddressRoad());
    }
    else {
      pBuilder.add("permanentAddressRoad", "");
    }
    if(pReadOnly.getPresentAddressThana() != null) {
      pBuilder.add("permanentAddressPoliceStation", pReadOnly.getPermanentAddressThana());
    }
    else {
      pBuilder.add("permanentAddressPoliceStation", "");
    }
    if(pReadOnly.getPermanentAddressZip() != null) {
      pBuilder.add("permanentAddressPostalCode", pReadOnly.getPermanentAddressZip());
    }
    else {
      pBuilder.add("permanentAddressPostalCode", "");
    }
    if(pReadOnly.getPermanentAddressDistrict() != null) {
      pBuilder.add("permanentAddressDistrict", pReadOnly.getPermanentAddressDistrict());
    }
    else {
      pBuilder.add("permanentAddressDistrict", "");
    }
    if(pReadOnly.getPermanentAddressDivision() != null) {
      pBuilder.add("permanentAddressDivision", pReadOnly.getPermanentAddressDivision());
    }
    else {
      pBuilder.add("permanentAddressDivision", "");
    }
    pBuilder.add("permanentAddressCountry", pReadOnly.getPermanentAddressCountry());
    pBuilder.add("emergencyContactName", pReadOnly.getEmergencyContactName());
    pBuilder.add("emergencyContactRelation", pReadOnly.getEmergencyContactRelation());
    pBuilder.add("emergencyContactPhone", pReadOnly.getEmergencyContactPhone());
    if(pReadOnly.getEmergencyContactAddress() != null) {
      pBuilder.add("emergencyContactAddress", pReadOnly.getEmergencyContactAddress());
    }
    else {
      pBuilder.add("emergencyContactAddress", "");
    }
  }

  @Override
  public void build(MutablePersonalInformation pMutable, JsonObject pJsonObject, LocalCache pLocalCache) {

    pMutable.setEmployeeId(userManager.get(SecurityUtils.getSubject().getPrincipal().toString()).getEmployeeId());
    pMutable.setFirstName(pJsonObject.getString("firstName"));
    pMutable.setLastName(pJsonObject.getString("lastName"));
    pMutable.setFatherName(pJsonObject.getString("fatherName"));
    pMutable.setMotherName(pJsonObject.getString("motherName"));
    pMutable.setGender(pJsonObject.getJsonObject("gender").getString("id"));
    pMutable.setDateOfBirth(pJsonObject.getString("birthday"));
    pMutable.setNationality(pJsonObject.getJsonObject("nationality").getString("name"));
    pMutable.setReligion(pJsonObject.getJsonObject("religion").getString("name"));
    pMutable.setMaritalStatus(pJsonObject.getJsonObject("maritalStatus").getString("name"));
    if(!pJsonObject.containsKey("spouseName")) {
      pMutable.setSpouseName("");
    }
    else {
      pMutable.setSpouseName(pJsonObject.getString("spouseName"));
    }
    if(!pJsonObject.containsKey("nationalId")) {
      pMutable.setNationalId("");
    }
    else {
      pMutable.setNationalId(pJsonObject.getString("nationalIdNo"));
    }
    pMutable.setBloodGroup(pJsonObject.getJsonObject("bloodGroup").getString("name"));
    if(!pJsonObject.containsKey("spouseNationalIdNo")) {
      pMutable.setSpouseNationalId("");
    }
    else {
      pMutable.setSpouseNationalId(pJsonObject.getString("spouseNationalIdNo"));
    }
    if(!pJsonObject.containsKey("website")) {
      pMutable.setWebsite("");
    }
    else {
      pMutable.setWebsite(pJsonObject.getString("website"));
    }
    if(!pJsonObject.containsKey("organizationalEmail")) {
      pMutable.setOrganizationalEmail("");
    }
    else {
      pMutable.setOrganizationalEmail(pJsonObject.getString("organizationalEmail"));
    }
    pMutable.setPersonalEmail(pJsonObject.getString("personalEmail"));
    pMutable.setMobileNumber(pJsonObject.getString("mobile"));
    if(!pJsonObject.containsKey("phone")) {
      pMutable.setPhoneNumber("");
    }
    else {
      pMutable.setPhoneNumber(pJsonObject.getString("phone"));
    }
    pMutable.setPresentAddressHouse(pJsonObject.getString("presentAddressHouse"));
    if(!pJsonObject.containsKey("presentAddressRoad")) {
      pMutable.setPresentAddressRoad("");
    }
    else {
      pMutable.setPresentAddressRoad(pJsonObject.getString("presentAddressRoad"));
    }
    if(!pJsonObject.containsKey("presentAddressPostalCode")) {
      pMutable.setPresentAddressZip("");
    }
    else {
      pMutable.setPresentAddressZip(pJsonObject.getString("presentAddressPostalCode"));
    }
    if(pJsonObject.getJsonObject("presentAddressCountry").getString("name").equals("Bangladesh")) {
      pMutable.setPresentAddressThana(pJsonObject.getJsonObject("presentAddressPoliceStation").getString("name"));
      pMutable.setPresentAddressDistrict(pJsonObject.getJsonObject("presentAddressDistrict").getString("name"));
      pMutable.setPresentAddressDivision(pJsonObject.getJsonObject("presentAddressDivision").getString("name"));
    }
    else {
      pMutable.setPresentAddressThana("");
      pMutable.setPresentAddressDistrict("");
      pMutable.setPresentAddressDivision("");
    }
    pMutable.setPresentAddressCountry(pJsonObject.getJsonObject("presentAddressCountry").getString("name"));
    pMutable.setPermanentAddressHouse(pJsonObject.getString("permanentAddressHouse"));
    if(!pJsonObject.containsKey("permanentAddressRoad")) {
      pMutable.setPermanentAddressRoad("");
    }
    else {
      pMutable.setPermanentAddressRoad(pJsonObject.getString("permanentAddressRoad"));
    }
    if(!pJsonObject.containsKey("permanentAddressPostalCode")) {
      pMutable.setPermanentAddressZip("");
    }
    else {
      pMutable.setPermanentAddressZip(pJsonObject.getString("permanentAddressPostalCode"));
    }
    if(pJsonObject.getJsonObject("permanentAddressCountry").getString("name").equals("Bangladesh")) {
      pMutable.setPermanentAddressThana(pJsonObject.getJsonObject("permanentAddressPoliceStation").getString("name"));
      pMutable.setPermanentAddressDistrict(pJsonObject.getJsonObject("permanentAddressDistrict").getString("name"));
      pMutable.setPermanentAddressDivision(pJsonObject.getJsonObject("permanentAddressDivision").getString("name"));
    }
    else {
      pMutable.setPermanentAddressThana("");
      pMutable.setPermanentAddressDistrict("");
      pMutable.setPermanentAddressDivision("");
    }
    pMutable.setPermanentAddressCountry(pJsonObject.getJsonObject("permanentAddressCountry").getString("name"));
    pMutable.setEmergencyContactName(pJsonObject.getString("emergencyContactName"));
    pMutable.setEmergencyContactRelation(pJsonObject.getJsonObject("emergencyContactRelation").getString("name"));
    pMutable.setEmergencyContactPhone(pJsonObject.getString("emergencyContactPhone"));
    if(!pJsonObject.containsKey("emergencyContactAddress")) {
      pMutable.setEmergencyContactAddress("");
    }
    else {
      pMutable.setEmergencyContactAddress(pJsonObject.getString("emergencyContactAddress"));
    }
  }
}
