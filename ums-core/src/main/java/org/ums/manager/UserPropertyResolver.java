package org.ums.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ums.decorator.UserDaoDecorator;
import org.ums.domain.model.immutable.Employee;
import org.ums.domain.model.immutable.User;
import org.ums.domain.model.mutable.MutableUser;

public class UserPropertyResolver extends UserDaoDecorator {
  private static final Logger mLogger = LoggerFactory.getLogger(UserPropertyResolver.class);

  private EmployeeManager mEmployeeManager;
  private StudentManager mStudentManager;

  public UserPropertyResolver(EmployeeManager pEmployeeManager, StudentManager pStudentManager) {
    mEmployeeManager = pEmployeeManager;
    mStudentManager = pStudentManager;
  }

  @Override
  public User get(String pId) throws Exception {
    User user = getManager().get(pId);
    MutableUser mutableUser = user.edit();
    if (user.getPrimaryRole().getName().equalsIgnoreCase("student")) {
      mutableUser.setDepartment(mStudentManager.get(pId).getDepartment());
      mutableUser.setName(mutableUser.getName());
    } else {
      Employee employee = null;
      try {
        employee = mEmployeeManager.getByEmployeeId(user.getEmployeeId());
      } catch (Exception e) {
        mLogger.error("User not found as Employee, " + user.getId(), e);
      }
      if (employee != null) {
        mutableUser.setDepartment(employee.getDepartment());
        mutableUser.setName(employee.getEmployeeName());
      }
    }
    return mutableUser;
  }
}
