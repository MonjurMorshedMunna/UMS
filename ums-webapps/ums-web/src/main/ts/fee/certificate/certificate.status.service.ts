module ums {
  export interface CertificateStatus {
    id: string;
    transactionId: string;
    transactionStatus: string;
    studentId: string;
    studentName: string;
    semesterName: string;
    certificateType: string;
    processedBy: string;
    processedOn: string;
    status: string;
    lastModified: string;
  }

  export interface CertificateStatusResponse {
    entries: CertificateStatus[];
    next: string;
    previous: string;
  }

  export class CertificateStatusService {
    public static $inject = ['$q', 'HttpClient'];

    constructor(private $q: ng.IQService,
                private httpClient: HttpClient) {

    }

    public getCertificateStatus(): ng.IPromise<CertificateStatus[]> {
      let defer: ng.IDeferred<CertificateStatus[]> = this.$q.defer();
      this.httpClient.get('certificate-status', HttpClient.MIME_TYPE_JSON,
          (response: CertificateStatusResponse) => defer.resolve(response.entries));
      return defer.promise;
    }

    public listCertificateStatus(filters: SelectedFilter[], url?: string): ng.IPromise<CertificateStatusResponse> {
      let defer: ng.IDeferred<CertificateStatusResponse> = this.$q.defer();
      this.httpClient.post(url ? url : 'certificate-status/paginated', filters ? {"entries": filters} : {},
          HttpClient.MIME_TYPE_JSON)
          .success((response: CertificateStatusResponse) => {
            defer.resolve(response);
          })
          .error((error) => {
            console.error(error);
            defer.resolve(undefined);
          });
      return defer.promise;
    }

    public getFilters(): ng.IPromise<Filter[]> {
      let defer: ng.IDeferred<Filter[]> = this.$q.defer();
      this.httpClient.get('certificate-status/filters', HttpClient.MIME_TYPE_JSON, (filters: Filter[])=> {
        defer.resolve(filters);
      });
      return defer.promise;
    }

    public processCertificates(certificates: CertificateStatus[]): ng.IPromise<boolean> {
      let defer: ng.IDeferred<boolean> = this.$q.defer();
      this.httpClient.put(`certificate-status`, {
            "entries": certificates
          },
          HttpClient.MIME_TYPE_JSON)
          .success(() => defer.resolve(true))
          .error(() => defer.resolve(false));
      return defer.promise;
    }
  }

  UMS.service('CertificateStatusService', CertificateStatusService);
}
