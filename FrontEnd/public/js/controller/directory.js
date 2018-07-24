'use strict';

/**
 * @ngdoc function
 * @name sampleApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sampleApp
 */
(function(){
  var app = angular.module('SOSECommunity');
  app.controller('DirectoryCtrl',['$scope', 'socketioService','$http','$location','$rootScope', 'alertService','usernameService',
    function ($scope, socketioService, $http, $location, $rootScope, alertService,usernameService) {
    $scope.directory = [];
    $scope.hide = true;
    $scope.mySocket = socketioService.getSocket();

    $scope.$on('refreshDirectory', function(event, args) {
        $scope.directory = [];
        $http({
            method : 'GET',
            url : 'http://localhost:9000/getFollowers/' + usernameService.getUsername()
        }).success(function(res, status, headers, config) {
            if (res != '') {
                var arr = res.split(',');
                for (var i = 0; i < arr.length; i++) {
                    $scope.directory.push({username : arr[i]});
                }
            }
        }
        ).error(function (res, status, headers, config) {
            console.log(res);
        });
    });

  }]);
})();
