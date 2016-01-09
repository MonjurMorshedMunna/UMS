package org.ums.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.ums.domain.model.mutable.MutableUser;
import org.ums.domain.model.regular.User;
import org.ums.manager.ContentManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class NewUserFilter extends AuthorizationFilter {

  @Autowired
  @Qualifier("userManager")
  private ContentManager<User, MutableUser, String> mUserManager;

  private String mChangePasswordUrl;

  protected PatternMatcher mPathMatcher = new AntPathMatcher();

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

    if (isLoginRequest(request, response)) {
      return true;
    }

    Subject subject = SecurityUtils.getSubject();
    String userName = subject.getPrincipal().toString();
    try {
      User user = mUserManager.get(userName);
      if (user.getTemporaryPassword() != null) {
        if (!isChangePasswordRequest(request)) {
          return false;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  protected boolean isChangePasswordRequest(ServletRequest pRequest) {
    return mPathMatcher.matches(mChangePasswordUrl, getPathWithinApplication(pRequest));
  }

  protected String getPathWithinApplication(ServletRequest request) {
    return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
  }

  public void setChangePasswordUrl(String pChangePasswordUrl) {
    mChangePasswordUrl = pChangePasswordUrl;
  }
}
