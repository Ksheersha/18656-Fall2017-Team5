'use strict';

var app = angular.module('SOSECommunity');

app.controller('FollowCtrl', ['$scope','$http', '$rootScope', '$location', 'socketioService','usernameService','recommendService',
  function($scope, $http, $rootScope, $location, socketioService,usernameService,recommendService) {

        var mySocket = socketioService.getSocket();
        $scope.following = '';

        $scope.follow = function(){
            var username = usernameService.getUsername();
            $http({
                method : 'POST',
                url : 'http://localhost:9000/addFollowers/' + username + '/' + $scope.authorname
            }).success(function(data, status, headers, config) {
                $rootScope.$broadcast('refreshDirectory', {});
                $scope.recommend($scope.authorname);
            	$scope.authorname = '';
            }).error(function(data, status, headers, config) {
                console.log(status);
            });
        }

        $scope.search = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/getFollowers/' + $scope.author
            }).success(function(res, status, headers, config) {
                var num = 0;
            	if (res != '') {
            		num = res.split(',').length;
            	}
                $scope.recommend($scope.author);
                $scope.following = $scope.author + ' is following ' + num + ' researchers';
                $scope.author = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.recommend = function(name) {
            $http({
                method : 'GET',
                url : 'http://localhost:9000/getRecommendation/' + name + "/author"
            }).success(function(res, status, headers, config) {
                var prevName = recommendService.getAuthor();
                if (prevName != '' && prevName != name) {
                    $scope.connect(prevName, name);
                }
                recommendService.setAuthor(name);
                if (res.length > 0) {
                    $scope.visit = "People also search " + res[0].name;
                } else {
                    $scope.visit = '';
                }
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        };

        $scope.connect = function(prevName, currName){
            $http({
                method : 'POST',
                url : 'http://localhost:9000/addRelatedRelationship/' + prevName + ',' + currName + '/author'
            }).success(function(res, status, headers, config) {
                //console.log(res);
            }).error(function (res, status, headers, config) {
                console.log(res);
            });
        };
    }
]);
