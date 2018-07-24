'use strict';

var app = angular.module('SOSECommunity');

app.controller('JournalCtrl', ['$scope','$http', '$rootScope', '$location', 'socketioService',
  function($scope, $http, $rootScope, $location, socketioService) {

        var mySocket = socketioService.getSocket();

        $scope.generate = function(){
                       $http({
                          method : 'GET',
                          url : 'http://localhost:9000/getJournalContributor/' + $scope.journalname
                      }).success(function(res, status, headers, config) {
                          console.log(res);
                          console.log(res);
                               //{"volume":["1","2","3"],"count":[43,89,78]}
                          var resV = res["volume"];
                          var resC = res["count"];
                          console.log(resV + "\n" + resC);
                          var finalR = new Array();
                          for (var i = 0; i < resV.length; i++){
                              finalR[i] = new Array(parseInt(resV[i]),parseInt(resC[i]));
                          }
                               $scope.data = [
                                   {
                                       "key" : "Quantity" ,
                                       "bar": true,
                                       "values" : finalR
                                   }];

                               $scope.options = {
                                   chart: {
                                       type: 'historicalBarChart',
                                       height: 450,
                                       margin : {
                                           top: 20,
                                           right: 20,
                                           bottom: 65,
                                           left: 50
                                       },
                                       x: function(d){return d[0];},
                                       y: function(d){return d[1];},
                                       showValues: true,
                                       valueFormat: function(d){
                                           return d;
                                       },
                                       duration: 100,
                                       xAxis: {
                                           axisLabel: 'Volume',
                                           tickFormat: function(d) {
                                               return d;
                                           },
                                           rotateLabels: 30,
                                           showMaxMin: true
                                       },
                                       yAxis: {
                                           axisLabel: 'Contributors',
                                           axisLabelDistance: -10,
                                           tickFormat: function(d){
                                               return d;
                                           },
                                       },
                                       tooltip: {
                                           keyFormatter: function(d) {
                                               return d;
                                           }
                                       },
                                       zoom: {
                                           enabled: true,
                                           scaleExtent: [1, 10],
                                           useFixedDomain: false,
                                           useNiceScale: false,
                                           horizontalOff: false,
                                           verticalOff: true,
                                           unzoomEventType: 'dblclick.zoom'
                                       }
                                   }
                               };
                      }
                      ).error(function (res, status, headers, config) {
                          console.log(res);
                      });
        }
    }
]);
