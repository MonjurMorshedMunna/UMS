package org.ums.resource.helper;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ums.builder.Builder;
import org.ums.builder.TrainingInformationBuilder;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.registrar.TrainingInformation;
import org.ums.domain.model.mutable.registrar.MutableTrainingInformation;
import org.ums.manager.ContentManager;
import org.ums.manager.UserManager;
import org.ums.manager.registrar.TrainingInformationManager;
import org.ums.persistent.model.registrar.PersistentTrainingInformation;
import org.ums.resource.ResourceHelper;

import javax.json.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingInformationResourceHelper extends
    ResourceHelper<TrainingInformation, MutableTrainingInformation, Integer> {

  @Autowired
  TrainingInformationManager mTrainingInformationManager;

  @Autowired
  TrainingInformationBuilder mTrainingInformationBuilder;

  @Autowired
  UserManager userManager;

  public JsonObject getTrainingInformation(final UriInfo pUriInfo) {
      String userId = userManager.get(
              SecurityUtils.getSubject().getPrincipal().toString()).getEmployeeId();
      List<TrainingInformation> pTrainingInformation = new ArrayList<>();
      try {
          pTrainingInformation =
                  mTrainingInformationManager.getEmployeeTrainingInformation(userId);
      } catch (EmptyResultDataAccessException e) {

      }
      return toJson(pTrainingInformation, pUriInfo);
  }

  @Transactional
  public Response saveTrainingInformation(JsonObject pJsonObject, UriInfo pUriInfo) {

    mTrainingInformationManager.deleteTrainingInformation(userManager.get(
        SecurityUtils.getSubject().getPrincipal().toString()).getEmployeeId());

    LocalCache localCache = new LocalCache();
    JsonArray entries = pJsonObject.getJsonArray("entries");

    JsonArray trainingJsonArray = entries.getJsonObject(0).getJsonArray("training");
    int sizeOfTrainingJsonArray = trainingJsonArray.size();

    List<MutableTrainingInformation> mutableTrainingInformation = new ArrayList<>();
    for(int i = 0; i < sizeOfTrainingJsonArray; i++) {
      MutableTrainingInformation trainingInformation = new PersistentTrainingInformation();
      mTrainingInformationBuilder.build(trainingInformation, trainingJsonArray.getJsonObject(i), localCache);
      mutableTrainingInformation.add(trainingInformation);
    }
    mTrainingInformationManager.saveTrainingInformation(mutableTrainingInformation);

    Response.ResponseBuilder builder = Response.created(null);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  private JsonObject toJson(List<TrainingInformation> pTrainingInformation, UriInfo pUriInfo) {
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    for(TrainingInformation trainingInformation : pTrainingInformation) {
      JsonObjectBuilder jsonObject = Json.createObjectBuilder();
      getBuilder().build(jsonObject, trainingInformation, pUriInfo, localCache);
      children.add(jsonObject);
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    return null;
  }

  @Override
  protected ContentManager<TrainingInformation, MutableTrainingInformation, Integer> getContentManager() {
    return mTrainingInformationManager;
  }

  @Override
  protected Builder<TrainingInformation, MutableTrainingInformation> getBuilder() {
    return mTrainingInformationBuilder;
  }

  @Override
  protected String getETag(TrainingInformation pReadonly) {
    return pReadonly.getLastModified();
  }
}
