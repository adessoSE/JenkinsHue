'use strict';

angular.module('jenkinshue')
  .controller('BuildAssignmentCtrl', ['$rootScope', '$scope', '$http', 'settings', '$uibModal', 'growl',
    function($rootScope, $scope, $http, settings, $uibModal, growl) {

      var findWithAttr = function(array, attr, value) {
        for(var i = 0; i < array.length; i++) {
          if(array[i][attr] === value) {
            return i;
          }
        }
        return -1;
      };

      var compareScenarioConfigs = function(a, b) {
        var posA = $scope.scenarioPriority.indexOf(a.scenario);
        var posB = $scope.scenarioPriority.indexOf(b.scenario);
        return posA - posB;
      };

      var sortScenarioConfigs = function() {
        for(var i in $scope.lampDTOs) {
          if($scope.lampDTOs[i].buildingConfigs && ($scope.lampDTOs[i].buildingConfigs instanceof Array)) {
            $scope.lampDTOs[i].buildingConfigs.sort(compareScenarioConfigs);
          }
          if($scope.lampDTOs[i].failureConfigs && ($scope.lampDTOs[i].failureConfigs instanceof Array)) {
            $scope.lampDTOs[i].failureConfigs.sort(compareScenarioConfigs);
          }
          if($scope.lampDTOs[i].unstableConfigs && ($scope.lampDTOs[i].unstableConfigs instanceof Array)) {
            $scope.lampDTOs[i].unstableConfigs.sort(compareScenarioConfigs);
          }
          if($scope.lampDTOs[i].successConfigs && ($scope.lampDTOs[i].successConfigs instanceof Array)) {
            $scope.lampDTOs[i].successConfigs.sort(compareScenarioConfigs);
          }
        }
      };

      $http.get(settings.restLamps + '/team/' + $rootScope.teamId, {})
        .success(function(data) {
          $scope.lampDTOs = data.lamps;
          $scope.scenarioPriority = data.scenarioPriority;
          sortScenarioConfigs();
        }).error(function(error) {
          growl.error(error.message);
        });

      $scope.removeJob = function(lampIndex, jobName) {
        for(var i in $scope.lampDTOs[lampIndex].jobs) {
          var job = $scope.lampDTOs[lampIndex].jobs[i];
          if(job.name == jobName) {
            $scope.lampDTOs[lampIndex].jobs.splice(i, 1);
            break;
          }
        }
      };

      $scope.addJob = function(lampIndex) {
        $scope.loadingJobs = true;
        $http.get(settings.restJenkins + '/jobs', {})
          .success(function(data) {
            $scope.loadingJobs = false;

            var currentJobs = $scope.lampDTOs[lampIndex].jobs;
            var jobs = data.jobs;

            // entferne schon ausgewaehlte Jobs
            for(var i in currentJobs) {
              var tmp = currentJobs[i];
              var index = findWithAttr(jobs, 'name', tmp.name);
              if(index > -1) {
                jobs.splice(index, 1);
              }
            }

            var modal = $uibModal.open({
              animation: true,
              templateUrl: '/views/dialogs/jobsDialog.html',
              controller: 'JobsDialogCtrl',
              size: 'lg',
              resolve: {
                jobs: function () {
                  return jobs;
                }
              }
            });

            modal.result.then(function (selectedJobs) {
              if($scope.lampDTOs[lampIndex].jobs instanceof Array) {
                $scope.lampDTOs[lampIndex].jobs.push.apply($scope.lampDTOs[lampIndex].jobs, selectedJobs);
              } else {
                $scope.lampDTOs[lampIndex].jobs = selectedJobs;
              }
            });
          })
          .error(function() {
            $scope.loadingJobs = false;
            growl.error("Fehler beim Laden der verfügbaren Jobs aufgetreten!");
          });
      };

      $scope.removeScenario = function(lampIndex, name) {
        var lists = ['buildingConfigs', 'failureConfigs', 'unstableConfigs', 'successConfigs'];
        outer:
        for(var listIndex in lists) {
          var list = lists[listIndex];
          for(var i in $scope.lampDTOs[lampIndex][list]) {
            var scenarioConfig = $scope.lampDTOs[lampIndex][list][i];
            if(scenarioConfig.scenario.name == name) {
              $scope.lampDTOs[lampIndex][list].splice(i, 1);
              break outer;
            }
          }
        }
      };

      $scope.addScenario = function(lampIndex) {
        $scope.loadingScenarios = true;
        $http.get(settings.restUniversal + '/scenarios', {})
          .success(function(data) {
            $scope.loadingScenarios = false;

            var currentScenarios = [];
            if($scope.lampDTOs[lampIndex].buildingConfigs && ($scope.lampDTOs[lampIndex].buildingConfigs instanceof Array)) {
              currentScenarios = currentScenarios.concat($scope.lampDTOs[lampIndex].buildingConfigs);
            }
            if($scope.lampDTOs[lampIndex].failureConfigs && ($scope.lampDTOs[lampIndex].failureConfigs instanceof Array)) {
              currentScenarios = currentScenarios.concat($scope.lampDTOs[lampIndex].failureConfigs);
            }
            if($scope.lampDTOs[lampIndex].unstableConfigs && ($scope.lampDTOs[lampIndex].unstableConfigs instanceof Array)) {
              currentScenarios = currentScenarios.concat($scope.lampDTOs[lampIndex].unstableConfigs);
            }
            if($scope.lampDTOs[lampIndex].successConfigs && ($scope.lampDTOs[lampIndex].successConfigs instanceof Array)) {
              currentScenarios = currentScenarios.concat($scope.lampDTOs[lampIndex].successConfigs);
            }
            var scenarios = data;

            // entferne schon ausgewaehlte Szenarien
            for(var i in currentScenarios) {
              var name = currentScenarios[i].scenario.name;

              var index = findWithAttr(scenarios, 'name', name);
              if(index > -1) {
                scenarios.splice(index, 1);
              }
            }

            if(scenarios.length) {
              var modal = $uibModal.open({
                animation: true,
                templateUrl: '/views/dialogs/scenariosDialog.html',
                controller: 'ScenariosDialogCtrl',
                size: 'lg',
                resolve: {
                  scenarios: function () {
                    return scenarios;
                  }
                }
              });

              modal.result.then(function (selectedScenarios) {
                if(!($scope.lampDTOs[lampIndex].buildingConfigs instanceof Array)) {
                  $scope.lampDTOs[lampIndex].buildingConfigs = [];
                }
                if(!($scope.lampDTOs[lampIndex].failureConfigs instanceof Array)) {
                  $scope.lampDTOs[lampIndex].failureConfigs = [];
                }
                if(!($scope.lampDTOs[lampIndex].unstableConfigs instanceof Array)) {
                  $scope.lampDTOs[lampIndex].unstableConfigs = [];
                }
                if(!($scope.lampDTOs[lampIndex].successConfigs instanceof Array)) {
                  $scope.lampDTOs[lampIndex].successConfigs = [];
                }

                for(var i in selectedScenarios) {
                  var s = selectedScenarios[i];
                  var newScenario = {config: {}, scenario: s};
                  if(s.name.startsWith('BUILDING')) {
                    $scope.lampDTOs[lampIndex].buildingConfigs.push(newScenario);
                  } else if(s.name.startsWith('FAILURE')) {
                    $scope.lampDTOs[lampIndex].failureConfigs.push(newScenario);
                  } else if(s.name.startsWith('UNSTABLE')) {
                    $scope.lampDTOs[lampIndex].unstableConfigs.push(newScenario);
                  } else if(s.name.startsWith('SUCCESS')) {
                    $scope.lampDTOs[lampIndex].successConfigs.push(newScenario);
                  }
                }
                sortScenarioConfigs();
              });
            } else {
              growl.warning("Keine weiteren Szenarios verfügbar!");
            }
          }).error(function() {
          	$scope.loadingScenarios = false;
            growl.error("Fehler beim Laden der verfügbaren Szenarios aufgetreten!");
          });
      };

      $scope.saveLamp = function(lampIndex) {
        $scope.lampDTOs[lampIndex].teamId = $rootScope.teamId;
        $http.post(settings.restLamps + '/update', $scope.lampDTOs[lampIndex], {})
          .success(function() {
            growl.info("Einstellungen gespeichert!");
          }).error(function(error) {
            growl.error(error.message);
          });
      };

      $scope.resetLamp = function(lampIndex) {
        $http.get(settings.restLamps + '/' + $scope.lampDTOs[lampIndex].id, {})
          .success(function(data) {
            $scope.lampDTOs[lampIndex].name = data.name;
            $scope.lampDTOs[lampIndex].workingStart = data.workingStart;
            $scope.lampDTOs[lampIndex].workingEnd = data.workingEnd;
            $scope.lampDTOs[lampIndex].jobs = data.jobs;

            $scope.lampDTOs[lampIndex].buildingConfigs = data.buildingConfigs;
            $scope.lampDTOs[lampIndex].failureConfigs = data.failureConfigs;
            $scope.lampDTOs[lampIndex].unstableConfigs = data.unstableConfigs;
            $scope.lampDTOs[lampIndex].successConfigs = data.successConfigs;

            sortScenarioConfigs();
            growl.info("Änderungen verworfen!");
          }).error(function(error) {
            growl.error(error.message);
          });
      };

    }]);
