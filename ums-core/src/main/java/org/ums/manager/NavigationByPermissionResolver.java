package org.ums.manager;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.realm.AuthorizingRealm;
import org.ums.decorator.NavigationDaoDecorator;
import org.ums.domain.model.immutable.Navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NavigationByPermissionResolver extends NavigationDaoDecorator {
  private AuthorizingRealm mAuthorizingRealm;

  public NavigationByPermissionResolver(final AuthorizingRealm pAuthorizingRealm) {
    mAuthorizingRealm = pAuthorizingRealm;
  }

  @Override
  public List<Navigation> getByPermissions(Set<String> pPermissions) {
    List<Navigation> navigationList = getManager().getAll();
    List<Navigation> permittedNavigation = null;

    PermissionResolver permissionResolver = mAuthorizingRealm.getPermissionResolver();

    if(navigationList != null && navigationList.size() > 0) {
      permittedNavigation = new ArrayList<>();

      if(pPermissions != null) {
        for(String permissionString : pPermissions) {
          Permission permission = permissionResolver.resolvePermission(permissionString);

          for(Navigation navigation : navigationList) {
            String navigationPermissionString = navigation.getPermission();
            Permission navigationPermission =
                permissionResolver.resolvePermission(navigationPermissionString);
            if(permission.implies(navigationPermission)) {
              permittedNavigation.add(navigation);
            }
          }
        }
      }
    }
    return permittedNavigation;
  }

  @Override
  public List<Navigation> getByPermissionsId(Set<Integer> pPermissionIds) {
    return getManager().getByPermissionsId(pPermissionIds);
  }
}
