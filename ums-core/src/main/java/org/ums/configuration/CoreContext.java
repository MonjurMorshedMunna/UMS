package org.ums.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.ums.cache.*;
import org.ums.formatter.DateFormat;
import org.ums.generator.IdGenerator;
import org.ums.manager.*;
import org.ums.persistent.dao.*;
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
public class CoreContext {
  @Autowired
  CacheFactory mCacheFactory;

  @Autowired
  JdbcTemplateFactory mTemplateFactory;

  @Autowired
  IdGenerator mIdGenerator;

  @Autowired
  UMSConfiguration mUMSConfiguration;

  @Autowired
  @Qualifier("mongoTemplate")
  @Lazy
  MongoTemplate mMongoOperations;

  @Autowired
  UMSAuthenticationRealm mAuthenticationRealm;

  @Autowired
  @Lazy
  @Qualifier("employeeRepositoryImpl")
  EmployeeRepository mEmployeeRepository;

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
  AppSettingManager appSettingManager() {
    AppSettingCache appSettingCache = new AppSettingCache(mCacheFactory.getCacheManager());
    appSettingCache.setManager(new PersistentAppSettingDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return appSettingCache;
  }

  @Bean
  PermissionManager permissionManager() {
    PermissionCache permissionCache = new PermissionCache(mCacheFactory.getCacheManager());
    permissionCache.setManager(new PersistentPermissionDao(mTemplateFactory.getJdbcTemplate(), mIdGenerator));
    return permissionCache;
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
  LoginService loginService() {
    return new LoginService();
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
  QueryLogger dbLogger() {
    return new DBLogger();
  }

  @Bean
  QueryLogger textLogger() {
    return new TextLogger();
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
  TaskStatusManager taskStatusManager() {
    return new PersistentTaskStatusDao(mTemplateFactory.getJdbcTemplate());
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
  UserGuideManager userGuideManager() {
    UserGuideCache userGuideCache = new UserGuideCache(mCacheFactory.getCacheManager());
    userGuideCache.setManager(new PersistentUserGuideDao(mTemplateFactory.getJdbcTemplate()));
    return userGuideCache;
  }

  @Bean
  RoleManager roleManager() {
    RoleCache roleCache = new RoleCache(mCacheFactory.getCacheManager());
    roleCache.setManager(new PersistentRoleDao(mTemplateFactory.getJdbcTemplate()));
    return roleCache;
  }

  @Bean
  DateFormat getGenericDateFormat() {
    return new DateFormat(Constants.DATE_FORMAT);
  }
}
