package org.ums.common.report.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ums.common.builder.SeatPlanBuilder;
import org.ums.domain.model.dto.ExamRoutineDto;
import org.ums.domain.model.immutable.*;
import org.ums.manager.*;
import org.ums.services.academic.SeatPlanService;

import javax.ws.rs.WebApplicationException;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by My Pc on 5/25/2016.
 */

@Service
public class SeatPlanReportGeneratorImpl implements SeatPlanReportGenerator{

  @Autowired
  private SeatPlanManager mManager;

  @Autowired
  private SeatPlanBuilder mBuilder;

  @Autowired
  private SeatPlanService mSeatPlanService;

  @Autowired
  private ClassRoomManager mRoomManager;

  @Autowired
  private SemesterManager mSemesterManager;

  @Autowired
  private SeatPlanGroupManager mSeatPlanGroupManager;

  @Autowired
  private ExamRoutineManager mExamRoutineManager;

  @Autowired
  private SeatPlanManager mSeatPlanManager;

  @Autowired
  private ProgramManager mProgramManager;

  @Autowired
  private SpStudentManager mSpStudentManager;

  @Autowired
  private CourseManager mCourseManager;



  public static final String DEST = "seat_plan_report.pdf";

  @Override
  public void createPdf(String dest, boolean noSeatPlanInfo, int pSemesterId, int groupNo, int type, String examDate, OutputStream pOutputStream) throws Exception, IOException, DocumentException,WebApplicationException {




    Document document = new Document();
    document.addTitle("Seat Plan");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfWriter writer = PdfWriter.getInstance(document,baos);
    MyFooter event = new MyFooter();
    writer.setPageEvent(event);
    document.open();
    document.setPageSize(PageSize.A4.rotate());


    document.newPage();

    Font f = new Font(Font.FontFamily.TIMES_ROMAN,10.0f,Font.BOLD, BaseColor.BLACK);
    //to do , mSemesterManager is not working.
    Semester mSemester = mSemesterManager.get(pSemesterId);

    String semesterName =mSemester.getName();

    String examDates = "Date: ";
    java.util.List<SeatPlanGroup> seatPlanGroup = mSeatPlanGroupManager.getBySemesterGroupAndType(pSemesterId,groupNo,type) ;
    java.util.List<ExamRoutineDto> examRoutines = mExamRoutineManager.getExamRoutine(pSemesterId,type);
    java.util.List<SeatPlan> seatPlans;
    if(groupNo==0){
      seatPlans=mSeatPlanManager.getBySemesterAndGroupAndExamTypeAndExamDate(pSemesterId,groupNo,type,examDate);
    }
    else{
      seatPlans= mSeatPlanManager.getBySemesterAndGroupAndExamType(pSemesterId,groupNo,type);

    }
    java.util.List<SpStudent> students;
    if(groupNo!=0){
     students = mSpStudentManager.getAll();
    }else{
      students = mSpStudentManager.getStudentBySemesterIdAndExamDateForCCI(pSemesterId,examDate);
    }
    java.util.List<Program> programs = mProgramManager.getAll();

    java.util.List<Integer> roomsOfTheSeatPlan = new ArrayList<>();
    Map<String,SpStudent> studentIdWIthStuddentInfoMap = new HashMap<>();
    Map<String,SeatPlan> roomRowColWithSeatPlanMap = new HashMap<>();
    Map<Integer,Program> programIdWithProgramInfoMap = new HashMap<>();
    long startTime = System.currentTimeMillis();
    for(SeatPlan seatPlan: seatPlans){
      roomsOfTheSeatPlan.add(seatPlan.getClassRoomId());
      /*StringBuilder sb = new StringBuilder();
      sb.append(seatPlan.getClassRoom().getId()).append(seatPlan.getRowNo()).append(seatPlan.getColumnNo());*/
      /*String roomRowCol =sb.toString();*/
      roomRowColWithSeatPlanMap.put(seatPlan.getClassRoomId()+""+seatPlan.getRowNo()+""+seatPlan.getColumnNo(),seatPlan);
    }
    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    for(SpStudent student: students){
      studentIdWIthStuddentInfoMap.put(student.getId(),student);
    }
    for(Program program: programs){
      programIdWithProgramInfoMap.put(program.getId(),program);
    }

    int routineCounter=0;
    if(examDate.equals("null")){
      for(ExamRoutineDto routine:examRoutines){
        SeatPlanGroup group = seatPlanGroup.get(0);

        if(routine.getProgramId() == group.getProgramId() && routine.getCourseYear()==group.getAcademicYear() && routine.getCourseSemester()== group.getAcademicSemester()){

          if(routineCounter==examRoutines.size()){
            examDates = examDates+routine.getExamDate();
          }else{
            if(examDates.equals("Date: ")){
              examDates=examDates+routine.getExamDate();
            }else{
              examDates=examDates+", "+routine.getExamDate();
            }

          }
        }
        routineCounter+=1;

      }
    }
    else{
      //examDate="";
      examDates="Date: "+examDate;
    }

    long startTimeOfTheMainAlgorithm= System.currentTimeMillis();

    if(noSeatPlanInfo){
      Chunk c = new Chunk("No SubGroup and No Seat Plan Information in the database, create one and then come back again!",f);
      c.setBackground(BaseColor.WHITE);
      Paragraph p= new Paragraph(c);
      p.setAlignment(Element.ALIGN_CENTER);
      document.add(p);
    }else{
      java.util.List<ClassRoom> rooms = mRoomManager.getAll() ;


      int roomCounter=0;
      for(ClassRoom room: rooms){
        float fontSize;
        roomCounter+=1;
        boolean checkIfRoomExistsInSeatPlan = roomsOfTheSeatPlan.contains(room.getId());
        //int checkIfRoomExists = mSeatPlanManager.checkIfExistsByRoomSemesterGroupExamType(room.getId(),pSemesterId,groupNo,type);

        if(checkIfRoomExistsInSeatPlan && room.getId()!=284  ){
          long startTimeInRoom = System.currentTimeMillis();
          String roomHeader = "Room No: "+ room.getRoomNo();
          Paragraph pRoomHeader = new Paragraph(roomHeader,FontFactory.getFont(FontFactory.TIMES_BOLD,12));
          pRoomHeader.setAlignment(Element.ALIGN_CENTER);
          document.add(pRoomHeader);
          String semesterInfo="";
          if(type==1){
            semesterInfo = "Semester Final Examination " + semesterName+". Capacity: "+room.getCapacity()+".";
          }
          if(type==2){
            semesterInfo = "Clearance/Improvement/Carryover Examination " + semesterName+". Capacity: "+room.getCapacity()+".";
          }
          Paragraph pSemesterInfo = new Paragraph(semesterInfo,FontFactory.getFont(FontFactory.TIMES_BOLD,12));
          pSemesterInfo.setAlignment(Element.ALIGN_CENTER);
          document.add(pSemesterInfo);

          Paragraph pExamDates = new Paragraph(examDates,FontFactory.getFont(FontFactory.TIMES_BOLD,10));
          pExamDates.setAlignment(Element.ALIGN_CENTER);
          pExamDates.setSpacingAfter(6f);
          document.add(pExamDates);

          //for getting the summery

          java.util.List<String> deptList = new ArrayList<>();
          Map<String, java.util.List<String>> deptStudentListMap = new HashMap<>();
          Map<String,String> deptWithDeptNameMap = new HashMap<>();
          Map<String,String> deptWithYearSemesterMap = new HashMap<>();

          for(int i=1;i<=room.getTotalRow();i++){
            for(int j=1;j<=room.getTotalColumn();j++){
              SeatPlan seatPlanOfTheRowAndCol = roomRowColWithSeatPlanMap.get(room.getId()+""+i+""+j) ;
              int ifSeatPlanExist;
              if(groupNo==0){
                ifSeatPlanExist = mSeatPlanManager.checkIfExistsBySemesterGroupTypeExamDateRoomRowAndCol(pSemesterId,groupNo,type,examDate,room.getId(),i,j);
              }
              else{
                ifSeatPlanExist= mSeatPlanManager.checkIfExistsBySemesterGroupTypeRoomRowAndCol(pSemesterId,groupNo,type,room.getId(),i,j);

              }

              if(seatPlanOfTheRowAndCol!=null){
                SeatPlan seatPlan = seatPlanOfTheRowAndCol;                                  //roomRowColWithSeatPlanMap.get(room.getId()+""+i+""+j) ;
                SpStudent student = studentIdWIthStuddentInfoMap.get(seatPlan.getStudent().getId());
                Program program ;
                String dept;
                String deptName;
                if(groupNo==0){
                  dept = student.getProgramShortName()+" "+student.getAcademicYear()+"/"+student.getAcademicSemester();
                  deptName=student.getProgramShortName();
                }
                else{
                  program = programIdWithProgramInfoMap.get(student.getProgram().getId());
                  dept = program.getShortName()+" "+student.getAcademicYear()+"/"+student.getAcademicSemester();
                  deptName = program.getShortName();

                }
                String yearSemester = student.getAcademicYear()+"/"+student.getAcademicSemester();
                if(deptList.size()==0){
                  deptList.add(dept);
                  java.util.List<String> studentList = new ArrayList<>();
                  if(groupNo==0){
                    if(student.getApplicationType()==3){
                      studentList.add(student.getId()+"(C)");
                    }
                    else if(student.getApplicationType()==5){
                      studentList.add(student.getId()+"(I)");

                    }
                    else{
                      studentList.add(student.getId());

                    }
                  }
                  else{
                    studentList.add(student.getId());

                  }
                  deptStudentListMap.put(dept,studentList);
                  deptWithDeptNameMap.put(dept,deptName);
                  deptWithYearSemesterMap.put(dept,yearSemester);
                }else{
                  boolean foundInTheList=false;
                  for(String deptOfTheList:deptList){
                    if(deptOfTheList.equals(dept)){
                      java.util.List<String> studentList = deptStudentListMap.get(dept);
                      if(groupNo==0){
                        if(student.getApplicationType()==3){
                          studentList.add(student.getId()+"(C)");
                        }
                        else if(student.getApplicationType()==5){
                          studentList.add(student.getId()+"(I)");

                        }
                        else{
                          studentList.add(student.getId());

                        }
                      }
                      else{
                        studentList.add(student.getId());

                      }
                      deptStudentListMap.put(dept,studentList);
                      foundInTheList=true;
                      break;
                    }
                  }
                  if(foundInTheList==false){
                    deptList.add(dept);
                    java.util.List<String> studentList = new ArrayList<>();
                    if(groupNo==0){
                      if(student.getApplicationType()==3){
                        studentList.add(student.getId()+"(C)");
                      }
                      else if(student.getApplicationType()==5){
                        studentList.add(student.getId()+"(I)");

                      }
                      else{
                        studentList.add(student.getId());

                      }
                    }
                    else{
                      studentList.add(student.getId());

                    }
                    deptStudentListMap.put(dept,studentList);
                    deptStudentListMap.put(dept,studentList);
                    deptWithDeptNameMap.put(dept,deptName);
                    deptWithYearSemesterMap.put(dept,yearSemester);
                  }
                }
              }
            }
          }
          float summaryFontSize=10.0f;
          if(deptList.size()<6){
            fontSize=11.0f;
          }
          else if(deptList.size()==6){
            fontSize=11.0f;

          }
          else if(deptList.size()==9){
            fontSize=11.0f;
//            summaryFontSize=9.0f;
          }
          else{
            if(room.getCapacity()<=40){
              fontSize=11.0f;
            }else{
              fontSize=11.0f;

            }
          }


          int totalStudent = 0;
          float[] tableWithForSummery = new float[]{1,1,8,1};
          float[] columnWiths = new float[]{0.5f,0.4f,9f,0.6f};

          PdfPTable summaryTable = new PdfPTable(tableWithForSummery);
          summaryTable.setWidthPercentage(100);
          summaryTable.setWidths(columnWiths);
          PdfPCell deptLabelCell = new PdfPCell();
          Paragraph deptLabel = new Paragraph("DEPT",FontFactory.getFont(FontFactory.TIMES_BOLD,summaryFontSize));
          deptLabelCell.addElement(deptLabel);
          deptLabelCell.setPaddingRight(-0.5f);
          deptLabelCell.setColspan(1);
          deptLabelCell.setPaddingTop(-2f);
          summaryTable.addCell(deptLabelCell);

          PdfPCell yearSemesterLabelCell = new PdfPCell();
          Paragraph yearSemesterLabel = new Paragraph("Y/S",FontFactory.getFont(FontFactory.TIMES_BOLD,summaryFontSize));
          yearSemesterLabelCell.addElement(yearSemesterLabel);
          yearSemesterLabelCell.setPaddingRight(-0.5f);
          yearSemesterLabelCell.setColspan(1);
          yearSemesterLabelCell.setPaddingTop(-2f);
          summaryTable.addCell(yearSemesterLabelCell);

          PdfPCell studentLabelCell = new PdfPCell();
          Paragraph studentLabel = new Paragraph("STUDENT ID",FontFactory.getFont(FontFactory.TIMES_BOLD,summaryFontSize));
          studentLabel.setAlignment(Element.ALIGN_CENTER);
          studentLabelCell.addElement(studentLabel);
          studentLabelCell.setPaddingTop(-2f);
          summaryTable.addCell(studentLabelCell);

          PdfPCell totalCellLabel = new PdfPCell();
          Paragraph totalLabel = new Paragraph("TOTAL",FontFactory.getFont(FontFactory.TIMES_BOLD,summaryFontSize));
          totalCellLabel.addElement(totalLabel);
          totalCellLabel.setPaddingTop(-2f);
          summaryTable.addCell(totalCellLabel);

          int deptListCounter=0;
          for(String deptOfTheList: deptList){
            PdfPCell deptCell = new PdfPCell();
            Paragraph deptParagraph = new Paragraph(deptWithDeptNameMap.get(deptOfTheList),FontFactory.getFont(FontFactory.TIMES_ROMAN,summaryFontSize));
            deptCell.addElement(deptParagraph);
            deptCell.setPaddingTop(-2f);
            summaryTable.addCell(deptCell);
            PdfPCell yearSemesterCell = new PdfPCell();
            Paragraph yearSemesterCellParagraph = new Paragraph(deptWithYearSemesterMap.get(deptOfTheList),FontFactory.getFont(FontFactory.TIMES_ROMAN,summaryFontSize));
            yearSemesterCell.addElement(yearSemesterCellParagraph);
            yearSemesterCell.setPaddingTop(-2f);
            summaryTable.addCell(yearSemesterCell);
            PdfPCell studentCell = new PdfPCell();
            String studentListInString = "";
            java.util.List<String> studentList = deptStudentListMap.get(deptOfTheList);
            Collections.sort(studentList);
            totalStudent+= studentList.size();
            int studentCounter=0;
            for(String studentOfTheList: studentList){
              if(studentCounter==0){
                studentListInString=studentListInString+studentOfTheList;
                studentCounter+=1;
              }
              else{
                if(studentList.size()>10){
                  studentListInString = studentListInString+","+studentOfTheList;

                }else{
                  studentListInString = studentListInString+", "+studentOfTheList;

                }

              }
            }

            deptListCounter+=1;
            studentListInString=studentListInString+" = "+studentList.size();

            if(studentList.size()>10){
              summaryFontSize = 9.0f;
            }

            Paragraph studentCellParagraph = new Paragraph(studentListInString,FontFactory.getFont(FontFactory.TIMES_ROMAN,summaryFontSize));
            studentCell.addElement(studentCellParagraph);
            studentCell.setPaddingTop(-2f);
            summaryTable.addCell(studentCell);

            if(deptListCounter==deptList.size()){
              PdfPCell totalCell = new PdfPCell();
              Paragraph totalLabels = new Paragraph(""+totalStudent,FontFactory.getFont(FontFactory.TIMES_BOLD,summaryFontSize));
              totalLabels.setAlignment(Element.ALIGN_CENTER);
              totalCell.addElement(totalLabels);
              totalCell.setPaddingTop(-2f);
              summaryTable.addCell(totalCell);
            }else{
              PdfPCell totalCell = new PdfPCell();
              Paragraph totalLabels = new Paragraph("");
              totalCell.addElement(totalLabels);
              summaryTable.addCell(totalCell);
            }


          }
          summaryTable.setSpacingAfter(8f);
          document.add(summaryTable);
          //end of getting the summary


          for(int i=1;i<=room.getTotalRow();i++){
            Paragraph tableRow = new Paragraph();
            PdfPTable mainTable = new PdfPTable(room.getTotalColumn()/2);
            mainTable.setWidthPercentage(100);
            PdfPCell[] cellsForMainTable = new PdfPCell[room.getTotalColumn()/2];
            int cellCounter=0;
            for(int j=1;j<=room.getTotalColumn();j++){
              cellsForMainTable[cellCounter]=new PdfPCell();
              PdfPTable table = new PdfPTable(2);
              SeatPlan seatPlan = roomRowColWithSeatPlanMap.get(room.getId()+""+i+""+j);
//              int ifSeatPlanExists = mSeatPlanManager.checkIfExistsBySemesterGroupTypeRoomRowAndCol(pSemesterId,groupNo,type,room.getId(),i,j);
              if(seatPlan==null){
                PdfPCell emptyCell = new PdfPCell();
                String emptyString = "  ";
                Paragraph emptyParagraph = new Paragraph(emptyString);
                Paragraph emptyParagraph2 = new Paragraph("  ");
                emptyCell.addElement(emptyParagraph);
                emptyCell.addElement(emptyParagraph2);
                table.addCell(emptyCell);
                //cellsForMainTable[cellCounter].addElement(emptyCell);
              }
              else{
               /* java.util.List<SeatPlan> seatPlanLists = mSeatPlanManager.getBySemesterGroupTypeRoomRowAndCol(pSemesterId,groupNo,type,room.getId(),i,j);

                SeatPlan seatPlan = seatPlanLists.get(0);*/

                SpStudent student = studentIdWIthStuddentInfoMap.get(seatPlan.getStudent().getId());
                Program program = programIdWithProgramInfoMap.get(student.getProgram().getId());


                PdfPCell upperCell = new PdfPCell();

                PdfPCell lowerCell = new PdfPCell();

                String upperPart = program.getShortName()+" "+student.getAcademicYear()+"/"+student.getAcademicSemester();
                Paragraph upperParagraph = new Paragraph(upperPart,FontFactory.getFont(FontFactory.TIMES_ROMAN,fontSize));
                upperParagraph.setSpacingBefore(-1f);
                String lowerPart = student.getId();
                Paragraph lowerParagraph = new Paragraph(lowerPart,FontFactory.getFont(FontFactory.TIMES_ROMAN,fontSize));
                lowerParagraph.setSpacingBefore(-1f);
                upperCell.addElement(upperParagraph);
                upperCell.addElement(lowerParagraph);
                upperCell.setPaddingTop(-2f);

                table.addCell(upperCell);
              }


              j=j+1;
               SeatPlan seatPlan2 = roomRowColWithSeatPlanMap.get(room.getId()+""+i+""+j);


              if(seatPlan2==null){
                PdfPCell emptyCell = new PdfPCell();
                String emptyString = "  ";
                Paragraph emptyParagraph = new Paragraph(emptyString);
                Paragraph emptyParagraph2 = new Paragraph("  ");
                emptyCell.addElement(emptyParagraph);
                emptyCell.addElement(emptyParagraph2);
                table.addCell(emptyCell);

              }
              else{


                SpStudent student2 = studentIdWIthStuddentInfoMap.get(seatPlan2.getStudent().getId());
                Program program2 = programIdWithProgramInfoMap.get(student2.getProgram().getId());

                PdfPCell upperCell = new PdfPCell();
                upperCell.setColspan(10);
                PdfPCell lowerCell = new PdfPCell();
                lowerCell.setColspan(10);
                String upperPart = program2.getShortName()+" "+student2.getAcademicYear()+"/"+student2.getAcademicSemester();
                Paragraph upperParagraph = new Paragraph(upperPart,FontFactory.getFont(FontFactory.TIMES_ROMAN,fontSize));
                upperParagraph.setSpacingBefore(-1f);
                String lowerPart = student2.getId();
                Paragraph lowerParagraph = new Paragraph(lowerPart,FontFactory.getFont(FontFactory.TIMES_ROMAN,fontSize));
                lowerParagraph.setSpacingBefore(-1f);
                upperCell.addElement(upperParagraph);
                upperCell.addElement(lowerParagraph);
                upperCell.setPaddingTop(-2f);
                table.addCell(upperCell);
              }

              cellsForMainTable[cellCounter].addElement(table);
              cellsForMainTable[cellCounter].setBorder(PdfPCell.NO_BORDER);
              mainTable.addCell(cellsForMainTable[cellCounter]);

              cellCounter+=1;
            }

            tableRow.add(mainTable);
            tableRow.setSpacingAfter(8f);

            document.add(tableRow);
          }

          PdfPTable footer = new PdfPTable(3);
          float footerFontSize;
          if(deptList.size()>=9){
            footerFontSize=10.0f;
          }
          else{
            footerFontSize = 12.0f;

          }

          PdfPCell dateAndPreparedByCell = new PdfPCell();
          dateAndPreparedByCell.addElement(new Phrase("Date:",FontFactory.getFont(FontFactory.TIMES_BOLD,footerFontSize)));
          dateAndPreparedByCell.addElement(new Phrase("Prepared By",FontFactory.getFont(FontFactory.TIMES_BOLD,footerFontSize)));
          dateAndPreparedByCell.setBorder(Rectangle.NO_BORDER);
          footer.addCell(dateAndPreparedByCell);
          //todo: remove border
          PdfPCell spaceCell = new PdfPCell();
          Paragraph spaceParagraph = new Paragraph(" ");
          spaceCell.addElement(spaceParagraph);
          spaceCell.setBorder(PdfPCell.NO_BORDER);
          PdfPCell checkedByCell = new PdfPCell();
          Paragraph pCheckedBy = new Paragraph("Checked By",FontFactory.getFont(FontFactory.TIMES_BOLD,footerFontSize));
          checkedByCell.addElement(spaceParagraph);
          checkedByCell.addElement(pCheckedBy);
          checkedByCell.setBorder(PdfPCell.NO_BORDER);
          footer.addCell(checkedByCell);
          PdfPCell controllerCell = new PdfPCell();
          Paragraph pController = new Paragraph("Controller of Examinations",FontFactory.getFont(FontFactory.TIMES_BOLD,footerFontSize));
          controllerCell.addElement(spaceParagraph);
          controllerCell.addElement(pController);
          controllerCell.setBorder(PdfPCell.NO_BORDER);
          footer.addCell(controllerCell);

          if(room.getCapacity()<=40){
            footer.setSpacingBefore(40f);

          }else{
            footer.setSpacingBefore(15.0f);

          }

          /*document.add(footer);*/
          long endTimeInRoom = System.currentTimeMillis();
          long totalTimeInRoom = endTimeInRoom-startTimeInRoom;
          document.newPage();



        }

        //just for debug purpose
        /*if(roomCounter==26){
          break;
        }*/

      }
    }

    long endTimeOfTheMainAlgorithm = System.currentTimeMillis();
    long totalTimeOfTHeMainAlgorithm = endTimeOfTheMainAlgorithm - startTimeOfTheMainAlgorithm;

    long exp = totalTimeOfTHeMainAlgorithm;
    document.close();
    baos.writeTo(pOutputStream);
  }

