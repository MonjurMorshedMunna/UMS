package org.ums.security.bearertoken;

import com.google.common.collect.Sets;
import org.apache.commons.lang.Validate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.ums.domain.model.immutable.BearerAccessToken;
import org.ums.domain.model.immutable.Permission;
import org.ums.domain.model.immutable.User;
import org.ums.domain.model.mutable.MutableUser;
import org.ums.manager.BearerAccessTokenManager;
import org.ums.manager.ContentManager;
import org.ums.manager.PermissionManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BearerTokenAuthenticatingRealm extends AuthorizingRealm {
  @Autowired
  private BearerAccessTokenManager mBearerAccessTokenManager;
  @Autowired
  private PermissionManager mPermissionManager;
  @Autowired
  private ContentManager<User, MutableUser, String> mUserManager;

  private class BearerAuthenticationInfo implements AuthenticationInfo {
    private final BearerAccessToken token;

    BearerAuthenticationInfo(BearerAccessToken token) {
      this.token = token;
    }

    @Override
    public Object getCredentials() {
      return token.getId();
    }

    @Override
    public PrincipalCollection getPrincipals() {
      RealmSecurityManager manager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
      SimplePrincipalCollection ret = new SimplePrincipalCollection();
      try {
        for (Realm realm : manager.getRealms()) {
          if (realm instanceof ProfileRealm) {
            String userId = token.getUserId();
            if (((ProfileRealm) realm).accountExists(userId)) {
              ret.add(userId, realm.getName());
            }
          }
        }
        ret.add(token.getId(), getName());
      } catch (Exception e) {
        e.printStackTrace();
      }

      return ret;
    }

  }

  public BearerTokenAuthenticatingRealm() {
    super(new AllowAllCredentialsMatcher());
    setAuthenticationTokenClass(BearerToken.class);
  }

  private static boolean tokenIsInvalid(BearerToken token, BearerAccessToken dbToken) {
    return token == null || dbToken == null || !dbToken.getUserId().equals(token.getPrincipal());
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken pAuthenticationToken) throws AuthenticationException {
    BearerToken token = (BearerToken) pAuthenticationToken;
    String userId = (String) token.getPrincipal();
    String credentials = (String) token.getCredentials();

    Validate.notNull(userId, "UserId can't be null");
    Validate.notNull(token, "Token can't be null");
    BearerAccessToken dbToken;
    try {
      dbToken = mBearerAccessTokenManager.get(credentials);
      if (tokenIsInvalid(token, dbToken)) {
        throw new AuthenticationException("Access denied. Invalid access token");
      }
    } catch (Exception e) {
      throw new AuthenticationException("Not able to find provided bearer access token");
    }

    return new BearerAuthenticationInfo(dbToken);
  }

  //TODO: Move this method to a common place so both the realm can use same authorization base
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthorizationException {
    //null usernames are invalid
    if (principals == null) {
      throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
    }
    SimpleAuthorizationInfo info = null;
    String token = (String) getAvailablePrincipal(principals);
    try {
      BearerAccessToken bearerAccessToken = mBearerAccessTokenManager.get(token);
      User user = mUserManager.get(bearerAccessToken.getUserId());
      info = new SimpleAuthorizationInfo(Sets.newHashSet(user.getPrimaryRole().getName()));
      List<Permission> rolePermissions = mPermissionManager.getPermissionByRole(user.getPrimaryRole());

      Set<String> permissions = new HashSet<>();

      for (Permission permission : rolePermissions) {
        permissions.addAll(permission.getPermissions());
      }

      info.setStringPermissions(permissions);
    } catch (Exception e) {
      throw new AuthorizationException("Invalid access token", e);
    }
    return info;
  }

  @Override
  public void onLogout(PrincipalCollection principals) {
    super.onLogout(principals);
    deleteTokens(principals);
  }

  @SuppressWarnings("unchecked")
  private void deleteTokens(PrincipalCollection principals) {
    Collection<String> tokens = principals.fromRealm(getName());
    if (tokens != null) {
      for (String token : tokens) {
        mBearerAccessTokenManager.deleteToken(token);
      }
    }
  }

}
