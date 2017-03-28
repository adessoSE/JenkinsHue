'use strict';

angular.module('jenkinshue').directive('scenarioConfig', function() {
  return {
    restrict: 'E',
    replace : true,
    scope: false,
		templateUrl : '/views/scenarioConfigTemplate.html'
  };
});
