///<reference path="../../model/master_data/Semester.ts"/>
///<reference path="../../service/HttpClient.ts"/>
///<reference path="../../lib/jquery.notific8.d.ts"/>
module ums {

  interface INewSemesterScope extends ng.IScope {
    submit: Function;
    semester:Semester;
    data:any;
  }

  export class NewSemester {
    public static $inject = ['appConstants', 'HttpClient','$scope'];
    constructor(private appConstants:any,private httpClient:HttpClient,private $scope:INewSemesterScope) {

      $scope.data = {
        programTypeOptions:appConstants.programType,
        semesterTypeOptions:appConstants.semesterType
      };

      setTimeout(function () {
        $('.make-switch').bootstrapSwitch();
        $('#TheCheckBox').bootstrapSwitch();
      }, 50);


      $('.datepicker-default').datepicker();
      $('.datepicker-default').on('change', function(){
        $('.datepicker').hide();
      });
      $scope.submit = this.submit.bind(this);

    }
    private submit():void {
      this.$scope. semester.semesterName=$("#semesterType option:selected").text()+","+this.$scope.semester.year;
      var semesterId=this.$scope.semester.programTypeId+this.$scope.semester.semesterTypeId+this.$scope.semester.year;
      this.$scope. semester.semesterId=+semesterId;
      this.$scope.semester.statusId=1;
      this.httpClient.post('academic/semester/', this.$scope.semester, 'application/json')
          .success(() => {
            $.notific8('Successfully created a new semester.');
          }).error((data) => {
      })
    }

  }
  UMS.controller('NewSemester', NewSemester);
}

