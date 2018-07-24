'use strict';

var app = angular.module('SOSECommunity');

app.controller('CategorizeCtrl', ['$scope','$http', '$rootScope', '$location',
  function($scope, $http, $rootScope, $location) {

        $scope.topics = [];
        $scope.papers = "paper list"

        $scope.categorize = function(){
            if ($scope.channel == null || $scope.channel == '' || $scope.keywords == null || $scope.keywords == '') {
                $scope.normalCategorize();
            } else {
                $scope.advancedCategorize();
            }
        }

        $scope.advancedCategorize = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/advancedCategorize/' + $scope.startyear + '/' + $scope.endyear + '/' + $scope.channel + '/' + $scope.keywords.replace(/ /g, "_")
            }).success(function(res, status, headers, config) {
                $scope.topics = [];
                var i = 1;
                for (var key in res) {
                    var data = "";
                    var arr = res[key].split(',');
                    for (var i = 0; i < arr.length; i++) {
                        data += arr[i];
                        if (i < arr.length - 1) {
                            data += '\n';
                        }
                    }
                    var topic = {
                        id : i,
                        name : key,
                        papers : data
                    }
                    i++;
                    $scope.topics.push(topic);
                }
                $scope.startyear = '';
                $scope.endyear = '';
                $scope.channel = '';
                $scope.keywords = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.normalCategorize = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/categorizeByTimestamp/' + $scope.startyear + '/' + $scope.endyear
            }).success(function(res, status, headers, config) {
                $scope.topics = [];
                var i = 1;
                for (var key in res) {
                    var data = "";
                    var arr = res[key].split(',');
                    for (var i = 0; i < arr.length; i++) {
                        data += arr[i];
                        if (i < arr.length - 1) {
                            data += '\n';
                        }
                    }
                    var topic = {
                        id : i,
                        name : key,
                        papers : data
                    }
                    i++;
                    $scope.topics.push(topic);
                }
                $scope.startyear = '';
                $scope.endyear = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

    }
]);
