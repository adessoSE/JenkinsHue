'use strict';

angular.module('jenkinshue')
  .controller('JobsDialogCtrl', ['$scope', '$uibModalInstance', 'jobs',
    function($scope, $uibModalInstance, jobs) {

      $scope.jobs = jobs;
      $scope.result = {}; // important

      $scope.addJobs = function () {
        $uibModalInstance.close($scope.result.selectedJobs);
      };

      $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };
    }]);
