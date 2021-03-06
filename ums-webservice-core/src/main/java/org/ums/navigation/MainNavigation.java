package org.ums.navigation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.navigation.helper.MainNavigationHelper;
import org.ums.resource.Resource;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/mainNavigation")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class MainNavigation extends Resource {
  @Autowired
  MainNavigationHelper mNavigationHelper;

  @GET
  public JsonObject navigationItems() {
    return mNavigationHelper.getNavigationItems(mUriInfo);
  }
}
