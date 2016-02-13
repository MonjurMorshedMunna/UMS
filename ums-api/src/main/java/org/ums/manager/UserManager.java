package org.ums.manager;

import org.ums.domain.model.mutable.MutableUser;
import org.ums.domain.model.readOnly.User;

public interface UserManager extends ContentManager<User, MutableUser, String> {
  int setPasswordResetToken(final String pToken, final String pUserId) throws Exception;

  int updatePassword(final String pUserId, final String pPassword) throws Exception;

  int clearPasswordResetToken(final String pUserId) throws Exception;
}
