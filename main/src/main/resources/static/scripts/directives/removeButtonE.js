'use strict';

angular.module('jenkinshue').directive('removeButton', function() {
  return {
    restrict: 'E',
    replace : true,
		template : '<button class="btn-link" style="padding: 3px 3px; font-size: 15px; line-height: 1.1; border-radius: 3px; box-shadow: none; border: 1px solid transparent; display: inline-block; font-weight: 200; text-align: center; white-space: nowrap; vertical-align: middle; touch-action: manipulation; cursor: pointer; -webkit-user-select: none;"><span class="glyphicon glyphicon-remove"></span></button>'
  };
});
