/**
 * Created by Monjur-E-Morshed on 17-Dec-16.
 */

module ums{
  export class AdmissionStudentService{



    public static $inject = ['appConstants','HttpClient','$q','notify','$sce','$window'];
    constructor(private appConstants: any, private httpClient: HttpClient,
                private $q:ng.IQService, private notify: Notify,
                private $sce:ng.ISCEService,private $window:ng.IWindowService) {

    }


    public fetchTaletalkData(semesterId:number):ng.IPromise<any>{
      var url="academic/admission/taletalkData/semester/"+semesterId;
      var defer = this.$q.defer();

      this.httpClient.get(url, this.appConstants.mimeTypeJson,
          (json:any, etag:string)=>{
            var admissionStudents:any = json.entries;
            defer.resolve(admissionStudents);
          },
          (response:ng.IHttpPromiseCallbackArg<any>)=>{
            console.error(response);
          });

      return defer.promise;
    }

    public downloadExcelFile(semesterId:number):any{

      var fileName = "Taletalk_data_"+semesterId;
      var contentType:string = Utils.getFileContentType('xls');
      var url="admission/xlx/taletalkData/semester/"+semesterId;

      this.httpClient.get(url, contentType,
          (data:any, etag:string)=>{
        console.log(data);
        console.log(contentType);
            Utils.writeFileContent(data, contentType, fileName);
            console.log("got the file");
          },
          (response:ng.IHttpPromiseCallbackArg<any>)=>{
            console.error(response);
          },'arraybuffer');
    }
  }

  UMS.service("admissionStudentService", AdmissionStudentService);
}