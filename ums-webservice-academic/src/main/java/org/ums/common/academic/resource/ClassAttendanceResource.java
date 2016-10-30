package org.ums.common.academic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.manager.ClassRoomManager;
import org.ums.resource.Resource;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.StringReader;

/**
 * Created by Ifti on 25-Oct-16.
 */
@Component
@Path("/academic/classattendance")
@Produces(Resource.MIME_TYPE_JSON)
@Consumes(Resource.MIME_TYPE_JSON)
public class ClassAttendanceResource {

  @Autowired
  ClassRoomManager mManager;

  @GET
  @Path("/ifti")
  public JsonObject getAll() throws Exception {

    return null;

  }

}
