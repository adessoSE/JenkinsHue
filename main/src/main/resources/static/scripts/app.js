'use strict';

angular.module('jenkinshue', ['ngAnimate', 'ngResource', 'ngRoute',
	'ui.bootstrap', 'colorpicker.module', 'ui.select',
	'ui.bootstrap.timepicker', 'angular-growl', 'as.sortable']);

angular.module('jenkinshue')
	.constant('routeNames', {
		dashboard: 'Dashboard',
		buildAssignment: 'Buildzuweisung',
		lampManagement: 'Lampenverwaltung',
		bridgeManagement: 'Bridgeverwaltung',
		userManagement: 'Benutzerverwaltung',
		teamManagement: 'Teamverwaltung',
		teamSettings: 'Teameinstellungen',
		colorTest: 'Farbentest'
	});

angular.module('jenkinshue')
	.factory('routeIcons', ['routeNames',
		function(routeNames) {
			return {
				getIcon : function(routeName) {
					switch(routeName) {
						case routeNames.dashboard:
							return 'fa fa-dashboard';
						case routeNames.buildAssignment:
							return 'fa fa-rocket';
						case routeNames.lampManagement:
							return 'fa fa-lightbulb-o';
						case routeNames.bridgeManagement:
							return 'fa fa-plug';
						case routeNames.userManagement:
							return 'fa fa-user';
						case routeNames.teamManagement:
							return 'fa fa-users';
						case routeNames.teamSettings:
							return 'fa fa-gears';
						case routeNames.colorTest:
							return 'fa fa-paint-brush';
						default:
							return 'fa fa-link';
					}
				}
			}
		}
	]);

angular.module('jenkinshue')
  .run(['$rootScope', '$route', '$http', 'settings', function($rootScope, $route, $http, settings) {
		$http.get(settings.restUniversal + '/principal', {}).success(function(data) {
			$rootScope.principal = data.principal.principal;
			$rootScope.userId = data.userId;
			$rootScope.teamId = data.teamId;
			// TODO Fehlerquelle entfernen
		});

    $rootScope.$on('$routeChangeSuccess', function(e, current, pre) {
			if($route.current.$$route) {
				$rootScope.currentPage = $route.current.$$route.name;
			}
    });
  }]);

angular.module('jenkinshue')
	.config(['$routeProvider', 'routeNames', '$httpProvider', 'growlProvider', function($routeProvider, routeNames, $httpProvider, growlProvider) {

		/*
		 * Definieren von Views und Navigations-Punkten (Routing)
		 * Wenn 'name' nicht vorhanden, wird die View in der
		 * Navigation nicht angezeigt
		 */
		$routeProvider.when('/', {
			templateUrl: 'views/dashboard.html',
			controller: 'DashboardCtrl',
			controllerAs: 'DashboardCtrl',
			name: routeNames.dashboard
		}).when('/buildAssignment', {
			templateUrl: 'views/buildAssignment.html',
			controller: 'BuildAssignmentCtrl',
			controllerAs: 'BuildAssignmentCtrl',
			name: routeNames.buildAssignment
		}).when('/lampManagement', {
			templateUrl: 'views/lampManagement.html',
			controller: 'LampManagementCtrl',
			controllerAs: 'LampManagementCtrl',
			name: routeNames.lampManagement
		}).when('/bridgeManagement', {
			templateUrl: 'views/bridgeManagement.html',
			controller: 'BridgeManagementCtrl',
			controllerAs: 'BridgeManagementCtrl',
			name: routeNames.bridgeManagement
		}).when('/userManagement', {
			templateUrl: 'views/userManagement.html',
			controller: 'UserManagementCtrl',
			controllerAs: 'UserManagementCtrl',
			name: routeNames.userManagement
		}).when('/teamManagement', {
			templateUrl: 'views/teamManagement.html',
			controller: 'TeamManagementCtrl',
			controllerAs: 'TeamManagementCtrl',
			name: routeNames.teamManagement
		}).when('/teamSettings', {
			templateUrl: 'views/teamSettings.html',
			controller: 'TeamSettingsCtrl',
			controllerAs: 'TeamSettingsCtrl',
			name: routeNames.teamSettings
		}).when('/colorTest', {
			templateUrl: 'views/colorTest.html',
			controller: 'ColorTestCtrl',
			controllerAs: 'ColorTestCtrl',
			name: routeNames.colorTest
		}).otherwise({
			redirectTo: '/'
		});

		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

		growlProvider.globalTimeToLive(5000);
	}]);

angular.module('jenkinshue')
	.constant('settings', {
		restBridges: '/rest/bridges',
		restLamps: '/rest/lamps',
		restUsers: '/rest/users',
		restTeams: '/rest/teams',
		restJenkins: '/rest/jenkins',
		restUniversal: '/rest/universal'
	});
