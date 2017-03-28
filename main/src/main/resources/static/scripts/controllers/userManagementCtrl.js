'use strict';

angular.module('jenkinshue')
  .controller('UserManagementCtrl', ['$rootScope', '$scope', '$http', 'settings', 'paginationFactory', '$uibModal', 'growl', 'userService',
    function($rootScope, $scope, $http, settings, paginationFactory, $uibModal, growl, userService) {
      paginationFactory.setURL(settings.restUsers);
      $scope.userData = paginationFactory;

      $scope.loginName;

      $scope.createUser = function() {
        // TODO user landet aktuell im gleichen Team
        $http.post(settings.restUsers + '/create', {login: $scope.loginName, teamId: $rootScope.teamId}, {})
          .success(function() {
            $scope.userData.refresh();
            growl.info("Benutzer hinzugefügt!");
          }).error(function(error) {
            growl.error(error.message);
          });
        $scope.loginName = '';
        $scope.loginForm.$setUntouched();
      };

      $scope.removeUser = function(id) {
        $http.delete(settings.restUsers + '/remove/' + id, {})
          .success(function(data) {
            $scope.userData.refresh();
          }).error(function() {
            growl.error("Fehler beim Entfernen des Benutzers!");
          });
      };

      $scope.removeUserPossible = function(u) {
        return ((!$rootScope.adminRole(u.roles) && u.team.id == $rootScope.teamId) || $rootScope.isAdmin()) && (u.id != $rootScope.userId);
      };

      $scope.loadingRoles = function() {
        return userService.loadingRoles;
      };

      $scope.changeRoles = function(id, oldRoles) {
        userService.changeRoles(id, oldRoles, function() {
          $scope.userData.refresh();
        });
      };

    }]);
