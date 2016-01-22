///<reference path="../../service/HttpClient.ts"/>
///<reference path="../../lib/jquery.notific8.d.ts"/>
///<reference path="../../lib/bootstrap.selectpicker.d.ts"/>
///<reference path="../../model/CourseTeacherSearchParamModel.ts"/>
///<reference path="../../model/CourseTeacherSearchParamModelImp.ts"/>
module ums {

  interface ICourseTeacherScope extends ng.IScope {
    submit: Function;
    courseTeacherSearchParamModel:CourseTeacherSearchParamModelImp;
    data:any;
    loadingVisibility:boolean;
    fetchCourseTeacherInfo:Function;
    entries: IFormattedCourseTeacherMap;
    addTeacher: Function;
    editCourseTeacher: Function;
    removeCourseTeacher: Function;
    saveCourseTeacher: Function;
    programName: string;
    departmentName: string;
    semesterName: string;
  }

  interface ITeacher {
    id?: string;
    name?: string;
    sections?: Array<string>;
    selectedSections?: Array<{id: string; name: string; uniqueId: string}>;
  }

  interface ICourseTeacher {
    id: string;
    courseId: string;
    courseNo: string;
    courseTitle: string;
    courseCrHr: string;
    year: string;
    semester: string;
    syllabusId: string;
    teacherId: string;
    teacherName: string;
    section: string;
    courseOfferedByDepartmentId: string;
    courseOfferedByDepartmentName: string;
    teachers: Array<ITeacher>;
    selectedTeachers: {[key: string]: ITeacher};
    sections:Array<{id: string; name: string}>;
    editMode: boolean;
  }

  interface ICourseTeachers {
    entries : Array<ICourseTeacher>;
  }

  interface ITeachersMap {
    [key: string]: Array<ITeacher>;
  }

  interface ITeachers {
    entries : Array<ITeacher>;
  }

  interface IFormattedCourseTeacherMap {
    [courseId: string]: ICourseTeacher;
  }

  interface IPostCourseTeacherEntries {
    entries? : Array<IPostCourseTeacherModel>;
  }

  interface IPostCourseTeacherModel {
    semesterId: string;
    courseId: string;
    teacherId: string;
    section?: string;
    updateType: string;
    id?: string;
  }

  export class CourseTeacher {
    public static $inject = ['appConstants', 'HttpClient', '$scope', '$q'];
    private teachersList:ITeachersMap;
    private formattedMap:IFormattedCourseTeacherMap;
    private savedCopy:IFormattedCourseTeacherMap;

    private newTeacherId:number = 0;

    constructor(private appConstants:any, private httpClient:HttpClient,
                private $scope:ICourseTeacherScope, private $q:ng.IQService) {
      $scope.courseTeacherSearchParamModel = new CourseTeacherSearchParamModelImp(this.appConstants, this.httpClient);
      $scope.data = {
        courseCategoryOptions: appConstants.courseCategory,
        academicYearOptions: appConstants.academicYear,
        academicSemesterOptions: appConstants.academicSemester
      };

      $scope.loadingVisibility = false;
      $scope.fetchCourseTeacherInfo = this.fetchCourseTeacherInfo.bind(this);
      $scope.addTeacher = this.addTeacher.bind(this);
      $scope.editCourseTeacher = this.editCourseTeacher.bind(this);
      $scope.removeCourseTeacher = this.removeCourseTeacher.bind(this);
      $scope.saveCourseTeacher = this.saveCourseTeacher.bind(this);

      $('.selectpicker').selectpicker({
        iconBase: 'fa',
        tickIcon: 'fa-check'
      });

      this.teachersList = {};
      this.formattedMap = {};
    }


