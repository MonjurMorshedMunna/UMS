module ums {
  interface CustomDocument extends Document {
    mozFullScreenElement: any;
    fullscreenElement: any;
    webkitFullscreenElement: any;
    msFullscreenElement: any;
    requestFullscreen: any;
    mozRequestFullScreen: any;
    documentElement:any;
    exitFullscreen: any;
    mozCancelFullScreen: any;
    webkitExitFullscreen: any;
    msExitFullscreen: any;
    loading_visibility:boolean;
  }
  declare var document:CustomDocument;

  interface CustomElement extends Element {
    ALLOW_KEYBOARD_INPUT :any;
  }

  declare var CustomElement:CustomElement;

  export class LeftMenu implements ng.IDirective {

    constructor(private httpClient: HttpClient) {

    }

    public restrict = 'AE';

    public link = ($scope:any, element:JQuery, attributes:any) => {
      $scope.loading_visibility=true;
      $scope._menu = {status: [], collapse: {}, hover: []};

      $scope._menu.mouseleave = function () {
        for (var j = 0; j < $scope._menu.hover.length; j++) {
          $scope._menu.hover[j] = '';
        }
      };
      $scope._menu.mouseover = function (i) {
        for (var j = 0; j < $scope._menu.hover.length; j++) {
          $scope._menu.hover[j] = '';
        }
        $scope._menu.hover[i] = 'nav-hover';
      };
      $scope._menu.collapse = function (i) {
        $scope._menu.status[i] = !$scope._menu.status[i];

        var current = $(element).find('a[index=' + i + ']');

        current.parent('li').addClass('active').siblings().removeClass('active').children('ul').each(function () {
          $scope._menu.status[$(this).attr('index')] = true;
        });

        if (current.hasClass('btn-fullscreen')) {
          if (!document.fullscreenElement && !document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement) {
            if (document.documentElement.requestFullscreen) {
              document.documentElement.requestFullscreen();
            } else if (document.documentElement.msRequestFullscreen) {
              document.documentElement.msRequestFullscreen();
            } else if (document.documentElement.mozRequestFullScreen) {
              document.documentElement.mozRequestFullScreen();
            } else if (document.documentElement.webkitRequestFullscreen) {
              document.documentElement.webkitRequestFullscreen(CustomElement.ALLOW_KEYBOARD_INPUT);
            }
          } else {
            if (document.exitFullscreen) {
              document.exitFullscreen();
            } else if (document.msExitFullscreen) {
              document.msExitFullscreen();
            } else if (document.mozCancelFullScreen) {
              document.mozCancelFullScreen();
            } else if (document.webkitExitFullscreen) {
              document.webkitExitFullscreen();
            }
          }
        }
      };

      $scope.isMenuLoading = true;
      this.httpClient.get("mainNavigation", HttpClient.MIME_TYPE_JSON,
          (data: any, etag: string) => {
            $scope.loading_visibility=false;
            $scope.menuEntries = data.entries;
            $scope.isMenuLoading = false;
          });
    };

    public templateUrl = "./views/common/navigation.html";

  }
  UMS.directive("ngMenu", ['HttpClient', (httpClient) => new LeftMenu(httpClient)]);
}