package org.ums.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ums.cache.CacheFactory;
import org.ums.cache.common.DivisionCache;
import org.ums.cache.registrar.*;
import org.ums.generator.IdGenerator;
import org.ums.manager.common.DivisionManager;
import org.ums.manager.registrar.*;
import org.ums.persistent.dao.common.PersistentDivisionDao;
import org.ums.persistent.dao.registrar.*;
import org.ums.statistics.JdbcTemplateFactory;

@Configuration
public class RegistrarContext {

  @Autowired
  CacheFactory mCacheFactory;

  @Autowired
  JdbcTemplateFactory mTemplateFactory;

  @Autowired
  IdGenerator mIdGenerator;

  @Bean
  AcademicInformationManager academicInformationManager() {
    AcademicInformationCache academicInformationCache = new AcademicInformationCache(mCacheFactory.getCacheManager());
    academicInformationCache.setManager(new PersistentAcademicInformationDao(mTemplateFactory.getJdbcTemplate()));
    return academicInformationCache;
  }

  @Bean
  AwardInformationManager awardInformationManager() {
    AwardInformationCache awardInformationCache = new AwardInformationCache(mCacheFactory.getCacheManager());
    awardInformationCache.setManager(new PersistentAwardInformationDao(mTemplateFactory.getJdbcTemplate()));
    return awardInformationCache;
  }

  @Bean
  ServiceInformationManager serviceInformationManager() {
    ServiceInformationCache serviceInformationCache = new ServiceInformationCache(mCacheFactory.getCacheManager());
    serviceInformationCache.setManager(new PersistentServiceInformationDao(mTemplateFactory.getJdbcTemplate()));
    return serviceInformationCache;
  }

  @Bean
  ServiceInformationDetailManager serviceInformationDetailManager() {
    ServiceInformationDetailCache serviceInformationDetailCache =
        new ServiceInformationDetailCache(mCacheFactory.getCacheManager());
    serviceInformationDetailCache.setManager(new PersistentServiceInformationDetailDao(mTemplateFactory
        .getJdbcTemplate()));
    return serviceInformationDetailCache;
  }

  @Bean
  ExperienceInformationManager experienceInformationManager() {
    ExperienceInformationCache experienceInformationCache =
        new ExperienceInformationCache(mCacheFactory.getCacheManager());
    experienceInformationCache.setManager(new PersistentExperienceInformationDao(mTemplateFactory.getJdbcTemplate()));
    return experienceInformationCache;
  }

  @Bean
  PersonalInformationManager personalInformationManager() {
    PersonalInformationCache personalInformationCache = new PersonalInformationCache(mCacheFactory.getCacheManager());
    personalInformationCache.setManager(new PersistentPersonalInformationDao(mTemplateFactory.getJdbcTemplate()));
    return personalInformationCache;
  }

  @Bean
  PublicationInformationManager publicationInformationManager() {
    PublicationInformationCache publicationInformationCache =
        new PublicationInformationCache(mCacheFactory.getCacheManager());
    publicationInformationCache.setManager(new PersistentPublicationInformationDao(mTemplateFactory.getJdbcTemplate()));
    return publicationInformationCache;
  }

  @Bean
  TrainingInformationManager trainingInformationManager() {
    TrainingInformationCache trainingInformationCache = new TrainingInformationCache(mCacheFactory.getCacheManager());
    trainingInformationCache.setManager(new PersistentTrainingInformationDao(mTemplateFactory.getJdbcTemplate()));
    return trainingInformationCache;
  }

  @Bean
  AreaOfInterestInformationManager areaOfInterestInformationManager() {
    AreaOfInterestInformationCache areaOfInterestInformationCache =
        new AreaOfInterestInformationCache(mCacheFactory.getCacheManager());
    areaOfInterestInformationCache.setManager(new PersistentAreaOfInterestInformationDao(mTemplateFactory
        .getJdbcTemplate()));
    return areaOfInterestInformationCache;
  }

  @Bean
  AdditionalInformationManager additionalInformationManager() {
    AdditionalInformationCache additionalInformationCache =
        new AdditionalInformationCache(mCacheFactory.getCacheManager());
    additionalInformationCache.setManager(new PersistentAdditionalInformationDao(mTemplateFactory.getJdbcTemplate()));
    return additionalInformationCache;
  }

}