    private fetchCourseTeacherInfo():void {
      $("#leftDiv").hide();
      $("#arrowDiv").show();

      $("#rightDiv").removeClass("orgRightClass");
      $("#rightDiv").addClass("newRightClass");


      this.$scope.loadingVisibility = true;
      this.formattedMap = {};

      for (var i = 0; i < this.$scope.courseTeacherSearchParamModel.programs.length; i++) {
        if (this.$scope.courseTeacherSearchParamModel.programs[i].id == this.$scope.courseTeacherSearchParamModel.programId) {
          this.$scope.programName = this.$scope.courseTeacherSearchParamModel.programs[i].longName;
        }
      }

      for (var i = 0; i < this.$scope.courseTeacherSearchParamModel.semesters.length; i++) {
        if (this.$scope.courseTeacherSearchParamModel.semesters[i].id == this.$scope.courseTeacherSearchParamModel.semesterId) {
          this.$scope.semesterName = this.$scope.courseTeacherSearchParamModel.semesters[i].name;
        }
      }

      for (var i = 0; i < this.$scope.courseTeacherSearchParamModel.departments.length; i++) {
        if (this.$scope.courseTeacherSearchParamModel.departments[i].id == this.$scope.courseTeacherSearchParamModel.departmentId) {
          this.$scope.departmentName = this.$scope.courseTeacherSearchParamModel.departments[i].name;
        }
      }

      var fetchUri:string = "academic/courseTeacher/programId/" + this.$scope.courseTeacherSearchParamModel.programId
          + "/semesterId/" + this.$scope.courseTeacherSearchParamModel.semesterId;

      if (this.$scope.courseTeacherSearchParamModel.academicYearId) {
        fetchUri = fetchUri + "/year/" + this.$scope.courseTeacherSearchParamModel.academicYearId;
      }

      if (this.$scope.courseTeacherSearchParamModel.academicSemesterId) {
        fetchUri = fetchUri + "/semester/" + this.$scope.courseTeacherSearchParamModel.academicSemesterId;
      }

      if (this.$scope.courseTeacherSearchParamModel.courseCategoryId) {
        fetchUri = fetchUri + "/category/" + this.$scope.courseTeacherSearchParamModel.courseCategoryId;
      }

      this.httpClient.get(fetchUri,
          this.appConstants.mimeTypeJson,
          (data:ICourseTeachers, etag:string)=> {
            this.formatCourseTeacher(data.entries);
            this.populateTeachers(this.$scope.entries);
            //this.$scope.entries = data.entries;
            this.$scope.loadingVisibility = false;
          });
    }

    private formatCourseTeacher(courseTeachers:Array<ICourseTeacher>):void {
      for (var i = 0; i < courseTeachers.length; i++) {
        if (!this.formattedMap[courseTeachers[i].courseId]) {
          this.formattedMap[courseTeachers[i].courseId] = courseTeachers[i];
          this.formattedMap[courseTeachers[i].courseId].selectedTeachers = {};
          this.formattedMap[courseTeachers[i].courseId].editMode = false;
        }
        if (courseTeachers[i].teacherId) {
          var teacher:ITeacher = {
            id: courseTeachers[i].teacherId,
            name: courseTeachers[i].teacherName,
            sections: [],
            selectedSections: []
          };
          if (!this.formattedMap[courseTeachers[i].courseId].selectedTeachers[courseTeachers[i].teacherId]) {
            this.formattedMap[courseTeachers[i].courseId].selectedTeachers[courseTeachers[i].teacherId] = teacher;
          }
          var section = {
            id: courseTeachers[i].section,
            name: courseTeachers[i].section,
            uniqueId: courseTeachers[i].id
          };
          this.formattedMap[courseTeachers[i].courseId].selectedTeachers[courseTeachers[i].teacherId].selectedSections.push(section);
          this.formattedMap[courseTeachers[i].courseId].selectedTeachers[courseTeachers[i].teacherId].sections.push(section.id);

        }

      }
      console.debug("%o", this.formattedMap);
      //save the fetched copy, later on it will be used to decided whats are values has been created/update/removed
      this.savedCopy = $.extend(true, {}, this.formattedMap);
      console.debug("Saved copy %o", this.formattedMap);

      this.$scope.entries = this.formattedMap;
    }

    private populateTeachers(courseTeachers:IFormattedCourseTeacherMap):void {
      for (var courseId in courseTeachers) {
        if (courseTeachers.hasOwnProperty(courseId)) {
          this.getTeachers(courseTeachers[courseId]).then(()=> {
            //do nothing
          });
        }
      }
    }

    private getTeachers(courseTeacher:ICourseTeacher):ng.IPromise<any> {
      var defer = this.$q.defer();
      courseTeacher.sections = this.appConstants.theorySections;
      if (this.teachersList[courseTeacher.courseOfferedByDepartmentId]) {
        courseTeacher.teachers = this.teachersList[courseTeacher.courseOfferedByDepartmentId];
        defer.resolve(null);
      } else {
        this.httpClient.get("academic/teacher/department/" + courseTeacher.courseOfferedByDepartmentId, this.appConstants.mimeTypeJson,
            (data:ITeachers, etag:string) => {
              this.teachersList[courseTeacher.courseOfferedByDepartmentId] = data.entries;
              courseTeacher.teachers = this.teachersList[courseTeacher.courseOfferedByDepartmentId];
              defer.resolve(null);
            });
      }

      return defer.promise;
    }

    private addTeacher(courseId:string):void {
      this.newTeacherId = this.newTeacherId - 1;
      this.formattedMap[courseId].selectedTeachers[this.newTeacherId] = {};
      this.formattedMap[courseId].selectedTeachers[this.newTeacherId].id = this.newTeacherId + "";
    }

    private editCourseTeacher(courseId:string):void {
      console.debug("edit");
      this.$scope.entries[courseId].editMode = true;
      //console.debug("%o", this.$scope.entries[courseId].editMode);
    }

    private removeCourseTeacher(courseId:string, teacherId:string):void {
      delete this.formattedMap[courseId].selectedTeachers[teacherId];
    }

