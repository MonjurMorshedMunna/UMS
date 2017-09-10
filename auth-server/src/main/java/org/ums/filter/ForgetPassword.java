package org.ums.filter;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.ums.domain.model.common.Mutable;
import org.ums.domain.model.immutable.Employee;
import org.ums.manager.EmployeeManager;
import org.ums.message.MessageResource;
import org.ums.services.EmailService;
import org.ums.token.TokenBuilder;
import org.ums.usermanagement.user.MutableUser;
import org.ums.usermanagement.user.User;
import org.ums.usermanagement.user.UserManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

public class ForgetPassword extends AbstractPathMatchingFilter {
  @Autowired
  private UserManager mUserManager;

  @Autowired
  private MessageResource mMessageResource;

  @Autowired
  TokenBuilder mTokenBuilder;

  @Autowired
  EmailService mEmailService;

  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    JSONObject jsonObject = getRequestJson((HttpServletRequest) request);
    Validate.notNull(jsonObject.get("recoverBy"), "Recover by is empty");
    String recoverBy = jsonObject.get("recoverBy").toString();
    User user = null;
    if (recoverBy.equals("userId")) {
      Validate.notNull(jsonObject.get("userId"), "User id is empty");
      String userId = jsonObject.get("userId").toString();
      if (!mUserManager.exists(userId)) {
        return sendError(mMessageResource.getMessage("user.not.exists"), response);
      }
      user = mUserManager.get(userId);
    }
    else if (recoverBy.equals("email")) {
      Validate.notNull(jsonObject.get("email"), "Email is empty");
      String email = jsonObject.get("email").toString();
      Optional<User> userOptional = mUserManager.getByEmail(email);
      if (!userOptional.isPresent()) {
        return sendError(mMessageResource.getMessage("email.not.exists"), response);
      }
      user = userOptional.get();
    }

    if(user != null) {
      MutableUser mutableUser = user.edit();
      String prToken = mTokenBuilder.passwordResetToken(user.getId());
      mutableUser.setPasswordResetToken(prToken);
      mutableUser.setPasswordTokenGenerateDateTime(new Date());
      mutableUser.update();
    }
    return false;
  }
}
