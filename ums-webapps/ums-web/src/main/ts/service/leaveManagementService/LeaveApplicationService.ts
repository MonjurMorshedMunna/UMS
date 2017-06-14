/**
 * Created by My Pc on 09-May-17.
 */
module ums {
  export class LeaveApplicationService {
    public static $inject = ['appConstants', 'HttpClient', '$q', 'notify', '$sce', '$window'];

    constructor(private appConstants: any, private httpClient: HttpClient,
                private $q: ng.IQService, private notify: Notify,
                private $sce: ng.ISCEService, private $window: ng.IWindowService) {

    }

    public fetchRemainingLeaves(): ng.IPromise<any> {
      var url = "lmsApplication/remainingLeaves";
      var defer = this.$q.defer();

      this.httpClient.get(url, this.appConstants.mimeTypeJson,
          (json: any, etag: string) => {
            var lmsTypes: any = {};
            lmsTypes = json.entries;
            defer.resolve(lmsTypes);
          },
          (response: ng.IHttpPromiseCallbackArg<any>) => {
            console.error(response);
            this.notify.error("Error in getting remaining leaves");
          });

      return defer.promise;
    }

    public fetchRemainingLeavesByEmployeeId(employeeId: string): ng.IPromise<any> {
      var url = "lmsApplication/remainingLeaves/employeeId/" + employeeId;
      var defer = this.$q.defer();

      this.httpClient.get(url, this.appConstants.mimeTypeJson,
          (json: any, etag: string) => {
            var lmsTypes: any = {};
            lmsTypes = json.entries;
            defer.resolve(lmsTypes);
          },
          (response: ng.IHttpPromiseCallbackArg<any>) => {
            console.error(response);
            this.notify.error("Error in getting remaining leaves");
          });

      return defer.promise;
    }

    public fetchPendingLeaves(): ng.IPromise<any> {
      var url = "lmsApplication/pendingLeaves";
      var defer = this.$q.defer();

      this.httpClient.get(url, this.appConstants.mimeTypeJson,
          (json: any, etag: string) => {
            var leaves: any = {};
            leaves = json.entries;
            console.log("pending leaves from service");
            console.log(json.entries);
            defer.resolve(leaves);
          },
          (response: ng.IHttpPromiseCallbackArg<any>) => {
            console.error(response);
            this.notify.error("Error in getting pending  leaves");
          });

      return defer.promise;
    }

    public fetchApprovedLeavesWithDateRange(startDate: string, endDate: string): ng.IPromise<any> {
      var url = "lmsApplication/approvedApplications";
      var defer = this.$q.defer();

      this.httpClient.get(url + "/startDate/" + startDate + "/endDate/" + endDate, this.appConstants.mimeTypeJson,
          (json: any, etag: string) => {
            var leaves: any = {};
            leaves = json.entries;
            defer.resolve(leaves);
          },
          (response: ng.IHttpPromiseCallbackArg<any>) => {
            console.error(response);
            this.notify.error("Error in getting leave applications");
          });

      return defer.promise;
    }

    public saveLeaveApplication(json: any): ng.IPromise<any> {
      console.log("json");
      console.log(json);
      var defer = this.$q.defer();
      var url = "lmsApplication/save";
      console.log("Found json");
      console.log(json);
      this.httpClient.post(url, json, 'application/json')
          .success(() => {
            defer.resolve("success");
            this.notify.success("Success");
          }).error((data) => {
        defer.resolve("error");
      });

      return defer.promise;
    }


  }

  UMS.service("leaveApplicationService", LeaveApplicationService);
}