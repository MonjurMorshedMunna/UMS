package org.ums.common.academic.resource.helper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.common.academic.resource.ResourceHelper;
import org.ums.common.academic.resource.RoutineResource;
import org.ums.common.academic.resource.SemesterWithdrawalLogResource;
import org.ums.common.builder.Builder;
import org.ums.common.builder.SemesterWithdrawalLogBuilder;
import org.ums.domain.model.immutable.SemesterWithdrawalLog;
import org.ums.domain.model.mutable.MutableRoutine;
import org.ums.domain.model.mutable.MutableSemesterWithdrawalLog;
import org.ums.manager.ContentManager;
import org.ums.manager.SemesterWithdrawalLogManager;
import org.ums.persistent.model.PersistentRoutine;
import org.ums.persistent.model.PersistentSemesterWithdrawalLog;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Component
public class SemesterWithdrawalLogResourceHelper extends ResourceHelper<SemesterWithdrawalLog,MutableSemesterWithdrawalLog,Integer> {

  @Autowired
  private SemesterWithdrawalLogManager mManager;

  @Autowired
  public SemesterWithdrawalLogBuilder mBuilder;

  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    MutableSemesterWithdrawalLog mutableLog= new PersistentSemesterWithdrawalLog();
    LocalCache localCache = new LocalCache();
    getBuilder().build(mutableLog, pJsonObject, localCache);
    mutableLog.commit(false);
    URI contextURI = pUriInfo.getBaseUriBuilder().path(SemesterWithdrawalLogResource.class).path(SemesterWithdrawalLogResource.class, "get").build(mutableLog.getId());
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }


  public JsonObject getBySemesterWithdrawalId(final int semesterWithdrawalid,final Request pRequest,final UriInfo pUriInfo)throws Exception{
    SemesterWithdrawalLog mLog = getContentManager().getBySemesterWithdrawalId(semesterWithdrawalid);


    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();


    children.add(toJson(mLog, pUriInfo, localCache));
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  @Override
  public SemesterWithdrawalLogManager getContentManager() {
    return mManager;
  }

  @Override
  public Builder<SemesterWithdrawalLog, MutableSemesterWithdrawalLog> getBuilder() {
    return mBuilder;
  }

  @Override
  public String getEtag(SemesterWithdrawalLog pReadonly) {
    return pReadonly.getLastModified();
  }
}
