module ums{

  interface ITeachersRoutine extends ng.IScope{

    routines:Array<IRoutine>;
    getTeachersRoutine:Function;
    pdfFile:any;
  }

  class TeachersRoutine{

    public static $inject = ['appConstants','HttpClient','$scope','$q','notify','$sce','$window','classRoutineService'];
    constructor(private appConstants: any, private httpClient: HttpClient, private $scope: ITeachersRoutine,
                private $q:ng.IQService, private notify: Notify,
                private $sce:ng.ISCEService,private $window:ng.IWindowService,private classRoutineService:ClassRoutineService) {

      this.$scope.routines=[];
      $scope.getTeachersRoutine = this.getTeachersRoutine.bind(this);

    }


    private getTeachersRoutine(){
      this.classRoutineService.getRoutineForTeacher().then((routines:Array<IRoutine>)=>{
        this.$scope.routines = routines;
      });
    }

    private showRoutineReport(){
      this.$scope.pdfFile;
      this.classRoutineService.getClassRoutineReportForTeacher().then((file:any)=>{
        if(file!="failure"){
          this.$scope.pdfFile=file;
        }else{
          this.notify.error("Error in generating routine report");
        }
      });
    }

  }

  UMS.controller("TeachersRoutine",TeachersRoutine);

}