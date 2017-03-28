'use strict';

angular.module('jenkinshue')
  .controller('DashboardCtrl', ['$rootScope', '$scope', '$http', 'settings', 'growl',
    function($rootScope, $scope, $http, settings, growl) {

      $http.get(settings.restUniversal + '/dashboard', {params: {teamId: $rootScope.teamId}})
        .success(function(data) {
          $scope.dashboard = data;
        }).error(function(error) {
          growl.error(error.message);
        });

    }]);
