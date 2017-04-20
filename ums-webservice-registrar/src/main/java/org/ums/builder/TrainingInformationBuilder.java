package org.ums.builder;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.registrar.TrainingInformation;
import org.ums.domain.model.mutable.registrar.MutableTrainingInformation;
import org.ums.manager.UserManager;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;

@Component
public class TrainingInformationBuilder implements Builder<TrainingInformation, MutableTrainingInformation> {

  @Autowired
  UserManager userManager;

  @Override
  public void build(JsonObjectBuilder pBuilder, TrainingInformation pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) {
    pBuilder.add("employeeId", pReadOnly.getEmployeeId());
    pBuilder.add("trainingName", pReadOnly.getTrainingName());
    pBuilder.add("trainingInstitution", pReadOnly.getTrainingInstitute());
    pBuilder.add("trainingFrom", pReadOnly.getTrainingFromDate());
    pBuilder.add("trainingTo", pReadOnly.getTrainingToDate());
  }

  @Override
  public void build(MutableTrainingInformation pMutable, JsonObject pJsonObject, LocalCache pLocalCache) {
    pMutable.setEmployeeId(userManager.get(SecurityUtils.getSubject().getPrincipal().toString()).getEmployeeId());

    if(!pJsonObject.getJsonString("trainingName").equals("")) {
      pMutable.setTrainingName(pJsonObject.getString("trainingName"));
    }
    if(!pJsonObject.getJsonString("trainingInstitution").equals("")) {
      pMutable.setTrainingInstitute(pJsonObject.getString("trainingInstitution"));
    }
    if(!pJsonObject.getJsonString("trainingFrom").equals("")) {
      pMutable.setTrainingFromDate(pJsonObject.getString("trainingFrom"));
    }
    if(!pJsonObject.getJsonString("trainingTo").equals("")) {
      pMutable.setTrainingToDate(pJsonObject.getString("trainingTo"));
    }
  }
}
