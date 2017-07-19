package org.ums.resource.leavemanagement;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ums.builder.Builder;
import org.ums.cache.LocalCache;
import org.ums.context.AppContext;
import org.ums.domain.model.immutable.Employee;
import org.ums.domain.model.immutable.common.LmsApplication;
import org.ums.domain.model.immutable.common.LmsType;
import org.ums.domain.model.mutable.common.MutableLmsAppStatus;
import org.ums.domain.model.mutable.common.MutableLmsApplication;
import org.ums.enums.common.*;
import org.ums.manager.EmployeeManager;
import org.ums.manager.common.AttachmentManager;
import org.ums.manager.common.LmsAppStatusManager;
import org.ums.manager.common.LmsApplicationManager;
import org.ums.manager.common.LmsTypeManager;
import org.ums.persistent.model.common.PersistentLmsAppStatus;
import org.ums.persistent.model.common.PersistentLmsApplication;
import org.ums.resource.ResourceHelper;
import org.ums.services.leave.management.LeaveManagementService;
import org.ums.usermanagement.permission.AdditionalRolePermissions;
import org.ums.usermanagement.permission.AdditionalRolePermissionsManager;
import org.ums.usermanagement.user.User;
import org.ums.usermanagement.user.UserManager;

import javax.json.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Monjur-E-Morshed on 08-May-17.
 */
@Component
public class LmsApplicationResourceHelper extends ResourceHelper<LmsApplication, MutableLmsApplication, Long> {

  @Autowired
  private LmsApplicationManager mManager;

  @Autowired
  private UserManager mUserManager;

  @Autowired
  private EmployeeManager mEmployeeManager;

  @Autowired
  private LmsApplicationBuilder mBuilder;

  @Autowired
  private LmsAppStatusManager mLmsAppStatusManager;

  @Autowired
  private LmsTypeManager mLmsTypeManager;

  @Autowired
  private LeaveManagementService mLeaveManagementService;

  @Autowired
  private AdditionalRolePermissionsManager mAdditionalRolePermissionsManager;

  @Autowired
  private AttachmentManager mAttachmentManager;

  ApplicationContext applicationContext = AppContext.getApplicationContext();

  MessageChannel lmsChannel = applicationContext.getBean("lmsChannel", MessageChannel.class);

  @Transactional
  @Override
  public Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
    JsonArray entries = pJsonObject.getJsonArray("entries");
    LocalCache localCache = new LocalCache();
    JsonObject jsonObject = entries.getJsonObject(0);
    PersistentLmsApplication application = new PersistentLmsApplication();
    getBuilder().build(application, jsonObject, localCache);
    inserIntoLeaveApplicationStatus(application);

