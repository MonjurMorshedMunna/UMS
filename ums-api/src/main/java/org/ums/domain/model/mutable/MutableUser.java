package org.ums.domain.model.mutable;

import org.ums.domain.model.common.Mutable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.Department;
import org.ums.domain.model.immutable.Role;
import org.ums.domain.model.immutable.User;

import java.util.List;

import java.util.Date;

public interface MutableUser extends User, Mutable, MutableIdentifier<String>, MutableLastModifier {
  void setPassword(final char[] pPassword);

  void setEmployeeId(final String pEmployeeId);

  void setTemporaryPassword(final char[] pPassword);

  void setRoleIds(final List<Integer> pRoleIds);

  void setRoles(final List<Role> pRoles);

  void setActive(final boolean pActive);

  void setPasswordResetToken(final String pPasswordResetToken);

  void setPasswordTokenGenerateDateTime(final Date pDateTime);

  void setPrimaryRoleId(final Integer pRoleId);

  void setPrimaryRole(final Role pPrimaryRole);

  void setAdditionalPermissions(List<String> pAdditionalPermissions);

  void setDepartment(Department pDepartment);

  void setDepartmentId(String pDepartmentId);

  void setName(String pName);
}
