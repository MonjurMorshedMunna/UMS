package org.ums.manager;

import org.ums.domain.model.immutable.Employee;
import org.ums.domain.model.mutable.MutableEmployee;

import java.util.List;

public interface EmployeeManager extends ContentManager<Employee, MutableEmployee, String> {

  boolean existenceByEmail(final String pEmailAddress);

  Employee getByEmail(final String pEmailAddress);

  List<Employee> getByDesignation(final String pDesignationId);

  List<Employee> getActiveTeachersOfDept(String deptId);
}
