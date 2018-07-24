'use strict';

var app = angular.module('SOSECommunity');

app.controller('YearlyPubCtrl', ['$scope','$http', '$rootScope', '$location', 'socketioService',
  function($scope, $http, $rootScope, $location, socketioService) {

        var mySocket = socketioService.getSocket();
        $scope.generate = function(){
                       $http({
                          method : 'GET',
                          url : 'http://localhost:9000/getAuthorPaperByYear/' + $scope.authorname
                          + "/" + $scope.startyear + "/" + $scope.endyear
                      }).success(function(res, status, headers, config) {
                          var resY = res.toString().split("*")[0].split(",");
                          var resC = res.toString().split("*")[1].split(",");

                          var finalR = new Array();
                          for (var i = 0; i < resY.length; i++){
                              finalR[i] = new Array(parseInt(resY[i]),parseInt(resC[i]));
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
                                           top: 25,
                                           right: 20,
                                           bottom: 65,
                                           left: 55
                                       },
                                       x: function(d){return d[0];},
                                       y: function(d){return d[1];},
                                       showValues: true,
                                       valueFormat: function(d){
                                           return d;
                                       },
                                       duration: 100,
                                       xAxis: {
                                           axisLabel: 'Year',
                                           tickFormat: function(d) {
                                               return d;
                                           },
                                           rotateLabels: 30,
                                           showMaxMin: false
                                       },
                                       yAxis: {
                                           axisLabel: 'No. of Papers published',
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
                          console.log( $scope.data["values"][0]);
                          $scope.result = finalR;
                      }
                      ).error(function (res, status, headers, config) {
                          console.log(res);
                      });
        }
  }
]);
