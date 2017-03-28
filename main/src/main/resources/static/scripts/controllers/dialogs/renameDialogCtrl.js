'use strict';

angular.module('jenkinshue')
  .controller('RenameDialogCtrl', ['$scope', '$uibModalInstance', 'dialogTitle', 'icon', 'currentName',
    function($scope, $uibModalInstance, dialogTitle, icon, currentName) {

      $scope.dialogTitle = dialogTitle;
      $scope.icon = icon;
      $scope.name = currentName;

      $scope.save = function () {
        $uibModalInstance.close($scope.name);
      };

      $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };
    }]);
