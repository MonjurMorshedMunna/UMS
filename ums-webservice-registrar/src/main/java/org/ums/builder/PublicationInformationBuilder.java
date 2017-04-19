package org.ums.builder;

import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.registrar.PublicationInformation;
import org.ums.domain.model.mutable.registrar.MutablePublicationInformation;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;

@Component
public class PublicationInformationBuilder implements Builder<PublicationInformation, MutablePublicationInformation> {
  @Override
  public void build(JsonObjectBuilder pBuilder, PublicationInformation pReadOnly, UriInfo pUriInfo,
      LocalCache pLocalCache) {
    pBuilder.add("employeeId", pReadOnly.getEmployeeId());
    pBuilder.add("publicationTitle", pReadOnly.getPublicationTitle());
    pBuilder.add("publicationInterestGenre", pReadOnly.getInterestGenre());
    pBuilder.add("publisherName", pReadOnly.getPublisherName());
    pBuilder.add("dateOfPublication", pReadOnly.getDateOfPublication());
    pBuilder.add("publicationType", pReadOnly.getPublicationType());
    pBuilder.add("publicationWebLink", pReadOnly.getPublicationWebLink());
  }

  @Override
  public void build(MutablePublicationInformation pMutable, JsonObject pJsonObject, LocalCache pLocalCache) {
    pMutable.setEmployeeId(pJsonObject.getString("employeeId"));
    pMutable.setPublicationTitle(pJsonObject.getString("publicationTitle"));
    pMutable.setInterestGenre(pJsonObject.getString("publicationInterestGenre"));
    pMutable.setPublisherName(pJsonObject.getString("publisherName"));
    pMutable.setDateOfPublication(pJsonObject.getString("dateOfPublication"));
    pMutable.setPublicationType(pJsonObject.getJsonObject("publicationType").getString("name"));
    pMutable.setPublicationWebLink(pJsonObject.getString("publicationWebLink"));
  }
}
