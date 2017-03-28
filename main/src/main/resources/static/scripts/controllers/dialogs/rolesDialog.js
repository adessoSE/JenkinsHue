'use strict';

angular.module('jenkinshue')
  .controller('RolesDialogCtrl', ['$scope', '$uibModalInstance', 'roles', 'oldRoles',
    function($scope, $uibModalInstance, roles, oldRoles) {

      for(var i in roles) {
        for(var e in oldRoles) {
          if(oldRoles[e].name == roles[i].name) {
            roles[i].selection = true;
          }
        }
      }

      $scope.roles = roles;

      $scope.updateRoles = function () {
        var selectedRoles = [];
        for(var i in $scope.roles) {
          var role = $scope.roles[i];
          if(role.selection) {
            selectedRoles.push(role);
          }
        }
        $uibModalInstance.close(selectedRoles);
      };

      $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
      };

    }]);
