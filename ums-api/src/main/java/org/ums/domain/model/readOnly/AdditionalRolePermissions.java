package org.ums.domain.model.readOnly;


import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.MutableAdditionalRolePermission;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public interface AdditionalRolePermissions extends Serializable, Identifier<Integer>, LastModifier, EditType<MutableAdditionalRolePermission> {
  User getUser() throws Exception;

  String getUserId();

  Role getRole() throws Exception;

  Integer getRoleId();

  Set<String> getPermission() throws Exception;

  Date getValidFrom();

  Date getValidTo();

  boolean isActive();
}
