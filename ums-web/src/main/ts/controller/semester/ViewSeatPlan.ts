module ums{

  interface IViewSeatPlan extends ng.IScope{
    student:Student;
    recordNotFoundMessage:string;
    seatPlanArr:ISeatPlan;
    roomId:number
    classRoom:IClassRoom;
    /*Both colIteration and rowIteration will be used
    * for looping purpose, as angular filter is trouble some*/
    colIterationArr:Array<number>;
    rowIterationArr:Array<number>;
    applicationCCIArr:Array<IApplicationCCI>;

    //booleans
    studentRecordFound:boolean;
    semesterFinalSelected:boolean;
    civilSelected:boolean;
    cciSelected:boolean;


    init:Function;
    getStudentInfo:Function;
    getStudentRecordInfo:Function;
    getSeatPlanInfoFinalExam:Function;
    getClassRoomInfo:Function;
    showSemesterFinalSeatPlan:Function;
    showCivilSpecialSeatPlan:Function;
    showCCISeatPlan:Function;
    getApplicationCCIInfo:Function;

  }

  interface ISeatPlan{
    id:number;
    roomId:number;
    roomNo:string;
    rowNo:number;
    colNo:number;
    studentId:string;
    examType:number;
    semesterId:number;
    groupNo:number;
  }

  interface IClassRoom{
    totalRow:number;
    totalColumn:number;
  }

  interface IApplicationCCI{
    examDate:string;
    courseNo:string;
    applicationType:number;
    roomNo:string;
    roomId:number;
  }

  class ViewSeatPlan{
    public static $inject = ['appConstants','HttpClient','$scope','$q','notify','$sce','$window'];
    constructor(private appConstants: any, private httpClient: HttpClient, private $scope: IViewSeatPlan,
                private $q:ng.IQService, private notify: Notify,
                private $sce:ng.ISCEService,private $window:ng.IWindowService) {

      //$scope.studentRecordFound=false;

      $scope.semesterFinalSelected=true;
      $scope.civilSelected=false;
      $scope.cciSelected=false;
      $scope.seatPlanArr;
      $scope.init = this.init.bind(this);
      $scope.getStudentInfo = this.getStudentInfo.bind(this);
      $scope.getStudentRecordInfo = this.getStudentRecordInfo.bind(this);
      $scope.getSeatPlanInfoFinalExam = this.getSeatPlanInfoFinalExam.bind(this);
      $scope.getClassRoomInfo = this.getClassRoomInfo.bind(this);
      $scope.showSemesterFinalSeatPlan = this.showSemesterFinalSeatPlan.bind(this);
      $scope.getApplicationCCIInfo = this.getApplicationCCIInfo.bind(this);
      $scope.showCCISeatPlan = this.showCCISeatPlan.bind(this);

    }

    private init():void{
      /*First retrieve the student information*/
      this.getStudentInfo().then((studentInfo:Student)=>{
        this.getStudentRecordInfo().then((studentRecords:any)=>{
          if(studentRecords.length>0){
            this.$scope.studentRecordFound=true;
            this.showSemesterFinalSeatPlan();
          }else{
            this.$scope.studentRecordFound=false;
            this.$scope.recordNotFoundMessage="Not Yet Registered for the semester!";
          }
        });
      });
    }


    private showSemesterFinalSeatPlan():void{
      this.$scope.semesterFinalSelected=true;
      this.$scope.cciSelected=false;
      this.$scope.civilSelected=false;
      this.getSeatPlanInfoFinalExam().then((seatPlanArr:Array<ISeatPlan>)=>{
        this.$scope.seatPlanArr = seatPlanArr[0];
        console.log(this.$scope.seatPlanArr);
        this.$scope.roomId = this.$scope.seatPlanArr.roomId;

        this.getClassRoomInfo(this.$scope.roomId).then((roomArr:Array<IClassRoom>)=>{
          this.$scope.classRoom = roomArr[0];
          this.$scope.colIterationArr=[];
          this.$scope.rowIterationArr=[];
          for(var i=1;i<=this.$scope.classRoom.totalColumn;i++){
            this.$scope.colIterationArr.push(i);
          }
          for(var i=1;i<=this.$scope.classRoom.totalRow;i++){
            this.$scope.rowIterationArr.push(i);
          }
        });

      });
    }

    private showCCISeatPlan():void{
      this.$scope.cciSelected=true;
      this.$scope.civilSelected=false;
      this.$scope.semesterFinalSelected=false;
      this.getApplicationCCIInfo().then((applicationCCI:Array<IApplicationCCI>)=>{
        this.$scope.applicationCCIArr=applicationCCI;
      });
    }


    private getStudentInfo():ng.IPromise<any>{
      var defer = this.$q.defer();
      var student:Student;

      this.httpClient.get('/ums-webservice-common/academic/student/getStudentInfoById','application/json',
          (json:any,etag:string)=>{
            student = json.entries;
            this.$scope.student = student;
            console.log(this.$scope.student);
            defer.resolve(student);
          },
          (response:ng.IHttpPromiseCallbackArg<any>)=>{
            console.error(response);
          });

      return defer.promise;
    }

    private getStudentRecordInfo():ng.IPromise<any>{
      var defer = this.$q.defer();
      var studentRecord:any={};

      this.httpClient.get("/ums-webservice-common/academic/studentrecord/student/"+this.$scope.student[0].id+
              "/semesterId/"+this.$scope.student[0].semesterId+"/year/"+this.$scope.student[0].year+"/semester/"+this.$scope.student[0].academicSemester,
              'application/json',
          (json:any,etag:string)=>{

            studentRecord=json.entries;
            defer.resolve(studentRecord);
          },(response:ng.IHttpPromiseCallbackArg<any>)=>{
            console.error(response);
          });

      return defer.promise;
    }

    private getSeatPlanInfoFinalExam():ng.IPromise<any>{
      var defer = this.$q.defer();
      var seatPlanArr:Array<ISeatPlan>=[];
      this.httpClient.get("/ums-webservice-common/academic/seatplan/studentId/"+this.$scope.student[0].id+"/semesterId/"+this.$scope.student[0].semesterId,'application/json',
          (json:any,etag:string)=>{
            seatPlanArr = json.entries;
            console.log("*** seat plan information****");
            console.log(seatPlanArr);
            defer.resolve(seatPlanArr);
          },(response:ng.IHttpPromiseCallbackArg<any>)=>{
              console.error(response);
          });
      return defer.promise;
    }

    private getClassRoomInfo(roomId:number):ng.IPromise<any>{
      var defer = this.$q.defer();
      var classRoom:Array<IClassRoom>=[];
      this.httpClient.get("/ums-webservice-common/academic/classroom/roomId/"+roomId,'application/json',
          (json:any,etag:string)=>{
            classRoom=json.entries;
            defer.resolve(classRoom);
          },
          (response:ng.IHttpPromiseCallbackArg<any>)=>{
            console.error(response);
          });

      return defer.promise;
    }


    private getApplicationCCIInfo():ng.IPromise<any>{
      var defer = this.$q.defer();
      var applicationCCI:Array<IApplicationCCI>=[];
      this.httpClient.get("/ums-webservice-common/academic/applicationCCI/seatPlanView","application/json",
          (json:any,etag:string)=>{
            applicationCCI=json.entries;
            console.log(applicationCCI);
            defer.resolve(applicationCCI);
          },
          (response:ng.IHttpPromiseCallbackArg<any>)=>{
            console.error(response);
          });

      return defer.promise;
    }



  }

  UMS.controller("ViewSeatPlan",ViewSeatPlan);
}