  class MyFooter extends PdfPageEventHelper{
    Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
    public void onEndPage(PdfWriter writer,Document document){
      PdfContentByte cb = writer.getDirectContent();
      Paragraph pDate = new Paragraph("Date:",FontFactory.getFont(FontFactory.TIMES_BOLD,12));
      Paragraph pPreparedBy = new Paragraph("Prepared by:",FontFactory.getFont(FontFactory.TIMES_BOLD,12));
      Paragraph pCheckedBy = new Paragraph("Checked by:",FontFactory.getFont(FontFactory.TIMES_BOLD,12));
      Paragraph pController = new Paragraph("Controller of Examinations",FontFactory.getFont(FontFactory.TIMES_BOLD,12));


      PdfPCell dateAndPreparedByCell = new PdfPCell();
      dateAndPreparedByCell.addElement(new Phrase("Date:",FontFactory.getFont(FontFactory.TIMES_BOLD,12)));
      dateAndPreparedByCell.addElement(new Phrase("Prepared By",FontFactory.getFont(FontFactory.TIMES_BOLD,12)));
      dateAndPreparedByCell.setBorder(Rectangle.NO_BORDER);
      Phrase leftPhrase = new Phrase();
      leftPhrase.add(pDate);
      Phrase leftPhraseBottom = new Phrase();
      leftPhraseBottom.add(pPreparedBy);

      Phrase middlePhraseBottom = new Phrase();
      middlePhraseBottom.add(pCheckedBy);

      Phrase rightPhraseButtom = new Phrase();
      rightPhraseButtom.add(pController);

      ColumnText.showTextAligned(cb,Element.ALIGN_LEFT,leftPhrase,( document.left()) / 3 ,
          document.bottom() - 1, 0);

      ColumnText.showTextAligned(cb,Element.ALIGN_LEFT,leftPhraseBottom,( document.left()) / 3 ,
          document.bottom() - 20, 0);

      ColumnText.showTextAligned(cb,Element.ALIGN_LEFT,middlePhraseBottom,(document.right()) / 2 ,
          document.bottom() - 20, 0);

      ColumnText.showTextAligned(cb,Element.ALIGN_RIGHT,rightPhraseButtom,(document.right()) / 1 ,
          document.bottom() - 20, 0);



    }
  }

