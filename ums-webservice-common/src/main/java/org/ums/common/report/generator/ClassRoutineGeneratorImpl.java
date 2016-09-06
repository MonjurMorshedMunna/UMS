package org.ums.common.report.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteArray;
import org.apache.commons.lang.UnhandledException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ums.domain.model.immutable.*;
import org.ums.manager.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by My Pc on 30-Aug-16.
 */
@Service
public class ClassRoutineGeneratorImpl implements ClassRoutineGenerator {

  @Autowired
  TeacherManager mTeacherManager;

  @Autowired
  SemesterManager mSemesterManager;

  @Autowired
  DepartmentManager mDepartmentManager;

  @Autowired
  ProgramManager mProgramManager;

  @Autowired
  RoutineManager mRoutineManager;

  @Autowired
  AppSettingManager mAppSettingManager;

  @Autowired
  UserManager mUserManager;

  @Override
  public void createClassRoutineStudentReport(OutputStream pOutputStream) throws Exception, IOException, DocumentException {

  }

  @Override
  public void createClassRoutineTeacherReport(OutputStream pOutputStream) throws Exception, IOException, DocumentException {
    String teacherId= SecurityUtils.getSubject().getPrincipal().toString();
    User user = mUserManager.get(teacherId);
    Teacher teacher = mTeacherManager.get(user.getEmployeeId());

    List<Routine> routines = mRoutineManager.getTeacherRoutine(teacher.getId());

    Department department = mDepartmentManager.get(teacher.getDepartment().getId());

    List<Semester> semesterList = mSemesterManager.getAll().stream()
                                                    .filter(pSemester -> {
                                                      try{
                                                        return pSemester.getStatus().getValue()==1;
                                                      }catch(Exception e){
                                                        throw new UnhandledException(e);
                                                      }
                                                    }).collect(Collectors.toList());

    Semester semester = semesterList.get(0);

    Map<Integer,String> dateMap = new HashMap<>();
    dateMap.put(1,"SATURDAY");
    dateMap.put(2,"SUNDAY");
    dateMap.put(3,"MONDAY");
    dateMap.put(4,"TUESDAY");
    dateMap.put(5,"WEDNESDAY");
    dateMap.put(6,"THURSDAY");


    //AppSetting appSetting = mAppSettingManager.getAll();

    Map<String,String> appSettingMap = mAppSettingManager.getAll().stream()
                                                      .collect(Collectors.toMap(AppSetting::getParameterName,AppSetting::getParameterValue));


    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    Time startTime = new Time(timeFormat.parse(appSettingMap.get("class start time")).getTime());
    Time endTime =new Time(timeFormat.parse(appSettingMap.get("class end time")).getTime());
    int classDuration = Integer.parseInt(appSettingMap.get("class duration"));







    Document document = new Document();
    document.addTitle("Teacher's Routine");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfWriter writer = PdfWriter.getInstance(document,baos);

    Font tableDataFont = new Font(Font.FontFamily.TIMES_ROMAN,10);
    Font tableDataFontTime = new Font(Font.FontFamily.TIMES_ROMAN,11);

    Font universityNameFont = new Font(FontFactory.getFont(FontFactory.TIMES_BOLD,20));
    Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN,13);
    document.open();
    document.setPageSize(PageSize.A4.rotate());

    document.newPage();

    Paragraph paragraph = new Paragraph("Ahsanullah University of Science and Technology",universityNameFont);
    paragraph.setAlignment(Element.ALIGN_CENTER);

    document.add(paragraph);

    paragraph = new Paragraph(department.getLongName().toUpperCase(),headingFont);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    document.add(paragraph);

    paragraph = new Paragraph("Load Schedule ("+semester.getName()+")",headingFont);
    paragraph.setAlignment(Element.ALIGN_CENTER);

    document.add(paragraph);

    paragraph = new Paragraph("TEACHER'S NAME: "+ teacher.getName().toUpperCase(),headingFont);
    paragraph.setAlignment(Element.ALIGN_CENTER);

