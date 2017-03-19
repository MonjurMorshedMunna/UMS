package org.ums.builder;

import org.springframework.stereotype.Component;
import org.ums.builder.Builder;
import org.ums.cache.LocalCache;
import org.ums.domain.model.mutable.MutableProgramType;
import org.ums.domain.model.immutable.ProgramType;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;

@Component
public class ProgramTypeBuilder implements Builder<ProgramType, MutableProgramType> {
  public void build(final JsonObjectBuilder pBuilder, final ProgramType pProgramType, final UriInfo pUriInfo,
      final LocalCache pLocalCache) {
    pBuilder.add("id", pProgramType.getId());
    pBuilder.add("name", pProgramType.getName());
    pBuilder.add("self",
        pUriInfo.getBaseUriBuilder().path("academic").path("programtype").path(String.valueOf(pProgramType.getId()))
            .build().toString());
  }

  public void build(final MutableProgramType pMutableProgramType, JsonObject pJsonObject, final LocalCache pLocalCache) {
    pMutableProgramType.setId(pJsonObject.getInt("id"));
    pMutableProgramType.setName(pJsonObject.getString("name"));
  }
}
