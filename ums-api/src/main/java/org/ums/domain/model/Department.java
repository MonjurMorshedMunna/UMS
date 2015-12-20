package org.ums.domain.model;


import java.io.Serializable;

public interface Department extends Serializable, EditType<MutableDepartment>, LastModifier, Cacheable<Integer> {
  String getShortName() throws Exception;

  String getLongName() throws Exception;

  int getType() throws Exception;
}
