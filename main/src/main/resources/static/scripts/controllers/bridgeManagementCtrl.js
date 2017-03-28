'use strict';

angular.module('jenkinshue')
  .controller('BridgeManagementCtrl', ['$rootScope', '$scope', '$http', '$timeout', 'settings', 'paginationFactory', '$uibModal', 'growl',
    function($rootScope, $scope, $http, $timeout, settings, paginationFactory, $uibModal, growl) {
      paginationFactory.setURL(settings.restBridges);
      $scope.bridgeData = paginationFactory;

      $scope.ip;

      var createBridge = function(ip) {
        $http.post(settings.restBridges + '/create', {ip: ip, userId: $rootScope.userId}, {})
          .success(function() {
            // Die Zeit wird vom HueSDK benoetigt,
            // um sich mit der Bride zu verbinden!
            // (-> Hue-User ist vorher nicht gesetzt)
            $timeout(function() {
              $scope.bridgeData.refresh();
            }, 400);
            growl.info("Bridge hinzugefügt!");
          }).error(function(error) {
            growl.error(error.message);
          });
        $scope.ip = '';
        $scope.ipForm.$setUntouched();
      };

      $scope.createBridge = function() {
        createBridge($scope.ip);
      };

      $scope.searchForBridges = function() {
        $scope.loadingBridges = true;
        $http.get(settings.restBridges + '/available', {})
          .success(function(data) {
            $scope.loadingBridges = false;
            
            var modal = $uibModal.open({
              animation: true,
              templateUrl: '/views/dialogs/bridgeDialog.html',
              controller: 'BridgeDialogCtrl',
              size: 'lg',
              resolve: {
                bridges: function () {
                  return data;
                }
              }
            });

            modal.result.then(function(ip) {
              createBridge(ip);
            });
          })
          .error(function() {
            $scope.loadingBridges = false;
            growl.error("Fehler beim Laden der verfügbaren Bridges aufgetreten!");
          });
      };

      $scope.removeBridge = function(id) {
        $http.delete(settings.restBridges + '/remove/' + id, {})
          .success(function(data) {
            $scope.bridgeData.refresh();
          }).error(function() {
            growl.error("Fehler beim Entfernen der Bridge!");
          });
      };
    }]);
