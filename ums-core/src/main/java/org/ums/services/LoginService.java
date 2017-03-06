package org.ums.services;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ums.domain.model.immutable.Employee;
import org.ums.domain.model.immutable.User;
import org.ums.domain.model.mutable.MutableEmployee;
import org.ums.manager.EmployeeManager;
import org.ums.manager.UserManager;
import org.ums.message.MessageResource;
import org.ums.response.type.GenericMessageResponse;
import org.ums.response.type.GenericResponse;
import org.ums.util.Constants;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginService {

  private static Logger mLogger = LoggerFactory.getLogger(LoginService.class);

  @Autowired
  private UserManager mUserManager;

  @Autowired
  private EmployeeManager mEmployeeManager;

  @Autowired
  private PasswordService mPasswordService;

  @Autowired
  private EmailService emailService;

  @Autowired
  MessageResource mMessageResource;

  @Autowired
  private String dummyEmail;

  public GenericResponse<Map> checkAndSendPasswordResetEmailToUser(final String pUserId,
      final String pEmailAddress, final String pRecoverBy) {
    String token = UUID.randomUUID().toString();
    Date now = new Date();
    Date tokenInvalidDate = null;
    Date tokenEmailInvalidDate = null;

    // ToDo: Need to add code for students password recovery from email address as well.

    if(pRecoverBy.equals("byUserId") && !mUserManager.exists(pUserId)) {
      return new GenericMessageResponse(GenericResponse.ResponseType.ERROR,
          mMessageResource.getMessage("user.not.exists"));
    }
    else if(pRecoverBy.equals("byEmailAddress")
        && (!mEmployeeManager.existenceByEmail(pEmailAddress))) {
      return new GenericMessageResponse(GenericResponse.ResponseType.ERROR,
          mMessageResource.getMessage("email.not.exists"));
    }

    User user = null;

    if(pRecoverBy.equals("byEmailAddress")) {
      Employee employee = mEmployeeManager.getByEmail(pEmailAddress);
      user = mUserManager.getByEmployeeId(employee.getId());
    }
    else {
      user = mUserManager.get(pUserId);
    }
    if(user == null) {
      return new GenericMessageResponse(GenericResponse.ResponseType.ERROR,
          mMessageResource.getMessage("Invalid User ID or Email Address."));
    }

    if(user.getPasswordTokenGenerateDateTime() != null) {
      tokenInvalidDate =
          new Date(user.getPasswordTokenGenerateDateTime().getTime()
              + (Constants.PASSWORD_RESET_TOKEN_LIFE * Constants.ONE_MINUTE_IN_MILLIS));
      tokenEmailInvalidDate =
          new Date(user.getPasswordTokenGenerateDateTime().getTime()
              + (Constants.PASSWORD_RESET_TOKEN_EMAIL_LIFE * Constants.ONE_MINUTE_IN_MILLIS));
    }

    if(user.getPasswordTokenGenerateDateTime() != null && tokenEmailInvalidDate != null
        && now.before(tokenEmailInvalidDate)) {
      return new GenericMessageResponse(GenericResponse.ResponseType.INFO,
          mMessageResource.getMessage("token.already.send"));
    }
    else {
      mLogger.info("Send an password token email again.");
    }

    // I think we don't need this checking.....
    // if(StringUtils.isBlank(user.getPasswordResetToken())
    // || (tokenInvalidDate != null && now.after(tokenInvalidDate))
    // || user.getPasswordTokenGenerateDateTime() == null) {
    mUserManager.setPasswordResetToken(mPasswordService.encryptPassword(token).replaceAll("=", ""),
        user.getId());
    user = mUserManager.get(user.getId());
    // }

    // ToDo: Need to check whether the user has an email address in the database
    // Here for the time being we only considered employee email address, But later on we will
    // consider student's email address as well....
    Employee employee = mEmployeeManager.get(user.getEmployeeId());
    emailService.setUser(user);
    emailService.sendEmail(employee.getEmailAddress(), dummyEmail, "Reset Your IUMS Password");
    return new GenericMessageResponse(GenericResponse.ResponseType.SUCCESS);

  }

  public GenericResponse<Map> resetPassword(final String pUserId, final String pResetToken,
      final String pNewPassword, final String pConfirmNewPassword) {
    Date tokenInvalidDate = null;
    Date now = new Date();

    if(!mUserManager.exists(pUserId)) {
      return new GenericMessageResponse(GenericResponse.ResponseType.ERROR,
          mMessageResource.getMessage("user.not.exists"));
    }

    User user = mUserManager.get(pUserId);

    if(user.getPasswordTokenGenerateDateTime() != null) {
      tokenInvalidDate =
          new Date(user.getPasswordTokenGenerateDateTime().getTime()
              + (Constants.PASSWORD_RESET_TOKEN_LIFE * Constants.ONE_MINUTE_IN_MILLIS));
    }
    if(user.getPasswordTokenGenerateDateTime() != null && tokenInvalidDate.before(now)) {
      return new GenericMessageResponse(GenericResponse.ResponseType.ERROR,
          mMessageResource.getMessage("invalid.password.reset.url"));
    }
    if(!pNewPassword.equals(pConfirmNewPassword)) {
      return new GenericMessageResponse(GenericResponse.ResponseType.ERROR,
          mMessageResource.getMessage("password.confirm.password.different"));
    }
    if(pResetToken.equals(user.getPasswordResetToken())) {
      mUserManager.updatePassword(pUserId, mPasswordService.encryptPassword(pNewPassword));
      mUserManager.clearPasswordResetToken(pUserId);
      return new GenericMessageResponse(GenericResponse.ResponseType.SUCCESS);
    }

    return new GenericMessageResponse(GenericResponse.ResponseType.ERROR);
  }
}
