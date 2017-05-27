package org.ums.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.ums.domain.model.immutable.registrar.PersonalInformation;
import org.ums.domain.model.mutable.registrar.MutablePersonalInformation;
import org.ums.resource.helper.PersonalInformationResourceHelper;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class MutablePersonalInformationResource extends Resource {

  @Autowired
  ResourceHelper<PersonalInformation, MutablePersonalInformation, String> mResourceHelper;

  @POST
  @Path("/savePersonalInformation")
  public Response savePersonalInformation(final JsonObject pJsonObject) throws Exception {
    return mResourceHelper.post(pJsonObject, mUriInfo);
    // return null;
  }

  @PUT
  @Path("/updatePersonalInformation")
  public Response updatePersonalInformation(final JsonObject pJsonObject) {
    // return mResourceHelper.put(pJsonObject, mUriInfo);
    return null;
  }
}
