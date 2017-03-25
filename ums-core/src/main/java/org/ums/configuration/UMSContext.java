package org.ums.configuration;

import javax.sql.DataSource;

import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.convert.SolrJConverter;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.ums.cache.*;
import org.ums.cache.common.CountryCache;
import org.ums.cache.library.*;
import org.ums.cachewarmer.AutoCacheWarmer;
import org.ums.cachewarmer.CacheWarmerManagerImpl;
import org.ums.fee.*;
import org.ums.formatter.DateFormat;
import org.ums.generator.IdGenerator;
import org.ums.generator.JxlsGenerator;
import org.ums.generator.XlsGenerator;
import org.ums.solr.indexer.ConsumeIndex;
import org.ums.solr.indexer.ConsumeIndexJobImpl;
import org.ums.solr.indexer.IndexConsumerDao;
import org.ums.solr.indexer.IndexDao;
import org.ums.solr.indexer.manager.IndexConsumerManager;
import org.ums.solr.indexer.manager.IndexManager;
import org.ums.solr.indexer.resolver.EntityResolverFactory;
import org.ums.solr.indexer.resolver.EntityResolverFactoryImpl;
import org.ums.lock.LockDao;
import org.ums.lock.LockManager;
import org.ums.manager.*;
import org.ums.manager.common.CountryManager;
import org.ums.manager.library.*;
import org.ums.message.MessageResource;
import org.ums.persistent.dao.*;
import org.ums.persistent.dao.common.PersistentCountryDao;
import org.ums.persistent.dao.library.*;
import org.ums.security.authentication.UMSAuthenticationRealm;
import org.ums.services.LoginService;
import org.ums.services.NotificationGenerator;
import org.ums.services.NotificationGeneratorImpl;
import org.ums.solr.repository.EmployeeRepository;
import org.ums.solr.repository.transaction.EmployeeTransaction;
import org.ums.statistics.DBLogger;
import org.ums.statistics.JdbcTemplateFactory;
import org.ums.statistics.QueryLogger;
import org.ums.statistics.TextLogger;
import org.ums.util.Constants;

@Configuration
@EnableAsync
@EnableScheduling
@EnableSolrRepositories(basePackages = "org.ums.solr.repository", multicoreSupport = true)
public class UMSContext {
  @Autowired
  DataSource mDataSource;

  @Autowired
  CacheFactory mCacheFactory;

  @Autowired
  PasswordService mPasswordService;

  @Autowired
  JdbcTemplateFactory mTemplateFactory;

  @Autowired
  @Qualifier("fileContentManager")
  BinaryContentManager<byte[]> mBinaryContentManager;

  @Autowired
  UMSAuthenticationRealm mAuthenticationRealm;

  @Autowired
  UMSConfiguration mUMSConfiguration;

  @Autowired
  MessageResource mMessageResource;

  @Autowired
  @Qualifier("mongoTemplate")
  @Lazy
  MongoTemplate mMongoOperations;

  @Autowired
  @Qualifier("backendSecurityManager")
  SecurityManager mSecurityManager;

  @Autowired
  IdGenerator mIdGenerator;

  @Autowired
  @Lazy
  EmployeeRepository mEmployeeRepository;

  @Bean
  LibraryManager libraryManager() {
    LibraryCache libraryCache = new LibraryCache(mCacheFactory.getCacheManager());
    libraryCache.setManager(new PersistentLibraryDao(mTemplateFactory.getJdbcTemplate()));
    return libraryCache;
  }

  @Bean
  AdmissionTotalSeatManager admissionTotalSeatManager() {
    AdmissionTotalSeatCache admissionTotalSeatCache = new AdmissionTotalSeatCache(mCacheFactory.getCacheManager());
    admissionTotalSeatCache.setManager(new PersistentAdmissionTotalSeatDao(mTemplateFactory.getJdbcTemplate()));
    return admissionTotalSeatCache;
  }

