'use strict';

angular.module('jenkinshue')
  .controller('BridgeDialogCtrl', ['$scope', '$uibModalInstance', 'bridges',
    function($scope, $uibModalInstance, bridges) {

      $scope.bridges = bridges;

      $scope.addBridge = function(ip) {
        $uibModalInstance.close(ip);
      };

      $scope.cancel = function() {
        $uibModalInstance.dismiss('cancel');
      };
      
    }]);