    private saveCourseTeacher(courseId:string):void {
      //initialize what needs to be posted
      var savedCourseTeacher:IPostCourseTeacherEntries = {};
      savedCourseTeacher.entries = [];

      var saved:ICourseTeacher = this.savedCopy[courseId];
      var modified:ICourseTeacher = this.formattedMap[courseId];
      console.debug("%o", modified);

      for (var teacherId in saved.selectedTeachers) {
        if (saved.selectedTeachers.hasOwnProperty(teacherId)) {
          if (!modified.selectedTeachers.hasOwnProperty(teacherId)) {
            var selectedSections = saved.selectedTeachers[teacherId].selectedSections;
            for (var i = 0; i < selectedSections.length; i++) {
              console.debug("delete 1");
              savedCourseTeacher.entries.push({
                id: selectedSections[i].uniqueId,
                courseId: courseId,
                semesterId: this.$scope.courseTeacherSearchParamModel.semesterId,
                teacherId: teacherId,
                updateType: 'delete'
              });
            }

          } else {
            var modifiedTeacher:ITeacher = modified.selectedTeachers[teacherId];
            var savedTeacher:ITeacher = saved.selectedTeachers[teacherId];

            if (teacherId != modifiedTeacher.id) {
              var selectedSections = saved.selectedTeachers[teacherId].selectedSections;
              for (var i = 0; i < selectedSections.length; i++) {
                console.debug("delete 2");
                savedCourseTeacher.entries.push({
                  id: selectedSections[i].uniqueId,
                  courseId: courseId,
                  semesterId: this.$scope.courseTeacherSearchParamModel.semesterId,
                  teacherId: teacherId,
                  updateType: 'delete'
                });
              }
            }
            for (var i = 0; i < savedTeacher.selectedSections.length; i++) {
              var sectionFound:boolean = false;
              for (var j = 0; j < modifiedTeacher.sections.length; j++) {
                if (savedTeacher.selectedSections[i].id == modifiedTeacher.sections[j]) {
                  sectionFound = true;
                }
              }

              if (!sectionFound) {
                console.debug("delete 3");
                savedCourseTeacher.entries.push({
                  id: savedTeacher.selectedSections[i].uniqueId,
                  courseId: courseId,
                  semesterId: this.$scope.courseTeacherSearchParamModel.semesterId,
                  teacherId: teacherId,
                  section: '',
                  updateType: 'delete'
                });
              }
            }
          }
        }
      }

      for (var teacherId in modified.selectedTeachers) {
        if (modified.selectedTeachers.hasOwnProperty(teacherId)) {
          if (!saved.selectedTeachers.hasOwnProperty(teacherId)) {
            var modifiedSelectedSections:Array<string> = modified.selectedTeachers[teacherId].sections;
            for (var i = 0; i < modifiedSelectedSections.length; i++) {
              console.debug("insert 1");
              savedCourseTeacher.entries.push({
                courseId: courseId,
                semesterId: this.$scope.courseTeacherSearchParamModel.semesterId,
                teacherId: modified.selectedTeachers[teacherId].id,
                updateType: 'insert',
                section: modifiedSelectedSections[i]
              });
            }

          } else {
            var modifiedTeacher:ITeacher = modified.selectedTeachers[teacherId];
            var savedTeacher:ITeacher = saved.selectedTeachers[teacherId];

            if (teacherId != modifiedTeacher.id) {
              var modifiedSelectedSections:Array<string> = modified.selectedTeachers[teacherId].sections;
              for (var i = 0; i < modifiedSelectedSections.length; i++) {
                console.debug("insert 2");
                savedCourseTeacher.entries.push({
                  courseId: courseId,
                  semesterId: this.$scope.courseTeacherSearchParamModel.semesterId,
                  teacherId: modifiedTeacher.id,
                  updateType: 'insert',
                  section: modifiedSelectedSections[i]
                });
              }
            }

            for (var i = 0; i < modifiedTeacher.sections.length; i++) {
              var sectionFound:boolean = false;
              for (var j = 0; j < savedTeacher.selectedSections.length; j++) {
                if (modifiedTeacher.sections[i] == savedTeacher.selectedSections[j].id) {
                  sectionFound = true;
                }
              }
              console.debug("sectionFound: %o", sectionFound);
              if (!sectionFound) {
                console.debug("insert 3");
                savedCourseTeacher.entries.push({
                  courseId: courseId,
                  semesterId: this.$scope.courseTeacherSearchParamModel.semesterId,
                  teacherId: teacherId,
                  section: modifiedTeacher.sections[i],
                  updateType: 'insert'
                });
              }
            }
          }
        }
      }

      console.debug("%o", savedCourseTeacher);

      this.httpClient.post('academic/courseTeacher/', savedCourseTeacher, 'application/json')
          .success(() => {
            console.debug("saved");
            this.fetchCourseTeacherInfo();
          }).error((error) => {
        console.error(error);
      });
    }
  }
  UMS.controller('CourseTeacher', CourseTeacher);
}

