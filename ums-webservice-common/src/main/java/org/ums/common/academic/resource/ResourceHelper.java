package org.ums.common.academic.resource;


import org.ums.academic.builder.Builder;
import org.ums.cache.LocalCache;
import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Mutable;
import org.ums.manager.ContentManager;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.*;
import java.util.List;

public abstract class ResourceHelper<R extends EditType<M>, M extends Mutable, I> {

  protected R load(final I pObjectId) throws Exception {
    return getContentManager().get(pObjectId);
  }

  protected Response get(final I pObjectId, final Request pRequest, final UriInfo pUriInfo) throws Exception {
    R readOnly = load(pObjectId);
    Response.ResponseBuilder builder;
    //Calculate the ETag on last modified date of user resource
    EntityTag etag = new EntityTag(getEtag(readOnly));
    //Verify if it matched with etag available in http request
    builder = pRequest.evaluatePreconditions(etag);
    builder = null;
    if (builder == null) {
      LocalCache localCache = new LocalCache();
      builder = Response.ok(toJson(readOnly, pUriInfo, localCache));
      builder.tag(etag);
      localCache.invalidate();
    }

//    CacheControl cacheControl = new CacheControl();
//    cacheControl.setMaxAge(86400);
//    cacheControl.setPrivate(true);
//    builder.cacheControl(cacheControl);

    return builder.build();
  }

  protected JsonObject getAll(final UriInfo pUriInfo) throws Exception {
    List<R> readOnlys = getContentManager().getAll();
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for (R readOnly : readOnlys) {
      children.add(toJson(readOnly, pUriInfo, localCache));
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  protected JsonObject toJson(final R pObject, final UriInfo pUriInfo, final LocalCache pLocalCache) throws Exception {
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    for (Builder<R, M> builder : getBuilders()) {
      builder.build(jsonObjectBuilder, pObject, pUriInfo, pLocalCache);
    }
    return jsonObjectBuilder.build();
  }

  protected Response delete(final I pObjectId) throws Exception {
    R readOnly = load(pObjectId);
    readOnly.edit().delete();
    return Response.noContent().build();
  }

  protected Response put(final I pObjectId, final Request pRequest,
                         final String pIfMatch, final JsonObject pJsonObject) throws Exception {
    if (pIfMatch == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("No If-Match header found.").build();
    }
    R readOnly = load(pObjectId);
    EntityTag etag = new EntityTag(getEtag(readOnly));

    Response.ResponseBuilder preconditionResponse = pRequest.evaluatePreconditions(etag);
    // client is not up to date (send back 412)
    if (preconditionResponse != null) {
      return preconditionResponse.build();
    }

    M mutable = readOnly.edit();
    LocalCache localCache = new LocalCache();
    for (Builder<R, M> builder : getBuilders()) {
      builder.build(mutable, pJsonObject, localCache);
    }
    mutable.commit(true);
    localCache.invalidate();

    return Response.noContent().build();
  }

  protected abstract Response post(final JsonObject pJsonObject, final UriInfo pUriInfo) throws Exception;

  protected abstract ContentManager<R, M, I> getContentManager();

  protected abstract List<Builder<R, M>> getBuilders();

  protected abstract String getEtag(R pReadonly);
}
