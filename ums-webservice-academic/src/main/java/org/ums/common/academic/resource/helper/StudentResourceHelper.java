package org.ums.common.academic.resource.helper;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ums.cache.LocalCache;
import org.ums.common.academic.resource.StudentResource;
import org.ums.common.builder.StudentBuilder;
import org.ums.domain.model.immutable.Employee;
import org.ums.domain.model.immutable.Student;
import org.ums.domain.model.immutable.StudentRecord;
import org.ums.domain.model.immutable.User;
import org.ums.domain.model.mutable.MutableStudent;
import org.ums.domain.model.mutable.MutableStudentRecord;
import org.ums.domain.model.mutable.MutableUser;
import org.ums.manager.*;
import org.ums.persistent.model.PersistentStudent;
import org.ums.persistent.model.PersistentStudentRecord;
import org.ums.persistent.model.PersistentUser;
import org.ums.resource.ResourceHelper;
import org.ums.util.UmsUtils;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("StudentResourceHelper")
public class StudentResourceHelper extends ResourceHelper<Student, MutableStudent, String> {
  @Autowired
  RoleManager mRoleManager;

  @Autowired
  @Qualifier("fileContentManager")
  BinaryContentManager<byte[]> mBinaryContentManager;

  @Autowired
  private StudentManager mManager;

  @Autowired
  private UserManager mUserManager;

  @Autowired
  private EmployeeManager mEmployeeManager;

  @Autowired
  @Qualifier("StudentBuilder")
  private StudentBuilder mBuilder;

  //TODO: Move this to service layer
  @Override
  @Transactional
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    MutableStudent mutableStudent = new PersistentStudent();

    LocalCache localCache = new LocalCache();
    getBuilder().build(mutableStudent, pJsonObject, localCache);

    mutableStudent.setCurrentYear(UmsUtils.FIRST);
    mutableStudent.setCurrentAcademicSemester(UmsUtils.FIRST);
    mutableStudent.setEnrollmentType(Student.EnrollmentType.TEMPORARY);
    mutableStudent.commit(false);

    MutableStudentRecord mutableStudentRecord = new PersistentStudentRecord();
    mutableStudentRecord.setStudentId(mutableStudent.getId());
    mutableStudentRecord.setSemesterId(mutableStudent.getSemesterId());
    mutableStudentRecord.setProgramId(mutableStudent.getProgramId());
    mutableStudentRecord.setYear(UmsUtils.FIRST);
    mutableStudentRecord.setAcademicSemester(UmsUtils.FIRST);
    mutableStudentRecord.setType(StudentRecord.Type.TEMPORARY);
    mutableStudentRecord.setStatus(StudentRecord.Status.UNKNOWN);
    mutableStudentRecord.commit(false);

    MutableUser studentUser = new PersistentUser();
    studentUser.setId(pJsonObject.getString("id"));
    //TODO: Use a password generator to generate temporary password
    String random = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    studentUser.setTemporaryPassword(random.toCharArray());
    //TODO: Use role name to fetch a particular role, say for Student it should be "student"
    studentUser.setPrimaryRole(mRoleManager.get(11));
    studentUser.setActive(true);
    studentUser.commit(false);

    String encodingPrefix = "base64,", data = pJsonObject.getString("imageData");
    int contentStartIndex = data.indexOf(encodingPrefix) + encodingPrefix.length();
    byte[] imageData = Base64.getDecoder().decode(data.substring(contentStartIndex));

    mBinaryContentManager.create(imageData, pJsonObject.getString("id"), BinaryContentManager.Domain.PICTURE);
    URI contextURI = pUriInfo.getBaseUriBuilder().path(StudentResource.class).path(StudentResource.class, "get").build(mutableStudent.getId());
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);

    return builder.build();
  }

  public JsonObject getStudentInfoById(final UriInfo pUriInfo) throws Exception {
    String mStudentId = SecurityUtils.getSubject().getPrincipal().toString();
    Student student = getContentManager().get(mStudentId);
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    children.add(toJson(student, pUriInfo, localCache));
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }


  public JsonObject getActiveStudentsByDepartment(final UriInfo pUriInfo) throws Exception{
    Employee employee = getLoggedEmployee();
    String deptId = employee.getDepartment().getId();
    List<Student> students = getContentManager()
        .getActiveStudents()
        .parallelStream()
        .filter(s-> s.getDepartmentId().equals(deptId))
        .collect(Collectors.toList());

    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    for(Student student : students){
      children.add(toJson(student,pUriInfo,localCache));
    }
    object.add("entries",children);
    localCache.invalidate();
    return object.build();
  }



  private Employee getLoggedEmployee() throws Exception{
    String userId = SecurityUtils.getSubject().getPrincipal().toString();
    User user = mUserManager.get(userId);
    Employee employee = mEmployeeManager.getByEmployeeId(user.getEmployeeId());
    return employee;
  }

  @Override
  protected StudentManager getContentManager() {
    return mManager;
  }

  @Override
  protected StudentBuilder getBuilder() {
    return mBuilder;
  }

  @Override
  protected String getEtag(Student pReadonly) {
    return pReadonly.getLastModified();
  }
}
