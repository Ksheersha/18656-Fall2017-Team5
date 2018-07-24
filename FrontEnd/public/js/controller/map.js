'use strict';

var app = angular.module('SOSECommunity');

app.controller('MapCtrl', ['$scope','$http', '$rootScope', '$location',
  function($scope, $http, $rootScope, $location, googlechart) {

			  var chart1 = {};
			  chart1.type = "GeoChart";
			  chart1.data = [
			    ['Locale', 'Count'],
			  ];

			  chart1.options = {
			    width: 1000,
			    height: 500,
			    chartArea: {left:10,top:10,bottom:0,height:"100%"},
			    colorAxis: {colors: ['#aec7e8', '#1f77b4']},
			    displayMode: 'regions'
			  };

			  chart1.formatters = {
			    number : [{
			      columnNum: 1,
			      pattern: " #,##0"
			    }]
			  };

			  $scope.chart = chart1;

        $scope.generate1 = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/countryContributorInTopic/' + $scope.country + '/' + $scope.topic
            }).success(function(res, status, headers, config) {
                $scope.experts = res;
							  chart1.data = [
							    ['Locale', 'Count'],
							  ];
							  for (var i = 0; i < res.length; i++) {
							  	chart1.data.push([res[i].country, res[i].count]);
							  }
							  $scope.chart = chart1;
                $scope.country = '';
                $scope.topic = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.generate2 = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/countryContributorInChannel/' + $scope.channelname + '/' + $scope.startyear + '/' + $scope.endyear
            }).success(function(res, status, headers, config) {
                $scope.experts = res;
							  chart1.data = [
							    ['Locale', 'Count'],
							  ];
							  for (var i = 0; i < res.length; i++) {
							  	chart1.data.push([res[i].country, res[i].count]);
							  }
							  $scope.chart = chart1;
                $scope.channelname = '';
                $scope.startyear = '';
                $scope.endyear = '';
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }
    }
]);
