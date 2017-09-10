package org.ums.filter;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.ums.domain.model.mutable.MutableBearerAccessToken;
import org.ums.persistent.model.PersistentBearerAccessToken;
import org.ums.security.TokenBuilder;

public class LoginFilter extends BasicHttpAuthenticationFilter {
  @Autowired
  private TokenBuilder mTokenBuilder;

  @Override
  protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
      ServletResponse response) throws Exception {
    String accessToken = mTokenBuilder.accessToken();
    String refreshToken = mTokenBuilder.refreshToken();
    persistToken(accessToken, refreshToken);

    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    out.print(String.format("{\"accessToken\": \"Bearer %s\", \"refreshToken\": \"Bearer %s\"}", accessToken, refreshToken));
    out.flush();
    return false;
  }

  @Override
  protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
    HttpServletResponse httpResponse = WebUtils.toHttp(response);
    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return false;
  }

  private void persistToken(String accessToken, String refreshToken) {
    MutableBearerAccessToken token = new PersistentBearerAccessToken();
    token.setUserId(SecurityUtils.getSubject().getPrincipal().toString());
    token.setId(accessToken);
    token.setRefreshToken(refreshToken);
    token.create();
  }
}