    JsonArray fileEntries = pJsonObject.getJsonArray("fileEntries");
    List<File> files = new ArrayList<>();
    for (int i = 0; i < fileEntries.size(); i++) {
      JsonObject fileJsonObject = fileEntries.getJsonObject(0);
      String fileStr = fileJsonObject.getString("file");
      System.out.println(fileStr);
      /*InputStream inputStream = new ByteArrayInputStream(fileStr.getBytes());
      File file = new File(fileJsonObject.getString("fileName"));
      OutputStream fileOutputStream = new FileOutputStream(file);
      IOUtils.copy(inputStream, fileOutputStream);
      fileOutputStream.close();
      Message<File> messageA = MessageBuilder.withPayload(file).build();
      lmsChannel.send(messageA);*/

    }
    URI contextURI = null;
    Response.ResponseBuilder builder = Response.created(contextURI);
    builder.status(Response.Status.CREATED);
    return builder.build();
  }

  private void inserIntoLeaveApplicationStatus(PersistentLmsApplication pApplication) {
    Long pAppId = new Long(0);
    MutableLmsAppStatus lmsAppStatus = new PersistentLmsAppStatus();
    lmsAppStatus.setLmsApplicationId(pAppId);
    lmsAppStatus.setActionTakenOn(pApplication.getAppliedOn());
    User user = mUserManager.get(SecurityUtils.getSubject().getPrincipal().toString());
    Employee employee = mEmployeeManager.get(user.getEmployeeId());
    lmsAppStatus.setActionTakenById(user.getEmployeeId());
    lmsAppStatus.setComments(LeaveApplicationStatus.APPLIED.getLabel());
    List<AdditionalRolePermissions> rolePermissionsStream = mAdditionalRolePermissionsManager.getAdditionalRole(employee.getDepartment().getId()).stream().filter(r -> r.getRoleId() == RoleType.DEPT_HEAD.getId()).collect(Collectors.toList());

    // todo add more roles, currently mst_role table in db is not complete.
    List<Integer> roles = user.getRoleIds();
    String message = "Leave Application from employee: " + employee.getEmployeeName() + " of department: "
        + employee.getDepartment().getShortName() + " is waiting for your approval.";
    if (rolePermissionsStream.get(0).getUserId().equals(user.getId())
        || user.getPrimaryRole().getId() == RoleType.COE.getId()
        || user.getPrimaryRole().getId() == RoleType.REGISTRAR.getId()
        || user.getPrimaryRole().getId() == RoleType.LIBRARIAN.getId()) {
      lmsAppStatus.setActionStatus(LeaveApplicationApprovalStatus.WAITING_FOR_VC_APPROVAL);
      pApplication.setLeaveApplicationStatus(LeaveApplicationApprovalStatus.WAITING_FOR_VC_APPROVAL);
      mLeaveManagementService.setNotification("vc", message);
    } else {
      lmsAppStatus.setActionStatus(LeaveApplicationApprovalStatus.WAITING_FOR_HEAD_APPROVAL);
      pApplication.setLeaveApplicationStatus(LeaveApplicationApprovalStatus.WAITING_FOR_HEAD_APPROVAL);

      mLeaveManagementService.setNotification(rolePermissionsStream.get(0).getUserId(), message);
    }

    pAppId = getContentManager().create(pApplication);
    lmsAppStatus.setLmsApplicationId(pAppId);

    mLmsAppStatusManager.create(lmsAppStatus);
  }

  public JsonObject getPendingLeavesOfEmployee(UriInfo pUriInfo) {
    User user = mUserManager.get(SecurityUtils.getSubject().getPrincipal().toString());
    List<LmsApplication> applications = getContentManager().getPendingLmsApplication(user.getEmployeeId());
    return getJsonObject(pUriInfo, applications);
  }

  private JsonObject getJsonObject(UriInfo pUriInfo, List<LmsApplication> pApplications) {
    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();
    for (LmsApplication application : pApplications) {
      JsonObjectBuilder jsonObject = Json.createObjectBuilder();
      getBuilder().build(jsonObject, application, pUriInfo, localCache);
      children.add(jsonObject);
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  public JsonObject getRemainingLeaves() {
    String userId = SecurityUtils.getSubject().getPrincipal().toString();
    String employeeId = mUserManager.get(userId).getEmployeeId();
    return getRemainingLeavesJsonObject(employeeId);
  }

  public JsonObject getRemainingLeaves(String pEmployeeId) {
    return getRemainingLeavesJsonObject(pEmployeeId);
  }

  private JsonObject getRemainingLeavesJsonObject(String pEmployeeId) {
    List<LmsType> lmsTypes = getLeaveTypes();
    int year = Calendar.getInstance().get(Calendar.YEAR);
    List<LmsApplication> applications = getContentManager()
        .getLmsApplication(pEmployeeId, year);
    Map<Integer, List<LmsApplication>> applicationMap = applications
        .stream()
        .filter(app -> app.getApplicationStatus() == LeaveApplicationApprovalStatus.APPLICATION_APPROVED)
        .collect(Collectors.groupingBy(LmsApplication::getLeaveTypeId));

    JsonObjectBuilder object = Json.createObjectBuilder();
    JsonArrayBuilder children = Json.createArrayBuilder();
    LocalCache localCache = new LocalCache();

    for (LmsType lmsType : lmsTypes) {
      JsonObjectBuilder jsonObject = Json.createObjectBuilder();
      jsonObject.add("leaveTypeId", lmsType.getId());
      jsonObject.add("leaveName", lmsType.getName());
      int leavesTaken = getLeavesTaken(applicationMap, lmsType);
      jsonObject.add("daysLeft", applicationMap.get(lmsType.getId()) != null ? (lmsType.getMaxDuration() - leavesTaken) : lmsType.getMaxDuration());
      children.add(jsonObject);
    }
    object.add("entries", children);
    localCache.invalidate();
    return object.build();
  }

  private int getLeavesTaken(Map<Integer, List<LmsApplication>> pApplicationMap, LmsType lmsType) {
    int leavesTaken = 0;
    if (pApplicationMap.get(lmsType.getId()) != null)
      for (LmsApplication application : pApplicationMap.get(lmsType.getId())) {
        leavesTaken +=
            (application.getToDate().getTime() - application.getFromDate().getTime()) / (1000 * 60 * 60 * 24);
      }
    return leavesTaken;
  }

  protected JsonObject getApprovedLeaveApplicationsWithinDateRange(String pStartDate, String pEndDate, UriInfo pUriInfo) {
    String userId = SecurityUtils.getSubject().getPrincipal().toString();
    User user = mUserManager.get(userId);
    List<LmsApplication> leaveApplications =
        getContentManager().getApprovedApplicationsWithinDateRange(user.getEmployeeId(), pStartDate, pEndDate);
    return getJsonObject(pUriInfo, leaveApplications);
  }

  private List<LmsType> getLeaveTypes() {
    List<LmsType> lmsTypes = new ArrayList<>();
    Employee employee =
        mEmployeeManager.get(mUserManager.get(SecurityUtils.getSubject().getPrincipal().toString()).getEmployeeId());
    if (employee.getEmploymentType().equals(EmployeeType.TEACHER.getId() + "")) {
      if (employee.getGender().equals("M"))
        lmsTypes = mLmsTypeManager.getLmsTypes(EmployeeLeaveType.TEACHERS_LEAVE, Gender.MALE);
      else
        lmsTypes = mLmsTypeManager.getLmsTypes(EmployeeLeaveType.TEACHERS_LEAVE, Gender.FEMALE);
    } else {
      if (employee.getGender().equals("M"))
        lmsTypes = mLmsTypeManager.getLmsTypes(EmployeeLeaveType.COMMON_LEAVE, Gender.MALE);
      else
        lmsTypes = mLmsTypeManager.getLmsTypes(EmployeeLeaveType.COMMON_LEAVE, Gender.FEMALE);
    }
    return lmsTypes;
  }

  @Override
  protected LmsApplicationManager getContentManager() {
    return mManager;
  }

  @Override
  protected Builder<LmsApplication, MutableLmsApplication> getBuilder() {
    return mBuilder;
  }

  @Override
  protected String getETag(LmsApplication pReadonly) {
    return pReadonly.getLastModified();
  }
}
