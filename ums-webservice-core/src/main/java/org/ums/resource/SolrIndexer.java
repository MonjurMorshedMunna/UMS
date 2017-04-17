package org.ums.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.ums.domain.model.immutable.User;
import org.ums.manager.UserManager;
import org.ums.solr.repository.EmployeeRepository;
import org.ums.solr.repository.converter.SimpleConverter;
import org.ums.solr.repository.document.EmployeeDocument;
import org.ums.solr.repository.lms.RecordRepository;

@Component
@Path("/indexer")
@Produces(Resource.MIME_TYPE_JSON)
public class SolrIndexer extends Resource {

  @Autowired
  @Qualifier("employeeRepositoryImpl")
  EmployeeRepository mUserRepository;

  @Qualifier("recordRepositoryImpl")
  RecordRepository mRecordRepository;

  @Autowired
  UserManager mUserManager;

  @GET
  @Path("/reindex")
  public Response reindex() throws Exception {
    indexDocuments();
    return Response.ok().build();
  }

  private void indexDocuments() {
    SimpleConverter<User, EmployeeDocument> converter = new SimpleConverter<>(User.class, EmployeeDocument.class);
    mUserRepository.deleteAll();
    mUserRepository.save(converter.convert(mUserManager.getAll()));
  }
}
