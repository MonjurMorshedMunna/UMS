package org.ums.decorator;

import org.ums.domain.model.mutable.MutableUser;
import org.ums.domain.model.immutable.User;
import org.ums.manager.UserManager;

import java.util.List;

public class UserDaoDecorator extends ContentDaoDecorator<User, MutableUser, String, UserManager>
    implements UserManager {

  public User getByEmployeeId(final String pEmployeeId) {
    return getManager().getByEmployeeId(pEmployeeId);
  }

  @Override
  public int setPasswordResetToken(String pToken, String pUserId) {
    int modified = getManager().setPasswordResetToken(pToken, pUserId);
    if(modified <= 0) {
      throw new IllegalArgumentException("No entry updated");
    }
    return modified;
  }

  @Override
  public int updatePassword(String pUserId, String pPassword) {
    int modified = getManager().updatePassword(pUserId, pPassword);
    if(modified <= 0) {
      throw new IllegalArgumentException("No entry updated");
    }
    return modified;
  }

  @Override
  public int clearPasswordResetToken(final String pUserId) {
    int modified = getManager().clearPasswordResetToken(pUserId);
    if(modified <= 0) {
      throw new IllegalArgumentException("No entry updated");
    }
    return modified;
  }

  @Override
  public List<User> getUsers() {
    return getManager().getUsers();
  }
}
