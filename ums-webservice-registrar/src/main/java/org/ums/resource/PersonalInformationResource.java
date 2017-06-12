package org.ums.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.resource.helper.PersonalInformationResourceHelper;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Component
@Path("employee/personal")
public class PersonalInformationResource extends MutablePersonalInformationResource {

  @Autowired
  PersonalInformationResourceHelper personalInformationResourceHelper;

  @GET
  @Path("/getPersonalInformation")
  public JsonObject getPersonalInformation() {
    JsonObject personalInfoJsonObj = personalInformationResourceHelper.getPersonalInformation(mUriInfo);
    return personalInfoJsonObj;
  }
}
