'use strict';

angular.module('jenkinshue')
  .controller('AppCtrl', ['$rootScope', '$scope', '$http', '$location', '$window', 'settings', 'growl',
  function($rootScope, $scope, $http, $location, $window, settings, growl) {

    $scope.failedAttempts = 0;

    $scope.login = function() {
      $http.post('/login', $.param($scope.credentials), {
        headers : {
          "content-type" : "application/x-www-form-urlencoded"
        }
      }).success(function(data) {
        // update principal
        $http.get(settings.restUniversal + '/principal', {}).success(function(data) {
          $rootScope.principal = data.principal.principal;
          $rootScope.userId = data.userId;
          $rootScope.teamId = data.teamId;
        });
      }).error(function(e) {
        if(e.message.indexOf('Access Denied') > -1) {
          // $scope.loginError = true;
          $scope.failedAttempts++;

          growl.error('Benutzername/Passwort falsch! (Fehlversuche: ' + $scope.failedAttempts + ')', {'globalPosition': 'bottom-center'});

        } else {
          growl.error('Bitte Seite erneut laden!');
        }
      });
    };

    $scope.logout = function() {
      $http.post('/logout', {}).success(function() {
        doLogout();
      }).error(function(data) {
        doLogout();
      });
    };

    function doLogout() {
      $window.location.reload(); // um die direkten "Relogin" zu ermoeglichen (XSRF-Token)
    }

    $rootScope.isAdmin = function() {
      for(var i in $rootScope.principal.authorities) {
        var authority = $rootScope.principal.authorities[i].authority;
        if(authority == 'ROLE_ADMIN') {
          return true;
        }
      }
      return false;
    };

    $rootScope.adminRole = function(roles) {
      for(var i in roles) {
        var role = roles[i].name;
        if(role == 'ROLE_ADMIN') {
          return true;
        }
      }
      return false;
    };

  }]);
