package org.ums.builder;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.ums.cache.LocalCache;
import org.ums.usermanagement.user.User;
import org.ums.usermanagement.user.MutableUser;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;

@Component
public class UserBuilder implements Builder<User, MutableUser> {
  @Override
  public void build(JsonObjectBuilder pBuilder, User pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) {
    pBuilder.add("id", pReadOnly.getId());
    pBuilder.add("name", pReadOnly.getName());
    pBuilder.add("departmentName", pReadOnly.getDepartment().getShortName());
    pBuilder.add("departmentId", pReadOnly.getDepartment().getId());
    if(!StringUtils.isEmpty(pReadOnly.getEmployeeId())) {
      pBuilder.add("employeeId", pReadOnly.getEmployeeId());
    }
    if(pReadOnly.getPrimaryRole() != null) {
      pBuilder.add("roleId", pReadOnly.getPrimaryRole().getId());
    }
  }

  @Override
  public void build(MutableUser pMutable, JsonObject pJsonObject, LocalCache pLocalCache) {
    // Do Nothing
  }
}
