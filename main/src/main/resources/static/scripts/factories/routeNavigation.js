'use strict';

angular.module('jenkinshue')
	.factory('routeNavigation', ['$route', '$location', 'routeNames', 'routeIcons',
		function($route, $location, routeNames, routeIcons) {

			/* Aufbau der dynamischen Navigation */
			var routes = [];
			angular.forEach($route.routes, function(route, path) {
				if (route.name) {
					var icon = routeIcons.getIcon(route.name);
					routes.push({
						path : path,
						name : route.name,
						icon : icon
					});
				}
			});
			return {
				routes : routes,
				/* Hervorhebung des aktiven Menuepunktes */
				activeRoute : function(route) {
					return route.path === $location.path();
				}
			};
		}]);
