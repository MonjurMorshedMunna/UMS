module ums {
  export interface NotificationScope extends ng.IScope {
    notifications: Array<INotification>;
    numOfUnreadNotification: number;
    setReadStatus: Function;
  }
  export interface INotification {
    payload: string;
    producedOn: string;
    consumedOn: string;
    isRead: boolean;
    id : string;
  }
  export interface NotificationEntries {
    entries: Array<INotification>;
  }
  export class Notification implements ng.IDirective {
    public scope: NotificationScope;
    private intervalPromise: ng.IPromise<any>;

    constructor(private httpClient: HttpClient,
                private $q: ng.IQService,
                private $interval: ng.IIntervalService,
                private settings: Settings) {

    }

    public restrict: string = 'AE';

    public link = ($scope: NotificationScope, element: JQuery, attributes: any) => {
      this.scope = $scope;
      this.scope.numOfUnreadNotification = 0;

      this.settings.getSettings().then((appSettings: {[key: string]: any}) => {
        if (appSettings['notification.enabled']) {
          this.getNotification();
          this.intervalPromise = this.$interval(()=> {
            this.getNotification();
          }, appSettings['polling.interval']);
        }
      });

      this.scope.setReadStatus = this.setReadStatus.bind(this);
    };

    public templateUrl = "./views/directive/notification.html";

    private getNotification() {
      this.httpClient.poll("notification/10/", HttpClient.MIME_TYPE_JSON,
          (response: NotificationEntries)=> {
            var tempCount: number = 0;
            for (var i = 0; i < response.entries.length; i++) {
              var notification: INotification = response.entries[i];
              var producedOn: any = notification.producedOn ? moment(notification.producedOn, 'DD-MM-YYYY hh:mm:ss') : null;
              var consumedOn: any = notification.consumedOn ? moment(notification.consumedOn, 'DD-MM-YYYY hh:mm:ss') : null;
              if (producedOn = null || consumedOn == null || !consumedOn.isAfter(producedOn)) {
                notification.isRead = false;
                tempCount = tempCount + 1;
              } else {
                notification.isRead = true;
              }
            }
            if(tempCount != this.scope.numOfUnreadNotification) {
              this.scope.numOfUnreadNotification = tempCount;
            }
            console.log(response.entries);
            this.scope.notifications = response.entries;
          },
          (response: ng.IHttpPromiseCallbackArg<any>) => {
            if (response.status === 401) {
              this.$interval.cancel(this.intervalPromise);
            }
          });
    }

    private setReadStatus(): void {
      //ToDo: Fix Me.
      //Caused a problem inside NotificationResourceHelper [Long.parseLong(notificationObject.getString("id"))]
      //During the conversion.  This is the Quick Fix for that Exception.
      for(var i=0;i<this.scope.notifications.length;i++){
        this.scope.notifications[i].id=this.scope.notifications[i].id+'';
      }

      this.httpClient.post('notification/read', this.scope.notifications, 'application/json')
          .success((data) => {
            delete this.scope.numOfUnreadNotification;
          }).error((data) => {
      });
    }
  }
  UMS.directive("notification",
      ['HttpClient',
        '$q',
        '$interval',
        'Settings',
        (httpClient, $q, $interval, settings: Settings)=> {
          return new Notification(httpClient, $q, $interval, settings);
        }]);
}
