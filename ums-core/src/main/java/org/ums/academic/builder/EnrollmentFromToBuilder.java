package org.ums.academic.builder;

import org.ums.cache.LocalCache;
import org.ums.domain.model.mutable.MutableEnrollmentFromTo;
import org.ums.domain.model.readOnly.EnrollmentFromTo;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;


public class EnrollmentFromToBuilder implements Builder<EnrollmentFromTo, MutableEnrollmentFromTo> {
  @Override
  public void build(JsonObjectBuilder pBuilder, EnrollmentFromTo pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) throws Exception {
    pBuilder.add("id", pReadOnly.getId());
    pBuilder.add("year", pReadOnly.getToYear());
    pBuilder.add("semester", pReadOnly.getToSemester());
  }

  @Override
  public void build(MutableEnrollmentFromTo pMutable, JsonObject pJsonObject, LocalCache pLocalCache) throws Exception {

  }
}
