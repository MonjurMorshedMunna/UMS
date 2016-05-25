package org.ums.common.academic.resource.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.cache.LocalCache;
import org.ums.common.academic.resource.ResourceHelper;
import org.ums.common.academic.resource.SemesterResource;
import org.ums.common.builder.ExamGradeBuilder;
import org.ums.common.builder.ExamRoutineBuilder;
import org.ums.domain.model.dto.ExamRoutineDto;
import org.ums.domain.model.dto.MarksSubmissionStatusDto;
import org.ums.domain.model.dto.OptionalCourseApplicationStatDto;
import org.ums.domain.model.dto.StudentGradeDto;
import org.ums.domain.model.immutable.Course;
import org.ums.domain.model.immutable.ExamGrade;
import org.ums.domain.model.immutable.ExamRoutine;
import org.ums.domain.model.mutable.MutableClassRoom;
import org.ums.domain.model.mutable.MutableExamGrade;
import org.ums.domain.model.mutable.MutableExamRoutine;
import org.ums.manager.ExamGradeManager;
import org.ums.manager.ExamRoutineManager;
import org.ums.persistent.model.PersistentClassRoom;
import org.ums.persistent.model.PersistentExamRoutine;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.json.*;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.StringReader;
import java.net.URI;
import java.util.List;

/**
 * Created by ikh on 4/29/2016.
 */
@Component
public class GradeSubmissionResourceHelper extends ResourceHelper<ExamGrade, MutableExamGrade, Object> {

    @Autowired
    private ExamGradeManager mManager;

    @Autowired
    private ExamGradeBuilder mBuilder;

    @Override
    protected Response post(JsonObject pJsonObject, UriInfo pUriInfo) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public ExamGradeManager getContentManager() {
        return mManager;
    }

    @Override
    public ExamGradeBuilder getBuilder() {
        return mBuilder;
    }

    @Override
    protected String getEtag(ExamGrade pReadonly) {
        return null;
    }


    public JsonObject getGradeList(final Integer pSemesterId, final Integer pExamType,final String pCourseId) throws Exception {
        List<StudentGradeDto> examGradeList = getContentManager().getAllGradeForTheoryCourse(pSemesterId,pCourseId, pExamType);

        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder children = Json.createArrayBuilder();

        JsonReader jsonReader;
        JsonObject object1;

        for (StudentGradeDto gradeDto : examGradeList) {
            jsonReader = Json.createReader(new StringReader(gradeDto.toString()));
            object1 = jsonReader.readObject();
            jsonReader.close();
            children.add(object1);
        }
        object.add("entries", children);




        MarksSubmissionStatusDto marksSubmissionStatusDto = getContentManager().getMarksSubmissionStatus(pSemesterId, pCourseId, pExamType);
        jsonReader = Json.createReader(new StringReader(marksSubmissionStatusDto.toString()));
        object1 = jsonReader.readObject();
        jsonReader.close();

        object.add("part_info",object1);

        return object.build();
    }


    public JsonObject getGradeSubmissionStatus(final Integer pSemesterId, final Integer pExamType) throws Exception {
        List<MarksSubmissionStatusDto> examGradeStatusList = getContentManager().getMarksSubmissionStatus(pSemesterId, pExamType);

        JsonObjectBuilder object = Json.createObjectBuilder();
        JsonArrayBuilder children = Json.createArrayBuilder();

        JsonReader jsonReader;
        JsonObject object1;

        for (MarksSubmissionStatusDto statusDto : examGradeStatusList) {
            jsonReader = Json.createReader(new StringReader(statusDto.toString()));
            object1 = jsonReader.readObject();
            jsonReader.close();
            children.add(object1);
        }
        object.add("entries", children);

        return object.build();
    }


    public Response saveGradeSheet(final JsonObject pJsonObject) throws Exception {
        List<StudentGradeDto> gradeList=getBuilder().build(pJsonObject);
        MarksSubmissionStatusDto partInfoDto=new MarksSubmissionStatusDto();
        getBuilder().build(partInfoDto,pJsonObject);

        int aa= getContentManager().updatePartInfo(partInfoDto.getSemesterId(),partInfoDto.getCourseId(),partInfoDto.getExamType(),partInfoDto.getTotal_part(),partInfoDto.getPart_a_total(),partInfoDto.getPart_b_total());
        boolean updateStatus = getContentManager().saveGradeSheet(11012016, "CID1",1, gradeList);

        Response.ResponseBuilder builder = Response.created(null);
        builder.status(Response.Status.CREATED);

        return builder.build();
    }




}
