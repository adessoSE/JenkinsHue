'use strict';

angular.module('jenkinshue')
  .controller('TeamManagementCtrl', ['$rootScope', '$scope', '$http', 'settings', 'paginationFactory', '$uibModal', 'growl', 'userService',
    function($rootScope, $scope, $http, settings, paginationFactory, $uibModal, growl, userService) {
      paginationFactory.setURL(settings.restTeams);
      $scope.teamData = paginationFactory;

      $scope.teamName;

      $scope.createTeam = function() {
        $http.post(settings.restTeams + '/create', {name: $scope.teamName}, {})
          .success(function() {
            $scope.teamData.refresh();
            growl.info("Team hinzugefügt!");
          }).error(function(error) {
            growl.error(error.message);
          });
        $scope.teamName = '';
        $scope.teamForm.$setUntouched();
      };

      $scope.removeTeam = function(id) {
        $http.delete(settings.restTeams + '/remove/' + id, {})
          .success(function(data) {
            $scope.teamData.refresh();
          }).error(function() {
            growl.error("Fehler beim Entfernen des Teams!");
          });
      };

      $scope.removeTeamPossible = function(teamId) {
        return teamId != $rootScope.teamId;
      };

      $scope.createUser = function(teamId) {
        var modal = $uibModal.open({
          animation: true,
          templateUrl: '/views/dialogs/renameDialog.html',
          controller: 'RenameDialogCtrl',
          resolve: {
            dialogTitle: function() {
              return "Benutzer hinzufügen";
            },
            icon: function() {
              return "glyphicon glyphicon-user";
            },
            currentName: function () {
              return "";
            }
          }
        });
        modal.result.then(function(name) {
          $http.post(settings.restUsers + '/create', {login: name, teamId: teamId}, {})
            .success(function() {
              $scope.teamData.refresh();
              growl.info("Benutzer hinzugefügt!");
            }).error(function(error) {
              growl.error(error.message);
            });
        });
      };

      $scope.removeUser = function(id) {
        $http.delete(settings.restUsers + '/remove/' + id, {})
          .success(function(data) {
            $scope.teamData.refresh();
          }).error(function() {
            growl.error("Fehler beim Entfernen des Benutzers!");
          });
      };

      $scope.removeUserPossible = function(userId) {
        return userId != $rootScope.userId;;
      };

      $scope.loadingRoles = function() {
        return userService.loadingRoles;
      };

      $scope.changeRoles = function(id, oldRoles) {
        userService.changeRoles(id, oldRoles, function() {
          $scope.teamData.refresh();
        });
      };

    }]);
