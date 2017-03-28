'use strict';

angular.module('jenkinshue')
  .controller('LampManagementCtrl', ['$rootScope', '$scope', '$http', 'settings', '$uibModal', 'growl',
    function($rootScope, $scope, $http, settings, $uibModal, growl) {

      var updateAvailableLamps = function(noAlert) {
        $http.get(settings.restLamps + '/available', {})
          .success(function(data) {
            $scope.availableLamps = data;
            if(!noAlert) {
              growl.info("Verfügbare Lampen aktualisiert!");
            }
            $scope.isRefreshing = false;
          }).error(function() {
            growl.error("Fehler beim Aktualisieren der verfügbaren Lampen!");
            $scope.isRefreshing = false;
          });
      };

      var updateTeamLamps = function() {
        $http.get(settings.restLamps + '/team/' + $rootScope.teamId, {})
          .success(function(data) {
            $scope.teamLamps = data.lamps;
          }).error(function(error) {
            growl.error(error.message);
          });
      };

      var updateLamps = function() {
        updateAvailableLamps(true);
        updateTeamLamps();
      };

      updateLamps();

      $scope.isRefreshing = false;
      $scope.isTaking = false;

      $scope.refresh = function() {
        if(!$scope.isRefreshing) {
          $scope.isRefreshing = true;
          updateAvailableLamps(); // TODO nur alle 5 s aufrufbar machen
        }
      };

      $scope.pulseOnce = function(uniqueId) {
        $http.get(settings.restLamps + '/pulseOnce/' + uniqueId, {});
      };

      $scope.takeLamp = function(uniqueId, name) {
        if(!$scope.isTaking) {
          $scope.isTaking = true;
          if(!name) {
            name = 'Lampe ' + Math.random();
          }
          $http.post(settings.restLamps + '/create', {hueUniqueId: uniqueId, name: name, teamId: $rootScope.teamId}, {})
            .success(function() {
              updateLamps();
              growl.info("Lampe übernommen!");
              $scope.isTaking = false;
            }).error(function(error) {
              growl.error(error.message);
              $scope.isTaking = false;
            });
        }
      };

      $scope.releaseLamp = function(id) {
        $http.delete(settings.restLamps + '/remove/' + id, {})
          .success(function(data) {
            updateLamps();
            growl.info("Lampe freigegeben!");
          }).error(function() {
            growl.error("Fehler beim Freigeben der Lampe!");
          });
      };

      $scope.renameLamp = function(id, currentName) {
        var modal = $uibModal.open({
          animation: true,
          templateUrl: '/views/dialogs/renameDialog.html',
          controller: 'RenameDialogCtrl',
          resolve: {
            dialogTitle: function() {
              return "Lampe umbenennen";
            },
            icon: function() {
              return "fa fa-lightbulb-o";
            },
            currentName: function () {
              return currentName;
            }
          }
        });
        modal.result.then(function(name) {
          if(name) {
            $http.post(settings.restLamps + '/rename', {id: id, name: name}, {})
              .success(function(data) {
                for(var index in $scope.teamLamps) {
                  if($scope.teamLamps[index].id === id) {
                    $scope.teamLamps[index].name = name;
                    break;
                  }
                }
                growl.info("Lampe umbenannt!");
              }).error(function(error) {
                growl.error(error.message);
              });
          } else {
            growl.warning("Eingabe ungültig!");
          }
        });
      };

    }]);
