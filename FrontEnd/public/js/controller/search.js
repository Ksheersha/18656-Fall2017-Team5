'use strict';

var app = angular.module('SOSECommunity');

app.controller('SearchCtrl', ['$scope','$http', '$rootScope', '$location', 'socketioService','usernameService',
  function($scope, $http, $rootScope, $location, socketioService,usernameService) {

        var mySocket = socketioService.getSocket();
        $scope.username = usernameService.getUsername();
            console.log("username From Search", $scope.username);
        $http({
                method : 'GET',
                url : 'http://localhost:9000/getAuthorInfo/' + $scope.username
            }).success(function(res, status, headers, config) {
                $scope.interests = res.interests;
                $scope.email = res.email;
                $scope.affiliation = res.affiliation;
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });

        $scope.searchExpert = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/searchExperts/' + $scope.searchkeyword
            }).success(function(res, status, headers, config) {
                $scope.experts = res;
                $scope.searchkeyword = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.searchExpertInterest = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/searchExpertInterests/' + $scope.searchkeyword + '/' + $scope.interests
            }).success(function(res, status, headers, config) {
                $scope.experts = res;
                $scope.searchkeyword = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.showEvolution = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/keywordEvolution/' + $scope.channel + '/' + $scope.startyear + '/' + $scope.endyear
            }).success(function(res, status, headers, config) {
                var data = "";
                for (var i = parseInt($scope.startyear); i <= parseInt($scope.endyear); i++) {
                    var year = i.toString();
                    data += i + ": " + res[year];
                    if (i < parseInt($scope.endyear)) {
                        data += '\n';
                    }
                }
                $scope.evolution = data;
                $scope.channel = '';
                $scope.startyear = '';
                $scope.endyear = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.searchTopic = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/searchTopics/' + $scope.searchchannel + '/' + $scope.searchyear
            }).success(function(res, status, headers, config) {
                $scope.topics = res;
                $scope.searchchannel = '';
                $scope.searchyear = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.formTeam = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/researchTeam/' + $scope.topic
            }).success(function(res, status, headers, config) {
                var data = "";
                for (var i = 0; i < res.length; i++) {
                    data += res[i];
                    if (i < res.length - 1) {
                        data += '\n';
                    }
                }
                $scope.members = data;
                $scope.topic = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.findLink = function() {
            $http({
                method : 'GET',
                url : 'http://localhost:9000/linkKeywords/' + $scope.keyword1 + '/' + $scope.keyword2
            }).success(function(res, status, headers, config) {
                $scope.link = res;
                $scope.keyword1 = '';
                $scope.keyword2 = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }
    }
]);