    document.add(paragraph);
    document.add(new Paragraph(" "));
    PdfPTable table = new PdfPTable(13);
    table.setWidthPercentage(105);
    PdfPCell dayTimeCell = new PdfPCell(new Paragraph("Time  /   Day",tableDataFontTime));
    table.addCell(dayTimeCell);

    Time timeInitializer = new Time(timeFormat.parse(appSettingMap.get("class start time")).getTime());

    while(true){
      if(timeInitializer.equals(endTime)){
        break;
      }else{
        DateFormat startTimeFormat = new SimpleDateFormat();
        Time tempTime =  new Time(timeInitializer.getTime()) ;
        tempTime.setTime(tempTime.getTime()+classDuration*60000);
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        final Date startTimeFormated = sdf.parse(timeInitializer.toString());
        final Date endTimeFormated = sdf.parse(tempTime.toString());
        PdfPCell cell = new PdfPCell(new Paragraph(  new SimpleDateFormat("hh:mm a").format(startTimeFormated)+"-"+new SimpleDateFormat("hh:mm a").format(endTimeFormated),tableDataFontTime));
        timeInitializer.setTime(timeInitializer.getTime()+classDuration * 60000);
        //timeInitializer = timeInitializer
        table.addCell(cell);
      }
    }

    SimpleDateFormat routineTimeFormat= new SimpleDateFormat("hh.mm a");

    Time routineTime = new Time(routineTimeFormat.parse(routines.get(0).getStartTime()).getTime());
    for(int i=1;i<=6;i++){
      PdfPCell dayCell = new PdfPCell();
      Paragraph dayParagraph = new Paragraph(dateMap.get(i));
      dayParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,9));
      dayCell.addElement(dayParagraph);
      table.addCell(dayCell);

      timeInitializer =  new Time(timeFormat.parse(appSettingMap.get("class start time")).getTime());

      while(true){
        if(timeInitializer.equals(endTime)){
          break;
        }else{
          if(routines.size()!=0){
            if(routineTime.equals(timeInitializer) && routines.get(0).getDay()==i){
              String courseNo = routines.get(0).getCourseNo();
              String courseId= routines.get(0).getCourseId();
              String sections= routines.get(0).getSection();
              String roomNo = routines.get(0).getRoomNo();
              int duration  = routines.get(0).getDuration();

              int routineIterator=1;
              while(true){
                if(routines.size()>1){
                  if(routines.get(routineIterator).getCourseId().equals(courseId) && routines.get(routineIterator).getDay()==i){
                    sections = sections+"+"+routines.get(routineIterator).getSection();
                    routines.remove(0);
                  }else{
                    break;
                  }
                }else{
                  break;
                }

              }

              Paragraph upperParagraph = new Paragraph(courseNo+"("+sections+")",tableDataFont);
              Paragraph lowerParagraph = new Paragraph(roomNo,tableDataFont);

              upperParagraph.setAlignment(Element.ALIGN_CENTER);
              lowerParagraph.setAlignment(Element.ALIGN_CENTER);
              PdfPCell cell = new PdfPCell();
              cell.addElement(upperParagraph);
              cell.addElement(lowerParagraph);
              cell.setColspan(duration);
              table.addCell(cell);
              if(routines.size()!=0){
                routines.remove(0);
                if(routines.size()!=0){
                  routineTime =new Time(routineTimeFormat.parse(routines.get(0).getStartTime()).getTime());
                }
              }
              timeInitializer.setTime(timeInitializer.getTime()+(duration*classDuration) * 60000);

            }else{
              PdfPCell cell = new PdfPCell();
              cell.addElement(new Paragraph(" "));
              cell.addElement(new Paragraph(" "));

              table.addCell(cell);
              timeInitializer.setTime(timeInitializer.getTime()+classDuration * 60000);
            }
          }
         else{
            PdfPCell cell = new PdfPCell();
            cell.addElement(new Paragraph(" "));
            cell.addElement(new Paragraph(" "));

            table.addCell(cell);
            timeInitializer.setTime(timeInitializer.getTime()+classDuration * 60000);
          }

        }
      }


    }

    document.add(table);

    document.close();
    baos.writeTo(pOutputStream);

  }
}
