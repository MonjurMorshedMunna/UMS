package org.ums.common.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.Course;
import org.ums.domain.model.immutable.Department;
import org.ums.domain.model.immutable.Examiner;
import org.ums.domain.model.immutable.Teacher;
import org.ums.domain.model.mutable.MutableExaminer;
import org.ums.manager.CourseManager;
import org.ums.manager.SemesterManager;
import org.ums.manager.TeacherManager;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;

@Component
public class ExaminerBuilder implements Builder<Examiner, MutableExaminer> {
  @Autowired
  private CourseManager mCourseManager;
  @Autowired
  private TeacherManager mTeacherManager;
  @Autowired
  private SemesterManager mSemesterManager;

  @Override
  public void build(JsonObjectBuilder pBuilder, Examiner pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) throws Exception {
    if (pReadOnly.getId() != null) {
      pBuilder.add("id", pReadOnly.getId());
    }

    Course course = (Course) pLocalCache.cache(() -> pReadOnly.getCourse(),
        pReadOnly.getCourseId(), Course.class);

    pBuilder.add("courseId", course.getId());
    pBuilder.add("courseNo", course.getNo());
    pBuilder.add("courseTitle", course.getTitle());
    pBuilder.add("courseCrHr", course.getCrHr());
    pBuilder.add("year", course.getYear());
    pBuilder.add("semester", course.getSemester());
    pBuilder.add("syllabusId", course.getSyllabusId());

    if (!StringUtils.isEmpty(pReadOnly.getPreparerId())) {
      Teacher teacher = (Teacher) pLocalCache.cache(() -> pReadOnly.getPreparer(),
          pReadOnly.getPreparerId(), Teacher.class);

      pBuilder.add("preparerId", teacher.getId());
      pBuilder.add("preparerName", teacher.getName());
    }

    if (!StringUtils.isEmpty(pReadOnly.getScrutinizerId())) {
      Teacher teacher = (Teacher) pLocalCache.cache(() -> pReadOnly.getScrutinizer(),
          pReadOnly.getScrutinizerId(), Teacher.class);

      pBuilder.add("scrutinizerId", teacher.getId());
      pBuilder.add("scrutinizerName", teacher.getName());
    }

    Department department = (Department) pLocalCache.cache(() -> course.getOfferedBy(),
        course.getOfferedDepartmentId(), Department.class);

    pBuilder.add("courseOfferedByDepartmentId", department.getId());
    pBuilder.add("courseOfferedByDepartmentName", department.getShortName());
  }

  @Override
  public void build(MutableExaminer pMutable, JsonObject pJsonObject, LocalCache pLocalCache) throws Exception {
    if (pJsonObject.containsKey("id")) {
      pMutable.setId(pJsonObject.getInt("id"));
    }
    pMutable.setCourse(mCourseManager.get(pJsonObject.getString("courseId")));
    pMutable.setCourseId(pJsonObject.getString("courseId"));
    if (!pJsonObject.getString("preparerId").equalsIgnoreCase("-1")) {
      pMutable.setPreparer(mTeacherManager.get(pJsonObject.getString("preparerId")));
    }
    if (!pJsonObject.getString("scrutinizerId").equalsIgnoreCase("-1")) {
      pMutable.setScrutinizer(mTeacherManager.get(pJsonObject.getString("scrutinizerId")));
    }
    pMutable.setSemester(mSemesterManager.get(Integer.parseInt(pJsonObject.getString("semesterId"))));
    pMutable.setSemesterId(Integer.parseInt(pJsonObject.getString("semesterId")));
  }
}