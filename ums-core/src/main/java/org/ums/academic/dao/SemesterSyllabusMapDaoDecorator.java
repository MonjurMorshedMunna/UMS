package org.ums.academic.dao;

import org.ums.domain.model.dto.SemesterSyllabusMapDto;
import org.ums.domain.model.mutable.MutableSemesterSyllabusMap;
import org.ums.domain.model.regular.SemesterSyllabusMap;
import org.ums.manager.SemesterSyllabusMapManager;

import java.util.List;

/**
 * Created by Ifti on 08-Jan-16.
 */
public class SemesterSyllabusMapDaoDecorator extends ContentDaoDecorator<SemesterSyllabusMap, MutableSemesterSyllabusMap, Integer, SemesterSyllabusMapManager> implements SemesterSyllabusMapManager {
  @Override
  public List<SemesterSyllabusMap> getMapsByProgramSemester(final Integer pProgramId,final Integer pSemesterId) throws Exception {
    return getManager().getMapsByProgramSemester(pProgramId, pSemesterId);
  }

  public  SemesterSyllabusMap get(final Integer pMapId) throws Exception {
    return getManager().get(pMapId);
  }

  @Override
  public void copySyllabus(SemesterSyllabusMapDto pSemesterSyllabusMapDto) throws Exception {
    getManager().copySyllabus(pSemesterSyllabusMapDto);
  }
}
