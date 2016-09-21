module ums {
  export interface ITeacherAssignmentScope extends ng.IScope {
    submit: Function;
    teacherSearchParamModel: TeacherAssignmentSearchParamModel;
    data:any;
    loadingVisibility:boolean;
    contentVisibility:boolean;
    fetchTeacherInfo:Function;
    entries: IFormattedTeacherMap<IAssignedTeacher>;
    addTeacher: Function;
    editTeacher: Function;
    removeTeacher: Function;
    saveTeacher: Function;
    programName: string;
    departmentName: string;
    semesterName: string;
    academicYear: string;
    academicSemester: string;
    courseCategory: string;
    isEmpty: Function;
  }

  export interface ITeacher {
    id?: string;
    name?: string;
  }

  export interface IAssignedTeacher {
    id: string;
    courseId: string;
    courseNo: string;
    courseTitle: string;
    courseCrHr: string;
    year: string;
    semester: string;
    syllabusId: string;
    courseOfferedByDepartmentId: string;
    courseOfferedByDepartmentName: string;
    teachers: Array<ITeacher>;

    editMode: boolean;
    updated: boolean;
  }

  interface IAssignedTeachers {
    entries : Array<IAssignedTeacher>;
  }

  interface ITeachersMap {
    [key: string]: Array<ITeacher>;
  }

  interface ITeachers {
    entries : Array<ITeacher>;
  }

  interface IFormattedTeacherMap<T> {
    [courseId: string]: T;
  }

  export interface IPostTeacherEntries {
    entries? : Array<IPostAssignedTeacherModel>;
  }

  export interface IPostAssignedTeacherModel {
    semesterId: string;
    courseId: string;
    teacherId: string;
    updateType: string;
    id?: string;
  }

  export class TeacherAssignment<T extends IAssignedTeacher> {
    public teachersList: ITeachersMap;
    public formattedMap: IFormattedTeacherMap<T>;
    public savedCopy: IFormattedTeacherMap<T>;

    constructor(public appConstants: any, public httpClient: HttpClient,
                public $scope: ITeacherAssignmentScope, public $q: ng.IQService,
                public notify: Notify) {
      $scope.teacherSearchParamModel = new TeacherAssignmentSearchParamModel(this.appConstants, this.httpClient);
      $scope.data = {
        courseCategoryOptions: appConstants.courseCategory,
        academicYearOptions: appConstants.academicYear,
        academicSemesterOptions: appConstants.academicSemester
      };

      $scope.loadingVisibility = false;
      $scope.contentVisibility = false;
      $scope.fetchTeacherInfo = this.fetchTeacherInfo.bind(this);
      $scope.addTeacher = this.addTeacher.bind(this);
      $scope.editTeacher = this.editTeacher.bind(this);
      $scope.removeTeacher = this.removeTeacher.bind(this);
      $scope.saveTeacher = this.saveTeacher.bind(this);
      $scope.isEmpty = UmsUtil.isEmpty;

      this.teachersList = {};
      this.formattedMap = {};

      //this.fetchTeacherInfo();
    }


    public fetchTeacherInfo(): void {
      $("#leftDiv").hide();
      $("#arrowDiv").show();
      $("#rightDiv").removeClass("orgRightClass").addClass("newRightClass").removeClass('hidden');

      this.$scope.loadingVisibility = true;
      this.$scope.contentVisibility = false;

      if (UmsUtil.isEmptyString(this.$scope.teacherSearchParamModel.courseId)) {
        this.renderHeader();
        this.formattedMap = {};
      }

      var fetchUri: string = this.uriBuilder(this.$scope.teacherSearchParamModel);

      this.httpClient.get(fetchUri,
          this.appConstants.mimeTypeJson,
          (data: IAssignedTeachers, etag: string)=> {
            if (!UmsUtil.isEmptyString(this.$scope.teacherSearchParamModel.courseId)) {
              this.formattedMap[this.$scope.teacherSearchParamModel.courseId].updated = true;
              this.formatTeacher(data.entries, this.$scope.teacherSearchParamModel['courseId']);
              delete this.$scope.teacherSearchParamModel['courseId'];
            } else {
              this.formatTeacher(data.entries);
            }

            this.$scope.loadingVisibility = false;
            this.$scope.contentVisibility = true;
          });
    }

    //override
    public formatTeacher(teachers: Array<IAssignedTeacher>, courseId?: string): void {
      throw new Error("Method not implemented");
    }


    public populateTeachers(courseId: string): void {
      if (this.$scope.entries.hasOwnProperty(courseId)) {
        this.getTeachers(this.$scope.entries[courseId]).then(()=> {
          //do nothing
        });
      }
    }

    private getTeachers(assignedTeacher: IAssignedTeacher): ng.IPromise<any> {
      var defer = this.$q.defer();

      this.decorateTeacher(assignedTeacher);

      if (this.teachersList[assignedTeacher.courseOfferedByDepartmentId]) {
        assignedTeacher.teachers = this.teachersList[assignedTeacher.courseOfferedByDepartmentId];
        defer.resolve(null);
      } else {
        console.log("================>>");
        console.log(assignedTeacher);
        this.httpClient.get("academic/teacher/department/" + assignedTeacher.courseOfferedByDepartmentId, this.appConstants.mimeTypeJson,
            (data: ITeachers, etag: string) => {
              this.teachersList[assignedTeacher.courseOfferedByDepartmentId] = data.entries;
              assignedTeacher.teachers = this.teachersList[assignedTeacher.courseOfferedByDepartmentId];
              defer.resolve(null);
            });
      }

      return defer.promise;
    }

    public decorateTeacher(assignedTeacher: IAssignedTeacher): void {
      throw new Error("Method not implemented");
    }

    public addTeacher(courseId: string): void {
      throw new Error("Method not implemented");
    }

    public editTeacher(courseId: string): void {
      throw new Error("Method not implemented");
    }

    public removeTeacher(courseId: string, teacherId: string): void {
      throw new Error("Method not implemented");
    }

    public saveTeacher(courseId: string): void {
      throw new Error('Method not implemented');
    }

    public postTeacher(save: IPostExaminerEntries, courseId: string): ng.IHttpPromise<any> {
      return this.httpClient.post(this.getPostUri(), save, 'application/json');
    }

    private renderHeader(): void {
      for (var i = 0; i < this.$scope.teacherSearchParamModel.programSelector.getPrograms().length; i++) {
        if (this.$scope.teacherSearchParamModel.programSelector.getPrograms()[i].id == this.$scope.teacherSearchParamModel.programSelector.programId) {
          this.$scope.programName = this.$scope.teacherSearchParamModel.programSelector.getPrograms()[i].longName;
        }
      }

      for (var i = 0; i < this.$scope.teacherSearchParamModel.programSelector.getSemesters().length; i++) {
        if (this.$scope.teacherSearchParamModel.programSelector.getSemesters()[i].id == this.$scope.teacherSearchParamModel.semesterId) {
          this.$scope.semesterName = this.$scope.teacherSearchParamModel.programSelector.getSemesters()[i].name;
        }
      }

      for (var i = 0; i < this.$scope.teacherSearchParamModel.programSelector.getDepartments().length; i++) {
        if (this.$scope.teacherSearchParamModel.programSelector.getDepartments()[i].id == this.$scope.teacherSearchParamModel.programSelector.departmentId) {
          this.$scope.departmentName = this.$scope.teacherSearchParamModel.programSelector.getDepartments()[i].name;
        }
      }

      for (var i = 0; i < this.$scope.data.academicYearOptions.length; i++) {
        if (this.$scope.data.academicYearOptions[i].id == this.$scope.teacherSearchParamModel.academicYearId) {
          this.$scope.academicYear = this.$scope.data.academicYearOptions[i].name.indexOf('Select') == 0 ? "" : this.$scope.data.academicYearOptions[i].name;
        }
      }

      for (var i = 0; i < this.$scope.data.academicSemesterOptions.length; i++) {
        if (this.$scope.data.academicSemesterOptions[i].id == this.$scope.teacherSearchParamModel.academicSemesterId) {
          this.$scope.academicSemester = this.$scope.data.academicSemesterOptions[i].name.indexOf('Select') == 0 ? "" : this.$scope.data.academicSemesterOptions[i].name;
        }
      }

      for (var i = 0; i < this.$scope.data.courseCategoryOptions.length; i++) {
        if (this.$scope.data.courseCategoryOptions[i].id == this.$scope.teacherSearchParamModel.courseCategoryId) {
          this.$scope.courseCategory = this.$scope.data.courseCategoryOptions[i].name.indexOf('Select') == 0 ? "All" : this.$scope.data.courseCategoryOptions[i].name;
        }
      }
    }

    public validate(modifiedVal: IAssignedTeacher, saved: IAssignedTeacher): boolean {
      throw new Error("Method not implemented");
    }

    private uriBuilder(param: CourseTeacherSearchParamModel): string {
      //var fetchUri: string = this.getBaseUri() + "/programId/" + '110500' + "/semesterId/" + '11012015' + '/year/4';
      var fetchUri = this.getBaseUri() + "/programId/" + param.programSelector.programId + "/semesterId/" + param.semesterId;

      if (!UmsUtil.isEmptyString(param.courseId)) {
        fetchUri = fetchUri + "/courseId/" + param.courseId;
        return fetchUri;
      }

      if (!UmsUtil.isEmptyString(param.academicYearId)) {
        fetchUri = fetchUri + "/year/" + param.academicYearId;
      }
      if (!UmsUtil.isEmptyString(param.academicSemesterId)) {
        fetchUri = fetchUri + "/semester/" + param.academicSemesterId;
      }
      if (!UmsUtil.isEmptyString(param.courseCategoryId)) {
        fetchUri = fetchUri + "/category/" + param.courseCategoryId;
      }

      return fetchUri;
    }

    public getBaseUri(): string {
      return "";
    }

    public getPostUri(): string {
      return "";
    }
  }
}

