'use strict';

var app = angular.module('SOSECommunity');

app.controller('PaperInfoCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','recommendService',
  function($scope, $http, md5, usernameService, $rootScope, $location, socketioService,recommendService) {

    var mySocket = socketioService.getSocket();

        $scope.searchAuthor = function(){

            $http({
                method : 'GET',
                url : 'http://localhost:9000/getAuthorByPaper/' + $scope.title
            }).success(function(res, status, headers, config) {
                $scope.authors = res;
                $scope.recommend($scope.title);
                console.log('success');
            }
            ).error(function (res, status, headers, config) {
                console.log('fail');
            });

        };

        $scope.searchCitation = function(){

            $http({
                method : 'GET',
                url : 'http://localhost:9000/getPaperInfo/' + $scope.title
            }).success(function(res, status, headers, config) {
                $scope.citations = res;
                $scope.recommend($scope.title);
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });

        };

        $scope.recommend = function(title) {
            $http({
                method : 'GET',
                url : 'http://localhost:9000/getRecommendation/' + title + "/paper"
            }).success(function(res, status, headers, config) {
                var prevTitle = recommendService.getPaper();
                if (prevTitle != '' && prevTitle != title) {
                    $scope.connect(prevTitle, title);
                }
                recommendService.setPaper(title);
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

        $scope.connect = function(prevTitle, currTitle){
            $http({
                method : 'POST',
                url : 'http://localhost:9000/addRelatedRelationship/' + prevTitle + ',' + currTitle + '/paper'
            }).success(function(res, status, headers, config) {
                //console.log(res);
            }).error(function (res, status, headers, config) {
                console.log(res);
            });
        };
    }
]);
