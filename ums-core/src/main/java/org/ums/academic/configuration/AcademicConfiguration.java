package org.ums.academic.configuration;

import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.ums.academic.builder.*;
import org.ums.academic.dao.*;
import org.ums.academic.model.PersistentRoutine;
import org.ums.authentication.UMSAuthenticationRealm;
import org.ums.domain.model.dto.MutableSemesterSyllabusMapDto;
import org.ums.domain.model.dto.SemesterSyllabusMapDto;
import org.ums.domain.model.mutable.*;
import org.ums.domain.model.readOnly.*;
import org.ums.manager.*;
import org.ums.services.LoginService;
import org.ums.util.Constants;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AcademicConfiguration {
  @Autowired
  DataSource mDataSource;

  @Autowired
  @Qualifier("localCache")
  CacheManager mLocalCacheManager;

  @Autowired
  PasswordService mPasswordService;

  @Autowired
  JdbcTemplate mJdbcTemplate;

  @Autowired
  private VelocityEngine velocityEngine;

  @Autowired
  BinaryContentManager<byte[]> mBinaryContentManager;

  @Autowired
  UMSAuthenticationRealm mAuthenticationRealm;

  SemesterManager getPersistentSemesterDao() {
    return new PersistentSemesterDao(mJdbcTemplate, getGenericDateFormat());
  }

  SemesterSyllabusMapManager getPersistentSemesterSyllabusMapDao() {
    return new PersistentSemesterSyllabusMapDao(new JdbcTemplate(mDataSource), syllabusManager());
  }

  SyllabusManager getPersistentSyllabusDao() {
    return new PersistentSyllabusDao(mJdbcTemplate);
  }

  @Bean
  ContentManager<Semester, MutableSemester, Integer> getSemesterCache() {
    SemesterCache semesterCache = new SemesterCache(mLocalCacheManager);
    semesterCache.setManager(getPersistentSemesterDao());
    return semesterCache;
  }

  ContentManager<Semester, MutableSemester, Integer> getSemesterAccessControl() {
    ContentAccessControl semesterAccessControl = new ContentAccessControl<>();
    semesterAccessControl.setManager(getSemesterCache());
    return semesterAccessControl;
  }


  @Bean
  SemesterManager semesterManager() {
/*    ContentTransaction<Semester, MutableSemester, Integer> semesterTransaction = new ContentTransaction<>();
    semesterTransaction.setManager(getSemesterAccessControl());*/
    SemesterCache semesterCache = new SemesterCache(mLocalCacheManager);
    semesterCache.setManager(getPersistentSemesterDao());
    return semesterCache;
  }

  @Bean
  SemesterSyllabusMapManager semesterSyllabusMapManager() {
    return getPersistentSemesterSyllabusMapDao();
  }

  @Bean
  DateFormat getGenericDateFormat() {
    return new SimpleDateFormat(Constants.DATE_FORMAT);
  }

  @Bean
  ContentManager<ProgramType, MutableProgramType, Integer> programTypeManager() {
    return new PersistentProgramTypeDao(mJdbcTemplate);
  }

  @Bean
  ContentManager<Program, MutableProgram, Integer> programManager() {
    return new PersistentProgramDao(mJdbcTemplate);
  }

  @Bean
  ContentManager<Department, MutableDepartment, String> departmentManager() {
    return new PersistentDepartmentDao(mJdbcTemplate);
  }

  @Bean
  SyllabusManager syllabusManager() {
    return getPersistentSyllabusDao();
  }

  @Bean
  CourseGroupManager courseGroupManager() {
    return new PersistentCourseGroupDao(mJdbcTemplate);
  }

  @Bean
  RoutineManager routineManager(){
    return new PersistentRoutineDao(mJdbcTemplate);
  }
  @Bean
  CourseManager courseManager() {
    CourseCache courseCache = new CourseCache(mLocalCacheManager);
    courseCache.setManager(new PersistentCourseDao(mJdbcTemplate));
    return courseCache;
  }

  @Bean
  ContentManager<Role, MutableRole, Integer> roleManager() {
    return new PersistentRoleDao(mJdbcTemplate);
  }

  @Bean(name = "studentManager")
  ContentManager<Student, MutableStudent, String> studentManager() {
    return new PersistentStudentDao(mJdbcTemplate, getGenericDateFormat());
  }

  @Bean(name = "userManager")
  UserManager userManager() {
    return new PersistentUserDao(mJdbcTemplate);
  }

  @Bean
  TeacherManager teacherManager() {
    return new PersistentTeacherDao(mJdbcTemplate);
  }

  @Bean
  CourseTeacherManager courseTeacherManager() {
    return new PersistentCourseTeacherDao(mJdbcTemplate);
  }

  @Bean
  PermissionManager permissionManager() {
    return new PersistentPermissionDao(mJdbcTemplate);
  }

  @Bean
  NavigationManager navigationManager() {
    NavigationByPermissionResolver navigationByPermissionResolver = new NavigationByPermissionResolver(mAuthenticationRealm);
    navigationByPermissionResolver.setManager(new PersistentNavigationDao(mJdbcTemplate));
    return navigationByPermissionResolver;
  }

  @Bean
  AdditionalRolePermissionsManager additionalRolePermissionsManager() {
    return new AdditionalRolePermissionsDao(mJdbcTemplate, getGenericDateFormat());
  }

  @Bean
  Builder<Program, MutableProgram> getProgramBuilder() {
    return new ProgramBuilder();
  }

  @Bean
  Builder<Department, MutableDepartment> getDepartmentBuilder() {
    return new DepartmentBuilder();
  }

  @Bean
  Builder<Syllabus, MutableSyllabus> getSyllabusBuilder() {
    return new SyllabusBuilder();
  }

  @Bean
  List<Builder<Program, MutableProgram>> getProgramBuilders() {
    return Arrays.asList(new ProgramBuilder());
  }

  @Bean
  List<Builder<Department, MutableDepartment>> getDepartmentBuilders() {
    return Arrays.asList(new DepartmentBuilder());
  }

  @Bean
  List<Builder<Syllabus, MutableSyllabus>> getSyllabusBuilders() {
    return Arrays.asList(new SyllabusBuilder());
  }

  @Bean
  Builder<ProgramType, MutableProgramType> getProgramTypeBuilder() {
    return new ProgramTypeBuilder();
  }

  @Bean
  List<Builder<ProgramType, MutableProgramType>> getProgramTypeBuilders() {
    return Arrays.asList(new ProgramTypeBuilder());
  }

  @Bean
  Builder<Semester, MutableSemester> getSemesterBuilder() {
    return new SemesterBuilder(getGenericDateFormat(), programTypeManager());
  }

  @Bean
  List<Builder<Semester, MutableSemester>> getSemesterBuilders() {
    List<Builder<Semester, MutableSemester>> builders = new ArrayList<>();
    builders.add(new SemesterBuilder(getGenericDateFormat(), programTypeManager()));
    return builders;
  }

  @Bean
  Builder<SemesterSyllabusMap, MutableSemesterSyllabusMap> getSemesterSyllabusMapBuilder() {
    return new SemesterSyllabusMapBuilder(semesterSyllabusMapManager());
  }

  @Bean
  List<Builder<SemesterSyllabusMap, MutableSemesterSyllabusMap>> getSemesterSyllabusMapBuilders() {
    List<Builder<SemesterSyllabusMap, MutableSemesterSyllabusMap>> builders = new ArrayList<>();
    builders.add(new SemesterSyllabusMapBuilder(semesterSyllabusMapManager()));
    return builders;
  }

  @Bean
  Builder<CourseGroup, MutableCourseGroup> getCourseGroupBuilder() {
    return new CourseGroupBuilder();
  }

  @Bean
  List<Builder<CourseGroup, MutableCourseGroup>> getCourseGroupBuilders() {
    return Arrays.asList(new CourseGroupBuilder());
  }

  @Bean
  Builder<Course, MutableCourse> getCourseBuilder() {
    return new CourseBuilder();
  }

  @Bean
  List<Builder<Course, MutableCourse>> getCourseBuilders() {
    return Arrays.asList(new CourseBuilder());
  }

  @Bean
  Builder<Student, MutableStudent> getStudentBuilder() {
    return new StudentBuilder(getGenericDateFormat(), mBinaryContentManager);
  }

  @Bean
  List<Builder<Student, MutableStudent>> getStudentBuilders() {
    return Arrays.asList(new StudentBuilder(getGenericDateFormat(), mBinaryContentManager));
  }

  @Bean
  Builder<SemesterSyllabusMapDto, MutableSemesterSyllabusMapDto> getSemesterSyllabusMapsBuilder() {
    return new SemesterSyllabusMapsBuilder();
  }

  @Bean
  List<Builder<SemesterSyllabusMapDto, MutableSemesterSyllabusMapDto>> getSemesterSyllabusMapsBuilders() {
    List<Builder<SemesterSyllabusMapDto, MutableSemesterSyllabusMapDto>> builders = new ArrayList<>();
    builders.add(new SemesterSyllabusMapsBuilder());
    return builders;
  }

  @Bean
  Builder<Teacher, MutableTeacher> getTeacherBuilder() {
    return new TeacherBuilder();
  }

  @Bean
  List<Builder<Teacher, MutableTeacher>> getTeacherBuilders() {
    List<Builder<Teacher, MutableTeacher>> builders = new ArrayList<>();
    builders.add(new TeacherBuilder());
    return builders;
  }

  @Bean
  Builder<CourseTeacher, MutableCourseTeacher> getCourseTeacherBuilder() {
    return new CourseTeacherBuilder(courseManager(), teacherManager(), semesterManager());
  }

  @Bean
  List<Builder<CourseTeacher, MutableCourseTeacher>> getCourseTeacherBuilders() {
    List<Builder<CourseTeacher, MutableCourseTeacher>> builders = new ArrayList<>();
    builders.add(new CourseTeacherBuilder(courseManager(), teacherManager(), semesterManager()));
    return builders;
  }
  ClassRoomManager getPersistentClassRoomDao() {
    return new PersistentClassRoomDao(mJdbcTemplate);
  }
  //@Bean(name = "classRoomManager")
  @Bean
  ClassRoomManager classRoomManager() {
    return new PersistentClassRoomDao(mJdbcTemplate);
  }
  //@Bean(name = "examRoutineManager")
  @Bean
  ExamRoutineManager examRoutineManager() {
    return new PersistentExamRoutineDao(mJdbcTemplate);
  }
  @Bean
  Builder<ClassRoom, MutableClassRoom> getClassRoomBuilder() {
    return new ClassRoomBuilder();
  }

  @Bean
  List<Builder<ClassRoom, MutableClassRoom>> getClassRoomBuilders() {
    return Arrays.asList(new ClassRoomBuilder());
  }

  @Bean
  Builder<ExamRoutine, MutableExamRoutine> getExamRoutineBuilder() {
    return new ExamRoutineBuilder();
  }


  @Bean
  Builder<Navigation, MutableNavigation> getNavigationBuilder() {
    return new NavigationBuilder(velocityEngine);
  }

  @Bean
  List<Builder<Navigation, MutableNavigation>> getNavigationBuilders() {
    List<Builder<Navigation, MutableNavigation>> builders = new ArrayList<>();
    builders.add(new NavigationBuilder(velocityEngine));
    return builders;
  }

  @Bean
  List<Builder<User, MutableUser>> getUserBuilders() {
    List<Builder<User, MutableUser>> builders = new ArrayList<>();
    builders.add(new UserBuilder());
    return builders;
  }

  @Bean
  Builder<User, MutableUser> getUserBuilder() {
    return new UserBuilder();
  }

  @Bean
  Builder<AdditionalRolePermissions, MutableAdditionalRolePermissions> getAdditionalRolePermissionsBuilder() {
    return new AdditionalRolePermissionsBuilder(navigationManager(), getGenericDateFormat());
  }

  @Bean
  List<Builder<AdditionalRolePermissions, MutableAdditionalRolePermissions>> getAdditionalRolePermissionsBuilders() {
    List<Builder<AdditionalRolePermissions, MutableAdditionalRolePermissions>> builders = new ArrayList<>();
    builders.add(getAdditionalRolePermissionsBuilder());
    return builders;
  }

  @Bean
  LoginService  loginService() {
    return new LoginService();
  }
}
