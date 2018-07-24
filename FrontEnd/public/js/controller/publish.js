'use strict';

var app = angular.module('SOSECommunity');

app.controller('PublishCtrl', ['$scope','$http', '$rootScope', '$location', 'socketioService',
  function($scope, $http, $rootScope, $location, socketioService) {

        var mySocket = socketioService.getSocket();

        $scope.publish = function(){
            if (!$scope.title || !$scope.authornames || !$scope.year || !$scope.journal || !$scope.volume) {
              return;
            }
            
            $http({
                method : 'POST',
                url : 'http://localhost:9000/publishPaper/' + $scope.title + '/' + $scope.year + '/' + $scope.authornames + '/' + $scope.journal + '/' + $scope.volume
            }).success(function(res, status, headers, config) {
                console.log(res);
                $scope.published = $scope.title + ' has been published';
                $scope.title = '';
                $scope.authornames = '';
                $scope.year = '';
                $scope.journal = '';
                $scope.volume = '';
            }).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

    }
]);
