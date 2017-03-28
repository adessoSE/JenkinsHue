'use strict';

angular.module('jenkinshue')
	.directive('navigation', ['routeNavigation',
		function(routeNavigation) {
			/* Dynamisch generierte Navigation */
			return {
				restrict : "E",
				replace : true,
				templateUrl : "views/navigation.html",
				controller : function($scope) {
					$scope.routes = routeNavigation.routes;
					$scope.activeRoute = routeNavigation.activeRoute;
				}
			};
		}]);
