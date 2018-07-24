'use strict';

var app = angular.module('SOSECommunity');

app.controller('AuthorInfoCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','recommendService',
  function($scope, $http, md5, usernameService, $rootScope, $location,socketioService,recommendService) {

    var mySocket = socketioService.getSocket();

        $scope.search = function(){

            $http({
                method : 'GET',
                url : 'http://localhost:9000/getAuthorInfo/' + $scope.authorname.replace(' ', '_')
            }).success(function(res, status, headers, config) {
                $scope.name = res.name;
                $scope.homepage = res.homepage;
                $scope.picture = res.picture;
                $scope.interests = res.interests;
                $scope.email = res.email;
                $scope.affiliation = res.affiliation;

                $scope.recommend($scope.authorname);

                $scope.authorname = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });

        };

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

        $scope.searchCollaboration = function(){

            $http({
                method : 'GET',
                url : 'http://localhost:9000/getCoauthorInfo/' + $scope.authorname1 + ';' + $scope.authorname2
            }).success(function(res, status, headers, config) {
                $scope.collaboration = res;
                $scope.authorname1 = '';
                $scope.authorname2 = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        };

        $scope.findPath = function() {
            $http({
                method : 'GET',
                url : 'http://localhost:9000/researcherPath/' + $scope.author1 + '/' + $scope.author2
            }).success(function(res, status, headers, config) {
                $scope.path = res;
                $scope.author1 = '';
                $scope.author2 = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }
    }
]);
