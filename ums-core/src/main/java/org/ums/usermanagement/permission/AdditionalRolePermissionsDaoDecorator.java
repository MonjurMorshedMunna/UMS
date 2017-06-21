package org.ums.usermanagement.permission;

import org.ums.decorator.ContentDaoDecorator;
import org.ums.usermanagement.role.Role;
import org.ums.usermanagement.user.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class AdditionalRolePermissionsDaoDecorator
    extends
    ContentDaoDecorator<AdditionalRolePermissions, MutableAdditionalRolePermissions, Long, AdditionalRolePermissionsManager>
    implements AdditionalRolePermissionsManager {
  @Override
  public List<AdditionalRolePermissions> getPermissionsByUser(String pUserId) {
    return getManager().getPermissionsByUser(pUserId);
  }

  @Override
  public List<AdditionalRolePermissions> getUserPermissionsByAssignedUser(String pUserId, String pAssignedBy) {
    return getManager().getUserPermissionsByAssignedUser(pUserId, pAssignedBy);
  }

  @Override
  public int addRole(String pUserId, Role pRole, User pAssignedBy, Date pFromDate, Date pToDate) {
    return getManager().addRole(pUserId, pRole, pAssignedBy, pFromDate, pToDate);
  }

  @Override
  public int addPermissions(String pUserId, Set<String> pPermissions, User pAssignedBy, Date pFromDate, Date pToDate) {
    return getManager().addPermissions(pUserId, pPermissions, pAssignedBy, pFromDate, pToDate);
  }

  @Override
  public int removeExistingAdditionalRolePermissions(String pUserId, String pAssignedBy) {
    return getManager().removeExistingAdditionalRolePermissions(pUserId, pAssignedBy);
  }

  @Override
  public List<AdditionalRolePermissions> getAdditionalRole(String pDepartmentId) {
    return getManager().getAdditionalRole(pDepartmentId);
  }
}