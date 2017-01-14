module ums {
  export interface MarksSubmissionStatus {
    programId: number;
    programName: string;
    departmentId: string;
    departmentName: string;
    courseId: string;
    courseNo: string;
    courseTitle: string;
    year: number;
    academicSemester: number;
    status: string;
    statusId: number;
    lastSubmissionDate: string;
  }

  export interface MarksSubmissionStatusResponse {
    entries: Array<MarksSubmissionStatus>;
  }

  export interface AggregatedStatus {
    entries: Array<MarksSubmissionStatus>;
    status?: number;
    year?: number;
    academicSemester?: number;
  }

  export interface StatusByYearSemester {
    yearSemester?: {
      [key: string]: AggregatedStatus
    };
    departmentName?: string;
    programName?: string;
    taskStatus?: any;
  }

  interface StatusMapByProgram {
    [key: string]: StatusByYearSemester;
  }

  export class ResultProcessing {
    public static $inject = ['$scope', '$stateParams', 'appConstants', 'HttpClient', '$modal'];
    private resultProcessingSearchParamModel: ProgramSelectorModel;

    constructor(private $scope: any, private $stateParams: any,
                private appConstants: any, private httpClient: HttpClient,
                private $modal: any) {

      $scope.showGif = true;
      $scope.showAllDept = false;

      $scope.fetchStatusInfo = this.fetchStatusInfo.bind(this);
      $scope.showYearSemesterWise = this.showYearSemesterWise.bind(this);
      $scope.showDefault = this.showDefault.bind(this);
      $scope.isReadyForResultProcess = this.isReadyForResultProcess.bind(this);
      $scope.sortedKeys = this.sortedKeys.bind(this);
      $scope.showCourseList = this.showCourseList.bind(this);
      $scope.isReadyForProcess=this.isReadyForProcess.bind(this);
      $scope.length = UmsUtil.length;

      this.resultProcessingSearchParamModel
          = new ProgramSelectorModel(this.appConstants, this.httpClient, true, true, true);
      this.resultProcessingSearchParamModel.setProgramType(this.appConstants.programTypeEnum.UG,
          FieldViewTypes.selected);
      this.resultProcessingSearchParamModel.setDepartment(this.appConstants.deptAll.id,
          FieldViewTypes.selected);
      this.resultProcessingSearchParamModel.setProgram(this.appConstants.programAll.id,
          FieldViewTypes.selected);
      $scope.resultProcessingSearchParamModel = this.resultProcessingSearchParamModel;
    }

    private fetchStatusInfo(): void {
      $("#leftDiv").hide(100);
      $("#arrowDiv").show(50);

      this.$scope.showGif = false;
      this.$scope.showAllDept = true;

      if (this.resultProcessingSearchParamModel.programId === this.appConstants.programAll.id) {
        this.getStatus(this.getAllStatusUri(
            parseInt(this.resultProcessingSearchParamModel.programTypeId),
            parseInt(this.resultProcessingSearchParamModel.semesterId)));
      } else {
        this.getStatus(this.getProgramWiseStatusUri(
            parseInt(this.resultProcessingSearchParamModel.programId),
            parseInt(this.resultProcessingSearchParamModel.semesterId)));
      }

      this.$scope.semesterId = this.resultProcessingSearchParamModel.semesterId;
    }

    private showYearSemesterWise(programId: String): void {
      $(`#${programId}_view1`).hide('slide', {direction: 'right', easing: 'easeOutBounce'}, 400);

      if ($(`#${programId}_download`)) {
        $(`#${programId}_download`).hide('slide', {
          direction: 'right',
          easing: 'easeOutBounce'
        }, 200);
      }

      if ($(`#${programId}_publish`)) {
        $(`#${programId}_publish`).hide('slide', {
          direction: 'right',
          easing: 'easeOutBounce'
        }, 200);
      }
      setTimeout(function () {
        $(`#${programId}_view2`).fadeIn(200);
      }, 400);

    }

    private showDefault(programId: String): void {
      $(`#${programId}_view2`).hide('slide', {direction: 'right', easing: 'easeOutBounce'}, 200);

      setTimeout(function () {
        $(`#${programId}_view1`).fadeIn(200);
        if ($(`#${programId}_download`)) {
          $(`#${programId}_download`).fadeIn(100);
        }
        $(`#${programId}`).fadeIn(100);
        if ($(`#${programId}_publish`)) {
          $(`#${programId}_publish`).fadeIn(100);
        }
      }, 200);

    }

    private getStatus(uri: string): void {
      this.httpClient.get(uri, HttpClient.MIME_TYPE_JSON,
          (resposne: MarksSubmissionStatusResponse) => {
            this.groupMarksSubmissionStatus(resposne.entries);
          });
    }

    private groupMarksSubmissionStatus(statuses: Array<MarksSubmissionStatus>): void {
      var statusMap: StatusMapByProgram = {};

      for (var i = 0; i < statuses.length; i++) {

        if (!statusMap[statuses[i].programId]) {
          statusMap[statuses[i].programId] = {};
        }

        var yearSemester = `${statuses[i].year}-${statuses[i].academicSemester}`;

        if (!statusMap[statuses[i].programId].yearSemester) {
          statusMap[statuses[i].programId].yearSemester = {};
          statusMap[statuses[i].programId].taskStatus = {};
        }

        if (!statusMap[statuses[i].programId].yearSemester[yearSemester]) {
          var aggregatedStatus: AggregatedStatus = {
            entries: [],
            status: this.appConstants.MARKS_SUBMISSION_STATUS.ACCEPTED_BY_COE,
            year: statuses[i].year,
            academicSemester: statuses[i].academicSemester
          };
          statusMap[statuses[i].programId].yearSemester[yearSemester] = aggregatedStatus;
          statusMap[statuses[i].programId].departmentName = statuses[i].departmentName;
          statusMap[statuses[i].programId].programName = statuses[i].programName;
        }

        statusMap[statuses[i].programId].yearSemester[yearSemester].entries.push(statuses[i]);

        if (statuses[i].statusId != this.appConstants.MARKS_SUBMISSION_STATUS.ACCEPTED_BY_COE) {
          statusMap[statuses[i].programId].yearSemester[yearSemester].status = statuses[i].statusId;
        }
      }
      this.$scope.statusMap = statusMap; //console.log(statusMap);
      if (Object.keys(statusMap).length > 2) {
        this.$scope.numberOfRows = 2;
        this.$scope.row1start = 0;
        this.$scope.row1end = 3;
        this.$scope.row2start = 4;
        this.$scope.row2end = 7;
      }
      else {
        this.$scope.numberOfRows = 1;
        this.$scope.row1start = 0;
        this.$scope.row1end = Object.keys(statusMap).length - 1;
      }
      this.$scope.updateTime = Date.now();
    }

    private isReadyForProcess(statusId:number,programId:number): boolean {
      if(statusId!=3) return false;

      var yearSemester=this.$scope.statusMap [programId].yearSemester;
      for (var key in yearSemester) {
        var entries = yearSemester[key].entries;
        for (var index in entries) {
          var status:number=entries[index].status;
          if(status!=this.appConstants.MARKS_SUBMISSION_STATUS.ACCEPTED_BY_COE)
            return false;
        }
      }
      return true;
    }

    private showCourseList(marksSubmissionStatusList: Array<MarksSubmissionStatus>,
                           departmentName: string,
                           yearSemester: string): void {
      this.$modal.open({
        templateUrl: 'views/result/modal-content.html',
        controller: CourseStatusList,
        resolve: {
          marksSubmissionStatusList: () => {
            return marksSubmissionStatusList;
          },
          departmentName: () => {
            return departmentName;
          },
          yearSemester: () => {
            return yearSemester;
          }
        }
      });
    }

    private sortedKeys(obj): Array<string> {
      return Object.keys(obj).sort();
    }

    private isReadyForResultProcess(status): boolean {
      return parseInt(status) === this.appConstants.MARKS_SUBMISSION_STATUS.ACCEPTED_BY_COE;
    }

    private getAllStatusUri(pProgramType: number, semesterId: number): string {
      return `academic/marksSubmissionStatus/programType/${pProgramType}/semester/${semesterId}`;
    }

    private getProgramWiseStatusUri(pProgramId: number, semesterId: number): string {
      return `academic/marksSubmissionStatus/program/${pProgramId}/semester/${semesterId}`;
    }

  }

  UMS.controller("ResultProcessing", ResultProcessing);
}
