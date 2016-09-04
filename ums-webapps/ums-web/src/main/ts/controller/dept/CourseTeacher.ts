module ums {

  interface ICTeacher extends ITeacher {
    sections?: Array<string>;
    selectedSections?: Array<{id: string; name: string; uniqueId: string}>;
  }

  interface ICourseTeacher extends IAssignedTeacher {
    teacherId: string;
    teacherName: string;
    section: string;
    sections:Array<{id: string; name: string}>;
    selectedTeachers: {[key: string]: ICTeacher};
  }

  interface ICourseTeachers {
    entries : Array<ICourseTeacher>;
  }

  interface ITeachersMap {
    [key: string]: Array<ICTeacher>;
  }

  interface ITeachers {
    entries : Array<ICTeacher>;
  }

  interface IFormattedCourseTeacherMap {
    [courseId: string]: ICourseTeacher;
  }

  interface IPostCourseTeacherEntries {
    entries? : Array<IPostCourseTeacherModel>;
  }

  interface IPostCourseTeacherModel extends IPostAssignedTeacherModel {
    semesterId: string;
    courseId: string;
    teacherId: string;
    section?: string;
    updateType: string;
    id?: string;
  }

  export class CourseTeacher extends TeacherAssignment<ICourseTeacher> {
    public static $inject = ['appConstants', 'HttpClient', '$scope', '$q', 'notify'];

    private newTeacherId: number = 0;

    constructor(appConstants: any, httpClient: HttpClient,
                $scope: ITeacherAssignmentScope, $q: ng.IQService,
                notify: Notify) {
      super(appConstants, httpClient, $scope, $q, notify);
    }

    public formatTeacher(courseTeachers: Array<ICourseTeacher>, courseId?: string): void {
      for (var i = 0; i < courseTeachers.length; i++) {
        if (!this.formattedMap[courseTeachers[i].courseId] || this.formattedMap[courseTeachers[i].courseId].updated) {
          this.formattedMap[courseTeachers[i].courseId] = courseTeachers[i];
          this.formattedMap[courseTeachers[i].courseId].selectedTeachers = {};
          this.formattedMap[courseTeachers[i].courseId].editMode = false;
          this.formattedMap[courseTeachers[i].courseId].updated = false;
        }
        if (courseTeachers[i].teacherId) {
          var teacher: ICTeacher = {
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
      //save the fetched copy, later on it will be used to decided whats are values has been created/update/removed
      this.savedCopy = $.extend(true, {}, this.formattedMap);
      this.$scope.entries = this.formattedMap;
    }

    public decorateTeacher(assignedTeacher: ICourseTeacher): void {
      var sectionArray = [];
      sectionArray.push.apply(sectionArray, this.appConstants.theorySections);
      sectionArray.push.apply(sectionArray, this.appConstants.sessionalSections);
      assignedTeacher.sections = sectionArray;
    }

    public addTeacher(courseId: string): void {
      this.populateTeachers(courseId);
      this.$scope.entries[courseId].editMode = true;
      this.newTeacherId = this.newTeacherId - 1;
      this.formattedMap[courseId].selectedTeachers[this.newTeacherId] = {};
      this.formattedMap[courseId].selectedTeachers[this.newTeacherId].id = this.newTeacherId + "";
    }

    public editTeacher(courseId: string): void {
      this.populateTeachers(courseId);
      this.$scope.entries[courseId].editMode = true;
    }

    public removeTeacher(courseId: string, teacherId: string): void {
      if (this.formattedMap[courseId].selectedTeachers[teacherId]) {
        delete this.formattedMap[courseId].selectedTeachers[teacherId];
      } else {
        for (var teacher in this.formattedMap[courseId].selectedTeachers) {
          if (this.formattedMap[courseId].selectedTeachers.hasOwnProperty(teacher)) {
            if (this.formattedMap[courseId].selectedTeachers[teacher].id == teacherId) {
              delete this.formattedMap[courseId].selectedTeachers[teacher];
            }
          }
        }
      }
    }

    public saveTeacher(courseId: string): void {
      //initialize what needs to be posted
      var savedCourseTeacher: IPostCourseTeacherEntries = {};
      savedCourseTeacher.entries = [];

      var saved: ICourseTeacher = this.savedCopy[courseId];
      var modified: ICourseTeacher = this.formattedMap[courseId];

      if (!this.validate(modified, saved)) {
        return;
      }
      for (var teacherId in saved.selectedTeachers) {
        if (saved.selectedTeachers.hasOwnProperty(teacherId)) {
          if (!modified.selectedTeachers.hasOwnProperty(teacherId)) {
            var selectedSections = saved.selectedTeachers[teacherId].selectedSections;
            for (var i = 0; i < selectedSections.length; i++) {
              savedCourseTeacher.entries.push({
                id: selectedSections[i].uniqueId,
                courseId: courseId,
                semesterId: this.$scope.teacherSearchParamModel.semesterId,
                teacherId: teacherId,
                updateType: 'delete'
              });
            }

          } else {
            var modifiedTeacher: ICTeacher = modified.selectedTeachers[teacherId];
            var savedTeacher: ICTeacher = saved.selectedTeachers[teacherId];

            if (teacherId != modifiedTeacher.id) {
              var selectedSections = saved.selectedTeachers[teacherId].selectedSections;
              for (var i = 0; i < selectedSections.length; i++) {
                savedCourseTeacher.entries.push({
                  id: selectedSections[i].uniqueId,
                  courseId: courseId,
                  semesterId: this.$scope.teacherSearchParamModel.semesterId,
                  teacherId: teacherId,
                  updateType: 'delete'
                });
              }
            }
            for (var i = 0; i < savedTeacher.selectedSections.length; i++) {
              var sectionFound: boolean = false;
              for (var j = 0; j < modifiedTeacher.sections.length; j++) {
                if (savedTeacher.selectedSections[i].id == modifiedTeacher.sections[j]) {
                  sectionFound = true;
                }
              }

              if (!sectionFound) {
                savedCourseTeacher.entries.push({
                  id: savedTeacher.selectedSections[i].uniqueId,
                  courseId: courseId,
                  semesterId: this.$scope.teacherSearchParamModel.semesterId,
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
            var modifiedSelectedSections: Array<string> = modified.selectedTeachers[teacherId].sections;
            for (var i = 0; i < modifiedSelectedSections.length; i++) {
              savedCourseTeacher.entries.push({
                courseId: courseId,
                semesterId: this.$scope.teacherSearchParamModel.semesterId,
                teacherId: modified.selectedTeachers[teacherId].id,
                updateType: 'insert',
                section: modifiedSelectedSections[i]
              });
            }

          } else {
            var modifiedTeacher: ICTeacher = modified.selectedTeachers[teacherId];
            var savedTeacher: ICTeacher = saved.selectedTeachers[teacherId];

            if (teacherId != modifiedTeacher.id) {
              var modifiedSelectedSections: Array<string> = modified.selectedTeachers[teacherId].sections;
              for (var i = 0; i < modifiedSelectedSections.length; i++) {
                savedCourseTeacher.entries.push({
                  courseId: courseId,
                  semesterId: this.$scope.teacherSearchParamModel.semesterId,
                  teacherId: modifiedTeacher.id,
                  updateType: 'insert',
                  section: modifiedSelectedSections[i]
                });
              }
            }

            for (var i = 0; i < modifiedTeacher.sections.length; i++) {
              var sectionFound: boolean = false;
              for (var j = 0; j < savedTeacher.selectedSections.length; j++) {
                if (modifiedTeacher.sections[i] == savedTeacher.selectedSections[j].id) {
                  sectionFound = true;
                }
              }
              if (!sectionFound) {
                savedCourseTeacher.entries.push({
                  courseId: courseId,
                  semesterId: this.$scope.teacherSearchParamModel.semesterId,
                  teacherId: teacherId,
                  section: modifiedTeacher.sections[i],
                  updateType: 'insert'
                });
              }
            }
          }
        }
      }

      this.postTeacher(savedCourseTeacher, courseId);
    }


    public validate(modifiedVal: ICourseTeacher, saved: ICourseTeacher): boolean {
      if (UmsUtil.isEmpty(modifiedVal.selectedTeachers)) {
        if (UmsUtil.isEmpty(saved.selectedTeachers)) {
          this.notify.warn("Please select teacher/s");
          return false;
        } else {
          return true;
        }
      }

      for (var key in modifiedVal.selectedTeachers) {
        if (modifiedVal.selectedTeachers.hasOwnProperty(key)) {
          if (parseInt(key) < 0 && modifiedVal.selectedTeachers[key].id == null) {
            this.notify.warn("Please select teacher/s");
            return false;
          } else {
            var selectedSections = modifiedVal.selectedTeachers[key].sections;
            if (!selectedSections || selectedSections.length == 0) {
              this.notify.warn("Please select section/s");
              return false;
            }
          }
        }
      }

      return true;
    }

    public getBaseUri(): string {
      return "academic/courseTeacher";
    }

    public getPostUri(): string {
      return 'academic/courseTeacher/';
    }
  }

  UMS.controller('CourseTeacher', CourseTeacher);
}

