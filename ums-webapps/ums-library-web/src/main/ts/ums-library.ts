module ums {
  export var UMS = angular.module('UMS', [
    'ngRoute',
    'ui.bootstrap',
    'ui.router',
    'oc.lazyLoad',
    'LocalStorageModule',
    'ngCookies',
    'ngSanitize',
    'scrollable-table',
    'amChartsDirective',
    'ui.sortable',
    'ngHandsontable',
    "dataGrid",
    "pagination",
     'angular-loading-bar'
  ]);

   UMS.factory('myAppFactory', ["$http",function($http) {
    var herokuDomain = 'https://server-pagination.herokuapp.com';
    return {
      getOrdersData: getOrdersData,
      getStatuses: getStatuses
    };


    function getOrdersData(params, callback) {
      $http.defaults.headers.common={};
      $http.get(herokuDomain + '/orders' + params).success(function (response) {
        callback(response.orders, response.ordersCount);
      });
    }

    function getStatuses() {
      return $http.get(herokuDomain + '/orders/statuses');
    }
  }]);

  UMS.config(['BaseUriProvider', (baseUriProvider: BaseUriProvider) => {
    baseUriProvider.setServicePath('/ums-webservice-library/');
  }]);

  UMS.config(($stateProvider, $urlRouterProvider, $locationProvider) => {
    $urlRouterProvider.when('/cataloging','/cataloging/search');
    $urlRouterProvider.otherwise("/userHome");
    $stateProvider
        .state('userHome', {
          url: "/userHome",
          templateUrl: 'views/user-home.html',
          controller: 'MainController',
          resolve: {
            loadMyCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
              return $ocLazyLoad.load({
                files: [
                  'vendors/bootstrap-select/bootstrap-select.min.js',
                  'vendors/bootstrap-select/bootstrap-select.css'
                ]
              });
            }]
          }
        })
        .state('cataloging', {
          url: "/cataloging",
          templateUrl: 'views/admin/cataloging/catalog-home.html'
        })
        .state('cataloging.search', {
          url: "/search",
          templateUrl: 'views/admin/cataloging/catalog-search.html',
          resolve: {
            loadMyCtrl: ['$ocLazyLoad', function ($ocLazyLoad) {
              return $ocLazyLoad.load({
                files: [
                  'vendors/bootstrap-select/bootstrap-select.min.js',
                  'vendors/bootstrap-select/bootstrap-select.css'
                ]
              });
            }]
          }
        })
        .state('cataloging.newBook', {
          url: "/newBook",
          controller: 'BookInfo',
          templateUrl: 'views/admin/cataloging/catalog-new-book.html'
        })
        .state('cataloging.thesis', {
          url: "/thesis",
          templateUrl: 'views/admin/cataloging/catalog-thesis.html'
        })
        .state('cataloging.journal', {
          url: "/journal",
          templateUrl: 'views/admin/cataloging/catalog-journal.html'
        })
        .state('cataloging.map', {
          url: "/map",
          templateUrl: 'views/admin/cataloging/catalog-map.html'
        })
        .state('cataloging.cdDvd', {
          url: "/cdDvd",
          templateUrl: 'views/admin/cataloging/catalog-cdDvd.html'
        })
        .state('cataloging.magazine', {
          url: "/magazine",
          templateUrl: 'views/admin/cataloging/catalog-magazine.html'
        })
        .state('circulation', {
          url: "/circulation",
          templateUrl: 'views/admin/circulation/circulation-home.html'
        })
        .state('patrons', {
          url: "/patrons",
          templateUrl: 'views/admin/patron/patron-home.html',
          controller: 'PatronHome'

        })
        .state('circulation.checkOut', {
          url: "/checkOut",
          templateUrl: 'views/admin/circulation/circulation-checkout.html'
        })
        .state('circulation.checkIn', {
          url: "/checkIn",
          templateUrl: 'views/admin/circulation/circulation-checkin.html'
        })
        .state('circulation.checkOut.patronCheckOut', {
        url: "/patronCheckOut",
        templateUrl: 'views/admin/patron/patron-checkout.html'
       })
        .state('circulation.checkOut.patronDetail', {
          url: "/patronDetail",
          templateUrl: 'views/admin/patron/patron-detail.html'
        })
        .state('logout', {
          url: "/logout",
          controller: 'Logout'
        })
  });
}
