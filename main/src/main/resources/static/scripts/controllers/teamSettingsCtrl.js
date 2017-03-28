'use strict';

angular.module('jenkinshue')
  .controller('TeamSettingsCtrl', ['$rootScope', '$scope', '$http', 'settings', '$uibModal', 'growl',
  function($rootScope, $scope, $http, settings, $uibModal, growl) {

    $scope.dragControlListeners = {
      itemMoved: function(event) {
        console.log(event);
      },
      orderChanged: function(event) {
        console.log(event);
      },
      allowDuplicates: false //optional param allows duplicates to be dropped.
    };

    $scope.team;

    $http.get(settings.restTeams + '/' + $rootScope.teamId, {})
      .success(function(data) {
        $scope.team = data;

        for(var i in $scope.team.scenarioPriority) {
          var name = $scope.team.scenarioPriority[i].name;
          var states = name.split('_AFTER_');
          var color1 = 'white';
          var color2 = 'white';

          var blueColor = '#3c8dbc';
          var redColor = '#dd4b39';
          var yellowColor = '#f39c12';
          var greenColor = '#00a65a';

          switch(states[0]) {
            case 'BUILDING':
              color1 = blueColor;
              break;
            case 'FAILURE':
              color1 = redColor;
              break;
            case 'UNSTABLE':
              color1 = yellowColor;
              break;
            case 'SUCCESS':
              color1 = greenColor;
              break;
          }
          switch(states[1]) {
            case 'BUILDING':
              color2 = blueColor;
              break;
            case 'FAILURE':
              color2 = redColor;
              break;
            case 'UNSTABLE':
              color2 = yellowColor;
              break;
            case 'SUCCESS':
              color2 = greenColor;
              break;
          }

          $scope.team.scenarioPriority[i].background = 'linear-gradient(to right, ' + color1 + ' 48%, ' + color2 + ' 52%)';
        }

      }).error(function(error) {
        growl.error(error.message);
      });

    $scope.renameTeam = function() {
      var modal = $uibModal.open({
        animation: true,
        templateUrl: '/views/dialogs/renameDialog.html',
        controller: 'RenameDialogCtrl',
        resolve: {
          dialogTitle: function() {
            return "Team umbenennen";
          },
          icon: function() {
            return "fa fa-users";
          },
          currentName: function () {
            return $scope.team.name;
          }
        }
      });
      modal.result.then(function(name) {
        $http.post(settings.restTeams + '/rename', {id: $rootScope.teamId, name: name}, {})
          .success(function(data) {
            $scope.team.name = data.name;
            growl.info("Team umbenannt!");
          }).error(function(error) {
            growl.error(error.message);
          });
      });
    };

    $scope.saveScenarioPriority = function() {
      $http.post(settings.restTeams + '/update', {id: $scope.team.id, scenarioPriority: $scope.team.scenarioPriority}, {})
        .success(function() {
          growl.info("Einstellungen gespeichert!");
        }).error(function(error) {
          growl.error(error.message);
        });
    };

  }]);
