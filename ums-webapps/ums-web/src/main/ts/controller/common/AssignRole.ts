module ums {
  interface NavigationItem {
    id: number;
    title: String;
    parentMenu: number;
    viewOrder: number;
    location: string
    iconContent: string;
    status: boolean;
    children? : Array<NavigationItem>;
  }
  export class AssignRole {
    public static $inject = ['$scope', 'HttpClient'];

    constructor(private $scope: any,
                private httpClient: HttpClient) {

      httpClient.get('users', HttpClient.MIME_TYPE_JSON,
          (data: any, etag) => {
            $scope.users = data.entries;
          });
      var permissions: {number?: boolean} = {};

      this.httpClient.get("mainNavigation", HttpClient.MIME_TYPE_JSON,
          (data: any, etag: string) => {
            var navigationItems: Array<NavigationItem> = data.entries[0].items;

            for (var i = 0; i < navigationItems.length; i++) {
              permissions[navigationItems[i].id] = false;
              if (navigationItems[i].children) {
                for (var j = 0; j < navigationItems[i].children.length; j++) {
                  permissions[navigationItems[i].children[j].id] = false;
                }
              }
            }
          });

      $scope.permissions = permissions;
      $scope.saveAssignment = this.saveAssignment.bind(this);

      $scope.savedAdditionalRolePermission = () => {
        $scope.isPermissionLoading = true;
        httpClient.get('additionalRolePermissions/' + $scope.userId, HttpClient.MIME_TYPE_JSON,
            (data: any, etag: string) => {
              this.reset($scope.permissions);
              for (var i = 0; i < data.entries.length; i++) {
                $scope.startDate = data.entries[i].start;
                $scope.endDate = data.entries[i].end;
                for (var j = 0; j < data.entries[i].permissions.entries.length; j++) {
                  $scope.permissions[data.entries[i].permissions.entries[j]] = true;
                }
              }
              $scope.isPermissionLoading = false;
            });
      };
    }

    private saveAssignment(): void {
      var savedPermissions: Array<number> = [];
      for (var id in this.$scope.permissions) {
        if (this.$scope.permissions.hasOwnProperty(id) && this.$scope.permissions[id]) {
          savedPermissions[savedPermissions.length] = parseInt(id);
        }
      }

      var save = {};
      save['user'] = this.$scope.userId;
      save['permissions'] = savedPermissions;
      save['start'] = this.$scope.startDate;
      save['end'] = this.$scope.endDate;

      this.httpClient.post('additionalRolePermissions/', save, 'application/json')
          .success((data) => {
            console.debug(data);
          }).error((data) => {
          });
    }

    private reset(permissions: {number?: boolean}): void {
      for (var id in permissions) {
        if (permissions.hasOwnProperty(id) && permissions[id]) {
          permissions[id] = false;
        }
      }
    }

  }

  UMS.controller("AssignRole", AssignRole);
}
