package org.ums.common.builder;

import org.springframework.stereotype.Component;
import org.ums.academic.model.PersistentProgram;
import org.ums.academic.model.PersistentSemester;
import org.ums.cache.LocalCache;
import org.ums.domain.model.dto.MutableSemesterSyllabusMapDto;
import org.ums.domain.model.dto.SemesterSyllabusMapDto;
import org.ums.domain.model.mutable.MutableProgram;
import org.ums.domain.model.mutable.MutableSemester;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;

@Component
public class SemesterSyllabusMapsBuilder implements Builder<SemesterSyllabusMapDto, MutableSemesterSyllabusMapDto> {
  @Override
  public void build(JsonObjectBuilder pBuilder, SemesterSyllabusMapDto pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) throws Exception {

  }

  @Override
  public void build(MutableSemesterSyllabusMapDto pMutable, JsonObject pJsonObject, LocalCache pLocalCache) throws Exception {
    MutableSemester semester = new PersistentSemester();
    semester.setId(pJsonObject.getInt("semesterId"));
    pMutable.setAcademicSemester(semester);
    MutableSemester copySemester = new PersistentSemester();
    copySemester.setId(pJsonObject.getInt("copySemesterId"));
    pMutable.setCopySemester(copySemester);

    MutableProgram program = new PersistentProgram();
    program.setId(pJsonObject.getInt("programId"));
    pMutable.setProgram(program);
  }
}
