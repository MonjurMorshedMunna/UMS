package org.ums.academic.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.ums.academic.builder.*;
import org.ums.academic.dao.*;
import org.ums.domain.model.*;
import org.ums.manager.ContentManager;
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

  ContentManager<Semester, MutableSemester, Integer> getPersistentSemesterDao() {
    return new PersistentSemesterDao(new JdbcTemplate(mDataSource), getGenericDateFormat());
  }

  ContentManager<Semester, MutableSemester, Integer> getSemesterCache() {
    ContentCache<Semester, MutableSemester, Integer> semesterCache = new ContentCache<>();
    semesterCache.setManager(getPersistentSemesterDao());
    return semesterCache;
  }

  ContentManager<Semester, MutableSemester, Integer> getSemesterAccessControl() {
    ContentAccessControl<Semester, MutableSemester, Integer> semesterAccessControl = new ContentAccessControl<>();
    semesterAccessControl.setManager(getSemesterCache());
    return semesterAccessControl;
  }


  @Bean
  ContentManager<Semester, MutableSemester, Integer> semesterManager() {
    ContentTransaction<Semester, MutableSemester, Integer> semesterTransaction = new ContentTransaction<>();
    semesterTransaction.setManager(getSemesterAccessControl());
    return semesterTransaction;
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
  DateFormat getGenericDateFormat() {
    return new SimpleDateFormat(Constants.DATE_FORMAT);
  }

  @Bean
  ContentManager<ProgramType, MutableProgramType, Integer> programTypeManager() {
    return new ProgramTypeDao(new JdbcTemplate(mDataSource));
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
  ContentManager<Program, MutableProgram, Integer> programManager() {
    return new PersistentProgramDao(new JdbcTemplate(mDataSource));
  }

  @Bean
  ContentManager<Department, MutableDepartment, Integer> departmentManager() {
    return new PersistentDepartmentDao(new JdbcTemplate(mDataSource));
  }

  @Bean
  ContentManager<Syllabus, MutableSyllabus, String> syllabusManager() {
    return new PersistentSyllabusDao(new JdbcTemplate(mDataSource));
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
}
