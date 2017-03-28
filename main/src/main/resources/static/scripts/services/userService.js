'use strict';

angular.module('jenkinshue')
	.service('userService', ['$http', 'settings', '$uibModal', 'growl',
		function($http, settings, $uibModal, growl) {
      var service = this;

      service.loadingRoles = false;

      service.changeRoles = function(id, oldRoles, onSuccess) {
        service.loadingRoles = true;
        $http.get(settings.restUniversal + '/roles', {})
          .success(function(data) {
            service.loadingRoles = false;

            var roles = data;

            var modal = $uibModal.open({
              animation: true,
              templateUrl: '/views/dialogs/rolesDialog.html',
              controller: 'RolesDialogCtrl',
              resolve: {
                roles: function() {
                  return roles;
                },
                oldRoles: function() {
                  return oldRoles;
                }
              }
            });

            modal.result.then(function (selectedRoles) {
              $http.post(settings.restUsers + '/update', {id: id, roles: selectedRoles}, {})
                .success(function() {
                  onSuccess();
                  growl.info("Rollen aktualisiert!");
                }).error(function(error) {
                  growl.error(error.message);
                });
            });
          })
          .error(function() {
            service.loadingRoles = false;
            growl.error("Fehler beim Laden der verfügbaren Rollen aufgetreten!");
          });
      };

    }]);
