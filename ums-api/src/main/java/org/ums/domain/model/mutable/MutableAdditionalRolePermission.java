package org.ums.domain.model.mutable;

import org.ums.domain.model.common.Mutable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.readOnly.AdditionalRolePermissions;
import org.ums.domain.model.readOnly.Permission;
import org.ums.domain.model.readOnly.Role;
import org.ums.domain.model.readOnly.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface MutableAdditionalRolePermission extends AdditionalRolePermissions, Mutable, MutableLastModifier, MutableIdentifier<Integer> {
  void setUser(final User pUser);

  void setUserId(final String pUserId);

  void setRole(final Role pRole);

  void setPermission(final Set<String> pPermission);

  void setRoleId(final Integer pRoleId);

  void setValidFrom(final Date pFromDate);

  void setValidTo(final Date pToDate);

  void setActive(final boolean pActive);
}
