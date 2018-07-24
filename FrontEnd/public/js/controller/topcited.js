'use strict';

var app = angular.module('SOSECommunity');

app.controller('TopCitedCtrl', ['$scope','$http', 'usernameService', '$rootScope', '$location',
  function($scope, $http, usernameService, $rootScope, $location) {


        $scope.search = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/getTopCitedPaperByJournal/' + $scope.channelname
            }).success(function(res, status, headers, config) {
                var data = "";
                for (var i = 0; i < res.length; i++) {
                    data += res[i].title;
                    if (i < res.length - 1) {
                        data += '\n';
                    }
                }
                $scope.channelname = '';
                $scope.papers = data;
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        };

        $scope.search2 = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/getTopCitedPaperByJournalAndYear/' + $scope.channelname2 + '/' + $scope.year
            }).success(function(res, status, headers, config) {
                var data = "";
                for (var i = 0; i < res.length; i++) {
                    data += res[i].title;
                    if (i < res.length - 1) {
                        data += '\n';
                    }
                }
                $scope.channelname2 = '';
                $scope.year = '';
                $scope.papers2 = data;
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        };

        $scope.search3 = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/getTopCitedPaperByJournalAndYear/' + $scope.channelname3 + '/' + $scope.startyear + ',' + $scope.endyear
            }).success(function(res, status, headers, config) {
                var data = "";
                for (var i = 0; i < res.length; i++) {
                    data += res[i].title;
                    if (i < res.length - 1) {
                        data += '\n';
                    }
                }
                $scope.channelname3 = '';
                $scope.startyear = '';
                $scope.endyear = '';
                $scope.papers3 = data;
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        };
    }
]);
