package org.ums.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.ums.usermanagement.user.User;

public class NewUserRealm extends UserRealm {
  private static final String LOGIN_AS_SEPARATOR = ">";

  @Override
  public boolean supports(AuthenticationToken token) {
    if(super.supports(token) && !token.getPrincipal().toString().contains(LOGIN_AS_SEPARATOR)) {
      User user = getUser(token.getPrincipal().toString());
      return user.getPassword() == null && user.getTemporaryPassword() != null;
    }
    return false;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    User user = getUser(token.getPrincipal().toString());
    return new SimpleAuthenticationInfo(user.getId(), user.getTemporaryPassword(), getName());
  }
}