  @Override
  public void createSeatPlanAttendenceReport(Integer pProgramType, Integer pSemesterId, Integer pExamType, String pExamDate) throws Exception,IOException,DocumentException{
    java.util.List<ExamRoutineDto> examRoutines = new ArrayList<>();
    List<SeatPlan> seatPlans = new ArrayList<>();
    String universityName = new String("Ahsanullah University of Science and Technology") ;
    String attendenceSheet = new String("ATTENDANCE SHEET");
    String original = new String("ORIGINAL");
    String duplicate = new String("DUPLICATE");
    String roomNo=new String("ROOM NO...");
    String year=new String("Year: ");
    String semester=new String("Semester: ");
    String courseNo=new String("Course No: ");
    String courseTitle=new String("Course Title: ");
    Semester semesterManager = mSemesterManager.get(pSemesterId);
    String examName = new String("Semester Final Examination, "+semesterManager.getName());
    String studentNo = new String("Student No.");
    String signatureOfTheStudents = new String("Signature of the examinee");
    String numberOfTheExamineesRegistered=new String("* Number of the examinees registered: ");
    String numberOfTheExamineesAbsent = new String("* Number of the examinees absent: ");
    String numberofTheExamineesPresent = new String("* Number of the examinees present: ");
    String signatureOfTheInvigilator = new String("Signature of the Invigilator");
    String nameOfTheInvigilator=new String("Name of the Invigilator");
    /*String tr="<tr>";
    String trEnd="</tr>";
    String td="<td>";
    String tdEnd="</td>";*/
    if(pExamDate.equals("NULL")){
      examRoutines=mExamRoutineManager.getExamRoutineBySemesterAndExamTypeOrderByExamDateAndProgramIdAndCourseId(pSemesterId,pExamType);
      seatPlans = mSeatPlanManager.getSeatPlanBySemesterAndExamTypeOrderByExamDateAndCourseAndYearAndSemesterAndStudentId(pSemesterId,pExamType);
    }else{

    }
    Document document = new Document();
    document.addTitle("Seat Plan Attendance Sheet");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfWriter writer = PdfWriter.getInstance(document,baos);

    document.open();
    document.setPageSize(PageSize.A4.rotate());

    for(ExamRoutineDto routine: examRoutines){
      Course course = mCourseManager.get(routine.getCourseId());

      document.newPage();
      document.open();

      SeatPlan seatPlan = seatPlans.get(0);
        if(seatPlan.getStudent().getAcademicYear()== course.getYear() &&
            seatPlan.getStudent().getAcademicSemester()== course.getSemester() &&
            seatPlan.getStudent().getProgram().getId() == routine.getProgramId()){

          PdfPTable table = new PdfPTable(2);
          PdfPTable tableOne = new PdfPTable(1);
          PdfPTable tableTwo = new PdfPTable(1);
          Paragraph universityParagraph = new Paragraph(universityName);
          universityParagraph.setAlignment(Element.ALIGN_CENTER);
          universityParagraph.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD,12));
          PdfPCell cellUniversityName= new PdfPCell(new Phrase(universityParagraph));
          cellUniversityName.setBorderColorBottom(BaseColor.BLACK);
          tableOne.addCell(cellUniversityName);
          tableTwo.addCell(cellUniversityName);

          Paragraph originalParagraph = new Paragraph(original);
          originalParagraph.setAlignment(Element.ALIGN_CENTER);
          originalParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,14));
          PdfPCell originalCell = new PdfPCell(originalParagraph);
          originalCell.setBorderColorBottom(BaseColor.BLACK);
          tableOne.addCell(originalCell);

          Paragraph duplicateParagraph= new Paragraph(duplicate);
          duplicateParagraph.setAlignment(Element.ALIGN_CENTER);
          duplicateParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,14));
          PdfPCell duplicateCell = new PdfPCell(duplicateParagraph);
          duplicateCell.setBorderColorBottom(BaseColor.BLACK);
          tableTwo.addCell(duplicateCell);


          Paragraph attendanceParagraph=new Paragraph(attendenceSheet);
          attendanceParagraph.setAlignment(Element.ALIGN_CENTER);
          attendanceParagraph.setFont(FontFactory.getFont(FontFactory.TIMES,14));
          PdfPCell attendanceCell = new PdfPCell(attendanceParagraph);
          attendanceCell.setBorder(Rectangle.NO_BORDER);
          tableOne.addCell(attendanceCell);
          tableTwo.addCell(attendanceCell);


          ClassRoom room = mRoomManager.get(seatPlan.getClassRoomId());
          Paragraph roomNoParagraph= new Paragraph(roomNo+ room.getRoomNo());
          roomNoParagraph.setAlignment(Element.ALIGN_CENTER);
          roomNoParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD));
          PdfPCell roomCell = new PdfPCell(roomNoParagraph);
          roomCell.setBorder(Rectangle.NO_BORDER);
          tableOne.addCell(roomCell);
          tableTwo.addCell(roomCell);


          Paragraph date = new Paragraph("Date: "+routine.getExamDate());
          date.setAlignment(Element.ALIGN_CENTER);
          date.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD));
          PdfPCell dateCell = new PdfPCell(date);
          dateCell.setBorder(Rectangle.NO_BORDER);
          tableOne.addCell(dateCell);
          tableTwo.addCell(dateCell);


          Paragraph examParagraph = new Paragraph(examName);
          examParagraph.setAlignment(Element.ALIGN_CENTER);
          examParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,14));
          PdfPCell examCell = new PdfPCell(examParagraph);
          tableOne.addCell(examCell);
          tableTwo.addCell(examCell);


          Paragraph yearSemesterParagraph = new Paragraph(year+seatPlan.getStudent().getAcademicYear()+"   "+semester+seatPlan.getStudent().getAcademicSemester());
          yearSemesterParagraph.setAlignment(Element.ALIGN_LEFT);
          yearSemesterParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,12));
          PdfPCell yearSemesterCell = new PdfPCell(yearSemesterParagraph);
          tableOne.addCell(yearSemesterCell);
          tableTwo.addCell(yearSemesterCell);

          Paragraph departmentParagraph = new Paragraph("Department: "+seatPlan.getStudent().getProgram().getShortName());
          departmentParagraph.setAlignment(Element.ALIGN_LEFT);
          departmentParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,12));
          PdfPCell departmentCell = new PdfPCell(departmentParagraph);
          tableOne.addCell(departmentCell);
          tableTwo.addCell(departmentCell);


          Paragraph courseNoParagraph = new Paragraph(courseNo+ course.getNo());
          courseNoParagraph.setAlignment(Element.ALIGN_LEFT);
          courseNoParagraph.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,12));
          PdfPCell courseNoCell = new PdfPCell(courseNoParagraph);
          tableOne.addCell(courseNoCell);
          tableTwo.addCell(courseNoCell);

          Paragraph courseTitleParagrah = new Paragraph(courseTitle+course.getTitle());
          courseTitleParagrah.setAlignment(Element.ALIGN_LEFT);
          courseTitleParagrah.setFont(FontFactory.getFont(FontFactory.TIMES_BOLD,12));
          PdfPCell courseTitleCell = new PdfPCell(courseTitleParagrah);
          tableOne.addCell(courseTitleCell);
          tableTwo.addCell(courseTitleCell);

          float[] attendenceSheetColSpan = {4,7};
          PdfPTable attendanceSheetTable = new PdfPTable(attendenceSheetColSpan);
          attendanceSheetTable.setWidthPercentage(100);
          PdfPCell sttudentNoCell = new PdfPCell(new Paragraph(studentNo,FontFactory.getFont(FontFactory.TIMES_BOLD,12)));
          PdfPCell signatureCell = new PdfPCell(new Paragraph(signatureOfTheStudents,FontFactory.getFont(FontFactory.TIMES_BOLD,12)));
          Integer seatPlanRoomId = seatPlan.getClassRoom().getId();
          seatPlans.remove(0);
          int counter=0;
          while(true){
            SeatPlan seatPlanInner = seatPlans.get(0);
            if(seatPlanInner.getClassRoom().getId()==seatPlanRoomId){
              PdfPCell innerCellOne = new PdfPCell(new Paragraph(seatPlanInner.getStudent().getId()));
              PdfPCell innerCellTwo = new PdfPCell(new Paragraph("  "));
              attendanceSheetTable.addCell(innerCellOne);
              attendanceSheetTable.addCell(innerCellTwo);
              counter+=1;
            }else{
              if(counter==20){
                break;
              }else{
                PdfPCell innerCellOne = new PdfPCell(new Paragraph("   "));
                PdfPCell innerCellTwo = new PdfPCell(new Paragraph("  "));
                attendanceSheetTable.addCell(innerCellOne);
                attendanceSheetTable.addCell(innerCellTwo);
              }
            }
          }

          PdfPCell cellOne = new PdfPCell(tableOne);
          cellOne.setBorder(Rectangle.NO_BORDER);
          PdfPCell cellTwo = new PdfPCell(tableTwo);
          cellTwo.setBorder(Rectangle.NO_BORDER);

          table.addCell(cellOne);
          table.addCell(cellTwo);

          document.newPage();

        }


    }
  }
}