  @Bean
  PaymentInfoManager paymentInfoManager() {
    PaymentInfoCache paymentInfoCache = new PaymentInfoCache(mCacheFactory.getCacheManager());
    paymentInfoCache.setManager(new PersistentPaymentInfoDao(mTemplateFactory.getJdbcTemplate()));
    return paymentInfoCache;
  }

  @Bean
  SemesterManager semesterManager() {
    SemesterCache semesterCache = new SemesterCache(mCacheFactory.getCacheManager());
    semesterCache.setManager(new PersistentSemesterDao(mTemplateFactory.getJdbcTemplate()));
    return semesterCache;
  }

  @Bean
  ClassRoomManager classRoomManager() {
    ClassRoomCache classRoomCache = new ClassRoomCache(mCacheFactory.getCacheManager());
    classRoomCache.setManager(new PersistentClassRoomDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return classRoomCache;
  }

  @Bean
  AdmissionStudentManager admissionStudentManager() {
    AdmissionStudentCache admissionStudentCache = new AdmissionStudentCache(mCacheFactory.getCacheManager());
    admissionStudentCache.setManager(new PersistentAdmissionStudentDao(mTemplateFactory.getJdbcTemplate()));
    return admissionStudentCache;
  }

  @Bean
  AdmissionMeritListManager admissionMeritListManager() {
    AdmissionMeritListCache admissionMeritListCache = new AdmissionMeritListCache(mCacheFactory.getCacheManager());
    admissionMeritListCache.setManager(new PersistentAdmissionMeritListDao(mTemplateFactory.getJdbcTemplate()));
    return admissionMeritListCache;
  }

  @Bean
  AdmissionAllTypesOfCertificateManager admissionStudentCertificateManager() {
    AdmissionAllTypesOfCertificateCache admissionStudentCertificateCache =
        new AdmissionAllTypesOfCertificateCache(mCacheFactory.getCacheManager());
    admissionStudentCertificateCache.setManager(new PersistentAdmissionAllTypesOfCertificateDao(mTemplateFactory
        .getJdbcTemplate()));
    return admissionStudentCertificateCache;
  }

  @Bean
  AdmissionCertificatesOfStudentManager admissionStudentsCertificateHistoryManager() {
    AdmissionCertificatesOfStudentCache admissionStudentsCertificateHistoryCache =
        new AdmissionCertificatesOfStudentCache(mCacheFactory.getCacheManager());
    admissionStudentsCertificateHistoryCache.setManager(new PersistentAdmissionCertificatesOfStudentDao(
        mTemplateFactory.getJdbcTemplate()));
    return admissionStudentsCertificateHistoryCache;
  }

  @Bean
  AdmissionCommentForStudentManager admissionStudentsCertificateCommentManager() {
    AdmissionCommentForStudentCache admissionStudentsCertificateCommentCache =
        new AdmissionCommentForStudentCache(mCacheFactory.getCacheManager());
    admissionStudentsCertificateCommentCache.setManager(new PersistentAdmissionCommentForStudentDao(mTemplateFactory
        .getJdbcTemplate()));
    return admissionStudentsCertificateCommentCache;
  }

  @Bean
  FacultyManager facultyManager() {
    FacultyCache facultyCache = new FacultyCache(mCacheFactory.getCacheManager());
    facultyCache.setManager(new PersistentFacultyDao(mTemplateFactory.getJdbcTemplate()));
    return facultyCache;
  }

  @Bean
  @Lazy
  EmployeeManager employeeManager() {
    EmployeeTransaction employeeTransaction = new EmployeeTransaction();
    PersistentEmployeeDao persistentEmployeeDao = new PersistentEmployeeDao(mTemplateFactory.getJdbcTemplate());
    employeeTransaction.setManager(persistentEmployeeDao);
    EmployeeCache employeeCache = new EmployeeCache(mCacheFactory.getCacheManager());
    employeeCache.setManager(employeeTransaction);
    return employeeCache;
  }

  @Bean
  SemesterWithDrawalManager semesterWithdrawalManager() {
    SemesterWithdrawalCache semesterWithdrawalCache = new SemesterWithdrawalCache(mCacheFactory.getCacheManager());
    semesterWithdrawalCache.setManager(new PersistentSemesterWithdrawalDao(mTemplateFactory.getJdbcTemplate(),
        mIdGenerator));
    return semesterWithdrawalCache;
  }

  @Bean
  SemesterWithdrawalLogManager semesterWithdrawalLogManager() {
    SemesterWithdrawalLogCache semesterWithdrawalLogCache =
        new SemesterWithdrawalLogCache(mCacheFactory.getCacheManager());
    semesterWithdrawalLogCache.setManager(new PersistentSemesterWithdrawalLogDao(mTemplateFactory.getJdbcTemplate(),
        mIdGenerator));
    return semesterWithdrawalLogCache;
  }

  @Bean
  SeatPlanPublishManager seatPlanPublishManager() {
    SeatPlanPublishCache seatPlanPublishCache = new SeatPlanPublishCache(mCacheFactory.getCacheManager());
    seatPlanPublishCache.setManager(new PersistentSeatPlanPublishDao(mTemplateFactory.getJdbcTemplate()));
    return seatPlanPublishCache;
  }

  @Bean
  SubGroupCCIManager subGroupCCIManager() {
    SubGroupCCICache subGroupCCICache = new SubGroupCCICache(mCacheFactory.getCacheManager());
    subGroupCCICache.setManager(new PersistentSubGroupCCIDao(mTemplateFactory.getJdbcTemplate()));
    return subGroupCCICache;
  }

  @Bean
  SubGroupManager subGroupManager() {
    SubGroupCache subGroupCache = new SubGroupCache(mCacheFactory.getCacheManager());
    subGroupCache.setManager(new PersistentSubGroupDao(mTemplateFactory.getJdbcTemplate()));
    return subGroupCache;
  }

  @Bean
  SeatPlanManager seatPlanManager() {
    SeatPlanCache seatPlanCache = new SeatPlanCache(mCacheFactory.getCacheManager());
    seatPlanCache.setManager(new PersistentSeatPlanDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return seatPlanCache;
  }

  @Bean
  ApplicationCCIManager applicationCCIManager() {
    ApplicationCCICache applicationCCICache = new ApplicationCCICache(mCacheFactory.getCacheManager());
    applicationCCICache.setManager(new PersistentApplicationCCIDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return applicationCCICache;
  }

  @Bean
  SemesterSyllabusMapManager semesterSyllabusMapManager() {
    SemesterSyllabusMapCache semesterSyllabusMapCache = new SemesterSyllabusMapCache(mCacheFactory.getCacheManager());
    semesterSyllabusMapCache.setManager(new PersistentSemesterSyllabusMapDao(new JdbcTemplate(mDataSource),
        syllabusManager()));
    return semesterSyllabusMapCache;
  }

  @Bean
  DateFormat getGenericDateFormat() {
    return new DateFormat(Constants.DATE_FORMAT);
  }

  @Bean
  ProgramTypeManager programTypeManager() {
    ProgramTypeCache programTypeCache = new ProgramTypeCache(mCacheFactory.getCacheManager());
    programTypeCache.setManager(new PersistentProgramTypeDao(mTemplateFactory.getJdbcTemplate()));
    return programTypeCache;
  }

  @Bean
  ProgramManager programManager() {
    ProgramCache programCache = new ProgramCache(mCacheFactory.getCacheManager());
    programCache.setManager(new PersistentProgramDao(mTemplateFactory.getJdbcTemplate()));
    return programCache;
  }

  @Bean
  DepartmentManager departmentManager() {
    DepartmentCache departmentCache = new DepartmentCache(mCacheFactory.getCacheManager());
    departmentCache.setManager(new PersistentDepartmentDao(mTemplateFactory.getJdbcTemplate()));
    return departmentCache;
  }

  @Bean
  SyllabusManager syllabusManager() {
    SyllabusCache syllabusCache = new SyllabusCache(mCacheFactory.getCacheManager());
    syllabusCache.setManager(new PersistentSyllabusDao(mTemplateFactory.getJdbcTemplate()));
    return syllabusCache;
  }

  @Bean
  CourseGroupManager courseGroupManager() {
    CourseGroupCache courseGroupCache = new CourseGroupCache(mCacheFactory.getCacheManager());
    courseGroupCache.setManager(new PersistentCourseGroupDao(mTemplateFactory.getJdbcTemplate()));
    return courseGroupCache;
  }

  @Bean
  CourseManager courseManager() {
    CourseCache courseCache = new CourseCache(mCacheFactory.getCacheManager());
    courseCache.setManager(new PersistentCourseDao(mTemplateFactory.getJdbcTemplate()));
    return courseCache;
  }

  @Bean
  RoleManager roleManager() {
    RoleCache roleCache = new RoleCache(mCacheFactory.getCacheManager());
    roleCache.setManager(new PersistentRoleDao(mTemplateFactory.getJdbcTemplate()));
    return roleCache;
  }

  @Bean
  StudentManager studentManager() {
    StudentCache studentCache = new StudentCache(mCacheFactory.getCacheManager());
    studentCache.setManager(new PersistentStudentDao(mTemplateFactory.getJdbcTemplate()));
    return studentCache;
  }

  @Bean
  UserManager userManager() {
    UserCache userCache = new UserCache(mCacheFactory.getCacheManager());
    userCache.setManager(new PersistentUserDao(mTemplateFactory.getJdbcTemplate()));
    UserPropertyResolver userPropertyResolver = new UserPropertyResolver(employeeManager(), studentManager());
    userPropertyResolver.setManager(userCache);
    return userPropertyResolver;
  }

  @Bean
  TeacherManager teacherManager() {
    TeacherCache teacherCache = new TeacherCache(mCacheFactory.getCacheManager());
    teacherCache.setManager(new PersistentTeacherDao(mTemplateFactory.getJdbcTemplate()));
    return teacherCache;
  }

  @Bean
  AppSettingManager appSettingManager() {
    AppSettingCache appSettingCache = new AppSettingCache(mCacheFactory.getCacheManager());
    appSettingCache.setManager(new PersistentAppSettingDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return appSettingCache;
  }

  @Bean
  CourseTeacherManager courseTeacherManager() {
    CourseTeacherCache courseTeacherCache = new CourseTeacherCache(mCacheFactory.getCacheManager());
    courseTeacherCache.setManager(new PersistentCourseTeacherDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return courseTeacherCache;
  }

  @Bean
  ExaminerManager examinerManager() {
    ExaminerCache examinerCache = new ExaminerCache(mCacheFactory.getCacheManager());
    examinerCache.setManager(new PersistentExaminerDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return examinerCache;
  }

  @Bean
  SpStudentManager spStudentManager() {
    SpStudentCache spStudentCache = new SpStudentCache(mCacheFactory.getCacheManager());
    spStudentCache.setManager(new PersistentSpStudentDao(mTemplateFactory.getJdbcTemplate()));
    return spStudentCache;
  }

  @Bean
  PermissionManager permissionManager() {
    PermissionCache permissionCache = new PermissionCache(mCacheFactory.getCacheManager());
    permissionCache.setManager(new PersistentPermissionDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return permissionCache;
  }

  @Bean
  SeatPlanGroupManager seatPlanGroupManager() {
    SeatPlanGroupCache seatPlanGroupCache = new SeatPlanGroupCache(mCacheFactory.getCacheManager());
    seatPlanGroupCache.setManager(new PersistentSeatPlanGroupDao(mTemplateFactory.getJdbcTemplate()));
    return seatPlanGroupCache;
  }

  @Bean
  NavigationManager navigationManager() {
    NavigationByPermissionResolver navigationByPermissionResolver =
        new NavigationByPermissionResolver(mAuthenticationRealm);
    NavigationCache navigationCache = new NavigationCache(mCacheFactory.getCacheManager());
    navigationCache.setManager(new PersistentNavigationDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    navigationByPermissionResolver.setManager(navigationCache);
    return navigationByPermissionResolver;
  }

  @Bean
  AdditionalRolePermissionsManager additionalRolePermissionsManager() {
    AdditionalRolePermissionsCache additionalRolePermissionsCache =
        new AdditionalRolePermissionsCache(mCacheFactory.getCacheManager());
    additionalRolePermissionsCache.setManager(new AdditionalRolePermissionsDao(mTemplateFactory.getJdbcTemplate(),
        mIdGenerator));
    return additionalRolePermissionsCache;
  }

  @Bean
  StudentRecordManager studentRecordManager() {
    StudentRecordCache studentRecordCache = new StudentRecordCache(mCacheFactory.getCacheManager());
    studentRecordCache.setManager(new PersistentStudentRecordDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return studentRecordCache;
  }

  @Bean
  SemesterEnrollmentManager semesterEnrollmentManager() {
    SemesterEnrollmentCache semesterEnrollmentCache = new SemesterEnrollmentCache(mCacheFactory.getCacheManager());
    semesterEnrollmentCache.setManager(new PersistentSemesterEnrollmentDao(mTemplateFactory.getJdbcTemplate(),
        mIdGenerator));
    return semesterEnrollmentCache;
  }

  @Bean
  EnrollmentFromToManager enrollmentFromToManager() {
    EnrollmentFromToCache enrollmentFromToCache = new EnrollmentFromToCache(mCacheFactory.getCacheManager());
    enrollmentFromToCache
        .setManager(new PersistentEnrollmentFromToDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return enrollmentFromToCache;
  }

  ClassRoomManager getPersistentClassRoomDao() {
    return new PersistentClassRoomDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  /*
   * @Bean ClassRoomManager classRoomManager() { return new
   * PersistentClassRoomDao(mTemplateFactory.getJdbcTemplate()); }
   */

  @Bean
  ExamRoutineManager examRoutineManager() {
    return new PersistentExamRoutineDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  PersistentOptionalCourseApplicationDao persistentOptionalCourseApplicationDao() {
    return new PersistentOptionalCourseApplicationDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  PersistentSemesterWiseCrHrDao persistentSemesterWiseCrHrDao() {
    return new PersistentSemesterWiseCrHrDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  LoginService loginService() {
    return new LoginService();
  }

  @Bean
  RoutineManager routineManager() {
    return new PersistentRoutineDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  ParameterManager parameterManager() {
    return new PersistentParameterDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  ParameterSettingManager parameterSettingManager() {
    return new PersistentParameterSettingDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  BearerAccessTokenManager bearerAccessTokenManager() {
    BearerAccessTokenCache bearerAccessTokenCache = new BearerAccessTokenCache(mCacheFactory.getCacheManager());
    bearerAccessTokenCache.setManager(new BearerAccessTokenDao(mTemplateFactory.getJdbcTemplate()));
    return bearerAccessTokenCache;
  }

  @Bean
  ExamGradeManager examGradeManager() {
    return new PersistentExamGradeDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  QueryLogger dbLogger() {
    return new DBLogger();
  }

  @Bean
  QueryLogger textLogger() {
    return new TextLogger();
  }

  @Bean
  UGRegistrationResultManager registrationResultManager() {
    UGRegistrationResultAggregator resultAggregator =
        new UGRegistrationResultAggregator(equivalentCourseManager(), taskStatusManager(), semesterManager());
    UGRegistrationResultCache registrationResultCache = new UGRegistrationResultCache(mCacheFactory.getCacheManager());
    registrationResultCache.setManager(new PersistentUGRegistrationResultDao(mTemplateFactory.getJdbcTemplate(),
        mIdGenerator));
    resultAggregator.setManager(registrationResultCache);
    return resultAggregator;
  }

  @Bean
  SeatPlanReportManager seatPlanReportManager() {
    return new PersistentSeatPlanReportDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  UGTheoryMarksManager theoryMarksManager() {
    return new PersistentUGTheoryMarksDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  UGSessionalMarksManager sessionalMarksManager() {
    return new PersistentUGSessionalMarksDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  XlsGenerator xlsGenerator() {
    return new JxlsGenerator();
  }

  @Bean
  @Lazy
  BinaryContentManager<byte[]> courseMaterialFileManagerForTeacher() {
    FileContentPermission fileContentPermission =
        new FileContentPermission(userManager(), bearerAccessTokenManager(), mUMSConfiguration, mMessageResource);
    CourseMaterialNotifier notifier =
        new CourseMaterialNotifier(userManager(), notificationGenerator(), registrationResultManager(),
            mUMSConfiguration, mMessageResource, courseTeacherManager(), bearerAccessTokenManager());
    fileContentPermission.setManager(notifier);
    notifier.setManager(mBinaryContentManager);
    return fileContentPermission;
  }

  @Bean
  @Lazy
  BinaryContentManager<byte[]> courseMaterialFileManagerForStudent() {
    StudentFileContentPermission fileContentPermission =
        new StudentFileContentPermission(userManager(), bearerAccessTokenManager(), mUMSConfiguration,
            mMessageResource, studentManager(), registrationResultManager(), courseTeacherManager());
    fileContentPermission.setManager(mBinaryContentManager);
    return fileContentPermission;
  }

  @Bean
  @Lazy
  NotificationManager notificationManager() {
    return mUMSConfiguration.isEnableObjectDb() ? new PersistentObjectNotificationDao(mMongoOperations)
        : new PersistentNotificationDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  @Lazy
  NotificationGenerator notificationGenerator() {
    return new NotificationGeneratorImpl(notificationManager(), mIdGenerator);
  }

  @Bean
  EquivalentCourseManager equivalentCourseManager() {
    EquivalentCourseCache equivalentCourseCache = new EquivalentCourseCache(mCacheFactory.getCacheManager());
    equivalentCourseCache.setManager(new EquivalentCourseDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return equivalentCourseCache;
  }

  @Bean
  MarksSubmissionStatusManager marksSubmissionStatusManager() {
    MarksSubmissionStatusCache cache = new MarksSubmissionStatusCache(mCacheFactory.getCacheManager());
    MarksSubmissionStatusAggregator aggregator = new MarksSubmissionStatusAggregator();
    cache.setManager(aggregator);
    aggregator.setManager(new PersistentMarkSubmissionStatusDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return cache;
  }

  @Bean
  TaskStatusManager taskStatusManager() {
    return new PersistentTaskStatusDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  ResultPublishManager resultPublishManager() {
    ResultPublishValidator validator = new ResultPublishValidator(marksSubmissionStatusManager());
    ResultPublishImpl resultPublish = new ResultPublishImpl();
    validator.setManager(resultPublish);
    resultPublish.setManager(new ResultPublishDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return validator;
  }

  @Bean
  ClassAttendanceManager classAttendanceManager() {
    return new PersistentClassAttendanceDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  UserGuideManager userGuideManager() {
    UserGuideCache userGuideCache = new UserGuideCache(mCacheFactory.getCacheManager());
    userGuideCache.setManager(new PersistentUserGuideDao(mTemplateFactory.getJdbcTemplate()));
    return userGuideCache;
  }

  @Bean
  FeeCategoryManager feeCategoryManager() {
    FeeCategoryCache feeCategoryCache = new FeeCategoryCache(mCacheFactory.getCacheManager());
    feeCategoryCache.setManager(new PersistentFeeCategoryDao(mTemplateFactory.getJdbcTemplate()));
    return feeCategoryCache;
  }

  @Bean
  UGFeeManager feeManager() {
    UGFeeCache feeCache = new UGFeeCache(mCacheFactory.getCacheManager());
    feeCache.setManager(new PersistentUGFeeDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return feeCache;
  }

  @Bean
  public SolrClient solrClient() {
    return new HttpSolrClient("http://localhost:8983/solr/");
  }

  @Bean
  public SolrTemplate solrTemplate(SolrClient client) throws Exception {
    SolrTemplate template = new SolrTemplate(client);
    template.setSolrConverter(new SolrJConverter());
    return template;
  }

  @Bean
  AuthorManager authorManager() {
    AuthorCache authorCache = new AuthorCache(mCacheFactory.getCacheManager());
    authorCache.setManager(new PersistentAuthorDao(mTemplateFactory.getLmsJdbcTemplate()));
    return authorCache;
  }

  @Bean
  CountryManager countryManager() {
    CountryCache countryCache = new CountryCache(mCacheFactory.getCacheManager());
    countryCache.setManager(new PersistentCountryDao(mTemplateFactory.getJdbcTemplate()));
    return countryCache;
  }

  @Bean
  SupplierManager supplierManager() {
    SupplierCache supplierCache = new SupplierCache(mCacheFactory.getCacheManager());
    supplierCache.setManager(new PersistentSupplierDao(mTemplateFactory.getLmsJdbcTemplate(), mIdGenerator));
    return supplierCache;
  }

  @Bean
  PublisherManager publisherManager() {
    PublisherCache publisherCache = new PublisherCache(mCacheFactory.getCacheManager());
    publisherCache.setManager(new PersistentPublisherDao(mTemplateFactory.getLmsJdbcTemplate()));
    return publisherCache;
  }

  @Bean
  RecordManager recordManager() {
    RecordCache recordCache = new RecordCache(mCacheFactory.getCacheManager());
    recordCache.setManager(new PersistentRecordDao(mTemplateFactory.getLmsJdbcTemplate(), mIdGenerator));
    return recordCache;
  }

  @Bean
  ItemManager itemManager() {
    ItemCache itemCache = new ItemCache(mCacheFactory.getCacheManager());
    itemCache.setManager(new PersistentItemDao(mTemplateFactory.getLmsJdbcTemplate(), mIdGenerator));
    return itemCache;
  }

  @Bean
  IndexManager indexManager() {
    return new IndexDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  IndexConsumerManager indexConsumerManager() {
    return new IndexConsumerDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator);
  }

  @Bean
  LockManager lockManager() {
    return new LockDao(mTemplateFactory.getJdbcTemplate());
  }

  @Bean
  EntityResolverFactory entityResolverFactory() {
    return new EntityResolverFactoryImpl(employeeManager(), mEmployeeRepository);
  }

  @Bean
  ConsumeIndex consumeIndex() {
    return new ConsumeIndexJobImpl(indexManager(), indexConsumerManager(), entityResolverFactory(), lockManager());
  }

  @Bean
  CacheWarmerManager cacheWarmerManager() {
    return new CacheWarmerManagerImpl(mSecurityManager, mCacheFactory, mUMSConfiguration, departmentManager(),
        roleManager(), permissionManager(), bearerAccessTokenManager(), additionalRolePermissionsManager(),
        navigationManager(), employeeManager(), programTypeManager(), programManager(), semesterManager(),
        syllabusManager(), courseGroupManager(), equivalentCourseManager(), teacherManager(), courseTeacherManager(),
        examinerManager(), studentManager(), studentRecordManager(), classRoomManager(), courseManager(),
        marksSubmissionStatusManager(), userManager());
  }

  @Bean
  AutoCacheWarmer autoCacheWarmer() {
    return new AutoCacheWarmer(cacheWarmerManager());
  }

}
