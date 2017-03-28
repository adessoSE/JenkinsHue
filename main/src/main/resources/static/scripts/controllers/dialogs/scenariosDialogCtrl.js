'use strict';

angular.module('jenkinshue')
  .controller('ScenariosDialogCtrl', ['$scope', '$uibModalInstance', 'scenarios',
    function($scope, $uibModalInstance, scenarios) {

      $scope.scenarios = scenarios;
      $scope.result = {}; // important

      $scope.addScenarios = function () {
        $uibModalInstance.close($scope.result.selectedScenarios);
      };

      $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };

    }]);
