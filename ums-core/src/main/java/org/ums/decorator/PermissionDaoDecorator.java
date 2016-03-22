package org.ums.decorator;


import org.ums.cache.ContentDaoDecorator;
import org.ums.domain.model.mutable.MutablePermission;
import org.ums.domain.model.immutable.Permission;
import org.ums.domain.model.immutable.Role;
import org.ums.manager.PermissionManager;

import java.util.List;

public class PermissionDaoDecorator extends ContentDaoDecorator<Permission, MutablePermission, Integer, PermissionManager> implements PermissionManager {
  @Override
  public List<Permission> getPermissionByRole(Role pRole) {
    return getManager().getPermissionByRole(pRole);
  }
}