'use strict';

var app = angular.module('SOSECommunity');

app.controller('WelcomeCtrl', ['$scope','$http', 'usernameService', '$rootScope', '$location', 'socketioService','timeService',
  function($scope, $http, usernameService, $rootScope, $location, socketioService,timeService) {

    var mySocket = socketioService.getSocket();
    $scope.username = usernameService.getUsername();
    var time = timeService.getVisitedTime();

    if (time == 'New') {
        $scope.visited = '';
    } else {
        $scope.visited = 'You have recently visited on ' + time;
    }

    $http({
        method : 'GET',
        url : 'http://localhost:9000/getNotification/' + $scope.username
    }).success(function(res, status, headers, config) {
        for (var i = 0; i < res.length; i++) {
            alert("New publication: " + res[i].title + " by " + res[i].authors)
        }
    }
    ).error(function (res, status, headers, config) {
    });

    }
]);
