package org.ums.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ums.decorator.UserDaoDecorator;
import org.ums.domain.model.immutable.Employee;
import org.ums.domain.model.immutable.Student;
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
  public List<User> getAll() {
    return super.getUsers().stream().map(user -> transform(user)).collect(Collectors.toList());
  }

  @Override
  public User get(String pId) {
    User user = getManager().get(pId);
    return transform(user);
  }

  @Override
  public List<User> getUsers() {
    return super.getUsers().stream().map(user -> transform(user)).collect(Collectors.toList());
  }

  private User transform(User user) {
    MutableUser mutableUser = user.edit();
    if(user.getPrimaryRole().getName().equalsIgnoreCase("sadmin")) {
      mutableUser.setDepartment(null);
      mutableUser.setName("Admin User");
      mutableUser.setEmployeeId("-1");
      return mutableUser;
    }
    if(user.getPrimaryRole().getName().equalsIgnoreCase("student")) {
      Student student = mStudentManager.get(user.getId());
      mutableUser.setDepartment(student.getDepartment());
      mutableUser.setName(student.getFullName());
    }
    else {
      Employee employee = null;
      try {
        employee = mEmployeeManager.get(user.getEmployeeId());
      } catch(Exception e) {
        mLogger.error("User not found as employee, " + user.getId(), e);
      }
      if(employee != null) {
        mutableUser.setDepartment(employee.getDepartment());
        mutableUser.setName(employee.getEmployeeName());
        mutableUser.setEmployeeId(employee.getId());
      }
    }
    return mutableUser;
  }
}
