module ums {
  export class StudentCourseMaterial {
    public static $inject = ['$scope', '$stateParams', 'appConstants', 'HttpClient'];
    private currentUser: Student;
    private courseMaterialSearchParamModel: CourseTeacherSearchParamModel;

    constructor(private $scope: any, private $stateParams: any,
                private appConstants: any, private httpClient: HttpClient) {

      $scope.loadingVisibility = false;
      $scope.contentVisibility = false;
      this.httpClient.get("academic/student", HttpClient.MIME_TYPE_JSON,
          (response: Student) => {
            this.courseMaterialSearchParamModel = new CourseTeacherSearchParamModel(this.appConstants, this.httpClient);
            this.courseMaterialSearchParamModel.programSelector.setDepartment(response.deptId);
            this.courseMaterialSearchParamModel.programSelector.setProgramId(response.programId);
            this.courseMaterialSearchParamModel.programSelector.setProgramTypeId(response.programTypeId);
            this.currentUser = response;
            this.$scope.courseMaterialSearchParamModel = this.courseMaterialSearchParamModel;
          });

      this.$scope.fetchCourseInfo = this.fetchCourseInfo.bind(this);
      this.$scope.initFileManager = this.initFileManager.bind(this);

      //this.initFileManager("Fall,2015", "EEE 1101");
    }

    private fetchCourseInfo(courseNo: string): void {
      $("#leftDiv").hide(100);
      $("#arrowDiv").show(50);

      this.$scope.loadingVisibility = true;
      this.$scope.contentVisibility = false;
      this.$scope.selectedCourseNo = '';

      this.httpClient.get("course/semester/" + this.courseMaterialSearchParamModel.semesterId + "/program/"
          + this.courseMaterialSearchParamModel.programSelector.programId + "/year/"
          + this.currentUser.year + "/academicSemester/" + this.currentUser.academicSemester, HttpClient.MIME_TYPE_JSON,
          (response: {entries: Course[]}) => {
            this.$scope.entries = response.entries;
            this.$scope.loadingVisibility = false;
            this.$scope.contentVisibility = true;
          }
      )
    }

    private initFileManager(courseNo: string,
                            semesterId: string,
                            courseId: string): void {
      var semesterName = this.getSelectedSemester();
      var baseUri: string = '/ums-webservice-academic/academic/student/courseMaterial/semester/' + semesterName + "/course/" + courseNo;
      var downloadBaseUri: string = '/ums-webservice-academic/academic/student/courseMaterial/download/semester/' + semesterName + "/course/" + courseNo;

      $("#courseSelectionDiv").hide(80);
      $("#topArrowDiv").show(50);

      FILEMANAGER_CONFIG.set({
        appName: semesterName + ' > ' + courseNo,
        listUrl: baseUri,
        uploadUrl: baseUri + "/upload",
        downloadFileUrl: downloadBaseUri,
        downloadMultipleUrl: downloadBaseUri,
        hidePermissions: true,
        hideOwner: false,
        multipleDownloadFileName: 'CourseMaterial-' + semesterName + "-" + courseNo + '.zip',
        searchForm: true,
        languageSelection: false,
        additionalParams: {
          semesterId: semesterId,
          courseId: courseId
        },
        allowedActions: angular.extend(FILEMANAGER_CONFIG.$get().allowedActions, {
          createAssignmentFolder: false,
          upload: true,
          rename: false,
          move: false,
          copy: false,
          edit: false,
          changePermissions: false,
          compress: false,
          compressChooseName: false,
          extract: false,
          download: true,
          downloadMultiple: true,
          preview: false,
          remove: false,
          createFolder: false
        })
      });

      this.$scope.reloadOn = courseNo;
    }

    private getSelectedSemester(): string {
      var semesters = this.courseMaterialSearchParamModel.programSelector.getSemesters();
      var semesterName = "";
      for (var i = 0; i < semesters.length; i++) {
        if (semesters[i].id == this.courseMaterialSearchParamModel.semesterId) {
          semesterName = semesters[i].name;
        }
      }

      return semesterName;
    }
  }

  UMS.controller("StudentCourseMaterial", StudentCourseMaterial);
}
