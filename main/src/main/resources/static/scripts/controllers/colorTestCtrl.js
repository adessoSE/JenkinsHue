'use strict';

angular.module('jenkinshue')
  .controller('ColorTestCtrl', ['$rootScope', '$scope', '$http', 'settings', 'growl',
  function($rootScope, $scope, $http, settings, growl) {

    $http.get(settings.restLamps + '/team/' + $rootScope.teamId + '/nameOnly', {})
      .success(function(data) {
        $scope.lamps = data;
      }).error(function(error) {
        growl.error(error.message);
      });

    $scope.test = {
      scenarioConfig: {
        lampOn: true,
        onetimePulsationEnabled: true,
        onetimePulsationColorChangeEnabled: true,
        onetimePulsationColorHex: '#F00',
        colorChangeEnabled: true,
        colorHex: '#0F0',
        brightnessChangeEnabled: true,
        brightness: 254
      }
    };

    $scope.testScenario = function() {
      $http.post(settings.restLamps + '/testScenario', $scope.test, {})
        .success(function() {
          growl.info("Lampe(n) getestet!");
        }).error(function() {
          growl.error("Fehler beim Testen der Lampe(n)!");
        });
    };

    $scope.turnLampsOff = function() {
      $http.post(settings.restLamps + '/turnOff', {lamps: $scope.test.lamps}, {})
        .success(function() {
          growl.info("Lampe(n) ausgeschaltet!");
        }).error(function() {
          growl.error("Fehler beim Ausschalten der Lampe(n)!");
        });
    };

  }]);
