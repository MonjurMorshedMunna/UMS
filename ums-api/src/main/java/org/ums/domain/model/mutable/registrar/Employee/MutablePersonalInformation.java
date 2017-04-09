package org.ums.domain.model.mutable.registrar.Employee;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.registrar.Employee.PersonalInformation;
import org.ums.domain.model.mutable.MutableLastModifier;

import java.util.Date;

public interface MutablePersonalInformation extends PersonalInformation, Editable<Integer>, MutableIdentifier<Integer>,
    MutableLastModifier {

  void setEmployeeId(final int pEmployeeId);

  void setFirstName(final String pFirstName);

  void setLastName(final String pLastName);

  void setGender(final String pGender);

  void setBloodGroup(final String pBloodGroup);

  void setFatherName(final String pFatherName);

  void setMotherName(final String pMotherName);

  void setNationality(final String pNationality);

  void setReligion(final String pReligion);

  void setDateOfBirth(final String pDateOfBirth);

  void setNationalId(final int pNationalId);

  void setMaritalStatus(final int pMaritalStatus);

  void setSpouseName(final String pSpouseName);

  void setSpouseNationalId(final int pSpouseNationalId);

  void setWebsite(final String pWebsite);

  void setOrganizationalEmail(final String pOrganizationEmail);

  void setPersonalEmail(final String pPersonalEmail);

  void setMobileNumber(final String pMobileNumber);

  void setPhoneNumber(final String pPhoneNumber);

  void setPresentAddressHouse(final String pPresentAddressHouse);

  void setPresentAddressRoad(final String pPresentAddressRoad);

  void setPresentAddressThana(final String pPresentAddressThana);

  void setPresentAddressDistrict(final String pPresentAddressDistrict);

  void setPresentAddressZip(final String pPresentAddressZip);

  void setPresentAddressDivision(final String pPresentAddressDivision);

  void setPresentAddressCountry(final String pPresentAddressHouseCountry);

  void setPermanentAddressHouse(final String pPermanentAddressHouse);

  void setPermanentAddressRoad(final String pPermanentAddressRoad);

  void setPermanentAddressThana(final String pPermanentAddressThana);

  void setPermanentAddressDistrict(final String pPermanentAddressDistrict);

  void setPermanentAddressZip(final String pPermanentAddressZip);

  void setPermanentAddressDivision(final String pPermanentAddressDivision);

  void setPermanentAddressCountry(final String pPermanentAddressCountry);

  void setEmergencyContactName(final String pEmergencyContactName);

  void setEmergencyContactRelation(final String pEmergencyContactRelation);

  void setEmergencyContactPhone(final String pEmergencyContactPhone);

  void setEmergencyContactAddress(final String pEmergencyContactAddress);
}
